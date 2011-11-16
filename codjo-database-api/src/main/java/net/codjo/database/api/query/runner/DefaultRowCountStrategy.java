package net.codjo.database.api.query.runner;
import net.codjo.database.api.query.PreparedSelectQuery;
import net.codjo.database.api.query.SelectResult;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import org.apache.log4j.Logger;

public class DefaultRowCountStrategy implements RowCountStrategy {
    private PreparedStatement countStatement;


    public DefaultRowCountStrategy(Connection connection, String query) throws SQLException {
        countStatement = createCountStatement(query, connection);
    }


    public void setBoolean(int parameterIndex, boolean value) throws SQLException {
        if (countStatement != null) {
            countStatement.setBoolean(parameterIndex, value);
        }
    }


    public void setString(int parameterIndex, String value) throws SQLException {
        if (countStatement != null) {
            countStatement.setString(parameterIndex, value);
        }
    }


    public void setTimestamp(int parameterIndex, Timestamp value) throws SQLException {
        if (countStatement != null) {
            countStatement.setTimestamp(parameterIndex, value);
        }
    }


    public void setBigDecimal(int parameterIndex, BigDecimal value) throws SQLException {
        if (countStatement != null) {
            countStatement.setBigDecimal(parameterIndex, value);
        }
    }


    public void setDate(int parameterIndex, Date value) throws SQLException {
        if (countStatement != null) {
            countStatement.setDate(parameterIndex, value);
        }
    }


    public void setDouble(int parameterIndex, double value) throws SQLException {
        if (countStatement != null) {
            countStatement.setDouble(parameterIndex, value);
        }
    }


    public void setInt(int parameterIndex, int value) throws SQLException {
        if (countStatement != null) {
            countStatement.setInt(parameterIndex, value);
        }
    }


    public void setObject(int parameterIndex, Object value) throws SQLException {
        if (countStatement != null) {
            countStatement.setObject(parameterIndex, value);
        }
    }


    public void setObject(int parameterIndex, Object value, int sqlType) throws SQLException {
        if (countStatement != null) {
            countStatement.setObject(parameterIndex, value, sqlType);
        }
    }


    public int computeRowCount() throws SQLException {
        if (countStatement == null) {
            return SelectResult.UNDEFINED_TOTAL_ROW_COUNT;
        }
        int recordCount = 0;
        try {
            ResultSet results = countStatement.executeQuery();
            if (results.next()) {
                recordCount = results.getInt(1);
            }
            results.close();
        }
        finally {
            countStatement.close();
        }
        return recordCount;
    }


    static PreparedStatement createCountStatement(String query, Connection connection) throws SQLException {
        String selectCountQuery = selectCountQueryFor(query);
        if (selectCountQuery == null) {
            return null;
        }
        return connection.prepareStatement(selectCountQuery);
    }


    static String selectCountQueryFor(String selectQuery) {
        String lowerQuery = selectQuery.toLowerCase();
        int selectIndex = lowerQuery.indexOf("select ");
        int firstFromIndex = lowerQuery.indexOf(" from ");

        if (selectIndex != -1 && firstFromIndex != -1) {
            int fromIndex = findMyFrom(lowerQuery, selectIndex, firstFromIndex);
            return "select count(1)" + selectQuery.substring(fromIndex);
        }
        else {
            Logger.getLogger(PreparedSelectQuery.class)
                  .warn("Détermination du nombre de ligne impossible sur des requetes autres que 'select' : "
                        + selectQuery);
            return null;
        }
    }


    private static int findMyFrom(String sqlQuery, int firstSelectIndex, int firstFromIndex) {
        String part = sqlQuery.substring(firstSelectIndex, firstFromIndex);
        int nextSelectRelativeIndex = part.indexOf(" select ");
        if (nextSelectRelativeIndex == -1) {
            return firstFromIndex;
        }
        else {
            int index = firstSelectIndex + nextSelectRelativeIndex + " select ".length();
            int fromIndexForNextSelect = findMyFrom(sqlQuery, index, firstFromIndex);

            int nextFirstFromIndex = sqlQuery.indexOf(" from ", fromIndexForNextSelect + 1);
            return findMyFrom(sqlQuery, firstFromIndex, nextFirstFromIndex);
        }
    }
}
