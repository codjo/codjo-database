package net.codjo.database.hsqldb.impl.script;
import java.io.File;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.StringTokenizer;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
import java.util.List;
import net.codjo.util.file.FileUtil;
import org.apache.log4j.Logger;
import org.apache.poi.hssf.record.formula.eval.ErrorEval;
public class HsqldbExecSqlScript extends AbstractExecSqlScript {
    private static final org.apache.log4j.Logger LOG = org.apache.log4j.Logger.getLogger(HsqldbExecSqlScript.class);

    private final DatabaseFactory databaseRepositorydatabaseFactory = new DatabaseFactory();

    @Override
    public String getQueryDelimiter() {
        return ";";
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
        return null; // unused
    }


    @Override
    public String getSqlErrorKeyWord() {
        return null;  // Todo
    }


    @Override
    protected String getScriptNotFoundErrorKeyWord() {
        return null;  // Todo
    }


    @Override
    protected Connection prepareExecuteScripts() throws SQLException {
        return databaseRepositorydatabaseFactory.createDatabaseHelper().createConnection(getConnectionMetadata());
    }

    /**
     *
     * @param scriptName
     * @param cmd <strong>unused parameter</strong>
     * @param workingDirectory
     */
    @Override
    protected void executeScript(Connection connection, String scriptName, String cmd, File workingDirectory) {
        File scriptFile = new File(workingDirectory, scriptName);
        try {
            final String script = FileUtil.loadContent(scriptFile);
            connection.createStatement().execute(script);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
        catch (SQLException e) {
            throw new RuntimeException(e.getMessage() + printScript(scriptFile, e), e);
        }
    }

    private String printScript(File scriptFile, SQLException e) {
        String result = "<unable to load script from file " + scriptFile.getAbsolutePath() + ">";

        try {
            final String script = FileUtil.loadContent(scriptFile);
            result = script;

            String errorMessage = e.getMessage();
            final String linePattern = "line:";
            int idx = errorMessage.lastIndexOf(linePattern);
            if (idx >= 0) {
                if (errorMessage.startsWith(e.getSQLState())) {
                    errorMessage = errorMessage.substring(e.getSQLState().length());
                }
                idx += linePattern.length();
                int line = Integer.parseInt(errorMessage.substring(idx).trim());
                LineNumberReader lnr = new LineNumberReader(new StringReader(script));
                String l;
                result = ".....\n";
                while ((l = lnr.readLine()) != null) {
                    if ((lnr.getLineNumber() >= (line - 2)) && (lnr.getLineNumber() <= (line + 2))) {
                        result += String.format("% 2d: %s\n", lnr.getLineNumber(), l);
                    }
                }
                result += ".....\n";
            }
        }
        catch (IOException e1) {
            LOG.error(e1);
        }
        catch (NumberFormatException e1) {
            // ignore
        }

        return ". Script:\n" + result;
    }
}
