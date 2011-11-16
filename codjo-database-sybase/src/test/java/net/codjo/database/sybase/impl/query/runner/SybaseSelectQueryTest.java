package net.codjo.database.sybase.impl.query.runner;

import net.codjo.database.common.api.confidential.query.SelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import net.codjo.database.common.impl.query.runner.SelectQueryTestCase;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class SybaseSelectQueryTest extends SelectQueryTestCase {

    @Override
    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new SybaseRowCountStrategy(jdbc.getConnection());
    }


    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        SelectQuery selectQuery = create("sp_spaceused sysobjects");

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result).trim(), is("sysobjects"));
        assertThat(result.getTotalRowCount(), equalTo(1));
    }
}
