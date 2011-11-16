package net.codjo.database.oracle.impl.script;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
public class OracleExecSqlScriptBuilder implements ExecSqlScriptBuilder {

    public ExecSqlScript get() {
        return new OracleExecSqlScript();
    }
}
