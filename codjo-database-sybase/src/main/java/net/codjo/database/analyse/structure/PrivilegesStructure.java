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
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public class PrivilegesStructure extends ObjectsStructure<PrivilegeStructure> {
    protected Set<String> getUpdtTables(List<String> commonTableNames) {
        findPrivilegeDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List getDelTablePrivilegeInfos(String tableName) {
        return getTablePrivilegeInfos(getRefTableObjectInfos(tableName));
    }


    protected List getNewTablePrivilegeInfos(String tableName) {
        return getTablePrivilegeInfos(getSrcTableObjectInfos(tableName));
    }


    private List getTablePrivilegeInfos(Map tablePrivilegeInfos) {
        List infos = new ArrayList();
        if (tablePrivilegeInfos == null) {
            return infos;
        }
        for (Iterator iterator = tablePrivilegeInfos.values().iterator();
             iterator.hasNext();) {
            PrivilegeStructure privilegeStructure = (PrivilegeStructure)iterator.next();
            infos.add(privilegeStructure.getPrivilegeInfos());
        }
        return infos;
    }


    protected void findPrivilegeDifferences(List commonTableNames) {
        for (int i = 0; i < commonTableNames.size(); i++) {
            String tableName = (String)commonTableNames.get(i);
            Map srcPrivileges = getSrcTableObjectInfos(tableName);
            Map refPrivileges = getRefTableObjectInfos(tableName);
            List addPrivileges;
            List deletePrivileges;
            if (refPrivileges != null && srcPrivileges == null) {
                deletePrivileges = new ArrayList(refPrivileges.keySet());
                fillPrivilegeInfos(deletePrivileges, refPrivileges, tableName,
                                   getDelObjects());
            }
            if (refPrivileges == null && srcPrivileges != null) {
                addPrivileges = new ArrayList(srcPrivileges.keySet());
                fillPrivilegeInfos(addPrivileges, srcPrivileges, tableName,
                                   getNewObjects());
            }
            if (refPrivileges != null && srcPrivileges != null) {
                deletePrivileges =
                      Util.removeElements(refPrivileges.keySet(), srcPrivileges.keySet());
                fillPrivilegeInfos(deletePrivileges, refPrivileges, tableName,
                                   getDelObjects());

                addPrivileges =
                      Util.removeElements(srcPrivileges.keySet(), refPrivileges.keySet());
                fillPrivilegeInfos(addPrivileges, srcPrivileges, tableName,
                                   getNewObjects());

                List commonPrivilegeNames = new ArrayList(refPrivileges.keySet());
                commonPrivilegeNames.retainAll(srcPrivileges.keySet());
                findUpdatedPrivileges(commonPrivilegeNames, refPrivileges, srcPrivileges,
                                      tableName);
            }
        }
    }


    private void findUpdatedPrivileges(List commonPrivilegeNames, Map refPrivileges,
                                       Map srcPrivileges, String tableName) {
        List refInfos = new ArrayList();
        List srcInfos = new ArrayList();
        for (int j = 0; j < commonPrivilegeNames.size(); j++) {
            String privilegeName = (String)commonPrivilegeNames.get(j);
            PrivilegeStructure refPrivilegeConfigs =
                  (PrivilegeStructure)refPrivileges.get(privilegeName);
            PrivilegeStructure srcPrivilegeConfigs =
                  (PrivilegeStructure)srcPrivileges.get(privilegeName);
            if (!isSamePkStructure(refPrivilegeConfigs, srcPrivilegeConfigs)) {
                refInfos.add(refPrivilegeConfigs.getPrivilegeInfos());
                srcInfos.add(srcPrivilegeConfigs.getPrivilegeInfos());
            }
        }
        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void fillPrivilegeInfos(List delPrivileges, Map refPrivileges,
                                    String tableName, Map privilegeInfos) {
        if (delPrivileges.size() != 0) {
            List infos = new ArrayList();
            for (int j = 0; j < delPrivileges.size(); j++) {
                String privilegeName = (String)delPrivileges.get(j);
                infos.add(((PrivilegeStructure)refPrivileges.get(privilegeName))
                      .getPrivilegeInfos());
            }
            privilegeInfos.put(tableName, infos);
        }
    }


    private boolean isSamePkStructure(PrivilegeStructure refPrivilegeConfigs,
                                      PrivilegeStructure srcPrivilegeConfigs) {
        if (!Util.isEqual(refPrivilegeConfigs.getGroup(), srcPrivilegeConfigs.getGroup())) {
            return false;
        }
        if (!Util.isEqual(refPrivilegeConfigs.getType(), srcPrivilegeConfigs.getType())) {
            return false;
        }
        if (!Util.isEqual(refPrivilegeConfigs.getAction(), srcPrivilegeConfigs.getAction())) {
            return false;
        }
        if (!Util.isEqual(refPrivilegeConfigs.getColumn(), srcPrivilegeConfigs.getColumn())) {
            return false;
        }
        return true;
    }
}
