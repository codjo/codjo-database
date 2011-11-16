package net.codjo.database.hsqldb.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
public class HsqldbCreateTableQueryBuilder extends AbstractCreateTableQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder().
              append("create ");
        if (table.isTemporary()) {
            create.append("temp ");
        }
        return create
              .append("table ").append(table.getName())
              .append(" ( ").append(content).append(" )").toString();
    }
}
