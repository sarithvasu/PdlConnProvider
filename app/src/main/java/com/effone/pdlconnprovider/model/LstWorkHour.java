
package com.effone.pdlconnprovider.model;

import java.util.HashMap;
import java.util.Map;


public class LstWorkHour {

    private String WeekDay;
    private String WorkHour;
    private String AuditDate;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The WeekDay
     */
    public String getWeekDay() {
        return WeekDay;
    }

    /**
     * 
     * @param WeekDay
     *     The WeekDay
     */
    public void setWeekDay(String WeekDay) {
        this.WeekDay = WeekDay;
    }

    /**
     * 
     * @return
     *     The WorkHour
     */
    public String getWorkHour() {
        return WorkHour;
    }

    /**
     * 
     * @param WorkHour
     *     The WorkHour
     */
    public void setWorkHour(String WorkHour) {
        this.WorkHour = WorkHour;
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
