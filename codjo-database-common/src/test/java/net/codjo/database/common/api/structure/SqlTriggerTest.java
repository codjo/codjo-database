package net.codjo.database.common.api.structure;
import static net.codjo.database.common.api.structure.SqlField.fields;
import static net.codjo.database.common.api.structure.SqlTable.table;
import static net.codjo.database.common.api.structure.SqlTrigger.checkRecordTrigger;
import junit.framework.TestCase;
import org.junit.Test;
public class SqlTriggerTest extends TestCase {

    @Test
    public void test_checkRecord() throws Exception {
        SqlTrigger trigger = checkRecordTrigger("TR_AP_TOTO_IU", table("AP_TOTO"));
        trigger.addTableLink(table("AP_MERETOTO"), fields("ID_TOTO", "ID_MERETOTO"));
        trigger.addTableLink(table("AP_PERETOTO"), fields("ID_TOTO", "ID_PERETOTO"));

        assertEquals("AP_TOTO", trigger.getTable().getName());

        assertEquals("AP_MERETOTO", trigger.getLinkedTable(0).getName());
        assertEquals("ID_TOTO", trigger.getLinkedFields(0, 0)[0].getName());
        assertEquals("ID_MERETOTO", trigger.getLinkedFields(0, 0)[1].getName());

        assertEquals("AP_PERETOTO", trigger.getLinkedTable(1).getName());
        assertEquals("ID_TOTO", trigger.getLinkedFields(1, 0)[0].getName());
        assertEquals("ID_PERETOTO", trigger.getLinkedFields(1, 0)[1].getName());
    }
}
