/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
//import net.codjo.tools.transbase.Params;

/**
 *
 */
public class ConstraintsStructure extends ObjectsStructure<ConstraintStructure> {

    protected Collection<String> getUpdtTables(List<String> commonTableNames) {
        findCheckDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List getDelTableCheckInfos(String tableName) {
        return getTableCheckInfos(getRefTableObjectInfos(tableName));
    }


    protected List getNewTableCheckInfos(String tableName) {
        return getTableCheckInfos(getSrcTableObjectInfos(tableName));
    }


    private List getTableCheckInfos(Map tableCheckInfos) {
        List infos = new ArrayList();
        if (tableCheckInfos == null) {
            return infos;
        }
        for (Iterator iterator = tableCheckInfos.values().iterator(); iterator.hasNext();) {
            ConstraintStructure constraintStructure = (ConstraintStructure)iterator.next();
            infos.add(constraintStructure.getCheckInfos());
        }
        return infos;
    }


    protected void findCheckDifferences(List commonTableNames) {
        for (int i = 0; i < commonTableNames.size(); i++) {
            String tableName = (String)commonTableNames.get(i);
            Map srcChecks = getSrcTableObjectInfos(tableName);
            Map refChecks = getRefTableObjectInfos(tableName);
            List addChecks;
            List deleteChecks;
            if (refChecks != null && srcChecks == null) {
                deleteChecks = new ArrayList(refChecks.keySet());
                fillCheckInfos(deleteChecks, refChecks, tableName, getDelObjects());
            }
            if (refChecks == null && srcChecks != null) {
                addChecks = new ArrayList(srcChecks.keySet());
                fillCheckInfos(addChecks, srcChecks, tableName, getNewObjects());
            }
            if (refChecks != null && srcChecks != null) {
                deleteChecks =
                      Util.removeElements(refChecks.keySet(), srcChecks.keySet());
                fillCheckInfos(deleteChecks, refChecks, tableName, getDelObjects());

                addChecks = Util.removeElements(srcChecks.keySet(), refChecks.keySet());
                fillCheckInfos(addChecks, srcChecks, tableName, getNewObjects());

                List commonCheckNames = new ArrayList(refChecks.keySet());
                commonCheckNames.retainAll(srcChecks.keySet());
                findUpdatedChecks(commonCheckNames, refChecks, srcChecks, tableName);
            }
        }
    }


    private void findUpdatedChecks(List commonCheckNames, Map refChecks, Map srcChecks,
                                   String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (Object commonCheckName : commonCheckNames) {
            String checkName = (String)commonCheckName;
            ConstraintStructure refConstraint = (ConstraintStructure)refChecks.get(checkName);
            ConstraintStructure srcConstraint = (ConstraintStructure)srcChecks.get(checkName);
            if (!isSameCheckStructure(refConstraint, srcConstraint)) {
                refInfos.add(refConstraint.getCheckInfos());
                srcInfos.add(srcConstraint.getCheckInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillCheckInfos(List delChecks, Map refChecks, String tableName,
                                Map checkInfos) {
        if (delChecks.size() != 0) {
            List infos = new ArrayList();
            for (Object delCheck : delChecks) {
                String colName = (String)delCheck;
                infos.add(((ConstraintStructure)refChecks.get(colName)).getCheckInfos());
            }
            checkInfos.put(tableName, infos);
        }
    }


    private boolean isSameCheckStructure(ConstraintStructure refChecks, ConstraintStructure srcChecks) {
        return refChecks.equals(srcChecks);
    }
}
