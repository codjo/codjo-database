package net.codjo.database.api;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;
/**
 *
 */
public class DatabaseTesterFactory {
    private DatabaseTesterFactory() {
    }


    public static DatabaseTester create(Engine engine) {
        return new DatabaseTester(engine);
    }


    public static DatabaseTester create() {
        Properties properties = loadDatabaseProperties("/database.properties", "/tokio.properties");
        Engine engine = Engine.toEngine(properties.getProperty("database.engine"));
        if (engine == null) {
            engine = Engine.SYBASE;
        }
        return new DatabaseTester(engine);
    }


    private static Properties loadDatabaseProperties(String... propertiesFiles) {
        Properties properties = new Properties();
        InputStream inputStream;
        for (String propertiesFile : propertiesFiles) {
            inputStream = DatabaseTesterFactory.class.getResourceAsStream(propertiesFile);
            if (inputStream != null) {
                try {
                    properties.load(inputStream);
                    return properties;
                }
                catch (IOException e) {
                    throw new RuntimeException("Le format de fichier de paramétrage de la base est incorrect");
                }
                finally {
                    try {
                        inputStream.close();
                    }
                    catch (IOException e) {
                        // Rien à faire.
                    }
                }
            }
        }
        throw new RuntimeException("Aucun fichier de paramétrage de la base n'a été trouvé: "
                                   + Arrays.asList(propertiesFiles));
    }
}
