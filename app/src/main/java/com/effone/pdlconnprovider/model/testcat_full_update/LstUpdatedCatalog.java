package com.effone.pdlconnprovider.model.testcat_full_update;

/**
 * Created by sarith.vasu on 14-06-2016.
 */
public class LstUpdatedCatalog {
    private String Name;

    private String TestCatalogID;

    private String status;

    private LstTestCatalogDetails[] lstTestCatalogDetails;

    private String DisplayCode;

    private String CreatedDate;

    private String Location;

    private String Code;

    private String URI;

    public String getName ()
    {
        return Name;
    }

    public void setName (String Name)
    {
        this.Name = Name;
    }

    public String getTestCatalogID ()
    {
        return TestCatalogID;
    }

    public void setTestCatalogID (String TestCatalogID)
    {
        this.TestCatalogID = TestCatalogID;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public LstTestCatalogDetails[] getLstTestCatalogDetails ()
    {
        return lstTestCatalogDetails;
    }

    public void setLstTestCatalogDetails (LstTestCatalogDetails[] lstTestCatalogDetails)
    {
        this.lstTestCatalogDetails = lstTestCatalogDetails;
    }

    public String getDisplayCode ()
    {
        return DisplayCode;
    }

    public void setDisplayCode (String DisplayCode)
    {
        this.DisplayCode = DisplayCode;
    }

    public String getCreatedDate ()
    {
        return CreatedDate;
    }

    public void setCreatedDate (String CreatedDate)
    {
        this.CreatedDate = CreatedDate;
    }

    public String getLocation ()
    {
        return Location;
    }

    public void setLocation (String Location)
    {
        this.Location = Location;
    }

    public String getCode ()
    {
        return Code;
    }

    public void setCode (String Code)
    {
        this.Code = Code;
    }

    public String getURI ()
    {
        return URI;
    }

    public void setURI (String URI)
    {
        this.URI = URI;
    }

}
