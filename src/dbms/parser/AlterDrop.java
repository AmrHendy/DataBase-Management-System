package dbms.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class AlterDrop implements IStatment {
	private String alterTable;
	private String tableName;
	private String dropColumn;
	private String columnName;
	private String semiColon;
	private String alterdropPattern;

	public AlterDrop() {
		alterTable = "^(\\s*)((?i)alter)(\\s+)((?i)table)(\\s+)";
		tableName = "(\\w+)";
		dropColumn = "(\\s+)((?i)drop)(\\s+)((?i)column)(\\s+)";
		columnName = "(\\w+)";
		semiColon = "(\\s*);?(\\s*)$";
		alterdropPattern = alterTable + tableName + dropColumn + columnName + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(alterdropPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(alterdropPattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			ArrayList<String> columnNames = new ArrayList<String>();
			columnNames.add(ma.group(12));
			Dpms.alterTable(ma.group(6), "drop", columnNames, null);
		}
	}
}