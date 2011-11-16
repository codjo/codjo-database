package net.codjo.database.common.repository.builder;
import net.codjo.database.common.api.TableComparator;
import java.sql.Connection;
public interface TableComparatorBuilder {

    TableComparator get(Connection connection, int columnToSkip, String orderClause);
}
