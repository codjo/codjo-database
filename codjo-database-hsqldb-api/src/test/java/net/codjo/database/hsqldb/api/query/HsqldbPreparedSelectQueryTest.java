package net.codjo.database.hsqldb.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.PreparedSelectQueryTestCase;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class HsqldbPreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        PreparedSelectQuery selectQuery = create("call \"java.lang.Math.max\"(?, 2)");
        selectQuery.setInt(1, 5);
        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("5"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Override
    protected PreparedSelectQuery create(String query) throws SQLException {
        return DatabaseFactory.create(Engine.HSQLDB).preparedSelectQuery(jdbc.getConnection(), query);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return HsqldbSelectQueryTest.createHsqldbFixture();
    }
}
