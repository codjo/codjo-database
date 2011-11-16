package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.SQLFieldList;
import java.sql.Connection;
import java.sql.SQLException;
public interface SQLFieldListBuilder {

    SQLFieldList get(Connection connection, String catalog, String tableName, boolean temporaryTable)
          throws SQLException;
}
