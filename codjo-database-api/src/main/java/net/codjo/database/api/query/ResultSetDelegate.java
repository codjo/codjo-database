package net.codjo.database.api.query;
import java.io.InputStream;
import java.io.Reader;
import java.math.BigDecimal;
import java.net.URL;
import java.sql.Array;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.Date;
import java.sql.Ref;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.sql.Time;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Map;

@SuppressWarnings({"OverlyComplexClass"})
public class ResultSetDelegate implements ResultSet {
    private ResultSet delegate;


    protected void setDelegate(ResultSet delegate) {
        this.delegate = delegate;
    }


    protected ResultSet getDelegate() {
        return delegate;
    }


    public boolean next() throws SQLException {
        return delegate.next();
    }


    public void close() throws SQLException {
        delegate.close();
    }


    public boolean wasNull() throws SQLException {
        return delegate.wasNull();
    }


    public String getString(int columnIndex) throws SQLException {
        return delegate.getString(columnIndex);
    }


    public boolean getBoolean(int columnIndex) throws SQLException {
        return delegate.getBoolean(columnIndex);
    }


    public byte getByte(int columnIndex) throws SQLException {
        return delegate.getByte(columnIndex);
    }


    public short getShort(int columnIndex) throws SQLException {
        return delegate.getShort(columnIndex);
    }


    public int getInt(int columnIndex) throws SQLException {
        return delegate.getInt(columnIndex);
    }


    public long getLong(int columnIndex) throws SQLException {
        return delegate.getLong(columnIndex);
    }


    public float getFloat(int columnIndex) throws SQLException {
        return delegate.getFloat(columnIndex);
    }


    public double getDouble(int columnIndex) throws SQLException {
        return delegate.getDouble(columnIndex);
    }


    @Deprecated
    public BigDecimal getBigDecimal(int columnIndex, int scale) throws SQLException {
        //noinspection deprecation
        return delegate.getBigDecimal(columnIndex, scale);
    }


    public byte[] getBytes(int columnIndex) throws SQLException {
        return delegate.getBytes(columnIndex);
    }


    public Date getDate(int columnIndex) throws SQLException {
        return delegate.getDate(columnIndex);
    }


    public Time getTime(int columnIndex) throws SQLException {
        return delegate.getTime(columnIndex);
    }


    public Timestamp getTimestamp(int columnIndex) throws SQLException {
        return delegate.getTimestamp(columnIndex);
    }


    public InputStream getAsciiStream(int columnIndex) throws SQLException {
        return delegate.getAsciiStream(columnIndex);
    }


    @Deprecated
    public InputStream getUnicodeStream(int columnIndex) throws SQLException {
        //noinspection deprecation
        return delegate.getUnicodeStream(columnIndex);
    }


    public InputStream getBinaryStream(int columnIndex) throws SQLException {
        return delegate.getBinaryStream(columnIndex);
    }


    public String getString(String columnName) throws SQLException {
        return delegate.getString(columnName);
    }


    public boolean getBoolean(String columnName) throws SQLException {
        return delegate.getBoolean(columnName);
    }


    public byte getByte(String columnName) throws SQLException {
        return delegate.getByte(columnName);
    }


    public short getShort(String columnName) throws SQLException {
        return delegate.getShort(columnName);
    }


    public int getInt(String columnName) throws SQLException {
        return delegate.getInt(columnName);
    }


    public long getLong(String columnName) throws SQLException {
        return delegate.getLong(columnName);
    }


    public float getFloat(String columnName) throws SQLException {
        return delegate.getFloat(columnName);
    }


    public double getDouble(String columnName) throws SQLException {
        return delegate.getDouble(columnName);
    }


    @Deprecated
    public BigDecimal getBigDecimal(String columnName, int scale) throws SQLException {
        //noinspection deprecation
        return delegate.getBigDecimal(columnName, scale);
    }


    public byte[] getBytes(String columnName) throws SQLException {
        return delegate.getBytes(columnName);
    }


    public Date getDate(String columnName) throws SQLException {
        return delegate.getDate(columnName);
    }


