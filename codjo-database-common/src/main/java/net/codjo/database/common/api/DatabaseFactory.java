package net.codjo.database.common.api;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.DatabaseRepository;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;
@SuppressWarnings({"OverlyCoupledClass"})
public class DatabaseFactory {
    private DatabaseRepository databaseRepository;


    public DatabaseFactory() {
        try {
            initDatabaseRepository();
        }
        catch (UnsupportedOperationException e) {
            throw e;
        }
        catch (Exception e) {
            throw new RuntimeException(
                  "Aucune base n'est configurée. Vérifiez votre dépendance vers agf-database-main", e);
        }
    }


    private void initDatabaseRepository()
          throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException {
        InputStream stream = getClass().getResourceAsStream("/META-INF/database-config.properties");
        Properties properties = new Properties();
        properties.load(stream);
        databaseRepository = (AbstractDatabaseRepository)Class
              .forName(properties.getProperty("net.codjo.database.common.repository.DatabaseRepository"))
              .newInstance();
    }


    public DatabaseHelper createDatabaseHelper() {
        return getImplementation(DatabaseHelper.class);
    }


    public DatabaseQueryHelper getDatabaseQueryHelper() {
        return getImplementation(DatabaseQueryHelper.class);
    }


    public DatabaseScriptHelper createDatabaseScriptHelper() {
        return getImplementation(DatabaseScriptHelper.class);
    }


    public ExecSqlScript createExecSqlScript() {
        return getImplementation(ExecSqlScriptBuilder.class).get();
    }


    public Relationship createRelationShip(Connection connection) throws SQLException {
        return getImplementation(RelationshipBuilder.class).get(connection);
    }


    public SQLFieldList createSQLFieldList(Connection connection,
                                           String catalog,
                                           String tableName) throws SQLException {
        return createSQLFieldList(connection, catalog, tableName, false);
    }


    public SQLFieldList createSQLFieldList(Connection connection,
                                           String catalog,
                                           String tableName,
                                           boolean temporaryTable) throws SQLException {
        return getImplementation(SQLFieldListBuilder.class)
              .get(connection, catalog, tableName, temporaryTable);
    }


    public JdbcFixture createJdbcFixture() throws SQLException {
        ConnectionMetadata connectionMetadata;
        DatabaseHelper databaseHelper = createDatabaseHelper();
        try {
            connectionMetadata = databaseHelper.createApplicationConnectionMetadata();
        }
        catch (Exception e) {
            connectionMetadata = databaseHelper.createLibraryConnectionMetadata();
        }
        return createJdbcFixture(connectionMetadata);
    }


    public JdbcFixture createJdbcFixture(ConnectionMetadata connectionMetadata) throws SQLException {
        return getImplementation(JdbcFixtureBuilder.class).get(connectionMetadata);
    }


    public DatabaseComparator createDatabaseComparator() {
        return getImplementation(DatabaseComparatorBuilder.class).get();
    }


    public TableComparator createTableComparator(Connection connection,
                                                 int columnToSkip,
                                                 String orderClause) throws SQLException {
        return getImplementation(TableComparatorBuilder.class).get(connection, columnToSkip, orderClause);
    }


    public DatabaseTranscoder createDatabaseTranscoder() {
        return getImplementation(DatabaseTranscoder.class);
    }


    protected <T> T getImplementation(Class<T> aClass) {
        return databaseRepository.getImplementation(aClass);
    }
}
