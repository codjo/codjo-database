package net.codjo.database.common.api;
import java.sql.SQLException;
public class RuntimeSqlException extends RuntimeException {

    public RuntimeSqlException(SQLException cause) {
        super(cause.getMessage(), cause);
    }


    public SQLException getSqlException() {
        return (SQLException)getCause();
    }
}
