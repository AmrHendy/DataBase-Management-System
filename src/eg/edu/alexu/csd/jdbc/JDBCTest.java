package eg.edu.alexu.csd.jdbc;

import static org.junit.Assert.*;

import java.io.File;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.junit.Test;

public class JDBCTest {

	@Test
	public void test() throws SQLException {
		Driver driver = new DriverImp();
		boolean resultFalse = driver.acceptsURL("anyUrl");
		assertFalse(resultFalse);
	}

	@Test
	public void test2() throws SQLException {
		Driver driver = new DriverImp();
		boolean resultTrue = driver.acceptsURL("jdbc:xmldb://localhost");
		assertTrue(resultTrue);
	}

	@Test
	public void test3() throws SQLException {
		Driver driver = new DriverImp();
		boolean resultTrue = driver.acceptsURL("jdbc:xmldb://localhost");
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Object con = driver.connect("jdbc:xmldb://localhost", info);
		assertTrue(con instanceof ConnectionImp);
	}

	@Test
	public void test4() throws SQLException {
		Driver driver = new DriverImp();
		boolean resultTrue = driver.acceptsURL("jdbc:altdb://localhost");
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Object con = driver.connect("jdbc:altdb://localhost", info);
		assertTrue(con instanceof ConnectionImp);
	}

	@Test
	public void test5() throws SQLException {
		Driver driver = new DriverImp();
		boolean resultTrue = driver.acceptsURL("jdbc:altdb://localhost");
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("create Database test1;");
			stat.execute("create Database test1;");
		} catch (SQLException e) {
			assertTrue(true);
		}
	}

	@Test
	public void test6() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database test1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
		} catch (SQLException e) {
			assertTrue(false);
		}
	}

	@Test
	public void test7() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database tesT1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
		} catch (SQLException e) {
			assertTrue(false);
		}
	}

	@Test
	public void test8() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database tesT1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
			stat.execute("create table x2(id int ,name varchar ,birthday date);");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x2       values     (1    ,'value'   ,'1792-8-7'  )   ;  ");
		} catch (SQLException e) {
			assertTrue(false);
		}
		assertTrue(true);
	}

	@Test
	public void test9() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database test1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
			stat.execute("create table x2(id int ,name varchar ,birthday date);");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x2       values     (1    ,'value'   ,'1792-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x2       values     (1    ,'value'   ,'1792-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			ResultSet set = stat.getResultSet();
			if (set != null) {
				assertFalse(true);
			}
		} catch (SQLException e) {
			assertTrue(false);
		}
		assertTrue(true);
	}

	public void test10() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database test1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			stat.execute("select * form x");
			ResultSet set = stat.getResultSet();
			if (set != null) {
				assertTrue(true);
			}
		} catch (SQLException e) {
			assertTrue(false);
		}
		assertTrue(true);
	}

	public void test11() throws SQLException {
		Driver driver = new DriverImp();
		Properties info = new Properties();
		info.put("path", new File("dataBases"));
		Connection con = driver.connect("jdbc:altdb://localhost", info);
		Statement stat = con.createStatement();
		try {
			stat.execute("drop database test1");
		} catch (Exception e) {

		}
		try {
			stat.execute("create Database test1;");
			stat.execute("create table x(id int ,name varchar ,birthday date);");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value'   ,'1992-8-7'  )   ;  ");
			stat.execute("insert       into x       values     (1    ,'value2'   ,'1792-8-7'  )   ;  ");
			stat.execute("select * form x;");
			ResultSet set = stat.getResultSet();
			if (set != null) {
				assertTrue(true);
			}
			int row = 0;
			while (set.next())
				row++;
			if (row == 4) {
				assertTrue(true);
			}
		} catch (SQLException e) {
			assertTrue(false);
		}
		assertTrue(true);
	}
}
