package com.effone.pdlconnprovider.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;
import android.util.TimingLogger;

import com.effone.pdlconnprovider.common.AppContant;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.common.Settings;
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;

import com.effone.pdlconnprovider.model.LstWorkHour;
import com.effone.pdlconnprovider.model.SyncInfo;
import com.effone.pdlconnprovider.model.TestCatUpdate;
import com.effone.pdlconnprovider.model.about_us.About;
import com.effone.pdlconnprovider.model.contacts.Contact;
import com.effone.pdlconnprovider.model.contacts.LstAddress;
import com.effone.pdlconnprovider.model.contacts.LstEmail;
import com.effone.pdlconnprovider.model.contacts.LstFax;
import com.effone.pdlconnprovider.model.contacts.LstPhone;
import com.effone.pdlconnprovider.model.testcat_full_update.LstDeletedCatalog;
import com.effone.pdlconnprovider.model.testcat_full_update.LstNewCatalog;
import com.effone.pdlconnprovider.model.testcat_full_update.LstTestCatalogDetails;
import com.effone.pdlconnprovider.model.testcat_full_update.LstUpdatedCatalog;
import com.effone.pdlconnprovider.model.testcat_full_update.TestCatFullUpdate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sarith.vasu on 17-05-2016.
 */
public class InsertDbHelper extends DataBaseHelper {

    /*  tables*/
    public static String TABLE_LOCATION = "Location";
    public static String TABLE_LOCATION_WORK_HOURS = "locationworkhours";
    public static String TABLE_CONTACT = "Contact";
    public static String TABLE_CONTENT = "Content";
    public static String TABLE_FAQ = "FAQ";
    public static String TABLE_SYNC_INFO = "SyncInfo";
    public static String TABLE_TEST_CATALOG = "TestCatalog";
    public static String TABLE_TEST_CATALOG_DETAILS = "TestcatalogDetails";

    private static final String TAG = "Timer";

    /* column values sync info */

    public static String TABLE_NAME = "sync_type";
    public static String LAST_SYNC_DATE = "last_synced_date";



    /* column varables location  */

    public static String PSC_LOCATION_ID = "psc_location_id";
    public static String NAME = "name";
    public static String ADDRESS = "address";
    public static String CITY = "city";
    public static String STATE = "state";
    public static String ZIP = "zip";
    public static String PHONE1 = "phone1";
    public static String PHONE2 = "phone2";
    public static String FAX = "fax";
    public static String APPOINTMENT_DURATION = "appointment_duration";
    public static String RESERVATION_PER_TIME_SLOT = "reservation_per_time_slot";
    public static String LATITUDE = "latitude";
    public static String LONGITUDE = "longitude";
    public static String AUDIT_DATE = "audit_date";
    public static String IS_ACTIVE = "is_active";


    /* column varables LocationWorkHours  */

    public static String LOCATION_WORK_HOUR_ID = "location_work_hour_id";
    public static String WEEK_DAY = "week_day";
    public static String WORK_HOUR = "work_hour";


    /*column variable Contact */

    public static String CONTACT_ID = "contact_id";
    public static String CONTACT_REFERENCE_ID = "contact_reference_id";
    public static String TITLE = "title";
    public static String TITLE_TEXT = "title_text";
    public static String SEQUENCE_ORDER = "sequence_order";

    /*aboutus variable */
    public static String CONTENT_ID = "content_id";
    public static String HTML_DESCRIPTION = "html_description";
    public static String CONTENT_REFERENCE_ID = "content_reference_id";

    /*faq column variables*/

    public static String FAQ_QUESTION = "faq_question";
    public static String FAQ_ANSWER = "faq_answer";


   /* *//*testcat table colomn name *//*

    public static String TESTCATALOGID = "TestCatalogID";
    public static String DISPLAYCODE = "DisplayCode";
    public static String URI = "URI";
    public static String CODE = "Code";
    public static String LOCATION = "Location";
    public static String CREATEDDATE = "CreatedDate";
    public static String CLIENTIP = "ClientIP";
    public static String STATUS = "Status";
*/
       /*testcat table colomn name */

    public static String TESTCATALOGID = "test_catalog_id";
    public static String DISPLAYCODE = "display_code";
    public static String URI = "URI";
    public static String CODE = "code";
    public static String LOCATION = "location";
    public static String CREATEDDATE = "CreatedDate";
    public static String CLIENTIP = "ClientIP";
    public static String STATUS = "status";

