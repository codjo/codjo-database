package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractInsertQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseInsertQueryBuilder extends AbstractInsertQueryBuilder {

    @Override
    protected final String getTableName() {
        return SybaseUtil.getTableName(table);
    }
}
