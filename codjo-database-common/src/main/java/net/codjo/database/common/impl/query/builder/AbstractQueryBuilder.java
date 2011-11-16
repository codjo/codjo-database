package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
public abstract class AbstractQueryBuilder {

    public abstract String get();


    protected String formatFieldValue(SqlField sqlField) {
        Object value = sqlField.getValue();
        if (value instanceof String) {
            return "'" + value + "'";
        }
        return value.toString();
    }
}
