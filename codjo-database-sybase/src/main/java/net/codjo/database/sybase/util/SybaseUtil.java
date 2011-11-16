package net.codjo.database.sybase.util;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlObject;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlView;
public class SybaseUtil {
    private static final int MAX_OBJECT_NAME_LENGTH = 30;


    private SybaseUtil() {
    }


    public static String getName(SqlObject sqlObject) {
        if (sqlObject == null) {
            return "";
        }

        if (sqlObject instanceof SqlTable) {
            return getTableName((SqlTable)sqlObject);
        }
        else if (sqlObject instanceof SqlView) {
            return getViewName((SqlView)sqlObject);
        }
        else if (sqlObject instanceof SqlIndex) {
            return getIndexName((SqlIndex)sqlObject);
        }
        else if (sqlObject instanceof SqlConstraint) {
            return getConstraintName((SqlConstraint)sqlObject);
        }
        else {
            return sqlObject.getName();
        }
    }


    public static String getTableName(SqlTable table) {
        StringBuilder tableName = new StringBuilder();
        if (table.isTemporary()) {
            tableName.append("#");
        }
        return tableName.append(table.getName()).toString();
    }


    public static String getViewName(SqlView view) {
        return view.getName();
    }


    public static String getIndexName(SqlIndex index) {
        String indexName = index.getName();
        return indexName.substring(0, Math.min(indexName.length(), MAX_OBJECT_NAME_LENGTH));
    }


    public static String getConstraintName(SqlConstraint constraint) {
        return constraint.getName();
    }


    public static String getFullIndexName(SqlIndex index) {
        String indexName = SybaseUtil.getIndexName(index);
        String tableName = SybaseUtil.getTableName(index.getTable());
        return new StringBuilder().append(tableName).append(".").append(indexName).toString();
    }
}
