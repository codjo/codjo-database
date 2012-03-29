package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.impl.query.builder.AbstractInsertQueryBuilder;
/**
 *
 */
public class OracleInsertQueryBuilder extends AbstractInsertQueryBuilder {

    protected final String getTableName() {
        return table.getName();
    }


    @Override
    protected String formatFieldName(SqlField sqlField) {
        String name = sqlField.getName();
        if (isOracleKeyword(name)) {
            return "\"" + name + "\"";
        }
        return name;
    }


    //TODO[]Oracle support] commonaliser avec OracleDatabaseScriptHelper
    private boolean isOracleKeyword(String name) {
        return "COMMENT".equals(name);
    }
}
