package net.codjo.database.api.query;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Date;
import java.sql.Timestamp;
import java.math.BigDecimal;

public class SqlAdapter {
    private SqlAdapter() {
    }


    public static PreparedQuery wrap(PreparedStatement statement) {
        return new Adapter(statement);
    }


    private static class Adapter implements PreparedQuery {
        private PreparedStatement statement;


        private Adapter(PreparedStatement statement) {
            this.statement = statement;
        }


        public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
            statement.setBigDecimal(parameterIndex, value);
        }


        public void setBoolean(int parameterIndex, boolean value) throws SQLException {
            statement.setBoolean(parameterIndex, value);
        }


        public void setDate(int parameterIndex, Date value) throws SQLException {
            statement.setDate(parameterIndex, value);
        }


        public void setDouble(int parameterIndex, double value) throws SQLException {
            statement.setDouble(parameterIndex, value);
        }


        public void setInt(int parameterIndex, int value) throws SQLException {
            statement.setInt(parameterIndex, value);
        }


        public void setObject(int parameterIndex, Object value) throws SQLException {
            statement.setObject(parameterIndex, value);
        }


        public void setObject(int parameterIndex, Object value, int sqlType) throws SQLException {
            statement.setObject(parameterIndex, value, sqlType);
        }


        public void setString(int parameterIndex, String value) throws SQLException {
            statement.setString(parameterIndex, value);
        }


        public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
            statement.setTimestamp(parameterIndex, value);
        }
    }
}
