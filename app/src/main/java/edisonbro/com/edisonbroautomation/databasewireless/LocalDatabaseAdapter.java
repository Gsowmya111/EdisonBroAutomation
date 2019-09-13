package edisonbro.com.edisonbroautomation.databasewireless;

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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;

//Database Adapter for holding  app information locally
public class LocalDatabaseAdapter extends SQLiteOpenHelper{

	public static SQLiteDatabase sdb=null;
	public static Context mcontext;
	private static final int DATABASE_VERSION = 3;
	private static final String DATABASE_NAME ="LocalDatabase.db";
	private static final String LOCAL_TABLE_NAME="HouseTable";

	public static final String SNO="sno";
	public static final String HOUSE_NO="hn";
	public static final String HOUSE_NAME="hna";
	public static final String LOGIN_ACCESS="la";
	public static final String SETTINGS="sett";
	public static final String USER_NAME="un";
	public static final String PASSWORD="ps";
	public static final String APP_LOCK="aplk";
	public static final String APP_PWD="appwd";

	public static final String DATA1="ea";
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


	//variable holding create table query
	private  String CREATE_TABLE="CREATE TABLE "+LOCAL_TABLE_NAME+"("+SNO+" INTEGER PRIMARY KEY, "
			+HOUSE_NO+"	INTEGER ,"+HOUSE_NAME+" TEXT ,"+LOGIN_ACCESS+" TEXT ,"+SETTINGS+" TEXT ,"
			+USER_NAME+" TEXT ,"+PASSWORD+" TEXT ,"+APP_LOCK+" TEXT ,"+APP_PWD+" TEXT ,"
			+DATA1+" TEXT ,"+DATA2+" TEXT ,"+DATA3+" TEXT ,"+DATA4+" TEXT ,"+DATA5+" TEXT ,"
			+DATA6+" TEXT ,"+DATA7+" TEXT ,"+DATA8+" TEXT ,"+DATA9+" TEXT ,"+DATA10+" TEXT "+");";	

	//constructor of class
	public LocalDatabaseAdapter(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		mcontext=context;
		DATABASE_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
		StaticVariables.printLog("DATABASE","constructor Called");
	}

	//this method will be called only once if database is not created
	@Override
	public void onCreate(SQLiteDatabase db) {

		db.execSQL(CREATE_TABLE);
		StaticVariables.printLog("DATABASE","Database Created");

	}

	//upgrading the database version
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS " + LOCAL_TABLE_NAME);
		onCreate(db);
	}

	//opening the database for read and write operations
	public void opendb() {
		try	{
			sdb = this.getWritableDatabase();
		}catch(Exception e)	{
			e.printStackTrace();
		}
	}

	//checking if Database is existing or not
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

	//closing database
	public void close() {
		try {
			if (sdb != null) {
				sdb.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//*************************DATABASE OPERATIONS************************************

	//Insert Records
/*	public long insert(int HouseNo,String HouseName)
	{
		ContentValues cv=new ContentValues();

		cv.put(HOUSE_NO, HouseNo);
		cv.put(HOUSE_NAME, HouseName);
		cv.put(LOGIN_ACCESS,"M");
		cv.put(SETTINGS, "false");

		cv.put(USER_NAME, "Default");
		cv.put(PASSWORD, "Default");
		cv.put(APP_LOCK, "false");
		cv.put(APP_PWD, "1234");		
		cv.put(DATA1, "Default");//UserType
		cv.put(DATA2, "Default");
		cv.put(DATA3, "Default");
		cv.put(DATA4, "Default");
		cv.put(DATA5, "Default");
		cv.put(DATA6, "Default");
		cv.put(DATA7, "Default");
		cv.put(DATA8, "Default");
		cv.put(DATA9, "Default");
		cv.put(DATA10, "Default");

		return sdb.insert(LOCAL_TABLE_NAME, null, cv);
	}*/


	/*public long insert(int HouseNo,String HouseName,String username,String password,String usertype)
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

		return sdb.insert(LOCAL_TABLE_NAME, null, cv);
	}*/

	public long insert(int HouseNo,String HouseName,String username,byte password[],String usertype)
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

		return sdb.insert(LOCAL_TABLE_NAME, null, cv);
	}
	//Update Records
	public boolean update(int HouseNo,String HouseName)
	{
		ContentValues cv=new ContentValues();
		cv.put(HOUSE_NAME, HouseName);

		return sdb.update(LOCAL_TABLE_NAME, cv, HOUSE_NO+"="+HouseNo, null)>0;
	}


	//Delete Record
	public boolean delete(int HouseNo) 
	{
		return sdb.delete(LOCAL_TABLE_NAME, HOUSE_NO+"="+HouseNo, null) > 0;
	}


	//Fetch Records
	public Cursor fetchAllRecords() {
		return sdb.query(true,LOCAL_TABLE_NAME, new String[] {HOUSE_NO,HOUSE_NAME},null, null, null, null, null, null);
	}

	//fetching last house number
	public int fetchMaxHouseNo()	{
		int HouseNo = 0;
		Cursor mCursor =null;
		try
		{
			mCursor= sdb.query(LOCAL_TABLE_NAME, new String[] { "MAX("+ HOUSE_NO + ")" },null , null, null, null, null);

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

	//checking whether house name is already exixting in db or not
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
					StaticVariables.printLog("Houses","House Name : "+houseName);
					if(houseName.equalsIgnoreCase(Housename)){
						isExists=true; 
						StaticVariables.printLog("Exists","House Already Exists");
					}
				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}

		StaticVariables.printLog("Houses","House Exists :"+isExists);
		return isExists;
	}

	//getting downloaded houses list
	public ArrayList<String> HouseNamesList(){
		ArrayList<String> HouseList=new ArrayList<String>();
		try
		{
			Cursor mcursor =fetchAllRecords();
			if(mcursor.getCount()==0){
			}else {
				String houseName=null;
				mcursor.moveToFirst();
				do{
					houseName=mcursor.getString(mcursor.getColumnIndex(HOUSE_NAME));
					StaticVariables.printLog("Houses","House Name : "+houseName);
					HouseList.add(houseName); //adding house names in list
				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return HouseList;
	}

	//fetching app lock password from database
	public String appPassword(){
		String password=null;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(LOCAL_TABLE_NAME, new String[] {APP_PWD},SNO+"=1", null, null, null, null);
			if(mcursor.getCount()!=0){
				StaticVariables.printLog("Count","cursor count is : "+mcursor.getCount());
				mcursor.moveToFirst();
				password=mcursor.getString(mcursor.getColumnIndex(APP_PWD));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return password;
	}


}
