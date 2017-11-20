package com.effone.pdlconnprovider.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.effone.pdlconnprovider.fragment.SettingsFragment;

/**
 * Created by sarith.vasu on 29-02-2016.
 */
public class AppPreferences {
    private SharedPreferences sharedPrefs;
    private SharedPreferences.Editor prefsEditor;
    private String APP_SHARED_PREFS	= AppPreferences.class.getSimpleName();
    private String SETTING_TEST_CAT_DATA= "setting_test_cat_data";
    private String SETTING_LOCATION_SERVICE="setting_location_service";
    private String SETTINGS_PUSH_NOTIFICATION="settings_push_notification";
    private String FIRST_TIME_LAUNCH="first_time_launch";
    private String SERVER_DATE="server_date";


    public AppPreferences(Context context) {
        this.sharedPrefs = context.getSharedPreferences(APP_SHARED_PREFS, Context.MODE_PRIVATE);
        this.prefsEditor = sharedPrefs.edit();
    }

    public String getSERVER_DATE() {
        return sharedPrefs.getString(SERVER_DATE, "");
    }
    public void saveSERVER_DATE(String server_date) {
        prefsEditor.putString(SERVER_DATE, server_date);
        prefsEditor.commit();
    }
    public String getSETTING_TEST_CAT_DATA() {
        return sharedPrefs.getString(SETTING_TEST_CAT_DATA, SettingsFragment.SETTING_YES);
    }
    public void saveSETTING_TEST_CAT_DATA(String setting_test_cat_data) {
        prefsEditor.putString(SETTING_TEST_CAT_DATA, setting_test_cat_data);
        prefsEditor.commit();
    }
    public boolean getFIRST_TIME_LAUNCH() {
        return sharedPrefs.getBoolean(FIRST_TIME_LAUNCH, true);
    }
    public void saveFIRST_TIME_LAUNCH(boolean first_time_launch ) {
        prefsEditor.putBoolean(FIRST_TIME_LAUNCH, first_time_launch);
        prefsEditor.commit();
    }
    public String getSETTING_LOCATION_SERVICE() {
        return sharedPrefs.getString(SETTING_LOCATION_SERVICE, SettingsFragment.SETTING_YES);
    }
    public void saveSETTING_LOCATION_SERVICE(String setting_location_service) {
        prefsEditor.putString(SETTING_LOCATION_SERVICE, setting_location_service);
        prefsEditor.commit();
    }
    public String getSETTINGS_PUSH_NOTIFICATION() {
        return sharedPrefs.getString(SETTINGS_PUSH_NOTIFICATION, SettingsFragment.SETTING_YES);
    }
    public void saveSETTINGS_PUSH_NOTIFICATION(String settings_push_notification) {
        prefsEditor.putString(SETTINGS_PUSH_NOTIFICATION, settings_push_notification);
        prefsEditor.commit();
    }


}
