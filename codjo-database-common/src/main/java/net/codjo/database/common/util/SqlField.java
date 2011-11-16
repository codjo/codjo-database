package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlField}
 */
@Deprecated
public class SqlField extends net.codjo.database.common.api.structure.SqlField {

    public static net.codjo.database.common.api.structure.SqlField fieldName(String fieldName) {
        return net.codjo.database.common.api.structure.SqlField.fieldName(fieldName);
    }


    public static net.codjo.database.common.api.structure.SqlField fieldValue(Object value) {
        return net.codjo.database.common.api.structure.SqlField.fieldValue(value);
    }


    public static net.codjo.database.common.api.structure.SqlField fieldDefinition(String fieldName,
                                                                                 String definition) {
        return net.codjo.database.common.api.structure.SqlField.fieldDefinition(fieldName, definition);
    }


    public static net.codjo.database.common.api.structure.SqlField field(String fieldName, Object value) {
        return net.codjo.database.common.api.structure.SqlField.field(fieldName, value);
    }


    public static net.codjo.database.common.api.structure.SqlField[] fields(String... fieldNames) {
        return net.codjo.database.common.api.structure.SqlField.fields(fieldNames);
    }
}
