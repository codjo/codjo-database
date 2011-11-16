package net.codjo.database.hsqldb.repository;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.AbstractDatabaseRepositoryTest;
import org.junit.Test;
public class HsqldbDatabaseRepositoryTest extends AbstractDatabaseRepositoryTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDatabaseRepository createDatabaseRepository() {
        return new HsqldbDatabaseRepository();
    }
}