package net.codjo.database.common.impl.helper;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fieldValue;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractDatabaseHelperTest {
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected final DatabaseQueryHelper databaseQueryHelper = databaseFactory.getDatabaseQueryHelper();
    protected JdbcFixture jdbcFixture;


    protected abstract DatabaseHelper createDatabaseHelper() throws SQLException;


    protected abstract String buildDatabaseUrl(Properties databaseProperties);


    protected abstract void assertLibraryConnectionMetadata(ConnectionMetadata connectionMetadata)
          throws IOException;


    @Before
    public void setUp() throws Exception {
        jdbcFixture = createJdbcFixture();
        jdbcFixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_createConnection() throws Exception {
        Properties properties = getConnectionProperties();
        ConnectionMetadata connectionMetadata = new ConnectionMetadata();
        connectionMetadata.setHostname(properties.getProperty("database.hostname"));
        connectionMetadata.setPort(properties.getProperty("database.port"));
        connectionMetadata.setUser(properties.getProperty("database.user"));
        connectionMetadata.setPassword(properties.getProperty("database.password"));
        connectionMetadata.setCatalog(properties.getProperty("database.catalog"));

        Connection connection = databaseHelper.createConnection(connectionMetadata);

        assertNotNull(connection);
        assertEquals(buildDatabaseUrl(properties), connection.getMetaData().getURL());
    }


    @Test(expected = SQLException.class)
    public void test_truncateTable_noTable() throws Exception {
        databaseHelper.truncateTable(jdbcFixture.getConnection(), table("NO_TABLE"));
    }


    @Test
    public void test_truncateTable() throws Exception {
        SqlTable table = table("MY_TABLE");
        createFillAndTruncateTable(table);
        jdbcFixture.assertContent(table, null);
    }


    @Test
    public void test_truncateTable_temporaryTable() throws Exception {
        SqlTable table = temporaryTable("MY_TABLE");
        createFillAndTruncateTable(table);
        jdbcFixture.assertContent(table, null);
    }


    @Test(expected = SQLException.class)
    public void test_dropForeignKey_noFK() throws Exception {
        SqlTable table = table("JDBC_TEST_1");
        jdbcFixture.create(table, "COL_STR varchar(255)");
        databaseHelper.dropForeignKey(jdbcFixture.getConnection(),
                                      foreignKey("FK_UNKNOWN", table));
    }


    @Test
    public void test_buildTablesOrder() throws Exception {
        jdbcFixture.create(table("JDBC_TEST_1"), "COL_STR varchar(255)");
        jdbcFixture.create(table("JDBC_TEST_2"), "COL_STR varchar(255)");
        jdbcFixture.create(table("JDBC_TEST_3"), "COL_STR varchar(255)");

        Map<String, List<String>> relations = new HashMap<String, List<String>>();
        relations.put("JDBC_TEST_1", Arrays.asList("JDBC_TEST_2", "JDBC_TEST_3"));
        relations.put("JDBC_TEST_3", Arrays.asList("JDBC_TEST_2"));
        List<String> tablesOrder = databaseHelper.buildTablesOrder(jdbcFixture.getConnection(), relations);

        assertEquals("JDBC_TEST_1", tablesOrder.get(0).toUpperCase());
        assertEquals("JDBC_TEST_3", tablesOrder.get(1).toUpperCase());
        assertEquals("JDBC_TEST_2", tablesOrder.get(2).toUpperCase());
    }


    @Test
    public void test_createApplicationConnectionMetadata() throws Exception {
        File databasePropertiesFile = createApplicationDatabaseProperties();
        try {
            ConnectionMetadata connectionMetadata = databaseHelper.createApplicationConnectionMetadata();

            assertEquals("databaseBase", connectionMetadata.getBase());
            assertEquals("databaseCatalog", connectionMetadata.getCatalog());
            assertEquals("databaseCharset", connectionMetadata.getCharset());
            assertEquals("databaseHostname", connectionMetadata.getHostname());
            assertEquals("databasePassword", connectionMetadata.getPassword());
            assertEquals("databasePort", connectionMetadata.getPort());
            assertEquals("databaseSchema", connectionMetadata.getSchema());
            assertEquals("databaseUser", connectionMetadata.getUser());
        }
        finally {
            databasePropertiesFile.delete();
        }
    }


    @Test(expected = RuntimeException.class)
    public void test_createApplicationConnectionMetadata_noProperties() throws Exception {
        databaseHelper.createApplicationConnectionMetadata();
    }


    @Test
    public void test_createLibraryConnectionMetadata() throws Exception {
        assertLibraryConnectionMetadata(databaseHelper.createLibraryConnectionMetadata());
    }


    protected final JdbcFixture createJdbcFixture() throws Exception {
        Properties databaseProperties = new Properties();
        databaseProperties.load(getClass().getResourceAsStream("/database-integration.properties"));
        ConnectionMetadata connectionMetadata = new ConnectionMetadata(databaseProperties);
        return JdbcFixture.newFixture(connectionMetadata);
    }


    protected Properties getConnectionProperties() throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/database-integration.properties"));
        return properties;
    }


    private void createFillAndTruncateTable(SqlTable table) throws SQLException {
        jdbcFixture.create(table, "MY_FIELD_1 int null");
        jdbcFixture.advanced().assertExists(table);

        jdbcFixture.executeUpdate(databaseQueryHelper.buildInsertQuery(table, fieldValue(1)));
        jdbcFixture.assertContent(table, new String[][]{{"1"},});

        databaseHelper.truncateTable(jdbcFixture.getConnection(), table);
    }


    private File createApplicationDatabaseProperties() throws IOException {
        Properties properties = new Properties();
        properties.setProperty("database.hostname", "databaseHostname");
        properties.setProperty("database.port", "databasePort");
        properties.setProperty("database.user", "databaseUser");
        properties.setProperty("database.password", "databasePassword");
        properties.setProperty("database.base", "databaseBase");
        properties.setProperty("database.catalog", "databaseCatalog");
        properties.setProperty("database.schema", "databaseSchema");
        properties.setProperty("database.charset", "databaseCharset");
        File databasePropertiesFile = new File(getClass().getResource("/").getFile(), "database.properties");
        FileOutputStream fileOutputStream = new FileOutputStream(databasePropertiesFile);
        try {
            properties.store(fileOutputStream, null);
        }
        finally {
            fileOutputStream.close();
        }
        return databasePropertiesFile;
    }
}

