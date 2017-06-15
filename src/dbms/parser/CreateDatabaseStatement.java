package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class CreateDatabaseStatement implements IStatment {
	private String createDataBase;
	private String databaseName;
	private String semiColon;
	private String creatPattern;

	public CreateDatabaseStatement() {
		createDataBase = "^(\\s*)((?i)create)(\\s+)((?i)database)(\\s+)";
		databaseName = "(\\w+)";
		semiColon = "(\\s*);?(\\s*)$";
		creatPattern = createDataBase + databaseName + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		return ma.matches();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			Dpms.createDataBase(ma.group(6));
		}
	}
}
