package net.codjo.database.common.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;

public class DefaultCreateConstraintQueryBuilder extends AbstractCreateConstraintQueryBuilder {
    @Override
    public String get() {
        StringBuilder alter = new StringBuilder()
              .append("alter table ").append(constraint.getAlteredTable().getName())
              .append(" add constraint ").append(constraint.getName())
              .append(" foreign key (");
        for (SqlField[] linkedFields : constraint.getLinkedFields()) {
            alter.append(linkedFields[0].getName()).append(", ");
        }
        alter.delete(alter.length() - 2, alter.length());
        alter.append(") references ")
              .append(constraint.getReferencedTable().getName()).append(" (");
        for (SqlField[] linkedFields : constraint.getLinkedFields()) {
            alter.append(linkedFields[1].getName()).append(", ");
        }
        alter.delete(alter.length() - 2, alter.length());
        return alter.append(")").toString();
    }
}
