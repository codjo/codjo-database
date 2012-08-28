package net.codjo.database.common.impl.helper;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlIndex.Type;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlView;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelper.CustomScript;
import net.codjo.test.common.LogString;
import net.codjo.test.common.fixture.CompositeFixture;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlField.fields;
import static net.codjo.database.common.api.structure.SqlIndex.Type.CLUSTERED;
import static net.codjo.database.common.api.structure.SqlIndex.Type.NORMAL;
import static net.codjo.database.common.api.structure.SqlIndex.Type.UNIQUE;
import static net.codjo.database.common.api.structure.SqlIndex.Type.UNIQUE_CLUSTERED;
import static net.codjo.database.common.api.structure.SqlIndex.index;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTrigger.checkRecordTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.deleteTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.insertTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.updateTrigger;
import static net.codjo.database.common.api.structure.SqlView.view;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
public abstract class AbstractDatabaseScriptHelperTest {
    protected static final String NOT_IMPLEMENTED_SCRIPT = "not implemented !!!";
    protected static final String EMPTY_SCRIPT = "";
    protected AbstractDatabaseScriptHelper scriptHelper;
    protected DatabaseFactory databaseFactory = new DatabaseFactory();
    protected DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected JdbcFixture jdbcFixture =
          JdbcFixture.newFixture(databaseHelper.createLibraryConnectionMetadata());
    protected DirectoryFixture directoryFixture = DirectoryFixture.newTemporaryDirectoryFixture();
    private CompositeFixture fixture = new CompositeFixture(jdbcFixture, directoryFixture);


    @Before
    public void setUp() throws Exception {
        scriptHelper = (AbstractDatabaseScriptHelper)databaseFactory.createDatabaseScriptHelper();
        fixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        fixture.doTearDown();
    }


    @Test
    public void test_dropTable() throws Exception {
        assertEquals(getDropTableScript(),
                     scriptHelper.buildDropTableScript(table("AP_GROUP_PERSON")));
    }


    @Test
    public void test_createTable_withIdentity() throws Exception {
        assertEquals(getCreateTableScript(),
                     scriptHelper.buildCreateTableScript(buildTable("AP_GROUP_PERSON", true)));
    }


    @Test
    public void test_executeCreateTable_withIdentity() throws Exception {
        executeScript(scriptHelper.buildCreateTableScript(buildTable("AP_GROUP_PERSON", true)));

        try {
            jdbcFixture.advanced().assertExists("AP_GROUP_PERSON");
        }
        finally {
            jdbcFixture.drop(table("AP_GROUP_PERSON"));
        }
    }


    @Test
    public void test_executeDropTable() throws Exception {
        create(table("AP_TEST"), "ID varchar(6)");

        executeScript(scriptHelper.buildDropTableScript(table("AP_TEST")));

        jdbcFixture.advanced().assertDoesntExist(table("AP_TEST"));
    }


    @Test
    public void test_logTableCreation() throws Exception {
        assertEquals(getLogTableCreationScript(),
                     scriptHelper.buildLogTableCreationScript(
                           table("AP_GROUP_PERSON")));
    }


    @Test
    public void test_createView() throws Exception {
        assertEquals(getCreateViewScript(),
                     scriptHelper.buildCreateViewScript(buildView()));
    }


    @Test
    public void test_executeCreateView() throws Exception {
        SqlView view = buildView();
        create(table("AP_TOTO"), "ID varchar(6)");

        executeScript(scriptHelper.buildCreateViewScript(view));

        try {
            jdbcFixture.advanced().assertExists(view);
        }
        finally {
            jdbcFixture.executeUpdate("drop view " + view.getName());
        }
    }


    @Test
    public void test_dropIndex() throws Exception {
        assertEquals(getDropIndexScript(),
                     scriptHelper.buildDropIndexScript(indexOfType(NORMAL)));
    }


    @Test
    public void test_executeDropIndex() throws Exception {
        create(indexOfType(NORMAL));

        executeScript(scriptHelper.buildDropIndexScript(index("X1_AP_TOTO", table("AP_TOTO"))));

        jdbcFixture.advanced().assertDoesntExist(index("X1_AP_TOTO", table("AP_TOTO")));
    }


    @Test
    public void test_createIndex() throws Exception {
        assertEquals(getCreateIndexScript(),
                     scriptHelper.buildCreateIndexScript(indexOfType(NORMAL)));
    }


    @Test
    public void test_executeCreateIndex() throws Exception {
        SqlIndex index = indexOfType(NORMAL);

        executeScript(scriptHelper.buildCreateIndexScript(index));

        jdbcFixture.advanced().assertExists(index);
    }


    @Test
    public void test_createIndex_unique() throws Exception {
        assertEquals(getCreateUniqueIndexScript(),
                     scriptHelper.buildCreateIndexScript(indexOfType(UNIQUE)));
    }


