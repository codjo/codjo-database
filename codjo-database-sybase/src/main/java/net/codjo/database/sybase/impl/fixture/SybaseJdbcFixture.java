package net.codjo.database.sybase.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.JdbcFixtureUtil;
public class SybaseJdbcFixture extends JdbcFixture {

    public SybaseJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }


    @Override
    protected JdbcFixtureAdvanced newJdbcFixtureAdvanced() {
        return new SybaseJdbcFixtureAdvanced(this);
    }


    @Override
    protected JdbcFixtureUtil newJdbcFixtureUtil() {
        return new SybaseJdbcFixtureUtil(this);
    }
}
