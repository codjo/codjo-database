package net.codjo.database.mysql.repository;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.AbstractDatabaseRepositoryTest;
import org.junit.Test;
public class MysqlDatabaseRepositoryTest extends AbstractDatabaseRepositoryTest {

    @Test
    public void test_allowIdeaRun() throws Exception {
    }


    @Override
    protected AbstractDatabaseRepository createDatabaseRepository() {
        return new MysqlDatabaseRepository();
    }
}
