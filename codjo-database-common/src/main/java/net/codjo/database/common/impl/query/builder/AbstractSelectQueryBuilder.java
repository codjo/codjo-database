package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlTable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
public abstract class AbstractSelectQueryBuilder extends AbstractQueryBuilder {
    protected SqlTable table;
    protected boolean allFields;
    protected final List<SqlField> fields = new ArrayList<SqlField>();


    public AbstractSelectQueryBuilder fields(SqlField... sqlFields) {
        this.fields.addAll(Arrays.asList(sqlFields));
        return this;
    }


    public AbstractSelectQueryBuilder oneField() {
        this.allFields = false;
        return this;
    }


    public AbstractSelectQueryBuilder allFields() {
        this.allFields = true;
        return this;
    }


    public AbstractSelectQueryBuilder fromTable(SqlTable sqlTable) {
        this.table = sqlTable;
        return this;
    }


    @Override
    public final String get() {
        StringBuilder select = new StringBuilder("select ");
        if (allFields) {
            select.append("*");
        }
        else if (fields.isEmpty()) {
            select.append("1");
        }
        else {
            for (SqlField field : fields) {
                select.append(field.getName()).append(", ");
            }
            select.delete(select.length() - 2, select.length());
        }
        return select.append(" from ").append(getTableName()).toString();
    }


    protected abstract String getTableName();
}
