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
	
	private DatabaseConnector() {
		try {
			Class.forName("org.hsqldb.jdbcDriver").newInstance();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		try {
			this.connection = DriverManager.getConnection("jdbc:hsqldb:file:restaurant", "sa",  "");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			this.connection.setAutoCommit(true);
			this.statement = connection.createStatement();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		this.createTables();
		this.instance = this;
	}
	
	public static DatabaseConnector getInstance() {
		if(instance == null)
			instance = new DatabaseConnector();
		return instance;
	}
	
	public void createTables() {
		try {
			statement.executeUpdate("CREATE TABLE favoriteRestaurant(user VARCHAR(100), restaurantId INT)");
			statement.executeUpdate("CREATE TABLE defaultArea(user VARCHAR(100), areaName VARCHAR(200)");
		} catch (SQLException e) {
			System.out.println("[INFO] Table favoriteRestaurant already exist");
		}
	}
	
	public ResultSet executeQuery(String query) {
		ResultSet results = null;
		try {
			// "SELECT restaurantId FROM favoriteRestaurant WHERE user='"+ username +"'"
			results = statement.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return results;
	}
	
	public void executeUpdate(String update) {
		try {
			statement.executeUpdate(update);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}
