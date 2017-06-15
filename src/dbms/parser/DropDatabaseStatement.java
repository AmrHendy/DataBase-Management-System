package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class DropDatabaseStatement implements IStatment {

	private String dropDataBase;
	private String databaseName;
	private String semiColon;
	private String dropDatabasePattern;

	public DropDatabaseStatement() {
		dropDataBase = "^\\s*((?i)drop)\\s+((?i)database)\\s+";
		databaseName = "(\\w+)";
		semiColon = "(\\s*);?(\\s*)$";
		dropDatabasePattern = dropDataBase + databaseName + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(dropDatabasePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(dropDatabasePattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {
			Dpms.dropDataBase(ma.group(3));
		}
	}
}