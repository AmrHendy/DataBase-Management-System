package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class CreateTableStatement implements IStatment {
	private String createTable;
	private String tableName;
	private String columNameAndType;
	private String semiColon;
	private String creatPattern;

	public CreateTableStatement() {
		createTable = "^(\\s*)((?i)create)(\\s+)((?i)table)(\\s+)";
		tableName = "(\\w+)(\\s*)";
		columNameAndType = "\\((((\\s*)(\\w+)(\\s+)((?i)varchar|(?i)int|(?i)date|(?i)float)(\\s*),)"
				+ "*((\\s*)(\\w+)(\\s+)((?i)varchar|(?i)int|(?i)date|(?i)float)(\\s*))\\)(\\s*))";
		semiColon = "(\\s*);?(\\s*)$";
		creatPattern = createTable + tableName + columNameAndType + semiColon;
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		return ma.matches();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		String table_name = new String();
		ArrayList<String> coloums = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		Pattern pat = Pattern.compile(creatPattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			table_name = ma.group(6);
			ArrayList<String> NameAndType = new ArrayList<String>(
					Arrays.asList(ma.group(8).replaceAll("[()]", "").split(",")));
			int size = NameAndType.size();
			for (int i = 0; i < size; i++) {
				ArrayList<String> temp = new ArrayList<String>(Arrays.asList(NameAndType.get(i).split("\\s+")));
				if (temp.get(0).equals(""))
					temp.remove(0);
				coloums.add(temp.get(0));
				value.add(temp.get(1));
			}
			Dpms.createTable(table_name, coloums, value);
		}
	}
}