    public Time getTime(String columnName) throws SQLException {
        return delegate.getTime(columnName);
    }


    public Timestamp getTimestamp(String columnName) throws SQLException {
        return delegate.getTimestamp(columnName);
    }


    public InputStream getAsciiStream(String columnName) throws SQLException {
        return delegate.getAsciiStream(columnName);
    }


    @Deprecated
    public InputStream getUnicodeStream(String columnName) throws SQLException {
        //noinspection deprecation
        return delegate.getUnicodeStream(columnName);
    }


    public InputStream getBinaryStream(String columnName) throws SQLException {
        return delegate.getBinaryStream(columnName);
    }


    public SQLWarning getWarnings() throws SQLException {
        return delegate.getWarnings();
    }


    public void clearWarnings() throws SQLException {
        delegate.clearWarnings();
    }


    public String getCursorName() throws SQLException {
        return delegate.getCursorName();
    }


    public ResultSetMetaData getMetaData() throws SQLException {
        return delegate.getMetaData();
    }


    public Object getObject(int columnIndex) throws SQLException {
        return delegate.getObject(columnIndex);
    }


    public Object getObject(String columnName) throws SQLException {
        return delegate.getObject(columnName);
    }


    public int findColumn(String columnName) throws SQLException {
        return delegate.findColumn(columnName);
    }


    public Reader getCharacterStream(int columnIndex) throws SQLException {
        return delegate.getCharacterStream(columnIndex);
    }


    public Reader getCharacterStream(String columnName) throws SQLException {
        return delegate.getCharacterStream(columnName);
    }


    public BigDecimal getBigDecimal(int columnIndex) throws SQLException {
        return delegate.getBigDecimal(columnIndex);
    }


    public BigDecimal getBigDecimal(String columnName) throws SQLException {
        return delegate.getBigDecimal(columnName);
    }


    public boolean isBeforeFirst() throws SQLException {
        return delegate.isBeforeFirst();
    }


    public boolean isAfterLast() throws SQLException {
        return delegate.isAfterLast();
    }


    public boolean isFirst() throws SQLException {
        return delegate.isFirst();
    }


    public boolean isLast() throws SQLException {
        return delegate.isLast();
    }


    public void beforeFirst() throws SQLException {
        delegate.beforeFirst();
    }


    public void afterLast() throws SQLException {
        delegate.afterLast();
    }


    public boolean first() throws SQLException {
        return delegate.first();
    }


    public boolean last() throws SQLException {
        return delegate.last();
    }


    public int getRow() throws SQLException {
        return delegate.getRow();
    }


    public boolean absolute(int row) throws SQLException {
        return delegate.absolute(row);
    }


    public boolean relative(int rows) throws SQLException {
        return delegate.relative(rows);
    }


    public boolean previous() throws SQLException {
        return delegate.previous();
    }


    public void setFetchDirection(int direction) throws SQLException {
        delegate.setFetchDirection(direction);
    }


    public int getFetchDirection() throws SQLException {
        return delegate.getFetchDirection();
    }


    public void setFetchSize(int rows) throws SQLException {
        delegate.setFetchSize(rows);
    }


    public int getFetchSize() throws SQLException {
        return delegate.getFetchSize();
    }


    public int getType() throws SQLException {
        return delegate.getType();
    }


    public int getConcurrency() throws SQLException {
        return delegate.getConcurrency();
    }


    public boolean rowUpdated() throws SQLException {
        return delegate.rowUpdated();
    }


    public boolean rowInserted() throws SQLException {
        return delegate.rowInserted();
    }


    public boolean rowDeleted() throws SQLException {
        return delegate.rowDeleted();
    }


    public void updateNull(int columnIndex) throws SQLException {
        delegate.updateNull(columnIndex);
    }


    public void updateBoolean(int columnIndex, boolean value) throws SQLException {
        delegate.updateBoolean(columnIndex, value);
    }


    public void updateByte(int columnIndex, byte value) throws SQLException {
        delegate.updateByte(columnIndex, value);
    }


