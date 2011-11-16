package net.codjo.database.mysql.impl.fixture;
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
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public class MysqlJdbcFixtureTest extends JdbcFixtureTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jdbcFixture.executeUpdate("SET storage_engine=INNODB");
    }


    @Test
    public void test_deleteAllTables_twoTables_oneFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1", table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.executeUpdate("insert into JDBC_FIXTURE_TEST values('TL')");
        jdbcFixture.executeUpdate("insert into JDBC_TEST_1 values('TL')");

        jdbcFixture.deleteAllTables();

        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_FIXTURE_TEST"));
        jdbcFixture.assertIsEmpty(SqlTable.table("JDBC_TEST_1"));
    }


    @Test
    public void test_deleteAllTables_threeTables_sameFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1", table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2", table("JDBC_TEST_2"), fields("COL_A"),
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
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_TEST_1 on JDBC_TEST_1 (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1", table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2", table("JDBC_TEST_2"), fields("COL_A"),
                                                 table("JDBC_TEST_1"), fields("COL_A")));
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

        jdbcFixture.advanced().create(foreignKey("FK_1", table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));

        jdbcFixture.advanced()
              .assertExists(SqlConstraint.foreignKey("FK_1", SqlTable.table("JDBC_TEST_1")));

        jdbcFixture.doTearDown();

        JdbcFixture anotherJdbcFixture = newFixture(databaseHelper.createLibraryConnectionMetadata());
        anotherJdbcFixture.doSetUp();
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_TEST_1");
        anotherJdbcFixture.advanced().assertDoesntExist("JDBC_FIXTURE_TEST");
        anotherJdbcFixture.doTearDown();
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
                     + "\tVARCHAR"
                     + "\t1",
                     result.toString().replaceAll("\\n|\\r", ""));
    }
}

