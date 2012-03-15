/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import net.codjo.database.analyse.structure.ColumnsStructure;
import net.codjo.database.analyse.structure.ConstraintsStructure;
import net.codjo.database.analyse.structure.ForeignKeysStructure;
import net.codjo.database.analyse.structure.IndexesStructure;
import net.codjo.database.analyse.structure.ObjectStructure;
import net.codjo.database.analyse.structure.PrimaryKeysStructure;
import net.codjo.database.analyse.structure.PrivilegesStructure;
import net.codjo.database.analyse.structure.ProcedureStructure;
import net.codjo.database.analyse.structure.RulesStructure;
import net.codjo.database.analyse.structure.ScriptsStructure;
import net.codjo.database.analyse.structure.TableStructure;
import net.codjo.database.analyse.structure.TriggerStructure;
import net.codjo.database.analyse.structure.UserDataType;
import net.codjo.database.analyse.structure.ViewStructure;
/**
 */
@SuppressWarnings({"UseOfSystemOutOrSystemErr"})
public class DatabaseComparator {
    private final TableStructure tableStructure = new TableStructure();
    private final RulesStructure rulesStructure = new RulesStructure();
    private final ForeignKeysStructure foreignKeysStructure = new ForeignKeysStructure();
    private final PrimaryKeysStructure primaryKeyStructure = new PrimaryKeysStructure();
    private final IndexesStructure indexesStructure = new IndexesStructure();
    private final ScriptsStructure scriptsStructure = new ScriptsStructure();
    private final ProcedureStructure procedureStructure = new ProcedureStructure();
    private final ViewStructure viewStructure = new ViewStructure();
    private final TriggerStructure triggerStructure = new TriggerStructure();
    private final UserDataType userDataType = new UserDataType();
    private final ConstraintsStructure constraintsStructure = new ConstraintsStructure();
    private ColumnsStructure columnsStructure = new ColumnsStructure();
    private PrivilegesStructure privilegesStructure = new PrivilegesStructure();
    private Collection<String> ignoredTableNames = Collections.emptyList();
    private Collection<String> ignoredProcedureNames = Collections.emptyList();


    public void setIgnoredTableNames(Collection<String> ignoredTableNames) {
        this.ignoredTableNames = ignoredTableNames;
    }


    public void setIgnoredProcedureNames(Collection<String> ignoredProcedureNames) {
        this.ignoredProcedureNames = ignoredProcedureNames;
    }


    public void loadActualStructure(String url, String user, String password, String base, String catalogue)
          throws SQLException, ClassNotFoundException {
        BaseManagerImpl baseManager;
        baseManager = initBaseManager(url, user, password, base, catalogue);

        loadTable(baseManager, false);
    }


    public void loadExpectedStructure(String url, String user, String password, String base, String catalogue)
          throws SQLException, ClassNotFoundException {
        BaseManagerImpl baseManager;
        baseManager = initBaseManager(url, user, password, base, catalogue);

        loadTable(baseManager, true);
    }


    public boolean areDatabasesDifferent() {
        return areDatabasesDifferentImpl();
    }


    private boolean areDatabasesDifferentImpl() {
        boolean tableModified = tableStructure.isModified();
        boolean udtModified = userDataType.isModified();
        boolean procModified = procedureStructure.isModified();
        boolean viewModified = viewStructure.isModified();
        boolean triggerModified = triggerStructure.isModified();

        boolean result = tableModified || udtModified || procModified || viewModified || triggerModified;
        if (result) {
            System.out.println("##############################################################");
            System.out.println("# codjo-database-analyse :                                   #");
            System.out.println("# differences found between database structures              #");
            System.out.println();
            if (tableModified) {
                System.out.println("Tables :");
                dumpList(" - updated : ", tableStructure.getUpdatedObjects());
                drawUpdatedTreeSet(tableStructure.getUpdatedObjects());
                dumpList(" - new     : ", tableStructure.getNewObjects());
                dumpList(" - deleted : ", tableStructure.getDelObjects());
                System.out.println();
            }
            if (udtModified) {
                dumpObjectStructure("User Data Types :", userDataType);
            }
            if (procModified) {
                dumpObjectStructure("Stored procedures :", procedureStructure);
            }
            if (viewModified) {
                dumpObjectStructure("Views :", viewStructure);
            }
            if (triggerModified) {
                dumpObjectStructure("Triggers :", triggerStructure);
            }
            System.out.println("##############################################################");
        }
        return result;
    }


    private void dumpObjectStructure(String header, ObjectStructure objectStructure) {
        System.out.println(header);
        dumpList(" - updated : ", objectStructure.getUpdatedObjects());
        dumpList(" - new     : ", objectStructure.getNewObjects());
        dumpList(" - deleted : ", objectStructure.getDelObjects());
        System.out.println();
    }


