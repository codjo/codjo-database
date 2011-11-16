package net.codjo.database.hsqldb.impl.helper;
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


    @Test
    public void test_dropForeignKey() throws Exception {
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate("create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1 foreign key (COL_A) "
                                  + "references JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        databaseHelper
              .dropForeignKey(jdbcFixture.getConnection(), foreignKey("FK_1", table("JDBC_TEST_1")));

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
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
