package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.Relationship;
import java.sql.Connection;
import java.sql.SQLException;
public interface RelationshipBuilder {

    Relationship get(Connection connection) throws SQLException;
}
