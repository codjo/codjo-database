package net.codjo.database.common.util;
import java.sql.SQLException;
/**
 * @deprecated {@link net.codjo.database.common.api.RuntimeSqlException}
 */
@Deprecated
public class RuntimeSqlException extends net.codjo.database.common.api.RuntimeSqlException {

    public RuntimeSqlException(SQLException cause) {
        super(cause);
    }
}
