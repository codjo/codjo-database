package net.codjo.database.hsqldb.impl.script;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
public class HsqldbExecSqlScriptBuilder implements ExecSqlScriptBuilder {

    public ExecSqlScript get() {
        return new HsqldbExecSqlScript();
    }
}
