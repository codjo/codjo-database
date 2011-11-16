package net.codjo.database.mysql.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
public class MysqlCreateTableQueryBuilder extends AbstractCreateTableQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder().
              append("create ");
        if (table.isTemporary()) {
            create.append("temporary ");
        }
        return create
              .append("table ").append(table.getName())
              .append(" ( ").append(content).append(" )").toString();
    }
}
