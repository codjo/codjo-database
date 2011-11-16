/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 */
public class IndexesStructure extends ObjectsStructure<IndexStructure> {
    protected Set<String> getUpdtTables(List<String> commonTableNames) {
        findIndexDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List getDelTableIndexInfos(String tableName) {
        return getTableIndexInfos(getRefTableObjectInfos(tableName));
    }


    protected List getNewTableIndexInfos(String tableName) {
        return getTableIndexInfos(getSrcTableObjectInfos(tableName));
    }


    private List<List> getTableIndexInfos(Map tableIndexInfos) {
        List<List> infos = new ArrayList<List>();
        if (tableIndexInfos == null) {
            return infos;
        }
        for (Object bject : tableIndexInfos.values()) {
            IndexStructure indexStructure = (IndexStructure)bject;
            infos.add(indexStructure.getIndexInfos());
        }
        return infos;
    }


    protected void findIndexDifferences(List<String> commonTableNames) {
        for (String tableName : commonTableNames) {
            Map srcIndexes = getSrcTableObjectInfos(tableName);
            Map refIndexes = getRefTableObjectInfos(tableName);
            List addIndexes;
            List deleteIndexes;
            if (refIndexes != null && srcIndexes == null) {
                deleteIndexes = new ArrayList<String>(refIndexes.keySet());
                fillIndexInfos(deleteIndexes, refIndexes, tableName, getDelObjects());
            }
            if (refIndexes == null && srcIndexes != null) {
                addIndexes = new ArrayList<String>(srcIndexes.keySet());
                fillIndexInfos(addIndexes, srcIndexes, tableName, getNewObjects());
            }
            if (refIndexes != null && srcIndexes != null) {
                deleteIndexes =
                      Util.removeElements(refIndexes.keySet(), srcIndexes.keySet());
                fillIndexInfos(deleteIndexes, refIndexes, tableName, getDelObjects());

                addIndexes =
                      Util.removeElements(srcIndexes.keySet(), refIndexes.keySet());
                fillIndexInfos(addIndexes, srcIndexes, tableName, getNewObjects());

                List<String> commonIndexNames = new ArrayList<String>(refIndexes.keySet());
                commonIndexNames.retainAll(srcIndexes.keySet());
                findUpdatedIndexes(commonIndexNames, refIndexes, srcIndexes, tableName);
            }
        }
    }


    private void findUpdatedIndexes(List<String> commonIndexNames, Map refIndexes,
                                    Map srcIndexes, String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (String indexName : commonIndexNames) {
            IndexStructure refIndexConfigs = (IndexStructure)refIndexes.get(indexName);
            IndexStructure srcIndexConfigs = (IndexStructure)srcIndexes.get(indexName);
            if (!isSameIndexStructure(refIndexConfigs, srcIndexConfigs)) {
                refInfos.add(refIndexConfigs.getIndexInfos());
                srcInfos.add(srcIndexConfigs.getIndexInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillIndexInfos(List<String> delIndexes, Map refIndexes, String tableName,
                                Map indexInfos) {
        if (delIndexes.size() != 0) {
            List infos = new ArrayList();
            for (String indexName : delIndexes) {
                infos.add(((IndexStructure)refIndexes.get(indexName)).getIndexInfos());
            }
            indexInfos.put(tableName, infos);
        }
    }


    private boolean isSameIndexStructure(IndexStructure refIndexConfigs,
                                         IndexStructure srcIndexConfigs) {
        if (!Util.isEqual(refIndexConfigs.getColumnNames(),
                          srcIndexConfigs.getColumnNames())) {
            return false;
        }
        if (!Util.isEqual(refIndexConfigs.getDefinition(), srcIndexConfigs.getDefinition())) {
            return false;
        }
        return true;
    }
}
