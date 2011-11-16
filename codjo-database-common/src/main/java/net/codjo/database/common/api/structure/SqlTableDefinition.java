package net.codjo.database.common.api.structure;
import java.util.List;
public class SqlTableDefinition extends AbstractSqlObject {
    private List<String> primaryKeys;
    private List<SqlFieldDefinition> sqlFieldDefinitions;
    private boolean pkGenerator;


    public SqlTableDefinition(String name) {
        super(name);
    }


    public boolean isPkGenerator() {
        return pkGenerator;
    }


    public void setPkGenerator(boolean pkGenerator) {
        this.pkGenerator = pkGenerator;
    }


    public List<String> getPrimaryKeys() {
        return primaryKeys;
    }


    public void setPrimaryKeys(List<String> primaryKeys) {
        this.primaryKeys = primaryKeys;
    }


    public List<SqlFieldDefinition> getSqlFieldDefinitions() {
        return sqlFieldDefinitions;
    }


    public void setSqlFieldDefinitions(List<SqlFieldDefinition> sqlFieldDefinitions) {
        this.sqlFieldDefinitions = sqlFieldDefinitions;
    }
}