package net.codjo.database.oracle.impl.fixture;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.JdbcFixture;
import net.codjo.database.common.api.JdbcFixtureAdvanced;
import net.codjo.database.common.api.JdbcFixtureUtil;
import net.codjo.database.common.api.structure.SqlTable;
public class OracleJdbcFixture extends JdbcFixture {

    protected OracleJdbcFixture(ConnectionMetadata connectionMetadata) {
        super(connectionMetadata);
    }


    @Override
    protected JdbcFixtureAdvanced newJdbcFixtureAdvanced() {
        return new OracleJdbcFixtureAdvanced(this);
    }


    @Override
    protected JdbcFixtureUtil newJdbcFixtureUtil() {
        return new OracleJdbcFixtureUtil(this);
    }


    @Override
    public void drop(SqlTable table) {
        try {
            if (table.isTemporary()) {
                executeUpdate("truncate  table " + table.getName());
            }
            executeUpdate("drop table " + table.getName() + " purge");
        }
        catch (Exception e) {
            // Rien à faire.
        }
    }
}
