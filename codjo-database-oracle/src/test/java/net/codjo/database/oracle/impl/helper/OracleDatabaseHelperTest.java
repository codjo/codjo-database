package net.codjo.database.oracle.impl.helper;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.ObjectType;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.impl.helper.AbstractDatabaseHelperTest;
import net.codjo.database.oracle.impl.query.OracleDatabaseQueryHelper;
import org.junit.Test;

import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlIndex.normalIndex;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
public class OracleDatabaseHelperTest extends AbstractDatabaseHelperTest {

    @Override
    protected DatabaseHelper createDatabaseHelper() throws SQLException {
        return new OracleDatabaseHelper(new OracleDatabaseQueryHelper());
    }


    @Override
    protected String buildDatabaseUrl(Properties databaseProperties) {
        return new StringBuilder()
              .append("jdbc:oracle:thin:@")
              .append(databaseProperties.getProperty("database.hostname")).append(":")
              .append(databaseProperties.getProperty("database.port")).append(":")
              .append(databaseProperties.getProperty("database.base")).toString();
    }


    @Test
    public void test_dropForeignKey() throws Exception {
        jdbcFixture.drop(table("JDBC_TEST_1"));
        jdbcFixture.drop(table("JDBC_FIXTURE_TEST"));

        jdbcFixture.create(table("JDBC_TEST_1"), "COL_A varchar(5)");
        jdbcFixture.create(table("JDBC_FIXTURE_TEST"), "COL_A varchar(5) constraint UN_COL_A unique");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1 foreign key (COL_A) "
                                  + "references JDBC_FIXTURE_TEST (COL_A)");

