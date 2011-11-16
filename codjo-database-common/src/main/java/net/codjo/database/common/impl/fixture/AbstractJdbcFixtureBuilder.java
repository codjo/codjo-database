package net.codjo.database.common.impl.fixture;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
public abstract class AbstractJdbcFixtureBuilder implements JdbcFixtureBuilder {
    private final DatabaseHelper databaseHelper;


    protected AbstractJdbcFixtureBuilder(DatabaseHelper databaseHelper) {
        this.databaseHelper = databaseHelper;
    }


    public DatabaseHelper getDatabaseHelper() {
        return databaseHelper;
    }
}
