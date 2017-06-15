package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class DropTableStatement implements IStatment {
	private String droptable;
	private String tableName;
	private String semiColon;
	private String dropTablePattern;

	public DropTableStatement() {
		droptable = "^\\s*((?i)drop)\\s+((?i)table)\\s+";
		tableName = "(\\w+)";
		semiColon = "(\\s*);?(\\s*)$";
		dropTablePattern = droptable + tableName + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(dropTablePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();

	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(dropTablePattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {
			Dpms.dropTable(ma.group(3));
		}
	}
}
