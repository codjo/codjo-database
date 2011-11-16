package net.codjo.database.sybase.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.PreparedSelectQueryTestCase;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class SybasePreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        PreparedSelectQuery selectQuery = create("sp_spaceused ?");
        selectQuery.setString(1, "sysobjects");

        SelectResult result = selectQuery.execute();

        assertThat(toString(result).trim(), is("sysobjects"));
        assertThat(result.getTotalRowCount(), equalTo(1));
    }


    @Override
    protected PreparedSelectQuery create(String query) throws SQLException {
        return DatabaseFactory.create(Engine.SYBASE).preparedSelectQuery(jdbc.getConnection(), query);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return SybaseSelectQueryTest.createSybaseFixture();
    }
}
