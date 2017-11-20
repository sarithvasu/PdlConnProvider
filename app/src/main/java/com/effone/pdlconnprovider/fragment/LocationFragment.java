package com.effone.pdlconnprovider.fragment;

import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.adapter.LoctionSummeryListAdapter;
import com.effone.pdlconnprovider.common.AppPreferences;
import com.effone.pdlconnprovider.common.PdlApiUtil;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.DisplayContact;
import com.effone.pdlconnprovider.model.Faq;
import com.effone.pdlconnprovider.model.Location;
import com.effone.pdlconnprovider.model.LocationSummery;
import com.effone.pdlconnprovider.model.TestCatUpdate;
import com.effone.pdlconnprovider.model.testcat_full_update.TestCatFullUpdate;
import com.effone.pdlconnprovider.volley.PdlRequest;
import com.google.gson.Gson;

import java.util.ArrayList;

public class LocationFragment extends Fragment implements AdapterView.OnItemClickListener {

    private ListView mLocationSummeryList;
    private SelectDbHelper mSelectDbHelper;
    private LoctionSummeryListAdapter mLoctionSummeryListAdapter;
    private int mLocationId;
    private LocationDeatilsFragment mLocationDeatilsFragment;
    private CommonHeaderFragment mCommonHeaderFragment;

    private ProgressDialog mCommonProgressDialog;
    private Gson mGson;
    private RequestQueue mQueue;
    private String mJson;
    private String mAdress = "";
    private double mLongitude;
    private double mLatitude;
    private int mRadius;
    private boolean fromSearch;
    private ArrayList<LocationSummery> locationSummeries;

    public LocationFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        mGson = new Gson();
        mQueue = Volley.newRequestQueue(getActivity());
        init(root);
        return root;

    }

    private void init(View root) {
        mLocationSummeryList = (ListView) root.findViewById(R.id.location_summery_list);
        setList();
        mLocationSummeryList.setOnItemClickListener(this);
    }

    private void setList() {
        if (mLocationSummeryList != null) {
            if (fromSearch) {
                getLocationsRequest(mRadius, mAdress, mLatitude, mLongitude);
            } else {
                mLoctionSummeryListAdapter = new LoctionSummeryListAdapter(getActivity(), R.layout.location_summery_ilist_tem, mSelectDbHelper.getLocationSummeryList());
                mLocationSummeryList.setAdapter(mLoctionSummeryListAdapter);
            }
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {

    }

    public void setFromSearch(boolean fromSearch) {
        this.fromSearch = fromSearch;
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
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        mLocationId = ((LocationSummery) parent.getAdapter().getItem(position)).locationId;
        mLocationDeatilsFragment = new LocationDeatilsFragment();
        mCommonHeaderFragment = new CommonHeaderFragment();
        mCommonHeaderFragment.setTitle("PSC Location Details");
        mLocationDeatilsFragment.setLocationId(mLocationId);
        FragmentTransaction ft1 = getFragmentManager().beginTransaction();
        ft1.add(R.id.header_fragment, mCommonHeaderFragment, "header").add(R.id.content_fragment, mLocationDeatilsFragment, "location details").addToBackStack(null).commit();

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

    private void getLocationsRequest(int radius, String adress, double lat, double lng) {


        if (mCommonProgressDialog == null) {
            mCommonProgressDialog = PdlUtils.createProgressDialog(getActivity());
            mCommonProgressDialog.show();
            mCommonProgressDialog.setMessage("Please wait...");
            mCommonProgressDialog.setCancelable(false);
        } else {
            mCommonProgressDialog.show();
        }
        String url = "";
        if (!adress.equals("")) {
            url = PdlApiUtil.GET_LOCATIONS + "?radius=" + radius + "&address=" + adress;
            url = url.replaceAll(" ", "%20");
        } else {
            url = PdlApiUtil.GET_LOCATIONS + "?radius=" + radius + "&latitude=" + lat+"&longitude="+lng;
        }
        PdlRequest locationStringRequest = new PdlRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                mJson = response;
                if (!mJson.contains("No location(s) found.")) {
                    locationSummeries = getLocationSummeryFromLocations(mGson.fromJson(mJson, Location[].class));
                    mLoctionSummeryListAdapter = new LoctionSummeryListAdapter(getActivity(), R.layout.location_summery_ilist_tem, locationSummeries);
                    mLocationSummeryList.setAdapter(mLoctionSummeryListAdapter);

                    if (mCommonProgressDialog != null) {
                        mCommonProgressDialog.cancel();
                    }
                } else {
                    if (mCommonProgressDialog != null) {
                        mCommonProgressDialog.cancel();
                    }
                    PdlUtils.createOKAlert(getActivity(),"", "No location(s) found.");
                    getActivity().onBackPressed();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (mCommonProgressDialog != null) {
                    mCommonProgressDialog.cancel();
                }
                PdlUtils.createOKAlert(getActivity(),"", "Server Error");
            }
        });


    mQueue.add(locationStringRequest);

}

    private ArrayList<LocationSummery> getLocationSummeryFromLocations(Location[] locations) {
        ArrayList<LocationSummery> locationSummeries = new ArrayList<LocationSummery>();
        for (Location location : locations) {
            LocationSummery locationSummery = new LocationSummery();
            locationSummery.locationId = location.getLocationID();
            locationSummery.locationName = location.getName();
            locationSummery.locationAdress1 = location.getAddress();
            locationSummery.locationAddress2 = location.getCity() + ", " + location.getState() + ", " + location.getZip();
            locationSummeries.add(locationSummery);
        }
        return locationSummeries;
    }

    public void setSearchValues(int mRadius, String mAdress) {
        this.mRadius = mRadius;
        this.mAdress = mAdress;
    }

    public void setSearchGPSValues(int mRadius, double latitude, double longitude) {
        this.mRadius = mRadius;
        this.mLongitude = longitude;
        this.mLatitude = latitude;
    }

}
