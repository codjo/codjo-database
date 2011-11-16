package net.codjo.database.oracle.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureTest;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fields;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.test.common.PathUtil;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.sql.SQLException;
import junit.framework.AssertionFailedError;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
public class OracleJdbcFixtureTest extends JdbcFixtureTest {

    @Test
    public void test_deleteAllTables_twoTables_oneFather() throws SQLException {
        jdbcFixture
              .create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5) constraint UN_COL_A unique");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");

        jdbcFixture.advanced().assertExists("JDBC_FIXTURE_TEST");
        jdbcFixture.advanced().assertExists("JDBC_FIXTURE_TEST");
        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_FIXTURE_TEST"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_1"));
    }


    @Test
    public void test_deleteAllTables_threeTables_sameFather() throws SQLException {
        jdbcFixture
              .create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5) constraint UN_COL_A unique");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
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
        jdbcFixture
              .create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5) constraint UN1_COL_A unique");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5) constraint UN2_COL_A unique");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
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
        jdbcFixture
              .create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5) constraint UN_COL_A unique");

        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        jdbcFixture.doTearDown();

        JdbcFixture anotherJdbcFixture = JdbcFixture
              .newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_TEST_1");
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_FIXTURE_TEST");
        anotherJdbcFixture.doTearDown();
    }


    // todo à supprimer dès que le script de création d'index oracle aura été implémenté
    @Override
    @Test
    public void test_executeCreateTableScriptFile() throws Exception {
        jdbcFixture.advanced().executeCreateTableScriptFile(new File(
              PathUtil.findResourcesFileDirectory(getClass()), "JdbcFixtureTest.tab"));

        jdbcFixture.advanced().assertExists("JDBC_FIXTURE_TEST");

        jdbcFixture.drop(SqlTable.table("JDBC_FIXTURE_TEST"));
    }


    @Test
    public void test_spoolQuery() throws Exception {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL1 varchar(5), COL2 varchar(5)");

        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values ('1','2')");

        ByteArrayOutputStream result = new ByteArrayOutputStream();
        jdbcFixture.util().spoolQueryResult("select COL1 from JDBC_FIXTURE_TEST", new PrintStream(result));

        Assert.assertEquals("******************** SPOOL ********************"
                            + "**  query = select COL1 from JDBC_FIXTURE_TEST"
                            + "\tCOL1"
                            + "\tVARCHAR2"
                            + "\t1",
                            result.toString().replaceAll("\\n|\\r", ""));
    }


    @Test
    public void test_assertUserInGroup() throws Exception {
        try {
            jdbcFixture.advanced().assertUserInGroup("APP_USER", "ROLE_BATCH_IDW");
            fail("L'utilisateur n'est pas dans le group.");
        }
        catch (AssertionFailedError e) {
            assertEquals("User 'APP_USER' not in group 'ROLE_BATCH_IDW'", e.getMessage());
        }
        jdbcFixture.advanced().assertUserInGroup("APP_USER", "ROLE_UTILISATEUR_IDW");
    }
}
