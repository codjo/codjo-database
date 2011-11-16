package net.codjo.database.oracle.api.query.runner;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.SelectQueryTestCase;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.oracle.impl.fixture.OracleJdbcFixtureBuilder;
import java.sql.SQLException;
import org.junit.Ignore;
import org.junit.Test;

public class OracleSelectQueryTest extends SelectQueryTestCase {

    @Ignore("Les procs Oracle ne renvoient pas de select.")
    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
    }


    @Override
    protected SelectQuery create(String sqlQuery) throws SQLException {
        return DatabaseFactory.create(Engine.ORACLE).select(jdbc.getConnection(), sqlQuery);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return createOracleFixture();
    }


    static JdbcFixture createOracleFixture() throws ClassNotFoundException, SQLException {
        ConnectionMetadata connectionMetadata = createMeta("oracle.jdbc.driver.OracleDriver",
                                                           "ad-idw",
                                                           "31522",
                                                           "LIB",
                                                           "LIB",
                                                           "IDWDEV2");
        return new OracleJdbcFixtureBuilder(null).get(connectionMetadata);
    }
}
