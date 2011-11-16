package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
import static net.codjo.database.sybase.util.SybaseUtil.getName;
public class SybaseDropConstraintQueryBuilder extends AbstractDropConstraintQueryBuilder {

    @Override
    public String get() {
        switch (type) {
            case FOREIGN_KEY:
                return new StringBuilder()
                      .append("alter table ").append(getName(constraint.getAlteredTable()))
                      .append(" drop constraint ").append(getName(constraint)).toString();
        }
        throw new UnsupportedOperationException();
    }
}
