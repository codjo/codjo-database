package net.codjo.database.api.query.runner;
import net.codjo.database.api.query.SelectQuery;
import net.codjo.database.api.query.SelectResult;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DefaultSelectQuery extends AbstractSelectQuery implements SelectQuery {
    private final String query;
    private RowCountStrategy rowCountStrategy;


    public DefaultSelectQuery(Connection connection, String query, RowCountStrategy rowCountStrategy)
          throws SQLException {
        super(connection);
        this.query = query;
        this.rowCountStrategy = rowCountStrategy;
    }


    public SelectResult execute() throws SQLException {
        Statement statement = getConnection().createStatement();
        ResultSet resultSet = statement.executeQuery(query);
        return new SelectResult(resultSet, statement, rowCountStrategy.computeRowCount(), getPage());
    }
}
