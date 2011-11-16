package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractAlterTableQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseAlterTableQueryBuilder extends AbstractAlterTableQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder()
              .append("alter table ").append(SybaseUtil.getTableName(table));
        switch (type) {
            case ADD_COLUMN:
                create.append(" add ").append(SybaseUtil.getName(field)).append(" ")
                      .append(field.getDefinition());
                break;

            case ADD_TABLE_CONSTRAINT:
            case ALTER_COLUMN:
            case DROP_COLUMN:
            case DROP_TABLE_CONSTRAINT:
            default:
                break;
        }
        return create.toString();
    }
}
