package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public abstract class AbstractUpdateQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;
    protected final List<SqlField> fields = new ArrayList<SqlField>();


    public AbstractUpdateQueryBuilder table(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }


    public AbstractQueryBuilder setFields(SqlField... sqlFields) {
        fields.addAll(Arrays.asList(sqlFields));
        return this;
    }


    @Override
    public final String get() {
        StringBuilder update = new StringBuilder()
              .append("update ").append(getTableName()).append(" set ");
        for (SqlField field : fields) {
            update.append(field.getName())
                  .append("=")
                  .append(formatFieldValue(field)).append(", ");
        }
        update.delete(update.length() - 2, update.length());
        return update.toString();
    }


    protected abstract String getTableName();
}
