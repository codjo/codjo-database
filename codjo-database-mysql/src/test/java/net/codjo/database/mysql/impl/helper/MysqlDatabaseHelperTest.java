package net.codjo.database.mysql.impl.helper;
import java.sql.SQLException;
import java.util.Properties;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelperTest;
import net.codjo.database.mysql.impl.query.MysqlDatabaseQueryHelper;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlIndex.normalIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
public class MysqlDatabaseHelperTest extends AbstractDatabaseHelperTest {

    @Override
    protected DatabaseHelper createDatabaseHelper() throws SQLException {
        return new MysqlDatabaseHelper(new MysqlDatabaseQueryHelper());
    }


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jdbcFixture.executeUpdate("SET storage_engine=INNODB");
    }


    @Override
    protected String buildDatabaseUrl(Properties databaseProperties) {
        return new StringBuilder()
              .append("jdbc:mysql://")
              .append(databaseProperties.getProperty("database.hostname")).append(":")
              .append(databaseProperties.getProperty("database.port")).toString();
    }


    @Test
    public void test_dropForeignKey() throws Exception {
        jdbcFixture.create(table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate("create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1 foreign key (COL_A) "
                                  + "references JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        databaseHelper
              .dropForeignKey(jdbcFixture.getConnection(), foreignKey("FK_1", table("JDBC_TEST_1")));

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
    }


    @Test
    public void test_truncateWithForeignKey() throws Exception {
        jdbcFixture.create(table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate("create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1 foreign key (COL_A) "
                                  + "references JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        final MysqlDatabaseHelper mysqlDatabaseHelper = (MysqlDatabaseHelper)databaseHelper;
        int foreignKeyChecks_before = mysqlDatabaseHelper.getForeignKeyChecks(jdbcFixture.getConnection());

        databaseHelper.truncateTable(jdbcFixture.getConnection(), table("JDBC_TEST_1"));

        int foreignKeyChecks_after = mysqlDatabaseHelper.getForeignKeyChecks(jdbcFixture.getConnection());
        assertEquals(foreignKeyChecks_before, foreignKeyChecks_after);
    }


    @Test
    public void test_dropAllObjects() throws SQLException {
        jdbcFixture.executeUpdate("create table JDBC_FIXTURE_TEST (COL_A varchar(5))");
        jdbcFixture.executeUpdate("create table JDBC_TEST_1 (COL_A varchar(5))");
        jdbcFixture.executeUpdate("create table JDBC_TEST_2 (COL_A varchar(5))");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_TEST_1 on JDBC_TEST_1 (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1"
                                  + " foreign key (COL_A) references JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_2 add constraint FK_2"
                                  + " foreign key (COL_A) references JDBC_TEST_1 (COL_A)");

        jdbcFixture.executeUpdate("CREATE VIEW view1 AS SELECT COL_A FROM JDBC_TEST_1");
        jdbcFixture.advanced().assertObjectExists("view1", ObjectType.VIEW);

        jdbcFixture.executeUpdate("CREATE PROCEDURE proc1() BEGIN select COL_A from JDBC_TEST_1; END");
        jdbcFixture.advanced().assertObjectExists("proc1", ObjectType.STORED_PROCEDURE);
        jdbcFixture.executeUpdate("CREATE PROCEDURE proc2() BEGIN call proc1(); END");
        jdbcFixture.advanced().assertObjectExists("proc2", ObjectType.STORED_PROCEDURE);

        jdbcFixture.executeUpdate("CREATE TRIGGER tr1 BEFORE INSERT ON JDBC_TEST_1" +
                                  " FOR EACH ROW BEGIN END");
        jdbcFixture.advanced().assertObjectExists("tr1", ObjectType.TRIGGER);

        databaseHelper.dropAllObjects(jdbcFixture.getConnection());

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_2", table("JDBC_TEST_2")));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_FIXTURE_TEST"));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_TEST_1"));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_TEST_2"));
        jdbcFixture.advanced()
              .assertDoesntExist(normalIndex("X1_JDBC_TEST_1", table("JDBC_TEST_1"),
                                             fieldName("COL_A")));
        jdbcFixture.advanced()
              .assertDoesntExist(normalIndex("X1_JDBC_FIXTURE_TEST", table("JDBC_FIXTURE_TEST"),
                                             fieldName("COL_A")));
        jdbcFixture.advanced().assertObjectDoesntExist("view1", ObjectType.VIEW);
        jdbcFixture.advanced().assertObjectDoesntExist("proc1", ObjectType.STORED_PROCEDURE);
        jdbcFixture.advanced().assertObjectDoesntExist("proc2", ObjectType.STORED_PROCEDURE);
        jdbcFixture.advanced().assertObjectDoesntExist("tr1", ObjectType.TRIGGER);
    }


    @Override
    protected void assertLibraryConnectionMetadata(ConnectionMetadata connectionMetadata) {
        assertEquals("ad-livmu", connectionMetadata.getHostname());
        assertEquals("33100", connectionMetadata.getPort());
        assertEquals("lib_dbo", connectionMetadata.getUser());
        assertEquals("lib_dbo", connectionMetadata.getPassword());
        assertEquals("lib", connectionMetadata.getCatalog());
        assertNull(connectionMetadata.getCharset());
        assertNull(connectionMetadata.getSchema());
    }
}
