package net.codjo.database.api.query;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public abstract class SelectQueryTestCase extends AbstractQueryTestCase {

    @Test
    public void test_select_emptyTable() throws Exception {
        SelectQuery selectQuery = create("select * from AP_TEST");

        SelectResult result = selectQuery.execute();

        assertThat(result.getTotalRowCount(), equalTo(0));
        assertThat(result.next(), is(false));
    }


    @Test
    public void test_select_2rows() throws Exception {
        insertInto("AP_TEST", 1, 2);

        SelectQuery selectQuery = create("select * from AP_TEST");

        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Test
    public void test_select_firstPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        SelectQuery selectQuery = create("select * from AP_TEST");
        selectQuery.setPage(new Page(1, 2));

        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public void test_select_lastPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        SelectQuery selectQuery = create("select * from AP_TEST");
        selectQuery.setPage(new Page(2, 2));

        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("3"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public abstract void test_select_storedProcedure() throws Exception;


    protected abstract SelectQuery create(String sqlQuery) throws SQLException;
}
