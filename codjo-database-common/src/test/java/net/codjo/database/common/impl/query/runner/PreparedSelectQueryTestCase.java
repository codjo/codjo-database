package net.codjo.database.common.impl.query.runner;
import net.codjo.database.common.api.confidential.query.Page;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;
public abstract class PreparedSelectQueryTestCase extends AbstractQueryTestCase {

    protected RowCountStrategy createRowCountStrategy(String query) throws SQLException {
        return new DefaultRowCountStrategy(jdbc.getConnection(), query);
    }


    @Test
    public void test_select_emptyTable() throws Exception {
        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID=?");

        selectQuery.setInt(1, 0);
        SelectResult result = selectQuery.executeQuery();

        assertThat(result.getTotalRowCount(), equalTo(0));
        assertThat(result.next(), is(false));
    }


    @Test
    public void test_select_2rows() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID < ?");
        selectQuery.setInt(1, 3);
        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Test
    public void test_select_firstPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3, 4);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID > ?");
        selectQuery.setInt(1, 1);
        selectQuery.setPage(new Page(1, 2));

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("2 | 3"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public void test_select_lastPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3, 4);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID > ?");
        selectQuery.setInt(1, 1);
        selectQuery.setPage(new Page(2, 2));

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("4"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public abstract void test_select_storedProcedure() throws Exception;


    @Test
    public void test_select_subSelect() throws Exception {
        insertInto("AP_TEST", 1, 2, 3, 4);

        PreparedSelectQuery selectQuery =
              create("select MY_ID as fromId, MY_ID as selectedId, ( select max(MY_ID) from AP_TEST )"
                     + " from AP_TEST where MY_ID > ?");
        selectQuery.setInt(1, 1);
        selectQuery.setPage(new Page(2, 2));

        SelectResult result = selectQuery.executeQuery();

        assertThat(toString(result), is("4"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    protected PreparedSelectQuery create(String query) throws SQLException {
        return new DefaultPreparedSelectQuery(jdbc.getConnection(),
                                              query,
                                              createRowCountStrategy(query));
    }
}
