package cache;

import java.util.EventListener;

public interface CacheListener extends EventListener {
	void configured(CacheEvent e);
	void populating(CacheEvent e);
	void populated(CacheEvent e);
	void invalidated(CacheEvent e);
}
