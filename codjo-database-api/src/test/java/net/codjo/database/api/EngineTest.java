package net.codjo.database.api;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import org.junit.Test;
/**
 *
 */
public class EngineTest {
    @Test
    public void test_toEngine() throws Exception {
        assertThat(Engine.toEngine("mysql"), is(Engine.MYSQL));
        assertThat(Engine.toEngine("MYSQL"), is(Engine.MYSQL));
        assertThat(Engine.toEngine(null), nullValue());
    }


    @Test
    public void test_toEngine_hackTemporaireSybase() throws Exception {
        assertThat(Engine.toEngine("${databasetype}"), is(Engine.SYBASE));
        assertThat(Engine.toEngine("${DATABASETYPE}"), is(Engine.SYBASE));
    }
}
