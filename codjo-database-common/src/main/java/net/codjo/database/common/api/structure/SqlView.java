package net.codjo.database.common.api.structure;
public class SqlView extends AbstractSqlObject {
    private String body;


    protected SqlView() {
    }


    SqlView(String name) {
        super(name);
    }


    SqlView(String name, String body) {
        super(name);
        this.body = body;
    }


    public String getBody() {
        return body;
    }


    public void setBody(String body) {
        this.body = body;
    }


    public static SqlView viewName(String viewName) {
        return new SqlView(viewName);
    }


    public static SqlView view(String viewName, String body) {
        return new SqlView(viewName, body);
    }
}
