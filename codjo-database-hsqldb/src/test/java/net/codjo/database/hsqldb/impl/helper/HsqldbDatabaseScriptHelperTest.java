package net.codjo.database.hsqldb.impl.helper;
import net.codjo.database.common.api.structure.SqlConstraint;
import static net.codjo.database.common.api.structure.SqlField.fields;
import static net.codjo.database.common.api.structure.SqlIndex.uniqueIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelperTest;
import org.junit.Test;
public class HsqldbDatabaseScriptHelperTest extends AbstractDatabaseScriptHelperTest {

    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeDropTable() throws Exception {
        super.test_executeDropTable();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createTable_withIdentity() throws Exception {
        super.test_createTable_withIdentity();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCreateTable_withIdentity() throws Exception {
        super.test_executeCreateTable_withIdentity();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_logTableCreation() throws Exception {
        super.test_logTableCreation();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createView() throws Exception {
        super.test_createView();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCreateView() throws Exception {
        super.test_executeCreateView();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeDropIndex() throws Exception {
        super.test_executeDropIndex();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createIndex() throws Exception {
        super.test_createIndex();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCreateIndex() throws Exception {
        super.test_executeCreateIndex();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createIndex_unique() throws Exception {
        super.test_createIndex_unique();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createIndex_clustered() throws Exception {
        super.test_createIndex_clustered();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createIndex_uniqueClustered() throws Exception {
        super.test_createIndex_uniqueClustered();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createInsertTrigger() throws Exception {
        super.test_createInsertTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeDropConstraint() throws Exception {
        super.test_executeDropConstraint();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCreateConstraint() throws Exception {
        super.test_executeCreateConstraint();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeInsertTrigger() throws Exception {
        super.test_executeInsertTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createUpdateTrigger() throws Exception {
        super.test_createUpdateTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeUpdateTrigger() throws Exception {
        super.test_executeUpdateTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createDeleteTrigger() throws Exception {
        super.test_createDeleteTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeDeleteTrigger() throws Exception {
        super.test_executeDeleteTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createDeleteCascadeTrigger() throws Exception {
        super.test_createDeleteCascadeTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_createCheckRecordTrigger() throws Exception {
        super.test_createCheckRecordTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeDeleteCascadeTrigger() throws Exception {
        super.test_executeDeleteCascadeTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCheckRecordTrigger() throws Exception {
        super.test_executeCheckRecordTrigger();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCheckRecordTrigger_errorOnInsert() throws Exception {
        super.test_executeCheckRecordTrigger_errorOnInsert();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_executeCheckRecordTrigger__errorOnUpdate() throws Exception {
        super.test_executeCheckRecordTrigger__errorOnUpdate();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_dropIndex() throws Exception {
        super.test_dropIndex();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_logIndexCreation() throws Exception {
        super.test_logIndexCreation();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_dropConstraint() throws Exception {
        super.test_dropConstraint();
    }


    @Override
    @Test(expected = UnsupportedOperationException.class)
    public void test_logConstraintCreation() throws Exception {
        super.test_logConstraintCreation();
    }


    @Override
    protected String getDropTableScript() {
        return loadFileContent("table/dropTable_etalon.sql");
    }


    @Override
    protected String getCreateTableScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getLogTableCreationScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateViewScript() {
        return loadFileContent("view/createView_etalon.sql");
    }


    @Override
    protected String getDropIndexScript() {
        return NOT_IMPLEMENTED_SCRIPT;
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
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateUniqueClusteredIndexScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getLogIndexCreationScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getDropConstraintScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateConstraintScript() {
        return loadFileContent("constraint/createConstraint_etalon.sql");
    }


    @Override
    protected String getLogConstraintCreationScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateInsertTriggerScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateUpdateTriggerScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateDeleteTriggerScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateDeleteCascadeTriggerScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected String getCreateCheckRecordTriggerScript() {
        return NOT_IMPLEMENTED_SCRIPT;
    }


    @Override
    protected SqlConstraint buildConstraint() {
        SqlConstraint constraint = super.buildConstraint();
        create(uniqueIndex("X1_AP_MERETOTO", table("AP_MERETOTO"), fields("ISIN_CODE", "AUTOMATIC_UPDATE")));
        return constraint;
    }
}
