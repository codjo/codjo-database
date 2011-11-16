package net.codjo.database.hsqldb.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.impl.fixture.AbstractJdbcFixtureBuilder;
import java.sql.SQLException;
public class HsqldbJdbcFixtureBuilder extends AbstractJdbcFixtureBuilder {

    public HsqldbJdbcFixtureBuilder(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }


    public JdbcFixture get(ConnectionMetadata connectionMetadata) throws SQLException {
        return new HsqldbJdbcFixture(connectionMetadata);
    }
}
