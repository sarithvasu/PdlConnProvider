
package com.effone.pdlconnprovider.model.about_us;

import java.util.HashMap;
import java.util.Map;



public class About {

    private Integer ContentID;
    private String HtmlContent;
    private String AuditDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The ContentID
     */
    public Integer getContentID() {
        return ContentID;
    }

    /**
     * 
     * @param ContentID
     *     The ContentID
     */
    public void setContentID(Integer ContentID) {
        this.ContentID = ContentID;
    }

    /**
     * 
     * @return
     *     The HtmlContent
     */
    public String getHtmlContent() {
        return HtmlContent;
    }

    /**
     * 
     * @param HtmlContent
     *     The HtmlContent
     */
    public void setHtmlContent(String HtmlContent) {
        this.HtmlContent = HtmlContent;
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
