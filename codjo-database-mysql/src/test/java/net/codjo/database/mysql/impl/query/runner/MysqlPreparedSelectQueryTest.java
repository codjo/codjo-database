package net.codjo.database.mysql.impl.query.runner;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.PreparedSelectQueryTestCase;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;
/**
 *
 */
public class MysqlPreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        insertInto("AP_TEST", 1);
        jdbc.executeUpdate("CREATE PROCEDURE proc1() BEGIN select * from AP_TEST; END");
        try {
            PreparedSelectQuery selectQuery = create("call proc1()");
            SelectResult result = selectQuery.executeQuery();

            assertThat(toString(result), is("1"));
            assertThat(result.getTotalRowCount(), equalTo(2));
        }
        finally {
            jdbc.executeUpdate("DROP PROCEDURE proc1");
        }
    }
}
