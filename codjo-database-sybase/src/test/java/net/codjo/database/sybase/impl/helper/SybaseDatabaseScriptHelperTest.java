package net.codjo.database.sybase.impl.helper;
import net.codjo.database.common.api.structure.SqlConstraint;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlField.fields;
import net.codjo.database.common.api.structure.SqlIndex;
import static net.codjo.database.common.api.structure.SqlIndex.normalIndex;
import static net.codjo.database.common.api.structure.SqlIndex.uniqueIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelperTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class SybaseDatabaseScriptHelperTest extends AbstractDatabaseScriptHelperTest {

    @Test
    public void test_dropIndex_longIndexName() throws Exception {
        assertEquals(loadFileContent("index/dropIndex_longName_etalon.sql"),
                     scriptHelper.buildDropIndexScript(createIndexWithLongName()));
    }


    @Test
    public void test_createIndex_longIndexName() throws Exception {
        assertEquals(loadFileContent("index/createIndex_longName_etalon.sql"),
                     scriptHelper.buildCreateIndexScript(createIndexWithLongName()));
    }


    @Test
    public void test_logIndexCreation_longIndexName() throws Exception {
        assertEquals(loadFileContent("index/logIndexCreation_longName_etalon.sql"),
                     scriptHelper.buildLogIndexCreationScript(createIndexWithLongName()));
    }


    @Test
    public void test_createGap() throws Exception {
        assertEquals(loadFileContent("gap/createGap_etalon.sql"),
                     scriptHelper.buildCustomScript("createGap",
                                                    table("AP_TOTO"), "10000"));
    }


    @Test
    public void test_logGapCreation() throws Exception {
        assertEquals(loadFileContent("gap/logGapCreation_etalon.sql"),
                     scriptHelper.buildCustomScript("logGapCreation",
                                                    table("AP_TOTO"), "10000"));
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
    protected String getCreateUniqueIndexScript() {
        return loadFileContent("index/createUniqueIndex_etalon.sql");
    }


    @Override
    protected String getCreateClusteredIndexScript() {
        return loadFileContent("index/createClusteredIndex_etalon.sql");
    }


    @Override
    protected String getCreateUniqueClusteredIndexScript() {
        return loadFileContent("index/createUniqueClusteredIndex_etalon.sql");
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
        create(uniqueIndex("X1_AP_TOTO", table("AP_TOTO"), fields("PORTFOLIO_CODE", "AUTOMATIC_UPDATE")));
        create(uniqueIndex("X1_AP_MERETOTO", table("AP_MERETOTO"), fields("ISIN_CODE", "AUTOMATIC_UPDATE")));
        return constraint;
    }


    private SqlIndex createIndexWithLongName() {
        return normalIndex("X1_INDEX_WITH_VERY_VERY_LONG_NAME",
                           table("AP_TOTO"), fieldName("ID"));
    }
}
