package net.codjo.database.sybase.impl.helper;
import java.util.List;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlTrigger.TableLink;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelper;
import net.codjo.database.sybase.impl.LegacyDatabaseTranscoder;
import net.codjo.database.sybase.impl.SybaseDatabaseTranscoder;
import net.codjo.database.sybase.util.SybaseUtil;

import static net.codjo.database.sybase.util.SybaseUtil.getName;
public class SybaseDatabaseScriptHelper extends AbstractDatabaseScriptHelper {
    private boolean legacyTable;


    public SybaseDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                      DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, "\ngo");
        registerCustomScript("createGap", new CustomScript() {
            public String buildScript(Object... parameters) {
                SqlTable table = (SqlTable)parameters[0];
                String gapValue = (String)parameters[1];
                return buildCreateGapScript(table, gapValue);
            }
        });
        registerCustomScript("logGapCreation", new CustomScript() {
            public String buildScript(Object... parameters) {
                SqlTable table = (SqlTable)parameters[0];
                String gapValue = (String)parameters[1];
                return buildLogGapCreationScript(table, gapValue);
            }
        });
    }


    @Override
    protected DatabaseTranscoder getDatabaseTranscoder() {
        if (legacyTable) {
            return new LegacyDatabaseTranscoder();
        }
        else {
            return new SybaseDatabaseTranscoder();
        }
    }


    @Override
    protected String buildFieldDefinition(SqlTableDefinition tableDefinition,
                                          SqlFieldDefinition fieldDefinition) {
        if (isLegacyMode()) {
            legacyTable = getLegacyPrefix() != null && tableDefinition.getName().startsWith(getLegacyPrefix());
            return super.buildFieldDefinition(tableDefinition, fieldDefinition)
                  .replace("bit default null", "bit");
        }
        else {
            return super.buildFieldDefinition(tableDefinition, fieldDefinition);
        }
    }


    public String buildDropTableScript(SqlTable table) {
        String tableName = getName(table);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where id = object_id('").append(tableName).append("') and type = 'U')\n")
              .append("begin\n")
              .append("    drop table ").append(tableName).append("\n")
              .append("    print 'Table ").append(tableName).append(" dropped'\n")
              .append("end")
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {
        script.append(" identity ");
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {
        script.append("\n")
              .append(" constraint CKC_").append(fieldDefinition.getName())
              .append(" check (")
              .append(fieldDefinition.getName())
              .append(" in (").append(fieldDefinition.getCheck()).append("))");
    }


    public String buildLogTableCreationScript(SqlTable table) {
        String tableName = getName(table);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where id = object_id('").append(tableName).append("') and type = 'U')\n")
              .append("    print 'Table ").append(tableName).append(" created'")
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildCreateViewScript(SqlView sqlView) {
        String viewName = getName(sqlView);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where type = 'V' and name = '").append(viewName).append("')\n")
              .append("begin\n")
              .append("    drop view ").append(viewName).append("\n")
              .append("    print 'View ").append(viewName).append(" dropped'\n")
              .append("end").append(getQueryDelimiter()).append("\n")
              .append("\n")
              .append(getQueryHelper().buildCreateViewQuery(sqlView)).append(getQueryDelimiter()).append("\n")
              .append("\n")
              .append("if exists (select 1 from sysobjects\n")
              .append("           where type = 'V' and name = '").append(viewName).append("')\n")
              .append("    print 'View ").append(viewName).append(" created'").append(getQueryDelimiter())
              .append("\n")
              .toString();
    }


    public String buildDropIndexScript(SqlIndex index) {
        String indexName = getName(index);
        String tableName = getName(index.getTable());
        String fullIndexName = SybaseUtil.getFullIndexName(index);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects o, sysindexes i\n")
              .append("           where o.id = i.id and i.name = '").append(indexName).append("'\n")
              .append("           and o.name = '").append(tableName).append("' and o.type = 'U')\n")
              .append("begin\n")
              .append("    drop index ").append(fullIndexName).append("\n")
              .append("    print 'Index ").append(fullIndexName).append(" dropped'\n")
              .append("end").append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildLogIndexCreationScript(SqlIndex index) {
        String indexName = getName(index);
        String tableName = getName(index.getTable());
        String fullIndexName = SybaseUtil.getFullIndexName(index);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects o, sysindexes i\n")
              .append("           where o.id = i.id and i.name = '").append(indexName).append("'\n")
              .append("           and o.name = '").append(tableName).append("' and o.type = 'U')\n")
              .append("    print 'Index ").append(fullIndexName).append(" created'")
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildDropConstraintScript(SqlConstraint constraint) {
        String constraintName = getName(constraint);
        String tableName = getName(constraint.getAlteredTable());
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where name = '").append(constraintName).append("' and type = 'RI')\n")
              .append("begin\n")
              .append("    ").append(getQueryHelper().buildDropConstraintQuery(constraint))
              .append("\n")
              .append("    print 'Foreign key ")
              .append(tableName).append(".").append(constraintName).append(" dropped'\n")
              .append("end").append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildLogConstraintCreationScript(SqlConstraint constraint) {
        String constraintName = getName(constraint);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where name = '").append(constraintName).append("'\n")
              .append("           and type = 'RI')\n")
              .append("    print 'Foreign key ").append(getName(constraint.getAlteredTable()))
              .append(".").append(constraintName).append(" created'").append(getQueryDelimiter()).append("\n")
              .toString();
    }


    @Override
    protected String buildTriggerScriptBeginPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        String triggerName = getName(trigger);
        String tableName = getName(trigger.getTable());

        script.append("if exists (select 1 from sysobjects\n")
              .append("           where id = object_id('").append(triggerName).append("')\n")
              .append("           and type = 'TR')\n")
              .append("begin\n")
              .append("    drop trigger ").append(triggerName).append("\n")
              .append("    print 'Trigger ").append(triggerName).append(" dropped'\n")
              .append("end").append(getQueryDelimiter()).append("\n")
              .append("\n");

        String triggerTypeAsString = "";
        switch (trigger.getType()) {
            case INSERT:
                triggerTypeAsString = "Insert";
                break;
            case DELETE:
                triggerTypeAsString = "Delete";
                break;
            case UPDATE:
                triggerTypeAsString = "Update";
                break;
            case CHECK_RECORD:
                triggerTypeAsString = "Insert, Update";
                break;
        }

        script.append("/*  ").append(triggerTypeAsString).append(" trigger \"").append(triggerName)
              .append("\" for table \"").append(tableName).append("\"  */\n")
              .append("create trigger ").append(triggerName).append(" on ")
              .append(tableName).append(" for ")
              .append(triggerTypeAsString.toLowerCase())
              .append(" as\n")
              .append("begin\n")
              .append("    declare\n")
              .append("       @numrows  int,\n")
              .append("       @errno    int,\n")
              .append("       @errmsg   varchar(255)\n")
              .append("\n")
              .append("    select  @numrows = @@rowcount\n")
              .append("    if @numrows = 0\n")
              .append("       return\n");

        return script.toString();
    }


    @Override
    protected String buildTriggerScriptEndPart(SqlTrigger trigger) {
        return new StringBuilder()
              .append("    return\n")
              .append("\n")
              .append("/*  Errors handling  */\n")
              .append("error:\n")
              .append("    raiserror @errno @errmsg\n")
              .append("    rollback  transaction\n")
              .append("end").append(getQueryDelimiter()).append("\n")
              .append("\n")
              .append(buildLogTriggerCreationScript(trigger)).toString();
    }


    @Override
    protected String buildTriggerScriptDeleteCascadePart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        for (TableLink link : trigger.getLinks()) {
            String otherTableName = getName(link.getOtherTable());
            List<SqlField[]> condition = link.getLinkedFields();
            script.append("\n")
                  .append("    /*  Delete all children in \"").append(otherTableName).append("\"  */\n")
                  .append("    delete ").append(otherTableName).append("\n")
                  .append("    from   ").append(otherTableName).append(" t2, deleted t1\n")
                  .append("    where  ");
            addConditions(script, condition);
            script.append("\n");
        }
        return script.toString();
    }


    @Override
    protected String buildTriggerScriptCheckRecordPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        String tableName = trigger.getTable().getName();
        for (TableLink link : trigger.getLinks()) {
            String otherTableName = getName(link.getOtherTable());
            List<SqlField[]> condition = link.getLinkedFields();
            String fieldName = getName(condition.get(0)[0]);
            String otherFieldName = getName(condition.get(0)[1]);
            script.append("\n")
                  .append("    /*  Parent \"").append(otherTableName)
                  .append("\" must exist when inserting a child in \"").append(tableName)
                  .append("\"  */\n")
                  .append("    if update(").append(fieldName).append(")\n")
                  .append("    begin\n")
                  .append("       if (select count(*)\n")
                  .append("           from   ").append(otherTableName).append(" t1, inserted t2\n")
                  .append("           where  t1.").append(otherFieldName).append(" = t2.")
                  .append(fieldName).append(") != @numrows\n")
                  .append("          begin\n")
                  .append("             select @errno  = 30002,\n")
                  .append(
                        "                    @errmsg = 'Parent does not exist in \"")
                  .append(otherTableName).append("\". Cannot create child in \"").append(tableName)
                  .append("\".'\n")
                  .append("             goto error\n")
                  .append("          end\n")
                  .append("    end\n")
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
        String t1FieldName = getName(t1Field);
        String t2FieldName = getName(t2Field);
        script.append("t2.").append(t2FieldName).append(" = t1.").append(t1FieldName);
    }


    private String buildCreateGapScript(SqlTable table, String gapValue) {
        return new StringBuilder()
              .append("sp_chgattribute '").append(getName(table))
              .append("', 'identity_gap', ").append(gapValue).append(getQueryDelimiter()).toString();
    }


    private String buildLogGapCreationScript(SqlTable table, String gapValue) {
        return new StringBuilder()
              .append("if exists (select 1 from sysindexes\n")
              .append("           where id = object_id('").append(getName(table))
              .append("')\n")
              .append("           and identitygap = ").append(gapValue).append(")\n")
              .append("    print 'Identity Gap = ").append(gapValue).append(" created'")
              .append(getQueryDelimiter()).toString();
    }


    private String buildLogTriggerCreationScript(SqlTrigger trigger) {
        String triggerName = getName(trigger);
        return new StringBuilder()
              .append("if exists (select 1 from sysobjects\n")
              .append("           where id = object_id('").append(triggerName).append("')\n")
              .append("           and type = 'TR')\n")
              .append("    print 'Trigger ").append(triggerName).append(" created'")
              .append(getQueryDelimiter()).toString();
    }
}
