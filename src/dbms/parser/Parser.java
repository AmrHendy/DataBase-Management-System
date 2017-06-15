package dbms.parser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.eztech.util.JavaClassFinder;
import dataBase.control.DataBaseControlImpl;

public class Parser implements IParser {

	private DataBaseControlImpl Dpms;
	private List<Class<? extends IStatment>> classes;

	public Parser(String path, String type) {
		this.Dpms = new DataBaseControlImpl(path, type);
		JavaClassFinder classFinder = new JavaClassFinder();
		classes = classFinder.findAllMatchingTypes(IStatment.class);
	}

	@Override
	public void InsertQuery(String query) throws SQLException {
		boolean syntex = true;
		query = query.replaceAll("\"", "'");
		ArrayList<IStatment> statments = new ArrayList<>();
		statments.add(new AlterTableAdd());
		statments.add(new AlterDrop());
		statments.add(new CreateDatabaseStatement());
		statments.add(new CreateTableStatement());
		statments.add(new DeleteStatment());
		statments.add(new DropDatabaseStatement());
		statments.add(new DropTableStatement());
		statments.add(new InsertStatement());
		statments.add(new SelectStatement());
		statments.add(new UpdateStatement());
		statments.add(new UseStatment());

		for (int i = 0; i < statments.size(); i++) {
			try {
				if (statments.get(i).isValid(query)) {
					syntex = false;
					statments.get(i).excute(query, this.Dpms);
					break;
				}
			} catch (Exception e) {
				throw e;
			}
		}
		if (syntex)
			throw new SQLException("syntex error");
	}

	public DataBaseControlImpl getDatabaseManger() {
		return this.Dpms;
	}

	/*
	 * for (Class clazz : classes) {
	 * 
	 * Constructor<?> cons = null; try { if
	 * (!clazz.getName().equals("dbms.parser.IStatment")) cons =
	 * clazz.getConstructor(null); } catch (Exception e1) { // TODO
	 * Auto-generated catch block e1.printStackTrace(); } try { if
	 * (!clazz.getName().equals("dbms.parser.IStatment")) { IStatment object =
	 * (IStatment) cons.newInstance(); //System.out.println(object.getClass());
	 * if (object.isValid(query)) { syntex = false; object.excute(query,
	 * this.Dpms); break; } } } catch (Exception e) { // TODO Auto-generated
	 * catch block throw new SQLException(e.getMessage());
	 * //System.out.println(e.getMessage()); } }
	 */
}