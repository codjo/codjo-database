package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import java.sql.SQLException;
public interface JdbcFixtureBuilder {

    JdbcFixture get(ConnectionMetadata connectionMetadata) throws SQLException;
}
