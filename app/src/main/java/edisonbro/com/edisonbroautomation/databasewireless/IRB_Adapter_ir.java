package edisonbro.com.edisonbroautomation.databasewireless;

/**
 *  FILENAME: IRB_Adapter_ir.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing ir blaster table .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeSet;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;

public class IRB_Adapter_ir {

	public static SQLiteDatabase sdb;

	Context mcontext;
	public String ORIGINAL_PATH =null;
	public String OriginalDataBase = null; 


	private static final String DATABASE_TABLE = "IRBlasterTable";

	public static final String ROWID = "_id";
	public static final String MODEL_NO = "mn";
	public static final String BRAND_NAME = "bn";
	public static final String MAKE = "m";

	public static final String HOUSE_NO = "de";
	public static final String HOUSE_NAME = "dd";
	public static final String ROOM_NO = "a";
	public static final String ROOM_NAME = "b";
	public static final String DEV_TYPE = "c";
	public static final String DEV_NO = "d";
	public static final String DEV_NAME = "e";
	public static final String KEY_NUMBER = "kno";
	public static final String KEY_LOC_CODE = "klc";
	public static final String KEY_GROUP = "kg";
	public static final String KEY_NAME = "knm";
	public static final String KEY_STATUS = "ks";
	public static final String KEY_DATA = "kd";
	public static final String KEY_ACTUAL_DATA = "kad";

	public static final String DATA1 = "ea";
	public static final String DATA2 = "eb";
	public static final String DATA3 = "ec";
	public static final String DATA4 = "ed";
	public static final String DATA5 = "ee";
	public static final String DATA6 = "ef";
	public static final String DATA7 = "eg";
	public static final String DATA8 = "eh";
	public static final String DATA9 = "ei";
	public static final String DATA10 = "ej";

	public String DATABASE_PATH = null;

	//IRCONFIG TABLE
	private static final String DATABASE_TABLE_CFG = "IRConfigTable";

	public static final String IRCNFG_ROWID      ="_id"; 
	public static final String IRCNFG_HOUSE_NO   ="de";
	public static final String IRCNFG_HOUSE_NAME ="dd";
	public static final String IRCNFG_RoomNo     ="a";
	public static final String IRCNFG_RoomName   ="b";
	public static final String IRCNFG_DeviceNo   ="d";	
	public static final String IRCNFG_DeviceName ="e";	
	public static final String IRCNFG_DevMacID   ="f";	
	public static final String IRCNFG_DeviceIP   ="i";
	public static final String IRCNFG_DevicePORT ="p"; 
	public static final String IRCNFG_DeviceBroadcastPORT ="bp"; 
	public static final String IRCNFG_net_SSID   ="ss";	
	public static final String IRCNFG_net_PASSWORD  ="ps"; 

	//IRCONFIG TABLE
	private static final String DATABASE_AC_TABLE = "IRAcTable";

	public static final String AC_ROWID      ="_id";  
	public static final String AC_ModelNo     ="mn";
	public static final String AC_Tempertaure   ="tp";
	public static final String AC_SwingLR   ="slr";	
	public static final String AC_SwingUD ="sud";	
	public static final String AC_FanSpeed   ="fn";	
	public static final String AC_SleepTimer   ="st";
	public static final String AC_Mode="md"; 
	public static final String AC_RoomNo    ="a";
	public static final String AC_DeviceNo="d"; 
	public static final String AC_DeviceName="e"; 
	public static final String AC_KeyActualData ="kad";  
	public static final String AC_LastOperated="los"; 

	String IR_DEFAULT_SSID = "IR BLASTER";


	public IRB_Adapter_ir(Context context) {
		mcontext = context;		//passing context of current activity
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
		OriginalDataBase = StaticVariables.HOUSE_NAME+".db";
	}


	//checking if database existing 
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
		}

		return sdb1 != null ? true : false;

	}	

	//opening database
	public void open() {
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;
			StaticVariabes_div.log("DATABASE", "Databse Path is " + path);
			//making database instance read/write able
			sdb = SQLiteDatabase.openDatabase(path, null,
					SQLiteDatabase.OPEN_READWRITE
					+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);

		} catch (Exception e) {
			e.printStackTrace();
		}
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

	//*******************************DATABASE OPERTAIONS**************************//

	//Fetching Max Wireless Device Number from ir config table
	public int Max_Number(String Column){
		int maxDevNo=0;
		try{
			SQLiteStatement stmt = sdb.compileStatement("select MAX("+Column+") from "+DATABASE_TABLE_CFG+";");
			maxDevNo = (int) stmt.simpleQueryForLong();
		}catch(Exception e){
			e.printStackTrace();
		}
		return maxDevNo;
	}	

	//Fetching Max port number (broadcast and tcp)
	public int maxPortNumber(String Column,String macid){
		int maxPortNo=0;
		Cursor mcursor=null;
		try{ 

			mcursor=sdb.query(true, DATABASE_TABLE_CFG, new String[]{Column},
					IRCNFG_DevMacID+"='"+macid+"'", null, null, null, null, null);
			/*checking if ir is already configured then
			 *  returning previous assigned port no 
			 */  
			if(mcursor!=null && mcursor.getCount()>0){
				StaticVariables.printLog("TAG","IR Already Exists");
				mcursor.moveToFirst();
				maxPortNo=mcursor.getInt(mcursor.getColumnIndex(Column));

			}else{
				//fetching max port number from table
				maxPortNo=Max_Number(Column);
				if(maxPortNo>0){  
					//incrementing max port number by 1
					maxPortNo++; 
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing the cursor
			if(mcursor!=null)
				mcursor.close(); 
		}
		StaticVariables.printLog("TAG","port from db :"+maxPortNo);

		return maxPortNo;
	}

	//inserting data in irconfig table
	public boolean insertORupdate_cfg(int house_no, String house_name, int roomno,
			String roomname, int devno,String DevName,String MacId ,String ip,
			int port,int broadcastPort,String ssid,String password){
		boolean inserted=false ,isdeviceExists=false;

		String result=isIRconfigured(MacId);
		if(result!=null){
			if(result.startsWith("TRUE")){
				StaticVariables.printLog("DB","new dev no  :"+devno);
				devno=Integer.parseInt(result.substring(4));
				isdeviceExists=true;
				StaticVariables.printLog("DB","old dev no set :"+devno);

			}
		}
		if(isdeviceExists){
			//updating existing records 
			try{
				ContentValues cv = new ContentValues(); 
				cv.put(IRCNFG_RoomName, roomname);
				cv.put(IRCNFG_RoomNo, roomno);
				cv.put(IRCNFG_DeviceNo, devno); 
				cv.put(IRCNFG_DeviceName, DevName);  
				cv.put(IRCNFG_DeviceIP, ip);
				cv.put(IRCNFG_DevicePORT, port);
				cv.put(IRCNFG_DeviceBroadcastPORT, broadcastPort);  
				cv.put(IRCNFG_net_SSID, ssid);
				cv.put(IRCNFG_net_PASSWORD, password); 

				inserted= sdb.update(DATABASE_TABLE_CFG, cv,IRCNFG_DevMacID+"='"+MacId+"'", null)>0?true:false;
				StaticVariables.printLog("DB","updated");

				//updating ir blaster table
				ContentValues cv2 = new ContentValues(); 
				cv2.put(ROOM_NAME, roomname);
				cv2.put(ROOM_NO, roomno);

				boolean update2= sdb.update(DATABASE_TABLE, cv2,DEV_NO+"="+devno+"", null)>0?true:false;
				StaticVariables.printLog("DB","updated2 :"+update2);

				//updating ir ac table
				ContentValues cv3 = new ContentValues();  
				cv3.put(AC_RoomNo, roomno);
				boolean update3= sdb.update(DATABASE_AC_TABLE, cv3,AC_DeviceNo+"="+devno+"", null)>0?true:false;
				StaticVariables.printLog("DB","updated3 :"+update3);

			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			//inserting new records
			try{
				ContentValues cv = new ContentValues();

				cv.put(IRCNFG_HOUSE_NO, house_no);
				cv.put(IRCNFG_HOUSE_NAME, house_name);

				cv.put(IRCNFG_RoomName, roomname);
				cv.put(IRCNFG_RoomNo, roomno);
				cv.put(IRCNFG_DeviceNo, devno); 
				cv.put(IRCNFG_DeviceName, DevName); 
				cv.put(IRCNFG_DevMacID, MacId); 
				cv.put(IRCNFG_DeviceIP, ip);
				cv.put(IRCNFG_DevicePORT, port);
				cv.put(IRCNFG_DeviceBroadcastPORT, broadcastPort);  
				cv.put(IRCNFG_net_SSID, ssid);
				cv.put(IRCNFG_net_PASSWORD, password); 

				cv.put(DATA1, "empty");
				cv.put(DATA2, "empty");
				cv.put(DATA3, "empty");
				cv.put(DATA4, "empty");
				cv.put(DATA5, "empty");
				cv.put(DATA6, "empty");
				cv.put(DATA7, "empty");
				cv.put(DATA8, "empty");
				cv.put(DATA9, "empty");
				cv.put(DATA10,"empty");
				inserted= sdb.insert(DATABASE_TABLE_CFG, null, cv)>0?true:false;
				StaticVariables.printLog("DB","inserted");
			}catch(Exception e){
				e.printStackTrace();
			}
		}

		return inserted;
	}

	//fetching all records form config. table
	public Cursor fetchAllConfigRecords(int deviceNo) {
		return sdb.query(true, DATABASE_TABLE_CFG, new String[] {IRCNFG_HOUSE_NO,
				IRCNFG_HOUSE_NAME, IRCNFG_RoomNo, IRCNFG_RoomName, IRCNFG_DeviceNo,
				IRCNFG_DeviceName, IRCNFG_DevMacID,IRCNFG_DeviceIP,IRCNFG_DevicePORT
				,IRCNFG_DeviceBroadcastPORT,IRCNFG_net_SSID,IRCNFG_net_PASSWORD
		}, IRCNFG_DeviceNo+"="+deviceNo, null, null, null, null, null);
	}

	//checking if device name or room name already used or not
	public boolean isDevInfo(String column,String value){
		boolean isExists=false;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, DATABASE_TABLE_CFG, 
					new String[]{column}, column+"='"+value+"'", null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				isExists=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return isExists;
	}

	//checking if device name or room name already used or not
	public boolean isSamedevExists(String roomName,String devName){
		boolean isExists=false;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, DATABASE_TABLE_CFG, 
					new String[]{IRCNFG_DeviceName}, IRCNFG_DeviceName+"='"+devName+
					"' AND "+IRCNFG_RoomName+"='"+roomName+"'", null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				isExists=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return isExists;
	}	
	//getting devno or room no of selected room/devname
	public int getNumber(String column1,String column2,String value){
		int num=0;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, DATABASE_TABLE_CFG, new String[]{column1,column2}, column1+"='"+value+"'", null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				num=mcursor.getInt(mcursor.getColumnIndex(column2));
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return num;
	}

	//method to check if ir is already configured or not
	public String isIRconfigured(String IRmacId){
		String isConfigured="FALSE";
		Cursor mcursor=null;
		int Devno=0;
		try{
			mcursor=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_DeviceNo,IRCNFG_DevMacID}, IRCNFG_DevMacID+"='"+IRmacId+"'", null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				Devno=mcursor.getInt(mcursor.getColumnIndex(IRCNFG_DeviceNo));

				isConfigured="TRUE"+Devno;
				StaticVariables.printLog("TAG", "MAC ID EXISTS");
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return isConfigured;
	}

	//inserting data in ir table
	public long insert(String model_no,String brand_name,String maker,int house_no, String house_name, int roomno,
			String roomname, int devno, String devname, String devtype,
			int keynumber, int keyloccode, String key_name, String key_group,
			String key_data,String key_actual_data, int key_status) {

		ContentValues cv = new ContentValues();

		cv.put(MODEL_NO, model_no);
		cv.put(BRAND_NAME, brand_name);
		cv.put(MAKE, maker);

		cv.put(HOUSE_NO, house_no);
		cv.put(HOUSE_NAME, house_name);

		cv.put(ROOM_NAME, roomname);
		cv.put(ROOM_NO, roomno);
		cv.put(DEV_NO, devno);
		cv.put(DEV_NAME, devname);
		cv.put(DEV_TYPE, devtype);

		cv.put(KEY_NUMBER, keynumber);
		cv.put(KEY_LOC_CODE, keyloccode);

		cv.put(KEY_NAME, key_name);
		cv.put(KEY_GROUP, key_group);
		cv.put(KEY_DATA, key_data);

		cv.put(KEY_ACTUAL_DATA, key_actual_data);

		cv.put(KEY_STATUS, key_status);

		cv.put(DATA1, "empty");
		cv.put(DATA2, "empty");
		cv.put(DATA3, "empty");
		cv.put(DATA4, "empty");
		cv.put(DATA5, "empty");
		cv.put(DATA6, "empty");
		cv.put(DATA7, "empty");
		cv.put(DATA8, "empty");
		cv.put(DATA9, "empty");
		cv.put(DATA10,"empty");
		return sdb.insert(DATABASE_TABLE, null, cv);
	}

	public boolean update(String model_no,String brand_name,String maker,int house_no, String house_name, int roomno,
			String roomname, int devno, String devname, String devtype,
			int keynumber, int keyloccode, String key_name, String key_group,
			String key_data,String key_actual_data, int key_status) {

		ContentValues cv = new ContentValues();

		cv.put(MODEL_NO, model_no);
		cv.put(BRAND_NAME, brand_name);
		cv.put(MAKE, maker);

		cv.put(HOUSE_NO, house_no);
		cv.put(HOUSE_NAME, house_name);

		cv.put(ROOM_NAME, roomname);
		cv.put(ROOM_NO, roomno);
		cv.put(DEV_NO, devno);
		cv.put(DEV_NAME, devname);
		cv.put(DEV_TYPE, devtype);

		cv.put(KEY_NUMBER, keynumber);
		cv.put(KEY_LOC_CODE, keyloccode);

		cv.put(KEY_NAME, key_name);
		cv.put(KEY_GROUP, key_group);
		cv.put(KEY_DATA, key_data);

		cv.put(KEY_ACTUAL_DATA, key_actual_data);

		cv.put(KEY_STATUS, key_status);

		return sdb.update(DATABASE_TABLE, cv, DEV_NO + "=" + devno, null) > 0;
	}

	public boolean updatelearntkey(int devno, int keycode, String keydata) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_DATA, keydata);
		cv.put(KEY_STATUS, 1);

		return sdb.update(DATABASE_TABLE, cv, DEV_NO + "=" + devno + " AND "
				+ KEY_LOC_CODE + "=" + keycode, null) > 0;
	}


	public boolean updateRoomName(int oldRno,int roomno, String newrname) {
		//starting db transaction
		sdb.beginTransaction();

		try {

			// updating ir blaster table

			ContentValues cv = new ContentValues();
			cv.put(ROOM_NAME, newrname);
			cv.put(ROOM_NO, roomno);
			boolean upd1=sdb.update(DATABASE_TABLE, cv, ROOM_NO + "=" + oldRno + ";",
					null)>0;
			StaticVariables.printLog("TAG", "ir blaster room details updated");


			// updating config table
			ContentValues cv2 = new ContentValues();
			cv2.put(IRCNFG_RoomName, newrname);
			cv2.put(IRCNFG_RoomNo, roomno);
			boolean upd2=sdb.update(DATABASE_TABLE_CFG, cv2, IRCNFG_RoomNo + "="
							+ oldRno + ";", null)>0;
			StaticVariables.printLog("TAG", "ir config room details updated");



			// updating ir ac table
			ContentValues cv3 = new ContentValues();
			cv3.put(AC_RoomNo, roomno);
			boolean upd3=sdb.update(DATABASE_AC_TABLE, cv3, AC_RoomNo + "=" + oldRno
									+ ";", null)>0;
		    StaticVariables.printLog("TAG", "ir ac room details updated");


			sdb.setTransactionSuccessful();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			sdb.endTransaction();
		}

		return false;

	}

	public boolean updateRemoteName(int roomno, int devno, String oldname,
			String newname) 
	{
		Cursor mcursor = sdb.query(true, DATABASE_TABLE, new String[] {DEV_NAME 
		}, DEV_NAME+ "='" + newname + "'", null, null, null, null, null);

		if (mcursor != null && mcursor.getCount()>0) {
			return false;
		}else{
			ContentValues cv = new ContentValues();
			cv.put(DEV_NAME, newname);
			return sdb.update(DATABASE_TABLE, cv, ROOM_NO + "=" + roomno + " AND "
					+ DEV_NO + "=" + devno + " AND " + DEV_NAME + " ='" + oldname
					+ "';", null) > 0;
		}

	}

	public boolean delete(int rno, int devno) {
		return sdb.delete(DATABASE_TABLE, ROOM_NO + "=" + rno + " AND "
				+ DEV_NO + "=" + devno + " AND " + DEV_TYPE + " = 'empty' ;",
				null) > 0;
	}

	//method to delete room(method updated)
	public boolean deleteRooms(int rno) {
		boolean isDeleted=false;
		try{
			try{
				isDeleted=sdb.delete(DATABASE_TABLE, ROOM_NO + "=" + rno + ";", null)>0;
			}catch(Exception e){
				e.printStackTrace();
			}

			//deleting record from ir blaster table
			isDeleted=sdb.delete(DATABASE_TABLE_CFG, IRCNFG_RoomNo + "=" + rno + ";", null)>0;
			StaticVariables.printLog("TAG","IS DELETED :"+isDeleted);
		}catch(Exception e){
			e.printStackTrace();
		}
		return isDeleted;
	}

	//getting list of all unique room numbers
	public TreeSet<Integer> getUniqueRoomsNo(){
		TreeSet<Integer> rno=new TreeSet<Integer>();
		Cursor mcursor=null;
		try{

			mcursor=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_RoomNo}, null,
					null, null, null, null, null);
			int roomno=0;
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					roomno=mcursor.getInt(mcursor.getColumnIndex(IRCNFG_RoomNo));
					rno.add(roomno);
				}while(mcursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing cursor
			if(mcursor!=null)
				mcursor.close();
		}

		return rno;
	}


	//getting list of all unique room numbers
	public TreeSet<Integer> getUniqueDeviceNo(){
		TreeSet<Integer> dno=new TreeSet<Integer>();
		Cursor mcursor=null;
		try{

			mcursor=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_DeviceNo}, null,
					null, null, null, null, null);
			int devno=0;
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					devno=mcursor.getInt(mcursor.getColumnIndex(IRCNFG_DeviceNo));
					dno.add(devno);
				}while(mcursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing cursor
			if(mcursor!=null)
				mcursor.close();
		}

		return dno;
	}	

	// not used yet
	public boolean deleteRoomsDevice(int rno, String devtyp) {
		return sdb.delete(DATABASE_TABLE, ROOM_NO + "=" + rno + " AND "
				+ DEV_TYPE + "='" + devtyp + "';", null) > 0;
	}

	public boolean deleteRoomsRemotes(int rno, int devno, String devtyp,
			String remotename) {
		return sdb.delete(DATABASE_TABLE, ROOM_NO + "=" + rno + " AND "
				+ DEV_NO + "=" + devno + " AND " + DEV_TYPE + "='" + devtyp
				+ "' AND " + DEV_NAME + "='" + remotename + "';", null) > 0;
	}

	public Cursor fetchAllRecords() {
		return sdb.query(true, DATABASE_TABLE, new String[] { HOUSE_NO,
				HOUSE_NAME, ROOM_NO, ROOM_NAME, DEV_NO, DEV_NAME, DEV_TYPE,
				KEY_NUMBER, KEY_LOC_CODE, KEY_NAME, KEY_GROUP, KEY_DATA,
				KEY_STATUS }, null, null, null, null, null, null);
	}

	public boolean chekRemoteExists(String rname,String devname) {
		Cursor mcursor= sdb.query(true, DATABASE_TABLE, new String[] {DEV_NAME },
				ROOM_NAME+"='"+rname+"' AND "+DEV_NAME+"='"+devname+"'", null, null, null, null, null);

		if(mcursor!=null && mcursor.getCount()>0){
			return true;
		}
		return false;
	}

	public Cursor fetchAllDistinctRooms(String Keycolumn1, String Keycolumn2) {
		return sdb.query(true, DATABASE_TABLE_CFG, new String[] { Keycolumn1,
				Keycolumn2 }, null, null, null, null, Keycolumn1, null);
	}

	public Cursor fetchAllDistinctRecords(String Keycolumn1, String keycolumn2,
			int value) {
		return sdb.query(true, DATABASE_TABLE_CFG, new String[] { Keycolumn1,
				keycolumn2 }, keycolumn2 + "= " + value + " ;", null, null,
				null, Keycolumn1, null);
	}

	public Cursor fetchAllDistinctRecords(String Keycolumn, String keycolumn1,
			String keycolumn2, String keycolumn3, String value1, String value2,
			int value3) {
		return sdb.query(true, DATABASE_TABLE, new String[] { Keycolumn },
				keycolumn1 + "='" + value1 + "' AND " + keycolumn2 + "='"
						+ value2 + "' AND " + keycolumn3 + "=" + value3, null,
						null, null, null, null);
	}

	public Cursor fetchAllDistinctkeygroups(String Keycolumn, int rno,
			String devtyp, String remote, int devno) {
		return sdb.query(true, DATABASE_TABLE, new String[] { Keycolumn },
				ROOM_NO + "=" + rno + " AND " + DEV_NO + "=" + devno + " AND "
						+ DEV_NAME + "='" + remote + "' AND " + DEV_TYPE + "='"
						+ devtyp + "' ;", null, null, null, null, null);
	}

	public Cursor fetchallremotes(String keycolumn, int roomno, int devno) {
		return sdb.query(true, DATABASE_TABLE, new String[] { keycolumn,
				DEV_TYPE }, ROOM_NO + "=" + roomno + " AND " + DEV_NO + "="
						+ devno + " ;", null, null, null, null, null);
	}

	//fetching unique devices(newly added)
	public Cursor fetchallDevices(String keycolumn, int roomno, int devno) {
		return sdb.query(true, DATABASE_TABLE, new String[] { DEV_TYPE,keycolumn
		}, ROOM_NO + "=" + roomno + " AND " + DEV_NO + "="
				+ devno + " ;", null, null, null, null, null);
	}

	public Cursor fetchDevData(String key, int rno, int devno) {
		return sdb.query(true, DATABASE_TABLE, new String[] { key }, ROOM_NO
				+ "=" + rno + " AND " + DEV_NO + "=" + devno + ";", null, null,
				null, null, null);
	}

	public Cursor fetch_SingleRecord(String key1, String key2, int val1,
			int val2) throws SQLException {
		Cursor mCursor = sdb.query(true, DATABASE_TABLE, new String[] {
				HOUSE_NO, HOUSE_NAME, ROOM_NO, ROOM_NAME, DEV_NO, DEV_NAME,
				DEV_TYPE, KEY_NUMBER, KEY_LOC_CODE, KEY_NAME, KEY_GROUP,
				KEY_DATA, KEY_STATUS }, key1 + "=" + val1 + " AND " + key2
				+ "=" + val2, null, null, null, null, null);
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor fetch_FinalRemote(int roomno, int devno, String devtype,
			String devname) throws SQLException {
		return sdb.query(true, DATABASE_TABLE, new String[] { KEY_GROUP },
				ROOM_NO + "=" + roomno + " AND " + DEV_NO + "=" + devno
				+ " AND " + DEV_TYPE + "='" + devtype + "' AND "
				+ DEV_NAME + "='" + devname + "' ;", null, null, null,
				null, null);

	}

	//new method o fetch all details of remote to upload
	public Cursor fetchRemoteDetails(int roomno, int devno, String devtype,
			String devname) throws SQLException {
		return sdb.query(true, DATABASE_TABLE, new String[] {DEV_TYPE, KEY_GROUP,KEY_DATA,KEY_NAME,BRAND_NAME,MODEL_NO,MAKE },
				ROOM_NO + "=" + roomno + " AND " + DEV_NO + "=" + devno
				+ " AND " + DEV_TYPE + "='" + devtype + "' AND "
				+ DEV_NAME + "='" + devname + "' ;", null, null, null,
				null, null);

	}
	public String fetch_UNV_transmissiondata(String keyname, int rno,
			String dtyp, int dno) {
		String data = null;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_DATA },
					ROOM_NO + " =" + rno + " AND " + KEY_NAME + " ='" + keyname
					+ "' AND " + DEV_TYPE + " ='" + dtyp + "' AND "
					+ DEV_NO + " =" + dno + " ;", null, null, null,
					null, null);

			if (mCursor!=null && mCursor.getCount()>0){
				mCursor.moveToFirst();
				data = mCursor.getString(mCursor.getColumnIndex(KEY_DATA));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mCursor!=null){
			mCursor.close(); 
		}
		return data;
	}

	//fetch data to transmit
	public String fetch_transmissiondata(String keyname, int rno, String dtyp,
			int dno, String remotename) {
		String data = null;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_DATA },
					DEV_NAME + " ='" + remotename + "' AND " + ROOM_NO + " ="
							+ rno + " AND " + KEY_NAME + " ='" + keyname
							+ "' AND " + DEV_TYPE + " ='" + dtyp + "' AND "
							+ DEV_NO + " =" + dno + " ;", null, null, null,
							null, null);

			if (mCursor!=null && mCursor.getCount()>0){ 
				mCursor.moveToFirst();
				data = mCursor.getString(mCursor.getColumnIndex(KEY_DATA));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null){
			mCursor.close(); 
		}
		return data;
	}

	public boolean fetch_devkeyStatus(String keyname, int rno, String dtyp,
			int dno) {
		boolean readstatus = false;
		int sts = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_STATUS },
					ROOM_NO + " =" + rno + " AND " + KEY_NAME + " ='" + keyname
					+ "' AND " + DEV_TYPE + " ='" + dtyp + "' AND "
					+ DEV_NO + " =" + dno + " ;", null, null, null,
					null, null);

			if (mCursor!=null && mCursor.getCount()>0) { 
				mCursor.moveToFirst();
				sts = mCursor.getInt(mCursor.getColumnIndex(KEY_STATUS));

				if (sts == 1) {
					readstatus = true;
				} else {
					readstatus = false;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null){
			mCursor.close(); 
		}
		return readstatus;
	}

	public boolean fetch_keyStatus(String keyname, int rno, String dtyp,
			String dname, int dno) {
		boolean readstatus = false;
		int sts = 0;
		Cursor mCursor = null;

		try {

			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_STATUS },
					ROOM_NO + " =" + rno + " AND " + DEV_NAME + " ='" + dname
					+ "' AND " + KEY_NAME + " ='" + keyname + "' AND "
					+ DEV_TYPE + " ='" + dtyp + "' AND " + DEV_NO
					+ " =" + dno + " ;", null, null, null, null, null);



			if (mCursor!=null && mCursor.getCount()>0){

				mCursor.moveToFirst();
				sts = mCursor.getInt(mCursor.getColumnIndex(KEY_STATUS));


				if (sts == 1) {
					readstatus = true;
				} else {
					readstatus = false;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null){
			mCursor.close(); 
		}

		return readstatus;
	}

	public int currentdevno(int roomno, String devtyp, String remotename) {
		int DEVICE_NO = 0;

		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { DEV_NO },
					ROOM_NO + " =" + roomno + " AND " + DEV_TYPE + " ='"
							+ devtyp + "' AND " + DEV_NAME + " ='" + remotename
							+ "' ;", null, null, null, null, null);

			if (mCursor!=null && mCursor.getCount()>0) { 
				mCursor.moveToFirst();
				DEVICE_NO = (int) mCursor.getLong(mCursor
						.getColumnIndex(DEV_NO));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mCursor!=null)
			mCursor.close();
		return DEVICE_NO; 
	}

	public int fetch_MaxKeyLocRecord(int devno) {

		final int DEFAULT_BASE = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { "MAX("
					+ KEY_LOC_CODE + ")" }, DEV_NO + " =" + devno, null, null,
					null, null, null);

			if (mCursor!=null && mCursor.getCount()>0) { 
				mCursor.moveToFirst();
				return (int) mCursor.getLong(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mCursor!=null)
			mCursor.close();
		return DEFAULT_BASE;
	}

	public int fetch_KeyLocCode(int roomno, String devname, int devno,
			String keyname) {

		int Key_Loc_Code = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_LOC_CODE },
					DEV_NO + " =" + devno + " AND " + KEY_NAME + "='" + keyname
					+ "' AND " + ROOM_NO + " =" + roomno + " AND "
					+ DEV_NAME + "='" + devname + "';", null, null,
					null, null, null);

			if (mCursor!=null && mCursor.getCount()>0) { 
				mCursor.moveToFirst();
				Key_Loc_Code = mCursor.getInt(mCursor
						.getColumnIndex(KEY_LOC_CODE));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return Key_Loc_Code;
	}

	public int fetch_MaxDevno() throws SQLException {

		final int DEFAULT_BASE = 0;
		Cursor mCursor = null;
		try {

			mCursor = sdb.query(DATABASE_TABLE, new String[] { "MAX(" + DEV_NO
					+ ")" }, null, null, null, null, null);

			if (mCursor!=null && mCursor.getCount()>0) {
				mCursor.moveToFirst();
				return (int) mCursor.getLong(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if(mCursor!=null)
			mCursor.close();

		return DEFAULT_BASE;

	}

	public Cursor roomExists(int rno) {
		Cursor mcursor = sdb.query(true, DATABASE_TABLE, new String[] {
				ROOM_NO, ROOM_NAME }, ROOM_NO + "=" + rno, null, null, null,
				null, null);

		return mcursor;
	}

	public int fetch_DelteKey_MaxData(String Key, int devno, String devtyp,
			String devname) {

		final int DEFAULT_BASE = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { "MAX(" + Key
					+ ")" }, DEV_NO + " =" + devno + " AND " + DEV_TYPE
					+ " = '" + devtyp + "' AND " + DEV_NAME + " ='" + devname
					+ "' ;", null, null, null, null, null);

			if (mCursor.getCount() == 0) {
				return DEFAULT_BASE;
			} else {
				mCursor.moveToFirst();
				return (int) mCursor.getLong(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return DEFAULT_BASE;
	}

	public int fetch_DelteKey_MinData(String Key, int devno, String devtyp,
			String devname) {

		final int DEFAULT_BASE = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { "MIN(" + Key
					+ ")" }, DEV_NO + " =" + devno + " AND " + DEV_TYPE
					+ " = '" + devtyp + "' AND " + DEV_NAME + " ='" + devname
					+ "' ;", null, null, null, null, null);

			if (mCursor.getCount() == 0) {
				return DEFAULT_BASE;
			} else {
				mCursor.moveToFirst();
				return (int) mCursor.getLong(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return DEFAULT_BASE;
	}

	public boolean update_key_loc_codes(int key_lno, int max_del_keyloc,
			int devno) {
		Cursor mCursor = null;
		try {
			int rowcount = 0;
			try {

				mCursor = sdb.query(DATABASE_TABLE,
						new String[] { KEY_LOC_CODE }, DEV_NO + " =" + devno
						+ " AND " + KEY_LOC_CODE + " >" + key_lno
						+ " AND " + KEY_LOC_CODE + "!=0 ;", null, null,
						null, null, null);

				if(mCursor!=null)
					rowcount = mCursor.getCount();

				StaticVariables.printLog("", "row count is : " + rowcount);
			} catch (Exception e) {
				e.printStackTrace();
			}

			ContentValues cv1 = new ContentValues();
			ContentValues cv2 = new ContentValues();
			int count = 0, count2 = 0;
			while (rowcount > 0) {
				cv1.put(KEY_LOC_CODE, key_lno);
				max_del_keyloc++;

				String klno = "" + key_lno;
				String keydata = "";
				while (klno.length() < 4) {
					klno = "0" + klno;
				}

				if (klno.length() == 4) {
					keydata = "2" + klno;
				}

				cv2.put(KEY_DATA, keydata);
				count2 = sdb.update(DATABASE_TABLE, cv2, DEV_NO + "=" + devno
						+ " AND " + KEY_LOC_CODE + " =" + max_del_keyloc
						+ " AND " + KEY_LOC_CODE + " > 0 " + " AND " + KEY_DATA
						+ "!='empty' ;", null);
				StaticVariables.printLog("", "key data : " + keydata + " count hit : " + count2);

				count = sdb.update(DATABASE_TABLE, cv1, DEV_NO + "=" + devno
						+ " AND " + KEY_LOC_CODE + " =" + max_del_keyloc
						+ " AND " + KEY_LOC_CODE + " > 0 ;", null);
				StaticVariables.printLog("", "key loc in loop :" + key_lno + " maxd:"
						+ (max_del_keyloc) + " count hit s  :" + count);

				key_lno++;
				rowcount--;
			}

			StaticVariables.printLog("", "rows updated");
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return false;
	}

	public int rowscount(int devno, String devtyp, String devname) {
		int rcount = 0;
		Cursor mCursor = null;
		try {
			mCursor = sdb.query(DATABASE_TABLE, new String[] { KEY_LOC_CODE },
					DEV_NO + " =" + devno + " AND " + DEV_TYPE + " = '"
							+ devtyp + "' AND " + DEV_NAME + " ='" + devname
							+ "' ;", null, null, null, null, null);

			if(mCursor!=null)
				rcount = mCursor.getCount();

		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return rcount;
	}

	public void sequencerowid(int minloc, int rows, int minrid) {
		if (minloc == 1) {
			StaticVariables.printLog("", "min loc is : " + minloc);
			sdb.execSQL("UPDATE " + DATABASE_TABLE + " set " + ROWID + " = ("
					+ ROWID + "-" + rows + "); ");
		} else {
			StaticVariables.printLog("", "min loc is : " + minloc + " min rid : " + minrid);
			sdb.execSQL("UPDATE " + DATABASE_TABLE + " set " + ROWID + " = ("
					+ ROWID + "-" + rows + " ) WHERE " + ROWID + ">"
					+ (minrid - 1));
		}
	}

	public ArrayList<Integer> fetchDeviceNumberList(int room_no) {

		ArrayList<Integer> devnumbers = new ArrayList<Integer>();
		Cursor mcursor = null;
		int devnum = 0;
		try {

			mcursor = fetchAllDistinctRecords(IRB_Adapter_ir.DEV_NO,
					IRB_Adapter_ir.ROOM_NO, room_no);
			if (mcursor != null && mcursor.getCount() > 0) {
				mcursor.moveToFirst();
				do {
					devnum = mcursor.getInt(mcursor
							.getColumnIndex(IRB_Adapter_ir.DEV_NO));
					devnumbers.add(devnum);
				} while (mcursor.moveToNext());
				// sorting elements
				Collections.sort(devnumbers);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mcursor!=null)
			mcursor.close(); 

		return devnumbers;
	}

	//new method added
	public String IRdevName(int devno){
		String DevName=null; 
		Cursor mCursor=null;
		try{
			mCursor=sdb.query(true, DATABASE_TABLE_CFG, 
					new String[]{IRCNFG_DeviceName}, IRCNFG_DeviceNo+"="+devno, null, null, null, null, null);

			if(mCursor!=null && mCursor.getCount()>0){
				mCursor.moveToFirst();
				DevName=mCursor.getString(mCursor.getColumnIndex(IRCNFG_DeviceName));
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		if(mCursor!=null)
			mCursor.close();

		return DevName;
	}

	//new method to fetch key name list of particular group
	public String keyinfo(String column1,String column2,String value,
			int devno,String devtyp,String remote){
		Cursor mCursor=null;
		StringBuilder sb=new StringBuilder();
		try{

			mCursor=sdb.query(true, DATABASE_TABLE, new String[]{column1}, column2+"='"+value+"' AND "
					+DEV_NO+"="+devno+" AND "+DEV_TYPE+"='"+devtyp+"' AND "+DEV_NAME+"='"+remote+"'", null, null, null, null, null );

			if(mCursor!=null && mCursor.getCount()>0){
				//moving cursor to first position
				mCursor.moveToFirst();
				//getting cursor count
				int count=mCursor.getCount();
				int i=1;
				if(count>0){ 
					do{
						if(i<count){
							sb.append(mCursor.getString(mCursor.getColumnIndex(column1))+"#");
						}else{
							sb.append(mCursor.getString(mCursor.getColumnIndex(column1)));
						}
						i++;
					}while(mCursor.moveToNext());
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return sb.toString();
	}

	//new method to fetch key data list of all key names
	public String keyActualDataList(String kgpvalue,int devno,String devtyp,String remote){
		StringBuilder sb=new StringBuilder();
		Cursor mCursor=null;
		try{

			mCursor=sdb.query(true, DATABASE_TABLE, new String[]{KEY_NAME,KEY_DATA},
					DEV_NO+"="+devno+" AND "+DEV_TYPE+"='"+devtyp+"' AND "+
							DEV_NAME+"='"+remote+"' AND "+KEY_GROUP+"='"+kgpvalue+"'", null, null, null, null, null );

			if(mCursor!=null && mCursor.getCount()>0){
				//moving cursor to first position
				mCursor.moveToFirst();
				//getting cursor count
				int count=mCursor.getCount();
				int i=1;
				if(count>0){ 
					do{
						if(i<count){
							sb.append(mCursor.getString(mCursor.getColumnIndex(KEY_NAME))+
									":"+mCursor.getString(mCursor.getColumnIndex(KEY_DATA))+",");
						}else{
							sb.append(mCursor.getString(mCursor.getColumnIndex(KEY_NAME))+
									":"+mCursor.getString(mCursor.getColumnIndex(KEY_DATA)));
						}
						i++;
					}while(mCursor.moveToNext());
				}

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return sb.toString();
	}	

	//fetching all records of config table
	public Cursor isIrDataAvailable() {
		return sdb.query(true, DATABASE_TABLE_CFG, new String[] { IRCNFG_DeviceNo }, null,null, null,	null, null, null);
	}



	//method to check if ssid is of ir blaster and is there any ir already configured
	public boolean isIRexists(final String macid){
		try{
			Cursor mcursor=null,mcursor2=null;
			mcursor2=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_DevMacID},
					IRCNFG_net_SSID+"='"+IR_DEFAULT_SSID+"'",null, null, null, null, null);
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor=sdb.query(true,DATABASE_TABLE_CFG, new String[]{}, 
						IRCNFG_DevMacID+"='"+macid+"' AND "+IRCNFG_net_SSID+"='"+IR_DEFAULT_SSID+"'", null, null, null, null, null);	
				if(mcursor!=null && mcursor.getCount()>0){
					return false; 
				}
				return true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}	

	//update ir device name
	public boolean updateIRDeviceName(String rname,String irOldName,String irName){
		boolean a=false;
		try{

			//checking if ir name already exists
			boolean isDevExists=isSamedevExists(rname,irName);
			if(isDevExists){
				return false;
			}else{
				//updating ir name in config table
				ContentValues cv1 = new ContentValues(); 
				cv1.put(IRCNFG_DeviceName, irName);
				a=sdb.update(DATABASE_TABLE_CFG, cv1, IRCNFG_DeviceName+"='"+irOldName+"'",null)>0;

				StaticVariables.printLog("sts","updt1 :"+a);

				if(a){
					return true;
				}
			}


		}catch(Exception e){
			e.printStackTrace();
		} 

		return false;
	}


	//method to delete ir device
	public boolean deleteIRDeviceName(int devno){
		boolean a=false,b=false,c=false;
		try{
			//updating ir name in config table
			a=sdb.delete(DATABASE_TABLE_CFG, IRCNFG_DeviceNo + "=" + devno,	null) > 0;

			try{
				//updating ir name in irtable
				b=sdb.delete(DATABASE_TABLE, DEV_NO + "=" + devno,	null) > 0;	 
			}catch(Exception e){
				e.printStackTrace();
			}

			try{
				//updating ir name in irtable
				c=sdb.delete(DATABASE_AC_TABLE, AC_DeviceNo + "=" + devno,	null) > 0;	 
			}catch(Exception e){
				e.printStackTrace();
			}


			StaticVariables.printLog("sts","dlt1 :"+a+" dlt2 :"+b+" dlt3 :"+c);
			if(a || b || c){
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		} 

		return false;
	}


	//ir room list
	public TreeSet<String> IRB_RoomNameList(){
		TreeSet<String>UniqueRoomNames=new TreeSet<String>();//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();//list for storing room names temporarly
		Cursor mcursor1 = null;		//initialize cursor
		String RoomName=null;

		//Fetching room names from Config  table
		try{
			mcursor1=sdb.query(true, DATABASE_TABLE_CFG, new String[]{ROOM_NAME},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1.getCount()!=0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					RoomName=mcursor1.getString(mcursor1.getColumnIndex(ROOM_NAME));	//getting room name from cursor
					roomNames.add(RoomName);	// adding room names one by one in array list
				}while(mcursor1.moveToNext());
				//closing the cursor
				mcursor1.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		//adding unique rooms from arraylist into tree list
		UniqueRoomNames.addAll(roomNames);
		StaticVariables.printLog("TAG","irb Room List Size is :"+UniqueRoomNames.size());

		return UniqueRoomNames;		

	}


	//fetching ir room Name
	public int irRoomNumber(String roomName){
		int room_number=0;
		try{
			Cursor mcursor3=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_RoomName,IRCNFG_RoomNo},IRCNFG_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()!=0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_number=mcursor3.getInt(mcursor3.getColumnIndex(IRCNFG_RoomNo));	//getting room name from cursor
				}while(mcursor3.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return room_number;
	}


	//fetching ir room Image Name
	public String irRoomIMage(String roomName){
		String room_img_nam=null;
		try{
			Cursor mcursor3=sdb.query(true, DATABASE_TABLE_CFG, new String[]{IRCNFG_RoomName,IRCNFG_RoomNo},IRCNFG_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()!=0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_img_nam=mcursor3.getString(mcursor3.getColumnIndex(IRCNFG_RoomNo));	//getting room name from cursor
				}while(mcursor3.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return room_img_nam;
	}


	//getting max room number
	public int Max_IR_RoomNumber(){
		int maxDevNo=0;
		try{
			SQLiteStatement stmt = sdb.compileStatement("select MAX("+IRCNFG_RoomNo+") from "+DATABASE_TABLE_CFG+";");
			maxDevNo = (int) stmt.simpleQueryForLong();
		}catch(Exception e){
			e.printStackTrace();
		}

		return maxDevNo;
	}

	//*************************************AC TABLE OPERATIONS*******************************//

	//inserting data in ir AC Table
	public boolean insertORupdate_AC_TABLE(String model_no,String mode,int temperature, int swingLR,
			int swingUD, int fanSpeed,int sleepTime,int deviceNo ,String deviceName,int roomNo,
			String keyData )
	{
		boolean inserted=false ,isRecordExists=false;

		//checking if key data already exists 
		try{
			Cursor mcursor=null; 
			mcursor = sdb.query(DATABASE_AC_TABLE, new String[] { AC_KeyActualData },AC_ModelNo+"='"+model_no+"' AND "+
					AC_SwingLR+"="+swingLR+" AND "+AC_SwingUD+"="+swingLR+" AND "+
					AC_Mode+"='"+mode+"' AND "+AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"'"+" AND "+AC_FanSpeed+"="+fanSpeed
					+" AND "+AC_SleepTimer+"="+sleepTime+" AND "+AC_Tempertaure+"="+temperature+" AND "+AC_RoomNo+"="+roomNo,  null, null, null, null, null);

			//checking if cursor is not null
			if(mcursor!=null && mcursor.getCount()>0){
				//closing cursor
				mcursor.close(); 
				isRecordExists=true;
				StaticVariables.printLog("AC","AC record existing");
			} 

		}catch(Exception e){
			e.printStackTrace();
		}   


		if(isRecordExists){
			//updating existing records 
			try{
				ContentValues cv = new ContentValues(); 
				cv.put(AC_ModelNo, model_no);
				cv.put(AC_Tempertaure, temperature);
				cv.put(AC_SwingLR, swingLR); 
				cv.put(AC_SwingUD, swingUD);  
				cv.put(AC_FanSpeed, fanSpeed);
				cv.put(AC_SleepTimer, sleepTime);
				cv.put(AC_Mode, mode);
				cv.put(AC_DeviceNo, deviceNo);  
				cv.put(AC_DeviceName, deviceName); 
				cv.put(AC_RoomNo, roomNo); 
				cv.put(AC_KeyActualData, keyData); 

				inserted= sdb.update(DATABASE_AC_TABLE, cv,AC_ModelNo+"='"+model_no+"' AND "+
						AC_SwingLR+"="+swingLR+" AND "+AC_SwingUD+"="+swingLR+" AND "+
						AC_Mode+"='"+mode+"' AND "+AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"'"+" AND "+AC_FanSpeed+"="+fanSpeed
						+" AND "+AC_SleepTimer+"="+sleepTime+" AND "+AC_Tempertaure+"="+temperature+" AND "+AC_RoomNo+"="+roomNo, null)>0?true:false;

						StaticVariables.printLog("DB","Ac key data updated "+inserted);

						try{
							//updating last accessed record
							fetch_AC_TransmissionData(swingLR, swingUD, model_no, mode, temperature, fanSpeed, sleepTime, deviceNo, deviceName);
						}catch(Exception e){
							e.printStackTrace();
						}

						return inserted;
			}catch(Exception e){
				e.printStackTrace();
			}
		}else{
			//inserting new records
			try{

				ContentValues cv = new ContentValues();

				cv.put(AC_ModelNo, model_no);
				cv.put(AC_Tempertaure, temperature);
				cv.put(AC_SwingLR, swingLR); 
				cv.put(AC_SwingUD, swingUD);  
				cv.put(AC_FanSpeed, fanSpeed);
				cv.put(AC_SleepTimer, sleepTime);
				cv.put(AC_Mode, mode);
				cv.put(AC_DeviceNo, deviceNo);  
				cv.put(AC_DeviceName, deviceName);
				cv.put(AC_RoomNo, roomNo);
				cv.put(AC_KeyActualData, keyData); 
				cv.put(AC_LastOperated, 0); 

				cv.put(DATA1, "empty");
				cv.put(DATA2, "empty");
				cv.put(DATA3, "empty");
				cv.put(DATA4, "empty");
				cv.put(DATA5, "empty");
				cv.put(DATA6, "empty");
				cv.put(DATA7, "empty");
				cv.put(DATA8, "empty");
				cv.put(DATA9, "empty");
				cv.put(DATA10,"empty");
				inserted= sdb.insert(DATABASE_AC_TABLE, null, cv)>0?true:false;
				StaticVariables.printLog("DB","Ac key data inserted "+inserted);

				try{
					//updating last accessed record
					fetch_AC_TransmissionData(swingLR, swingUD, model_no, mode, temperature, fanSpeed, sleepTime, deviceNo, deviceName);
				}catch(Exception e){
					e.printStackTrace();
				}

				return inserted;
			}catch(Exception e){
				e.printStackTrace();
			}
		}



		return inserted;
	}


	//get data to transmit 
	public String fetch_AC_TransmissionData(int swingLR,int swingUD,String model_no, String mode,int temperature,int fanSpeed,int sleepTime,int deviceNo ,String deviceName) {
		String data = null;
		Cursor mCursor = null;
		try { 
			mCursor = sdb.query(DATABASE_AC_TABLE, new String[] { AC_KeyActualData },AC_ModelNo+"='"+model_no+"' AND "+
					AC_SwingLR+"="+swingLR+" AND "+AC_SwingUD+"="+swingUD+" AND "+
					AC_Mode+"='"+mode+"' AND "+AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"'"+" AND "+AC_FanSpeed+"="+fanSpeed
					+" AND "+AC_SleepTimer+"="+sleepTime+" AND "+AC_Tempertaure+"="+temperature,  null, null, null, null, null);

			//checking if data is available
			if (mCursor!=null && mCursor.getCount()>0){ 
				//moving cursor to first position
				mCursor.moveToFirst();
				data = mCursor.getString(mCursor.getColumnIndex(AC_KeyActualData));

				//making all rows status to zero first
				ContentValues cv = new ContentValues(); 
				cv.put(AC_LastOperated, 0);
				boolean updated1= sdb.update(DATABASE_AC_TABLE, cv,AC_DeviceName+"='"+deviceName+"'", null)>0?true:false;

				//updating last operated row in database
				if(updated1)
				{
					StaticVariables.printLog("TAG","all rows status updated");
					boolean updated2=false;
					ContentValues cv2 = new ContentValues(); 
					cv2.put(AC_LastOperated, 1);
					if(mode.equals("1") || mode.equals("4")){
						updated2= sdb.update(DATABASE_AC_TABLE, cv2,AC_ModelNo+"='"+model_no+"' AND "+
								AC_Mode+"='"+mode+"' AND "+AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"'"+" AND "+AC_FanSpeed+"="+fanSpeed
								+" AND "+AC_SleepTimer+"="+sleepTime+" AND "+AC_Tempertaure+"="+temperature+
								" AND "+AC_SwingLR+"="+swingLR+" AND "+AC_SwingUD+"="+swingUD, null)>0?true:false;


					}else{
						updated2= sdb.update(DATABASE_AC_TABLE, cv2,AC_ModelNo+"='"+model_no+"' AND "+
								AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"'"+" AND "+AC_FanSpeed+"="+fanSpeed+
								" AND "+AC_Mode+"='"+mode+"' AND "+AC_SleepTimer+"="+sleepTime +
								" AND "+AC_SwingLR+"="+swingLR+" AND "+AC_SwingUD+"="+swingUD, null)>0?true:false;

					}	
					if(updated2)
						StaticVariables.printLog("TAG","last operated status updated");
					else
						StaticVariables.printLog("TAG","last operated status not updated");
				}

			}else{
				StaticVariables.printLog("TAG","no data available for this ac combination");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(mCursor!=null){
			mCursor.close(); 
		}
		return data;
	}


	//get last operated values cursor
	public Cursor getLastOperatedValues(String model_no,int deviceNo,String deviceName){
		try{
			Cursor mcurCursor= sdb.query(DATABASE_AC_TABLE, new String[] {AC_Tempertaure,AC_SwingLR,AC_SwingUD,
					AC_FanSpeed,AC_SleepTimer,AC_Mode  },AC_ModelNo+"='"+model_no+"' AND "+
							AC_DeviceNo+"="+deviceNo+" AND "+AC_DeviceName+"='"+deviceName+"' AND " +AC_LastOperated+"="+1,  null, null, null, null, null);

			if(mcurCursor!=null && mcurCursor.getCount()>0){
				mcurCursor.moveToFirst();
				return mcurCursor;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return null;
	}

	//deleting records from ac table
	public boolean deleteAcKeyData(String deviceName,int devno,String modelNo){

		try{
			return sdb.delete(DATABASE_AC_TABLE, AC_ModelNo + "='" + modelNo + "' AND "
					+ AC_DeviceNo + "=" + devno + " AND " + AC_DeviceName + " ='"+deviceName+"';",
					null) > 0;
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}
	//deleting records from ac tble
	public boolean deleteACrecords(int rno){

		try{
			return sdb.delete(DATABASE_AC_TABLE, AC_RoomNo+"="+rno+";",
					null) > 0;
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;

	}	
	//method to update remote name in ac table
	public boolean updateACremoteName(String modelno, int devno, String oldname,
			String newname) 
	{

		ContentValues cv = new ContentValues();
		cv.put(AC_DeviceName, newname);
		return sdb.update(DATABASE_AC_TABLE, cv, AC_ModelNo + "='" + modelno + "' AND "
				+ AC_DeviceNo+ "=" + devno + " AND " +AC_DeviceName + " ='" + oldname
				+ "';", null) > 0;

	}


	//new method to fetch key data list of all key names
	public String AcKeysDataList(int devno,String remote,String modelNo,int roomNo){
		StringBuilder sb=new StringBuilder();
		Cursor mCursor=null;
		int Tempertaure=0 ,SwingLR=0,SwingUD=0,
				FanSpeed=0,SleepTimer=0;
		String Mode=null,KeyActualData=null;
		try{

			mCursor=sdb.query(true, DATABASE_AC_TABLE, new String[]{AC_Tempertaure,AC_SwingLR,AC_SwingUD,
					AC_FanSpeed,AC_SleepTimer,AC_Mode,AC_KeyActualData},
					AC_DeviceNo+"="+devno+" AND "+AC_ModelNo+"='"+modelNo+"' AND "+
							AC_DeviceName+"='"+remote+"' AND "+AC_RoomNo+"="+roomNo+"", null, null, null, null, null );

			if(mCursor!=null && mCursor.getCount()>0){
				//moving cursor to first position
				mCursor.moveToFirst();
				StaticVariables.printLog("TAG","AC ROWS COUNT :"+mCursor.getCount());
				do{ 
					Tempertaure=mCursor.getInt(mCursor.getColumnIndex(AC_Tempertaure));
					SwingLR=mCursor.getInt(mCursor.getColumnIndex(AC_SwingLR));
					SwingUD=mCursor.getInt(mCursor.getColumnIndex(AC_SwingUD));
					FanSpeed=mCursor.getInt(mCursor.getColumnIndex(AC_FanSpeed));
					SleepTimer=mCursor.getInt(mCursor.getColumnIndex(AC_SleepTimer));
					Mode=mCursor.getString(mCursor.getColumnIndex(AC_Mode));
					KeyActualData=mCursor.getString(mCursor.getColumnIndex(AC_KeyActualData));

					//appending data
					sb.append(AC_Tempertaure+":"+Tempertaure+","+
							AC_SwingLR+":"+SwingLR+","+	
							AC_SwingUD+":"+SwingUD+","+
							AC_FanSpeed+":"+FanSpeed+","+
							AC_SleepTimer+":"+SleepTimer+","+
							AC_Mode+":"+Mode+","+
							AC_KeyActualData+":"+KeyActualData+";");

				}while(mCursor.moveToNext());

			}

		}catch(Exception e){
			e.printStackTrace();
		}

		if(mCursor!=null)
			mCursor.close();

		return sb.toString();
	}		

	//****************************************AC TABLE END**********************************//

	// Db Pull out method
	public boolean pullout() {
		String DATABASE_NAME=StaticVariables.HOUSE_NAME;
		File f = new File(DATABASE_PATH + DATABASE_NAME);
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {

			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/UniversalRemote/Write/" + "Remote.db";
			StaticVariables.printLog("TAG","file out director :"+path);

			fin = new FileInputStream(f);
			fout = new FileOutputStream(path);
			int i = 0;
			while ((i = fin.read()) != -1) {
				fout.write(i);
			}
			//closing file streams
			fout.flush();
			fin.close();
			fout.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return false;
	} 

	//overwrite existing database
	public boolean pushIn() {
		String DATABASE_NAME=StaticVariables.HOUSE_NAME;
		File f = new File(DATABASE_PATH + DATABASE_NAME);
		FileInputStream fin = null;
		FileOutputStream fout = null;
		try {
			String path=Environment.getExternalStorageDirectory().getAbsolutePath()+"/UniversalRemote/Read/" + "Remote.db";
			fin = new FileInputStream(path);
			StaticVariables.printLog("TAG","file in director :"+path);
			fout = new FileOutputStream(f);
			int i = 0;
			while ((i = fin.read()) != -1) {
				fout.write(i);
			}

			fout.flush(); 
			return true;
		} catch (Exception e) {
			e.printStackTrace(); 
		}
		return false;
	} 

	

	
	///////////////////////////////////////////////////////////////////////////////////////////
	public String getroomo_frmdevno(String devno) {
		String roomno="";
		Cursor mCursor = sdb.rawQuery(
				"SELECT a FROM IRConfigTable WHERE d='"+devno+"'", null);	
		if (mCursor.getCount() == 0){		    	
		}
		else{
			mCursor.moveToFirst();
			roomno = mCursor.getString(mCursor.getColumnIndex("a"));
		}		       		       		       		           		 
		return roomno;
	}
	
	
	
	
	public String getroomname(String roomno) {
		String roomname="";
		Cursor mCursor = sdb.rawQuery(
				"SELECT b FROM IRConfigTable WHERE a='"+roomno+"'", null);	
		if (mCursor.getCount() == 0){		    	
		}
		else{
			mCursor.moveToFirst();
			roomname = mCursor.getString(mCursor.getColumnIndex("b"));
		}		       		       		       		           		 
		return roomname;
	}
	
	
	////////////////////////////////////HA methods///////////////////////////////////////////////////////
	
	public int getCounthouseno() {
		final int DEFAULT_BASE = 0;
		Cursor mCursor = sdb.rawQuery(
				"SELECT Count(*) FROM IRConfigTable", null);
		if (mCursor.getCount() == 0)
			return DEFAULT_BASE;
		else
			mCursor.moveToFirst();
		return (int) mCursor.getLong(0);
	}

	public String[] getall_roomname(int count) {		  		

		Cursor mCursor = sdb.rawQuery(
				"SELECT _id,b FROM IRConfigTable", null);
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
		String uname="";
		Cursor mCursor = sdb.rawQuery(
				"SELECT a FROM IRConfigTable WHERE b='"+roomname+"'", null);	
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			uname = mCursor.getString(mCursor.getColumnIndex("a"));
		}


		return uname;
	}

	public Cursor checkroomnameexistir(String roomname) {
		Cursor mCursor = sdb.rawQuery(
				"SELECT a FROM IRBlasterTable WHERE b='"+roomname+"'", null);	
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	public Cursor checkroomnameexist(String roomname) {
		Cursor mCursor = sdb.rawQuery(
				"SELECT a FROM IRConfigTable WHERE b='"+roomname+"'", null);	
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
		args.put(IRCNFG_RoomName, newname);
		sdb.update(DATABASE_TABLE_CFG, args, IRCNFG_RoomName + "=" + roomname, null);   
	}

	public void updatenameir(String roomname,String newname){
		roomname="'"+roomname+"'";
		ContentValues args = new ContentValues();
		args.put(ROOM_NAME, newname);
		sdb.update(DATABASE_TABLE, args, ROOM_NAME + "=" + roomname, null);
	}

}
