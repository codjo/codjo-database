package net.codjo.database.common.impl.comparator;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.SQLFieldList;
import net.codjo.database.common.api.TableComparator;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
public class DefaultTableComparator implements TableComparator {
    private static final String SELECT_FROM = "select * from ";
    private static final String COLONNES_STR = " colonne(s)";
    private static final String TABLES_STR = " Table ";
    private static final String SELECT_COUNT_STR = "select count(*) from ";
    private static final String NEW_LINE = "\n";
    private static final String SEPARATOR = " : ";
    private int[] columnsToSkip = null;
    private String orderClause = "";
    private double precision = -1;
    private Connection connection = null;


    public DefaultTableComparator(Connection connection, int columnToSkip, String orderClause) {
        this.connection = connection;
        initColumnToSkip(columnToSkip);
        this.orderClause = orderClause;
    }


    public void setOrderClause(String orderClause) {
        this.orderClause = orderClause;
    }


    /**
     * Positionne les colonnes a ne pas comparer
     *
     * @param columns liste d'indice de colonne
     */
    public void setColumnsToSkip(int[] columns) {
        this.columnsToSkip = columns;
    }


    /**
     * Positionne la precision utilisee lors de la comparaison de deux numerique. On dit que a et b sont egaux
     * si : <br> <code>(abs(a - b)) =&lt; precision</code>
     *
     * @param precision La nouvelle valeur de precision
     */
    public void setPrecision(double precision) {
        this.precision = precision;
    }


    /**
     * Retourne l'attribut equal de Comparator
     *
     * @param obj1 Description of the Parameter
     * @param obj2 Description of the Parameter
     *
     * @return La valeur de equal
     */
    public boolean isEqual(Object obj1, Object obj2) {
        if (obj1 == obj2) {
            return true;
        }

        if (obj1 != null && obj1.equals(obj2)) {
            return true;
        }

        if (obj1 != null && obj2 != null && precision > 0 && obj1 instanceof Number
            && obj2 instanceof Number) {
            double valA = ((Number)obj1).doubleValue();
            double valB = ((Number)obj2).doubleValue();
            return Math.abs(valA - valB) <= precision;
        }

        return false;
    }


    /**
     * Lancement de la comparaison de deux tables
     *
     * @param table1 La première table
     * @param table2 La deuxième table (et oui !)
     *
     * @return Egalité des deux tables
     *
     * @throws java.sql.SQLException Problème d'accès base
     */
    public boolean equals(String table1, String table2)
          throws SQLException {
        return equals(table1, table2, null);
    }


    /**
     * Lancement de la comparaison de deux tables
     *
     * @param table1            La première table
     * @param table2            La deuxième table (et oui !)
     * @param columns
     * @param orderClauseString
     *
     * @return Egalité des deux tables
     *
     * @throws java.sql.SQLException Problème d'accès base
     */
    public boolean equals(String table1, String table2, int[] columns, String orderClauseString)
          throws SQLException {
        setColumnsToSkip(columns);
        setOrderClause(orderClauseString);
        return equals(table1, table2, null);
    }


    /**
     * Lancement de la comparaison de deux tables
     *
     * @param table1  La première table
     * @param table2  La deuxième table (et oui !)
     * @param catalog Le catalogue de la comparaison
     *
     * @return Egalité des deux tables
     *
     * @throws java.sql.SQLException Problème d'accès base
     */
    public boolean equals(String table1, String table2, String catalog)
          throws SQLException {
        DatabaseFactory factory = new DatabaseFactory();

        SQLFieldList strucTable1 = factory.createSQLFieldList(connection, catalog, table1);
        SQLFieldList strucTable2 = factory.createSQLFieldList(connection, catalog, table2);

        if (!sameNumberOfCol(table1, strucTable1, table2, strucTable2)) {
            return false;
        }

        Statement stmt1 = null;
        Statement stmt2 = null;

        try {

            if (!sameNumberOfRow(table1, table2)) {
                return false;
            }

            stmt1 = connection.createStatement();
            stmt2 = connection.createStatement();
            ResultSet rs1 = executeSelectRequest(stmt1, table1);
            ResultSet rs2 = executeSelectRequest(stmt2, table2);

            Object obj1;
            Object obj2;
            int numLine = 0;
            while ((rs1.next()) && (rs2.next())) {
                numLine++;
                for (int i = 1; i <= strucTable1.size(); i++) {
                    if (!isColumnToSkip(i)) {
                        obj1 = rs1.getObject(i);
                        obj2 = rs2.getObject(i);

                        if (!isEqual(obj1, obj2)) {
                            String msg =
                                  "[Comparator] Ligne " + numLine + " Colonne "
                                  + rs1.getMetaData().getColumnName(i) + NEW_LINE + TABLES_STR + table1
                                  + " valeur = (" + obj1 + ")" + NEW_LINE + TABLES_STR + table2
                                  + " valeur = ("
                                  + obj2 + ")";
                            info(msg);
                            return false;
                        }
                    }
                }
            }
            return true;
        }
        finally {
            if (stmt1 != null) {
                stmt1.close();
            }
            if (stmt2 != null) {
                stmt2.close();
            }
        }
    }


    private ResultSet executeSelectRequest(Statement statement, String tableName) throws SQLException {
        String query = SELECT_FROM + tableName;
        if (!"".equals(orderClause)) {
            query = query + " order by " + orderClause;
        }

        return statement.executeQuery(query);
    }


    /**
     * Initialise le tableau des colonnes "a ne pas comparer" avec une seule colonne.
     *
     * @param columnToSkip Description of the Parameter
     */
    private void initColumnToSkip(int columnToSkip) {
        this.columnsToSkip = new int[]{columnToSkip};
    }


    /**
     * Retourne l'attribut columnToSkip de Comparator
     *
     * @param col Description of the Parameter
     *
     * @return La valeur de columnToSkip
     */
    private boolean isColumnToSkip(int col) {
        if (columnsToSkip == null) {
            return false;
        }
        for (int aColumnsToSkip : columnsToSkip) {
            if (aColumnsToSkip == col) {
                return true;
            }
        }
        return false;
    }


    private boolean sameNumberOfCol(String table1, SQLFieldList strucTable1, String table2,
                                    SQLFieldList strucTable2) {
        if (strucTable1.size() != strucTable2.size()) {
            info("[Comparator] Nombre de colonnes différent !" + NEW_LINE + TABLES_STR + table1 + SEPARATOR
                 + strucTable1.size() + COLONNES_STR + NEW_LINE + TABLES_STR + table2 + SEPARATOR
                 + strucTable2.size() + COLONNES_STR);
            return false;
        }
        return true;
    }


    private boolean sameNumberOfRow(String table1, String table2)
          throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet rs = statement.executeQuery(SELECT_COUNT_STR + table1);
        try {
            rs.next();
            int nbRow1 = rs.getInt(1);
            rs = statement.executeQuery(SELECT_COUNT_STR + table2);
            rs.next();
            int nbRow2 = rs.getInt(1);
            if (nbRow1 != nbRow2) {
                info("[Comparator] Nombre de lignes différent !" + NEW_LINE + TABLES_STR + table1 + SEPARATOR
                     + nbRow1 + " ligne(s)" + NEW_LINE + TABLES_STR + table2 + SEPARATOR + nbRow2
                     + " ligne(s)");
                return false;
            }
            return true;
        }
        finally {
            statement.close();
        }
    }


    private void info(String msg) {
        //noinspection UseOfSystemOutOrSystemErr
        System.out.println(msg);
    }
}
