package jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * See Springframework, package <code>org.springframework.jdbc.support</code>.
 * And Hibernate, packages <code>org.hibernate.dialect</code> and <code>org.hibernate.id</code>.
 */
public class JdbcUtils {
	private static final Logger LOG = LoggerFactory.getLogger(JdbcUtils.class);

	private JdbcUtils() {
	}

	public static java.sql.Date toSqlDate(Date date) {
		if (null == date) {
			return null;
		} else {
			return new java.sql.Date(date.getTime());
		}
	}
	public static java.sql.Timestamp toSqlTimestamp(Date date) {
		if (null == date) {
			return null;
		} else {
			return new Timestamp(date.getTime());
		}
	}

	/**
	 * Close the given JDBC Statement and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 * @param stmt the JDBC Statement to close (may be <code>null</code>)
	 */
	public static void closeStatement(Statement stmt) {
		if (stmt != null) {
			try {
				stmt.close();
			}
			catch (SQLException ex) {
				LOG.trace("Could not close JDBC Statement", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				LOG.trace("Unexpected exception on closing JDBC Statement", ex);
			}
		}
	}

	public static void closeResultSetAndAssociatedStatement(ResultSet rs) {
		Statement statement = null;
		if (rs != null) {
			try {
				statement = rs.getStatement();
			} catch (SQLException e) {
				LOG.trace("Could not get JDBC Statement associated to ResultSet", e);
			}
		}
		closeResultSet(rs);
		closeStatement(statement);
	}

	/**
	 * Close the given JDBC ResultSet and ignore any thrown exception.
	 * This is useful for typical finally blocks in manual JDBC code.
	 *
	 * @param rs the JDBC ResultSet to close (may be <code>null</code>)
	 */
	public static void closeResultSet(ResultSet rs) {
		if (rs != null) {
			try {
				rs.close();
			}
			catch (SQLException ex) {
				LOG.trace("Could not close JDBC ResultSet", ex);
			}
			catch (Throwable ex) {
				// We don't trust the JDBC driver: It might throw RuntimeException or Error.
				LOG.trace("Unexpected exception on closing JDBC ResultSet", ex);
			}
		}
	}

	public static <T> T query(Connection conn, String query, PreparedStatementSetter pss, ResultSetExtractor<T> rch) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(query);
			pss.setValues(ps);
			rs = ps.executeQuery();
			return rch.extractData(rs);
		} catch (SQLException e) {
			final String msg = String.format("Error while executing this query: %s", query);
			LOG.error(msg, e);
			throw new IllegalStateException(msg, e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
	}

	public static void query(Connection conn, String query, PreparedStatementSetter pss, final RowCallbackHandler rch) {
		query(conn, query, pss, new ResultSetExtractor<Void>() {
			public Void extractData(ResultSet rs) throws SQLException {
				while (rs.next()) {
					rch.processRow(rs);
				}
				return null;
			}
		});
	}

	public static int insertAndSelectIdentity(Connection conn, String insert, PreparedStatementSetter pss) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		try {
			ps = conn.prepareStatement(insert + "\nselect @@identity");
			pss.setValues(ps);
			return insertAndSelectIdentity(ps);
		} catch (SQLException e) {
			final String msg = String.format("Error while executing: %s", insert);
			LOG.error(msg, e);
			throw new IllegalStateException(msg, e);
		} finally {
			JdbcUtils.closeResultSet(rs);
			JdbcUtils.closeStatement(ps);
		}
	}
	private static int insertAndSelectIdentity(PreparedStatement insert) throws SQLException {
		if ( !insert.execute() ) {
		  while ( !insert.getMoreResults() && insert.getUpdateCount() != -1 ) {
			// do nothing until we hit the result set containing the generated id
		  }
		}
		ResultSet rs = insert.getResultSet();
		try {
			if (!rs.next()) {
				throw new SQLException("The database returned no natively generated identity value");
			}
			return rs.getInt(1);
		}
		finally {
		  rs.close();
		}
	}

	public static String buildParams(int paramCount) {
		final StringBuilder sql = new StringBuilder();
		for (int i = 0; i < paramCount; i++) {
			if (i > 0) {
				sql.append(", ");
			}
			sql.append('?');
		}
		return sql.toString();
	}
}
