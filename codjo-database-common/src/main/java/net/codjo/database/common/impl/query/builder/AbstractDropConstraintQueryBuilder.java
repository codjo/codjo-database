package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlConstraint;
public abstract class AbstractDropConstraintQueryBuilder extends AbstractQueryBuilder {
    protected SqlConstraint constraint;
    protected Type type;

    protected enum Type {
        FOREIGN_KEY;
    }


    public AbstractDropConstraintQueryBuilder foreignKey(SqlConstraint sqlConstraint) {
        this.constraint = sqlConstraint;
        this.type = Type.FOREIGN_KEY;
        return this;
    }
}
