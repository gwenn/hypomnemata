package cache;

import java.util.EventObject;

public class CacheEvent extends EventObject {
	public CacheEvent(Cache source) {
		super(source);
	}

	public Cache getSource() {
		return (Cache)super.getSource();
	}
}
