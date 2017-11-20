package com.effone.pdlconnprovider.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.AppPreferences;



public class SettingsFragment extends Fragment implements View.OnClickListener {

    private Switch mSWPush,mSWLocation,mSWTestCat;

    private TextView mBtnSave;

    private AppPreferences mAppPreferences;

    public static final String SETTING_YES="yes";

    public static final String SETTING_NO="no";





    public SettingsFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_settings, container, false);
        mAppPreferences=new AppPreferences(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mSWPush=(Switch)root.findViewById(R.id.sw_push);
        mSWLocation=(Switch)root.findViewById(R.id.sw_location);
        mSWTestCat=(Switch)root.findViewById(R.id.sw_test_cat);
        mBtnSave=(TextView)root.findViewById(R.id.btn_save);
        if(mAppPreferences.getSETTING_TEST_CAT_DATA().equals(SETTING_YES)){
            mSWTestCat.setChecked(true);
        }
        else{
            mSWTestCat.setChecked(false);
        }
        if(mAppPreferences.getSETTING_LOCATION_SERVICE().equals(SETTING_YES)){
            mSWLocation.setChecked(true);
        }
        else{
            mSWLocation.setChecked(false);
        }
        if(mAppPreferences.getSETTINGS_PUSH_NOTIFICATION().equals(SETTING_YES)){
            mSWPush.setChecked(true);
        }
        else{
            mSWPush.setChecked(false);
        }
        mBtnSave.setOnClickListener(this);
        mSWPush.setOnClickListener(this);
        mSWLocation.setOnClickListener(this);
        mSWTestCat.setOnClickListener(this);
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

    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_save) {
            if (mSWTestCat.isChecked()) {
                mAppPreferences.saveSETTING_TEST_CAT_DATA(SETTING_YES);
            } else {
                mAppPreferences.saveSETTING_TEST_CAT_DATA(SETTING_NO);
            }
            if (mSWLocation.isChecked()) {
                mAppPreferences.saveSETTING_LOCATION_SERVICE(SETTING_YES);
            } else {
                mAppPreferences.saveSETTING_LOCATION_SERVICE(SETTING_NO);
            }
            if (mSWPush.isChecked()) {
                mAppPreferences.saveSETTINGS_PUSH_NOTIFICATION(SETTING_YES);
            } else {
                mAppPreferences.saveSETTINGS_PUSH_NOTIFICATION(SETTING_NO);
            }
            getActivity().getFragmentManager().popBackStack();
        }
    }
}
