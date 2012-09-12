package net.codjo.database.common.impl.script;
import java.io.File;
import java.io.IOException;
import java.util.Properties;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.ExecSqlScript.Logger;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.test.common.fixture.DirectoryFixture;
import net.codjo.util.file.FileUtil;
import org.apache.log4j.LogManager;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static net.codjo.test.common.matcher.JUnitMatchers.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;
public abstract class AbstractExecSqlScriptTest {
    private static final org.apache.log4j.Logger LOGGER = LogManager
          .getLogger(AbstractExecSqlScriptTest.class);
    private static final String NEW_LINE = System.getProperty("line.separator");
    protected JdbcFixture jdbcFixture;
    protected AbstractExecSqlScript execSqlScript;
    protected DirectoryFixture directoryFixture = DirectoryFixture.newTemporaryDirectoryFixture();
    protected ConnectionMetadata connectionMetadata;


    @Before
    public void setUp() throws Exception {
        Properties databaseProperties = new Properties();
        databaseProperties.load(getClass().getResourceAsStream("/database-integration.properties"));
        connectionMetadata = new ConnectionMetadata(databaseProperties);
        jdbcFixture = JdbcFixture.newFixture(connectionMetadata);
        execSqlScript = createExecSqlScriptTask();
        execSqlScript.setConnectionMetadata(connectionMetadata);
        execSqlScript.setLogger(new Logger() {
            public void log(String log) {
                LOGGER.info(log);
            }
        });

        jdbcFixture.doSetUp();
        directoryFixture.doSetUp();

        jdbcFixture.create(SqlTable.table("AP_TEST"), "COL_TEXT varchar(255)");
    }


    @After
    public void tearDown() throws Exception {
        jdbcFixture.doTearDown();
        directoryFixture.doTearDown();
    }


    @Test
    public void test_executeContentOfFile_scriptWithError() throws Exception {
        String applicationFile = getDeliveryFilePath("livraison-sql-KO.txt");
        try {
            execSqlScript.executeContentOfFile(applicationFile);
            fail("Erreur dans le script AP_TEST_KO.txt !");
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("AP_TEST_NOT_EXISTS"));
            jdbcFixture.assertContent(SqlTable.table("AP_TEST"), new String[][]{{"TABLE"}});
        }
    }


    @Test
    public void test_executeContentOfFile_connectionWithError()
          throws Exception {
        String applicationFile = getDeliveryFilePath("livraison-sql-KO.txt");
        try {
            execSqlScript.setConnectionMetadata(new ConnectionMetadata());
            execSqlScript.executeContentOfFile(applicationFile);
            fail("Erreur dans la connection !");
        }
        catch (Exception e) {
            jdbcFixture.assertIsEmpty(SqlTable.table("AP_TEST"));
        }
    }


    @Test
    public void test_executeContentOfFile_noApplicationFile() throws Exception {
        String unknownAppliFile = "unknown.txt";
        try {
            execSqlScript.executeContentOfFile(unknownAppliFile);
            fail("Fichier application introuvable !");
        }
        catch (Exception e) {
            String expectedMessage =
                  "Le fichier " + unknownAppliFile + " est introuvable";
            assertEquals(expectedMessage, e.getMessage().trim());
        }
    }


    @Test
    public void test_executeContentOfFile_invalidContentsFile() throws Exception {
        String appFile = getDeliveryFilePath("livraison-sql-KOBadFilenames.txt");
        try {
            execSqlScript.executeContentOfFile(appFile);
            fail("Exception attendue.");
        }
        catch (Exception e) {
            String expectedMessage = "Les fichiers suivants sont introuvables :" + NEW_LINE
                                     + ">Tables" + File.separatorChar + "AP_TEST_BAD.txt<" + NEW_LINE;
            assertEquals(expectedMessage, e.getMessage());
        }
    }


    @Test
    public void test_executeContentOfFile_emptyApplicationFile() throws Exception {
        File emptyFile = new File(directoryFixture, "emptyFile.txt");
        emptyFile.createNewFile();
        execSqlScript.executeContentOfFile(emptyFile.getPath());
        emptyFile.delete();
    }


    @Test
    public void test_executeContentOfFile_emptyScriptFile() throws Exception {
        File emptyscriptFile = new File(directoryFixture, "emptyScript.txt");
        emptyscriptFile.createNewFile();

        File applicationFile = new File(directoryFixture, "livraison-sql-emptyScript.txt");
        applicationFile.createNewFile();
        FileUtil.saveContent(applicationFile, emptyscriptFile.getName());

        execSqlScript.executeContentOfFile(applicationFile.getPath());
        emptyscriptFile.delete();
        applicationFile.delete();
    }


    @Test
    public void test_executeContentOfFile_ok() throws Exception {
        execSqlScript.executeContentOfFile(getDeliveryFilePath("livraison-sql-OK.txt"));
        jdbcFixture.assertContent(SqlTable.table("AP_TEST"), new String[][]{{"TABLE"}});
    }


    @Test
    public void test_executeContentOfFile_unixEndOfLine() throws Exception {
        executeContentOfFileUsingLineSeparator("\n");
    }


    @Test
    public void test_executeContentOfFile_windowsEndOfLine() throws Exception {
        executeContentOfFileUsingLineSeparator("\r\n");
    }


    private void executeContentOfFileUsingLineSeparator(String lineSeparator) throws IOException {
        File livraisonSql = new File(directoryFixture, "livraison-sql-OK.txt");
        directoryFixture.makeSubDirectory("Tables");
        FileUtil.copyFile(new File(getDeliveryFilePath("Tables/AP_TEST OK.txt")),
                          new File(directoryFixture, "Tables/AP_TEST OK.txt"));

        FileUtil.saveContent(livraisonSql, "Tables\\AP_TEST OK.txt"
                                           + lineSeparator
                                           + "Tables\\AP_TEST OK.txt");

        execSqlScript.executeContentOfFile(livraisonSql.getPath());
        jdbcFixture.assertContent(SqlTable.table("AP_TEST"), new String[][]{{"TABLE"}, {"TABLE"}});
    }


    @Test
    public void test_execute_ok() throws Exception {
        execSqlScript.execute(getScriptDirectoryPath(), "Tables\\AP_TEST OK.txt", "Tables\\AP_TEST OK.txt");
        jdbcFixture.assertContent(SqlTable.table("AP_TEST"), new String[][]{{"TABLE"}, {"TABLE"}});
    }


    @Test
    public void test_execute_unexistingScript() throws Exception {
        try {
            execSqlScript
                  .execute(getScriptDirectoryPath(), "Tables\\AP_TEST OK.txt",
                           "Tables\\AP_TEST_UNEXISTING.txt");
            fail("Script inexistant");
        }
        catch (Exception ex) {
            jdbcFixture.assertContent(SqlTable.table("AP_TEST"), null);
        }
    }


    @Test
    public void test_execute_wrongWorkingDirectory() throws Exception {
        try {
            execSqlScript.execute("C:\\Dev", "Tables\\AP_TEST OK.txt");
            fail("Mauvais répertoire de travail");
        }
        catch (Exception ex) {
            jdbcFixture.assertIsEmpty(SqlTable.table("AP_TEST"));
        }
    }


    protected abstract AbstractExecSqlScript createExecSqlScriptTask();


    protected abstract String getDeliveryFilePath(String deliveryFileName);


    protected abstract String getScriptDirectoryPath();
}
