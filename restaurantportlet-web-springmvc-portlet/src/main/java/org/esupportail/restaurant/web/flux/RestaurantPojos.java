package org.esupportail.restaurant.web.flux;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.JCodeModel;

public class RestaurantPojos {

    private final File jsonSchema = new File("src/main/resources/mock-data/portlet-schema.json"); 
    private final File outputFolder = new File("src/main/java/org/esupportail/restaurant/web/model");
    
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
	        new SchemaMapper().generate(codeModel, "RestaurantFeedRoot", "org.esupportail.restaurant.web.model.model", jsonSchemaUrl);
	        codeModel.build(this.outputFolder);
	        System.out.println("POJOs successfully generated.");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
}