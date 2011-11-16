package net.codjo.database.hsqldb.impl.sqlfield;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldListBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilderTest;
import java.sql.SQLException;
import org.junit.Test;
public class HsqldbSQLFieldListBuilderTest extends SQLFieldListBuilderTest {

    @Override
    protected SQLFieldListBuilder createSQLFieldListBuilder() {
        return new DefaultSQLFieldListBuilder();
    }


    @Test
    public void test_allowIdeaRun() throws SQLException {
    }
}
