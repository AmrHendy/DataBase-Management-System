package dataBase.control;

import java.io.File;
import java.util.ArrayList;

import javax.management.RuntimeErrorException;

import files.control.*;
import model.Printer;
import model.PrinterIF;

public class DataBase implements IDataBase {

	private String dataBaseName;
	private ArrayList<String> tables;
	private Table curTable;
	private IWriter saveObj;
	private IReader loadObj;
	private String path;
	private String type;

	public void setTables(ArrayList<String> tables) {
		this.tables = tables;
	}

	public DataBase(String dataBaseName, String path, String type) {
		this.type = "." + type;
		this.path = path;
		this.dataBaseName = dataBaseName;
		tables = new ArrayList<String>();
		WriterFactory fw = new WriterFactory();
		saveObj = fw.getWriter(type);
		ReaderFactory fr = new ReaderFactory();
		loadObj = fr.getReader(type);
		load();
	}

	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types) {
		try {
			File file = makeFile(dataBaseName, tableName, this.type);
			ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
			String str = tableName.toLowerCase();
			checkExistingTable(str, myTables);
			checkRepettedColumnNames(columnNames);
			curTable = new Table(this, tableName, columnNames, types, this.path);
			PrinterIF printerObj = Printer.getInstance();
			printerObj.printTable(columnNames, curTable.getWantedData(), tableName);
			tables.add(tableName);

		} catch (Exception e) {
			throw new RuntimeException("Error in create Table");
		}
	}

	public void dropTable(String tableName) {
		try {
			ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
			String str = tableName.toLowerCase();
			if (!myTables.contains(tableName.toLowerCase())) {
				throw new RuntimeException();
			}
			String validTableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
			File data = makeFile(dataBaseName, validTableName, this.type);
			data.delete();
			File schema = makeFile(dataBaseName, validTableName, ".dtd");
			schema.delete();
			tables.remove(validTableName);
			this.load();
		} catch (Exception e) {
			throw new RuntimeException("This Table is not exisitng to be dropped");
		}
	}

	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		Table table = makeTable(tableName);
		ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
		String str = tableName.toLowerCase();
		tableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
		table.insertIntoTable(columns, values, tableName);
	}

	public void deleteFromTable(String[] conditions, String tableName) {
		Table table = makeTable(tableName);
		ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
		String str = tableName.toLowerCase();
		tableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
		table.deleteFromTable(conditions, tableName);
	}

	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName, String coulmnOrder,
			String order, boolean distinct) {
		Table table = makeTable(tableName);
		ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
		String str = tableName.toLowerCase();
		tableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
		table.selectFromTable(column, conditions, tableName, coulmnOrder, order, distinct);
	}

	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {
		Table table = makeTable(tableName);
		ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
		String str = tableName.toLowerCase();
		tableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
		table.updateTable(columns, value, conditions, tableName);
	}

	public void alterTable(String tableName, String alterType, ArrayList<String> columnNames,
			ArrayList<String> columnTypes) {
		Table table = makeTable(tableName);
		ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
		String str = tableName.toLowerCase();
		tableName = this.tables.get(myTables.indexOf(tableName.toLowerCase()));
		table.alterTable(tableName, alterType, columnNames, columnTypes);
	}

	public void load() {
		File x = new File(this.path);
		File dataBasesFolder = new File(x.getAbsoluteFile(), this.dataBaseName);
		if (dataBasesFolder.exists()) {
			this.tables.clear();
			String[] databaseFiles = dataBasesFolder.list();
			for (int i = 0; i < databaseFiles.length; i++) {
				databaseFiles[i] = databaseFiles[i].substring(0, databaseFiles[i].indexOf('.'));
				if (!this.tables.contains(databaseFiles[i])) {
					this.tables.add(databaseFiles[i]);
				}
			}
		} else {
			throw new RuntimeException("This Data Base is not exisitng to be used");
		}
	}

	public Table getCurrentTable() {
		return curTable;
	}

	private File makeFile(String dataBaseName, String tableName, String extension) {
		if (dataBaseName.equals("") || tableName.equals("")) {
			throw new RuntimeException("Invalid Names for Data Base , Table");
		}
		File file = new File(this.path);
		String path = file.getAbsolutePath() + File.separator + dataBaseName + File.separator + tableName + extension;
		File filee = new File(path);
		return filee;
	}

	private Table makeTable(String tableName) {
		if (curTable != null && curTable.getTableName().equals(tableName)) {
			return curTable;
		} else {
			try {
				ArrayList<String> myTables = (ArrayList<String>) toLow(this.tables).clone();
				String str = tableName.toLowerCase();
				if (myTables.contains(str)) {
					tableName = this.tables.get(myTables.indexOf(str));
					File filee = makeFile(this.dataBaseName, tableName, this.type);
					ArrayList<ArrayList<String>> data = loadObj.load(filee);
					Table table;
					if (data == null || data.size() == 0) {
						table = new Table(this, tableName, loadObj.getCoulmnNames(), loadObj.getCoulmnTypes(),
								this.path);
					} else {
						table = new Table(this, tableName, loadObj.getCoulmnNames(), loadObj.getCoulmnTypes(), data,
								this.path);
					}
					curTable = table;
					return table;
				} else {
					throw new RuntimeException("not valid table Name");
				}
			} catch (Exception e) {
				throw new RuntimeException("Error in using the table");
			}
		}
	}

	private ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}

	private void checkRepettedColumnNames(ArrayList<String> columnNames) {
		ArrayList<String> existingColumnNames = new ArrayList<String>();
		ArrayList<String> myColumnNames = (ArrayList<String>) toLow(columnNames).clone();
		for (int i = 0; i < myColumnNames.size(); i++) {
			if (existingColumnNames.contains(myColumnNames.get(i))) {
				throw new RuntimeException("Repetted Columns");
			} else {
				existingColumnNames.add(myColumnNames.get(i));
			}
		}
	}

	private void checkExistingTable(String str, ArrayList<String> myTable) {
		myTable.add("table_name5");
		for (int i = 0; i < myTable.size(); i++) {
			if (myTable.get(i).equalsIgnoreCase(str) || str.equalsIgnoreCase("table")) {
				throw new RuntimeException();
			}
		}
	}

	@Override
	public int getChangedRowNumber() {
		if (curTable == null)
			return 0;
		return curTable.getChangedRowNumber();
	}

	public IWriter getWriter() {
		return saveObj;
	}

	public IReader getReader() {
		return loadObj;
	}

	public String getDataBaseName() {
		return this.dataBaseName;
	}

	@Override
	public Table getSelectedTable() {
		if (curTable == null)
			return null;
		return this.curTable.getSelectedTable();
	}

	@Override
	public String getType() {
		return this.type;
	}
}
