package map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class ListValueMap<K,V> extends AbstractMultiValueMap<K, V, List<V>> {
  public static <K,V> ListValueMap<K,V> createLinked() {
    return new ListValueMap<K,V>(new LinkedHashMap<K,List<V>>()) {
      @Override
      protected List<V> create() {
        return new LinkedList<V>();
      }
    };
  }
  public static <K,V> ListValueMap<K,V> createArray() {
    return new ListValueMap<K,V>(new HashMap<K,List<V>>()) {
      @Override
      protected List<V> create() {
        return new ArrayList<V>();
      }
    };
  }
  public ListValueMap(Map<K, List<V>> delegate) {
    super(delegate);
  }
  @Override
  protected void append(List<V> values, V value) {
    values.add(value);
  }
}
