package net.codjo.database.common.api;
import java.sql.SQLException;
import org.apache.log4j.Appender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.spi.LoggingEvent;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
/**
 * TODO[codjo-test] extract a Log4jFixture ?
 */
public class JdbcLogUtilTest {
    private Appender appender;
    private Logger logger;
    private ArgumentCaptor<LoggingEvent> arguments;


    @Before
    public void doSetup() {
        appender = mock(Appender.class);
        logger = Logger.getRootLogger();
        logger.addAppender(appender);
        arguments = ArgumentCaptor.forClass(LoggingEvent.class);
        logger.setLevel(Level.TRACE);
    }


    @After
    public void doTearDown() {
        logger.removeAppender(appender);
    }


    @Test
    public void test_logWithLogLevel() throws Exception {
        JdbcLogUtil.logWarningsAndError(logger, Level.INFO, new SQLException("La raison c'est que ca va pas trop",
                                                                             "SQL_STATE_MOCK",
                                                                             52000));

        verifyLogger(Level.INFO, "\n----------- BEGIN trace SQL state -------------\n"
                                 + "- ERROR : La raison c'est que ca va pas trop\n"
                                 + "\tSQLState: SQL_STATE_MOCK\n"
                                 + "\tErrorCode: 52000\n"
                                 + "----------- END   trace SQL state -------------\n");
    }


    @Test
    public void test_logWithNullException() throws Exception {
        JdbcLogUtil.traceWarningsAndError(logger, null);
        verifyLogger(Level.TRACE, "\nSQL state: no error or warnings\n");
    }


    @Test
    public void test_logWithEmptySqlException() throws Exception {
        JdbcLogUtil.traceWarningsAndError(logger, new SQLException());
        verifyLogger(Level.TRACE, "\n----------- BEGIN trace SQL state -------------\n"
                                  + "- ERROR : null\n"
                                  + "\tSQLState: null\n"
                                  + "\tErrorCode: 0\n"
                                  + "----------- END   trace SQL state -------------\n");
    }


    @Test
    public void test_logWithErrorCode() throws Exception {
        JdbcLogUtil.traceWarningsAndError(logger, new SQLException("La raison c'est que ca va pas trop",
                                                                   "SQL_STATE_MOCK",
                                                                   52000));
        verifyLogger(Level.TRACE, "\n----------- BEGIN trace SQL state -------------\n"
                                  + "- ERROR : La raison c'est que ca va pas trop\n"
                                  + "\tSQLState: SQL_STATE_MOCK\n"
                                  + "\tErrorCode: 52000\n"
                                  + "----------- END   trace SQL state -------------\n");
    }


    @Test
    public void test_logNoErrorCode() throws Exception {
        JdbcLogUtil.traceWarningsAndError(logger,
                                          new SQLException("La raison c'est que ca va pas trop", "SQL_STATE_MOCK"));
        verifyLogger(Level.TRACE, "\n----------- BEGIN trace SQL state -------------\n"
                                  + "- ERROR : La raison c'est que ca va pas trop\n"
                                  + "\tSQLState: SQL_STATE_MOCK\n"
                                  + "\tErrorCode: 0\n"
                                  + "----------- END   trace SQL state -------------\n");
    }


    private void verifyLogger(Level logLevel, String message) {
        verify(appender).doAppend(arguments.capture());
        assertThat(arguments.getValue().getLevel(), is(logLevel));
        assertThat((String)arguments.getValue().getMessage(), is(message));
    }
}
