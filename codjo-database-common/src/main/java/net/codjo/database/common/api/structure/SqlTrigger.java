package net.codjo.database.common.api.structure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class SqlTrigger extends AbstractSqlObject {
    public enum Type {
        INSERT,
        UPDATE,
        DELETE,
        CHECK_RECORD;
    }

    private Type type;
    private SqlTable table;
    private String sqlContent;

    private List<TableLink> links = new ArrayList<TableLink>();


    protected SqlTrigger() {
    }


    SqlTrigger(String name) {
        super(name);
    }


    public Type getType() {
        return type;
    }


    private void setType(Type type) {
        this.type = type;
    }


    public SqlTable getTable() {
        return table;
    }


    public void setTable(SqlTable table) {
        this.table = table;
    }


    public String getSqlContent() {
        return sqlContent;
    }


    public void setSqlContent(String sqlContent) {
        this.sqlContent = sqlContent;
    }


    public List<TableLink> getLinks() {
        return links;
    }


    public SqlTable getLinkedTable(int tableIndex) {
        return links.get(tableIndex).getOtherTable();
    }


    public SqlField[] getLinkedFields(int tableIndex, int fieldIndex) {
        return links.get(tableIndex).getLinkedFields().get(fieldIndex);
    }


    public void addTableLink(SqlTable childTable, SqlField[]... linkedFields) {
        links.add(new TableLink(childTable, linkedFields));
    }


    public static SqlTrigger triggerName(String triggerName) {
        return new SqlTrigger(triggerName);
    }


    public static SqlTrigger insertTrigger(String triggerName,
                                           SqlTable table,
                                           String sqlContent) {
        SqlTrigger trigger = new SqlTrigger(triggerName);
        trigger.setType(Type.INSERT);
        trigger.setTable(table);
        trigger.setSqlContent(sqlContent);
        return trigger;
    }


    public static SqlTrigger updateTrigger(String triggerName,
                                           SqlTable table) {
        return updateTrigger(triggerName, table, "");
    }


    public static SqlTrigger updateTrigger(String triggerName,
                                           SqlTable table,
                                           String sqlContent) {
        SqlTrigger trigger = new SqlTrigger(triggerName);
        trigger.setType(Type.UPDATE);
        trigger.setTable(table);
        trigger.setSqlContent(sqlContent);
        return trigger;
    }


    public static SqlTrigger deleteTrigger(String triggerName,
                                           SqlTable table) {
        return deleteTrigger(triggerName, table, "");
    }


    public static SqlTrigger deleteTrigger(String triggerName,
                                           SqlTable table,
                                           String sqlContent) {
        SqlTrigger trigger = new SqlTrigger(triggerName);
        trigger.setType(Type.DELETE);
        trigger.setTable(table);
        trigger.setSqlContent(sqlContent);
        return trigger;
    }


    public static SqlTrigger checkRecordTrigger(String triggerName, SqlTable table) {
        SqlTrigger trigger = new SqlTrigger(triggerName);
        trigger.setType(Type.CHECK_RECORD);
        trigger.setTable(table);
        return trigger;
    }


    public class TableLink {
        private SqlTable otherTable;
        private final List<SqlField[]> linkedFields = new ArrayList<SqlField[]>();


        private TableLink(SqlTable child, SqlField[]... linkedFields) {
            this.otherTable = child;
            this.linkedFields.addAll(Arrays.asList(linkedFields));
        }


        public List<SqlField[]> getLinkedFields() {
            return Collections.unmodifiableList(linkedFields);
        }


        public void addLinkedFields(SqlField fromField, SqlField toField) {
            linkedFields.add(new SqlField[]{fromField, toField});
        }


        public SqlTable getOtherTable() {
            return otherTable;
        }
    }
}
