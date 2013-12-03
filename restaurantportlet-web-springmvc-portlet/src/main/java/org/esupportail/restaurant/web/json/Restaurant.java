
package org.esupportail.restaurant.web.json;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Generated;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.googlecode.ehcache.annotations.Cacheable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Generated("com.googlecode.jsonschema2pojo")
@JsonPropertyOrder({
    "access",
    "accessibility",
    "address",
    "area",
    "closing",
    "contact",
    "description",
    "id",
    "lat",
    "lon",
    "menus",
    "opening",
    "operationalhours",
    "photo",
    "shortdesc",
    "title",
    "type"
})
public class Restaurant {

    /**
     * 
     */
    @JsonProperty("access")
    private String access;
    /**
     * 
     */
    @JsonProperty("accessibility")
    private Boolean accessibility;
    /**
     * 
     */
    @JsonProperty("address")
    private String address;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    private String area;
    /**
     * 
     */
    @JsonProperty("closing")
    private String closing;
    /**
     * 
     */
    @JsonProperty("contact")
    private Contact contact;
    /**
     * 
     */
    @JsonProperty("description")
    private String description;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    private Integer id;
    /**
     * 
     */
    @JsonProperty("lat")
    private Double lat;
    /**
     * 
     */
    @JsonProperty("lon")
    private Double lon;
    /**
     * 
     */
    @JsonProperty("menus")
    private List<Manus> menus = new ArrayList<Manus>();
    /**
     * 
     */
    @JsonProperty("opening")
    private String opening;
    /**
     * 
     */
    @JsonProperty("operationalhours")
    private String operationalhours;
    /**
     * 
     */
    @JsonProperty("photo")
    private Photo photo;
    /**
     * 
     */
    @JsonProperty("shortdesc")
    private String shortdesc;
    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    private String title;
    /**
     * 
     */
    @JsonProperty("type")
    private String type;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     */
    @JsonProperty("access")
    public String getAccess() {
        return access;
    }

    /**
     * 
     */
    @JsonProperty("access")
    public void setAccess(String access) {
        this.access = access;
    }

    /**
     * 
     */
    @JsonProperty("accessibility")
    public Boolean getAccessibility() {
        return accessibility;
    }

    /**
     * 
     */
    @JsonProperty("accessibility")
    public void setAccessibility(Boolean accessibility) {
        this.accessibility = accessibility;
    }

    /**
     * 
     */
    @JsonProperty("address")
    public String getAddress() {
        return address;
    }

    /**
     * 
     */
    @JsonProperty("address")
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    public String getArea() {
        return area;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("area")
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 
     */
    @JsonProperty("closing")
    public String getClosing() {
        return closing;
    }

    /**
     * 
     */
    @JsonProperty("closing")
    public void setClosing(String closing) {
        this.closing = closing;
    }

    /**
     * 
     */
    @JsonProperty("contact")
    public Contact getContact() {
        return contact;
    }

    /**
     * 
     */
    @JsonProperty("contact")
    public void setContact(Contact contact) {
        this.contact = contact;
    }

    /**
     * 
     */
    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    /**
     * 
     */
    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public Integer getId() {
        return id;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("id")
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public Double getLat() {
        return lat;
    }

    /**
     * 
     */
    @JsonProperty("lat")
    public void setLat(Double lat) {
        this.lat = lat;
    }

    /**
     * 
     */
    @JsonProperty("lon")
    public Double getLon() {
        return lon;
    }

    /**
     * 
     */
    @JsonProperty("lon")
    public void setLon(Double lon) {
        this.lon = lon;
    }

    /**
     * 
     */
    @JsonProperty("menus")
    public List<Manus> getMenus() {
        return menus;
    }

    /**
     * 
     */
    @JsonProperty("menus")
    public void setMenus(List<Manus> menus) {
        this.menus = menus;
    }

    /**
     * 
     */
    @JsonProperty("opening")
    public String getOpening() {
        return opening;
    }

    /**
     * 
     */
    @JsonProperty("opening")
    public void setOpening(String opening) {
        this.opening = opening;
    }

    /**
     * 
     */
    @JsonProperty("operationalhours")
    public String getOperationalhours() {
        return operationalhours;
    }

    /**
     * 
     */
    @JsonProperty("operationalhours")
    public void setOperationalhours(String operationalhours) {
        this.operationalhours = operationalhours;
    }

    /**
     * 
     */
    @JsonProperty("photo")
    public Photo getPhoto() {
        return photo;
    }

    /**
     * 
     */
    @JsonProperty("photo")
    public void setPhoto(Photo photo) {
        this.photo = photo;
    }

    /**
     * 
     */
    @JsonProperty("shortdesc")
    public String getShortdesc() {
        return shortdesc;
    }

    /**
     * 
     */
    @JsonProperty("shortdesc")
    public void setShortdesc(String shortdesc) {
        this.shortdesc = shortdesc;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    public String getTitle() {
        return title;
    }

    /**
     * 
     * (Required)
     * 
     */
    @JsonProperty("title")
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * 
     */
    @JsonProperty("type")
    public String getType() {
        return type;
    }

    /**
     * 
     */
    @JsonProperty("type")
    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public boolean equals(Object other) {
        return EqualsBuilder.reflectionEquals(this, other);
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperties(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
