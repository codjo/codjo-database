package net.codjo.database.sybase.impl.query.runner;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
/**
 *
 */
public class SybaseRowCountStrategy implements RowCountStrategy {
    private final Connection connection;


    public SybaseRowCountStrategy(Connection connection) {
        this.connection = connection;
    }


    public static int computeRowCount(Connection connection) throws SQLException {
        int recordCount = 0;
        Statement stmt = connection.createStatement();
        try {
            ResultSet results = stmt.executeQuery("SELECT @@ROWCOUNT");
            if (results.next()) {
                recordCount = results.getInt(1);
            }
            results.close();
        }
        finally {
            stmt.close();
        }
        return recordCount;
    }


    public int computeRowCount() throws SQLException {
        return computeRowCount(connection);
    }


    public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
    }


    public void setBoolean(int parameterIndex, boolean value) throws SQLException {
    }


    public void setDate(int parameterIndex, Date value) throws SQLException {
    }


    public void setDouble(int parameterIndex, double value) throws SQLException {
    }


    public void setInt(int parameterIndex, int value) throws SQLException {
    }


    public void setObject(int parameterIndex, Object value) throws SQLException {
    }


    public void setObject(int parameterIndex, Object value, int sqlType) throws SQLException {
    }


    public void setString(int parameterIndex, String value) throws SQLException {
    }


    public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
    }
}
