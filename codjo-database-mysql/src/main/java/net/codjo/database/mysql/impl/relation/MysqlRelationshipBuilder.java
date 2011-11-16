package net.codjo.database.mysql.impl.relation;
import net.codjo.database.common.api.Relationship;
import net.codjo.database.common.impl.relation.DefaultRelationship;
import net.codjo.database.common.impl.relation.DefaultRelationshipBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class MysqlRelationshipBuilder extends DefaultRelationshipBuilder {
    static final String EXPORTED_KEYS_REQUEST =
          "SELECT TABLE_NAME as FILS, REFERENCED_TABLE_NAME as PERE "
          + "FROM information_schema.KEY_COLUMN_USAGE "
          + "WHERE REFERENCED_TABLE_NAME is not null";


    @Override
    public Relationship get(Connection connection) throws SQLException {
        Relationship relations = new DefaultRelationship();

        Statement statement = connection.createStatement();

        try {
            ResultSet rs = statement.executeQuery(EXPORTED_KEYS_REQUEST);

            while (rs.next()) {
                relations.declare(rs.getString("FILS"), rs.getString("PERE"));
            }
        }
        finally {
            statement.close();
        }

        return relations;
    }
}
