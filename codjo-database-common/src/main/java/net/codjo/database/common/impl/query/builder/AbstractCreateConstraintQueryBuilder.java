package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlConstraint;
public abstract class AbstractCreateConstraintQueryBuilder extends AbstractQueryBuilder {
    protected SqlConstraint constraint;


    public AbstractCreateConstraintQueryBuilder constraint(SqlConstraint sqlConstraint) {
        this.constraint = sqlConstraint;
        return this;
    }
}
