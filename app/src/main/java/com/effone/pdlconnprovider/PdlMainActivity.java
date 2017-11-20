package com.effone.pdlconnprovider;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.util.TimingLogger;
import android.view.Gravity;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.effone.pdlconnprovider.common.AppContant;
import com.effone.pdlconnprovider.common.AppPreferences;
import com.effone.pdlconnprovider.common.PdlApiUtil;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.common.Settings;
import com.effone.pdlconnprovider.db.DataBaseHelper;
import com.effone.pdlconnprovider.db.DeleteDbHelper;
import com.effone.pdlconnprovider.db.InsertDbHelper;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.fragment.CommonHeaderFragment;
import com.effone.pdlconnprovider.fragment.HomeFragment;
import com.effone.pdlconnprovider.fragment.LocationDeatilsFragment;
import com.effone.pdlconnprovider.fragment.LocationSearchFragment;
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.SyncInfo;
import com.effone.pdlconnprovider.model.TestCatUpdate;
import com.effone.pdlconnprovider.model.about_us.About;
import com.effone.pdlconnprovider.model.contacts.Contact;
import com.effone.pdlconnprovider.model.contacts.SyncDetails;
import com.effone.pdlconnprovider.model.test_cat_full.TestCalFullData;
import com.effone.pdlconnprovider.model.testcat_full_update.TestCatFullUpdate;
import com.effone.pdlconnprovider.volley.PdlFileRequest;
import com.effone.pdlconnprovider.volley.PdlRequest;
import com.google.gson.Gson;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class PdlMainActivity extends AppCompatActivity {
    private DataBaseHelper mDataBaseHelper;
    private InsertDbHelper mInsertDbHelper;
    private SelectDbHelper mSelectDbHelper;
    private DeleteDbHelper mDeleteDbHelper;
    private Gson mGson;
    private RequestQueue mQueue;

    private byte[] mResponse;
    private long startTime;
    private long endTime;
    private String mJson;
    private Location[] mLocations;
    private SyncDetails mSyncDetails;
    private Contact[] mContacts;
    private About[] mAbouts;
    private Faq[] mFaqs;
    private TestCalFullData[] mTestCalFullDatas;
    private Fragment homeFragment;
    private ProgressDialog mCommonProgressDialog;
    private String mUrl;
    private AppPreferences mAppPreferences;

    private TestCatFullUpdate mTestCatFullUpdates;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;


    private static final String TAG = "Timer";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdl_main);
        registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mAppPreferences = new AppPreferences(this);
        mDataBaseHelper = new DataBaseHelper(PdlMainActivity.this);
        mInsertDbHelper = new InsertDbHelper(PdlMainActivity.this);
        mSelectDbHelper = new SelectDbHelper(PdlMainActivity.this);
        mDeleteDbHelper = new DeleteDbHelper(PdlMainActivity.this);
        mGson = new Gson();
        mQueue = Volley.newRequestQueue(this);
        if (!checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getApplicationContext(), this)){
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getApplicationContext(), this);
        }

        if(!PdlUtils.isInternetOn(this)){
            AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setCancelable(false);
            builder.setTitle("No Internet connection.");
            builder.setMessage("Unable to download updates. Do you want to continue offline?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();

                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    finish();
                }
            });
            AlertDialog alert = builder.show();
            TextView messageText = (TextView)alert.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            alert.show();
        }
        if (mAppPreferences.getFIRST_TIME_LAUNCH()) {
            setUpDataBase();
            if (PdlUtils.isInternetOn(this)) {
                startTime=System.currentTimeMillis();
                sendVolleyRequest();
            }


        } else {
            if (PdlUtils.isInternetOn(this)) {
              sendNeedSyncRequest();

                //  getTestCatlogDetailsApi();
            }
        }
        if (savedInstanceState == null) {
            homeFragment = new HomeFragment();
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.content_fragment, homeFragment).commit();
        }
    }
