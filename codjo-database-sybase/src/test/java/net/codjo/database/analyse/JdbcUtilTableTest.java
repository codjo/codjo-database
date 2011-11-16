/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.ColumnsStructure;
import net.codjo.database.analyse.structure.ConstraintsStructure;
import net.codjo.database.analyse.structure.ForeignKeysStructure;
import net.codjo.database.analyse.structure.IndexesStructure;
import net.codjo.database.analyse.structure.PrimaryKeysStructure;
import net.codjo.database.analyse.structure.PrivilegesStructure;
import net.codjo.database.analyse.structure.RulesStructure;
import net.codjo.database.analyse.structure.TableStructure;
import net.codjo.database.common.api.JdbcFixture;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
/**
 */
public class JdbcUtilTableTest {
    private MockBaseManager mockBaseManager;
    private TableStructure tableStructure;
    private RulesStructure ruleStructure;
    private ForeignKeysStructure foreignKeysStructure;
    private ColumnsStructure columnsStructure;
    private PrimaryKeysStructure primaryKeyStructure;
    private IndexesStructure indexesStructure;
    private Connection connection;
    private JdbcFixture jdbcFixture = JdbcFixture.newFixture();


    @Before
    public void setUp() throws Exception {
        jdbcFixture.doSetUp();
        mockBaseManager = new MockBaseManager();
        mockBaseManager.load();
        tableStructure = new TableStructure();
        ruleStructure = new RulesStructure();
        foreignKeysStructure = new ForeignKeysStructure();
        columnsStructure = new ColumnsStructure();
        primaryKeyStructure = new PrimaryKeysStructure();
        indexesStructure = new IndexesStructure();
        connection = JdbcUtil.getConnection(mockBaseManager, getDatabaseName());
    }


    @After
    public void tearDown() throws Exception {
        JdbcUtil.releaseConnection(connection);
        jdbcFixture.advanced().dropAllObjects();
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_verifyConnection() throws SQLException {
        JdbcUtil.verifyConnection(mockBaseManager.getUrl(""),
                                  mockBaseManager.getLoggin(""), mockBaseManager.getPassWord(""),
                                  mockBaseManager.getCatalog(""));
    }


    @Test
    public void test_getAllTables() throws SQLException {
        createTable("TABLEA");
        createTable("TABLEB");

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              Collections.<String>emptyList(),
                              true);
        TestUtil.assertObjectExistsInList("TABLEA", tableStructure.getRefObjectNames());
        TestUtil.assertObjectExistsInList("TABLEB", tableStructure.getRefObjectNames());

        tableStructure.setTableNames(false, new ArrayList<String>());
        TestUtil.assertObjectExistsInList("TABLEA", tableStructure.getDelObjects());
        TestUtil.assertObjectExistsInList("TABLEB", tableStructure.getDelObjects());

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              Collections.<String>emptyList(),
                              false);
        assertEquals("getDelObjects() give some results ! ", 0,
                     tableStructure.getDelObjects().size());

