
package com.effone.pdlconnprovider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestCatUpdate {

    private String ServerDate;
    private List<LstDeletedCatalog> lstDeletedCatalog = new ArrayList<LstDeletedCatalog>();
    private List<LstUpdatedCatalog> lstUpdatedCatalog = new ArrayList<LstUpdatedCatalog>();
    private List<LstNewCatalog> lstNewCatalog = new ArrayList<LstNewCatalog>();
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The serverDate
     */
    public String getServerDate() {
        return ServerDate;
    }

    /**
     * 
     * @param serverDate
     *     The ServerDate
     */
    public void setServerDate(String serverDate) {
        this.ServerDate = serverDate;
    }

    /**
     * 
     * @return
     *     The lstDeletedCatalog
     */
    public List<LstDeletedCatalog> getLstDeletedCatalog() {
        return lstDeletedCatalog;
    }

    /**
     * 
     * @param lstDeletedCatalog
     *     The lstDeletedCatalog
     */
    public void setLstDeletedCatalog(List<LstDeletedCatalog> lstDeletedCatalog) {
        this.lstDeletedCatalog = lstDeletedCatalog;
    }

    /**
     * 
     * @return
     *     The lstUpdatedCatalog
     */
    public List<LstUpdatedCatalog> getLstUpdatedCatalog() {
        return lstUpdatedCatalog;
    }

    /**
     * 
     * @param lstUpdatedCatalog
     *     The lstUpdatedCatalog
     */
    public void setLstUpdatedCatalog(List<LstUpdatedCatalog> lstUpdatedCatalog) {
        this.lstUpdatedCatalog = lstUpdatedCatalog;
    }

    /**
     * 
     * @return
     *     The lstNewCatalog
     */
    public List<LstNewCatalog> getLstNewCatalog() {
        return lstNewCatalog;
    }

    /**
     * 
     * @param lstNewCatalog
     *     The lstNewCatalog
     */
    public void setLstNewCatalog(List<LstNewCatalog> lstNewCatalog) {
        this.lstNewCatalog = lstNewCatalog;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
