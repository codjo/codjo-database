package net.codjo.database.mysql.impl.helper;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.apache.log4j.Logger;
public class MysqlDatabaseHelper extends AbstractDatabaseHelper {
    private static final Logger LOGGER = Logger.getLogger(MysqlDatabaseHelper.class);
    private enum ObjectType {
        PROCEDURE,
        VIEW,
        TABLE,
        DEFAULT,
        RULE
    }


    public MysqlDatabaseHelper(DatabaseQueryHelper queryHelper) {
        super(queryHelper);
    }


    public String getConnectionUrl(ConnectionMetadata connectionMetadata) {
        return "jdbc:mysql://" + connectionMetadata.getHostname() + ":" + connectionMetadata.getPort();
    }


    public void truncateTable(Connection connection, SqlTable table) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("truncate table " + table.getName());
        }
        finally {
            statement.close();
        }
    }


    @Override
    protected String getSelectAllFksQuery(Connection connection) throws SQLException {
        return "select TABLE_NAME, CONSTRAINT_NAME                       "
               + "  from information_schema.TABLE_CONSTRAINTS            "
               + "  where CONSTRAINT_TYPE = 'FOREIGN KEY'                "
               + "  and CONSTRAINT_SCHEMA = '" + connection.getCatalog() + "'"
               + "  order by TABLE_NAME, CONSTRAINT_NAME                 ";
    }


    @Override
    protected List<String> getAllTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<String>();
        Statement statement = connection.createStatement();
        try {
            String query = "select TABLE_NAME from information_schema.TABLES"
                           + " where TABLE_SCHEMA = '" + connection.getCatalog() + "'"
                           + " and TABLE_TYPE = 'BASE TABLE'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                tables.add(resultSet.getString("TABLE_NAME").toUpperCase());
            }
        }
        finally {
            statement.close();
        }
        return tables;
    }


    public void changeUserGroup(Connection connection, String userName, String groupName)
          throws SQLException {
        LOGGER.info("Changement de l'utilisateur " + userName + " dans le groupe " + groupName
                    + " : non effectue (pas de notion de groupe dans Mysql).");
    }


    public void dropAllObjects(Connection connection) throws SQLException {
        dropAllForeignKeys(connection);
        dropAllObjects(connection, ObjectType.PROCEDURE);
        dropAllObjects(connection, ObjectType.VIEW);
        dropAllObjects(connection, ObjectType.TABLE);
        dropAllObjects(connection, ObjectType.DEFAULT);
        dropAllObjects(connection, ObjectType.RULE);
    }


    private void dropAllObjects(Connection connection, ObjectType type)
          throws SQLException {
        String catalog = connection.getCatalog();
        String selectQuery;

        switch (type) {
            case PROCEDURE:
                selectQuery = "SHOW PROCEDURE STATUS where Db = '" + catalog + "'";
                dropObject(connection, selectQuery, ObjectType.PROCEDURE, "Name");
                break;
            case TABLE:
                selectQuery = "select TABLE_NAME from information_schema.TABLES"
                              + " where TABLE_SCHEMA = '" + catalog + "'"
                              + " and TABLE_TYPE = 'BASE TABLE'";
                dropObject(connection, selectQuery, ObjectType.TABLE, "TABLE_NAME");
                break;
            case VIEW:
                selectQuery = "select TABLE_NAME from information_schema.VIEWS"
                              + " where TABLE_SCHEMA = '" + catalog + "'";
                dropObject(connection, selectQuery, ObjectType.VIEW, "TABLE_NAME");
                break;
            case DEFAULT:
                LOGGER.info("Drop des " + ObjectType.DEFAULT + " not yet implemented");
                break;
            case RULE:
                LOGGER.info("Drop des " + ObjectType.RULE + " not yet implemented");
                break;
            default:
                break;
        }
    }


    private void dropObject(Connection connection,
                            String selectQuery,
                            ObjectType objectType,
                            String columnName)
          throws SQLException {
        Statement selectStatement = connection.createStatement();
        Statement dropStatement = connection.createStatement();

        ResultSet resultSet = selectStatement.executeQuery(selectQuery);
        while (resultSet.next()) {
            dropStatement.executeUpdate("drop " + objectType + " " + resultSet.getString(columnName));
        }

        dropStatement.close();
        selectStatement.close();
    }
}
