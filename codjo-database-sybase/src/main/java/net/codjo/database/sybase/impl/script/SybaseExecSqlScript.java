package net.codjo.database.sybase.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
public class SybaseExecSqlScript extends AbstractExecSqlScript {

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
        return "\ngo";
    }


    @Override
    public String createSqlScriptCommand(String scriptFileName) {
        return "isql -U" + getConnectionMetadata().getUser()
               + " -P" + getConnectionMetadata().getPassword()
               + " -S" + getConnectionMetadata().getBase()
               + " -D" + getConnectionMetadata().getCatalog()
               + " -i \"" + scriptFileName + "\"";
    }


    @Override
    public String getSqlErrorKeyWord() {
        return "msg ";
    }


    @Override
    protected String getScriptNotFoundErrorKeyWord() {
        return null;
    }
}