    public void updateShort(int columnIndex, short value) throws SQLException {
        delegate.updateShort(columnIndex, value);
    }


    public void updateInt(int columnIndex, int value) throws SQLException {
        delegate.updateInt(columnIndex, value);
    }


    public void updateLong(int columnIndex, long value) throws SQLException {
        delegate.updateLong(columnIndex, value);
    }


    public void updateFloat(int columnIndex, float value) throws SQLException {
        delegate.updateFloat(columnIndex, value);
    }


    public void updateDouble(int columnIndex, double value) throws SQLException {
        delegate.updateDouble(columnIndex, value);
    }


    public void updateBigDecimal(int columnIndex, BigDecimal value) throws SQLException {
        delegate.updateBigDecimal(columnIndex, value);
    }


    public void updateString(int columnIndex, String value) throws SQLException {
        delegate.updateString(columnIndex, value);
    }


    public void updateBytes(int columnIndex, byte[] value) throws SQLException {
        delegate.updateBytes(columnIndex, value);
    }


    public void updateDate(int columnIndex, Date value) throws SQLException {
        delegate.updateDate(columnIndex, value);
    }


    public void updateTime(int columnIndex, Time value) throws SQLException {
        delegate.updateTime(columnIndex, value);
    }


    public void updateTimestamp(int columnIndex, Timestamp value) throws SQLException {
        delegate.updateTimestamp(columnIndex, value);
    }


    public void updateAsciiStream(int columnIndex, InputStream value, int length) throws SQLException {
        delegate.updateAsciiStream(columnIndex, value, length);
    }


    public void updateBinaryStream(int columnIndex, InputStream value, int length) throws SQLException {
        delegate.updateBinaryStream(columnIndex, value, length);
    }


    public void updateCharacterStream(int columnIndex, Reader value, int length) throws SQLException {
        delegate.updateCharacterStream(columnIndex, value, length);
    }


    public void updateObject(int columnIndex, Object value, int scale) throws SQLException {
        delegate.updateObject(columnIndex, value, scale);
    }


    public void updateObject(int columnIndex, Object value) throws SQLException {
        delegate.updateObject(columnIndex, value);
    }


    public void updateNull(String columnName) throws SQLException {
        delegate.updateNull(columnName);
    }


    public void updateBoolean(String columnName, boolean value) throws SQLException {
        delegate.updateBoolean(columnName, value);
    }


    public void updateByte(String columnName, byte value) throws SQLException {
        delegate.updateByte(columnName, value);
    }


    public void updateShort(String columnName, short value) throws SQLException {
        delegate.updateShort(columnName, value);
    }


    public void updateInt(String columnName, int value) throws SQLException {
        delegate.updateInt(columnName, value);
    }


    public void updateLong(String columnName, long value) throws SQLException {
        delegate.updateLong(columnName, value);
    }


    public void updateFloat(String columnName, float value) throws SQLException {
        delegate.updateFloat(columnName, value);
    }


    public void updateDouble(String columnName, double value) throws SQLException {
        delegate.updateDouble(columnName, value);
    }


    public void updateBigDecimal(String columnName, BigDecimal value) throws SQLException {
        delegate.updateBigDecimal(columnName, value);
    }


    public void updateString(String columnName, String value) throws SQLException {
        delegate.updateString(columnName, value);
    }


    public void updateBytes(String columnName, byte[] value) throws SQLException {
        delegate.updateBytes(columnName, value);
    }


    public void updateDate(String columnName, Date value) throws SQLException {
        delegate.updateDate(columnName, value);
    }


    public void updateTime(String columnName, Time value) throws SQLException {
        delegate.updateTime(columnName, value);
    }


    public void updateTimestamp(String columnName, Timestamp value) throws SQLException {
        delegate.updateTimestamp(columnName, value);
    }


    public void updateAsciiStream(String columnName, InputStream value, int length) throws SQLException {
        delegate.updateAsciiStream(columnName, value, length);
    }


    public void updateBinaryStream(String columnName, InputStream value, int length) throws SQLException {
        delegate.updateBinaryStream(columnName, value, length);
    }


