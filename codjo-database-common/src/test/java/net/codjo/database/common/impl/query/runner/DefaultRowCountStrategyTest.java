package net.codjo.database.common.impl.query.runner;

import static net.codjo.database.common.impl.query.runner.DefaultRowCountStrategy.selectCountQueryFor;
import static net.codjo.test.common.matcher.JUnitMatchers.*;
import org.junit.Test;

public class DefaultRowCountStrategyTest {

    @Test
    public void test_selectCountQueryFor() throws Exception {
        assertThat(selectCountQueryFor(" select A, B from AP_TEST where TITI > ? "),
                   is("select count(1) from AP_TEST where TITI > ? "));
    }


    @Test
    public void test_selectCountQueryFor_storedProcedure() throws Exception {
        assertThat(selectCountQueryFor("call truc"), is(nullValue()));
    }


    @Test
    public void test_selectCountQueryFor_ambiguous() throws Exception {
        assertThat(
              selectCountQueryFor("select from_id, selected_stuff, next_select from AP_TEST where TITI > ? "),
              is("select count(1) from AP_TEST where TITI > ? "));
    }


    @Test
    public void test_selectCountQueryFor_subQuery() throws Exception {

        assertThat(selectCountQueryFor("select MY_ID as fromId, select max(My_ID) as idselect from AP_TEST"
                                       + " from AP_TEST where MY_ID > select X from TOTO"),
                   is("select count(1) from AP_TEST where MY_ID > select X from TOTO"));
    }


    @Test
    public void test_selectCountQueryFor_subQueryOfSubQuery() throws Exception {

        assertThat(selectCountQueryFor("select COL_A,"
                                       + "    select COL_B from ( select * from AP_I )"
                                       + " from AP_TEST"),
                   is("select count(1) from AP_TEST"));
    }
}
