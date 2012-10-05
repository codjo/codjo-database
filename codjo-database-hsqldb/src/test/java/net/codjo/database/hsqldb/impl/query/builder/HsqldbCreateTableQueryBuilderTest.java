package net.codjo.database.hsqldb.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilderTest;
import org.junit.Test;
public class HsqldbCreateTableQueryBuilderTest extends AbstractCreateTableQueryBuilderTest {

    @Override
    protected AbstractCreateTableQueryBuilder createQueryBuilder() {
        return new HsqldbCreateTableQueryBuilder();
    }


    @Override
    protected String getCreateTemporaryTableQuery() {
        return "create temp table MA_TABLE ( COL1 varchar(255) ) ON COMMIT PRESERVE ROWS";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
