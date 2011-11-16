package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlTable;
public abstract class AbstractAlterTableQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;
    protected SqlField field;
    protected Type type;

    protected enum Type {
        ADD_COLUMN,
        ALTER_COLUMN,
        DROP_COLUMN,
        ADD_TABLE_CONSTRAINT,
        DROP_TABLE_CONSTRAINT;
    }


    public AbstractAlterTableQueryBuilder table(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }


    public AbstractAlterTableQueryBuilder addColumn(SqlField sqlField) {
        this.field = sqlField;
        this.type = Type.ADD_COLUMN;
        return this;
    }
}
