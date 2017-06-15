package dbms.parser;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class AlterTableAdd implements IStatment {
	private String alterTable;
	private String tableName;
	private String add;
	private String columnName;
	private String semiColon;
	private String type;
	private String alteraddPattern;

	public AlterTableAdd() {
		alterTable = "^(\\s*)((?i)alter)(\\s+)((?i)table)(\\s+)";
		tableName = "(\\w+)";
		add = "(\\s+)((?i)add)(\\s+)";
		columnName = "(\\w+)(\\s+)";
		semiColon = "(\\s*);?(\\s*)$";
		type = "((?i)varchar|(?i)int|(?i)float|(?i)date)";
		alteraddPattern = alterTable + tableName + add + columnName + type + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(alteraddPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(alteraddPattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			ArrayList<String> columnNames = new ArrayList<String>();
			ArrayList<String> columnTypes = new ArrayList<String>();
			columnNames.add(ma.group(10));
			columnTypes.add(ma.group(12));
			Dpms.alterTable(ma.group(6), "add", columnNames, columnTypes);
		}
	}
}
