
package com.effone.pdlconnprovider.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public class Location {


    private Integer LocationID;

    private String Name;

    private String Address;

    private String City;

    private String State;

    private String Zip;

    private String Phone1;

    private String Phone2;

    private String Fax;

    private Integer AppointmentDuration;

    private Integer ReservationPerTimeslot;

    private Double Latitude;

    private Double Longitude;

    private List<LstWorkHour> lstWorkHours = new ArrayList<LstWorkHour>();

    private String AuditDate;

    private Boolean IsActive;

    private List<Service> Services = new ArrayList<Service>();

    private Boolean IsAcceptingAppointments;

    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    /**
     * 
     * @return
     *     The LocationID
     */

    public Integer getLocationID() {
        return LocationID;
    }

    /**
     * 
     * @param LocationID
     *     The LocationID
     */

    public void setLocationID(Integer LocationID) {
        this.LocationID = LocationID;
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
     *     The Address
     */

    public String getAddress() {
        return Address;
    }

    /**
     * 
     * @param Address
     *     The Address
     */

    public void setAddress(String Address) {
        this.Address = Address;
    }

    /**
     * 
     * @return
     *     The City
     */

    public String getCity() {
        return City;
    }

    /**
     * 
     * @param City
     *     The City
     */

    public void setCity(String City) {
        this.City = City;
    }

    /**
     * 
     * @return
     *     The State
     */

    public String getState() {
        return State;
    }

    /**
     * 
     * @param State
     *     The State
     */

    public void setState(String State) {
        this.State = State;
    }

    /**
     * 
     * @return
     *     The Zip
     */

    public String getZip() {
        return Zip;
    }

    /**
     * 
     * @param Zip
     *     The Zip
     */

    public void setZip(String Zip) {
        this.Zip = Zip;
    }

    /**
     * 
     * @return
     *     The Phone1
     */

    public String getPhone1() {
        return Phone1;
    }

    /**
     * 
     * @param Phone1
     *     The Phone1
     */

    public void setPhone1(String Phone1) {
        this.Phone1 = Phone1;
    }

    /**
     * 
     * @return
     *     The Phone2
     */

    public String getPhone2() {
        return Phone2;
    }

    /**
     * 
     * @param Phone2
     *     The Phone2
     */

    public void setPhone2(String Phone2) {
        this.Phone2 = Phone2;
    }

    /**
     * 
     * @return
     *     The Fax
     */

    public String getFax() {
        return Fax;
    }

    /**
     * 
     * @param Fax
     *     The Fax
     */

    public void setFax(String Fax) {
        this.Fax = Fax;
    }

    /**
     * 
     * @return
     *     The AppointmentDuration
     */

    public Integer getAppointmentDuration() {
        return AppointmentDuration;
    }

    /**
     * 
     * @param AppointmentDuration
     *     The AppointmentDuration
     */

    public void setAppointmentDuration(Integer AppointmentDuration) {
        this.AppointmentDuration = AppointmentDuration;
    }

    /**
     * 
     * @return
     *     The ReservationPerTimeslot
     */

    public Integer getReservationPerTimeslot() {
        return ReservationPerTimeslot;
    }

    /**
     * 
     * @param ReservationPerTimeslot
     *     The ReservationPerTimeslot
     */

    public void setReservationPerTimeslot(Integer ReservationPerTimeslot) {
        this.ReservationPerTimeslot = ReservationPerTimeslot;
    }

    /**
     * 
     * @return
     *     The Latitude
     */

    public Double getLatitude() {
        return Latitude;
    }

    /**
     * 
     * @param Latitude
     *     The Latitude
     */

    public void setLatitude(Double Latitude) {
        this.Latitude = Latitude;
    }

    /**
     * 
     * @return
     *     The Longitude
     */

    public Double getLongitude() {
        return Longitude;
    }

    /**
     * 
     * @param Longitude
     *     The Longitude
     */

    public void setLongitude(Double Longitude) {
        this.Longitude = Longitude;
    }

    /**
     * 
     * @return
     *     The lstWorkHours
     */

    public List<LstWorkHour> getLstWorkHours() {
        return lstWorkHours;
    }

    /**
     * 
     * @param lstWorkHours
     *     The lstWorkHours
     */

    public void setLstWorkHours(List<LstWorkHour> lstWorkHours) {
        this.lstWorkHours = lstWorkHours;
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

    /**
     * 
     * @return
     *     The IsActive
     */

    public Boolean getIsActive() {
        return IsActive;
    }

    /**
     * 
     * @param IsActive
     *     The IsActive
     */

    public void setIsActive(Boolean IsActive) {
        this.IsActive = IsActive;
    }

    /**
     * 
     * @return
     *     The Services
     */

    public List<Service> getServices() {
        return Services;
    }

    /**
     * 
     * @param Services
     *     The Services
     */

    public void setServices(List<Service> Services) {
        this.Services = Services;
    }

    /**
     * 
     * @return
     *     The IsAcceptingAppointments
     */

    public Boolean getIsAcceptingAppointments() {
        return IsAcceptingAppointments;
    }

    /**
     * 
     * @param IsAcceptingAppointments
     *     The IsAcceptingAppointments
     */

    public void setIsAcceptingAppointments(Boolean IsAcceptingAppointments) {
        this.IsAcceptingAppointments = IsAcceptingAppointments;
    }


    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }


    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}
