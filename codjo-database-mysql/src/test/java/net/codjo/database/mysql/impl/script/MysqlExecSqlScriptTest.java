package net.codjo.database.mysql.impl.script;
import net.codjo.database.common.impl.script.AbstractExecSqlScript;
import net.codjo.database.common.impl.script.AbstractExecSqlScriptTest;
import net.codjo.test.common.PathUtil;
import java.io.File;
import org.junit.Test;
public class MysqlExecSqlScriptTest extends AbstractExecSqlScriptTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractExecSqlScript createExecSqlScriptTask() {
        return new MysqlExecSqlScript();
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
