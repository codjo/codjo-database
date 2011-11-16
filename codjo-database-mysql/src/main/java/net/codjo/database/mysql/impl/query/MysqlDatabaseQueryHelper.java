package net.codjo.database.mysql.impl.query;
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
import net.codjo.database.common.impl.query.builder.DefaultAlterTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultCreateConstraintQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultCreateIndexQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultCreateViewQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultDropTableQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultInsertQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultSelectQueryBuilder;
import net.codjo.database.common.impl.query.builder.DefaultUpdateQueryBuilder;
import net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import net.codjo.database.mysql.impl.query.builder.MysqlCreateTableQueryBuilder;
import net.codjo.database.mysql.impl.query.builder.MysqlDropConstraintQueryBuilder;
import java.sql.Connection;
import java.sql.SQLException;
public class MysqlDatabaseQueryHelper extends AbstractDatabaseQueryHelper {

    @Override
    protected AbstractSelectQueryBuilder newSelectQueryBuilder() {
        return new DefaultSelectQueryBuilder();
    }


    @Override
    protected AbstractDropTableQueryBuilder newDropTableQueryBuilder() {
        return new DefaultDropTableQueryBuilder();
    }


    @Override
    protected AbstractDropConstraintQueryBuilder newDropConstraintQueryBuilder() {
        return new MysqlDropConstraintQueryBuilder();
    }


    @Override
    protected AbstractCreateTableQueryBuilder newCreateTableQueryBuilder() {
        return new MysqlCreateTableQueryBuilder();
    }


    @Override
    protected AbstractInsertQueryBuilder newInsertQueryBuilder() {
        return new DefaultInsertQueryBuilder();
    }


    @Override
    protected AbstractUpdateQueryBuilder newUpdateQueryBuilder() {
        return new DefaultUpdateQueryBuilder();
    }


    @Override
    protected AbstractCreateIndexQueryBuilder newCreateIndexQueryBuilder() {
        return new DefaultCreateIndexQueryBuilder();
    }


    @Override
    protected AbstractCreateViewQueryBuilder newCreateViewQueryBuilder() {
        return new DefaultCreateViewQueryBuilder();
    }


    @Override
    protected AbstractAlterTableQueryBuilder newAlterTableQueryBuilder() {
        return new DefaultAlterTableQueryBuilder();
    }


    @Override
    protected AbstractCreateConstraintQueryBuilder newCreateConstraintQueryBuilder() {
        return new DefaultCreateConstraintQueryBuilder();
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
