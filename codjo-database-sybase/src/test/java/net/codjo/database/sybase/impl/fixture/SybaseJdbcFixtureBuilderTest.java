package net.codjo.database.sybase.impl.fixture;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilderTest;
import net.codjo.database.sybase.impl.helper.SybaseDatabaseHelper;
import net.codjo.database.sybase.impl.query.SybaseDatabaseQueryHelper;
import org.junit.Test;
public class SybaseJdbcFixtureBuilderTest extends JdbcFixtureBuilderTest {

    @Override
    protected JdbcFixtureBuilder createJdbcFixtureBuilder() {
        return new SybaseJdbcFixtureBuilder(new SybaseDatabaseHelper(new SybaseDatabaseQueryHelper()));
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
