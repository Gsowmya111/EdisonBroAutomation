package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: LocalListArrangeTable.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database class for storing roomnames in local table .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

/**
 * Created by Divya on 7/12/2017.
 */

public class LocalListArrangeTable extends SQLiteOpenHelper {

    private static SQLiteDatabase dbs=null;
    Context mcontext;
    private static final int DATABASE_VERSION = 3;
    private static final String DBS_NAME ="";
    public static  String TABLE_NAME="";


    private static String DBS_PATH=null;

    public static final String IDNO="sno";
    public static final String NAME="hn";
    public static final String VALUE="hna";

    private static final String TAG1="Localdbhelper - ";

    //creating ID's/columns to table
    public static final String ID = "ID";
    public static final String ROOMNAME = "roomname";
    public static final String ROOMNO = "roomno";
    public static final String POSITIONNO = "positionno";
    public static final String IMGNAME= "imgname";


   // private  String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+ID+" INTEGER PRIMARY KEY,"+ROOMNAME+" TEXT ,"+VALUE+" TEXT "+");";

   // String homenam="+ StaticVariabes_div.housename+"";



    private String CREATE_TABLE = "CREATE TABLE " + StaticVariabes_div.housename + "(" + ID + " INTEGER PRIMARY KEY , " +
            ROOMNAME + " TEXT ," + ROOMNO + " TEXT ," + POSITIONNO + " TEXT ," + IMGNAME + " TEXT " + ");";


    public LocalListArrangeTable(Context context,String tablename,String DBS_NAME) {
        super(context, DBS_NAME, null, DATABASE_VERSION);
        TABLE_NAME=tablename;
        DBS_NAME=DBS_NAME;
        mcontext=context;
        DBS_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";

       // StaticVariabes_div.log("Local DATABASE"+"constructor Called", TAG1);

    }

    @Override
    public void onCreate(SQLiteDatabase sdb) {

        sdb.execSQL(CREATE_TABLE);

        StaticVariabes_div.log("Local DATABASE Created", TAG1);

    }



    @Override
    public void onUpgrade(SQLiteDatabase sdb, int oldVersion, int newVersion) {
        sdb.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(dbs);
    }


    public void open() {
        try
        {
           // String DB_PATH = DBS_PATH+ DBS_NAME;
            dbs = this.getWritableDatabase();
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
    }


 /*   public SwbAdapter(Context context) {
        mcontext = context;
        ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
    }

    public void open() {
        try {
            String path = ORIGINAL_PATH + OriginalDataBase;
            StaticVariabes_div.log("Databse Path is " + path, TAG1);
            dbs = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE
                            + SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            if (dbs != null) {
                dbs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    */
    public boolean isDbExists() throws IOException {
        SQLiteDatabase sldb = null;
        try {
            String DB_PATH = DBS_PATH+ DBS_NAME;
            sldb = SQLiteDatabase.openDatabase(DB_PATH, null,
                    SQLiteDatabase.OPEN_READWRITE
                            + SQLiteDatabase.NO_LOCALIZED_COLLATORS);

        } catch (Exception e) {
            e.printStackTrace();
        }

        if (sldb != null)
            sldb.close();

        return sldb != null ? true : false;

    }


    public void close() {
        try {
            if (dbs != null) {
                dbs.close();
                StaticVariabes_div.log("Local DATABASE"+"closed", TAG1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public  boolean insertData(String name, String roomno,String position,String usertype) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues1 = new ContentValues();
        //  contentValues1.put(ID, id);
        contentValues1.put(ROOMNAME, name);
        contentValues1.put(ROOMNO, roomno);
        contentValues1.put(POSITIONNO, position);
        contentValues1.put(IMGNAME, usertype);
        //  long result = db.insertWithOnConflict(TABLE_NAME, null, contentValues1,SQLiteDatabase.CONFLICT_REPLACE);
        boolean result = db.insert(TABLE_NAME, null, contentValues1)>0;
        return  result;
        /*if (result == -1)
            return false;
        else
            return true;*/
    }

    //getting number of rows from table
    public int numberOfRows(String TABLE_NAME){
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db,TABLE_NAME);
        return numRows;
    }
    //getting data based on roomnames
    public List<String> getData1() {
        List<String> arr=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + StaticVariabes_div.housename +" ORDER BY " + POSITIONNO + " ASC",null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(ROOMNAME));

                arr.add(name);
                cursor.moveToNext();
            }
        }

        return arr;
    }

