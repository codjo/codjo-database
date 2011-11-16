package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractSelectQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseSelectQueryBuilder extends AbstractSelectQueryBuilder {

    @Override
    protected final String getTableName() {
        return SybaseUtil.getTableName(table);
    }
}
