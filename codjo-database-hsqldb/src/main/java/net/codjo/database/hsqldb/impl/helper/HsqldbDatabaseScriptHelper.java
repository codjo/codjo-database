package net.codjo.database.hsqldb.impl.helper;
import java.util.List;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlObject;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlTrigger.TableLink;
import net.codjo.database.common.api.structure.SqlTrigger.Type;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelper;
public class HsqldbDatabaseScriptHelper extends AbstractDatabaseScriptHelper {
    private static enum HsqldbObject {
        TABLE("table"),
        INDEX("index"),
        VIEW("view"),
        CONSTRAINT("constraint") {
            @Override
            public StringBuilder buildDropScriptImpl(StringBuilder sb, HsqldbDatabaseScriptHelper sh, SqlObject sqlObject) {
                SqlConstraint c = (SqlConstraint) sqlObject;
                sb.append("alter table ").append(c.getAlteredTable().getName());
                sb.append(" drop constraint ").append(c.getName());
                return sb;
            }
        },
        TRIGGER("trigger");

        private final String type;


        private HsqldbObject(String type) {
            this.type = type;
        }

        public final String buildDropScript(HsqldbDatabaseScriptHelper sh, SqlObject sqlObject) {
            return buildDropScript(null, sh, sqlObject).toString();
        }

        public final StringBuilder buildDropScript(StringBuilder sb, HsqldbDatabaseScriptHelper sh, SqlObject sqlObject) {
            return buildDropScriptImpl((sb == null) ? new StringBuilder() : sb, sh, sqlObject).append(";\n");
        }

