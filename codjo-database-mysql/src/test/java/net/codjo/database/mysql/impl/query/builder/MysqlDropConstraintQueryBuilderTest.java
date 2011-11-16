package net.codjo.database.mysql.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilderTest;
import org.junit.Test;
public class MysqlDropConstraintQueryBuilderTest extends AbstractDropConstraintQueryBuilderTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDropConstraintQueryBuilder createQueryBuilder() {
        return new MysqlDropConstraintQueryBuilder();
    }


    @Override
    protected String getForeignKeyQuery() {
        return "alter table AP_TOTO drop foreign key FK_TOTO";
    }
}
