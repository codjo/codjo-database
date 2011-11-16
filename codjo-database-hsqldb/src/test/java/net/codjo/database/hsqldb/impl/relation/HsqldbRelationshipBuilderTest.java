package net.codjo.database.hsqldb.impl.relation;
import net.codjo.database.common.impl.relation.DefaultRelationshipBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilderTest;
import java.sql.SQLException;
import org.junit.Test;
public class HsqldbRelationshipBuilderTest extends RelationshipBuilderTest {
    @Override
    protected RelationshipBuilder createRelationshipBuilder() {
        return new DefaultRelationshipBuilder();
    }


    @Test
    public void test_allowIdeaRun() throws SQLException {
    }
}
