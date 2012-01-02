package map;

import java.util.Map;

public interface LazyMap<K, V> extends Map<K, V> {
	V getOrCreate(K key);
}
