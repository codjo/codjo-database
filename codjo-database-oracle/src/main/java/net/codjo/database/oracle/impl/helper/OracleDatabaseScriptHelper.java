package net.codjo.database.oracle.impl.helper;
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
public class OracleDatabaseScriptHelper extends AbstractDatabaseScriptHelper {

    public OracleDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                      DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, ";");
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
        return new StringBuilder()
              .append("execute util_pk.dropTable('").append(table.getName()).append("');\n")
              .append("/\n")
              .append("\n")
              .append("sho err\n")
              .toString();
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {
        throw new UnsupportedOperationException();
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {
        String fieldName = fieldDefinition.getName();
        script.append("\n")
              .append(" constraint CKC_").append(fieldName)
              .append(" check (").append(fieldName)
              .append(" in (").append(fieldDefinition.getCheck()).append("))");
    }


    @Override
    protected String buildCreateTableScriptEndPart(SqlTableDefinition tableDefinition) {
        StringBuilder script = new StringBuilder();
        if (tableDefinition.isPkGenerator() && !tableDefinition.getPrimaryKeys().isEmpty()) {
            script.append(",\n")
                  .append(" constraint PK_").append(tableDefinition.getName()).append(" primary key (");
            int idx = 0;
            for (String primaryKey : tableDefinition.getPrimaryKeys()) {
                script.append(primaryKey);
                if (++idx < tableDefinition.getPrimaryKeys().size()) {
                    script.append(", ");
                }
            }
            script.append(")");
        }
        return script.append(super.buildCreateTableScriptEndPart(tableDefinition)).toString();
    }


    public String buildLogTableCreationScript(SqlTable sqlTable) {
        return "";
    }


    public String buildDropIndexScript(SqlIndex index) {
        return new StringBuilder()
              .append("execute util_pk.dropIndex('").append(index.getName()).append("');\n")
              .append("/\n")
              .append("\n")
              .append("sho err\n")
              .toString();
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
        return new StringBuilder()
              .append("execute util_pk.dropConstraint('").append(constraint.getName()).append("');\n")
              .append("/\n")
              .append("\n")
              .append("sho err\n")
              .toString();
    }


    @Override
    public String buildCreateConstraintScript(SqlConstraint constraint) {
        return new StringBuilder()
              .append(buildCreatePrimaryKeyQuery(constraint.getReferencedTable(),
                                                 constraint.getLinkedFields())).append(";\n")
              .append("\n")
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
        switch (trigger.getType()) {
            case CHECK_RECORD:
                script.append("\n")
                      .append("declare\n")
                      .append("    parentCount number := 0;\n")
                      .append("\n");
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
              .append("end ").append(triggerName).append(";\n")
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
}
