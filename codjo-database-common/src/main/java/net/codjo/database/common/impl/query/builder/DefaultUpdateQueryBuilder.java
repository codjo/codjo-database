package net.codjo.database.common.impl.query.builder;
public class DefaultUpdateQueryBuilder extends AbstractUpdateQueryBuilder {

    @Override
    protected final String getTableName() {
        return table.getName();
    }
}