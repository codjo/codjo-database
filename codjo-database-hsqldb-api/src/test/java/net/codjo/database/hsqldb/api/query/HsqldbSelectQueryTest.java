package net.codjo.database.hsqldb.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.SelectQueryTestCase;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.hsqldb.impl.fixture.HsqldbJdbcFixture;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class HsqldbSelectQueryTest extends SelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        SelectQuery selectQuery = create("call \"java.lang.Math.max\"(10, 20)");

        SelectResult result = selectQuery.execute();

        assertThat(toString(result).trim(), is("20"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Override
    protected SelectQuery create(String sqlQuery) throws SQLException {
        return DatabaseFactory.create(Engine.HSQLDB).select(jdbc.getConnection(), sqlQuery);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return createHsqldbFixture();
    }


    static JdbcFixture createHsqldbFixture() throws ClassNotFoundException {
        ConnectionMetadata connectionMetadata = createMeta("org.hsqldb.jdbcDriver",
                                                           "",
                                                           "",
                                                           "sa",
                                                           "",
                                                           "");
        return new HsqldbJdbcFixture(connectionMetadata) {
        };
    }
}
