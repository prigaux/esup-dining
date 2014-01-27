package org.esupportail.restaurant.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

	private Connection connection;
	private Statement statement;
	
	// db connection infos in src/main/resources/defaults.properties
	public DatabaseConnector(String db_driver, String db_infos, String db_user, String db_pwd) {
	    try {
            Class.forName(db_driver).newInstance();
            this.connection = DriverManager.getConnection(db_infos, db_user, db_pwd);
            this.connection.setAutoCommit(true);
            this.statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
	    } catch (Exception e) {
	        // Problem with the db connection
            e.printStackTrace();
        }
	}

	public void deleteTables() {
        try {
            statement.executeUpdate("DROP TABLE IF EXISTS FAVORITERESTAURANT");
            statement.executeUpdate("DROP TABLE IF EXISTS USERAREA");
            statement.executeUpdate("DROP TABLE IF EXISTS PATHFLUX");
            statement.executeUpdate("DROP TABLE IF EXISTS NUTRITIONPREFERENCES");
        } catch (SQLException e1) {
            e1.printStackTrace();
        }    
	}
	
	public void createTables() {		
		
	    this.deleteTables();
	    
		try {
			statement.executeUpdate("CREATE TABLE FAVORITERESTAURANT(USERNAME VARCHAR(100), RESTAURANTID VARCHAR(100), CONSTRAINT pk_fav PRIMARY KEY (USERNAME, RESTAURANTID))");
		} catch (SQLException e) {
			System.out.println("[INFO] Table favoriteRestaurant already exist");
		}
		try {
			statement.executeUpdate("CREATE TABLE USERAREA (USERNAME VARCHAR(500), AREANAME VARCHAR(200), CONSTRAINT pk_userarea PRIMARY KEY (USERNAME, AREANAME))");
		} catch (SQLException e) {
			System.out.println("[INFO] Table USERAREA already exist");
		}
		try {
			statement.executeUpdate("CREATE TABLE PATHFLUX (URLFLUX VARCHAR(500) PRIMARY KEY, AREANAME VARCHAR(200))");
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
