
package com.effone.pdlconnprovider.model.contacts;

import java.util.HashMap;
import java.util.Map;

public class LstFax {

    private Integer ContactId;
    private String Name;
    private String Value;
    private Integer SequenceOrder;
    private String AuditDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ContactId
     */
    public Integer getContactId() {
        return ContactId;
    }

    /**
     * 
     * @param ContactId
     *     The ContactId
     */
    public void setContactId(Integer ContactId) {
        this.ContactId = ContactId;
    }

    /**
     * 
     * @return
     *     The Name
     */
    public String getName() {
        return Name;
    }

    /**
     * 
     * @param Name
     *     The Name
     */
    public void setName(String Name) {
        this.Name = Name;
    }

    /**
     * 
     * @return
     *     The Value
     */
    public String getValue() {
        return Value;
    }

    /**
     * 
     * @param Value
     *     The Value
     */
    public void setValue(String Value) {
        this.Value = Value;
    }

    /**
     * 
     * @return
     *     The SequenceOrder
     */
    public Integer getSequenceOrder() {
        return SequenceOrder;
    }

    /**
     * 
     * @param SequenceOrder
     *     The SequenceOrder
     */
    public void setSequenceOrder(Integer SequenceOrder) {
        this.SequenceOrder = SequenceOrder;
    }

    /**
     * 
     * @return
     *     The AuditDate
     */
    public String getAuditDate() {
        return AuditDate;
    }

    /**
     * 
     * @param AuditDate
     *     The AuditDate
     */
    public void setAuditDate(String AuditDate) {
        this.AuditDate = AuditDate;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
