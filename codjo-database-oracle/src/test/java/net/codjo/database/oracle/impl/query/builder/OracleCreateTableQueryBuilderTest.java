package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilderTest;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static org.junit.Assert.assertTrue;
public class OracleCreateTableQueryBuilderTest extends AbstractCreateTableQueryBuilderTest {

    @Override
    protected AbstractCreateTableQueryBuilder createQueryBuilder() {
        return new OracleCreateTableQueryBuilder();
    }


    @Override
    protected String getCreateTemporaryTableQuery() {
        return "create global temporary table MA_TABLE ( COL1 varchar(255) ) on commit delete rows";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    public void test_hasDeleteRowStrategy_temporaryTable() throws Exception {
        getQueryBuilder()
              .table(temporaryTable("MA_TABLE"))
              .withContent("COL1 varchar(255)");
        assertTrue(getQueryBuilder().hasDeleteRowStrategy());
    }
}
