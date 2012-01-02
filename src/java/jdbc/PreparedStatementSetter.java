package jdbc;

import java.sql.PreparedStatement;
import java.sql.SQLException;

// Stolen from spring framework
public interface PreparedStatementSetter {
	PreparedStatementSetter NO_PARAM_SETTER = new PreparedStatementSetter() {
		public void setValues(PreparedStatement ps) throws SQLException {
		}
	};
	void setValues(PreparedStatement ps) throws SQLException;
}
