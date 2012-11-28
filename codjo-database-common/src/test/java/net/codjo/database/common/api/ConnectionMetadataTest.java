package net.codjo.database.common.api;
import java.util.Properties;
import junit.framework.TestCase;
import org.junit.Test;
/**
 *
 */
public class ConnectionMetadataTest extends TestCase {

    @Test
    public void test_createConnexion_prefixDatabase() throws Exception {
        Properties databaseProperties = buildProperties("database.");
        ConnectionMetadata metadata= new ConnectionMetadata(databaseProperties);
        assertNotNull(metadata.getUser());
        assertNotNull(metadata.getHostname());
    }

    @Test
    public void test_createConnexion_prefixJdbc() throws Exception {
        Properties databaseProperties = buildProperties("jdbc.");
        ConnectionMetadata metadata= new ConnectionMetadata(databaseProperties);
        assertNotNull(metadata.getUser());
        assertNotNull(metadata.getHostname());
    }

    @Test
    public void test_createConnexion_prefixTokio() throws Exception {
        Properties databaseProperties = buildProperties("tokio.jdbc.");
        ConnectionMetadata metadata= new ConnectionMetadata(databaseProperties);
        assertNotNull(metadata.getUser());
        assertNotNull(metadata.getHostname());
    }

    @Test
    public void test_createConnexion_NoPrefix() throws Exception {
        Properties databaseProperties = buildProperties("");
        ConnectionMetadata metadata= new ConnectionMetadata(databaseProperties);
        assertNotNull(metadata.getUser());
        assertNotNull(metadata.getHostname());
    }


    private Properties buildProperties(String prefix) {
        Properties databaseProperties = new Properties();
        databaseProperties.put(prefix+"user","LIB");
        databaseProperties.put(prefix+"password","LIB");
        databaseProperties.put(prefix+"base","LIB");
        databaseProperties.put(prefix+"catalog","LIB");
        databaseProperties.put(prefix+"schema","LIB");
        databaseProperties.put(prefix+"charset","LIB");
        databaseProperties.put(prefix+"hostname","GALABERT");
        databaseProperties.put(prefix+"server","uncafesilvousplait:89/ca/ne/sera/pas/pris");
        return databaseProperties;
    }
}
