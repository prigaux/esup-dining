package org.esupportail.dining.web.feed;

import org.codehaus.jackson.annotate.JsonProperty;

public class FeedInformation {

	@JsonProperty("id")
	private int id;
	@JsonProperty("name")
	private String name;
	@JsonProperty("arename")
	private String areaname;
	@JsonProperty("url")
	private String path;
	@JsonProperty("is_default")
	private boolean isDefault;
	
	// Dummy constructor for Jackson
	public FeedInformation() {
		
	}
	
	public FeedInformation(int id, String name, String areaname, String path,
			boolean isDefault) {
		super();
		this.id = id;
		this.name = name;
		this.areaname = areaname;
		this.path = path;
		this.isDefault = isDefault;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAreaname() {
		return areaname;
	}
	public void setAreaname(String areaname) {
		this.areaname = areaname;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}	

	public boolean isDefault() {
		return isDefault;
	}
	public boolean getIsDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}
	
	
	
}
