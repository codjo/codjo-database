package net.codjo.database.sybase.api.query;
import net.codjo.database.api.DatabaseFactory;
import net.codjo.database.api.Engine;
import net.codjo.database.api.query.SelectQueryTestCase;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.SelectResult;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.sybase.impl.fixture.SybaseJdbcFixtureBuilder;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import java.sql.SQLException;
import org.junit.Test;

public class SybaseSelectQueryTest extends SelectQueryTestCase {

    @Override
    @Test
    public void test_select_storedProcedure() throws Exception {
        SelectQuery selectQuery = create("sp_spaceused sysobjects");

        SelectResult result = selectQuery.execute();

        assertThat(toString(result).trim(), is("sysobjects"));
        assertThat(result.getTotalRowCount(), equalTo(1));
    }


    @Override
    protected SelectQuery create(String sqlQuery) throws SQLException {
        return DatabaseFactory.create(Engine.SYBASE).select(jdbc.getConnection(), sqlQuery);
    }


    @Override
    protected JdbcFixture createFixture() throws SQLException, ClassNotFoundException {
        return createSybaseFixture();
    }


    static JdbcFixture createSybaseFixture() throws ClassNotFoundException, SQLException {
        ConnectionMetadata connectionMetadata = new ConnectionMetadata();
        connectionMetadata.setHostname("ai-lib");
        connectionMetadata.setPort("34100");
        connectionMetadata.setUser("LIB_dbo");
        connectionMetadata.setPassword("LIBPWD");
        connectionMetadata.setCatalog("LIB");
        Class.forName("com.sybase.jdbc2.jdbc.SybDriver");
        return new SybaseJdbcFixtureBuilder(null).get(connectionMetadata);
    }
}
