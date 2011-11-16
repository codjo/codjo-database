package net.codjo.database.common.impl.fixture;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
public class MapOrderListTransformer {
    private Map<String, List<String>> map;


    public MapOrderListTransformer(Map<String, List<String>> map) {
        this.map = map;
    }


    public void transform(List<String> list) {
        boolean changed = false;

        for (Entry<String, List<String>> entry : map.entrySet()) {
            String predecessor = entry.getKey();
            int predecessorIndex = indexOf(list, predecessor);
            if (predecessorIndex != -1) {
                predecessor = list.get(predecessorIndex);
            }

            List<String> successors = entry.getValue();
            Iterator<String> succIter = successors.iterator();
            int minIndex = predecessorIndex;
            while (succIter.hasNext()) {
                int successorIndex = indexOf(list, succIter.next());

                if (successorIndex != -1) {
                    minIndex = Math.min(minIndex, successorIndex);
                }
            }

            if (minIndex < predecessorIndex) {
                list.remove(predecessorIndex);
                list.add(minIndex, predecessor);

                changed = true;
            }
        }

        if (changed) {
            transform(list);
        }
    }


    private int indexOf(List<String> list, String element) {
        for (int i = 0; i < list.size(); i++) {
            if (element.equalsIgnoreCase(list.get(i))) {
                return i;
            }
        }
        return -1;
    }
}
