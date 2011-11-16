/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import java.io.IOException;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;
/**
 */
public class MockBaseManager implements BaseManager {
    private final DatabaseHelper databaseHelper = new DatabaseFactory().createDatabaseHelper();
    private final ConnectionMetadata connectionMetadata = databaseHelper.createLibraryConnectionMetadata();


    public void load()
          throws ParserConfigurationException, SAXException, IOException,
                 ClassNotFoundException {
        initJDBCDriver();
    }


    protected void initJDBCDriver() throws ClassNotFoundException {
        new com.sybase.jdbc2.jdbc.SybDriver();
        Class.forName(getDriver());
    }


    public void deleteParamFile() {
        //To change body of implemented methods use File | Settings | File Templates.
    }


    public String getDriver() {
        return databaseHelper.getDriverClassName();
    }


    public Set<String> getBases() {
        Set<String> baseTreeSet = new TreeSet<String>();
        baseTreeSet.add(connectionMetadata.getBase());
        return baseTreeSet;
    }


    public String getPassWord(String aliasName) {
        return connectionMetadata.getPassword();
    }


    public String getUrl(String aliasName) {
        return String.format("%s/%s",
                             databaseHelper.getConnectionUrl(connectionMetadata),
                             connectionMetadata.getBase());
    }


    public String getLoggin(String aliasName) {
        return connectionMetadata.getUser();
    }


    public String getCatalog(String aliasName) {
        return connectionMetadata.getCatalog();
    }


    public String getBase(String aliasName) {
        return connectionMetadata.getBase();
    }
}
