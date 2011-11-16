package net.codjo.database.mysql.impl.helper;
import net.codjo.database.common.api.structure.SqlConstraint;
import static net.codjo.database.common.api.structure.SqlField.fields;
import static net.codjo.database.common.api.structure.SqlIndex.uniqueIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.api.structure.SqlTrigger;
import static net.codjo.database.common.api.structure.SqlTrigger.triggerName;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelperTest;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;
public class MysqlDatabaseScriptHelperTest extends AbstractDatabaseScriptHelperTest {

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jdbcFixture.executeUpdate("SET storage_engine=INNODB");
    }


    @Override
    @Test(expected = AssertionError.class)
    public void test_executeCheckRecordTrigger_errorOnInsert() throws Exception {
        super.test_executeCheckRecordTrigger_errorOnInsert();
    }


    @Override
    @Test(expected = AssertionError.class)
    public void test_executeCheckRecordTrigger__errorOnUpdate() throws Exception {
        super.test_executeCheckRecordTrigger__errorOnUpdate();
    }


    @Override
    protected String getDropTableScript() {
        return loadFileContent("table/dropTable_etalon.sql");
    }


    @Override
    protected String getCreateTableScript() {
        return loadFileContent("table/createTable_etalon.sql");
    }


    @Override
    protected String getLogTableCreationScript() {
        return loadFileContent("table/logTableCreation_etalon.sql");
    }


    @Override
    protected String getDropIndexScript() {
        return loadFileContent("index/dropIndex_etalon.sql");
    }


    @Override
    protected String getCreateIndexScript() {
        return loadFileContent("index/createIndex_etalon.sql");
    }


    @Override
    protected String getCreateUniqueIndexScript() {
        return loadFileContent("index/createUniqueIndex_etalon.sql");
    }


    @Override
    protected String getCreateClusteredIndexScript() {
        return loadFileContent("index/createIndex_etalon.sql");
    }


    @Override
    protected String getCreateUniqueClusteredIndexScript() {
        return loadFileContent("index/createUniqueIndex_etalon.sql");
    }


    @Override
    protected String getLogIndexCreationScript() {
        return loadFileContent("index/logIndexCreation_etalon.sql");
    }


    @Override
    protected String getDropConstraintScript() {
        return loadFileContent("constraint/dropConstraint_etalon.sql");
    }


    @Override
    protected String getCreateConstraintScript() {
        return loadFileContent("constraint/createConstraint_etalon.sql");
    }


    @Override
    protected String getLogConstraintCreationScript() {
        return loadFileContent("constraint/logConstraintCreation_etalon.sql");
    }


    @Override
    protected String getCreateViewScript() {
        return loadFileContent("view/createView_etalon.sql");
    }


    @Override
    protected String getCreateInsertTriggerScript() {
        return loadFileContent("trigger/createInsertTrigger_etalon.sql");
    }


    @Override
    protected String getCreateUpdateTriggerScript() {
        return loadFileContent("trigger/createUpdateTrigger_etalon.sql");
    }


    @Override
    protected String getCreateDeleteTriggerScript() {
        return loadFileContent("trigger/createDeleteTrigger_etalon.sql");
    }


    @Override
    protected String getCreateDeleteCascadeTriggerScript() {
        return loadFileContent("trigger/createDeleteCascadeTrigger_etalon.sql");
    }


    @Override
    protected String getCreateCheckRecordTriggerScript() {
        return loadFileContent("trigger/createCheckRecordTrigger_etalon.sql");
    }


    @Override
    protected SqlConstraint buildConstraint() {
        SqlConstraint constraint = super.buildConstraint();
        create(uniqueIndex("X1_AP_MERETOTO", table("AP_MERETOTO"), fields("ISIN_CODE", "AUTOMATIC_UPDATE")));
        return constraint;
    }


    @Override
    protected void createAndAssertTriggerExists(SqlTrigger trigger) throws IOException {
        switch (trigger.getType()) {
            case CHECK_RECORD:
                executeScript(scriptHelper.buildCreateTriggerScript(trigger));
                jdbcFixture.advanced().assertExists(triggerName(trigger.getName() + "_i"));
                jdbcFixture.advanced().assertExists(triggerName(trigger.getName() + "_u"));
                break;

            case DELETE:
            case INSERT:
            case UPDATE:
            default:
                super.createAndAssertTriggerExists(trigger);
                break;
        }
    }
}
