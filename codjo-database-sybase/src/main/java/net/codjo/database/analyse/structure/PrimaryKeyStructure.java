/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.List;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public class PrimaryKeyStructure implements ObjectInfo {
    private String primaryKeyName;
    private String columnNames;
    private String definition;


    public List<String> getPkInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(primaryKeyName);
        infos.add(columnNames);
        infos.add(definition);
        return infos;
    }


    public String getPrimaryKeyName() {
        return primaryKeyName;
    }


    public String getColumnNames() {
        return columnNames;
    }


    public String getDefinition() {
        return definition;
    }


    public void setPrimaryKeyName(String primaryKeyName) {
        this.primaryKeyName = primaryKeyName;
    }


    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }


    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
