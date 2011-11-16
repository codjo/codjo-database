package net.codjo.database.common.repository.builder;
import org.junit.Before;
public abstract class JdbcFixtureBuilderTest {
    protected JdbcFixtureBuilder jdbcFixtureBuilder;


    @Before
    public void setUp() throws Exception {
        jdbcFixtureBuilder = createJdbcFixtureBuilder();
    }


    protected abstract JdbcFixtureBuilder createJdbcFixtureBuilder();
}
