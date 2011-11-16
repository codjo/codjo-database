package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlIndex;
public abstract class AbstractCreateIndexQueryBuilder extends AbstractQueryBuilder {
    protected SqlIndex index;


    public AbstractCreateIndexQueryBuilder index(SqlIndex sqlIndex) {
        this.index = sqlIndex;
        return this;
    }
}
