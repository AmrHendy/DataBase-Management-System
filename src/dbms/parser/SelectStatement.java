package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dataBase.control.DataBaseControlImpl;

public class SelectStatement implements IStatment {
	private String typesValues;
	private String selectPattern;

	public SelectStatement() {
		typesValues = "(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))";
		selectPattern = "^\\s*((?i)select\\s+)((?i)distinct\\s+)?"
				+ "((\\*\\s*)|((\\s*(\\w+)\\s*,)*(\\s*(\\w+)\\s+)))\\s*((?i)from\\s+)(\\w+)"
				+ "(\\s+((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*" + typesValues + "))?"
				+ "(\\s+(?i)order\\s+(?i)by\\s+(\\w+)(\\s+((?i)asc|(?i)desc))?)?\\s*;?\\s*$";
	}

	@Override
	public boolean isValid(String query) {
		query = query.replaceFirst("^\\s*((?i)select\\s*\\*)", "select * ");
		Pattern pat = Pattern.compile(selectPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		query = query.replaceFirst("^\\s*((?i)select\\s*\\*)", "select * ");
		ArrayList<String> colomsName = new ArrayList<>();
		boolean distinct = false;
		String tableName, order = "asc";
		Pattern pat = Pattern.compile(selectPattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {
			if (ma.group(2) != null)
				distinct = true;

			colomsName = new ArrayList<String>(Arrays.asList(ma.group(3).replaceAll("\\s+", "").split(",")));
			tableName = ma.group(11);
			String[] wherecondition = new WhereCondition().Where(ma.group(14));
			if (colomsName.get(0).equals("*"))
				colomsName = new ArrayList<>();
			if (ma.group(24) != null) {
				if (ma.group(26) != null) {
					order = ma.group(23).trim();
				}
				Dpms.selectFromTable(colomsName, wherecondition, tableName, ma.group(22), order, distinct);
			} else {
				Dpms.selectFromTable(colomsName, wherecondition, tableName, null, null, distinct);
			}
		}
	}
}
