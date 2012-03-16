package net.codjo.database.common.api;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.sql.ResultSet;
import java.sql.SQLException;
import junit.framework.AssertionFailedError;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlIndex.normalIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static net.codjo.database.common.api.structure.SqlView.view;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public abstract class JdbcFixtureTest {
    protected DirectoryFixture directoryFixture = DirectoryFixture.newTemporaryDirectoryFixture();
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected final DatabaseQueryHelper queryHelper = databaseFactory.getDatabaseQueryHelper();
    protected final DatabaseScriptHelper scriptHelper = databaseFactory.createDatabaseScriptHelper();
    protected JdbcFixture jdbcFixture;


    @Before
    public void setUp() throws Exception {
        jdbcFixture = JdbcFixture.newFixture(databaseHelper.createLibraryConnectionMetadata());
        jdbcFixture.doSetUp();
        directoryFixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
        directoryFixture.doTearDown();
    }


    @Test
    public void test_getConnection() {
        assertNotNull(jdbcFixture.getConnection());
    }


    @Test(expected = RuntimeSqlException.class)
    public void test_doTeardown_onClosedConnection() throws Exception {
        JdbcFixture anotherJdbcFixture = JdbcFixture
              .newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.getConnection().close();
        anotherJdbcFixture.doTearDown();
    }


    @Test
    public void test_executeCreateTable() throws Exception {
        jdbcFixture.create(table("JDBC_TEST_1"), "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.advanced().assertExists(table("JDBC_TEST_1"));

        jdbcFixture.doTearDown();

        JdbcFixture anotherJdbcFixture = JdbcFixture
              .newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.advanced().assertDoesntExist(table("JDBC_TEST_1"));
        anotherJdbcFixture.doTearDown();
    }


    @Test
    public void test_executeCreateTemporaryTable() throws Exception {
        SqlTable table = temporaryTable("JDBC_TEST_1");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.advanced().assertExists(table);

        jdbcFixture.doTearDown();

        JdbcFixture anotherJdbcFixture = JdbcFixture
              .newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.advanced().assertDoesntExist(table);
        anotherJdbcFixture.doTearDown();
    }


    @Test
    public void test_executeUpdate() throws SQLException {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('3','4')");

        String[][] content = {
              {"1", "2"},
              {"3", "4"}
        };
        jdbcFixture.assertContent(table, content);
    }


    @Test
    public void test_assertContent_failingDueToRowCountMismatch() throws SQLException {
        jdbcFixture.create(table("JDBC_FIXTURE_TEST"), "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('3','4')");

        try {
            jdbcFixture.assertContent(table("JDBC_FIXTURE_TEST"), new String[][]{{"1", "2"}});
        }
        catch (AssertionFailedError ex) {
            assertThat(ex.getMessage(), containsString("Total row count expected:<1> but was:<2>"));
            return; // Ok
        }
        fail();
    }


    @Test
    public void test_assertContent_failingDueToColumnCountMismatch() throws SQLException {
        jdbcFixture.create(table("JDBC_FIXTURE_TEST"), "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");

        try {
            jdbcFixture.assertContent(table("JDBC_FIXTURE_TEST"), new String[][]{{"1"}});
        }
        catch (AssertionFailedError ex) {
            assertThat(ex.getMessage(), containsString("On row[1] column count expected:<1> but was:<2>"));
            return; // Ok
        }
        fail();
    }


    @Test
    public void test_assertQueryResult() throws SQLException {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('3','4')");

        String[][] string = {
              {"1", "2"},
              {"3", "4"}
        };
        jdbcFixture.assertQueryResult("select * from JDBC_FIXTURE_TEST", string);
    }


    @Test
    public void test_executeQuery() throws SQLException {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");

        ResultSet rs = jdbcFixture.executeQuery("select * from JDBC_FIXTURE_TEST");

        assertTrue(rs.next());
        assertEquals("1", rs.getString(1));
        assertEquals("2", rs.getString(2));
    }


    @Test
    public void test_dropTable() throws Exception {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.drop(table);

        jdbcFixture.advanced().assertDoesntExist(table);
    }


    @Test
    public void test_dropTable_badTableName() throws Exception {
        jdbcFixture.drop(table("TEST_TABLE"));
    }


    @Test
    public void test_dropTable_temporaryTable() throws Exception {
        SqlTable table = temporaryTable("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.advanced().assertExists(table);

        jdbcFixture.drop(table);

        jdbcFixture.advanced().assertDoesntExist(table);
    }


    @Test
    public void test_deleteTable() throws Exception {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('a', 'b')");

        jdbcFixture.delete(table);

        jdbcFixture.assertIsEmpty(table);
    }


    @Test
    public void test_assertTableDoNotExist() throws Exception {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.advanced().assertDoesntExist(table);
        jdbcFixture.create(table, "COL1 varchar(5)");

        try {
            jdbcFixture.advanced().assertDoesntExist(table);
            fail("La table existe !");
        }
        catch (AssertionFailedError e) {
            assertEquals("Table 'JDBC_FIXTURE_TEST' exists", e.getMessage());
        }
    }


    @Test
    public void test_assertTableExist() throws Exception {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        try {
            jdbcFixture.advanced().assertExists(table);
            fail("La table n'existe pas !");
        }
        catch (AssertionFailedError e) {
            assertEquals("Table 'JDBC_FIXTURE_TEST' doesn't exist", e.getMessage());
        }
        jdbcFixture.create(table, "COL1 varchar(5)");

        jdbcFixture.advanced().assertExists(table);
    }


    @Test
    public void test_assertIndexExists() throws Exception {
        SqlTable table = table("DEPARTMENT");
        SqlIndex index = normalIndex("X1_DEPARTMENT", table);

        jdbcFixture.create(table, "DEPARTMENT_ID varchar(255)");

        try {
            jdbcFixture.advanced().assertExists(index);
            fail("AssertionFailedError attendue.");
        }
        catch (AssertionFailedError e) {
            // Rien à faire.
        }

        jdbcFixture.executeUpdate("create unique index X1_DEPARTMENT on DEPARTMENT(DEPARTMENT_ID)");

        jdbcFixture.advanced().assertExists(index);
    }


    @Test
    public void test_executeQuery_emptyTable() throws SQLException {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.assertContent(table, null);
    }


    @Test(expected = SQLException.class)
    public void test_executeQuery_withError() throws SQLException {
        jdbcFixture.executeUpdate("select * from");
    }


    @Test
    public void test_deleteAllTables_noTables() throws SQLException {
        jdbcFixture.deleteAllTables();
    }


    @Test
    public void test_deleteAllTables_oneTable_noFather() throws SQLException {
        SqlTable table = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table, "COL_A varchar(5)");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(table);
    }


    @Test
    public void test_deleteAllTables_twoTables_noFather() throws SQLException {
        SqlTable table1 = table("JDBC_FIXTURE_TEST");
        jdbcFixture.create(table1, "COL_A varchar(5)");
        SqlTable table2 = table("JDBC_TEST_1");
        jdbcFixture.create(table2, "COL_A varchar(5)");
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(table1);
        jdbcFixture.assertIsEmpty(table2);
    }


    @Test
    public void test_executeCreateTableScriptFile() throws Exception {
        SqlTable table = table("AP_TOTO");
        SqlIndex index = SqlIndex.uniqueIndex("X1_AP_TOTO", table, fieldName("COL_STR"));
        File indexScript = new File(directoryFixture, "X1_AP_TOTO.sql");
        BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(indexScript));
        try {
            bufferedWriter.write(scriptHelper.buildCreateIndexScript(index));
        }
        finally {
            bufferedWriter.close();
        }
        jdbcFixture.create(table, "COL_STR varchar(255)");

        jdbcFixture.advanced().executeCreateTableScriptFile(indexScript);

        jdbcFixture.advanced().assertExists(index);
    }


    @Test
    public void test_assertObjectExists_table() throws Exception {
        SqlTable table = table("AP_EMPLOYEE");

        try {
            jdbcFixture.advanced().assertObjectExists(table.getName(), ObjectType.TABLE);
            fail("AssertionFailedError attendue.");
        }
        catch (AssertionFailedError e) {
            ;
        }

        jdbcFixture.create(table, "NAME varchar(255)");
        jdbcFixture.advanced().assertObjectExists(table.getName(), ObjectType.TABLE);
    }


    @Test
    public void test_assertObjectExists_view() throws Exception {
        SqlView view = view("VU_AP_EMPLOYE", "select * from AP_EMPLOYE");

        try {
            jdbcFixture.advanced().assertObjectExists(view.getName(), ObjectType.VIEW);
            fail("AssertionFailedError attendue.");
        }
        catch (AssertionFailedError e) {
            ;
        }

        jdbcFixture.create(SqlTable.table("AP_EMPLOYE"), "NAME varchar(255)");

        String createViewScript = scriptHelper.buildCreateViewScript(view);
        File createViewScriptFile = new File(directoryFixture, "create_vu_EMPLOYE.sql");
        FileUtil.saveContent(createViewScriptFile, createViewScript);

        try {
            jdbcFixture.advanced().executeCreateTableScriptFile(createViewScriptFile);
            jdbcFixture.advanced().assertObjectExists(view.getName(), ObjectType.VIEW);
        }
        finally {
            try {
                jdbcFixture.executeUpdate("drop view VU_AP_EMPLOYE");
            }
            catch (SQLException e) {
                ;
            }
        }
    }


    @Test
    public void test_assertObjectExists_trigger() throws Exception {
        SqlTrigger trigger = SqlTrigger.insertTrigger("TR_AP_EMPLOYE", table("AP_EMPLOYE"), "");

        try {
            jdbcFixture.advanced().assertObjectExists(trigger.getName(), ObjectType.TRIGGER);
            fail("AssertionFailedError attendue.");
        }
        catch (AssertionFailedError e) {
            ;
        }

        jdbcFixture.create(SqlTable.table("AP_EMPLOYE"), "NAME varchar(255)");

        String triggerScript = scriptHelper.buildCreateTriggerScript(trigger);
        File triggerScriptFile = new File(directoryFixture, "tr_AP_EMPLOYE.sql");
        FileUtil.saveContent(triggerScriptFile, triggerScript);

        jdbcFixture.advanced().executeCreateTableScriptFile(triggerScriptFile);
        jdbcFixture.advanced().assertObjectExists(trigger.getName(), ObjectType.TRIGGER);
    }
}
