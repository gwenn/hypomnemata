package cache;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * <ul>
 * <li>To clean the cache state (cache values) at each call to {@link #setCriteria} even when not invalidated (same criteria),
 * you just have to implement the interface {@link Cleanable}.
 * </li>
 * <li>To clean the cache state (child attributes) <em>at each invalidation/reconstruction</em> (at each call to {@link #populate}),
 * you must add your own listener listener:
 * <blockquote><pre>
 * addListener(new AbstractCacheListener() {
 * 	public void populating(CacheEvent e) {
 * 	}
 * });
 * </pre></blockquote>
 * </li>
 * </ul>
 */
public abstract class AbstractCache<K, V, C> implements Cache<K, V, C> {
	private final Map<K, V> cache;
	private final Map<K, V> unmodifiableCache;
	private State state = State.INITIALIZED;
	private final List<CacheListener> listeners = Collections.synchronizedList(new ArrayList<CacheListener>());
	private C criteria;
	private final CleanListener cleanListener;

	public AbstractCache() {
		this.cache = createMap();
		this.unmodifiableCache = Collections.unmodifiableMap(cache);
		if (this instanceof Cleanable) {
			cleanListener = new CleanListener();
			addListener(cleanListener);
		} else {
			cleanListener = null;
		}
	}
	
	protected abstract Map<K, V> createMap();
	protected abstract void populate();

	protected final Map<K, V> getUnmodifiableCache() {
		check();
		return unmodifiableCache;
	}

	protected final void clear() {
		cache.clear();
		invalidate();
	}
	protected final V put(K key, V value) {
		if (state != State.POPULATING) {
			throw new IllegalStateException("Cache cannot be altered after being populated.");
		}
		return putUnsafe(key, value);
	}
	// Access with no check
	protected final V putUnsafe(K key, V value) {
		return cache.put(key, value);
	}

	protected final void check() {
		if (state == State.POPULATED) {
			return;
		} else if (state == State.INITIALIZED) {
			throw new IllegalStateException("Cache has not been configured yet.");
		} else if (state == State.POPULATING) {
			throw new IllegalStateException();
		} else {
			setState(State.POPULATING);
			populate();
			setState(State.POPULATED);
		}
	}

	protected final void setCriteria(C criteria) {
		if (state == State.POPULATING) {
			throw new IllegalStateException();
		}
		if (this.criteria != criteria && (this.criteria == null || ! this.criteria.equals(criteria))) {
			this.criteria = criteria; // TODO clone?
			setState(State.CONFIGURED);
		}
		if (null != cleanListener && cleanListener.shouldBeClean) {
			((Cleanable) this).clean();
		}
	}
	// The returned value should not be altered.
	protected final C getCriteria() {
		return criteria;
	}

	public final void invalidate() {
		if (state != State.POPULATING) {
			setState(State.INVALIDATED);
		}
	}

	public final int size() {
		check();
		return cache.size();
	}

	public final boolean isEmpty() {
		check();
		return cache.isEmpty();
	}

	public final boolean containsKey(K key) {
		check();
		return containsKeyUnsafe(key);
	}
	// Access with no check
	protected final boolean containsKeyUnsafe(K key) {
		return cache.containsKey(key);
	}

	public final V get(K key) {
		check();
		V v = cache.get(key);
		if (v == null) {
			v = getDefault(key);
		} else {
			v = wrap(v);
		}
		return v;
	}

	// Indirection to decorate/protect the returned value.
	protected V wrap(V v) {
		return v;
	}

	// Indirection to return a default value.
	protected V getDefault(K key) {
		return null;
	}

	// Indirection to return a defaut value while populating the cache.
	// The {@link #createValue} method must be overriden accordingly.
	protected final V getOrCreate(K key) {
		V v = getUnsafe(key);
		if (v == null) {
			v = createValue(key);
			put(key, v);
		}
		return v;
	}
	protected V createValue(K key) {
		throw new UnsupportedOperationException();
	}

	// Access with no check
	protected final V getUnsafe(K key) {
		return cache.get(key);
	}

	public final Set<K> keySet() {
		check();
		return unmodifiableCache.keySet();
	}

	public final Collection<V> values() {
		check();
		return unmodifiableCache.values();
	}

	public final Set<Map.Entry<K, V>> entrySet() {
		check();
		return unmodifiableCache.entrySet();
	}

	private void setState(State state) {
		if (this.state != state) {
			this.state = state;
			fireEvent(state);
		}
	}

	private void fireEvent(State newState) {
		synchronized (listeners) {
			final CacheEvent evt = new CacheEvent(this);
			for (CacheListener listener : listeners) {
				if (newState == State.CONFIGURED) {
					listener.configured(evt);
				} else if (newState == State.POPULATING) {
					listener.populating(evt);
				} else if (newState == State.POPULATED) {
					listener.populated(evt);
				} else if (newState == State.INVALIDATED) {
					listener.invalidated(evt);
				}
			}
		}
	}

	public final void addListener(CacheListener l) {
		listeners.add(l);
	}
	public final void removeListener(CacheListener l) {
		listeners.remove(l);
	}

	public static class CleanListener extends AbstractCacheListener {
		private boolean shouldBeClean;

		public void configured(CacheEvent e) {
			// no need to clean when the cache is (re)configured.
			shouldBeClean = false;
		}

		public void populated(CacheEvent e) {
			shouldBeClean = true;
		}

		public void invalidated(CacheEvent e) {
			// no need to clean when the cache is invalidated.
			shouldBeClean = false;
		}
	}

	private static enum State {
		INITIALIZED,
		CONFIGURED,
		POPULATING,
		POPULATED,
		INVALIDATED
	}
}
