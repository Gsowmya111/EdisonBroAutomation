package edisonbro.com.edisonbroautomation.StaticClasses;

/**
 *  FILENAME: StaticVariables.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  class for accessing static variables and method created to store data during runtime of application.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.Context;
import android.util.Log;

public class StaticVariables 
{


	public static volatile boolean isLogEnabled=false;



	//Static variables used in Home Automation app

	public static String UserName_Used= null;
	public static String Password_Used=null;
	public static String Loggeduser_Used=null;


	//variables to hold data temporarly
	public static String HOUSE_NAME= null;
	public static String WHOUSE_NAME=null;
	public static String HOUSE_DB_NAME=null;
	public static String Bluetooth_MacID=null;
	public static String CurrentConfigurationRoom=null;


	//static variables used in both apps
	//public static String UserName="admin12345";
	//public static String Password="12345678";

	//public static int Port=9951; //default port for main server
	//public static String Host= null;

	public static int MAIN_SERVER_PORT=9951,UDP_SERVER_PORT=9952;
			//WIRELSESS_SERVER_PORT=9961;

	public static boolean udp_use=false;
	public static int Port_udp=9951; //default port for main server
	public static String Host_udp= null;

	public static int counter=1; 

	//static variables used in IR App

	public static String ADMIN_PASSWORD="54321"; 
	public static volatile String Current_RoomName = "abc";
	public static volatile int Current_RoomNo = 0;
	public static volatile String Current_DeviceName = "xyz";
	public static volatile String Current_Devicetype = "pqr";
	public static volatile int current_DevicNo = 0;
	public static volatile String Temp_RoomName = "room";
	public static volatile int Temp_RoomNo = 0;
	public static volatile boolean Learning_Mode = false;


	public static int BroadcastPort=0000;  
	public static String ConnectedSSID=null;
	public static String ConnectedPassword=null;
	public static boolean isNoInternetRequire=false; 
	public static String RemoteConnectionIP=null;


	//static variable to get info from inside house app
	public static boolean IStoConfig=false;
	public static String ACTIVITY_NAME=null;
	public static String HOUSE_NUMBER=null;

	public static Context appContext=null;

	//logging app
	public static void printLog(String tag,String logText){

		if(isLogEnabled){

			//showing log
			Log.d("wrls test"+tag,logText);

		}

	}

}
