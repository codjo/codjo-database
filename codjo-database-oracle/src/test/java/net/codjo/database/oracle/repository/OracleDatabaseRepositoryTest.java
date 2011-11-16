package net.codjo.database.oracle.repository;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.AbstractDatabaseRepositoryTest;
import org.junit.Test;
public class OracleDatabaseRepositoryTest extends AbstractDatabaseRepositoryTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDatabaseRepository createDatabaseRepository() {
        return new OracleDatabaseRepository();
    }
}