/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.UserDataType;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import junit.framework.TestCase;
import org.xml.sax.SAXException;
/**
 */
public class JdbcUtilUDTTest extends TestCase {
    private MockBaseManager mockBaseManager;
    private Connection con;
    private UserDataType userDataType;

    public void setUp()
            throws IOException, ParserConfigurationException, SAXException, 
                ClassNotFoundException, SQLException {
        mockBaseManager = new MockBaseManager();
        mockBaseManager.load();
        userDataType = new UserDataType();

        con = JdbcUtil.getConnection(mockBaseManager,
                ((TreeSet)mockBaseManager.getBases()).first().toString());
    }


    protected void tearDown() throws Exception {
        JdbcUtil.releaseConnection(con);
    }


    public void test_getAllUDTDel() throws SQLException {
        prepareAllUDT();

        JdbcUtil.doUpdate(con, "sp_addtype MONINT,int");
        String request = "sp_droptype MONINT";
        computeAllUDT(request);

        assertObjectExistsInList("MONINT", userDataType.getDelObjects());
        assertEquals("there is objects in new", 0, userDataType.getNewObjects().size());
    }


    public void test_getAllUDTNew() throws SQLException {
        prepareAllUDT();
        String request = "sp_addtype MONINT,int";
        computeAllUDT(request);

        assertObjectExistsInList("MONINT", userDataType.getNewObjects());
        assertEquals("there is objects in del", 0, userDataType.getDelObjects().size());
    }


    private void computeAllUDT(String request) throws SQLException {
        JdbcUtil.getAllUserDataTypesInfo(mockBaseManager,
            ((TreeSet)mockBaseManager.getBases()).first().toString(), userDataType, true);
        JdbcUtil.doUpdate(con, request);
        JdbcUtil.getAllUserDataTypesInfo(mockBaseManager,
            ((TreeSet)mockBaseManager.getBases()).first().toString(), userDataType, false);
        userDataType.getUpdatedObjects();
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


    private void prepareAllUDT() {
        JdbcUtil.doUpdate(con, "sp_droptype MONINT");
    }
}
