package net.codjo.database.common.api;
import java.sql.Connection;
import java.sql.SQLException;
import junit.framework.TestCase;
import net.codjo.test.common.LogString;
import net.codjo.test.common.mock.ConnectionMock;
import org.junit.Test;
/**
 *
 */
public class TransactionManagerTest extends TestCase {
    private LogString log = new LogString();


    @Test
    public void test_runSwitchAutocommit() throws Exception {
        final String result = runTransactionManagerWithAutocommmitAt(true, "result");
        log.assertContent(
              "getAutoCommit(), setAutoCommit(false), runSql(), getAutoCommit(), commit(), setAutoCommit(false)");
        assertEquals("result", result);
    }


    @Test
    public void test_rundontSwitchAutocommit() throws Exception {
        final String result = runTransactionManagerWithAutocommmitAt(false, "AnotherResult");
        log.assertContent("getAutoCommit(), runSql()");
        assertEquals("AnotherResult", result);
    }


    @Test
    public void test_runWithException() throws Exception {
        String result = null;
        try {
            result = runTransactionManagerWithException(true, "result");
            fail();
        }
        catch (Exception e) {
            log.assertContent(
                  "getAutoCommit(), setAutoCommit(false), runSql(), getAutoCommit(), rollback(), setAutoCommit(false)");
            assertNull(result);
        }
    }


    private String runTransactionManagerWithAutocommmitAt(final boolean hasToChangeAutocommit,
                                                          final String resultAsString) throws Exception {
        return runTransactionManagerWithAutocommmitAt(hasToChangeAutocommit, resultAsString, new CallBack() {
            public String doIt(final String result) {
                return result;
            }
        });
    }


    private String runTransactionManagerWithException(final boolean hasToChangeAutocommit,
                                                      final String resultAsString) throws Exception {
        return runTransactionManagerWithAutocommmitAt(hasToChangeAutocommit, resultAsString, new CallBack() {
            public String doIt(final String result) throws SQLException {
                throw new SQLException("An exception has been thrown");
            }
        });
    }


    private String runTransactionManagerWithAutocommmitAt(final boolean hasToChangeAutocommit,
                                                          final String resultAsString, final CallBack callback)
          throws Exception {
        final Connection connection = new ConnectionMock(log).getStub();
        TransactionManager<String> transactionManager
              = new TransactionManager<String>(connection) {
            @Override
            protected String runSql(Connection connection) throws SQLException {
                log.call("runSql");
                return callback.doIt(resultAsString);
            }


            @Override
            boolean hasToChangeAutoCommit() {
                return hasToChangeAutocommit;
            }
        };
        return transactionManager.run(connection);
    }


    //TODO[segolene] peut-etre renommer l'interface ?
    private interface CallBack {
        String doIt(final String result) throws SQLException;
    }
}
