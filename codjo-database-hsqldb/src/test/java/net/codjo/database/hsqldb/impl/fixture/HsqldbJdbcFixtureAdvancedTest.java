package net.codjo.database.hsqldb.impl.fixture;
import net.codjo.database.common.api.JdbcFixtureAdvancedTest;
import org.junit.Test;
public class HsqldbJdbcFixtureAdvancedTest extends JdbcFixtureAdvancedTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_assertExists_trigger() throws Exception {
        super.test_assertExists_trigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_assertExists_trigger_fail() throws Exception {
        super.test_assertExists_trigger_fail();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_assertDoesntExist_trigger() throws Exception {
        super.test_assertDoesntExist_trigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_assertDoesntExist_trigger_fail() throws Exception {
        super.test_assertDoesntExist_trigger_fail();
    }
}
