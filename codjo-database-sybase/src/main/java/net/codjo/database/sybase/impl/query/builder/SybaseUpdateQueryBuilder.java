package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractUpdateQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseUpdateQueryBuilder extends AbstractUpdateQueryBuilder {

    @Override
    protected final String getTableName() {
        return SybaseUtil.getTableName(table);
    }
}
