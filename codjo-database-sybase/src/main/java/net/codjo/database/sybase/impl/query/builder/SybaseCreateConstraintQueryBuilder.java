package net.codjo.database.sybase.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.impl.query.builder.AbstractCreateConstraintQueryBuilder;
import net.codjo.database.sybase.util.SybaseUtil;
public class SybaseCreateConstraintQueryBuilder extends AbstractCreateConstraintQueryBuilder {

    @Override
    public String get() {
        StringBuilder alter = new StringBuilder()
              .append("alter table ").append(SybaseUtil.getTableName(constraint.getAlteredTable()))
              .append(" add constraint ").append(SybaseUtil.getConstraintName(constraint)).append("\n")
              .append("foreign key (");
        for (SqlField[] linkedFields : constraint.getLinkedFields()) {
            alter.append(linkedFields[0].getName()).append(", ");
        }
        alter.delete(alter.length() - 2, alter.length());
        alter.append(") references ")
              .append(SybaseUtil.getTableName(constraint.getReferencedTable())).append(" (");
        for (SqlField[] linkedFields : constraint.getLinkedFields()) {
            alter.append(linkedFields[1].getName()).append(", ");
        }
        alter.delete(alter.length() - 2, alter.length());
        return alter.append(")").toString();
    }
}
