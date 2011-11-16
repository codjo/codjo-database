package net.codjo.database.common.impl.query.builder;
public class DefaultInsertQueryBuilder extends AbstractInsertQueryBuilder {

    @Override
    protected final String getTableName() {
        return table.getName();
    }
}
