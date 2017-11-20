package com.effone.pdlconnprovider.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.Uri;
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
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.about_us.About;
import com.effone.pdlconnprovider.model.contacts.SyncDetails;
import com.effone.pdlconnprovider.volley.PdlRequest;
import com.google.gson.Gson;

public class AboutUsFragment extends Fragment implements View.OnClickListener {

    private TextView mTvAboutUs, mTvFaq, mTvPrivacy;
    private CommonHeaderFragment mCommonHeaderFragment;
    private AboutFragment mAboutFragment;
    private PrivacyFragment mPrivacyFragment;
    private FaqFragment mFaqFragment;
    private InsertDbHelper mInsertDbHelper;
    private DeleteDbHelper mDeleteDbHelper;
    private Gson mGson;
    private String mJson;
    private Location[] mLocations;
    private RequestQueue mQueue;
    private About[] mAbouts;
    private Faq[] mFaqs;
    private ProgressDialog mCommonProgressDialog;

    private String mUrl;
    private AppPreferences mAppPreferences;

    public AboutUsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about_us, container, false);
        mInsertDbHelper=new InsertDbHelper(getActivity());
        mDeleteDbHelper=new DeleteDbHelper(getActivity());
        getActivity().registerReceiver(this.mConnReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
        mGson = new Gson();
        mQueue = Volley.newRequestQueue(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mTvAboutUs = (TextView) root.findViewById(R.id.tv_about);
        mTvFaq = (TextView) root.findViewById(R.id.tv_faq);
        mTvPrivacy = (TextView) root.findViewById(R.id.tv_privacy);
        mTvAboutUs.setOnClickListener(this);
        mTvFaq .setOnClickListener(this);
        mTvPrivacy .setOnClickListener(this);

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
        if(v.getId()==R.id.tv_about){
            if(PdlUtils.isInternetOn(getActivity())&&Settings.ABOUT){
                getAboutUsFromApi();

            }
            mCommonHeaderFragment =new  CommonHeaderFragment();
            mAboutFragment= new AboutFragment();
            mCommonHeaderFragment.setTitle("About Us");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mAboutFragment, "about").addToBackStack(null).commit();
        }
        else if(v.getId()==R.id.tv_faq){
            if(PdlUtils.isInternetOn(getActivity())&&Settings.FAQ){
                getFAQfromApi();

            }
            mCommonHeaderFragment =new  CommonHeaderFragment();
            mFaqFragment= new FaqFragment();
            mCommonHeaderFragment.setTitle("FAQ");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mFaqFragment, "faq").addToBackStack(null).commit();
        }
        else if(v.getId()==R.id.tv_privacy){
            if(PdlUtils.isInternetOn(getActivity())&&Settings.PRIVACY){
                getPrivacyFromApi();

            }
            mCommonHeaderFragment =new  CommonHeaderFragment();
            mPrivacyFragment= new PrivacyFragment();
            mCommonHeaderFragment.setTitle("Privacy");
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment,"header").add(R.id.content_fragment, mPrivacyFragment, "privacy").addToBackStack(null).commit();
        }
    }
    private void getAboutUsFromApi() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_ABOUT_US, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mAbouts = mGson.fromJson(mJson, About[].class);
                mDeleteDbHelper.deleteAllFromCONTENT(8);
                mInsertDbHelper.insertIntoAboutUs(mAbouts, 8);
                if(mCommonProgressDialog!=null){
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
    private void getFAQfromApi() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }

        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_FAQ, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mFaqs = mGson.fromJson(mJson, Faq[].class);
                mDeleteDbHelper.deleteAllFromFAQ();
                mInsertDbHelper.insertIntoFaq(mFaqs);
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
        mQueue.add(contactStringRequest);

    }


    private void getPrivacyFromApi() {
        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }
        PdlRequest contactStringRequest = new PdlRequest(Request.Method.GET, PdlApiUtil.GET_PRIVACY, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                mAbouts = mGson.fromJson(mJson, About[].class);
                mDeleteDbHelper.deleteAllFromCONTENT(9);
                mInsertDbHelper.insertIntoAboutUs(mAbouts, 9);
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

        mQueue.add(contactStringRequest);

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
