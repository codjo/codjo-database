package net.codjo.database.sybase.impl.comparator;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseComparator;
import net.codjo.database.common.api.DatabaseHelper;
import java.sql.SQLException;
import java.util.Collection;
public class SybaseDatabaseComparator implements DatabaseComparator {
    private net.codjo.database.analyse.DatabaseComparator databaseComparator
          = new net.codjo.database.analyse.DatabaseComparator();
    private DatabaseHelper databaseHelper;


    public SybaseDatabaseComparator(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    public void setIgnoredTableNames(Collection<String> ignoredTableNames) {
        databaseComparator.setIgnoredTableNames(ignoredTableNames);
    }


    public void setIgnoredProcedureNames(Collection<String> ignoredProcedureNames) {
        databaseComparator.setIgnoredProcedureNames(ignoredProcedureNames);
    }


    public void loadActualStructure(ConnectionMetadata connectionMetadata)
          throws SQLException, ClassNotFoundException {
        databaseComparator.loadActualStructure(databaseHelper.getConnectionUrl(connectionMetadata),
                                               connectionMetadata.getUser(),
                                               connectionMetadata.getPassword(),
                                               connectionMetadata.getBase(),
                                               connectionMetadata.getCatalog());
    }


    public void loadExpectedStructure(ConnectionMetadata connectionMetadata)
          throws SQLException, ClassNotFoundException {
        databaseComparator.loadExpectedStructure(databaseHelper.getConnectionUrl(connectionMetadata),
                                                 connectionMetadata.getUser(),
                                                 connectionMetadata.getPassword(),
                                                 connectionMetadata.getBase(),
                                                 connectionMetadata.getCatalog());
    }


    public boolean areDatabasesDifferent() {
        return databaseComparator.areDatabasesDifferent();
    }
}
