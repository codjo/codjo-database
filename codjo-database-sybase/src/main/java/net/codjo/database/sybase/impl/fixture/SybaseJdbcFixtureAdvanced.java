package net.codjo.database.sybase.impl.fixture;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlIndex;
import static net.codjo.database.sybase.util.SybaseUtil.getName;
public class SybaseJdbcFixtureAdvanced extends JdbcFixtureAdvanced {

    public SybaseJdbcFixtureAdvanced(JdbcFixture jdbcFixture) {
        super(jdbcFixture);
    }


    @Override
    public boolean isUserInGroup(String userName, String groupName) {
        String request = "select aGroup.name from sysusers aGroup "
                         + "  inner join sysusers aUsuer "
                         + "    on aGroup.uid = aUsuer.gid "
                         + "where aUsuer.name='" + userName + "' and aGroup.name = '" + groupName + "'";
        return hasResults(request);
    }


    @Override
    protected boolean objectExists(String objectName, ObjectType objectType) {
        return hasResults("select 1 from sysobjects where type = '" + objectType + "' and name='"
                          + objectName + "'");
    }


    @Override
    protected boolean exists(SqlIndex index) {
        String request =
              "select 1 from sysobjects o, sysindexes i where o.id = i.id and i.name = '" + getName(index)
              + "' and o.name = '" + getName(index.getTable()) + "' and o.type = 'U'";
        return hasResults(request);
    }


    @Override
    protected boolean exists(SqlConstraint constraint) {
        String request =
              "select 1 " + "from sysobjects o1                         "
              + "  inner join sysconstraints c on (o1.id = c.constrid)  "
              + "  inner join sysobjects o2 on (c.tableid = o2.id)      "
              + "  where o1.type = 'RI'                                 "
              + "  and o2.name = '" + getName(constraint.getAlteredTable()) + "'"
              + "  and o1.name = '" + getName(constraint) + "'";
        return hasResults(request);
    }
}
