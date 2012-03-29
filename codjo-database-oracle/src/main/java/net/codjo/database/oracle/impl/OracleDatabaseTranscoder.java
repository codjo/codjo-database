package net.codjo.database.oracle.impl;
import net.codjo.database.common.impl.DefaultDatabaseTranscoder;
public class OracleDatabaseTranscoder extends DefaultDatabaseTranscoder {

    public OracleDatabaseTranscoder() {
        addSqlFieldType(LONGVARCHAR, "clob");
        addSqlFieldType(DATETIME, "timestamp");
        addSqlFieldType("text", "clob");
        addSqlFieldType("bit", "number(1)");
        addSqlFieldType("varchar", "varchar2");

        addSqlFieldDefault(NOW, "systimestamp");
    }
}
