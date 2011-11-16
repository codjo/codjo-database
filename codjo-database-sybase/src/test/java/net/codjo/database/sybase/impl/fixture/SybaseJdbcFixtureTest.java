package net.codjo.database.sybase.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.database.common.api.JdbcFixture.newFixture;
import net.codjo.database.common.api.JdbcFixtureTest;
import net.codjo.database.common.api.structure.SqlConstraint;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fields;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import junit.framework.AssertionFailedError;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
public class SybaseJdbcFixtureTest extends JdbcFixtureTest {

    @Test
    public void test_deleteAllTables_twoTables_oneFather() throws SQLException {
        SqlTable table1 = table("JDBC_FIXTURE_TEST");
        SqlTable table2 = table("JDBC_TEST_1");
        jdbcFixture.create(table1, "COL_A varchar(5)");
        jdbcFixture.create(table2, "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        SqlConstraint foreignKey = foreignKey("FK_1",
                                              table2,
                                              fields("COL_A"),
                                              table1,
                                              fields("COL_A"));
        jdbcFixture.advanced().create(foreignKey);
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(table1);
        jdbcFixture.assertIsEmpty(table2);
    }


    @Test
    public void test_deleteAllTables_threeTables_sameFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"),
                                                 fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"),
                                                 fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"),
                                                 fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"),
                                                 fields("COL_A")));
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_2 values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_FIXTURE_TEST"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_1"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_2"));
    }


    @Test
    public void test_deleteAllTables_threeTables_twoFathers() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_TEST_1 on JDBC_TEST_1 (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"),
                                                 fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"),
                                                 fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"),
                                                 fields("COL_A"),
                                                 table("JDBC_TEST_1"),
                                                 fields("COL_A")));
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_2 values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_FIXTURE_TEST"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_1"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_2"));
    }


    @Test
    public void test_executeCreateForeignKey() throws Exception {
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        jdbcFixture.doTearDown();

        JdbcFixture anotherJdbcFixture = newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_TEST_1");
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_FIXTURE_TEST");
        anotherJdbcFixture.doTearDown();
    }


    @Test
    public void test_assertConstraintExists() throws Exception {
        SqlTable parentTable = table("DEPARTMENT");
        SqlTable childTable = table("EMPLOYEE");

        jdbcFixture.create(parentTable, "DEPARTMENT_ID varchar(255)");
        jdbcFixture.create(childTable,
                           "FIELD varchar(255), REFERENCE_TO_DEPARTMENT_ID varchar(255)");

        try {
            jdbcFixture.advanced().assertExists(foreignKey("FK_DEPARTMENT_ID", childTable));
            fail("AssertionFailedError attendue.");
        }
        catch (AssertionFailedError e) {
            // Rien à faire.
        }

        jdbcFixture
              .executeUpdate("create unique clustered index X1_DEPARTMENT on DEPARTMENT(DEPARTMENT_ID)");
        SqlConstraint foreignKey = foreignKey("FK_DEPARTMENT_ID",
                                              childTable,
                                              fields("REFERENCE_TO_DEPARTMENT_ID"),
                                              parentTable,
                                              fields("DEPARTMENT_ID"));
        jdbcFixture.advanced().create(foreignKey);

        jdbcFixture.advanced().assertExists(foreignKey);
    }


    @Test
    public void test_spoolQuery() throws Exception {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        jdbcFixture.util().spoolQueryResult("select COL1 from JDBC_FIXTURE_TEST", new PrintStream(result));

        assertEquals("******************** SPOOL ********************"
                     + "**  query = select COL1 from JDBC_FIXTURE_TEST"
                     + "\tCOL1"
                     + "\tvarchar"
                     + "\t1",
                     result.toString().replaceAll("\\n|\\r", ""));
    }


    @Test
    public void test_assertUserInGroup() throws Exception {
        databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "Maintenance");

        try {
            jdbcFixture.advanced().assertUserInGroup("APP_USER", "Utilisateur");
            fail("L'utilisateur n'est pas dans le group.");
        }
        catch (AssertionFailedError e) {
            assertEquals("User 'APP_USER' not in group 'Utilisateur'", e.getMessage());
        }
        databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "Utilisateur");
        jdbcFixture.advanced().assertUserInGroup("APP_USER", "Utilisateur");
    }
}
