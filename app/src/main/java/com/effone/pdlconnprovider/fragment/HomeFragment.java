package com.effone.pdlconnprovider.fragment;

import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.AppPreferences;
import com.effone.pdlconnprovider.common.PdlApiUtil;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.common.Settings;
import com.effone.pdlconnprovider.db.DeleteDbHelper;
import com.effone.pdlconnprovider.db.InsertDbHelper;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.TestCat;
import com.effone.pdlconnprovider.model.TestCatUpdate;
import com.effone.pdlconnprovider.model.about_us.About;
import com.effone.pdlconnprovider.model.contacts.Contact;
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
import java.util.Calendar;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class HomeFragment extends Fragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    // TODO: Rename and change types of parameters


    private TextView mPdlConnectBtn, mLocationBtn, mContactUsBtn, mAboutUsBtn, mSettingBtn, mTestCatBtn,mTvCopyRight;
    private CommonHeaderFragment mCommonHeaderFragment;
    private LocationFragment mLocationFragment;
    private ContactFragment mContactFragment;
    private AboutUsFragment mAboutUsFragment;
    private SettingsFragment mSettingsFragment;
    private InsertDbHelper mInsertDbHelper;
    private DeleteDbHelper mDeleteDbHelper;
    private SelectDbHelper mSelectDbHelper;
    private TestCatFragment mTestCatFragment;
    private Contact[] mContacts;
    private About[] mAbouts;
    private Faq[] mFaqs;
    private Location[] mLocations;
    private TestCatFullUpdate mTestCatFullUpdates;
    private ProgressDialog mCommonProgressDialog;
    private AppPreferences mAppPreferences;


    private Gson mGson;

    private TestCatUpdate mTestCatelogUpdate;


    private RequestQueue mQueue;

    private String mJson;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_home, container, false);
         mInsertDbHelper=new InsertDbHelper(getActivity());
        mDeleteDbHelper=new DeleteDbHelper(getActivity());
        mSelectDbHelper=new SelectDbHelper(getActivity());
        mAppPreferences = new AppPreferences(getActivity());
        getActivity().registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mGson = new Gson();
        mQueue = Volley.newRequestQueue(getActivity());
        init(root);
        return root;
    }

    private void init(View root) {
        mPdlConnectBtn = (TextView) root.findViewById(R.id.btn_pdl_connector);
        mLocationBtn = (TextView) root.findViewById(R.id.btn_location);
        mContactUsBtn = (TextView) root.findViewById(R.id.btn_conatact_us);
        mAboutUsBtn = (TextView) root.findViewById(R.id.btn_about_us);
        mSettingBtn = (TextView) root.findViewById(R.id.btn_settings);
        mTestCatBtn = (TextView) root.findViewById(R.id.btn_test_cat);
        mTvCopyRight= (TextView) root.findViewById(R.id.tv_copy_right);
        Calendar now = Calendar.getInstance();
        int year = now.get(Calendar.YEAR);
        String yearInString = String.valueOf(year);
        Resources res = getResources();
        String text = String.format(res.getString(R.string.copyright_text), yearInString);
        mTvCopyRight.setText(text);
        mSettingBtn.setOnClickListener(this);
        mContactUsBtn.setOnClickListener(this);
        mAboutUsBtn.setOnClickListener(this);
        mPdlConnectBtn.setOnClickListener(this);
        mLocationBtn.setOnClickListener(this);
        mTestCatBtn.setOnClickListener(this);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if(getActivity()!=null && mConnReceiver!=null) {
            getActivity().unregisterReceiver(this.mConnReceiver);
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_pdl_connector) {
            connectPdlConnectorAppOrDownload();
        } else if (v.getId() == R.id.btn_test_cat) {

            if(PdlUtils.isInternetOn(getActivity())&& mAppPreferences.getSETTING_TEST_CAT_DATA().equals(SettingsFragment.SETTING_YES)&& PdlUtils.getDaysCount(mAppPreferences.getSERVER_DATE(),Settings.SERVER_DATE)>=5) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setCancelable(false);
                builder.setMessage("Test Catalog data is not updated. Do you want to update now?");
                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getTestCatlogDetailsApi();
                    }
                });
                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog alert = builder.create();
                alert.show();
            }
            else if(PdlUtils.isInternetOn(getActivity())&&Settings.TEST_CATLOG&&PdlUtils.isInternetOn(getActivity())&&mAppPreferences.getSETTING_TEST_CAT_DATA().equals(SettingsFragment.SETTING_YES)&&mAppPreferences.getSERVER_DATE().equals(Settings.SERVER_DATE)) {
        //       getTestCatelogUpdatefromApi();
                getTestCatlogDetailsApi();
          }
            mCommonHeaderFragment =new  CommonHeaderFragment();
            mTestCatFragment= new TestCatFragment();
            mCommonHeaderFragment.setTitle("Test Catalog");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mTestCatFragment, "testCatlog").addToBackStack(null).commit();

        } else if (v.getId() == R.id.btn_conatact_us) {
            if(PdlUtils.isInternetOn(getActivity())&&Settings.CONTACT){
                getContactFromApi();

            }
            mCommonHeaderFragment = new CommonHeaderFragment();
            mContactFragment = new ContactFragment();
            mCommonHeaderFragment.setTitle("Contact Us");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mContactFragment, "contact").addToBackStack(null).commit();

        } else if (v.getId() == R.id.btn_settings) {
            mCommonHeaderFragment = new CommonHeaderFragment();
            mSettingsFragment = new SettingsFragment();
            mCommonHeaderFragment.setTitle("Settings");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mSettingsFragment, "settings").addToBackStack(null).commit();

        } else if (v.getId() == R.id.btn_about_us) {

            mCommonHeaderFragment = new CommonHeaderFragment();
            mAboutUsFragment = new AboutUsFragment();
            mCommonHeaderFragment.setTitle("About");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mAboutUsFragment, "about").addToBackStack(null).commit();
        } else if (v.getId() == R.id.btn_location) {
            if(PdlUtils.isInternetOn(getActivity())&&Settings.LOCATION){
                getLocationsRequest();

            }
            mCommonHeaderFragment = new CommonHeaderFragment();
            mLocationFragment = new LocationFragment();
            mCommonHeaderFragment.setTitle("PDL PSC Locations");
            mCommonHeaderFragment.changeToSearchIcon(true);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"locationheader").add(R.id.content_fragment, mLocationFragment, "location").addToBackStack(null).commit();
        }
    }

    private void connectPdlConnectorAppOrDownload() {
        boolean installed = appInstalledOrNot("com.lifepoint.pdloutreachmd");

        if (installed) {
            //This intent will help you to launch if the package is already installed
            Intent LaunchIntent = getActivity().getPackageManager()
                    .getLaunchIntentForPackage("com.lifepoint.pdloutreachmd");//if this name does not work provide the other one
            startActivity(LaunchIntent);
        } else {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.lifepoint.pdloutreachmd")));
            } catch (android.content.ActivityNotFoundException anfe) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://play.google.com/store/apps/details?id=com.lifepoint.pdloutreachmd")));
            }
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getActivity().getPackageManager();
        boolean app_installed = false;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        } catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */

    private void getTestCatelogUpdatefromApi() {
        String url=PdlApiUtil.GET_TEST_CAT_UPDATE+mSelectDbHelper.getTestcatSyncDate();
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mTestCatelogUpdate = mGson.fromJson(mJson, TestCatUpdate.class);
               // mInsertDbHelper.updateTestCatalog(mTestCatelogUpdate);
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON :", "" + error);
            }
        });
        mQueue.add(contactStringRequest);

    }
    private void getContactFromApi() {

        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_CONTACT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mContacts = mGson.fromJson(mJson, Contact[].class);
                mDeleteDbHelper.deleteAllFromContacts();
                mInsertDbHelper.insertInToContactTable(mContacts);
                if(mCommonProgressDialog!=null) {
                    mCommonProgressDialog.cancel();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON :", "" + error);
            }
        });

        mQueue.add(contactStringRequest);
    }


    private void getLocationsRequest() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
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
                if (mCommonProgressDialog != null) {
                    mCommonProgressDialog.cancel();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("JSON :", "" + error);
            }
        });

        mQueue.add(locationStringRequest);
    }
    private void getTestCatlogDetailsApi() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }

        String url=PdlApiUtil.GET_TEST_CAT_DETAILS+mSelectDbHelper.getTestcatSyncDate();
        PdlFileRequest testCatDetailPdlFileRequest = new PdlFileRequest(Request.Method.GET, url, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                try {
                    if (response != null) {

                        FileOutputStream outputStream;
                        String name = "json.zip";
                        outputStream = new FileOutputStream(getActivity().getFilesDir() + "/json.zip");
                        outputStream.write(response);
                        outputStream.close();
                        String ZipPath = getActivity().getFilesDir()+ "/json.zip";
                       new newAsych().execute(ZipPath);


                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    Log.e("KEY_ERROR", "UNABLE TO DOWNLOAD FILE");
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("error",""+error);
            }
        });
        mQueue.add(testCatDetailPdlFileRequest);
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

            }

        } catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
        File file = new File(getActivity().getFilesDir() + "/json.zip");
        file.delete();
        if(mCommonProgressDialog!=null){
            mCommonProgressDialog.cancel();
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

}

