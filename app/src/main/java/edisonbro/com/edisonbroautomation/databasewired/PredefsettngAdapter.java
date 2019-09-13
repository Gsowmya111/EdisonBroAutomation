package edisonbro.com.edisonbroautomation.databasewired;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

/**
 *  FILENAME: PredefsettinHelper.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing Predefined table[contains details of all devices set for mood setting] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

public class PredefsettngAdapter {

	public static final String KEY_ROWID   = "_id";	
	public static final String KEY_Hn      = "hn";
	public static final String KEY_Hna     = "hna";
	public static final String KEY_Rn      = "rn";
	public static final String KEY_Rna     = "rna";
	public static final String KEY_stno    = "stno";
	public static final String KEY_stna    = "stna";
	public static final String KEY_Dt      = "dt";
	public static final String KEY_Dna     = "dna";
	public static final String KEY_Dno     = "dno";
	public static final String KEY_wd      = "wd";
	public static final String KEY_DD      = "dd";
	public static final String KEY_ea      = "ea";
	public static final String KEY_eb      = "eb";
	public static final String KEY_ec      = "ec";
	public static final String KEY_ed      = "ed";
	public static final String KEY_ee      = "ee";
	public static final String KEY_ef      = "ef";
	public static final String KEY_eg      = "eg";
	public static final String KEY_eh      = "eh";
	public static final String KEY_ei      = "ei";
	public static final String KEY_ej      = "ej";
	
	
	private static final String DATABASE_TABLE = "PredefDetails_table";
	private Context context;
	private SQLiteDatabase database;
	private PredefsettinHelper pre_dbhelp;
	
	
	public PredefsettngAdapter(Context context) {
		this.context = context;
	}

	public PredefsettngAdapter open() throws SQLException {
		pre_dbhelp = new PredefsettinHelper(context);
		database = pre_dbhelp.getWritableDatabase();
		database=pre_dbhelp.getReadableDatabase();
		return this;
	}

	public void close() {
		pre_dbhelp.close();
	}

	
	public long create(String hn, String hna,String rn,String rna,String stno,String stna,String dt,String dna,
			String dno,String wd,String dd,String ea,String eb,String ec,String ed,String ee,String ef,String eg
			,String eh,String ei,String ej) {
		
		ContentValues initialValues = createContentValues(hn,hna,rn,rna,stno,stna,dt,dna,dno,wd,dd,
				ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}
	
/*	public void updatesettname(String room,String settngno,String name,String roomn){
		String devno2="'"+room+"'";
		String settng="'"+settngno+"'";
		String roomno="'"+roomn+"'";
		ContentValues args = new ContentValues();
	    args.put(KEY_stna, name);
	    database.update(DATABASE_TABLE, args, KEY_RoomNo + "="
				+ roomno +"AND "+KEY_SettingNo+"="+settng, null);
	}*/
	/*public boolean updateeditroom(String hn, String hna,String rn,String rna,String stno,String stna,String dt,String dna,
			String dno,String wd,String dd,String ea,String eb,String ec,String ed,String ee,String ef,String eg
			,String eh,String ei,String ej) {
		 String roomno2="'"+roomnon+"'";
		String devno2="'"+devicenon+"'";
		String settng="'"+settngno+"'";
		ContentValues updateValues = createContentValues(hn,hna,rn,rna,stno,stna,dt,dna,dno,wd,dd,
				ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		
		
		return database.update(DATABASE_TABLE, updateValues, KEY_RoomNo + "="
				+ roomno2 +"AND "+KEY_DeviceNo+"="+devno2+"AND "+KEY_SettingNo+"="+settng, null) > 0;
	}*/
	
	
	public boolean updatevalfordevtyp(String hn,String rn,String setno,String dt,String dno,String val,String wd,String status,String userlogged){
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		dt="'"+dt+"'";
		dno="'"+dno+"'";
		userlogged ="'"+userlogged+"'";
		ContentValues args = new ContentValues();
	    args.put(KEY_DD, val);
	    args.put(KEY_wd, wd);
		args.put(KEY_eb, status);
	   return database.update(DATABASE_TABLE, args, KEY_Hn + "="
				+ hn +"AND "+KEY_Rn+"="+rn+"AND "+KEY_stno+"="+setno+"AND "+KEY_Dt+"="+dt+"AND "+KEY_Dno+"="+dno+"AND "+KEY_ed+"="+userlogged, null)>0;
	}
	
	
	public void updatesettingname(String hn,String rn,String setno,String value){
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		
		ContentValues args = new ContentValues();
	    args.put(KEY_stna, value);
	    
	    database.update(DATABASE_TABLE, args, KEY_Hn + "="
				+ hn +"AND "+KEY_Rn+"="+rn+"AND "+KEY_stno+"="+setno, null);
	}
	
	public boolean deletevalfordevtyp(String hn,String rn,String setno,String dt,String dno){
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		dt="'"+dt+"'";
		dno="'"+dno+"'";		
	    return database.delete(DATABASE_TABLE, KEY_Hn + "="
				+ hn +"AND "+KEY_Rn+"="+rn+"AND "+KEY_stno+"="+setno+"AND "+KEY_Dno+"="+dno, null) > 0;
	}

	public boolean delete_all_valforroom(String hn,String rn,String setno){
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		return database.delete(DATABASE_TABLE, KEY_Hn + "="
				+ hn +"AND "+KEY_Rn+"="+rn+"AND "+KEY_stno+"="+setno, null) > 0;
	}
	public boolean deletevalforhouseno(String hn){
		hn="'"+hn+"'";	
	    return database.delete(DATABASE_TABLE, KEY_Hn + "="
				+ hn, null) > 0;
	}
	
	public long insert(String hn, String hna,String rn,String rna,String stno,String stna,String dt,String dna,
			String dno,String wd,String dd,String ea,String eb,String ec,String ed,String ee,String ef,String eg
			,String eh,String ei,String ej) {
		ContentValues initialValues = createContentValues(hn,hna,rn,rna,stno,stna,dt,dna,dno,wd,dd,
				ea,eb,ec,ed,ee,ef,eg,eh,ei,ej);

		return database.insert(DATABASE_TABLE, null, initialValues);
	}	
	/*public Cursor fetchAll() {
		return database.query(DATABASE_TABLE, new String[] { KEY_ROWID,
				KEY_DeviceId,KEY_DeviceNo,KEY_bulb1,KEY_bulb2,KEY_bulb3
				,KEY_bulb4,KEY_bulb5,KEY_bulb6,KEY_bulb7,KEY_Fan,KEY_Allon,KEY_Alloff,KEY_curtain1,KEY_curtain2,KEY_curtain3,KEY_curtain4,KEY_gassensor
				,KEY_dimmer,KEY_Videoserv,KEY_watrhtr,KEY_Ac,KEY_Remote,KEY_securitysys,KEY_Camera,KEY_moodlight
				,KEY_dimmerval,KEY_rgbcolor,KEY_curstate}, null, null, null,
				null, null);
	}*/
	
	
	/*public Cursor fetch_rowdevnosetno(String devno,String setno,String roomno) throws SQLException {
		devno="'"+devno+"'";
		setno="'"+setno+"'";
		roomno="'"+roomno+"'";
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
				,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
				,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
				,KEY_ei,KEY_ej},
				KEY_Rn + "=" +roomno+"AND "+KEY_Dno +"="+devno+"AND "+KEY_stno +"=" +setno, null, null, null, null, null);		
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}	*/
	
	
	public Cursor fetch_devicerow(String hn,String rn,String setno,String dt,String dno,String usrloggd) throws SQLException {
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		dt="'"+dt+"'";
		dno="'"+dno+"'";
		usrloggd="'"+usrloggd+"'";
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
				,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
				,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
				,KEY_ei,KEY_ej},
				KEY_Rn + "=" +rn+"AND "+KEY_Hn +"="+hn+"AND "+KEY_stno +"=" +setno+"AND "+KEY_Dt +"=" +dt+"AND "+KEY_Dno +"=" +dno+"AND "+KEY_ed +"=" +usrloggd, null, null, null, null, null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}	
	
	public Cursor fetch_roomsett(String hn,String rn,String setname) throws SQLException {
		hn="'"+hn+"'";
		setname="'"+setname+"'";
		rn="'"+rn+"'";
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
				,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
				,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
				,KEY_ei,KEY_ej},
				KEY_Rn + "=" +rn+"AND "+KEY_Hn +"="+hn+"AND "+KEY_stna +"=" +setname, null, null, null, null, null);		
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}	
	
	public Cursor fetch_prefsettname(String hn,String rn,String setno) throws SQLException {
		hn="'"+hn+"'";
		setno="'"+setno+"'";
		rn="'"+rn+"'";
		Cursor mCursor = database.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
				,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
				,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
				,KEY_ei,KEY_ej},
				KEY_Rn + "=" +rn+"AND "+KEY_Hn +"="+hn+"AND "+KEY_stno +"=" +setno, null, null, null, null, null);		
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}	
	
	public int getCount_housenoroomnodevtype(String hn,String rn,String setno,String dt) {
	    final int DEFAULT_BASE = 0;
	    Cursor mCursor = database.rawQuery(
	            "SELECT Count(*) FROM PredefDetails_table WHERE hn='"+hn+"'"+" AND rn='"+rn+"'"+" AND dt='"+dt+"'"+" AND stno='"+setno+"'", null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}


	public int getCount_housenoroomnomoodnum(String hn,String rn,String setno) {
		 int DEFAULT_BASE = 0;
		Cursor mCursor = database.rawQuery(
				"SELECT Count(*) FROM PredefDetails_table WHERE hn='"+hn+"'"+" AND rn='"+rn+"'"+" AND stno='"+setno+"'", null);
		if (mCursor.getCount() == 0)
			DEFAULT_BASE=0;
		else {
			mCursor.moveToFirst();
			DEFAULT_BASE=(int) mCursor.getLong(0);
		}
		return DEFAULT_BASE;
	}


	public String[] getall_housenoroomnodevicetype(int count,String hn,String rn,String setno,String dt) {		  
	//	hn="'"+hn+"'";
	//	setno="'"+setno+"'";
	//	rn="'"+rn+"'";
	//	dt="'"+dt+"'";
		    Cursor mCursor = database.rawQuery(
		    		 "SELECT _id,hn,hna,rn,rna,stno,stna,dt,dna,dno,wd,dd,ea,eb,ec,ed,ee,ef,eg,eh,ei,ej" +
		    		            " FROM PredefDetails_table WHERE hn='"+hn+"'"+"AND rn='"+rn+"'"+"AND dt='"+dt+"'"+" AND stno='"+setno+"'", null);
		    String[] array = new String[count];		   
		    int i = 0;
		    while(mCursor.moveToNext()){
		        String uname = mCursor.getString(mCursor.getColumnIndex("dno"));
		        array[i] = uname;
		        i++;
		    }
		if(mCursor!=null){
			mCursor.close();
		}
		    return array;
	}
	
	private ContentValues createContentValues(String hn, String hna,String rn,String rna,String stno,String stna,String dt,String dna,
			String dno,String wd,String dd,String ea,String eb,String ec,String ed,String ee,String ef,String eg
			,String eh,String ei,String ej) {
		ContentValues values = new ContentValues();
		
		values.put(KEY_Hn, hn);
		values.put(KEY_Hna, hna);
		values.put(KEY_Rn, rn);
		values.put(KEY_Rna, rna);
		values.put(KEY_stno, stno);
		values.put(KEY_stna, stna);
		values.put(KEY_Dt, dt);	
		values.put(KEY_Dna, dna);
		values.put(KEY_Dno, dno);
		values.put(KEY_wd, wd);
		values.put(KEY_DD, dd);
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
	

}

