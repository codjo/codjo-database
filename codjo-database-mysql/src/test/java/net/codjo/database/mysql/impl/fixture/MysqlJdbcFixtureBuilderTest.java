package net.codjo.database.mysql.impl.fixture;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilderTest;
import net.codjo.database.mysql.impl.helper.MysqlDatabaseHelper;
import net.codjo.database.mysql.impl.query.MysqlDatabaseQueryHelper;
import org.junit.Test;
public class MysqlJdbcFixtureBuilderTest extends JdbcFixtureBuilderTest {

    @Override
    protected JdbcFixtureBuilder createJdbcFixtureBuilder() {
        return new MysqlJdbcFixtureBuilder(new MysqlDatabaseHelper(new MysqlDatabaseQueryHelper()));
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}