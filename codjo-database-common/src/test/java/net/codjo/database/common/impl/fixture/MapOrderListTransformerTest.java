package net.codjo.database.common.impl.fixture;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.TreeSet;
import org.junit.Assert;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import org.junit.Test;
public class MapOrderListTransformerTest {

    @Test
    public void test_comparator() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("A -> C;"
                                              + "C -> D;"
                                              + "D -> B;"
                                              + "E -> F;"));

        transformer.transform(list);

        assertBefore("A", "C", list);
        assertBefore("C", "D", list);
        assertBefore("D", "B", list);
        assertBefore("E", "F", list);

        assertBefore("A", "D", list);
        assertBefore("A", "B", list);
    }


    @Test
    public void test_comparator_notCaseSensitive() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "b", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("a -> c;"
                                              + "c -> d;"
                                              + "d -> B;"
                                              + "e -> f;"
                                              + "x -> z"));

        transformer.transform(list);

        assertBefore("A", "C", list);
        assertBefore("C", "D", list);
        assertBefore("D", "b", list);
        assertBefore("E", "F", list);
        assertBefore("A", "D", list);
        assertBefore("A", "b", list);
    }


    @Test
    public void test_comparator_a_lot_of_father() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("A -> C,D;"));

        transformer.transform(list);

        assertBefore("A", "C", list);
        assertBefore("A", "D", list);
    }


    @Test
    public void test_comparator_unrelated() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("A -> C;"
                                              + "C -> D;"
                                              + "D -> B;"
                                              + "E -> F;"));

        transformer.transform(list);

        assertBefore("Q", "F", list);
    }


    @Test
    public void test_twoBranch() {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("A -> B;"
                                              + "C -> B;"
                                              + "D -> C;"
                                              + "F -> E;"));

        transformer.transform(list);

        assertBefore("A", "B", list);
        assertBefore("C", "B", list);
        assertBefore("D", "B", list);
        assertBefore("D", "C", list);
        assertBefore("F", "E", list);
    }


    @Test
    public void test_longTreeMap() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer =
              new MapOrderListTransformer(map("A -> B;"
                                              + "B -> C;"
                                              + "C -> D;"
                                              + "D -> E;"
                                              + "E -> F;"));

        transformer.transform(list);

        assertBefore("A", "B", list);
        assertBefore("B", "C", list);
        assertBefore("C", "D", list);
        assertBefore("D", "E", list);
        assertBefore("E", "F", list);
    }


    @Test
    public void test_noDependancy() throws Exception {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        MapOrderListTransformer transformer = new MapOrderListTransformer(
              Collections.<String, List<String>>emptyMap());

        transformer.transform(list);

        assertEquals("La liste ne doit pas être modifiée s'il n'a pas de contraintes",
                     toList("C", "Q", "F", "D", "B", "E", "A"), list);
    }


    @Test
    public void test_notInList() {
        List<String> list = toList("C", "Q", "F", "D", "B", "E", "A");

        Map<String, List<String>> result = map("A -> C;"
                                               + "C -> D;"
                                               + "D -> B;"
                                               + "E -> F;"
                                               + "B -> ;");
        MapOrderListTransformer transformer = new MapOrderListTransformer(result);

        transformer.transform(list);

        assertBefore("A", "C", list);
        assertBefore("C", "D", list);
        assertBefore("D", "B", list);
        assertBefore("E", "F", list);

        assertBefore("A", "D", list);
        assertBefore("A", "B", list);
    }


    @Test
    public void test_transform_sameListSizeAfter() throws Exception {
        List<String> tableNames = new ArrayList<String>(Arrays.asList("A", "B", "C"));
        Map<String, List<String>> sonToFathers = new HashMap<String, List<String>>();
        sonToFathers.put("B", Arrays.asList("A", "C"));

        MapOrderListTransformer transformer = new MapOrderListTransformer(sonToFathers);
        transformer.transform(tableNames);

        Assert.assertEquals(3, new TreeSet<String>(tableNames).size());
    }


    private void assertBefore(String first, String second, List<String> list) {
        assertTrue(first + " before " + second + " in " + list,
                   list.indexOf(first) < list.indexOf(second));
    }


    private List<String> toList(String... elements) {
        return new LinkedList<String>(Arrays.asList(elements));
    }


    private Map<String, List<String>> map(String relations) {
        Map<String, List<String>> result = new HashMap<String, List<String>>();

        for (StringTokenizer tokenizer = new StringTokenizer(relations, ";");
             tokenizer.hasMoreTokens();) {
            String relation = tokenizer.nextToken();
            int separatorIndex = relation.indexOf("->");
            String son = relation.substring(0, separatorIndex).trim();
            String[] fatherList = relation.substring(separatorIndex + 2, relation.length()).split(",");
            for (int i = 0; i < fatherList.length; i++) {
                fatherList[i] = fatherList[i].trim();
            }
            result.put(son, Arrays.asList(fatherList));
        }

        return result;
    }
}
