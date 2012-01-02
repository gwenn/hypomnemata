package jdbc;

import java.sql.ResultSet;
import java.sql.SQLException;

// Stolen from spring framework
public interface RowCallbackHandler {
	void processRow(ResultSet rs) throws SQLException;
}
