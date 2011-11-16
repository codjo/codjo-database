package net.codjo.database.mysql.impl.sqlfield;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilderTest;
import java.sql.SQLException;
import org.junit.Before;
import org.junit.Test;
public class MysqlSQLFieldListBuilderTest extends SQLFieldListBuilderTest {

    @Override
    protected SQLFieldListBuilder createSQLFieldListBuilder() {
        return new MysqlSQLFieldListBuilder();
    }


    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();
        jdbcFixture.executeUpdate("SET storage_engine=INNODB");
    }


    @Test
    public void test_allowIdeaRun() throws SQLException {
    }
}