package dbms.parser;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhereCondition {

	public WhereCondition() {
	}

	public String[] Where(String condition) {
		if (condition == null) {
			return new String[0];
		}
		String typesValues = "(('[^']*')|([+-]?\\d+\\.?\\d*)|(\\d{4}\\-(0?[1-9]|1[012])\\-(0?[1-9]|[12][0-9]|3[01])))";

		String WhereCondtionPattern = "(\\w+)\\s*(>|<|=|>=|<=|<>)\\s*" + typesValues;
		Pattern pat = Pattern.compile(WhereCondtionPattern);
		Matcher ma = pat.matcher(condition);
		String[] cndition = new String[3];
		if (ma.find()) {
			cndition[0] = ma.group(1);
			cndition[1] = ma.group(2);
			cndition[2] = ma.group(3);
		}
		return cndition;
	}
}