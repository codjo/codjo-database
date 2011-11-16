package net.codjo.database.oracle.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
import net.codjo.database.common.impl.script.AbstractExecSqlScriptTest;
import net.codjo.test.common.PathUtil;
import java.io.File;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class OracleExecSqlScriptTest extends AbstractExecSqlScriptTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Test
    public void test_removeProcessMessage() throws Exception {
        String processMessage = "SQL*Plus: Release 9.2.0.6.0 - Production on Wed Aug 27 11:15:18 2008\n"
                                + "\n"
                                + "Copyright (c) 1982, 2002, Oracle Corporation.  All rights reserved.\n"
                                + "\n"
                                + "Error accessing PRODUCT_USER_PROFILE\n"
                                + "Warning:  Product user profile information not loaded!\n"
                                + "You may need to run PUPBLD.SQL as SYSTEM\n"
                                + "\n"
                                + "Connected to:\n"
                                + "Oracle Database 10g Enterprise Edition Release 10.2.0.3.0 - 64bit Production\n"
                                + "With the Partitioning, OLAP and Data Mining options\n"
                                + "\n"
                                + "insert\n"
                                + "     *\n"
                                + "ERROR at line 1:\n"
                                + "ORA-00925: missing INTO keyword\n"
                                + "\n"
                                + "\n"
                                + "SQL> SQL> SQL> Disconnected from Oracle Database 10g Enterprise Edition Release 10.2.0.3.0 - 64bit Production\n"
                                + "With the Partitioning, OLAP and Data Mining options";
        String result = "insert\n"
                        + "     *\n"
                        + "ERROR at line 1:\n"
                        + "ORA-00925: missing INTO keyword";
        assertEquals(result, execSqlScript.removeConnectionMessage(processMessage));
    }


    @Test
    public void test_removeProcessMessage_notRealMessage() throws Exception {
        String processMessage = "Ceci est un message bidon";
        String result = "Ceci est un message bidon";
        assertEquals(result, execSqlScript.removeConnectionMessage(processMessage));
    }


    @Override
    protected AbstractExecSqlScript createExecSqlScriptTask() {
        return new OracleExecSqlScript();
    }


    @Override
    protected String getDeliveryFilePath(String deliveryFileName) {
        return new File(PathUtil.findResourcesFileDirectory(getClass()),
                        deliveryFileName).getPath();
    }


    @Override
    protected String getScriptDirectoryPath() {
        return PathUtil.findResourcesFileDirectory(getClass()).getPath();
    }
}
