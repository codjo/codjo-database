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


    protected String formatFieldName(SqlField sqlField) {
        return sqlField.getName();
    }

    public static StringBuilder appendFieldList(StringBuilder sql, Iterable<SqlField> fields) {
        sql.append(" (");
        for (SqlField field : fields) {
            sql.append(field.getName()).append(", ");
        }
        sql.delete(sql.length() - 2, sql.length());
        return sql.append(")");
    }
}
