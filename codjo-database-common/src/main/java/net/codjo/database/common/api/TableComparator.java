package net.codjo.database.common.api;
import java.sql.SQLException;
public interface TableComparator {
    void setOrderClause(String orderClause);


    void setColumnsToSkip(int[] columns);


    void setPrecision(double precision);


    boolean equals(String table1, String table2) throws SQLException;


    boolean equals(String table1, String table2, int[] columns, String orderClauseString) throws SQLException;


    boolean equals(String table1, String table2, String catalog) throws SQLException;


    boolean isEqual(Object obj1, Object obj2);
}