    private String drawUpdatedTreeSet(Set<String> treeSet) {
        for (String table : treeSet) {
            //Columns
            dumpUpdatedColumns(table,
                               columnsStructure.getSrcUpdtObjects(),
                               columnsStructure.getRefUpdtObjects());
            // RULES
            dumpList("UpdatedRules : ", tableStructure.getSrcUpdtRules(table),
                     tableStructure.getRefUpdtRules(table));
            // FKS
            dumpList("UpdatedFks : ", tableStructure.getSrcUpdtFks(table),
                     tableStructure.getRefUpdtFks(table));
            // INDEXES
            dumpList("UpdatedIdx : ", tableStructure.getSrcUpdtIndexes(table),
                     tableStructure.getRefUpdtIndexes(table));
            // PKS
            dumpList("UpdatedPks : ", tableStructure.getSrcUpdtPks(table),
                     tableStructure.getRefUpdtPks(table));
            // Checks
            dumpList("UpdatedChecks : ", tableStructure.getSrcUpdtChecks(table),
                     tableStructure.getRefUpdtChecks(table));
        }
        return null;
    }


    private void dumpList(String headerTest, List srcObject, List refObject) {
        if (!srcObject.isEmpty() || !refObject.isEmpty()) {
            dumpList("\t** Src " + headerTest, srcObject);
            dumpList("\t** Ref " + headerTest, refObject);
        }
    }


    private void dumpList(String headerText, Collection list) {
        if (!list.isEmpty()) {
            System.out.println(headerText);
            for (Object obj : list) {
                System.out.println("\t\t" + obj);
            }
        }
    }


    private void loadTable(BaseManager baseManager, boolean destination) throws SQLException {
        JdbcUtil.getAllTables(baseManager,
                              tableStructure,
                              getDatabaseName(baseManager),
                              ignoredTableNames,
                              destination);

        JdbcUtil.getAllTableRulesInfo(baseManager,
                                      tableStructure,
                                      rulesStructure,
                                      getDatabaseName(baseManager),
                                      ignoredTableNames,
                                      destination);

        JdbcUtil.getAllTableConstraintsInfo(baseManager,
                                            getDatabaseName(baseManager),
                                            tableStructure,
                                            constraintsStructure,
                                            ignoredTableNames,
                                            destination);

        JdbcUtil.getAllTableForeignKeysInfo(baseManager,
                                            getDatabaseName(baseManager),
                                            tableStructure,
                                            foreignKeysStructure,
                                            ignoredTableNames,
                                            destination);

        JdbcUtil.getAllTablePrimaryKeysInfo(baseManager,
                                            getDatabaseName(baseManager),
                                            tableStructure,
                                            primaryKeyStructure,
                                            ignoredTableNames,
                                            destination);

        JdbcUtil.getAllTableIndexesInfo(baseManager,
                                        getDatabaseName(baseManager),
                                        tableStructure,
                                        indexesStructure,
                                        ignoredTableNames,
                                        destination);

        JdbcUtil.getAllObjectScriptsInfo(baseManager,
                                         getDatabaseName(baseManager),
                                         scriptsStructure,
                                         destination);

        columnsStructure = JdbcUtil.getAllTableColumnsInfo(baseManager,
                                                           getDatabaseName(baseManager),
                                                           columnsStructure,
                                                           ignoredTableNames,
                                                           destination);

        privilegesStructure = JdbcUtil.getAllObjectPrivilegesInfo(baseManager,
                                                                  getDatabaseName(baseManager),
                                                                  privilegesStructure,
                                                                  destination);

        tableStructure.setColumnsStructure(columnsStructure);
        tableStructure.setPrivilegesStructure(privilegesStructure);

        procedureStructure.setColumnsStructure(columnsStructure);
        procedureStructure.setPrivilegesStructure(privilegesStructure);
        procedureStructure.setScriptsStructure(scriptsStructure);
        JdbcUtil.getAllStroredProcedures(baseManager,
                                         procedureStructure,
                                         getDatabaseName(baseManager),
                                         ignoredProcedureNames,
                                         destination);

        viewStructure.setColumnsStructure(columnsStructure);
        viewStructure.setPrivilegesStructure(privilegesStructure);
        viewStructure.setScriptsStructure(scriptsStructure);

        JdbcUtil.getAllViews(baseManager, getDatabaseName(baseManager), viewStructure, destination);

        triggerStructure.setScriptsStructure(scriptsStructure);
        JdbcUtil.getAllTriggers(baseManager, getDatabaseName(baseManager), triggerStructure, destination);

        JdbcUtil.getAllUserDataTypesInfo(baseManager,
                                         getDatabaseName(baseManager),
                                         userDataType,
                                         destination);
    }


    private void dumpUpdatedColumns(String table,
                                    Map<String, List<List<String>>> srcUpdatedObjects,
                                    Map<String, List<List<String>>> refUpdtObjects) {
        List srcObjects = srcUpdatedObjects.get(table);
        List refObjects = refUpdtObjects.get(table);
        if (srcObjects != null) {
            System.out.println("  -- Colonnes src");
            for (Object srcObject : srcObjects) {
                System.out.println("   " + srcObject);
            }
        }
        if (refObjects != null) {
            System.out.println("  -- Colonnes ref");
            for (Object refObject : refObjects) {
                System.out.println("   " + refObject);
            }
        }
    }


    private BaseManagerImpl initBaseManager(String url, String user, String password,
                                            String base, String catalogue) throws ClassNotFoundException {
        BaseManagerImpl baseManager;
        baseManager = new BaseManagerImpl(url, user, password, base, catalogue);
        return baseManager;
    }


    private String getDatabaseName(BaseManager baseManager) {
        return ((TreeSet)baseManager.getBases()).first().toString();
    }
}