        jdbcFixture.advanced()
              .assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));
        jdbcFixture.advanced()
              .assertExists(foreignKey("FK_1", table("JDBC_TEST_1")));

        databaseHelper.dropForeignKey(jdbcFixture.getConnection(),
                                      SqlConstraint.foreignKey("FK_1", SqlTable.table("JDBC_TEST_1")));

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
    }


    @Test
    public void test_dropForeignKey_bug() throws Exception {
        jdbcFixture.dropWithException(table("AP_BOOK"));
        jdbcFixture.dropWithException(table("REF_AUTHOR"));

        jdbcFixture.executeUpdate("create table AP_BOOK ( "
                                  + "    TITLE      varchar(255)  not null, "
                                  + "    AUTHOR      varchar(150)  not null "
                                  + ")");
        jdbcFixture.executeUpdate("create table REF_AUTHOR ( "
                                  + "    AUTHOR      varchar(150)  not null "
                                  + ")");

        // Index
        jdbcFixture.executeUpdate("create unique index X1_REF_AUTHOR on REF_AUTHOR (AUTHOR)");
        jdbcFixture.executeUpdate("create unique index X1_AP_BOOK on AP_BOOK (TITLE, AUTHOR)");

        // FK
        jdbcFixture.executeUpdate("alter table REF_AUTHOR add (constraint pk_REF_AUTHOR primary key (AUTHOR))");
        jdbcFixture.executeUpdate(
              "alter table AP_BOOK add constraint FK_AUTH_BOOK foreign key (AUTHOR) references REF_AUTHOR (AUTHOR)");

        // Grant
        jdbcFixture.executeUpdate(
              "grant select, insert, delete, update, references on AP_BOOK                         to APP_USER");

        // Drop FK
        databaseHelper.dropAllObjects(jdbcFixture.getConnection());

        jdbcFixture.advanced().assertDoesntExist(foreignKey("FK_AUTH_BOOK", table("AP_BOOK")));
    }


    @Test
    public void test_dropAllObjects() throws SQLException {
        jdbcFixture.executeUpdate("create table JDBC_FIXTURE_TEST (COL_A varchar(5) constraint UN_COL_A unique)");
        jdbcFixture.executeUpdate("create table JDBC_TEST_1 (COL_A varchar(5) constraint UN1_COL_A unique)");
        jdbcFixture.executeUpdate("create table JDBC_TEST_2 (COL_A varchar(5))");

        jdbcFixture.executeUpdate(
              "create unique index X1_JDBC_TEST_2 on JDBC_TEST_2 (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_1 add constraint FK_1"
                                  + " foreign key (COL_A) references JDBC_FIXTURE_TEST (COL_A)");
        jdbcFixture.executeUpdate("alter table JDBC_TEST_2 add constraint FK_2"
                                  + " foreign key (COL_A) references JDBC_TEST_1 (COL_A)");

        jdbcFixture.executeUpdate("CREATE VIEW VIEW1 AS SELECT COL_A FROM JDBC_TEST_1");
        jdbcFixture.advanced().assertObjectExists("VIEW1", ObjectType.VIEW);

        jdbcFixture.executeUpdate(
              "CREATE PROCEDURE PROC1 AS BEGIN insert into JDBC_TEST_1 values ('aa'); END ;");
        jdbcFixture.advanced().assertObjectExists("PROC1", ObjectType.STORED_PROCEDURE);

        jdbcFixture.executeUpdate("CREATE PROCEDURE PROC2 AS BEGIN PROC1; END ;");
        jdbcFixture.advanced().assertObjectExists("PROC2", ObjectType.STORED_PROCEDURE);

        jdbcFixture.executeUpdate("CREATE TRIGGER TR1 BEFORE UPDATE ON JDBC_TEST_1 FOR EACH ROW BEGIN END");
        jdbcFixture.advanced().assertObjectExists("TR1", ObjectType.TRIGGER);

        jdbcFixture.executeUpdate("CREATE SEQUENCE SEQ1 START WITH 10 INCREMENT BY 10 NOMAXVALUE");
        assertTrue(sequenceExist("SEQ1"));

        jdbcFixture.executeUpdate("CREATE FUNCTION FCT1\n"
                                  + "RETURN NUMBER\n"
                                  + "IS\n"
                                  + "val_number NUMBER (10,2);\n"
                                  + "BEGIN\n"
                                  + "val_number:=1;\n"
                                  + "RETURN (val_number);\n"
                                  + "END;");
        assertTrue(functionExist("FCT1"));

        jdbcFixture.executeUpdate("CREATE PACKAGE PKG1 AS\n"
                                  + "FUNCTION FCT_1 return number;\n"
                                  + "END PKG1;");
        assertTrue(packageExist("PKG1"));

        databaseHelper.dropAllObjects(jdbcFixture.getConnection());

        jdbcFixture.advanced()
              .assertDoesntExist(foreignKey("FK_1", table("JDBC_TEST_1")));
        jdbcFixture.advanced()
              .assertDoesntExist(foreignKey("FK_2", table("JDBC_TEST_2")));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_FIXTURE_TEST"));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_TEST_1"));
        jdbcFixture.advanced().assertDoesntExist(table("JDBC_TEST_2"));
        jdbcFixture.advanced()
              .assertDoesntExist(normalIndex("X1_JDBC_TEST_2", table("JDBC_TEST_2")));
        jdbcFixture.advanced().assertObjectDoesntExist("VIEW1", ObjectType.VIEW);
        jdbcFixture.advanced().assertObjectDoesntExist("PROC1", ObjectType.STORED_PROCEDURE);
        jdbcFixture.advanced().assertObjectDoesntExist("PROC2", ObjectType.STORED_PROCEDURE);
        jdbcFixture.advanced().assertObjectDoesntExist("TR1", ObjectType.TRIGGER);
        assertFalse(sequenceExist("SEQ1"));
        assertFalse(functionExist("FCT1"));
        assertFalse(packageExist("PKG1"));
    }


    @Override
    protected void assertLibraryConnectionMetadata(ConnectionMetadata connectionMetadata) throws IOException {
        Properties properties = new Properties();
        properties.load(getClass().getResourceAsStream("/database-integration.properties"));
        assertEquals(properties.get("database.hostname"), connectionMetadata.getHostname());
        assertEquals(properties.get("database.port"), connectionMetadata.getPort());
        assertEquals(properties.get("database.user"), connectionMetadata.getUser());
        assertEquals(properties.get("database.password"), connectionMetadata.getPassword());
        assertEquals(properties.get("database.catalog"), connectionMetadata.getCatalog());
        assertEquals(properties.get("database.hostname"), connectionMetadata.getHostname());
        assertNull(connectionMetadata.getCharset());
        assertEquals(properties.get("database.schema"), connectionMetadata.getSchema());
    }


    private boolean packageExist(String packageName) {
        return objectExist(packageName, "user_procedures", "OBJECT_NAME");
    }


    private boolean functionExist(String functionName) {
        return objectExist(functionName, "user_procedures", "OBJECT_NAME");
    }


    private boolean sequenceExist(String sequenceName) {
        return objectExist(sequenceName, "user_sequences", "SEQUENCE_NAME");
    }


    private boolean objectExist(String objectName, String systemTableName, String systemColumnName) {
        ResultSet resultSet = null;
        try {
            resultSet = jdbcFixture.getConnection().createStatement()
                  .executeQuery("select " + systemColumnName + " from " + systemTableName);

            while (resultSet.next()) {
                String proc = resultSet.getString(systemColumnName);
                if (objectName.equals(proc)) {
                    return true;
                }
            }
        }
        catch (SQLException e) {
            ;
        }
        finally {
            if (resultSet != null) {
                try {
                    resultSet.close();
                }
                catch (SQLException e) {
                    ;
                }
            }
        }
        return false;
    }


    @Test
    public void test_changeUserGroup() throws Exception {
        databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "ROLE_UTILISATEUR_IDW");
        jdbcFixture.advanced().assertUserInGroup("APP_USER", "ROLE_UTILISATEUR_IDW");
        try {
            databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "CONNECT");
            jdbcFixture.advanced().assertUserInGroup("APP_USER", "CONNECT");
        }
        finally {
            jdbcFixture.executeUpdate("REVOKE CONNECT FROM APP_USER");
        }
    }


    @Test
    public void test_changeUserGroup_badGroup() throws Exception {
        try {
            databaseHelper.changeUserGroup(jdbcFixture.getConnection(), "APP_USER", "BadGroup");
            fail("Erreur car le group n'existe pas.");
        }
        catch (SQLException exception) {
            ;
        }
    }


    @Override
    public void test_truncateTable_temporaryTable() throws Exception {
        Connection connection = super.jdbcFixture.getConnection();
        connection.setAutoCommit(false);
        try {
            super.test_truncateTable_temporaryTable();
        }
        finally {
            connection.commit();
            connection.setAutoCommit(true);
        }
    }
}
