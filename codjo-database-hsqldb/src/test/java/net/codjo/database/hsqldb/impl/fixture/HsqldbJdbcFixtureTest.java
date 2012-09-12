package net.codjo.database.hsqldb.impl.fixture;
import net.codjo.database.common.api.JdbcFixtureTest;
import org.junit.Test;
public class HsqldbJdbcFixtureTest extends JdbcFixtureTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    @Test
    public void test_executeCreateTableScriptFile() throws Exception {
        super.test_executeCreateTableScriptFile();
    }


    @Override
    @Test
    public void test_assertObjectExists_table() throws Exception {
        super.test_assertObjectExists_table();
    }


    @Override
    @Test
    public void test_assertObjectExists_view() throws Exception {
        super.test_assertObjectExists_view();
    }


    @Override
    @Test
    public void test_assertObjectExists_trigger() throws Exception {
        super.test_assertObjectExists_trigger();
    }
}
