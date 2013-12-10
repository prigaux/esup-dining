package org.esupportail.restaurant.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.esupportail.restaurant.web.flux.RestaurantCache;

public class DatabaseConnector {

	private Connection connection;
	private Statement statement;
	private static DatabaseConnector instance;

	private static final String DB_INFOS = "jdbc:hsqldb:file:restaurant";
	private static final String DB_USER = "sa";
	private static final String DB_PWD = "";
	
	private DatabaseConnector() {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			this.connection = DriverManager.getConnection(DatabaseConnector.DB_INFOS, DatabaseConnector.DB_USER,  DatabaseConnector.DB_PWD);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.connection.setAutoCommit(true);
			this.statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.createTables();
		DatabaseConnector.instance = this;
	}
	
	public static DatabaseConnector getInstance() {
		if(instance == null)
			instance = new DatabaseConnector();
		return instance;
	}
	
	public void createTables() {		

		/*try {
			statement.executeUpdate("DROP TABLE FAVORITERESTAURANT");
			statement.executeUpdate("DROP TABLE USERAREA");
			statement.executeUpdate("DROP TABLE PATHFLUX");
			statement.executeUpdate("DROP TABLE NUTRITIONPREFERENCES");
		} catch (SQLException e1) {
			e1.printStackTrace();
		}*/
		
		try {
			statement.executeUpdate("CREATE TABLE FAVORITERESTAURANT(USERNAME VARCHAR(100), RESTAURANTID VARCHAR(100))");
		} catch (SQLException e) {
			System.out.println("[INFO] Table favoriteRestaurant already exist");
		}
		try {
			statement.executeUpdate("CREATE TABLE USERAREA (USERNAME VARCHAR(500), AREANAME VARCHAR(200))");
		} catch (SQLException e) {
			System.out.println("[INFO] Table USERAREA already exist");
		}
		try {
			statement.executeUpdate("CREATE TABLE PATHFLUX (URLFLUX VARCHAR(500), AREANAME VARCHAR(200))");
		} catch (SQLException e) {
			System.out.println("[INFO] Table pathFlux already exist");
		}
		try {
			statement.executeUpdate("CREATE TABLE nutritionPreferences (USERNAME varchar(100), NUTRITIONCODE INTEGER, CONSTRAINT pk_nutrition PRIMARY KEY (USERNAME, NUTRITIONCODE))");
		} catch (SQLException e ) {
			System.out.println("[INFO] Table nutritionPreferences already exist");
		}
		
		
	}
	
	public ResultSet executeQuery(String query) throws SQLException {
		return statement.executeQuery(query);
	}
	
	public void executeUpdate(String update) throws SQLException {
		statement.executeUpdate(update);
	}
	
}
