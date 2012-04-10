package net.codjo.database.common.impl.query.builder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlTable;
public abstract class AbstractInsertQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;
    protected final List<SqlField> fields = new ArrayList<SqlField>();


    public AbstractInsertQueryBuilder intoTable(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }


    public AbstractInsertQueryBuilder fields(SqlField... sqlFields) {
        fields.addAll(Arrays.asList(sqlFields));
        return this;
    }


    @Override
    public final String get() {
        StringBuilder insert = new StringBuilder()
              .append("insert into ").append(getTableName()).append(" ");
        SqlField firstField = fields.get(0);
        if (formatFieldName(firstField) != null) {
            insert.append("( ");
            for (SqlField field : fields) {
                insert.append(formatFieldName(field)).append(", ");
            }
            insert.delete(insert.length() - 2, insert.length());
            insert.append(" ) ");
        }
        insert.append("values ( ");
        if (firstField.getValue() == null) {
            //noinspection UnusedDeclaration
            for (SqlField field : fields) {
                insert.append("?, ");
            }
        }
        else {
            for (SqlField field : fields) {
                insert.append(formatFieldValue(field)).append(", ");
            }
        }
        insert.delete(insert.length() - 2, insert.length());
        insert.append(" )");
        return insert.toString();
    }


    protected abstract String getTableName();
}
