package net.codjo.database.common.api;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlView;
public interface DatabaseScriptHelper {

    void setLegacyMode(boolean legacyMode);


    void setLegacyPrefix(String legacyPrefix);


    String getQueryDelimiter();


    String buildDropTableScript(SqlTable sqlTable);


    String buildCreateTableScript(SqlTableDefinition sqlTableDefintion);


    String buildLogTableCreationScript(SqlTable sqlTable);


    String buildCreateViewScript(SqlView sqlView);


    String buildDropIndexScript(SqlIndex sqlIndex);


    String buildCreateIndexScript(SqlIndex sqlIndex);


    String buildLogIndexCreationScript(SqlIndex sqlIndex);


    String buildDropConstraintScript(SqlConstraint sqlConstraint);


    String buildCreateConstraintScript(SqlConstraint sqlConstraint);


    String buildLogConstraintCreationScript(SqlConstraint sqlConstraint);


    String buildCreateTriggerScript(SqlTrigger trigger);


    String buildCustomScript(String customScriptName, Object... parameters);
}
