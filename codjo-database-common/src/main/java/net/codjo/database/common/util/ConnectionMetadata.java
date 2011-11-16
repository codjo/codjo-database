package net.codjo.database.common.util;
import java.util.Properties;
/**
 * @deprecated {@link net.codjo.database.common.api.ConnectionMetadata}
 */
@Deprecated
public class ConnectionMetadata extends net.codjo.database.common.api.ConnectionMetadata {

    public ConnectionMetadata() {
    }


    public ConnectionMetadata(Properties databaseProperties) {
        super(databaseProperties);
    }
}
