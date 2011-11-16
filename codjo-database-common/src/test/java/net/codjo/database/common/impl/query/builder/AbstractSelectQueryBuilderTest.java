package net.codjo.database.common.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractSelectQueryBuilderTest {
    private AbstractSelectQueryBuilder queryBuilder;


    @Before
    public void setUp() {
        queryBuilder = createQueryBuilder();
    }


    protected abstract AbstractSelectQueryBuilder createQueryBuilder();


    protected final AbstractSelectQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }


    @Test
    public void test_nominal() throws Exception {
        queryBuilder
              .fields(fieldName("COL1"), fieldName("COL2"))
              .fromTable(table("MA_TABLE"));

        assertEquals("select COL1, COL2 from MA_TABLE", queryBuilder.get());
    }


    @Test
    public void test_allColumns() throws Exception {
        queryBuilder
              .allFields()
              .fromTable(table("MA_TABLE"));

        assertEquals("select * from MA_TABLE", queryBuilder.get());
    }


    @Test
    public void test_oneColumn() throws Exception {
        queryBuilder
              .oneField()
              .fromTable(table("MA_TABLE"));

        assertEquals("select 1 from MA_TABLE", queryBuilder.get());
    }


    @Test
    public void test_temporaryTable() throws Exception {
        queryBuilder
              .fields(fieldName("COL1"), fieldName("COL2"))
              .fromTable(temporaryTable("MA_TABLE"));

        assertEquals(getTemporaryTableQuery(), queryBuilder.get());
    }


    protected abstract String getTemporaryTableQuery();
}