    //ORDER BY " + SQLiteHelper.listDate+ " ASC"

    //getting data based on roomnames
    public List<String> getimagenames() {
        List<String> arrimg=new ArrayList<String>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + StaticVariabes_div.housename +" ORDER BY " + POSITIONNO + " ASC" ,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(IMGNAME));

                arrimg.add(name);
                cursor.moveToNext();
            }
        }

        return arrimg;
    }

    public int[] getposvalues( int size) {
        List<String> arrpos=new ArrayList<String>();
        int poss[]=new int[size];int h=0;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor  cursor = db.rawQuery("select * from " + TABLE_NAME  ,null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                //String name = cursor.getString(cursor.getColumnIndex(POSITIONNO));
                poss[h]=Integer.parseInt(cursor.getString(cursor.getColumnIndex(POSITIONNO)));
                h++;
                // arrpos.add(name);
                cursor.moveToNext();
            }
        }


        return poss;
    }

    public boolean updateroompos_whr_nameis(String roomname,String newpos) {
        SQLiteDatabase db = this.getWritableDatabase();
        boolean status;
        roomname="'"+roomname+"'";
        ContentValues args = new ContentValues();
        args.put(POSITIONNO, newpos);
        status= db.update(TABLE_NAME, args, ROOMNAME + "=" + roomname, null)>0;
        return status;
    }


    public boolean deleteall(){

        SQLiteDatabase db= this.getWritableDatabase();
        boolean del_table=db.delete(TABLE_NAME, null, null)>0;
        return del_table;
    }

//**************************************TABLE2*****************************************
/*

    public long insert(String nam,String val)
    {
        ContentValues cv=new ContentValues();
        cv.put(NAME, nam);
        cv.put(VALUE, val);
        return sdb.insert(TABLE_NAME, null, cv);
    }

    public Cursor fetch_Applockdetails(int idno) throws SQLException {

        Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
                        IDNO,NAME,VALUE},
                IDNO + "=" + idno, null, null, null, null, null);

        if(!(mCursor.getCount()>0))
            return null;
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void updateAppLockEna(int idnum,String lock){
        ContentValues args = new ContentValues();
        args.put(VALUE, lock);
        sdb.update(TABLE_NAME, args, IDNO + "=" + idnum, null);
    }

    public boolean updateApplockPwd(int idnum,String Pwd){
        ContentValues args = new ContentValues();
        args.put(VALUE, Pwd);
        int m=sdb.update(TABLE_NAME, args, IDNO + "=" + idnum, null);
        if(m>0){
            return true;
        }
        return false;
    }
*/

    //*************************DATABASE OPERATIONS*****************************************

    //Insert Records


//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  /*  public int getBasehouse() {
        final int DEFAULT_BASE = 0;
        Cursor mCursor = sdb.rawQuery(
                "SELECT sno FROM HouseTable ORDER BY sno desc limit 1", null);

        if (mCursor.getCount() == 0)
            return DEFAULT_BASE;
        else
            mCursor.moveToFirst();
        return (int) mCursor.getLong(0);
    }

    public int getCount() {
        final int DEFAULT_BASE = 0;
        Cursor mCursor = sdb.rawQuery(
                "SELECT Count(*) FROM HouseTable", null);
        if (mCursor.getCount() == 0)
            return DEFAULT_BASE;
        else
            mCursor.moveToFirst();
        return (int) mCursor.getLong(0);
    }

    public String[] getallhouseno(int count) {

        Cursor mCursor = sdb.rawQuery(
                "SELECT hn FROM HouseTable", null);
        String[] housenoarray = new String[count];
        int i = 0;
        while(mCursor.moveToNext()){
            String uname = mCursor.getString(mCursor.getColumnIndex("hn"));
            housenoarray[i] = uname;
            i++;
        }

        return housenoarray;
    }
*/

}
