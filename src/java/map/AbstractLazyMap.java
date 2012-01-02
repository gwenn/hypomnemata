package map;

import java.util.Map;

/**
 * See LazyMap in (<a href="http://commons.apache.org/collections/">Commons collections</a>).
 */
public abstract class AbstractLazyMap<K, V> extends AbstractMapDecorator<K, V> implements LazyMap<K, V> {
	protected AbstractLazyMap(Map<K, V> delegate) {
		super(delegate);
	}

	protected abstract V lazyCreate(K key);

	public V getOrCreate(K key) {
		V v = delegate.get(key);
		if (null == v) {
			v = lazyCreate(key);
			delegate.put(key, v);
		}
		return v;
	}
}
