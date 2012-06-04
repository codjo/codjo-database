package net.codjo.database.common.impl.script;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import net.codjo.util.file.FileUtil;
/**
 * Executes une commande. Une instance de <code>WindowsExec</code> peut être réutilisé pour executer plusieurs
 * commande de manière sequentiel.
 */
public class WindowsExec {
    private static final String NEW_LINE = System.getProperty("line.separator");
    private String errorMessage;
    private String processMessage;
    private String processInput = null;


    /**
     * Execute de manière synchrone la commande <code>command</code>, en recuperant les sorties du process
     * (ie. stdout et stderr).
     *
     * @param command          une commande
     * @param workingDirectory répertoire de travail
     *
     * @return Le code retour du process lancé pour la commande.
     */
    public int exec(String command, File workingDirectory) {
        if (command == null) {
            return buildError("Aucune commande specifiée");
        }

        Process process = null;
        try {
            process = Runtime.getRuntime().exec(command, null, workingDirectory);
            return checkProcess(process);
        }
        catch (Throwable t) {
            return buildError(t.getMessage());
        }
        finally {
            if (process != null) {
                process.destroy();
            }
        }
    }


    public int exec(String[] commands, File workingDirectory) {
        if (commands == null) {
            return buildError("Aucune commande specifiée");
        }

        try {
            Process process = Runtime.getRuntime().exec(commands, null, workingDirectory);
            return checkProcess(process);
        }
        catch (Throwable t) {
            return buildError(t.getMessage());
        }
    }


    private int buildError(String message) {
        setErrorMessage(message);
        return -1;
    }


    private int checkProcess(Process process) throws InterruptedException {
        setErrorMessage(null);
        setProcessMessage(null);

        writeProcessInput(process);
        readProcessMessages(process);

        int returnCode = process.exitValue();
        if (returnCode != 0) {
            StringBuffer errorMsg = new StringBuffer();
            if (!"".equals(getErrorMessage())) {
                errorMsg.append("Error message :").append(NEW_LINE).append(getErrorMessage());
            }
            errorMsg.append("Output message :").append(NEW_LINE).append(getProcessMessage());
            setErrorMessage(errorMsg.toString());
        }
        return returnCode;
    }


    public void setProcessInput(String processInput) {
        this.processInput = processInput;
    }


    /**
     * Recupère les sorties du process (ie. stdout et stderr).
     *
     * @param process Le Process
     *
     * @throws InterruptedException Erreur
     */
    private void readProcessMessages(Process process)
          throws InterruptedException {
        StreamReader errorReader = new StreamReader(process.getErrorStream());
        StreamReader outputReader = new StreamReader(process.getInputStream());

        errorReader.start();
        outputReader.start();

        process.waitFor();
        outputReader.waitReadFinished();
        errorReader.waitReadFinished();

        setErrorMessage(errorReader.getMessage());
        setProcessMessage(outputReader.getMessage());
    }


    /**
     * Retourne le message d'erreur de la derniere execution (stderr).
     *
     * @return La valeur de errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }


    /**
     * Retourne une chaine de caractère qui est la concatenation du flux stdout et stderr du process
     * saveParams.
     *
     * @return La valeur de outputMessage (not null)
     */
    public String getProcessMessage() {
        return processMessage;
    }


    private void writeProcessInput(Process process) {
        if (processInput == null) {
            return;
        }
        PrintStream printStream = new PrintStream(process.getOutputStream());
        printStream.println(processInput);
        printStream.flush();
        printStream.close();
    }


    /**
     * Positionne le message d'erreur de l'object WindowsExec.
     *
     * @param newErrorMessage La nouvelle valeur de errorMessage
     */
    private void setErrorMessage(String newErrorMessage) {
        if (newErrorMessage == null) {
            newErrorMessage = "";
        }

        errorMessage = newErrorMessage;
    }


    /**
     * Positionne l'attribut outputMessage.
     *
     * @param newProcessMessage La nouvelle valeur de processMessage
     */
    private void setProcessMessage(String newProcessMessage) {
        processMessage = newProcessMessage;
    }


    /**
     * Lecteur d'un flux.
     */
    private static class StreamReader extends Thread {
        private InputStream inputStream;
        private StringBuffer message = new StringBuffer();


        StreamReader(InputStream inputStream) {
            this.inputStream = inputStream;
        }


        public synchronized String getMessage() {
            return message.toString();
        }


        @Override
        public synchronized void run() {
            try {
                BufferedReader bufferedReader =
                      new BufferedReader(new InputStreamReader(inputStream));

                String messageContent = FileUtil.loadContent(bufferedReader);
                if (messageContent != null) {
                    message.append(messageContent);
                }
            }
            catch (IOException ioe) {
                message.append(ioe.toString());
            }

            notify();
        }


        private synchronized void waitReadFinished() {
            while (isAlive()) {
                try {
                    wait();
                }
                catch (InterruptedException ex) {
                    ;
                }
            }
        }
    }
}
