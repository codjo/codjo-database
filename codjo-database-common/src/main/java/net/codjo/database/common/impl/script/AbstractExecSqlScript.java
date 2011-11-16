package net.codjo.database.common.impl.script;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.ExecSqlScript;
import net.codjo.database.common.impl.FileUtil;
import java.io.File;
import java.io.IOException;
import java.util.List;

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


    public void execute(String workingDirectory, String... scriptFileNames) {
        for (String scriptName : scriptFileNames) {
            if (!"".equals(scriptName.trim())) {
                executeScript(scriptName, createSqlScriptCommand(scriptName), new File(workingDirectory));
            }
        }
    }


    public void execute(String workingDirectory, List<String> scriptFileNames) {
        execute(workingDirectory, scriptFileNames.toArray(new String[0]));
    }


    public void executeContentOfFile(String deliveryFileName) {
        File applicationFile = new File(deliveryFileName);
        if (!applicationFile.exists()) {
            throw new RuntimeException("Le fichier " + deliveryFileName + " est introuvable");
        }

        String unexistingFiles = findUnexistingFilesIn(deliveryFileName);
        if (!"".equals(unexistingFiles.trim())) {
            throw new RuntimeException(unexistingFiles);
        }

        File workingDirectory = new File(deliveryFileName).getParentFile();
        String[] scripts = getScripts(deliveryFileName);
        for (String scriptName : scripts) {
            File scriptFile = new File(workingDirectory, scriptName);
            String scriptContent;
            try {
                scriptContent = FileUtil.loadContent(scriptFile);
            }
            catch (IOException e) {
                throw new RuntimeException("Impossible de lire le fichier " + scriptFile.getAbsolutePath());
            }
            if (!"".equals(scriptName.trim()) && !"".equals(scriptContent)) {
                executeScript(scriptName, createSqlScriptCommand(scriptName),
                              workingDirectory);
            }
        }
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


    private String[] getScripts(String deliveryFileName) {
        String contentOfFile;
        try {
            contentOfFile = FileUtil.loadContent(new File(deliveryFileName));
        }
        catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier " + deliveryFileName, e);
        }
        if ("".equals(contentOfFile)) {
            logger.log("Le fichier " + deliveryFileName + " est vide !");
            return new String[]{};
        }

        return contentOfFile.split(NEW_LINE);
    }


    private void executeScript(String scriptName, String cmd, File workingDirectory) {
        StringBuffer sqlLog = new StringBuffer("Exécution du script : " + scriptName.trim() + NEW_LINE);

        String[] cmds = new String[]{"cmd.exe", "/c", cmd};

        windowsExec.setProcessInput(getProcessInputMessage());
        int returnCode = windowsExec.exec(cmds, workingDirectory);

        if (returnCode != 0) {
            sqlLog.append("Erreur lors de l'exécution de la commande.").append(NEW_LINE)
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


    private String findUnexistingFilesIn(String filePath) {
        StringBuffer filesNotFound = new StringBuffer();

        File file = new File(filePath);

        String contentOfFile;
        try {
            contentOfFile = FileUtil.loadContent(file);
        }
        catch (IOException e) {
            throw new RuntimeException("Impossible de lire le fichier " + file.getAbsolutePath(), e);
        }

        if ("".equals(contentOfFile)) {
            return "";
        }
        String[] scripts = contentOfFile.split(NEW_LINE);
        for (String script : scripts) {
            File parentFile = file.getParentFile();
            if (!new File(parentFile, script).exists()) {
                if (filesNotFound.length() == 0) {
                    filesNotFound.append("Les fichiers suivants sont introuvables :").append(NEW_LINE);
                }
                filesNotFound.append(script.trim()).append(NEW_LINE);
            }
        }
        return filesNotFound.toString();
    }
}
