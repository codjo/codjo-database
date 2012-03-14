package net.codjo.database.oracle.impl.helper;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelper;
import org.apache.log4j.Logger;

import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlTable.table;
public class OracleDatabaseHelper extends AbstractDatabaseHelper {
    private static final Logger LOG = Logger.getLogger(OracleDatabaseHelper.class);
    private enum ObjectType {
        PROCEDURE,
        VIEW,
        TABLE,
        SYNONYM,
        SEQUENCE,
        FUNCTION,
        PACKAGE
    }


    public OracleDatabaseHelper(DatabaseQueryHelper queryHelper) {
        super(queryHelper);
    }


    public String getConnectionUrl(ConnectionMetadata connectionMetadata) {
        return "jdbc:oracle:thin:@" + connectionMetadata.getHostname() + ":" + connectionMetadata.getPort()
               + ":" + connectionMetadata.getBase();
    }


    public void truncateTable(Connection connection, SqlTable table) throws SQLException {
        // TODO de sego : je ne sais pas pourquoi j'ai du rajouter cela
        if (table.getName().contains("$")) {
            return;
        }

        List<String> constraints = getUniqueConstraints(connection, table.getName());
        Map<String, String> foreignKeys = getLinkedForeignKeys(connection, constraints);

        enabledForeignKeys(connection, foreignKeys, false);
        enabledUniqueConstraints(connection, table.getName(), constraints, false);

        Statement statement = connection.createStatement();
        try {
            statement.executeUpdate("truncate table " + table.getName());
        }
        finally {
            statement.close();
        }

        enabledUniqueConstraints(connection, table.getName(), constraints, true);
        enabledForeignKeys(connection, foreignKeys, true);
    }


    public void dropAllObjects(Connection connection) throws SQLException {
        dropAllConstraints(connection);
        dropAllObjects(connection, ObjectType.PROCEDURE);
        dropAllObjects(connection, ObjectType.FUNCTION);
        dropAllObjects(connection, ObjectType.SEQUENCE);
        dropAllObjects(connection, ObjectType.PACKAGE);
        dropAllObjects(connection, ObjectType.VIEW);
        dropAllObjects(connection, ObjectType.TABLE);
    }


    // TODO commonaliser avec jdbcFixture.isUserInGroup
    public void changeUserGroup(Connection connection, String userName, String groupName)
          throws SQLException {
        Statement statement = connection.createStatement();
        try {
            String query = "select GRANTED_ROLE from DBA_ROLE_PRIVS "
                           + "where GRANTEE ='" + userName + "' "
                           + "and GRANTED_ROLE = '" + groupName.toUpperCase() + "'";
            ResultSet resultSet = statement.executeQuery(query);
            if (!resultSet.next()) {
                statement.executeUpdate("GRANT " + groupName + " TO " + userName);
            }
        }
        finally {
            statement.close();
        }
    }


    protected void dropAllConstraints(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();
        try {
            dropTheseConstraints(connection, statement,
                                 getSelectPkConstraintQuery(connection, "where CONSTRAINT_TYPE != 'P'"));
            dropTheseConstraints(connection, statement,
                                 getSelectPkConstraintQuery(connection, "where CONSTRAINT_TYPE = 'P'"));
        }
        finally {
            statement.close();
        }
    }


    private void dropTheseConstraints(Connection connection, Statement statement, String query) throws SQLException {
        ResultSet resultSet = statement.executeQuery(query);
        while (resultSet.next()) {
            String tableName = resultSet.getString("TABLE_NAME");
            String constraintName = resultSet.getString("CONSTRAINT_NAME");
            try {
                dropForeignKey(connection, foreignKey(constraintName, table(tableName)));
            }
            catch (SQLException e) {
                SQLException sqlException =
                      new SQLException("Unable to drop constraint " + tableName + "." + constraintName + " - "
                                       + e.getMessage());
                sqlException.initCause(e);
                throw sqlException;
            }
        }
        resultSet.close();
    }


    protected String getSelectPkConstraintQuery(Connection connection, String where) {
        return "select TABLE_NAME, CONSTRAINT_NAME "
               + "from user_constraints "
               + where
               + "order by CONSTRAINT_TYPE";
    }


    @Override
    protected String getSelectAllFksQuery(Connection connection) {
        return "select TABLE_NAME, CONSTRAINT_NAME from user_constraints where CONSTRAINT_TYPE = 'R'";
    }


    @Override
    protected List<String> getAllTables(Connection connection) throws SQLException {
        List<String> tables = new ArrayList<String>();
        Statement statement = connection.createStatement();
        try {
            String query = "select OBJECT_NAME from user_objects where OBJECT_TYPE='TABLE'";
            ResultSet resultSet = statement.executeQuery(query);
            while (resultSet.next()) {
                tables.add(resultSet.getString("OBJECT_NAME"));
            }
        }
        finally {
            statement.close();
        }
        return tables;
    }


