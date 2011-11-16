package net.codjo.database.sybase.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.impl.fixture.AbstractJdbcFixtureBuilder;
import java.sql.SQLException;
public class SybaseJdbcFixtureBuilder extends AbstractJdbcFixtureBuilder {

    public SybaseJdbcFixtureBuilder(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }


    public JdbcFixture get(ConnectionMetadata connectionMetadata) throws SQLException {
        return new SybaseJdbcFixture(connectionMetadata);
    }
}
