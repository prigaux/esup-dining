package org.esupportail.restaurant.web.flux;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.jdom2.transform.XSLTransformException;
import org.jdom2.transform.XSLTransformer;

public class RestaurantParser {

	private static final File CROUS_STYLESHEET = new File("src/main/resources/schema/portlet.xsl");
	
	private Document xml;
	
	private Document restaurants;
	private Document menus;

	public RestaurantParser(URL restaurantsURL, URL menuURL) {	
		this.restaurants = this.buildDocument(restaurantsURL);
		this.menus = this.buildDocument(menuURL);
		this.xml = mergeFeeds(this.restaurants, this.menus);
	}
	
	private Document mergeFeeds(Document restaurants, Document menus) {
		
		Document newDc = new Document();
		newDc.addContent(new Element("root"));
		Element root = newDc.getRootElement();
	
		List<Element> listRestaurants = restaurants.getRootElement().getChildren();
		List<Element> listMenus = menus.getRootElement().getChildren();

		for(Element el : listRestaurants) {
			Element newEl = el.clone();
			root.addContent(newEl.detach());
		}
		for(Element el : listMenus) {
			Element newEl = el.clone();
			root.addContent(newEl.detach());
		}
		return newDc;
	}
	
	public Document buildDocument(URL docUrl) {
		Document doc = new Document();
		SAXBuilder builder = new SAXBuilder();
		try {
			doc = builder.build(docUrl.openStream());
		} catch (JDOMException e) {
			System.out.println("JDOM Error : " + e);
		} catch (IOException e2) {
			System.out.println("I/O Error : " + e2);
		}
		return doc;
	}
	
	public String buildJsonString() {
		Document doc = null;
		try {
			doc = this.transf(this.xml, RestaurantParser.CROUS_STYLESHEET);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return doc != null ? doc.getRootElement().getText().trim() : null;
	}

	public Document transf(Document source, File stylesheetFile) {
		Document modifiedDoc = null;
		XSLTransformer transformer = null;
		try {
			transformer = new XSLTransformer(stylesheetFile);
			modifiedDoc = transformer.transform(source);
		} catch (XSLTransformException e) {
			e.printStackTrace();
		}		
		return modifiedDoc;
	}
	
	public Document getXML() {
		return this.xml;
	}	
}

