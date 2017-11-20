package com.effone.pdlconnprovider.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

/**
 * Created by sarith.vasu on 16-05-2016.
 */
public class DataBaseHelper extends SQLiteOpenHelper {


    public static String DB_NAME = "PDL_Provider.db";
    /*table names*/
    private static String TABLE_LOCATION = "Location";
    private static String TABLE_LOCATION_WORK_HOURS = "LocationWorkHours";



    private static int DB_VERSION = 1;
    private static String DB_PATH = "";
    // column names

    private Context mContext;

    public DataBaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
        this.mContext = context;
        DB_PATH = "/data/data/" + mContext.getPackageName() + "/" + "databases/";

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void SetUpAndImportDataBase() {
        try {
            // Creates a empty database on the system and rewrites it with your own database.
            this.getWritableDatabase();
            copyDataBase();
            // We haver done with initial setup. Now save it to the prefereance.

        } catch (IOException ioe) {
            throw new Error("Unable to create database");
        }
    }

    private void copyDataBase() throws IOException {

        //Open your local db as the input stream
        InputStream myInput = mContext.getAssets().open(DB_NAME);

        // Path to the just created empty db
        String outFileName = DB_PATH + DB_NAME;

        //Open the empty db as the output stream
        OutputStream myOutput = new FileOutputStream(outFileName);

        //transfer bytes from the inputfile to the outputfile
        byte[] buffer = new byte[1024];
        int length;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        //Close the streams
        myOutput.flush();
        myOutput.close();
        myInput.close();

    }

    protected void CloseDataBaseConnection(SQLiteDatabase db) {
        if (db != null) {
            db.close();
        }
    }
    protected SQLiteDatabase OpenDataBase() {
        SQLiteDatabase db = null;

        try {
            String myPath = DB_PATH + DB_NAME;

            File file = new File(myPath);
            boolean a = file.exists();
            boolean b = file.isDirectory();

            if (a && !b) {
                db = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
                db.execSQL("PRAGMA foreign_keys = ON;");


            }

        } catch (SQLiteException e) {
            db = null;
            e.printStackTrace();
        }

        return db;
    }
}
