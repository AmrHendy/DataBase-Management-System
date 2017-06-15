package dataBase.control;

import java.sql.Date;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

@SuppressWarnings("all")
public class DataTypesValidator {

	private ArrayList<String> coulmnNames;
	private ArrayList<String> coulmnTypes;

	public DataTypesValidator(ArrayList<String> coulmnNames, ArrayList<String> coulmnTypes) {
		this.coulmnNames = coulmnNames;
		this.coulmnTypes = coulmnTypes;
	}

	public int typeAsInSql(Table table, int indexFormZero) throws SQLException {
		Types type = null;
		String columnType = table.getCoulmnTypes().get(indexFormZero);
		if (columnType.equalsIgnoreCase("varchar")) {
			return type.VARCHAR;
		} else if (columnType.equalsIgnoreCase("int")) {
			return type.INTEGER;
		} else if (columnType.equalsIgnoreCase("date")) {
			return type.DATE;
		} else if (columnType.equalsIgnoreCase("float")) {
			return type.FLOAT;
		} else {
			throw new SQLException();
		}
	}

	public boolean isValidDataType(ArrayList<String> coulmnNames, ArrayList<String> coulmnValues) {
		ArrayList<String> coulmnNames1 = toLow((ArrayList<String>) coulmnNames.clone());
		ArrayList<String> mycoulmnNames = toLow((ArrayList<String>) this.coulmnNames.clone());
		if (coulmnNames1.size() == 0) {
			coulmnNames1 = (ArrayList<String>) mycoulmnNames.clone();
		}
		for (int i = 0; i < coulmnValues.size(); i++) {
			if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("varchar")) {
				if (coulmnValues.get(i).charAt(0) == '\''
						&& coulmnValues.get(i).charAt(coulmnValues.get(i).length() - 1) == '\'') {
					continue;
				} else {
					return false;
				}
			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("date")) {
				if (coulmnValues.get(i).charAt(0) == '\''
						&& coulmnValues.get(i).charAt(coulmnValues.get(i).length() - 1) == '\'') {
					coulmnValues.set(i, coulmnValues.get(i).substring(1, coulmnValues.get(i).length() - 1));
				}
				if (!isValidDate(coulmnValues.get(i))) {
					return false;
				}

			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("float")) {
				if (coulmnValues.get(i).charAt(0) == '\''
						&& coulmnValues.get(i).charAt(coulmnValues.get(i).length() - 1) == '\'') {
					coulmnValues.set(i, coulmnValues.get(i).substring(1, coulmnValues.get(i).length() - 1));
				}
				if (!isValidFloat(coulmnValues.get(i))) {
					return false;
				}
			} else if (this.coulmnTypes.get(mycoulmnNames.indexOf(coulmnNames1.get(i))).equalsIgnoreCase("int")) {
				if (coulmnValues.get(i).charAt(0) == '\''
						&& coulmnValues.get(i).charAt(coulmnValues.get(i).length() - 1) == '\'') {
					coulmnValues.set(i, coulmnValues.get(i).substring(1, coulmnValues.get(i).length() - 1));
				}
				if (!isValidInt(coulmnValues.get(i))) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean isValidInt(String inputInt) {
		try {
			if (inputInt.contains(" "))
				return false;
			Integer.parseInt(inputInt);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private boolean isValidDate(String inputDate) {
		Date date;
		try {
			if (inputDate.contains(" "))
				return false;
			DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
			formatter.setLenient(false);
			date = new Date(formatter.parse(inputDate).getTime());
		} catch (ParseException e) {
			return false;
		}
		return true;
	}

	private boolean isValidFloat(String inputFloat) {
		try {
			if (inputFloat.contains(" "))
				return false;
			Float.parseFloat(inputFloat);
		} catch (NumberFormatException e) {
			return false;
		}
		return true;
	}

	private ArrayList<String> toLow(ArrayList<String> x) {
		ArrayList<String> x2 = (ArrayList<String>) x.clone();
		for (int i = 0; i < x.size(); i++) {
			x2.set(i, x2.get(i).toLowerCase());
		}
		return (ArrayList<String>) x2.clone();
	}
}