package net.codjo.database.oracle.impl.relation;
import net.codjo.database.common.api.Relationship;
import net.codjo.database.common.impl.relation.DefaultRelationship;
import net.codjo.database.common.impl.relation.DefaultRelationshipBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class OracleRelationshipBuilder extends DefaultRelationshipBuilder {
    static final String EXPORTED_KEYS_REQUEST =
          "select ac2.TABLE_NAME \"FILS\", ac1.TABLE_NAME \"PERE\" from user_constraints ac1 "
          + "inner join user_constraints ac2 on (ac1.CONSTRAINT_NAME = ac2.R_CONSTRAINT_NAME) "
          + "order by ac2.TABLE_NAME";


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
