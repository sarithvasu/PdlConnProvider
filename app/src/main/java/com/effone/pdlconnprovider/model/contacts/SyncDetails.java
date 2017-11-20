
package com.effone.pdlconnprovider.model.contacts;

import java.util.HashMap;
import java.util.Map;



public class SyncDetails {

    private String ServerDate;
    private Boolean CanSyncLocation;
    private Boolean CanSyncContact;
    private Boolean CanSyncAbout;
    private Boolean CanSyncPrivacy;
    private Boolean CanSyncFAQ;
    private Boolean CanSyncCity;
    private Boolean CanSyncTestCatalog;
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
     *     The canSyncLocation
     */
    public Boolean getCanSyncLocation() {
        return CanSyncLocation;
    }

    /**
     * 
     * @param canSyncLocation
     *     The CanSyncLocation
     */
    public void setCanSyncLocation(Boolean canSyncLocation) {
        this.CanSyncLocation = canSyncLocation;
    }

    /**
     * 
     * @return
     *     The canSyncContact
     */
    public Boolean getCanSyncContact() {
        return CanSyncContact;
    }

    /**
     * 
     * @param canSyncContact
     *     The CanSyncContact
     */
    public void setCanSyncContact(Boolean canSyncContact) {
        this.CanSyncContact = canSyncContact;
    }

    /**
     * 
     * @return
     *     The canSyncAbout
     */
    public Boolean getCanSyncAbout() {
        return CanSyncAbout;
    }

    /**
     * 
     * @param canSyncAbout
     *     The CanSyncAbout
     */
    public void setCanSyncAbout(Boolean canSyncAbout) {
        this.CanSyncAbout = canSyncAbout;
    }

    /**
     * 
     * @return
     *     The canSyncPrivacy
     */
    public Boolean getCanSyncPrivacy() {
        return CanSyncPrivacy;
    }

    /**
     * 
     * @param canSyncPrivacy
     *     The CanSyncPrivacy
     */
    public void setCanSyncPrivacy(Boolean canSyncPrivacy) {
        this.CanSyncPrivacy = canSyncPrivacy;
    }

    /**
     * 
     * @return
     *     The canSyncFAQ
     */
    public Boolean getCanSyncFAQ() {
        return CanSyncFAQ;
    }

    /**
     * 
     * @param canSyncFAQ
     *     The CanSyncFAQ
     */
    public void setCanSyncFAQ(Boolean canSyncFAQ) {
        this.CanSyncFAQ = canSyncFAQ;
    }

    /**
     * 
     * @return
     *     The canSyncCity
     */
    public Boolean getCanSyncCity() {
        return CanSyncCity;
    }

    /**
     * 
     * @param canSyncCity
     *     The CanSyncCity
     */
    public void setCanSyncCity(Boolean canSyncCity) {
        this.CanSyncCity = canSyncCity;
    }

    /**
     * 
     * @return
     *     The canSyncTestCatalog
     */
    public Boolean getCanSyncTestCatalog() {
        return CanSyncTestCatalog;
    }

    /**
     * 
     * @param canSyncTestCatalog
     *     The CanSyncTestCatalog
     */
    public void setCanSyncTestCatalog(Boolean canSyncTestCatalog) {
        this.CanSyncTestCatalog = canSyncTestCatalog;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
