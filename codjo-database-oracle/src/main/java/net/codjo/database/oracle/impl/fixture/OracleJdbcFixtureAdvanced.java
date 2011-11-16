package net.codjo.database.oracle.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlIndex;
import java.sql.ResultSet;
import java.sql.SQLException;
public class OracleJdbcFixtureAdvanced extends JdbcFixtureAdvanced {

    protected OracleJdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        super(jdbcFixture);
    }


    @Override
    protected boolean exists(SqlConstraint constraint) {
        String request =
              "select 1 from all_constraints"
              + " where TABLE_NAME = '" + constraint.getAlteredTable().getName() + "'"
              + " and CONSTRAINT_NAME = '" + constraint.getName() + "'";
        return hasResults(request);
    }


    @Override
    protected boolean exists(SqlIndex index) {
        String request =
              "select 1 from user_indexes"
              + " where TABLE_NAME = '" + index.getTable().getName() + "'"
              + " and INDEX_NAME = '" + index.getName() + "'";

        ResultSet resultSet = null;
        try {
            resultSet = jdbcFixture.executeQuery(request);
            if (resultSet.next()) {
                return true;
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


    @Override
    protected boolean objectExists(String objectName, ObjectType objectType) {
        String type;
        if (objectType == ObjectType.TABLE) {
            type = "TABLE";
        }
        else if (objectType == ObjectType.TRIGGER) {
            type = "TRIGGER";
        }
        else if (objectType == ObjectType.STORED_PROCEDURE) {
            type = "PROCEDURE";
        }
        else if (objectType == ObjectType.VIEW) {
            type = "VIEW";
        }
        else {
            throw new UnsupportedOperationException();
        }
        String query = "select 1 from user_objects where OBJECT_TYPE='" + type
                       + "' and OBJECT_NAME='" + objectName + "'";
        return hasResults(query);
    }


    @Override
    protected boolean isUserInGroup(String userName, String groupName) {
        String request = "select GRANTED_ROLE from DBA_ROLE_PRIVS "
                         + "where GRANTEE ='" + userName + "' "
                         + "and GRANTED_ROLE = '" + groupName.toUpperCase() + "'";
        return hasResults(request);
    }
}
