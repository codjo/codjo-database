/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 */
public class UdtsStructure extends ObjectsStructure<UdtStructure> {
    public UdtsStructure() {
    }


    protected Set<String> getUpdtTables(List<String> commonUdtNames) {
        findUdtDifferences(commonUdtNames);
        return getUpdtTableNames(commonUdtNames);
    }


    protected List getDelUdtInfos(String udtName) {
        return getUdtInfos(getRefTableObjectInfos(udtName));
    }


    protected List getNewUdtInfos(String tableName) {
        return getUdtInfos(getSrcTableObjectInfos(tableName));
    }


    private void findUdtDifferences(List commonTableNames) {
        if (commonTableNames == null) {
            return;
        }
        for (int i = 0; i < commonTableNames.size(); i++) {
            String tableName = (String)commonTableNames.get(i);
            Map srcCols = getSrcTableObjectInfos(tableName);
            Map refCols = getRefTableObjectInfos(tableName);

            List addCols;
            List deleteCols;
            if (refCols != null && srcCols == null) {
                deleteCols = new ArrayList(refCols.keySet());
                fillColumnInfos(deleteCols, refCols, tableName, getDelObjects());
            }
            if (refCols == null && srcCols != null) {
                addCols = new ArrayList(srcCols.keySet());
                fillColumnInfos(addCols, srcCols, tableName, getNewObjects());
            }
            if (refCols != null && srcCols != null) {
                deleteCols = Util.removeElements(refCols.keySet(), srcCols.keySet());
                fillColumnInfos(deleteCols, refCols, tableName, getDelObjects());

                addCols = Util.removeElements(srcCols.keySet(), refCols.keySet());
                fillColumnInfos(addCols, srcCols, tableName, getNewObjects());

                List commonColNames = new ArrayList(refCols.keySet());
                commonColNames.retainAll(srcCols.keySet());
                findUpdatedColumns(commonColNames, refCols, srcCols, tableName);
            }
        }
    }


    private void fillColumnInfos(List delColumns, Map refCols, String tableName,
                                 Map colInfos) {
        if (delColumns.size() != 0) {
            List infos = new ArrayList();
            for (int j = 0; j < delColumns.size(); j++) {
                String colName = (String)delColumns.get(j);
                infos.add(((UdtStructure)refCols.get(colName)).getColumnInfos());
            }
            colInfos.put(tableName, infos);
        }
    }


    private List getUdtInfos(Map tableColInfos) {
        List infos = new ArrayList();
        for (Iterator iterator = tableColInfos.values().iterator(); iterator.hasNext();) {
            UdtStructure columnStructure = (UdtStructure)iterator.next();
            infos.add(columnStructure.getColumnInfos());
        }
        return infos;
    }


    private void findUpdatedColumns(List commonColNames, Map refCols, Map srcCols,
                                    String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (int j = 0; j < commonColNames.size(); j++) {
            String colName = (String)commonColNames.get(j);
            UdtStructure refColConfigs = (UdtStructure)refCols.get(colName);
            UdtStructure srcColConfigs = (UdtStructure)srcCols.get(colName);
            if (!isSameUdtStructure(refColConfigs, srcColConfigs)) {
                refInfos.add(refColConfigs.getColumnInfos());
                srcInfos.add(srcColConfigs.getColumnInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private boolean isSameUdtStructure(UdtStructure refColConfigs,
                                       UdtStructure srcColConfigs) {
        if (!Util.isEqual(refColConfigs.getUdtType(), srcColConfigs.getUdtType())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getLength(), srcColConfigs.getLength())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getPrecision(), srcColConfigs.getPrecision())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getScale(), srcColConfigs.getScale())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getAllowNulls(), srcColConfigs.getAllowNulls())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getDefaultName(), srcColConfigs.getDefaultName())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getRuleName(), srcColConfigs.getRuleName())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getIdentity(), srcColConfigs.getIdentity())) {
            return false;
        }
        return true;
    }
}
