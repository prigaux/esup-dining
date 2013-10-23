package org.esupportail.restaurant.web.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

import org.esupportail.restaurant.web.model.bindings.*;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.googlecode.jsonschema2pojo.SchemaMapper;
import com.sun.codemodel.JCodeModel;

public class POJOGenerator {

	private final String schemaLocation;
	private final String outputDir;
	
	private ObjectMapper mapper;
	
	public POJOGenerator() throws Exception {
	
	   this.schemaLocation = "http://www.souquet.eu/test/schema.json";
	   
	   File f = new File(new File(".").getAbsolutePath());
	   
		this.outputDir =  f.getCanonicalPath() + "/src/main/java";

		this.mapper = new ObjectMapper();
		
	   this.generatePOJO();
	    
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
			new SchemaMapper().generate(codeModel, "BindingsRestaurant", "org.esupportail.restaurant.web.model.bindings", source);
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
	
	public BindingsRestaurant mapJson() {		
	
	InputStream inputstream = null;
	try {
		inputstream = new FileInputStream("http://www.souquet.eu/test/flux.json");
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	BindingsRestaurant resto = null;
	try {
		resto = mapper.readValue(inputstream, BindingsRestaurant.class);
	} catch (JsonParseException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (JsonMappingException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
	
	return resto;
	}
	
	// Create Area zone
	/*JsonNode areas = rootNode.path("areas");
	ArrayList<Area> listeArea = new ArrayList<Area>();
	for(JsonNode area : areas) {
		listeArea.add(mapper.readValue(area.toString(), Area.class));
	}*/
	
}
