package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.impl.query.builder.AbstractCreateIndexQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseCreateIndexQueryBuilder extends AbstractCreateIndexQueryBuilder {

    @Override
    public String get() {
        StringBuilder create = new StringBuilder("create ");
        switch (index.getType()) {
            case NORMAL:
                break;
            case UNIQUE:
            case PRIMARY_KEY:
                create.append("unique ");
                break;
            case CLUSTERED:
                create.append("clustered ");
                break;
            case UNIQUE_CLUSTERED:
            case PRIMARY_KEY_CLUSTERED:
                create.append("unique ");
                create.append("clustered ");
                break;
        }
        create.append("index ").append(SybaseUtil.getIndexName(index))
              .append(" on ").append(SybaseUtil.getTableName(index.getTable())).append(" (");
        for (SqlField field : index.getFields()) {
            create.append(field.getName()).append(", ");
        }
        create.delete(create.length() - 2, create.length());
        return create.append(")").toString();
    }
}
