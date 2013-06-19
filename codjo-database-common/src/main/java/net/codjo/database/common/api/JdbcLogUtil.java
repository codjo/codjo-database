package net.codjo.database.common.api;
import java.sql.SQLException;
import java.sql.SQLWarning;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

/**
 * Utility class for logging JDBC errors and warnings.
 */
public class JdbcLogUtil {
    private JdbcLogUtil() {
    }


    /**
     * Log the SQL state at {@link org.apache.log4j.Level.TRACE} level. The parameter <param>sqlException</param> gives the SQL state,
     * which is compound of an optional error and optional warnings.
     */
    public static void traceWarningsAndError(Logger logger, SQLException sqlException) {
        logWarningsAndError(logger, Level.TRACE, sqlException);
    }


    /**
     * Log the SQL state at provided level. The parameter <param>sqlException</param> gives the SQL state, which is
     * compound of an optional error and optional warnings.
     */
    public static void logWarningsAndError(Logger logger, Level level, SQLException sqlException) {
        StringBuilder msg = new StringBuilder(128);
        appendWarningsAndError(msg, sqlException);
        logger.log(level, msg.toString());
    }


    /**
     * Append the SQL state to the provided buffer. The parameter <param>sqlException</param> gives the SQL state, which
     * is compound of an optional error and optional warnings.
     */
    public static void appendWarningsAndError(StringBuilder msg, SQLException sqlException) {
        SQLException error = null;

        if (sqlException != null) {
            msg.append("\n----------- BEGIN trace SQL state -------------\n");
            SQLException ex = sqlException;
            while (ex != null) {
                msg.append("- ");
                if (ex instanceof SQLWarning) {
                    msg.append("WARNING : ").append(ex.getMessage());
                }
                else {
                    msg.append("ERROR : ").append(ex.getMessage());
                    if (error != null) {
                        msg.append("error already defined");
                    }
                    error = ex;
                }
                if (msg.charAt(msg.length() - 1) != '\n') {
                    msg.append('\n');
                }
                appendNewLineIfNone(msg);
                msg.append("\tSQLState: ").append(ex.getSQLState());
                appendNewLineIfNone(msg);
                msg.append("\tErrorCode: ").append(ex.getErrorCode()).append('\n');
                ex = ex.getNextException();
            }
            msg.append("----------- END   trace SQL state -------------\n");
        }
        else {
            msg.append("\nSQL state: no error or warnings\n");
        }
    }


    private static void appendNewLineIfNone(StringBuilder msg) {
        if (msg.charAt(msg.length() - 1) != '\n') {
            msg.append('\n');
        }
    }
}
