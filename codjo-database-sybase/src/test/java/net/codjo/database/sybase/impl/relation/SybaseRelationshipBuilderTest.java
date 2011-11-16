package net.codjo.database.sybase.impl.relation;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlField.fields;
import net.codjo.database.common.api.structure.SqlTable;
import static net.codjo.database.common.api.structure.SqlTable.table;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilderTest;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.junit.Test;
public class SybaseRelationshipBuilderTest extends RelationshipBuilderTest {

    @Override
    protected RelationshipBuilder createRelationshipBuilder() {
        return new SybaseRelationshipBuilder();
    }


    @Test
    public void test_twoTables_oneFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        relationship = relationshipBuilder.get(jdbcFixture.getConnection());

        Map<String, List<String>> fatherToSon = new HashMap<String, List<String>>();
        List<String> sons = new ArrayList<String>();
        sons.add("JDBC_TEST_1");
        fatherToSon.put("JDBC_FIXTURE_TEST", sons);

        assertEqualsNotCaseSensitive(fatherToSon, relationship.getFatherToSon());

        Map<String, List<String>> sonToFather = new HashMap<String, List<String>>();
        List<String> fathers = new ArrayList<String>();
        fathers.add("JDBC_FIXTURE_TEST");
        sonToFather.put("JDBC_TEST_1", fathers);

        assertEqualsNotCaseSensitive(sonToFather, relationship.getSonToFather());
    }


    @Test
    public void test_threeTables_sameFather() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create  unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));

        relationship = relationshipBuilder.get(jdbcFixture.getConnection());

        Map<String, List<String>> fatherToSon = new HashMap<String, List<String>>();
        List<String> sons = new ArrayList<String>();
        sons.add("JDBC_TEST_1");
        sons.add("JDBC_TEST_2");
        fatherToSon.put("JDBC_FIXTURE_TEST", sons);

        assertEqualsNotCaseSensitive(fatherToSon, relationship.getFatherToSon());

        Map<String, List<String>> sonToFather = new HashMap<String, List<String>>();
        List<String> fathers = new ArrayList<String>();
        fathers.add("JDBC_FIXTURE_TEST");
        sonToFather.put("JDBC_TEST_1", fathers);
        sonToFather.put("JDBC_TEST_2", fathers);

        assertEqualsNotCaseSensitive(sonToFather, relationship.getSonToFather());
    }


    @Test
    public void test_threeTables_twoFathers() throws SQLException {
        jdbcFixture.create(SqlTable.table("JDBC_FIXTURE_TEST"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(SqlTable.table("JDBC_TEST_2"), "COL_A varchar(5)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_FIXTURE_TEST on JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_TEST_1 on JDBC_TEST_1 (COL_A)");
        jdbcFixture.advanced().create(foreignKey("FK_1",
                                                 table("JDBC_TEST_1"), fields("COL_A"),
                                                 table("JDBC_FIXTURE_TEST"), fields("COL_A")));
        jdbcFixture.advanced().create(foreignKey("FK_2",
                                                 table("JDBC_TEST_2"), fields("COL_A"),
                                                 table("JDBC_TEST_1"), fields("COL_A")));

        relationship = relationshipBuilder.get(jdbcFixture.getConnection());

        Map<String, List<String>> fatherToSon = new HashMap<String, List<String>>();
        List<String> sons = new ArrayList<String>();
        sons.add("JDBC_TEST_1");
        fatherToSon.put("JDBC_FIXTURE_TEST", sons);
        sons = new ArrayList<String>();
        sons.add("JDBC_TEST_2");
        fatherToSon.put("JDBC_TEST_1", sons);

        assertEqualsNotCaseSensitive(fatherToSon, relationship.getFatherToSon());

        Map<String, List<String>> sonToFather = new HashMap<String, List<String>>();
        List<String> fathers = new ArrayList<String>();
        fathers.add("JDBC_FIXTURE_TEST");
        sonToFather.put("JDBC_TEST_1", fathers);
        fathers = new ArrayList<String>();
        fathers.add("JDBC_TEST_1");
        sonToFather.put("JDBC_TEST_2", fathers);

        assertEqualsNotCaseSensitive(sonToFather, relationship.getSonToFather());
    }
}
