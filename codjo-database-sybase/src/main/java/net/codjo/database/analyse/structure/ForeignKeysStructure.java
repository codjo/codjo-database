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
public class ForeignKeysStructure extends ObjectsStructure<ForeignKeyStructure> {
    public ForeignKeysStructure() {
    }


    protected Set<String> getUpdtTables(List<String> commonTableNames) {
        findFkDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List getDelTableFkInfos(String tableName) {
        return getTableFkInfos(getRefTableObjectInfos(tableName));
    }


    protected List getNewTableFkInfos(String tableName) {
        return getTableFkInfos(getSrcTableObjectInfos(tableName));
    }


    private List getTableFkInfos(Map tableColInfos) {
        List infos = new ArrayList();
        if (tableColInfos == null) {
            return infos;
        }
        for (Iterator iterator = tableColInfos.values().iterator(); iterator.hasNext();) {
            ForeignKeyStructure columnStructure = (ForeignKeyStructure)iterator.next();
            infos.add(columnStructure.getFkInfos());
        }
        return infos;
    }


    protected void findFkDifferences(List commonTableNames) {
        for (int i = 0; i < commonTableNames.size(); i++) {
            String tableName = (String)commonTableNames.get(i);
            Map srcRules = getSrcTableObjectInfos(tableName);
            Map refRules = getRefTableObjectInfos(tableName);
            List addRules;
            List deleteRules;
            if (refRules != null && srcRules == null) {
                deleteRules = new ArrayList(refRules.keySet());
                fillFkInfos(deleteRules, refRules, tableName, getDelObjects());
            }
            if (refRules == null && srcRules != null) {
                addRules = new ArrayList(srcRules.keySet());
                fillFkInfos(addRules, srcRules, tableName, getNewObjects());
            }
            if (refRules != null && srcRules != null) {
                deleteRules = Util.removeElements(refRules.keySet(), srcRules.keySet());
                fillFkInfos(deleteRules, refRules, tableName, getDelObjects());

                addRules = Util.removeElements(srcRules.keySet(), refRules.keySet());
                fillFkInfos(addRules, srcRules, tableName, getNewObjects());

                List commonRuleNames = new ArrayList(refRules.keySet());
                commonRuleNames.retainAll(srcRules.keySet());
                findUpdatedFks(commonRuleNames, refRules, srcRules, tableName);
            }
        }
    }


    private void findUpdatedFks(List commonRuleNames, Map refRules, Map srcRules,
                                String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (int j = 0; j < commonRuleNames.size(); j++) {
            String colName = (String)commonRuleNames.get(j);
            ForeignKeyStructure refRuleConfigs =
                  (ForeignKeyStructure)refRules.get(colName);
            ForeignKeyStructure srcRuleConfigs =
                  (ForeignKeyStructure)srcRules.get(colName);
            if (!isSameRuleStructure(refRuleConfigs, srcRuleConfigs)) {
                refInfos.add(refRuleConfigs.getFkInfos());
                srcInfos.add(srcRuleConfigs.getFkInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillFkInfos(List delColumns, Map refCols, String tableName, Map colInfos) {
        if (delColumns.size() != 0) {
            List infos = new ArrayList();
            for (int j = 0; j < delColumns.size(); j++) {
                String colName = (String)delColumns.get(j);
                infos.add(((ForeignKeyStructure)refCols.get(colName)).getFkInfos());
            }
            colInfos.put(tableName, infos);
        }
    }


    private boolean isSameRuleStructure(ForeignKeyStructure refRuleConfigs,
                                        ForeignKeyStructure srcRuleConfigs) {
        if (!Util.isEqual(refRuleConfigs.getColumnNames(), srcRuleConfigs.getColumnNames())) {
            return false;
        }
        if (!Util.isEqual(refRuleConfigs.getDefinition(), srcRuleConfigs.getDefinition())) {
            return false;
        }
        return true;
    }
}
