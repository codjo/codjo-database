package net.codjo.database.common.impl.query.builder;
public class DefaultDropConstraintQueryBuilder extends AbstractDropConstraintQueryBuilder {

    @Override
    public String get() {
        switch (type) {
            case FOREIGN_KEY:
                return new StringBuilder()
                      .append("alter table ").append(constraint.getAlteredTable().getName())
                      .append(" drop constraint ").append(constraint.getName())
                      .toString();
        }
        throw new UnsupportedOperationException();
    }
}
