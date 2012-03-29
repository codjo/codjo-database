package net.codjo.database.common.api.structure;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
public class SqlConstraint extends AbstractSqlObject {
    private SqlTable alteredTable;
    private SqlTable referencedTable;
    private final List<SqlField[]> linkedFields = new ArrayList<SqlField[]>();
    private Type type = Type.PRIMARY;

    public enum Type {
        UNIQUE,
        FOREIGN,
        PRIMARY
    }


    protected SqlConstraint() {
    }


    SqlConstraint(String name) {
        super(name);
    }


    SqlConstraint(String name, SqlTable alteredTable) {
        super(name);
        this.alteredTable = alteredTable;
    }


    SqlConstraint(String name,
                  SqlTable alteredTable,
                  SqlTable referencedTable,
                  SqlField[]... linkedFields) {
        super(name);
        this.alteredTable = alteredTable;
        this.referencedTable = referencedTable;
        this.linkedFields.addAll(Arrays.asList(linkedFields));
    }


    public SqlTable getAlteredTable() {
        return alteredTable;
    }


    public void setAlteredTable(SqlTable alteredTable) {
        this.alteredTable = alteredTable;
    }


    public SqlTable getReferencedTable() {
        return referencedTable;
    }


    public void setReferencedTable(SqlTable referencedTable) {
        this.referencedTable = referencedTable;
    }


    public List<SqlField[]> getLinkedFields() {
        return Collections.unmodifiableList(linkedFields);
    }


    public void addLinkedFields(SqlField fromField, SqlField toField) {
        linkedFields.add(new SqlField[]{fromField, toField});
    }


    public static SqlConstraint constraintName(String constraintName) {
        return new SqlConstraint(constraintName);
    }


    public static SqlConstraint foreignKey(String name, SqlTable alteredTable) {
        SqlConstraint sqlConstraint = new SqlConstraint(name, alteredTable);
        sqlConstraint.setType(Type.FOREIGN);
        return sqlConstraint;
    }


    public static SqlConstraint foreignKey(String name,
                                           SqlTable alteredTable,
                                           SqlField[] alteredFields,
                                           SqlTable referencedTable,
                                           SqlField[] referencedFields) {

        SqlConstraint constraint = new SqlConstraint(name);
        constraint.setAlteredTable(alteredTable);
        for (int i = 0; i < alteredFields.length; i++) {
            constraint.addLinkedFields(alteredFields[i], referencedFields[i]);
        }
        constraint.setReferencedTable(referencedTable);
        constraint.setType(Type.FOREIGN);
        return constraint;
    }


    public Type getType() {
        return type;
    }


    public void setType(Type type) {
        this.type = type;
    }
}
