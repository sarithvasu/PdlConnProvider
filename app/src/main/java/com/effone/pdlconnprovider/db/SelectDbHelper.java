package com.effone.pdlconnprovider.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.effone.pdlconnprovider.common.AppContant;
import com.effone.pdlconnprovider.model.DisplayContact;
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.LocationDetail;
import com.effone.pdlconnprovider.model.LocationSummery;
import com.effone.pdlconnprovider.model.SyncInfo;
import com.effone.pdlconnprovider.model.TestCat;
import com.effone.pdlconnprovider.model.contacts.Contact;

import java.util.ArrayList;

/**
 * Created by sarith.vasu on 18-05-2016.
 */
public class SelectDbHelper extends DataBaseHelper {

    public static String DISPLAYCODE = "display_code";
    public static String NAME = "name";
    public static String LOCATION = "location";
    public static String TESTCATALOGID = "test_catalog_id";



    public SelectDbHelper(Context context) {
        super(context);
    }

    public ArrayList<LocationSummery> getLocationSummeryList() {

        ArrayList<LocationSummery> locationSummeries = new ArrayList<LocationSummery>();
        String query = "SELECT      PSC_Location_ID," +
                "            NAME," +
                "            (Address) AS Address1," +
                "            (City||', '||State||', '||Zip) AS Address2 " +
                " FROM  Location";


        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();


        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            LocationSummery locationSummery = new LocationSummery();
                            locationSummery.locationId = cursor.getInt(cursor.getColumnIndex(InsertDbHelper.PSC_LOCATION_ID));
                            locationSummery.locationName = cursor.getString(cursor.getColumnIndex(InsertDbHelper.NAME));
                            locationSummery.locationAdress1 = cursor.getString(cursor.getColumnIndex("Address1"));
                            locationSummery.locationAddress2 = cursor.getString(cursor.getColumnIndex("Address2"));
                            locationSummeries.add(locationSummery);
                        } while (cursor.moveToNext());
                    }

                }
            }
        }
        this.CloseDataBaseConnection(db);
        return locationSummeries;
    }
    public String getInitialDate(){
        String intDate="";
        String query1 = "SELECT last_synced_date FROM  SyncInfo where sync_type='Initial Db'";
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        intDate= cursor.getString(cursor.getColumnIndex(InsertDbHelper.LAST_SYNC_DATE));
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return intDate;
    }
    public String getTestcatSyncDate(){
        String date="";
        String query1 = "SELECT last_synced_date FROM  SyncInfo where sync_type='"+ AppContant.TEST_CATLOG+"'";
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        date= cursor.getString(cursor.getColumnIndex(InsertDbHelper.LAST_SYNC_DATE));
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return date;
    }
    public LocationDetail getLocationDeatails(int locationId) {

        LocationDetail locationDetail=null;
        String query = "SELECT T1.NAME AS NAME," +
                "       T1.ADDRESS AS ADSRESS_1," +
                "       (T1.CITY||', '||T1.STATE||' '||T1.ZIP) AS ADDRESS_2," +
                "       T1.PHONE1 AS PHONE," +
                "       T1.FAX AS FAX," +
                "       T1.LATITUDE AS LATITUDE," +
                "       T1.LONGITUDE AS LONGITUDE," +
                "  (SELECT group_concat((WEEK_DAY||': '||WORK_HOUR),'<br/>') AS WORK_HOUR " +
                "   FROM LOCATIONWORKHOURS " +
                "   WHERE PSC_LOCATION_ID=21) AS WORK_HOUR " +
                "FROM LOCATION T1 " +
                "WHERE T1.PSC_LOCATION_ID="+locationId;


        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();


        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {

                            locationDetail = new LocationDetail();
                            locationDetail.locationName = cursor.getString(cursor.getColumnIndex("NAME"));
                            locationDetail.locationAdress1 = cursor.getString(cursor.getColumnIndex("ADSRESS_1"));
                            locationDetail.locationAddress2 = cursor.getString(cursor.getColumnIndex("ADDRESS_2"));
                            locationDetail.locationPhone = cursor.getString(cursor.getColumnIndex("PHONE"));
                            locationDetail.locationFax = cursor.getString(cursor.getColumnIndex("FAX"));
                            locationDetail.locationWorkHours = cursor.getString(cursor.getColumnIndex("WORK_HOUR"));
                            locationDetail.locationLattitude = cursor.getDouble(cursor.getColumnIndex("LATITUDE"));
                            locationDetail.locationLongitude = cursor.getDouble(cursor.getColumnIndex("LONGITUDE"));


                    }

                }
            }
        }
        this.CloseDataBaseConnection(db);
        return locationDetail;
    }

    public ArrayList<DisplayContact> getContact() {

        ArrayList<DisplayContact> contacts = new ArrayList<DisplayContact>();
        String query1 = "SELECT    *              FROM  Contact";
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            DisplayContact displayContact = new DisplayContact();
                            displayContact.title = cursor.getString(cursor.getColumnIndex(InsertDbHelper.TITLE));
                            displayContact.title_text = cursor.getString(cursor.getColumnIndex(InsertDbHelper.TITLE_TEXT));
                            displayContact.audit_date = cursor.getString(cursor.getColumnIndex(InsertDbHelper.AUDIT_DATE));

                            contacts.add(displayContact);
                        } while (cursor.moveToNext());
                    }

                }
            }
        }
        this.CloseDataBaseConnection(db);
       return contacts;

    }
    public String getAboutUsContent(int refId) {

        String htmlContent="";
        String query1 = "SELECT ('<style type=text/css>p,div,ul > li {color:white}p{-webkit-margin-before:0em;-webkit-margin-after:0em;}</style>' || HTML_DESCRIPTION) AS HTML_DESCRIPTION FROM  Content where content_reference_id="+refId;
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                            htmlContent= cursor.getString(cursor.getColumnIndex("HTML_DESCRIPTION"));
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return htmlContent;

    }
    public ArrayList<Faq> getFaq() {

        ArrayList<Faq> faqs=new ArrayList<Faq>();
        String query1 = "SELECT * FROM  FAQ";
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            Faq faq = new Faq();
                            faq.setQuestion(cursor.getString(cursor.getColumnIndex(InsertDbHelper.FAQ_QUESTION)));
                            faq.setAnswer(cursor.getString(cursor.getColumnIndex(InsertDbHelper.FAQ_ANSWER)));
                            faqs.add(faq);
                        } while (cursor.moveToNext());
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return faqs;

    }
    public ArrayList<TestCat> getTestCatList() {

        ArrayList<TestCat> testCats = new ArrayList<TestCat>();
        String query = "SELECT test_catalog_id,CASE WHEN display_code IS NULL THEN CODE WHEN length(display_code)=0 THEN code ELSE display_code END As display_code," +
                "name,location from  TestCatalog order by display_code ASC";


        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();


        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            TestCat testCat = new TestCat();
                            testCat.testCatalogID = cursor.getInt(cursor.getColumnIndex(TESTCATALOGID));
                            testCat.displayCode = cursor.getString(cursor.getColumnIndex(DISPLAYCODE));
                            testCat.name = cursor.getString(cursor.getColumnIndex(NAME));
                            testCat.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                            testCats.add(testCat);
                        } while (cursor.moveToNext());
                    }

                }
            }
        }
        this.CloseDataBaseConnection(db);
        return testCats;
    }

    public ArrayList<TestCat> getSearchTestCatList(String searchText) {

        ArrayList<TestCat> testCats = new ArrayList<TestCat>();
        String query = " SELECT test_catalog_id,CASE WHEN display_code IS NULL THEN CODE WHEN length(display_code)=0 THEN code ELSE display_code END As display_code,name,location FROM TESTCATALOG WHERE" +
                " display_code LIKE ('%'||'"+searchText+"'||'%') OR code LIKE ('%'||'"+searchText+"'||'%') OR "+
                " name LIKE ('%'||'"+searchText+"'||'%') OR" +
                " location LIKE ('%'||'"+searchText+"'||'%') " +
                " ORDER BY display_code ASC;";


        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();


        if (db != null) {
            cursor = db.rawQuery(query, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            TestCat testCat = new TestCat();
                            testCat.testCatalogID = cursor.getInt(cursor.getColumnIndex(TESTCATALOGID));
                            testCat.displayCode = cursor.getString(cursor.getColumnIndex(DISPLAYCODE));
                            testCat.name = cursor.getString(cursor.getColumnIndex(NAME));
                            testCat.location = cursor.getString(cursor.getColumnIndex(LOCATION));
                            testCats.add(testCat);
                        } while (cursor.moveToNext());
                    }

                }
            }
        }
        this.CloseDataBaseConnection(db);
        return testCats;
    }
    public SyncInfo getSyncINfo() {

        SyncInfo syncInfos=new SyncInfo();
        String query1 = "SELECT * FROM  SyncInfo";
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        do {
                            SyncInfo syncInfo = new SyncInfo(cursor.getString(cursor.getColumnIndex(InsertDbHelper.TABLE_NAME)),cursor.getString(cursor.getColumnIndex(InsertDbHelper.LAST_SYNC_DATE)));
                            syncInfos.add(syncInfo);
                        } while (cursor.moveToNext());
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return syncInfos;

    }

    public String getTestCatDetails(int catId) {

        String testCatDetails="";
       String query1 = "SELECT ('<style type=text/css>body {}strong, h4 > b, h3 > b, h4 > table > tbody > tr > th, h4 > p, h4 > img, h4, h4 > p > b {font-weight: normal !important;} table { color: white !important; } table > tbody > tr > td { text-align: center; } h3, h4, h3 > p, h4 > p, table > tbody > tr > td > p { margin: 5px 0px 0px 0px;color:#8eb9d7; }a{color:#fff;}table > tbody > tr > td,table > tr > td{color:#8eb9d7}</style>'|| REPLACE(GROUP_CONCAT (('<h3 style=color:#fff><b>'||name||'</b></h3>')||('<h4 style=color:#8eb9d7 !important>'|| value||'</h4><hr/>')),',','')) AS name FROM TESTCATALOGDETAILS WHERE test_catalog_id='"+catId+"'";
        //   String query1="SELECT ('<style type=text/css>strong,h4 > b,h3 > b,h4 > table > tbody > tr > th,h4 > p,h4 > img,h4,h4 > p > b{font-weight:normal !important;}table{color:white !important}h3,h4{-webkit-margin-before:0.2em;-webkit-margin-after:0.2em;}</style>'|| GROUP_CONCAT (('<h3 style=color:white><b>'||name||'</b></h3>')||('<h4 style=color:#ABCAE3 !important>'|| value||'</h4><hr/>'))) AS name FROM TESTCATALOGDETAILS WHERE test_catalog_id='"+catId+"'";
        Cursor cursor = null;

        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        testCatDetails= cursor.getString(cursor.getColumnIndex("name"));
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return testCatDetails;

    }
    public String getAboutUsContentForMail(int catId) {

        String htmlContent="";
        String query1 = "SELECT ( HTML_DESCRIPTION) AS HTML_DESCRIPTION FROM  Content where content_reference_id="+catId;
        Cursor cursor = null;
        SQLiteDatabase db = this.OpenDataBase();
        if (db != null) {
            cursor = db.rawQuery(query1, null);
            if (cursor != null) {
                if (cursor.getCount() >= 1) {
                    if (cursor.moveToFirst()) {
                        htmlContent= cursor.getString(cursor.getColumnIndex("HTML_DESCRIPTION"));
                    }
                }
            }
        }
        this.CloseDataBaseConnection(db);
        return htmlContent;

    }
}
