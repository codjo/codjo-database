package net.codjo.database.common.api;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
public class DatabaseFactoryTest {
    @Test
    public void test_noRepository() throws Exception {
        try {
            new DatabaseFactory();
            fail();
        }
        catch (Exception e) {
            assertEquals("Aucune base n'est configurée."
                         + " Vérifiez votre dépendance vers codjo-database-${databaseType}-api",
                         e.getMessage());
        }
    }
}
