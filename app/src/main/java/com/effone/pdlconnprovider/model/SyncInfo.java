package com.effone.pdlconnprovider.model;

import java.util.ArrayList;

/**
 * Created by sarith.vasu on 31-05-2016.
 */
public class SyncInfo extends ArrayList<SyncInfo>{
    public SyncInfo() {
    }
    public SyncInfo(String table_name, String last_sync_date) {
        this.table_name = table_name;
        this.last_sync_date = last_sync_date;
    }

    public String table_name;
    public String last_sync_date;
}
