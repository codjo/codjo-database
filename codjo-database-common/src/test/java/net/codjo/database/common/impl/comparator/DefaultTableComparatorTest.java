package net.codjo.database.common.impl.comparator;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.TableComparator;
import net.codjo.database.common.api.structure.SqlTable;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Properties;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
public abstract class DefaultTableComparatorTest {
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected JdbcFixture jdbcFixture;
    protected TableComparator tableComparator;


    @Before
    public void setUp() throws Exception {
        jdbcFixture = createJdbcFixture();
        jdbcFixture.doSetUp();
        tableComparator = new DefaultTableComparator(jdbcFixture.getConnection(), -1, "");
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_equals_BadComparison() throws Exception {
        createTables("TABLE_1", "TABLE_2");
        insertValues("TABLE_1", "a", "b", "c", BigDecimal.valueOf(1));
        insertValues("TABLE_2", "a", "e", "c", BigDecimal.valueOf(1));

        assertEquals(tableComparator.equals("TABLE_1", "TABLE_2"), false);
    }


    @Test
    public void test_equals_GoodComparison() throws Exception {
        createTables("TABLE_1", "TABLE_2");
        insertValues("TABLE_1", "a", "b", "c", BigDecimal.valueOf(1));
        insertValues("TABLE_2", "a", "b", "c", BigDecimal.valueOf(1));

        assertEquals(tableComparator.equals("TABLE_1", "TABLE_2"), true);
    }


    @Test
    public void test_isEqual() {
        assertTrue("null, null", tableComparator.isEqual(null, null));
        assertFalse("!null, null", tableComparator.isEqual(tableComparator, null));
        assertTrue("int : 1,1", tableComparator.isEqual(1, 1));
        assertFalse("int : 2,1", tableComparator.isEqual(2, 1));
        assertTrue("BigDecimal : 2.1 , 2.1",
                   tableComparator.isEqual(new BigDecimal(2.1), new BigDecimal(2.1)));
        assertFalse("BigDecimal : 2.1 , 2.2",
                    tableComparator.isEqual(new BigDecimal(2.1), new BigDecimal(2.2)));
        assertTrue("String : a , a", tableComparator.isEqual("a", "a"));
        assertFalse("String : a , b", tableComparator.isEqual("a", "b"));
    }


    @Test
    public void test_isEqual_Precision() {
        tableComparator.setPrecision(1);
        assertTrue("int : 2,1", tableComparator.isEqual(2, 1));
        assertTrue("int : 9,10", tableComparator.isEqual(9, 10));

        tableComparator.setPrecision(0.9);
        assertTrue("BigDecimal (precision 0.9): 2.1 , 2.8",
                   tableComparator.isEqual(new BigDecimal(2.1), new BigDecimal(2.8)));
    }


    @Test
    public void test_isEqual_Precision_BUG() {
        tableComparator.setPrecision(0.9);
        assertFalse(tableComparator.isEqual(new BigDecimal("105.85000"), new BigDecimal("10585.00000")));
        assertFalse(tableComparator.isEqual(new BigDecimal("-10"), new BigDecimal("10")));
        assertFalse(tableComparator.isEqual(new BigDecimal("10"), new BigDecimal("-10")));
    }


    @Test
    public void test_setPrecision() throws Exception {
        createTables("TABLE_1", "TABLE_2");
        insertValues("TABLE_1", "a", "b", "c", BigDecimal.valueOf(.1));
        insertValues("TABLE_2", "a", "b", "c", BigDecimal.valueOf(.01));

        tableComparator.setPrecision(0.1);
        assertEquals(tableComparator.equals("TABLE_1", "TABLE_2"), true);
    }


    protected void createTables(String tableName1, String tableName2) throws SQLException {
        createTable(tableName1);
        createTable(tableName2);
    }


    private void createTable(String tableName1) throws SQLException {
        jdbcFixture.create(SqlTable.table(tableName1), "COL_A varchar(5) null,"
                                                       + "COL_B varchar(5) null,"
                                                       + "COL_C varchar(5) null,"
                                                       + "COL_D numeric(10,2) null");
    }


    protected void insertValues(String tableName,
                                String valueColA,
                                String valueColB,
                                String valueColC,
                                BigDecimal valueColD) throws SQLException {
        jdbcFixture.executeUpdate("insert into " + tableName + " values ('"
                                  + valueColA + "','" + valueColB + "','" + valueColC + "'," + valueColD
                                  + ")");
    }


    protected final JdbcFixture createJdbcFixture() throws Exception {
        Properties databaseProperties = new Properties();
        databaseProperties.load(getClass().getResourceAsStream("/database-integration.properties"));
        ConnectionMetadata connectionMetadata = new ConnectionMetadata(databaseProperties);
        return JdbcFixture.newFixture(connectionMetadata);
    }
}
