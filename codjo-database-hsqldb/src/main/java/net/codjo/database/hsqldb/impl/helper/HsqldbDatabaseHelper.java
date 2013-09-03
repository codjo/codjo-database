package net.codjo.database.hsqldb.impl.helper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelper;
public class HsqldbDatabaseHelper extends AbstractDatabaseHelper {

    public HsqldbDatabaseHelper(DatabaseQueryHelper queryHelper) {
        super(queryHelper);
    }


    public String getConnectionUrl(ConnectionMetadata connectionMetadata) {
        StringBuilder hostname = new StringBuilder(connectionMetadata.getHostname());
        if (hostname.length() == 0) {
            return "jdbc:hsqldb:.";
        }
        else {
            String port = connectionMetadata.getPort();
            if (!"".equals(port)) {
                hostname.append(":").append(port);
            }
            if (!"".equals(connectionMetadata.getBase())) {
                hostname.append("/").append(connectionMetadata.getBase());
            }
            return "jdbc:hsqldb:hsql://" + hostname.toString();
        }
    }


    public void truncateTable(Connection connection, SqlTable table) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("delete from " + table.getName());
        }
        finally {
            statement.close();
        }
    }


    @Override
    protected String getSelectAllFksQuery(Connection connection) throws SQLException {
        return "select TABLE_NAME, CONSTRAINT_NAME from INFORMATION_SCHEMA.TABLE_CONSTRAINTS where CONSTRAINT_TYPE = 'FOREIGN KEY'";
    }


    public void changeUserGroup(Connection connection, String userName, String groupName)
          throws SQLException {
        Statement s = connection.createStatement();
        try {
            s.executeUpdate("GRANT " + groupName + " TO " + userName);
        }
        finally {
            s.close();
        }
    }


    public void dropAllObjects(Connection connection) throws SQLException {
        Statement s = connection.createStatement();
        try {
            s.executeUpdate("DROP SCHEMA PUBLIC CASCADE");
        }
        finally {
            s.close();
        }
/*
        dropAllForeignKeys(connection);
        dropAllObjects(connection, ObjectType.STORED_PROCEDURE);
        dropAllObjects(connection, ObjectType.VIEW);
        dropAllObjects(connection, ObjectType.TABLE);
        dropAllObjects(connection, ObjectType.DEFAULT);
        dropAllObjects(connection, ObjectType.RULE);
        dropAllObjects(connection, ObjectType.TRIGGER);
*/
    }


    @Override
    public void setIdentityInsert(Connection connection,
                                  String catalog,
                                  String tableName,
                                  boolean temporaryTable,
                                  boolean identityInsert) throws SQLException {
    }
}
