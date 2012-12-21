package net.codjo.database.common.api;

import java.sql.Connection;
import java.sql.SQLException;
import org.apache.log4j.Logger;

public abstract class TransactionManager<T> {
    private boolean previousAutoCommitMode;
    private boolean hasToChangeAutoCommit;
    private final static Logger LOGGER = Logger.getLogger(TransactionManager.class);


    public TransactionManager(Connection connection) throws SQLException {
        previousAutoCommitMode = connection.getAutoCommit();
        hasToChangeAutoCommit = hasToChangeAutoCommit();
        if (hasToChangeAutoCommit) {
            LOGGER.debug("[TransactionManager] changing autocommit mode to false");
            connection.setAutoCommit(false);
        }
    }


    boolean hasToChangeAutoCommit() {
        return new DatabaseFactory().getDatabaseQueryHelper().hasDeleteRowStrategyOnTemporaryTable();
    }


    private void release(Connection connection) throws SQLException {
        if (hasToChangeAutoCommit) {
            LOGGER.debug("[TransactionManager] restoring autocommit to:" + previousAutoCommitMode);
            if (connection != null) {
                connection.setAutoCommit(previousAutoCommitMode);
            }
        }
    }


    private void commit(Connection connection) throws SQLException {
        if (hasToChangeAutoCommit && !connection.getAutoCommit()) {
            LOGGER.debug("[TransactionManager] Commiting transaction");
            connection.commit();
        }
    }


    private void rollback(Connection connection) throws SQLException {
        if (hasToChangeAutoCommit && !connection.getAutoCommit()) {
            LOGGER.debug("[TransactionManager] Rollbacking transaction");
            connection.rollback();
        }
    }


    //TODO[segolene] peut-etre renommer la methode ?
    protected abstract T runSql(Connection connection) throws Exception;


    public final T run(Connection connection) throws Exception {
        try {
            T result = runSql(connection);
            commit(connection);
            return result;
        }
        catch (Exception ex) {
            rollback(connection);
            throw ex;
        }
        finally {
            release(connection);
        }
    }
}
