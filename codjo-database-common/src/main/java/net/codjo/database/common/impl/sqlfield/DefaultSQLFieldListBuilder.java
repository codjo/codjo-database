package net.codjo.database.common.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DefaultSQLFieldListBuilder implements SQLFieldListBuilder {

    public SQLFieldList get(Connection connection,
                            String catalog,
                            String dbTableName,
                            boolean temporaryTable) throws SQLException {
        DefaultSQLFieldList sqlFieldList = new DefaultSQLFieldList();
        initSQLFieldListFromCatalog(sqlFieldList, connection, catalog, dbTableName, temporaryTable);
        return sqlFieldList;
    }


    protected void initSQLFieldListFromCatalog(DefaultSQLFieldList sqlFieldList,
                                               Connection con,
                                               String catalog,
                                               String dbTableName,
                                               boolean temporaryTable) throws SQLException {
        DatabaseMetaData md = con.getMetaData();
        ResultSet rs = null;

        if (catalog == null) {
            int index = dbTableName.indexOf(".");
            if (index >= 0) {
                catalog = dbTableName.substring(0, index);
                rs = md.getColumns(catalog, null, dbTableName.substring(index + 2), null);
            }
        }
        if (rs == null) {
            rs = md.getColumns(catalog, null, dbTableName, null);
        }
        if (rs != null) {
            while (rs.next()) {
                String dbFieldName = rs.getString(4);
                int sqlType = rs.getInt(5);
                sqlFieldList.addField(dbFieldName, sqlType);
            }
        }
    }
}
