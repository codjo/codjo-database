package net.codjo.database.common.impl.query.builder;
import org.junit.Test;
public class DefaultInsertQueryBuilderTest extends AbstractInsertQueryBuilderTest {

    @Override
    protected AbstractInsertQueryBuilder createQueryBuilder() {
        return new DefaultInsertQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "insert into MA_TABLE ( COL1, COL2 ) values ( 'A', 2 )";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
