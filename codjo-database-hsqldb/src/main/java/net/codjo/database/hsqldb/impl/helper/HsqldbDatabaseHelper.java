package net.codjo.database.hsqldb.impl.helper;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelper;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
public class HsqldbDatabaseHelper extends AbstractDatabaseHelper {

    public HsqldbDatabaseHelper(DatabaseQueryHelper queryHelper) {
        super(queryHelper);
    }


    public String getConnectionUrl(ConnectionMetadata connectionMetadata) {
        return "jdbc:hsqldb:.";
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
        throw new UnsupportedOperationException();// todo getSelectAllFksQuery
    }


    public void changeUserGroup(Connection connection, String userName, String groupName)
          throws SQLException {
        throw new UnsupportedOperationException();// todo changeUserGroup
    }


    public void dropAllObjects(Connection connection) throws SQLException {
        throw new UnsupportedOperationException();// todo dropAllObjects
    }
}
