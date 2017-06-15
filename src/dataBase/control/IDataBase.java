package dataBase.control;

import java.util.ArrayList;

import files.control.IReader;
import files.control.IWriter;

public interface IDataBase {
	/**
	 * create table
	 * 
	 * @param tableName
	 * @param columnNames
	 * @param types
	 */
	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types);

	/**
	 * insert into a specific row some data.
	 * 
	 * @param columns
	 * @param values
	 * @param tableName
	 */
	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName);

	/**
	 * delete data form table
	 * 
	 * @param conditions
	 *            if condition is empty then delete all.
	 * @param tableName
	 */
	public void deleteFromTable(String[] conditions, String tableName);

	/**
	 * select data form table
	 * 
	 * @param column
	 *            columns names if (empty or *) then all
	 * @param conditions
	 *            2D array of size 3 (i.e column name,operation,value)
	 *            operations mean > < =
	 * @param tableName
	 */

	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName, String coulmnOrder,
			String order, boolean distinct);

	/**
	 * update a specific row in table
	 * 
	 * @param columns
	 * @param conditions
	 * @param tableName
	 */
	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName);

	/**
	 * delete table.
	 * 
	 * @param tableName
	 */
	public void dropTable(String tableName);

	public void load();

	public Table getCurrentTable();

	/**
	 * alter table (add , remove) columns.
	 * 
	 * @param alterTable
	 *            (add , remove).
	 * @param tableName.
	 * @param columns
	 *            to added or removed.
	 * @param columnNames.
	 * @param columnTypes.
	 */
	public void alterTable(String tableName, String alterType, ArrayList<String> columnNames,
			ArrayList<String> columnTypes);

	/**
	 * get the number of changed rows.
	 * 
	 * @return - number of changed row.
	 */
	public int getChangedRowNumber();

	/**
	 * get a writer object to save in files.
	 * 
	 * @return an object of IWriter.
	 */
	public IWriter getWriter();

	/**
	 * get a reader object to load from files.
	 * 
	 * @return an object of IReader.
	 */
	public IReader getReader();

	public String getDataBaseName();

	public Table getSelectedTable();

	public String getType();

}
