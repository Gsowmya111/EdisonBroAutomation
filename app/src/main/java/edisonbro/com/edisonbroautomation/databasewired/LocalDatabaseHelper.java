package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: LocalDatabaseHelper.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database Adapter class for Local table .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


import java.io.IOException;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class LocalDatabaseHelper extends SQLiteOpenHelper{

	public static SQLiteDatabase sdb=null;
	public static Context mcontext;
	private static final int DATABASE_VERSION = 3;
    private static final String DATABASE_NAME ="LocalDatabase.db";
    private static final String TABLE_NAME="HouseTable";
    private static final String TABLE_NAME2="SampleTable";
	private static final String TABLE_NAME3="Table";
    
    public static final String SNO="sno";
    public static final String HOUSE_NO="hn";
    public static final String HOUSE_NAME="hna";
    public static final String LOGIN_ACCESS="la";
    public static final String SETTINGS="sett";
    public static final String USER_NAME="un";
    public static final String PASSWORD="ps";
    public static final String APP_LOCK="aplk";
    public static final String APP_PWD="appwd";
   
    
    public static final String DATA1="ea";//logintype
    public static final String DATA2="eb";
    public static final String DATA3="ec";
    public static final String DATA4="ed";
    public static final String DATA5="ee";
    public static final String DATA6="ef";
    public static final String DATA7="eg";
    public static final String DATA8="eh";
    public static final String DATA9="ei";
    public static final String DATA10="ej";

    private static String DATABASE_PATH=null;
    
    public static final String IDNO="sno";
    public static final String NAME="hn";
    public static final String VALUE="hna";
    
    private static final String TAG1="Localdbhelper - ";

    private  String CREATE_TABLE="CREATE TABLE "+TABLE_NAME+"("+SNO+" INTEGER PRIMARY KEY, "
    		+HOUSE_NO+"	INTEGER ,"+HOUSE_NAME+" TEXT ,"+LOGIN_ACCESS+" TEXT ,"+SETTINGS+" TEXT ,"
    		+USER_NAME+" TEXT ,"+PASSWORD+" TEXT ,"+APP_LOCK+" TEXT ,"+APP_PWD+" TEXT ,"
    		+DATA1+" TEXT ,"+DATA2+" TEXT ,"+DATA3+" TEXT ,"+DATA4+" TEXT ,"+DATA5+" TEXT ,"
    		+DATA6+" TEXT ,"+DATA7+" TEXT ,"+DATA8+" TEXT ,"+DATA9+" TEXT ,"+DATA10+" TEXT "+");";	
    
    private  String CREATE_TABLE2="CREATE TABLE "+TABLE_NAME2+"("+IDNO+" INTEGER PRIMARY KEY,"+NAME+" TEXT ,"+VALUE+" TEXT "+");";



	private  String CREATE_TABLE3="CREATE TABLE "+TABLE_NAME2+"("+IDNO+" INTEGER PRIMARY KEY,"+NAME+" TEXT ,"+VALUE+" TEXT "+");";

	public LocalDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mcontext=context;
		DATABASE_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
		StaticVariabes_div.log("Local DATABASE"+"constructor Called", TAG1);		
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(CREATE_TABLE);
		db.execSQL(CREATE_TABLE2);
		StaticVariabes_div.log("Local DATABASE Created", TAG1);	
						
	}



	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
		onCreate(db);
	}
	
	
	public void opendb() {
		try
		{
			String DB_PATH = DATABASE_PATH+ DATABASE_NAME;
			sdb = this.getWritableDatabase();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public boolean isDbExists() throws IOException {
		SQLiteDatabase sldb = null;
		try {
			String DB_PATH = DATABASE_PATH+ DATABASE_NAME;
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
			if (sdb != null) {
				sdb.close();
				StaticVariabes_div.log("Local DATABASE"+"closed", TAG1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
//**************************************TABLE2*****************************************

	public long insert(String nam,String val)
	{
		ContentValues cv=new ContentValues();					
		cv.put(NAME, nam);
		cv.put(VALUE, val);
		return sdb.insert(TABLE_NAME2, null, cv);
	}
	
public Cursor fetch_Applockdetails(int idno) throws SQLException {
		
		Cursor mCursor = sdb.query(true, TABLE_NAME2, new String[] {
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
   sdb.update(TABLE_NAME2, args, IDNO + "=" + idnum, null);
}

public boolean updateApplockPwd(int idnum,String Pwd){				
	ContentValues args = new ContentValues();
    args.put(VALUE, Pwd);
   int m=sdb.update(TABLE_NAME2, args, IDNO + "=" + idnum, null);
   if(m>0){
	   return true;  
   }
return false;
}

	public int getCount_Applock() {
		final int DEFAULT_BASE = 0;
		Cursor mCursor = sdb.rawQuery(
				"SELECT Count(*) FROM "+TABLE_NAME2, null);
		if (mCursor.getCount() == 0)
			return DEFAULT_BASE;
		else
			mCursor.moveToFirst();
		return (int) mCursor.getLong(0);
	}

	//*************************DATABASE OPERATIONS*****************************************

	/*public long insert_hp(int HouseNo,String HouseName,String username,String password,String usertype)
	{
		ContentValues cv=new ContentValues();

		cv.put(HOUSE_NO, HouseNo);
		cv.put(HOUSE_NAME, HouseName);
		cv.put(LOGIN_ACCESS,"AU");
		cv.put(SETTINGS, "false");

		cv.put(USER_NAME, username);
		cv.put(PASSWORD, password);
		cv.put(APP_LOCK, "false");
		cv.put(APP_PWD, "1234");
		cv.put(DATA1, usertype);//UserType
		cv.put(DATA2, "Default");
		cv.put(DATA3, "Default");
		cv.put(DATA4, "Default");
		cv.put(DATA5, "Default");
		cv.put(DATA6, "Default");
		cv.put(DATA7, "Default");
		cv.put(DATA8, "Default");
		cv.put(DATA9, "Default");
		cv.put(DATA10, "Default");

		return sdb.insert(TABLE_NAME, null, cv);
	}*/

	public long insert_hp(int HouseNo,String HouseName,String username,byte[] password,String usertype)
	{
		ContentValues cv=new ContentValues();

		cv.put(HOUSE_NO, HouseNo);
		cv.put(HOUSE_NAME, HouseName);
		cv.put(LOGIN_ACCESS,"AU");
		cv.put(SETTINGS, "false");

		cv.put(USER_NAME, username);
		//cv.put(PASSWORD, password);
		cv.put(PASSWORD, password);
		cv.put(APP_LOCK, "false");
		cv.put(APP_PWD, "1234");
		cv.put(DATA1, usertype);//UserType
		cv.put(DATA2, "Default");
		cv.put(DATA3, "Default");
		cv.put(DATA4, "Default");
		cv.put(DATA5, "Default");
		cv.put(DATA6, "Default");
		cv.put(DATA7, "Default");
		cv.put(DATA8, "Default");
		cv.put(DATA9, "Default");
		cv.put(DATA10, "Default");

		return sdb.insert(TABLE_NAME, null, cv);
	}

	//Insert Records
	public long insert(int HouseNo,String HouseName)
	{
		ContentValues cv=new ContentValues();
		
	
		
		cv.put(HOUSE_NO, HouseNo);
		cv.put(HOUSE_NAME, HouseName);
		cv.put(LOGIN_ACCESS,"AU");
		cv.put(SETTINGS, "false");
		
		cv.put(USER_NAME, "Default");
		cv.put(PASSWORD, "Default");
		cv.put(APP_LOCK, "false");
		cv.put(APP_PWD, "1234");
		
		cv.put(DATA1, "Default");
		cv.put(DATA2, "Default");
		cv.put(DATA3, "Default");
		cv.put(DATA4, "Default");
		cv.put(DATA5, "Default");
		cv.put(DATA6, "Default");
		cv.put(DATA7, "Default");
		cv.put(DATA8, "Default");
		cv.put(DATA9, "Default");
		cv.put(DATA10, "Default");

		
		return sdb.insert(TABLE_NAME, null, cv);
	}
	
	//Update Records
	public boolean update(int HouseNo,String HouseName)
	{
		ContentValues cv=new ContentValues();
		cv.put(HOUSE_NAME, HouseName);
		
		return sdb.update(TABLE_NAME, cv, HOUSE_NO+"="+HouseNo, null)>0;
	}


	//Update Records
	public boolean update_login_account(String HouseNo,String usr,byte[] pwd,String logdusrtyp)
	{
		HouseNo="'"+HouseNo+"'";

		ContentValues cv=new ContentValues();
		cv.put(DATA1, logdusrtyp);
		cv.put(USER_NAME, usr);
		//cv.put(PASSWORD, pwd);
		cv.put(PASSWORD, pwd);

		//return sdb.update(TABLE_NAME, cv, HOUSE_NAME+"="+HOUSE_NAME, null)>0;
		return sdb.update(TABLE_NAME, cv, HOUSE_NO+"="+HouseNo, null)>0;
	}

	public boolean update_login_usertype(String HouseNo,String logdusrtyp)
	{
		HouseNo="'"+HouseNo+"'";

		ContentValues cv=new ContentValues();
		cv.put(DATA1, logdusrtyp);

		return sdb.update(TABLE_NAME, cv, HOUSE_NO+"="+HouseNo, null)>0;
	}

	//Delete Record
	public boolean delete(int HouseNo) 
	{
		return sdb.delete(TABLE_NAME, HOUSE_NO+"="+HouseNo, null) > 0;
	}
	//Delete Record housename
	public boolean delete_hom(String HouseName)
	{
		HouseName="'"+HouseName+"'";
		return sdb.delete(TABLE_NAME, HOUSE_NAME+"="+HouseName, null) > 0;
	}
	
	//Fetch Records
	public Cursor fetchAllRecords() {
		return sdb.query(true,TABLE_NAME, new String[] {HOUSE_NO,HOUSE_NAME},null, null, null, null, null, null);
	}
	
	public int fetchMaxHouseNo()	{
		int HouseNo = 0;
		Cursor mCursor =null;
		try
		{
			mCursor= sdb.query(TABLE_NAME, new String[] { "MAX("+ HOUSE_NO + ")" }, null, null, null, null, null);
		
			if (mCursor.getCount() == 0){
				return HouseNo;
			}
			else{
				mCursor.moveToFirst();
				HouseNo=mCursor.getInt(0);
			}
		}catch(Exception e)	{
			e.printStackTrace();
		}
		return HouseNo;
	}
	
	public boolean HouseNameExists(String Housename){

		boolean isExists=false; 
		try
		{
			Cursor mcursor =fetchAllRecords();
			if(mcursor.getCount()==0){
				isExists=false; 
			}else {
				String houseName=null;
				mcursor.moveToFirst();
				do{
					houseName=mcursor.getString(mcursor.getColumnIndex(HOUSE_NAME));
					StaticVariabes_div.log("Houses"+"House Name : "+houseName, TAG1);	
					if(houseName.equalsIgnoreCase(Housename)){
						isExists=true; 
						StaticVariabes_div.log("Exists"+"House Already Exists", TAG1);
					}
				}while(mcursor.moveToNext());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		StaticVariabes_div.log("Check"+"is House Exists ?"+isExists, TAG1);
		return isExists;
	}


	
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	public int getBasehouse() {
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
	
	
	
	public Cursor fetch_housename(String houseno) throws SQLException {
		houseno="'"+houseno+"'";
		Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
				SNO,HOUSE_NO,HOUSE_NAME,LOGIN_ACCESS,SETTINGS,USER_NAME,PASSWORD,APP_LOCK,APP_PWD,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10},
				HOUSE_NO + "=" + houseno, null, null, null, null, null);

		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetch_AppPwd(int sno) throws SQLException {
		
		Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
				SNO,HOUSE_NO,HOUSE_NAME,LOGIN_ACCESS,SETTINGS,USER_NAME,PASSWORD,APP_LOCK,APP_PWD,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10},
				SNO + "=" + sno, null, null, null, null, null);

		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetch_logintype(String houseno) throws SQLException {
		houseno="'"+houseno+"'";
		Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
				SNO,HOUSE_NO,HOUSE_NAME,LOGIN_ACCESS,SETTINGS,USER_NAME,PASSWORD,APP_LOCK,APP_PWD,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10},
				HOUSE_NO + "=" + houseno, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetch_LOGINACCESS(String HOUSENAME) throws SQLException {
		HOUSENAME="'"+HOUSENAME+"'";
		Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
				SNO,HOUSE_NO,HOUSE_NAME,LOGIN_ACCESS,SETTINGS,USER_NAME,PASSWORD,APP_LOCK,APP_PWD,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10},
				HOUSE_NAME + "=" + HOUSENAME, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public void updatevalue(String houseno,String value){
		houseno="'"+houseno+"'";
		
		ContentValues args = new ContentValues();
	    args.put(SETTINGS, value);
	    sdb.update(TABLE_NAME, args, HOUSE_NO + "=" + houseno, null);
	}
	
	
	public boolean updateuserpass(String houseno,String usr,byte[] pwd){
		houseno="'"+houseno+"'";
		
		ContentValues args = new ContentValues();
	    args.put(USER_NAME, usr);
		args.put(PASSWORD, pwd);
	   //args.put(PASSWORD, pwd);
	   return  sdb.update(TABLE_NAME, args, HOUSE_NO + "=" + houseno, null)>0;
	}
	
	public boolean deleteuserpass(String houseno,String usr,String pwd){
		houseno="'"+houseno+"'";		
		return sdb.delete(TABLE_NAME, HOUSE_NO + "=" + houseno, null)>0;
	}
	
	public boolean updateLoginAccess(String houseno,String login){
		houseno="'"+houseno+"'";
		
		ContentValues args = new ContentValues();
	   args.put(LOGIN_ACCESS, login);
	  return sdb.update(TABLE_NAME, args, HOUSE_NO + "=" + houseno, null)>0;
	}
	
	public void updateAppLock(int snum,String lock){				
		ContentValues args = new ContentValues();
	    args.put(APP_LOCK, lock);
	   sdb.update(TABLE_NAME, args, SNO + "=" + snum, null);
	}
	
	public void updateAppPwd(int snum,String Pwd){				
		ContentValues args = new ContentValues();
	    args.put(APP_PWD, Pwd);
	   sdb.update(TABLE_NAME, args, SNO + "=" + snum, null);
	}
	
	public Cursor fetch_value(String houseno,String name) throws SQLException {
		houseno="'"+houseno+"'";
		name="'"+name+"'";
		Cursor mCursor = sdb.query(true, TABLE_NAME, new String[] {
				SNO,HOUSE_NO,HOUSE_NAME,LOGIN_ACCESS,SETTINGS,USER_NAME,PASSWORD,APP_LOCK,APP_PWD,DATA1,DATA2,DATA3,DATA4,DATA5,DATA6,DATA7,DATA8,DATA9,DATA10},
				HOUSE_NO + "="+ houseno+" AND "+SETTINGS + "="+name, null, null, null, null, null);
		/*if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;*/
		
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
}
