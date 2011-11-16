package net.codjo.database.api;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectQuery;
import java.sql.Connection;
import java.sql.SQLException;
/**
 *
 */
public interface Database {
    SelectQuery select(Connection connection, String sqlQuery) throws SQLException;


    PreparedSelectQuery preparedSelectQuery(Connection connection, String sqlQuery) throws SQLException;


    Confidential confidential();


    public interface Confidential {
        /**
         * @deprecated cette méthode n'est utilisé que pour le select via OQL qui doit disparaitre
         */
        @Deprecated
        int computeRowCount(Connection connection, int readRowCount) throws SQLException;
    }
}
