package net.codjo.database.common.impl.helper;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.impl.fixture.MapOrderListTransformer;

import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
public abstract class AbstractDatabaseHelper implements DatabaseHelper {
    private final DatabaseQueryHelper queryHelper;
    private Properties databaseProperties;


    protected AbstractDatabaseHelper(DatabaseQueryHelper queryHelper) {
        this.queryHelper = queryHelper;
    }


    public String getDriverClassName() {
        return getDatabaseProperties().getProperty("database.driver");
    }


    public ConnectionMetadata createApplicationConnectionMetadata() {
        return createConnectionMetadataFromProperties("/database.properties", "/tokio.properties");
    }


    public ConnectionMetadata createLibraryConnectionMetadata() {
        return createConnectionMetadataFromProperties("/database-integration.properties");
    }


    public final Connection createConnection(ConnectionMetadata connectionMetadata) throws SQLException {
        String driver = getDriverClassName();
        try {
            Class.forName(driver);
        }
        catch (ClassNotFoundException e) {
            throw new RuntimeException("Impossible de charger le driver " + driver);
        }

        Properties connectionProperties = new Properties();
        connectionProperties.put("user", connectionMetadata.getUser());
        connectionProperties.put("password", connectionMetadata.getPassword());
        if (connectionMetadata.getCharset() != null) {
            connectionProperties.put("CHARSET", connectionMetadata.getCharset());
        }
        configureConnectionProperties(connectionProperties);
        Connection connection = DriverManager
              .getConnection(getConnectionUrl(connectionMetadata), connectionProperties);
        connection.setCatalog(connectionMetadata.getCatalog());
        return connection;
    }


    public boolean isIdentityInsertAllowed() {
        return false;
    }


    public void setIdentityInsert(Connection connection,
                                  String catalog,
                                  String tableName,
                                  boolean identityInsert) throws SQLException {
        setIdentityInsert(connection, catalog, tableName, false, identityInsert);
    }


    public void setIdentityInsert(Connection connection,
                                  String catalog,
                                  String tableName,
                                  boolean temporaryTable,
                                  boolean identityInsert) throws SQLException {
        throw new UnsupportedOperationException(
              "setIdentityInsert is not allowed with current database type");
    }


    public void truncateTable(Connection connection, String tableName) throws SQLException {
        truncateTable(connection, table(tableName));
    }


    @Deprecated
    public void truncateTable(Connection connection, String tableName, boolean temporaryTable)
          throws SQLException {
        truncateTable(connection, temporaryTable ?
                                  temporaryTable(tableName) :
                                  table(tableName));
    }


    public List<String> buildTablesOrder(Connection connection,
                                         Map<String, List<String>> relations) throws SQLException {

        List<String> list = new LinkedList<String>(getAllTables(connection));
        if (list.isEmpty()) {
            return new ArrayList<String>();
        }
        new MapOrderListTransformer(relations).transform(list);
        return new ArrayList<String>(list);
    }


    @Deprecated
    public void dropForeignKey(Connection connection,
                               String tableName,
                               String foreignKeyName) throws SQLException {
        dropForeignKey(connection, foreignKey(foreignKeyName, table(tableName)));
    }


    public void dropForeignKey(Connection connection, SqlConstraint constraint) throws SQLException {
        executeUpdate(connection, queryHelper.buildDropConstraintQuery(constraint));
    }


    public List<String> buildTablesOrder(Connection connection,
                                         Map<String, List<String>> relations,
                                         List<String> tablesName) throws SQLException {
        new MapOrderListTransformer(relations).transform(tablesName);
        return new ArrayList<String>(tablesName);
    }


    protected abstract String getSelectAllFksQuery(Connection connection) throws SQLException;


    protected List<String> getAllTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<String>();
        ResultSet resultSet = connection.getMetaData().getTables(null, null, "%", new String[]{"TABLE"});
        try {
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME"));
            }
        }
        finally {
            resultSet.close();
        }
        return tables;
    }


    protected void dropAllForeignKeys(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            ResultSet resultSet = statement.executeQuery(getSelectAllFksQuery(connection));
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                String constraintName = resultSet.getString("CONSTRAINT_NAME");
                try {
                    dropForeignKey(connection, foreignKey(constraintName, table(tableName)));
                }
                catch (SQLException e) {
                    SQLException sqlException =
                          new SQLException("Unable to drop foreign Key " + tableName + "." + constraintName + " - "
                                           + e.getMessage());
                    sqlException.initCause(e);
                    throw sqlException;
                }
            }
        }
        finally {
            statement.close();
        }
    }


    protected Properties getDatabaseProperties() {
        if (databaseProperties == null) {
            databaseProperties = new Properties();
            try {
                databaseProperties.load(getClass().getResourceAsStream("/database-integration.properties"));
            }
            catch (IOException e) {
                throw new RuntimeException("/database.properties est introuvable");
            }
        }
        return databaseProperties;
    }


    protected void configureConnectionProperties(Properties connectionProperties) {
    }


    protected void executeUpdate(Connection connection, String sql) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate(sql);
        }
        finally {
            statement.close();
        }
    }


    private ConnectionMetadata createConnectionMetadataFromProperties(String... propertiesFiles) {
        Properties properties = new Properties();
        InputStream inputStream;
        for (String propertiesFile : propertiesFiles) {
            inputStream = getClass().getResourceAsStream(propertiesFile);
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                    return new ConnectionMetadata(properties);
                }
                catch (IOException e) {
                    throw new RuntimeException(
                          "Le format de fichier de paramétrage de la base est incorrect");
                }
                finally {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e) {
                        // Rien à faire.
                    }
                }
            }
        }
        throw new RuntimeException("Aucun fichier de paramétrage de la base n'a été trouvé");
    }
}
