package dbms.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import dataBase.control.DataBaseControlImpl;

public class UpdateStatement implements IStatment {

	private String typesValues;
	private String updatepattern;

	public UpdateStatement() {
		typesValues = "(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))";
		updatepattern = "^\\s*((?i)update)\\s+(\\w+)\\s+((?i)set)\\s+" + "((\\s*(\\w+)\\s*=\\s*" + typesValues
				+ "\\s*,)*" + "(\\s*(\\w+)\\s*=\\s*" + typesValues + "))"
				+ "(\\s*((?i)where)\\s+((\\w+)(\\s*)(>|<|=|>=|<=|<>)\\s*" + typesValues + "))?\\s*;?\\s*$";
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(updatepattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(updatepattern);
		Matcher ma = pat.matcher(query);
		if (ma.find()) {

			String tableName = ma.group(2);
			ArrayList<ArrayList<String>> clmAndVlu = colomValuesSpliter(ma.group(4));
			String[] wherecondition = new WhereCondition().Where(ma.group(23));
			ArrayList<String> columns = new ArrayList<>();
			ArrayList<String> value = new ArrayList<>();
			for (ArrayList<String> st : clmAndVlu) {
				columns.add(st.get(0));
				value.add(st.get(1));
			}
			Dpms.updateTable(columns, value, wherecondition, tableName);
		}
	}

	public ArrayList<ArrayList<String>> colomValuesSpliter(String colomStateMent) {
		ArrayList<ArrayList<String>> clmAndVlu = new ArrayList<>();
		ArrayList<String> temp = new ArrayList<>();
		String oneClm = "\\s*(\\w+)\\s*=\\s*(.*)\\s*";
		Pattern pa = Pattern.compile(oneClm);
		ArrayList<String> coloms = new ArrayList<String>(Arrays.asList(colomStateMent.split("(\\s*,\\s*)")));
		for (String ss : coloms) {
			Matcher ma = pa.matcher(ss);
			if (ma.find()) {
				temp = new ArrayList<>();
				temp.add(ma.group(1));
				temp.add(ma.group(2));
				clmAndVlu.add(temp);
			}
		}
		return clmAndVlu;
	}
}
