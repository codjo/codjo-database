package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import static net.codjo.database.common.api.structure.SqlField.field;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import static net.codjo.database.common.api.structure.SqlField.fieldValue;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractInsertQueryBuilderTest {

    private AbstractInsertQueryBuilder queryBuilder;


    @Before
    public void setUp() {
        queryBuilder = createQueryBuilder();
    }


    protected abstract AbstractInsertQueryBuilder createQueryBuilder();


    protected final AbstractInsertQueryBuilder getQueryBuilder() {
        return queryBuilder;
    }


    @Test
    public void test_nominal() throws Exception {
        queryBuilder
              .intoTable(table("MA_TABLE"))
              .fields(field("COL1", "A"), field("COL2", 2));

        assertEquals("insert into MA_TABLE ( COL1, COL2 ) values ( 'A', 2 )",
                     queryBuilder.get());
    }


    @Test
    public void test_noValues() throws Exception {
        queryBuilder
              .intoTable(table("MA_TABLE"))
              .fields(fieldName("COL1"), fieldName("COL2"));

        assertEquals("insert into MA_TABLE ( COL1, COL2 ) values ( ?, ? )",
                     queryBuilder.get());
    }


    @Test
    public void test_noColumns() throws Exception {
        queryBuilder
              .intoTable(table("MA_TABLE"))
              .fields(createValuatedField("A"), createValuatedField(2));

        assertEquals("insert into MA_TABLE values ( 'A', 2 )",
                     queryBuilder.get());
    }


    @Test
    public void test_temporaryTable() throws Exception {
        queryBuilder
              .intoTable(temporaryTable("MA_TABLE"))
              .fields(field("COL1", "A"), field("COL2", 2));

        assertEquals(getTemporaryTableQuery(), queryBuilder.get());
    }


    protected abstract String getTemporaryTableQuery();


    protected SqlField createValuatedField(Object value) {
        return fieldValue(value);
    }
}
