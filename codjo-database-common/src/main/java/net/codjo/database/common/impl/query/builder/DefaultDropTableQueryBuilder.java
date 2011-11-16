package net.codjo.database.common.impl.query.builder;
public class DefaultDropTableQueryBuilder extends AbstractDropTableQueryBuilder {

    @Override
    public String get() {
        return new StringBuilder()
              .append("drop table ").append(table.getName()).toString();
    }
}
