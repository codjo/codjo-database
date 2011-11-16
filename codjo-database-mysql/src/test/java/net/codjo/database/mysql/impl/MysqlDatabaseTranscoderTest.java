package net.codjo.database.mysql.impl;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 *
 */
public class MysqlDatabaseTranscoderTest {

    @Test
    public void test_transcodeSqlFieldType() throws Exception {
        MysqlDatabaseTranscoder transcoder = new MysqlDatabaseTranscoder();

        assertEquals("timestamp", transcoder.transcodeSqlFieldType("timestamp"));
        assertEquals("int", transcoder.transcodeSqlFieldType("integer"));
        assertEquals("text", transcoder.transcodeSqlFieldType("longvarchar"));
        assertEquals("varchar", transcoder.transcodeSqlFieldType("varchar"));

        assertEquals("CURRENT_TIMESTAMP()", transcoder.transcodeSqlFieldDefault("now"));
    }
}