package net.codjo.database.oracle.impl.helper;
import java.util.List;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlTrigger.TableLink;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelper;
public class OracleDatabaseScriptHelper extends AbstractDatabaseScriptHelper {

    public OracleDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                      DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, ";");
        registerCustomScript("createSequence", new CustomScript() {
            public String buildScript(Object... parameters) {
                SqlTable table = (SqlTable)parameters[0];
                return buildSequenceScript(table);
            }
        });
        registerCustomScript("createTriggerForSequence", new CustomScript() {
            public String buildScript(Object... parameters) {
                SqlTable table = (SqlTable)parameters[0];
                String identityField = (String)parameters[1];
                return buildTriggerSequenceScript(table, identityField, getSequenceName(table));
            }
        });
    }


    private String buildTriggerSequenceScript(SqlTable table, String identityField, String sequenceName) {
        StringBuilder sqlContent =
              new StringBuilder("if :new.").append(identityField).append(" is null then\n    select ")
                    .append(sequenceName).append(".nextval into :new.").append(identityField)
                    .append(" from dual;\nend if;\n");
        SqlTrigger trigger = SqlTrigger.insertTrigger("TR_" + table.getName() + "_SEQ", table, sqlContent.toString());

        String result = new StringBuilder()
              .append(buildTriggerScriptBeginPart(trigger))
              .append(sqlContent)
              .append(buildTriggerScriptEndPart(trigger)).toString();

        return result.replaceAll("after insert", "before insert");
    }


    @Override
    protected String getFieldDefinitionName(SqlFieldDefinition fieldDefinition) {
        String name = fieldDefinition.getName();
        if (isOracleKeyword(name)) {
            return "\"" + name + "\"";
        }
        return name;
    }


    public String buildCreateViewScript(SqlView view) {
        return new StringBuilder()
              .append("create or replace view ").append(view.getName()).append("\n")
              .append("    as\n")
              .append(view.getBody()).append(";\n")
              .append("/\n")
              .append("\n")
              .append("prompt View ").append(view.getName()).append(" created\n")
              .append("\n")
              .append("sho err\n")
              .toString();
    }


    public String buildDropTableScript(SqlTable table) {
        return dropObject("Table", table.getName());
    }


    private String buildSequenceScript(SqlTable table) {
        String sequenceName = getSequenceName(table);
        return new StringBuilder().append(dropObject("Sequence", sequenceName))
              .append("/\n\n")
              .append("create sequence ")
              .append(sequenceName)
              .append(
                    getQueryDelimiter())
              .append("\n")
              .toString();
    }


    private String getSequenceName(SqlTable table) {
        return "SEQ_" + table.getName();
    }


    private String dropObject(String type, String tableName) {
        return new StringBuilder()
              .append("execute util_pk.drop").append(type).append("('").append(tableName).append("');\n")
              .append("/\n")
              .append("\n")
              .append("sho err\n")
              .toString();
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {
        String check = fieldDefinition.getCheck();
        String checkValue = null;
        if (check != null) {
            checkValue = check.replaceAll("\"", "'");
        }

        script.append("\n")
              .append(" constraint CKC_").append(fieldDefinition.getName())
              .append(" check (").append(getFieldDefinitionName(fieldDefinition))
              .append(" in (").append(checkValue).append("))");
    }


    public String buildLogTableCreationScript(SqlTable sqlTable) {
        return "";
    }


    public String buildDropIndexScript(SqlIndex index) {
        return dropObject("Index", index.getName());
    }


    @Override
    public String buildCreateIndexScript(SqlIndex index) {
        return new StringBuilder()
              .append(getQueryHelper().buildCreateIndexQuery(index)).append(";\n")
              .toString();
    }


    public String buildLogIndexCreationScript(SqlIndex sqlIndex) {
        return "";
    }


    public String buildDropConstraintScript(SqlConstraint constraint) {
        return dropObject("Constraint", constraint.getName());
    }


    @Override
    public String buildCreateConstraintScript(SqlConstraint constraint) {
        return new StringBuilder()
              .append(getQueryHelper().buildCreateConstraintQuery(constraint)).append(";\n")
              .toString();
    }


    public String buildLogConstraintCreationScript(SqlConstraint sqlConstraint) {
        return "";
    }


    @Override
    protected String buildTriggerScriptBeginPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        script.append("create or replace trigger ").append(trigger.getName()).append("\n");
        switch (trigger.getType()) {
            case CHECK_RECORD:
                script.append("    after insert or update");
                break;
            case DELETE:
                script.append("    after delete");
                break;
            case INSERT:
                script.append("    after insert");
                break;
            case UPDATE:
                script.append("    after update");
                break;
        }
        script.append(" on ").append(trigger.getTable().getName()).append(" for each row\n");
        script.append("declare\n")
              .append("    v_errno    NUMBER(12)").append(";\n")
              .append("    v_errmsg   VARCHAR2(255)").append(";\n")
              .append("\n");

        switch (trigger.getType()) {
            case CHECK_RECORD:
                script.append("    parentCount number := 0").append(";\n");
                break;
            case DELETE:
            case INSERT:
            case UPDATE:
            default:
                break;
        }
        script.append("begin\n");

        return script.toString();
    }


    @Override
    protected String buildTriggerScriptEndPart(SqlTrigger trigger) {
        String triggerName = trigger.getName();
        return new StringBuilder()
              .append("return").append(";\n")
              .append("   /*  Errors handling  */").append("\n")
              .append("   <<error>>").append("\n")
              .append("   raise_application_error( -20002, v_errno|| ':' ||v_errmsg )").append(";\n")
              .append("   ROLLBACK").append(";\n")
              .append("end").append(";\n")
              .append("/\n")
              .append("\n")
              .append("prompt Trigger ").append(triggerName).append(" created\n")
              .append("\n")
              .append("sho err").toString();
    }


    @Override
    protected String buildTriggerScriptDeleteCascadePart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        for (TableLink link : trigger.getLinks()) {
            String otherTableName = link.getOtherTable().getName();
            List<SqlField[]> condition = link.getLinkedFields();
            script.append("\n")
                  .append("    /*  Delete all children in \"").append(otherTableName).append("\"  */\n")
                  .append("    delete\n")
                  .append("    from   ").append(otherTableName).append("\n")
                  .append("    where  ");
            addConditions(script, condition);
            script.append(";\n");
        }
        return script.toString();
    }


    @Override
    protected String buildTriggerScriptCheckRecordPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        String tableName = trigger.getTable().getName();
        for (TableLink tableLink : trigger.getLinks()) {
            String otherTableName = tableLink.getOtherTable().getName();
            SqlField[] sqlFields = tableLink.getLinkedFields().get(0);
            String childFieldName = sqlFields[0].getName();
            String parentFieldName = sqlFields[1].getName();
            script.append("\n")
                  .append("    select count(1) into parentCount\n")
                  .append("    from   ").append(otherTableName).append(" t1\n")
                  .append("    where  t1.").append(parentFieldName)
                  .append(" = :new.").append(childFieldName).append(";\n")
                  .append("\n")
                  .append("  if (parentCount = 0) then\n")
                  .append("    raise_application_error(-20000,\n")
                  .append("           'Parent does not exist in \"").append(otherTableName).append("\".")
                  .append(" Cannot create child in \"").append(tableName).append("\".');\n")
                  .append("  end if;\n")
                  .append("\n");
        }
        return script.toString();
    }


    private StringBuilder addConditions(StringBuilder script, List<SqlField[]> conditions) {
        if (conditions.size() > 0) {
            addCondition(script, conditions.get(0)[0], conditions.get(0)[1]);

            for (int i = 1; i < conditions.size(); i++) {
                script.append("\n")
                      .append("           and ");
                addCondition(script, conditions.get(i)[0], conditions.get(i)[1]);
            }
        }
        return script;
    }


    private void addCondition(StringBuilder script, SqlField t1Field, SqlField t2Field) {
        String t1FieldName = t1Field.getName();
        String t2FieldName = t2Field.getName();
        script.append(t2FieldName).append(" = :old.").append(t1FieldName);
    }


    private String buildCreatePrimaryKeyQuery(SqlTable table, List<SqlField[]> linkedFields) {
        String referencedTableName = table.getName();
        StringBuilder script = new StringBuilder()
              .append("alter table ").append(referencedTableName)
              .append(" add (constraint pk_").append(referencedTableName)
              .append(" primary key (");
        for (int i = 0; i < linkedFields.size(); i++) {
            SqlField sqlField = linkedFields.get(i)[1];
            script.append(sqlField.getName());
            if (i + 1 < linkedFields.size()) {
                script.append(", ");
            }
        }
        return script
              .append("))").toString();
    }


    private boolean isOracleKeyword(String name) {
        return "COMMENT".equals(name);
    }
}
