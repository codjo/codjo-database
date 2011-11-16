package net.codjo.database.common.impl.query.builder;
import static net.codjo.database.common.api.structure.SqlConstraint.foreignKey;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public abstract class AbstractDropConstraintQueryBuilderTest extends AbstractQueryBuilderTest<AbstractDropConstraintQueryBuilder> {

    @Test
    public void test_foreignKey() throws Exception {
        String query = getQueryBuilder().foreignKey(foreignKey("FK_TOTO", table("AP_TOTO"))).get();

        assertEquals(getForeignKeyQuery(), query);
    }


    protected abstract String getForeignKeyQuery();
}
