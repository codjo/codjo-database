package net.codjo.database.oracle.impl.query;
import java.sql.Connection;
import java.sql.SQLException;
import net.codjo.database.common.impl.query.AbstractDatabaseQueryHelper;
import net.codjo.database.common.impl.query.builder.AbstractAlterTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateIndexQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractCreateViewQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractDropConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractDropTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractInsertQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractSelectQueryBuilder;
import net.codjo.database.common.impl.query.builder.AbstractUpdateQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultCreateViewQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultDropConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultDropTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultSelectQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultUpdateQueryBuilder;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import net.codjo.database.oracle.impl.query.builder.OracleCreateConstraintQueryBuilder;
import net.codjo.database.oracle.impl.query.builder.OracleCreateIndexQueryBuilder;
import net.codjo.database.oracle.impl.query.builder.OracleCreateTableQueryBuilder;
import net.codjo.database.oracle.impl.query.builder.OracleInsertQueryBuilder;
public class OracleDatabaseQueryHelper extends AbstractDatabaseQueryHelper {

    @Override
    protected AbstractSelectQueryBuilder newSelectQueryBuilder() {
        return new DefaultSelectQueryBuilder();
    }


    @Override
    protected AbstractDropTableQueryBuilder newDropTableQueryBuilder() {
        return new DefaultDropTableQueryBuilder();
    }


    @Override
    protected AbstractCreateTableQueryBuilder newCreateTableQueryBuilder() {
        return new OracleCreateTableQueryBuilder();
    }


    @Override
    protected AbstractAlterTableQueryBuilder newAlterTableQueryBuilder() {
        throw new UnsupportedOperationException();// todo newAlterTableQueryBuilder
    }


    @Override
    protected AbstractInsertQueryBuilder newInsertQueryBuilder() {
        return new OracleInsertQueryBuilder();
    }


    @Override
    protected AbstractUpdateQueryBuilder newUpdateQueryBuilder() {
        return new DefaultUpdateQueryBuilder();
    }


    @Override
    protected AbstractCreateViewQueryBuilder newCreateViewQueryBuilder() {
        return new DefaultCreateViewQueryBuilder();
    }


    @Override
    protected AbstractCreateIndexQueryBuilder newCreateIndexQueryBuilder() {
        return new OracleCreateIndexQueryBuilder();
    }


    @Override
    protected AbstractDropConstraintQueryBuilder newDropConstraintQueryBuilder() {
        return new DefaultDropConstraintQueryBuilder();
    }


    @Override
    protected AbstractCreateConstraintQueryBuilder newCreateConstraintQueryBuilder() {
        return new OracleCreateConstraintQueryBuilder();
    }


    @Override
    protected RowCountStrategy createRowCountStrategy(Connection connection, String sqlSelectQuery)
          throws SQLException {
        return new DefaultRowCountStrategy(connection, sqlSelectQuery);
    }


    @Deprecated
    public int computeRowCount(Connection connection, int readRowCount) throws SQLException {
        return readRowCount + 1;
    }
}
