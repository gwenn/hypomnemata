package cache;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TimerListener extends AbstractCacheListener {
	private static final Logger LOG = LoggerFactory.getLogger(TimerListener.class);
	private final Map<Cache, Long> times = new HashMap<Cache, Long>();

	// Lazy initialization holder class idiom for static fields
	private static class FieldHolder {
		static final TimerListener field = new TimerListener();
	}

	public static TimerListener getInstance() {
		return FieldHolder.field;
	}

	private TimerListener() {
	}

	public void populating(CacheEvent e) {
		if (LOG.isInfoEnabled()) {
			final Cache source = e.getSource();
			times.put(source, System.currentTimeMillis());
			LOG.info("Cache '{}' will be initialized using config : {}", source.getName(), source.criteriaAsString());
		}
	}

	public void populated(CacheEvent e) {
		if (LOG.isInfoEnabled()) {
			final long endTime = System.currentTimeMillis();
			final Cache source = e.getSource();
			final Long startTime = times.remove(source);
			LOG.info("Cache '{}' initialized in {} ms with {} keys",
					new Object[] {source.getName(), (endTime - startTime), source.size()});
		}
	}
}
