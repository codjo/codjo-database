package net.codjo.database.oracle.impl.query.builder;
import net.codjo.database.common.api.structure.SqlConstraint.Type;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.impl.query.builder.AbstractCreateConstraintQueryBuilder;
/**
 *
 */
public class OracleCreateConstraintQueryBuilder extends AbstractCreateConstraintQueryBuilder {
    @Override
    public String get() {
        StringBuilder alter = new StringBuilder()
              .append("alter table ").append(constraint.getAlteredTable().getName())
              .append(" add constraint ").append(constraint.getName());
        switch (constraint.getType()) {
            case UNIQUE:
                alter.append(" unique (");
                break;
            case PRIMARY:
                alter.append(" primary key (");
                break;
            case FOREIGN:
                alter.append(" foreign key (");
                break;
        }
        for (SqlField[] linkedFields : constraint.getLinkedFields()) {
            alter.append(linkedFields[0].getName()).append(", ");
        }
        alter.delete(alter.length() - 2, alter.length());

        if (constraint.getType().equals(Type.FOREIGN)) {
            alter.append(") references ")
                  .append(constraint.getReferencedTable().getName()).append(" (");
            for (SqlField[] linkedFields : constraint.getLinkedFields()) {
                alter.append(linkedFields[1].getName()).append(", ");
            }
            alter.delete(alter.length() - 2, alter.length());
        }
        return alter.append(")").toString();
    }
}

