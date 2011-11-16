package net.codjo.database.api.query.runner;
import net.codjo.database.api.query.PreparedQuery;
import java.sql.SQLException;

public interface RowCountStrategy extends PreparedQuery {
    int computeRowCount() throws SQLException;
}
