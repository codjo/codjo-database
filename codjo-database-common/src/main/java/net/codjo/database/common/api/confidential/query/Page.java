package net.codjo.database.common.api.confidential.query;
/**
 *
 */
public class Page {
    private int beginIndex;
    private int endIndex;


    public Page(int pageNumber, int pageSize) {
        beginIndex = (pageNumber - 1) * pageSize;
        endIndex = (pageNumber * pageSize) - 1;
    }


    public boolean containsRow(final int rowIndex) {
        return rowIndex >= beginIndex && rowIndex <= endIndex;
    }


    public int getBeginIndex() {
        return beginIndex;
    }


    public int getEndIndex() {
        return endIndex;
    }
}
