package net.codjo.database.common.impl.relation;
import net.codjo.database.common.api.Relationship;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class DefaultRelationship implements Relationship {
    private Map<String, List<String>> sonToFather = new HashMap<String, List<String>>();
    private Map<String, List<String>> fatherToSon = new HashMap<String, List<String>>();


    public Map<String, List<String>> getSonToFather() {
        return sonToFather;
    }


    public Map<String, List<String>> getFatherToSon() {
        return fatherToSon;
    }


    public void declare(String son, String father) {
        declareToMap(sonToFather, son, father);
        declareToMap(fatherToSon, father, son);
    }


    private void declareToMap(Map<String, List<String>> relations, String key, String value) {
        List<String> list = relations.get(key);

        if (null == list) {
            list = new ArrayList<String>();
            relations.put(key, list);
        }

        list.add(value);
    }
}
