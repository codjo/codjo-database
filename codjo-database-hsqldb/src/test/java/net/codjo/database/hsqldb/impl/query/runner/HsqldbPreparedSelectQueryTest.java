package net.codjo.database.hsqldb.impl.query.runner;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;
/**
 *
 */
public class HsqldbPreparedSelectQueryTest extends net.codjo.database.common.impl.query.runner.PreparedSelectQueryTestCase {
    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        PreparedSelectQuery selectQuery = create("call \"java.lang.Math.max\"(?, 2)");
        selectQuery.setInt(1, 5);
        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("5"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }
}
