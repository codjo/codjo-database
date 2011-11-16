/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.common.api.ConnectionMetadata;
import net.codjo.database.common.api.DatabaseFactory;
import net.codjo.database.common.api.DatabaseHelper;
import java.sql.DriverManager;
import java.sql.SQLException;
import junit.framework.TestCase;
/**
 * Classe de test de {@link BaseManagerImpl}.
 */
public class BaseManagerImplTest extends TestCase {
    private final DatabaseHelper databaseHelper = new DatabaseFactory().createDatabaseHelper();
    private final ConnectionMetadata connectionMetadata = databaseHelper.createLibraryConnectionMetadata();


    public void test_loadDriver() throws ClassNotFoundException, SQLException {
        BaseManagerImpl baseManager =
              new BaseManagerImpl(String.format("%s/%s",
                                                databaseHelper.getConnectionUrl(connectionMetadata),
                                                connectionMetadata.getBase()),
                                  connectionMetadata.getUser(),
                                  connectionMetadata.getPassword(),
                                  connectionMetadata.getBase(),
                                  connectionMetadata.getCatalog());
        DriverManager.getConnection(baseManager.getUrl(""), baseManager.getLoggin(""),
                                    baseManager.getPassWord(""));
    }
}
