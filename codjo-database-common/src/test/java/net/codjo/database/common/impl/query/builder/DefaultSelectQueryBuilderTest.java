package net.codjo.database.common.impl.query.builder;
import org.junit.Test;
public class DefaultSelectQueryBuilderTest extends AbstractSelectQueryBuilderTest {

    @Override
    protected AbstractSelectQueryBuilder createQueryBuilder() {
        return new DefaultSelectQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "select COL1, COL2 from MA_TABLE";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
