package net.codjo.database.hsqldb.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilderTest;
import org.junit.Test;
public class HsqldbDropConstraintQueryBuilderTest extends AbstractDropConstraintQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDropConstraintQueryBuilder createQueryBuilder() {
        return new HsqldbDropConstraintQueryBuilder();
    }


    @Override
    protected String getForeignKeyQuery() {
        return "alter table AP_TOTO drop constraint FK_TOTO";
    }
}
