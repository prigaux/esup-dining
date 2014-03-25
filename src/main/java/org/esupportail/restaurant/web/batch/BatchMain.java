/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.esupportail.restaurant.web.batch;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import org.esupportail.restaurant.web.dao.DatabaseConnector;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 *
 * @author gsouquet
 */
public class BatchMain {
    
    public static void main(String[] args) throws IOException, SQLException {
        
        ClassPathXmlApplicationContext springContext = new ClassPathXmlApplicationContext("classpath*:/applicationContext*.xml");
        
        List<String> argValue = Arrays.asList(DatabaseConnector.INIT_CREATE, 
                DatabaseConnector.INIT_UPDATE, 
                DatabaseConnector.INIT_DELETE, 
                DatabaseConnector.INIT_DROP, 
                DatabaseConnector.INIT_VALIDATE);
        
        if (args.length == 0 || !argValue.contains(args[0])) {
            System.out.println("[ERROR] Incorrect argument, try again using -Dexec.args=\"...\""
                             + ", argument can take this values : " + argValue.toString());
            return;
        }
        
        DatabaseConnector dbConnector = springContext.getBean("DatabaseConnector", DatabaseConnector.class);
        
        if (DatabaseConnector.INIT_CREATE.equals(args[0]))
            dbConnector.createTables();
        if (DatabaseConnector.INIT_UPDATE.equals(args[0]))
            dbConnector.updateTables();
        if (DatabaseConnector.INIT_DELETE.equals(args[0]))
            dbConnector.deleteTables();
        if (DatabaseConnector.INIT_DROP.equals(args[0]))
            dbConnector.dropTables();
        
        return;
    }
}
