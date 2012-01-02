package cache;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public abstract class AbstractNoCriteriaDBCache<K, V> extends AbstractDBCache<K, V, AbstractNoCriteriaDBCache.NoCriteria> {
	protected AbstractNoCriteriaDBCache() {
		setCriteria(NoCriteria.NONE);
	}

	@Override
	protected final void setParameters(PreparedStatement stmt, NoCriteria p) throws SQLException {}

	static enum NoCriteria {
		NONE
	}
}
