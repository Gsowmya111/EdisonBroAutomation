package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: MasterAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database class for accessing Master table[contains details of all devices other than switchboard] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import java.util.ArrayList;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class MasterAdapter {
	
	Context mcontext;
	private SQLiteDatabase dbs;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_a = "a";
	public static final String KEY_b = "b";		
	public static final String KEY_c = "c"; 
	public static final String KEY_d = "d";	
	public static final String KEY_e = "e";
	public static final String KEY_f = "f";
	public static final String KEY_g = "g";
	public static final String KEY_h = "h";
	public static final String KEY_i = "i";    
	public static final String KEY_da = "da";
	public static final String KEY_db = "db";
	public static final String KEY_dc = "dc";
	public static final String KEY_dd = "dd"; 
	public static final String KEY_de = "de";	
	public static final String KEY_df = "df";
	public static final String KEY_dg = "dg";
	public static final String KEY_dh = "dh";
	public static final String KEY_di = "di"; 
	public static final String KEY_dj = "dj";	
	public static final String KEY_dk = "dk";
	public static final String KEY_dl = "dl";
	public static final String KEY_dm = "dm";
	public static final String KEY_dn = "dn"; 
	public static final String KEY_do = "do";
	public static final String KEY_dp = "dp";
	public static final String KEY_dq = "dq";
	public static final String KEY_dr = "dr";
	public static final String KEY_ds = "ds"; 
	public static final String KEY_dt = "dt";	
	public static final String KEY_du = "du";
	public static final String KEY_dv = "dv";
	public static final String KEY_dw = "dw";
	public static final String KEY_dx = "dx"; 
	public static final String KEY_dy = "dy";	
    public static final String KEY_dz = "dz";
    public static final String KEY_dza = "dza";
    public static final String KEY_dzb = "dzb";
    public static final String KEY_dzc = "dzc"; 
    public static final String KEY_dzd = "dzd";
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
	
	 public static String ORIGINAL_PATH =null;
	  	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
		private static final String DATABASE_TABLE = "MasterTable";
		private static final String TAG1="Master Adap  - ";
		
		public MasterAdapter(Context context) {
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
		
		//**************************************************************************************************88
		
		public int getCounthouseno() {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM MasterTable", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}
		
		public String[] getall_roomname(int count) {		  		
			
			    Cursor mCursor = dbs.rawQuery(
			    		 "SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
			    		            " FROM MasterTable", null);
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("b"));
			        array[i] = uname;
			        i++;
			    }
			    return array;
		}
		
		public String getroomno(String roomname) {
		    final int DEFAULT_BASE = 0; String uname="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT a FROM MasterTable WHERE b='"+roomname+"'", null);	
		    if (mCursor.getCount() == 0){
		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        uname = mCursor.getString(mCursor.getColumnIndex("a"));
		    }
		       
		      if(mCursor!=null)mCursor.close();
			return uname;
		}
		
		
		public String getroomname(String roomno) {
		    final int DEFAULT_BASE = 0; String roomname="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT b FROM MasterTable WHERE a='"+roomno+"'", null);	
		    if (mCursor.getCount() == 0){		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        roomname = mCursor.getString(mCursor.getColumnIndex("b"));
		    }		       		       		       		           		 
			return roomname;
		}
		
		
		public String getroomno_devno(String devno) {
		    final int DEFAULT_BASE = 0; String roomno="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT a FROM MasterTable WHERE d='"+devno+"'", null);	
		    if (mCursor.getCount() == 0){		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        roomno = mCursor.getString(mCursor.getColumnIndex("a"));
		    }		       		       		       		           		 
			return roomno;
		}

	public String getdevid_frmdevno(String devno) {
		final int DEFAULT_BASE = 0; String roomno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT f FROM MasterTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("f"));
		}
		return roomno;
	}


	public String getdevtype_devno(String devno) {
		    final int DEFAULT_BASE = 0; String roomno="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT da FROM MasterTable WHERE d='"+devno+"'", null);	
		    if (mCursor.getCount() == 0){		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        roomno = mCursor.getString(mCursor.getColumnIndex("da"));
		    }		       		       		       		           		 
			return roomno;
		}

	public String getdevversion_devno(String devno) {
		String roomno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT ed FROM MasterTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("ed"));
		}
		return roomno;
	}
		public String getmodtype_devno(String devno) {
		final int DEFAULT_BASE = 0; String roomno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT e FROM MasterTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("e"));
		}
		return roomno;
	}

	public String getgrp_devname(String devno) {
		 String roomno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT ec FROM MasterTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("ec"));
		}
		return roomno;
	}


	public String getdevno_frmdevname(String devname) {
		StaticVariabes_div.log("devname"+devname,TAG1);
		String devno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT d FROM MasterTable WHERE ec='"+devname+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			devno = mCursor.getString(mCursor.getColumnIndex("d"));
		}
		return devno;
	}
		public String getgrouptype_devno(String devno) {
		    final int DEFAULT_BASE = 0; String roomno="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT ea FROM MasterTable WHERE d='"+devno+"'", null);	
		    if (mCursor.getCount() == 0){		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        roomno = mCursor.getString(mCursor.getColumnIndex("ea"));
		    }		       		       		       		           		 
			return roomno;
		}
	public String getdevnam_frmdevno(String devno) {
		final int DEFAULT_BASE = 0; String roomno="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT ec FROM MasterTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("ec"));
		}
		return roomno;
	}
		public int getCount_housenoroomnodevtype(String roomno,String devtype) {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM MasterTable WHERE a='"+roomno+"'"+"AND c='"+devtype+"'", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}	
		
		public int getCount_housenoroomnodevtypename(String roomno,String devtypename) {
			String curtyp=devtypename;
			String to_eliminate_model="CLSH";
			to_eliminate_model="'"+to_eliminate_model+"'";

			Cursor mCursor =null;

		    int DEFAULT_BASE = 0;

			if(curtyp.equals("CUR")){
				mCursor = dbs.rawQuery(
						"SELECT Count(*) FROM MasterTable WHERE a='" + roomno + "'" + "AND da='" + devtypename + "'" +"AND e!="+to_eliminate_model, null);
			}else {
				mCursor = dbs.rawQuery(
						"SELECT Count(*) FROM MasterTable WHERE a='" + roomno + "'" + "AND da='" + devtypename + "'", null);
			}

			//if(mCursor!=null)
		    if (mCursor.getCount() == 0)
		       DEFAULT_BASE=0;
		    else {
				mCursor.moveToFirst();
				DEFAULT_BASE=(int) mCursor.getLong(0);
			}
			if(mCursor!=null)mCursor.close();
		        return DEFAULT_BASE;
		}

		public int wtrgetCount_housenoroomnodevtypename(String roomno,String devtypename,String modtype1,String modtype2,String modtype3,String modtype4) {
		    final int DEFAULT_BASE = 0;
		    modtype1="'"+modtype1+"'"; modtype2="'"+modtype2+"'"; modtype3="'"+modtype3+"'"; modtype4="'"+modtype4+"'";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM MasterTable WHERE a='"+roomno+"'"+"AND da='"+devtypename+"'"+" AND e NOT IN ("+modtype1+","+modtype2+","+modtype3+","+modtype4+")", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}	
		public String[] getall_housenoroomnodevicetype(int count,String roomnum,String type) {		  
			roomnum="'"+roomnum+"'";
			type="'"+type+"'";
			
			    Cursor mCursor = dbs.rawQuery(
			    		 "SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
			    		            " FROM MasterTable WHERE a="+roomnum+" AND c="+type, null);
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }
			    return array;
		}
		public String[] getall_devicetypemodel(int count,String roomnum,String type) {
			String curtyp=type;
			String to_eliminate_model="CLSH";
			to_eliminate_model="'"+to_eliminate_model+"'";

			Cursor mCursor=null;
			roomnum="'"+roomnum+"'";
			type="'"+type+"'";

			if(curtyp.equals("CUR")){

				mCursor = dbs.rawQuery(
						"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
								" FROM MasterTable WHERE a=" + roomnum + " AND da=" + type + " AND e!="+to_eliminate_model, null);

			}else {
				 mCursor = dbs.rawQuery(
						"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
								" FROM MasterTable WHERE a=" + roomnum + " AND da=" + type, null);
			}
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("e"));
			        array[i] = uname;
			        i++;
			    }
			if(mCursor!=null)mCursor.close();
			    return array;
		}
		
		public String[] wtrgetall_devicetypemodel(int count,String roomnum,String type,String modtype1,String modtype2,String modtype3,String modtype4) {		  
			roomnum="'"+roomnum+"'";
			type="'"+type+"'";
			 modtype1="'"+modtype1+"'"; modtype2="'"+modtype2+"'"; modtype3="'"+modtype3+"'"; modtype4="'"+modtype4+"'";
			    Cursor mCursor = dbs.rawQuery(
			    		 "SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
			    		            " FROM MasterTable WHERE a="+roomnum+" AND da="+type+" AND e NOT IN ("+modtype1+","+modtype2+","+modtype3+","+modtype4+")", null);
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("e"));
			        array[i] = uname;
			        i++;
			    }
			    return array;
		}
		public String[] getall_housenoroomnodevicetypename(int count,String roomnum,String typename) {
			String curtyp=typename;
			String to_eliminate_model="CLSH";
			to_eliminate_model="'"+to_eliminate_model+"'";

			Cursor mCursor =null;

			roomnum="'"+roomnum+"'";
			typename="'"+typename+"'";

			if(curtyp.equals("CUR")){

				mCursor = dbs.rawQuery(
						"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
								" FROM MasterTable WHERE a=" + roomnum + " AND da=" + typename + "AND e!="+to_eliminate_model , null);


			}else {
				mCursor = dbs.rawQuery(
						"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
								" FROM MasterTable WHERE a=" + roomnum + " AND da=" + typename, null);
			}
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }
			if(mCursor!=null)mCursor.close();
			    return array;
		}



	public String[] getall_roomnodevicenames(int count,String roomnum,String typename) {
		String curtyp=typename;
		String to_eliminate_model="CLSH";
		to_eliminate_model="'"+to_eliminate_model+"'";

		Cursor mCursor =null;

		roomnum="'"+roomnum+"'";
		typename="'"+typename+"'";

		if(curtyp.equals("CUR")){

			mCursor = dbs.rawQuery(
					"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd,ec" +
							" FROM MasterTable WHERE a=" + roomnum + " AND da=" + typename + "AND e!="+to_eliminate_model , null);


		}else {
			mCursor = dbs.rawQuery(
					"SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd,ec" +
							" FROM MasterTable WHERE a=" + roomnum + " AND da=" + typename, null);
		}
		String[] array = new String[count];
		int i = 0;
		while(mCursor.moveToNext()){
			String uname = mCursor.getString(mCursor.getColumnIndex("ec"));
			array[i] = uname;
			i++;
		}
		if(mCursor!=null)mCursor.close();
		return array;
	}
		public String[] wtrgetall_housenoroomnodevicetypename(int count,String roomnum,String typename,String modtype1,String modtype2,String modtype3,String modtype4) {		  
			roomnum="'"+roomnum+"'";
			typename="'"+typename+"'";
			 modtype1="'"+modtype1+"'"; modtype2="'"+modtype2+"'"; modtype3="'"+modtype3+"'"; modtype4="'"+modtype4+"'";
			    Cursor mCursor = dbs.rawQuery(
			    		 "SELECT _id,a,b,c,d,e,f,g,h,i,da,db,dc,dd,de,df,dg,dh,di,dj,dk,dl,dk,dl,dm,dn,do,dp,dq,dr,ds,dt,du,dv,dw,dx,dy,dz,dza,dzb,dzc,dzd" +
			    		            " FROM MasterTable WHERE a="+roomnum+" AND da="+typename+" AND e NOT IN ("+modtype1+","+modtype2+","+modtype3+","+modtype4+")", null);
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }
			    return array;
		}
		public int getCount_housenoroomno(String roomno) {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM MasterTable WHERE a='"+roomno+"'", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}
		
		public Cursor checkroomnameexist(String roomname) {
			Cursor mCursor = dbs.rawQuery(
			            "SELECT a FROM MasterTable WHERE b='"+roomname+"'", null);	
			if(!(mCursor.getCount()>0))
				return null;
			if (mCursor != null) {
				mCursor.moveToFirst();
			}
			return mCursor;
			}

	public Cursor checkdevnameexist(String devname) {
		Cursor mCursor = dbs.rawQuery(
				"SELECT d FROM MasterTable WHERE ec='"+devname+"'", null);
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
		
		
		public int getCountgroups_housenoroomnodevtypename(String roomno,String devtypename) {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM MasterTable WHERE a='"+roomno+"'"+"AND da='"+devtypename+"'", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}	
		
		
		public String[] getGroupIds(int numberOfgroups,String devtype,String roomn ) {
			devtype="'"+devtype+"'";
             String[] columns = new String[]{KEY_ea};
             String[] groups = new String[numberOfgroups];

             int counter = 0;

        Cursor cursor = dbs.query(DATABASE_TABLE, columns, 
        		KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn, null, null, null, null);

             if (cursor != null){

             while(cursor.moveToNext()){
            	 groups[counter++] = cursor.getString(cursor.getColumnIndex(KEY_ea));                                   
            }

            }
        return groups;                        
}
		
		
		//public ArrayList<String> fetchUniquegroupids(String colKey,String devtype,String roomn) throws SQLException {
			public String[] fetchUniquegroupids(String colKey,String devtype,String roomn) throws SQLException {
			String[] cols = new String[] {KEY_ea};
	       // Cursor mCursor =
	       // dbs.query(true, DATABASE_TABLE, cols, null, null, null, null, colKey + " ASC", null);
	        devtype="'"+devtype+"'";
	        //String val="000";"'"+000+"'";
	       /* Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
					KEY_ROWID, KEY_a,KEY_b,KEY_c,KEY_d,KEY_e,KEY_f
					,KEY_g,KEY_h,KEY_i					
					,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de
					,KEY_ea,KEY_eb,KEY_ec,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh,KEY_ei,KEY_ej},
					KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn, null, KEY_ea, null, null, null);*/
	        
	        Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {					
					KEY_ea},
					KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn , null, KEY_ea, null, null, null);
	        
	        if (mCursor != null) {
	            mCursor.moveToFirst();
	        }
	            int colIndex =  mCursor.getColumnIndex(KEY_ea);
	            ArrayList<String> mArrayList = new ArrayList<String>();
	          
	            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
	            	if(!(mCursor.getString(colIndex).equals("000"))){
	                mArrayList.add(mCursor.getString(colIndex));	                
	            	}
	            }
	            String[] groupuni = mArrayList.toArray( new String[mArrayList.size()] );
	            return groupuni;
	    }
		//*********************************************************************************
			public String[] fetchdevnoundergroup(String groupid,String devtype,String roomn) throws SQLException {
				String[] cols = new String[] {KEY_d};
		      
		        devtype="'"+devtype+"'";
		        groupid="'"+groupid+"'";
				String toeliminate="'CLSH'";
		        Cursor mCursor = dbs.query(false, DATABASE_TABLE, new String[] {					
						KEY_d},
						KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn+" AND "+KEY_ea+ "="+groupid +" AND "+KEY_e+ "!="+toeliminate , null, KEY_d, null, null, null);
		        
		        if (mCursor != null) {
		            mCursor.moveToFirst();
		        }
		            int colIndex =  mCursor.getColumnIndex(KEY_d);
		            ArrayList<String> mdimArrayList = new ArrayList<String>();
		          
		            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){		            	
		            		mdimArrayList.add(mCursor.getString(colIndex));
		            }
		            String[] dimdevarrunder_group = mdimArrayList.toArray( new String[mdimArrayList.size()] );
		            return dimdevarrunder_group;
		    }
		//**************************************************************************


	   //*********************************************************************************
			public String[] fetchdevnounderdevtype(String devtype,String roomn) throws SQLException {
				String[] cols = new String[] {KEY_d};
		      
		        devtype="'"+devtype+"'";
		         
		        Cursor mCursor = dbs.query(false, DATABASE_TABLE, new String[] {					
						KEY_d},
						KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn, null, KEY_d, null, null, null);
		        
		        if (mCursor != null) {
		            mCursor.moveToFirst();
		        }
		            int colIndex =  mCursor.getColumnIndex(KEY_d);
		            ArrayList<String> mdimArrayList = new ArrayList<String>();
		          
		            for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){		            	
		            		mdimArrayList.add(mCursor.getString(colIndex));
		               // Log.d("msg", "dev dim no"+mCursor.getString(colIndex));
		            }
		            String[] dimdevarrunder_group = mdimArrayList.toArray( new String[mdimArrayList.size()] );
		            return dimdevarrunder_group;
		    }





	//*********************************************************************************
	public String fetchmodelunderdevtype(String devtype,String roomn,String devn) throws SQLException {
		String[] cols = new String[] {KEY_e};

		devtype="'"+devtype+"'";
		roomn="'"+roomn+"'";
		devn="'"+devn+"'";


		Cursor mCursor = dbs.query(false, DATABASE_TABLE, new String[] {
						KEY_e},
				KEY_da + "=" + devtype+" AND "+KEY_a + "=" + roomn+" AND "+KEY_d + "=" + devn, null, KEY_d, null, null, null);

		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		int colIndex =  mCursor.getColumnIndex(KEY_e);
		//ArrayList<String> mdimArrayList = new ArrayList<String>();

		//for(mCursor.moveToFirst();!mCursor.isAfterLast();mCursor.moveToNext()){
		//	mdimArrayList.add(mCursor.getString(colIndex));
		//	Log.d("msg", "dev dim no"+mCursor.getString(colIndex));
		//}
		//String[] devmodelarrunder_group = mdimArrayList.toArray( new String[mdimArrayList.size()] );

		return mCursor.getString(colIndex);
	}

		/*	public String[] getall_devicetypemodel(int count,String roomnum,String typename) {		  
				roomnum="'"+roomnum+"'";
				typename="'"+typename+"'";
				
				    Cursor mCursor = dbs.rawQuery(
				    		 "SELECT _id,a,b,c,cg,d,e,f,za,"
						+"zb,zc, zd, ze, zf,zg, zh, zi, zj, zk, zl, zm, zn,zo, zp, zq, zr, zs, zt, zu," 
						+"zv,zw, zx, zy, zz, ya, yb,yc, yd, ye, yf, yg, yh, yi, yj, yk, yl, ym, yn, yo,"
						+"yp, yq, yr, ys, yt, yu, yv, yw,"
						+"nfnl,fa1, fb2, fc3, fd4, fe5, fs1, fs2, fs3, fs4, fs5,an,af,asn,asf,afn,aff,"
						+"bn1,bn2,bn3,bn4,bn5,bn6,bn7,bn8,bn9,bn10,bn11,bn12,bn13,bn14,bn15,bn16,bn17,bn18,bn19,bn20,"
						+"bn21,bn22,bn23,bn24,bn25,bn26,bn27,bn28,bn29,bn30,bn31,bn32,bn33,bn34,bn35,bn36,bn37,bn38,bn39,bn40,"
						+"bn41,bn42,bn43,bn44,bn45,bn46,bn47,bn48,"
						+"bi1,bi2,bi3,bi4,bi5,bi6,bi7,bi8,bi9,bi10,bi11,bi12,bi13,bi14,bi15,bi16,bi17,bi18,bi19,bi20,"
						+"bi21,bi22,bi23,bi24,bi25,bi6,bi27,bi28,bi29,bi30,bi31,bi32,bi33,bi34,bi35,bi36,bi37,bi38,bi39,bi40,"
						+"bi41,bi42,bi3,bi44,bi45,bi46,bi47,bi48,da,db,dc,dd,de" +
				    		            " FROM SwitchBoardTable WHERE a="+roomnum+" AND da="+typename, null);
				    String[] array = new String[count];		   
				    int i = 0;
				    while(mCursor.moveToNext()){
				        String uname = mCursor.getString(mCursor.getColumnIndex("e"));
				        array[i] = uname;
				        i++;
				    }
				    return array;
			}*/


	public String get_image_name(String roomname) {
		StaticVariabes_div.log("from mastr"+roomname,"Master");
		String uroomnam="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT eb FROM MasterTable WHERE b='"+roomname+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uroomnam = mCursor.getString(mCursor.getColumnIndex("eb"));
		}
		StaticVariabes_div.log("from mastr uroomnam"+uroomnam,"Master");
		return uroomnam;
	}
}
