package net.codjo.database.common.api;
import java.util.List;
public interface ExecSqlScript {

    public void setConnectionMetadata(ConnectionMetadata connectionMetadata);


    void setLogger(Logger logger);


    public void executeContentOfFile(String deliveryFileName);


    public void execute(String workingDirectory, String... scriptFileNames);


    public void execute(String workingDirectory, List<String> scriptFileNames);


    interface Logger {
        public void log(String log);
    }
}