        tableStructure.setTableNames(true, new ArrayList<String>());
        TestUtil.assertObjectExistsInList("TABLEA", tableStructure.getNewObjects());
        TestUtil.assertObjectExistsInList("TABLEB", tableStructure.getNewObjects());
    }


    @Test
    public void test_getAllTables_ignoreTables() throws Exception {
        createTable("TABLEA");
        createTable("TABLEB");
        createTable("TABLEC");
        createTable("ANOTHER_TABLEC");
        createTable("TABLED");

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Arrays.asList("TABLEB", ".*C"));

        TestUtil.assertObjectExistsInList("TABLEA", tableStructure.getSrcObjectNames());
        TestUtil.assertObjectNotExistsInList("TABLEB", tableStructure.getSrcObjectNames());
        TestUtil.assertObjectNotExistsInList("TABLEC", tableStructure.getSrcObjectNames());
        TestUtil.assertObjectNotExistsInList("ANOTHER_TABLEC", tableStructure.getSrcObjectNames());
        TestUtil.assertObjectExistsInList("TABLED", tableStructure.getSrcObjectNames());
    }


    @Test
    public void test_getAllTablesDiff() throws SQLException {
        createTable("TABLEA", "CHAMP1 int not null", "CHAMP2 varchar(6) null", "CHAMP3 varchar(1) null");
        prepareAllTableRulesInfo();
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        true);
        createTable("TABLEA",
                    "CHAMP1 varchar(6) not null",
                    "CHAMP2 varchar(6) null",
                    "CHAMP4 varchar(4) null");

        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        false);

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              Collections.<String>emptyList(),
                              false);

        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.getUpdatedObjects();

        TestUtil.assertObjectExistsInListOfList("CHAMP1", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP2", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP3", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectExistsInListOfList("CHAMP4", tableStructure.getSrcUpdatedColumns("TABLEA"));

        TestUtil.assertObjectExistsInListOfList("CHAMP1", tableStructure.getRefUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP2", tableStructure.getRefUpdatedColumns("TABLEA"));
        TestUtil.assertObjectExistsInListOfList("CHAMP3", tableStructure.getRefUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP4", tableStructure.getRefUpdatedColumns("TABLEA"));
    }


    @Test
    public void test_columnsDiff() throws SQLException {
        createTable("TABLEA");
        prepareAllTableRulesInfo();
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        true);
        createTable("TABLEA", "CHAMP2 varchar(6) null", "CHAMP1 int not null");

        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        false);

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              Collections.<String>emptyList(),
                              false);

        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.getUpdatedObjects();
        TestUtil.assertObjectExistsInListOfList("CHAMP1", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectExistsInListOfList("CHAMP2", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectExistsInListOfList("CHAMP1", tableStructure.getRefUpdatedColumns("TABLEA"));
        TestUtil.assertObjectExistsInListOfList("CHAMP2", tableStructure.getRefUpdatedColumns("TABLEA"));
    }


    @Test
    public void test_columnsNoDiff() throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEA (CHAMP1 int not null,CHAMP2 varchar(6) null)");
        prepareAllTableRulesInfo();
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        true);
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEA (CHAMP1 int not null,CHAMP2 varchar(6) null)");

        JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                        getDatabaseName(),
                                        columnsStructure,
                                        Collections.<String>emptyList(),
                                        false);

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              Collections.<String>emptyList(),
                              false);

        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.getUpdatedObjects();

        TestUtil.assertObjectNotExistsInListOfList("CHAMP1", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP2", tableStructure.getSrcUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP1", tableStructure.getRefUpdatedColumns("TABLEA"));
        TestUtil.assertObjectNotExistsInListOfList("CHAMP2", tableStructure.getRefUpdatedColumns("TABLEA"));
    }


    @Test
    public void test_getAllTableRulesInfoRef() throws SQLException {
        JdbcUtil.doUpdate(connection, "drop rule CKC_CHAMP2");
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6)  not null constraint CKC_CHAMP2 check (CHAMP2 in ('D', 'R')))");

        prepareAllTableRulesInfo();

        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Collections.<String>emptyList(),
                                      true);
        JdbcUtil.doUpdate(connection, "alter table TABLEA drop constraint CKC_CHAMP2");
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Collections.<String>emptyList(),
                                      false);
        tableStructure.getUpdatedObjects();

        TestUtil.assertObjectExistsInList("CKC_CHAMP2", tableStructure.getRefUpdtRules("TABLEA").get(0));

        assertEquals("there is some updated objects in src", 0,
                     tableStructure.getSrcUpdtRules("TABLEA").size());
    }


    @Test
    public void test_getAllTableRulesInfoScr() throws SQLException {
        JdbcUtil.doUpdate(connection, "drop rule CKC_CHAMP2");
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6)  not null)");
        prepareAllTableRulesInfo();
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Collections.<String>emptyList(),
                                      true);
        JdbcUtil.doUpdate(connection, "create rule CKC_CHAMP2  as @CHAMP2  in ('D', 'R')");
        JdbcUtil.doUpdate(connection, "sp_bindrule CKC_CHAMP2, 'TABLEA.CHAMP2'");
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Collections.<String>emptyList(),
                                      false);
        tableStructure.getUpdatedObjects();
        TestUtil.assertObjectExistsInList("CKC_CHAMP2", tableStructure.getSrcUpdtRules("TABLEA").get(0));
        assertEquals("there is some updated objects in ref", 0,
                     tableStructure.getRefUpdtRules("TABLEA").size());
    }


    @Test
    public void test_getAllTableRulesInfo_ignoreTables() throws Exception {
        JdbcUtil.doUpdate(connection, "drop rule CKC_A");
        JdbcUtil.doUpdate(connection, "drop rule CKC_B");
        JdbcUtil.doUpdate(connection, "drop rule CKC_C");
        JdbcUtil.doUpdate(connection, "drop rule CKC_D");
        createTable("TABLEA");
        createTable("TABLEB");
        createTable("TABLEC");
        createTable("TABLED");

        prepareAllTableRulesInfo();

        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Arrays.asList("TABLEB", "TABLEC"),
                                      true);

        JdbcUtil.doUpdate(connection, "create rule CKC_A  as @CKCA in ('D', 'R')");
        JdbcUtil.doUpdate(connection, "sp_bindrule CKC_A, 'TABLEA.CHAMP2'");
        JdbcUtil.doUpdate(connection, "create rule CKC_B  as @CKC_B in ('D', 'R')");
        JdbcUtil.doUpdate(connection, "sp_bindrule CKC_B, 'TABLEB.CHAMP2'");
        JdbcUtil.doUpdate(connection, "create rule CKC_C  as @CKC_C in ('D', 'R')");
        JdbcUtil.doUpdate(connection, "sp_bindrule CKC_C, 'TABLEC.CHAMP2'");
        JdbcUtil.doUpdate(connection, "create rule CKC_D  as @CKC_D in ('D', 'R')");
        JdbcUtil.doUpdate(connection, "sp_bindrule CKC_D, 'TABLED.CHAMP2'");

        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      ruleStructure,
                                      getDatabaseName(),
                                      Arrays.asList("TABLEB", "TABLEC"),
                                      false);

        tableStructure.getUpdatedObjects();
        TestUtil.assertObjectExistsInList("CKC_A", tableStructure.getSrcUpdtRules("TABLEA").get(0));
        assertTrue(tableStructure.getSrcUpdtRules("TABLEB").isEmpty());
        assertTrue(tableStructure.getSrcUpdtRules("TABLEC").isEmpty());
        TestUtil.assertObjectExistsInList("CKC_D", tableStructure.getSrcUpdtRules("TABLED").get(0));
    }


    @Test
    public void test_getAllTableForeignKeysInfoSrc()
          throws SQLException {
        prepareAllTableForeignKeysInfo();
        computeAllTableForeignKeysInfo(
              "alter table TABLEA add constraint FK_TABLEA_TABLEB foreign key (CHAMP1A) references TABLEB (CHAMP1B)");
        tableStructure.getUpdatedObjects();
        TestUtil.assertObjectExistsInList("FK_TABLEA_TABLEB", tableStructure.getSrcUpdtFks("TABLEA").get(0));
        assertEquals("there is some updated objects in src", 0,
                     tableStructure.getRefUpdtFks("TABLEA").size());
    }


    @Test
    public void test_getAllTableForeignKeysInfoRef()
          throws SQLException {
        prepareAllTableForeignKeysInfo();
        JdbcUtil.doUpdate(connection,
                          "alter table TABLEA add constraint FK_TABLEA_TABLEB foreign key (CHAMP1A) references TABLEB (CHAMP1B)");
        computeAllTableForeignKeysInfo(
              "alter table TABLEA drop constraint FK_TABLEA_TABLEB");
        tableStructure.getUpdatedObjects();
        TestUtil.assertObjectExistsInList("FK_TABLEA_TABLEB", tableStructure.getRefUpdtFks("TABLEA").get(0));
        assertEquals("there is some updated objects in src", 0,
                     tableStructure.getSrcUpdtFks("TABLEA").size());
    }


    @Test
    public void test_getAllTablePrimaryKeysInfoRef()
          throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEB");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEB (CHAMP1B int not null,CHAMP2 varchar(6) null,constraint PK_TABLEB primary key (CHAMP1B))");
        prepareAllTablePrimaryKeysInfo();
        computeAllTablePrimaryKeysInfo("alter table TABLEB drop constraint PK_TABLEB");
        TestUtil.assertObjectExistsInList("PK_TABLEB", tableStructure.getRefUpdtPks("TABLEB").get(0));
        assertEquals("there is some updated objects in src", 0,
                     tableStructure.getSrcUpdtPks("TABLEB").size());
    }


    @Test
    public void test_getAllTablePrimaryKeysInfoSrc()
          throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEB");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEB (CHAMP1B int not null,CHAMP2 varchar(6) null)");
        prepareAllTablePrimaryKeysInfo();
        computeAllTablePrimaryKeysInfo(
              "alter table TABLEB add  constraint PK_TABLEB primary key (CHAMP1B)");
        TestUtil.assertObjectExistsInList("PK_TABLEB", tableStructure.getSrcUpdtPks("TABLEB").get(0));
        assertEquals("there is some updated objects in ref", 0,
                     tableStructure.getRefUpdtPks("TABLEB").size());
    }


    @Test
    public void test_getAllTableIndexesInfoRef() throws SQLException {
        prepareAllTableIndexesInfo();
        JdbcUtil.doUpdate(connection,
                          "create  unique  clustered  index X1_TABLEB on TABLEB (CHAMP1B,CHAMP2)");
        computeAllTableIndexesInfo("drop index TABLEB.X1_TABLEB ");
        TestUtil.assertObjectExistsInList("X1_TABLEB", tableStructure.getRefUpdtIndexes("TABLEB").get(0));
        assertEquals("there is some updated objects in src", 0,
                     tableStructure.getSrcUpdtIndexes("TABLEB").size());
    }


    @Test
    public void test_getAllTableIndexesInfoSrc() throws SQLException {
        prepareAllTableIndexesInfo();
        computeAllTableIndexesInfo(
              "create  unique  clustered  index X1_TABLEB on TABLEB (CHAMP1B,CHAMP2)");
        TestUtil.assertObjectExistsInList("X1_TABLEB", tableStructure.getSrcUpdtIndexes("TABLEB").get(0));
        assertEquals("there is some updated objects in ref", 0,
                     tableStructure.getRefUpdtIndexes("TABLEB").size());
    }


    @Test
    public void test_getAllConstraints() throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.doUpdate(connection, "create table TABLEA (CHAMP1A int not null)");
        JdbcUtil.doUpdate(connection,
                          "alter table TABLEA add constraint CKC_MY_CONSTRAINT check (CHAMP1A in ('valueA', 'valueB'))");

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager,
                                        tableStructure,
                                        getDatabaseName(),
                                        getDatabaseName(),
                                        Collections.<String>emptyList());
        columnsStructure = JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                                           getDatabaseName(),
                                                           getDatabaseName(),
                                                           Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure = JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager,
                                                                                      getDatabaseName(),
                                                                                      getDatabaseName());
        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);

        ConstraintsStructure constraintsStructure = new ConstraintsStructure();

        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            getDatabaseName(),
                                            tableStructure,
                                            constraintsStructure,
                                            Collections.<String>emptyList(),
                                            true);

        JdbcUtil.doUpdate(connection,
                          "alter table TABLEA drop constraint CKC_MY_CONSTRAINT");

        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            getDatabaseName(),
                                            tableStructure,
                                            constraintsStructure,
                                            Collections.<String>emptyList(),
                                            false);

        assertEquals("objects MAJ", "[TABLEA]", tableStructure.getUpdatedObjects().toString());
        assertEquals(1, tableStructure.getRefUpdtChecks("TABLEA").size());
        assertEquals(0, tableStructure.getSrcUpdtChecks("TABLEA").size());
    }


    private void prepareAllTableRulesInfo() throws SQLException {
        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager,
                                        tableStructure,
                                        getDatabaseName(),
                                        getDatabaseName(),
                                        Collections.<String>emptyList());
        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        columnsStructure = JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                                           getDatabaseName(),
                                                           getDatabaseName(),
                                                           Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure =
              JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager, getDatabaseName(),
                                                  getDatabaseName());
        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);
    }


    private void computeAllTableForeignKeysInfo(String request)
          throws SQLException {
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            getDatabaseName(),
                                            tableStructure,
                                            foreignKeysStructure,
                                            Collections.<String>emptyList(),
                                            true);
        JdbcUtil.doUpdate(connection, request);
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            getDatabaseName(),
                                            tableStructure,
                                            foreignKeysStructure,
                                            Collections.<String>emptyList(),
                                            false);
    }


    private void prepareAllTableForeignKeysInfo()
          throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEA");
        JdbcUtil.dropTable(connection, "TABLEB");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6) null)");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEB (CHAMP1B int not null,CHAMP2 varchar(6) null,constraint PK_TABLEB primary key (CHAMP1B))");

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager,
                                        tableStructure,
                                        getDatabaseName(),
                                        getDatabaseName(),
                                        Collections.<String>emptyList());
        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        columnsStructure = JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                                           getDatabaseName(),
                                                           getDatabaseName(),
                                                           Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure = JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager,
                                                                                      getDatabaseName(),
                                                                                      getDatabaseName());
        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);
    }


    private void prepareAllTablePrimaryKeysInfo()
          throws SQLException {
        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager,
                                        tableStructure,
                                        getDatabaseName(),
                                        getDatabaseName(),
                                        Collections.<String>emptyList());
        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());

        columnsStructure = JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                                           getDatabaseName(),
                                                           getDatabaseName(),
                                                           Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure = JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager,
                                                                                      getDatabaseName(),
                                                                                      getDatabaseName());
        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);
    }


    private void computeAllTablePrimaryKeysInfo(String request)
          throws SQLException {
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager, getDatabaseName(),
                                            tableStructure,
                                            primaryKeyStructure,
                                            Collections.<String>emptyList(),
                                            true);
        JdbcUtil.doUpdate(connection, request);
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager, getDatabaseName(),
                                            tableStructure,
                                            primaryKeyStructure,
                                            Collections.<String>emptyList(),
                                            false);
        tableStructure.getUpdatedObjects();
    }


    private void computeAllTableIndexesInfo(String request)
          throws SQLException {
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager, getDatabaseName(),
                                        tableStructure,
                                        indexesStructure,
                                        Collections.<String>emptyList(),
                                        true);
        JdbcUtil.doUpdate(connection, request);
        JdbcUtil.getAllTableIndexesInfo(mockBaseManager, getDatabaseName(),
                                        tableStructure,
                                        indexesStructure,
                                        Collections.<String>emptyList(),
                                        false);
        tableStructure.getUpdatedObjects();
    }


    private void prepareAllTableIndexesInfo() throws SQLException {
        JdbcUtil.dropTable(connection, "TABLEB");
        JdbcUtil.doUpdate(connection,
                          "create table TABLEB (CHAMP1B int not null,CHAMP2 varchar(6) null)");

        JdbcUtil.getAllTables(mockBaseManager,
                              tableStructure,
                              getDatabaseName(),
                              getDatabaseName(),
                              Collections.<String>emptyList());
        JdbcUtil.getAllTableRulesInfo(mockBaseManager,
                                      tableStructure,
                                      getDatabaseName(),
                                      getDatabaseName(),
                                      Collections.<String>emptyList());
        JdbcUtil.getAllTableForeignKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTablePrimaryKeysInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());
        JdbcUtil.getAllTableConstraintsInfo(mockBaseManager,
                                            tableStructure,
                                            getDatabaseName(),
                                            getDatabaseName(),
                                            Collections.<String>emptyList());

        columnsStructure =
              JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                              getDatabaseName(),
                                              getDatabaseName(),
                                              Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure =
              JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager, getDatabaseName(),
                                                  getDatabaseName());
        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);
    }


    private String getDatabaseName() {
        return ((TreeSet)mockBaseManager.getBases()).first().toString();
    }


    private void createTable(String tableName) {
        createTable(tableName, "CHAMP1 int not null", "CHAMP2 varchar(6) null");
    }


    private void createTable(String tableName, String... columns) {
        StringBuilder request = new StringBuilder("create table ").append(tableName).append(" (");
        boolean printComma = false;
        for (String column : columns) {
            if (printComma) {
                request.append(", ");
            }
            request.append(column);
            printComma = true;
        }
        request.append(")");

        JdbcUtil.dropTable(connection, tableName);
        JdbcUtil.doUpdate(connection, request.toString());
    }
}
