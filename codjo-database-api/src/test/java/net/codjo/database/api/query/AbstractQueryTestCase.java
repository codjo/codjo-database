package net.codjo.database.api.query;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.structure.SqlTable;
import java.sql.SQLException;
import org.junit.After;
import org.junit.Before;

public abstract class AbstractQueryTestCase {
    protected JdbcFixture jdbc;


    @Before
    public void setUp() throws Exception {
        if (jdbc == null) {
            jdbc = createFixture();
        }
        jdbc.doSetUp();
        jdbc.create(SqlTable.table("AP_TEST"), "MY_ID int");
    }


    @After
    public void tearDown() throws Exception {
        jdbc.doTearDown();
    }


    protected abstract JdbcFixture createFixture() throws SQLException, ClassNotFoundException;


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


    protected static ConnectionMetadata createMeta(String className,
                                                   String hostname,
                                                   String port,
                                                   String user,
                                                   String password,
                                                   String catalog)
          throws ClassNotFoundException {
        ConnectionMetadata connectionMetadata = new ConnectionMetadata();
        connectionMetadata.setHostname(hostname);
        connectionMetadata.setPort(port);
        connectionMetadata.setUser(user);
        connectionMetadata.setPassword(password);
        connectionMetadata.setCatalog(catalog);
        Class.forName(className);
        return connectionMetadata;
    }
}
