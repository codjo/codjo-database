package net.codjo.database.common.api.confidential;
/**
 *
 */
public interface DatabaseTranscoder {
    public static final String DATETIME = "datetime";
    public static final String TIMESTAMP = "timestamp";
    public static final String INTEGER = "integer";
    public static final String LONGVARCHAR = "longvarchar";
    public static final String NOW = "now";


    String transcodeSqlFieldType(String sqlType);


    String transcodeSqlFieldDefault(String sqlDefault);
}
