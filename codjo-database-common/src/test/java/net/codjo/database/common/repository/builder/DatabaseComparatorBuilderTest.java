package net.codjo.database.common.repository.builder;
import org.junit.Before;
public abstract class DatabaseComparatorBuilderTest {
    protected DatabaseComparatorBuilder databaseComparatorBuilder;


    @Before
    public void setUp() throws Exception {
        databaseComparatorBuilder = createDatabaseComparatorBuilder();
    }


    protected abstract DatabaseComparatorBuilder createDatabaseComparatorBuilder();
}
