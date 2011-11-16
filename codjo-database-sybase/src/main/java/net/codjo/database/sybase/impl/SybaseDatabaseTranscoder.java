package net.codjo.database.sybase.impl;
import net.codjo.database.common.impl.DefaultDatabaseTranscoder;
/**
 *
 */
public class SybaseDatabaseTranscoder extends DefaultDatabaseTranscoder {

    public SybaseDatabaseTranscoder() {
        addSqlFieldType(TIMESTAMP, "datetime");
        addSqlFieldType(INTEGER, "int");
        addSqlFieldType(LONGVARCHAR, "text");

        addSqlFieldDefault(NOW, "getdate()");
    }
}
