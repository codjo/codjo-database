package net.codjo.database.common.impl.query.builder;
import org.junit.Test;
public class DefaultUpdateQueryBuilderTest extends AbstractUpdateQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractUpdateQueryBuilder createQueryBuilder() {
        return new DefaultUpdateQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "update MA_TABLE set COL1='VAL1', COL2=2";
    }
}
