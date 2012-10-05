package net.codjo.database.hsqldb.impl.fixture;
import java.sql.SQLException;
import java.util.List;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.JdbcFixtureUtil;
import net.codjo.database.common.api.RuntimeSqlException;
public class HsqldbJdbcFixture extends JdbcFixture {

    protected HsqldbJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }


    @Override
    protected HsqldbJdbcFixtureAdvanced newJdbcFixtureAdvanced() {
        return new HsqldbJdbcFixtureAdvanced(this);
    }


    @Override
    protected JdbcFixtureUtil newJdbcFixtureUtil() {
        return new HsqldbJdbcFixtureUtil(this);
    }

    protected List<String> getAllTables() {
        return newJdbcFixtureAdvanced().getAllTables();
    }
}
