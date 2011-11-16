package net.codjo.database.sybase.impl.comparator;
import net.codjo.database.common.api.DatabaseComparator;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.impl.comparator.AbstractDatabaseComparatorBuilder;
public class SybaseDatabaseComparatorBuilder extends AbstractDatabaseComparatorBuilder {
    public SybaseDatabaseComparatorBuilder(DatabaseHelper databaseHelper) {
        super(databaseHelper);
    }


    public DatabaseComparator get() {
        return new SybaseDatabaseComparator(getDatabaseHelper());
    }
}
