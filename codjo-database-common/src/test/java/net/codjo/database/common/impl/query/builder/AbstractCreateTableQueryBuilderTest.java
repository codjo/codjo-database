package net.codjo.database.common.impl.query.builder;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
public abstract class AbstractCreateTableQueryBuilderTest {
    private AbstractCreateTableQueryBuilder queryBuilder;


    @Before
    public void setUp() {
        queryBuilder = createQueryBuilder();
    }


    protected abstract AbstractCreateTableQueryBuilder createQueryBuilder();


    protected final AbstractCreateTableQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }


    @Test
    public void test_nominal() throws Exception {
        queryBuilder
              .table(table("MA_TABLE"))
              .withContent("COL1 varchar(255)");
        assertEquals("create table MA_TABLE ( COL1 varchar(255) )", queryBuilder.get());
    }


    @Test
    public void test_temporaryTable() throws Exception {
        queryBuilder
              .table(temporaryTable("MA_TABLE"))
              .withContent("COL1 varchar(255)");
        assertEquals(getCreateTemporaryTableQuery(), queryBuilder.get());
    }

    @Test
    public void test_hasDeleteRowStrategy() throws Exception {
        assertFalse(getQueryBuilder().hasDeleteRowStrategy());
    }


    protected abstract String getCreateTemporaryTableQuery();
}
