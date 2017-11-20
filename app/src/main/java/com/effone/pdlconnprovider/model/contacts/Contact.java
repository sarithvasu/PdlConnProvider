
package com.effone.pdlconnprovider.model.contacts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Contact {

    private List<LstPhone> lstPhone = new ArrayList<LstPhone>();
    private List<LstFax> lstFax = new ArrayList<LstFax>();
    private List<LstEmail> lstEmail = new ArrayList<LstEmail>();
    private List<LstAddress> lstAddress = new ArrayList<LstAddress>();
    private Object lstDeletedContactId;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The lstPhone
     */
    public List<LstPhone> getLstPhone() {
        return lstPhone;
    }

    /**
     * 
     * @param lstPhone
     *     The lstPhone
     */
    public void setLstPhone(List<LstPhone> lstPhone) {
        this.lstPhone = lstPhone;
    }

    /**
     * 
     * @return
     *     The lstFax
     */
    public List<LstFax> getLstFax() {
        return lstFax;
    }

    /**
     * 
     * @param lstFax
     *     The lstFax
     */
    public void setLstFax(List<LstFax> lstFax) {
        this.lstFax = lstFax;
    }

    /**
     * 
     * @return
     *     The lstEmail
     */
    public List<LstEmail> getLstEmail() {
        return lstEmail;
    }

    /**
     * 
     * @param lstEmail
     *     The lstEmail
     */
    public void setLstEmail(List<LstEmail> lstEmail) {
        this.lstEmail = lstEmail;
    }

    /**
     * 
     * @return
     *     The lstAddress
     */
    public List<LstAddress> getLstAddress() {
        return lstAddress;
    }

    /**
     * 
     * @param lstAddress
     *     The lstAddress
     */
    public void setLstAddress(List<LstAddress> lstAddress) {
        this.lstAddress = lstAddress;
    }

    /**
     * 
     * @return
     *     The lstDeletedContactId
     */
    public Object getLstDeletedContactId() {
        return lstDeletedContactId;
    }

    /**
     * 
     * @param lstDeletedContactId
     *     The lstDeletedContactId
     */
    public void setLstDeletedContactId(Object lstDeletedContactId) {
        this.lstDeletedContactId = lstDeletedContactId;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
