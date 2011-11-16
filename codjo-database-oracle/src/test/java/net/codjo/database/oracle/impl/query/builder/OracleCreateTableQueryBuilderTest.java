package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilderTest;
import org.junit.Test;
public class OracleCreateTableQueryBuilderTest extends AbstractCreateTableQueryBuilderTest {

    @Override
    protected AbstractCreateTableQueryBuilder createQueryBuilder() {
        return new OracleCreateTableQueryBuilder();
    }


    @Override
    protected String getCreateTemporaryTableQuery() {
        return "create global temporary table MA_TABLE ( COL1 varchar(255) ) on commit preserve rows";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
