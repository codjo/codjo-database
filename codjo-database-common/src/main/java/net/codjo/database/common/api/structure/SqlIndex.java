package net.codjo.database.common.api.structure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class SqlIndex extends AbstractSqlObject {
    public enum Type {
        NORMAL,
        UNIQUE,
        CLUSTERED,
        UNIQUE_CLUSTERED,
        PRIMARY_KEY,
        PRIMARY_KEY_CLUSTERED;
    }

    private Type type = Type.NORMAL;
    private SqlTable table;
    private final List<SqlField> fields = new ArrayList<SqlField>();


    protected SqlIndex() {
    }


    SqlIndex(String indexName, SqlTable table) {
        this(indexName, Type.NORMAL, table);
    }


    SqlIndex(String indexName, Type type, SqlTable table, SqlField... columns) {
        super(indexName);
        this.type = type;
        this.table = table;
        this.fields.addAll(Arrays.asList(columns));
    }


    public SqlTable getTable() {
        return table;
    }


    public void setTable(SqlTable table) {
        this.table = table;
    }


    public List<SqlField> getFields() {
        return Collections.unmodifiableList(fields);
    }


    public void addField(SqlField column) {
        fields.add(column);
    }


    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }


    public static SqlIndex index(String indexName, SqlTable table) {
        return new SqlIndex(indexName, table);
    }


    public static SqlIndex normalIndex(String indexName,
                                       SqlTable table, SqlField... fields) {
        return new SqlIndex(indexName, Type.NORMAL, table, fields);
    }


    public static SqlIndex uniqueIndex(String indexName,
                                       SqlTable table,
                                       SqlField... fields) {
        return new SqlIndex(indexName, Type.UNIQUE, table, fields);
    }


    public static SqlIndex clusteredIndex(String indexName,
                                          SqlTable table, SqlField... fields) {
        return new SqlIndex(indexName, Type.CLUSTERED, table, fields);
    }


    public static SqlIndex uniqueClusteredIndex(String indexName,
                                                SqlTable table, SqlField... fields) {
        return new SqlIndex(indexName, Type.UNIQUE_CLUSTERED, table, fields);
    }


    public static SqlIndex primaryKeyIndex(String indexName,
                                           SqlTable table, SqlField... fields) {
        return new SqlIndex(indexName, Type.PRIMARY_KEY, table, fields);
    }


    public static SqlIndex primaryKeyClusteredIndex(String indexName,
                                                    SqlTable table, SqlField... fields) {
        return new SqlIndex(indexName, Type.PRIMARY_KEY_CLUSTERED, table, fields);
    }
}
