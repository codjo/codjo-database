package net.codjo.database.mysql.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.structure.SqlConstraint;
import org.apache.log4j.Logger;
public class MysqlJdbcFixtureAdvanced extends JdbcFixtureAdvanced {
    private static final Logger LOGGER = Logger.getLogger(MysqlJdbcFixtureAdvanced.class);


    protected MysqlJdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        super(jdbcFixture);
    }


    @Override
    protected boolean exists(SqlConstraint constraint) {
        String catalog = getConnectionMetadata().getCatalog();
        String request =
              "select 1 from information_schema.TABLE_CONSTRAINTS"
              + " where CONSTRAINT_TYPE = 'FOREIGN KEY'"
              + " and CONSTRAINT_SCHEMA = '" + catalog + "'"
              + " and CONSTRAINT_NAME = '" + constraint.getName() + "'";
        return hasResults(request);
    }


    @Override
    protected boolean objectExists(String objectName, ObjectType objectType) {
        String catalog = getConnectionMetadata().getCatalog();
        String query;
        if (objectType == ObjectType.TABLE) {
            query = "select 1 from information_schema.TABLES"
                    + " where TABLE_SCHEMA = '" + catalog + "'"
                    + " and TABLE_NAME = '" + objectName + "'";
        }
        else if (objectType == ObjectType.VIEW) {
            query = "select 1 from information_schema.VIEWS"
                    + " where TABLE_SCHEMA = '" + catalog + "'"
                    + " and TABLE_NAME = '" + objectName + "'";
        }
        else if (objectType == ObjectType.TRIGGER) {
            query = "select 1 from information_schema.TRIGGERS"
                    + " where TRIGGER_SCHEMA = '" + catalog + "'"
                    + " and TRIGGER_NAME = '" + objectName + "'";
        }
        else if (objectType == ObjectType.STORED_PROCEDURE) {
            query = "show procedure status"
                    + " where Db = '" + catalog + "'"
                    + " and Name = '" + objectName + "'";
        }
        else {
            throw new UnsupportedOperationException();
        }
        return hasResults(query);
    }


    @Override
    public void assertUserInGroup(String userName, String groupName) {
        LOGGER.info("Assert inutile car pas de notion de groupe dans Mysql.");
    }


    @Override
    protected boolean isUserInGroup(String userName, String groupName) {
        throw new UnsupportedOperationException();
    }
}
