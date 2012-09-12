package net.codjo.database.common.impl.script;
import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.util.file.FileUtil;
import static net.codjo.test.common.PathUtil.normalize;

public abstract class AbstractExecSqlScript implements ExecSqlScript {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private final WindowsExec windowsExec = new WindowsExec();
    private ConnectionMetadata connectionMetadata;
    private Logger logger;


    public void setConnectionMetadata(ConnectionMetadata connectionMetadata) {
        this.connectionMetadata = connectionMetadata;
    }


    public void setLogger(Logger logger) {
        this.logger = logger;
    }


    public Logger getLogger() {
        return logger;
    }


    public void execute(String workingDirectory, String... scriptFileNames) {
        executeScripts(new File(workingDirectory), scriptFileNames);
    }

    public void execute(String workingDirectory, List<String> scriptFileNames) {
        execute(workingDirectory, scriptFileNames.toArray(new String[scriptFileNames.size()]));
    }

    public void executeContentOfFile(String deliveryFileName) {
        if (!new File(deliveryFileName).exists()) {
            throw new RuntimeException("Le fichier " + deliveryFileName + " est introuvable");
        }

        String[] scripts = getScriptsFrom(deliveryFileName);

        File rootDirectory = new File(deliveryFileName).getParentFile();
        executeScripts(rootDirectory, scripts);
    }

    protected final void executeScripts(File rootDirectory, String[] scripts) {
        for (int i = 0;  i < scripts.length; i++) {
            scripts[i] = normalize(scripts[i]);
        }
        assertAllFilesExist(rootDirectory, scripts);

        Connection connection = null; // can return null
        try {
            connection = prepareExecuteScripts();
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
        try {
            for (String scriptName : scripts) {
                File scriptFile = new File(rootDirectory, scriptName);
                String scriptContent;
                try {
                    scriptContent = FileUtil.loadContent(scriptFile);
                }
                catch (IOException e) {
                    throw new RuntimeException("Impossible de lire le fichier " + scriptFile.getAbsolutePath());
                }
                if (!"".equals(scriptName.trim()) && !"".equals(scriptContent)) {
                    executeScript(connection, scriptName, createSqlScriptCommand(scriptName), rootDirectory);
                }
            }
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    logger.log("ERROR : can't close connection : " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }
    }

    protected Connection prepareExecuteScripts() throws SQLException {
        return null;
    }

    public abstract String getQueryDelimiter();


    public abstract String getProcessInputMessage();


    public abstract String removeConnectionMessage(String processMessage);


    public abstract String createSqlScriptCommand(String scriptFileName);


    public abstract String getSqlErrorKeyWord();


    protected abstract String getScriptNotFoundErrorKeyWord();


    protected ConnectionMetadata getConnectionMetadata() {
        return connectionMetadata;
    }


    private String[] getScriptsFrom(String fileName) {
        String[] contentOfFile = FileUtil.loadContentAsLines(new File(fileName));
        if (contentOfFile.length == 0) {
            logger.log("Le fichier " + fileName + " est vide !");
        }
        return contentOfFile;
    }


    /**
     *
     * @param connection
     * @param scriptName
     * @param cmd <strong>unused</strong>
     * @param workingDirectory
     */
    protected void executeScript(Connection connection, String scriptName, String cmd, File workingDirectory) {
        StringBuilder sqlLog = new StringBuilder("Ex�cution du script : " + scriptName.trim() + NEW_LINE);

        String[] cmds = new String[]{"cmd.exe", "/c", cmd};

        windowsExec.setProcessInput(getProcessInputMessage());
        int returnCode = windowsExec.exec(cmds, workingDirectory);

        if (returnCode != 0) {
            sqlLog.append("Erreur lors de l'ex�cution de la commande.").append(NEW_LINE)
                  .append(windowsExec.getErrorMessage());
            throw new RuntimeException(sqlLog.toString());
        }

        String processMessage = windowsExec.getProcessMessage();
        processMessage = removeConnectionMessage(processMessage);

        String lowerCaseProcessMessage = processMessage.toLowerCase();
        String scriptNotFoundKeyWord = getScriptNotFoundErrorKeyWord();
        if (scriptNotFoundKeyWord != null && lowerCaseProcessMessage.contains(scriptNotFoundKeyWord)) {
            sqlLog.append("Erreur :").append(NEW_LINE).append(processMessage);
            throw new RuntimeException(sqlLog.toString());
        }
        if (lowerCaseProcessMessage.contains(getSqlErrorKeyWord())) {
            sqlLog.append("Erreur SQL :").append(NEW_LINE).append(processMessage);
            throw new RuntimeException(sqlLog.toString());
        }

        sqlLog.append(processMessage);
        logger.log(sqlLog.toString());
    }


    private void assertAllFilesExist(File rootDirectory, String[] scripts) {
        if (scripts.length == 0) {
            return;
        }

        StringBuilder filesNotFound = new StringBuilder();
        for (String script : scripts) {
            if (!new File(rootDirectory, script).exists()) {
                if (filesNotFound.length() == 0) {
                    filesNotFound.append("Les fichiers suivants sont introuvables :").append(NEW_LINE);
                }
                filesNotFound.append(">").append(script).append("<").append(NEW_LINE);
            }
        }

        String unexistingFiles = filesNotFound.toString();
        if (!"".equals(unexistingFiles.trim())) {
            throw new RuntimeException(unexistingFiles);
        }
    }
}
