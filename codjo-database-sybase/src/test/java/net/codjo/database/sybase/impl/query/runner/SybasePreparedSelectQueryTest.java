package net.codjo.database.sybase.impl.query.runner;

import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class SybasePreparedSelectQueryTest extends net.codjo.database.common.impl.query.runner.PreparedSelectQueryTestCase {
    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new SybaseRowCountStrategy(jdbc.getConnection());
    }


    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        PreparedSelectQuery selectQuery = create("sp_spaceused ?");
        selectQuery.setString(1, "sysobjects");

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result).trim(), is("sysobjects"));
        assertThat(result.getTotalRowCount(), equalTo(1));
    }
}
