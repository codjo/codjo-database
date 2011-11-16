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
public class PrivilegeStructure implements ObjectInfo {
    private String group;
    private String type;
    private String action;
    private String column;


    public List<String> getPrivilegeInfos() {
        List<String> infos = new ArrayList<String>();
        infos.add(group);
        infos.add(type);
        infos.add(action);
        infos.add(column);
        return infos;
    }


    public String getGroup() {
        return group;
    }


    public String getType() {
        return type;
    }


    public String getAction() {
        return action;
    }


    public String getColumn() {
        return column;
    }


    public void setGroup(String group) {
        this.group = group;
    }


    public void setType(String type) {
        this.type = type;
    }


    public void setAction(String action) {
        this.action = action;
    }


    public void setColumn(String column) {
        this.column = column;
    }
}
