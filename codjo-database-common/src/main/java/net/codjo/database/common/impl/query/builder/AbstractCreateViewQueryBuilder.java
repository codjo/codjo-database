package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlView;
public abstract class AbstractCreateViewQueryBuilder extends AbstractQueryBuilder {
    protected SqlView view;


    public AbstractCreateViewQueryBuilder view(SqlView sqlView) {
        this.view = sqlView;
        return this;
    }
}
