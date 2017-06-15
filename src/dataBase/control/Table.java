package dataBase.control;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import files.control.*;
import model.Printer;
import model.PrinterIF;

@SuppressWarnings("all")
public class Table implements ITable {

	private String tableName;
	private ArrayList<String> coulmnNames;
	private ArrayList<String> coulmnTypes;
	private Validator validator;
	private String dataBaseName;
	private ArrayList<ArrayList<String>> currentTableData, wantedData;
	private File file;
	private PrinterIF printerObj;
	private int changedRows;
	private String dataBasePath;
	private IWriter saveObj;
	private IReader loadObj;
	private Table selectedTable;
	private DataBase dataBase;

	public Table(DataBase dataBaseName, String tableName, ArrayList<String> columnNames, ArrayList<String> types,
			String db) {
		this.dataBase = dataBaseName;
		this.dataBasePath = db;
		this.tableName = tableName;
		changedRows = 0;
		this.coulmnNames = columnNames;
		this.coulmnTypes = types;
		this.dataBaseName = dataBaseName.getDataBaseName();
		this.currentTableData = new ArrayList<ArrayList<String>>();
		this.wantedData = new ArrayList<ArrayList<String>>();
		this.validator = new Validator(dataBaseName.getDataBaseName(), this.tableName,
				(ArrayList<String>) columnNames.clone(), (ArrayList<String>) types.clone(), db);
		saveObj = dataBaseName.getWriter();
		loadObj = dataBaseName.getReader();
		printerObj = Printer.getInstance();
		saveObj.save(makeFile(this.dataBaseName, tableName, this.dataBase.getType()), currentTableData, columnNames,
				types, tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
	}

	public Table(DataBase dataBaseName, String tableName, ArrayList<String> columnNames, ArrayList<String> types,
			ArrayList<ArrayList<String>> data, String db) {
		this.dataBase = dataBaseName;
		this.dataBasePath = db;
		changedRows = 0;
		this.tableName = tableName;
		this.coulmnNames = columnNames;
		this.coulmnTypes = types;
		this.dataBaseName = dataBaseName.getDataBaseName();
		this.currentTableData = data;
		this.wantedData = new ArrayList<ArrayList<String>>();
		this.validator = new Validator(dataBaseName.getDataBaseName(), this.tableName,
				(ArrayList<String>) columnNames.clone(), (ArrayList<String>) types.clone(), db);
		saveObj = dataBaseName.getWriter();
		loadObj = dataBaseName.getReader();
		printerObj = Printer.getInstance();
		setWantedData(this.coulmnNames, this.currentTableData);
	}

	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		if ((columns.size() == 0 && values.size() != coulmnNames.size())
				|| (columns.size() != 0 && columns.size() != values.size())) {
			throw new RuntimeException("Invalid Parameters In The SQL Command");
		}
		if (columns.size() == 0) {
			columns = (ArrayList<String>) this.coulmnNames.clone();
		}
		if (!validator.validateCoulmnNames(columns) || !validator.validateDataTypes(columns, values)
				|| !validator.validTableName(tableName)) {
			throw new RuntimeException("Invalid Parameters In The SQL Command when executing Insert");
		}
		ready(tableName);
		ArrayList<String> row = new ArrayList<String>();
		for (int i = 0; i < coulmnNames.size(); i++) {
			row.add(new String(" "));
		}
		values = removeQuotes(values);
		for (int i = 0; i < coulmnNames.size(); i++) {
			for (int j = 0; j < columns.size(); j++) {
				if (coulmnNames.get(i).equalsIgnoreCase(columns.get(j))) {
					row.set(i, values.get(j));
					break;
				} else {
					row.set(i, new String(" "));
				}
			}
		}
		changedRows = 1;
		currentTableData.add(row);
		saveObj.save(file, currentTableData, coulmnNames, coulmnTypes, this.tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	public void deleteFromTable(String[] conditions, String tableName) {
		ready(tableName);
		if (conditions.length == 3) {
			validator.validCondition(conditions);
		}
		if (!validator.validTableName(tableName)) {
			throw new RuntimeException("Invalid Parameters In The SQL Command when executing Delete");
		}
		ready(tableName);
		ArrayList<Integer> indexes = makeConditions(conditions);
		changedRows = indexes.size();
		for (int i = indexes.size() - 1; i >= 0; i--) {
			currentTableData.remove((int) indexes.get(i));
		}
		saveObj.save(file, currentTableData, coulmnNames, coulmnTypes, tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName, String coulmnOrder,
			String order, boolean distinct) {

		if (conditions.length == 3) {
			validator.validCondition(conditions);
		}
		if (column.size() == 0) {
			column = (ArrayList<String>) this.coulmnNames.clone();
		}
		if (!validator.validateCoulmnNames(column) || !validator.validTableName(tableName)) {
			throw new RuntimeException("Invalid Parameters In The SQL Command when executing Select");
		}
		if (coulmnOrder != null && order != null) {
			ArrayList<String> col = new ArrayList<String>();
			col.add(coulmnOrder);
			if (!validator.validateCoulmnNames(col)) {
				throw new RuntimeException("Invalid Parameters In The SQL Command when executing Select with order");
			}
		}
		ready(tableName);
		ArrayList<Integer> colIndex = getColIndex(column);
		ArrayList<Integer> indexes = makeConditions(conditions);
		if (order != null) {
			ArrayList<String> strr = new ArrayList<String>();
			strr.add(coulmnOrder);
			Integer indexx = getColIndex(strr).get(0);
			indexes = (ArrayList<Integer>) makeOrder(indexes, indexx, order).clone();
		}
		ArrayList<ArrayList<String>> selectedData = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < indexes.size(); i++) {
			ArrayList<String> row = currentTableData.get(indexes.get(i));
			ArrayList<String> rowSelectedData = new ArrayList<String>();
			for (int j = 0; j < colIndex.size(); j++) {
				rowSelectedData.add(row.get(colIndex.get(j)));
			}
			selectedData.add(rowSelectedData);
		}
		if (distinct) {
			selectedData = removeRepetedRows(selectedData);
		}
		column.clear();
		this.changedRows = -1;
		for (int i = 0; i < colIndex.size(); i++)
			column.add(i, this.coulmnNames.get(colIndex.get(i)));
		setWantedData(column, selectedData);
		makeSelectedTable(this.dataBase, this.tableName, column, makeTypes(column), selectedData, this.dataBasePath);
		printerObj.printTable(column, getWantedData(), getTableName());
	}

	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {

		if (conditions.length == 3) {
			validator.validCondition(conditions);
		}

		if ((columns.size() != value.size()) || !validator.validateCoulmnNames(columns)
				|| !validator.validateDataTypes(columns, value) || !validator.validTableName(tableName)) {
			throw new RuntimeException("Invalid Parameters In The SQL Command when executing Update");
		}
		ready(tableName);
		ArrayList<Integer> indexes = makeConditions(conditions);
		changedRows = indexes.size();
		value = removeQuotes(value);
		for (int k = 0; k < indexes.size(); k++) {
			ArrayList<String> row = currentTableData.get(indexes.get(k));
			for (int i = 0; i < coulmnNames.size(); i++) {
				for (int j = 0; j < columns.size(); j++) {
					if (coulmnNames.get(i).equalsIgnoreCase(columns.get(j))) {
						row.set(i, value.get(j));
						break;
					}
				}
			}
			currentTableData.set(indexes.get(k), row);
		}
		saveObj.save(file, currentTableData, coulmnNames, coulmnTypes, tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());

	}

	public void alterTable(String tableName, String alterType, ArrayList<String> columnNames,
			ArrayList<String> columnTypes) {
		ready(tableName);
		if (alterType.equalsIgnoreCase("ADD")) {
			this.addColumns(columnNames, columnTypes);
		} else if (alterType.equalsIgnoreCase("DROP")) {
			this.dropColumns(columnNames);
		}
	}

	private void addColumns(ArrayList<String> coulmnNames, ArrayList<String> coulmnTypes) {
		if (!validator.checkRepetedCoulmns(coulmnNames)) {
			throw new RuntimeException("Invalid Coulmn Names , This Names are already used before !");
		}
		for (int i = 0; i < coulmnNames.size(); i++) {
			this.coulmnNames.add(coulmnNames.get(i));
			this.coulmnTypes.add(coulmnTypes.get(i));
		}
		validator.setCoulmnNames(this.coulmnNames);
		validator.setCoulmnTypes(this.coulmnTypes);

		for (int i = 0; i < this.currentTableData.size(); i++) {
			this.currentTableData.get(i).add(" ");
		}
		saveObj.save(file, this.currentTableData, this.coulmnNames, this.coulmnTypes, this.tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	private void dropColumns(ArrayList<String> coulmnNames) {
		if (!validator.validateCoulmnNames(coulmnNames)) {
			throw new RuntimeException("Invalid Coulmn Names while executing ALter statment !");
		}
		for (int i = 0; i < coulmnNames.size(); i++) {
			int index = this.coulmnNames.indexOf(coulmnNames.get(i));
			this.coulmnTypes.remove(index);
			this.coulmnNames.remove(index);
			for (int j = 0; j < this.currentTableData.size(); j++) {
				this.currentTableData.get(j).remove(index);
			}
		}
		validator.setCoulmnNames(this.coulmnNames);
		validator.setCoulmnTypes(this.coulmnTypes);
		saveObj.save(file, this.currentTableData, this.coulmnNames, this.coulmnTypes, this.tableName);
		setWantedData(this.coulmnNames, this.currentTableData);
		printerObj.printTable(getCoulmnNames(), getWantedData(), getTableName());
	}

	private void ready(String tableName) {
		if (!validator.validTableName(tableName)) {
			throw new RuntimeException("Invalid Table Name");
		}
		changedRows = 0;
		this.selectedTable = null;
		this.tableName = tableName;
		file = makeFile(this.dataBaseName, tableName, this.dataBase.getType());
		currentTableData = loadObj.load(file);
		coulmnNames = loadObj.getCoulmnNames();
		coulmnTypes = loadObj.getCoulmnTypes();
	}

	private ArrayList<ArrayList<String>> removeRepetedRows(ArrayList<ArrayList<String>> selectedData) {
		ArrayList<ArrayList<String>> data = new ArrayList<ArrayList<String>>();
		ArrayList<ArrayList<String>> finalData = new ArrayList<ArrayList<String>>();
		for (int i = 0; i < selectedData.size(); i++) {
			if (!finalData.contains(selectedData.get(i))) {
				data.add(selectedData.get(i));
				finalData.add(selectedData.get(i));
			}
		}
		return (ArrayList<ArrayList<String>>) data.clone();
	}

	private ArrayList<Integer> makeConditions(String[] conditions) {
		ArrayList<Integer> indexes = new ArrayList<Integer>();
		if (conditions.length != 3) {
			for (int i = 0; i < currentTableData.size(); i++) {
				indexes.add(i);
			}
			return indexes;
		}
		ArrayList<String> values = new ArrayList<String>();
		values.add(conditions[2]);
		values = removeQuotes(values);
		conditions[2] = values.get(0);
		String coulmn = conditions[0];
		coulmn = coulmn.toLowerCase();
		ArrayList<String> myCoulmnNames = validator.toLow((ArrayList<String>) this.coulmnNames.clone());
		int indexOfCoulmn = myCoulmnNames.indexOf(coulmn);
		String operator = conditions[1];
		String value = conditions[2];
		for (int i = 0; i < currentTableData.size(); i++) {
			if (isMatch(currentTableData.get(i).get(indexOfCoulmn), operator, value)) {
				indexes.add(i);
			}
		}
		return indexes;
	}

	private boolean isMatch(String data, String operator, String value) {
		try {
			Float data2, value2;
			data2 = Float.parseFloat(data);
			value2 = Float.parseFloat(value);
			if (operator.equals("=")) {
				return data2.equals(value2);
			} else if (operator.equals("<>")) {
				return (!data2.equals(value2));
			} else if (operator.equals(">")) {
				return (data2 > value2);
			} else if (operator.equals("<")) {
				return (data2 < value2);
			} else if (operator.equals(">=")) {
				return (data2 > value2 || data2.equals(value2));
			} else if (operator.equals("<=")) {
				return (data2 < value2 || data2.equals(value2));
			}
			return false;
		} catch (Exception e) {
			String[] strings = new String[2];
			strings[0] = data;
			strings[1] = value;
			Arrays.sort(strings, new StringComparator());
			if (operator.equals("=")) {
				return data.equals(value);
			} else if (operator.equals("<>")) {
				return (!data.equals(value));
			} else if (operator.equals(">")) {
				return (strings[0].equals(value) && !value.equals(data));
			} else if (operator.equals("<")) {
				return (strings[1].equals(value) && !value.equals(data));
			} else if (operator.equals(">=")) {
				return (data.equals(value) || strings[0].equals(value));
			} else if (operator.equals("<=")) {
				return (data.equals(value) || strings[1].equals(value));
			}
			return false;
		}
	}

	private File makeFile(String dataBaseName, String tableName, String extension) {
		if (this.dataBaseName == "" || this.tableName == "") {
			throw new RuntimeException("Invalid Names for Data Base , Table");
		}
		File file = new File(this.dataBasePath);
		String path = file.getAbsolutePath() + File.separator + dataBaseName + File.separator + tableName + extension;
		File filee = new File(path);
		return filee;
	}

	private ArrayList<Integer> getColIndex(ArrayList<String> columns) {
		ArrayList<Integer> colIndex = new ArrayList<Integer>();
		ArrayList<String> temp1 = validator.toLow(this.coulmnNames);
		ArrayList<String> temp2 = validator.toLow(columns);
		for (int i = 0; i < columns.size(); i++) {
			colIndex.add(temp1.indexOf(temp2.get(i)));
		}
		return colIndex;
	}

	private ArrayList<Integer> makeOrder(ArrayList<Integer> rowIndex, int orderCoulmnIndex, String order) {
		ArrayList<Integer> newRowIndex = new ArrayList<Integer>();
		ArrayList<String> oldData = new ArrayList<String>();
		for (int i = 0; i < rowIndex.size(); i++) {
			oldData.add(this.currentTableData.get(i).get(orderCoulmnIndex));
		}
		ArrayList<String> orderedData = new ArrayList<String>();
		orderedData = (ArrayList<String>) oldData.clone();
		if (order.equalsIgnoreCase("ASC")) {
			Collections.sort(orderedData, new StringComparator());
		} else {
			Collections.sort(orderedData, new StringComparatorDesc());
		}
		for (int i = 0; i < orderedData.size(); i++) {
			for (int j = 0; j < oldData.size(); j++) {
				if (oldData.get(j) == null)
					continue;
				if (orderedData.get(i).equals(oldData.get(j))) {
					newRowIndex.add(j);
					oldData.set(j, null);
					break;
				}
			}
		}
		return newRowIndex;
	}

	public String getTableName() {
		return tableName;
	}

	public ArrayList<String> getCoulmnNames() {
		return coulmnNames;
	}

	public void setCoulmnNames(ArrayList<String> coulmnNames) {
		this.coulmnNames = coulmnNames;
	}

	public ArrayList<String> getCoulmnTypes() {
		return coulmnTypes;
	}

	public void setCoulmnTypes(ArrayList<String> coulmnTypes) {
		this.coulmnTypes = coulmnTypes;
	}

	public ArrayList<ArrayList<String>> getWantedData() {
		return wantedData;
	}

	public void setWantedData(ArrayList<String> coulmnNames, ArrayList<ArrayList<String>> tableData) {
		wantedData = new ArrayList<ArrayList<String>>();
		wantedData.add((ArrayList<String>) coulmnNames.clone());
		for (int i = 0; i < tableData.size(); i++) {
			wantedData.add(tableData.get(i));
		}
	}

	@Override
	public int getChangedRowNumber() {
		return changedRows;
	}

	@Override
	public Table getSelectedTable() {
		if (this.selectedTable == null) {
			return null;
		}
		return this.selectedTable;
	}

	private void makeSelectedTable(DataBase dataBaseName, String tableName, ArrayList<String> columnNames,
			ArrayList<String> types, ArrayList<ArrayList<String>> data, String db) {
		this.selectedTable = new Table(dataBaseName, tableName, columnNames, types, data, db);
	}

	private ArrayList<String> makeTypes(ArrayList<String> columns) {
		ArrayList<String> types = new ArrayList<String>();
		for (int i = 0; i < columns.size(); i++) {
			int temp = this.coulmnNames.indexOf(columns.get(i));
			if (temp != -1) {
				types.add(this.coulmnTypes.get(temp));
			}
		}
		return types;
	}

	private ArrayList<String> removeQuotes(ArrayList<String> values) {
		ArrayList<String> modifiedValues = new ArrayList<String>();
		for (int i = 0; i < values.size(); i++) {
			modifiedValues.add(values.get(i).replaceAll("'", ""));
		}
		return (ArrayList<String>) modifiedValues.clone();
	}

}

class StringComparator implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		if (arg0.equals(arg1))
			return 0;
		else if (((String) arg0).length() != ((String) arg1).length()) {
			return ((String) arg0).length() - ((String) arg1).length();
		} else {
			String[] str = new String[2];
			str[0] = (String) arg0;
			str[1] = (String) arg1;
			Arrays.sort(str);
			return (str[1].equals(arg0) ? 1 : -1);
		}
	}
}

class StringComparatorDesc implements Comparator<String> {

	@Override
	public int compare(String arg0, String arg1) {
		if (arg0.equals(arg1))
			return 0;
		else if (((String) arg0).length() != ((String) arg1).length()) {
			return ((String) arg1).length() - ((String) arg0).length();
		} else {
			String[] str = new String[2];
			str[0] = (String) arg0;
			str[1] = (String) arg1;
			Arrays.sort(str);
			return (str[1].equals(arg1) ? 1 : -1);
		}
	}

}