package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: SwbAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing Switchboard table[contains details of switchboard ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class SwbAdapter {
	SQLiteDatabase dbs;
	
	Context mcontext;
	
	public static final String KEY_ROWID = "_id";
	public static final String KEY_a = "a";
	public static final String KEY_b = "b";	
	public static final String KEY_c = "c";	//devtype
	public static final String KEY_cg = "cg";//image
	public static final String KEY_d = "d";	
	public static final String KEY_e = "e";
	public static final String KEY_f = "f";
	
	public static final String KEY_za = "za";	
//=========================================================================================
//                         Boolean
//*****************************************************************************
       
	public static final String KEY_zb = "zb";
	public static final String KEY_zc = "zc";
	public static final String KEY_zd = "zd";
	public static final String KEY_ze = "ze";
	public static final String KEY_zf = "zf";
	public static final String KEY_zg = "zg";
	public static final String KEY_zh = "zh";
	public static final String KEY_zi = "zi";
	public static final String KEY_zj = "zj";
	public static final String KEY_zk = "zk";
	public static final String KEY_zl = "zl";
	public static final String KEY_zm = "zm";
	public static final String KEY_zn = "zn";
	public static final String KEY_zo = "zo";
	public static final String KEY_zp = "zp";
	public static final String KEY_zq = "zq";
	public static final String KEY_zr = "zr";
	public static final String KEY_zs = "zs";
	public static final String KEY_zt = "zt";
	public static final String KEY_zu = "zu";
	public static final String KEY_zv = "zv";
	public static final String KEY_zw = "zw";
	public static final String KEY_zx = "zx";
	public static final String KEY_zy = "zy";
	public static final String KEY_zz = "zz";
	public static final String KEY_ya = "ya";
	public static final String KEY_yb = "yb";
	public static final String KEY_yc = "yc";
	public static final String KEY_yd = "yd";
	public static final String KEY_ye = "ye";
	public static final String KEY_yf = "yf";
	public static final String KEY_yg = "yg";
	public static final String KEY_yh = "yh";
	public static final String KEY_yi = "yi";
	public static final String KEY_yj = "yj";
	public static final String KEY_yk = "yk";
	public static final String KEY_yl = "yl";
	public static final String KEY_ym = "ym";
	public static final String KEY_yn = "yn";
	public static final String KEY_yo = "yo";
	public static final String KEY_yp = "yp";
	public static final String KEY_yq = "yq";
	public static final String KEY_yr = "yr";
	public static final String KEY_ys = "ys";
	public static final String KEY_yt = "yt";
	public static final String KEY_yu = "yu";
	public static final String KEY_yv = "yv";
	public static final String KEY_yw = "yw";
//=========================================================================================
//*****************************************************************************
    public static final String KEY_nfnl = "nfnl";
    public static final String KEY_fa1 = "fa1";
    public static final String KEY_fb2 = "fb2";
    public static final String KEY_fc3 = "fc3";
    public static final String KEY_fd4 = "fd4";
    public static final String KEY_fe5 = "fe5";
    public static final String KEY_fs1 = "fs1";
    public static final String KEY_fs2 = "fs2";
    public static final String KEY_fs3 = "fs3";
    public static final String KEY_fs4 = "fs4";
    public static final String KEY_fs5 = "fs5";
    public static final String KEY_an = "an";
    public static final String KEY_af = "af";
    public static final String KEY_asn = "asn";
    public static final String KEY_asf= "asf";
    public static final String KEY_afn = "afn";
    public static final String KEY_aff = "aff";

//=========================================================================================
//                         string(10) bulbname
//*****************************************************************************
    public static final String KEY_bn1 = "bn1";
    public static final String KEY_bn2 = "bn2";
    public static final String KEY_bn3 = "bn3";
    public static final String KEY_bn4 = "bn4";
    public static final String KEY_bn5 = "bn5";
    public static final String KEY_bn6 = "bn6";
    public static final String KEY_bn7 = "bn7";
    public static final String KEY_bn8 = "bn8";
    public static final String KEY_bn9 = "bn9";
    public static final String KEY_bn10 = "bn10";
    public static final String KEY_bn11 = "bn11";
    public static final String KEY_bn12 = "bn12";
    public static final String KEY_bn13 = "bn13";
    public static final String KEY_bn14 = "bn14";
    public static final String KEY_bn15 = "bn15";
    public static final String KEY_bn16 = "bn16";
    public static final String KEY_bn17 = "bn17";
    public static final String KEY_bn18 = "bn18";
    public static final String KEY_bn19 = "bn19";
    public static final String KEY_bn20 = "bn20";
    public static final String KEY_bn21 = "bn21";
    public static final String KEY_bn22 = "bn22";
    public static final String KEY_bn23 = "bn23";
    public static final String KEY_bn24 = "bn24";
    public static final String KEY_bn25 = "bn25";
    public static final String KEY_bn26 = "bn26";
    public static final String KEY_bn27 = "bn27";
    public static final String KEY_bn28 = "bn28";
    public static final String KEY_bn29 = "bn29";
    public static final String KEY_bn30 = "bn30";
    public static final String KEY_bn31 = "bn31";
    public static final String KEY_bn32 = "bn32";
    public static final String KEY_bn33 = "bn33";
    public static final String KEY_bn34 = "bn34";
    public static final String KEY_bn35 = "bn35";
    public static final String KEY_bn36 = "bn36";
    public static final String KEY_bn37 = "bn37";
    public static final String KEY_bn38 = "bn38";
    public static final String KEY_bn39 = "bn39";
    public static final String KEY_bn40 = "bn40";
    public static final String KEY_bn41 = "bn41";
    public static final String KEY_bn42 = "bn42";
    public static final String KEY_bn43 = "bn43";
    public static final String KEY_bn44 = "bn44";
    public static final String KEY_bn45 = "bn45";
    public static final String KEY_bn46 = "bn46";
    public static final String KEY_bn47 = "bn47";
    public static final String KEY_bn48 = "bn48";
        
//=========================================================================================
//                                  String(10)
//*****************************************************************************
       public static final String KEY_bi1 = "bi1";
       public static final String KEY_bi2 = "bi2";
       public static final String KEY_bi3 = "bi3";
       public static final String KEY_bi4 = "bi4";
       public static final String KEY_bi5 = "bi5";
       public static final String KEY_bi6 = "bi6";
       public static final String KEY_bi7 = "bi7";
       public static final String KEY_bi8 = "bi8";
       public static final String KEY_bi9 = "bi9";
       public static final String KEY_bi10= "bi10";
       public static final String KEY_bi11 = "bi11";
       public static final String KEY_bi12 = "bi12";
       public static final String KEY_bi13 = "bi13";
       public static final String KEY_bi14 = "bi14";
       public static final String KEY_bi15 = "bi15";
       public static final String KEY_bi16 = "bi16";
       public static final String KEY_bi17 = "bi17";
       public static final String KEY_bi18 = "bi18";
       public static final String KEY_bi19 = "bi19";
       public static final String KEY_bi20 = "bi20";
       public static final String KEY_bi21 = "bi21";
       public static final String KEY_bi22 = "bi22";
       public static final String KEY_bi23 = "bi23";
       public static final String KEY_bi24 = "bi24";
       public static final String KEY_bi25 = "bi25";
       public static final String KEY_bi26 = "bi26";
       public static final String KEY_bi27 = "bi27";
       public static final String KEY_bi28 = "bi28";
       public static final String KEY_bi29 = "bi29";
       public static final String KEY_bi30 = "bi30";
       public static final String KEY_bi31 = "bi31";
       public static final String KEY_bi32 = "bi32";
       public static final String KEY_bi33 = "bi33";
       public static final String KEY_bi34 = "bi34";
       public static final String KEY_bi35 = "bi35";
       public static final String KEY_bi36 = "bi36";
       public static final String KEY_bi37 = "bi37";
       public static final String KEY_bi38 = "bi38";
       public static final String KEY_bi39 = "bi39";
       public static final String KEY_bi40 = "bi40";
       public static final String KEY_bi41 = "bi41";
       public static final String KEY_bi42 = "bi42";
       public static final String KEY_bi43 = "bi43";
       public static final String KEY_bi44 = "bi44";
       public static final String KEY_bi45 = "bi45";
       public static final String KEY_bi46 = "bi46";
       public static final String KEY_bi47 = "bi47";
       public static final String KEY_bi48 = "bi48";
//=========================================================================================
//*****************************************************************************
	   public static final String KEY_da = "da";
	   public static final String KEY_db = "db";
	   public static final String KEY_dc = "dc";
	   public static final String KEY_dd = "dd"; 
	   public static final String KEY_de = "de";		
//**************************************************************************
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
   //*******************************************************************************
  	
  	public static String ORIGINAL_PATH =null;
  	public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
	private static final String DATABASE_TABLE = "SwitchBoardTable";
	private static final String TAG1="Swb Adap - ";

	String[] bulbnumarr=new String[]{KEY_bn1,KEY_bn2,KEY_bn3,KEY_bn4,KEY_bn5,KEY_bn6,KEY_bn7,KEY_bn8,KEY_bn9,KEY_bn10,KEY_bn11,KEY_bn12};
	
	public SwbAdapter(Context context) {
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
	//****************************************************************************************
	                            //delete//
	
	  public boolean deleteDatabase(String DATABASE_NAME) { 
	    //  mDbHelper.close(); 
	      dbs.close(); 
	      if (mcontext.deleteDatabase(DATABASE_NAME)) { 
				StaticVariabes_div.log("deleteDatabase(): database deleted", TAG1); 
	            return true; 
	      } else { 
				StaticVariabes_div.log("deleteDatabase(): database NOT deleted.", TAG1);  
	        return false; 
	      }
	
	    } 

	
	//**************************************swbdetails******************************************
		
	
	public int getCounthouseno(String homeno) {
	    final int DEFAULT_BASE = 0;
	    Cursor mCursor = dbs.rawQuery(
	           // "SELECT Count(*) FROM SwitchBoardTable WHERE dd='"+homeno+"'", null);
	    "SELECT Count(*) FROM SwitchBoardTable", null);
	    if (mCursor.getCount() == 0)
	        return DEFAULT_BASE;
	    else
	        mCursor.moveToFirst();
	        return (int) mCursor.getLong(0);
	}
	public int getCountswbno(String homeno) {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM SwitchBoardTable", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}
		public String getroomno(String roomname) {
		    final int DEFAULT_BASE = 0; String uname="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT a FROM SwitchBoardTable WHERE b='"+roomname+"'", null);	
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
		   String uroomnam="";
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT b FROM SwitchBoardTable WHERE a='"+roomno+"'", null);	
		    if (mCursor.getCount() == 0){
		    	
		    }
		    else{
		        mCursor.moveToFirst();
		        uroomnam = mCursor.getString(mCursor.getColumnIndex("b"));
		    }		       		       		           		 
			return uroomnam;
		}
		
		public String getroomno_frmdevno(String devno) {
			   String uroomnam="";
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT a FROM SwitchBoardTable WHERE d='"+devno+"'", null);	
			    if (mCursor.getCount() == 0){
			    	
			    }
			    else{
			        mCursor.moveToFirst();
			        uroomnam = mCursor.getString(mCursor.getColumnIndex("a"));
			    }			       		       		           		 
				return uroomnam;
			}

	public String getdevid_frmdevno(String devno) {
		String uroomnam="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT f FROM SwitchBoardTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uroomnam = mCursor.getString(mCursor.getColumnIndex("f"));
		}
		return uroomnam;
	}

		public String getdevtype_frmdevno(String devno) {
			   String uroomnam="";
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT da FROM SwitchBoardTable WHERE d='"+devno+"'", null);	
			    if (mCursor.getCount() == 0){
			    	
			    }
			    else{
			        mCursor.moveToFirst();
			        uroomnam = mCursor.getString(mCursor.getColumnIndex("da"));
			    }			       		       		           		 
				return uroomnam;
			}

	public String getdevversion_frmdevno(String devno) {
		String uroomnam="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT ed FROM SwitchBoardTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uroomnam = mCursor.getString(mCursor.getColumnIndex("ed"));
		}
		return uroomnam;
	}
		public String getmodtype_frmdevno(String devno) {
			   String uroomnam="";
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT e FROM SwitchBoardTable WHERE d='"+devno+"'", null);	
			    if (mCursor.getCount() == 0){
			    	
			    }
			    else{
			        mCursor.moveToFirst();
			        uroomnam = mCursor.getString(mCursor.getColumnIndex("e"));
			    }			       		       		           		 
				return uroomnam;
			}

		public String getgrouptype_frmdevno(String devno) {
			   String uroomnam="";
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT ea FROM SwitchBoardTable WHERE d='"+devno+"'", null);	
			    if (mCursor.getCount() == 0){
			    	
			    }
			    else{
			        mCursor.moveToFirst();
			        uroomnam = mCursor.getString(mCursor.getColumnIndex("ea"));
			    }			       		       		           		 
				return uroomnam;
			}
	public String getdevnam_frmdevno(String devno) {
		String uroomnam="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT ec FROM SwitchBoardTable WHERE d='"+devno+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uroomnam = mCursor.getString(mCursor.getColumnIndex("ec"));
		}
		return uroomnam;
	}
		public int getCount_housenoroomnodevtype(String roomno,String devtype) {
		    final int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM SwitchBoardTable WHERE a='"+roomno+"'"+" AND c='"+devtype+"'", null);
		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else
		        mCursor.moveToFirst();
		        return (int) mCursor.getLong(0);
		}
		
		public int getCount_housenoroomnodevtypename(String roomno,String devtypename) {
		    int DEFAULT_BASE = 0;
		    Cursor mCursor = dbs.rawQuery(
		            "SELECT Count(*) FROM SwitchBoardTable WHERE a='"+roomno+"'"+" AND da='"+devtypename+"'", null);

		    if (mCursor.getCount() == 0)
		        return DEFAULT_BASE;
		    else {
				mCursor.moveToFirst();
				DEFAULT_BASE = (int) mCursor.getLong(0);

			}
			if(mCursor!=null)mCursor.close();

			return DEFAULT_BASE;
		}
		public String[] getall_housenoroomnodevicetypename(int count,String roomnum,String typename) {		  
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
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }
			if(mCursor!=null)mCursor.close();
			    return array;
		}
		public String[] getall_devicetypemodel(int count,String roomnum,String typename) {		  
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
			if(mCursor!=null)mCursor.close();
			    return array;
		}

	public String[] getall_devicenames(int count,String roomnum,String typename) {
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
						+"bi41,bi42,bi3,bi44,bi45,bi46,bi47,bi48,da,db,dc,dd,de,eb,ec" +
						" FROM SwitchBoardTable WHERE a="+roomnum+" AND da="+typename, null);
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
		public String[] getall_housenoroomnodevicetype(int count,String roomnum,String type) {		  
			roomnum="'"+roomnum+"'";
			type="'"+type+"'";
			
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
			    		            " FROM SwitchBoardTable WHERE a="+roomnum+" AND c="+type, null);
			    String[] array = new String[count];		   
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }
			    return array;
		}
		//***************************************************************************************

				public boolean updateiconsnw(String devno,String icon,String bulbkey,String roomnum){
					//devno="'"+devno+"'";
					ContentValues args = new ContentValues();
					if(bulbkey.equals("bulb1")){
					     args.put(KEY_bi1, icon);
					}else if(bulbkey.equals("bulb2")){
						 args.put(KEY_bi2, icon);
					}else if(bulbkey.equals("bulb3")){
						 args.put(KEY_bi3, icon);
					}else if(bulbkey.equals("bulb4")){
						 args.put(KEY_bi4, icon);
					}else if(bulbkey.equals("bulb5")){
						 args.put(KEY_bi5, icon);
					}else if(bulbkey.equals("bulb6")){
						 args.put(KEY_bi6, icon);
					}else if(bulbkey.equals("bulb7")){
						 args.put(KEY_bi7, icon);
					}else if(bulbkey.equals("bulb8")){
						 args.put(KEY_bi8, icon);
					}else if(bulbkey.equals("bulb9")){
						 args.put(KEY_bi9, icon);
					}else if(bulbkey.equals("bulb10")){
						 args.put(KEY_bi10, icon);
					}else if(bulbkey.equals("bulb11")){
						 args.put(KEY_bi11, icon);
					}else if(bulbkey.equals("bulb12")){
						 args.put(KEY_bi12, icon);
					}
				 boolean test=   dbs.update(DATABASE_TABLE, args, KEY_d + "=" + devno+" AND "+KEY_a + "=" + roomnum, null)>0;
				return test;
				   // boolean swb=dbs.update("SwitchBoardTable", args, KEY_b + "=" + roomname, null)>0;
				}
			
				
				public boolean updatebulbname(String devn,int bulbno,String name){
					devn="'"+devn+"'"; boolean bnmk=false;
					ContentValues args = new ContentValues();
				    args.put(bulbnumarr[bulbno], name);
				   bnmk= dbs.update(DATABASE_TABLE, args, KEY_d + "=" + devn, null)>0;
				   return bnmk;
				}
			
				public void updateroomimg(String devn,String imgpath){
					devn="'"+devn+"'";
					ContentValues args = new ContentValues();
				    args.put(KEY_cg, imgpath);
				    dbs.update(DATABASE_TABLE, args, KEY_d + "=" + devn, null);
				}
				
				//***************************************************************************************
				
				
		public String[] getalldevnohome(int count,String homeno) {
			   // final int DEFAULT_BASE = 0;
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT d FROM SwitchBoardTable", null);
			    String[] array = new String[count];
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("d"));
			        array[i] = uname;
			        i++;
			    }		    		  
				return array;
			}
		
		public String[] getallroomnohome(int count,String homeno) {
			  
			    Cursor mCursor = dbs.rawQuery(
			            "SELECT a FROM SwitchBoardTable ", null);
			    String[] array = new String[count];
			    int i = 0;
			    while(mCursor.moveToNext()){
			        String uname = mCursor.getString(mCursor.getColumnIndex("a"));
			        array[i] = uname;
			        i++;
			    }		    	
				return array;
			}
	
	public Cursor fetch_rowdevnoroomnohouseno(String devno,String roomnum) throws SQLException {
		devno="'"+devno+"'";
		roomnum="'"+roomnum+"'";
		
		Cursor mCursor = dbs.query(true, DATABASE_TABLE, new String[] {
				KEY_ROWID, KEY_a,KEY_b,KEY_c,KEY_cg,KEY_d,KEY_e,KEY_f
				,KEY_za,KEY_zb,KEY_zc,KEY_zd,KEY_ze,KEY_zf
				,KEY_zg,KEY_zh,KEY_zi,KEY_zj,KEY_zk,KEY_zl,KEY_zm,KEY_zn
				,KEY_zo,KEY_zp,KEY_zq,KEY_zr,KEY_zs,KEY_zt,KEY_zu,KEY_zv
				,KEY_zw,KEY_zx,KEY_zy,KEY_zz
				,KEY_ya,KEY_yb,KEY_yc,KEY_yd,KEY_ye,KEY_yf,KEY_yg,KEY_yh
				,KEY_yi,KEY_yj,KEY_yk,KEY_yl,KEY_ym,KEY_yn,KEY_yo
				,KEY_yp,KEY_yq,KEY_yr,KEY_ys,KEY_yt,KEY_yu,KEY_yv,KEY_yw
				,KEY_nfnl,KEY_fa1,KEY_fb2,KEY_fc3,KEY_fd4,KEY_fe5
				,KEY_fs1,KEY_fs2,KEY_fs3,KEY_fs4,KEY_fs5,KEY_an,KEY_af,KEY_asn,KEY_asf,KEY_afn,KEY_aff
				,KEY_bn1,KEY_bn2,KEY_bn3,KEY_bn4,KEY_bn5,KEY_bn6,KEY_bn7,KEY_bn8,KEY_bn9,KEY_bn10
				,KEY_bn11,KEY_bn12,KEY_bn13,KEY_bn14,KEY_bn15,KEY_bn16,KEY_bn17,KEY_bn18,KEY_bn19,KEY_bn20
				,KEY_bn21,KEY_bn22,KEY_bn23,KEY_bn24,KEY_bn25,KEY_bn26,KEY_bn27,KEY_bn28,KEY_bn29,KEY_bn30
				,KEY_bn31,KEY_bn32,KEY_bn33,KEY_bn34,KEY_bn35,KEY_bn36,KEY_bn37,KEY_bn38,KEY_bn39,KEY_bn40
				,KEY_bn41,KEY_bn42,KEY_bn43,KEY_bn44,KEY_bn45,KEY_bn46,KEY_bn47,KEY_bn48
				,KEY_bi1,KEY_bi2,KEY_bi3,KEY_bi4,KEY_bi5,KEY_bi6,KEY_bi7,KEY_bi8,KEY_bi9,KEY_bi10
				,KEY_bi11,KEY_bi12,KEY_bi13,KEY_bi14,KEY_bi15,KEY_bi16,KEY_bi17,KEY_bi18,KEY_bi19,KEY_bi20
				,KEY_bi21,KEY_bi22,KEY_bi23,KEY_bi24,KEY_bi25,KEY_bi26,KEY_bi27,KEY_bi28,KEY_bi29,KEY_bi30
				,KEY_bi31,KEY_bi32,KEY_bi33,KEY_bi34,KEY_bi35,KEY_bi36,KEY_bi37,KEY_bi38,KEY_bi39,KEY_bi40
				,KEY_bi41,KEY_bi42,KEY_bi43,KEY_bi44,KEY_bi45,KEY_bi46,KEY_bi47,KEY_bi48
				,KEY_da,KEY_db,KEY_dc,KEY_dd,KEY_de},
				KEY_d + "=" + devno+" AND "+KEY_a + "=" + roomnum, null, null, null, null, null);
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
	
	public Cursor checkroomnameexist(String roomname) {
		Cursor mCursor = dbs.rawQuery(
		            "SELECT a FROM SwitchBoardTable WHERE b='"+roomname+"'", null);	
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor checkdevnameexist(String devname) {
		Cursor mCursor = dbs.rawQuery(
				"SELECT d FROM SwitchBoardTable WHERE ec='"+devname+"'", null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	/*public void updatebulbname(String devn,int bulbno,String name){
		devn="'"+devn+"'";
		ContentValues args = new ContentValues();
	    args.put(bulbnumarr[bulbno], name);
	    database.update(DATABASE_TABLE, args, KEY_d + "=" + devn, null);
	}*/
	public void updatename(String roomname,String newname){
		roomname="'"+roomname+"'";
		ContentValues args = new ContentValues();
	   args.put(KEY_b, newname);
	   dbs.update(DATABASE_TABLE, args, KEY_b + "=" + roomname, null);
	}
	public void updatename_whr_nois(String roomno,String newname){
		roomno="'"+roomno+"'";
		ContentValues args = new ContentValues();
	   args.put(KEY_b, newname);
	   dbs.update(DATABASE_TABLE, args, KEY_a + "=" + roomno, null);
	}
	/*public void updateroomimg(String devn,String img){
		devn="'"+devn+"'";
		ContentValues args = new ContentValues();
	    args.put(KEY_cg, img);
	    database.update(DATABASE_TABLE, args, KEY_d + "=" + devn, null);
	}*/
///////////////////////////////////////////////////////////////////////////////////////////
	
	public void fetchallroonamesjoin(){
		
	}
	
	
	
	
	public String[] getall_roomnameswb(int count) {		  		
		
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
				    		            " FROM SwitchBoardTable", null);
	    String[] array = new String[count];		   
	    int i = 0;
	    while(mCursor.moveToNext()){
	        String uname = mCursor.getString(mCursor.getColumnIndex("b"));
	        array[i] = uname;
	        i++;
	    }
	    return array;
}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
/*	public TreeSet<String> RoomNameList(){
		
		TreeSet<String>UniqueRoomNames=new TreeSet<String>();	//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();	//list for storing room names temporarly
		Cursor mcursor1 = null,mcursor2= null,mcursor3 = null;					//initialize cursor
		String RoomName=null;
		
		//Fetching room names from switch board table
		try{
			mcursor1=dbs.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomName},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1!=null&& mcursor1.getCount()>0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					RoomName=mcursor1.getString(mcursor1.getColumnIndex(SWB_RoomName));	//getting room name from cursor
					roomNames.add(RoomName);	// adding room names one by one in array list
				}while(mcursor1.moveToNext());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//fetch room from master table
		try{
			mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_RoomName},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();	//placing cursor to first position
				do{
					//getting room name from cursor
					RoomName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_RoomName));
					roomNames.add(RoomName);	// adding room names one by one in array list
				}while(mcursor2.moveToNext());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//fetch room from camera table
		try{
			mcursor3=sdb.query(true, CAM_TABLE, new String[]{CAM_RoomName},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()>0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room name from cursor
					RoomName=mcursor3.getString(mcursor3.getColumnIndex(CAM_RoomName));
					roomNames.add(RoomName);	// adding room names one by one in array list
				}while(mcursor3.moveToNext());
				
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
		//fetch room names from irBlaster_config. table
		try{
			//opening database
			IRB_Adapter_ir irb_adapter=new IRB_Adapter_ir(mcontext);
			
			//opening database
			irb_adapter.open();
			
			TreeSet<String> uniqueRoomsList_wls=new TreeSet<String>();
			uniqueRoomsList_wls=irb_adapter.IRB_RoomNameList();
			if(uniqueRoomsList_wls.size()>0){
				//adding ir rooms in room list
				roomNames.addAll(uniqueRoomsList_wls);
			}
			//closing database
			irb_adapter.close();
		  
				
			
		}catch(Exception e){
			e.printStackTrace();
		}
				

		//fetch room names from wireless config. table
		try{
			//opening database
			WirelessConfigurationAdapter WhouseDB=new WirelessConfigurationAdapter(mcontext);
			boolean isDbExists=WhouseDB.checkdb();
			if(isDbExists){
				WhouseDB.open();
				
				TreeSet<String> uniqueRoomsList_wls=new TreeSet<String>();
				uniqueRoomsList_wls=WhouseDB.WirelessPanelsRoomNameList(); 
				if(uniqueRoomsList_wls.size()>0){
					//adding wireless rooms in room list
					roomNames.addAll(uniqueRoomsList_wls);
				}
				//closing database
				WhouseDB.close();
			}else{
			    Log.d("TAG","wireless database not exists!"); 	 
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
		 
		//adding unique rooms from array list into tree list
		UniqueRoomNames.addAll(roomNames);
		
		for(String rm:UniqueRoomNames){
			Log.d("TAG","Room Name :"+rm);
		}
		
		Log.d("TAG","Room List Size is :"+UniqueRoomNames.size());
		
		return UniqueRoomNames;
	}*/


	public String get_image_name(String roomname) {
		String uroomnam="";
		Cursor mCursor = dbs.rawQuery(
				"SELECT eb FROM SwitchBoardTable WHERE b='"+roomname+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uroomnam = mCursor.getString(mCursor.getColumnIndex("eb"));
		}
		return uroomnam;
	}
}
