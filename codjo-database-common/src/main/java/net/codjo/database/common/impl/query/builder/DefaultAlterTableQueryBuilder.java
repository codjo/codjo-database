package net.codjo.database.common.impl.query.builder;
public class DefaultAlterTableQueryBuilder extends AbstractAlterTableQueryBuilder {

    @Override
    public String get() {
        StringBuilder alter = new StringBuilder()
              .append("alter table ").append(table.getName());
        switch (type) {
            case ADD_COLUMN:
                alter.append(" add column ").append(field.getName()).append(" ")
                      .append(field.getDefinition());
                break;

            case ADD_TABLE_CONSTRAINT:
            case ALTER_COLUMN:
            case DROP_COLUMN:
            case DROP_TABLE_CONSTRAINT:
            default:
                break;
        }
        return alter.toString();
    }
}
