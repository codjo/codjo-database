package net.codjo.database.common.api;
import net.codjo.database.common.api.structure.SqlObject;
import net.codjo.database.common.api.structure.SqlTable;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
public abstract class SqlMatcher<T extends SqlObject> extends BaseMatcher<T> {
    private JdbcFixture jdbcFixture;


    public static SqlMatcher<SqlTable> isEmpty() {
        return new IsEmpty();
    }


    public static SqlMatcher<SqlTable> exists() {
        return new IsEmpty();
    }


    public void setSqlFixture(JdbcFixture neoSqlFixture) {
        this.jdbcFixture = neoSqlFixture;
    }


    private static class IsEmpty extends SqlMatcher<SqlTable> {
        public boolean matches(Object object) {
            return false;  // Todo
        }


        public void describeTo(Description description) {
            // Todo
        }
    }
}
