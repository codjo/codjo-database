/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.ColumnsStructure;
import net.codjo.database.analyse.structure.PrivilegesStructure;
import net.codjo.database.analyse.structure.ScriptsStructure;
import net.codjo.database.analyse.structure.ViewStructure;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;
/**
 */
public class JdbcUtilViewTest extends TestCase {
    private MockBaseManager mockBaseManager;
    private Connection con;
    private ViewStructure viewStructure;


    @Override
    public void setUp()
          throws IOException, ParserConfigurationException, SAXException,
                 ClassNotFoundException, SQLException {
        mockBaseManager = new MockBaseManager();
        mockBaseManager.load();
        viewStructure = new ViewStructure();
        con = JdbcUtil.getConnection(mockBaseManager,
                                     ((TreeSet)mockBaseManager.getBases()).first().toString());
    }


    @Override
    protected void tearDown() throws Exception {
        JdbcUtil.releaseConnection(con);
    }


    public void test_getAllViewDel() throws SQLException {
        prepareAllView();

        JdbcUtil.doUpdate(con, "create view VU_ALL_TABLEA  as  select *  from TABLEA");
        String request = "drop view VU_ALL_TABLEA";
        computeAllView(request);

        assertObjectExistsInList("VU_ALL_TABLEA", viewStructure.getDelObjects());
        assertEquals(0, viewStructure.getNewObjects().size());
    }


    public void test_getAllViewNew() throws SQLException {
        prepareAllView();
        String request = "create view VU_ALL_TABLEA  as  select *  from TABLEA";
        computeAllView(request);

        assertObjectExistsInList("VU_ALL_TABLEA", viewStructure.getNewObjects());
        assertEquals(0, viewStructure.getDelObjects().size());
    }


    private void computeAllView(String request) throws SQLException {
        JdbcUtil.getAllViews(mockBaseManager,
                             ((TreeSet)mockBaseManager.getBases()).first().toString(), viewStructure, true);

        JdbcUtil.doUpdate(con, request);
        JdbcUtil.getAllViews(mockBaseManager,
                             ((TreeSet)mockBaseManager.getBases()).first().toString(), viewStructure, false);

        viewStructure.getUpdatedObjects();
    }


    private void assertObjectExistsInList(String objectName, Collection refObjectNames) {
        Iterator it = refObjectNames.iterator();
        while (it.hasNext()) {
            if (it.next().equals(objectName)) {
                return;
            }
        }
        fail("Can't found '" + objectName + "' in list");
    }


    private void prepareAllView() throws SQLException {
        JdbcUtil.dropTable(con, "TABLEA");
        JdbcUtil.doUpdate(con,
                          "create table TABLEA (CHAMP1A int not null,CHAMP2 varchar(6)  not null)");
        JdbcUtil.doUpdate(con, "drop view  VU_ALL_TABLEA");

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

        viewStructure.setColumnsStructure(columnsStructure);
        viewStructure.setPrivilegesStructure(privilegesStructure);
        viewStructure.setScriptsStructure(scriptsStructure);
    }
}
