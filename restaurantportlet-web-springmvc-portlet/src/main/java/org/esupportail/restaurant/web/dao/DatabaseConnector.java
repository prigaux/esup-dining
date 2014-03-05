package org.esupportail.restaurant.web.dao;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.InitializingBean;

public class DatabaseConnector implements InitializingBean {

    private Connection connection;
    private Statement statement;
    private static String dbInitType;
    
    private static final String INIT_VALIDATE = "validate";
    private static final String INIT_CREATE   = "create";
    private static final String INIT_UPDATE   = "update";
    private static final String INIT_DROP     = "drop";
    private static final String INIT_DELETE   = "delete";

    private static final String PATH_FILE = "src/main/resources/database/";
    
    private static final String FILE_CREATE = DatabaseConnector.PATH_FILE + "create.sql";
    private static final String FILE_UPDATE = DatabaseConnector.PATH_FILE + "update.sql";
    private static final String FILE_DELETE = DatabaseConnector.PATH_FILE + "delete.sql";
    private static final String FILE_DROP   = DatabaseConnector.PATH_FILE + "drop.sql";
    
    // db connection infos in src/main/resources/defaults.properties
    public DatabaseConnector(String db_driver, String db_infos, String db_user, String db_pwd, String db_init_type) {
        try {
            Class.forName(db_driver).newInstance();
            this.connection = DriverManager.getConnection(db_infos, db_user, db_pwd);
            this.connection.setAutoCommit(true);
            this.statement = connection.createStatement(ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_UPDATABLE);
        } catch (Exception e) {
            // Problem with the db connection
            e.printStackTrace();
        }

        this.dbInitType = db_init_type;
    }

    public ResultSet executeQuery(String query) throws SQLException {
        return statement.executeQuery(query);
    }

    public void executeUpdate(String update) throws SQLException {
        statement.executeUpdate(update);
    }

    @Override
    public void afterPropertiesSet() throws Exception {

        if (dbInitType.equalsIgnoreCase(DatabaseConnector.INIT_CREATE))
            this.createTables();
        
        if (dbInitType.equalsIgnoreCase(DatabaseConnector.INIT_UPDATE))
            this.updateTables();

        if (dbInitType.equalsIgnoreCase(DatabaseConnector.INIT_DROP))
            this.dropTables();

        if (dbInitType.equalsIgnoreCase(DatabaseConnector.INIT_DELETE))
            this.deleteTables();

    }
    
    private void createTables() {
        this.dropTables();
        String[] sqlStatements = this.getFileContent(DatabaseConnector.FILE_CREATE);
        this.multipleStatementExecute(sqlStatements);
    }
    
    private void updateTables() {
        String[] sqlStatements = this.getFileContent(DatabaseConnector.FILE_UPDATE);
        this.multipleStatementExecute(sqlStatements);
    }
    
    private void dropTables() {
        String[] sqlStatements = this.getFileContent(DatabaseConnector.FILE_DROP);
        this.multipleStatementExecute(sqlStatements);
    }
    
    private void deleteTables() {
        String[] sqlStatements = this.getFileContent(DatabaseConnector.FILE_DELETE);
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

    private String[] getFileContent(String url) {
        
        String fileContent = "";
        
        BufferedReader br = null;
        
        try {
            
            String currentLine;
            
            br = new BufferedReader(new FileReader(url));
            while( (currentLine = br.readLine()) != null ) {
                
                
                int indexOfComment = currentLine.indexOf("--");
                
                if(currentLine.startsWith("#") || indexOfComment == 0) {
                    currentLine="";
                }
                
                if(indexOfComment > 0) {
                    currentLine = currentLine.substring(0, indexOfComment-1);
                }
                
                int indexOfLongComment = currentLine.indexOf("/*");
                int indexOfLongCommentEnd = currentLine.indexOf("*/");
                
                if(indexOfLongComment != -1 && indexOfLongCommentEnd == -1) {
                    
                    if(indexOfLongComment == 0) {
                        currentLine = "";
                    } else {
                        currentLine = currentLine.substring(0, indexOfLongComment -1);
                        fileContent+=currentLine;
                    } 
                    
                    do {
                        currentLine = br.readLine();
                    } while (currentLine != null && !currentLine.contains("*/"));
                    
                    int lastIndexComment = currentLine.indexOf("*/");
                    if(lastIndexComment != -1) {
                        
                        if(currentLine.endsWith("*/")) {
                            currentLine = "";
                        } else {
                            currentLine = currentLine.substring(lastIndexComment+2, currentLine.length());
                        }
                        
                    }
 
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
