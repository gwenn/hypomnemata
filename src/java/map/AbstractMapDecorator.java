package map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

// Inspired from org.apache.commons.collections.map.AbstractMapDecorator
public class AbstractMapDecorator<K, V> implements Map<K, V> {
	protected final Map<K, V> delegate;

	protected AbstractMapDecorator(Map<K, V> delegate) {
		if (null == delegate) {
			throw new IllegalArgumentException("delegate must not be null!");
		}
		this.delegate = delegate;
	}

	public int size() {
		return delegate.size();
	}

	public boolean isEmpty() {
		return delegate.isEmpty();
	}

	public boolean containsKey(Object key) {
		return delegate.containsKey(key);
	}

	public boolean containsValue(Object value) {
		return delegate.containsValue(value);
	}

	public V get(Object key) {
		return delegate.get(key);
	}

	public V put(K key, V value) {
		return delegate.put(key, value);
	}

	public V remove(Object key) {
		return delegate.remove(key);
	}

	public void putAll(Map<? extends K, ? extends V> m) {
		delegate.putAll(m);
	}

	public void clear() {
		delegate.clear();
	}

	public Set<K> keySet() {
		return delegate.keySet();
	}

	public Collection<V> values() {
		return delegate.values();
	}

	public Set<Map.Entry<K, V>> entrySet() {
		return delegate.entrySet();
	}

	@Override
	public String toString() {
		return delegate.toString();
	}
}
