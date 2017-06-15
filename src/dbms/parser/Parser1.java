package dbms.parser;

import java.io.File;
import java.lang.reflect.Constructor;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import com.eztech.util.JavaClassFinder;
import dataBase.control.DataBaseControlImpl;

public class Parser1 implements IParser {

	private DataBaseControlImpl Dpms;
	private List<Class<? extends IStatment>> classes;

	public Parser1(String path, String type) {
		this.Dpms = new DataBaseControlImpl(path, type);
		JavaClassFinder classFinder = new JavaClassFinder();
		classes = classFinder.findAllMatchingTypes(IStatment.class);
	}

	@Override
	public void InsertQuery(String query) throws SQLException {
		boolean syntex = true;
		query = query.replaceAll("\"", "'");

		for (Class clazz : classes) {
			Constructor<?> cons = null;
			try {
				if (!clazz.getName().equals("dbms.parser.IStatment")) //$NON-NLS-1$
					cons = clazz.getConstructor(null);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				if (!clazz.getName().equals("dbms.parser.IStatment")) {
					IStatment object = (IStatment) cons.newInstance();
					if (object.isValid(query)) {
						syntex = false;
						object.excute(query, this.Dpms);
						break;
					}
				}
			} catch (Exception e) {
				throw new SQLException(e.getMessage());
			}
		}
		if (syntex)
			throw new SQLException("syntex error");
	}

	public DataBaseControlImpl getDatabaseManger() {
		return this.Dpms;
	}
}