package net.codjo.database.common.api.confidential.query;
import net.codjo.database.common.impl.query.runner.ResultSetDelegate;
import java.sql.ResultSet;
import java.sql.SQLException;
/**
 *
 */
public class SelectResult extends ResultSetDelegate {
    private int totalRowCount;
    private final Page page;
    private int currentRow = 0;
    public static final int UNDEFINED_TOTAL_ROW_COUNT = -1;


    public SelectResult(ResultSet resultSet, int totalRowCount, Page page) {
        this.totalRowCount = totalRowCount;
        this.page = page;
        setDelegate(resultSet);
    }


    public int getTotalRowCount() {
        if (totalRowCount == UNDEFINED_TOTAL_ROW_COUNT) {
            return currentRow;
        }
        return totalRowCount;
    }


    @Override
    public boolean next() throws SQLException {
        if (isBeforePage()) {
            moveToPage();
        }
        if (page != null && !page.containsRow(currentRow)) {
            return false;
        }
        currentRow++;

        boolean hasNext = super.next();
        if (!hasNext) {
            getDelegate().getStatement().close();
        }
        return hasNext;
    }


    private boolean isBeforePage() {
        return page != null && currentRow < page.getBeginIndex();
    }


    private void moveToPage() throws SQLException {
        while (getDelegate().next()) {
            currentRow++;
            if (page.containsRow(currentRow)) {
                break;
            }
        }
    }
}
