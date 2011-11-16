package net.codjo.database.common.api.structure;
import java.util.ArrayList;
import java.util.List;
public class SqlField extends AbstractSqlObject {
    private Object value;
    private String definition;


    protected SqlField() {
    }


    SqlField(String name) {
        this(name, null);
    }


    SqlField(String name, Object value) {
        super(name);
        this.value = value;
    }


    public Object getValue() {
        return value;
    }


    public void setValue(Object value) {
        this.value = value;
    }


    public String getDefinition() {
        return definition;
    }


    public void setDefinition(String definition) {
        this.definition = definition;
    }


    public static SqlField fieldName(String fieldName) {
        return new SqlField(fieldName);
    }


    public static SqlField fieldValue(Object value) {
        return new SqlField(null, value);
    }


    public static SqlField fieldDefinition(String fieldName, String definition) {
        SqlField field = new SqlField(fieldName);
        field.setDefinition(definition);
        return field;
    }


    public static SqlField field(String fieldName, Object value) {
        return new SqlField(fieldName, value);
    }


    public static SqlField[] fields(String... fieldNames) {
        List<SqlField> fields = new ArrayList<SqlField>();
        for (String fieldName : fieldNames) {
            fields.add(new SqlField(fieldName));
        }
        return fields.toArray(new SqlField[0]);
    }
}
