package net.codjo.database.analyse;
import java.util.Collection;
import java.util.List;
import static org.junit.Assert.fail;
/**
 *
 */
public class TestUtil {
    private TestUtil() {
    }


    public static void assertObjectExistsInList(String objectName, Collection<String> refObjectNames) {
        for (String refObjectName : refObjectNames) {
            if (refObjectName.equals(objectName)) {
                return;
            }
        }
        fail("Can't found '" + objectName + "' in list");
    }


    public static void assertObjectNotExistsInList(String objectName, Collection<String> refObjectNames) {
        for (String refObjectName : refObjectNames) {
            if (refObjectName.equals(objectName)) {
                fail("Found '" + objectName + "' in list");
            }
        }
    }


    public static void assertObjectExistsInListOfList(String objectName,
                                                      Collection<List<String>> refObjectNames) {
        for (List<String> subList : refObjectNames) {
            for (String field : subList) {
                if (field != null && field.equals(objectName)) {
                    return;
                }
            }
        }
        fail("Can't found '" + objectName + "' in list");
    }


    public static void assertObjectNotExistsInListOfList(String objectName,
                                                         Collection<List<String>> refObjectNames) {
        for (List<String> subList : refObjectNames) {
            for (String field : subList) {
                if (field != null && field.equals(objectName)) {
                    fail("Found '" + objectName + "' in list");
                }
            }
        }
    }
}
