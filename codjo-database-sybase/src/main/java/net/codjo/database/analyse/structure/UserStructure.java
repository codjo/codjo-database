/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import java.util.ArrayList;
import java.util.List;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.1.1.1 $
 */
public class UserStructure {
    private String userName;
    private String groupName;
    private String loginName;

    public List getColumnInfos() {
        List infos = new ArrayList();
        infos.add(userName);
        infos.add(groupName);
        infos.add(loginName);
        return infos;
    }


    public String getUserName() {
        return userName;
    }


    public String getGroupName() {
        return groupName;
    }


    public String getLoginName() {
        return loginName;
    }


    public void setUserName(String userName) {
        this.userName = userName;
    }


    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }


    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }
}
