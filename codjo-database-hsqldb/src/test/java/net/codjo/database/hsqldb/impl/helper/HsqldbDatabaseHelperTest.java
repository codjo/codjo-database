package net.codjo.database.hsqldb.impl.helper;
import java.lang.Thread.State;
import java.sql.Statement;
import junit.framework.AssertionFailedError;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelperTest;
import net.codjo.database.hsqldb.impl.query.HsqldbDatabaseQueryHelper;
import java.sql.SQLException;
import java.util.Properties;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
public class HsqldbDatabaseHelperTest extends AbstractDatabaseHelperTest {

    @Override
    protected DatabaseHelper createDatabaseHelper() throws SQLException {
        return new HsqldbDatabaseHelper(new HsqldbDatabaseQueryHelper());
    }


    @Override
    protected String buildDatabaseUrl(Properties databaseProperties) {
        return "jdbc:hsqldb:.";
    }


    @Test(expected = SQLException.class)
    public void test_setIdentityInsert_false() throws Exception {
        jdbcFixture.create(SqlTable.table("MY_TABLE"), "MY_FIELD_1 numeric(18,0) identity not null,"
                                                       + "MY_FIELD_2 int null");
        databaseHelper.setIdentityInsert(jdbcFixture.getConnection(), null, "MY_TABLE", false);

        jdbcFixture.executeUpdate("insert into MY_TABLE (MY_FIELD_1, MY_FIELD_2) select 3, 1");
    }


    @Test
    public void test_setIdentityInsert_true() throws Exception {
        jdbcFixture.create(SqlTable.table("MY_TABLE"), "MY_FIELD_1 numeric(18,0) identity not null,"
                                                       + "MY_FIELD_2 int null");
        databaseHelper.setIdentityInsert(jdbcFixture.getConnection(), null, "MY_TABLE", true);

        jdbcFixture.executeUpdate("insert into MY_TABLE (MY_FIELD_1, MY_FIELD_2) values (3, 1)");
        jdbcFixture.assertContent(SqlTable.table("MY_TABLE"), new String[][]{{"3", "1"},});
    }


    @Test
    public void test_dropAllObjects_tables() throws Exception {
        jdbcFixture.create(SqlTable.table("MY_TABLE_1"), "MY_FIELD_1 numeric null");
        jdbcFixture.create(SqlTable.table("MY_TABLE_2"), "MY_FIELD_1 numeric null");

        jdbcFixture.advanced().dropAllObjects();

        jdbcFixture.advanced().assertDoesntExist("MY_TABLE_1");
        jdbcFixture.advanced().assertDoesntExist("MY_TABLE_2");
    }


    @Test
    public void test_dropAllObjects_constraints()
          throws Exception {
        jdbcFixture.create(SqlTable.table("MY_TABLE_1"), "MY_FIELD_1 numeric primary key");
        jdbcFixture.create(SqlTable.table("MY_TABLE_2"), "MY_FIELD_1 numeric null, MY_REF_1 numeric");
        jdbcFixture.executeUpdate("alter table MY_TABLE_2 add constraint FK_MY_TABLE_1 "
                                  + "foreign key (MY_REF_1) references MY_TABLE_1(MY_FIELD_1)");

        jdbcFixture.advanced().dropAllObjects();

        jdbcFixture.advanced().assertDoesntExist("MY_TABLE_1");
        jdbcFixture.advanced().assertDoesntExist("MY_TABLE_2");

        try {
            jdbcFixture.advanced().assertExists(foreignKey("FK_MY_TABLE_1", table("MY_TABLE_2")));
            fail();
        }
        catch (AssertionFailedError ex) {
            ;
        }
    }


    @Test
    public void test_dropForeignKey() throws Exception {
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate("create index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_FIXTURE_TEST add constraint X1_JDBC_FIXTURE_TEST unique (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1 foreign key (COL_A) "
                                  + "references JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        databaseHelper
              .dropForeignKey(jdbcFixture.getConnection(), foreignKey("FK_1", table("JDBC_TEST_1")));

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
    }


    @Test
    public void test_changeUserGroup() throws Exception {
        String user = "APP_USER";
        String users = "Utilisateur";
        String maintenance = "Maintenance";
        String[] roles = {users, maintenance};
        Statement s = jdbcFixture.getConnection().createStatement();
        try {
            s.executeUpdate("CREATE USER " + user + " PASSWORD abcdef");
            for (String role : roles) {
                s.executeUpdate("CREATE ROLE " + role);
            }
        } finally {
            s.close();
        }
        databaseHelper.changeUserGroup(jdbcFixture.getConnection(), user, users);
        jdbcFixture.advanced().assertUserInGroup(user, users);
        databaseHelper.changeUserGroup(jdbcFixture.getConnection(), user, maintenance);
        jdbcFixture.advanced().assertUserInGroup(user, maintenance);
    }


    @Test
    public void test_changeUserGroup_badGroup() {
        try {
            databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "BadGroup");
            fail("Erreur car le group n'existe pas.");
        }
        catch (SQLException exception) {
            ;
        }
    }


    @Test
    public void test_changeUserGroup_badUser() {
        try {
            databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "BAD_USER", "Utilisateur");
            fail("Erreur car le user n'existe pas.");
        }
        catch (SQLException exception) {
            ;
        }
    }

    @Override
    protected void assertLibraryConnectionMetadata(ConnectionMetadata connectionMetadata) {
        assertEquals("", connectionMetadata.getHostname());
        assertEquals("", connectionMetadata.getPort());
        assertEquals("sa", connectionMetadata.getUser());
        assertEquals("", connectionMetadata.getPassword());
        assertEquals("", connectionMetadata.getCatalog());
        assertNull(connectionMetadata.getCharset());
        assertNull(connectionMetadata.getSchema());
    }
}
