package net.codjo.database.common.api;
import java.util.Properties;
public class ConnectionMetadata {
    private String hostname;
    private String port;
    private String user;
    private String password;
    private String base;
    private String catalog;
    private String schema;
    private String charset;


    public ConnectionMetadata() {
    }


    public ConnectionMetadata(Properties databaseProperties) {
        this.hostname = databaseProperties.getProperty("database.hostname");
        this.port = databaseProperties.getProperty("database.port");
        this.user = databaseProperties.getProperty("database.user");
        this.password = databaseProperties.getProperty("database.password");
        this.base = databaseProperties.getProperty("database.base");
        this.catalog = databaseProperties.getProperty("database.catalog");
        this.schema = databaseProperties.getProperty("database.schema");
        this.charset = databaseProperties.getProperty("database.charset");

        if (user == null) {
            loadFromTokio(databaseProperties);
        }
        if (user == null) {
            loadFromJdbc(databaseProperties);
        }
        if (user == null) {
            loadFromServerConfig(databaseProperties);
        }
    }


    private void loadFromServerConfig(Properties databaseProperties) {
        loadFromProperties(databaseProperties, "");
    }


    public String getHostname() {
        return hostname;
    }


    public void setHostname(String hostname) {
        this.hostname = hostname;
    }


    public String getPort() {
        return port;
    }


    public void setPort(String port) {
        this.port = port;
    }


    public String getUser() {
        return user;
    }


    public void setUser(String user) {
        this.user = user;
    }


    public String getPassword() {
        return password;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public String getBase() {
        return base;
    }


    public void setBase(String base) {
        this.base = base;
    }


    public String getCatalog() {
        return catalog;
    }


    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    public String getSchema() {
        return schema;
    }


    public void setSchema(String schema) {
        this.schema = schema;
    }


    public String getCharset() {
        return charset;
    }


    public void setCharset(String charset) {
        this.charset = charset;
    }


    private void loadFromTokio(Properties databaseProperties) {
        loadFromProperties(databaseProperties, "tokio.jdbc");
    }


    private void loadFromJdbc(Properties databaseProperties) {
        loadFromProperties(databaseProperties, "jdbc");
    }


    private void loadFromProperties(Properties databaseProperties, String prefix) {
        String smartPrefix = prefix;
        if (prefix.length() != 0) {
            smartPrefix += ".";
        }

        String server = databaseProperties.getProperty(smartPrefix + "server");
        if (server != null) {
            String[] tokens = server.split(":");
            if (tokens.length >= 2) {
                hostname = tokens[tokens.length - 2];
                port = tokens[tokens.length - 1];
            }
            else {
                hostname = null;
                port = null;
            }
        }
        user = databaseProperties.getProperty(smartPrefix + "user");
        password = databaseProperties.getProperty(smartPrefix + "pwd");
        catalog = databaseProperties.getProperty(smartPrefix + "catalog");
        base = databaseProperties.getProperty(smartPrefix + "base");
        charset = databaseProperties.getProperty(smartPrefix + "charset");
        schema = null;
    }
}
