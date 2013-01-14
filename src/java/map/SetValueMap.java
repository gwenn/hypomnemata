package map;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

public abstract class SetValueMap<K,V> extends AbstractMultiValueMap<K,V,Set<V>> {
  public static <K,V> SetValueMap<K,V> createHash() {
    return new SetValueMap<K,V>(new HashMap<K,Set<V>>()) {
      @Override
      protected Set<V> create() {
        return new HashSet<V>();
      }
    };
  }
  public static <K,V> SetValueMap<K,V> createTree() {
    return new SetValueMap<K,V>(new TreeMap<K,Set<V>>()) {
      @Override
      protected Set<V> create() {
        return new TreeSet<V>();
      }
    };
  }
  public SetValueMap(Map<K, Set<V>> delegate) {
    super(delegate);
  }
  @Override
  protected void append(Set<V> values, V value) {
    values.add(value);
  }
}
