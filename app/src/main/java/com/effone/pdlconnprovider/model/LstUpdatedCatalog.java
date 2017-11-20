
package com.effone.pdlconnprovider.model;

import java.util.HashMap;
import java.util.Map;



public class LstUpdatedCatalog {

    private Integer TestCatalogID;
    private String DisplayCode;
    private String Code;
    private String Name;
    private String Location;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The testCatalogID
     */
    public Integer getTestCatalogID() {
        return TestCatalogID;
    }

    /**
     * 
     * @param testCatalogID
     *     The TestCatalogID
     */
    public void setTestCatalogID(Integer testCatalogID) {
        this.TestCatalogID = testCatalogID;
    }

    /**
     * 
     * @return
     *     The displayCode
     */
    public String getDisplayCode() {
        return DisplayCode;
    }

    /**
     * 
     * @param displayCode
     *     The DisplayCode
     */
    public void setDisplayCode(String displayCode) {
        this.DisplayCode = displayCode;
    }

    /**
     * 
     * @return
     *     The code
     */
    public String getCode() {
        return Code;
    }

    /**
     * 
     * @param code
     *     The Code
     */
    public void setCode(String code) {
        this.Code = code;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return Name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    public void setName(String name) {
        this.Name = name;
    }

    /**
     * 
     * @return
     *     The location
     */
    public String getLocation() {
        return Location;
    }

    /**
     * 
     * @param location
     *     The Location
     */
    public void setLocation(String location) {
        this.Location = location;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