    @Test
    public void test_createIndex_clustered() throws Exception {
        assertEquals(getCreateClusteredIndexScript(),
                     scriptHelper.buildCreateIndexScript(indexOfType(CLUSTERED)));
    }


    @Test
    public void test_createIndex_uniqueClustered() throws Exception {
        assertEquals(getCreateUniqueClusteredIndexScript(),
                     scriptHelper.buildCreateIndexScript(indexOfType(UNIQUE_CLUSTERED)));
    }


    @Test
    public void test_logIndexCreation() throws Exception {
        assertEquals(getLogIndexCreationScript(),
                     scriptHelper.buildLogIndexCreationScript(indexOfType(NORMAL)));
    }


    @Test
    public void test_dropConstraint() throws Exception {
        assertEquals(getDropConstraintScript(),
                     scriptHelper.buildDropConstraintScript(buildConstraint()));
    }


    @Test
    public void test_executeDropConstraint() throws Exception {
        SqlConstraint constraint = buildConstraint();
        create(constraint);

        executeScript(scriptHelper.buildDropConstraintScript(constraint));

        jdbcFixture.advanced().assertDoesntExist(constraint);
    }


    @Test
    public void test_createConstraint() throws Exception {
        assertEquals(getCreateConstraintScript(),
                     scriptHelper.buildCreateConstraintScript(buildConstraint()));
    }


    @Test
    public void test_executeCreateConstraint() throws Exception {
        SqlConstraint constraint = buildConstraint();
        executeScript(scriptHelper.buildCreateConstraintScript(constraint));

        jdbcFixture.advanced().assertExists(constraint);
    }


    @Test
    public void test_logConstraintCreation() throws Exception {
        assertEquals(getLogConstraintCreationScript(),
                     scriptHelper.buildLogConstraintCreationScript(buildConstraint()));
    }


    @Test
    public void test_customScript() throws Exception {
        final LogString logString = new LogString();
        scriptHelper.registerCustomScript("customScriptTest", new CustomScript() {
            public String buildScript(Object... parameters) {
                logString.call("buildScript", parameters[0], parameters[1]);
                return null;
            }
        });

        scriptHelper.buildCustomScript("customScriptTest", "UNE_CHAINE", 10000);

        logString.assertContent("buildScript(UNE_CHAINE, 10000)");
    }


    @Test
    public void test_customScript_unknownScript() throws Exception {
        assertEquals("", scriptHelper.buildCustomScript("customScriptTest", "UNE_CHAINE", 10000));
    }

    protected String createEmptySqlContentForTrigger(boolean useComment) {
        return useComment ? "// CUSTOM SQL CODE" : "";
    }

    @Test
    public void test_createInsertTrigger() throws Exception {
        assertEquals(getCreateInsertTriggerScript(),
                     scriptHelper.buildCreateTriggerScript(
                           buildInsertTrigger(createEmptySqlContentForTrigger(true))));
    }


    @Test
    public void test_executeInsertTrigger() throws Exception {
        createAndAssertTriggerExists(buildInsertTrigger(createEmptySqlContentForTrigger(false)));
    }

    @Test
    public void test_createUpdateTrigger() throws Exception {
        assertEquals(getCreateUpdateTriggerScript(),
                     scriptHelper.buildCreateTriggerScript(
                           buildUpdateTrigger(createEmptySqlContentForTrigger(true))));
    }


    @Test
    public void test_executeUpdateTrigger() throws Exception {
        createAndAssertTriggerExists(buildUpdateTrigger(createEmptySqlContentForTrigger(false)));
    }


    @Test
    public void test_createDeleteTrigger() throws Exception {
        assertEquals(getCreateDeleteTriggerScript(),
                     scriptHelper.buildCreateTriggerScript(
                           buildDeleteTrigger(createEmptySqlContentForTrigger(true))));
    }


    @Test
    public void test_executeDeleteTrigger() throws Exception {
        createAndAssertTriggerExists(buildDeleteTrigger(createEmptySqlContentForTrigger(false)));
    }


    @Test
    public void test_createDeleteCascadeTrigger() throws Exception {
        assertEquals(getCreateDeleteCascadeTriggerScript(),
                     scriptHelper.buildCreateTriggerScript(
                           buildDeleteCascadeTrigger(createEmptySqlContentForTrigger(true))));
    }


