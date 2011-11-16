package net.codjo.database.sybase.repository;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.DatabaseScriptHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.impl.comparator.DefaultTableComparatorBuilder;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
import net.codjo.database.common.repository.builder.JdbcFixtureBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import net.codjo.database.sybase.impl.SybaseDatabaseTranscoder;
import net.codjo.database.sybase.impl.comparator.SybaseDatabaseComparatorBuilder;
import net.codjo.database.sybase.impl.fixture.SybaseJdbcFixtureBuilder;
import net.codjo.database.sybase.impl.helper.SybaseDatabaseHelper;
import net.codjo.database.sybase.impl.helper.SybaseDatabaseScriptHelper;
import net.codjo.database.sybase.impl.query.SybaseDatabaseQueryHelper;
import net.codjo.database.sybase.impl.relation.SybaseRelationshipBuilder;
import net.codjo.database.sybase.impl.script.SybaseExecSqlScriptBuilder;
import net.codjo.database.sybase.impl.sqlfield.SybaseSQLFieldListBuilder;
public class SybaseDatabaseRepository extends AbstractDatabaseRepository {
    @Override
    protected Class<? extends DatabaseHelper> getDatabaseHelperImplementationClass() {
        return SybaseDatabaseHelper.class;
    }


    @Override
    protected Class<? extends DatabaseQueryHelper> getDatabaseQueryHelperImplementationClass() {
        return SybaseDatabaseQueryHelper.class;
    }


    @Override
    protected Class<? extends DatabaseScriptHelper> getDatabaseScriptHelperImplementationClass() {
        return SybaseDatabaseScriptHelper.class;
    }


    @Override
    protected Class<? extends DatabaseTranscoder> getDatabaseTranscoderImplementationClass() {
        return SybaseDatabaseTranscoder.class;
    }


    @Override
    protected Class<? extends ExecSqlScriptBuilder> getExecSqlScriptBuilderImplementationClass() {
        return SybaseExecSqlScriptBuilder.class;
    }


    @Override
    public Class<? extends RelationshipBuilder> getRelationshipBuilderImplementationClass() {
        return SybaseRelationshipBuilder.class;
    }


    @Override
    protected Class<? extends SQLFieldListBuilder> getSQLFIeldListBuilderImplementationClass() {
        return SybaseSQLFieldListBuilder.class;
    }


    @Override
    protected Class<? extends JdbcFixtureBuilder> getJdbcFixtureBuilderImplementationClass() {
        return SybaseJdbcFixtureBuilder.class;
    }


    @Override
    protected Class<? extends DatabaseComparatorBuilder> getDatabaseComparatorBuilderImplementationClass() {
        return SybaseDatabaseComparatorBuilder.class;
    }


    @Override
    protected Class<? extends TableComparatorBuilder> getTableComparatorBuilderImplementationClass() {
        return DefaultTableComparatorBuilder.class;
    }
}