    /*test cat details column names*/
    public static String TEST_CATALOG_DETAILS_ID = "test_catalog_details_id";
    public static String VALUE = "value";


    public InsertDbHelper(Context context) {
        super(context);
    }


    public void insertInToLocationTable(Location[] locations) {


        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.OpenDataBase();
        for (Location location : locations) {
            contentValues.put(PSC_LOCATION_ID, location.getLocationID());
            contentValues.put(NAME, location.getName());
            contentValues.put(ADDRESS, location.getAddress());
            contentValues.put(CITY, location.getCity());
            contentValues.put(STATE, location.getState());
            contentValues.put(ZIP, location.getZip());
            contentValues.put(PHONE1, location.getPhone1());
            contentValues.put(PHONE2, location.getPhone2());
            contentValues.put(FAX, location.getFax());
            contentValues.put(APPOINTMENT_DURATION, location.getAppointmentDuration());
            contentValues.put(RESERVATION_PER_TIME_SLOT, location.getReservationPerTimeslot());
            contentValues.put(LATITUDE, location.getLatitude());
            contentValues.put(LONGITUDE, location.getLongitude());
            contentValues.put(AUDIT_DATE, location.getAuditDate());
            contentValues.put(IS_ACTIVE, location.getIsActive());
            insertIntoLocationWorkHours(location.getLocationID(), location.getLstWorkHours(), db);
            db.insert(TABLE_LOCATION, null, contentValues);

        }
        if (Settings.LOCATION) {
            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.LOCATION);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.LOCATION + "'", null);
            Settings.LOCATION = false;
        }

        this.CloseDataBaseConnection(db);
    }

    private void insertIntoLocationWorkHours(int location_id, List<LstWorkHour> lstWorkHours, SQLiteDatabase db) {

        ContentValues contentValues = new ContentValues();

        for (LstWorkHour lstWorkHour : lstWorkHours) {
            contentValues.put(PSC_LOCATION_ID, location_id);
            contentValues.put(WEEK_DAY, lstWorkHour.getWeekDay());
            contentValues.put(WORK_HOUR, lstWorkHour.getWorkHour());
            contentValues.put(AUDIT_DATE, lstWorkHour.getAuditDate());
            db.insert(TABLE_LOCATION_WORK_HOURS, null, contentValues);
        }

    }

    public void insertAllDataIntoSyncInfoTable(ArrayList<SyncInfo> syncInfos) {
        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = this.OpenDataBase();
        for (SyncInfo syncInfo : syncInfos) {
            contentValues.put(TABLE_NAME, syncInfo.table_name);
            contentValues.put(LAST_SYNC_DATE, syncInfo.last_sync_date);
            db.insert(TABLE_SYNC_INFO, null, contentValues);
        }
        this.CloseDataBaseConnection(db);
    }

    public void insertInToContactTable(Contact[] contacts) {


        SQLiteDatabase db = this.OpenDataBase();

        ArrayList<LstPhone> lstPhones = (ArrayList<LstPhone>) contacts[0].getLstPhone();
        ArrayList<LstAddress> lstAddresses = (ArrayList<LstAddress>) contacts[0].getLstAddress();
        ArrayList<LstEmail> lstEmails = (ArrayList<LstEmail>) contacts[0].getLstEmail();
        ArrayList<LstFax> lstFaxs = (ArrayList<LstFax>) contacts[0].getLstFax();

        for (LstPhone lstPhone : lstPhones) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_ID, lstPhone.getContactId());
            contentValues.put(CONTACT_REFERENCE_ID, 4);
            contentValues.put(TITLE, lstPhone.getName());
            contentValues.put(TITLE_TEXT, lstPhone.getValue());
            contentValues.put(SEQUENCE_ORDER, lstPhone.getSequenceOrder());
            contentValues.put(AUDIT_DATE, lstPhone.getAuditDate());
            db.insert(TABLE_CONTACT, null, contentValues);
        }

        for (LstFax lstFax : lstFaxs) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_ID, lstFax.getContactId());
            contentValues.put(CONTACT_REFERENCE_ID, 5);
            contentValues.put(TITLE, lstFax.getName());
            contentValues.put(TITLE_TEXT, lstFax.getValue());
            contentValues.put(SEQUENCE_ORDER, lstFax.getSequenceOrder());
            contentValues.put(AUDIT_DATE, lstFax.getAuditDate());
            db.insert(TABLE_CONTACT, null, contentValues);
        }
        for (LstEmail LstEmail : lstEmails) {

            ContentValues contentValues = new ContentValues();
            contentValues.put(CONTACT_ID, LstEmail.getContactId());
            contentValues.put(CONTACT_REFERENCE_ID, 6);
            contentValues.put(TITLE, LstEmail.getName());
            contentValues.put(TITLE_TEXT, LstEmail.getValue());
            contentValues.put(SEQUENCE_ORDER, LstEmail.getSequenceOrder());
            contentValues.put(AUDIT_DATE, LstEmail.getAuditDate());
            db.insert(TABLE_CONTACT, null, contentValues);
        }
        for (LstAddress lstAddress : lstAddresses) {
            ContentValues contentValues = new ContentValues();

            contentValues.put(CONTACT_ID, lstAddress.getContactId());
            contentValues.put(CONTACT_REFERENCE_ID, 7);
            contentValues.put(TITLE, lstAddress.getName());
            contentValues.put(TITLE_TEXT, lstAddress.getValue());
            contentValues.put(SEQUENCE_ORDER, lstAddress.getSequenceOrder());
            contentValues.put(AUDIT_DATE, lstAddress.getAuditDate());
            db.insert(TABLE_CONTACT, null, contentValues);
        }
        if (Settings.CONTACT) {
            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.CONTACT);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.CONTACT + "'", null);
            Settings.CONTACT = false;
        }


        this.CloseDataBaseConnection(db);
    }

    public void insertIntoAboutUs(About[] abouts, int refId) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = super.OpenDataBase();

        contentValues.put(CONTENT_ID, abouts[0].getContentID());
        contentValues.put(CONTENT_REFERENCE_ID, refId);
        contentValues.put(HTML_DESCRIPTION, abouts[0].getHtmlContent());
        contentValues.put(AUDIT_DATE, abouts[0].getAuditDate());
        db.insert(TABLE_CONTENT, null, contentValues);
        if (Settings.ABOUT && refId == 8) {

            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.ABOUT);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.ABOUT + "'", null);
            Settings.ABOUT = false;
        }
        if (Settings.PRIVACY && refId == 9) {
            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.PRIVACY);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.PRIVACY + "'", null);
            Settings.PRIVACY = false;
        }
        this.CloseDataBaseConnection(db);

    }

    public void insertIntoFaq(Faq[] faqs) {

        ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = super.OpenDataBase();
        for (Faq faq : faqs) {
            contentValues.put(FAQ_QUESTION, faq.getQuestion());
            contentValues.put(FAQ_ANSWER, faq.getAnswer());

            db.insert(TABLE_FAQ, null, contentValues);
        }
        if (Settings.FAQ) {
            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.FAQ);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.FAQ + "'", null);
            Settings.FAQ = false;
        }
        this.CloseDataBaseConnection(db);

    }
    /* insert update delete test cat log records*/

    public void updateTestCatalog(TestCatFullUpdate testCatelogFullUpdate) {
        SQLiteDatabase db = this.OpenDataBase();
        long startTimeINSERT = System.currentTimeMillis();
        insertTestCatlog(testCatelogFullUpdate.getLstNewCatalog(), db);
        long endTimeINSERT = System.currentTimeMillis();
        long durationInsert=(endTimeINSERT-startTimeINSERT);
        Log.e("TIME_INSERT",""+durationInsert);
        long startTimeUPDATE = System.currentTimeMillis();
        updateTestCatlog(testCatelogFullUpdate.getLstUpdatedCatalog(), db);
        long endTimeUPDATE = System.currentTimeMillis();
        long durationUPDATE=(endTimeUPDATE-startTimeUPDATE);
        Log.e("TIME_UPDATE",""+durationUPDATE);
        long startTimeDEL = System.currentTimeMillis();
        deleteTestCatlog(testCatelogFullUpdate.getLstDeletedCatalog(), db);
        long endTimeDEL = System.currentTimeMillis();
        long durationDEL=(endTimeDEL-startTimeDEL);
        Log.e("TIME_DEL",""+durationDEL);
        this.CloseDataBaseConnection(db);

    }

    private void deleteTestCatlog(LstDeletedCatalog[] value, SQLiteDatabase db) {

        if(value.length>0) {

            StringBuilder td = new StringBuilder("delete from TestcatalogDetails where test_catalog_id in (");
            StringBuilder tc = new StringBuilder("delete from TestCatalog where test_catalog_id in (");

            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (LstDeletedCatalog n : value) {
                if (sb.length() > 0) sb.append(',');
                sb.append(n.getTestCatalogID());
                if (i++ == value.length) sb.append(")");
            }

            String queryTestCat = tc.append(sb.toString()).toString();
            String queryTestDetails = td.append(sb.toString()).toString();
            ;
            db.execSQL(queryTestDetails);
            db.execSQL(queryTestCat);
        }
     /*   for (LstDeletedCatalog deletedCatalog : value) {
            db.delete(InsertDbHelper.TABLE_TEST_CATALOG_DETAILS, InsertDbHelper.TESTCATALOGID + "=" + deletedCatalog.getTestCatalogID(), null);
            db.delete(InsertDbHelper.TABLE_TEST_CATALOG, InsertDbHelper.TESTCATALOGID + "=" + deletedCatalog.getTestCatalogID(), null);

        }*/
        if (Settings.TEST_CATLOG) {
            String now = Settings.SERVER_DATE;
            ContentValues values = new ContentValues();
            values.put(TABLE_NAME, AppContant.TEST_CATLOG);
            values.put(LAST_SYNC_DATE, now);
            db.update(TABLE_SYNC_INFO, values, TABLE_NAME + "='" + AppContant.TEST_CATLOG + "'", null);
            Settings.TEST_CATLOG = false;
        }

    }

    private void updateTestCatlog(LstUpdatedCatalog[] value, SQLiteDatabase db) {

        if(value.length>0) {
            StringBuilder td = new StringBuilder("delete from TestcatalogDetails where test_catalog_id in (");
            StringBuilder tc = new StringBuilder("delete from TestCatalog where test_catalog_id in (");

            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (LstUpdatedCatalog n : value) {
                if (sb.length() > 0) sb.append(',');
                sb.append(n.getTestCatalogID());
                if (i++ == value.length) sb.append(")");
            }

            String queryTestCat = tc.append(sb.toString()).toString();
            String queryTestDetails = td.append(sb.toString()).toString();
            ;
            db.execSQL(queryTestDetails);
            db.execSQL(queryTestCat);

            try {
                String sql = "insert into TestCatalog (test_catalog_id, display_code, code, name,location,status) values (?, ?, ?, ?,?,?);";
                db.beginTransaction();
                SQLiteStatement stmt = db.compileStatement(sql);

                for (LstUpdatedCatalog newCatalog : value) {

                    //generate some values

                    stmt.bindString(1, newCatalog.getTestCatalogID());
                    stmt.bindString(2, newCatalog.getDisplayCode());
                    stmt.bindString(3, newCatalog.getCode());
                    stmt.bindString(4, newCatalog.getName());
                    stmt.bindString(5, newCatalog.getLocation());
                    stmt.bindString(6, newCatalog.getStatus());
                    stmt.executeInsert();
                    stmt.clearBindings();
                    if (newCatalog.getLstTestCatalogDetails() != null && newCatalog.getLstTestCatalogDetails().length > 0) {
                        insertIntoTestCatDetails(newCatalog.getLstTestCatalogDetails(), db);
                    }


                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.w("XML:", e);
            } finally {
                db.endTransaction();
            }
        }
       /* ContentValues contentValues = new ContentValues();


        for (LstUpdatedCatalog updatedCatalog : value) {
            contentValues.put(DISPLAYCODE, updatedCatalog.getDisplayCode());
            contentValues.put(CODE, updatedCatalog.getCode());
            contentValues.put(NAME, updatedCatalog.getName());
            contentValues.put(LOCATION, updatedCatalog.getLocation());
            contentValues.put(STATUS, updatedCatalog.getStatus());
            updateTestCatalogDetails(updatedCatalog.getLstTestCatalogDetails(), db);
            db.update(TABLE_TEST_CATALOG, contentValues, TESTCATALOGID + "=" + updatedCatalog.getTestCatalogID(), null);

        }
*/

     /*   try {
            String sql = "update TestCatalog SET  display_code=?, code=?, name=?,location=?,status=? WHERE test_catalog_id=?";
            ContentValues contentValues = new ContentValues();
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);

            for (LstUpdatedCatalog updatedCatalog : value) {

                stmt.bindString(1, updatedCatalog.getDisplayCode());
                stmt.bindString(2, updatedCatalog.getCode());
                stmt.bindString(3, updatedCatalog.getName());
                stmt.bindString(4, updatedCatalog.getLocation());
                stmt.bindString(5, updatedCatalog.getStatus());
                stmt.bindString(6, updatedCatalog.getTestCatalogID());
                stmt.executeUpdateDelete();
                stmt.clearBindings();
                updateTestCatalogDetails(updatedCatalog.getLstTestCatalogDetails(), db);

            }
            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("XML:", e);
        } finally {
            db.endTransaction();
        }*/
    }

    private void updateTestCatalogDetails(LstTestCatalogDetails[] lstTestCatalogDetails, SQLiteDatabase db) {

     /*  ContentValues contentValues = new ContentValues();
        for (LstTestCatalogDetails testCatalogDetail : lstTestCatalogDetails) {
            contentValues.put(TEST_CATALOG_DETAILS_ID, testCatalogDetail.getTestCatalogDetailsID());
            contentValues.put(TESTCATALOGID, testCatalogDetail.getTestCatalogID());
            contentValues.put(NAME, testCatalogDetail.getName());
            contentValues.put(VALUE, testCatalogDetail.getValue());
            db.update(TABLE_TEST_CATALOG_DETAILS, contentValues, TESTCATALOGID + "=" + testCatalogDetail.getTestCatalogID(), null);
        }*/

        String sql = "update TestcatalogDetails SET  test_catalog_details_id=?, test_catalog_id=?, name=?,value=? WHERE test_catalog_id=?";

        SQLiteStatement stmt = db.compileStatement(sql);

        for (LstTestCatalogDetails testCatalogDetail : lstTestCatalogDetails) {
            stmt.bindString(1, testCatalogDetail.getTestCatalogDetailsID());
            stmt.bindString(2, testCatalogDetail.getTestCatalogID());
            stmt.bindString(3, testCatalogDetail.getName());
            stmt.bindString(4, testCatalogDetail.getValue());
            stmt.bindString(5, testCatalogDetail.getTestCatalogID());
            stmt.executeUpdateDelete();
            stmt.clearBindings();
        }


    }

    private void insertTestCatlog(LstNewCatalog[] value, SQLiteDatabase db) {


        if(value.length>0) {
            StringBuilder td = new StringBuilder("delete from TestcatalogDetails where test_catalog_id in (");
            StringBuilder tc = new StringBuilder("delete from TestCatalog where test_catalog_id in (");

            StringBuilder sb = new StringBuilder();
            int i = 1;
            for (LstNewCatalog n : value) {
                if (sb.length() > 0) sb.append(',');
                sb.append(n.getTestCatalogID());
                if (i++ == value.length) sb.append(")");
            }

            String queryTestCat = tc.append(sb.toString()).toString();
            String queryTestDetails = td.append(sb.toString()).toString();
            ;
            db.execSQL(queryTestDetails);
            db.execSQL(queryTestCat);

     /*   StringBuilder sb = new StringBuilder("insert into TestCatalog (test_catalog_id, display_code, code, name,location,status) values ");

        int i = 1;
        for (LstNewCatalog newCatalog : value) {
            sb.append("(");
            sb.append("'" + newCatalog.getTestCatalogID() + "','" + newCatalog.getDisplayCode() + "','" + newCatalog.getCode() + "','" + newCatalog.getName() + "','" + newCatalog.getLocation() + "','" + newCatalog.getStatus() + "')");
            if (i++ != value.length) {
                sb.append(",");
            }
        }
        String query = sb.toString();*/


            try {
                String sql = "insert into TestCatalog (test_catalog_id, display_code, code, name,location,status) values (?, ?, ?, ?,?,?);";
                db.beginTransaction();
                SQLiteStatement stmt = db.compileStatement(sql);

                for (LstNewCatalog newCatalog : value) {

                    //generate some values

                    stmt.bindString(1, newCatalog.getTestCatalogID());
                    stmt.bindString(2, newCatalog.getDisplayCode());
                    stmt.bindString(3, newCatalog.getCode());
                    stmt.bindString(4, newCatalog.getName());
                    stmt.bindString(5, newCatalog.getLocation());
                    stmt.bindString(6, newCatalog.getStatus());
                    stmt.executeInsert();
                    stmt.clearBindings();
                    if (newCatalog.getLstTestCatalogDetails() != null && newCatalog.getLstTestCatalogDetails().length > 0) {
                        insertIntoTestCatDetails(newCatalog.getLstTestCatalogDetails(), db);
                    }


                }

                db.setTransactionSuccessful();
            } catch (Exception e) {
                Log.w("XML:", e);
            } finally {
                db.endTransaction();
            }
        }





   /*     ContentValues contentValues = new ContentValues();


        for (LstNewCatalog newCatalog:value) {
            contentValues.put(TESTCATALOGID,newCatalog.getTestCatalogID());
            contentValues.put(DISPLAYCODE,newCatalog.getDisplayCode());
            contentValues.put(CODE,newCatalog.getCode());
            contentValues.put(NAME,newCatalog.getName());
            contentValues.put(LOCATION,newCatalog.getLocation());
            contentValues.put(STATUS,newCatalog.getStatus());
            if(newCatalog.getLstTestCatalogDetails()!=null&&newCatalog.getLstTestCatalogDetails().length>0) {
                insertIntoTestCatDetails(newCatalog.getLstTestCatalogDetails(),db);
            }
            db.insert(TABLE_TEST_CATALOG,  null, contentValues);*/




      /*  ContentValues contentValues = new ContentValues();
        SQLiteDatabase db = super.OpenDataBase();
        for (TestCatelogUpdate testCatelogUpdate:value) {
            contentValues.put(FAQ_QUESTION, testCatelogUpdate.getQuestion());
            contentValues.put(FAQ_ANSWER, faq.getAnswer());

            db.insert(TABLE_FAQ, null, contentValues);
        }
        this.CloseDataBaseConnection(db);*/
    }

    private void insertIntoTestCatDetails(LstTestCatalogDetails[] lstTestCatalogDetails, SQLiteDatabase db) {

        try {
            String sql = "insert into TestcatalogDetails (test_catalog_details_id, test_catalog_id, name, value) values (?, ?, ?, ?);";
            db.beginTransaction();
            SQLiteStatement stmt = db.compileStatement(sql);

            for (LstTestCatalogDetails testCatalogDetails : lstTestCatalogDetails) {
                //generate some values

                stmt.bindString(1, testCatalogDetails.getTestCatalogDetailsID());
                stmt.bindString(2, testCatalogDetails.getTestCatalogID());
                stmt.bindString(3, testCatalogDetails.getName());
                stmt.bindString(4, testCatalogDetails.getValue());


                long entryID = stmt.executeInsert();

            }

            db.setTransactionSuccessful();
        } catch (Exception e) {
            Log.w("XML:", e);
        } finally {
            db.endTransaction();
        }

      /*  ContentValues contentValues = new ContentValues();

        for (LstTestCatalogDetails testCatalogDetail : lstTestCatalogDetails) {
            contentValues.put(TEST_CATALOG_DETAILS_ID, testCatalogDetail.getTestCatalogDetailsID());
            contentValues.put(TESTCATALOGID, testCatalogDetail.getTestCatalogID());
            contentValues.put(NAME, testCatalogDetail.getName());
            contentValues.put(VALUE,  testCatalogDetail.getValue());
            db.insert(TABLE_TEST_CATALOG_DETAILS, null, contentValues);
        }*/


    }

   /* private HashMap<String,ArrayList<TestCatelogUpdate>> splitInsertUpdateDeleteCollection(TestCatelogUpdate[] testCatelogUpdates){

        HashMap<String,ArrayList<TestCatelogUpdate>> updatedMap=new HashMap<>();
        ArrayList<TestCatelogUpdate> newTestCatelogs= new ArrayList<>();
        ArrayList<TestCatelogUpdate> updatedTestCatelogs= new ArrayList<>();
        ArrayList<TestCatelogUpdate> deletedTestCatelogs= new ArrayList<>();
        for (TestCatelogUpdate testCatelogUpdate:testCatelogUpdates) {
            if(testCatelogUpdate.getStatus().equals("N")){
                newTestCatelogs.add(testCatelogUpdate);
            }
            else if(testCatelogUpdate.getStatus().equals("D")){
                deletedTestCatelogs.add(testCatelogUpdate);
            }
            else if(testCatelogUpdate.getStatus().equals("U")){
                updatedTestCatelogs.add(testCatelogUpdate);
            }

        }
        updatedMap.put("Insert",newTestCatelogs);
        updatedMap.put("Update",updatedTestCatelogs);
        updatedMap.put("Delete",deletedTestCatelogs);

        return updatedMap;
    }*/

}
