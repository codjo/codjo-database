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
public class IndexStructure implements ObjectInfo {
    private String indexName;
    private String columnNames;
    private String definition;


    public List<String> getIndexInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(indexName);
        infos.add(columnNames);
        infos.add(definition);
        return infos;
    }


    public String getIndexName() {
        return indexName;
    }


    public String getColumnNames() {
        return columnNames;
    }


    public String getDefinition() {
        return definition;
    }


    public void setIndexName(String indexName) {
        this.indexName = indexName;
    }


    public void setColumnNames(String columnNames) {
        this.columnNames = columnNames;
    }


    public void setDefinition(String definition) {
        this.definition = definition;
    }
}