    @Test
    public void test_executeDeleteCascadeTrigger() throws Exception {
        createAndAssertTriggerExists(buildDeleteCascadeTrigger(createEmptySqlContentForTrigger(false)));

        jdbcFixture
              .executeUpdate("insert into AP_TOTO (ID, ID1, ID2) values ('idValue', 'id1Value', 'id2Value')");
        jdbcFixture.executeUpdate("insert into AP_CHILD1_TOTO (CHILD_ID) values ('idValue')");
        jdbcFixture.executeUpdate(
              "insert into AP_CHILD2_TOTO (CHILD_ID1, CHILD_ID2) values ('id1Value', 'id2Value')");

        jdbcFixture.executeUpdate("delete from AP_TOTO");

        jdbcFixture.assertIsEmpty(SqlTable.table("AP_TOTO"));
        jdbcFixture.assertIsEmpty(SqlTable.table("AP_CHILD1_TOTO"));
        jdbcFixture.assertIsEmpty(SqlTable.table("AP_CHILD2_TOTO"));
    }


    @Test
    public void test_createCheckRecordTrigger() throws Exception {
        assertEquals(getCreateCheckRecordTriggerScript(),
                     scriptHelper.buildCreateTriggerScript(
                           buildCheckRecordTrigger()));
    }


    @Test
    public void test_executeCheckRecordTrigger() throws Exception {
        createAndAssertTriggerExists(buildCheckRecordTrigger());

        jdbcFixture.executeUpdate("insert into AP_PARENT_TOTO (PARENT_ID) values ('test')");
        jdbcFixture.executeUpdate("insert into AP_TOTO (ID) values ('test')");

        jdbcFixture.assertContent(table("AP_PARENT_TOTO"), new String[][]{{"test"}});
        jdbcFixture.assertContent(table("AP_TOTO"), new String[][]{{"test"}});
    }


    @Test
    public void test_executeCheckRecordTrigger_errorOnInsert() throws Exception {
        createAndAssertTriggerExists(buildCheckRecordTrigger());

        try {
            jdbcFixture.executeUpdate("insert into AP_TOTO (ID) values ('test')");
            fail();
        }
        catch (SQLException e) {
            assertThat(e.getMessage(),
                       containsString(
                             "Parent does not exist in \"AP_PARENT_TOTO\". Cannot create child in \"AP_TOTO\"."));
            jdbcFixture.assertIsEmpty(SqlTable.table("AP_TOTO"));
        }
    }


    @Test
    public void test_executeCheckRecordTrigger__errorOnUpdate() throws Exception {
        createAndAssertTriggerExists(buildCheckRecordTrigger());

        jdbcFixture.executeUpdate("insert into AP_PARENT_TOTO (PARENT_ID) values ('test')");
        jdbcFixture.executeUpdate("insert into AP_TOTO (ID) values ('test')");

        try {
            jdbcFixture.executeUpdate("update AP_TOTO set ID='retest' where ID='test'");
            fail();
        }
        catch (SQLException e) {
            assertThat(e.getMessage(),
                       containsString(
                             "Parent does not exist in \"AP_PARENT_TOTO\". Cannot create child in \"AP_TOTO\"."));
            jdbcFixture.assertContent(SqlTable.table("AP_TOTO"), new String[][]{{"test"}});
        }
    }


    protected abstract String getDropTableScript();


    protected abstract String getCreateTableScript();


    protected abstract String getLogTableCreationScript();


    protected abstract String getCreateViewScript();


    protected abstract String getDropIndexScript();


    protected abstract String getCreateIndexScript();


    protected abstract String getCreateUniqueIndexScript();


    protected abstract String getCreateClusteredIndexScript();


    protected abstract String getCreateUniqueClusteredIndexScript();


    protected abstract String getLogIndexCreationScript();


    protected abstract String getDropConstraintScript();


    protected abstract String getCreateConstraintScript();


    protected abstract String getLogConstraintCreationScript();


    protected abstract String getCreateInsertTriggerScript();


    protected abstract String getCreateUpdateTriggerScript();


    protected abstract String getCreateDeleteTriggerScript();


    protected abstract String getCreateDeleteCascadeTriggerScript();


    protected abstract String getCreateCheckRecordTriggerScript();


