package net.codjo.database.mysql.impl.helper;
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
import java.util.List;
public class MysqlDatabaseScriptHelper extends AbstractDatabaseScriptHelper {

    public MysqlDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                     DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, ";");
    }


    public String buildDropTableScript(SqlTable sqlTable) {
        return new StringBuilder()
              .append("drop table if exists ").append(sqlTable.getName()).append(";\n")
              .toString();
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {
        script.append(" auto_increment ");
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {
        script.append(" check (")
              .append(fieldDefinition.getName())
              .append(" in (").append(fieldDefinition.getCheck()).append("))");
    }


    @Override
    protected String buildCreateTableScriptEndPart(SqlTableDefinition tableDefinition) {
        StringBuilder script = new StringBuilder();
        if (tableDefinition.isPkGenerator() && !tableDefinition.getPrimaryKeys().isEmpty()) {
            script.append(",\n")
                  .append("    primary key (");
            int idx = 0;
            for (String primaryKey : tableDefinition.getPrimaryKeys()) {
                script.append(primaryKey);
                if (++idx < tableDefinition.getPrimaryKeys().size()) {
                    script.append(", ");
                }
            }
            script.append(")");
        }
        return script
              .append("\n")
              .append(") ENGINE=InnoDB").append(getQueryDelimiter()).append("\n")
              .toString();
    }


    public String buildLogTableCreationScript(SqlTable sqlTable) {
        String tableName = sqlTable.getName();
        return new StringBuilder()
              .append("select 'Table ").append(tableName).append(" created' as ''\n")
              .append("from information_schema.TABLES\n")
              .append("where TABLE_NAME='").append(tableName).append("' ")
              .append("and TABLE_TYPE='BASE TABLE'").append(";\n")
              .toString();
    }


    public String buildCreateViewScript(SqlView view) {
        String viewName = view.getName();
        return new StringBuilder()
              .append("drop view if exists ").append(viewName).append(getQueryDelimiter()).append("\n")
              .append("\n")
              .append(getQueryHelper().buildCreateViewQuery(view)).append(getQueryDelimiter()).append("\n")
              .append("\n")
              .append("select 'View ").append(viewName).append(" created' as ''\n")
              .append("from information_schema.TABLES\n")
              .append("where TABLE_NAME='").append(viewName).append("' ")
              .append("and TABLE_TYPE='VIEW'").append(";\n")
              .toString();
    }


    public String buildDropIndexScript(SqlIndex sqlIndex) {
        return new StringBuilder()
              .append("call tools.sp_dropIndex (database(), '").append(sqlIndex.getTable().getName())
              .append("'")
              .append(",'").append(sqlIndex.getName()).append("')").append(";\n")
              .toString();
    }


    public String buildLogIndexCreationScript(SqlIndex sqlIndex) {
        String indexTableName = sqlIndex.getTable().getName();
        String indexName = sqlIndex.getName();
        return new StringBuilder()
              .append("select 'Index ").append(indexTableName).append(".").append(indexName)
              .append(" created' as ''\n")
              .append("from information_schema.STATISTICS\n")
              .append("where INDEX_NAME='").append(indexName).append("' ")
              .append("and TABLE_NAME='").append(indexTableName).append("'\n")
              .append("group by INDEX_NAME").append(";\n")
              .toString();
    }


    public String buildDropConstraintScript(SqlConstraint sqlConstraint) {
        String alteredTableName = sqlConstraint.getAlteredTable().getName();
        return new StringBuilder()
              .append("call tools.sp_dropForeignKey (database(), '").append(alteredTableName).append("'")
              .append(", '").append(sqlConstraint.getName()).append("')").append(";\n")
              .toString();
    }


    public String buildLogConstraintCreationScript(SqlConstraint sqlConstraint) {
        String constraintName = sqlConstraint.getName();
        String alteredTableName = sqlConstraint.getAlteredTable().getName();
        return new StringBuilder()
              .append("select 'Foreign key ").append(alteredTableName).append(".").append(constraintName)
              .append(" created' as ''\n")
              .append("from information_schema.TABLE_CONSTRAINTS\n")
              .append("where CONSTRAINT_NAME='").append(constraintName).append("' ")
              .append("and CONSTRAINT_TYPE='FOREIGN KEY'").append(";\n")
              .toString();
    }


    @Override
    protected String createCheckRecordTriggerScript(SqlTrigger trigger) {
        String sqlContent = buildCheckRecordSqlContent(trigger);
        SqlTrigger insertTrigger = SqlTrigger
              .insertTrigger(trigger.getName() + "_I", trigger.getTable(), sqlContent);
        SqlTrigger updateTrigger = SqlTrigger
              .updateTrigger(trigger.getName() + "_U", trigger.getTable(), sqlContent);
        return new StringBuilder()
              .append(super.createInsertTriggerScript(insertTrigger))
              .append("\n")
              .append("\n")
              .append(super.createUpdateTriggerScript(updateTrigger)).toString();
    }


    @Override
    protected String buildTriggerScriptBeginPart(SqlTrigger trigger) {
        StringBuilder script = new StringBuilder();
        String triggerName = trigger.getName();
        script.append("drop procedure if exists foo_trigger_deleter;\n")
              .append("\n")
              .append("delimiter $$\n")
              .append("create procedure foo_trigger_deleter()\n")
              .append("begin\n")
              .append("\n")
              .append("    declare l_count integer;\n")
              .append("\n")
              .append("    select 1 into l_count\n")
              .append("    from information_schema.TRIGGERS\n")
              .append("    where TRIGGER_NAME = '").append(triggerName).append("';\n")
              .append("\n")
              .append("    if (l_count > 0) then\n")
              .append("        drop trigger ").append(triggerName).append(";\n")
              .append("        select 'Trigger ").append(triggerName).append(" dropped' as '';\n")
              .append("    end if;\n")
              .append("\n")
              .append("end $$\n")
              .append("delimiter ;\n")
              .append("\n")
              .append("call foo_trigger_deleter();\n")
              .append("drop procedure foo_trigger_deleter;\n")
              .append("\n")
              .append("\n")
              .append("delimiter $$\n")
              .append("create trigger ").append(triggerName).append("\n");
        switch (trigger.getType()) {
            case INSERT:
                script.append("    after insert");
                break;
            case DELETE:
                script.append("    after delete");
                break;
            case UPDATE:
                script.append("    after update");
                break;
            case CHECK_RECORD:
            default:
                throw new UnsupportedOperationException();
        }
        script.append(" on ").append(trigger.getTable().getName()).append(" for each row\n")
              .append("begin\n");
        return script.toString();
    }


    @Override
    protected String buildTriggerScriptEndPart(SqlTrigger trigger) {
        String triggerName = trigger.getName();
        return new StringBuilder()
              .append("end $$\n")
              .append("delimiter ;\n")
              .append("\n")
              .append("\n")
              .append("select 'Trigger ").append(triggerName).append(" created' as ''\n")
              .append("from information_schema.TRIGGERS\n")
              .append("where TRIGGER_NAME = '").append(triggerName).append("';\n").toString();
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
        throw new UnsupportedOperationException();
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
        script.append(t2FieldName).append(" = OLD.").append(t1FieldName);
    }


    private String buildCheckRecordSqlContent(SqlTrigger trigger) {
        String tableName = trigger.getTable().getName();
        StringBuilder script = new StringBuilder()
              .append("    declare parentCount integer;\n")
              .append("\n");
        for (int i = 0; i < trigger.getLinks().size(); i++) {
            TableLink tableLink = trigger.getLinks().get(i);
            String otherTableName = tableLink.getOtherTable().getName();
            SqlField[] sqlFields = tableLink.getLinkedFields().get(0);
            String childFieldName = sqlFields[0].getName();
            String parentFieldName = sqlFields[1].getName();
            if (i > 0) {
                script.append("\n");
            }

            script.append("    select count(1) into parentCount\n")
                  .append("    from   ").append(otherTableName).append(" t1\n")
                  .append("    where  t1.").append(parentFieldName)
                  .append(" = new.").append(childFieldName).append(";\n")
                  .append("\n")
                  .append("    if (parentCount = 0) then\n")
                  .append("        call raise_error('Parent does not exist in \"").append(otherTableName)
                  .append("\".").append(" Cannot create child in \"").append(tableName).append("\".');\n")
                  .append("    end if;");

            if (i + 1 < trigger.getLinks().size()) {
                script.append("\n");
            }
        }
        return script.toString();
    }
}