/*
    private boolean hasSyncForTheDay(String serverDate) {
        if()
        return true;
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    private void sendNeedSyncRequest() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(this);
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait.");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }

        SyncInfo syncInfos = new SyncInfo();
        syncInfos = mSelectDbHelper.getSyncINfo();
        String locationDate = "";
        String aboutDate = "";
        String faqDate = "";
        String contactDate = "";
        String privacyDate = "";
        String testCatDate = "";
        for (SyncInfo syncInfo : syncInfos) {
            if (syncInfo.table_name.equals(AppContant.LOCATION)) {
                locationDate = syncInfo.last_sync_date;
            } else if (syncInfo.table_name.equals(AppContant.CONTACT)) {
                contactDate = syncInfo.last_sync_date;
            } else if (syncInfo.table_name.equals(AppContant.ABOUT)) {
                aboutDate = syncInfo.last_sync_date;
            } else if (syncInfo.table_name.equals(AppContant.FAQ)) {
                faqDate = syncInfo.last_sync_date;
            } else if (syncInfo.table_name.equals(AppContant.PRIVACY)) {
                privacyDate = syncInfo.last_sync_date;
            } else if (syncInfo.table_name.equals(AppContant.TEST_CATLOG)) {
                testCatDate = syncInfo.last_sync_date;
            }
        }
        mUrl = PdlApiUtil.GET_NEEDED_DATA_SYNC + "?LocationAuditDate=" + locationDate + "&ContactAuditDate=" + contactDate + "&AboutAuditDate=" + aboutDate + "&PrivacyAuditDate=" + privacyDate + "&FAQAuditDate=" + faqDate +"&CatalogAuditDate=" + testCatDate;
        PdlRequest neededSyncStringRequest = new PdlRequest(Request.Method.GET, mUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mSyncDetails = mGson.fromJson(mJson, SyncDetails.class);
                Settings.SERVER_DATE = mSyncDetails.getServerDate();
                if (!mAppPreferences.getSERVER_DATE().equals(mSyncDetails.getServerDate())) {
                    Settings.ABOUT = mSyncDetails.getCanSyncAbout();

                    Settings.CONTACT = mSyncDetails.getCanSyncContact();
                    Settings.FAQ = mSyncDetails.getCanSyncFAQ();
                    Settings.LOCATION = mSyncDetails.getCanSyncLocation();
                    Settings.PRIVACY = mSyncDetails.getCanSyncPrivacy();
                    Settings.TEST_CATLOG = mSyncDetails.getCanSyncTestCatalog();
                    mAppPreferences.saveSERVER_DATE(mSyncDetails.getServerDate());
                }
                if (mCommonProgressDialog != null) {

                    mCommonProgressDialog.cancel();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQueue.add(neededSyncStringRequest);
    }

    private void sendVolleyRequest() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(this);
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait…..Syncing for first time and this may take a while.");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }

        PdlRequest locationStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_LOCATIONS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mLocations = mGson.fromJson(mJson, Location[].class);
                mDeleteDbHelper.deleteAllFromLocation();
                mInsertDbHelper.insertInToLocationTable(mLocations);
                getContactFromApi();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQueue.add(locationStringRequest);
    }

    private void getContactFromApi() {
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mContacts = mGson.fromJson(mJson, Contact[].class);
                mDeleteDbHelper.deleteAllFromContacts();
                mInsertDbHelper.insertInToContactTable(mContacts);
                getAboutUsFromApi();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON :", "" + error);
            }
        });

        mQueue.add(contactStringRequest);
    }

    private void getAboutUsFromApi() {
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_ABOUT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mAbouts = mGson.fromJson(mJson, About[].class);
                mDeleteDbHelper.deleteAllFromCONTENT(8);
                mInsertDbHelper.insertIntoAboutUs(mAbouts, 8);

                getPrivacyFromApi();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQueue.add(contactStringRequest);

    }

    private void getPrivacyFromApi() {
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_PRIVACY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mAbouts = mGson.fromJson(mJson, About[].class);
                mDeleteDbHelper.deleteAllFromCONTENT(9);
                mInsertDbHelper.insertIntoAboutUs(mAbouts, 9);
                getFAQfromApi();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error.",Toast.LENGTH_SHORT).show();
                }
            }
        });

        mQueue.add(contactStringRequest);

    }

    private void getFAQfromApi() {

        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_FAQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mFaqs = mGson.fromJson(mJson, Faq[].class);
                mDeleteDbHelper.deleteAllFromFAQ();
                mInsertDbHelper.insertIntoFaq(mFaqs);
                getTestCatelogUpdatefromApi();

            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mQueue.add(contactStringRequest);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Settings.ABOUT = false;
        Settings.CONTACT = false;
        Settings.FAQ = false;
        Settings.LOCATION = false;
        Settings.PRIVACY = false;
        Settings.SERVER_DATE = "";



        if(mConnReceiver!=null) {
            unregisterReceiver(mConnReceiver);
        }
    }

    private void setUpDataBase() {


        mDataBaseHelper.SetUpAndImportDataBase();
    }



    private class newAsych extends AsyncTask<String, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(String... params) {





            writeToFile(params[0]);



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);


        }
    }

    private void writeToFile(String path) {

        try {
            ZipFile zipFile = new ZipFile(path);

            Enumeration<? extends ZipEntry> entries = zipFile.entries();

            while (entries.hasMoreElements()) {

                ZipEntry entry = entries.nextElement();

                InputStream stream = zipFile.getInputStream(entry);

                int size = stream.available();


                    mTestCatFullUpdates= mGson.fromJson((new InputStreamReader(stream, "UTF-8")), TestCatFullUpdate.class);
                    mInsertDbHelper.updateTestCatalog(mTestCatFullUpdates);
                    mAppPreferences.saveFIRST_TIME_LAUNCH(false);
                    String serverDate = mTestCatFullUpdates.getServerDate();
                    ArrayList<SyncInfo> syncInfos = new ArrayList<>();
                    syncInfos.add(new SyncInfo(AppContant.LOCATION, serverDate));
                    syncInfos.add(new SyncInfo(AppContant.ABOUT, serverDate));
                    syncInfos.add(new SyncInfo(AppContant.PRIVACY, serverDate));
                    syncInfos.add(new SyncInfo(AppContant.CONTACT, serverDate));
                    syncInfos.add(new SyncInfo(AppContant.FAQ, serverDate));
                    syncInfos.add(new SyncInfo(AppContant.TEST_CATLOG, serverDate));
                    mInsertDbHelper.insertAllDataIntoSyncInfoTable(syncInfos);
                    if (mCommonProgressDialog != null) {
                       endTime=System.currentTimeMillis();
                       long duration=(startTime-endTime)/1000;
                        Log.e("TIME_TOTAL"," "+duration);
                        mCommonProgressDialog.cancel();
                    }

            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        File file = new File(getFilesDir() + "/json.zip");
        file.delete();
    }


    private void getTestCatelogUpdatefromApi() {



        String url=PdlApiUtil.GET_TEST_CAT_DETAILS+mSelectDbHelper.getInitialDate();
        PdlFileRequest testCatDetailPdlFileRequest = new PdlFileRequest(Request.Method.GET, url, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                try {
                    if (response != null) {
                        TimingLogger timings = new TimingLogger(TAG, "methodA");





                        FileOutputStream outputStream;
                        String name = "json.zip";
                        outputStream = new FileOutputStream(getFilesDir() + "/json.zip");
                        outputStream.write(response);
                        outputStream.close();
                        timings.addSplit("work A");
                        timings.dumpToLog();
                        String ZipPath = getFilesDir()+ "/json.zip";
                        new newAsych().execute(ZipPath);


                    }
                } catch (Exception e) {
                    if(mCommonProgressDialog!=null){
                        mCommonProgressDialog.cancel();
                        Toast.makeText(PdlMainActivity.this,"Occur Some Server Error",Toast.LENGTH_SHORT).show();
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if(mCommonProgressDialog!=null){
                    mCommonProgressDialog.cancel();
                    Toast.makeText(PdlMainActivity.this,"Occur Some Server Error",Toast.LENGTH_SHORT).show();
                }
            }
        });
        mQueue.add(testCatDetailPdlFileRequest);


    }

    @Override
    public void onBackPressed() {
        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
        }
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
            Fragment fragment = getFragmentManager().findFragmentByTag("locationheader");
            if(fragment instanceof CommonHeaderFragment){
                ((CommonHeaderFragment) fragment).changeToSearchIcon(true);
                fragment.getFragmentManager().beginTransaction().attach(fragment).commit();
            }

        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
            builder.setCancelable(false);
            builder.setMessage("Are you sure, you want to close?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Settings.ABOUT = false;
                    Settings.CONTACT = false;
                    Settings.FAQ = false;
                    Settings.LOCATION = false;
                    Settings.PRIVACY = false;
                    Settings.SERVER_DATE = "";
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            AlertDialog alert = builder.show();
            TextView messageText = (TextView)alert.findViewById(android.R.id.message);
            messageText.setGravity(Gravity.CENTER);
            alert.show();
        }

    }

    public BroadcastReceiver mConnReceiver = new BroadcastReceiver()
    {
        public void onReceive(Context context, Intent intent)
        {
            if(!PdlUtils.isInternetOn(context)&&   mCommonProgressDialog!=null){
                mCommonProgressDialog.cancel();
                Toast.makeText(context,"lost connection",Toast.LENGTH_SHORT).show();
            }

        }
    };
    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            //Toast.makeText(getActivity().getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }
    public  boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);

        if (result == PackageManager.PERMISSION_GRANTED){

            return true;

        }
        else{
            return false;
        }
    }

}
