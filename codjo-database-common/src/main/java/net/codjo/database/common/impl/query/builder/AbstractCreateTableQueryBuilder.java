package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlTable;
public abstract class AbstractCreateTableQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;
    protected String content;


    public AbstractCreateTableQueryBuilder table(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }


    public AbstractCreateTableQueryBuilder withContent(String tableContent) {
        this.content = tableContent;
        return this;
    }
}
