package net.codjo.database.common.impl.comparator;
import net.codjo.database.common.api.TableComparator;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import java.sql.Connection;
public class DefaultTableComparatorBuilder implements TableComparatorBuilder {

    public TableComparator get(Connection connection, int columnToSkip, String orderClause) {
        return new DefaultTableComparator(connection, columnToSkip, orderClause);
    }
}
