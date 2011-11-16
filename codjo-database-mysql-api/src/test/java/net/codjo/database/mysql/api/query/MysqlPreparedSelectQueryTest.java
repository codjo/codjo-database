package net.codjo.database.mysql.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.PreparedSelectQueryTestCase;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class MysqlPreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        try {
            jdbc.executeUpdate("DROP PROCEDURE proc1");
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        insertInto("AP_TEST", 1);
        try {
            jdbc.executeUpdate("CREATE PROCEDURE proc1() BEGIN select * from AP_TEST; END");
            PreparedSelectQuery selectQuery = create("call proc1()");
            SelectResult result = selectQuery.execute();

            assertThat(toString(result), is("1"));
            assertThat(result.getTotalRowCount(), equalTo(2));
        }
        finally {
            jdbc.executeUpdate("DROP PROCEDURE proc1");
        }
    }


    @Override
    protected PreparedSelectQuery create(String query) throws SQLException {
        return DatabaseFactory.create(Engine.MYSQL).preparedSelectQuery(jdbc.getConnection(), query);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return MysqlSelectQueryTest.createMysqlFixture();
    }
}
