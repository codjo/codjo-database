package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.Relationship;
import net.codjo.database.common.api.structure.SqlTable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
public abstract class RelationshipBuilderTest {
    protected final DatabaseFactory databaseFactory = new DatabaseFactory();
    protected final DatabaseHelper databaseHelper = databaseFactory.createDatabaseHelper();
    protected RelationshipBuilder relationshipBuilder;
    protected JdbcFixture jdbcFixture;
    protected Relationship relationship;


    protected abstract RelationshipBuilder createRelationshipBuilder();


    @Before
    public void setUp() throws Exception {
        jdbcFixture = createJdbcFixture();
        jdbcFixture.doSetUp();
        relationshipBuilder = createRelationshipBuilder();
        // TODO : ajouter un prérequis : pas de table en base
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
    }


    @Test
    public void test_noTables() throws SQLException {
        relationship = relationshipBuilder.get(jdbcFixture.getConnection());
        Assert.assertTrue(relationship.getFatherToSon().size() == 0);
        Assert.assertTrue(relationship.getSonToFather().size() == 0);
    }


    @Test
    public void test_oneTable_noFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        relationship = relationshipBuilder.get(jdbcFixture.getConnection());

        Assert.assertTrue(relationship.getFatherToSon().size() == 0);
        Assert.assertTrue(relationship.getSonToFather().size() == 0);
    }


    @Test
    public void test_twoTables_noFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        relationship = relationshipBuilder.get(jdbcFixture.getConnection());

        Assert.assertTrue(relationship.getFatherToSon().size() == 0);
        Assert.assertTrue(relationship.getSonToFather().size() == 0);
    }


    protected void assertEqualsNotCaseSensitive(Map<String, List<String>> expected,
                                                Map<String, List<String>> actual) {
        Assert.assertEquals(mapToUppercase(expected), mapToUppercase(actual));
    }


    private Map<String, List<String>> mapToUppercase(Map<String, List<String>> map) {
        if (map == null) {
            return null;
        }
        Map<String, List<String>> mapToUpper = new HashMap<String, List<String>>(map.size());
        for (Entry<String, List<String>> entry : map.entrySet()) {
            List<String> newValue = new ArrayList<String>(entry.getValue().size());
            for (String aValue : entry.getValue()) {
                newValue.add(aValue.toUpperCase());
            }

            mapToUpper.put(entry.getKey().toUpperCase(), newValue);
        }
        return mapToUpper;
    }


    protected final JdbcFixture createJdbcFixture() throws Exception {
        Properties databaseProperties = new Properties();
        databaseProperties.load(getClass().getResourceAsStream("/database-integration.properties"));
        ConnectionMetadata connectionMetadata = new ConnectionMetadata(databaseProperties);
        return JdbcFixture.newFixture(connectionMetadata);
    }
}
