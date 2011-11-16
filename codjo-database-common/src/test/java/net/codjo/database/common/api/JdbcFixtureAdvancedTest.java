package net.codjo.database.common.api;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.api.structure.SqlTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.insertTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.triggerName;
import net.codjo.database.common.api.structure.SqlView;
import static net.codjo.database.common.api.structure.SqlView.view;
import static net.codjo.database.common.api.structure.SqlView.viewName;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import java.io.File;
import java.io.IOException;
import junit.framework.AssertionFailedError;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
public abstract class JdbcFixtureAdvancedTest {
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected JdbcFixture jdbcFixture;
    protected DirectoryFixture directoryFixture = DirectoryFixture.newTemporaryDirectoryFixture();
    private DatabaseScriptHelper scriptHelper = databaseFactory.createDatabaseScriptHelper();


    @Before
    public void setUp() throws Exception {
        jdbcFixture = JdbcFixture.newFixture(databaseHelper.createLibraryConnectionMetadata());
        jdbcFixture.doSetUp();
        directoryFixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        directoryFixture.doTearDown();
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_createAndAssertExists_view() throws Exception {
        SqlView view = createView();

        jdbcFixture.advanced().assertExists(view);
    }


    @Test(expected = AssertionFailedError.class)
    public void test_assertExists_view_fail() throws Exception {
        jdbcFixture.advanced().assertExists(viewName("VU_AP_TOTO"));
    }


    @Test
    public void test_assertDoesntExist_view() throws Exception {
        jdbcFixture.advanced().assertDoesntExist(viewName("VU_AP_TOTO"));
    }


    @Test(expected = AssertionFailedError.class)
    public void test_assertDoesntExist_view_fail() throws Exception {
        SqlView view = createView();

        jdbcFixture.advanced().assertDoesntExist(view);
    }


    @Test
    public void test_assertExists_trigger() throws Exception {
        SqlTrigger trigger = createTrigger();

        jdbcFixture.advanced().assertExists(trigger);
    }


    @Test(expected = AssertionFailedError.class)
    public void test_assertExists_trigger_fail() throws Exception {
        jdbcFixture.advanced().assertExists(triggerName("TR_AP_TOTO"));
    }


    @Test
    public void test_assertDoesntExist_trigger() throws Exception {
        jdbcFixture.advanced().assertDoesntExist(triggerName("TR_AP_TOTO"));
    }


    @Test(expected = AssertionFailedError.class)
    public void test_assertDoesntExist_trigger_fail() throws Exception {
        SqlTrigger trigger = createTrigger();

        jdbcFixture.advanced().assertDoesntExist(trigger);
    }


    private SqlView createView() {
        jdbcFixture.create(SqlTable.table("AP_TOTO"), "ID varchar(6)");
        SqlView view = view("VU_AP_TOTO", "select * from AP_TOTO");
        jdbcFixture.advanced().create(view);
        return view;
    }


    private SqlTrigger createTrigger() throws IOException {
        SqlTrigger trigger = insertTrigger("TR_AP_TOTO_I", table("AP_TOTO"), "");
        jdbcFixture.create(SqlTable.table("AP_TOTO"), "ID varchar(6)");
        File scriptFile = new File(directoryFixture, "trigger.sql");
        FileUtil.saveContent(scriptFile, scriptHelper.buildCreateTriggerScript(trigger));
        jdbcFixture.advanced().executeCreateTableScriptFile(scriptFile);
        return trigger;
    }
}
