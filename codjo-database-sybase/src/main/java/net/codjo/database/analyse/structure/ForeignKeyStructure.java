/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.List;
/**
 */
public class ForeignKeyStructure implements ObjectInfo {
    private String foreignKeyName;
    private String columnNames;
    private String definition;


    public List<String> getFkInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(foreignKeyName);
        infos.add(columnNames);
        infos.add(definition);
        return infos;
    }


    public String getForeignKeyName() {
        return foreignKeyName;
    }


    public String getColumnNames() {
        return columnNames;
    }


    public String getDefinition() {
        return definition;
    }


    public void setForeignKeyName(String foreignKeyName) {
        this.foreignKeyName = foreignKeyName;
    }


    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }


    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
