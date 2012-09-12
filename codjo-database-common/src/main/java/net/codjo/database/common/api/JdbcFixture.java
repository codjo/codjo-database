package net.codjo.database.common.api;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import junit.framework.Assert;
import net.codjo.database.common.api.DatabaseQueryHelper.SelectType;
import net.codjo.database.common.api.structure.SqlObject;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.fixture.MapOrderListTransformer;
import net.codjo.test.common.fixture.Fixture;

import static junit.framework.Assert.assertEquals;
import static net.codjo.database.common.api.structure.SqlTable.table;
public abstract class JdbcFixture implements Fixture {
    private final DatabaseFactory databaseFactory = new DatabaseFactory();
    private final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    private final DatabaseQueryHelper queryHelper = databaseFactory.getDatabaseQueryHelper();

    private final List<SqlTable> createdTables = new ArrayList<SqlTable>();
    private final ConnectionMetadata connectionMetadata;
    private JdbcFixtureAdvanced advanced;
    private JdbcFixtureUtil util;
    private Connection connection;


    protected JdbcFixture(ConnectionMetadata connectionMetadata) {
        this.connectionMetadata = connectionMetadata;
    }


    public static JdbcFixture newFixture() {
        try {
            return new DatabaseFactory().createJdbcFixture();
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public static JdbcFixture newFixture(ConnectionMetadata connectionMetadata) {
        try {
            return new DatabaseFactory().createJdbcFixture(connectionMetadata);
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public JdbcFixtureAdvanced advanced() {
        if (advanced == null) {
            advanced = newJdbcFixtureAdvanced();
        }
        return advanced;
    }


    public JdbcFixtureUtil util() {
        if (util == null) {
            util = newJdbcFixtureUtil();
        }
        return util;
    }


    public Connection getConnection() {
        return connection;
    }


    public void executeUpdate(String sql) throws SQLException {
        Statement statement = getConnection().createStatement();
        try {
            statement.executeUpdate(sql);
        }
        finally {
            statement.close();
        }
    }


    public ResultSet executeQuery(String sql) throws SQLException {
        return getConnection().createStatement().executeQuery(sql);
    }


    public void create(SqlTable table, String tableContent) {
        createdTables.add(table);
        String create = queryHelper.buildCreateTableQuery(table, tableContent);
        try {
            executeUpdate(create);
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public void delete(SqlTable table) {
        try {
            Statement statement = getConnection().createStatement();
            try {
                statement.executeUpdate("delete from " + table.getName());
            }
            finally {
                statement.close();
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    protected List<String> getAllTables() {
        try {
            Connection con = getConnection();
            Relationship relationship = databaseFactory.createRelationShip(con);
            DatabaseMetaData metaData = con.getMetaData();

            ResultSet resultSet = metaData.getTables(getConnectionMetadata().getCatalog(),
                                                     getConnectionMetadata().getSchema(),
                                                     "%",
                                                     new String[]{"TABLE"});

            List<String> list = new LinkedList<String>();
            while (resultSet.next()) {
                list.add(resultSet.getString("TABLE_NAME"));
            }

            return list;
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }

    public void deleteAllTables() {
        try {
            Connection con = getConnection();
            Relationship relationship = databaseFactory.createRelationShip(con);
            List<String> list = getAllTables();

            if (list.isEmpty()) {
                return;
            }

            new MapOrderListTransformer(relationship.getSonToFather()).transform(list);
            for (String tableName : list) {
                databaseHelper.truncateTable(con, table(tableName));
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public void drop(SqlTable table) {
        try {
            executeUpdate(queryHelper.buildDropTableQuery(table));
        }
        catch (Exception e) {
            ;
        }
    }


    public void assertIsEmpty(SqlTable table) {
        assertContent(table, null);
    }


    public void assertContent(SqlTable table, String[][] expectedValue) {
        assertQueryResult(queryHelper.buildSelectQuery(table, SelectType.ALL), expectedValue);
    }


    public void assertQueryResult(String sqlQuery, String[][] expectedValue) {
        try {
            Statement statement = getConnection().createStatement();

            String[][] actualValue = getResult(statement, sqlQuery);
            if ((expectedValue == null) && (actualValue == null)) {
                // expected empty and actual result is also empty => OK
                return;
            }
            if ((expectedValue != null) && (actualValue == null)) {
                Assert.assertNull("Expected not empty but it was", expectedValue);
                return;
            }
            if ((expectedValue == null) && (actualValue != null)) {
                Assert.assertNull("Expected empty but was not", actualValue);
                return;
            }
            int columnCount = expectedValue[0].length;

            for (int rowIndex = 0; rowIndex < expectedValue.length; rowIndex++) {
                for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                    assertEquals(expectedValue[rowIndex][columnIndex], actualValue[rowIndex][columnIndex]);
                }
                Assert.assertEquals("On row[" + (rowIndex + 1) + "] column count",
                                    expectedValue[rowIndex].length, actualValue[rowIndex].length);
            }
            Assert.assertEquals("Total row count", expectedValue.length, actualValue.length);
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public void doSetUp() {
        try {
            if (connection == null) {
                connection = databaseHelper.createConnection(connectionMetadata);
            }

            advanced().doSetUp();
            util().doSetUp();
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public void doTearDown() {
        try {
            if (connection != null && connection.isClosed()) {
                throw new SQLException("La connexion a déjà été arrêtée.");
            }

            if (connection == null) {
                return;
            }

            if (!connection.getAutoCommit()) {
                connection.rollback();
                connection.setAutoCommit(true);
            }

            advanced().doTearDown();
            util().doTearDown();

            try {
                for (SqlTable table : createdTables) {
                    try {
                        drop(table);
                    }
                    catch (Exception e) {
                        ;
                    }
                }
            }
            finally {
                connection.close();
                connection = null;
                createdTables.clear();
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public <T extends SqlObject> void assertThat(T actual, SqlMatcher<T> matcher) {
        matcher.setSqlFixture(this);
        assertThat(actual, matcher);
    }


    protected abstract JdbcFixtureAdvanced newJdbcFixtureAdvanced();


    protected abstract JdbcFixtureUtil newJdbcFixtureUtil();


    protected ConnectionMetadata getConnectionMetadata() {
        return connectionMetadata;
    }


    private static String[][] getResult(Statement stmt, String sql) throws SQLException {
        ResultSet resultSet = stmt.executeQuery(sql);
        return dumpResultSet(resultSet);
    }


    private static String[][] dumpResultSet(ResultSet rs) throws SQLException {
        List<String[]> resultContent = new ArrayList<String[]>();
        int columnCount = rs.getMetaData().getColumnCount();

        while (rs.next()) {
            String[] resultLine = new String[columnCount];
            for (int columnIndex = 0; columnIndex < columnCount; columnIndex++) {
                resultLine[columnIndex] = rs.getString(columnIndex + 1);
            }
            resultContent.add(resultLine);
        }
        if (resultContent.isEmpty()) {
            return null;
        }
        //noinspection ToArrayCallWithZeroLengthArrayArgument
        return resultContent.toArray(new String[][]{});
    }
}
