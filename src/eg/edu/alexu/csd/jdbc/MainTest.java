package eg.edu.alexu.csd.jdbc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.Scanner;

public class MainTest {
	public static void main(String[] args) throws SQLException {
		Driver driver = new DriverImp();
		File x = new File(getProperty("MainTest.pathName"), getProperty("MainTest.fileName")); //$NON-NLS-1$ //$NON-NLS-2$
		Properties info = new Properties();
		info.put(getProperty("MainTest.path"), x); //$NON-NLS-1$
		Connection con = driver.connect(getProperty("MainTest.protocal"), info); //$NON-NLS-1$
		Statement stat = con.createStatement();
		System.out.println(getProperty("MainTest.userName"));
		while (true) {
			Scanner scan = new Scanner(System.in);
			try {
				stat.execute(scan.nextLine());
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}
	}

	public static String getProperty(String name) {
		String versionString = null;
		// to load application's properties, we use this class
		Properties mainProperties = new Properties();
		FileInputStream file = null;
		// the base folder is ./, the root of the main.properties file
		String path = "./messages.properties";
		// load the file handle for main.properties
		try {
			file = new FileInputStream(path);
		} catch (FileNotFoundException e) {
		}
		// load all the properties from this file
		try {
			mainProperties.load(file);
		} catch (IOException e) {
		}
		// we have loaded the properties, so close the file handle
		try {
			file.close();
		} catch (IOException e) {
		}
		// retrieve the property we are intrested, the app.version
		versionString = mainProperties.getProperty(name);
		return versionString;
	}
}
