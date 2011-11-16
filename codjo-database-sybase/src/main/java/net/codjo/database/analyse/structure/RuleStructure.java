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
public class RuleStructure implements ObjectInfo {
    private String ruleName;
    private String columnName;
    private String description;


    public List<String> getRuleInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(ruleName);
        infos.add(columnName);
        infos.add(description);
        return infos;
    }


    public String getRuleName() {
        return ruleName;
    }


    public String getColumnName() {
        return columnName;
    }


    public String getDescription() {
        return description;
    }


    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }


    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }


    public void setDescription(String description) {
        this.description = description;
    }
}
