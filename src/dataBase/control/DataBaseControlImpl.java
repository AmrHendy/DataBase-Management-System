package dataBase.control;

import java.io.File;
import java.util.ArrayList;

public class DataBaseControlImpl implements DataBaseControl {
	private String currentDataBaseName;
	private ArrayList<String> dataBases;
	private DataBase currentDataBase;
	private String path, type;

	public DataBaseControlImpl(String path, String type) {
		currentDataBaseName = null;
		this.path = path;
		currentDataBase = null;
		dataBases = new ArrayList<String>();
		this.type = type;
		load();
	}

	@Override
	public void createDataBase(String dataBaseName) {
		try {
			if (dataBaseName.length() != 0) {
				File x = new File(path);
				File newDataBase = new File(x.getAbsolutePath(), dataBaseName);
				ArrayList<String> myDataBases = (ArrayList<String>) toLow(dataBases).clone();
				if (myDataBases.contains(dataBaseName.toLowerCase()) || newDataBase.exists()
						|| dataBaseName.equalsIgnoreCase("database")) {
					currentDataBase = null;
					currentDataBaseName = null;
					throw new RuntimeException("Can't Create This Data Base as This Name is already exisitng");
				} else {
					newDataBase.mkdirs();
					currentDataBaseName = dataBaseName;
					currentDataBase = new DataBase(dataBaseName, this.path, this.type);
					currentDataBase.load();
					this.load();
				}
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in Creating this Data Base");
		}
	}

	@Override
	public void createTable(String tableName, ArrayList<String> columnNames, ArrayList<String> types) {
		changeDataBase(currentDataBaseName);
		currentDataBase.createTable(tableName, columnNames, types);
	}

	@Override
	public void insertIntoTable(ArrayList<String> columns, ArrayList<String> values, String tableName) {
		changeDataBase(currentDataBaseName);
		currentDataBase.insertIntoTable(columns, values, tableName);
	}

	@Override
	public void deleteFromTable(String[] conditions, String tableName) {
		changeDataBase(currentDataBaseName);
		currentDataBase.deleteFromTable(conditions, tableName);
	}

	@Override
	public void selectFromTable(ArrayList<String> column, String[] conditions, String tableName, String coulmnOrder,
			String order, boolean distinct) {
		changeDataBase(currentDataBaseName);
		currentDataBase.selectFromTable(column, conditions, tableName, coulmnOrder, order, distinct);
	}

	@Override
	public void updateTable(ArrayList<String> columns, ArrayList<String> value, String[] conditions, String tableName) {
		changeDataBase(currentDataBaseName);
		currentDataBase.updateTable(columns, value, conditions, tableName);
	}

	@Override
	public void dropDataBase(String dataBaseName) {
		try {
			changeDataBase(dataBaseName);
			File x = new File(path);
			ArrayList<String> myDataBases = (ArrayList<String>) toLow(dataBases).clone();
			dataBaseName = this.dataBases.get(myDataBases.indexOf(dataBaseName.toLowerCase()));
			File dataBasesFolder = new File(x.getAbsoluteFile(), dataBaseName);
			if (myDataBases.contains(dataBaseName.toLowerCase()) && dataBasesFolder.exists()) {
				String[] databaseFiles = dataBasesFolder.list();
				for (String s : databaseFiles) {
					File file = new File(dataBasesFolder, s);
					file.delete();
				}
				dataBasesFolder.delete();
				currentDataBase = null;
				this.load();
			} else {
				throw new RuntimeException("This Data Base is not exisitng to be dropped");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in using this Data Base");
		}
	}

	@Override
	public void dropTable(String tableName) {
		changeDataBase(currentDataBaseName);
		currentDataBase.dropTable(tableName);
	}

	@Override
	public void changeDataBase(String newDataBaseName) {
		try {
			this.load();
			if (newDataBaseName != null && newDataBaseName.length() != 0) {
				File x = new File(path);
				ArrayList<String> myDataBases = (ArrayList<String>) toLow(dataBases).clone();
				newDataBaseName = this.dataBases.get(myDataBases.indexOf(newDataBaseName.toLowerCase()));
				File dataBaseFolder = new File(x.getAbsoluteFile(), newDataBaseName);
				if (myDataBases.contains(newDataBaseName.toLowerCase()) && dataBaseFolder.exists()) {
					this.currentDataBase = new DataBase(newDataBaseName, this.path, this.type);
					currentDataBase.load();
					currentDataBaseName = newDataBaseName;
				} else {
					currentDataBase = null;
					currentDataBaseName = null;
					throw new RuntimeException("This Data Base is not exisitng to be used");
				}
			} else {
				throw new RuntimeException("This Data Base is not exisitng to be used");
			}
		} catch (Exception e) {
			throw new RuntimeException("Error in using this Data Base");
		}
	}

	@Override
	public DataBase getCurrentDataBase() {
		return this.currentDataBase;
	}

	public void alterTable(String tableName, String alterType, ArrayList<String> columnNames,
			ArrayList<String> columnTypes) {
		changeDataBase(currentDataBaseName);
		currentDataBase.alterTable(tableName, alterType, columnNames, columnTypes);
	}

	private void load() {
		File file = new File(path);
		String[] dataBasesNames = file.list();
		if (dataBasesNames == null) {
			this.dataBases = new ArrayList<String>();
			return;
		}
		ArrayList<String> db = new ArrayList<String>();
		for (int i = 0; i < dataBasesNames.length; i++) {
			db.add(dataBasesNames[i]);
		}
		this.dataBases.clear();
		this.dataBases = (ArrayList<String>) db.clone();
	}

	@Override
	public int getChangedRowNumber() {
		// TODO Auto-generated method stub
		if (this.currentDataBase == null) {
			return 0;
		}
		return this.currentDataBase.getChangedRowNumber();
	}

	@Override
	public Table getCurrentTable() {
		// TODO Auto-generated method stub
		return this.currentDataBase.getCurrentTable();
	}

	@Override
	public Table getSelectedTable() {
		// TODO Auto-generated method stub
		if (currentDataBase == null) {
			return null;
		}
		return this.currentDataBase.getSelectedTable();
	}

	private ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}

}
