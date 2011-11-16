/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
/**
 */
public class TableStructure extends ObjectStructure {
    private RulesStructure rulesStructure;
    private ForeignKeysStructure foreignKeysStructure;
    private PrimaryKeysStructure primaryKeysStructure;
    private ConstraintsStructure constraintsStructure;
    private IndexesStructure indexesStructure;
    private List<String> commonObjectNames;
    private Collection<String> refObjectNames;
    private Collection<String> srcObjectNames;


    @Override
    public Set<String> getUpdatedObjects() {
        Set<String> updtObjects = new TreeSet<String>();

        if (commonObjectNames != null) {
            addUpdatedObjectsByColumns(updtObjects, commonObjectNames);
            addUpdatedObjectsByPrivileges(updtObjects, commonObjectNames);

            Collection<String> ruleUpdtTables = rulesStructure.getUpdtTables(commonObjectNames);
            if (!ruleUpdtTables.isEmpty()) {
                updtObjects.addAll(ruleUpdtTables);
            }

            Collection<String> fkUpdtTables = foreignKeysStructure.getUpdtTables(commonObjectNames);
            if (!fkUpdtTables.isEmpty()) {
                updtObjects.addAll(fkUpdtTables);
            }

            Collection<String> pkUpdtTables = primaryKeysStructure.getUpdtTables(commonObjectNames);
            if (!pkUpdtTables.isEmpty()) {
                updtObjects.addAll(pkUpdtTables);
            }

            Collection<String> indexUpdtTables = indexesStructure.getUpdtTables(commonObjectNames);
            if (!indexUpdtTables.isEmpty()) {
                updtObjects.addAll(indexUpdtTables);
            }

            Collection<String> constraintUpdtTables = constraintsStructure.getUpdtTables(commonObjectNames);
            if (!constraintUpdtTables.isEmpty()) {
                updtObjects.addAll(constraintUpdtTables);
            }
        }
        return updtObjects;
    }


    @Override
    public Set<String> getDelObjects() {
        return new TreeSet<String>(Util.removeElements(refObjectNames, srcObjectNames));
    }


    @Override
    public Set<String> getNewObjects() {
        return new TreeSet<String>(Util.removeElements(srcObjectNames, refObjectNames));
    }


    public Collection<String> getRefObjectNames() {
        return Collections.unmodifiableCollection(refObjectNames);
    }


    public Collection<String> getSrcObjectNames() {
        return Collections.unmodifiableCollection(srcObjectNames);
    }


    public void setRulesStructure(RulesStructure rulesStructure) {
        this.rulesStructure = rulesStructure;
    }


    public void setForeignKeysStructure(ForeignKeysStructure foreignKeysStructure) {
        this.foreignKeysStructure = foreignKeysStructure;
    }


    public void setPrimaryKeysStructure(PrimaryKeysStructure primaryKeysStructure) {
        this.primaryKeysStructure = primaryKeysStructure;
    }


    public void setIndexesStructure(IndexesStructure indexesStructure) {
        this.indexesStructure = indexesStructure;
    }


    public void setConstraintsStructure(ConstraintsStructure constraintsStructure) {
        this.constraintsStructure = constraintsStructure;
    }


    public List getRefUpdtChecks(String tableName) {
        return constraintsStructure.getRefUpdtObjects(tableName);
    }


    public List getSrcUpdtChecks(String tableName) {
        return constraintsStructure.getSrcUpdtObjects(tableName);
    }


    public List getNewTableRuleInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return rulesStructure.getNewTableRuleInfos(tableName);
        }
    }


    public List getNewTableCheckInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return constraintsStructure.getNewTableCheckInfos(tableName);
        }
    }


    public List<List<String>> getRefUpdtRules(String tableName) {
        return rulesStructure.getRefUpdtObjects(tableName);
    }


    public List<List<String>> getSrcUpdtRules(String tableName) {
        return rulesStructure.getSrcUpdtObjects(tableName);
    }


    public List getDelTableRuleInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return rulesStructure.getDelTableRuleInfos(tableName);
        }
    }


    public List getDelTableCheckInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return constraintsStructure.getDelTableCheckInfos(tableName);
        }
    }


    public List<List<String>> getRefUpdtFks(String tableName) {
        return foreignKeysStructure.getRefUpdtObjects(tableName);
    }


    public List<List<String>> getSrcUpdtFks(String tableName) {
        return foreignKeysStructure.getSrcUpdtObjects(tableName);
    }


    public List getNewTableFkInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return foreignKeysStructure.getNewTableFkInfos(tableName);
        }
    }


    public List getDelTableFkInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return foreignKeysStructure.getDelTableFkInfos(tableName);
        }
    }


    public List<List<String>> getRefUpdtPks(String tableName) {
        return primaryKeysStructure.getRefUpdtObjects(tableName);
    }


    public List<List<String>> getSrcUpdtPks(String tableName) {
        return primaryKeysStructure.getSrcUpdtObjects(tableName);
    }


    public List getNewTablePkInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return primaryKeysStructure.getNewTablePkInfos(tableName);
        }
    }


    public List getDelTablePkInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return primaryKeysStructure.getDelTablePkInfos(tableName);
        }
    }


    public List<List<String>> getRefUpdtIndexes(String tableName) {
        return indexesStructure.getRefUpdtObjects(tableName);
    }


    public List<List<String>> getSrcUpdtIndexes(String tableName) {
        return indexesStructure.getSrcUpdtObjects(tableName);
    }


    public List getNewTableIndexeInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return indexesStructure.getNewTableIndexInfos(tableName);
        }
    }


    public List getDelTableIndexeInfos(String tableName) {
        if (commonObjectNames.contains(tableName)) {
            return new ArrayList();
        }
        else {
            return indexesStructure.getDelTableIndexInfos(tableName);
        }
    }


    public void setTableNames(boolean isRefBase, List<String> viewNames) {
        if (isRefBase) {
            refObjectNames = viewNames;
        }
        else {
            srcObjectNames = viewNames;
        }
        setCommonTableNames();
    }


    private void setCommonTableNames() {
        if (srcObjectNames != null && refObjectNames != null) {
            commonObjectNames = new ArrayList<String>(refObjectNames);
            commonObjectNames.retainAll(srcObjectNames);
        }
    }
}
