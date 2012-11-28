package net.codjo.database.sybase.impl.helper;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelper;
import net.codjo.database.sybase.util.SybaseUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
public class SybaseDatabaseHelper extends AbstractDatabaseHelper {

    public SybaseDatabaseHelper(DatabaseQueryHelper queryHelper) {
        super(queryHelper);
    }


    public String getConnectionUrl(ConnectionMetadata connectionMetadata) {
        return "jdbc:sybase:Tds:" + connectionMetadata.getHostname() + ":" + connectionMetadata.getPort();
    }


    @Override
    public boolean isIdentityInsertAllowed() {
        return true;
    }


    @Override
    public void setIdentityInsert(Connection connection,
                                  String catalog,
                                  String tableName,
                                  boolean temporaryTable,
                                  boolean identityInsert) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement
                  .executeUpdate("set identity_insert " + (temporaryTable ? "#" : "") + tableName + " " + (
                        identityInsert ?
                        "on" :
                        "off"));
        }
        finally {
            statement.close();
        }
    }


    public void changeUserGroup(Connection connection, String userName, String groupName)
          throws SQLException {
        String sql = "if not exists ( select 1 from sysusers gr "
                     + "  inner join sysusers us "
                     + "    on gr.uid = us.gid "
                     + "  where us.name='" + userName + "' and gr.name='" + groupName + "') "
                     + "begin "
                     + "    exec sp_changegroup  " + groupName + ",'" + userName + "' "
                     + "end";

        Statement statement = connection.createStatement();
        statement.executeUpdate(sql);
    }


    public void dropAllObjects(Connection connection) throws SQLException {
        dropAllForeignKeys(connection);
        dropAllObjects(connection, ObjectType.STORED_PROCEDURE, "sp_dba_");
        dropAllObjects(connection, ObjectType.VIEW);
        dropAllObjects(connection, ObjectType.TABLE);
        dropAllObjects(connection, ObjectType.DEFAULT);
        dropAllObjects(connection, ObjectType.RULE);
    }


    @Override
    protected String getSelectAllFksQuery(Connection connection) {
        return "select o2.name as TABLE_NAME, o1.name as CONSTRAINT_NAME "
               + "  from sysobjects o1                                   "
               + "  inner join sysconstraints c on (o1.id = c.constrid)  "
               + "  inner join sysobjects o2 on (c.tableid = o2.id)      "
               + "  where o1.type = 'RI'                                 "
               + "  order by o2.name, o1.name                            ";
    }


    @Override
    protected List<String> getAllTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<String>();
        Statement statement = connection.createStatement();
        try {
            String query = "select name from sysobjects where type = '"
                           + ObjectType.TABLE + "' order by name";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                tables.add(resultSet.getString("name"));
            }
        }
        finally {
            statement.close();
        }
        return tables;
    }


    public void truncateTable(Connection connection, SqlTable sqlTable) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("truncate table " + SybaseUtil.getTableName(sqlTable));
        }
        finally {
            statement.close();
        }
    }


    @Override
    protected void configureConnectionProperties(Properties connectionProperties) {
        if (connectionProperties.getProperty(LANGUAGE_KEY) == null) {
            connectionProperties.put(LANGUAGE_KEY, "french");
        }
    }


    private void dropAllObjects(Connection connection, ObjectType type, String... excludedPrefixes)
          throws SQLException {
        ResultSet resultSet = connection.createStatement().executeQuery(
              String.format("select name from sysobjects where type ='%s' order by name", type));
        try {
            while (resultSet.next()) {
                String objectName = resultSet.getString("name");
                if (!containsPrefix(objectName, excludedPrefixes)) {
                    connection.createStatement().executeUpdate(
                          String.format("drop %s %s", type.getName(), objectName));
                }
            }
        }
        finally {
            resultSet.close();
        }
    }


    private boolean containsPrefix(String objectName, String... prefixes) {
        for (String prefix : prefixes) {
            if (objectName.startsWith(prefix)) {
                return true;
            }
        }
        return false;
    }
}
