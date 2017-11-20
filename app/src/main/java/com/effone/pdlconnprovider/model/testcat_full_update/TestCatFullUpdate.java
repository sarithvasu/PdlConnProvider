package com.effone.pdlconnprovider.model.testcat_full_update;

/**
 * Created by sarith.vasu on 14-06-2016.
 */
public class TestCatFullUpdate {

    private LstUpdatedCatalog[] lstUpdatedCatalog;

    private String ServerDate;

    private LstNewCatalog[] lstNewCatalog;

    private LstDeletedCatalog[] lstDeletedCatalog;

    public LstUpdatedCatalog[] getLstUpdatedCatalog() {
        return lstUpdatedCatalog;
    }

    public void setLstUpdatedCatalog(LstUpdatedCatalog[] lstUpdatedCatalog) {
        this.lstUpdatedCatalog = lstUpdatedCatalog;
    }

    public String getServerDate() {
        return ServerDate;
    }

    public void setServerDate(String ServerDate) {
        this.ServerDate = ServerDate;
    }

    public LstNewCatalog[] getLstNewCatalog() {
        return lstNewCatalog;
    }

    public void setLstNewCatalog(LstNewCatalog[] lstNewCatalog) {
        this.lstNewCatalog = lstNewCatalog;
    }

    public LstDeletedCatalog[] getLstDeletedCatalog() {
        return lstDeletedCatalog;
    }

    public void setLstDeletedCatalog(LstDeletedCatalog[] lstDeletedCatalog) {
        this.lstDeletedCatalog = lstDeletedCatalog;
    }
}
