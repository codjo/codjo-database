package net.codjo.database.sybase.impl.script;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
public class SybaseExecSqlScriptBuilder implements ExecSqlScriptBuilder {

    public ExecSqlScript get() {
        return new SybaseExecSqlScript();
    }
}
