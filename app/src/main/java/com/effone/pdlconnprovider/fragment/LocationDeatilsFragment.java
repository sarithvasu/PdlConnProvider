package com.effone.pdlconnprovider.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.LocationDetail;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class LocationDeatilsFragment extends Fragment implements View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER

    private TextView mTvLocationName;
    private TextView mTvLocationAddress1;
    private TextView mTvLocationAddress2;
    private TextView mTvLocationPhone;
    private TextView mTvLocationFax;
    private TextView mTvLocationWorkHours;
    private TextView mTvLocationGetDirection;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    private static final int REQUEST_INTERNET = 200;

    private LocationDetail mLocationDetail;
    private SelectDbHelper mSelectDbHelper;
    public static double mMylocationLatitude, mMyLocationLongitude;
    private int mLocationId;
    private LocationManager mLocationManager;
    private boolean isDenied;
    private static final int LOCATION_REQUEST = 1340;
    private static final String[] LOCATION_PERMS = {
            Manifest.permission.ACCESS_FINE_LOCATION
    };

    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;

    public LocationDeatilsFragment() {
        // Required empty public constructor
    }

    public void setLocationId(int locationId) {
        mLocationId = locationId;
    }

    // TODO: Rename and change types and number of parameters


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
        //  mGoogleApiClient.connect();
      /*  if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);


            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }*/


    }

    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
            //Toast.makeText(getActivity().getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
        } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PERMISSION_REQUEST_CODE_LOCATION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    fetchLocationData();

                } else {

                    Toast.makeText(getActivity().getApplicationContext(), "Permission Denied, You cannot access location data.", Toast.LENGTH_LONG).show();

                }
                break;

        }
        /*if (requestCode == REQUEST_INTERNET) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchLocationData();

            }else if (grantResults[0] == PackageManager.PERMISSION_DENIED){
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
                    //Show an explanation to the user *asynchronously*
                    android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(getActivity());
                    builder.setMessage("This permission is important to get direction.")
                            .setTitle("Important permission required");

                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
                        }
                    });
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_INTERNET);
                }else{
                    //Never ask again and handle your app without permission.
                }
            }
        }*/
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View root = inflater.inflate(R.layout.fragment_location_deatils, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
     //  requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
        return root;

    }

    private void init(View root) {
        mTvLocationName = (TextView) root.findViewById(R.id.tv_location_name_in_details);
        mTvLocationAddress1 = (TextView) root.findViewById(R.id.tv_address1_in_details);
        mTvLocationAddress2 = (TextView) root.findViewById(R.id.tv_address2_in_details);
        mTvLocationPhone = (TextView) root.findViewById(R.id.tv_phone_in_details);
        mTvLocationFax = (TextView) root.findViewById(R.id.tv_fax_in_details);
        mTvLocationWorkHours = (TextView) root.findViewById(R.id.tv_work_hours_in_details);
        mTvLocationGetDirection = (TextView) root.findViewById(R.id.tv_get_direction);
        mTvLocationGetDirection.setOnClickListener(this);
        mLocationDetail = mSelectDbHelper.getLocationDeatails(mLocationId);
        if (mLocationDetail != null) {
            mTvLocationName.setText(mLocationDetail.locationName);
            mTvLocationAddress1.setText(mLocationDetail.locationAdress1);
            mTvLocationAddress2.setText(mLocationDetail.locationAddress2);
            mTvLocationPhone.setText(mLocationDetail.locationPhone);
            mTvLocationFax.setText(mLocationDetail.locationFax);
            mTvLocationWorkHours.setText(Html.fromHtml(mLocationDetail.locationWorkHours));
            if (mLocationDetail.locationLongitude != 0.0f && mLocationDetail.locationLattitude != 0.f) {
                Log.e("LOCATION", "" + mLocationDetail.locationLattitude + "   " + mLocationDetail.locationLongitude);
                mTvLocationGetDirection.setVisibility(View.VISIBLE);
            } else {
                mTvLocationGetDirection.setVisibility(View.GONE);
            }
        }
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
        if (v.getId() == R.id.tv_get_direction) {
            if (PdlUtils.isInternetOn(getActivity())) {
                if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity().getApplicationContext(), getActivity())) {
                    if (!isDenied) {
                        fetchLocationData();
                    }

                } else {
                    requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
                }

            } else {
                PdlUtils.createOKAlert(getActivity(), "No Internet Connection.", "Unable to contact server.\nPlease check your internet connection");
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

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
    public void onLocationChanged(Location location) {
    /*    mMylocationLatitude=location.getLatitude();
        mMyLocationLongitude=location.getLongitude();*/
    }


    @Override
    public void onResume() {
        super.onResume();
      /*  if(mMylocationLatitude!=0.0f&&mMyLocationLongitude!=0.0f) {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", mMylocationLatitude, mMyLocationLongitude, "My Location", mLocationDetail.locationLattitude, mLocationDetail.locationLongitude, "PDL");
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
        }*/

    }

    private void fetchLocationData() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (mGoogleApiClient != null && PdlUtils.isLocationEnabled(getActivity())) {
            mGoogleApiClient.connect();
            // if(mMylocationLatitude!=0.0f&&mMyLocationLongitude!=0.0f) {
            String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%f,%f (%s)", mMylocationLatitude, mMyLocationLongitude, "My Location", mLocationDetail.locationLattitude, mLocationDetail.locationLongitude, mLocationDetail.locationName);
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
            //  }
            //  else if(mGoogleApiClient!=null&&PdlUtils.isLocationEnabled(getActivity())) {
            //    Toast.makeText(getActivity(), "GPS did not warm up, Please tap again.", Toast.LENGTH_LONG).show();
            // }
        } else {
            if (mGoogleApiClient != null) {
                turnGPSOn();
                mGoogleApiClient.connect();

            }
        }
    }

    public void turnGPSOn() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setCancelable(false);
        builder.setMessage("Unable to get location data. Do you want to enable GPS?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.show();
        TextView messageText = (TextView) alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        alert.show();

    }

    public boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {
            isDenied = false;
            return true;

        } else if (result == PackageManager.PERMISSION_DENIED) {
            PdlUtils.createOKAlert(getActivity(), "Allow Location", "Allow to access  your device location in settings", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent i = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    i.addCategory(Intent.CATEGORY_DEFAULT);
                    i.setData(Uri.parse("package:" + getActivity().getPackageName()));
                    startActivity(i);
                }
            });
            isDenied = true;
            return true;

        } else {
            return false;
        }
    }

    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, LocationDeatilsFragment.this);
    }

    @Override
    public void onConnected(Bundle bundle) {
        Location mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // Note that this can be NULL if last location isn't already known.
        if (mCurrentLocation != null) {
            // Print current location if not null
            mMylocationLatitude = mCurrentLocation.getLatitude();
            mMyLocationLongitude = mCurrentLocation.getLongitude();

        }
        // Begin polling for new location updates.
        startLocationUpdates();
    }

    @Override
    public void onConnectionSuspended(int i) {
        if (i == CAUSE_SERVICE_DISCONNECTED) {
            Toast.makeText(getActivity(), "Disconnected. Please re-connect.", Toast.LENGTH_SHORT).show();
        } else if (i == CAUSE_NETWORK_LOST) {
            Toast.makeText(getActivity(), "Network lost. Please re-connect.", Toast.LENGTH_SHORT).show();
        }

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
