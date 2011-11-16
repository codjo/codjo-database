package net.codjo.database.mysql.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldList;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldListBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

public class MysqlSQLFieldListBuilder extends DefaultSQLFieldListBuilder {

    @Override
    public SQLFieldList get(Connection connection, String catalog, String tableName, boolean temporaryTable)
          throws SQLException {
        SQLFieldList sqlFieldList;
        if (temporaryTable) {
            sqlFieldList = new DefaultSQLFieldList();
            ResultSet resultSet = connection.createStatement().executeQuery("select * from " + tableName);
            ResultSetMetaData metaData = resultSet.getMetaData();
            for (int i = 1; i <= metaData.getColumnCount(); i++) {
                sqlFieldList.addField(metaData.getColumnName(i), metaData.getColumnType(i));
            }
        }
        else {
            sqlFieldList = super.get(connection, catalog, tableName, false);
        }

        return sqlFieldList;
    }
}
