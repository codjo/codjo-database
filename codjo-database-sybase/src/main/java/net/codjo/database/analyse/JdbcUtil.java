/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse;
import net.codjo.database.analyse.structure.ColumnStructure;
import net.codjo.database.analyse.structure.ColumnsStructure;
import net.codjo.database.analyse.structure.ConstraintStructure;
import net.codjo.database.analyse.structure.ConstraintsStructure;
import net.codjo.database.analyse.structure.ForeignKeyStructure;
import net.codjo.database.analyse.structure.ForeignKeysStructure;
import net.codjo.database.analyse.structure.IndexStructure;
import net.codjo.database.analyse.structure.IndexesStructure;
import net.codjo.database.analyse.structure.PrimaryKeyStructure;
import net.codjo.database.analyse.structure.PrimaryKeysStructure;
import net.codjo.database.analyse.structure.PrivilegeStructure;
import net.codjo.database.analyse.structure.PrivilegesStructure;
import net.codjo.database.analyse.structure.ProcedureStructure;
import net.codjo.database.analyse.structure.RuleStructure;
import net.codjo.database.analyse.structure.RulesStructure;
import net.codjo.database.analyse.structure.ScriptStructure;
import net.codjo.database.analyse.structure.ScriptsStructure;
import net.codjo.database.analyse.structure.TableStructure;
import net.codjo.database.analyse.structure.TriggerStructure;
import net.codjo.database.analyse.structure.UdtStructure;
import net.codjo.database.analyse.structure.UserDataType;
import net.codjo.database.analyse.structure.UserStructure;
import net.codjo.database.analyse.structure.UsersStructure;
import net.codjo.database.analyse.structure.ViewStructure;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import org.apache.log4j.Logger;
/**
 * Classe utilitaire pour les requêtes JDBC.
 */
public final class JdbcUtil {
    public static final Logger LOGGER = Logger.getLogger("global");
    private static final String GRANTEE = "grantee";
    private static final String TYPE = "type";
    private static final String ACTION = "action";
    private static final String COLUMN = "column";
    private static final String TABLE_NAME = "Table_name";


    private JdbcUtil() {
    }


    /**
     * Drop une table temporaire.
     *
     * @param con       La connexion
     * @param tableName La table à dropper
     */
    public static void dropTable(Connection con, String tableName) {
        doDrop(con, "drop table " + tableName);
    }


    /**
     * Drop une table temporaire.
     *
     * @param con       La connexion
     * @param catalog   Le catalogue de sa création
     * @param tableName La table à dropper
     */
    public static void dropTable(Connection con, String catalog, String tableName) {
        doDrop(con, "drop table " + catalog + ".." + tableName);
    }


