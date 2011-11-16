package net.codjo.database.mysql.repository;
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
import net.codjo.database.mysql.impl.MysqlDatabaseTranscoder;
import net.codjo.database.mysql.impl.comparator.MysqlDatabaseComparatorBuilder;
import net.codjo.database.mysql.impl.fixture.MysqlJdbcFixtureBuilder;
import net.codjo.database.mysql.impl.helper.MysqlDatabaseHelper;
import net.codjo.database.mysql.impl.helper.MysqlDatabaseScriptHelper;
import net.codjo.database.mysql.impl.query.MysqlDatabaseQueryHelper;
import net.codjo.database.mysql.impl.relation.MysqlRelationshipBuilder;
import net.codjo.database.mysql.impl.script.MysqlExecSqlScriptBuilder;
import net.codjo.database.mysql.impl.sqlfield.MysqlSQLFieldListBuilder;
public class MysqlDatabaseRepository extends AbstractDatabaseRepository {
    @Override
    protected Class<? extends DatabaseHelper> getDatabaseHelperImplementationClass() {
        return MysqlDatabaseHelper.class;
    }


    @Override
    protected Class<? extends DatabaseQueryHelper> getDatabaseQueryHelperImplementationClass() {
        return MysqlDatabaseQueryHelper.class;
    }


    @Override
    protected Class<? extends DatabaseScriptHelper> getDatabaseScriptHelperImplementationClass() {
        return MysqlDatabaseScriptHelper.class;
    }


    @Override
    protected Class<? extends DatabaseTranscoder> getDatabaseTranscoderImplementationClass() {
        return MysqlDatabaseTranscoder.class;
    }


    @Override
    protected Class<? extends ExecSqlScriptBuilder> getExecSqlScriptBuilderImplementationClass() {
        return MysqlExecSqlScriptBuilder.class;
    }


    @Override
    public Class<? extends RelationshipBuilder> getRelationshipBuilderImplementationClass() {
        return MysqlRelationshipBuilder.class;
    }


    @Override
    protected Class<? extends SQLFieldListBuilder> getSQLFIeldListBuilderImplementationClass() {
        return MysqlSQLFieldListBuilder.class;
    }


    @Override
    protected Class<? extends MysqlJdbcFixtureBuilder> getJdbcFixtureBuilderImplementationClass() {
        return MysqlJdbcFixtureBuilder.class;
    }


    @Override
    protected Class<? extends DatabaseComparatorBuilder> getDatabaseComparatorBuilderImplementationClass() {
        return MysqlDatabaseComparatorBuilder.class;
    }


    @Override
    protected Class<? extends TableComparatorBuilder> getTableComparatorBuilderImplementationClass() {
        return DefaultTableComparatorBuilder.class;
    }
}
