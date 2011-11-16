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
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public class UsersStructure extends ObjectStructure {
    private List commonObjectNames = new ArrayList();
    private TreeSet updtObjectNames = new TreeSet();
    private Map refObjectNames;
    private Map srcObjectNames;


    public List getNewUserInfos(String userName) {
        List newUsers = new ArrayList();
        if (!commonObjectNames.contains(userName)) {
            newUsers.add(((UserStructure)srcObjectNames.get(userName)).getColumnInfos());
        }
        return newUsers;
    }


    public List getDelUserInfos(String userName) {
        List delUsers = new ArrayList();
        if (!commonObjectNames.contains(userName)) {
            delUsers.add(((UserStructure)refObjectNames.get(userName)).getColumnInfos());
        }
        return delUsers;
    }


    public List getRefUpdtUsers(String userName) {
        List refUsers = new ArrayList();
        refUsers.add(((UserStructure)refObjectNames.get(userName)).getColumnInfos());
        return refUsers;
    }


    public List getSrcUpdtUsers(String userName) {
        List srcUsers = new ArrayList();
        srcUsers.add(((UserStructure)srcObjectNames.get(userName)).getColumnInfos());
        return srcUsers;
    }


    public void setUserNames(boolean isRefBase, Map udtInfos) {
        if (isRefBase) {
            refObjectNames = udtInfos;
        }
        else {
            srcObjectNames = udtInfos;
        }
        setCommonUserNames();
    }


    private void setCommonUserNames() {
        if (srcObjectNames != null && refObjectNames != null) {
            commonObjectNames = new ArrayList(refObjectNames.keySet());
            commonObjectNames.retainAll(srcObjectNames.keySet());
        }
    }


    @Override
    public TreeSet getUpdatedObjects() {
        findUserDifferences();
        return updtObjectNames;
    }


    private void findUserDifferences() {
        for (int i = 0; i < commonObjectNames.size(); i++) {
            String tableName = (String)commonObjectNames.get(i);
            UserStructure srcCols = (UserStructure)srcObjectNames.get(tableName);
            UserStructure refCols = (UserStructure)refObjectNames.get(tableName);

            if (!isSameUserStructure(refCols, srcCols)) {
                updtObjectNames.add(tableName);
            }
        }
    }


    private boolean isSameUserStructure(UserStructure refColConfigs,
                                        UserStructure srcColConfigs) {
        if (!Util.isEqual(refColConfigs.getGroupName(), srcColConfigs.getGroupName())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getLoginName(), srcColConfigs.getLoginName())) {
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
