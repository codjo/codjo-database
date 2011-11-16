package net.codjo.database.api;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.runner.DefaultPreparedSelectQuery;
import net.codjo.database.api.query.runner.DefaultRowCountStrategy;
import net.codjo.database.api.query.runner.DefaultSelectQuery;
import net.codjo.database.api.query.runner.RowCountStrategy;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 */
public class DefaultDatabase implements Database {
    private Confidential confidential = new DefaultConfidential();


    public SelectQuery select(Connection connection, String sqlQuery) throws SQLException {
        return new DefaultSelectQuery(connection, sqlQuery, createRowCount(connection, sqlQuery));
    }


    public PreparedSelectQuery preparedSelectQuery(Connection connection, String sqlQuery)
          throws SQLException {
        return new DefaultPreparedSelectQuery(connection, sqlQuery, createRowCount(connection, sqlQuery));
    }


    public Confidential confidential() {
        return confidential;
    }


    private RowCountStrategy createRowCount(Connection connection, String sqlQuery) throws SQLException {
        return new DefaultRowCountStrategy(connection, sqlQuery);
    }


    private class DefaultConfidential implements Confidential {
        public int computeRowCount(Connection connection, int readRowCount) throws SQLException {
            return readRowCount + 1;
        }
    }
}
