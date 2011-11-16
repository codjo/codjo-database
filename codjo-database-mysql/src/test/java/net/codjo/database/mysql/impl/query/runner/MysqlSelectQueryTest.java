package net.codjo.database.mysql.impl.query.runner;
import net.codjo.database.common.api.confidential.query.SelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import net.codjo.database.common.impl.query.runner.SelectQueryTestCase;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;
/**
 *
 */
public class MysqlSelectQueryTest extends SelectQueryTestCase {

    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        SelectQuery selectQuery = create("SHOW COLUMNS FROM AP_TEST");

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result).trim(), is("MY_ID"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }
}
