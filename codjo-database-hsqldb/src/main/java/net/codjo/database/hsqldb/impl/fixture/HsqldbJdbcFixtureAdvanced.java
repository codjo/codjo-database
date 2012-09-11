package net.codjo.database.hsqldb.impl.fixture;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.RuntimeSqlException;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTrigger.Type;
import net.codjo.database.hsqldb.impl.helper.HsqldbDatabaseScriptHelper;
public class HsqldbJdbcFixtureAdvanced extends JdbcFixtureAdvanced {

    public HsqldbJdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        super(jdbcFixture);
    }

    protected boolean exists(SqlIndex index) {
        String query = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_INDEXINFO WHERE INDEX_NAME='";
        query += index.getName() + "'";
        return hasResults(query);
    }

    @Override
    protected boolean objectExists(String objectName, ObjectType objectType) {
        boolean result;

        String query;
        switch (objectType) {
        case VIEW:
            result = true;
            query = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_TABLES "
                    + "where TABLE_TYPE='VIEW' AND TABLE_NAME='" + objectName + "'";
            break;
        case TABLE:
            result = true;
            query = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_TABLES "
                    + "where TABLE_TYPE='TABLE' AND TABLE_NAME='" + objectName + "'";
            break;
        case STORED_PROCEDURE:
            result = true;
            query = "SELECT 1 FROM INFORMATION_SCHEMA.SYSTEM_PROCEDURES "
                    + "WHERE PROCEDURE_SCHEM='PUBLIC' AND PROCEDURE_NAME='" + objectName + "'";
            break;
        case DEFAULT:
            result = true;
            query = null; //TODO
            break;
        case RULE:
            result = true;
            query = null; //TODO
            break;
        case TRIGGER:
            result = true;
            query = "SELECT 1 FROM INFORMATION_SCHEMA.TRIGGERS "
                    + "WHERE TRIGGER_NAME='" + objectName + "'"
                    + " OR TRIGGER_NAME='" + HsqldbDatabaseScriptHelper.getTriggerName(objectName, Type.INSERT) + "'"
                    + " OR TRIGGER_NAME='" + HsqldbDatabaseScriptHelper.getTriggerName(objectName, Type.UPDATE) + "'";
            break;
        default:
            result = false;
            query = null;
        }

        if (result) {
            result = hasResults(query);
        }

        return result;
    }


    @Override
    protected boolean isUserInGroup(String userName, String groupName) {
        boolean result = false;

        userName = userName.toUpperCase();
        groupName = groupName.toUpperCase();

        ResultSet rs = executeQuery("select 1 from INFORMATION_SCHEMA.ROLE_AUTHORIZATION_DESCRIPTORS "
                                    + "WHERE GRANTEE='" + userName + "' and ROLE_NAME='" + groupName + "'");
        try {
            if (rs.next()) {
                result = true;
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
        finally {
            try {
                rs.close();
            }
            catch (SQLException e) {
                throw new RuntimeSqlException(e);
            }
            rs = null;
        }

        return result;
    }


    public List<String> getAllTables() {
        List<String> tables = new ArrayList<String>();

        ResultSet rs = executeQuery("SELECT TABLE_NAME FROM INFORMATION_SCHEMA.SYSTEM_TABLES where TABLE_TYPE='TABLE'");
        try {
            while (rs.next()) {
                tables.add(rs.getString(1));
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
        finally {
            try {
                rs.close();
            }
            catch (SQLException e) {
                throw new RuntimeSqlException(e);
            }
            rs = null;
        }

        return tables;
    }
}
