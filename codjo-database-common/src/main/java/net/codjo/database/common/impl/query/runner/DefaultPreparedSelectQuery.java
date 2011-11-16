package net.codjo.database.common.impl.query.runner;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectResult;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 *
 */
public class DefaultPreparedSelectQuery extends AbstractSelectQuery implements PreparedSelectQuery {
    private PreparedStatement statement;
    private RowCountStrategy rowCountStrategy;


    public DefaultPreparedSelectQuery(Connection connection,
                                      String query,
                                      RowCountStrategy countStrategy) throws SQLException {
        super(connection);
        statement = connection.prepareStatement(query);
        rowCountStrategy = countStrategy;
    }


    public void setBoolean(int parameterIndex, boolean value) throws SQLException {
        statement.setBoolean(parameterIndex, value);
        rowCountStrategy.setBoolean(parameterIndex, value);
    }


    public void setString(int parameterIndex, String value) throws SQLException {
        statement.setString(parameterIndex, value);
        rowCountStrategy.setString(parameterIndex, value);
    }


    public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
        statement.setTimestamp(parameterIndex, value);
        rowCountStrategy.setTimestamp(parameterIndex, value);
    }


    public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
        statement.setBigDecimal(parameterIndex, value);
        rowCountStrategy.setBigDecimal(parameterIndex, value);
    }


    public void setDate(int parameterIndex, Date value) throws SQLException {
        statement.setDate(parameterIndex, value);
        rowCountStrategy.setDate(parameterIndex, value);
    }


    public void setDouble(int parameterIndex, double value) throws SQLException {
        statement.setDouble(parameterIndex, value);
        rowCountStrategy.setDouble(parameterIndex, value);
    }


    public void setInt(int parameterIndex, int value) throws SQLException {
        statement.setInt(parameterIndex, value);
        rowCountStrategy.setInt(parameterIndex, value);
    }


    public void setObject(int parameterIndex, Object value) throws SQLException {
        statement.setObject(parameterIndex, value);
        rowCountStrategy.setObject(parameterIndex, value);
    }


    public void setObject(int parameterIndex, Object value, int sqlType) throws SQLException {
        statement.setObject(parameterIndex, value, sqlType);
        rowCountStrategy.setObject(parameterIndex, value, sqlType);
    }


    public SelectResult executeQuery() throws SQLException {
        return new SelectResult(statement.executeQuery(), rowCountStrategy.computeRowCount(), getPage());
    }
}
