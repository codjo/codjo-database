package net.codjo.database.common.api;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Test;
public class DatabaseFactoryTest {
    @Test
    public void test_noRepository() throws Exception {
        try {
            new DatabaseFactory();
            fail();
        }
        catch (Exception e) {
            assertEquals(
                  "Aucune base n'est configurée. Vérifiez votre dépendance vers agf-database-main",
                  e.getMessage());
        }
    }
}
