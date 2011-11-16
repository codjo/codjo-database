package net.codjo.database.oracle.impl.query.runner;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.PreparedSelectQueryTestCase;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import java.sql.SQLException;
import org.junit.Ignore;
import org.junit.Test;
/**
 *
 */
public class OraclePreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Ignore("Les procs Oracle ne renvoient pas de select.")
    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
    }
}
