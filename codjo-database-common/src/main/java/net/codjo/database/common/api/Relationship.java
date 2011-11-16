package net.codjo.database.common.api;
import java.util.List;
import java.util.Map;
public interface Relationship {
    Map<String, List<String>> getSonToFather();


    Map<String, List<String>> getFatherToSon();


    void declare(String son, String father);
}
