package net.codjo.database.mysql.impl;
import net.codjo.database.common.impl.DefaultDatabaseTranscoder;
/**
 *
 */
public class MysqlDatabaseTranscoder extends DefaultDatabaseTranscoder {

    public MysqlDatabaseTranscoder() {
        addSqlFieldType(INTEGER, "int");
        addSqlFieldType(LONGVARCHAR, "text");

        addSqlFieldDefault(NOW, "CURRENT_TIMESTAMP()");
    }
}
