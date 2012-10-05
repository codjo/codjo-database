package net.codjo.database.common.api;
import net.codjo.database.common.api.DatabaseQueryHelper.SelectType;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.api.structure.SqlTrigger;
import net.codjo.database.common.api.structure.SqlView;
import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import static junit.framework.Assert.assertFalse;
import static junit.framework.Assert.assertTrue;
import org.apache.log4j.Logger;
public abstract class JdbcFixtureAdvanced {
    private static final Logger LOGGER = Logger.getLogger(JdbcFixtureAdvanced.class);
    protected final JdbcFixture jdbcFixture;
    private final List<SqlView> createdViews = new ArrayList<SqlView>();
    private final List<SqlConstraint> createdForeignKeys = new ArrayList<SqlConstraint>();
    private final DatabaseFactory databaseFactory = new DatabaseFactory();
    private final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    private final DatabaseQueryHelper queryHelper = databaseFactory.getDatabaseQueryHelper();
    private final ExecSqlScript execSqlScript = databaseFactory.createExecSqlScript();
    private final DatabaseScriptHelper scriptHelper = databaseFactory.createDatabaseScriptHelper();

    protected JdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        this.jdbcFixture = jdbcFixture;
        this.execSqlScript.setConnectionMetadata(getConnectionMetadata());
        this.execSqlScript.setLogger(new net.codjo.database.common.api.ExecSqlScript.Logger() {
            public void log(String log) {
                LOGGER.info(log);
            }
        });
    }


    public void assertExists(String tableName) {
        assertExists(table(tableName));
    }


    public void assertExists(SqlTable table) {
        assertTrue("Table '" + table.getName() + "' doesn't exist", exists(table));
    }


    public void assertDoesntExist(String tableName) {
        assertDoesntExist(table(tableName));
    }


    public void assertDoesntExist(SqlTable table) {
        assertFalse("Table '" + table.getName() + "' exists", exists(table));
    }


    public void dropAllObjects() {
        try {
            databaseHelper.dropAllObjects(jdbcFixture.getConnection());
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    public void executeCreateTableScriptFile(File scriptFile) {
        execSqlScript.execute(scriptFile.getParentFile().getAbsolutePath(), scriptFile.getName());
    }


    public void create(SqlView view) {
        executeUpdate(queryHelper.buildCreateViewQuery(view));
        createdViews.add(view);
    }


    public void create(SqlIndex index) {
        String query = queryHelper.buildCreateIndexQuery(index);
        if (queryHelper.buildCreateIndexQueryReturnsQueries(index)) {
            for(String q : query.split(scriptHelper.getQueryDelimiter())) {
                executeUpdate(q);
            }
        } else {
            executeUpdate(query);
        }
    }


    public void create(SqlConstraint constraint) {
        executeUpdate(queryHelper.buildCreateConstraintQuery(constraint));
        createdForeignKeys.add(constraint);
    }


    public void assertObjectExists(String objectName, ObjectType objectType) {
        assertTrue("Object '" + objectName + "' of type '" + objectType.getName() + "' doesn't exist",
                   objectExists(objectName, objectType));
    }


    public void assertObjectDoesntExist(String objectName, ObjectType objectType) {
        assertFalse("Object '" + objectName + "' of type '" + objectType.getName() + "' exists",
                    objectExists(objectName, objectType));
    }


    public void assertExists(SqlView view) {
        assertTrue("View '" + view.getName() + "' doesn't exist", exists(view));
    }


    public void assertDoesntExist(SqlView view) {
        assertFalse("View '" + view.getName() + "' exists", exists(view));
    }


    public void assertExists(SqlConstraint constraint) {
        assertTrue("Constraint '" + constraint.getName() + "' doesn't exist", exists(constraint));
    }


    public void assertDoesntExist(SqlConstraint constraint) {
        assertFalse("Constraint '" + constraint.getName() + "' exists", exists(constraint));
    }


    public void assertExists(SqlIndex index) {
        assertTrue("Index '" + index.getName() + "' doesn't exist", exists(index));
    }


    public void assertDoesntExist(SqlIndex index) {
        assertFalse("Index '" + index.getName() + "' exists", exists(index));
    }


    public void assertExists(SqlTrigger trigger) {
        assertTrue("Trigger '" + trigger.getName() + "' doesn't exist", exists(trigger));
    }


    public void assertDoesntExist(SqlTrigger trigger) {
        assertFalse("Trigger '" + trigger.getName() + "' exists", exists(trigger));
    }


    public void assertUserInGroup(String userName, String groupName) {
        assertTrue("User '" + userName + "' not in group '" + groupName + "'",
                   isUserInGroup(userName, groupName));
    }


    public void assertUserNotInGroup(String userName, String groupName) {
        assertFalse("User '" + userName + "' is in group '" + groupName + "'",
                    isUserInGroup(userName, groupName));
    }


    public ConnectionMetadata getConnectionMetadata() {
        return jdbcFixture.getConnectionMetadata();
    }


    protected boolean exists(SqlTable table) {
        ResultSet resultSet = null;
        try {
            resultSet = executeQuery(queryHelper.buildSelectQuery(table, SelectType.ONE));
            return true;
        }
        catch (RuntimeSqlException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    protected boolean exists(SqlView view) {
        ResultSet resultSet = null;
        try {
            resultSet = jdbcFixture.getConnection().getMetaData()
                  .getTables(jdbcFixture.getConnection().getCatalog(),
                             "%",
                             view.getName(),
                             new String[]{"VIEW"});
            while (resultSet.next()) {
                String viewName = resultSet.getString("TABLE_NAME");
                if (view.getName().equalsIgnoreCase(viewName)) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    protected boolean exists(SqlIndex index) {
        ResultSet resultSet = null;
        try {
            resultSet = jdbcFixture.getConnection().getMetaData()
                  .getIndexInfo(jdbcFixture.getConnection().getCatalog(),
                                "%",
                                index.getTable().getName(),
                                false,
                                false);
            while (resultSet.next()) {
                String indexName = resultSet.getString("INDEX_NAME");
                if (index.getName().equalsIgnoreCase(indexName)) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    protected boolean exists(SqlConstraint constraint) {
        ResultSet resultSet = null;
        try {
            resultSet = jdbcFixture.getConnection().getMetaData()
                  .getImportedKeys(jdbcFixture.getConnection().getCatalog(),
                                   null,
                                   constraint.getAlteredTable().getName());
            while (resultSet.next()) {
                String fkName = resultSet.getString("FK_NAME");
                if (constraint.getName().equalsIgnoreCase(fkName)) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            ;
        }
        catch (RuntimeSqlException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    protected boolean exists(SqlTrigger trigger) {
        return objectExists(trigger.getName(), ObjectType.TRIGGER);
    }


    protected final boolean hasResults(String request) {
        ResultSet resultSet = null;
        try {
            resultSet = executeQuery(request);
            return resultSet.next();
        }
        catch (SQLException e) {
            ;
        }
        catch (RuntimeSqlException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    protected abstract boolean objectExists(String objectName, ObjectType objectType);


    protected abstract boolean isUserInGroup(String userName, String groupName);


    private void executeUpdate(String query) {
        try {
            jdbcFixture.executeUpdate(query);
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    protected ResultSet executeQuery(String query) {
        try {
            return jdbcFixture.executeQuery(query);
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    void doSetUp() {
    }


    void doTearDown() {
        try {
            for (SqlConstraint createdForeignKey : createdForeignKeys) {
                try {
                    databaseHelper.dropForeignKey(jdbcFixture.getConnection(), createdForeignKey);
                }
                catch (Exception e) {
                    ;
                }
            }

            for (SqlView view : createdViews) {
                try {
                    executeUpdate("drop view " + view.getName());
                }
                catch (Exception e) {
                    ;
                }
            }
        }
        finally {
            createdForeignKeys.clear();
        }
    }
}
