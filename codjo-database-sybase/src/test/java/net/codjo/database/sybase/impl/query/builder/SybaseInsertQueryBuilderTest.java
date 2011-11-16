package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractInsertQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractInsertQueryBuilderTest;
import org.junit.Test;
public class SybaseInsertQueryBuilderTest extends AbstractInsertQueryBuilderTest {

    @Override
    protected AbstractInsertQueryBuilder createQueryBuilder() {
        return new SybaseInsertQueryBuilder();
    }


    @Override
    protected String getTemporaryTableQuery() {
        return "insert into #MA_TABLE ( COL1, COL2 ) values ( 'A', 2 )";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
