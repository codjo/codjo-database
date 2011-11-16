package net.codjo.database.hsqldb.impl.helper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelper;
public class HsqldbDatabaseScriptHelper extends AbstractDatabaseScriptHelper {

    public HsqldbDatabaseScriptHelper(DatabaseQueryHelper databaseQueryHelper,
                                      DatabaseTranscoder databaseTranscoder) {
        super(databaseQueryHelper, databaseTranscoder, ";");
    }


    public String buildDropTableScript(SqlTable sqlTable) {
        return new StringBuilder()
              .append("drop table ").append(sqlTable.getName()).append(" if exists")
              .append(getQueryDelimiter()).append("\n")
              .toString();
    }


    @Override
    public String buildCreateTableScript(SqlTableDefinition sqlTableDefintion) {
        throw new UnsupportedOperationException();// Todo buildCreateTableScript
    }


    public String buildLogTableCreationScript(SqlTable sqlTable) {
        throw new UnsupportedOperationException();// Todo buildLogTableCreationScript
    }


    public String buildCreateViewScript(SqlView sqlView) {
        throw new UnsupportedOperationException();// Todo buildCreateViewScript
    }


    public String buildDropIndexScript(SqlIndex sqlIndex) {
        throw new UnsupportedOperationException(); // Todo buildDropIndexScript
    }


    @Override
    public String buildCreateIndexScript(SqlIndex index) {// TODO
        throw new UnsupportedOperationException();
    }


    public String buildLogIndexCreationScript(SqlIndex sqlIndex) {
        throw new UnsupportedOperationException(); // Todo buildLogIndexCreationScript
    }


    public String buildDropConstraintScript(SqlConstraint sqlConstraint) {
        throw new UnsupportedOperationException();  // Todo buildDropConstraintScript
    }


    public String buildLogConstraintCreationScript(SqlConstraint sqlConstraint) {
        throw new UnsupportedOperationException();  // Todo buildLogConstraintCreationScript
    }


    @Override
    protected String buildTriggerScriptBeginPart(SqlTrigger trigger) {
        throw new UnsupportedOperationException();// Todo buildTriggerScriptBeginPart
    }


    @Override
    protected String buildTriggerScriptEndPart(SqlTrigger trigger) {
        throw new UnsupportedOperationException();// Todo buildTriggerScriptEndPart
    }


    @Override
    protected String buildTriggerScriptDeleteCascadePart(SqlTrigger trigger) {
        throw new UnsupportedOperationException();// Todo buildTriggerScriptDeleteCascadePart
    }


    @Override
    protected String buildTriggerScriptCheckRecordPart(SqlTrigger trigger) {
        throw new UnsupportedOperationException();// Todo buildTriggerScriptCheckRecordPart
    }


    @Override
    protected void handleFieldIsIdentity(StringBuilder script) {// TODO
        throw new UnsupportedOperationException();
    }


    @Override
    protected void handleFieldIsCheck(StringBuilder script, SqlFieldDefinition fieldDefinition) {// TODO
        throw new UnsupportedOperationException();
    }
}
