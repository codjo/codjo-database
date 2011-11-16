package net.codjo.database.hsqldb.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
import java.util.List;
public class HsqldbExecSqlScript extends AbstractExecSqlScript {

    @Override
    public void execute(String workingDirectory, String... scriptFileNames) {// TODO
        throw new UnsupportedOperationException();
    }


    @Override
    public void execute(String workingDirectory, List<String> scriptFileNames) {// TODO
        throw new UnsupportedOperationException();
    }


    @Override
    public void executeContentOfFile(String deliveryFileName) {// TODO
        throw new UnsupportedOperationException();
    }


    @Override
    public String getQueryDelimiter() {
        return null;  // Todo
    }


    @Override
    public String getProcessInputMessage() {
        return null;  // Todo
    }


    @Override
    public String removeConnectionMessage(String processMessage) {
        return null;  // Todo
    }


    @Override
    public String createSqlScriptCommand(String scriptFileName) {
        return null;  // Todo
    }


    @Override
    public String getSqlErrorKeyWord() {
        return null;  // Todo
    }


    @Override
    protected String getScriptNotFoundErrorKeyWord() {
        return null;  // Todo
    }
}
