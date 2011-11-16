package net.codjo.database.sybase.impl.relation;
import net.codjo.database.common.api.Relationship;
import net.codjo.database.common.impl.relation.DefaultRelationship;
import net.codjo.database.common.impl.relation.DefaultRelationshipBuilder;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class SybaseRelationshipBuilder extends DefaultRelationshipBuilder {
    static final String EXPORTED_KEYS_REQUEST =
          "select o.name as FILS, o2.name as PERE from sysreferences r "
          + "inner join sysobjects o on (r.tableid=o.id) "
          + "inner join sysobjects o2 on (r.reftabid=o2.id)";


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
