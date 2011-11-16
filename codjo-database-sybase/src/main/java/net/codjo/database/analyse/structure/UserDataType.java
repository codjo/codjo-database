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
import java.util.TreeSet;
/**
 */
public class UserDataType extends ObjectStructure {
    private List commonObjectNames = new ArrayList();
    private TreeSet updtObjectNames = new TreeSet();
    private Map refObjectNames;
    private Map srcObjectNames;

    public List getNewUdtInfos(String udtName) {
        List newUdts = new ArrayList();
        if (!commonObjectNames.contains(udtName)) {
            newUdts.add(((UdtStructure)srcObjectNames.get(udtName)).getColumnInfos());
        }
        return newUdts;
    }


    public List getDelUdtInfos(String udtName) {
        List delUdts = new ArrayList();
        if (!commonObjectNames.contains(udtName)) {
            delUdts.add(((UdtStructure)refObjectNames.get(udtName)).getColumnInfos());
        }
        return delUdts;
    }


    public List getRefUpdtUdts(String udtName) {
        List refUdts = new ArrayList();
        refUdts.add(((UdtStructure)refObjectNames.get(udtName)).getColumnInfos());
        return refUdts;
    }


    public List getSrcUpdtUdts(String udtName) {
        List srcUdts = new ArrayList();
        srcUdts.add(((UdtStructure)srcObjectNames.get(udtName)).getColumnInfos());
        return srcUdts;
    }


    public void setUdtNames(boolean isRefBase, Map udtInfos) {
        if (isRefBase) {
            refObjectNames = udtInfos;
        }
        else {
            srcObjectNames = udtInfos;
        }
        setCommonUdtNames();
    }


    private void setCommonUdtNames() {
        if (srcObjectNames != null && refObjectNames != null) {
            commonObjectNames = new ArrayList(refObjectNames.keySet());
            commonObjectNames.retainAll(srcObjectNames.keySet());
        }
    }


    public TreeSet getUpdatedObjects() {
        findUdtDifferences();
        return updtObjectNames;
    }


    private void findUdtDifferences() {
        for (int i = 0; i < commonObjectNames.size(); i++) {
            String tableName = (String)commonObjectNames.get(i);
            UdtStructure srcCols = (UdtStructure)srcObjectNames.get(tableName);
            UdtStructure refCols = (UdtStructure)refObjectNames.get(tableName);

            if (!isSameUdtStructure(refCols, srcCols)) {
                updtObjectNames.add(tableName);
            }
        }
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


    public TreeSet getDelObjects() {
        List delObjects =
            Util.removeElements(refObjectNames != null ? refObjectNames.keySet()
                                                       : new TreeSet(),
                srcObjectNames != null ? srcObjectNames.keySet() : new TreeSet());
        if (delObjects == null) {
            return new TreeSet();
        }
        else {
            return new TreeSet(delObjects);
        }
    }


    public TreeSet getNewObjects() {
        List newObjects =
            Util.removeElements(srcObjectNames != null ? srcObjectNames.keySet()
                                                       : new TreeSet(),
                refObjectNames != null ? refObjectNames.keySet() : new TreeSet());
        if (newObjects == null) {
            return new TreeSet();
        }
        else {
            return new TreeSet(newObjects);
        }
    }
}
