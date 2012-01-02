package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

// Stolen from spring framework
public interface ResultSetExtractor<T> {
	ResultSetExtractor<Integer> ONE_INT_EXTRACTOR = new ResultSetExtractor<Integer>() {
		public Integer extractData(ResultSet rs) throws SQLException {
			if (rs.next()) {
				final int value = rs.getInt(1);
				if (rs.next()) {
					throw new SQLException("Only one row expected.");
				}
				return value;
			} else {
				throw new SQLException("At least one row expected.");
			}
		}
	};
	T extractData(ResultSet rs) throws SQLException;
}
