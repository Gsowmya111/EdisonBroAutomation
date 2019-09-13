package edisonbro.com.edisonbroautomation.databasewired;


/**
 *  FILENAME: ServerDetailsAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing ServerDetails table[contains details like username ,password, refno of gateway ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.io.IOException;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class ServerDetailsAdapter {
	SQLiteDatabase sdb;
	Context mcontext;
	
	private static final String SERVER_TABLE ="ServerTable";
	private static final String SERVER_DETTABLE ="ServerDetails";
	public static String ORIGINAL_PATH =null;
	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
	private static final String TAG1="ServerDetails Adap  - ";


	public static final String ID="_id";
	public static final String SERVER_IP="i";
	public static final String SERVER_PORT="p";
	public static final String SERVER_SSID="ss";
	

	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_dg = "dg";
	public static final String KEY_pn = "pn";	
	public static final String KEY_du = "du";	//devtype
	public static final String KEY_ds = "ds";//image
	public static final String KEY_ps = "ps";	
	public static final String KEY_dl = "dl";
	public static final String KEY_di = "di";
	public static final String KEY_us = "us";
	public static final String KEY_un = "un";
	public static final String KEY_cs = "cs";	
	public static final String KEY_rno= "rno";
	public static final String KEY_da = "da";
	public static final String KEY_db = "db";
	public static final String KEY_dc = "dc";
	public static final String KEY_dd = "dd";
	public static final String KEY_de = "de";
	public static final String KEY_ea = "ea";
	public static final String KEY_eb = "eb";
	public static final String KEY_ec = "ec";
	public static final String KEY_ed = "ed";
	public static final String KEY_ee = "ee";
	public static final String KEY_ef = "ef";
	public static final String KEY_eg = "eg";
	public static final String KEY_eh = "eh";
	public static final String KEY_ei = "ei";
	public static final String KEY_ej = "ej";
	
	
	
	public ServerDetailsAdapter(Context context) {
		mcontext = context;
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
	}

	public boolean checkdb() throws IOException {
		SQLiteDatabase sdb1 = null;
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;

			sdb1 = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		} catch (Exception e) {
			e.printStackTrace();
		}

		if (sdb1 != null) {
			sdb1.close();
		//	Log.d("", "in sldb close");
		}

		return sdb1 != null ? true : false;

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
	
	public boolean updateServerTable(String ip,int port,String ssid) {
		boolean updated = false;
		
		try{
			ContentValues cv=new ContentValues();
			cv.put(SERVER_IP, ip);
			cv.put(SERVER_PORT, port);
			cv.put(SERVER_SSID, ssid);
			sdb.update(SERVER_TABLE, cv, ID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return updated;
	}
//**********************************serverdetails******************************************
	public Cursor fetchadminpass(String usr,String pass) throws SQLException {
		usr="'"+usr+"'";
		pass="'"+pass+"'";
		Cursor mCursor = sdb.query(true, SERVER_DETTABLE, new String[] {
				KEY_ROWID,KEY_dg,KEY_pn,KEY_du,KEY_ds,KEY_ps,KEY_dl,KEY_di,KEY_us,KEY_un,KEY_cs,
				KEY_rno,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
				KEY_un + "=" + usr+" AND "+ KEY_ps+"="+pass, null, null, null, null, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;			
	}
//*******************************************************************************
public Cursor fetchlogintype(String usr) throws SQLException {
	usr="'"+usr+"'";

	Cursor mCursor = sdb.query(true, SERVER_DETTABLE, new String[] {
					KEY_ROWID,KEY_dg,KEY_pn,KEY_du,KEY_ds,KEY_ps,KEY_dl,KEY_di,KEY_us,KEY_un,KEY_cs,
					KEY_rno,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
			KEY_un + "=" + usr, null, null, null, null, null);
	if(!(mCursor.getCount()>0))
		return null;
	if (mCursor != null) {
		mCursor.moveToFirst();
	}
	return mCursor;

	//****************************************
}
	public Cursor checkuser_exist(String usr,String passwd) throws SQLException {
		usr="'"+usr+"'";
		passwd="'"+passwd+"'";
		Cursor mCursor = sdb.query(true, SERVER_DETTABLE, new String[] {
						KEY_ROWID,KEY_dg,KEY_pn,KEY_du,KEY_ds,KEY_ps,KEY_dl,KEY_di,KEY_us,KEY_un,KEY_cs,
						KEY_rno,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
				KEY_un + "=" + usr  +" AND "+KEY_ps +"="+passwd, null, null, null, null, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;

		//****************************************
	}

	public Cursor fetchrefno(long rowId) throws SQLException {
		//rowId="'"+rowId+"'";
		Cursor mCursor = sdb.query(true, SERVER_DETTABLE, new String[] {
						KEY_ROWID,KEY_dg,KEY_pn,KEY_du,KEY_ds,KEY_ps,KEY_dl,KEY_di,KEY_us,KEY_un,KEY_cs,
						KEY_rno,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
				KEY_ROWID + "=" +rowId, null, null, null, null, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	//**************************************************************************
	
	public boolean updateServerDetailsTable(String ipnetgateway,String port) {
		boolean updated = false;
		
		try{
			ContentValues cv=new ContentValues();
			cv.put(KEY_dg, ipnetgateway);
			cv.put(KEY_pn, port);
			sdb.update(SERVER_DETTABLE, cv, KEY_ROWID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}
		
		return updated;
	}
	//************************************************************
	
	public boolean updatePassword(String user,byte[] Password) {
		boolean updated = false;
		user="'"+user+"'";
		try{
			ContentValues cv=new ContentValues();			
			//cv.put(KEY_ps, Password);
			cv.put(KEY_ps, Password);
			updated=sdb.update(SERVER_DETTABLE, cv, KEY_un+"="+user, null) > 0;
			//updated=true;
		}catch(Exception e){
			e.printStackTrace();
		}
		
		return updated;
	}

	//***********************************************************
	public boolean update_versionnumber(String versionnumber) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(KEY_db, versionnumber);
			updated=sdb.update(SERVER_DETTABLE, cv, KEY_ROWID+"=1", null)>0;
			//updated=true;
		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}

		return updated;
	}
	//*******************************************************8
	public boolean deleteusr(String user) {
		user="'"+user+"'";
		return sdb.delete(SERVER_DETTABLE, KEY_un+ "=" + user, null) > 0;
	}
	/**inserts values into database*/
	/*public long insertData(String dg, String pn,String  du,String ds,String ps,String dl,String di,String us,String un,String cs,
			String rno,String da,String db,String dc,String dd,String de,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej){
		ContentValues initialValues = createContentValues(dg,pn,du,ds,ps,dl,di,us,un,cs,
				rno,da,db,dc,dd,de,ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		return sdb.insert(SERVER_DETTABLE, null, initialValues);
	}*/
	public long insertData(String dg, String pn,String  du,String ds,byte[] ps,String dl,String di,String us,String un,String cs,
						   String rno,String da,String db,String dc,String dd,String de,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej){
		/*ContentValues initialValues = createContentValues(dg,pn,du,ds,ps,dl,di,us,un,cs,
				rno,da,db,dc,dd,de,ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		return sdb.insert(SERVER_DETTABLE, null, initialValues);*/

		ContentValues cv=new ContentValues();

		cv.put(KEY_dg, dg);
		cv.put(KEY_pn, pn);
		cv.put(KEY_du,du);
		cv.put(KEY_ds, ds);
		cv.put(KEY_ps,ps);
		cv.put(KEY_dl,dl);
		cv.put(KEY_di, di);
		cv.put(KEY_us,us);
		cv.put(KEY_un,un);
		cv.put(KEY_cs,cs);
		cv.put(KEY_rno,rno);
		cv.put(KEY_da,da);
		cv.put(KEY_db,db);
		cv.put(KEY_dc, dc);
		cv.put(KEY_dd,dd);
		cv.put(KEY_de,de);
		cv.put(KEY_ea,ea);
		cv.put(KEY_eb,eb);
		cv.put(KEY_ec, ec);
		cv.put(KEY_ed,ed);
		cv.put(KEY_ee,ee);
		cv.put(KEY_ef,ef);
		cv.put(KEY_eg,eg);
		cv.put(KEY_eh,eh);
		cv.put(KEY_ei,ei);
		cv.put(KEY_ej,ej);
		return sdb.insert(SERVER_DETTABLE, null, cv);
	}
	///////////////for Admin//////////////////////////////////////////////////////


	public boolean update_useraccess(String username,String role) {
		boolean updated = false;
		username="'"+username+"'";
		try{
			ContentValues cv=new ContentValues();
			cv.put(KEY_da, role);
			updated=sdb.update(SERVER_DETTABLE, cv, KEY_un+"="+username, null)>0;
		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}

		return updated;
	}
	//************************************************************************************
	public int getCount_allusers() {
	    final int DEFAULT_BASE = 0;
	    Cursor mCursor = sdb.rawQuery(
	            "SELECT Count(*) FROM ServerDetails WHERE da='U' OR da='G'"  , null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}
	//**********************************************************************************
	public String[] getallusers(int count) {
		   // final int DEFAULT_BASE = 0;
		    Cursor mCursor = sdb.rawQuery(
		            "SELECT un FROM ServerDetails  WHERE da='U' OR da='G' ", null);
		    String[] array = new String[count];
		    int i = 0;
		    while(mCursor.moveToNext()){
		        String uname = mCursor.getString(mCursor.getColumnIndex("un"));
		        array[i] = uname;
		        i++;
		    }		    		  
			return array;
		}
	//**************************************************************************
	public Cursor getallusers_types(String usr) {
		usr="'"+usr+"'";
		// final int DEFAULT_BASE = 0;
		Cursor mCursor = sdb.rawQuery(
				"SELECT da FROM ServerDetails  WHERE un="+usr, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	////////////////// For Admin ///////////////////////////////////////////////////////////////
	public int getCount_allusers_admin() {
		final int DEFAULT_BASE = 0;
		Cursor mCursor = sdb.rawQuery(
				"SELECT Count(*) FROM ServerDetails WHERE da='U' OR da='G' OR da='A'"  , null);
		if (mCursor.getCount() == 0)
			return DEFAULT_BASE;
		else
			mCursor.moveToFirst();
		return (int) mCursor.getLong(0);
	}

	//**********************************************************************************
	public String[] getallusers_admin(int count) {
		// final int DEFAULT_BASE = 0;
		Cursor mCursor = sdb.rawQuery(
				"SELECT un FROM ServerDetails  WHERE da='U' OR da='G' OR da='A' ", null);
		String[] array = new String[count];
		int i = 0;
		while(mCursor.moveToNext()){
			String uname = mCursor.getString(mCursor.getColumnIndex("un"));
			array[i] = uname;
			i++;
		}
		return array;
	}
	//*************************************************************************
	 public Cursor fetch_sd(long rowId) throws SQLException {
			Cursor mCursor = sdb.query(true, SERVER_DETTABLE, new String[] {
					KEY_ROWID, KEY_dg,KEY_pn,KEY_du,KEY_ds,KEY_ps,KEY_dl,KEY_di,KEY_us,KEY_un,KEY_cs,
					KEY_rno,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
					KEY_ROWID + "=" + rowId, null, null, null, null, null);
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
		}
	
	//**************************************************************************
	private ContentValues createContentValues(String dg, String pn,String  du,String ds,String ps,String dl,String di,String us,String un,String cs,
			String rno,String da,String db,String dc,String dd,String de,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej) {
		ContentValues values = new ContentValues();

		values.put(KEY_dg, dg);
		values.put(KEY_pn, pn);
		values.put(KEY_du, du);
		values.put(KEY_ds, ds);
		values.put(KEY_ps, ps);
		values.put(KEY_dl, dl);
		values.put(KEY_di, di);
		values.put(KEY_us, us);
		values.put(KEY_un, un);
		values.put(KEY_cs, cs);
		values.put(KEY_rno,rno);
		values.put(KEY_da, da);
		values.put(KEY_db, db);
		values.put(KEY_dc, dc);
		values.put(KEY_dd, dd);
		values.put(KEY_de, de);
		values.put(KEY_ea, ea);
		values.put(KEY_eb, eb);
		values.put(KEY_ec, ec);
		values.put(KEY_ed, ed);
		values.put(KEY_ee, ee);
		values.put(KEY_ef, ef);
		values.put(KEY_eg, eg);
		values.put(KEY_eh, eh);
		values.put(KEY_eh, ei);
		values.put(KEY_eh, ej);
		return values;
	}
}
