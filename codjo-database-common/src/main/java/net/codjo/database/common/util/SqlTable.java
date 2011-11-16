package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlTable}
 */
@Deprecated
public class SqlTable extends net.codjo.database.common.api.structure.SqlTable {

    public static net.codjo.database.common.api.structure.SqlTable table(String tableName) {
        return net.codjo.database.common.api.structure.SqlTable.table(tableName);
    }


    public static net.codjo.database.common.api.structure.SqlTable temporaryTable(String tableName) {
        return net.codjo.database.common.api.structure.SqlTable.temporaryTable(tableName);
    }
}
