/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.common.impl.sqlfield;
/**
 * SQL Field specific pour le type SQL date et assimile (TIME, TIMESTAMP,...).
 *
 * @author $Author: crego $
 * @version $Revision: 1.5 $
 */
class SQLDateField extends SQLField {
    /**
     * Constructor for the SQLDateField structure
     *
     * @param sqlDateType Type sql Date ( DATE, TIMESTAMP, TIME)
     * @param str         Nom physique du champs
     */
    SQLDateField(int sqlDateType, String str) {
        super(sqlDateType, str);
    }


    /**
     * Positionne la valeur. Si la valeur est de type <code>java.util.Date</code> elle est convertit en
     * <code>java.sql.Date</code>
     *
     * @param obj The new Value value
     */
    @Override
    public void setValue(Object obj) {
        if (obj != null && obj.getClass() == java.util.Date.class) {
            super.setValue(new java.sql.Timestamp(((java.util.Date)obj).getTime()));
        }
        else {
            super.setValue(obj);
        }
    }
}
