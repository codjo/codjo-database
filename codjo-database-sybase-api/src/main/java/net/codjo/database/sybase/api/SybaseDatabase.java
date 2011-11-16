package net.codjo.database.sybase.api;
import net.codjo.database.api.Database;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.runner.DefaultPreparedSelectQuery;
import net.codjo.database.api.query.runner.DefaultSelectQuery;
import net.codjo.database.sybase.api.query.runner.SybaseRowCountStrategy;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 */
public class SybaseDatabase implements Database {
    private SybaseConfidential confidential = new SybaseConfidential();


    public SelectQuery select(Connection connection, String sqlQuery) throws SQLException {
        return new DefaultSelectQuery(connection, sqlQuery, new SybaseRowCountStrategy(connection));
    }


    public PreparedSelectQuery preparedSelectQuery(Connection connection, String sqlQuery)
          throws SQLException {
        return new DefaultPreparedSelectQuery(connection, sqlQuery, new SybaseRowCountStrategy(connection));
    }


    public Confidential confidential() {
        return confidential;
    }


    private static class SybaseConfidential implements Confidential {
        public int computeRowCount(Connection connection, int readRowCount) throws SQLException {
            return SybaseRowCountStrategy.computeRowCount(connection);
        }
    }
}
