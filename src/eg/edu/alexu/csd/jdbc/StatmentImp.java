package eg.edu.alexu.csd.jdbc;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLWarning;
import java.sql.Statement;
import java.util.ArrayList;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.omg.CosNaming.NamingContextPackage.CannotProceedHelper;

import dataBase.control.Table;
import dbms.parser.Parser;
import dbms.parser.SelectStatement;

@SuppressWarnings("all")
public class StatmentImp implements Statement {

	private Parser parser;
	private final Logger logger = LogManager.getLogger();
	private ArrayList<String> batchsToExcute;
	private Connection currentConnection;
	private boolean closeState = false;
	private ResultsetImp result;
	private ArrayList<ResultsetImp> allResults;
	private int updatecounter;
	private boolean isResult;
	private boolean bool;

	public StatmentImp(ConnectionImp c, String databaseUrl) {
		isResult = false;
		this.currentConnection = c;
		this.parser = c.parser;
		result = null;
		allResults = new ArrayList<ResultsetImp>();
		bool = false;
	}

	@Override
	public void addBatch(String sql) throws SQLException {
		try {
			if (currentConnection.isClosed() || isClosed()) {
				close();
				logger.error("fail to add batch");
				throw new SQLException();
			}
			if (this.batchsToExcute == null)
				this.batchsToExcute = new ArrayList<>();
			if (sql != null)
				this.batchsToExcute.add(sql);
		} catch (Exception e) {
			logger.error("fail to add batch");
			throw new SQLException();
		}
	}

	@Override
	public void clearBatch() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			logger.error("fail to clear batch");
			throw new SQLException();
		}
		if (!(this.batchsToExcute == null || this.batchsToExcute.size() == 0))
			this.batchsToExcute.clear();
	}

	@Override
	public void close() throws SQLException {
		try {
			this.closeState = true;
			if (!(this.batchsToExcute == null || this.batchsToExcute.size() == 0))
				this.batchsToExcute.clear();

			for (int i = 0; i < allResults.size(); i++) {
				if (allResults.get(i) == null || allResults.get(i).isClosed())
					continue;
				allResults.get(i).close();
			}
			this.result = null;
		} catch (Exception e) {
			logger.error("fail close statment.");
			throw new SQLException("Error in Exceute");
		}
	}

	@Override
	public boolean execute(String sql) throws SQLException {
		if (currentConnection.isClosed() || this.isClosed()) {
			this.close();
			logger.error("fail to excute.");
			throw new SQLException();
		}
		try {
			parser.InsertQuery(sql);
			bool = true;
			Table t = parser.getDatabaseManger().getSelectedTable();
			result = new ResultsetImp(parser.getDatabaseManger().getSelectedTable(), this);
			allResults.add(result);
			updatecounter = parser.getDatabaseManger().getChangedRowNumber();
			if (result.isReal()) {
				isResult = true;
			} else {
				isResult = false;
			}
			logger.info(sql + " executed successfully.");
			return isResult;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SQLException("Error in Execute \n");
		}
	}

	@Override
	public int[] executeBatch() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail to excute.");
			throw new SQLException();
		}

		try {
			int[] updateCounterArray = new int[batchsToExcute.size()];
			for (int i = 0; i < batchsToExcute.size(); i++) {
				try {
					execute(batchsToExcute.get(i));
					updateCounterArray[i] = (isResult) ? SUCCESS_NO_INFO : updatecounter;
				} catch (Exception e) {
					updateCounterArray[i] = EXECUTE_FAILED;
					throw new SQLException();
				}
			}
			return updateCounterArray;
		} catch (Exception e) {
			logger.error("fail to excute.");
			throw new SQLException();
		}
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail to excute.");
			throw new SQLException();
		}
		SelectStatement se = new SelectStatement();
		try {
			if (se.isValid(sql)) {
				execute(sql);
				return result;
			}
			throw new SQLException();
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new SQLException();
		}
	}

	@Override
	public int executeUpdate(String sql) throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail to excute.");
			throw new SQLException();
		}
		if (execute(sql)) {
			throw new SQLException();
		}

		return updatecounter;
	}

	@Override
	public Connection getConnection() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail to excute.");
			throw new SQLException();
		}
		return currentConnection;
	}

	@Override
	public ResultSet getResultSet() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail to excute.");
			throw new SQLException();
		}
		if (bool) {
			bool = false;
			logger.info("getting result data.");
			return (result.isReal() ? result : null);
		} else {
			return null;
		}
	}

	@Override
	public int getUpdateCount() throws SQLException {
		if (currentConnection.isClosed() || isClosed()) {
			close();
			logger.error("fail update count.");
			throw new SQLException();
		}
		int temp_count = updatecounter;
		updatecounter = -1;
		return isResult ? -1 : temp_count;
	}

	@Override
	public boolean isClosed() throws SQLException {
		return closeState;
	}

	@Override
	public int getQueryTimeout() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setQueryTimeout(int seconds) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isWrapperFor(Class<?> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T unwrap(Class<T> iface) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void cancel() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void clearWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void closeOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean execute(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int autoGeneratedKeys) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, int[] columnIndexes) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int executeUpdate(String sql, String[] columnNames) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchDirection() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getFetchSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResultSet getGeneratedKeys() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxFieldSize() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getMaxRows() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean getMoreResults(int current) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetConcurrency() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetHoldability() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public int getResultSetType() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public SQLWarning getWarnings() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isCloseOnCompletion() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isPoolable() throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setCursorName(String name) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEscapeProcessing(boolean enable) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchDirection(int direction) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setFetchSize(int rows) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxFieldSize(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMaxRows(int max) throws SQLException {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setPoolable(boolean poolable) throws SQLException {
		throw new UnsupportedOperationException();
	}

}
