package net.codjo.database.common.api;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectQuery;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlView;
import java.sql.Connection;
import java.sql.SQLException;
public interface DatabaseQueryHelper {
    enum SelectType {
        ONE,
        ALL;
    }


    String buildSelectQuery(SqlTable sqlTable, String... columns);


    String buildSelectQuery(SqlTable sqlTable, SqlField... sqlFields);


    String buildSelectQuery(SqlTable sqlTable, SelectType selectType);


    /**
     * @deprecated use buildCreateTableQuery instead.
     */
    @Deprecated
    String buildCreateQuery(SqlTable sqlTable, String content);


    String buildCreateTableQuery(SqlTable sqlTable, String content);


    /**
     * @deprecated use buildDropTableQuery(SqlTable sqlTable) instead.
     */
    @Deprecated
    String buildDropTableQuery(SqlTable sqlTable, boolean checkExists);


    String buildDropTableQuery(SqlTable sqlTable);


    String buildInsertQuery(SqlTable sqlTable, String... columns);


    String buildInsertQuery(SqlTable sqlTable, SqlField... sqlFields);


    String buildUpdateQuery(SqlTable sqlTable, String... columns);


    String buildUpdateQuery(SqlTable sqlTable, SqlField... sqlFields);


    String buildCreateIndexQuery(SqlIndex sqlIndex);


    String buildCreateViewQuery(SqlView sqlView);


    String buildDropConstraintQuery(SqlConstraint sqlConstraint);


    String buildCreateConstraintQuery(SqlConstraint sqlConstraint);


    Confidential confidential();


    public interface Confidential {
        SelectQuery createSelectQuery(Connection connection, String sqlSelectQuery) throws SQLException;


        PreparedSelectQuery createPreparedSelectQuery(Connection connection, String sqlSelectQuery)
              throws SQLException;


        /**
         * @deprecated cette méthode n'est utilisé que pour le select via OQL qui doit disparaitre
         */
        @Deprecated
        int computeRowCount(Connection connection, int readRowCount) throws SQLException;
    }
}
