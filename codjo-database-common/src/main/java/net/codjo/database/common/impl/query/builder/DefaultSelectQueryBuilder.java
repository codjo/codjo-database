package net.codjo.database.common.impl.query.builder;
public class DefaultSelectQueryBuilder extends AbstractSelectQueryBuilder {

    @Override
    protected final String getTableName() {
        return table.getName();
    }
}
