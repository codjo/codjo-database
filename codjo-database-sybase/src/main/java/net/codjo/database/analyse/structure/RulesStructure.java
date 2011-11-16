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
public class RulesStructure extends ObjectsStructure<RuleStructure> {
    public RulesStructure() {
    }


    protected Set<String> getUpdtTables(List<String> commonTableNames) {
        findRuleDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List<List<String>> getDelTableRuleInfos(String tableName) {
        return getTableRuleInfos(getRefTableObjectInfos(tableName));
    }


    protected List<List<String>> getNewTableRuleInfos(String tableName) {
        return getTableRuleInfos(getSrcTableObjectInfos(tableName));
    }


    private List<List<String>> getTableRuleInfos(Map<String, RuleStructure> tableColInfos) {
        List<List<String>> infos = new ArrayList<List<String>>();
        if (tableColInfos == null) {
            return infos;
        }
        for (RuleStructure ruleStructure : tableColInfos.values()) {
            infos.add(ruleStructure.getRuleInfos());
        }
        return infos;
    }


    private void findRuleDifferences(List<String> commonTableNames) {
        for (String tableName : commonTableNames) {
            Map<String, RuleStructure> srcRules = getSrcTableObjectInfos(tableName);
            Map<String, RuleStructure> refRules = getRefTableObjectInfos(tableName);
            List<String> addRules;
            List<String> deleteRules;
            if (refRules != null && srcRules == null) {
                deleteRules = new ArrayList<String>(refRules.keySet());
                fillRuleInfos(deleteRules, refRules, tableName, getDelObjects());
            }
            if (refRules == null && srcRules != null) {
                addRules = new ArrayList<String>(srcRules.keySet());
                fillRuleInfos(addRules, srcRules, tableName, getNewObjects());
            }
            if (refRules != null && srcRules != null) {
                deleteRules = Util.removeElements(refRules.keySet(), srcRules.keySet());
                fillRuleInfos(deleteRules, refRules, tableName, getDelObjects());

                addRules = Util.removeElements(srcRules.keySet(), refRules.keySet());
                fillRuleInfos(addRules, srcRules, tableName, getNewObjects());

                List<String> commonRuleNames = new ArrayList<String>(refRules.keySet());
                commonRuleNames.retainAll(srcRules.keySet());
                findUpdatedRules(commonRuleNames, refRules, srcRules, tableName);
            }
        }
    }


    private void findUpdatedRules(List<String> commonRuleNames, Map refRules, Map srcRules,
                                  String tableName) {
        List<List<String>> refInfos = new ArrayList<List<String>>();
        List<List<String>> srcInfos = new ArrayList<List<String>>();
        for (String colName : commonRuleNames) {
            RuleStructure refRuleConfigs = (RuleStructure)refRules.get(colName);
            RuleStructure srcRuleConfigs = (RuleStructure)srcRules.get(colName);
            if (!isSameRuleStructure(refRuleConfigs, srcRuleConfigs)) {
                refInfos.add(refRuleConfigs.getRuleInfos());
                srcInfos.add(srcRuleConfigs.getRuleInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillRuleInfos(List<String> delColumns,
                               Map<String, RuleStructure> refCols,
                               String tableName,
                               Map<String, List<List<String>>> colInfos) {
        if (!delColumns.isEmpty()) {
            List<List<String>> infos = new ArrayList<List<String>>();
            for (String colName : delColumns) {
                infos.add(refCols.get(colName).getRuleInfos());
            }
            colInfos.put(tableName, infos);
        }
    }


    private boolean isSameRuleStructure(RuleStructure refRuleConfigs,
                                        RuleStructure srcRuleConfigs) {
        if (!Util.isEqual(refRuleConfigs.getColumnName(), srcRuleConfigs.getColumnName())) {
            return false;
        }
        if (!Util.isEqual(refRuleConfigs.getDescription(), srcRuleConfigs.getDescription())) {
            return false;
        }
        return true;
    }
}
