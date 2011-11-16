/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import java.util.Set;
/**
 * Classe permettant de gérer le paramétrage des bases.
 */
public interface BaseManager {
    public String getDriver();


    public Set getBases();


    public String getPassWord(String aliasName);


    public String getUrl(String aliasName);


    public String getLoggin(String aliasName);


    public String getCatalog(String aliasName);


    public String getBase(String aliasName);
}
