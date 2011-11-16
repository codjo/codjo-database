/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import java.util.Set;
import java.util.TreeSet;
/**
 */
class BaseManagerImpl implements BaseManager {
    private Set bases = new TreeSet();
    private String catalog;
    private String password;
    private String url;
    private String login;

    BaseManagerImpl(String url, String login, String password, String bases,
        String catalog) throws ClassNotFoundException {
        this.bases.add(bases);
        this.catalog = catalog;
        this.password = password;
        this.url = url;
        this.login = login;
        Class.forName(getDriver());
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }


    public void setPassword(String password) {
        this.password = password;
    }


    public void setUrl(String url) {
        this.url = url;
    }


    public void setLogin(String login) {
        this.login = login;
    }


    public String getDriver() {
        return "com.sybase.jdbc2.jdbc.SybDriver";
    }


    public Set getBases() {
        return bases;
    }


    public String getPassWord(String aliasName) {
        return password;
    }


    public String getUrl(String aliasName) {
        return url;
    }


    public String getLoggin(String aliasName) {
        return login;
    }


    public String getCatalog(String aliasName) {
        return catalog;
    }


    public String getBase(String aliasName) {
        // Not yet implemented
        return null;
    }


    public void addBases(String base) {
        bases.add(base);
    }
}
