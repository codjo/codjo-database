package net.codjo.database.hsqldb.repository;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.DatabaseScriptHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.impl.comparator.DefaultTableComparatorBuilder;
import net.codjo.database.common.impl.relation.DefaultRelationshipBuilder;
import net.codjo.database.common.impl.sqlfield.DefaultSQLFieldListBuilder;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import net.codjo.database.hsqldb.impl.HsqldbDatabaseTranscoder;
import net.codjo.database.hsqldb.impl.comparator.HsqldbDatabaseComparatorBuilder;
import net.codjo.database.hsqldb.impl.fixture.HsqldbJdbcFixtureBuilder;
import net.codjo.database.hsqldb.impl.helper.HsqldbDatabaseHelper;
import net.codjo.database.hsqldb.impl.helper.HsqldbDatabaseScriptHelper;
import net.codjo.database.hsqldb.impl.query.HsqldbDatabaseQueryHelper;
import net.codjo.database.hsqldb.impl.script.HsqldbExecSqlScriptBuilder;
public class HsqldbDatabaseRepository extends AbstractDatabaseRepository {
    @Override
    protected Class<? extends DatabaseHelper> getDatabaseHelperImplementationClass() {
        return HsqldbDatabaseHelper.class;
    }


    @Override
    protected Class<? extends DatabaseQueryHelper> getDatabaseQueryHelperImplementationClass() {
        return HsqldbDatabaseQueryHelper.class;
    }


    @Override
    protected Class<? extends DatabaseScriptHelper> getDatabaseScriptHelperImplementationClass() {
        return HsqldbDatabaseScriptHelper.class;
    }


    @Override
    protected Class<? extends DatabaseTranscoder> getDatabaseTranscoderImplementationClass() {
        return HsqldbDatabaseTranscoder.class;
    }


    @Override
    protected Class<? extends ExecSqlScriptBuilder> getExecSqlScriptBuilderImplementationClass() {
        return HsqldbExecSqlScriptBuilder.class;
    }


    @Override
    public Class<? extends RelationshipBuilder> getRelationshipBuilderImplementationClass() {
        return DefaultRelationshipBuilder.class;
    }


    @Override
    protected Class<? extends SQLFieldListBuilder> getSQLFIeldListBuilderImplementationClass() {
        return DefaultSQLFieldListBuilder.class;
    }


    @Override
    protected Class<? extends HsqldbJdbcFixtureBuilder> getJdbcFixtureBuilderImplementationClass() {
        return HsqldbJdbcFixtureBuilder.class;
    }


    @Override
    protected Class<? extends DatabaseComparatorBuilder> getDatabaseComparatorBuilderImplementationClass() {
        return HsqldbDatabaseComparatorBuilder.class;
    }


    @Override
    protected Class<? extends TableComparatorBuilder> getTableComparatorBuilderImplementationClass() {
        return DefaultTableComparatorBuilder.class;
    }
}
