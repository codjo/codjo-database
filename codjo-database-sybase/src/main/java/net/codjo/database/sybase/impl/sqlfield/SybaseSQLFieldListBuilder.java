package net.codjo.database.sybase.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldList;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldListBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
public class SybaseSQLFieldListBuilder extends DefaultSQLFieldListBuilder {

    @Override
    public SQLFieldList get(Connection connection,
                            String catalog,
                            String dbTableName,
                            boolean temporayTable) throws SQLException {
        return getSQLFieldList(dbTableName, connection, catalog, temporayTable);
    }


    protected SQLFieldList getSQLFieldList(String dbTableName,
                                           Connection connection,
                                           String catalog,
                                           boolean temporayTable) throws SQLException {
        DefaultSQLFieldList sqlFieldList = new DefaultSQLFieldList();
        if (dbTableName.startsWith("#")) {
            initSQLFieldListFromTemporaryTable(sqlFieldList, connection, dbTableName);
        }
        else if (temporayTable) {
            initSQLFieldListFromTemporaryTable(sqlFieldList, connection, "#" + dbTableName);
        }
        else {
            initSQLFieldListFromCatalog(sqlFieldList, connection, catalog, dbTableName, temporayTable);
        }
        return sqlFieldList;
    }


    private void initSQLFieldListFromTemporaryTable(DefaultSQLFieldList sqlFieldList,
                                                    Connection con,
                                                    String dbTableName)
          throws SQLException {
        ResultSet rs =
              con.createStatement().executeQuery("select * from " + dbTableName
                                                 + " where 1=2");
        ResultSetMetaData rsmd = rs.getMetaData();

        int colmumnCount = rsmd.getColumnCount();
        for (int i = 1; i <= colmumnCount; i++) {
            String dbFieldName = rsmd.getColumnName(i);
            int sqlType = rsmd.getColumnType(i);
            sqlFieldList.addField(dbFieldName, sqlType);
        }
    }
}
