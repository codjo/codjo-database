package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlIndex}
 */
@Deprecated
public class SqlIndex extends net.codjo.database.common.api.structure.SqlIndex {

    public static net.codjo.database.common.api.structure.SqlIndex index(String indexName, SqlTable table) {
        return net.codjo.database.common.api.structure.SqlIndex.index(indexName, table);
    }


    public static net.codjo.database.common.api.structure.SqlIndex normalIndex(String indexName,
                                                                             SqlTable table,
                                                                             SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex.normalIndex(indexName, table, fields);
    }


    public static net.codjo.database.common.api.structure.SqlIndex uniqueIndex(String indexName,
                                                                             SqlTable table,
                                                                             SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex.uniqueIndex(indexName, table, fields);
    }


    public static net.codjo.database.common.api.structure.SqlIndex clusteredIndex(String indexName,
                                                                                SqlTable table,
                                                                                SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex.clusteredIndex(indexName, table, fields);
    }


    public static net.codjo.database.common.api.structure.SqlIndex uniqueClusteredIndex(String indexName,
                                                                                      SqlTable table,
                                                                                      SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex.uniqueClusteredIndex(indexName, table, fields);
    }


    public static net.codjo.database.common.api.structure.SqlIndex primaryKeyIndex(String indexName,
                                                                                 SqlTable table,
                                                                                 SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex.primaryKeyIndex(indexName, table, fields);
    }


    public static net.codjo.database.common.api.structure.SqlIndex primaryKeyClusteredIndex(String indexName,
                                                                                          SqlTable table,
                                                                                          SqlField... fields) {
        return net.codjo.database.common.api.structure.SqlIndex
              .primaryKeyClusteredIndex(indexName, table, fields);
    }
}
