package net.codjo.database.common.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlIndex.Type;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public abstract class AbstractCreateIndexQueryBuilderTest extends AbstractQueryBuilderTest<AbstractCreateIndexQueryBuilder> {

    @Test
    public void test_createIndex() throws Exception {
        getQueryBuilder().index(createIndex(Type.NORMAL));
        assertEquals(getCreateIndexQuery(), getQueryBuilder().get());
    }


    @Test
    public void test_createIndex_unique() throws Exception {
        assertEquals(getCreateUniqueIndexQuery(),
                     getQueryBuilder().index(createIndex(Type.UNIQUE)).get());
    }


    @Test
    public void test_createIndex_clustered() throws Exception {
        assertEquals(getCreateClusteredIndexQuery(),
                     getQueryBuilder().index(createIndex(Type.CLUSTERED)).get());
    }


    @Test
    public void test_createIndex_uniqueClustered() throws Exception {
        assertEquals(getCreateUniqueClusteredIndexQuery(),
                     getQueryBuilder().index(createIndex(Type.UNIQUE_CLUSTERED)).get());
    }


    @Test
    public void test_createIndex_primaryKey() throws Exception {
        assertEquals(getCreatePrimaryKeyQuery(),
                     getQueryBuilder().index(createIndex(Type.PRIMARY_KEY)).get());
    }


    @Test
    public void test_createIndex_primaryKeyClustered() throws Exception {
        assertEquals(getCreatePrimaryKeyClusteredQuery(),
                     getQueryBuilder().index(createIndex(Type.PRIMARY_KEY_CLUSTERED)).get());
    }


    protected abstract String getCreateIndexQuery();


    protected abstract String getCreateUniqueIndexQuery();


    protected abstract String getCreateClusteredIndexQuery();


    protected abstract String getCreateUniqueClusteredIndexQuery();


    protected abstract String getCreatePrimaryKeyQuery();


    protected abstract String getCreatePrimaryKeyClusteredQuery();


    private SqlIndex createIndex(Type type) {
        SqlIndex index = SqlIndex.index("X1_AP_TOTO", table("AP_TOTO"));
        index.setType(type);
        index.addField(fieldName("PORTFOLIO_CODE"));
        index.addField(fieldName("AUTOMATIC_UPDATE"));
        return index;
    }
}
