package net.codjo.database.common.impl.sqlfield;
import net.codjo.database.common.api.SQLFieldList;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
public class DefaultSQLFieldListTest {
    private SQLFieldList sqlFieldList;


    @Before
    public void setUp() {
        sqlFieldList = new DefaultSQLFieldList();
    }


    @Test
    public void test_clear() {
        sqlFieldList.addStringField("a");
        sqlFieldList.setFieldValue("a", "valA");
        assertEquals(sqlFieldList.getFieldValue("a"), "valA");

        sqlFieldList.clearValues();

        assertEquals(sqlFieldList.getFieldValue("a"), null);
    }


    @Test
    public void test_addAll() {
        sqlFieldList.addStringField("a");
        sqlFieldList.setFieldValue("a", "listA_valA");
        sqlFieldList.addStringField("c");
        sqlFieldList.setFieldValue("c", "listA_valC");

        DefaultSQLFieldList listB = new DefaultSQLFieldList();
        listB.addStringField("a");
        listB.setFieldValue("a", "listB_valA");
        listB.addStringField("b");
        listB.setFieldValue("b", "listB_valB");

        sqlFieldList.addAll(listB);
        assertEquals(sqlFieldList.getFieldValue("a"), "listB_valA");
        assertEquals(sqlFieldList.getFieldValue("b"), "listB_valB");
        assertEquals(sqlFieldList.getFieldValue("c"), "listA_valC");
    }


    @Test(expected = Exception.class)
    public void test_removeField() {
        sqlFieldList.addStringField("a");
        sqlFieldList.addStringField("b");
        sqlFieldList.setFieldValue("a", "valA");
        assertEquals(sqlFieldList.getFieldValue("a"), "valA");

        sqlFieldList.removeField("a");

        sqlFieldList.getFieldValue("a");
    }
}
