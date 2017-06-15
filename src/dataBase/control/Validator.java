package dataBase.control;

import java.io.File;
import java.util.ArrayList;

public class Validator implements IValidator {

	private String currentDataBase, currentTableName;
	private ArrayList<String> coulmnNames, coulmnTypes;
	private String path;

	public Validator(String dataBaseName, String currentTableName, ArrayList<String> coulmnNames,
			ArrayList<String> coulmnTypes, String path) {
		this.path = path;
		this.currentDataBase = dataBaseName;
		this.currentTableName = currentTableName;
		this.coulmnNames = coulmnNames;
		this.coulmnTypes = coulmnTypes;
	}

	public boolean validateCoulmnNames(ArrayList<String> coulmnNames) {
		ArrayList<String> coulmnNames1 = toLow((ArrayList<String>) coulmnNames.clone());
		ArrayList<String> mycoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		for (int i = 0; i < coulmnNames1.size(); i++) {
			if (!mycoulmnNames.contains(coulmnNames1.get(i))) {
				return false;
			}
		}
		return true;
	}

	public boolean validateDataTypes(ArrayList<String> coulmnNames, ArrayList<String> coulmnValues) {
		DataTypesValidator valid = new DataTypesValidator(this.coulmnNames, this.coulmnTypes);
		return valid.isValidDataType(coulmnNames, coulmnValues);

	}

	public boolean validTableName(String tableName) {
		tableName = tableName.toLowerCase();
		File file = new File(this.path);
		File folderOfDataBase = new File(file.getAbsolutePath(), currentDataBase);
		folderOfDataBase.mkdir();
		String[] tables = folderOfDataBase.list();
		for (int i = 0; i < tables.length; i++) {
			tables[i] = tables[i].toLowerCase();
			if (tables[i].indexOf(tableName) == 0) {
				return true;
			}
		}
		return false;
	}

	public ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}

	public void validCondition(String[] conditions) {
		ArrayList<String> str1 = new ArrayList<String>();
		str1.add(conditions[0]);
		ArrayList<String> str2 = new ArrayList<String>();
		str2.add(conditions[2]);
		str1 = toLow(str1);
		if (!validateCoulmnNames(str1) || !validateDataTypes(str1, str2)) {
			throw new RuntimeException("Invalid Coulmn Names , Data Types");
		}
	}

	public boolean checkRepetedCoulmns(ArrayList<String> coulmnNames) {
		for (int i = 0; i < coulmnNames.size(); i++) {
			if (this.coulmnNames.contains(coulmnNames.get(i))) {
				return false;
			}
		}
		return true;
	}

	public void setCoulmnNames(ArrayList<String> coulmnNames) {
		this.coulmnNames = coulmnNames;
	}

	public void setCoulmnTypes(ArrayList<String> coulmnTypes) {
		this.coulmnTypes = coulmnTypes;
	}
}
