package net.codjo.database.common.api;
import net.codjo.database.common.api.structure.SqlTable;
import java.io.PrintStream;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
public class JdbcFixtureUtil {
    private JdbcFixture jdbcFixture;


    protected JdbcFixtureUtil(JdbcFixture jdbcFixture) {
        this.jdbcFixture = jdbcFixture;
    }


    public void spoolQueryResult(String query, PrintStream os) {
        try {
            Statement stmt = jdbcFixture.getConnection().createStatement();
            os.println("******************** SPOOL ********************");
            os.println("**  query = " + query);
            try {
                ResultSet rs = stmt.executeQuery(query);
                ResultSetMetaData rsmd = rs.getMetaData();

                // Spool Header
                int colmumnCount = rsmd.getColumnCount();
                for (int i = 1; i <= colmumnCount; i++) {
                    os.print("\t" + rsmd.getColumnName(i));
                }
                os.println();
                for (int i = 1; i <= colmumnCount; i++) {
                    os.print("\t" + rsmd.getColumnTypeName(i));
                }
                os.println();
                // Spool Content
                while (rs.next()) {
                    for (int i = 1; i <= colmumnCount; i++) {
                        os.print("\t" + rs.getObject(i));
                    }
                    os.println();
                }
            }
            finally {
                stmt.close();
            }
        }
        catch (SQLException e) {
            throw new RuntimeSqlException(e);
        }
    }


    public void spool(SqlTable table) {
        spool(table, System.out);
    }


    public void spool(SqlTable table, PrintStream os) {
        spoolQueryResult("select * from " + table.getName(), os);
    }


    void doSetUp() {
    }


    void doTearDown() {
    }
}
