package com.effone.pdlconnprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by sarith.vasu on 31-05-2016.
 */
public class DeleteDbHelper extends DataBaseHelper {
    public DeleteDbHelper(Context context) {
        super(context);
    }

    public void deleteAllFromContacts(){
        SQLiteDatabase db = this.OpenDataBase();
        db.delete(InsertDbHelper.TABLE_CONTACT,null,null);
        this.CloseDataBaseConnection(db);
    }
    public void deleteAllFromFAQ(){
        SQLiteDatabase db = this.OpenDataBase();
        db.delete(InsertDbHelper.TABLE_FAQ,null,null);
        this.CloseDataBaseConnection(db);
    }
    public void deleteAllFromLocation(){
        SQLiteDatabase db = this.OpenDataBase();
        db.delete(InsertDbHelper.TABLE_LOCATION,null,null);
        db.delete(InsertDbHelper.TABLE_LOCATION_WORK_HOURS,null,null);
        this.CloseDataBaseConnection(db);
    }
    public void deleteAllFromCONTENT(int id){
        SQLiteDatabase db = this.OpenDataBase();
        db.delete(InsertDbHelper.TABLE_CONTENT,InsertDbHelper.CONTENT_REFERENCE_ID+"="+id,null);
        this.CloseDataBaseConnection(db);
    }
}
