package net.codjo.database.api;
/**
 *
 */
public class DatabaseTester {
    private Engine currentEngine;


    DatabaseTester(Engine currentEngine) {
        this.currentEngine = currentEngine;
    }


    public Database createDatabase() {
        return DatabaseFactory.create(currentEngine);
    }
}
