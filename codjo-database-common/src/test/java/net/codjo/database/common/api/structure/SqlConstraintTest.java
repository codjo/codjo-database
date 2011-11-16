package net.codjo.database.common.api.structure;
import static net.codjo.database.common.api.structure.SqlConstraint.constraintName;
import static net.codjo.database.common.api.structure.SqlField.fieldName;
import net.codjo.test.common.matcher.JUnitMatchers;
import java.util.List;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.junit.Before;
import org.junit.Test;
public class SqlConstraintTest {
    private SqlConstraint constraint;


    @Before
    public void setUp() {
        constraint = constraintName("FK_MY_CONSTRAINT");
    }


    @Test
    public void test_linkFields() throws Exception {
        constraint.addLinkedFields(fieldName("FROM_FIELD"), fieldName("TO_FIELD"));
        constraint.addLinkedFields(fieldName("FROM_FIELD_2"), fieldName("TO_FIELD_2"));

        List<SqlField[]> linkedFields = constraint.getLinkedFields();

        JUnitMatchers.assumeThat(linkedFields.get(0), new LinkedFieldsMatcher("FROM_FIELD", "TO_FIELD"));
        JUnitMatchers.assumeThat(linkedFields.get(1), new LinkedFieldsMatcher("FROM_FIELD_2", "TO_FIELD_2"));
    }


    private static class LinkedFieldsMatcher extends BaseMatcher<SqlField[]> {
        private String fromFieldName;
        private String toFieldName;


        private LinkedFieldsMatcher(String fromFieldName, String toFieldName) {
            this.fromFieldName = fromFieldName;
            this.toFieldName = toFieldName;
        }


        public boolean matches(Object obj) {
            SqlField[] fields = (SqlField[])obj;
            return fromFieldName.equals(fields[0].getName()) && toFieldName.equals(fields[1].getName());
        }


        public void describeTo(Description description) {
        }
    }
}
