package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlTable;
public abstract class AbstractDropTableQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;


    public AbstractDropTableQueryBuilder table(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }
}
