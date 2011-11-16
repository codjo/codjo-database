package net.codjo.database.common.util;
/**
 * @deprecated {@link net.codjo.database.common.api.structure.SqlConstraint}
 */
@Deprecated
public class SqlConstraint extends net.codjo.database.common.api.structure.SqlConstraint {

    public static net.codjo.database.common.api.structure.SqlConstraint constraintName(String constraintName) {
        return net.codjo.database.common.api.structure.SqlConstraint.constraintName(constraintName);
    }


    public static net.codjo.database.common.api.structure.SqlConstraint foreignKey(String name,
                                                                                 SqlTable alteredTable) {
        return net.codjo.database.common.api.structure.SqlConstraint.foreignKey(name, alteredTable);
    }


    public static net.codjo.database.common.api.structure.SqlConstraint foreignKey(String name,
                                                                                 SqlTable alteredTable,
                                                                                 SqlField[] alteredFields,
                                                                                 SqlTable referencedTable,
                                                                                 SqlField[] referencedFields) {

        return net.codjo.database.common.api.structure.SqlConstraint
              .foreignKey(name, alteredTable, alteredFields, referencedTable, referencedFields);
    }
}
