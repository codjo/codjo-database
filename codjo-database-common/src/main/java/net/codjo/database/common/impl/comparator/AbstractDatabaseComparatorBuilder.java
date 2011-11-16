package net.codjo.database.common.impl.comparator;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
public abstract class AbstractDatabaseComparatorBuilder implements DatabaseComparatorBuilder {
    private final DatabaseHelper databaseHelper;


    protected AbstractDatabaseComparatorBuilder(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
