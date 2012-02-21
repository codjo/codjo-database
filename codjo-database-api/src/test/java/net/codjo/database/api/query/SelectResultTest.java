package net.codjo.database.api.query;
import net.codjo.test.common.LogString;
import net.codjo.test.common.mock.ResultSetMock;
import net.codjo.test.common.mock.StatementMock;
import org.junit.Test;
/**
 *
 */
public class SelectResultTest {
    private LogString log = new LogString();


    @Test
    public void testCloseStatement() throws Exception {
        SelectResult result = new SelectResult(new ResultSetMock(new LogString("resultSet", log)).getStub(),
                                               new StatementMock(new LogString("statement", log)).getStub(), 0, null);

        result.close();

        log.assertContent("resultSet.close(), statement.close()");
    }
}
