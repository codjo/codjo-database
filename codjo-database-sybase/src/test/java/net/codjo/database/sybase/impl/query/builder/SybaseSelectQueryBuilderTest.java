package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractSelectQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractSelectQueryBuilderTest;
import org.junit.Test;
public class SybaseSelectQueryBuilderTest extends AbstractSelectQueryBuilderTest {

    @Override
    protected AbstractSelectQueryBuilder createQueryBuilder() {
        return new SybaseSelectQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "select COL1, COL2 from #MA_TABLE";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
