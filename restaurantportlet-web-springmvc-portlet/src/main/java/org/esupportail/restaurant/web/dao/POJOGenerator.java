package org.esupportail.restaurant.web.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.JCodeModel;

public class POJOGenerator {

	private final String schemaLocation;
	private final String outputDir;
	
	public POJOGenerator() throws Exception {
		this.schemaLocation = "http://www.souquet.eu/test/v2/schema.json";   
		File f = new File(new File(".").getAbsolutePath());
		this.outputDir =  f.getCanonicalPath() + "/src/main/java";
				
		//this.generatePOJO();  
	}
	
	private void generatePOJO() {
		JCodeModel codeModel = new JCodeModel();
	    URL source = null;
		try {
			source = new URL(this.schemaLocation);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    try {
			new SchemaMapper().generate(codeModel, "RestaurantFeedRoot", "org.esupportail.restaurant.web.json", source);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    File currentDirectory = new File(this.outputDir);
		try {
			codeModel.build(new File(currentDirectory.getCanonicalPath()));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
