package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.management.RuntimeErrorException;

import dataBase.control.DataBaseControlImpl;

public class DeleteStatment implements IStatment {
	private String deleteFrom;
	private String tableName;
	private String where;
	private String columnName;
	private String operator;
	private String compare;
	private String semiColon;
	private String deletePattern;

	public DeleteStatment() {
		deleteFrom = "^(\\s*)((?i)delete)(\\s+)((?i)from)(\\s+)";
		tableName = "(\\w+)";
		where = "(\\s+((?i)where)\\s+";
		columnName = "((\\w+)(\\s*)";
		operator = "(>|<|=|>=|<=|<>)\\s*";
		compare = "(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))))?";
		semiColon = "(\\s*);?(\\s*)$";
		deletePattern = deleteFrom + tableName + where + columnName + operator + compare + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(deletePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(deletePattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			Dpms.deleteFromTable(new WhereCondition().Where((ma.group(9))), ma.group(6));
		}
	}
}
