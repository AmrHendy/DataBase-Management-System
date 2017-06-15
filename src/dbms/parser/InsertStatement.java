package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class InsertStatement implements IStatment {

	private String insertPattern;

	public InsertStatement() {
		insertPattern = new String();
		insertPattern = "^(\\s*)((?i)insert)(\\s+)((?i)into)(\\s+)(\\w+)";
		insertPattern += "((\\s*)\\(((\\s*)(\\w+)(\\s*),)*((\\s*)(\\w+)(\\s*)\\)(\\s*)))?";
		insertPattern += "((\\s+)((?i)values))(\\s*)";
		insertPattern += "\\((((\\s*)(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))(\\s*),)*((\\s*)(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))))(\\s*)\\)(\\s*)(\\s*);?(\\s*)$";
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(insertPattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		query = query.replaceAll("\\s*,\\s*", ",");
		String table_name = new String();
		ArrayList<String> coloums = new ArrayList<String>();
		ArrayList<String> value = new ArrayList<String>();
		Pattern pat = Pattern.compile(insertPattern);
		Matcher ma = pat.matcher(query.replaceAll("\\)(?i)values", "\\) values"));
		if (ma.matches()) {
			table_name = ma.group(6);
			if (ma.group(7) == null)
				coloums = new ArrayList<String>();
			else
				coloums = new ArrayList<String>(
						Arrays.asList(ma.group(7).replaceAll("\\s+", "").replaceAll("[()]", "").split(",")));
			String trim = ma.group(22) + ",";
			while (!trim.replaceAll("\\s+", "").replaceAll("[,]", "").equals("")) {
				Pattern patt = Pattern.compile(
						"('([^']*)')|((([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))\\s*,)");
				trim = trim.trim();
				Matcher matc = patt.matcher(trim);
				if (matc.find()) {
					if (trim.charAt(matc.start()) == '\'') {
						value.add(trim.substring(matc.start(), matc.end()));
					} else {
						value.add(trim.replaceAll("\\s+", "").substring(matc.start(), matc.end() - 1));
					}
					trim = trim.substring(matc.end());
				}
			}
			Dpms.insertIntoTable(coloums, value, table_name);
		}
	}
}
