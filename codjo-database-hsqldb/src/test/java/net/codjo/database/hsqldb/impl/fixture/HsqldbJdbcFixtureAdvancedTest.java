package net.codjo.database.hsqldb.impl.fixture;
import junit.framework.AssertionFailedError;
import net.codjo.database.common.api.JdbcFixtureAdvancedTest;
import org.junit.Test;
public class HsqldbJdbcFixtureAdvancedTest extends JdbcFixtureAdvancedTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    @Test
    public void test_assertExists_trigger() throws Exception {
        super.test_assertExists_trigger();
    }


    @Override
    @Test(expected = AssertionFailedError.class)
    public void test_assertExists_trigger_fail() throws Exception {
        super.test_assertExists_trigger_fail();
    }


    @Override
    @Test
    public void test_assertDoesntExist_trigger() throws Exception {
        super.test_assertDoesntExist_trigger();
    }


    @Override
    @Test(expected = AssertionFailedError.class)
    public void test_assertDoesntExist_trigger_fail() throws Exception {
        super.test_assertDoesntExist_trigger_fail();
    }
}
