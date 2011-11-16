package net.codjo.database.mysql.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.SelectQueryTestCase;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.mysql.impl.fixture.MysqlJdbcFixtureBuilder;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class MysqlSelectQueryTest extends SelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        SelectQuery selectQuery = create("SHOW COLUMNS FROM AP_TEST");

        SelectResult result = selectQuery.execute();

        assertThat(toString(result).trim(), is("MY_ID"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Override
    protected SelectQuery create(String sqlQuery) throws SQLException {
        return DatabaseFactory.create(Engine.MYSQL).select(jdbc.getConnection(), sqlQuery);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return createMysqlFixture();
    }


    static JdbcFixture createMysqlFixture() throws ClassNotFoundException, SQLException {
        ConnectionMetadata connectionMetadata = createMeta("com.mysql.jdbc.Driver",
                                                           "ad-livmu",
                                                           "33100",
                                                           "lib_dbo",
                                                           "lib_dbo",
                                                           "lib");
        return new MysqlJdbcFixtureBuilder(null).get(connectionMetadata);
    }
}
