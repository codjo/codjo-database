package net.codjo.database.mysql.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilderTest;
import org.junit.Test;
public class MysqlCreateTableQueryBuilderTest extends AbstractCreateTableQueryBuilderTest {

    @Override
    protected AbstractCreateTableQueryBuilder createQueryBuilder() {
        return new MysqlCreateTableQueryBuilder();
    }


    @Override
    protected String getCreateTemporaryTableQuery() {
        return "create temporary table MA_TABLE ( COL1 varchar(255) )";
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
