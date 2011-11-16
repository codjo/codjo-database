package net.codjo.database.hsqldb.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.ObjectType;
public class HsqldbJdbcFixtureAdvanced extends JdbcFixtureAdvanced {

    public HsqldbJdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        super(jdbcFixture);
    }


    @Override
    protected boolean objectExists(String objectName, ObjectType objectType) {
        throw new UnsupportedOperationException();// Todo objectExists
    }


    @Override
    protected boolean isUserInGroup(String userName, String groupName) {
        throw new UnsupportedOperationException();// Todo isUserInGroup
    }
}
