package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
public class OracleCreateTableQueryBuilder extends AbstractCreateTableQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder().
              append("create ");
        if (table.isTemporary()) {
            create.append("global temporary ");
        }
        create.append("table ").append(table.getName())
              .append(" ( ").append(content).append(" )");
        if (table.isTemporary()) {
            create.append(" on commit delete rows");
        }
        return create.toString();
    }
}
