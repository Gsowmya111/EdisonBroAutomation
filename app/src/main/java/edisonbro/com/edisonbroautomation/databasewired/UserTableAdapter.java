package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: UserTableAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing Usertable table[contains details of room access  set for users ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;


public class UserTableAdapter {

	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_username= "un";
	public static final String KEY_roomno= "rns";
	public static final String KEY_ea= "ea";
	public static final String KEY_eb= "eb";
	public static final String KEY_ec= "ec";
	public static final String KEY_ed= "ed";
	public static final String KEY_ee= "ee";
	public static final String KEY_ef= "ef";
	public static final String KEY_eg= "eg";
	public static final String KEY_eh= "eh";
	public static final String KEY_ei= "ei";
	public static final String KEY_ej= "ej";
	private static final String DATABASE_TABLE = "UserTable";
	private Context mcontext;
	private SQLiteDatabase dbs;
	//private a$a_A camera_dbhelp;
	public static String ORIGINAL_PATH =null;
  	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
  	private static final String TAG1="Usertable Adap  - ";
	
	public UserTableAdapter(Context context) {
		mcontext = context;
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
	}

	public void open() throws SQLException {
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

		
	public long insert(String un, String rns,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej) {
		// TODO Auto-generated method stub

		ContentValues initialValues = createContentValues(un,rns,ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		return dbs.insert(DATABASE_TABLE, null, initialValues);
	}

	public Cursor fetch_User(String usrnam) throws SQLException {
		usrnam="'"+usrnam+"'";
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_username,KEY_roomno,KEY_ea},
				KEY_username + "=" +usrnam, null, null, null, null, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		/*if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;*/
	}
		
	
	public boolean updateuserAccess(String usrnam,String value,String eavalue){
		usrnam="'"+usrnam+"'";
		
		ContentValues args = new ContentValues();
	    args.put(KEY_roomno, value);
	    args.put(KEY_ea, eavalue);
	   int up= dbs.update(DATABASE_TABLE, args, KEY_username + "=" + usrnam, null);
	   if(up>0){
		return true;  
	   }else{
		return false;
	   }
	}



	public boolean deleteusrrooms(String user) {
		user="'"+user+"'";
		return dbs.delete(DATABASE_TABLE, KEY_username+ "=" + user, null) > 0;
	}
	private ContentValues createContentValues(String un, String rns,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej) {
		ContentValues values = new ContentValues();
		values.put(KEY_username, un);
		values.put(KEY_roomno, rns);
		values.put(KEY_ea, ea);
		values.put(KEY_eb, eb);
		values.put(KEY_ec, ec);
		values.put(KEY_ed, ed);
		values.put(KEY_ee, ee);
		values.put(KEY_ef, ef);
		values.put(KEY_eg, eg);
		values.put(KEY_eh, eh);
		values.put(KEY_ei, ei);
		values.put(KEY_ej, ej);
		return values;
	}
	
	
	public long insertvalue(String un, String rns,String ea,String eb,String ec,String ed,String ee,String ef,String eg,String eh,String ei,String ej){
		ContentValues values = new ContentValues();
		values.put(KEY_username, un);
		values.put(KEY_roomno, rns);
		values.put(KEY_ea, ea);
		values.put(KEY_eb, eb);
		values.put(KEY_ec, ec);
		values.put(KEY_ed, ed);
		values.put(KEY_ee, ee);
		values.put(KEY_ef, ef);
		values.put(KEY_eg, eg);
		values.put(KEY_eh, eh);
		values.put(KEY_ei, ei);
		values.put(KEY_ej, ej);
		return dbs.insert(DATABASE_TABLE, null, values);
	}
	

}

