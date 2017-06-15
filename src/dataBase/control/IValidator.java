package dataBase.control;

import java.util.ArrayList;

public interface IValidator {

	public boolean validateCoulmnNames(ArrayList<String> coulmnNames);

	public boolean validateDataTypes(ArrayList<String> coulmnNames, ArrayList<String> coulmnValues);

	public boolean validTableName(String tableName);

	public ArrayList<String> toLow(ArrayList<String> x);

	public void validCondition(String[] conditions);

	public void setCoulmnNames(ArrayList<String> coulmnNames);

	public void setCoulmnTypes(ArrayList<String> coulmnTypes);

	public boolean checkRepetedCoulmns(ArrayList<String> coulmnNames);
}