    public void updateCharacterStream(String columnName, Reader reader, int length) throws SQLException {
        delegate.updateCharacterStream(columnName, reader, length);
    }


    public void updateObject(String columnName, Object value, int scale) throws SQLException {
        delegate.updateObject(columnName, value, scale);
    }


    public void updateObject(String columnName, Object value) throws SQLException {
        delegate.updateObject(columnName, value);
    }


    public void insertRow() throws SQLException {
        delegate.insertRow();
    }


    public void updateRow() throws SQLException {
        delegate.updateRow();
    }


    public void deleteRow() throws SQLException {
        delegate.deleteRow();
    }


    public void refreshRow() throws SQLException {
        delegate.refreshRow();
    }


    public void cancelRowUpdates() throws SQLException {
        delegate.cancelRowUpdates();
    }


    public void moveToInsertRow() throws SQLException {
        delegate.moveToInsertRow();
    }


    public void moveToCurrentRow() throws SQLException {
        delegate.moveToCurrentRow();
    }


    public Statement getStatement() throws SQLException {
        return delegate.getStatement();
    }


    public Object getObject(int index, Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(index, map);
    }


    public Ref getRef(int index) throws SQLException {
        return delegate.getRef(index);
    }


    public Blob getBlob(int index) throws SQLException {
        return delegate.getBlob(index);
    }


    public Clob getClob(int index) throws SQLException {
        return delegate.getClob(index);
    }


    public Array getArray(int index) throws SQLException {
        return delegate.getArray(index);
    }


    public Object getObject(String colName, Map<String, Class<?>> map) throws SQLException {
        return delegate.getObject(colName, map);
    }


    public Ref getRef(String colName) throws SQLException {
        return delegate.getRef(colName);
    }


    public Blob getBlob(String colName) throws SQLException {
        return delegate.getBlob(colName);
    }


    public Clob getClob(String colName) throws SQLException {
        return delegate.getClob(colName);
    }


    public Array getArray(String colName) throws SQLException {
        return delegate.getArray(colName);
    }


    public Date getDate(int columnIndex, Calendar cal) throws SQLException {
        return delegate.getDate(columnIndex, cal);
    }


    public Date getDate(String columnName, Calendar cal) throws SQLException {
        return delegate.getDate(columnName, cal);
    }


    public Time getTime(int columnIndex, Calendar cal) throws SQLException {
        return delegate.getTime(columnIndex, cal);
    }


    public Time getTime(String columnName, Calendar cal) throws SQLException {
        return delegate.getTime(columnName, cal);
    }


    public Timestamp getTimestamp(int columnIndex, Calendar cal) throws SQLException {
        return delegate.getTimestamp(columnIndex, cal);
    }


    public Timestamp getTimestamp(String columnName, Calendar cal) throws SQLException {
        return delegate.getTimestamp(columnName, cal);
    }


    public URL getURL(int columnIndex) throws SQLException {
        return delegate.getURL(columnIndex);
    }


    public URL getURL(String columnName) throws SQLException {
        return delegate.getURL(columnName);
    }


    public void updateRef(int columnIndex, Ref value) throws SQLException {
        delegate.updateRef(columnIndex, value);
    }


    public void updateRef(String columnName, Ref value) throws SQLException {
        delegate.updateRef(columnName, value);
    }


    public void updateBlob(int columnIndex, Blob value) throws SQLException {
        delegate.updateBlob(columnIndex, value);
    }


    public void updateBlob(String columnName, Blob value) throws SQLException {
        delegate.updateBlob(columnName, value);
    }


    public void updateClob(int columnIndex, Clob value) throws SQLException {
        delegate.updateClob(columnIndex, value);
    }


    public void updateClob(String columnName, Clob value) throws SQLException {
        delegate.updateClob(columnName, value);
    }


    public void updateArray(int columnIndex, Array value) throws SQLException {
        delegate.updateArray(columnIndex, value);
    }


    public void updateArray(String columnName, Array value) throws SQLException {
        delegate.updateArray(columnName, value);
    }
}
