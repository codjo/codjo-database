/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.ColumnsStructure;
import net.codjo.database.analyse.structure.PrivilegesStructure;
import net.codjo.database.analyse.structure.ProcedureStructure;
import net.codjo.database.analyse.structure.ScriptsStructure;
import net.codjo.database.common.api.JdbcFixture;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
/**
 */
public class JdbcUtilProcTest {
    private MockBaseManager mockBaseManager;
    private Connection con;
    private ProcedureStructure procedureStructure;
    private JdbcFixture jdbcFixture = JdbcFixture.newFixture();


    @Before
    public void setUp() throws Exception {
        jdbcFixture.doSetUp();
        mockBaseManager = new MockBaseManager();
        mockBaseManager.load();
        procedureStructure = new ProcedureStructure();
        con = JdbcUtil.getConnection(mockBaseManager,
                                     ((TreeSet)mockBaseManager.getBases()).first().toString());
    }


    @After
    public void tearDown() throws Exception {
        JdbcUtil.releaseConnection(con);
        jdbcFixture.advanced().dropAllObjects();
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_getAllStroredProceduresDel() throws SQLException {
        prepareAllStroredProcedures();

        JdbcUtil.doUpdate(con, "create proc sp_acces_TABLEA as begin SELECT * FROM TABLEA end");
        fillProcedureStructure("drop proc sp_acces_TABLEA");

        TestUtil.assertObjectExistsInList("sp_acces_TABLEA", procedureStructure.getDelObjects());
        assertEquals(0, procedureStructure.getNewObjects().size());
    }


    @Test
    public void test_getAllStroredProceduresNew() throws SQLException {
        prepareAllStroredProcedures();
        fillProcedureStructure("create proc sp_acces_TABLEA as begin SELECT * FROM TABLEA end");

        TestUtil.assertObjectExistsInList("sp_acces_TABLEA", procedureStructure.getNewObjects());
        assertEquals(0, procedureStructure.getDelObjects().size());
    }


    @Test
    public void test_getAllStroredProcedures_dbaProceduresCass() throws SQLException {
        prepareAllStroredProcedures();

        fillProcedureStructure("create proc sp_dba_Bobo as begin SELECT * FROM TABLEA end");

        assertEquals(0, procedureStructure.getNewObjects().size());
        assertEquals(0, procedureStructure.getDelObjects().size());
    }


    @Test
    public void test_getAllStoredProcedure_ignoreStoredProcedures() throws Exception {
        prepareAllStroredProcedures();

        JdbcUtil.doUpdate(con, "create proc sp_proc1 as begin SELECT * FROM TABLEA end");
        JdbcUtil.doUpdate(con, "create proc sp_proc2 as begin SELECT * FROM TABLEA end");
        JdbcUtil.doUpdate(con, "create proc sp_proc3 as begin SELECT * FROM TABLEA end");
        JdbcUtil.doUpdate(con, "create proc sp_another_proc3 as begin SELECT * FROM TABLEA end");
        JdbcUtil.doUpdate(con, "create proc sp_proc4 as begin SELECT * FROM TABLEA end");

        String baseName = mockBaseManager.getBases().iterator().next();
        List<String> procedures = JdbcUtil.getAllStroredProcedures(mockBaseManager,
                                                                   procedureStructure,
                                                                   baseName,
                                                                   Arrays.asList("sp_proc2", ".*3"),
                                                                   true);

        assertEquals(2, procedures.size());
        assertTrue(procedures.contains("sp_proc1"));
        assertFalse(procedures.contains("sp_proc2"));
        assertFalse(procedures.contains("sp_proc3"));
        assertFalse(procedures.contains("sp_another_proc3"));
        assertTrue(procedures.contains("sp_proc4"));
    }


    private void prepareAllStroredProcedures() throws SQLException {
        JdbcUtil.dropTable(con, "TABLEA");
        JdbcUtil.doUpdate(con,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6)  not null)");
        JdbcUtil.doUpdate(con, "drop proc sp_acces_TABLEA");

        ColumnsStructure columnsStructure =
              JdbcUtil.getAllTableColumnsInfo(mockBaseManager,
                                              ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                              ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                              Collections.<String>emptyList());
        PrivilegesStructure privilegesStructure =
              JdbcUtil.getAllObjectPrivilegesInfo(mockBaseManager,
                                                  ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                                  ((TreeSet)mockBaseManager.getBases()).first().toString());
        ScriptsStructure scriptsStructure =
              JdbcUtil.getAllObjectScriptsInfo(mockBaseManager,
                                               ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                               ((TreeSet)mockBaseManager.getBases()).first().toString());

        procedureStructure.setColumnsStructure(columnsStructure);
        procedureStructure.setPrivilegesStructure(privilegesStructure);
        procedureStructure.setScriptsStructure(scriptsStructure);
    }


    private void fillProcedureStructure(String request)
          throws SQLException {
        JdbcUtil.getAllStroredProcedures(mockBaseManager,
                                         procedureStructure,
                                         ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                         Collections.<String>emptyList(),
                                         true);

        JdbcUtil.doUpdate(con, request);
        JdbcUtil.getAllStroredProcedures(mockBaseManager,
                                         procedureStructure,
                                         ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                         Collections.<String>emptyList(),
                                         false);

        procedureStructure.getUpdatedObjects();
    }
}
