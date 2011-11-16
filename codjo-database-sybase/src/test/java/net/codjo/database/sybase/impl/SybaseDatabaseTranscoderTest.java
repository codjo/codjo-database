package net.codjo.database.sybase.impl;
import static org.junit.Assert.assertEquals;
import org.junit.Test;
/**
 *
 */
public class SybaseDatabaseTranscoderTest {

    @Test
    public void test_constructor() throws Exception {
        SybaseDatabaseTranscoder transcoder = new SybaseDatabaseTranscoder();

        assertEquals("datetime", transcoder.transcodeSqlFieldType("timestamp"));
        assertEquals("int", transcoder.transcodeSqlFieldType("integer"));
        assertEquals("text", transcoder.transcodeSqlFieldType("longvarchar"));
        assertEquals("varchar", transcoder.transcodeSqlFieldType("varchar"));

        assertEquals("getdate()", transcoder.transcodeSqlFieldDefault("now"));
    }
}
