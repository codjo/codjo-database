package net.codjo.database.common.impl;
import net.codjo.database.common.api.confidential.DatabaseTranscoder;
import junit.framework.Assert;
import org.junit.Test;
/**
 *
 */
public class DefaultDatabaseTranscoderTest {
    @Test
    public void test_transcodeSqlFieldDefault() {
        DefaultDatabaseTranscoder databaseTranscoder = new DefaultDatabaseTranscoder();
        databaseTranscoder.addSqlFieldDefault(DatabaseTranscoder.NOW, "myNowFunction()");

        Assert.assertEquals("myNowFunction()",
                            databaseTranscoder.transcodeSqlFieldDefault(DatabaseTranscoder.NOW));
        Assert.assertEquals("other", databaseTranscoder.transcodeSqlFieldDefault("other"));
    }


    @Test
    public void test_transcodeSqlFieldType() {
        DefaultDatabaseTranscoder databaseTranscoder = new DefaultDatabaseTranscoder();
        databaseTranscoder.addSqlFieldType(DatabaseTranscoder.INTEGER, "myInteger");

        Assert.assertEquals("myInteger",
                            databaseTranscoder.transcodeSqlFieldType(DatabaseTranscoder.INTEGER));
        Assert.assertEquals("other", databaseTranscoder.transcodeSqlFieldType("other"));
    }
}
