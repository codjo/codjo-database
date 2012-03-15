package net.codjo.database.sybase.impl;
/**
 *
 */
public class LegacyDatabaseTranscoder extends SybaseDatabaseTranscoder {
    public LegacyDatabaseTranscoder() {
        addSqlFieldType(TIMESTAMP, "timestamp");
    }
}

