package net.codjo.database.api.query.runner;
import net.codjo.database.api.query.Page;
import java.sql.Connection;

public abstract class AbstractSelectQuery {
    private Connection connection;
    private Page page;


    protected AbstractSelectQuery(Connection connection) {
        this.connection = connection;
    }


    public void setPage(Page page) {
        this.page = page;
    }


    protected Page getPage() {
        return page;
    }


    protected Connection getConnection() {
        return connection;
    }
}
