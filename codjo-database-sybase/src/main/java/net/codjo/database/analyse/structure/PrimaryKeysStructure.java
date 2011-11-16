/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.2 $
 */
public class PrimaryKeysStructure extends ObjectsStructure<PrimaryKeyStructure> {
    public PrimaryKeysStructure() {
    }


    protected Collection<String> getUpdtTables(List<String> commonTableNames) {
        findPkDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List getDelTablePkInfos(String tableName) {
        return getTablePkInfos(getRefTableObjectInfos(tableName));
    }


    protected List getNewTablePkInfos(String tableName) {
        return getTablePkInfos(getSrcTableObjectInfos(tableName));
    }


    private List getTablePkInfos(Map tablePkInfos) {
        List infos = new ArrayList();
        if (tablePkInfos == null) {
            return infos;
        }
        for (Object o : tablePkInfos.values()) {
            PrimaryKeyStructure pkStructure = (PrimaryKeyStructure)o;
            infos.add(pkStructure.getPkInfos());
        }
        return infos;
    }


    protected void findPkDifferences(List commonTableNames) {
        for (int i = 0; i < commonTableNames.size(); i++) {
            String tableName = (String)commonTableNames.get(i);
            Map srcPks = getSrcTableObjectInfos(tableName);
            Map refPks = getRefTableObjectInfos(tableName);
            List addPks;
            List deletePks;
            if (refPks != null && srcPks == null) {
                deletePks = new ArrayList(refPks.keySet());
                fillPkInfos(deletePks, refPks, tableName, getDelObjects());
            }
            if (refPks == null && srcPks != null) {
                addPks = new ArrayList(srcPks.keySet());
                fillPkInfos(addPks, srcPks, tableName, getNewObjects());
            }
            if (refPks != null && srcPks != null) {
                deletePks = Util.removeElements(refPks.keySet(), srcPks.keySet());
                fillPkInfos(deletePks, refPks, tableName, getDelObjects());

                addPks = Util.removeElements(srcPks.keySet(), refPks.keySet());
                fillPkInfos(addPks, srcPks, tableName, getNewObjects());

                List commonPkNames = new ArrayList(refPks.keySet());
                commonPkNames.retainAll(srcPks.keySet());
                findUpdatedPks(commonPkNames, refPks, srcPks, tableName);
            }
        }
    }


    private void findUpdatedPks(List commonPkNames, Map refPks, Map srcPks,
                                String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (int j = 0; j < commonPkNames.size(); j++) {
            String pkName = (String)commonPkNames.get(j);
            PrimaryKeyStructure refPkConfigs = (PrimaryKeyStructure)refPks.get(pkName);
            PrimaryKeyStructure srcPkConfigs = (PrimaryKeyStructure)srcPks.get(pkName);
            if (!isSamePkStructure(refPkConfigs, srcPkConfigs)) {
                refInfos.add(refPkConfigs.getPkInfos());
                srcInfos.add(srcPkConfigs.getPkInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillPkInfos(List delPks, Map refPks, String tableName, Map colInfos) {
        if (delPks.size() != 0) {
            List infos = new ArrayList();
            for (int j = 0; j < delPks.size(); j++) {
                String colName = (String)delPks.get(j);
                infos.add(((PrimaryKeyStructure)refPks.get(colName)).getPkInfos());
            }
            colInfos.put(tableName, infos);
        }
    }


    private boolean isSamePkStructure(PrimaryKeyStructure refPkConfigs,
                                      PrimaryKeyStructure srcPkConfigs) {
        if (!Util.isEqual(refPkConfigs.getColumnNames(), srcPkConfigs.getColumnNames())) {
            return false;
        }
        if (!Util.isEqual(refPkConfigs.getDefinition(), srcPkConfigs.getDefinition())) {
            return false;
        }
        return true;
    }
}
