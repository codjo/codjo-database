package net.codjo.database.hsqldb.impl.query.builder;
import net.codjo.database.common.api.structure.SqlField;
import net.codjo.database.common.api.structure.SqlIndex.Type;
import net.codjo.database.common.impl.query.builder.AbstractAlterTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultCreateIndexQueryBuilder;
import net.codjo.database.hsqldb.impl.query.HsqldbDatabaseQueryHelper;

import static net.codjo.database.common.api.structure.SqlIndex.Type.UNIQUE;
import static net.codjo.database.common.api.structure.SqlIndex.Type.UNIQUE_CLUSTERED;
/**
 *
 */
public class HsqldbCreateIndexQueryBuilder extends DefaultCreateIndexQueryBuilder {
    @Override
    protected StringBuilder getImpl(StringBuilder create) {
        if (UNIQUE.equals(index.getType()) || UNIQUE_CLUSTERED.equals(index.getType())) {
            Type oldType = index.getType();
            // set the index to NORMAL and build the query
            index.setType(Type.NORMAL);
            try {
                super.getImpl(create).append(";\n");
            } finally {
                // restore index to oldType to avoid side effects
                index.setType(oldType);
            }

            // append a second query for the UNIQUE constraint
            create.append("alter table ").append(index.getTable().getName());
            create.append(" add constraint ").append(index.getName());
            create.append(" unique");
            appendFieldList(create, index.getFields());
        } else {
            super.getImpl(create);
        }

        return create;
    }
}
