package net.codjo.database.common.impl.query.builder;
import org.junit.Test;
public class DefaultCreateIndexQueryBuilderTest extends AbstractCreateIndexQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractCreateIndexQueryBuilder createQueryBuilder() {
        return new DefaultCreateIndexQueryBuilder();
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
        return "create index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreateUniqueClusteredIndexQuery() {
        return "create unique index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreatePrimaryKeyQuery() {
        return "create unique index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }


    @Override
    protected String getCreatePrimaryKeyClusteredQuery() {
        return "create unique index X1_AP_TOTO on AP_TOTO (PORTFOLIO_CODE, AUTOMATIC_UPDATE)";
    }
}
