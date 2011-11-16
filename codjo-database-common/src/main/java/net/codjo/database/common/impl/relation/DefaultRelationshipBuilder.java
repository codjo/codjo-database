package net.codjo.database.common.impl.relation;
import net.codjo.database.common.api.Relationship;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
public class DefaultRelationshipBuilder implements RelationshipBuilder {

    public Relationship get(Connection connection) throws SQLException {
        Relationship relations = new DefaultRelationship();

        ResultSet resultSet = connection.getMetaData().getTables(connection.getCatalog(), null, "%", null);
        try {
            while (resultSet.next()) {
                String tableName = resultSet.getString("TABLE_NAME");
                getSonTableName(connection, tableName, relations);
            }
        }
        finally {
            resultSet.close();
        }

        return relations;
    }


    private void getSonTableName(Connection connection, String tableName, Relationship relations)
          throws SQLException {
        ResultSet resultSet = connection.getMetaData()
              .getExportedKeys(connection.getCatalog(), null, tableName);
        try {
            while (resultSet.next()) {
                String fktableName = resultSet.getString("FKTABLE_NAME");
                relations.declare(fktableName, tableName);
            }
        }
        finally {
            resultSet.close();
        }
    }
}
