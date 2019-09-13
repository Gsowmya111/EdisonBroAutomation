package edisonbro.com.edisonbroautomation.databasewireless;

/**
 *  FILENAME: WirelessConfigurationAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing wireless table[contains details of wireless devices ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TreeSet;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;

public class WirelessConfigurationAdapter {
	public static SQLiteDatabase sdb=null;
	Context mcontext;

	public static String ORIGINAL_PATH =null;
	public static String OriginalDataBase =null;

	// Wireless Table Key Fields
	public static final String WRLS_TABLE ="WirelessTable";
	public static final String WRLS_ID="_id";
	public static final String RoomNo="a";
	public static final String RoomName="b";
	public static final String WRLS_DeviceName="wdn";
	public static final String WRLS_DevNo="wd";
	public static final String WRLS_DevType="wc";
	public static final String WRLS_DevId="wf";
	public static final String DeviceName="dn";
	public static final String WRLS_RoomName="wb";
	public static final String WRLS_SwitchNo="wsn";
	public static final String WRLS_SwitchData="wsd";
	public static final String DevType="c";
	public static final String DevNo="d";
	public static final String DevId="f";
	public static final String DevSwitchNo="bb";
	public static final String DevLocalRef="ea";

	public static final String Data1="ea";
	public static final String Data2="eb";
	public static final String Data3="ec";
	public static final String Data4="ed";
	public static final String Data5="ee";
	public static final String Data6="ef";
	public static final String Data7="eg";
	public static final String Data8="eh";
	public static final String Data9="ei";
	public static final String Data10="ej";

	//Wireless Config Table key fields
	public static final String CNFG_TABLE ="WirelessConfigTable";
	public static final String CNFG_ID="_id";
	public static final String CNFG_DEVNAME ="wdn";
	public static final String CNFG_DEVNO="wd";
	public static final String CNFG_DEVTYPE ="wc";
	public static final String CNFG_DEVID="wf";
	public static final String CNFG_ROOM_NUMBER="wa";
	public static final String CNFG_ROOMNAME="wb";

	public static final String CNFG_Data1="ea";

	//********************
	public static final String CNFG_Data2="eb";

	public static final String CNFG_Data3="ec";
	public static final String CNFG_Data4="ed";
	public static final String CNFG_Data5="ee";
	public static final String CNFG_Data6="ef";
	public static final String CNFG_Data7="eg";
	public static final String CNFG_Data8="eh";
	public static final String CNFG_Data9="ei";
	public static final String CNFG_Data10="ej";

	//server config table
	public static final String SERVER_CNFG_TABLE ="ConfigTable";
	public static final String SERVER_CNFG_ID="_id";
	public static final String SERVER_CNFG_DevId="f";



	//constructor
	public WirelessConfigurationAdapter(Context context) {
		mcontext = context;		//passing context of current activity
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
		OriginalDataBase = StaticVariables.WHOUSE_NAME+".db";
	}


	//constructor
	public WirelessConfigurationAdapter(Context context, String DB_PATH, String DB_NAME) {
		mcontext = context;		//passing context of current activity
		ORIGINAL_PATH=DB_PATH;
		OriginalDataBase =DB_NAME;
	}	


	//checking if database existing 
	public boolean checkdb() throws IOException {
		SQLiteDatabase sdb1 = null;
		try {
			String path = ORIGINAL_PATH + OriginalDataBase;
			File f=new File(path);
			if(f.exists()){
				sdb1 = SQLiteDatabase.openDatabase(path, null,
						SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				StaticVariables.printLog("TAG","wls db file exists");
			}else{
				StaticVariables.printLog("TAG","wls db file not exists");
			}


		} catch (Exception e) {
			//e.printStackTrace();
			StaticVariables.printLog("TAG","unable to open db");
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
			StaticVariables.printLog("TAG","wireless db path :"+path); 
			File f=new File(path);
			if(f.exists()){
				//making database instance read/write able
				sdb = SQLiteDatabase.openDatabase(path, null,
						SQLiteDatabase.OPEN_READWRITE
						+ SQLiteDatabase.NO_LOCALIZED_COLLATORS);
				StaticVariables.printLog("TAG","wls db file exists");
			}else{
				StaticVariables.printLog("TAG","wls db file not exists");
			}

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


	//****************************DataBase Operations****************************//

	//Fetching Max Wireless Device Number from config. table
	public int Max_CNFG_DeviceNumber(){
		int maxDevNo=0;
		try{
			SQLiteStatement stmt = sdb.compileStatement("select MAX("+CNFG_DEVNO+") from "+CNFG_TABLE+";");
			maxDevNo = (int) stmt.simpleQueryForLong();
		}catch(Exception e){
			e.printStackTrace();
		}

		return maxDevNo;
	}


	//getting max room number
	public int Max_CNFG_RoomNumber(){
		int maxDevNo=0;
		try{
			SQLiteStatement stmt = sdb.compileStatement("select MAX("+CNFG_ROOM_NUMBER+") from "+CNFG_TABLE+";");
			maxDevNo = (int) stmt.simpleQueryForLong();
		}catch(Exception e){
			e.printStackTrace();
		}

		return maxDevNo;
	}


	//Getting Coordinator ID From ServerDetails Table
	public String WLS_CordinatorID(){
		String DeviceID=null;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(SERVER_CNFG_TABLE, new String[] {SERVER_CNFG_DevId},SERVER_CNFG_ID+"=1" , 
					null, null, null, null);
			if(mcursor.getCount()!=0){
				mcursor.moveToFirst();
				DeviceID=mcursor.getString(mcursor.getColumnIndex(SERVER_CNFG_DevId));
				Log.d("TAG","wireless server Cordinator Id :"+DeviceID);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return DeviceID;
	}	

	//checking if Device is already Configured or not
	public String isDeviceExists(String id) {
		String  RESULT="FALSE",devtype = null, devid1 = null ;
		Cursor mcursor = null ;
		int old_dev_no=0;
		//checking device in Config table
		try {
			mcursor = sdb.rawQuery("select * from "+CNFG_TABLE+" where "+CNFG_DEVID+"='"+ id + "';", null);
			if (mcursor.getCount()!=0) {
				mcursor.moveToFirst();				//placing cursor to first position
				devtype = mcursor.getString(mcursor.getColumnIndex(CNFG_DEVTYPE));
				devid1 = mcursor.getString(mcursor.getColumnIndex(CNFG_DEVID));
				old_dev_no= mcursor.getInt(mcursor.getColumnIndex(CNFG_DEVNO));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((devid1!=null && devid1.equals(id))) {
			Log.d("TAG", "device exists");
			//device existing and setting result TRUE & Device Name
			RESULT="TRUE"+devtype+old_dev_no;	
		}else{
			Log.d("TAG", "device not exists");
			RESULT="FALSE";
		}

		return RESULT;
	}	


	//Updating Or Inserting Records in Config Table
	public boolean Update_CNFG_Table(String devid,String devname,int devtype,
			int devno,String RoomName_WLS,int RoomNo,String usr_dev_name){
		boolean isUpdated=false;
		try{
			String Result=isDeviceExists(devid);
			boolean isDevExists=false;
			String devid1 = null ;
			Cursor mcursor = null ;
			int old_devType=0;

			//checking device in Config table
			try {
				mcursor = sdb.rawQuery("select * from "+CNFG_TABLE+" where "+CNFG_DEVID+"='"+ devid + "';", null);
				if (mcursor.getCount()!=0) {
					mcursor.moveToFirst();		//placing cursor to first position
					devid1 = mcursor.getString(mcursor.getColumnIndex(CNFG_DEVID));
					old_devType= mcursor.getInt(mcursor.getColumnIndex(CNFG_DEVTYPE));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			if ((devid1!=null && devid1.equals(devid))) {
				Log.d("TAG", "device exists");
				//device existing and setting result TRUE
				isDevExists=true;

				//checking if new device type and old device type are same or not
				//if not then deleting old records from wireless table
				if(devtype!=old_devType) {
					//deleting records of device from wireless table
					boolean deleted=DeletePanelSettings(devno);
					if(deleted){
						Log.d("TAG","details deleted");
					}
				}



			}


			Log.d("CNFG","IS DEVICE ID EXISTS : "+Result);
			if(!isDevExists){
				//Inserting record in Wireless table
				ContentValues cv = new ContentValues();
				cv.put(CNFG_DEVNAME,devname);
				cv.put(CNFG_DEVNO,devno);
				cv.put(CNFG_DEVTYPE,devtype);
				cv.put(CNFG_DEVID,devid);
				cv.put(CNFG_ROOMNAME,RoomName_WLS);
				cv.put(CNFG_ROOM_NUMBER,RoomNo);

				cv.put(Data1,"Default");
				cv.put(Data2,"Default");
				cv.put(Data3,"Default");
				cv.put(Data4,"Default");
				cv.put(Data5,"Default");
				cv.put(Data6,"Default");
				cv.put(Data7,"Default");
				cv.put(Data8,"Default");
				cv.put(Data9,"Default");
				cv.put(Data10,"Default");

				cv.put(CNFG_Data3,usr_dev_name);
				//inserting data into table
				sdb.insert(CNFG_TABLE, null, cv);
				Log.d("CNFG", "CNFG_TABLE data inserted ");
				isUpdated=true;
			}else{
				//update existing record in switch board table
				ContentValues cv = new ContentValues();
				cv.put(CNFG_DEVNAME,devname);
				cv.put(CNFG_DEVNO,devno);
				cv.put(CNFG_DEVTYPE,devtype);
				cv.put(CNFG_ROOMNAME,RoomName_WLS);
				cv.put(CNFG_ROOM_NUMBER,RoomNo);
				cv.put(CNFG_Data3,usr_dev_name);

				sdb.update(CNFG_TABLE, cv, CNFG_DEVID+"='" + devid + "';", null);
				Log.d("CNFG", "CNFG_TABLE data updated ");
				isUpdated=true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return isUpdated;
	}

	//method to insert static panel's data in congig table
	public boolean insertOrUpdateStaticpanelData(int roomNo,String roomName,
			String devName_WLS,int devNo_WLS,int devtype_WLS,String devId_WLS,
			String DevName,String loacalRefData,int switchNo_WLS,String switchData_WLS,String devType,
			int devNo,String devId,String devSwitchNo,String RoomName_WLS)
	{
		boolean updated=false;
		try{
			Cursor mcursor=null;
			try{
				mcursor=sdb.query(false, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
						WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
						DevId,DevSwitchNo},
						WRLS_DevNo+"="+devNo_WLS+" AND "+DevNo+"="+devNo+" AND "+WRLS_SwitchNo+"="+switchNo_WLS, null, null, null, null, null);

			}catch(Exception e){
				e.printStackTrace();
			}
			//checking whether cursor have some data or not
			if(mcursor!=null && mcursor.getCount()>0){ 
				//if cursor have data then update existing records

				try{
					ContentValues cv = new ContentValues();
					cv.put(WRLS_SwitchData,switchData_WLS);
					cv.put(DevSwitchNo,devSwitchNo);

					//inserting data into table
					sdb.update(WRLS_TABLE ,cv,WRLS_DevNo+"="+devNo_WLS+
							" AND "+DevNo+"="+devNo+" AND "+WRLS_SwitchNo+"="+switchNo_WLS, null);
					Log.d("WRLS", "static data Updated ");
					updated=true;
				}catch(Exception e){
					e.printStackTrace();
				}

			}else{
				//if cursor is not having data then insert new records

				try{
					ContentValues cv = new ContentValues();
					cv.put(RoomNo,roomNo);
					cv.put(RoomName,roomName);
					cv.put(WRLS_DeviceName,devName_WLS);
					cv.put(WRLS_DevNo,devNo_WLS);
					cv.put(WRLS_DevType,devtype_WLS);
					cv.put(WRLS_DevId,devId_WLS);
					cv.put(DeviceName,DevName);
					cv.put(WRLS_SwitchNo,switchNo_WLS);
					cv.put(WRLS_SwitchData,switchData_WLS);
					cv.put(WRLS_RoomName,RoomName_WLS);

					cv.put(DevType,devType);
					cv.put(DevNo,devNo);
					cv.put(DevId,devId);
					cv.put(DevSwitchNo,devSwitchNo);

					cv.put(DevLocalRef,loacalRefData);

					cv.put(Data2,"Default");
					cv.put(Data3,"Default");
					cv.put(Data4,"Default");
					cv.put(Data5,"Default");
					cv.put(Data6,"Default");
					cv.put(Data7,"Default");
					cv.put(Data8,"Default");
					cv.put(Data9,"Default");
					cv.put(Data10,"Default");
					//inserting data into table
					sdb.insert(WRLS_TABLE, null, cv);
					Log.d("WRLS", "static data Inserted ");
					updated=true;
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}


	//method to fetch wireless panel configured to server
	public Cursor WirelessDeviceData(){
		return sdb.query(true,WRLS_TABLE, new String[] {WRLS_DevType,WRLS_DevNo,RoomNo,RoomName,WRLS_DeviceName,
				WRLS_DevId, DeviceName, WRLS_SwitchData,DevType,DevNo,
				DevId,DevSwitchNo},null, null, null, null, null, null);
	}

	//fetching record cursor corresponding to  device type and will get unique records
	public Cursor CNFG_DeviceRecords(String devtype){
		Cursor mcursor=null  ;

		//Fetching room names from switch board table
		try{
			mcursor=sdb.query(true, CNFG_TABLE, new String[]{CNFG_DEVTYPE,CNFG_DEVNO,
					CNFG_DEVNAME,CNFG_DEVID,CNFG_ROOMNAME,CNFG_Data3},
					CNFG_DEVTYPE+"="+devtype+";", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor.getCount()!=0){
				mcursor.moveToFirst();	//placing cursor to first position			
			}
		}catch(Exception e){
			e.printStackTrace();
		}	

		return mcursor;

	}

	//fetching record cursor corresponding to  roomname and will get unique records
		public Cursor CNFG_DeviceroomRecords(String devtype,String Rname){
			Cursor mcursor=null  ;
						
			//Fetching room names from switch board table
			try{
				mcursor=sdb.query(true, CNFG_TABLE, new String[]{CNFG_DEVTYPE,CNFG_DEVNO,
						CNFG_DEVNAME,CNFG_DEVID,CNFG_ROOMNAME},
						CNFG_DEVTYPE+"="+devtype+" And "+CNFG_ROOMNAME+"="+Rname+";", null, null, null, null, null);
				//checking whether cursor have some data or not
				if(mcursor.getCount()!=0){
					mcursor.moveToFirst();	//placing cursor to first position			
				}
			}catch(Exception e){
				e.printStackTrace();
			}	

			return mcursor;

		}
	
	//inserting wireless details
	public boolean Update_WRLS_details(int roomNo,String roomName,String devName_WLS,
			int devNo_WLS,int devtype_WLS,String devId_WLS,String DevName,String loacalRefData,
			int switchNo_WLS,String switchData_WLS,String devType,int devNo,
			String devId,String devSwitchNo,String RoomName_WLS,String usr_dv_nam)
	{
		boolean istoUpdate=false,recordsupdated=false;

		try{
			Cursor mcursor =ConfiguredPanelPreDetails(devNo_WLS,devNo,switchNo_WLS);
			if(mcursor!=null && mcursor.getCount()>0){
				istoUpdate=true;
				//closing the cursor
				mcursor.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//checking if records needs to be update or insert
		if(istoUpdate){

			try{
				ContentValues cv = new ContentValues();
				cv.put(WRLS_SwitchData,switchData_WLS);
				cv.put(DevSwitchNo,devSwitchNo);
				cv.put(DevLocalRef,loacalRefData);

				//inserting data into table
				sdb.update(WRLS_TABLE ,cv,WRLS_DevNo+"="+devNo_WLS+
						" AND "+DevNo+"="+devNo+" AND "+WRLS_SwitchNo+"="+switchNo_WLS, null);
				Log.d("WRLS", "WRLS data Updated ");
				recordsupdated=true;

			}catch(Exception e){
				e.printStackTrace();
			}

		}else{

			try{
				ContentValues cv = new ContentValues();
				cv.put(RoomNo,roomNo);
				cv.put(RoomName,roomName);
				cv.put(WRLS_DeviceName,devName_WLS);
				cv.put(WRLS_DevNo,devNo_WLS);
				cv.put(WRLS_DevType,devtype_WLS);
				cv.put(WRLS_DevId,devId_WLS);
				cv.put(DeviceName,DevName);
				cv.put(WRLS_SwitchNo,switchNo_WLS);
				cv.put(WRLS_SwitchData,switchData_WLS);
				cv.put(WRLS_RoomName,RoomName_WLS);

				cv.put(DevType,devType);
				cv.put(DevNo,devNo);
				cv.put(DevId,devId);
				cv.put(DevSwitchNo,devSwitchNo);
				cv.put(DevLocalRef,loacalRefData);

				cv.put(Data2,"Default");
				cv.put(Data3,usr_dv_nam);
				cv.put(Data4,"Default");
				cv.put(Data5,"Default");
				cv.put(Data6,"Default");
				cv.put(Data7,"Default");
				cv.put(Data8,"Default");
				cv.put(Data9,"Default");
				cv.put(Data10,"Default");
				//inserting data into table
				sdb.insert(WRLS_TABLE, null, cv);
				Log.d("WRLS", "WRLS data Inserted ");
				recordsupdated=true;

			}catch(Exception e){
				e.printStackTrace();
			}
		}


		return recordsupdated;
	}


	
	//inserting wireless details for pir
	public boolean Update_WRLS_details_PIR(int roomNo,String roomName,String devName_WLS,
			int devNo_WLS,int devtype_WLS,String devId_WLS,String DevName,String loacalRefData,
			int switchNo_WLS,String switchData_WLS,String devType,int devNo,
			String devId,String devSwitchNo,String RoomName_WLS,String Lightsensorval,String usr_dv_nam)
	{
		boolean istoUpdate=false,recordsupdated=false;

		try{
			Cursor mcursor =ConfiguredPanelPreDetails_PIR(devNo_WLS,devNo,switchNo_WLS,Lightsensorval);
			if(mcursor!=null && mcursor.getCount()>0){
				istoUpdate=true;
				//closing the cursor
				mcursor.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//checking if records needs to be update or insert
		if(istoUpdate){

			try{
				ContentValues cv = new ContentValues();
				cv.put(WRLS_SwitchData,switchData_WLS);
				cv.put(DevSwitchNo,devSwitchNo);
				cv.put(DevLocalRef,loacalRefData);
				cv.put(Data2,Lightsensorval);
				
				//inserting data into table
				sdb.update(WRLS_TABLE ,cv,WRLS_DevNo+"="+devNo_WLS+
				" AND "+DevNo+"="+devNo+" AND "+WRLS_SwitchNo+"="+switchNo_WLS+" AND "+Data2+"="+Lightsensorval, null);
				Log.d("WRLS", "WRLS data Updated ");
				recordsupdated=true;

			}catch(Exception e){
				e.printStackTrace();
			}

		}else{

			try{
				ContentValues cv = new ContentValues();
				cv.put(RoomNo,roomNo);
				cv.put(RoomName,roomName);
				cv.put(WRLS_DeviceName,devName_WLS);
				cv.put(WRLS_DevNo,devNo_WLS);
				cv.put(WRLS_DevType,devtype_WLS);
				cv.put(WRLS_DevId,devId_WLS);
				cv.put(DeviceName,DevName);
				cv.put(WRLS_SwitchNo,switchNo_WLS);
				cv.put(WRLS_SwitchData,switchData_WLS);
				cv.put(WRLS_RoomName,RoomName_WLS);

				cv.put(DevType,devType);
				cv.put(DevNo,devNo);
				cv.put(DevId,devId);
				cv.put(DevSwitchNo,devSwitchNo);
				cv.put(DevLocalRef,loacalRefData);

				cv.put(Data2,Lightsensorval);
				
				cv.put(Data3,usr_dv_nam);
				cv.put(Data4,"Default");
				cv.put(Data5,"Default");
				cv.put(Data6,"Default");
				cv.put(Data7,"Default");
				cv.put(Data8,"Default");
				cv.put(Data9,"Default");
				cv.put(Data10,"Default");
				//inserting data into table
				sdb.insert(WRLS_TABLE, null, cv);
				Log.d("WRLS", "WRLS data Inserted ");
				recordsupdated=true;

			}catch(Exception e){
				e.printStackTrace();
			}
		}


		return recordsupdated;
	}

	
	//wireless panel room list
	public TreeSet<String> WirelessPanelsRoomNameList(){
		TreeSet<String>UniqueRoomNames=new TreeSet<String>();//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();//list for storing room names temporarly
		Cursor mcursor1 = null;		//initialize cursor
		String RoomName=null;

		//Fetching room names from Config  table
		try{
			mcursor1=sdb.query(true, CNFG_TABLE, new String[]{CNFG_ROOMNAME},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1.getCount()!=0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					RoomName=mcursor1.getString(mcursor1.getColumnIndex(CNFG_ROOMNAME));	//getting room name from cursor
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
		Log.d("TAG","Panel Room List Size is :"+UniqueRoomNames.size());

		return UniqueRoomNames;		

	}

	//method to fetch data related to a room
	public Cursor Rooms_panelData(String roomName,String devtype){
		return sdb.query(true,CNFG_TABLE, new String[] { CNFG_DEVID,CNFG_DEVNO,
				CNFG_DEVTYPE,CNFG_DEVNAME ,CNFG_Data3},CNFG_ROOMNAME+"='"+roomName+"' AND "+CNFG_DEVTYPE+"="+devtype,
				null, null, null, null, null);

	}

	//method to fetch data related to a room
	public Cursor Current_Configured_Records(String wdevid,int wdevno,String currentRoom,String devtype){
		return sdb.query(true,WRLS_TABLE, new String[] {DevNo,WRLS_DevId,WRLS_DevNo,WRLS_DevType,
				RoomNo,RoomName,WRLS_DeviceName,DeviceName,WRLS_SwitchNo, WRLS_SwitchData,DevType,
				DevId,DevSwitchNo,DevLocalRef,Data3},WRLS_DevId+"= '"+wdevid+"' AND "+WRLS_DevNo+"="+wdevno+" AND "+
						RoomName+"='"+currentRoom+"' AND "+DevType+"='"+devtype+"'", null, null, null,WRLS_SwitchNo, null);
	}

	//method to fetch rooms related to currently selected panel to delete
	public  TreeSet<String> Current_PanelsRoomNameList(String wdevid,int wdevno){
		TreeSet<String>UniqueRoomNames=new TreeSet<String>();//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();//list for storing room names temporarly
		Cursor mcursor1 = null;		//initialize cursor
		String roomname=null;

		//Fetching room names from Config board table
		try{
			mcursor1=sdb.query(true, WRLS_TABLE, new String[]{RoomName},WRLS_DevId+"='"+wdevid+"' AND "+WRLS_DevNo+"="+wdevno, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1.getCount()!=0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					roomname=mcursor1.getString(mcursor1.getColumnIndex(RoomName));	//getting room name from cursor
					roomNames.add(roomname);	// adding room names one by one in array list
				}while(mcursor1.moveToNext());
				//closing the cursor
				mcursor1.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}


		//adding unique rooms from arraylist into tree list
		UniqueRoomNames.addAll(roomNames);
		Log.d("TAG","Panel Room List Size is :"+UniqueRoomNames.size());

		return UniqueRoomNames;		
	}

	//checking if panel already configured to some device or not
	public boolean isPanelConfiguredToDevice(int wdevno,int devno){
		boolean isRecordExists=false;
		try{
			Cursor mcursor=sdb.query(false, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName,WRLS_SwitchNo, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},WRLS_DevNo+"="+wdevno+" AND "+DevNo+"="+devno, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor.getCount()!=0){
				isRecordExists=true;
				//closing the cursor
				mcursor.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return isRecordExists;
	}

	
	//checking if PIR panel already configured to some device or not
		public boolean isPanelConfiguredToDevice_PIR(int wdevno,int devno,String lightval){
			boolean isRecordExists=false;
			try{
				Cursor mcursor=sdb.query(false, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
						WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName,WRLS_SwitchNo, WRLS_SwitchData,DevType,DevNo,
						DevId,DevSwitchNo,DevLocalRef},WRLS_DevNo+"="+wdevno+" AND "+DevNo+"="+devno+" AND "+Data2+"="+lightval, null, null, null, null, null);
				//checking whether cursor have some data or not
				if(mcursor.getCount()!=0){
					isRecordExists=true;
					//closing the cursor
					mcursor.close();
				}
			}catch(Exception e){
				e.printStackTrace();
			}
			return isRecordExists;
		}
	
	//method to fetch cursor data related to already configured device
	public Cursor ConfiguredPanelPreDetails(int wdevno,int devno,int wsno){
		Cursor mcursor=null;
		try{
			Cursor cursor=sdb.query(false, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},
					WRLS_DevNo+"="+wdevno+" AND "+DevNo+"="+devno+" AND "+WRLS_SwitchNo+"="+wsno, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				//assigning cursor 
				return cursor; 
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return mcursor;
	}

	public Cursor ConfiguredPanelPreDetails_PIR(int wdevno,int devno,int wsno,String ligval){
		Cursor mcursor=null;
		try{
			Cursor cursor=sdb.query(false, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},
					WRLS_DevNo+"="+wdevno+" AND "+DevNo+"="+devno+" AND "+WRLS_SwitchNo+"="+wsno+
					" AND "+Data2+"="+ligval, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(cursor.getCount()!=0){
				cursor.moveToFirst();
				//assigning cursor 
				return cursor; 
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return mcursor;
	}

	//method to fetch configured device number list
	public ArrayList<String> ConfiguredPanelDeviceNumberList(int wdevno,int wsno,String wiredDeviceRoomName){
		Cursor mcursor=null;
		ArrayList<String> deviceNumbers=new ArrayList<String>();

		try{
			mcursor=sdb.query(true, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},
					WRLS_DevNo+"="+wdevno+" AND "+WRLS_SwitchNo+"="+wsno+" AND "+RoomName+"='"+wiredDeviceRoomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor.getCount()!=0){
				mcursor.moveToFirst();

				do{

					//get dev no.
					int dno=mcursor.getInt(mcursor.getColumnIndex(DevNo));
					deviceNumbers.add(String.valueOf(dno));

				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(mcursor!=null){
				mcursor.close();
			}

		}

		return deviceNumbers;
	}

	
	//method to fetch configured device number list PIR
	public ArrayList<String> ConfiguredPanelDeviceNumberList_PIR(int wdevno,int wsno,String wiredDeviceRoomName,String ligval){
		Cursor mcursor=null;
		ArrayList<String> deviceNumbers=new ArrayList<String>();

		try{
			mcursor=sdb.query(true, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},
					WRLS_DevNo+"="+wdevno+" AND "+WRLS_SwitchNo+"="+wsno+" AND "+RoomName+"='"+wiredDeviceRoomName+"'"+" AND "+Data2+"='"+ligval+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor.getCount()!=0){
				mcursor.moveToFirst();

				do{

					//get dev no.
					int dno=mcursor.getInt(mcursor.getColumnIndex(DevNo));
					deviceNumbers.add(String.valueOf(dno));

				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(mcursor!=null){
				mcursor.close();
			}

		}

		return deviceNumbers;
	}
	
	
	//method to fetch configured device number list
	public ArrayList<String> ConfiguredStaticPanelDeviceNumberList(int wdevno,String wiredDeviceRoomName){
		Cursor mcursor=null;
		ArrayList<String> deviceNumbers=new ArrayList<String>();

		try{
			mcursor=sdb.query(true, WRLS_TABLE, new String[]{WRLS_DevId,WRLS_DevNo,WRLS_DevType,
					WRLS_RoomName,RoomNo,RoomName,WRLS_DeviceName,DeviceName, WRLS_SwitchData,DevType,DevNo,
					DevId,DevSwitchNo,DevLocalRef},
					WRLS_DevNo+"="+wdevno+" AND "+RoomName+"='"+wiredDeviceRoomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor.getCount()!=0){
				mcursor.moveToFirst();

				do{

					//get dev no.
					int dno=mcursor.getInt(mcursor.getColumnIndex(DevNo));
					deviceNumbers.add(String.valueOf(dno));

				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(mcursor!=null){
				mcursor.close();
			}

		}

		return deviceNumbers;
	}

	//method to delete panel setting corresponding to particular panel switch
	public boolean DeletePanelSwitchDetails(int wdevno,int devno,int wsno){
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, WRLS_DevNo+"=" + wdevno + " AND "+DevNo+"="+devno+" AND "+WRLS_SwitchNo+"="+wsno+" ;", null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}


	//method to delete panel setting corresponding to particular panel switch PIR
	public boolean DeletePanelSwitchDetails_PIR(int wdevno,int devno,int wsno,String lightval){
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, WRLS_DevNo+"=" + wdevno + " AND "+DevNo+"="+devno+" AND "+WRLS_SwitchNo+"="+wsno+" AND "+Data2+"="+lightval+" ;", null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}
	//method to delete panel setting corresponding to particular panel switch
	public boolean DeleteWiredDeviceDetails(int devno){
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, DevNo+"="+devno+";", null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}
	//method to delete room
	public boolean DeleteWiredroomDetails(String roomname){
		//roomname="'"+roomname+"'";
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, RoomName+"='"+roomname+"'", null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}

	
	
public boolean deleteroom(String roomname){
		
		roomname="'"+roomname+"'";
		sdb.beginTransaction();
		try{
			boolean rowsdeleted=sdb.delete(WRLS_TABLE, RoomName+"='"+roomname+"'", null)>0;	
			boolean rowsdeletedconfig=sdb.delete(CNFG_TABLE, RoomName+"='"+roomname+"'", null)>0;	
			
			sdb.setTransactionSuccessful();
			return true;
		}catch(Exception ce){
			ce.printStackTrace(); 
		}finally{
			sdb.endTransaction();
			close();
		}
		return false;

	}


public boolean deleteroomtest(String roomname){
	
	//roomname="'"+roomname+"'";
	
	boolean isDeleted=false;

	try{
		int rowsdeleted1=sdb.delete(WRLS_TABLE, RoomName+"='"+roomname+"'", null);
		int rowsdeleted2=sdb.delete(CNFG_TABLE, CNFG_ROOMNAME+"='"+roomname+"'", null);
		if(rowsdeleted1>0 || rowsdeleted2>0){
			isDeleted=true;
		}	
	}catch(Exception e){
		e.printStackTrace();
	}
	return isDeleted;	
}

public boolean EditRoomWireless(String presentroom,String Prevroom){
	boolean recordsupdated=false;
	
	//presentroom="'"+presentroom+"'";
	Prevroom="'"+Prevroom+"'";
	Log.d("WRLS", "Prevroom "+Prevroom);
	try{
		ContentValues cv = new ContentValues();
		cv.put(WRLS_RoomName,presentroom);
		cv.put(RoomName, presentroom);
		//inserting data into table
		int wrlstb=sdb.update(WRLS_TABLE ,cv,WRLS_RoomName+"="+Prevroom, null);

		StaticVariabes_div.log("WRLS", "wrlstb "+wrlstb);

		ContentValues cvcnfg = new ContentValues();
		cvcnfg.put(CNFG_ROOMNAME,presentroom);
		
		//inserting data into table
		int wrlsconfgtb=sdb.update(CNFG_TABLE ,cvcnfg,CNFG_ROOMNAME+"="+Prevroom, null);

		StaticVariabes_div.log("WRLS", "wrlsconfgtb "+wrlsconfgtb);

		StaticVariabes_div.log("WRLS", "WRLS room data Updated ");

		if(wrlstb>0 || wrlsconfgtb>0){
			Log.d("WRLS", "WRLS room data Updated ");
			recordsupdated=true;
		}	
		

	}catch(Exception e){
		e.printStackTrace();
	}

		
	return recordsupdated;
		
	
}

	public boolean EditDeviceName_Wireless(String presentdevname,String prevdevname){
		boolean recordsupdated=false;

		Log.d("WRLS", presentdevname+"WRLS room data Updated "+prevdevname);

		//presentroom="'"+presentroom+"'";
		prevdevname="'"+prevdevname+"'";
		try{
			ContentValues cv = new ContentValues();
			cv.put(Data3,presentdevname);

			//inserting data into table
			int wrlstb=sdb.update(WRLS_TABLE ,cv,Data3+"="+prevdevname, null);

			ContentValues cvcnfg = new ContentValues();
			cvcnfg.put(CNFG_Data3,presentdevname);

			//inserting data into table
			int wrlsconfgtb=sdb.update(CNFG_TABLE ,cvcnfg,CNFG_Data3+"="+prevdevname, null);

			Log.d("WRLS", "WRLS room data Updated ");

			if(wrlstb>0 || wrlsconfgtb>0){
				Log.d("WRLS", "WRLS room data Updated ");
				recordsupdated=true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return recordsupdated;
	}

public Cursor checkroomnameexistWrls(String roomname) {
	Cursor mCursor = sdb.rawQuery(
	            "SELECT b FROM "+WRLS_TABLE +" WHERE b='"+roomname+"'", null);	
	if(!(mCursor.getCount()>0))
		return null;
	if (mCursor != null) {
		mCursor.moveToFirst();
	}
	return mCursor;
	}

	public Cursor checkdevnameexistWrls(String devname) {
		Cursor mCursor = sdb.rawQuery(
				"SELECT b FROM "+WRLS_TABLE +" WHERE ec='"+devname+"'", null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	public Cursor checkdevnameexistconfgWrls(String devname) {
		Cursor mCursor = sdb.rawQuery(
				"SELECT wb FROM "+ CNFG_TABLE +" WHERE ec='"+devname+"'", null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}
	
	public Cursor checkroomnameexistconfgWrls(String roomname) {
		Cursor mCursor = sdb.rawQuery(
					"SELECT wb FROM "+ CNFG_TABLE +" WHERE wb='"+roomname+"'", null);
		if(!(mCursor.getCount()>0))
			return null;
		if (mCursor != null) {
			mCursor.moveToFirst();
		}
		return mCursor;
	}

	//method to delete panel setting corresponding to particular panel switch
	public boolean DeletePanel(int wdevno){
		boolean isDeleted=false;

		try{
			int rowsdeleted1=sdb.delete(WRLS_TABLE, WRLS_DevNo+"=" + wdevno, null);
			int rowsdeleted2=sdb.delete(CNFG_TABLE, WRLS_DevNo+"=" + wdevno, null);
			if(rowsdeleted1>0 || rowsdeleted2>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}


	//method to delete dimmer device related to particular panel 
	public boolean DeleteStaticPanelDevice(int wdevno,int devno){
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, WRLS_DevNo+"=" + wdevno + " AND "+DevNo+"="+devno+";", null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}	

	//method to delete old records of device from wireless table if device type of existing device changes 
	public boolean DeletePanelSettings(int wdevno){
		boolean isDeleted=false;

		try{
			int rowsdeleted=sdb.delete(WRLS_TABLE, WRLS_DevNo+"=" + wdevno, null);
			if(rowsdeleted>0){
				isDeleted=true;
			}	
		}catch(Exception e){
			e.printStackTrace();
		}

		return isDeleted;
	}	

	//method to get room number of supplied room name
	public int wirlessRoomNumber(String roomName){
		int room_number=0;
		try{
			Cursor mcursor3=sdb.query(true, CNFG_TABLE, new String[]{CNFG_ROOM_NUMBER},CNFG_ROOMNAME+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()!=0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_number=mcursor3.getInt(mcursor3.getColumnIndex(CNFG_ROOM_NUMBER));	//getting room name from cursor
				}while(mcursor3.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return room_number;
	}


	//method to get room number of supplied room name
	public String wirlessRoomImages(String roomName){
		String room_img_nam=null;
		try{
			Cursor mcursor3=sdb.query(true, CNFG_TABLE, new String[]{CNFG_Data2},CNFG_ROOMNAME+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()!=0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_img_nam=mcursor3.getString(mcursor3.getColumnIndex(CNFG_Data2));	//getting room name from cursor
				}while(mcursor3.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return room_img_nam;
	}


	//getting list of all unique room numbers
	public TreeSet<Integer> getUniqueRoomsNo(){
		TreeSet<Integer> rno=new TreeSet<Integer>();
		Cursor mcursor=null;
		try{

			mcursor=sdb.query(true, CNFG_TABLE, new String[]{CNFG_ROOM_NUMBER}, null,
					null, null, null, null, null);
			int roomno=0;
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					roomno=mcursor.getInt(mcursor.getColumnIndex(CNFG_ROOM_NUMBER));
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
		TreeSet<Integer> devno=new TreeSet<Integer>();
		Cursor mcursor=null;
		try{

			mcursor=sdb.query(true, CNFG_TABLE, new String[]{CNFG_DEVNO}, null,
					null, null, null, null, null);
			int dno=0;
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					dno=mcursor.getInt(mcursor.getColumnIndex(CNFG_DEVNO));
					devno.add(dno);
				}while(mcursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing cursor
			if(mcursor!=null)
				mcursor.close();
		}

		return devno;
	}



	//checking database integrity
	public  boolean isDataBaseCorrect(){

		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, SERVER_CNFG_TABLE, new String[]{SERVER_CNFG_ID}, null, null
					, null, null, null, null);

			//checking if cursor count is greater than 0
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.close();
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}
}
