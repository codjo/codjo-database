package net.codjo.database.oracle.impl.helper;
import net.codjo.database.common.api.RuntimeSqlException;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelperTest;
import java.sql.SQLException;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class OracleDatabaseScriptHelperTest extends AbstractDatabaseScriptHelperTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createTable_withIdentity() throws Exception {
        super.test_createTable_withIdentity();
    }


    @Test
    public void test_createTable_noIdentity() throws Exception {
        assertEquals(getCreateTableScript(),
                     scriptHelper.buildCreateTableScript(buildTable("AP_GROUP_PERSON", false)));
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCreateTable_withIdentity() throws Exception {
        super.test_executeCreateTable_withIdentity();
    }


    @Test
    public void test_executeCreateTable_noIdentity() throws Exception {
        executeScript(scriptHelper.buildCreateTableScript(buildTable("AP_GROUP_PERSON", false)));

        try {
            jdbcFixture.advanced().assertExists("AP_GROUP_PERSON");
        }
        finally {
            jdbcFixture.drop(table("AP_GROUP_PERSON"));
        }
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
        return EMPTY_SCRIPT;
    }


    @Override
    protected String getCreateViewScript() {
        return loadFileContent("view/createView_etalon.sql");
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
    protected String getLogIndexCreationScript() {
        return EMPTY_SCRIPT;
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
    protected String getDropConstraintScript() {
        return loadFileContent("constraint/dropConstraint_etalon.sql");
    }


    @Override
    protected String getCreateConstraintScript() {
        return loadFileContent("constraint/createConstraint_etalon.sql");
    }


    @Override
    protected String getLogConstraintCreationScript() {
        return EMPTY_SCRIPT;
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
    protected void create(SqlTable table, String sqlContent) {
        super.create(table, sqlContent);
        try {
            jdbcFixture.executeUpdate("grant all on " + table.getName() + " to tools");
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    @Override
    protected void create(SqlConstraint constraint) {
        try {
            jdbcFixture.executeUpdate(
                  "alter table AP_MERETOTO add (constraint fk_AP_MERETOTO primary key (ISIN_CODE, AUTOMATIC_UPDATE))");
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
        super.create(constraint);
    }
}
