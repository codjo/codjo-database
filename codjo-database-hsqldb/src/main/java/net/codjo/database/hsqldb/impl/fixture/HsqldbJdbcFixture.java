package net.codjo.database.hsqldb.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.JdbcFixtureUtil;
public class HsqldbJdbcFixture extends JdbcFixture {

    protected HsqldbJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }


    @Override
    protected JdbcFixtureAdvanced newJdbcFixtureAdvanced() {
        return new HsqldbJdbcFixtureAdvanced(this);
    }


    @Override
    protected JdbcFixtureUtil newJdbcFixtureUtil() {
        return new HsqldbJdbcFixtureUtil(this);
    }
}
