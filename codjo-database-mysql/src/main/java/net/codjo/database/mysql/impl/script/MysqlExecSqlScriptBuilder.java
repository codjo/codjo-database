package net.codjo.database.mysql.impl.script;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
public class MysqlExecSqlScriptBuilder implements ExecSqlScriptBuilder {

    public ExecSqlScript get() {
        return new MysqlExecSqlScript();
    }
}
