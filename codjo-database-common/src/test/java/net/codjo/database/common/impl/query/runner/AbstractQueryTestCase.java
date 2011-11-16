package net.codjo.database.common.impl.query.runner;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.confidential.query.SelectResult;
import net.codjo.database.common.api.structure.SqlTable;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;
/**
 *
 */
public abstract class AbstractQueryTestCase {
    protected JdbcFixture jdbc = JdbcFixture.newFixture();


    @Before
    public void setUp() throws Exception {
        jdbc.doSetUp();
        jdbc.create(SqlTable.table("AP_TEST"), "MY_ID int");
    }


    @After
    public void tearDown() throws Exception {
        jdbc.doTearDown();
    }


    protected void insertInto(String tableName, int... values) throws SQLException {
        for (int value : values) {
            jdbc.executeUpdate("insert into " + tableName + " values(" + value + ")");
        }
    }


    protected String toString(SelectResult result) throws SQLException {
        StringBuilder builder = new StringBuilder();
        while (result.next()) {
            if (builder.length() != 0) {
                builder.append(" | ");
            }
            builder.append(result.getObject(1));
        }
        return builder.toString();
    }
}
