package net.codjo.database.mysql.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.JdbcFixtureUtil;
public class MysqlJdbcFixture extends JdbcFixture {

    protected MysqlJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }


    @Override
    protected JdbcFixtureAdvanced newJdbcFixtureAdvanced() {
        return new MysqlJdbcFixtureAdvanced(this);
    }


    @Override
    protected JdbcFixtureUtil newJdbcFixtureUtil() {
        return new MysqlJdbcFixtureUtil(this);
    }
}
