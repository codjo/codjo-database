package net.codjo.database.common.impl.query.runner;
import net.codjo.database.common.api.query.PreparedQuery;
import java.sql.SQLException;
/**
 *
 */
public interface RowCountStrategy extends PreparedQuery {
    int computeRowCount() throws SQLException;
}
