
package com.effone.pdlconnprovider.model.test_cat_full;

import java.util.HashMap;
import java.util.Map;



public class LstTestCatalogDetail {

    private Integer TestCatalogDetailsID;
    private Integer TestCatalogID;
    private String Name;
    private String Value;
    private String CreatedDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The testCatalogDetailsID
     */
    public Integer getTestCatalogDetailsID() {
        return TestCatalogDetailsID;
    }

    /**
     * 
     * @param testCatalogDetailsID
     *     The TestCatalogDetailsID
     */
    public void setTestCatalogDetailsID(Integer testCatalogDetailsID) {
        this.TestCatalogDetailsID = testCatalogDetailsID;
    }

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
     *     The value
     */
    public String getValue() {
        return Value;
    }

    /**
     * 
     * @param value
     *     The Value
     */
    public void setValue(String value) {
        this.Value = value;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
