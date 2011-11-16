package net.codjo.database.oracle.repository;
import net.codjo.database.common.api.DatabaseHelper;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.DatabaseScriptHelper;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import net.codjo.database.common.impl.comparator.DefaultTableComparatorBuilder;
import net.codjo.database.common.repository.AbstractDatabaseRepository;
import net.codjo.database.common.repository.builder.DatabaseComparatorBuilder;
import net.codjo.database.common.repository.builder.ExecSqlScriptBuilder;
import net.codjo.database.common.repository.builder.RelationshipBuilder;
import net.codjo.database.common.repository.builder.SQLFieldListBuilder;
import net.codjo.database.common.repository.builder.TableComparatorBuilder;
import net.codjo.database.oracle.impl.OracleDatabaseTranscoder;
import net.codjo.database.oracle.impl.comparator.OracleDatabaseComparatorBuilder;
import net.codjo.database.oracle.impl.fixture.OracleJdbcFixtureBuilder;
import net.codjo.database.oracle.impl.helper.OracleDatabaseHelper;
import net.codjo.database.oracle.impl.helper.OracleDatabaseScriptHelper;
import net.codjo.database.oracle.impl.query.OracleDatabaseQueryHelper;
import net.codjo.database.oracle.impl.relation.OracleRelationshipBuilder;
import net.codjo.database.oracle.impl.script.OracleExecSqlScriptBuilder;
import net.codjo.database.oracle.impl.sqlfield.OracleSQLFieldListBuilder;
public class OracleDatabaseRepository extends AbstractDatabaseRepository {
    @Override
    protected Class<? extends DatabaseHelper> getDatabaseHelperImplementationClass() {
        return OracleDatabaseHelper.class;
    }


    @Override
    protected Class<? extends DatabaseQueryHelper> getDatabaseQueryHelperImplementationClass() {
        return OracleDatabaseQueryHelper.class;
    }


    @Override
    protected Class<? extends DatabaseScriptHelper> getDatabaseScriptHelperImplementationClass() {
        return OracleDatabaseScriptHelper.class;
    }


    @Override
    protected Class<? extends DatabaseTranscoder> getDatabaseTranscoderImplementationClass() {
        return OracleDatabaseTranscoder.class;
    }


    @Override
    protected Class<? extends ExecSqlScriptBuilder> getExecSqlScriptBuilderImplementationClass() {
        return OracleExecSqlScriptBuilder.class;
    }


    @Override
    public Class<? extends RelationshipBuilder> getRelationshipBuilderImplementationClass() {
        return OracleRelationshipBuilder.class;
    }


    @Override
    protected Class<? extends SQLFieldListBuilder> getSQLFIeldListBuilderImplementationClass() {
        return OracleSQLFieldListBuilder.class;
    }


    @Override
    protected Class<? extends OracleJdbcFixtureBuilder> getJdbcFixtureBuilderImplementationClass() {
        return OracleJdbcFixtureBuilder.class;
    }


    @Override
    protected Class<? extends DatabaseComparatorBuilder> getDatabaseComparatorBuilderImplementationClass() {
        return OracleDatabaseComparatorBuilder.class;
    }


    @Override
    protected Class<? extends TableComparatorBuilder> getTableComparatorBuilderImplementationClass() {
        return DefaultTableComparatorBuilder.class;
    }
}
