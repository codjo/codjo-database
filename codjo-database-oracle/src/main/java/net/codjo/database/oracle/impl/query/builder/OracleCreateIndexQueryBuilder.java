package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.impl.query.builder.AbstractCreateIndexQueryBuilder;
/**
 *
 */
public class OracleCreateIndexQueryBuilder extends AbstractCreateIndexQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder("create ");
        switch (index.getType()) {
            case UNIQUE:
            case PRIMARY_KEY:
            case UNIQUE_CLUSTERED:
            case PRIMARY_KEY_CLUSTERED:
                return alterTableAddUniqueConstraint(index);
            case NORMAL:
            case CLUSTERED:
                break;
        }
        create.append("index ").append(index.getName())
              .append(" on ").append(index.getTable().getName()).append(" (");
        for (SqlField field : index.getFields()) {
            create.append(field.getName()).append(", ");
        }
        create.delete(create.length() - 2, create.length());
        return create.append(")").toString();
    }


    private String alterTableAddUniqueConstraint(SqlIndex index) {
        StringBuilder sqlString;
        sqlString = new StringBuilder("alter table ").append(this.index.getTable().getName())
              .append(" add (constraint ").append(this.index.getName()).append(" unique (");
        for (SqlField field : index.getFields()) {
            sqlString.append(field.getName()).append(", ");
        }
        sqlString.delete(sqlString.length() - 2, sqlString.length());
        return sqlString.append("))").toString();
    }
}

