/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.List;
import java.util.Set;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public abstract class ObjectStructure {
    private ColumnsStructure columnsStructure;
    private PrivilegesStructure privilegesStructure;
    private ScriptsStructure scriptsStructure;


    public abstract Set<String> getNewObjects();


    public abstract Set<String> getDelObjects();


    public abstract Set<String> getUpdatedObjects();


    public boolean isModified() {
        return !(getUpdatedObjects().isEmpty() && getNewObjects().isEmpty() && getDelObjects().isEmpty());
    }


    public List<List<String>> getRefUpdatedColumns(String viewName) {
        return columnsStructure.getRefUpdtObjects(viewName);
    }


    public List<List<String>> getSrcUpdatedColumns(String viewName) {
        return columnsStructure.getSrcUpdtObjects(viewName);
    }


    public List getDelColumns(String viewName) {
        return columnsStructure.getDelTableColInfos(viewName);
    }


    public List getNewColumns(String viewName) {
        return columnsStructure.getNewTableColInfos(viewName);
    }


    public void setColumnsStructure(ColumnsStructure columnsStructure) {
        this.columnsStructure = columnsStructure;
    }


    public List getRefUpdatedPrivileges(String viewName) {
        return privilegesStructure.getRefUpdtObjects(viewName);
    }


    public List getSrcUpdatedPrivileges(String viewName) {
        return privilegesStructure.getSrcUpdtObjects(viewName);
    }


    public List getNewPrivileges(String viewName) {
        return privilegesStructure.getNewTablePrivilegeInfos(viewName);
    }


    public List getDelPrivileges(String viewName) {
        return privilegesStructure.getDelTablePrivilegeInfos(viewName);
    }


    public void setPrivilegesStructure(PrivilegesStructure privilegesStructure) {
        this.privilegesStructure = privilegesStructure;
    }


    public String getRefUpdatedScript(String viewName) {
        return scriptsStructure.getRefUpdtObjects(viewName);
    }


    public String getSrcUpdatedScript(String viewName) {
        return scriptsStructure.getSrcUpdtObjects(viewName);
    }


    public String getNewScript(String viewName) {
        return scriptsStructure.getNewProcedureScriptInfos(viewName);
    }


    public String getDelScript(String viewName) {
        return scriptsStructure.getDelProcedureScriptInfos(viewName);
    }


    public void setScriptsStructure(ScriptsStructure scriptsStructure) {
        this.scriptsStructure = scriptsStructure;
    }


    protected void addUpdatedObjectsByColumns(Set<String> updtObjects, List<String> objectNames) {
        Set<String> colUpdtObjects = columnsStructure.getUpdtTables(objectNames);
        if (!colUpdtObjects.isEmpty()) {
            updtObjects.addAll(colUpdtObjects);
        }
    }


    protected void addUpdatedObjectsByPrivileges(Set<String> updtObjects, List<String> objectNames) {
        Set<String> privUpdtObjects = privilegesStructure.getUpdtTables(objectNames);
        if (!privUpdtObjects.isEmpty()) {
            updtObjects.addAll(privUpdtObjects);
        }
    }


    protected void addUpdatedObjectsByScript(Set<String> updtObjects, List<String> objectNames) {
        Set<String> scriptUpdtObjects = scriptsStructure.getUpdtTables(objectNames);
        if (!scriptUpdtObjects.isEmpty()) {
            updtObjects.addAll(scriptUpdtObjects);
        }
    }
}