    protected String loadFileContent(String etalonFileName) {
        try {
            return FileUtil.loadContent(getClass().getResource(etalonFileName))
                  .replaceAll(System.getProperty("line.separator"), "\n");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    protected void create(SqlTable table, String sqlContent) {
        jdbcFixture.create(table, sqlContent);
    }


    protected void create(SqlIndex index) {
        jdbcFixture.advanced().create(index);
    }


    protected void create(SqlConstraint constraint) {
        jdbcFixture.advanced().create(constraint);
    }


    protected SqlTableDefinition buildTable(String tableName, boolean identity) {
        SqlTableDefinition table = new SqlTableDefinition(tableName);
        table.setPkGenerator(true);
        table.setPrimaryKeys(Arrays.asList("MY_ID", "MY_VARCHAR"));
        SqlFieldDefinition myId = new SqlFieldDefinition("MY_ID");
        myId.setType("integer");
        myId.setRequired(true);
        myId.setIdentity(identity);
        SqlFieldDefinition myVarchar = new SqlFieldDefinition("MY_VARCHAR");
        myVarchar.setType("varchar");
        myVarchar.setRequired(false);
        myVarchar.setIdentity(false);
        myVarchar.setPrecision("15");
        myVarchar.setCheck("'A','B'");
        SqlFieldDefinition myTimestamp = new SqlFieldDefinition("MY_TIMESTAMP");
        myTimestamp.setType("timestamp");
        myTimestamp.setDefault("now");
        table.setSqlFieldDefinitions(Arrays.asList(myId, myVarchar, myTimestamp));
        return table;
    }


    private SqlView buildView() {
        return view("VU_AP_TOTO", "select * from AP_TOTO");
    }


    private SqlIndex indexOfType(Type type) {
        create(table("AP_TOTO"), "PORTFOLIO_CODE varchar(6), AUTOMATIC_UPDATE varchar(6)");

        SqlIndex index = index("X1_AP_TOTO", table("AP_TOTO"));
        index.setType(type);
        index.addField(fieldName("PORTFOLIO_CODE"));
        index.addField(fieldName("AUTOMATIC_UPDATE"));
        return index;
    }


    protected SqlConstraint buildConstraint() {
        create(table("AP_TOTO"), "PORTFOLIO_CODE varchar(6), AUTOMATIC_UPDATE varchar(6)");
        create(table("AP_MERETOTO"), "ISIN_CODE varchar(6), AUTOMATIC_UPDATE varchar(6)");

        return foreignKey("FK_MERETOTO_TOTO",
                          table("AP_TOTO"),
                          fields("PORTFOLIO_CODE", "AUTOMATIC_UPDATE"),
                          table("AP_MERETOTO"),
                          fields("ISIN_CODE", "AUTOMATIC_UPDATE"));
    }


    protected void executeScript(String scriptContent) throws IOException {
        File scriptFile = new File(directoryFixture, "script.sql");
        FileUtil.saveContent(scriptFile, scriptContent);
        jdbcFixture.advanced().executeCreateTableScriptFile(scriptFile);
    }


    protected void createAndAssertTriggerExists(SqlTrigger trigger) throws IOException {
        executeScript(scriptHelper.buildCreateTriggerScript(trigger));
        jdbcFixture.advanced().assertExists(trigger);
    }


    private SqlTrigger buildInsertTrigger(String sqlContent) {
        create(table("AP_TOTO"), "ID varchar(255), ID1 varchar(255), ID2 varchar(255)");
        return insertTrigger("TR_AP_TOTO_I",
                             table("AP_TOTO"),
                             sqlContent);
    }


    private SqlTrigger buildUpdateTrigger(String sqlContent) {
        create(table("AP_TOTO"), "ID varchar(255), ID1 varchar(255), ID2 varchar(255)");
        return updateTrigger("TR_AP_TOTO_U",
                             table("AP_TOTO"),
                             sqlContent);
    }


    private SqlTrigger buildDeleteTrigger(String sqlContent) {
        create(table("AP_TOTO"), "ID varchar(255), ID1 varchar(255), ID2 varchar(255)");
        return deleteTrigger("TR_AP_TOTO_D",
                             table("AP_TOTO"),
                             sqlContent);
    }


    private SqlTrigger buildDeleteCascadeTrigger(String sqlContent) {
        create(table("AP_TOTO"), "ID varchar(255), ID1 varchar(255), ID2 varchar(255)");
        create(table("AP_CHILD1_TOTO"), "CHILD_ID varchar(255)");
        create(table("AP_CHILD2_TOTO"), "CHILD_ID1 varchar(255), CHILD_ID2 varchar(255)");

        SqlTrigger trigger = deleteTrigger("TR_AP_TOTO_D",
                                           table("AP_TOTO"),
                                           sqlContent);
        trigger.addTableLink(table("AP_CHILD1_TOTO"),
                             fields("ID", "CHILD_ID"));
        trigger.addTableLink(table("AP_CHILD2_TOTO"),
                             fields("ID1", "CHILD_ID1"),
                             fields("ID2", "CHILD_ID2"));
        return trigger;
    }


    private SqlTrigger buildCheckRecordTrigger() {
        create(table("AP_TOTO"), "ID varchar(255)");
        create(table("AP_PARENT_TOTO"), "PARENT_ID varchar(255)");

        SqlTrigger trigger = checkRecordTrigger("TR_AP_TOTO_IU", table("AP_TOTO"));
        trigger.addTableLink(table("AP_PARENT_TOTO"), fields("ID", "PARENT_ID"));
        trigger.setSqlContent(createEmptySqlContentForTrigger(false));
        return trigger;
    }
}
