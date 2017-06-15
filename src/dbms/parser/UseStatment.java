package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import dataBase.control.DataBaseControlImpl;

public class UseStatment implements IStatment {

	private String usePattern;

	public UseStatment() {
		usePattern = "^\\s*((?i)use)\\s+(\\w+)\\s*;?\\s*$";
	}

	@Override
	public boolean isValid(String query) {
		Pattern pat = Pattern.compile(usePattern);
		Matcher ma = pat.matcher(query);
		return ma.find();
	}

	@Override
	public void excute(String query, DataBaseControlImpl Dpms) {
		Pattern pat = Pattern.compile(usePattern);
		Matcher ma = pat.matcher(query);
		if (ma.matches()) {
			Dpms.changeDataBase(ma.group(2));
		}
	}
}
