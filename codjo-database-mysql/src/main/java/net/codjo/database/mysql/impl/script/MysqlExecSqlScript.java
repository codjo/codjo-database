package net.codjo.database.mysql.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
public class MysqlExecSqlScript extends AbstractExecSqlScript {

    @Override
    public String getProcessInputMessage() {
        return null;
    }


    @Override
    public String removeConnectionMessage(String processMessage) {
        return processMessage;
    }


    @Override
    public String getQueryDelimiter() {
        return ";";
    }


    @Override
    public String createSqlScriptCommand(String scriptFileName) {
        return "mysql " + getConnectionMetadata().getCatalog()
               + " -h " + getConnectionMetadata().getHostname()
               + " -P " + getConnectionMetadata().getPort()
               + " -u " + getConnectionMetadata().getUser()
               + " -p" + getConnectionMetadata().getPassword()
               + " <\"" + scriptFileName + "\">  log.txt";
    }


    @Override
    public String getSqlErrorKeyWord() {
        return "error ";
    }


    @Override
    protected String getScriptNotFoundErrorKeyWord() {
        return null;
    }
}
