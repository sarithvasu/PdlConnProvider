package com.effone.pdlconnprovider.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.effone.pdlconnprovider.R;
import com.effone.pdlconnprovider.common.PdlUtils;
import com.effone.pdlconnprovider.db.SelectDbHelper;
import com.effone.pdlconnprovider.model.DisplayContact;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Locale;


public class ContactFragment extends Fragment implements  GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks, LocationListener {

    //private ImageView mIvMapIcon;

    private TextView mTvGeneralInquiries, mTvGeneralInquiriesToll, mTvBillingInquiries, mTvFax, mTvEmail, mTvLabAddress,mTvLabAddressWithICon;
    private SelectDbHelper mSelectDbHelper;
    private ArrayList<DisplayContact> mDisplayContacts;
    private GoogleApiClient mGoogleApiClient;
    public  double mMylocationLatitude, mMyLocationLongitude;
    private LocationRequest mLocationRequest;
    private long UPDATE_INTERVAL = 2 * 1000;  /* 10 secs */
    private long FASTEST_INTERVAL = 2000;
    private boolean isDenied;
    private static final int PERMISSION_REQUEST_CODE_LOCATION = 1;
    public ContactFragment() {
        // Required empty public constructor
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


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_contact, container, false);
        mSelectDbHelper = new SelectDbHelper(getActivity());
        init(root);
      //  requestPermission(Manifest.permission.ACCESS_FINE_LOCATION, PERMISSION_REQUEST_CODE_LOCATION, getActivity().getApplicationContext(), getActivity());
        return root;

    }

    private void init(View root) {
        mTvGeneralInquiries = (TextView) root.findViewById(R.id.tv_g_i_phone);
        mTvGeneralInquiriesToll = (TextView) root.findViewById(R.id.tv_g_i_t_phone);
        mTvBillingInquiries = (TextView) root.findViewById(R.id.tv_bill_phone);
        mTvFax = (TextView) root.findViewById(R.id.tv_fax_in_contact);
        mTvEmail = (TextView) root.findViewById(R.id.tv_email_in_contact);
        mTvLabAddress = (TextView) root.findViewById(R.id.tv_address_in_contact);
        mTvLabAddressWithICon = (TextView) root.findViewById(R.id.tv_lab_address);
        //mIvMapIcon= (ImageView) root.findViewById(R.id.iv_mapIcon);


        mDisplayContacts=mSelectDbHelper.getContact();
        if(mDisplayContacts.size()>0) {
            mTvGeneralInquiries.setText(mDisplayContacts.get(0).title + ": " + mDisplayContacts.get(0).title_text);
            mTvGeneralInquiriesToll.setText(mDisplayContacts.get(1).title + ": " + mDisplayContacts.get(1).title_text);
            mTvBillingInquiries.setText(mDisplayContacts.get(2).title + ": " + mDisplayContacts.get(2).title_text + "\n");
            mTvFax.setText(mDisplayContacts.get(3).title_text + "\n");
            mTvEmail.setText( mDisplayContacts.get(4).title_text + "\n");
            mTvLabAddress.setText(mDisplayContacts.get(5).title_text);
        }
        mTvLabAddressWithICon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(PdlUtils.isInternetOn(getActivity())) {
                    if (PdlUtils.isInternetOn(getActivity())) {
                        if (checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getActivity().getApplicationContext(), getActivity())) {
                            if(!isDenied) {
                                fetchLocationData();
                            }
                        }

                    } else {
                        PdlUtils.createOKAlert(getActivity(),"", "Please check your Internet connectivity and try again.");
                    }
                }
                else{
                    PdlUtils.createOKAlert(getActivity(),"","Please check your Internet connectivity and try again.");
                }
            }
        });
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
    public  boolean checkPermission(String strPermission, Context _c, Activity _a) {
        int result = ContextCompat.checkSelfPermission(_c, strPermission);
        if (result == PackageManager.PERMISSION_GRANTED) {
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
        else {
            return false;
        }

    }
    public static void requestPermission(String strPermission, int perCode, Context _c, Activity _a) {

     if (ActivityCompat.shouldShowRequestPermissionRationale(_a, strPermission)) {
          // Toast.makeText(getActivity().getApplicationContext(), "GPS permission allows us to access location data. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show();
       } else {

            ActivityCompat.requestPermissions(_a, new String[]{strPermission}, perCode);
       }
    }
    private void fetchLocationData() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }

        if (mGoogleApiClient != null && PdlUtils.isLocationEnabled(getActivity())) {
            mGoogleApiClient.connect();
           // if(mMylocationLatitude!=0.0f&&mMyLocationLongitude!=0.0f) {
                String uri = String.format(Locale.ENGLISH, "http://maps.google.com/maps?saddr=%f,%f(%s)&daddr=%s", mMylocationLatitude, mMyLocationLongitude, "My Location",  mDisplayContacts.get(5).title_text);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
         //   }
           // else if(mGoogleApiClient!=null&&PdlUtils.isLocationEnabled(getActivity())) {
           //     Toast.makeText(getActivity(), "GPS did not warm up, Please tap again.", Toast.LENGTH_LONG).show();
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
        AlertDialog alert = builder.create();
        alert.show();

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
    protected void startLocationUpdates() {
        // Create the location request
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL)
                .setFastestInterval(FASTEST_INTERVAL);
        // Request location updates
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,
                mLocationRequest, ContactFragment.this);
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
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }
}
