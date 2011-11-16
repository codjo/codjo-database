/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import net.codjo.database.analyse.Util;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 */
public class ColumnsStructure extends ObjectsStructure<ColumnStructure> {
    public ColumnsStructure() {
    }


    protected Set<String> getUpdtTables(List<String> commonTableNames) {
        findColumnDifferences(commonTableNames);
        return getUpdtTableNames(commonTableNames);
    }


    protected List<List<String>> getDelTableColInfos(String tableName) {
        return getTableColInfos(getRefTableObjectInfos(tableName));
    }


    protected List<List<String>> getNewTableColInfos(String tableName) {
        return getTableColInfos(getSrcTableObjectInfos(tableName));
    }


    private void findColumnDifferences(List<String> commonTableNames) {
        for (Object commonTableName : commonTableNames) {
            String tableName = (String)commonTableName;
            Map<String, ColumnStructure> srcCols = getSrcTableObjectInfos(tableName);
            Map<String, ColumnStructure> refCols = getRefTableObjectInfos(tableName);

            List<String> addCols;
            List<String> deleteCols;
            if (refCols != null && srcCols == null) {
                deleteCols = new ArrayList<String>(refCols.keySet());
                fillColumnInfos(deleteCols, refCols, tableName, getDelObjects());
            }
            if (refCols == null && srcCols != null) {
                addCols = new ArrayList<String>(srcCols.keySet());
                fillColumnInfos(addCols, srcCols, tableName, getNewObjects());
            }
            if (refCols != null && srcCols != null) {
                deleteCols = Util.removeElements(refCols.keySet(), srcCols.keySet());
                fillColumnInfos(deleteCols, refCols, tableName, getDelObjects());

                addCols = Util.removeElements(srcCols.keySet(), refCols.keySet());
                fillColumnInfos(addCols, srcCols, tableName, getNewObjects());

                List<String> commonColNames = new ArrayList<String>(refCols.keySet());
                commonColNames.retainAll(srcCols.keySet());
                findUpdatedColumns(commonColNames, refCols, srcCols, tableName);
            }
        }
    }


    private void fillColumnInfos(List<String> delColumns,
                                 Map<String, ColumnStructure> refCols,
                                 String tableName,
                                 Map<String, List<List<String>>> colInfos) {
        if (!delColumns.isEmpty()) {
            List<List<String>> infos = new ArrayList<List<String>>();
            for (String colName : delColumns) {
                infos.add(refCols.get(colName).getColumnInfos());
            }
            colInfos.put(tableName, infos);
        }
    }


    private List<List<String>> getTableColInfos(Map<String, ColumnStructure> tableColInfos) {
        List<List<String>> infos = new ArrayList<List<String>>();
        for (ColumnStructure columnStructure : tableColInfos.values()) {
            infos.add(columnStructure.getColumnInfos());
        }
        return infos;
    }


    private void findUpdatedColumns(List<String> commonColNames,
                                    Map<String, ColumnStructure> refCols,
                                    Map<String, ColumnStructure> srcCols,
                                    String tableName) {
        List<List<String>> refInfos = new ArrayList<List<String>>();
        List<List<String>> srcInfos = new ArrayList<List<String>>();
        List<ColumnStructure> refColsList = new ArrayList<ColumnStructure>();
        List<ColumnStructure> srcColsList = new ArrayList<ColumnStructure>();
        for (String colName : commonColNames) {
            ColumnStructure refColConfigs = refCols.get(colName);
            ColumnStructure srcColConfigs = srcCols.get(colName);
            refColsList.add(refColConfigs);
            srcColsList.add(srcColConfigs);
            if (!isSameColumnStructure(refColConfigs, srcColConfigs)) {
                refInfos.add(refColConfigs.getColumnInfos());
                srcInfos.add(srcColConfigs.getColumnInfos());
            }
        }

        checkColumnOrdering(refColsList, srcColsList, refInfos, srcInfos);

        fillUpdatedObjects(tableName, refInfos, srcInfos);
    }


    private void checkColumnOrdering(List<ColumnStructure> refColsList,
                                     List<ColumnStructure> srcColsList,
                                     List<List<String>> refInfos,
                                     List<List<String>> srcInfos) {
        sortColumns(refColsList);
        sortColumns(srcColsList);

        if (refColsList.size() == srcColsList.size()) {
            for (int i = 0; i < refColsList.size(); i++) {
                ColumnStructure refColumn = refColsList.get(i);
                ColumnStructure srcColumn = srcColsList.get(i);
                if (!refColumn.getColumnName().equals(srcColumn.getColumnName())) {
                    refInfos.add(refColumn.getColumnInfos());
                    srcInfos.add(srcColumn.getColumnInfos());
                }
            }
        }
    }


    private void sortColumns(List<ColumnStructure> cols) {
        Collections.sort(cols,
                         new Comparator<ColumnStructure>() {
                             public int compare(ColumnStructure column1, ColumnStructure column2) {
                                 if (column1.getColumnPosition() < column2.getColumnPosition()) {
                                     return -1;
                                 }
                                 else if (column1.getColumnPosition() > column2.getColumnPosition()) {
                                     return 1;
                                 }
                                 return 0;
                             }
                         });
    }


    private boolean isSameColumnStructure(ColumnStructure refColConfigs, ColumnStructure srcColConfigs) {
        if (!Util.isEqual(refColConfigs.getColumnType(), srcColConfigs.getColumnType())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getLength(), srcColConfigs.getLength())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getPrecision(), srcColConfigs.getPrecision())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getScale(), srcColConfigs.getScale())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getAllowNulls(), srcColConfigs.getAllowNulls())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getDefaultValue(), srcColConfigs.getDefaultValue())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getRuleName(), srcColConfigs.getRuleName())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getIdentity(), srcColConfigs.getIdentity())) {
            return false;
        }
        if (!Util.isEqual(refColConfigs.getIdentityGap(), srcColConfigs.getIdentityGap())) {
            return false;
        }
        return true;
    }
}
