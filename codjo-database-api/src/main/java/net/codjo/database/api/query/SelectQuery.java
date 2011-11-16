package net.codjo.database.api.query;
import java.sql.SQLException;

public interface SelectQuery {
    void setPage(Page page);


    SelectResult execute() throws SQLException;
}
