package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
public class DefaultCreateIndexQueryBuilder extends AbstractCreateIndexQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder("create ");
        switch (index.getType()) {
            case UNIQUE:
            case PRIMARY_KEY:
            case UNIQUE_CLUSTERED:
            case PRIMARY_KEY_CLUSTERED:
                create.append("unique ");
                break;

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
}
