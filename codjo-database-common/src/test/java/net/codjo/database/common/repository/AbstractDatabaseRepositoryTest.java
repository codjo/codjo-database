package net.codjo.database.common.repository;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import org.junit.Before;
import org.junit.Test;
public abstract class AbstractDatabaseRepositoryTest {
    protected AbstractDatabaseRepository databaseRepository;


    protected abstract AbstractDatabaseRepository createDatabaseRepository();


    @Before
    public void setUp() {
        databaseRepository = createDatabaseRepository();
    }


    @Test
    public void test_getImplementation() throws Exception {
        databaseRepository.registerImplementationClass(DummyClass.class);

        assertNotNull(databaseRepository.getImplementation(DummyClass.class));
    }


    @Test
    public void test_getImplementation_noRegisteredImplementation() throws Exception {
        assertNull(databaseRepository.getImplementation(DummyClass.class));
    }


    @Test
    public void test_getDatabaseHelperImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getDatabaseHelperImplementationClass());
    }


    @Test
    public void test_getDatabaseQueryHelperImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getDatabaseQueryHelperImplementationClass());
    }


    @Test
    public void test_getDatabaseScriptHelperImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getDatabaseScriptHelperImplementationClass());
    }


    @Test
    public void test_getExecSqlScriptHelperImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getExecSqlScriptBuilderImplementationClass());
    }


    @Test
    public void test_getRelationshipBuilderImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getRelationshipBuilderImplementationClass());
    }


    @Test
    public void test_getSQLFIeldListBuilderImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getSQLFIeldListBuilderImplementationClass());
    }


    @Test
    public void test_getJdbcFixtureBuilderImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getJdbcFixtureBuilderImplementationClass());
    }


    @Test
    public void test_getDatabaseComparatorBuilderImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getDatabaseComparatorBuilderImplementationClass());
    }


    @Test
    public void test_getDatabaseTranscoderImplementationClass() throws Exception {
        assertNotNull(databaseRepository.getDatabaseTranscoderImplementationClass());
    }
}
