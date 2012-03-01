package net.codjo.database.api;
import net.codjo.database.api.DatabaseFactory.DatabaseInstantiationException;
import org.junit.Test;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
/**
 *
 */
public class DatabaseFactoryTest {
    @Test
    public void test_createWithoutDependencies() {
        try {
            DatabaseFactory.create(Engine.SYBASE);
            fail();
        }
        catch (DatabaseInstantiationException ex) {
            assertThat(ex.getMessage(), is("Le moteur 'sybase' ne peut être créé."
                                           + " Avez vous la dépendance sur 'codjo-database-sybase-api' ?"));

            Throwable cause = ex.getCause();
            assertThat(cause.getClass().getSimpleName(), is("ClassNotFoundException"));
            assertThat(cause.getMessage(), is("net.codjo.database.sybase.api.SybaseDatabase"));
        }
    }
}
