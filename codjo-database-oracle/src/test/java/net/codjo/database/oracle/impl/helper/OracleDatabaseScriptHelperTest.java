package net.codjo.database.oracle.impl.helper;
import java.sql.SQLException;
import java.util.Arrays;
import net.codjo.database.common.api.RuntimeSqlException;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlFieldDefinition;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlTableDefinition;
import net.codjo.database.common.impl.helper.AbstractDatabaseScriptHelperTest;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
public class OracleDatabaseScriptHelperTest extends AbstractDatabaseScriptHelperTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Test
    public void test_createTable_withReservedKeyword() throws Exception {
        SqlTableDefinition table = new SqlTableDefinition("AP_TABLE");
        SqlFieldDefinition comment = new SqlFieldDefinition("COMMENT");
        comment.setType("varchar");
        comment.setPrecision("15");
        comment.setCheck("'A','B'");
        table.setSqlFieldDefinitions(Arrays.asList(comment));

        String definition = scriptHelper.buildCreateTableScript(table);

        assertThat(cleanUp(definition), is("create table AP_TABLE("
                                           + "    \"COMMENT\"      varchar(15)  null"
                                           + " constraint CKC_COMMENT check (\"COMMENT\" in ('A','B'))"
                                           + ");"));
    }


    @Override
    public void test_createTable_withIdentity() throws Exception {
        super.test_createTable_withIdentity();
    }


    @Test
    public void test_createTable_noIdentity() throws Exception {
        assertEquals(getCreateTableScript(),
                     scriptHelper.buildCreateTableScript(buildTable("AP_GROUP_PERSON", false)));
    }


    @Override
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


    private static String cleanUp(String definition) {
        return definition.replaceAll("\n", "");
    }
}