    private void enabledUniqueConstraints(Connection connection,
                                          String tableName,
                                          List<String> constraints,
                                          boolean enabled)
          throws SQLException {
        String enable = enabled ? " enable" : " disable";
        Statement statement = connection.createStatement();
        try {
            for (String constraint : constraints) {
                statement.executeUpdate(
                      "alter table " + tableName + enable + " CONSTRAINT " + constraint);
            }
        }
        finally {
            statement.close();
        }
    }


    private void enabledForeignKeys(Connection connection, Map<String, String> fks, boolean enabled)
          throws SQLException {
        String enable = enabled ? " enable" : " disable";
        Statement statement = connection.createStatement();
        try {
            for (Entry<String, String> entry : fks.entrySet()) {
                statement.executeUpdate(
                      "alter table " + entry.getValue() + enable + " CONSTRAINT " + entry.getKey());
            }
        }
        finally {
            statement.close();
        }
    }


    private List<String> getUniqueConstraints(Connection connection, String tableName) throws SQLException {
        Statement statement = connection.createStatement();
        List<String> constraints = new ArrayList<String>();

        try {
            ResultSet resultSet = statement.executeQuery("select CONSTRAINT_NAME from user_constraints "
                                                         + "where TABLE_NAME = '" + tableName + "'"
                                                         + " and CONSTRAINT_TYPE = 'U'");
            while (resultSet.next()) {
                constraints.add(resultSet.getString(1));
            }
        }
        finally {
            statement.close();
        }
        return constraints;
    }


    private Map<String, String> getLinkedForeignKeys(Connection connection, List<String> constraints)
          throws SQLException {
        Statement statement = connection.createStatement();
        Map<String, String> fks = new HashMap<String, String>();

        try {
            for (String constraint : constraints) {
                ResultSet resultSet = statement
                      .executeQuery("select TABLE_NAME, CONSTRAINT_NAME from user_constraints "
                                    + "where R_CONSTRAINT_NAME = '" + constraint + "'");
                while (resultSet.next()) {
                    String fkTableName = resultSet.getString(1);
                    String fkName = resultSet.getString(2);
                    fks.put(fkName, fkTableName);
                }
            }
        }
        finally {
            statement.close();
        }
        return fks;
    }


    private void dropAllObjects(Connection connection, ObjectType type)
          throws SQLException {
        String selectQuery = "select OBJECT_NAME from user_objects where OBJECT_TYPE=";
        String systenColumnName = "OBJECT_NAME";

        switch (type) {
            case PROCEDURE:
                selectQuery = selectQuery + "'" + ObjectType.PROCEDURE + "'";
                dropObject(connection, selectQuery, ObjectType.PROCEDURE, systenColumnName);
                break;
            case TABLE:
                selectQuery = selectQuery + "'" + ObjectType.TABLE + "'";
                dropObject(connection, selectQuery, ObjectType.TABLE, systenColumnName);
                break;
            case VIEW:
                selectQuery = selectQuery + "'" + ObjectType.VIEW + "'";
                dropObject(connection, selectQuery, ObjectType.VIEW, systenColumnName);
                break;
            case FUNCTION:
                selectQuery = selectQuery + "'" + ObjectType.FUNCTION + "'";
                dropObject(connection, selectQuery, ObjectType.FUNCTION, systenColumnName);
                break;
            case SEQUENCE:
                selectQuery = selectQuery + "'" + ObjectType.SEQUENCE + "'";
                dropObject(connection, selectQuery, ObjectType.SEQUENCE, systenColumnName);
                break;
            case PACKAGE:
                selectQuery = selectQuery + "'" + ObjectType.PACKAGE + "'";
                dropObject(connection, selectQuery, ObjectType.PACKAGE, systenColumnName);
                break;
            case SYNONYM:
                LOG.info("Drop des " + ObjectType.SYNONYM + " not yet implemented");
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
        try {
            Statement dropStatement = connection.createStatement();
            try {
                ResultSet resultSet = selectStatement.executeQuery(selectQuery);
                while (resultSet.next()) {
                    String sql = "drop " + objectType + " " + resultSet.getString(columnName);
                    if (ObjectType.TABLE.equals(objectType)) {
                        dropStatement.executeUpdate(sql + " purge");
                    }
                    else {
                        dropStatement.executeUpdate(sql);
                    }
                }
            }
            finally {
                dropStatement.close();
            }
        }
        finally {
            selectStatement.close();
        }
    }
}
