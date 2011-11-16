package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractUpdateQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractUpdateQueryBuilderTest;
import org.junit.Test;
public class SybaseUpdateQueryBuilderTest extends AbstractUpdateQueryBuilderTest {

    @Override
    protected AbstractUpdateQueryBuilder createQueryBuilder() {
        return new SybaseUpdateQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "update #MA_TABLE set COL1='VAL1', COL2=2";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
