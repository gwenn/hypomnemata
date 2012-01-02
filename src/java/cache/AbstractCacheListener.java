package cache;

public abstract class AbstractCacheListener implements CacheListener {
	public void configured(CacheEvent e) {
	}

	public void populating(CacheEvent e) {
	}

	public void populated(CacheEvent e) {
	}

	public void invalidated(CacheEvent e) {
	}
}
