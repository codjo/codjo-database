package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilderTest;
import org.junit.Test;
public class SybaseDropConstraintQueryBuilderTest extends AbstractDropConstraintQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDropConstraintQueryBuilder createQueryBuilder() {
        return new SybaseDropConstraintQueryBuilder();
    }


    @Override
    protected String getForeignKeyQuery() {
        return "alter table AP_TOTO drop constraint FK_TOTO";
    }
}
