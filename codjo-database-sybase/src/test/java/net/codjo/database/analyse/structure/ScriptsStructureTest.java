/*
 * Team : AGF AM / OSI / SI / BO
 *
 * Copyright (c) 2001 AGF Asset Management.
 */
package net.codjo.database.analyse.structure;
import junit.framework.TestCase;
/**
 *
 */
public class ScriptsStructureTest extends TestCase {
    public void test_isSameScriptStructure() throws Exception {
        assertScriptStructure(true,
            "-- un commentaire\ncreate view VU_ALL_TABLEA  as \n   select t1         ,\nt2  from TABLEA",
            "create view VU_ALL_TABLEA  as   \n      select t1,\nt2  from TABLEA");

        assertScriptStructure(false,
            "create view VU_ALL_TABLEA  as \n   select t1         ,t2  from TABLEA",
            "create view VU_ALL_TABLEA  as   \n      select t1,t2  from TABLEA");

        assertScriptStructure(false,
            "create view VU_ALL_TABLEA  as \n select t1,\nt2  from      TABLEA",
            "create view VU_ALL_TABLEA  as \n select t1,\nt2  from TABLEA");

        assertScriptStructure(false,
            "create view VU_ALL_TABLEA  as \n select *  from TABLE_B",
            "create view VU_ALL_TABLEA  as \n select *  from TABLE_a");

        assertScriptStructure(true,
            "-- mon commentaire\ncreate view VU_ALL_TABLEA as \n select * from TABLE_A",
            "create view VU_ALL_TABLEA as \n select * from TABLE_A");
        assertScriptStructure(true,
            "/* mon commentaire*/\ncreate view VU_ALL_TABLEA as \n select * from TABLE_A",
            "create view VU_ALL_TABLEA as \n select * from TABLE_A");
    }


    public void test_removeSomeInternalSpaces() throws Exception {
        assertEquals("qsdhgfkqsdj  fqdsfqf fqsdf    ",
            ScriptsStructure.removeSomeInternalSpaces("qsdhgfkqsdj  fqdsfqf fqsdf    "));
        assertEquals("qsdhgfkqsdj,     ",
            ScriptsStructure.removeSomeInternalSpaces("qsdhgfkqsdj,     "));
        assertEquals("xxxxxxx,",
            ScriptsStructure.removeSomeInternalSpaces("xxxxxxx     ,"));
        assertEquals("oooooooooo,,",
            ScriptsStructure.removeSomeInternalSpaces("oooooooooo,     ,"));
    }


    public void test_removeCommentBlocks() throws Exception {
        assertEquals("mon  script sans commentaires",
            ScriptsStructure.removeCommentBlocks(
                "mon /* un commentaire */ script sans commentaires"));

        assertEquals("", ScriptsStructure.removeCommentBlocks("/*mon commentaire */"));
        assertEquals("le mien a moi",
            ScriptsStructure.removeCommentBlocks("le mien /*mon commentaire "
                + "qui tient" + "plusieurs lignes */a moi"));
    }


    private void assertScriptStructure(boolean expected, String refStructure,
        String newStructure) {
        ScriptStructure refScriptStructure = new ScriptStructure();
        refScriptStructure.setScript(refStructure);

        ScriptStructure srcScriptStructure = new ScriptStructure();
        srcScriptStructure.setScript(newStructure);
        assertEquals(expected,
            ScriptsStructure.isSameScriptStructure(refScriptStructure, srcScriptStructure));
    }
}