        protected StringBuilder buildDropScriptImpl(StringBuilder sb, HsqldbDatabaseScriptHelper sh, SqlObject sqlObject) {
            sb = (sb == null) ? new StringBuilder() : sb;
            sb.append("drop ").append(type).append(" ")
                  .append(sqlObject.getName()).append(" if exists");
            return sb;
        }
    }
    public HsqldbDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                      DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, ";\n");
    }


    public String buildDropTableScript(SqlTable sqlTable) {
        return HsqldbObject.TABLE.buildDropScript(this, sqlTable);
    }

    public String buildLogTableCreationScript(SqlTable table) {
/*
        String tableName = table.getName();
        return new StringBuilder()
              .append("if exists (SELECT 1 FROM SYSTEM_TABLES\n")
              .append("           WHERE TABLE_NAME = '").append(tableName).append("')\n")
              .append("    print 'Table ").append(tableName).append(" created'")
              .append(getQueryDelimiter()).append("\n")
              .toString();
*/
        return "";
    }

    public String buildCreateViewScript(SqlView sqlView) {
        StringBuilder sb = HsqldbObject.VIEW.buildDropScript(null, this, sqlView);
        sb.append(getQueryHelper().buildCreateViewQuery(sqlView)).append(getQueryDelimiter()).append('\n');
        return sb.toString();
    }

    public String buildDropIndexScript(SqlIndex sqlIndex) {
        return HsqldbObject.INDEX.buildDropScript(this, sqlIndex);
    }

    public String buildLogIndexCreationScript(SqlIndex sqlIndex) {
        return "";
    }


    public String buildDropConstraintScript(SqlConstraint sqlConstraint) {
        return HsqldbObject.CONSTRAINT.buildDropScript(this, sqlConstraint);
    }


    public String buildLogConstraintCreationScript(SqlConstraint sqlConstraint) {
        return "";
    }

    protected String createCheckRecordTriggerScript(SqlTrigger trigger) {
        String result;
        if (Type.CHECK_RECORD.equals(trigger.getType())) {
            String baseName = trigger.getName();
            try {
                trigger.setType(Type.INSERT);
                trigger.setName(getTriggerName(baseName, Type.INSERT));
                StringBuilder res = new StringBuilder(super.createCheckRecordTriggerScript(trigger));

                trigger.setType(Type.UPDATE);
                trigger.setName(getTriggerName(baseName, Type.UPDATE));
                res.append(super.createCheckRecordTriggerScript(trigger));

                result = res.toString();
            } finally {
                trigger.setType(Type.CHECK_RECORD);
                trigger.setName(baseName);
            }
        } else {
            result = super.createCheckRecordTriggerScript(trigger);
        }

        return result;
    }

    public static String getTriggerName(String baseName, Type type) {
        String result = baseName;
        if (Type.INSERT.equals(type)) {
            result += "_INSERT";
        } else if (Type.UPDATE.equals(type)) {
            result += "_UPDATE";
        }
        return result;
    }

    @Override
    protected String buildTriggerScriptBeginPart(SqlTrigger trigger) {
        String triggerName = trigger.getName();
        String tableName = trigger.getTable().getName();

        StringBuilder script = HsqldbObject.TRIGGER.buildDropScript(null, this, trigger);

        String triggerTypeAsString = "";
        boolean delete = false;
        switch (trigger.getType()) {
            case INSERT:
                triggerTypeAsString = "Insert";
                break;
            case DELETE:
                triggerTypeAsString = "Delete";
                delete = true;
                break;
            case UPDATE:
                triggerTypeAsString = "Update";
                break;
            case CHECK_RECORD:
                throw new UnsupportedOperationException("CHECK_RECORD is not directly supported by hsqldb");
        }

        script.append("/*  ").append(triggerTypeAsString).append(" trigger \"").append(triggerName)
              .append("\" for table \"").append(tableName).append("\"  */\n")
              .append("create trigger ").append(triggerName).append(" after ")
              .append(triggerTypeAsString.toLowerCase()).append(" on ").append(tableName);

        script.append(" referencing");
        if (Type.UPDATE.equals(trigger.getType()) || Type.DELETE.equals(trigger.getType())) {
            script.append(" old row as oldrow");
        }
        if (Type.UPDATE.equals(trigger.getType()) || Type.INSERT.equals(trigger.getType())) {
            script.append(" new row as newrow");
        }
        script.append(" for each row\n");

        script.append("begin atomic\n");
        return script.toString();
    }


    @Override
    protected String buildTriggerScriptEndPart(SqlTrigger trigger) {
        return "end;\n";
    }


    @Override
    protected String buildTriggerScriptDeleteCascadePart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        for (TableLink link : trigger.getLinks()) {
            String otherTableName = link.getOtherTable().getName();

            script.append("    /*  Delete all children in \"").append(otherTableName).append("\"  */\n");

            List<SqlField[]> condition = link.getLinkedFields();
            script.append("    delete from   ").append(otherTableName).append(" t2")
                  .append("    where  ");
            addConditions(script, condition, "oldrow");
            script.append(";\n\n");
        }
        return script.toString();
    }

    private StringBuilder addConditions(StringBuilder script, List<SqlField[]> conditions, String t1Name) {
        if (conditions.size() > 0) {
            addCondition(script, conditions.get(0)[0], conditions.get(0)[1], t1Name);

            for (int i = 1; i < conditions.size(); i++) {
                script.append("\n")
                      .append("           and ");
                addCondition(script, conditions.get(i)[0], conditions.get(i)[1], t1Name);
            }
        }
        return script;
    }

    private void addCondition(StringBuilder script, SqlField t1Field, SqlField t2Field, String t1Name) {
        t1Name = (t1Name == null) ? "t1" : t1Name;
        String t1FieldName = t1Field.getName();
        String t2FieldName = t2Field.getName();
        script.append("t2.").append(t2FieldName).append(" = ").append(t1Name).append(".").append(t1FieldName);
    }

    @Override
    protected String buildTriggerScript(SqlTrigger trigger, String centralPart) {
        if ((centralPart == null) || (centralPart.trim().length() == 0)) {
            // hsqldb doesn't allow empty blocks
            // => add some dummy code
            centralPart = "DECLARE V CHAR(10);\n"
                        + "set V = 'V';\n";
        }

        return super.buildTriggerScript(trigger, centralPart);
    }

    @Override
    protected String buildTriggerScriptCheckRecordPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();

//        if (Type.UPDATE.equals(trigger.getType())) {
            String tableName = trigger.getTable().getName();
            for (TableLink link : trigger.getLinks()) {
                String otherTableName = link.getOtherTable().getName();
                List<SqlField[]> condition = link.getLinkedFields();
                String fieldName = condition.get(0)[0].getName();
                String otherFieldName = condition.get(0)[1].getName();
                script.append("\n")
                      .append("    /*  Parent \"").append(otherTableName)
                      .append("\" must exist when inserting a child in \"").append(tableName)
                      .append("\"  */\n")
                      .append("       if not exists(select 1\n")
                      .append("           from   ").append(otherTableName).append(" t1\n")
                      .append("           where  t1.").append(otherFieldName).append(" = newrow.")
                      .append(fieldName)
                      .append(") then\n")
                      .append("             signal SQLSTATE '30002' set MESSAGE_TEXT='Parent does not exist in \"")
                      .append(otherTableName).append("\". Cannot create child in \"").append(tableName)
                      .append("\".';\n")
                      .append("    end if;\n")
                      .append("\n");
            }
//        } else {
//            script.append(trigger.getSqlContent());
//        }
        return script.toString();
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {
        script.append(" GENERATED BY DEFAULT AS IDENTITY (START WITH 1, INCREMENT BY 1) ");
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {
        script.append("\n")
              .append(" constraint CKC_").append(fieldDefinition.getName())
              .append(" check (")
              .append(fieldDefinition.getName())
              .append(" in (").append(fieldDefinition.getCheck()).append("))");
    }
}
