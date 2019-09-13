package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: CamAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database Adapter class for camera table .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class CamAdapter {
	public static final String KEY_ROWID = "_id";
	public static final String KEY_a= "a";
	public static final String KEY_b= "b";
	public static final String KEY_CameraNo= "cn";
	public static final String KEY_CameraIp= "ci";
	public static final String KEY_CameraPort= "cp";
	public static final String KEY_CameraURL= "cu";
	public static final String KEY_CameraName= "ce";
	public static final String KEY_ShowCam= "sc";
	public static final String KEY_da= "da";
	public static final String KEY_db= "db";
	public static final String KEY_dc= "dc";
	public static final String KEY_dd= "dd";
	public static final String KEY_de= "de";
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
	private static final String DATABASE_TABLE = "CameraTable";
	private Context mcontext;
	private SQLiteDatabase dbs;
	//private a$a_A camera_dbhelp;
	public static String ORIGINAL_PATH =null;
  	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
  	private static final String TAG1="Cam Adap  - ";
	
	public CamAdapter(Context context) {
		mcontext = context;
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
	}

	public void open() throws SQLException {
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;
			StaticVariabes_div.log("database cam path open"+path, TAG1);
			dbs = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE
							+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		StaticVariabes_div.log("database cam close", TAG1);
		try {
			if (dbs != null) {
				dbs.close();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	public long create(String camno, String camip, String camport,String camurl,String camname,boolean showcam,String da,String db,String dc,String dd,String de ) {
		
		ContentValues initialValues = createContentValues(camno,camip,camport,camurl,camname,showcam,da,db,dc,dd,de);

		return dbs.insert(DATABASE_TABLE, null, initialValues);
	}



	
	public long insert(String camno, String camip, String camport,String camurl,String camname,boolean showcam,String da,String db,String dc,String dd,String de) {
		ContentValues initialValues = createContentValues(camno,camip,camport,camurl,camname,showcam,da,db,dc,dd,de);
		return dbs.insert(DATABASE_TABLE, null, initialValues);
	}
	
	
	public boolean updateeditroom(String cn, String ci, String cp,String cu,String ce,boolean sc,String da,String db,String dc,String dd,String de,String roomno) {
		//String devno2="'"+devno+"'";
		String devno2="'"+roomno+"'";
		ContentValues updateValues = createContentValues(cn,ci,cp,cu,ce,sc,da,db,dc,dd,de);

		return dbs.update(DATABASE_TABLE, updateValues, KEY_CameraNo + "="
				+ devno2, null) > 0;
	}
	
	
	public boolean deletecamera(String devno) {
		devno="'"+devno+"'";
		return dbs.delete(DATABASE_TABLE, KEY_CameraNo + "=" + devno, null) > 0;
	}
	
	
	public Cursor fetch_rowdevno(String devno) throws SQLException {
		devno="'"+devno+"'";
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_CameraNo,KEY_CameraIp,KEY_CameraPort,KEY_CameraURL,KEY_CameraName,KEY_ShowCam,
				KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de},
				KEY_CameraNo+ "=" + devno, null, null, null, null, null);
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
	
	
	public Cursor fetch_camip(String roomno,String camno) throws SQLException {
		roomno="'"+roomno+"'";
		camno="'"+camno+"'";
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_CameraNo,KEY_CameraIp,KEY_CameraPort,KEY_CameraURL,KEY_CameraName,KEY_ShowCam,
				KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de},
				KEY_CameraNo+ "=" + camno+" AND "+KEY_a + "=" + roomno, null, null, null, null, null);				
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor fetch_rowroomno(String roomno) throws SQLException {
		roomno="'"+roomno+"'";
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_CameraNo,KEY_CameraIp,KEY_CameraPort,KEY_CameraURL,KEY_CameraName,KEY_ShowCam},
				KEY_CameraNo+ "=" + roomno, null, null, null, null, null);
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
	
	
	private ContentValues createContentValues(String cn, String ci, String cp,String cu,String ce,boolean sc,String da,String db,String dc,String dd,String de) {
		ContentValues values = new ContentValues();
		values.put(KEY_CameraNo, cn);
		values.put(KEY_CameraIp, ci);
		values.put(KEY_CameraPort, cp);
		values.put(KEY_CameraURL, cu);
		values.put(KEY_CameraName, ce);
		values.put(KEY_ShowCam, sc);
		values.put(KEY_da, da);
		values.put(KEY_db, db);
		values.put(KEY_dc, dc);
		values.put(KEY_dd, dd);
		values.put(KEY_de, de);
		return values;
	}
	

	public int getCount() {
	    final int DEFAULT_BASE = 0;
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT Count(*) FROM CameraTable", null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}
	
	
	public Cursor fetch_row(long rowId) throws SQLException {
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_CameraNo,KEY_CameraIp,KEY_CameraPort,KEY_CameraURL,KEY_CameraName,KEY_ShowCam},
				KEY_ROWID + "=" + rowId, null, null, null, null, null);
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
	
	
	public String[] getallcamno(int count) {
		   // final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT cn FROM CameraTable" , null);		    
		    String[] camnoarray = new String[count];
		    int i = 0;
		    while(mCursor.moveToNext()){
		        String uname = mCursor.getString(mCursor.getColumnIndex("cn"));
		        camnoarray[i] = uname;
		        i++;
		    }
		    return camnoarray;
	}
	
	public int getCountroomno(String roomno) {
	    final int DEFAULT_BASE = 0;
	    roomno="'"+roomno+"'";
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT Count(*) FROM CameraTable WHERE a="+roomno, null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}
	
	public String[] getallcamnoroomno(int count,String roomno) {		   
		    roomno="'"+roomno+"'";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT cn FROM CameraTable WHERE a="+roomno , null);		    
		    String[] camnoarray = new String[count];
		    int i = 0;
		    while(mCursor.moveToNext()){
		        String uname = mCursor.getString(mCursor.getColumnIndex("cn"));
		        camnoarray[i] = uname;
		        i++;
		    }
		    return camnoarray;
	}
	
	public String[] getallcamnameroomno(int count,String roomno) {		   
	    roomno="'"+roomno+"'";
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT ce FROM CameraTable WHERE a="+roomno , null);		    
	    String[] camnamearray = new String[count];
	    int i = 0;
	    while(mCursor.moveToNext()){
	        String uname = mCursor.getString(mCursor.getColumnIndex("ce"));
	        camnamearray[i] = uname;
	        i++;
	    }
	    return camnamearray;
}
	public boolean insertdataincamtable(String PackageName, String DataBaseName,int houseno,String housename)
	{
		try
		{
			String sql2="ATTACH '/data/data/"+ PackageName +"/databases/"+DataBaseName+"' AS temprodb;";
			dbs.execSQL(sql2);
			//ATTACH '/mnt/fastaccessDS/core/csv/allmsa.db' AS AM;
			String sql="INSERT INTO CameraTable(cn,ci,cp,cu,ce,sc,da,db,dc,dd,de) SELECT cn,ci,cp,cu,ce,sc,da,db,dc,"+houseno+",'"+housename+"' FROM temprodb.CameraTable;" ;//"INSERT INTO MasterTable(a,b,c,e,g,h) SELECT a,b,c,e,g,h FROM temptable.MasterTable ;";
			dbs.execSQL(sql);
			return true;
		}catch(Exception e)
		{e.printStackTrace();}
		return false;
	}
	
	
	public Cursor checkroomnameexist(String roomname) {
		Cursor mCursor = dbs.rawQuery(
		            "SELECT a FROM CameraTable WHERE b='"+roomname+"'", null);	
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
		}
	
	public void updatename(String roomname,String newname){
		roomname="'"+roomname+"'";
		ContentValues args = new ContentValues();
	   args.put(KEY_b, newname);
	   dbs.update(DATABASE_TABLE, args, KEY_b + "=" + roomname, null);
	}
	
	public int getCounthouseno() {
	    final int DEFAULT_BASE = 0;
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT Count(*) FROM CameraTable", null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}
	
	public String[] getall_roomname(int count) {		  		
		
	    Cursor mCursor = dbs.rawQuery(
	    		 "SELECT _id,a,b,cn,ci,cp,cu,ce,sc,da,db,dc,dd,de FROM CameraTable", null);
	    String[] array = new String[count];		   
	    int i = 0;
	    while(mCursor.moveToNext()){
	        String uname = mCursor.getString(mCursor.getColumnIndex("b"));
	        array[i] = uname;
	        i++;
	    }
	    return array;
}
	public String getroomname(String roomno) {
	    final int DEFAULT_BASE = 0; String roomname="";
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT b FROM CameraTable WHERE a='"+roomno+"'", null);	
	    if (mCursor.getCount() == 0){		    	
	    }
	    else{
	        mCursor.moveToFirst();
	        roomname = mCursor.getString(mCursor.getColumnIndex("b"));
	    }		       		       		       		           		 
		return roomname;
	}
	
	
	public String getroomno_frmdevno(String devno) {
	    final int DEFAULT_BASE = 0; String rno="";
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT a FROM CameraTable WHERE cn='"+devno+"'", null);	
	    if (mCursor.getCount() == 0){
	    	
	    }
	    else{
	        mCursor.moveToFirst();
	        rno = mCursor.getString(mCursor.getColumnIndex("a"));
	    }
	       
	       		       		           		 
		return rno;
	}
	
	public String getroomno(String roomname) {
	    final int DEFAULT_BASE = 0; String uname="";
	    Cursor mCursor = dbs.rawQuery(
	            "SELECT a FROM CameraTable WHERE b='"+roomname+"'", null);	
	    if (mCursor.getCount() == 0){
	    	
	    }
	    else{
	        mCursor.moveToFirst();
	        uname = mCursor.getString(mCursor.getColumnIndex("a"));
	    }
	       
	       		       		           		 
		return uname;
	}
}


