package net.codjo.database.sybase.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilderTest;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class SybaseSQLFieldListBuilderTest extends SQLFieldListBuilderTest {

    @Override
    protected SQLFieldListBuilder createSQLFieldListBuilder() {
        return new SybaseSQLFieldListBuilder();
    }


    @Test
    public void test_constructorForOtherCatalog() throws Exception {
        try {
            jdbcFixture.create(SqlTable.table("tempdb..A_TABLE"), "COL_B varchar(5) null, COL_A int null");

            SQLFieldList list = sqlFieldListBuilder
                  .get(jdbcFixture.getConnection(), "tempdb", "A_TABLE", false);

            assertEquals(list.getFieldValue("COL_A"), null);
            assertEquals(list.getFieldType("COL_A"), java.sql.Types.INTEGER);
            assertEquals(list.getFieldValue("COL_B"), null);
            assertEquals(list.getFieldType("COL_B"), java.sql.Types.VARCHAR);
        }
        finally {
            jdbcFixture.drop(SqlTable.table("tempdb..A_TABLE"));
        }
    }
}
