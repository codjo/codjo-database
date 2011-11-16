package net.codjo.database.common.api.confidential.query;
import java.sql.SQLException;
/**
 *
 */
public interface SelectQuery {
    void setPage(Page page);


    SelectResult executeQuery() throws SQLException;
}
