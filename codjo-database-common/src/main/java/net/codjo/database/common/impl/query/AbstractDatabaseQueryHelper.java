package net.codjo.database.common.impl.query;
import net.codjo.database.common.api.DatabaseQueryHelper;
import net.codjo.database.common.api.confidential.query.PreparedSelectQuery;
import net.codjo.database.common.api.confidential.query.SelectQuery;
import net.codjo.database.common.api.structure.SqlConstraint;
import net.codjo.database.common.api.structure.SqlField;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import net.codjo.database.common.api.structure.SqlIndex;
import net.codjo.database.common.api.structure.SqlTable;
import net.codjo.database.common.api.structure.SqlView;
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
import net.codjo.database.common.impl.query.runner.DefaultPreparedSelectQuery;
import net.codjo.database.common.impl.query.runner.DefaultSelectQuery;
import net.codjo.database.common.impl.query.runner.RowCountStrategy;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
public abstract class AbstractDatabaseQueryHelper implements DatabaseQueryHelper,
                                                             DatabaseQueryHelper.Confidential {

    public final String buildSelectQuery(SqlTable sqlTable, String... columns) {
        return buildSelectQuery(sqlTable, columnsToSqlFields(columns));
    }


    public final String buildSelectQuery(SqlTable sqlTable, SqlField... sqlFields) {
        AbstractSelectQueryBuilder select = newSelectQueryBuilder();
        if (sqlFields.length == 0) {
            select.allFields();
        }
        else {
            select.fields(sqlFields);
        }
        select.fromTable(sqlTable);
        return select.get();
    }


    public final String buildSelectQuery(SqlTable sqlTable, SelectType selectType) {
        AbstractSelectQueryBuilder select = newSelectQueryBuilder();
        switch (selectType) {
            case ONE:
                select.oneField();
                break;

            case ALL:
                select.allFields();
                break;
        }
        select.fromTable(sqlTable);
        return select.get();
    }


    /**
     * @deprecated use buildCreateTableQuery instead.
     */
    @Deprecated
    public final String buildCreateQuery(SqlTable sqlTable, String content) {
        return buildCreateTableQuery(sqlTable, content);
    }


    public final String buildCreateTableQuery(SqlTable sqlTable, String content) {
        return newCreateTableQueryBuilder()
              .table(sqlTable)
              .withContent(content).get();
    }


    public String buildDropTableQuery(SqlTable sqlTable, boolean checkExists) {
        return buildDropTableQuery(sqlTable);
    }


    public String buildDropTableQuery(SqlTable sqlTable) {
        return newDropTableQueryBuilder()
              .table(sqlTable).get();
    }


    public final String buildInsertQuery(SqlTable sqlTable, String... columns) {
        return buildInsertQuery(sqlTable, columnsToSqlFields(columns));
    }


    public final String buildInsertQuery(SqlTable sqlTable, SqlField... sqlFields) {
        return newInsertQueryBuilder()
              .intoTable(sqlTable)
              .fields(sqlFields).get();
    }


    public final String buildUpdateQuery(SqlTable sqlTable, String... columns) {
        return buildUpdateQuery(sqlTable, columnsToSqlFields(columns));
    }


    public final String buildUpdateQuery(SqlTable sqlTable, SqlField... sqlFields) {
        AbstractUpdateQueryBuilder update = newUpdateQueryBuilder().table(sqlTable);
        for (SqlField sqlField : sqlFields) {
            update.setFields(sqlField);
        }
        return update.get();
    }


    public String buildCreateIndexQuery(SqlIndex sqlIndex) {
        return newCreateIndexQueryBuilder().index(sqlIndex).get();
    }


    public String buildCreateViewQuery(SqlView sqlView) {
        return newCreateViewQueryBuilder().view(sqlView).get();
    }


    public String buildDropConstraintQuery(SqlConstraint sqlConstraint) {
        return newDropConstraintQueryBuilder().foreignKey(sqlConstraint).get();
    }


    public String buildCreateConstraintQuery(SqlConstraint sqlConstraint) {
        return newCreateConstraintQueryBuilder().constraint(sqlConstraint).get();
    }


    public boolean hasDeleteRowStrategyOnTemporaryTable() {
        return false;
    }


    protected abstract AbstractSelectQueryBuilder newSelectQueryBuilder();


    protected abstract AbstractDropTableQueryBuilder newDropTableQueryBuilder();


    protected abstract AbstractCreateTableQueryBuilder newCreateTableQueryBuilder();


    protected abstract AbstractAlterTableQueryBuilder newAlterTableQueryBuilder();


    protected abstract AbstractInsertQueryBuilder newInsertQueryBuilder();


    protected abstract AbstractUpdateQueryBuilder newUpdateQueryBuilder();


    protected abstract AbstractCreateViewQueryBuilder newCreateViewQueryBuilder();


    protected abstract AbstractCreateIndexQueryBuilder newCreateIndexQueryBuilder();


    protected abstract AbstractDropConstraintQueryBuilder newDropConstraintQueryBuilder();


    protected abstract AbstractCreateConstraintQueryBuilder newCreateConstraintQueryBuilder();


    private SqlField[] columnsToSqlFields(String... columns) {
        List<SqlField> sqlFields = new ArrayList<SqlField>();
        for (String column : columns) {
            sqlFields.add(fieldName(column));
        }
        return sqlFields.toArray(new SqlField[0]);
    }


    public Confidential confidential() {
        return this;
    }


    public SelectQuery createSelectQuery(Connection connection, String sqlSelectQuery) throws SQLException {
        return new DefaultSelectQuery(connection,
                                      sqlSelectQuery,
                                      createRowCountStrategy(connection, sqlSelectQuery));
    }


    public PreparedSelectQuery createPreparedSelectQuery(Connection connection, String sqlSelectQuery)
          throws SQLException {
        return new DefaultPreparedSelectQuery(connection,
                                              sqlSelectQuery,
                                              createRowCountStrategy(connection, sqlSelectQuery));
    }


    protected abstract RowCountStrategy createRowCountStrategy(Connection connection, String sqlSelectQuery)
          throws SQLException;
}
