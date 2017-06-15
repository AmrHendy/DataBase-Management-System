package dbms.parser;

import dataBase.control.DataBaseControlImpl;;

public interface IStatment {

	public boolean isValid(String query);

	public void excute(String query, DataBaseControlImpl Dpms);

}
