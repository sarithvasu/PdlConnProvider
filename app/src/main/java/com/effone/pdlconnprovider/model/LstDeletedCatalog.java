
package com.effone.pdlconnprovider.model;

import java.util.HashMap;
import java.util.Map;

public class LstDeletedCatalog {

    private Integer TestCatalogID;
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

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
