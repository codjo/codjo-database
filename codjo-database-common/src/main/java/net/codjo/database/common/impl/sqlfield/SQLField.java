/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.common.impl.sqlfield;
public class SQLField {
    private String name;
    private Object value;
    private int sqlType;


    public SQLField(int sqlType, String name) {
        this.sqlType = sqlType;
        this.name = name;
    }


    public SQLField(int sqlType, String str, Object obj) {
        this.sqlType = sqlType;
        name = str;
        setValue(obj);
    }


    public SQLField(String name, Object value) {
        this.name = name;
        this.value = value;
    }


    public String getName() {
        return name;
    }


    public int getSQLType() {
        return sqlType;
    }


    public Object getValue() {
        return value;
    }


    public void setValue(Object obj) {
        value = obj;
    }
}
