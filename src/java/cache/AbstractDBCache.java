package cache;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jdbc.JdbcUtils;
import jdbc.PreparedStatementSetter;
import jdbc.RowCallbackHandler;

public abstract class AbstractDBCache<K, V, P> extends AbstractCache<K, V, P> {
	private static final Logger LOG = LoggerFactory.getLogger(AbstractDBCache.class);
	protected abstract String getQuery();
	protected abstract void setParameters(PreparedStatement stmt, P p) throws SQLException;
	protected abstract void cache(ResultSet rs) throws SQLException;

	protected AbstractDBCache() {
		addListener(TimerListener.getInstance());
	}

	@Override
	protected Map<K, V> createMap() {
		return new HashMap<K, V>();
	}

	@Override
	protected final void populate() {
		LOG.info("Populating cache '{}' with criteria {}", getName(), criteriaAsString());
		clear();
		populate(getQuery(), new DefaultRowCallbackHandler());
	}

	protected final void populate(String query, RowCallbackHandler rch) {
		JdbcUtils.query(getConnection(), query, new DefaultPreparedStatementSetter(), rch);
	}
	
	protected final Connection getConnection() {
		return null; // FIXME
	}

	class DefaultPreparedStatementSetter implements PreparedStatementSetter {
		public void setValues(PreparedStatement ps) throws SQLException {
			setParameters(ps, getCriteria());
		}
	}

	class DefaultRowCallbackHandler implements RowCallbackHandler {
		public void processRow(ResultSet rs) throws SQLException {
			cache(rs);
		}
	}
}
