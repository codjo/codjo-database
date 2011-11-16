package net.codjo.database.sybase.impl.query;
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
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import net.codjo.database.sybase.impl.query.builder.SybaseAlterTableQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseCreateConstraintQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseCreateIndexQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseCreateTableQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseDropConstraintQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseDropTableQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseInsertQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseSelectQueryBuilder;
import net.codjo.database.sybase.impl.query.builder.SybaseUpdateQueryBuilder;
import net.codjo.database.sybase.impl.query.runner.SybaseRowCountStrategy;
import java.sql.Connection;
import java.sql.SQLException;
public class SybaseDatabaseQueryHelper extends AbstractDatabaseQueryHelper {

    @Override
    protected AbstractSelectQueryBuilder newSelectQueryBuilder() {
        return new SybaseSelectQueryBuilder();
    }


    @Override
    protected AbstractCreateTableQueryBuilder newCreateTableQueryBuilder() {
        return new SybaseCreateTableQueryBuilder();
    }


    @Override
    protected AbstractDropTableQueryBuilder newDropTableQueryBuilder() {
        return new SybaseDropTableQueryBuilder();
    }


    @Override
    protected AbstractDropConstraintQueryBuilder newDropConstraintQueryBuilder() {
        return new SybaseDropConstraintQueryBuilder();
    }


    @Override
    protected AbstractInsertQueryBuilder newInsertQueryBuilder() {
        return new SybaseInsertQueryBuilder();
    }


    @Override
    protected AbstractUpdateQueryBuilder newUpdateQueryBuilder() {
        return new SybaseUpdateQueryBuilder();
    }


    @Override
    protected AbstractCreateIndexQueryBuilder newCreateIndexQueryBuilder() {
        return new SybaseCreateIndexQueryBuilder();
    }


    @Override
    protected AbstractCreateViewQueryBuilder newCreateViewQueryBuilder() {
        return new DefaultCreateViewQueryBuilder();
    }


    @Override
    protected AbstractAlterTableQueryBuilder newAlterTableQueryBuilder() {
        return new SybaseAlterTableQueryBuilder();
    }


    @Override
    protected AbstractCreateConstraintQueryBuilder newCreateConstraintQueryBuilder() {
        return new SybaseCreateConstraintQueryBuilder();
    }


    @Override
    protected RowCountStrategy createRowCountStrategy(Connection connection, String sqlSelectQuery)
          throws SQLException {
        return new SybaseRowCountStrategy(connection);
    }


    @Deprecated
    public int computeRowCount(Connection connection, int readRowCount) throws SQLException {
        return SybaseRowCountStrategy.computeRowCount(connection);
    }
}
