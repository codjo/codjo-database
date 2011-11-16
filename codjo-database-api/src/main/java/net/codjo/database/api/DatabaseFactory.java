package net.codjo.database.api;
/**
 *
 */
public class DatabaseFactory {

    private DatabaseFactory() {
    }


    public static Database create(Engine engine) {
        try {
            switch (engine) {
                case SYBASE:
                    return createDatabase("net.codjo.database.sybase.api.SybaseDatabase");
                case HSQLDB:
                case MYSQL:
                case ORACLE:
                default:
                    return new DefaultDatabase();
            }
        }
        catch (Exception cause) {
            throw new DatabaseInstantiationException(engine, cause);
        }
    }


    private static Database createDatabase(String databaseClassName) throws Exception {
        return (Database)Class.forName(databaseClassName).newInstance();
    }


    public static class DatabaseInstantiationException extends RuntimeException {

        public DatabaseInstantiationException(Engine engine, Exception cause) {
            super("Le moteur '" + engine.toString().toLowerCase() + "' ne peut être créé."
                  + " Avez vous la dépendance sur 'agf-database-"
                  + engine.toString().toLowerCase() + "-api' ?",
                  cause);
        }
    }
}
