
package com.effone.pdlconnprovider.model.test_cat_full;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class TestCalFullData {

    private Integer TestCatalogID;
    private String DisplayCode;
    private String URI;
    private String Code;
    private String Name;
    private String Location;
    private String CreatedDate;
    private String status;
    private List<LstTestCatalogDetail> lstTestCatalogDetails = new ArrayList<LstTestCatalogDetail>();
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
     *     The uRI
     */
    public String getURI() {
        return URI;
    }

    /**
     * 
     * @param uRI
     *     The URI
     */
    public void setURI(String uRI) {
        this.URI = uRI;
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

    /**
     * 
     * @return
     *     The createdDate
     */
    public String getCreatedDate() {
        return CreatedDate;
    }

    /**
     * 
     * @param createdDate
     *     The CreatedDate
     */
    public void setCreatedDate(String createdDate) {
        this.CreatedDate = createdDate;
    }

    /**
     * 
     * @return
     *     The status
     */
    public String getStatus() {
        return status;
    }

    /**
     * 
     * @param status
     *     The status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * 
     * @return
     *     The lstTestCatalogDetails
     */
    public List<LstTestCatalogDetail> getLstTestCatalogDetails() {
        return lstTestCatalogDetails;
    }

    /**
     * 
     * @param lstTestCatalogDetails
     *     The lstTestCatalogDetails
     */
    public void setLstTestCatalogDetails(List<LstTestCatalogDetail> lstTestCatalogDetails) {
        this.lstTestCatalogDetails = lstTestCatalogDetails;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
