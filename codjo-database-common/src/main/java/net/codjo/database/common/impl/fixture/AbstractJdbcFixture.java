package net.codjo.database.common.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
public abstract class AbstractJdbcFixture extends JdbcFixture {

    protected AbstractJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }
}
