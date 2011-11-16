package net.codjo.database.common.api;
import java.sql.SQLException;
import java.util.Collection;
public interface DatabaseComparator {

    void setIgnoredTableNames(Collection<String> ignoredTableNames);


    void setIgnoredProcedureNames(Collection<String> ignoredProcedureNames);


    void loadActualStructure(ConnectionMetadata connectionMetadata)
          throws SQLException, ClassNotFoundException;


    void loadExpectedStructure(ConnectionMetadata connectionMetadata)
          throws SQLException, ClassNotFoundException;


    boolean areDatabasesDifferent();
}
