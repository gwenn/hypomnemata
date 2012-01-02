package cache;

import java.util.Collection;
import java.util.Map;
import java.util.Set;


/**
 * Check <a href="http://jsr107cache.sourceforge.net/">JSR107 project</a>?
 * @param <K> Key type
 * @param <V> Value type
 * @param <C> Criteria type
 */
public interface Cache<K, V, C> {
	String getName();

	String criteriaAsString();
	void invalidate();

	int size();
	boolean isEmpty();

	boolean containsKey(K key);
	V get(K key);

	Set<K> keySet();
	Collection<V> values();
	Set<Map.Entry<K, V>> entrySet();

	void addListener(CacheListener l);
	void removeListener(CacheListener l);
}
