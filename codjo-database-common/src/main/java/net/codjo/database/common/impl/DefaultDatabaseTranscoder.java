package net.codjo.database.common.impl;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import java.util.HashMap;
import java.util.Map;
/**
 *
 */
public class DefaultDatabaseTranscoder implements DatabaseTranscoder {
    private final Map<String, String> sqlFieldDefaultMap = new HashMap<String, String>();
    private final Map<String, String> sqlFieldTypeMap = new HashMap<String, String>();


    public String transcodeSqlFieldType(String sqlType) {
        if (sqlFieldTypeMap.containsKey(sqlType)) {
            return sqlFieldTypeMap.get(sqlType);
        }
        return sqlType;
    }


    public String transcodeSqlFieldDefault(String sqlDefault) {
        if (sqlFieldDefaultMap.containsKey(sqlDefault)) {
            return sqlFieldDefaultMap.get(sqlDefault);
        }
        return sqlDefault;
    }


    protected void addSqlFieldType(String key, String value) {
        sqlFieldTypeMap.put(key, value);
    }


    protected void addSqlFieldDefault(String key, String value) {
        sqlFieldDefaultMap.put(key, value);
    }
}