    /**
     * Lance le drop d'une table
     *
     * @param con     La connexion
     * @param request La requête de drop
     */
    private static void doDrop(Connection con, String request) {
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.execute(request);
        }
        catch (SQLException ex) {
            ; // Erreur sans incidence
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }
            catch (SQLException e) {
                ; // Erreur sans incidence
            }
        }
    }


    /**
     * Lance le create d'un objet sybase
     *
     * @param con     La connexion
     * @param request La requête de drop
     */
    public static void doUpdate(Connection con, String request) {
        Statement stmt = null;

        try {
            stmt = con.createStatement();
            stmt.executeUpdate(request);
        }
        catch (SQLException ex) {
            ; // Erreur sans incidence
        }
        finally {
            try {
                if (stmt != null) {
                    stmt.close();
                }
            }
            catch (SQLException e) {
                ; // Erreur sans incidence
            }
        }
    }


    public static void getAllTables(BaseManager baseManager,
                                    TableStructure tableStructure,
                                    String refBaseName,
                                    String srcBaseName) throws SQLException {
        getAllTables(baseManager, tableStructure, refBaseName, srcBaseName, Collections.<String>emptyList());
    }


    public static void getAllTables(BaseManager baseManager,
                                    TableStructure tableStructure,
                                    String refBaseName,
                                    String srcBaseName,
                                    Collection<String> ignoredTableNames) throws SQLException {
        getAllTables(baseManager, tableStructure, refBaseName, ignoredTableNames, true);
        getAllTables(baseManager, tableStructure, srcBaseName, ignoredTableNames, false);
    }


    public static void getAllTables(BaseManager baseManager,
                                    TableStructure tableStructure,
                                    String baseName,
                                    boolean isRefBase) throws SQLException {
        getAllTables(baseManager, tableStructure, baseName, Collections.<String>emptyList(), isRefBase);
    }


    public static void getAllTables(BaseManager baseManager,
                                    TableStructure tableStructure,
                                    String baseName,
                                    Collection<String> ignoredTableNames,
                                    boolean isRefBase) throws SQLException {
        List<String> tablesList = new ArrayList<String>();
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();

            ResultSet rs =
                  stmt.executeQuery(
                        "select name from sysobjects where type='U' order by name");

            while (nextIgnoringFieldValue(rs, "name", ignoredTableNames)) {
                tablesList.add(rs.getString("name"));
            }
            tableStructure.setTableNames(isRefBase, tablesList);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    public static void getAllViews(BaseManager baseManager,
                                   ViewStructure viewStructure,
                                   String refBaseName,
                                   String srcBaseName) throws SQLException {
        getAllViews(baseManager, refBaseName, viewStructure, true);
        getAllViews(baseManager, srcBaseName, viewStructure, false);
    }


    public static List<String> getAllViews(BaseManager baseManager,
                                           String baseName,
                                           ViewStructure viewStructure,
                                           boolean isRefBase) throws SQLException {
        List<String> viewsList = new ArrayList<String>();
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection(baseManager, baseName);
            stmt = con.createStatement();

            ResultSet rs =
                  stmt.executeQuery(
                        "select name from sysobjects where type='V' order by name");

            while (rs.next()) {
                viewsList.add(rs.getString(1));
            }
            viewStructure.setViewNames(isRefBase, viewsList);
        }
        finally {
            releaseConnection(con, stmt);
        }

        return viewsList;
    }


    public static void getAllStroredProcedures(BaseManager baseManager,
                                               ProcedureStructure procedureStructure,
                                               String refBaseName,
                                               String srcBaseName) throws SQLException {
        getAllStroredProcedures(baseManager,
                                procedureStructure,
                                refBaseName,
                                srcBaseName,
                                Collections.<String>emptyList());
    }


    public static void getAllStroredProcedures(BaseManager baseManager,
                                               ProcedureStructure procedureStructure,
                                               String refBaseName,
                                               String srcBaseName,
                                               Collection<String> ignoredProcedureNames) throws SQLException {
        getAllStroredProcedures(baseManager, procedureStructure, refBaseName, ignoredProcedureNames, true);
        getAllStroredProcedures(baseManager, procedureStructure, srcBaseName, ignoredProcedureNames, false);
    }


    public static List<String> getAllStroredProcedures(BaseManager baseManager,
                                                       ProcedureStructure procedureStructure,
                                                       String baseName,
                                                       boolean isRefBase) throws SQLException {

        return getAllStroredProcedures(baseManager,
                                       procedureStructure,
                                       baseName,
                                       Collections.<String>emptyList(),
                                       isRefBase);
    }


    public static List<String> getAllStroredProcedures(BaseManager baseManager,
                                                       ProcedureStructure procedureStructure,
                                                       String baseName,
                                                       Collection<String> ignoredProcedureNames,
                                                       boolean isRefBase) throws SQLException {
        List<String> procsList = new ArrayList<String>();
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery(
                        "select name from sysobjects where type='P' and name not like 'sp_dba%' order by name");

            while (nextIgnoringFieldValue(rs, "name", ignoredProcedureNames)) {
                procsList.add(rs.getString("name"));
            }
            procedureStructure.setProcedureNames(isRefBase, procsList);
        }
        finally {
            releaseConnection(con, stmt);
        }

        return procsList;
    }


    public static void getAllTriggers(BaseManager baseManager,
                                      TriggerStructure triggerStructure,
                                      String refBaseName,
                                      String srcBaseName)
          throws SQLException {
        getAllTriggers(baseManager, refBaseName, triggerStructure, true);
        getAllTriggers(baseManager, srcBaseName, triggerStructure, false);
    }


    public static List<String> getAllTriggers(BaseManager baseManager,
                                              String baseName,
                                              TriggerStructure triggerStructure,
                                              boolean isRefBase) throws SQLException {
        List<String> procsList = new ArrayList<String>();
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection(baseManager, baseName);
            stmt = con.createStatement();

            ResultSet rs =
                  stmt.executeQuery(
                        "select name from sysobjects where type='TR' order by name");

            while (rs.next()) {
                procsList.add(rs.getString(1));
            }
            triggerStructure.setTriggerNames(isRefBase, procsList);
        }
        finally {
            releaseConnection(con, stmt);
        }

        return procsList;
    }


    public static ScriptsStructure getAllObjectScriptsInfo(BaseManager baseManager,
                                                           String refBaseName, String srcBaseName)
          throws SQLException {
        ScriptsStructure scriptsStructure = new ScriptsStructure();
        getAllObjectScriptsInfo(baseManager, refBaseName, scriptsStructure, true);
        getAllObjectScriptsInfo(baseManager, srcBaseName, scriptsStructure, false);
        return scriptsStructure;
    }


    public static ScriptsStructure getAllObjectScriptsInfo(BaseManager baseManager,
                                                           String baseName,
                                                           ScriptsStructure scriptsStructure,
                                                           boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select name, text                                              "
                                    + " from syscomments c                                          "
                                    + "     inner join sysobjects o                                 "
                                    + "         on (c.id = o.id)                                    "
                                    + " where type in ('P', 'V', 'TR')                              "
                                    + " order by name, colid");

            Map<String, ScriptStructure> objectScriptNames = new HashMap<String, ScriptStructure>();
            String oldObjectName = "";
            ScriptStructure scriptStructure = new ScriptStructure();
            StringBuffer text = new StringBuffer();
            while (rs.next()) {
                String objectName = rs.getString("name");
                if (!"".equals(oldObjectName) && !oldObjectName.equals(objectName)) {
                    scriptStructure.setScript(Util.replace(text.toString(), "\t", "    "));
                    objectScriptNames.put(oldObjectName, scriptStructure);
                    scriptStructure = new ScriptStructure();
                    text = new StringBuffer();
                }
                text.append(rs.getString("text"));
                oldObjectName = objectName;
            }
            scriptStructure.setScript(Util.replace(text.toString(), "\t", "    "));
            objectScriptNames.put(oldObjectName, scriptStructure);
            scriptsStructure.setProcedureScriptInfos(isRefBase, objectScriptNames);
        }
        finally {
            releaseConnection(con, stmt);
        }

        return scriptsStructure;
    }


    public static ColumnsStructure getAllTableColumnsInfo(BaseManager baseManager,
                                                          String refBaseName,
                                                          String srcBaseName) throws SQLException {
        return getAllTableColumnsInfo(baseManager, refBaseName, srcBaseName, Collections.<String>emptyList());
    }


    public static ColumnsStructure getAllTableColumnsInfo(BaseManager baseManager,
                                                          String refBaseName,
                                                          String srcBaseName,
                                                          Collection<String> ignoredTableNames)
          throws SQLException {
        ColumnsStructure columnsStructure = new ColumnsStructure();
        getAllTableColumnsInfo(baseManager, refBaseName, columnsStructure, ignoredTableNames, true);
        getAllTableColumnsInfo(baseManager, srcBaseName, columnsStructure, ignoredTableNames, false);
        return columnsStructure;
    }


    public static ColumnsStructure getAllTableColumnsInfo(BaseManager baseManager,
                                                          String baseName,
                                                          ColumnsStructure columnsStructure,
                                                          boolean isRefBase) throws SQLException {
        return getAllTableColumnsInfo(baseManager,
                                      baseName,
                                      columnsStructure,
                                      Collections.<String>emptyList(),
                                      isRefBase);
    }


    public static ColumnsStructure getAllTableColumnsInfo(BaseManager baseManager,
                                                          String baseName,
                                                          ColumnsStructure columnsStructure,
                                                          Collection<String> ignoredTableNames,
                                                          boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                                   "
                                    + " Column_position = colid,                            "
                                    + " Table_name = o.name,                                "
                                    + " Column_name = isnull(c.name, null),                 "
                                    + " Type = isnull(x.xtname, isnull(get_xtypename(c.xtype, c.xdbid), t.name)),"
                                    + " Length = case when                                  "
                                    + "         (t.type = 47 and  t.usertype = 24)          "
                                    + "         or (t.type = 39 and  t.usertype = 25)       "
                                    + "            or (t.type in (select type from systypes where name in ('unichar', 'univarchar')))"
                                    + "        then c.length / @@unicharsize                   "
                                    + "        else c.length                                   "
                                    + "        end,                                            "
                                    + " Prec = c.prec,                                      "
                                    + " Scale = c.scale,                                    "
                                    + " Nulls = convert(bit, (c.status & 8)),               "
                                    + " Default_value = case when                           "
                                    + "         object_name(c.cdefault) is not null         "
                                    + "        then (select text from syscomments where id =c.cdefault)"
                                    + "        else null                                       "
                                    + "        end,                                            "
                                    + " Rule_name = object_name(c.domain),                  "
                                    + " Ident = convert(bit, (c.status & 0x80)),            "
                                    + " Gap = case when                                     "
                                    + "         convert(bit, (c.status & 0x80)) = 1         "
                                    + "        then (select isnull(identitygap, 0) from sysindexes where id = o.id and indid <= 1)"
                                    + "        else null                                       "
                                    + "        end                                             "
                                    + " from syscolumns c                                   "
                                    + "        inner join sysobjects o                         "
                                    + "            on (c.id = o.id)                            "
                                    + "        left join systypes t                            "
                                    + "            on (c.usertype = t.usertype)                "
                                    + "        left join sysxtypes x                           "
                                    + "            on (c.xtype = x.xtid)                       "
                                    + " order by o.name, c.colid");

            Map<String, Map<String, ColumnStructure>> tableColumnNames
                  = new HashMap<String, Map<String, ColumnStructure>>();
            Map<String, ColumnStructure> colInfos = new HashMap<String, ColumnStructure>();
            String oldTableName = "";
            ColumnStructure columnStructure;
            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tableColumnNames.put(oldTableName, colInfos);
                    colInfos = new HashMap<String, ColumnStructure>();
                }
                columnStructure = new ColumnStructure();
                columnStructure.setColumnPosition(rs.getInt("Column_position"));
                columnStructure.setColumnName(rs.getString("Column_name"));
                columnStructure.setColumnType(rs.getString("Type").trim());
                columnStructure.setLength(rs.getInt("Length"));
                int prec = rs.getInt("Prec");
                int scale = rs.getInt("Scale");
                columnStructure.setPrecision((prec == 0) ? "" : Integer.toString(prec));
                columnStructure.setScale((scale == 0 && prec == 0) ? ""
                                                                   : Integer.toString(
                      scale));
                columnStructure.setAllowNulls(toBoolean(rs, "Nulls"));
                String defaultValue = rs.getString("Default_value");

                String newDefaultValue = "";
                if (defaultValue != null) {
                    newDefaultValue = Util.replace(defaultValue, "DEFAULT", "");
                    int beginIndex = newDefaultValue.indexOf(" as ");
                    if (beginIndex > -1) {
                        newDefaultValue = newDefaultValue.substring(beginIndex + 4);
                    }
                    newDefaultValue = newDefaultValue.trim();
                }

                columnStructure.setDefaultValue(newDefaultValue);
                columnStructure.setRuleName(rs.getString("Rule_name"));
                columnStructure.setIdentity(toBoolean(rs, "Ident"));
                columnStructure.setIdentityGap(Integer.toString(rs.getInt("Gap")));
                colInfos.put(rs.getString("Column_name"), columnStructure);
                oldTableName = tableName;
            }
            tableColumnNames.put(oldTableName, colInfos);
            columnsStructure.setTableObjectInfos(isRefBase, tableColumnNames);
        }
        finally {
            releaseConnection(con, stmt);
        }
        return columnsStructure;
    }


    public static PrivilegesStructure getAllObjectPrivilegesInfo(
          BaseManager baseManager,
          String refBaseName,
          String srcBaseName) throws SQLException {
        PrivilegesStructure privilegesStructure = new PrivilegesStructure();
        getAllObjectPrivilegesInfo(baseManager, refBaseName, privilegesStructure, true);
        getAllObjectPrivilegesInfo(baseManager, srcBaseName, privilegesStructure, false);
        return privilegesStructure;
    }


    public static PrivilegesStructure getAllObjectPrivilegesInfo(BaseManager baseManager,
                                                                 String baseName,
                                                                 PrivilegesStructure privilegesStructure,
                                                                 boolean isRefBase) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;

        try {
            con = getConnection(baseManager, baseName);
            cstmt = con.prepareCall("{call sp_helprotect}");
            ResultSet rs = cstmt.executeQuery();

            Set<String> tableNames = new TreeSet<String>();
            Map<String, PrivilegeStructure> privilegeInfos = new HashMap<String, PrivilegeStructure>();
            PrivilegeStructure privilegeStructure;
            while (rs.next()) {
                String objectName = rs.getString("object");
                tableNames.add(objectName);
                privilegeStructure = new PrivilegeStructure();
                privilegeStructure.setGroup(rs.getString(GRANTEE));
                privilegeStructure.setType(rs.getString(TYPE));
                privilegeStructure.setAction(rs.getString(ACTION));
                privilegeStructure.setColumn(rs.getString(COLUMN));
                privilegeInfos.put(objectName + "$" + rs.getString(GRANTEE) + "$"
                                   + rs.getString(ACTION) + "$" + rs.getString(COLUMN),
                                   privilegeStructure);
            }

            Map<String, Map<String, PrivilegeStructure>> tablePrivilegeNames
                  = new HashMap<String, Map<String, PrivilegeStructure>>();
            for (String tableName : tableNames) {
                Map<String, PrivilegeStructure> privInfo = new HashMap<String, PrivilegeStructure>();
                for (String key : privilegeInfos.keySet()) {
                    if ((key.substring(0, key.indexOf("$"))).equals(tableName)) {
                        privInfo.put(key, privilegeInfos.get(key));
                    }
                }
                tablePrivilegeNames.put(tableName, privInfo);
            }
            privilegesStructure.setTableObjectInfos(isRefBase, tablePrivilegeNames);
        }
        finally {
            releaseConnection(con, cstmt);
        }
        return privilegesStructure;
    }


    public static void getAllUserDataTypesInfo(BaseManager baseManager,
                                               UserDataType userDataType,
                                               String refBaseName,
                                               String srcBaseName)
          throws SQLException {
        getAllUserDataTypesInfo(baseManager, refBaseName, userDataType, true);
        getAllUserDataTypesInfo(baseManager, srcBaseName, userDataType, false);
    }


    public static void getAllUserDataTypesInfo(BaseManager baseManager, String baseName,
                                               UserDataType userDataType, boolean isRefBase)
          throws SQLException {
        Connection con = null;
        Statement stmt = null;

        try {
            con = getConnection(baseManager, baseName);
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                                     "
                                    + "s1.name,                                               "
                                    + "type = s2.name,                                        "
                                    + "length = case when s2.name in ('unichar', 'univarchar')"
                                    + "          then s1.length/@@unicharsize else s1.length end, "
                                    + "s1.prec,                                               "
                                    + "s1.scale,                                              "
                                    + "s1.allownulls,                                         "
                                    + "Default_name = object_name(s1.tdefault),               "
                                    + "Rule_name = object_name(s1.domain),                    "
                                    + "s1.ident                                               "
                                    + "from systypes s1 inner join systypes s2                "
                                    + "on (s1.type = s2.type)                                 "
                                    + "where s1.usertype >= 100                               "
                                    + "and s2.usertype < 100                                  "
                                    + "and s2.name not in ('sysname', 'nchar', 'nvarchar')    "
                                    + "order by s1.name");

            Map<String, UdtStructure> udtInfos = new HashMap<String, UdtStructure>();
            UdtStructure udtStructure;
            while (rs.next()) {
                udtStructure = new UdtStructure();
                String udtName = rs.getString("name");
                udtStructure.setUdtName(udtName);
                udtStructure.setUdtType(rs.getString(TYPE).trim());
                udtStructure.setLength(rs.getInt("length"));
                int prec = rs.getInt("prec");
                int scale = rs.getInt("scale");
                udtStructure.setPrecision((prec == 0) ? "" : Integer.toString(prec));
                udtStructure.setScale((scale == 0 && prec == 0) ? ""
                                                                : Integer.toString(scale));
                udtStructure.setAllowNulls(toBoolean(rs, "allownulls"));
                udtStructure.setDefaultName(rs.getString("Default_name"));
                udtStructure.setRuleName(rs.getString("Rule_name"));
                udtStructure.setIdentity(toBoolean(rs, "ident"));
                udtInfos.put(udtName, udtStructure);
            }
            userDataType.setUdtNames(isRefBase, udtInfos);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    public static void getAllTableIndexesInfo(BaseManager baseManager,
                                              TableStructure tableStructure,
                                              String refBaseName,
                                              String srcBaseName) throws SQLException {
        getAllTableIndexesInfo(baseManager,
                               tableStructure,
                               refBaseName,
                               srcBaseName,
                               Collections.<String>emptyList());
    }


    public static void getAllTableIndexesInfo(BaseManager baseManager,
                                              TableStructure tableStructure,
                                              String refBaseName,
                                              String srcBaseName,
                                              Collection<String> ignoredTableNames) throws SQLException {
        IndexesStructure indexesStructure = new IndexesStructure();
        getAllTableIndexesInfo(baseManager,
                               refBaseName,
                               tableStructure,
                               indexesStructure,
                               ignoredTableNames,
                               true);
        getAllTableIndexesInfo(baseManager,
                               srcBaseName,
                               tableStructure,
                               indexesStructure,
                               ignoredTableNames,
                               false);
    }


    public static void getAllTableIndexesInfo(BaseManager baseManager,
                                              String baseName,
                                              TableStructure tableStructure,
                                              IndexesStructure indexesStructure,
                                              boolean isRefBase) throws SQLException {
        getAllTableIndexesInfo(baseManager,
                               baseName,
                               tableStructure,
                               indexesStructure,
                               Collections.<String>emptyList(),
                               isRefBase);
    }


    public static void getAllTableIndexesInfo(BaseManager baseManager,
                                              String baseName,
                                              TableStructure tableStructure,
                                              IndexesStructure indexesStructure,
                                              Collection<String> ignoredTableNames,
                                              boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                                        "
                                    + " Table_name = object_name(o.id),                          "
                                    + " Index_name = i.name," + " Column_names = "
                                    + isNullColumnName("1") + " + ', ' +                         "
                                    + isNullColumnName("2") + " + ', ' +                         "
                                    + isNullColumnName("3") + " + ', ' +                         "
                                    + isNullColumnName("4") + " + ', ' +                         "
                                    + isNullColumnName("5") + " + ', ' +                         "
                                    + isNullColumnName("6") + " + ', ' +                         "
                                    + isNullColumnName("7") + " + ', ' +                         "
                                    + isNullColumnName("8") + " + ', ' +                         "
                                    + isNullColumnName("9") + " + ', ' +                         "
                                    + isNullColumnName("10") + " + ', ' +                        "
                                    + isNullColumnName("11") + " + ', ' +                        "
                                    + isNullColumnName("12") + " + ', ' +                        "
                                    + isNullColumnName("13") + " + ', ' +                        "
                                    + isNullColumnName("14") + " + ', ' +                        "
                                    + isNullColumnName("15") + " + ', ' +                        "
                                    + isNullColumnName("16") + ", "
                                    + " Definition = case when (indid = 1 or (indid > 1 and status2 & 512 = 512))"
                                    + "                    then 'clustered'                           "
                                    + "                    else 'nonclustered'                        "
                                    + "                end                                            "
                                    + "                + ', ' + isnull((select v.name from master.dbo.spt_values v"
                                    + "                                where i.status & v.number = v.number "
                                    + "                                      and v.type = 'I' and v.number = 2), '*')"
                                    + "             + ', ' + isnull((select case when i.status & v.number = v.number "
                                    + "                                          then v.name else NULL end "
                                    + "                              from master.dbo.spt_values v"
                                    + "                              where v.type = 'I' and v.number = 64), '*')"
                                    + "                + ', ' + isnull((select v.name from master.dbo.spt_values v"
                                    + "                                 where i.status & v.number = v.number "
                                    + "                                       and v.type = 'I' and v.number = 1), '*')"
                                    + "                + ', ' + isnull((select v.name from master.dbo.spt_values v"
                                    + "                                 where i.status & v.number = v.number "
                                    + "                                       and v.type = 'I' and v.number = 4), '*')"
                                    + "                + ', ' + isnull((select v.name from master.dbo.spt_values v"
                                    + "                                 where i.status & v.number = v.number "
                                    + "                                       and v.type = 'I' and v.number = 64), '*')"
                                    + "                + ' located on ' + s.name                      "
                                    + " from sysindexes i                                          "
                                    + "        inner join sysobjects o                                "
                                    + "            on (i.id = o.id)                                   "
                                    + "        inner join syssegments s                               "
                                    + "            on (s.segment = i.segment)                         "
                                    + " where indid > 0                                            "
                                    + "        and indid < 255                                        "
                                    + "        and o.type='U'   "
                                    + " order by object_name(o.id), i.name");

            Map<String, Map<String, IndexStructure>> tableIndexNames
                  = new HashMap<String, Map<String, IndexStructure>>();
            Map<String, IndexStructure> indexInfos = new HashMap<String, IndexStructure>();
            String oldTableName = "";
            IndexStructure indexStructure;
            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tableIndexNames.put(oldTableName, indexInfos);
                    indexInfos = new HashMap<String, IndexStructure>();
                }
                indexStructure = new IndexStructure();
                indexStructure.setIndexName(rs.getString("Index_name"));
                String columnNames = rs.getString("Column_names");
                columnNames = Util.replace(columnNames, ", *", "");
                columnNames = Util.replace(columnNames, " ASC", "");
                indexStructure.setColumnNames(columnNames);
                indexStructure.setDefinition(Util.replace(rs.getString("Definition"),
                                                          ", *", ""));
                indexInfos.put(rs.getString("Index_name"), indexStructure);
                oldTableName = tableName;
            }
            tableIndexNames.put(oldTableName, indexInfos);

            indexesStructure.setTableObjectInfos(isRefBase, tableIndexNames);
            tableStructure.setIndexesStructure(indexesStructure);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    private static String isNullColumnName(String index) {
        return " isnull(index_col(object_name(o.id), indid, " + index
               + "), '*') + ' ' + index_colorder(object_name(o.id), indid, " + index + ") ";
    }


    public static void getAllTableRulesInfo(BaseManager baseManager,
                                            TableStructure tableStructure,
                                            String refBaseName,
                                            String srcBaseName) throws SQLException {
        getAllTableRulesInfo(baseManager,
                             tableStructure,
                             refBaseName,
                             srcBaseName,
                             Collections.<String>emptyList());
    }


    public static void getAllTableRulesInfo(BaseManager baseManager,
                                            TableStructure tableStructure,
                                            String refBaseName,
                                            String srcBaseName,
                                            Collection<String> ignoredTableNames) throws SQLException {
        RulesStructure rulesStructure = new RulesStructure();
        getAllTableRulesInfo(baseManager,
                             tableStructure,
                             rulesStructure,
                             refBaseName,
                             ignoredTableNames,
                             true);
        getAllTableRulesInfo(baseManager,
                             tableStructure,
                             rulesStructure,
                             srcBaseName,
                             ignoredTableNames,
                             false);
    }


    public static void getAllTableRulesInfo(BaseManager baseManager,
                                            TableStructure tableStructure,
                                            RulesStructure rulesStructure,
                                            String baseName,
                                            boolean isRefBase) throws SQLException {
        getAllTableRulesInfo(baseManager,
                             tableStructure,
                             rulesStructure,
                             baseName,
                             Collections.<String>emptyList(),
                             isRefBase);
    }


    public static void getAllTableRulesInfo(BaseManager baseManager,
                                            TableStructure tableStructure,
                                            RulesStructure rulesStructure,
                                            String baseName,
                                            Collection<String> ignoredTableNames,
                                            boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select"
                                    + " Table_name = object_name(col.id),"
                                    + " Col_name = col.name, "
                                    + " Rule_name = o.name, "
                                    + " Description = com.text "
                                    + " from syscolumns col "
                                    + " inner join sysobjects o "
                                    + " on (col.domain = o.id) "
                                    + " inner join syscomments com "
                                    + " on (com.id = o.id) "
                                    + " where o.type = 'R' "
                                    + " order by Table_name, Col_name, Rule_name ");

            Map<String, Map<String, RuleStructure>> tableRuleNames
                  = new HashMap<String, Map<String, RuleStructure>>();
            Map<String, RuleStructure> ruleInfos = new HashMap<String, RuleStructure>();
            String oldTableName = "";
            RuleStructure ruleStructure;
            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tableRuleNames.put(oldTableName, ruleInfos);
                    ruleInfos = new HashMap<String, RuleStructure>();
                }
                ruleStructure = new RuleStructure();
                ruleStructure.setRuleName(rs.getString("Rule_name"));
                ruleStructure.setColumnName(rs.getString("Col_name"));
                ruleStructure.setDescription(Util.replace(rs.getString("Description"),
                                                          "CONSTRAINT " + rs.getString("Rule_name"),
                                                          "").trim());
                ruleInfos.put(rs.getString("Rule_name"), ruleStructure);
                oldTableName = tableName;
            }
            tableRuleNames.put(oldTableName, ruleInfos);

            rulesStructure.setTableObjectInfos(isRefBase, tableRuleNames);
            tableStructure.setRulesStructure(rulesStructure);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    public static void getAllTableForeignKeysInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName) throws SQLException {
        getAllTableForeignKeysInfo(baseManager,
                                   tableStructure,
                                   refBaseName,
                                   srcBaseName,
                                   Collections.<String>emptyList());
    }


    public static void getAllTableForeignKeysInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName,
                                                  Collection<String> ignoredTableNames) throws SQLException {
        ForeignKeysStructure foreignKeysStructure = new ForeignKeysStructure();
        getAllTableForeignKeysInfo(baseManager,
                                   refBaseName,
                                   tableStructure,
                                   foreignKeysStructure,
                                   ignoredTableNames,
                                   true);
        getAllTableForeignKeysInfo(baseManager,
                                   srcBaseName,
                                   tableStructure,
                                   foreignKeysStructure,
                                   ignoredTableNames,
                                   false);
    }


    public static void getAllTableForeignKeysInfo(BaseManager baseManager,
                                                  String baseName,
                                                  TableStructure tableStructure,
                                                  ForeignKeysStructure foreignKeysStructure,
                                                  boolean isRefBase) throws SQLException {
        getAllTableForeignKeysInfo(baseManager,
                                   baseName,
                                   tableStructure,
                                   foreignKeysStructure,
                                   Collections.<String>emptyList(),
                                   isRefBase);
    }


    public static void getAllTableForeignKeysInfo(BaseManager baseManager,
                                                  String baseName,
                                                  TableStructure tableStructure,
                                                  ForeignKeysStructure foreignKeysStructure,
                                                  Collection<String> ignoredTableNames,
                                                  boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                       "
                                    + " Table_name = object_name(tableid),      "
                                    + " Foreign_key_name = object_name(constrid),"
                                    + " Column_names =     isnull(col_name(tableid, fokey1), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey2), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey3), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey4), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey5), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey6), '*') + ', ' +"
                                    + "                 isnull(col_name(tableid, fokey7), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey8), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey9), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey10), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey11), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey12), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey13), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey14), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey15), '*') + ', ' +"
                                    + "                    isnull(col_name(tableid, fokey16), '*'),"
                                    + " Definition = 'REFERENCES ' + object_name(reftabid) + ' (' +"
                                    + "                    isnull(col_name(reftabid, refkey1), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey2), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey3), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey4), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey5), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey6), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey7), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey8), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey9), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey10), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey11), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey12), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey13), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey14), '*') + ', ' +"
                                    + "                    isnull(col_name(reftabid, refkey15), '*') + ', ' +"
                                    + "                   isnull(col_name(reftabid, refkey16), '*') + ')'"
                                    + " from sysreferences"
                                    + "     where (frgndbname = NULL or db_id(frgndbname) = db_id())"
                                    + "         or (pmrydbname = NULL or db_id(pmrydbname) = db_id())"
                                    + " order by object_name(tableid), object_name(constrid)");

            Map<String, Map<String, ForeignKeyStructure>> tableFkNames
                  = new HashMap<String, Map<String, ForeignKeyStructure>>();
            Map<String, ForeignKeyStructure> fkInfos = new HashMap<String, ForeignKeyStructure>();
            String oldTableName = "";
            ForeignKeyStructure foreignKeyStructure;
            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tableFkNames.put(oldTableName, fkInfos);
                    fkInfos = new HashMap<String, ForeignKeyStructure>();
                }
                foreignKeyStructure = new ForeignKeyStructure();
                foreignKeyStructure.setForeignKeyName(rs.getString("Foreign_key_name"));
                foreignKeyStructure.setColumnNames(Util.replace(rs.getString(
                      "Column_names"), ", *", ""));
                foreignKeyStructure.setDefinition(Util.replace(rs.getString("Definition"),
                                                               ", *", ""));
                fkInfos.put(rs.getString("Foreign_key_name"), foreignKeyStructure);
                oldTableName = tableName;
            }
            tableFkNames.put(oldTableName, fkInfos);

            foreignKeysStructure.setTableObjectInfos(isRefBase, tableFkNames);
            tableStructure.setForeignKeysStructure(foreignKeysStructure);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    public static void getAllTablePrimaryKeysInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName) throws SQLException {
        getAllTablePrimaryKeysInfo(baseManager,
                                   tableStructure,
                                   refBaseName,
                                   srcBaseName,
                                   Collections.<String>emptyList());
    }


    public static void getAllTablePrimaryKeysInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName,
                                                  Collection<String> ignoredTableNames) throws SQLException {
        PrimaryKeysStructure primaryKeysStructure = new PrimaryKeysStructure();
        getAllTablePrimaryKeysInfo(baseManager, refBaseName, tableStructure,
                                   primaryKeysStructure, ignoredTableNames, true);
        getAllTablePrimaryKeysInfo(baseManager, srcBaseName, tableStructure,
                                   primaryKeysStructure, ignoredTableNames, false);
    }


    public static void getAllTablePrimaryKeysInfo(BaseManager baseManager,
                                                  String baseName,
                                                  TableStructure tableStructure,
                                                  PrimaryKeysStructure primaryKeysStructure,
                                                  boolean isRefBase) throws SQLException {
        getAllTablePrimaryKeysInfo(baseManager,
                                   baseName,
                                   tableStructure,
                                   primaryKeysStructure,
                                   Collections.<String>emptyList(),
                                   isRefBase);
    }


    public static void getAllTablePrimaryKeysInfo(BaseManager baseManager,
                                                  String baseName,
                                                  TableStructure tableStructure,
                                                  PrimaryKeysStructure primaryKeysStructure,
                                                  Collection<String> ignoredTableNames,
                                                  boolean isRefBase) throws SQLException {
        Connection con = getConnection(baseManager, baseName);
        Statement stmt = null;
        try {
            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                                 "
                                    + " Table_name = object_name(id),                     "
                                    + " Primary_key_name = name,                          "
                                    + " Column_names = isnull(index_col(object_name(id), indid, 1), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 2), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 3), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 4), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 5), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 6), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 7), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 8), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 9), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 10), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 11), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 12), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 13), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 14), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 15), '*') + ', ' +"
                                    + "                isnull(index_col(object_name(id), indid, 16), '*'),"
                                    + " Definition = 'PRIMARY KEY INDEX ' +                     "
                                    + "                case when (indid = 1 or (indid > 1 and status2 & 512 = 512))"
                                    + "                    then ': CLUSTERED'                      "
                                    + "                    else ': NONCLUSTERED'                   "
                                    + "                end +                                       "
                                    + "                case when status2 & 1 = 1                   "
                                    + "                    then ', FOREIGN REFERENCE'              "
                                    + "                    else ''                                 "
                                    + "                end                                         "
                                    + " from sysindexes                                         "
                                    + " where indid > 0                                         "
                                    + "     and status2 & 2 = 2                                 "
                                    + "     and status & 2048 = 2048"
                                    + " order by object_name(id)");

            Map<String, Map<String, PrimaryKeyStructure>> tablePkNames
                  = new HashMap<String, Map<String, PrimaryKeyStructure>>();
            Map<String, PrimaryKeyStructure> pkInfos = new HashMap<String, PrimaryKeyStructure>();
            String oldTableName = "";
            PrimaryKeyStructure primaryKeyStructure;
            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tablePkNames.put(oldTableName, pkInfos);
                    pkInfos = new HashMap<String, PrimaryKeyStructure>();
                }
                primaryKeyStructure = new PrimaryKeyStructure();
                primaryKeyStructure.setPrimaryKeyName(rs.getString("Primary_key_name"));
                primaryKeyStructure.setColumnNames(Util.replace(rs.getString(
                      "Column_names"), ", *", ""));
                primaryKeyStructure.setDefinition(rs.getString("Definition"));
                pkInfos.put(rs.getString("Primary_key_name"), primaryKeyStructure);
                oldTableName = tableName;
            }
            tablePkNames.put(oldTableName, pkInfos);

            primaryKeysStructure.setTableObjectInfos(isRefBase, tablePkNames);
            tableStructure.setPrimaryKeysStructure(primaryKeysStructure);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    public static void getAllUsersInfo(BaseManager baseManager, UsersStructure user,
                                       String refBaseName, String srcBaseName) throws SQLException {
        getAllUsersInfo(baseManager, refBaseName, user, true);
        getAllUsersInfo(baseManager, srcBaseName, user, false);
    }


    public static void getAllUsersInfo(BaseManager baseManager, String baseName,
                                       UsersStructure user, boolean isRefBase) throws SQLException {
        Connection con = null;
        CallableStatement cstmt = null;

        try {
            con = getConnection(baseManager, baseName);
            cstmt = con.prepareCall("{call sp_helpuser}");

            ResultSet rs = cstmt.executeQuery();

            Map<String, UserStructure> udtInfos = new HashMap<String, UserStructure>();
            UserStructure userStructure;
            while (rs.next()) {
                userStructure = new UserStructure();
                String userName = rs.getString("Users_name");
                userStructure.setUserName(userName);
                userStructure.setGroupName(rs.getString("Group_name"));
                userStructure.setLoginName(rs.getString("Login_name"));
                udtInfos.put(userName, userStructure);
            }
            user.setUserNames(isRefBase, udtInfos);
        }
        finally {
            releaseConnection(con, cstmt);
        }
    }


    public static Connection getConnection(BaseManager baseManager, String baseName)
          throws SQLException {
        Connection con =
              DriverManager.getConnection(baseManager.getUrl(baseName),
                                          baseManager.getLoggin(baseName), baseManager.getPassWord(baseName));
        con.setCatalog(baseManager.getCatalog(baseName));

        return con;
    }


    /**
     * Teste une connexion avec les paramétres passés en argument
     *
     * @param url      L'url du serveur
     * @param user     Le user
     * @param password Le password
     * @param catalog  Le catalogue
     *
     * @throws SQLException Erreur SQL
     */
    public static void verifyConnection(String url, String user, String password,
                                        String catalog) throws SQLException {
        Connection con = null;

        try {
            con = DriverManager.getConnection(url, user, password);
            con.setCatalog(catalog);
        }
        finally {
            if (con != null) {
                releaseConnection(con);
            }
        }
    }


    /**
     * Ferme une connexion et son statement.
     *
     * @param con  La connexion à fermer
     * @param stmt Le statement à fermer
     */
    public static void releaseConnection(Connection con, Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        }
        catch (SQLException ex) {
            LOGGER.warn("Impossible de fermer le statement !");
        }
        finally {
            releaseConnection(con);
        }
    }


    /**
     * Ferme une connexion.
     *
     * @param con La connexion à fermer
     */
    public static void releaseConnection(Connection con) {
        try {
            if ((con != null) && (!con.isClosed())) {
                con.close();
            }
        }
        catch (SQLException ex) {
            LOGGER.warn("Impossible de fermer la connxion !");
        }
    }


    private static Boolean toBoolean(ResultSet rs, String columnName)
          throws SQLException {
        return rs.getBoolean(columnName) ? Boolean.TRUE : Boolean.FALSE;
    }


    public static void getAllTableConstraintsInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName) throws SQLException {
        getAllTableConstraintsInfo(baseManager,
                                   tableStructure,
                                   refBaseName,
                                   srcBaseName,
                                   Collections.<String>emptyList());
    }


    public static void getAllTableConstraintsInfo(BaseManager baseManager,
                                                  TableStructure tableStructure,
                                                  String refBaseName,
                                                  String srcBaseName,
                                                  Collection<String> ignoredTableNames) throws SQLException {
        ConstraintsStructure constraintsStructure = new ConstraintsStructure();
        getAllTableConstraintsInfo(baseManager,
                                   refBaseName,
                                   tableStructure,
                                   constraintsStructure,
                                   ignoredTableNames,
                                   true);
        getAllTableConstraintsInfo(baseManager,
                                   srcBaseName,
                                   tableStructure,
                                   constraintsStructure,
                                   ignoredTableNames,
                                   false);
    }


    public static void getAllTableConstraintsInfo(BaseManager baseManager,
                                                  String databaseName,
                                                  TableStructure tableStructure,
                                                  ConstraintsStructure constraintsStructure,
                                                  boolean isRefBase) throws SQLException {
        getAllTableConstraintsInfo(baseManager,
                                   databaseName,
                                   tableStructure,
                                   constraintsStructure,
                                   Collections.<String>emptyList(),
                                   isRefBase);
    }


    public static void getAllTableConstraintsInfo(BaseManager baseManager,
                                                  String databaseName,
                                                  TableStructure tableStructure,
                                                  ConstraintsStructure constraintsStructure,
                                                  Collection<String> ignoredTableNames,
                                                  boolean isRefBase) throws SQLException {

        Map<String, Map<String, ConstraintStructure>> tableCheckNames
              = new HashMap<String, Map<String, ConstraintStructure>>();
        Map<String, ConstraintStructure> checkInfos = new HashMap<String, ConstraintStructure>();
        ConstraintStructure constraintStructure;
        String oldTableName = "";

        Connection con = getConnection(baseManager, databaseName);
        Statement stmt = null;
        try {

            stmt = con.createStatement();
            ResultSet rs =
                  stmt.executeQuery("select                    "
                                    + "        o2.name as Table_name,        "
                                    + "        o.name as name,               "
                                    + "        m.text as definition          "
                                    + "from sysconstraints c                 "
                                    + "        inner join sysobjects o       "
                                    + "                on c.constrid = o.id  "
                                    + "        inner join sysobjects o2      "
                                    + "                on c.tableid = o2.id  "
                                    + "        inner join syscomments m      "
                                    + "                on o.id = m.id        "
                                    + "where (o.sysstat & 15 = 7)            "
                                    + "       and o2.type='U'               "
                                    + "order by o2.name, o.name  ");

            while (nextIgnoringFieldValue(rs, TABLE_NAME, ignoredTableNames)) {
                String tableName = rs.getString(TABLE_NAME);
                if (!"".equals(oldTableName) && !oldTableName.equals(tableName)) {
                    tableCheckNames.put(oldTableName, checkInfos);
                    checkInfos = new HashMap<String, ConstraintStructure>();
                }
                constraintStructure =
                      new ConstraintStructure(rs.getString("name"), rs.getString("definition"));
                checkInfos.put(rs.getString("name"), constraintStructure);
                oldTableName = tableName;
            }
            tableCheckNames.put(oldTableName, checkInfos);

            constraintsStructure.setTableObjectInfos(isRefBase, tableCheckNames);
            tableStructure.setConstraintsStructure(constraintsStructure);
        }
        finally {
            releaseConnection(con, stmt);
        }
    }


    private static boolean nextIgnoringFieldValue(ResultSet resultSet,
                                                  String columnName,
                                                  Collection<String> ignoredFieldValues) throws SQLException {
        if (!resultSet.next()) {
            return false;
        }
        else if (!matchPattern(ignoredFieldValues, resultSet.getString(columnName))) {
            return true;
        }

        return nextIgnoringFieldValue(resultSet, columnName, ignoredFieldValues);
    }


    private static boolean matchPattern(Collection<String> patterns, String value) {
        for (String pattern : patterns) {
            if (value.matches(pattern)) {
                return true;
            }
        }
        return false;
    }
}
