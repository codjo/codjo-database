package net.codjo.database.oracle.impl;
import net.codjo.database.common.impl.DefaultDatabaseTranscoder;
public class OracleDatabaseTranscoder extends DefaultDatabaseTranscoder {

    public OracleDatabaseTranscoder() {
        addSqlFieldType(LONGVARCHAR, "clob");
        addSqlFieldType(DATETIME, "timestamp");

        addSqlFieldDefault(NOW, "systimestamp");
    }
}
