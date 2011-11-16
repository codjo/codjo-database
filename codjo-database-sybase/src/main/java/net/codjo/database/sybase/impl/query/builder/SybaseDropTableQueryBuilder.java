package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropTableQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseDropTableQueryBuilder extends AbstractDropTableQueryBuilder {

    @Override
    public String get() {
        return new StringBuilder()
              .append("drop table ").append(SybaseUtil.getTableName(table)).toString();
    }
}
