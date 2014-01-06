package org.esupportail.restaurant.web.flux;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.JCodeModel;

public class RestaurantPojos {

    private final File jsonSchema = new File("src/main/resources/schema/portlet-schema.json"); 
    private final File outputFolder = new File("src/main/java");
    
    public RestaurantPojos() throws Exception {
    	
    	if(outputFolder.listFiles().length > 0) this.deleteFilesFromFolder(outputFolder);
    	this.generatePOJO();  
    }
    
    
    private void deleteFilesFromFolder(File folder) {
    	for(File f : folder.listFiles()) {
    		boolean success = f.delete();
    		if(!success) {
    			System.out.println(f + " couldn't be deleted.");
    		}
    	}
    }
    
    private void generatePOJO() {
        JCodeModel codeModel = new JCodeModel();
        // method give in the javadoc, looks dirty but recommanded
        URL jsonSchemaUrl;
		try {
			jsonSchemaUrl = jsonSchema.toURI().toURL();
	        new SchemaMapper().generate(codeModel, "RestaurantFeedRoot", "org.esupportail.restaurant.web.model", jsonSchemaUrl);
	        codeModel.build(this.outputFolder);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}