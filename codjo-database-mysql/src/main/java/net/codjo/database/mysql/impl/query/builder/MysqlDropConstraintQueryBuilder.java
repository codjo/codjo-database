package net.codjo.database.mysql.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
public class MysqlDropConstraintQueryBuilder extends AbstractDropConstraintQueryBuilder {

    @Override
    public String get() {
        switch (type) {
            case FOREIGN_KEY:
                return new StringBuilder()
                      .append("alter table ").append(constraint.getAlteredTable().getName())
                      .append(" drop foreign key ").append(constraint.getName()).toString();
        }
        throw new UnsupportedOperationException();
    }
}
