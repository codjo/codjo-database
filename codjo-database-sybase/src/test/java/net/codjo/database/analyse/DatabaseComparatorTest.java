/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.common.api.JdbcFixture;
import static net.codjo.database.common.api.structure.SqlTable.table;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.sql.SQLException;
import junit.framework.TestCase;
/**
 */
public class DatabaseComparatorTest extends TestCase {
    private DatabaseComparator databaseComparator;
    private ByteArrayOutputStream outStream;
    private MockBaseManager mockBaseManager = new MockBaseManager();
    private JdbcFixture jdbcFixture = JdbcFixture.newFixture();


    @Override
    public void setUp() throws Exception {
        jdbcFixture.doSetUp();
        databaseComparator = new DatabaseComparator();
        mockBaseManager.load();
        outStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outStream));
    }


    @Override
    protected void tearDown() throws Exception {
        jdbcFixture.doTearDown();
    }


    public void test_sameDatabase() throws SQLException, ClassNotFoundException {
        makeDbOne();
        loadRef();
        loadSrc();
        assertFalse("Dbs are not same ??", databaseComparator.areDatabasesDifferent());
    }


    public void test_diffDatabase() throws SQLException, ClassNotFoundException {
        makeDbOne();
        loadRef();
        makeDbTwo();
        loadSrc();
        assertTrue("Dbs are same ??", databaseComparator.areDatabasesDifferent());
    }


    public void test_diffDatabaseByRule() throws SQLException, ClassNotFoundException {
        makeDbOne();
        loadRef();
        makeDbOneWithOtherRule();
        loadSrc();
        assertTrue("Dbs are same ??", databaseComparator.areDatabasesDifferent());
    }


    public void test_columnOrder() throws Exception {
        makeDbThree();
        loadRef();
        makeDbFour();
        loadSrc();
        assertTrue("Dbs are not same ??", databaseComparator.areDatabasesDifferent());
    }


    public void test_twoModifiedTables() throws Exception {
        makeDbThree();
        makeDbOne();
        loadRef();
        makeDbFour();
        makeDbOneWithNewColumn();
        loadSrc();
        assertTrue("Dbs are not same ??", databaseComparator.areDatabasesDifferent());
    }


    public void test_diffDatabaseByConstraints() throws SQLException, ClassNotFoundException {
        makeDbOne();
        loadRef();
        makeDbOneWithOtherConstraints();
        loadSrc();
        assertTrue("Dbs are same ??", databaseComparator.areDatabasesDifferent());
        assertDatabaseObject("CKC_MY_CONSTRAINT");
    }


    public void test_diffDatabaseByConstraintsExtended() throws SQLException, ClassNotFoundException {
        makeDbThree();
        loadRef();
        makeDbThreeWith2OtherConstraints();
        loadSrc();
        assertTrue("Dbs are same ??", databaseComparator.areDatabasesDifferent());
        assertDatabaseObject("CKC_ADR_MY_CONSTRAINT_1");
        assertDatabaseObject("CKC_ADR_MY_CONSTRAINT_2");
    }


    private void loadSrc() throws SQLException, ClassNotFoundException {
        databaseComparator.loadActualStructure(mockBaseManager.getUrl(""),
                                               mockBaseManager.getLoggin(""),
                                               mockBaseManager.getPassWord(""),
                                               mockBaseManager.getBase(""),
                                               mockBaseManager.getCatalog(""));
    }


    private void loadRef() throws SQLException, ClassNotFoundException {
        databaseComparator.loadExpectedStructure(mockBaseManager.getUrl(""),
                                                 mockBaseManager.getLoggin(""),
                                                 mockBaseManager.getPassWord(""),
                                                 mockBaseManager.getBase(""),
                                                 mockBaseManager.getCatalog(""));
    }


    private void makeDbOneWithOtherRule() {
        jdbcFixture.drop(table("TABLEA"));
        jdbcFixture.create(table("TABLEA"),
                           "CHAMP1 int not null,CHAMP2 varchar(6) not null  constraint CKC_GAP_TYPE check (CHAMP2 in ('J','M','S','B'))");
    }


    private void makeDbOneWithOtherConstraints() throws SQLException {
        jdbcFixture.drop(table("TABLEA"));
        makeDbOne();
        jdbcFixture.executeUpdate(
              "alter table TABLEA add constraint CKC_MY_CONSTRAINT check (CHAMP1 in ('valueA', 'valueB'))");
    }


    private void makeDbThreeWith2OtherConstraints() throws SQLException {
        jdbcFixture.drop(table("ADRESSE"));
        makeDbThree();
        jdbcFixture.executeUpdate(
              "alter table ADRESSE add constraint CKC_ADR_MY_CONSTRAINT_1 check (RUE in ('valueA', 'valueB'))");
        jdbcFixture.executeUpdate(
              "alter table ADRESSE add constraint CKC_ADR_MY_CONSTRAINT_2 check (VILLE in ('valueC', 'valueD'))");
    }


    private void makeDbOneWithNewColumn() {
        jdbcFixture.drop(table("TABLEA"));
        jdbcFixture.create(table("TABLEA"),
                           "CHAMP1 int not null,CHAMP2 varchar(6) not null, CHAMP3 varchar(20) null");
    }


    private void makeDbOne() {
        jdbcFixture.create(table("TABLEA"), "CHAMP1 int not null, CHAMP2 varchar(6) not null");
    }


    private void makeDbTwo() {
        jdbcFixture.drop(table("TABLEA"));
        jdbcFixture.create(table("TABLEA"), "CHAMP2 int not null, CHAMP3 varchar(6) null");
    }


    private void makeDbThree() {
        jdbcFixture.create(table("ADRESSE"),
                           "ID int not null, RUE varchar(100) null, VILLE varchar(50) null");
    }


    private void makeDbFour() {
        jdbcFixture.drop(table("ADRESSE"));
        jdbcFixture.create(table("ADRESSE"),
                           "ID int not null, VILLE varchar(50) null, RUE varchar(100) null");
    }


    private void assertDatabaseObject(String objectName) {
        assertTrue("La chaîne '" + objectName + "' ne figure pas dans le log",
                   outStream.toString().contains(objectName));
    }
}
