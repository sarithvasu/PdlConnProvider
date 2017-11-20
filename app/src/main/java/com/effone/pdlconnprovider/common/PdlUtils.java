package com.effone.pdlconnprovider.common;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.effone.pdlconnprovider.R;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by sarith.vasu on 16-05-2016.
 */
public class PdlUtils {

    public static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }
    public static boolean isInternetOn(Context context) {

        // get Connectivity Manager object to check connection
        ConnectivityManager connec = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        // Check for network connections

        if (connec != null) {
            NetworkInfo nInfo = connec.getActiveNetworkInfo();

            if (nInfo != null && nInfo.getState() == NetworkInfo.State.CONNECTED) {
                return true;
            }
        }


        return false;

        /* for future use*/
      /*  NetworkInfo activeNetwork = connec.getActiveNetworkInfo();
        if (activeNetwork != null) { // connected to the internet
            if (activeNetwork.getType() == ConnectivityManager.TYPE_WIFI) {
                // connected to wifi
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            } else if (activeNetwork.getType() == ConnectivityManager.TYPE_MOBILE) {
                // connected to the mobile provider's data plan
                Toast.makeText(context, activeNetwork.getTypeName(), Toast.LENGTH_SHORT).show();
            }
        } else {
            // not connected to the internet
        }*/

    }

    public static void createOKAlert(Context context,String title,String msg){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        if(!title.equals("")) {
            builder.setTitle(title);

        }
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });

        AlertDialog alert = builder.show();
        TextView messageText = (TextView)alert.findViewById(android.R.id.message);
        messageText.setGravity(Gravity.CENTER);
        alert.show();
    }
    public static void createOKAlert(Context context,String title,String msg ,DialogInterface.OnClickListener listener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context,AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        if(!title.equals("")) {
            builder.setTitle(title);

        }
        builder.setMessage(msg)
                .setCancelable(false)
                .setNeutralButton("OK", listener);

        AlertDialog alert = builder.show();
        alert.show();
    }

    public static String formatDateForSync(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
    public static ProgressDialog createProgressDialog(Context mContext) {
        ProgressDialog dialog = new ProgressDialog(mContext);
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
    //    dialog.setContentView(R.layout.common_progressbar);
        // dialog.setMessage(Message);
        return dialog;
    }
    public static int getDaysCount(String old_date_String,String new_date_String)
    {


        SimpleDateFormat dateFormat = new SimpleDateFormat(AppContant.JSON_DATE_FORMAT, Locale.getDefault());

        Date Created_convertedDate=null,Expire_CovertedDate=null,todayWithZeroTime=null;
        try
        {
            if(!old_date_String.equals("")&&!new_date_String.equals("")) {
                Created_convertedDate = dateFormat.parse(old_date_String);
                Expire_CovertedDate = dateFormat.parse(new_date_String);

                Date today = new Date();

                todayWithZeroTime = dateFormat.parse(dateFormat.format(today));
            }
            else{
                return 0;
            }
        } catch (ParseException e)
        {
            e.printStackTrace();
            return 0;
        }


        int c_year=0,c_month=0,c_day=0;

        if(Created_convertedDate.after(todayWithZeroTime))
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(Created_convertedDate);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);

        }
        else
        {
            Calendar c_cal = Calendar.getInstance();
            c_cal.setTime(todayWithZeroTime);

            c_year = c_cal.get(Calendar.YEAR);
            c_month = c_cal.get(Calendar.MONTH);
            c_day = c_cal.get(Calendar.DAY_OF_MONTH);
        }
        Calendar e_cal = Calendar.getInstance();
        e_cal.setTime(Expire_CovertedDate);

        int e_year = e_cal.get(Calendar.YEAR);
        int e_month = e_cal.get(Calendar.MONTH);
        int e_day = e_cal.get(Calendar.DAY_OF_MONTH);

        Calendar date1 = Calendar.getInstance();
        Calendar date2 = Calendar.getInstance();

        date1.clear();
        date1.set(c_year, c_month, c_day);
        date2.clear();
        date2.set(e_year, e_month, e_day);

        long diff = date2.getTimeInMillis() - date1.getTimeInMillis();

        float dayCount = (float) diff / (24 * 60 * 60 * 1000);


        return (int) dayCount;
    }
    public static boolean isLocationEnabled(Context context) {
        int locationMode = 0;
        String locationProviders;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            try {
                locationMode = android.provider.Settings.Secure.getInt(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_MODE);

            } catch (android.provider.Settings.SettingNotFoundException e) {
                e.printStackTrace();
            }

            return locationMode != android.provider.Settings.Secure.LOCATION_MODE_OFF;

        }else{
            locationProviders = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
            return !TextUtils.isEmpty(locationProviders);
        }


    }

}
