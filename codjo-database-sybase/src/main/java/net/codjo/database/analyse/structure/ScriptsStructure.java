/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.TreeSet;
/**
 * DOCUMENT ME!
 *
 * @version $Revision: 1.3 $
 */
public class ScriptsStructure {
    private Map refObjectScriptInfos;
    private Map srcObjectScriptInfos;
    private Map refUpdtScripts = new HashMap();
    private Map srcUpdtScripts = new HashMap();


    public Set<String> getUpdtTables(List<String> commonProcedureNames) {
        findScriptDifferences(commonProcedureNames);
        return getUpdtTableNames(commonProcedureNames);
    }


    public String getNewProcedureScriptInfos(String procedureName) {
        return ((net.codjo.database.analyse.structure.ScriptStructure)srcObjectScriptInfos.get(procedureName)).getScript();
    }


    public String getDelProcedureScriptInfos(String procedureName) {
        return ((ScriptStructure)refObjectScriptInfos.get(procedureName)).getScript();
    }


    public void setProcedureScriptInfos(boolean isRefBase, Map objectScriptNames) {
        if (isRefBase) {
            refObjectScriptInfos = objectScriptNames;
        }
        else {
            srcObjectScriptInfos = objectScriptNames;
        }
    }


    public String getRefUpdtObjects(String procedureName) {
        return (String)refUpdtScripts.get(procedureName);
    }


    public String getSrcUpdtObjects(String procedureName) {
        return (String)srcUpdtScripts.get(procedureName);
    }


    protected void findScriptDifferences(List commonProcedureNames) {
        for (int i = 0; i < commonProcedureNames.size(); i++) {
            String procedureName = (String)commonProcedureNames.get(i);
            net.codjo.database.analyse.structure.ScriptStructure srcScripts =
                  (net.codjo.database.analyse.structure.ScriptStructure)srcObjectScriptInfos
                        .get(procedureName);
            net.codjo.database.analyse.structure.ScriptStructure refScripts =
                  (net.codjo.database.analyse.structure.ScriptStructure)refObjectScriptInfos
                        .get(procedureName);
            if (!isSameScriptStructure(refScripts, srcScripts)) {
                refUpdtScripts.put(procedureName, refScripts.getScript());
                srcUpdtScripts.put(procedureName, srcScripts.getScript());
            }
        }
    }


    protected Set<String> getUpdtTableNames(List<String> commonProcedureNames) {
        Set<String> updtTables = new TreeSet<String>();
        for (String objectName : commonProcedureNames) {
            if (srcUpdtScripts.containsKey(objectName)) {
                updtTables.add(objectName);
            }
        }
        return updtTables;
    }


    static boolean isSameScriptStructure(
          net.codjo.database.analyse.structure.ScriptStructure refScriptConfigs,
          net.codjo.database.analyse.structure.ScriptStructure srcScriptConfigs) {
        String refScript = trimLines(refScriptConfigs.getScript());
        String srcScript = trimLines(srcScriptConfigs.getScript());
        return Util.isEqual(removeCommentBlocks(refScript), removeCommentBlocks(srcScript));
    }


    private static String trimLines(String script) {
        StringTokenizer tokenizer = new StringTokenizer(script, "\n");
        StringBuffer result = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String cleanLine = removeSomeInternalSpaces(tokenizer.nextToken().trim());
            result.append(removeLineComments(cleanLine));
        }
        return result.toString();
    }


    static String removeSomeInternalSpaces(String trimedLine) {
        if (trimedLine.endsWith(",")) {
            return trimedLine.substring(0, trimedLine.length() - 1).trim() + ",";
        }
        return trimedLine;
    }


    protected static String removeLineComments(String line) {
        if (line.indexOf("--") > -1) {
            return line.substring(0, line.indexOf("--"));
        }
        return line;
    }


    protected static String removeCommentBlocks(String script) {
        StringTokenizer tokenizer = new StringTokenizer(script, "/");
        StringBuffer result = new StringBuffer();
        while (tokenizer.hasMoreTokens()) {
            String str = tokenizer.nextToken();
            if (str.startsWith("*")) {
                continue;
            }
            else {
                result.append(str);
            }
        }
        return result.toString();
    }
}
