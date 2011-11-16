package net.codjo.database.common.api.query;
import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Timestamp;
/**
 *
 */
public interface PreparedQuery {
    void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException;


    void setBoolean(int parameterIndex, boolean value) throws SQLException;


    void setDate(int parameterIndex, Date value) throws SQLException;


    void setDouble(int parameterIndex, double value) throws SQLException;


    void setInt(int parameterIndex, int value) throws SQLException;


    void setObject(int parameterIndex, Object value) throws SQLException;


    void setObject(int parameterIndex, Object value, int sqlType) throws SQLException;


    void setString(int parameterIndex, String value) throws SQLException;


    void setTimestamp(int parameterIndex, Timestamp value) throws SQLException;
}
