package net.codjo.database.common.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlField.field;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractQueryBuilderTest<T extends AbstractQueryBuilder> {
    private T queryBuilder;


    @Before
    public final void setupQueryBuilder() {
        queryBuilder = createQueryBuilder();
    }


    protected final T getQueryBuilder() {
        return queryBuilder;
    }


    protected abstract T createQueryBuilder();


    @Test
    public void test_formatFieldValue_string() throws Exception {
        assertEquals("'A_STRING'", queryBuilder.formatFieldValue(field("COL_STRING", "A_STRING")));
    }
}
