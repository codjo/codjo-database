/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.ScriptsStructure;
import net.codjo.database.analyse.structure.TriggerStructure;
import net.codjo.database.common.api.JdbcFixture;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.TreeSet;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
import org.junit.Before;
import org.junit.Test;
/**
 */
public class JdbcUtilTriggerTest {
    private MockBaseManager mockBaseManager;
    private Connection con;
    private TriggerStructure triggerStructure;
    private JdbcFixture jdbcFixture = JdbcFixture.newFixture();


    @Before
    public void setUp() throws Exception {
        jdbcFixture.doSetUp();
        mockBaseManager = new MockBaseManager();
        mockBaseManager.load();
        triggerStructure = new TriggerStructure();

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
    public void test_getAllTriggerDel() throws SQLException {
        prepareAllTrigger();

        JdbcUtil.doUpdate(con,
                          "create trigger TR_TABLEA on TABLEA for insert, update as\n" + "begin\n"
                          + "    declare\n" + "       @numrows  int,\n" + "       @errno    int,\n"
                          + "       @errmsg   varchar(255)\n" + "\n" + "    select * from TABLEA\n"
                          + "\n" + "\n" + "    return\n" + "\n" + "/*  Errors handling  */\n"
                          + "error:\n" + "    raiserror @errno @errmsg\n"
                          + "    rollback  transaction\n" + "end");
        String request = "drop trigger TR_TABLEA";
        computeAllTrigger(request);

        assertObjectExistsInList("TR_TABLEA", triggerStructure.getDelObjects());
        assertEquals("there is objects in new", 0, triggerStructure.getNewObjects().size());
    }


    @Test
    public void test_getAllTriggerNew() throws SQLException {
        prepareAllTrigger();
        String request =
              "create trigger TR_TABLEA on TABLEA for insert, update as\n" + "begin\n"
              + "    declare\n" + "       @numrows  int,\n" + "       @errno    int,\n"
              + "       @errmsg   varchar(255)\n" + "\n" + "    select * from TABLEA\n"
              + "\n" + "\n" + "    return\n" + "\n" + "/*  Errors handling  */\n"
              + "error:\n" + "    raiserror @errno @errmsg\n"
              + "    rollback  transaction\n" + "end";
        computeAllTrigger(request);

        assertObjectExistsInList("TR_TABLEA", triggerStructure.getNewObjects());
        assertEquals("there is objects in del", 0, triggerStructure.getDelObjects().size());
    }


    private void computeAllTrigger(String request)
          throws SQLException {
        JdbcUtil.getAllTriggers(mockBaseManager,
                                ((TreeSet)mockBaseManager.getBases()).first().toString(), triggerStructure,
                                true);

        JdbcUtil.doUpdate(con, request);
        JdbcUtil.getAllTriggers(mockBaseManager,
                                ((TreeSet)mockBaseManager.getBases()).first().toString(), triggerStructure,
                                false);

        triggerStructure.getUpdatedObjects();
    }


    private void assertObjectExistsInList(String objectName, Collection refObjectNames) {
        for (Object refObjectName : refObjectNames) {
            if (refObjectName.equals(objectName)) {
                return;
            }
        }
        fail("Can't found '" + objectName + "' in list");
    }


    private void prepareAllTrigger() throws SQLException {
        JdbcUtil.dropTable(con, "TABLEA");
        JdbcUtil.doUpdate(con,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6)  not null)");
        JdbcUtil.doUpdate(con, "drop trigger TR_TABLEA");

        ScriptsStructure scriptsStructure =
              JdbcUtil.getAllObjectScriptsInfo(mockBaseManager,
                                               ((TreeSet)mockBaseManager.getBases()).first().toString(),
                                               ((TreeSet)mockBaseManager.getBases()).first().toString());

        triggerStructure.setScriptsStructure(scriptsStructure);
    }
}
