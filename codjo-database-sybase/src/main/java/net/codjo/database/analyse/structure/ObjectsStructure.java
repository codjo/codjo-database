/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
/**
 */
class ObjectsStructure<T extends ObjectInfo> {
    private final Map<String, List<List<String>>> newObjects = new HashMap<String, List<List<String>>>();
    private final Map<String, List<List<String>>> delObjects = new HashMap<String, List<List<String>>>();
    private final Map<String, List<List<String>>> refUpdtObjects = new HashMap<String, List<List<String>>>();
    private final Map<String, List<List<String>>> srcUpdtObjects = new HashMap<String, List<List<String>>>();
    private Map<String, Map<String, T>> refTableObjectInfos;
    private Map<String, Map<String, T>> srcTableObjectInfos;


    public void setTableObjectInfos(boolean isRefBase, Map<String, Map<String, T>> tableObjectNames) {
        if (isRefBase) {
            refTableObjectInfos = tableObjectNames;
        }
        else {
            srcTableObjectInfos = tableObjectNames;
        }
    }


    protected Map<String, List<List<String>>> getNewObjects() {
        return newObjects;
    }


    protected Map<String, List<List<String>>> getDelObjects() {
        return delObjects;
    }


    public Map<String, List<List<String>>> getRefUpdtObjects() {
        return refUpdtObjects;
    }


    public Map<String, List<List<String>>> getSrcUpdtObjects() {
        return srcUpdtObjects;
    }


    protected Map<String, T> getRefTableObjectInfos(String tableName) {
        return refTableObjectInfos.get(tableName);
    }


    protected Map<String, T> getSrcTableObjectInfos(String tableName) {
        return srcTableObjectInfos.get(tableName);
    }


    protected Set<String> getUpdtTableNames(List<String> commonTableNames) {
        Set<String> updtTables = new TreeSet<String>();
        if (commonTableNames != null) {
            for (String commonTableName : commonTableNames) {
                if (newObjects.containsKey(commonTableName)) {
                    updtTables.add(commonTableName);
                }
                if (delObjects.containsKey(commonTableName)) {
                    updtTables.add(commonTableName);
                }
                if (srcUpdtObjects.containsKey(commonTableName)) {
                    updtTables.add(commonTableName);
                }
            }
        }
        return updtTables;
    }


    protected List<List<String>> getRefUpdtObjects(String tableName) {
        List<List<String>> updtTables = new ArrayList<List<String>>();

        List<List<String>> updtTableObjects = refUpdtObjects.get(tableName);
        if (updtTableObjects != null) {
            updtTables.addAll(updtTableObjects);
        }

        List<List<String>> delTableObjects = delObjects.get(tableName);
        if (delTableObjects != null) {
            updtTables.addAll(delTableObjects);
        }

        return updtTables;
    }


    protected List<List<String>> getSrcUpdtObjects(String tableName) {
        List<List<String>> updtTables = new ArrayList<List<String>>();

        List<List<String>> updtTableObjects = srcUpdtObjects.get(tableName);
        if (updtTableObjects != null) {
            updtTables.addAll(updtTableObjects);
        }

        List<List<String>> newTableObjects = newObjects.get(tableName);
        if (newTableObjects != null) {
            updtTables.addAll(newTableObjects);
        }

        return updtTables;
    }


    protected void fillUpdatedObjects(String tableName,
                                      List<List<String>> refInfos,
                                      List<List<String>> srcInfos) {
        if (!refInfos.isEmpty()) {
            refUpdtObjects.put(tableName, refInfos);
        }
        if (!srcInfos.isEmpty()) {
            srcUpdtObjects.put(tableName, srcInfos);
        }
    }
}
