package net.codjo.database.api.query;
import net.codjo.database.api.query.AbstractQueryTestCase;
import net.codjo.database.api.query.Page;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectResult;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public abstract class PreparedSelectQueryTestCase extends AbstractQueryTestCase {

    @Test
    public void test_select_emptyTable() throws Exception {
        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID=?");

        selectQuery.setInt(1, 0);
        SelectResult result = selectQuery.execute();

        assertThat(result.getTotalRowCount(), equalTo(0));
        assertThat(result.next(), is(false));
    }


    @Test
    public void test_select_2rows() throws Exception {
        insertInto("AP_TEST", 1, 2, 3);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID < ?");
        selectQuery.setInt(1, 3);
        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("1 | 2"));
        assertThat(result.getTotalRowCount(), equalTo(2));
    }


    @Test
    public void test_select_firstPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3, 4);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID > ?");
        selectQuery.setInt(1, 1);
        selectQuery.setPage(new Page(1, 2));

        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("2 | 3"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    @Test
    public void test_select_lastPage() throws Exception {
        insertInto("AP_TEST", 1, 2, 3, 4);

        PreparedSelectQuery selectQuery = create("select * from AP_TEST where MY_ID > ?");
        selectQuery.setInt(1, 1);
        selectQuery.setPage(new Page(2, 2));

        SelectResult result = selectQuery.execute();

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

        SelectResult result = selectQuery.execute();

        assertThat(toString(result), is("4"));
        assertThat(result.getTotalRowCount(), equalTo(3));
    }


    protected abstract PreparedSelectQuery create(String query) throws SQLException;
}
