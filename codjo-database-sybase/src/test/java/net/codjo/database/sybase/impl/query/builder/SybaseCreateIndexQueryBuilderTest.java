package net.codjo.database.sybase.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import net.codjo.database.common.api.structure.SqlIndex;
import static net.codjo.database.common.api.structure.SqlIndex.index;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.impl.query.builder.AbstractCreateIndexQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateIndexQueryBuilderTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class SybaseCreateIndexQueryBuilderTest extends AbstractCreateIndexQueryBuilderTest {

    @Override
    protected AbstractCreateIndexQueryBuilder createQueryBuilder() {
        return new SybaseCreateIndexQueryBuilder();
    }


    @Test
    public void test_getCreateIndex_longIndexName() throws Exception {
        SqlIndex index = index("X1_VERY_VERY_VERY_VERY_VERY_VERY_VERY_VERY_LONG_INDEX_NAME",
                               table("AP_TOTO"));
        index.addField(fieldName("PORTFOLIO_CODE"));
        assertEquals("create index X1_VERY_VERY_VERY_VERY_VERY_VE on AP_TOTO (PORTFOLIO_CODE)",
                     getQueryBuilder().index(index).get());
    }


    @Override
    protected String getCreateIndexQuery() {
        return "create index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreateUniqueIndexQuery() {
        return "create unique index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreateClusteredIndexQuery() {
        return "create clustered index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreateUniqueClusteredIndexQuery() {
        return "create unique clustered index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreatePrimaryKeyQuery() {
        return "create unique index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreatePrimaryKeyClusteredQuery() {
        return "create unique clustered index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }
}
