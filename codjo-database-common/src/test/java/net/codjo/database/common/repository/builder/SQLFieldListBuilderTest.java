package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.temporaryTable;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public abstract class SQLFieldListBuilderTest {
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected SQLFieldListBuilder sqlFieldListBuilder;
    protected JdbcFixture jdbcFixture;


    protected abstract SQLFieldListBuilder createSQLFieldListBuilder();


    @Before
    public void setUp() throws Exception {
        sqlFieldListBuilder = createSQLFieldListBuilder();
        jdbcFixture = JdbcFixture.newFixture(databaseHelper.createLibraryConnectionMetadata());
        jdbcFixture.doSetUp();
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_constructor() throws Exception {
        jdbcFixture.create(SqlTable.table("A_TABLE"), "COL_B varchar(5) null, COL_A int null");

        SQLFieldList list = sqlFieldListBuilder.get(jdbcFixture.getConnection(), null, "A_TABLE", false);

        assertEquals(list.getFieldValue("COL_A"), null);
        assertEquals(list.getFieldType("COL_A"), java.sql.Types.INTEGER);
        assertEquals(list.getFieldValue("COL_B"), null);
        assertEquals(list.getFieldType("COL_B"), java.sql.Types.VARCHAR);
    }


    @Test
    public void test_constructor_temporaryTable() throws Exception {
        jdbcFixture.create(temporaryTable("A_TABLE"), "COL_B varchar(5) null, COL_A int null");

        SQLFieldList list = sqlFieldListBuilder.get(jdbcFixture.getConnection(), null, "A_TABLE", true);

        assertEquals(list.getFieldValue("COL_A"), null);
        assertEquals(list.getFieldType("COL_A"), java.sql.Types.INTEGER);
        assertEquals(list.getFieldValue("COL_B"), null);
        assertEquals(list.getFieldType("COL_B"), java.sql.Types.VARCHAR);
    }


    @Test
    public void test_sortDBFieldList() throws Exception {
        jdbcFixture.create(SqlTable.table("A_TABLE"), "COL_B varchar(5) null, COL_A int null");

        SQLFieldList list = sqlFieldListBuilder.get(jdbcFixture.getConnection(), null, "A_TABLE", false);

        assertEquals((list.getSortedDBFieldNameList()).get(0), "COL_A");
        assertEquals((list.getSortedDBFieldNameList()).get(1), "COL_B");
    }
}
