package net.codjo.database.mysql.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.impl.fixture.AbstractJdbcFixtureBuilder;
import java.sql.SQLException;
public class MysqlJdbcFixtureBuilder extends AbstractJdbcFixtureBuilder {

    public MysqlJdbcFixtureBuilder(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }


    public JdbcFixture get(ConnectionMetadata connectionMetadata) throws SQLException {
        return new MysqlJdbcFixture(connectionMetadata);
    }
}
