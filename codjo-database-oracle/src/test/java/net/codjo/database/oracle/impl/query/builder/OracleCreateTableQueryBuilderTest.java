package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilderTest;
import org.junit.Test;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertThat;
public class OracleCreateTableQueryBuilderTest extends AbstractCreateTableQueryBuilderTest {
    static final boolean DELETE_ROW_STRATEGY = true;


    @Override
    protected AbstractCreateTableQueryBuilder createQueryBuilder() {
        return new OracleCreateTableQueryBuilder(DELETE_ROW_STRATEGY);
    }


    @Override
    protected String getCreateTemporaryTableQuery() {
        return "create global temporary table MA_TABLE ( COL1 varchar(255) ) on commit delete rows";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    public void test_hasDeleteRowStrategy() throws Exception {
        assertThat(getQueryBuilder().hasDeleteRowStrategy(), is(DELETE_ROW_STRATEGY));
    }
}
