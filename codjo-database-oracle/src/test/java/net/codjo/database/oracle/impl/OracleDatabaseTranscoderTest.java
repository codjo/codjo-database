package net.codjo.database.oracle.impl;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
public class OracleDatabaseTranscoderTest {

    @Test
    public void test_constructor() throws Exception {
        OracleDatabaseTranscoder transcoder = new OracleDatabaseTranscoder();

        assertEquals("timestamp", transcoder.transcodeSqlFieldType("datetime"));
        assertEquals("integer", transcoder.transcodeSqlFieldType("integer"));
        assertEquals("clob", transcoder.transcodeSqlFieldType("longvarchar"));
        assertEquals("varchar", transcoder.transcodeSqlFieldType("varchar"));

        assertEquals("systimestamp", transcoder.transcodeSqlFieldDefault("now"));
    }
}