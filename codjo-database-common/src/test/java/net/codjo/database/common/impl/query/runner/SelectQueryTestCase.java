package net.codjo.database.common.impl.query.runner;
import net.codjo.database.common.api.confidential.query.Page;
import net.codjo.database.common.api.confidential.query.SelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;
public abstract class SelectQueryTestCase extends AbstractQueryTestCase {

    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Test
    public void test_select_emptyTable() throws Exception {
        SelectQuery selectQuery = create("select * from AP_TEST");

        SelectResult result = selectQuery.executeQuery();

        assertThat(result.getTotalRowCount(), equalTo(0));
        assertThat(result.next(), is(false));
    }


    @Test
    public void test_select_2rows() throws Exception {
        insertInto("AP_TEST", 1, 2);

        SelectQuery selectQuery = create("select * from AP_TEST");

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Test
    public void test_select_firstPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        SelectQuery selectQuery = create("select * from AP_TEST");
        selectQuery.setPage(new Page(1, 2));

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public void test_select_lastPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        SelectQuery selectQuery = create("select * from AP_TEST");
        selectQuery.setPage(new Page(2, 2));

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("3"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public abstract void test_select_storedProcedure() throws Exception;


    protected SelectQuery create(String sqlQuery) throws SQLException {
        return new DefaultSelectQuery(jdbc.getConnection(),
                                      sqlQuery,
                                      createRowCountStrategy(sqlQuery));
    }
}
