package org.esupportail.restaurant.web.dao;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.InitializingBean;

public class DatabaseConnector {

    private Connection connection;
    private Statement statement;
    private String dbInitType;
    
    public static final String INIT_VALIDATE = "validate";
    public static final String INIT_CREATE   = "create";
    public static final String INIT_UPDATE   = "update";
    public static final String INIT_DROP     = "drop";
    public static final String INIT_DELETE   = "delete";
    
    private static final String FILE_CREATE = "/database/create.sql";
    private static final String FILE_UPDATE = "/database/update.sql";
    private static final String FILE_DELETE = "/database/delete.sql";
    private static final String FILE_DROP   = "/database/drop.sql";
    
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
    
    private InputStream getResourceIS(String filename) {
        return this.getClass().getResourceAsStream(filename);
    }
    
    public void createTables() {
        this.dropTables();
        String[] sqlStatements = this.getSQLStatements(this.getResourceIS(DatabaseConnector.FILE_CREATE));
        this.multipleStatementExecute(sqlStatements);
    }
    
    public void updateTables() {
        String[] sqlStatements = this.getSQLStatements(getResourceIS(DatabaseConnector.FILE_UPDATE));
        this.multipleStatementExecute(sqlStatements);
    }
    
    public void dropTables() {
        String[] sqlStatements = this.getSQLStatements(getResourceIS(DatabaseConnector.FILE_DROP));
        this.multipleStatementExecute(sqlStatements);
    }
    
    public void deleteTables() {
        String[] sqlStatements = this.getSQLStatements(getResourceIS(DatabaseConnector.FILE_DELETE));
        this.multipleStatementExecute(sqlStatements);
    }
    
    private void multipleStatementExecute(String[] sqlStatements) {
        
        for(String sqlStatement : sqlStatements) {
            try {
                this.statement.execute(sqlStatement.trim() + ";");
            } catch (SQLException ex) {
                Logger.getLogger(DatabaseConnector.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
     
    }

//  @return String array of sql statements
//  This method will read file content, and remove any SQL comment and then split each SQL statement into an array cell.  
    private String[] getSQLStatements(InputStream is) {
        
        String fileContent = "";
        
        BufferedReader br = null;
        
        try {
            
            String currentLine;
            
            br = new BufferedReader(new InputStreamReader(is));
            while( (currentLine = br.readLine()) != null ) {
                
                
                int indexOfComment = currentLine.indexOf("--");
                
                // One line comment
                if(currentLine.startsWith("#") || indexOfComment == 0) {
                    currentLine="";
                }
                
                // One line comment but some instructions are before it.
                if(indexOfComment > 0) {
                    currentLine = currentLine.substring(0, indexOfComment-1);
                }
                
                int indexOfLongComment = currentLine.indexOf("/*");
                int indexOfLongCommentEnd = currentLine.indexOf("*/");
                
                // Multiple line comment with start/end delimiter
                if(indexOfLongComment != -1 && indexOfLongCommentEnd == -1) {
                    
                    // Check if there is some instructions before the starting delimiter
                    if(indexOfLongComment == 0) {
                        currentLine = "";
                    } else {
                        currentLine = currentLine.substring(0, indexOfLongComment -1);
                        fileContent+=currentLine;
                    } 
                    
                    // iterate over lines until with find the end delimiter
                    do {
                        currentLine = br.readLine();
                    } while (currentLine != null && !currentLine.contains("*/"));
                    
                    int lastIndexComment = currentLine.indexOf("*/");
                    
                    if(lastIndexComment != -1) {
                        
                        // Check if there is somme content after the end delimiter on the current line
                        if(currentLine.endsWith("*/")) {
                            currentLine = "";
                        } else {
                            currentLine = currentLine.substring(lastIndexComment+2, currentLine.length());
                        }
                        
                    }
                 // One line comment with start/end delimiter   
                } else if (indexOfLongComment != -1 && indexOfLongCommentEnd != -1) { 
                   currentLine = currentLine.substring(0, indexOfLongComment) + currentLine.substring(indexOfLongCommentEnd+2, currentLine.length());                   
                }
                 
                fileContent+=currentLine;
            }
            
        } catch(IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) br.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return fileContent.split(";");
    }

}
