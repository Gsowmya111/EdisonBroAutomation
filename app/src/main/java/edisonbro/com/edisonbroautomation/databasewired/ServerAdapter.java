package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: ServerAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing Server table[contains details like ip ,port,ssid of gateway ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class ServerAdapter {

	public static final String KEY_ROWID = "_id";
	public static final String KEY_i  = "i";
	public static final String KEY_p  = "p";
	public static final String KEY_ss = "ss"; 
	public static final String KEY_Ru = "ru";
	public static final String KEY_RC = "rc";
	public static final String KEY_ri = "ri";
	public static final String KEY_rp = "rp"; 
	public static final String KEY_ps = "ps"; 
	public static final String KEY_rsi= "rsi";
	public static final String KEY_rsp= "rsp"; 
	public static final String KEY_ds = "ds";
    public static final String KEY_da = "da";
    public static final String KEY_db = "db";
    public static final String KEY_dc = "dc";
    public static final String KEY_dd = "dd";
    public static final String KEY_de = "de";
    public static final String KEY_ea = "ea";
    public static final String KEY_eb = "eb";
    
    
    SQLiteDatabase sdb;
	Context mcontext;
	
	private static final String SERVER_TABLE ="ServerTable";
	public static String ORIGINAL_PATH =null;
	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";

	private static final String TAG1="Server Adap  - ";
   
	public ServerAdapter(Context context) {
		mcontext = context;
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
	}

	public void open() {
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;
			StaticVariabes_div.log("Databse Path is " + path, TAG1);

			sdb = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (sdb != null) {
				sdb.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ****************************Database Operation************************
   /* public Cursor fetch_rowdevnoroomno(String houseno) throws SQLException {
		houseno="'"+houseno+"'";
		Cursor mCursor = sdb.query(true, SERVER_TABLE, new String[] {
				KEY_ROWID, KEY_i,KEY_p,KEY_ss,KEY_Ru,KEY_RC,KEY_ri,KEY_rp,KEY_ps,KEY_rsi,KEY_rsp,KEY_ds,
				KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de},
				KEY_dd + "=" + houseno, null, null, null, null, null);

		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}*/
    public Cursor fetch_detdb(long rowId) throws SQLException {
		Cursor mCursor = sdb.query(true, SERVER_TABLE, new String[] {
				KEY_ROWID, KEY_i,KEY_p,KEY_ss,KEY_Ru,KEY_RC,KEY_ri,KEY_rp,KEY_ps,KEY_rsi,KEY_rsp,KEY_ds,
				KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
   // public boolean updateServerTable_ip_ssid(String ip,int port,String ssid) {

   public boolean updateServerTable_ip_ssid(String ip,String ssid) {
		boolean updated = false;
		
		try{
			ContentValues cv=new ContentValues();
			cv.put(KEY_i, ip);
			//cv.put(KEY_p, port);
			cv.put(KEY_ss, ssid);
			sdb.update(SERVER_TABLE, cv, KEY_ROWID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}
		
		return updated;
	}
	public boolean update_ssid_ServerTable(String ssid) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(KEY_ss, ssid);
			sdb.update(SERVER_TABLE, cv, KEY_ROWID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}
    public void updaterirp(int idn,String ri,String rp){	
		ContentValues args = new ContentValues();
	    args.put(KEY_ri, ri);
	    args.put(KEY_rp, rp);
	    sdb.update(SERVER_TABLE, args, KEY_ROWID + "=" + idn, null);
	}
    
    public void updatepwd(String un,String pw){	
    	un="'"+un+"'";
		ContentValues args = new ContentValues();
	    args.put(KEY_dc, pw);
	    sdb.update(SERVER_TABLE, args, KEY_db + "=" +un, null);
	}
}
