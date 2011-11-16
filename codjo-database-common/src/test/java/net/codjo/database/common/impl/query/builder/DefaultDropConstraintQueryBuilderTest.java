package net.codjo.database.common.impl.query.builder;
import org.junit.Test;
public class DefaultDropConstraintQueryBuilderTest extends AbstractDropConstraintQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDropConstraintQueryBuilder createQueryBuilder() {
        return new DefaultDropConstraintQueryBuilder();
    }


    @Override
    protected String getForeignKeyQuery() {
        return "alter table AP_TOTO drop constraint FK_TOTO";
    }
}
