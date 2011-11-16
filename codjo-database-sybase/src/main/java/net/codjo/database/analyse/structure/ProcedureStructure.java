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
import java.util.Set;
import java.util.TreeSet;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public class ProcedureStructure extends ObjectStructure {
    private List<String> commonObjectNames;
    private Collection<String> refObjectNames;
    private Collection<String> srcObjectNames;


    @Override
    public Set<String> getUpdatedObjects() {
        Set<String> updtObjects = new TreeSet<String>();
        if (commonObjectNames != null) {
            addUpdatedObjectsByColumns(updtObjects, commonObjectNames);
            addUpdatedObjectsByPrivileges(updtObjects, commonObjectNames);
            addUpdatedObjectsByScript(updtObjects, commonObjectNames);
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


    public void setProcedureNames(boolean isRefBase, List<String> viewNames) {
        if (isRefBase) {
            refObjectNames = viewNames;
        }
        else {
            srcObjectNames = viewNames;
        }
        setCommonProcedureNames();
    }


    private void setCommonProcedureNames() {
        if (srcObjectNames != null && refObjectNames != null) {
            commonObjectNames = new ArrayList<String>(refObjectNames);
            commonObjectNames.retainAll(srcObjectNames);
        }
    }
}
