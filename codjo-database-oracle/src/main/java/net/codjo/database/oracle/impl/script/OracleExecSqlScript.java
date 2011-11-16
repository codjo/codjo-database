package net.codjo.database.oracle.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
public class OracleExecSqlScript extends AbstractExecSqlScript {

    @Override
    public String getProcessInputMessage() {
        return "\n\nexit";
    }


    @Override
    public String removeConnectionMessage(String processMessage) {
        int promptIndex = processMessage.indexOf("SQL>");
        if (promptIndex != -1) {
            processMessage = processMessage.substring(0, promptIndex);
        }
        int productionIndex = processMessage.lastIndexOf("Data Mining options");
        if (productionIndex != -1) {
            processMessage = processMessage.substring(productionIndex + "Data Mining options".length());
        }
        return processMessage.trim();
    }


    @Override
    public String getQueryDelimiter() {
        return ";";
    }


    @Override
    public String createSqlScriptCommand(String scriptFileName) {
        return "sqlplus " + getConnectionMetadata().getUser() + "/" + getConnectionMetadata().getPassword()
               + "@" + getConnectionMetadata().getCatalog() + " @\"" + scriptFileName + "\"";
    }


    @Override
    public String getSqlErrorKeyWord() {
        return "ora-";
    }


    @Override
    public String getScriptNotFoundErrorKeyWord() {
        return "sp2-0310: unable to open file";
    }
}
