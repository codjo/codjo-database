package net.codjo.database.oracle.api.query.runner;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.PreparedSelectQueryTestCase;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.common.api.JdbcFixture;
import java.sql.SQLException;
import org.junit.Ignore;
import org.junit.Test;

public class OraclePreparedSelectQueryTest extends PreparedSelectQueryTestCase {

    @Ignore("Les procs Oracle ne renvoient pas de select.")
    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
    }


    @Override
    protected PreparedSelectQuery create(String query) throws SQLException {
        return DatabaseFactory.create(Engine.ORACLE).preparedSelectQuery(jdbc.getConnection(), query);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return JdbcFixture.newFixture();
    }
}
