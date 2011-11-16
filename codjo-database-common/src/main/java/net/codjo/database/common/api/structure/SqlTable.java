package net.codjo.database.common.api.structure;
public class SqlTable extends AbstractSqlObject {
    private boolean temporary;


    protected SqlTable() {
    }


    SqlTable(String name) {
        this(name, false);
    }


    SqlTable(String name, boolean temporary) {
        super(name);
        this.temporary = temporary;
    }


    public boolean isTemporary() {
        return temporary;
    }


    public void setTemporary(boolean temporary) {
        this.temporary = temporary;
    }


    public static SqlTable table(String tableName) {
        return new SqlTable(tableName, false);
    }


    public static SqlTable temporaryTable(String tableName) {
        return new SqlTable(tableName, true);
    }
}
