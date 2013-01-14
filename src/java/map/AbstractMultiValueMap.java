package map;

import java.util.Collection;
import java.util.Map;
import java.util.Set;

public abstract class AbstractMultiValueMap<K,V,M> implements MultiValueMap<K,V,M> {
  protected final Map<K,M> delegate;

  protected AbstractMultiValueMap(Map<K, M> delegate) {
    if (delegate == null) {
      throw new IllegalArgumentException("Null delegate");
    }
    this.delegate = delegate;
  }

  @Override
  public void add(K key, V value) {
    M values = get(key);
    if (values == null) {
      values = create();
      delegate.put(key, values);
    }
    append(values, value);
  }
  protected abstract M create();
  protected abstract void append(M values, V value);

  @Override
  public int size() {
    return delegate.size();
  }
  @Override
  public boolean isEmpty() {
    return delegate.isEmpty();
  }
  @Override
  public boolean containsKey(Object key) {
    return delegate.containsKey(key);
  }
  @Override
  public boolean containsValue(Object value) {
    //if (value instanceof M) {
      return delegate.containsValue(value);
    /*} else {
      for (M m : delegate.values()) {
        if (contains(m, value)) {
          return true;
        }
      }
      return false;
    }*/
  }
  @Override
  public M get(Object key) {
    return delegate.get(key);
  }
  @Override
  public M put(K key, M value) {
    return delegate.put(key, value);
  }
  @Override
  public M remove(Object key) {
    return delegate.remove(key);
  }
  @Override
  public void putAll(Map<? extends K, ? extends M> m) {
    delegate.putAll(m);
  }
  @Override
  public void clear() {
    delegate.clear();
  }
  @Override
  public Set<K> keySet() {
    return delegate.keySet();
  }
  @Override
  public Collection<M> values() {
    return delegate.values();
  }
  @Override
  public Set<Entry<K, M>> entrySet() {
    return delegate.entrySet();
  }

  @Override
  public boolean equals(Object obj) {
    return delegate.equals(obj);
  }
  @Override
  public int hashCode() {
    return delegate.hashCode();
  }
  @Override
  public String toString() {
    return delegate.toString();
  }
}
