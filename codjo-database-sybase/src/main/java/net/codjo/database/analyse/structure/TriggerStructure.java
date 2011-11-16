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
import java.util.TreeSet;
/**
 */
public class TriggerStructure extends ObjectStructure {
    private List commonObjectNames;
    private Collection refObjectNames;
    private Collection srcObjectNames;

    public void setTriggerNames(boolean isRefBase, List triggerNames) {
        if (isRefBase) {
            refObjectNames = triggerNames;
        }
        else {
            srcObjectNames = triggerNames;
        }
        setCommonTriggerNames();
    }


    public TreeSet getUpdatedObjects() {
        TreeSet updtObjects = new TreeSet();

        if (commonObjectNames != null) {
            addUpdatedObjectsByScript(updtObjects, commonObjectNames);
        }
        return updtObjects;
    }


    private void setCommonTriggerNames() {
        if (srcObjectNames != null && refObjectNames != null) {
            commonObjectNames = new ArrayList(refObjectNames);
            commonObjectNames.retainAll(srcObjectNames);
        }
    }


    public TreeSet getDelObjects() {
        List delObjects = Util.removeElements(refObjectNames, srcObjectNames);
        if (delObjects == null) {
            return new TreeSet();
        }
        else {
            return new TreeSet(delObjects);
        }
    }


    public TreeSet getNewObjects() {
        List newObjects = Util.removeElements(srcObjectNames, refObjectNames);
        if (newObjects == null) {
            return new TreeSet();
        }
        else {
            return new TreeSet(newObjects);
        }
    }
}
