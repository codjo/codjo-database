package net.codjo.database.sybase.impl.comparator;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilderTest;
import net.codjo.database.sybase.impl.helper.SybaseDatabaseHelper;
import net.codjo.database.sybase.impl.query.SybaseDatabaseQueryHelper;
import org.junit.Test;
public class SybaseDatabaseComparatorBuilderTest extends DatabaseComparatorBuilderTest {

    @Override
    protected DatabaseComparatorBuilder createDatabaseComparatorBuilder() {
        return new SybaseDatabaseComparatorBuilder(new SybaseDatabaseHelper(new SybaseDatabaseQueryHelper()));
    }


    @Test
    public void test_allowIdeaRun() throws Exception {
    }
}
