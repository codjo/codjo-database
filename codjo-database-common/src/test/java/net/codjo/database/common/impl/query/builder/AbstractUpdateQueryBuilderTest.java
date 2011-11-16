package net.codjo.database.common.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlField.field;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractUpdateQueryBuilderTest {
    private AbstractUpdateQueryBuilder queryBuilder;


    @Before
    public void setUp() {
        queryBuilder = createQueryBuilder();
    }


    protected abstract AbstractUpdateQueryBuilder createQueryBuilder();


    protected final AbstractUpdateQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }


    @Test
    public void test_nominal() throws Exception {
        queryBuilder
              .table(table("MA_TABLE"))
              .setFields(field("COL1", "VAL1"), field("COL2", 2));

        assertEquals("update MA_TABLE set COL1='VAL1', COL2=2", queryBuilder.get());
    }


    @Test
    public void test_temporaryTable() throws Exception {
        queryBuilder
              .table(temporaryTable("MA_TABLE"))
              .setFields(field("COL1", "VAL1"), field("COL2", 2));

        assertEquals(getTemporaryTableQuery(), queryBuilder.get());
    }


    protected abstract String getTemporaryTableQuery();
}
