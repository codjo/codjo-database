package net.codjo.database.api.query;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SelectResult extends ResultSetDelegate {
    private Statement statement;
    private int totalRowCount;
    private final Page page;
    private int currentRow = 0;
    public static final int UNDEFINED_TOTAL_ROW_COUNT = -1;


    public SelectResult(ResultSet resultSet, Statement statement, int totalRowCount, Page page) {
        this.statement = statement;
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

        return super.next();
    }


    @Override
    public void close() throws SQLException {
        try {
            super.close();
        }
        finally {
            statement.close();
        }
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
