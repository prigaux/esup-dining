/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esupportail.dining.batch;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * 
 * @author gsouquet
 */
public class DBManager {

	private static Connection connection;
	private static Statement statement;

	public static final String INIT_VALIDATE = "validate";
	public static final String INIT_CREATE = "create";
	public static final String INIT_UPDATE = "update";
	public static final String INIT_DROP = "drop";
	public static final String INIT_DELETE = "delete";

	private static final String FILE_CREATE = "database/create.sql";
	private static final String FILE_UPDATE = "database/update.sql";
	private static final String FILE_DELETE = "database/delete.sql";
	private static final String FILE_DROP = "database/drop.sql";

	public static void main(String[] args) throws Exception {

		Properties prop = new Properties();
		InputStream is = DBManager.class.getClassLoader().getResourceAsStream(
				"defaults.properties");

		prop.load(is);
		is.close();

		Class.forName((String) prop.get("db.driver")).newInstance();
		connection = DriverManager.getConnection((String) prop.get("db.infos"),
				(String) prop.get("db.username"),
				(String) prop.get("db.password"));
		connection.setAutoCommit(true);
		statement = connection.createStatement(
				ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);

		List<String> argValue = Arrays.asList(DBManager.INIT_CREATE,
				DBManager.INIT_UPDATE, DBManager.INIT_DELETE,
				DBManager.INIT_DROP, DBManager.INIT_VALIDATE);

		if (args.length == 0 || !argValue.contains(args[0])) {
			System.out
					.println("[ERROR] Incorrect argument, try again using -Dexec.args=\"...\""
							+ ", argument can take this values : "
							+ argValue.toString());
			return;
		}

		String returnSequence = "[INFO] All sql statements have been executed. Your tables are now ";

		if (DBManager.INIT_CREATE.equals(args[0])) {
			createTables();
			returnSequence += "created";
		}
		if (DBManager.INIT_UPDATE.equals(args[0])) {
			updateTables();
			returnSequence += "updated";
		}
		if (DBManager.INIT_DELETE.equals(args[0])) {
			deleteTables();
			returnSequence += "empty";
		}
		if (DBManager.INIT_DROP.equals(args[0])) {
			dropTables();
			returnSequence += "deleted";
		}
		System.out.println(returnSequence);

		System.exit(0);
	}

	private static void createTables() {
		dropTables();
		String[] sqlStatements = DBManager.getSQLStatements(DBManager
				.getResourceIS(DBManager.FILE_CREATE));
		multipleStatementExecute(sqlStatements);
	}

	private static void updateTables() {
		String[] sqlStatements = DBManager.getSQLStatements(DBManager
				.getResourceIS(DBManager.FILE_UPDATE));
		multipleStatementExecute(sqlStatements);
	}

	private static void dropTables() {
		String[] sqlStatements = DBManager.getSQLStatements(DBManager
				.getResourceIS(DBManager.FILE_DROP));
		multipleStatementExecute(sqlStatements);
	}

	private static void deleteTables() {
		String[] sqlStatements = DBManager.getSQLStatements(DBManager
				.getResourceIS(DBManager.FILE_DELETE));
		multipleStatementExecute(sqlStatements);
	}

	private static InputStream getResourceIS(String filename) {
		return DBManager.class.getClassLoader().getResourceAsStream(filename);
	}

	private static void multipleStatementExecute(String[] sqlStatements) {

		for (String sqlStatement : sqlStatements) {
			try {
				statement.execute(sqlStatement.trim() + ";");
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}

	}

	// @return String array of sql statements
	// This method will read file content, and remove any SQL comment and then
	// split each SQL statement into an array cell.
	private static String[] getSQLStatements(InputStream is) {

		String fileContent = "";

		BufferedReader br = null;

		try {

			String currentLine;

			br = new BufferedReader(new InputStreamReader(is));
			while ((currentLine = br.readLine()) != null) {

				int indexOfComment = currentLine.indexOf("--");

				// One line comment
				if (currentLine.startsWith("#") || indexOfComment == 0) {
					currentLine = "";
				}

				// One line comment but some instructions are before it.
				if (indexOfComment > 0) {
					currentLine = currentLine.substring(0, indexOfComment - 1);
				}

				int indexOfLongComment = currentLine.indexOf("/*");
				int indexOfLongCommentEnd = currentLine.indexOf("*/");

				// Multiple line comment with start/end delimiter
				if (indexOfLongComment != -1 && indexOfLongCommentEnd == -1) {

					// Check if there is some instructions before the starting
					// delimiter
					if (indexOfLongComment == 0) {
						currentLine = "";
					} else {
						currentLine = currentLine.substring(0,
								indexOfLongComment - 1);
						fileContent += currentLine;
					}

					// iterate over lines until with find the end delimiter
					do {
						currentLine = br.readLine();
					} while (currentLine != null && !currentLine.contains("*/"));

					int lastIndexComment = currentLine.indexOf("*/");

					if (lastIndexComment != -1) {

						// Check if there is somme content after the end
						// delimiter on the current line
						if (currentLine.endsWith("*/")) {
							currentLine = "";
						} else {
							currentLine = currentLine.substring(
									lastIndexComment + 2, currentLine.length());
						}

					}
					// One line comment with start/end delimiter
				} else if (indexOfLongComment != -1
						&& indexOfLongCommentEnd != -1) {
					currentLine = currentLine.substring(0, indexOfLongComment)
							+ currentLine.substring(indexOfLongCommentEnd + 2,
									currentLine.length());
				}

				fileContent += currentLine;
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
		return fileContent.split(";");
	}

}
