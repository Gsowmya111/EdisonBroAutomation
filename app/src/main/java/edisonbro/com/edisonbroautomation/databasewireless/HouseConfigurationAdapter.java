package edisonbro.com.edisonbroautomation.databasewireless;

/**
 *  FILENAME: HouseConfigurationAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing tables .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.os.Environment;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;

public class HouseConfigurationAdapter {
	public static SQLiteDatabase sdb;
	Context mcontext;

	public String ORIGINAL_PATH =null;
	public String OriginalDataBase = null; 

	// Server Table Key Fields
	public static final String SERVER_TABLE ="ServerTable";
	public static final String SERVER_ID="_id";
	public static final String SERVER_IP="i";
	public static final String SERVER_PORT="p";
	public static final String SERVER_SSID="ss";
	public static final String SERVER_DEVID="d";
	public static final String SERVER_ADMIN_USERNAME="db";
	public static final String SERVER_ADMIN_PASSWORD="dc";

	// ConfigTable Key Fields
	public static final String CONFIG_TABLE ="ConfigTable";
	public static final String CONFIG_ID="a";
	public static final String CONFIG_MacID="b";
	public static final String CONFIG_WpcNo="da";

	// SwitchBoardTable Key Fields
	public static final String SWITCH_BOARD_TABLE ="SwitchBoardTable";
	public static final String SWB_ROWID      ="_id";
	public static final String SWB_RoomNo     ="a";
	public static final String SWB_RoomName   ="b";
	public static final String SWB_DeviceType ="c";	
	public static final String SWB_RoomImg    ="cg";
	public static final String SWB_DeviceNo   ="d";	
	public static final String SWB_DeviceName ="e";
	public static final String SWB_DeviceId   ="f";	 
	public static final String SWB_NoofSwitches="za";
	public static final String SWB_NoofFans="nfnl";
	public static final String SWB_AllOn="an";
	public static final String SWB_AllOff="af";
	public static final String SWB_TYPE="da";
	public static final String SWB_GroupId="ea";

	//****************************************
	public static final String SWB_Image_Name="eb";
	public static final String SWB_Usr_dev_Name="ec";

	//MasterTable Key Fields
	public static final String MASTER_TABLE ="MasterTable";
	public static final String MASTER_ROWID      ="_id";
	public static final String MASTER_RoomNo     ="a";
	public static final String MASTER_RoomName   ="b";
	public static final String MASTER_DeviceType ="c";	
	public static final String MASTER_Roomimg    ="cg";
	public static final String MASTER_DeviceNo   ="d";	
	public static final String MASTER_DeviceName ="e";
	public static final String MASTER_DeviceId   ="f";	
	public static final String MASTER_TYPE="da";
	public static final String MASTER_GroupId="ea";
	//public static final String MASTER_ImageName="eb";
	//public static final String MASTER_DevName="ec";


	//
	public static final String MASTER_Image_Name="eb";
	public static final String MASTER_Usr_dev_Name="ec";

	//ServerDetails Key Fields
	public static final String SERVER_DETAILS_TABLE ="ServerDetails";
	public static final String SERVER_DETAILS_ROWID="_id";
	public static final String SERVER_DETAILS_DEVID="di";
	public static final String SERVER_DETAILS_HOUSENO="dd";
	public static final String SERVER_DETAILS_JPAN="eb"; 
	public static final String SERVER_DETAILS_ADMIN_UNAME="un";
	public static final String SERVER_DETAILS_ADMIN_PASS="ps";
	public static final String SERVER_TIMESTAMP="ec";

	//CameraTable Key Fields
	public static final String CAM_TABLE ="CameraTable";
	public static final String CAM_RoomName="b"; 
	public static final String CAM_RoomNo="a";
	public static final String CAM_Number="cn"; 
	public static final String CAM_IP="ci"; 
	public static final String CAM_PORT="cp"; 
	public static final String CAM_NAME="ce"; 
	public static final String CAM_URL="cu";

	//
	public static final String CAM_Image_Name="eb";


	//IR Blaster Table Key Fields
	private static final String IRB_TABLE = "IRBlasterTable";
	public static final String IRB_ROWID = "_id";
	public static final String IRB_HOUSE_NO = "de";
	public static final String IRB_HOUSE_NAME = "dd";
	public static final String IRB_ROOM_NO = "a";
	public static final String IRB_ROOM_NAME = "b";
	public static final String IRB_DEV_TYPE = "c";
	public static final String IRB_DEV_NO = "d";
	public static final String IRB_DEV_NAME = "e";
	public static final String IRB_KEY_NUMBER = "kno";
	public static final String IRB_KEY_LOC_CODE="klc";
	public static final String IRB_KEY_GROUP = "kg";
	public static final String IRB_KEY_NAME = "knm";
	public static final String IRB_KEY_STATUS = "ks";
	public static final String IRB_KEY_DATA = "kd";


	private static final String WATERPUMP_TABLE_NAME="WaterPumpTable";

	//variables holding columns name for device table
	public static final String WPC_DEV_NAME="e";
	public static final String WPC_DEV_ID="f";
	public static final String WPC_DEV_TYPE="c"; 

	String MUD_DevType="WMD1" ,MUC_DevType="WMC1",MUB_DevType="WMB1",MBSO_DevType="WBSO"
			,BM_DevType="WBM1" ,SM_DevType="WSM1",
			SS_DevType="WSS1",OTS_DevType="WOTS";


	final String CUR_LRS_DevType="CLS1",CUR_RS_DevType="CRS1"
			,CUR_LRD_DevType="CLD1",CUR_RD_DevType="CRD1"
			,SINGLE_FAN_DevType="SFN1",DUAL_FAN_DevType="DFN1"
			,RGB_DevType="RGB1",DMR_DevType="DMR1",GSR_DevType="GSR1",
			CLS_DevType="CSW1",CB_DevType="CLB1",WLS_MS_DevType="WSS1";
	
	String PIR_DevName="WPS1",MOOD_SWITCH_DEVTYPE="WSS1", PIR_DevName2="WPD1";
	
	String CLB_TYPE="CLB1",CSW_TYPE="CSW1",CUR_TYPE="CLNR";


	//constructor
	public HouseConfigurationAdapter(Context context) {
		mcontext = context;		//passing context of current activity
		ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
		OriginalDataBase = StaticVariables.HOUSE_NAME+".db";
	}


	//constructor
	public HouseConfigurationAdapter(Context context, String DB_PATH, String DB_NAME) {
		mcontext = context;		//passing context of current activity
		ORIGINAL_PATH=DB_PATH;
		OriginalDataBase =DB_NAME;
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

	// ****************************Database Operations************************

	// Updating Server Table with IP/PORT/SSID
	public boolean updateServerTable(String ip,int port,String ssid) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(SERVER_IP, ip);
			cv.put(SERVER_PORT, port);
			cv.put(SERVER_SSID, ssid);
			sdb.update(SERVER_TABLE, cv, SERVER_ID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}

	// Updating Server Table with IP/PORT/SSID
	public boolean updateServerPort_ServerTable(int port ) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(SERVER_PORT, port); 
			sdb.update(SERVER_TABLE, cv, SERVER_ID+"=1", null);
			updated=true;
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}

	//Updating Mac-ID in Configtable
	public boolean updateConfigTable_MacID(String macId) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(CONFIG_MacID, macId);
			sdb.update(CONFIG_TABLE, cv, CONFIG_ID+"=1", null);
			updated=true;
			StaticVariables.printLog("UPDATE","MAC_ID UPDATED");
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}


	//************water pump controller updation in config table*****

	public boolean updateConfigTable_WpcNo(String updateNo) {
		boolean updated = false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(CONFIG_WpcNo, updateNo);
			sdb.update(CONFIG_TABLE, cv, CONFIG_ID+"=1", null);
			updated=true;
			StaticVariables.printLog("UPDATE","WPC Number UPDATED");
		}catch(Exception e){
			e.printStackTrace();
		}

		return updated;
	}	

	//getting unique room number
	public int getUniqueRoomNo(){
		int roomNo=0;
		TreeSet<Integer> allRoomNumbers=new TreeSet<Integer>();
		TreeSet<Integer> homeRoomNo=new TreeSet<Integer>();
		TreeSet<Integer> irRoomNo=new TreeSet<Integer>();
		TreeSet<Integer> wirelessRoomNo=new TreeSet<Integer>();

		try{
			//getting room numbers from wireless config table
			//opening database
			WirelessConfigurationAdapter WhouseDB=new WirelessConfigurationAdapter(mcontext);

			boolean isDbExists=WhouseDB.checkdb();
			if(isDbExists){
				WhouseDB.open();

				wirelessRoomNo=WhouseDB.getUniqueRoomsNo();

				//closing database
				WhouseDB.close();	
			}else{
				StaticVariables.printLog("TAG","wireless database not exists!");
			}

		} catch(Exception e){
			e.printStackTrace();
		}

		try{
			//getting room number from ir config table
			//opening database
			IRB_Adapter_ir irAdapter=new IRB_Adapter_ir(mcontext);

			irAdapter.open();

			irRoomNo=irAdapter.getUniqueRoomsNo();

			//closing database
			if(irAdapter!=null)
				irAdapter.close();	


		} catch(Exception e){
			e.printStackTrace();
		}


		try{
			homeRoomNo=homeUniqueRoomsNo();

			if(homeRoomNo.size()>0){
				allRoomNumbers.addAll(homeRoomNo);
			}

			if(irRoomNo.size()>0){
				allRoomNumbers.addAll(irRoomNo);
			}

			if(wirelessRoomNo.size()>0){
				allRoomNumbers.addAll(wirelessRoomNo);
			}

			//clearing refernces
			wirelessRoomNo.clear();
			irRoomNo.clear();
			homeRoomNo.clear();


			//getting unique room number

			for(int i=1;i<100;i++){
				if(!allRoomNumbers.contains(i)){
					roomNo=i;
					break;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}	     

		StaticVariables.printLog("TAG","room number to assign :"+roomNo);
		return roomNo;
	}

	//getting list of all unique room numbers
	public TreeSet<Integer> homeUniqueRoomsNo(){
		TreeSet<Integer> rno=new TreeSet<Integer>();
		Cursor mcursor1=null,mcursor2=null,mcursor3=null;
		try{
			int roomno=0; 

			//getting room numbers from switch board  table
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomNo}, null,
					null, null, null, null, null);

			if(mcursor1!=null && mcursor1.getCount()>0){
				mcursor1.moveToFirst();
				do{
					roomno=mcursor1.getInt(mcursor1.getColumnIndex(SWB_RoomNo));
					rno.add(roomno);
				}while(mcursor1.moveToNext());
			}

			//getting room numbers from master table

			mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_RoomNo}, null,
					null, null, null, null, null);

			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{
					roomno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_RoomNo));
					rno.add(roomno);
				}while(mcursor2.moveToNext());
			}

			//getting room numbers from camera table
			mcursor3=sdb.query(true, CAM_TABLE, new String[]{CAM_RoomNo}, null,
					null, null, null, null, null);


			if(mcursor3!=null && mcursor3.getCount()>0){
				mcursor3.moveToFirst();
				do{
					roomno=mcursor3.getInt(mcursor3.getColumnIndex(CAM_RoomNo));
					rno.add(roomno);
				}while(mcursor3.moveToNext());
			}


		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing cursor
			if(mcursor1!=null)
				mcursor1.close();
			if(mcursor2!=null)
				mcursor2.close();
			if(mcursor3!=null)
				mcursor3.close();
		}


		return rno;
	}	

	//getting list of all unique room numbers
	public TreeSet<Integer> homeUniqueDeviceNo(){
		TreeSet<Integer> dno=new TreeSet<Integer>();
		Cursor mcursor1=null,mcursor2=null;
		try{
			int devno=0; 

			//getting room numbers from switch board  table
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_DeviceNo}, null,
					null, null, null, null, null);

			if(mcursor1!=null && mcursor1.getCount()>0){
				mcursor1.moveToFirst();
				do{
					devno=mcursor1.getInt(mcursor1.getColumnIndex(SWB_DeviceNo));
					dno.add(devno);
				}while(mcursor1.moveToNext());
			}

			//getting room numbers from master table

			mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceNo}, null,
					null, null, null, null, null);

			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{
					devno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					dno.add(devno);
				}while(mcursor2.moveToNext());
			}

		}catch(Exception e){
			e.printStackTrace();
		}finally{
			//closing cursor
			if(mcursor1!=null)
				mcursor1.close();
			if(mcursor2!=null)
				mcursor2.close(); 
		}


		return dno;
	}	


	//getting unique room number
	public int getUniqueDeviceNo(){
		int roomNo=0;
		TreeSet<Integer> allDevNumbers=new TreeSet<Integer>();
		TreeSet<Integer> homeDevNo=new TreeSet<Integer>();
		TreeSet<Integer> irDevNo=new TreeSet<Integer>();
		TreeSet<Integer> wirelessDevNo=new TreeSet<Integer>();

		try{
			//getting room numbers from wireless config table
			//opening database
			WirelessConfigurationAdapter WhouseDB=new WirelessConfigurationAdapter(mcontext);

			boolean isDbExists=WhouseDB.checkdb();
			if(isDbExists){
				WhouseDB.open();

				wirelessDevNo=WhouseDB.getUniqueDeviceNo();

				//closing database
				WhouseDB.close();	
			}else{
				StaticVariables.printLog("TAG","wireless database not exists!");
			}

		} catch(Exception e){
			e.printStackTrace();
		}

		try{
			//getting device number from ir config table
			//opening database
			IRB_Adapter_ir irAdapter=new IRB_Adapter_ir(mcontext);

			irAdapter.open();

			irDevNo=irAdapter.getUniqueDeviceNo();

			//closing database
			if(irAdapter!=null)
				irAdapter.close();	


		} catch(Exception e){
			e.printStackTrace();
		}


		try{
			homeDevNo=homeUniqueDeviceNo();

			if(homeDevNo.size()>0){
				allDevNumbers.addAll(homeDevNo);
			}

			if(irDevNo.size()>0){
				allDevNumbers.addAll(irDevNo);
			}

			if(wirelessDevNo.size()>0){
				allDevNumbers.addAll(wirelessDevNo);
			}

			//clearing references
			wirelessDevNo.clear();
			irDevNo.clear();
			homeDevNo.clear();


			//getting unique device number	  
			for(int i=1;i<10000;i++){
				if(!allDevNumbers.contains(i)){
					roomNo=i;
					break;
				}
			}

		}catch(Exception e){
			e.printStackTrace();
		}	     

		StaticVariables.printLog("TAG","Device number to assign :"+roomNo);
		return roomNo;
	}



	//Getting Coordinator ID From ServerDetails Table
	public String CordinatorID(){
		String DeviceID=null;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(SERVER_DETAILS_TABLE, new String[] {SERVER_DETAILS_DEVID},SERVER_DETAILS_ROWID+"=1" , 
					null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				DeviceID=mcursor.getString(mcursor.getColumnIndex(SERVER_DETAILS_DEVID));
				StaticVariables.printLog("TAG","Cordinator Id :"+DeviceID);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return DeviceID;
	}

	//Getting Coordinator ID From ServerDetails Table
	public String CordinatorJPAN(){
		String JpanId=null;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(SERVER_DETAILS_TABLE, new String[] {SERVER_DETAILS_JPAN},SERVER_DETAILS_ROWID+"=1" , 
					null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				JpanId=mcursor.getString(mcursor.getColumnIndex(SERVER_DETAILS_JPAN));
				StaticVariables.printLog("TAG","Cordinator jpan :"+JpanId);
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return JpanId;
	}	



	//get cursor of server details table to fetch admin name and password
	public Cursor serverData(){
		Cursor mcursor=null;
		try{
			mcursor=sdb.query(SERVER_DETAILS_TABLE, new String[] {SERVER_DETAILS_ADMIN_UNAME,SERVER_DETAILS_ADMIN_PASS},
					SERVER_DETAILS_ROWID+"=2" , null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();	
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return mcursor;
	}

	//Updating houseNo column of server details table to check house status next time before downloading 
	//i.e whether house is configured or not
	public boolean UpdateHouseConfigStatus(){
		boolean isUpdated=false;

		try{
			ContentValues cv=new ContentValues();
			cv.put(SERVER_DETAILS_HOUSENO, 1);
			cv.put(SERVER_TIMESTAMP, getDateTime()); 
			sdb.update(SERVER_DETAILS_TABLE, cv, SERVER_DETAILS_ROWID+"=1", null);
			isUpdated=true;	

		}catch(Exception e){
			e.printStackTrace();
		}

		return isUpdated;
	}





	//Inserting or Updating Configured Device's Data in Respective Database Table
	public boolean UpdateMaster(String devname, String devtype, int devno,String dev_address,
			String roomname, int roomno,String device_type_name,String groupId,String roomiconimg,String userdevname)
	{
		String devID = null, SWBdevID = null;
		boolean isDevExists = false; 

		//checking whether device id already existing or not in master table
		try {
			Cursor mcursor = null;
			mcursor = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_DeviceId+"='"+ dev_address + "';", null);
			if (mcursor!=null&& mcursor.getCount()>0) 	{
				mcursor.moveToFirst();		//Placing Cursor to first Position

				do {
					devID = mcursor.getString(mcursor.getColumnIndex(MASTER_DeviceId));

					if (devID.equals(dev_address)) {
						StaticVariables.printLog("UPDATE MASTER", "data exists");
						isDevExists = true;

						StaticVariables.printLog("DEV NUMBER","DEVICE NUMBER : "+devno);		
						break;
					}

				} while (mcursor.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Checking if Device ID existing in switch board table 
		try {
			Cursor cursor = null;
			boolean devExists = false;
			cursor = sdb.rawQuery("select * from "+SWITCH_BOARD_TABLE+" where "+SWB_DeviceId+"='"+ dev_address + "';", null);
			if (cursor!=null&& cursor.getCount()>0) {
				cursor.moveToFirst();		//Placing Cursor to first Position
				do {
					SWBdevID = cursor.getString(cursor.getColumnIndex(SWB_DeviceId));

					if (SWBdevID.equals(dev_address)) {
						StaticVariables.printLog("found in SwitchBoard table", "device exists");
						devExists = true;

						StaticVariables.printLog("DEV NUMBER","DEVICE NUMBER : "+devno);

						break;
					}
				} while (cursor.moveToNext());
			}

			//If Device Id Exists in SwitchBoard Table Then deleting device from switch board table
			//so that unique device must exist in either table
			if (devExists) {
				sdb.delete(SWITCH_BOARD_TABLE, SWB_DeviceId+"='" + dev_address + "';", null);
				devExists = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//if device id already existing in master table then updating 
		//existing record else inserting new record
		if (isDevExists) {
			try {
				//**********Updating Master Table******************

				ContentValues cv = new ContentValues();
				cv.put(MASTER_DeviceName, devname);
				cv.put(MASTER_DeviceNo, devno);
				cv.put(MASTER_RoomName, roomname);
				cv.put(MASTER_RoomNo, roomno);
				cv.put(MASTER_DeviceType, devtype);
				cv.put(MASTER_TYPE, device_type_name);
				cv.put(MASTER_GroupId, groupId);

	     		cv.put(MASTER_Image_Name,roomiconimg);
				cv.put(MASTER_Usr_dev_Name,userdevname);



				sdb.update(MASTER_TABLE, cv, MASTER_DeviceId+"='" + dev_address + "';", null);
				StaticVariables.printLog("MASTER", "Master data updated ");

				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

		} else {

			//Inserting new record in master table
			try {
				ContentValues cv = new ContentValues();
				cv.put(MASTER_DeviceNo, devno);
				cv.put(MASTER_DeviceId, dev_address);
				cv.put(MASTER_DeviceName, devname);
				cv.put(MASTER_RoomName, roomname);
				cv.put(MASTER_RoomNo, roomno);
				cv.put(MASTER_DeviceType, devtype);
				cv.put(MASTER_TYPE, device_type_name); 
				cv.put(MASTER_GroupId, groupId);

				cv.put(MASTER_Image_Name,roomiconimg);
				cv.put(MASTER_Usr_dev_Name,userdevname);

				cv.put("g", 0);
				cv.put("h", 0);
				cv.put("i", 0);  

				cv.put("db", "Default");
				cv.put("dc", "Default");
				cv.put("dd", "Default");
				cv.put("de", "Default");
				cv.put("df", "Default");
				cv.put("dg", "Default");
				cv.put("dh", "Default");
				cv.put("di", "Default");
				cv.put("dj", "Default");
				cv.put("dk", "Default");
				cv.put("dl", "Default");
				cv.put("dm", "Default");
				cv.put("dn", "Default");
				cv.put("do", "Default");
				cv.put("dp", "Default");
				cv.put("dq", "Default");
				cv.put("dr", "Default");
				cv.put("ds", "Default");
				cv.put("dt", "Default");
				cv.put("du", "Default");
				cv.put("dv", "Default");
				cv.put("dw", "Default");
				cv.put("dx", "Default");
				cv.put("dy", "Default");
				cv.put("dz", "Default");
				cv.put("dza","Default");
				cv.put("dzb","Default");
				cv.put("dzc","Default");
				cv.put("dzd","Default");
				cv.put("dzd","Default");

				//cv.put("eb","Default");
				//cv.put("ec","Default");
				cv.put("ed","Default");
				cv.put("ee","Default");
				cv.put("ef","Default");
				cv.put("eg","Default");
				cv.put("eh","Default");
				cv.put("ei","Default");
				cv.put("ej","Default");

				sdb.insert(MASTER_TABLE, null, cv);
				StaticVariables.printLog("MASTER TABLE", "Master inserted data saved");


				return true;
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

	}

	//Inserting or Updating Configured Device's Data in Respective Database Table
	public boolean UpdateSwitch(String devname,String devtype,int devno,
			String dev_address, String num_swtch, String num_fans,String roomname,
			int roomno,String device_type_name,String groupId,String iconnam,String usrdevname)
	{
		String devID = null, SWBdevID = null;
		boolean isDevIdExists = false; 

		int SwitchCount = Integer.parseInt(num_swtch);
		int FanCount = Integer.parseInt(num_fans);

		StaticVariables.printLog("","Device id is : "+dev_address);	

		//Checking if Device ID already Existing in Switch board table
		try {
			Cursor mcursor = null;
			mcursor = sdb.rawQuery("select * from "+SWITCH_BOARD_TABLE+" where "+SWB_DeviceId+"='"+ dev_address + "';", null);

			if (mcursor!=null&& mcursor.getCount()>0) 	{
				mcursor.moveToFirst();		//Placing cursor to first position

				do {
					SWBdevID = mcursor.getString(mcursor.getColumnIndex("f"));

					if (SWBdevID.equals(dev_address)) {
						StaticVariables.printLog("SWB", "data exists");
						isDevIdExists = true;

						StaticVariables.printLog("DEV NUMBER","DEVICE NUMBER : "+devno);
						break;
					}
				} while (mcursor.moveToNext());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		// Checking if Device ID existing in master table 
		try {
			Cursor cursor = null;
			boolean DevExists = false;
			cursor = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_DeviceId+"='"
					+ dev_address + "';", null);
			if (cursor!=null&& cursor.getCount()>0) 	{
				cursor.moveToFirst();		//Placing Cursor to first Position

				do {
					devID = cursor.getString(cursor.getColumnIndex(MASTER_DeviceId));

					if (devID.equals(dev_address)) {
						StaticVariables.printLog("MASTER", "device exists");
						DevExists = true;

						StaticVariables.printLog("DEV NUMBER","DEVICE NUMBER : "+devno);
						break;
					}
				} while (cursor.moveToNext());
			}

			//Deleting device id from master table if that exists 
			if (DevExists) 	{				
				sdb.delete(MASTER_TABLE, MASTER_DeviceId+"='" + dev_address + "';", null);
				DevExists = false;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		//Declaring switch status array of size 48
		int SwitchSts[] = new int[48];
		//initialize array elements with default values
		for(int i=0;i<SwitchSts.length;i++){
			SwitchSts[i]=0;
		}

		//declaring and initialize array element with default values
		int FanSts[] = { 0, 0, 0, 0, 0 };

		//changing switch status array elements according user requirements i.e switches counts
		for (int i = 0; i < SwitchCount; i++) {
			SwitchSts[i] = 1;
		}

		//changing fan status array elements according user reuirements i.e fan switch count
		for (int i = 0; i < FanCount; i++) {
			FanSts[i] = 1;
		}

		//if device id already existing in Switch board table then updating 
		//existing record else inserting new record
		if (isDevIdExists) 
		{
			try 
			{
				//update existing record in switch board table
				ContentValues cv = new ContentValues();

				cv.put(SWB_DeviceType, devtype);
				cv.put(SWB_DeviceName, devname);
				cv.put(SWB_DeviceNo, devno);
				cv.put(SWB_RoomName, roomname);
				cv.put(SWB_RoomNo, roomno);
				cv.put(SWB_NoofSwitches, SwitchCount);
				cv.put(SWB_NoofFans, FanCount);
				cv.put(SWB_AllOn, 1);
				cv.put(SWB_AllOff,1);
				cv.put(SWB_TYPE, device_type_name);
				cv.put(SWB_GroupId, groupId);

				cv.put(SWB_Image_Name, iconnam);
				cv.put(SWB_Usr_dev_Name, usrdevname);

				cv.put("zb", SwitchSts[0]);
				cv.put("zc", SwitchSts[1]);
				cv.put("zd", SwitchSts[2]);
				cv.put("ze", SwitchSts[3]);
				cv.put("zf", SwitchSts[4]);
				cv.put("zg", SwitchSts[5]);
				cv.put("zh", SwitchSts[6]);
				cv.put("zi", SwitchSts[7]);
				cv.put("zj", SwitchSts[8]);
				cv.put("zk", SwitchSts[9]);
				cv.put("zl", SwitchSts[10]);
				cv.put("zm", SwitchSts[11]);
				cv.put("zn", SwitchSts[12]);
				cv.put("zo", SwitchSts[13]);
				cv.put("zp", SwitchSts[14]);
				cv.put("zq", SwitchSts[15]);
				cv.put("zr", SwitchSts[16]);
				cv.put("zs", SwitchSts[17]);
				cv.put("zt", SwitchSts[18]);
				cv.put("zu", SwitchSts[19]);
				cv.put("zv", SwitchSts[20]);
				cv.put("zw", SwitchSts[21]);
				cv.put("zx", SwitchSts[22]);
				cv.put("zy", SwitchSts[23]);
				cv.put("zz", SwitchSts[24]);
				cv.put("ya", SwitchSts[25]);
				cv.put("yb", SwitchSts[26]);
				cv.put("yc", SwitchSts[27]);
				cv.put("yd", SwitchSts[28]);
				cv.put("ye", SwitchSts[29]);
				cv.put("yf", SwitchSts[30]);
				cv.put("yg", SwitchSts[31]);
				cv.put("yh", SwitchSts[32]);
				cv.put("yi", SwitchSts[33]);
				cv.put("yj", SwitchSts[34]);
				cv.put("yk", SwitchSts[35]);
				cv.put("yl", SwitchSts[36]);
				cv.put("ym", SwitchSts[37]);
				cv.put("yn", SwitchSts[38]);
				cv.put("yo", SwitchSts[39]);
				cv.put("yp", SwitchSts[40]);
				cv.put("yq", SwitchSts[41]);
				cv.put("yr", SwitchSts[42]);
				cv.put("ys", SwitchSts[43]);
				cv.put("yt", SwitchSts[44]);
				cv.put("yu", SwitchSts[45]);
				cv.put("yv", SwitchSts[46]);
				cv.put("yw", SwitchSts[47]);

				cv.put("fa1", FanSts[0]);
				cv.put("fb2", FanSts[1]);
				cv.put("fc3", FanSts[2]);
				cv.put("fd4", FanSts[3]);
				cv.put("fe5", FanSts[4]);

				sdb.update(SWITCH_BOARD_TABLE, cv, SWB_DeviceId+"='" + dev_address + "';", null);
				StaticVariables.printLog("SWB", "SWITCH data updated ");
				return true;
			} 
			catch (Exception e)	{
				e.printStackTrace();
				return false;
			}

		} 
		else 
		{
			try 
			{
				//Inserting new record in switch board table
				ContentValues cv = new ContentValues();
				cv.put(SWB_DeviceId, dev_address);
				cv.put(SWB_DeviceType, devtype);
				cv.put(SWB_DeviceName, devname);
				cv.put(SWB_DeviceNo, devno);
				cv.put(SWB_RoomName, roomname);
				cv.put(SWB_RoomNo, roomno);
				cv.put(SWB_NoofSwitches, SwitchCount);
				cv.put(SWB_NoofFans, FanCount);
				cv.put(SWB_AllOn,1);
				cv.put(SWB_AllOff,1);
				cv.put(SWB_TYPE, device_type_name);
				cv.put(SWB_GroupId, groupId);

				cv.put(SWB_Image_Name, iconnam);
				cv.put(SWB_Usr_dev_Name, usrdevname);

				cv.put("cg", "imagedata");

				cv.put("zb", SwitchSts[0]);
				cv.put("zc", SwitchSts[1]);
				cv.put("zd", SwitchSts[2]);
				cv.put("ze", SwitchSts[3]);
				cv.put("zf", SwitchSts[4]);
				cv.put("zg", SwitchSts[5]);
				cv.put("zh", SwitchSts[6]);
				cv.put("zi", SwitchSts[7]);
				cv.put("zj", SwitchSts[8]);
				cv.put("zk", SwitchSts[9]);
				cv.put("zl", SwitchSts[10]);
				cv.put("zm", SwitchSts[11]);
				cv.put("zn", SwitchSts[12]);
				cv.put("zo", SwitchSts[13]);
				cv.put("zp", SwitchSts[14]);
				cv.put("zq", SwitchSts[15]);
				cv.put("zr", SwitchSts[16]);
				cv.put("zs", SwitchSts[17]);
				cv.put("zt", SwitchSts[18]);
				cv.put("zu", SwitchSts[19]);
				cv.put("zv", SwitchSts[20]);
				cv.put("zw", SwitchSts[21]);
				cv.put("zx", SwitchSts[22]);
				cv.put("zy", SwitchSts[23]);
				cv.put("zz", SwitchSts[24]);
				cv.put("ya", SwitchSts[25]);
				cv.put("yb", SwitchSts[26]);
				cv.put("yc", SwitchSts[27]);
				cv.put("yd", SwitchSts[28]);
				cv.put("ye", SwitchSts[29]);
				cv.put("yf", SwitchSts[30]);
				cv.put("yg", SwitchSts[31]);
				cv.put("yh", SwitchSts[32]);
				cv.put("yi", SwitchSts[33]);
				cv.put("yj", SwitchSts[34]);
				cv.put("yk", SwitchSts[35]);
				cv.put("yl", SwitchSts[36]);
				cv.put("ym", SwitchSts[37]);
				cv.put("yn", SwitchSts[38]);
				cv.put("yo", SwitchSts[39]);
				cv.put("yp", SwitchSts[40]);
				cv.put("yq", SwitchSts[41]);
				cv.put("yr", SwitchSts[42]);
				cv.put("ys", SwitchSts[43]);
				cv.put("yt", SwitchSts[44]);
				cv.put("yu", SwitchSts[45]);
				cv.put("yv", SwitchSts[46]);
				cv.put("yw", SwitchSts[47]);

				cv.put("fa1", FanSts[0]);
				cv.put("fb2", FanSts[1]);
				cv.put("fc3", FanSts[2]);
				cv.put("fd4", FanSts[3]);
				cv.put("fe5", FanSts[4]);

				cv.put("fs1", 0);
				cv.put("fs2", 0);
				cv.put("fs3", 0);
				cv.put("fs4", 0);
				cv.put("fs5", 0);

				cv.put("asn", 0);
				cv.put("asf", 0);
				cv.put("afn", 0);
				cv.put("aff", 0);

				for (int i = 1; i < 49; i++) {
					cv.put("bn" + i, "Bulb" + i);
				}

				for (int i = 1; i < 49; i++) {
					cv.put("bi" + i, "Bicon" + i);
				}


				cv.put("db", "Default");
				cv.put("dc", "Default");
				cv.put("dd", "Default");
				cv.put("de", "Default");


				//cv.put("eb","Default");
				//cv.put("ec","Default");
				cv.put("ed","Default");
				cv.put("ee","Default");
				cv.put("ef","Default");
				cv.put("eg","Default");
				cv.put("eh","Default");
				cv.put("ei","Default");
				cv.put("ej","Default");

				//inserting data into table
				sdb.insert(SWITCH_BOARD_TABLE, null, cv);
				StaticVariables.printLog("SWB", "SWITCH  data inserted");
				return true;
			} 
			catch (Exception e)
			{
				e.printStackTrace();
				return false;
			}
		}

	}


	//Giving Count of calling bells already configured
	public long CountCallingBells() {
		long Count = 0;
		try {
			String query = "select COUNT("+MASTER_DeviceId+") from "+MASTER_TABLE+" where "+MASTER_DeviceName+"='"+CLB_TYPE+"';";
			SQLiteStatement stmt = sdb.compileStatement(query);
			Count = stmt.simpleQueryForLong();		//getting count from executed query

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Count;
	}





	//Giving Array of Device IDs of all calling bells
	public String[] callingBells_DeviceID(int CLB_Counts) {
		String DevID[] = new String[CLB_Counts];
		String ID;
		try {
			int i = 0;
			Cursor mcursor = null;
			mcursor = sdb.rawQuery("select "+MASTER_DeviceId+" from "+MASTER_TABLE+" where "+MASTER_DeviceName+"='"+CLB_TYPE+"';", null);
			if (mcursor!=null&& mcursor.getCount()>0) {		//checking if cusrsor have some data or not
				mcursor.moveToFirst();			//Placing Cursor to first position

				do {
					ID = mcursor.getString(mcursor.getColumnIndex(MASTER_DeviceId));	//Getting ID from cursor
					DevID[i] = ID;				// Adding device id in array
					StaticVariables.printLog("CLB", "CLB devid is : " + DevID[i]);
					i++;
				} while (mcursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return DevID;
	}

	//Giving Array of Device IDs of all calling bells with room and device number
	public String[] callingBells_Info() {
		String DevID[] =null;

		String roomName=null;
		int DevNo=0;
		try {
			int i = 0;
			Cursor mcursor = null;
			mcursor = sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceId,MASTER_DeviceNo,MASTER_RoomName}, MASTER_DeviceName+"='"+CLB_TYPE+"'",
					null, null, null, null, null);

			if (mcursor!=null&& mcursor.getCount()>0) {		//checking if cusrsor have some data or not
				mcursor.moveToFirst();			//Placing Cursor to first position

				DevID =new String[mcursor.getCount()];

				do {
					//Getting Data from cursor
					roomName= mcursor.getString(mcursor.getColumnIndex(MASTER_RoomName));
					DevNo= mcursor.getInt(mcursor.getColumnIndex(MASTER_DeviceNo));

					// Adding device id in array
					DevID[i] = roomName+" : "+"Bell-"+DevNo;				
					StaticVariables.printLog("CLB", "CLB devid is : " + DevID[i]);
					i++;
				} while (mcursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return DevID;
	}

	//Giving Count of calling bells already configured
	public long CountCurtain_Normal(String roomn) {
		long Count = 0;
		try {
			String query = "select COUNT("+MASTER_DeviceId+") from "+MASTER_TABLE+" where "+MASTER_DeviceName+"='"+CUR_TYPE+"' And "+MASTER_RoomNo+"='"+roomn+"';";
			SQLiteStatement stmt = sdb.compileStatement(query);
			Count = stmt.simpleQueryForLong();		//getting count from executed query

		} catch (Exception e) {
			e.printStackTrace();
		}
		return Count;
	}

	//Giving Array of Device IDs of all calling bells
	public String[] Curtain_Normal_DeviceName(int Cur_Counts,String roomn) {
		String Dev_names[] = new String[Cur_Counts];
		String ID;
		try {
			int i = 0;
			Cursor mcursor = null;
			mcursor = sdb.rawQuery("select "+MASTER_Usr_dev_Name+" from "+MASTER_TABLE+" where "+MASTER_DeviceName+"='"+CUR_TYPE+"' And "+MASTER_RoomNo+"='"+roomn+"';", null);
			if (mcursor!=null&& mcursor.getCount()>0) {		//checking if cusrsor have some data or not
				mcursor.moveToFirst();			//Placing Cursor to first position

				do {
					ID = mcursor.getString(mcursor.getColumnIndex(MASTER_Usr_dev_Name));	//Getting ID from cursor
					Dev_names[i] = ID;				// Adding device id in array
					StaticVariables.printLog("CUR", "Cur devnam is : " + Dev_names[i]);
					i++;
				} while (mcursor.moveToNext());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return Dev_names;
	}


	public String Curtain_Free_Normal(String curtnorm_nam) {
		String curt_dev_no=null;
		Cursor mCursor = sdb.rawQuery(
				"SELECT "+MASTER_DeviceNo+" FROM "+MASTER_TABLE+" WHERE "+MASTER_Usr_dev_Name+ "='"+curtnorm_nam+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			curt_dev_no = mCursor.getString(mCursor.getColumnIndex(MASTER_DeviceNo));
		}
		return curt_dev_no;
	}


	public String Curtain_Free_Normal_groupid(String curtnorm_nam) {
		String curt_dev_no=null;
		Cursor mCursor = sdb.rawQuery(
				"SELECT "+MASTER_GroupId+" FROM "+MASTER_TABLE+" WHERE "+MASTER_Usr_dev_Name+ "='"+curtnorm_nam+"'", null);
		if (mCursor.getCount() == 0){

		}
		else{
			mCursor.moveToFirst();
			curt_dev_no = mCursor.getString(mCursor.getColumnIndex(MASTER_GroupId));
		}
		return curt_dev_no;
	}

	//checking if Device is already Configured or not
	public String isDeviceExists(String id) {
		String  RESULT="FALSE",devtype = null, devid1 = null, devid2 = null;
		Cursor mcursor1 = null, mcursor2 = null;
		int old_dev_no=0;

		//checking device in switch board table
		try {
			mcursor1 = sdb.rawQuery("select * from "+SWITCH_BOARD_TABLE+" where "+SWB_DeviceId+"='"+ id + "';", null);
			if (mcursor1!=null&& mcursor1.getCount()>0) {
				mcursor1.moveToFirst();				//placing cursor to first position
				devtype = mcursor1.getString(mcursor1.getColumnIndex(SWB_DeviceType));
				devid1 = mcursor1.getString(mcursor1.getColumnIndex(SWB_DeviceId));
				old_dev_no= mcursor1.getInt(mcursor1.getColumnIndex(SWB_DeviceNo));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//checking device in master table
		try {
			mcursor2 = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_DeviceId+"='" + id + "';", null);
			if (mcursor2!=null&& mcursor2.getCount()>0) {
				mcursor2.moveToFirst();				//placing cursor to first position
				devtype = mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceType));
				devid2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceId));
				old_dev_no= mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((devid1!=null && devid1.equals(id)) || (devid2!=null && devid2.equals(id))) {
			StaticVariables.printLog("TAG", "device exists");
			RESULT="TRUE"+devtype+old_dev_no;					//device existing and setting result TRUE & Device Name
		}else{
			StaticVariables.printLog("TAG", "device not exists");
			RESULT="FALSE";
		}


		return RESULT;
	}

	//Getting list of all room names
	public TreeSet<String> RoomNameList(){

		TreeSet<String>UniqueRoomNames=new TreeSet<String>();	//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();	//list for storing room names temporarly
		Cursor mcursor1 = null,mcursor2= null,mcursor3 = null;					//initialize cursor
		String RoomName=null;

		roomNames.clear();
		//Fetching room names from switch board table
		try{
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomName},null, null, null, null, null, null);
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
				StaticVariables.printLog("TAG","wireless database not exists!"); 	 
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//adding unique rooms from array list into tree list
		UniqueRoomNames.addAll(roomNames);

		for(String rm:UniqueRoomNames){
			StaticVariables.printLog("TAG","Room Name :"+rm);
		}

		StaticVariables.printLog("TAG","Room List Size is :"+UniqueRoomNames.size());

		return UniqueRoomNames;
	}

	//Getting list of all room names having configured devices except from cam i.e fetching rooms from swb and master  table
	public TreeSet<String> WirelessRoomNameList(){
		TreeSet<String>UniqueRoomNames=new TreeSet<String>();	//unique list for room names
		ArrayList<String> roomNames=new ArrayList<String>();	//list for storing room names temporarly
		Cursor mcursor1 = null,mcursor2= null;					//initialize cursor
		String RoomName=null;

		//Fetching room names from switch board table
		try{
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomName},null, null, null, null, null, null);
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
			if(mcursor2!=null&& mcursor2.getCount()>0){
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

		//adding unique rooms from array list into tree list
		UniqueRoomNames.addAll(roomNames);
		StaticVariables.printLog("TAG","Room List Size is :"+UniqueRoomNames.size());

		return UniqueRoomNames;		

	}

	//Fetch Data from Database
	public Cursor fetchData(String TableName){
		Cursor mcursor=null;
		try{
			mcursor=sdb.rawQuery("select * from "+TableName+";",null);
			if(mcursor!=null&& mcursor.getCount()>0){
				mcursor.moveToFirst();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return mcursor;
	}


	//fetch room number from database(Master Table or SwitchBoard Table)
	public int CurrentRoomNumber(String roomName){

		Cursor mcursor1 = null,mcursor2= null,mcursor3= null;					//initialize cursor
		int room_number=0;

		//Fetching room number from switch board table
		try{
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomNo},SWB_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1!=null && mcursor1.getCount()>0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_number=mcursor1.getInt(mcursor1.getColumnIndex(SWB_RoomNo));	//getting room name from cursor
				}while(mcursor1.moveToNext());

			}
			//closing cursor
			if(mcursor1!=null){
				mcursor1.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_RoomNo},MASTER_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor2!=null&& mcursor2.getCount()>0){
				mcursor2.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_number=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_RoomNo));
				}while(mcursor2.moveToNext());

			}

			//closing cursor
			if(mcursor2!=null){
				mcursor2.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//Fetching room number from Camera table
		try{
		/*	mcursor3=sdb.query(true, CAM_TABLE, new String[]{CAM_RoomNo},CAM_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()>0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_number=mcursor3.getInt(mcursor3.getColumnIndex(CAM_RoomNo));	//getting room name from cursor
				}while(mcursor3.moveToNext());

			}

			//closing cursor
			if(mcursor3!=null){
				mcursor3.close();
			}*/
		}catch(Exception e){
			e.printStackTrace();
		}

		//fetch room names from ir config. table
		try{
	/*		if(room_number==0){

				//opening database
				IRB_Adapter_ir irb_adapter=new IRB_Adapter_ir(mcontext);

				//opening database
				irb_adapter.open();

				//getting room number from ir database
				room_number=irb_adapter.irRoomNumber(roomName);

				//closing ir adapter
				if(irb_adapter!=null)
					irb_adapter.close();

			}*/
		}catch(Exception e){
			e.printStackTrace();
		}



		//fetch room names from wireless config. table
		try{
			if(room_number==0){


				WirelessConfigurationAdapter WhouseDB=new WirelessConfigurationAdapter(mcontext);

				boolean isDbExists=WhouseDB.checkdb();
				if(isDbExists){
					//opening database
					WhouseDB.open();

					//getting room number from wireless database
					room_number=WhouseDB.wirlessRoomNumber(roomName);

					//closing database
					WhouseDB.close();	
				}else{
					StaticVariables.printLog("TAG","wireless database not exists!");
				}

			} 
		}catch(Exception e){
			e.printStackTrace();
		}


		return room_number;
	}


	//*************************************************************************

	public String CurrentRoomImageName(String roomName){

		Cursor mcursor1 = null,mcursor2= null,mcursor3= null;					//initialize cursor
		String room_img_nam=null;

		//Fetching room number from switch board table
		try{
			mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_Image_Name},SWB_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1!=null && mcursor1.getCount()>0){
				mcursor1.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_img_nam=mcursor1.getString(mcursor1.getColumnIndex(SWB_Image_Name));	//getting room name from cursor
				}while(mcursor1.moveToNext());

			}
			//closing cursor
			if(mcursor1!=null){
				mcursor1.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_Image_Name},MASTER_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor2!=null&& mcursor2.getCount()>0){
				mcursor2.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_img_nam=mcursor2.getString(mcursor2.getColumnIndex(MASTER_Image_Name));
				}while(mcursor2.moveToNext());

			}

			//closing cursor
			if(mcursor2!=null){
				mcursor2.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//Fetching room number from Camera table
		try{
			mcursor3=sdb.query(true, CAM_TABLE, new String[]{CAM_Image_Name},CAM_RoomName+"='"+roomName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor3!=null&& mcursor3.getCount()>0){
				mcursor3.moveToFirst();	//placing cursor to first position
				do{
					//getting room number from cursor
					room_img_nam=mcursor3.getString(mcursor3.getColumnIndex(CAM_Image_Name));	//getting room name from cursor
				}while(mcursor3.moveToNext());

			}

			//closing cursor
			if(mcursor3!=null){
				mcursor3.close();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//fetch room names from ir config. table
		try{
			if(room_img_nam==null){

				//opening database
				IRB_Adapter_ir irb_adapter=new IRB_Adapter_ir(mcontext);

				//opening database
				irb_adapter.open();

				//getting room number from ir database
				room_img_nam=irb_adapter.irRoomIMage(roomName);

				//closing ir adapter
				if(irb_adapter!=null)
					irb_adapter.close();

			}
		}catch(Exception e){
			e.printStackTrace();
		}



		//fetch room names from wireless config. table
		try{
			if(room_img_nam==null){


				WirelessConfigurationAdapter WhouseDB=new WirelessConfigurationAdapter(mcontext);

				boolean isDbExists=WhouseDB.checkdb();
				if(isDbExists){
					//opening database
					WhouseDB.open();

					//getting room number from wireless database
					room_img_nam=WhouseDB.wirlessRoomImages(roomName);

					//closing database
					WhouseDB.close();
				}else{
					StaticVariables.printLog("TAG","wireless database not exists!");
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}


		return room_img_nam;
	}







	//*****************************************************************************


	//fetching max camera number from db
	public int maxCamNumber(){
		int camNo=0;
		try{
			SQLiteStatement stmt1 = sdb.compileStatement("select MAX("+CAM_Number+") from "+CAM_TABLE+";");
			camNo = (int) stmt1.simpleQueryForLong();

		}catch(Exception e){
			e.printStackTrace();
		}
		return camNo;
	}

	//Inserting data to camera table
	public boolean InsertCameraDetails(String room_name,int room_no,
			String cam_no,String cam_name,String cam_ip,String cam_port,String cam_url){
		boolean isInserted=false;

		try{
			ContentValues cv = new ContentValues();
			cv.put(CAM_RoomName, room_name);
			cv.put(CAM_RoomNo, room_no);
			cv.put(CAM_Number, cam_no);
			cv.put(CAM_NAME, cam_name);
			cv.put(CAM_IP, cam_ip);
			cv.put(CAM_PORT, cam_port);
			cv.put(CAM_URL, cam_url);

			cv.put("sc", 0);
			cv.put("da", "Default");
			cv.put("db", "Default");
			cv.put("dc", "Default");
			cv.put("dd", "Default");
			cv.put("de", "Default");
			cv.put("ea", "Default");
			cv.put("eb", "Default");
			cv.put("ec", "Default");
			cv.put("ed", "Default");
			cv.put("ee", "Default");
			cv.put("ef", "Default");
			cv.put("eg", "Default");
			cv.put("eh", "Default");
			cv.put("ei", "Default");
			cv.put("ej", "Default");

			sdb.insert(CAM_TABLE, null, cv);
			isInserted=true;
			StaticVariables.printLog("CAMERA TABLE", "CAM  data inserted");

		}catch(Exception e){
			e.printStackTrace();
		}

		return isInserted;
	}

	//Updating camera table
	public boolean UpdateCameraDetails(String room_name,int room_no,
			String CAM_NO,String cam_name,String cam_ip,String cam_port,String cam_url){
		boolean isUpdated=false;

		try{
			ContentValues cv = new ContentValues();
			cv.put(CAM_RoomName, room_name);
			cv.put(CAM_RoomNo, room_no);
			cv.put(CAM_NAME, cam_name);
			cv.put(CAM_IP, cam_ip);
			cv.put(CAM_PORT, cam_port);
			cv.put(CAM_URL, cam_url);

			sdb.update(CAM_TABLE,cv, CAM_Number+"='" + CAM_NO + "';", null);
			isUpdated=true;
			StaticVariables.printLog("CAMERA TABLE", "CAM  data updated");

		}catch(Exception e){
			e.printStackTrace();
		}

		return isUpdated;
	}

	//checking weather cam name exists or not in db
	public boolean isCamExists(String CamName){

		Cursor mcursor1 = null ;	//initialize cursor

		//Fetching room names from switch board table
		try{
			mcursor1=sdb.query(true, CAM_TABLE, new String[]{CAM_NAME},CAM_NAME+"='"+CamName+"'", null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor1!=null&& mcursor1.getCount()>0){
				return true; 
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}

	//delete camera
	public boolean DeleteCam(String CamName){
		try{
			return sdb.delete(CAM_TABLE, CAM_NAME+"='"+CamName+"'", null)>0;
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	//getting camera name  list
	public ArrayList<String> CamNamesList(){
		ArrayList<String> CamList=new ArrayList<String>();
		try{
			Cursor mcursor=null;
			String CamName=null;
			mcursor=sdb.query(true, CAM_TABLE, new String[]{CAM_Number,CAM_NAME},null, null, null, null, null, null);
			//checking whether cursor have some data or not
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();	//placing cursor to first position
				do{
					CamName=mcursor.getString(mcursor.getColumnIndex(CAM_NAME));	//getting room name from cursor
					CamList.add(CamName);	// adding room names one by one in array list
				}while(mcursor.moveToNext());

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return CamList;
	}

	/* method to get list of master devices
	 * corresponding to selected device type 
	 */
	public ArrayList<String> MasterDevNumberList(String slaveMasterType,int roomno){
		ArrayList<String> masterDevNumbers=new ArrayList<String>();
		String master=null;
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceNo},
					MASTER_DeviceType+"='"+slaveMasterType+"' AND "+MASTER_RoomNo+"="+roomno
					, null, null, null,null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					master=mcursor.getString(mcursor.getColumnIndex(MASTER_DeviceNo));
					masterDevNumbers.add(master);
				}while(mcursor.moveToNext());
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return masterDevNumbers;
	}


	//method to delete device from house
	public boolean deleteDevice(int devNo){
		try{
			boolean delete1=false,delete2=false;

			//deleting device if existing in switch board table
			try{
				delete1=sdb.delete(SWITCH_BOARD_TABLE, SWB_DeviceNo+"="+devNo, null)>0;
			}catch(Exception e){
				e.printStackTrace();
			}

			//deleting device if existing in Master Table
			try{

				//checking if device is of wpc(water pump controller) type
				Cursor mcursor=null;
				try{

					mcursor=sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceNo,MASTER_DeviceId},MASTER_DeviceNo+"="+devNo+";", null, null, null, null, null);
					//checking whether cursor have some data or not
					if(mcursor!=null && mcursor.getCount()>0){
						mcursor.moveToFirst();	//placing cursor to first position

						String wpcDevID=mcursor.getString(mcursor.getColumnIndex(MASTER_DeviceId));

						//delete device from water pump table
						boolean delete3=sdb.delete(WATERPUMP_TABLE_NAME, WPC_DEV_ID+"='"+wpcDevID+"'", null)>0;

						if(delete3){
							StaticVariables.printLog("deleted", "water pump device deleted");
						}

					}
				}catch(Exception e){
					e.printStackTrace();
				}finally{
					if(mcursor!=null){
						mcursor.close();
					}
				}

				//deleting from master table
				delete2=sdb.delete(MASTER_TABLE, MASTER_DeviceNo+"="+devNo, null)>0;


			}catch(Exception e){
				e.printStackTrace();
			}

			if(delete1 || delete2){
				return true;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}

	//method to get devices present in given room
	public HashMap<String, String> getRoomDevices(int roomNo){
		HashMap<String,String> map=new HashMap<String, String>();
		Cursor mcursor=null,mcursor2=null;
		try{

			//fetching data from switchboard table
			mcursor=sdb.query(true,SWITCH_BOARD_TABLE , 
					new String[]{SWB_DeviceNo,SWB_DeviceName}, SWB_RoomNo+"='"+roomNo+"'",
					null, null, null, SWB_DeviceNo, null);

			//checking if cursor is null or not
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					int dno=mcursor.getInt(mcursor.getColumnIndex(SWB_DeviceNo));
					String devName=mcursor.getString(mcursor.getColumnIndex(SWB_DeviceName));
					//putting data in map
					map.put(""+dno, devName);
				}while(mcursor.moveToNext());

			}


			//fetching data from master table
			mcursor2=sdb.query(true,MASTER_TABLE , 
					new String[]{MASTER_DeviceNo,MASTER_DeviceName}, MASTER_RoomNo+"='"+roomNo+"'",
					null, null, null, MASTER_DeviceNo, null);

			//checking if cursor is null or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{

					int dno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					String devName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceName));

					//checking if device is not of pir sensor type
					if(!devName.equals("WPS1")){

						//putting data in map
						map.put(""+dno, devName);
					}

				}while(mcursor2.moveToNext()); 
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//closing cursor if not null
		if(mcursor!=null){
			mcursor.close();
		}
		if(mcursor2!=null){
			mcursor2.close();
		}

		StaticVariables.printLog("TAG","map size :"+map.size());

		return map; 

	}
	//method to get devices present in given room
	public HashMap<String, String> getRoomDevices_usrdevnames(int roomNo){
		HashMap<String,String> map=new HashMap<String, String>();
		Cursor mcursor=null,mcursor2=null;
		try{

			//fetching data from switchboard table
			mcursor=sdb.query(true,SWITCH_BOARD_TABLE ,
					new String[]{SWB_DeviceNo,SWB_Usr_dev_Name}, SWB_RoomNo+"='"+roomNo+"'",
					null, null, null, SWB_DeviceNo, null);

			//checking if cursor is null or not
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					int dno=mcursor.getInt(mcursor.getColumnIndex(SWB_DeviceNo));
					String devName=mcursor.getString(mcursor.getColumnIndex(SWB_Usr_dev_Name));
					//putting data in map
					map.put(""+dno, devName);
					StaticVariables.printLog("TAG","dno devName :"+dno +" "+devName);

				}while(mcursor.moveToNext());

			}


			//fetching data from master table
			mcursor2=sdb.query(true,MASTER_TABLE ,
					new String[]{MASTER_DeviceNo,MASTER_Usr_dev_Name}, MASTER_RoomNo+"='"+roomNo+"'",
					null, null, null, MASTER_DeviceNo, null);

			//checking if cursor is null or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{

					int dno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					String devName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_Usr_dev_Name));

					//checking if device is not of pir sensor type
					if(!devName.equals("WPS1")){

						//putting data in map
						map.put(""+dno, devName);
						StaticVariables.printLog("TAG","dno devName :"+dno +" "+devName);

					}

				}while(mcursor2.moveToNext());
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//closing cursor if not null
		if(mcursor!=null){
			mcursor.close();
		}
		if(mcursor2!=null){
			mcursor2.close();
		}

		StaticVariables.printLog("TAG","map size :"+map.size());

		return map;

	}
	//method to get devices present in given room
	public HashMap<String, String> getMoodDevices(int roomNo,String wirelessDevName){
		HashMap<String,String> map=new HashMap<String, String>();
		Cursor mcursor=null,mcursor2=null;
		try{

			//fetching data from switchboard table
			mcursor=sdb.query(true,SWITCH_BOARD_TABLE , 
					new String[]{SWB_DeviceNo,SWB_DeviceName,SWB_TYPE}, SWB_RoomNo+"="+roomNo,
					null, null, null, SWB_DeviceNo , null);

			//checking if cursor is null or not
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					int dno=mcursor.getInt(mcursor.getColumnIndex(SWB_DeviceNo));
					String devName=mcursor.getString(mcursor.getColumnIndex(SWB_DeviceName));
					StaticVariables.printLog("TAG","dev names :"+devName);
					//putting data in map
					map.put(""+dno, devName);
				}while(mcursor.moveToNext());

			}


			//fetching data from master table
			mcursor2=sdb.query(true,MASTER_TABLE , 
					new String[]{MASTER_DeviceNo,MASTER_DeviceName,MASTER_TYPE}, MASTER_RoomNo+"="+roomNo,
					null, null, null, MASTER_DeviceNo , null);

			//checking if cursor is null or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{

					int dno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					String devName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceName));
					String type=mcursor2.getString(mcursor2.getColumnIndex(MASTER_TYPE));
				
					if(wirelessDevName.equals(PIR_DevName)){
						if(!type.equals("CSW") && !type.equals("WPC") && !type.equals("RPR") && !type.equals("PIR")&&!type.equals("FMD")&&!type.equals("AQU")&&!type.equals("SDG")&&!type.equals("ALXA")){
							map.put(""+dno, devName);
						}
					}else{
						if(!type.equals("CSW") && !type.equals("WPC") && !type.equals("RPR")&& !type.equals("FMD")&&!type.equals("AQU")&&!type.equals("SDG")&&!type.equals("ALXA")){
							map.put(""+dno, devName);
						}
					}
					

				}while(mcursor2.moveToNext()); 
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//closing cursor if not null
		if(mcursor!=null){
			mcursor.close();
		}
		if(mcursor2!=null){
			mcursor2.close();
		}

		//sorting the map based on key
		return map;//getSortedMap(map);

	}



	//method to get devices user given names present in given room
	public HashMap<String, String> getMoodDevices_userdevicenames(int roomNo,String wirelessDevName){
		HashMap<String,String> map=new HashMap<String, String>();
		Cursor mcursor=null,mcursor2=null;
		try{

			//fetching data from switchboard table
			mcursor=sdb.query(true,SWITCH_BOARD_TABLE ,
					new String[]{SWB_DeviceNo,SWB_Usr_dev_Name,SWB_TYPE}, SWB_RoomNo+"="+roomNo,
					null, null, null, SWB_DeviceNo , null);

			//checking if cursor is null or not
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					int dno=mcursor.getInt(mcursor.getColumnIndex(SWB_DeviceNo));
					String devName=mcursor.getString(mcursor.getColumnIndex(SWB_Usr_dev_Name));
					StaticVariables.printLog("TAG","dev names :"+devName);
					//putting data in map
					map.put(""+dno, devName);
				}while(mcursor.moveToNext());

			}


			//fetching data from master table
			mcursor2=sdb.query(true,MASTER_TABLE ,
					new String[]{MASTER_DeviceNo,MASTER_Usr_dev_Name,MASTER_TYPE}, MASTER_RoomNo+"="+roomNo,
					null, null, null, MASTER_DeviceNo , null);

			//checking if cursor is null or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{

					int dno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					String devName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_Usr_dev_Name));
					String type=mcursor2.getString(mcursor2.getColumnIndex(MASTER_TYPE));
					StaticVariables.printLog("TAG","dev names :"+devName);


					if(wirelessDevName.equals(PIR_DevName)||wirelessDevName.equals(PIR_DevName2)){
						if(!type.equals("CSW") && !type.equals("WPC") && !type.equals("RPR") && !type.equals("PIR")&&!type.equals("FMD")&&!type.equals("AQU")&&!type.equals("SDG")&&!type.equals("ALXA")){
							map.put(""+dno, devName);
						}
					}else{
						if(!type.equals("CSW") && !type.equals("WPC") && !type.equals("RPR")&& !type.equals("FMD")&&!type.equals("AQU")&&!type.equals("SDG")&&!type.equals("ALXA")){
							map.put(""+dno, devName);
						}
					}


				}while(mcursor2.moveToNext());
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//closing cursor if not null
		if(mcursor!=null){
			mcursor.close();
		}
		if(mcursor2!=null){
			mcursor2.close();
		}

		//sorting the map based on key
		return map;//getSortedMap(map);

	}

	//method to get specific  devices present in given room
	public HashMap<String, String> getspecificMoodDevices(int roomNo,String Current_WRLS_DevName){
		HashMap<String,String> map=new HashMap<String, String>();
		Cursor mcursor2=null;
		try{

			String SpecificDevtype="";

			switch (wirelessDevTypes.valueOf(Current_WRLS_DevName)) {

			case WD01:{ 
				SpecificDevtype=MASTER_DeviceName+"='"+DMR_DevType+"'";
				break;
			} 
			case WR01:{
				SpecificDevtype=MASTER_DeviceName+"='"+RGB_DevType+"'";
				break;
			} 
			case WC01:{ 
				SpecificDevtype="("+MASTER_DeviceName+"='"+CUR_LRS_DevType+"' OR "+MASTER_DeviceName+"='"+CUR_RS_DevType+"')";
				break;
			}
			case WC02:{ 
				SpecificDevtype="("+MASTER_DeviceName+"='"+CUR_LRD_DevType+"' OR "+MASTER_DeviceName+"='"+CUR_RD_DevType+"')";
				break;
			}
			case WF01:{ 
				SpecificDevtype=MASTER_DeviceName+"='"+SINGLE_FAN_DevType+"'";
				break;
			}
			case WF02:{ 
				SpecificDevtype=MASTER_DeviceName+"='"+DUAL_FAN_DevType+"'";
				break;
			} 
			default:
				break;
			} 


			//fetching data from master table
			mcursor2=sdb.query(true,MASTER_TABLE , 
					new String[]{MASTER_DeviceNo,MASTER_DeviceName,MASTER_TYPE}, MASTER_RoomNo+"="+roomNo+"" +" AND "+ SpecificDevtype,
					null, null, null, MASTER_DeviceNo , null);

			//checking if cursor is null or not
			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{

					int dno=mcursor2.getInt(mcursor2.getColumnIndex(MASTER_DeviceNo));
					String devName=mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceName));

					map.put(""+dno, devName);

				}while(mcursor2.moveToNext()); 
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		//closing cursor if not null
		if(mcursor2!=null){
			mcursor2.close();
		}

		//sorting the map based on key
		return map; 

	}

	// device type collection
	enum wirelessDevTypes {
		WS01,WS02,WS03,WS04,WS05,WS06,WC01,WC02,WF01,WF02,
		WD01,WDD1,WR01,WZ01,WSS1,WBS1,WGS1,WPS1,WNFC 
	}

	//getting list of group-id's of a room related to selected device  
	public List<String> groupIdList(String RoomName,String DevType){
		StaticVariabes_div.log("DevType"+DevType,"hcadap");
		List<String> groupIdList=new ArrayList<String>();
		String gid=null;
		try{
			//fetching group id from switchboard table
			Cursor mcursor=sdb.query(true, SWITCH_BOARD_TABLE,
					new String[]{SWB_GroupId}, SWB_RoomName+"='"+RoomName+"' "
							+ "AND "+SWB_DeviceType+"='"+DevType+"'",
							null, null, null, null, null);

			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					gid=mcursor.getString(mcursor.getColumnIndex(SWB_GroupId));
					if(!gid.equals("000"))
						groupIdList.add(gid);
				}while(mcursor.moveToNext());
			}

			//closing cursor
			if(mcursor!=null){
				mcursor.close();
			}

			Cursor mcursor2 = null;
			if(DevType.equals("718")||DevType.equals("720")){

				StaticVariabes_div.log("RoomName "+RoomName+" DevType"+DevType,"pir");

                String pirdevtyp="718",dpirdevtyp="720";

				 mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_RoomName + "='" + RoomName + "' "
								+ "AND " + MASTER_DeviceType + "='" + pirdevtyp + "' OR "+ MASTER_DeviceType +"='"+ dpirdevtyp +"'",
						null, null, null, null, null);

			}else if(DevType.equals("033")||DevType.equals("035")||DevType.equals("050")||DevType.equals("051")){


				StaticVariabes_div.log("DevType"+DevType,"curt");
				String clsdevtyp="033",crsdevtyp="035",clshdevtyp="051",clnrdevtyp="050";
				mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_RoomName + "='" + RoomName + "' "
								+ "AND " + MASTER_DeviceType + "='" + clsdevtyp + "' OR "+ MASTER_DeviceType +"='"+ crsdevtyp +"' OR "+ MASTER_DeviceType +"='"+ clnrdevtyp +"'" ,
						null, null, null, null, null);
			}else {
				//fetching group id from master table
				 mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_RoomName + "='" + RoomName + "' "
								+ "AND " + MASTER_DeviceType + "='" + DevType + "'",
						null, null, null, null, null);

			}

			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{
					gid=mcursor2.getString(mcursor2.getColumnIndex(MASTER_GroupId));
					StaticVariabes_div.log("DevType"+gid,"gid");
					if(!gid.equals("000"))
						groupIdList.add(gid);
				}while(mcursor2.moveToNext());
			}



			//closing cursor
			if(mcursor2!=null){
				mcursor2.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		} 

		return groupIdList;
	}



	public List<String> groupIdList_typename(String RoomName,String DevType_nam){
		StaticVariabes_div.log("DevType"+DevType_nam,"hcadap");
		List<String> groupIdList=new ArrayList<String>();
		String gid=null;
		try{
			//fetching group id from switchboard table
			Cursor mcursor=sdb.query(true, SWITCH_BOARD_TABLE,
					new String[]{SWB_GroupId}, SWB_RoomName+"='"+RoomName+"' "
							+ "AND "+SWB_TYPE+"='"+DevType_nam+"'",
					null, null, null, null, null);

			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				do{
					gid=mcursor.getString(mcursor.getColumnIndex(SWB_GroupId));
					if(!gid.equals("000"))
						groupIdList.add(gid);
				}while(mcursor.moveToNext());
			}

			//closing cursor
			if(mcursor!=null){
				mcursor.close();
			}

			Cursor mcursor2 = null;

				//fetching group id from master table
				mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_RoomName + "='" + RoomName + "' "
								+ "AND " + MASTER_TYPE + "='" + DevType_nam + "'",
						null, null, null, null, null);



			if(mcursor2!=null && mcursor2.getCount()>0){
				mcursor2.moveToFirst();
				do{
					gid=mcursor2.getString(mcursor2.getColumnIndex(MASTER_GroupId));
					StaticVariabes_div.log("DevType"+gid,"gid");
					if(!gid.equals("000"))
						groupIdList.add(gid);
				}while(mcursor2.moveToNext());
			}



			//closing cursor
			if(mcursor2!=null){
				mcursor2.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		return groupIdList;
	}

	//method to check whether group id assigned is unique to each devicetypes
	public boolean isGroupIdUnique(String devname,String groupId){
		try{
			StaticVariables.printLog("TAG","info  devname:"+devname+" gid :"+groupId);
			//fetching group id from switchboard table
			Cursor mcursor=sdb.query(true, SWITCH_BOARD_TABLE,
					new String[]{SWB_GroupId}, SWB_DeviceName+"!='"+devname+"' "
							+ "AND "+SWB_GroupId+"='"+groupId+"' AND "+SWB_GroupId+"!='000'",
							null, null, null, null, null);

			if(mcursor!=null && mcursor.getCount()>0){ 
				return false;
			}
			Cursor mcursor2=null;
			if(devname.equals("WPS1")||devname.equals("WPD1")) {

				String dpirtype="WPD1",pirtype="WPS1";

				//fetching group id from master table
				 mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_DeviceName + "!='" + dpirtype + "' AND "+MASTER_DeviceName + "!='" + pirtype + "'"
								+ "AND " + MASTER_GroupId + "='" + groupId + "' AND " + MASTER_GroupId + "!='000'",
						null, null, null, null, null);

			}else if(devname.equals("CLS1")||devname.equals("CRS1")||devname.equals("CLSH")||devname.equals("CLNR")){
				String clsdevtyp="CLS1",crsdevtyp="CRS1",clshdevtyp="CLSH",clnrdevtyp="CLNR";

				  mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_DeviceName + "!='" + clsdevtyp + "' AND "+MASTER_DeviceName + "!='" + crsdevtyp + "' AND "+MASTER_DeviceName + "!='" + clnrdevtyp + "'"
								+ "AND " + MASTER_GroupId + "='" + groupId + "' AND " + MASTER_GroupId + "!='000'",
						null, null, null, null, null);

			}else{

				 mcursor2 = sdb.query(true, MASTER_TABLE,
						new String[]{MASTER_GroupId}, MASTER_DeviceName + "!='" + devname + "' "
								+ "AND " + MASTER_GroupId + "='" + groupId + "' AND " + MASTER_GroupId + "!='000'",
						null, null, null, null, null);
			}

			if(mcursor2!=null && mcursor2.getCount()>0){
				return false;
			} 

		}catch(Exception e){
			e.printStackTrace();
		}

		return true;
	}

	//method to check whether group id assigned is unique to each devicetypes
	public boolean isGroupIdUnique_type(String devname,String groupId){
		try{
			StaticVariables.printLog("TAG","info  devname:"+devname+" gid :"+groupId);
			//fetching group id from switchboard table
			Cursor mcursor=sdb.query(true, SWITCH_BOARD_TABLE,
					new String[]{SWB_GroupId}, SWB_TYPE+"!='"+devname+"' "
							+ "AND "+SWB_GroupId+"='"+groupId+"' AND "+SWB_GroupId+"!='000'",
					null, null, null, null, null);

			if(mcursor!=null && mcursor.getCount()>0){
				return false;
			}

			//fetching group id from master table
			Cursor mcursor2=sdb.query(true, MASTER_TABLE,
					new String[]{MASTER_GroupId},MASTER_TYPE+"!='"+devname+"' "
							+ "AND "+MASTER_GroupId+"='"+groupId+"' AND "+MASTER_GroupId+"!='000'",
					null, null, null, null, null);


			if(mcursor2!=null && mcursor2.getCount()>0){
				return false;
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return true;
	}


	//method to get unique group id for water pump controller
	public String getUniqueGroupId(){

		Cursor mcursor1=null ,mcursor2=null  ;
		try{
			boolean isPresentInSwbTable=false ,isPresentInMasterTable=false;

			//iterating all group id to check unused group id
			for(int i=1;i<1000;i++)
			{
				isPresentInSwbTable=false ;
				isPresentInMasterTable=false;

				String gid=""+i;
				while(gid.length()<3){
					gid="0"+gid;
				}

				//checking availability of group id in switchboard table
				mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{},
						SWB_GroupId+"='"+gid+"'",null,null,null,null,null);
				if(mcursor1!=null && mcursor1.getCount()>0){
					isPresentInSwbTable=true;
				}

				//closing cursor
				if(mcursor1!=null){
					mcursor1.close();
				}


				//checking availability of group id in Master table
				mcursor2=sdb.query(true, MASTER_TABLE, new String[]{},
						MASTER_GroupId+"='"+gid+"'",null,null,null,null,null);
				if(mcursor2!=null && mcursor2.getCount()>0){
					isPresentInMasterTable=true;
				}

				//closing cursor
				if(mcursor2!=null){
					mcursor2.close();
				}


				//checking if group id is not present in above tables
				if(!isPresentInSwbTable && !isPresentInMasterTable){
					return gid;
				}

			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return "000";
	}


	//getting group id from db for WPC
	public String getWPCgroupId(){
		Cursor mcursor =null;
		try{

			mcursor=sdb.query(false, MASTER_TABLE, new String[]{MASTER_DeviceName,MASTER_GroupId}, 
					MASTER_DeviceName+"='"+MUD_DevType+"' OR "+ MASTER_DeviceName+"='"+MUC_DevType+"' OR "
							+MASTER_DeviceName+"='"+MUB_DevType+"' OR "+ MASTER_DeviceName+"='"+MBSO_DevType+"' OR "
							+ MASTER_DeviceName+"='"+BM_DevType+"' OR "+ MASTER_DeviceName+"='"+SM_DevType+"' OR "
							+ MASTER_DeviceName+"='"+SS_DevType+"' OR "+ MASTER_DeviceName+"='"+OTS_DevType+"'",
							null, null, null, null, null) ;

			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
				String gid=mcursor.getString(mcursor.getColumnIndex(MASTER_GroupId));

				//closing cursor
				if(mcursor!=null){
					mcursor.close();
				}

				return gid;
			}else{

				//closing cursor
				if(mcursor!=null){
					mcursor.close();
				}

				return getUniqueGroupId();
			}

		}catch(Exception e){
			e.printStackTrace();
		} 

		return "000";
	}


	//getting wired device details based on device number supplied
	//fetching record cursor corresponding to  device type
	public Cursor wiredDeviceRecords(int devNo){
		Cursor mcursor=null,mcursor1=null,mcursor2=null;
		try{
			//Fetching room names from switch board table
			try{
				mcursor1=sdb.query(true, SWITCH_BOARD_TABLE, new String[]{SWB_RoomName,SWB_RoomNo,SWB_GroupId,
						SWB_DeviceName,SWB_DeviceNo,SWB_DeviceType,SWB_DeviceId,SWB_Usr_dev_Name},SWB_DeviceNo+"="+devNo+";", null, null, null, null, null);
				//checking whether cursor have some data or not
				if(mcursor1!=null && mcursor1.getCount()>0){
					mcursor1.moveToFirst();	//placing cursor to first position
					return mcursor1;				
				}
			}catch(Exception e){
				e.printStackTrace();
			} 

			try{
				mcursor2=sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceNo,MASTER_RoomName,MASTER_RoomNo,
						MASTER_GroupId,MASTER_DeviceName,MASTER_DeviceType,MASTER_DeviceId,MASTER_Usr_dev_Name},MASTER_DeviceNo+"="+devNo+";", null, null, null, null, null);
				//checking whether cursor have some data or not
				if(mcursor2!=null && mcursor2.getCount()>0){
					mcursor2.moveToFirst();	//placing cursor to first position
					return mcursor2;	
				}
			}catch(Exception e){
				e.printStackTrace();
			} 

		}catch(Exception e){
			e.printStackTrace();
		}

		return mcursor;

	}	


	//*******************water pump table end***************//

	//method to insert data in table
	public boolean insertOrUpdateMotors(String DevName,String Devtype,String DevId){
		try{
			//checking if device id existing or not 
			Cursor mcursor1=null;

			mcursor1=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_NAME},
					WPC_DEV_NAME+"='"+DevName+"'",null,null,null,null,null);

			if(mcursor1!=null && mcursor1.getCount()>0){
				StaticVariables.printLog("TAG","in update method");
				//updating the data if id is there 
				//inserting data in table
				ContentValues cv=new ContentValues();

				cv.put(WPC_DEV_ID,DevId);
				cv.put(WPC_DEV_NAME,DevName); 	 
				cv.put(WPC_DEV_TYPE,Devtype);

				return sdb.update(WATERPUMP_TABLE_NAME, cv, WPC_DEV_NAME+"='"+DevName+"'",null)>0;
			}else{
				StaticVariables.printLog("TAG","in insert method");
				//inserting data in table
				ContentValues cv=new ContentValues();

				cv.put(WPC_DEV_ID,DevId);
				cv.put(WPC_DEV_NAME,DevName);
				cv.put(WPC_DEV_TYPE,Devtype);	

				cv.put("ea","Default");
				cv.put("eb","Default");
				cv.put("ec","Default");
				cv.put("ed","Default");
				cv.put("ee","Default");
				cv.put("ef","Default");
				cv.put("eg","Default");
				cv.put("eh","Default");
				cv.put("ei","Default");
				cv.put("ej","Default");

				return sdb.insert(WATERPUMP_TABLE_NAME, null, cv)>0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	//method to insert data in table
	public boolean insertOrUpdateMasterUnit(String DevName,String Devtype,String DevId){
		try{
			//checking if device id existing or not 
			Cursor mcursor1=null;

			mcursor1=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_NAME},
					WPC_DEV_NAME+"='"+MUD_DevType+"' OR "+WPC_DEV_NAME+"='"+MUC_DevType+"' OR "+
							WPC_DEV_NAME+"='"+MUB_DevType+"' OR "+
							WPC_DEV_NAME+"='"+MBSO_DevType+"'",null,null,null,null,null);

			if(mcursor1!=null && mcursor1.getCount()>0){
				StaticVariables.printLog("TAG","in update method");
				//updating the data if id is there 
				//inserting data in table
				ContentValues cv=new ContentValues();

				cv.put(WPC_DEV_ID,DevId);
				cv.put(WPC_DEV_NAME,DevName); 	 
				cv.put(WPC_DEV_TYPE,Devtype);

				return sdb.update(WATERPUMP_TABLE_NAME, cv,
						WPC_DEV_NAME+"='"+MUD_DevType+"' OR "+WPC_DEV_NAME+"='"+MUC_DevType+"' OR "+
								WPC_DEV_NAME+"='"+MUB_DevType+"' OR "+
								WPC_DEV_NAME+"='"+MBSO_DevType+"'",null)>0;
			}else{
				StaticVariables.printLog("TAG","in insert method");
				//inserting data in table
				ContentValues cv=new ContentValues();

				cv.put(WPC_DEV_ID,DevId);
				cv.put(WPC_DEV_NAME,DevName);
				cv.put(WPC_DEV_TYPE,Devtype);

				cv.put("ea","Default");
				cv.put("eb","Default");
				cv.put("ec","Default");
				cv.put("ed","Default");
				cv.put("ee","Default");
				cv.put("ef","Default");
				cv.put("eg","Default");
				cv.put("eh","Default");
				cv.put("ei","Default");
				cv.put("ej","Default");
				return sdb.insert(WATERPUMP_TABLE_NAME, null, cv)>0;
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}


	//method to check if device type is configured or not
	public boolean isDeviceConfigured(String devName){
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_ID}, 
					WPC_DEV_NAME+"='"+devName+"'", null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0)
			{
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}

	//method to check if master device type is configured or not
	public boolean isMasterDeviceConfigured(){
		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_ID}, 
					WPC_DEV_NAME+"='"+MUD_DevType+"' OR "+WPC_DEV_NAME+"='"+
							MUC_DevType+"' OR "+WPC_DEV_NAME+"='"+MUB_DevType+"' OR "+WPC_DEV_NAME+"='"+MBSO_DevType+"'", 
							null, null, null, null, null);
			if(mcursor!=null && mcursor.getCount()>0)
			{
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}	

	//getting details from device table
	public Cursor getDeviceTableDetails(String devName){
		Cursor mcursor=null;
		try{
			mcursor=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_NAME,WPC_DEV_ID,WPC_DEV_TYPE}, WPC_DEV_NAME+"='"+devName+"'", null,null,null,null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mcursor;
	}

	//getting details from device table
	public Cursor getMasterUnitDetails(){
		Cursor mcursor=null;
		try{
			mcursor=sdb.query(true, WATERPUMP_TABLE_NAME, new String[]{WPC_DEV_NAME,WPC_DEV_ID,WPC_DEV_TYPE},
					WPC_DEV_NAME+"='"+MUD_DevType+"' OR "+WPC_DEV_NAME+"='"+MUC_DevType+"' OR "+
							WPC_DEV_NAME+"='"+MUB_DevType+"' OR "+
							WPC_DEV_NAME+"='"+MBSO_DevType+"'", null,null,null,null, null);
			if(mcursor!=null && mcursor.getCount()>0){
				mcursor.moveToFirst();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		return mcursor;
	}	


	//checking if device table have records or not
	public Cursor DeviceTableRecords(){
		return sdb.query(true, WATERPUMP_TABLE_NAME,new String[]{WPC_DEV_NAME,WPC_DEV_ID,WPC_DEV_TYPE}, null,null,null,null,null,null);
	}


	//check if master unit is configured or not 
	public boolean isMasterUnitConfigured(){
		Cursor mcursor=null;
		try{
			mcursor=sdb.query(true, MASTER_TABLE, new String[]{MASTER_DeviceType,MASTER_DeviceName,MASTER_DeviceId},
					MASTER_DeviceName+"='"+MUD_DevType+"' OR "+MASTER_DeviceName+"='"+MUC_DevType+"' OR "+
							MASTER_DeviceName+"='"+MUB_DevType+"' OR "+
							MASTER_DeviceName+"='"+MBSO_DevType+"'", null,null,null,null, null);
			if(mcursor!=null && mcursor.getCount()>0){ 
				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}finally{
			if(mcursor!=null){
				mcursor.close();
			}
		}
		return false;
	}	



	//*******************water pump table end***************//


	//checking database integrity
	public  boolean isDataBaseCorrect(){

		try{
			Cursor mcursor=null;
			mcursor=sdb.query(true, SERVER_DETAILS_TABLE, new String[]{SERVER_DETAILS_DEVID}, null, null
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


	// getting system time in 24 hr format
	static String getDateTime() {

		try {

			Calendar calendar = Calendar.getInstance();
			String dateTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar
					.getTime()); 

			return dateTime; 

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	//*******************PULL DB FILE ON SD********************

	//taking local backup of database on sd card
	public boolean BACKUPDB(String PackageName, String DataBaseName, String SDFolderName)
	{
		try {
			File sd = Environment.getExternalStorageDirectory();
			File data = Environment.getDataDirectory();

			String newFolder=SDFolderName;
			String extStorageDirectory=sd.toString();
			File myNewFolder = new File(extStorageDirectory + "//" + newFolder); 
			if(!myNewFolder.exists())
			{ 
				if(sd.canWrite())
					myNewFolder.mkdir();	
			}

			if (sd.canRead()) {
				String currentDBPath = "//data//"+ PackageName +"//databases//"+DataBaseName+".db";
				String backupDBPath = SDFolderName + "//" + DataBaseName + ".db";
				File currentDB = new File(data, currentDBPath);
				File backupDB = new File(sd, backupDBPath);

				FileChannel src = new FileInputStream(currentDB).getChannel();
				FileChannel dst = new FileOutputStream(backupDB).getChannel();
				dst.transferFrom(src, 0, src.size());
				src.close();
				dst.close();
				Toast.makeText(mcontext.getApplicationContext(), backupDB.toString(), Toast.LENGTH_LONG).show();
				return true;
			}
		} catch (Exception e) {
			Toast.makeText(mcontext.getApplicationContext(), e.toString(), Toast.LENGTH_LONG).show();
		}
		return false;
	}


	//checking if Device is already Configured or not
	public String isDeviceName_Exists(String dev_name) {
		String  RESULT="FALSE",devname1 = null, devno1 = null, devno2 = null,devname2 = null;
		Cursor mcursor1 = null, mcursor2 = null;
		int old_dev_no=0;

		//checking device in switch board table
		try {
			mcursor1 = sdb.rawQuery("select * from "+SWITCH_BOARD_TABLE+" where "+SWB_Usr_dev_Name+"='"+ dev_name + "';", null);
			if (mcursor1!=null&& mcursor1.getCount()>0) {
				mcursor1.moveToFirst();				//placing cursor to first position
				devname1 = mcursor1.getString(mcursor1.getColumnIndex(SWB_Usr_dev_Name));
				devno1 = mcursor1.getString(mcursor1.getColumnIndex(SWB_DeviceId));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//checking device in master table
		try {
			mcursor2 = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_Usr_dev_Name+"='" + dev_name + "';", null);
			if (mcursor2!=null&& mcursor2.getCount()>0) {
				mcursor2.moveToFirst();				//placing cursor to first position
				devname2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_Usr_dev_Name));
				devno2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceId));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((devname1!=null && devname1.equals(dev_name)) || (devname2!=null && devname2.equals(dev_name))) {
			StaticVariables.printLog("TAG", "device name exists");
			RESULT="TRUE";					//device existing and setting result TRUE & Device Name
		}else{
			StaticVariables.printLog("TAG", "device name not exists");
			RESULT="FALSE";
		}


		return RESULT;
	}




	//checking if Room is already Configured or not
	public String isRoomName_Exists(String dev_name) {
		String  RESULT="FALSE",devname1 = null, devno1 = null, devno2 = null,devname2 = null;
		Cursor mcursor1 = null, mcursor2 = null;
		int old_dev_no=0;

		//checking device in switch board table
		try {
			mcursor1 = sdb.rawQuery("select * from "+SWITCH_BOARD_TABLE+" where "+SWB_RoomName+"='"+ dev_name + "';", null);
			if (mcursor1!=null&& mcursor1.getCount()>0) {
				mcursor1.moveToFirst();				//placing cursor to first position
				devname1 = mcursor1.getString(mcursor1.getColumnIndex(SWB_RoomName));
				devno1 = mcursor1.getString(mcursor1.getColumnIndex(SWB_DeviceId));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		//checking device in master table
		try {
			mcursor2 = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_RoomName+"='" + dev_name + "';", null);
			if (mcursor2!=null&& mcursor2.getCount()>0) {
				mcursor2.moveToFirst();				//placing cursor to first position
				devname2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_RoomName));
				devno2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceId));

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if ((devname1!=null && devname1.equals(dev_name)) || (devname2!=null && devname2.equals(dev_name))) {
			StaticVariables.printLog("TAG", "room name exists");
			RESULT="TRUE";					//device existing and setting result TRUE & Device Name
		}else{
			StaticVariables.printLog("TAG", "room name not exists");
			RESULT="FALSE";
		}


		return RESULT;
	}


	//checking if Device is already Configured or not
	public String getalexa_id(String dev_name) {
		String  RESULT="FALSE", devno2 = null,devname2 = null;
		Cursor mcursor1 = null, mcursor2 = null;



		//checking device in master table
		try {
			mcursor2 = sdb.rawQuery("select * from "+MASTER_TABLE+" where "+MASTER_Usr_dev_Name+"='" + dev_name + "';", null);
			if (mcursor2!=null&& mcursor2.getCount()>0) {
				mcursor2.moveToFirst();				//placing cursor to first position
				devname2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_Usr_dev_Name));
				devno2 = mcursor2.getString(mcursor2.getColumnIndex(MASTER_DeviceId));

			}
		} catch (Exception e) {
			e.printStackTrace();
			RESULT="null";
			return RESULT;
		}

		if (devno2!=null) {
			StaticVariables.printLog("TAG", "device name exists");
			RESULT=devno2;					//device existing and setting result TRUE & Device Name
		}else{
			StaticVariables.printLog("TAG", "device name not exists");
			RESULT="null";
		}


		return RESULT;
	}

	// Updating Server Table with IP/PORT/SSID
	public boolean updateMasterTable(String roomno,String roomname,String usrdevnam ) {
		boolean updated = false;
		//usrdevnam="'"+usrdevnam+"'";

		try{
			ContentValues cv=new ContentValues();
			cv.put(MASTER_RoomName, roomname);
			cv.put(MASTER_RoomNo, roomno);

			//boolean bn=sdb.update(MASTER_TABLE, cv, MASTER_Usr_dev_Name+"="+usrdevnam, null)>0;
			//boolean test=   dbs.update(DATABASE_TABLE, args, KEY_d + "=" + devno+" AND "+KEY_a + "=" + roomnum, null)>0;
			updated=sdb.update(MASTER_TABLE, cv, "ec = ?", new String[]{usrdevnam})>0;

		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}

		return updated;
	}

	public boolean update_MstrTble_name_id(String usrdevname,String newusrdevname,String mstid,String type ) {
		boolean updated = false;
		//usrdevnam="'"+usrdevnam+"'";

		try{
			ContentValues cv=new ContentValues();

			if(type.equals("both")) {
				cv.put(MASTER_Usr_dev_Name, newusrdevname);
				cv.put(MASTER_DeviceId, mstid);
			}else if(type.equals("nam")) {
				cv.put(MASTER_Usr_dev_Name, newusrdevname);

			}else if(type.equals("id")) {
				cv.put(MASTER_DeviceId, mstid);
			}
			//boolean bn=sdb.update(MASTER_TABLE, cv, MASTER_Usr_dev_Name+"="+usrdevnam, null)>0;
			//boolean test=   dbs.update(DATABASE_TABLE, args, KEY_d + "=" + devno+" AND "+KEY_a + "=" + roomnum, null)>0;
			updated=sdb.update(MASTER_TABLE, cv, "ec = ?", new String[]{usrdevname})>0;

		}catch(Exception e){
			e.printStackTrace();
			updated=false;
		}

		return updated;
	}
}
