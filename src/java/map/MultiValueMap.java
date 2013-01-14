package map;

import java.util.Map;

/**
 * Because org.apache.commons.collections.map.MultiValueMap is not generic.
 * And because org.springframework.util.MultiValueMap is restricted to Map<K,List<V>>.
 * @param <K> Key type
 * @param <V> Single value type
 * @param <M> Multi value type (List, Set, Map, ...)
 */
public interface MultiValueMap<K,V,M> extends Map<K,M> { // TODO Add constraints on M?
  void add(K key, V value);
}
