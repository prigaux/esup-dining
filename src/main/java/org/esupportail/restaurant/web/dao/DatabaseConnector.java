package org.esupportail.restaurant.web.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnector {

    private Connection connection;
    private Statement statement;
    private String dbInitType;

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

    public ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public void executeUpdate(String update) throws SQLException {
        statement.executeUpdate(update);
    }

}
