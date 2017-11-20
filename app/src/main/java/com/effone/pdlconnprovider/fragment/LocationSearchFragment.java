package com.effone.pdlconnprovider.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;

import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.adapter.TestCatAdapter;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class LocationSearchFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks,LocationListener {
    private EditText mEtSearchLocation;
    private TextView mBtnSearchLocation;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    private Spinner mSpRadius;
    private HashMap<String, Integer> mDeCodeToIndex;
    private CommonHeaderFragment mCommonHeaderFragment;
    private LocationFragment mLocationFragment;
    private LocationManager mLocationManager;
    public static double mMylocationLatitude, mMyLocationLongitude;
    private static final int LOCATION_REQUEST = 1340;
    private Drawable mDrawableRight;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };
    private boolean isDenied;
    private boolean firstLocationChange=false;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;


    private GoogleApiClient mGoogleApiClient;

    public LocationSearchFragment() {
        // Required empty public constructor
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_location_search, container, false);


        mDeCodeToIndex = new HashMap<String, Integer>();
        mDeCodeToIndex.put("5 mi", 5);
        mDeCodeToIndex.put("10 mi", 10);
        mDeCodeToIndex.put("15 mi", 15);
        mDeCodeToIndex.put("20 mi", 20);
        mDeCodeToIndex.put("25 mi", 25);
        mDeCodeToIndex.put("50 mi", 50);
        mDeCodeToIndex.put("100 mi", 100);
        init(root);
        //requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
        return root;
    }


    private void init(View root) {
        mBtnSearchLocation=(TextView)root.findViewById(R.id.btn_search_location);
        mEtSearchLocation=(EditText)root.findViewById(R.id.et_search_location);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            mDrawableRight = getActivity().getDrawable(R.drawable.close_button);
        } else {
            mDrawableRight = getActivity().getResources().getDrawable(R.drawable.close_button);
        }
       setCrossButton();
        mSpRadius=(Spinner) root.findViewById(R.id.sp_radius);
        mBtnSearchLocation.setOnClickListener(this);
    }

    private void setCrossButton() {
        mEtSearchLocation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                final int DRAWABLE_LEFT = 0;
                final int DRAWABLE_TOP = 1;
                final int DRAWABLE_RIGHT = 2;
                final int DRAWABLE_BOTTOM = 3;
                mEtSearchLocation.setCursorVisible(true);
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (mEtSearchLocation.getCompoundDrawables()[DRAWABLE_RIGHT] != null) {
                        if (event.getRawX() >= (mEtSearchLocation.getRight() - mEtSearchLocation.getCompoundDrawables()[DRAWABLE_RIGHT].getBounds().width())) {
                            mEtSearchLocation.setText("");
                            InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                            if (getActivity()!=null&&getActivity().getCurrentFocus() != null && inputManager != null) {
                                inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                                        InputMethodManager.HIDE_NOT_ALWAYS);
                            }
                            mEtSearchLocation.clearFocus();
                            mEtSearchLocation.setCursorVisible(false);

                            return true;
                        }
                    }
                }
                return false;
            }
        });
        mEtSearchLocation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    mEtSearchLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, mDrawableRight, null);
                } else {

                    mEtSearchLocation.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }



    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    @Override
    public void onClick(View v) {
        InputMethodManager inputManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (getActivity()!=null&&getActivity().getCurrentFocus() != null && inputManager != null) {
            inputManager.hideSoftInputFromWindow(getActivity().getCurrentFocus().getWindowToken(),
                    InputMethodManager.HIDE_NOT_ALWAYS);
            mEtSearchLocation.clearFocus();
            mEtSearchLocation.setCursorVisible(false);
        }
        if(PdlUtils.isInternetOn(getActivity())) {
            String address = mEtSearchLocation.getText().toString().trim();
            int radius = mDeCodeToIndex.get(mSpRadius.getSelectedItem());
            if(!address.equals("")&&address.length()>0) {
                if(mEtSearchLocation!=null&&mSpRadius!=null) {
                    mEtSearchLocation.setText("");
                    mSpRadius.setSelection(0);
                    mEtSearchLocation.clearFocus();
                    mEtSearchLocation.setCursorVisible(false);
                }
                mCommonHeaderFragment = new CommonHeaderFragment();
                mLocationFragment = new LocationFragment();
                mLocationFragment.setFromSearch(true);
                mLocationFragment.setSearchValues(radius, address);
                mCommonHeaderFragment.setTitle("PSC Location Results");
                mCommonHeaderFragment.changeToSearchIcon(false);
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.add(R.id.header_fragment, mCommonHeaderFragment, "location_search").add(R.id.content_fragment, mLocationFragment, "location").addToBackStack(null).commit();
            }else{

                if(mGoogleApiClient!=null) {



                    if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity().getApplicationContext(), getActivity())) {
                        if(!isDenied) {
                            fetchLocationData();
                            firstLocationChange = true;
                        }
                    } else {
                        requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
                    }

                    if(PdlUtils.isLocationEnabled(getActivity())) {
                        if(mEtSearchLocation!=null&&mSpRadius!=null) {
                            mEtSearchLocation.setText("");
                            mSpRadius.setSelection(0);
                            mEtSearchLocation.clearFocus();
                            mEtSearchLocation.setCursorVisible(false);
                        }

                    }
                    /*else{
                        if(mGoogleApiClient!=null&&PdlUtils.isLocationEnabled(getActivity())) {
                           PdlUtils.createOKAlert(getActivity(),"","GPS did not warm up, Please tap again.");
                        }
                    }*/
                }
            }

        }else{
            PdlUtils.createOKAlert(getActivity(),"No Internet Connection","Please check your Internet connectivity and try again.");
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                    .addApi(LocationServices.API)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .build();
        }



     /*   if(getActivity()!=null) {
            mLocationManager = (LocationManager) getActivity().getSystemService(getActivity().LOCATION_SERVICE);
        }
        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity().getApplicationContext(), getActivity())) {
            fetchLocationData();
        } else {
            requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
        }*/

    }
       private void fetchLocationData()
    {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

       if(mGoogleApiClient!=null&&PdlUtils.isLocationEnabled(getActivity())) {
           mGoogleApiClient.connect();
       }
        else{
           if(mGoogleApiClient!=null){
               turnGPSOn();
               mGoogleApiClient.connect();
           }
       }

    }
    public void turnGPSOn()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        builder.setCancelable(false);
        builder.setMessage("Unable to get location data. Please enable GPS or enter zip / address and try again.\nDo you want to enable GPS?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

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

    public  boolean checkPermission(String strPermission,Context _c,Activity _a){
        int result = ContextCompat.checkSelfPermission(_c, strPermission);

        if (result == PackageManager.PERMISSION_GRANTED){
            isDenied=false;
            return true;

        } else if(result == PackageManager.PERMISSION_DENIED) {
            PdlUtils.createOKAlert(getActivity(),"Allow Location","Allow to access  your device location in settings",new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(i);
                }
            });
            isDenied=true;
            return true;

        }
        else{
            return false;
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        if(firstLocationChange) {
            int radius = mDeCodeToIndex.get(mSpRadius.getSelectedItem());
            mMylocationLatitude = location.getLatitude();
            mMyLocationLongitude = location.getLongitude();
            mCommonHeaderFragment = new CommonHeaderFragment();
            mLocationFragment = new LocationFragment();
            mLocationFragment.setFromSearch(true);
            mLocationFragment.setSearchGPSValues(radius, mMylocationLatitude, mMyLocationLongitude);
            mCommonHeaderFragment.setTitle("PSC Location Results");
            mCommonHeaderFragment.changeToSearchIcon(false);
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.add(R.id.header_fragment, mCommonHeaderFragment, "location_search").add(R.id.content_fragment, mLocationFragment, "location").addToBackStack(null).commit();
            firstLocationChange=false;
        }
    }



    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e("","CONNECTION FAILED");

    }
    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
         //  Toast.makeText(getActivity().getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
      }
    }

    @Override
    public void onStart() {
        super.onStart();



    }

    @Override
    public void onStop() {
     /*   if(mGoogleApiClient != null)
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
*/
        // only stop if it's connected, otherwise we crash
        if (mGoogleApiClient != null) {
            mGoogleApiClient.disconnect();
         //   turnGPSOff();
        }
        super.onStop();

    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            mMylocationLatitude=mCurrentLocation.getLatitude();
            mMyLocationLongitude=mCurrentLocation.getLongitude();
        /*    try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(getActivity(), Locale.getDefault());

                addresses = geocoder.getFromLocation(mMylocationLatitude, mMyLocationLongitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                if(mEtSearchLocation!=null){
                    mEtSearchLocation.setText(address);
                }

            } catch (IOException e) {
                Log.e("Geocoder",""+e);
            }*/
        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, LocationSearchFragment.this);
    }
    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getActivity().getApplicationContext(),"Permission Denied, You cannot access location data.",Toast.LENGTH_LONG).show();

                }
                break;

        }
    }
}
