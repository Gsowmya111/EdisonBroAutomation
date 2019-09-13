package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: WirelessDeviceConfiguration.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to configure wireless panel.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Connections.UsbService;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

/**
 * Created by Divya on 8/30/2017.
 */


public class WirelessDeviceConfiguration extends Activity implements OnClickListener{
	TextView deviceInfo;
	Button configure,b_home;
	ImageView bt_status,serverStatus,navigateBack;
	ProgressDialog pdlg;

	int BT_ON  = R.drawable.usb_icon_on;
	int BT_OFF = R.drawable.usb_icon_off;
	int server_online=R.drawable.connected;
	int server_offline=R.drawable.not_connected;


	//config. module Reset Commands
	String ATF = "AT&F\r";
	String ECO = "ATS12=0510\r";
	String Link = "ATS09=0123456789ABCDEF0123456789ABCDEF:password\r";
	String Security = "ATS0A=0114:password\r";


	//Device Search commands 
	String ATI = "ATI\r";
	String DPAN = "AT+DASSL\r";
	String JPAN = "AT+JN\r";
	String SINK = "AT+SSINK\r";

	public static int cmdCount = 0;
	String DeviceName=null,Bluetooth_ID = null,
			CordintorID_WLS=null,CordintorID=null,CordinatorJPAN=null,
			DeviceID=null,DEVICE_FULL_NAME=null,ROOM_NAME_WLS=null,DeviceGroupId=null;
	int DeviceNumber=0 ,RoomNo=0;
	String switchCount=null, fanCount=null ,DeviceType=null ;

	boolean exceptionOccured=false ,isTimeOut=false;
	final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5;

	WirelessConfigurationAdapter WhouseDB=null;
	HouseConfigurationAdapter houseDB=null;
	//Bluetooth bluetooth;
	Tcp tcp=null;

	List<String> Full_DevNames_List;
	List<String> Device_Type_List;

	static boolean isSearchedCompleted=false,isResetCompleted=false,isConfigCompleted
			,resetTimeout=false,searchtimeout=false,configTimeout=false;

	static int searchAttempt=0;

	Handler searchTimerHandler=null; 
	Runnable searchTimerRunnable=null;

	Handler configTimerHandler=null;
	Runnable configTimerRunnable=null;

	Handler resetTimerHandler=null;
	Runnable resetTimerRunnable=null;

	boolean IS_SERVER_CONNECTED=false ;
	static boolean isTcpConnecting=false;


	long CLB_Counts=0;
	int CLB_DevID_INDEX=0;

	String DevIdList[] =null;

	boolean ConfigFLAG=false;
	int DEVICE_TYPE_PIR=718;
	String PIR_DevType="WPS1";
	int DEVICE_TYPE_PIR_2=720;
	String PIR_DevType_2="WPD1";

	String RoomIconName , UserDeviceName;
	String current_room_image_name=null;
	LocalDatabaseAdapter locdb;
	boolean Usb_device_connected=false;
	LocalListArrangeTable locallist_adap;

	private ServerDetailsAdapter sdadap;
	/*
      * Notifications from UsbService will be received here.
      */
	private final BroadcastReceiver mUsbReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			switch (intent.getAction()) {
				case UsbService.ACTION_USB_PERMISSION_GRANTED: // USB PERMISSION GRANTED
					Toast.makeText(context, "USB Ready", Toast.LENGTH_SHORT).show();
					//usbService.write(("AT" + "\r\n").getBytes());
					bt_status.setImageResource(BT_ON);
					Usb_device_connected=true;
					break;
				case UsbService.ACTION_USB_PERMISSION_NOT_GRANTED: // USB PERMISSION NOT GRANTED
					Toast.makeText(context, "USB Permission not granted", Toast.LENGTH_SHORT).show();
					break;
				case UsbService.ACTION_NO_USB: // NO USB CONNECTED
					Toast.makeText(context, "No USB connected", Toast.LENGTH_SHORT).show();
					Usb_device_connected=false;
					//popup("No USB connected");
					break;
				case UsbService.ACTION_USB_DISCONNECTED: // USB DISCONNECTED
					Toast.makeText(context, "USB disconnected", Toast.LENGTH_SHORT).show();
					bt_status.setImageResource(BT_OFF);
					popup("USB disconnected");
					break;
				case UsbService.ACTION_USB_NOT_SUPPORTED: // USB NOT SUPPORTED
					Toast.makeText(context, "USB device not supported", Toast.LENGTH_SHORT).show();
					break;
			}
		}
	};

	private UsbService usbService;

	private MyHandler mHandler;

	private final ServiceConnection usbConnection = new ServiceConnection() {
		@Override
		public void onServiceConnected(ComponentName arg0, IBinder arg1) {
			usbService = ((UsbService.UsbBinder) arg1).getService();
			usbService.setHandler(mHandler);
		}

		@Override
		public void onServiceDisconnected(ComponentName arg0) {
			usbService = null;
		}
	};


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.wireless_deviceconfiguration);
		mHandler = new MyHandler(this);
		serverStatus=(ImageView) findViewById(R.id.server_status);
		bt_status=(ImageView) findViewById(R.id.bt_status);
		deviceInfo=(TextView) findViewById(R.id.Device_Info);
		configure=(Button) findViewById(R.id.Configure);
		navigateBack=(ImageView) findViewById(R.id.imageView2);

		// Setting onclick listener
		configure.setOnClickListener(this); 		
		//setting click listener for server status image
		serverStatus.setOnClickListener(this);
		navigateBack.setOnClickListener(this);

		b_home= (Button) findViewById(R.id.btnhome);
		b_home.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intnt=new Intent(WirelessDeviceConfiguration.this, Main_Navigation_Activity.class);
				startActivity(intnt);
				finish();
			}
		});

//assigning house name
		StaticVariables.WHOUSE_NAME=StaticVariables.HOUSE_NAME+"_WLS";

		StaticVariabes_div.log("whn"+StaticVariables.WHOUSE_NAME,"test");

		FullDevNameList();				// Assigning Full names For Devices 
		DevTypeList();					// Assigning Device Types For Devices


		try{
			//opening wireless Panel database	
			WhouseDB=new WirelessConfigurationAdapter(this);
			//WhouseDB.open();

			//opening house database	
			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();

			locdb=new LocalDatabaseAdapter(this);
			locdb.opendb();
		}catch(Exception e){
			e.printStackTrace();
		}		

		/*if(Bluetooth.btConnected){
			//passing bluetooth handler instance
			bluetooth=new Bluetooth(BtHandler);
			bt_status.setImageResource(BT_ON);
		}*/


		//Getting Values From Intent
		Intent DataPacket=getIntent();				
		DeviceType=DataPacket.getStringExtra("devType");
		DeviceName=DataPacket.getStringExtra("devName");
		DEVICE_FULL_NAME=DataPacket.getStringExtra("devFullName");
		ROOM_NAME_WLS=DataPacket.getStringExtra("WLS_RoomName");
		DeviceGroupId=DataPacket.getStringExtra("groupId");
		RoomIconName=DataPacket.getStringExtra("roomiconnam");
		UserDeviceName=DataPacket.getStringExtra("usrdevnam");

		StaticVariabes_div.log("DeviceGroupId"+DeviceGroupId,"Dev");


		//setting current room as static field
		StaticVariables.CurrentConfigurationRoom=ROOM_NAME_WLS;

		//Fetching Max Device Number From DataBase
		DeviceNumber=houseDB.getUniqueDeviceNo();		

		//opening wireless database as closed in previous operation
		//openWlsDb();

		try{
			//Fetching Device ID from wireless server Database
			WhouseDB.open();
			CordintorID_WLS=WhouseDB.WLS_CordinatorID();
			StaticVariables.printLog("CordintorID_WLS 1",CordintorID_WLS);


			//Fetching coordinator Device JPAN from Database
			CordinatorJPAN=houseDB.CordinatorJPAN();

			//fetching device id from main server database
			CordintorID=houseDB.CordinatorID();

			CordintorID_WLS=CordintorID;

			StaticVariables.printLog("CordintorID_WLS 2",CordintorID_WLS);


		}catch(Exception e){
			e.printStackTrace();
		}

		try{
			//getting room number for current room
			RoomNo=setCurrentRoomNumber(ROOM_NAME_WLS);
			if(RoomNo>99){
				configure.setEnabled(false);
				msg("Room Numbers Length Exceeded.Please Choose old room name."); 
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		try{
			//Fetching bluetooth MacId from Database
			Cursor mcursor=houseDB.fetchData(HouseConfigurationAdapter.CONFIG_TABLE);
			Bluetooth_ID=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.CONFIG_MacID));
			if(Bluetooth_ID!=null){
				//Assigning mac id to static fields
				StaticVariables.Bluetooth_MacID=Bluetooth_ID;
			}		
			//closing cursor
			if(mcursor!=null){
				mcursor.close();
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		//Setting Text For Particular Device From Resources
		String DeviceText=ConfigDeviceText(DeviceName);
		//Setting configuration information for device
		deviceInfo.setText(DeviceText);

		//resetting search attempt
		searchAttempt=0;

		//calling connection in activity
		RegainingConnection();

		if(UsbService.SERVICE_CONNECTED) {

			StaticVariables.printLog("usb","SERVICE_CONNECTED");

			if (UsbService.serialPortConnected) {
				StaticVariables.printLog("usb","serialPortConnected");
				bt_status.setImageResource(BT_ON);
			} else {
				popup("Usb not connected 1");
			}
		}else {
			StaticVariables.printLog("usb","SERVICE_NOT_CONNECTED");

			if (Usb_device_connected) {
				bt_status.setImageResource(BT_ON);
			} else {
				//popup("Usb not connected 2");
			}
		}
		 
	}

	//method to get auto TCP connection in class
	void RegainingConnection(){

		StaticVariabes_div.log("regaining","Host");
		Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
		//starting a background thread to make connection
		Thread thread =new Thread(){
			public void run(){
				isTcpConnecting=true;
				IS_SERVER_CONNECTED=Tcp.EstablishConnection(TcpHandler);
				runOnUiThread(new Runnable() {
					public void run() 
					{
						if(IS_SERVER_CONNECTED){
							serverStatus.setImageResource(server_online);	
							StaticVariables.printLog("TAG","CONNECTED");

						}else{
							serverStatus.setImageResource(server_offline);	
							StaticVariables.printLog("TAG","NOT CONNECTED");
						}
						isTcpConnecting=false;
					}
				});

			}
		};thread.start();									
	}	




	// Filling Device Name List with Full Names which will Visible to End User
	private void FullDevNameList(){

		Full_DevNames_List=new ArrayList<String>();

		Resources res = getResources();
		String[] fullNames = res.getStringArray(R.array.Full_DevNames_Wireless);
		Full_DevNames_List .addAll(Arrays.asList(fullNames));

		//adding select option item in list
		Full_DevNames_List.add("Select Device");

	}

	// Filling Device Type Which Will be used Internally 
	private void DevTypeList(){
		Resources res = getResources();
		String[] devType = res.getStringArray(R.array.Device_Type_Wireless);
		Device_Type_List = Arrays.asList(devType);

	}

	//method to set room number  i.e if room exists then taking old room number and if 
	//room not exists then giving incremented room number
	private int setCurrentRoomNumber(String roomName){
		int currentRoomNo=0;
		int matched_room_number=0;

		String matched_room_image_name=null;

		try{
			ArrayList<String> UniqueRoomList=new ArrayList<String>();
			TreeSet<String> rooms_wls=new TreeSet<String>(); 
			try{
				rooms_wls=houseDB.RoomNameList();
				//Fetching list of All room names from database and adding to local array list
				if(rooms_wls.size()>0)
					UniqueRoomList.addAll(rooms_wls);
			}catch(Exception e){
				e.printStackTrace();
			}

			//loop to check if room supplied  matches from database or not
			for(int i=0;i<UniqueRoomList.size();i++){
				//checking if current room already existing in database or not
				if(roomName.equals(UniqueRoomList.get(i))){

					try{
						//fetching room number of current room
						matched_room_number=houseDB.CurrentRoomNumber(roomName);

						//fetching room image of current room
						matched_room_image_name=houseDB.CurrentRoomImageName(roomName);

						StaticVariables.printLog("DB ROOM NUMBER","Room Matched with db and Room No:"+matched_room_number);
					}catch(Exception e){
						e.printStackTrace();
					}

					break;
				}
			}


			//Fetching Max Room Number From DataBase
			//int max_rno=houseDB.maxRoomNumber();

			if(matched_room_number==0){ 
				//Fetching unique Room Number From DataBase
				currentRoomNo= houseDB.getUniqueRoomNo();

				current_room_image_name= RoomIconName;

				StaticVariables.printLog("TAG","ITS NEW ROOM");
			}
			else{
				//setting room number fetched from table  corresponding to current room
				//which already exists
				currentRoomNo=matched_room_number;

				current_room_image_name=matched_room_image_name;

				StaticVariables.printLog("TAG","ITS OLD ROOM");
			}

			//open database as it got closed in previous operation
			openWlsDb();

			StaticVariables.printLog("TAG","Room No. Assigned :"+currentRoomNo);
		}catch(Exception e){
			e.printStackTrace();
		}

		openWlsDb();

		return currentRoomNo;
	}

	//open db
	void openWlsDb(){


		//opening database again if it is closed
		if(!WirelessConfigurationAdapter.sdb.isOpen()){    
			WhouseDB.open(); 
			StaticVariables.printLog("TAG","DB opened again ");
		}	

	}


	//Device Configuration Information For user
	private String ConfigDeviceText(String device){
		String text="";
		Resources res = getResources();
		//assigning value for text variable depends upon device selected
		switch(deviceNames.valueOf(device)){
		case WS01 : case WS02: case WS03: case WS04: case WS05 : case WS06:
		case WC01 : case WC02: case WF01: case WF02: case WD01 : 
		case WR01 : case WSS1: case WPS1: case WPD1:case WS21:case WS51:case WS71:case WS81:{
			//removed case for phase1 are : case WDD1:  case WBS1: case WGS1 :case WZ01:
			text=res.getString(R.string.Wireless_Panel_Details);	
			break;
		}
		default:{
			text=res.getString(R.string.Default_Details);
			configure.setEnabled(false);
			break;
		}
		}

		return text;
	}

	//device types
	private enum deviceNames{ 
		WS01,WS02,WS03,WS04,WS05,WS06,WC01,WC02,WF01,WF02,
		WD01,WDD1,WR01,WZ01,WSS1,WBS1,WGS1,WPS1,WNFC,WPD1,WS21,WS51,WS71,WS81
	};

	//making data format that we have send to configuration device
	private String DataToSend(){
		String DataToSend=null;
		try{
			// Making A String With Desire format i.e 

			/*
			 * "@"+DeviceType(3char)+DeviceNumber(4char)+
				RoomNo(2char)+DevicID(16char)+"$"
			 */
			/*in case of PIR device type
			 * "@"+DeviceType(3char)+DeviceNumber(4char)+
				DeviceGroupId(3char)+RoomNo(2char)+DevicID(16char)+"$"
			 */
			String DEV_TYPE=null,DEV_NUMBER=null,DEV_ID=null,ROOM_NO=null;

			DEV_TYPE=DeviceType;
			DEV_NUMBER=""+DeviceNumber;
			DEV_ID=CordintorID;
			ROOM_NO=""+RoomNo;

			if(ROOM_NO.length()<2){
				ROOM_NO="0"+ROOM_NO;
			}

			//Making Device Number of length four if required
			while(DEV_NUMBER.length()<4){		   
				DEV_NUMBER="0"+DEV_NUMBER;
			}

			//Making Device group id of length three if required
			while(DeviceGroupId.length()<3){		  
				DeviceGroupId="0"+DeviceGroupId;
			}

			StaticVariables.printLog("TAG","DEV_TYPE"+DEV_TYPE);
			StaticVariables.printLog("TAG","DEV_NUMBER"+DEV_NUMBER);
			StaticVariables.printLog("TAG","DEV_ID"+DEV_ID);
			StaticVariables.printLog("TAG","ROOM_NO"+ROOM_NO);

			//setting group id for WPC if selected
			if((DeviceName.equals(PIR_DevType)||DeviceName.equals(PIR_DevType_2)))
			{
				StaticVariables.printLog("TAG","PIR DATA FORMAT");
				//Checking all parameters and making final string of length 30char
				if(DEV_TYPE.length()==3 && DEV_NUMBER.length()==4 && DeviceGroupId.length()==3
						&& ROOM_NO.length()==2  && DEV_ID.length()==16){
					//format to send main server device id S
					DataToSend="@"+DEV_TYPE+DEV_NUMBER+DeviceGroupId+ROOM_NO+DEV_ID+"$";
					StaticVariables.printLog("TAG","PIR DATA FORMAT IN USE");
				}
			}else{
				StaticVariables.printLog("TAG","OTHER Wireless Device DATA FORMAT");


				//Checking all parameters and making final string of length 27char
				if(DEV_TYPE.length()==3 && DEV_NUMBER.length()==4 
						&& ROOM_NO.length()==2  && DEV_ID.length()==16){
					//format to send main server device id S
					DataToSend="@"+DEV_TYPE+DEV_NUMBER+ROOM_NO+DEV_ID+"$";
					StaticVariables.printLog("TAG","OTHER Wireless Device DATA FORMAT IN USE");
				}
			}




		}catch(Exception e){
			e.printStackTrace();
		}

		return DataToSend;
	}

	//Sending Configuration Data to Searched Device
	private void ConfigurationCommand(){
		try{
			String data_to_send=DataToSend();		// getting desired data string
			StaticVariables.printLog("TAG "+"DATA TO SEND","Data to send check : "+data_to_send);

			//Making Final Command Format
			if(DeviceID.length()==16 && (data_to_send.length()==27 || (data_to_send.length()==30 &&( DeviceName.equals(PIR_DevType)||DeviceName.equals(PIR_DevType_2)))) ){
			//if(DeviceID.length()==16 && (data_to_send.length()==27 || (data_to_send.length()==30 )) ){
			    String DATA = "AT+UCAST:" + DeviceID + "=" + data_to_send + "\r\n";
				//String DATA = "AT+UCAST:" + DeviceID + "=$01001000D6F0004430AFF@\r\n";
				StaticVariables.printLog("DATA TO SEND","Data to send : "+DATA);
				//bluetooth.btWrite(DATA);				//Sending data to device
				usbService.write(DATA.getBytes());
			}else{
				Toast.makeText(getApplicationContext(), "Invalid Data Format", Toast.LENGTH_SHORT).show();
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//TCP handler for handling TCP responses
	private Handler TcpHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case TCP_LOST:
			{
				runOnUiThread(new Runnable() {
					public void run() 
					{
						serverStatus.setImageResource(server_offline);		
					}
				});

				break;
			}
			}
		}
	};





	public class MyHandler extends Handler {
		private final WeakReference<WirelessDeviceConfiguration> mActivity;

		public MyHandler(WirelessDeviceConfiguration activity) {
			mActivity = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{

				case UsbService.MESSAGE_FROM_SERIAL_PORT:
				{
					String DataG=(String) msg.obj;
					String DataGet=DataG.replaceAll("\r\n","");
					StaticVariables.printLog("usb RESPONSE","DATA GET IN HANDLER:"+DataGet+ "length "+DataGet.length());

					if(!configTimeout)
					{
						try{

							if(DataGet!=null && DataGet.contains("*ERROR,") && DataGet.endsWith("#")){

								//cancel main configuration timer
								configTimerHandler.removeCallbacks(configTimerRunnable);

								String ResData = DataGet.substring(26);
								StaticVariables.printLog("data", "data :"+ResData);

								String mainData=ResData.substring(1,ResData.length()-1);
								StaticVariables.printLog("data", "Main data :"+mainData);


								//splitting device type data
								String[] data=mainData.split(",");

								String realDevType=data[1];
								String deviceNname = null;

								if(realDevType!=null && realDevType.length()==3){
									// getting index of previously configured device
									int index=Device_Type_List.indexOf(realDevType);

									// getting full name of device based on  device type index
									deviceNname=Full_DevNames_List.get(index);

								}


								//showing error dialog for device mismatch operation
								deviceTypeErrorDialog("Device Type Error", "Attempted to Configure "+deviceNname+" As "+DEVICE_FULL_NAME+
										".\nDo you want to change the Device Type and come for configuration again?",realDevType);


								//breaking further processing
								break;

							}else if(DataGet!=null && DataGet.contains("#OK#")){
								StaticVariables.printLog("Response #ok# recieved","data to send");
								String DATA=null;
								//sending coordinator jpan id
								String datatoSend="@"+CordinatorJPAN+"$";

								//making config flag true
								ConfigFLAG=true;

								//checking if jpan id is not null
								if(datatoSend!=null && datatoSend.length()>0)
									DATA = "AT+UCAST:" + DeviceID + "="+datatoSend + "\r\n";
								else
									StaticVariables.printLog("TAG","Invalid jpan id");

								updateProgressStatus("Configuring Device...", 80);

								//sending data to device
								//bluetooth.btWrite(DATA);
								usbService.write(DATA.getBytes());
								StaticVariables.printLog("Response #ok# sent","data to send "+DATA);
								//breaking further processing
								break;

							}else if (ConfigFLAG && DataGet.startsWith("UCAST") && DataGet.endsWith("#")
									&& DataGet.length() == 35 ) {

								StaticVariables.printLog("RESPONSE ", "data length : " + DataGet.length());
								String Res_type = DataGet.substring(31, DataGet.length()-1);
								StaticVariables.printLog("RESPONSE", "Device type recievd : " + Res_type);
								ResponseDialog(Res_type);

								//breaking further processing
								break;

							}

							//getting @OK* response for data sent i.e main data
							if(ConfigFLAG && DataGet.startsWith("UCAST") && DataGet.endsWith("*")&& DataGet.length() == 30){

								//checking if data contains ok
								if(DataGet.endsWith("@OK*")){
									StaticVariables.printLog("RESPONSE ", "data length : " + DataGet.length());

									StaticVariables.printLog("TAG","timer 1 cancelled");
									delay(100);
									//sending wireless server device id
									String data_To_send="@"+CordintorID_WLS+"$";
									//String data_To_send="@"+"000D6F0001B5511C"+"$";
									String DATA = "AT+UCAST:" + DeviceID + "=" +data_To_send+ "\r\n";

									updateProgressStatus("Configuring Device...", 90);

									//bluetooth.btWrite(DATA);
									usbService.write(DATA.getBytes());
									StaticVariables.printLog("DATA SENT", "WLS DATA SENT IS  : " + DATA);

									//breaking further processing
									break;
								}

							}

						}catch(Exception e){
							e.printStackTrace();
						}

					}


					try
					{
						switch(cmdCount)
						{


							case 20: {
								if (DataGet.contains("OK")) {
									cmdCount = 21;
									DataGet = "";
									//bluetooth.btWrite(ECO);
									usbService.write(ECO.getBytes());
									updateProgressStatus("Resetting Configuration Tool...",20);

								} else if (DataGet.contains("ERROR")) {

									updateProgressStatus("Resetting Configuration Tool...",20);

									cmdCount = 20;
									DataGet = "";
									//bluetooth.btWrite(ATF);
									usbService.write(ATF.getBytes());
								}

								break;
							}
							case 21: {
								if (DataGet.contains("OK")) {
									cmdCount = 22;
									DataGet = "";
									updateProgressStatus("Resetting Configuration Tool...",40);
									//bluetooth.btWrite(Link);
									usbService.write(Link.getBytes());

								} else if (DataGet.contains("ERROR")) {
									cmdCount = 21;
									DataGet = "";
									//bluetooth.btWrite(ECO);
									usbService.write(ECO.getBytes());
								}
								break;

							}
							case 22: {

								if 	(DataGet.contains("OK")) {
									cmdCount = 23;
									DataGet = "";
									//bluetooth.btWrite(Security);
									usbService.write(Security.getBytes());
									updateProgressStatus("Resetting Configuration Tool...",60);
								} else if (DataGet.contains("ERROR")) {
									cmdCount = 22;
									DataGet = "";
									//bluetooth.btWrite(Link);
									usbService.write(Link.getBytes());

								}
								break;
							}
							case 23: {

								if (DataGet.contains("OK")) {

									if(!resetTimeout){
										//starting searching process
										cmdCount = 11;
										DataGet = "";
										updateProgressStatus("Configuration Tool Resetted...",100);

										isResetCompleted=true;
										//canceling reset timeout
										resetTimerHandler.removeCallbacks(resetTimerRunnable);

										//reset serch variable
										searchAttempt=0;

										//starting configuration process
										searchTimer();
									}else{
										cmdCount = 15;
										DataGet = "";
										break;
									}

								} else if (DataGet.contains("ERROR")) {
									cmdCount = 23;
									DataGet = "";
									//bluetooth.btWrite(Security);
									usbService.write(Security.getBytes());
								}
								break;
							}



							case 10: {
								if (DataGet.contains("OK")) {

									updateProgressStatus("Searching Device...",
											20);

									cmdCount = 11; // Setting Command Count to 11
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(DPAN); // Sending DPAN Command
									usbService.write(DPAN.getBytes());
									StaticVariables.printLog("DATA SENT", " CASE 10 :COMMAND SENT : DPAN");

								} else if (DataGet.contains("ERROR")) {

									// update_sts("Searching Device!...");
									cmdCount = 10; // Setting Command Count to 10
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(ATI); // Again Sending AT Command
									usbService.write(ATI.getBytes());
									StaticVariables.printLog("DATA SENT",
											" CASE 10 (ERROR):COMMAND SENT  : AT");
								}
								break;
							}

							case 11: {
								if (DataGet.contains("LeftPAN") || DataGet.contains("OK") ) {

									updateProgressStatus("Searching Device...",
											40);
									cmdCount = 12; // Setting Command Count to 12
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(JPAN); // Sending JPAN Command
									usbService.write(JPAN.getBytes());
									StaticVariables.printLog("DATA SENT", " CASE 11 :COMMAND SENT : JPAN");
								} else if (DataGet.contains("ERROR") ) {

									updateProgressStatus("Searching Device...",
											40);
									cmdCount = 12; // Setting Command Count to 12
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(JPAN); // Sending JPAN Command
									usbService.write(JPAN.getBytes());
									StaticVariables.printLog("DATA SENT",
											" CASE 11 (ERROR):COMMAND SENT : JPAN");
								}


								break;
							}

							case 12: {
								if (DataGet.contains("JPAN")) {

									updateProgressStatus("Searching Device...",
											60);

									cmdCount = 13; // Setting Command Count to 13
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(SINK); // Sending SINK Command
									usbService.write(SINK.getBytes());
									StaticVariables.printLog("DATA SENT", " CASE 12  :COMMAND SENT :SINK");
								} else if (DataGet.contains("ERROR")) {

									// update_sts("Searching Device..!.");
									cmdCount = 12; // Setting Command Count to 12
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(JPAN); // Again Sending JPAN Command
									usbService.write(JPAN.getBytes());
									StaticVariables.printLog("DATA SENT",
											" CASE 12 (ERROR):COMMAND SENT : JPAN");

								}
								break;
							}
							case 13 :
							{
								if (DataGet.contains("SINK:") )
								{
									try
									{
										Toast.makeText(WirelessDeviceConfiguration.this,"Sink Found",Toast.LENGTH_SHORT).show();
										updateProgressStatus("Device Searched...",
												100);

										int i = DataGet.indexOf("SINK:");

										StaticVariables.printLog("msg", "index is  : " + DataGet.indexOf("SINK:"));
										StaticVariables.printLog("msg", "DATA LENGTH : " + DataGet.length());

										// Fetching sub string from data got
										String address = DataGet.substring((5 + i), (21 + i));

										StaticVariables.printLog("address : ", "address : "+ address);

										if (address.length() == 16) {
											DeviceID = address;				//Storing Device Address in variable
											StaticVariables.printLog("address : ", "Device address : "+ DeviceID);

											//indicating that sink is searched
											isSearchedCompleted=true;

											//canceling search timer
											searchTimerHandler.removeCallbacks(searchTimerRunnable);
										}

										cmdCount = 15;		 	//Setting Command Count to 15

										DataGet="";							//Clearing Varaible
										StaticVariables.printLog("ACTION"," CASE 13  CANCELLING TIMERS") ;
										//*************** starting phase 2 **********************

										//Checking if Device is Already Existing
										try{
											String Result=WhouseDB.isDeviceExists(DeviceID);
											StaticVariables.printLog("RESULT","Result is : "+Result);

											if(!Result.equals("FALSE")){
												String PrevDevType=Result.substring(4,7);
												StaticVariables.printLog("RESULT","PrevDevType is : "+PrevDevType);

												//setting previous device number
												try{
													//filtering device type from response contains devtype and prrvious devno
													//i.e TRUE(3 char devtype)(4char device number) e.g TRUE0031111
													int prevDevNo=Integer.parseInt(Result.substring(7));
													StaticVariables.printLog("RESULT","PrevDevNumber is : "+prevDevNo);
													//assigning previous device number for current device
													DeviceNumber=prevDevNo;
												}catch(Exception e){
													e.printStackTrace();
												}

												//Fetching previous name of this already configured device
												String PrevDevName=previousDevicename(PrevDevType);
												//Showing Warning Dialog that Device Already Existing
												DeviceAlreadyExistsAlert(PrevDevName);
											}else {
												updateProgressStatus("Configuring Device...",70);
												ConfigurationProcessCall();
											}
										}catch(Exception e){
											e.printStackTrace();
										}



									} catch (Exception e) {
										e.printStackTrace();
									}

								} else if (DataGet.contains("ERROR")) {

									//update_sts("Searching Device...!");
									cmdCount = 13;							//Setting Command Count to 13
									DataGet = "";							//Clearing DataGet variable
									//bluetooth.btWrite(SINK);
									// Sending SINK Command
									usbService.write(SINK.getBytes());
									StaticVariables.printLog("DATA SENT"," CASE 13 (ERROR):COMMAND SENT :SINK") ;
								}

								break;
							}
						}

					}catch(Exception e){
						e.printStackTrace();
					}

					break;
				}

				case READ_BYTE:	{
					break;
				}

				case EXCEPTION:
				{
					final String Data=(String) msg.obj;
					StaticVariables.printLog("BT RESPONSE","DATA GET IN HANDLER:"+Data);
					exceptionOccured=true;
					break;
				}

				case BT_LOST:
				{
					runOnUiThread(new Runnable() {
						public void run()
						{
							//showing bluetooth is Disconnected
							bt_status.setImageResource(BT_OFF);
						}
					});
					break;
				}
			}
		}
	};





	// creating Bluetooth Handler to handle Bluetooth Socket Data
	/*private  Handler BtHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case READ_LINE:
			{
				String DataGet=(String) msg.obj;
				//StaticVariables.printLog("BT RESPONSE","DATA GET IN HANDLER:"+DataGet+ "length "+DataGet.length());

				if(!configTimeout)
				{
					try{

						if(DataGet!=null && DataGet.contains("*ERROR,") && DataGet.endsWith("#")){

							//cancel main configuration timer 
							configTimerHandler.removeCallbacks(configTimerRunnable);

							String ResData = DataGet.substring(26);
							StaticVariables.printLog("data", "data :"+ResData);

							String mainData=ResData.substring(1,ResData.length()-1);
							StaticVariables.printLog("data", "Main data :"+mainData);


							//splitting device type data
							String[] data=mainData.split(",");

							String realDevType=data[1];
							String deviceNname = null;

							if(realDevType!=null && realDevType.length()==3){
								// getting index of previously configured device
								int index=Device_Type_List.indexOf(realDevType);				

								// getting full name of device based on  device type index 
								deviceNname=Full_DevNames_List.get(index);	

							}


							//showing error dialog for device mismatch operation
							deviceTypeErrorDialog("Device Type Error", "Attempted to Configure "+deviceNname+" As "+DEVICE_FULL_NAME+
									".\nDo you want to change the Device Type and come for configuration again?",realDevType);


							//breaking further processing
							break;

						}else if(DataGet!=null && DataGet.contains("#OK#")){
							String DATA=null;
							//sending coordinator jpan id 
							String datatoSend="@"+CordinatorJPAN+"$";

							//making config flag true
							ConfigFLAG=true;

							//checking if jpan id is not null
							if(datatoSend!=null && datatoSend.length()>0)
								DATA = "AT+UCAST:" + DeviceID + "="+datatoSend + "\r\n"; 
							else
								StaticVariables.printLog("TAG","Invalid jpan id");

							updateProgressStatus("Configuring Device...", 80);

							//sending data to device
							bluetooth.btWrite(DATA); 

							//breaking further processing
							break;

						}else if (ConfigFLAG && DataGet.startsWith("UCAST") && DataGet.endsWith("#")		
								&& DataGet.length() == 35 ) {

							StaticVariables.printLog("RESPONSE ", "data length : " + DataGet.length());
							String Res_type = DataGet.substring(31, DataGet.length()-1);
							StaticVariables.printLog("RESPONSE", "Device type recievd : " + Res_type);
							ResponseDialog(Res_type);

							//breaking further processing
							break;

						} 

						//getting @OK* response for data sent i.e main data
						if(ConfigFLAG && DataGet.startsWith("UCAST") && DataGet.endsWith("*")&& DataGet.length() == 30){

							//checking if data contains ok
							if(DataGet.endsWith("@OK*")){
								StaticVariables.printLog("RESPONSE ", "data length : " + DataGet.length());

								StaticVariables.printLog("TAG","timer 1 cancelled");
								delay(100);
								//sending wireless server device id
								String data_To_send="@"+CordintorID_WLS+"$";
								//String data_To_send="@"+"000D6F0001B5511C"+"$";
								String DATA = "AT+UCAST:" + DeviceID + "=" +data_To_send+ "\r\n";

								updateProgressStatus("Configuring Device...", 90);

								bluetooth.btWrite(DATA);
								StaticVariables.printLog("DATA SENT", "WLS DATA SENT IS  : " + DATA);

								//breaking further processing
								break;
							}

						}

					}catch(Exception e){
						e.printStackTrace();
					}

				} 


				try
				{
					switch(cmdCount)
					{


					case 20: {
						if (DataGet.contains("OK")) { 
							cmdCount = 21;
							DataGet = "";
							bluetooth.btWrite(ECO); 
							updateProgressStatus("Resetting Configuration Tool...",20);

						} else if (DataGet.contains("ERROR")) {

							updateProgressStatus("Resetting Configuration Tool...",20);

							cmdCount = 20;
							DataGet = "";
							bluetooth.btWrite(ATF);

						}

						break;
					}
					case 21: {
						if (DataGet.contains("OK")) {  
							cmdCount = 22;
							DataGet = "";
							updateProgressStatus("Resetting Configuration Tool...",40);
							bluetooth.btWrite(Link);

						} else if (DataGet.contains("ERROR")) {
							cmdCount = 21;
							DataGet = "";
							bluetooth.btWrite(ECO);
						}
						break;

					}
					case 22: {

						if 	(DataGet.contains("OK")) { 
							cmdCount = 23;
							DataGet = "";
							bluetooth.btWrite(Security);
							updateProgressStatus("Resetting Configuration Tool...",60);
						} else if (DataGet.contains("ERROR")) {
							cmdCount = 22;
							DataGet = "";
							bluetooth.btWrite(Link); 

						} 
						break;
					}
					case 23: {

						if (DataGet.contains("OK")) { 

							if(!resetTimeout){
								//starting searching process 
								cmdCount = 11;
								DataGet = "";  
								updateProgressStatus("Configuration Tool Resetted...",100); 

								isResetCompleted=true;
								//canceling reset timeout
								resetTimerHandler.removeCallbacks(resetTimerRunnable);

								//reset serch variable
								searchAttempt=0;

								//starting configuration process
								searchTimer();
							}else{
								cmdCount = 15;
								DataGet = ""; 
								break;
							}

						} else if (DataGet.contains("ERROR")) {
							cmdCount = 23;
							DataGet = "";
							bluetooth.btWrite(Security);  
						} 
						break;
					}



					case 10: {
						if (DataGet.contains("OK")) {

							updateProgressStatus("Searching Device...",
									20);

							cmdCount = 11; // Setting Command Count to 11
							DataGet = ""; // Clearing DataGet variable
							bluetooth.btWrite(DPAN); // Sending DPAN Command
							StaticVariables.printLog("DATA SENT", " CASE 10 :COMMAND SENT : DPAN");

						} else if (DataGet.contains("ERROR")) {

							// update_sts("Searching Device!...");
							cmdCount = 10; // Setting Command Count to 10
							DataGet = ""; // Clearing DataGet variable
							bluetooth.btWrite(ATI); // Again Sending AT Command
							StaticVariables.printLog("DATA SENT",
									" CASE 10 (ERROR):COMMAND SENT  : AT");
						} 
						break;
					}

					case 11: {
						if (DataGet.contains("LeftPAN") || DataGet.contains("OK") ) {

							updateProgressStatus("Searching Device...",
									40);
							cmdCount = 12; // Setting Command Count to 12
							DataGet = ""; // Clearing DataGet variable 
							bluetooth.btWrite(JPAN); // Sending JPAN Command
							StaticVariables.printLog("DATA SENT", " CASE 11 :COMMAND SENT : JPAN");
						} else if (DataGet.contains("ERROR") ) {

							updateProgressStatus("Searching Device...",
									40);
							cmdCount = 12; // Setting Command Count to 12
							DataGet = ""; // Clearing DataGet variable 
							bluetooth.btWrite(JPAN); // Sending JPAN Command
							StaticVariables.printLog("DATA SENT",
									" CASE 11 (ERROR):COMMAND SENT : JPAN");
						}


						break;
					}

					case 12: {
						if (DataGet.contains("JPAN")) {

							updateProgressStatus("Searching Device...",
									60);

							cmdCount = 13; // Setting Command Count to 13
							DataGet = ""; // Clearing DataGet variable
							bluetooth.btWrite(SINK); // Sending SINK Command
							StaticVariables.printLog("DATA SENT", " CASE 12  :COMMAND SENT :SINK");
						} else if (DataGet.contains("ERROR")) {

							// update_sts("Searching Device..!.");
							cmdCount = 12; // Setting Command Count to 12
							DataGet = ""; // Clearing DataGet variable
							bluetooth.btWrite(JPAN); // Again Sending JPAN Command
							StaticVariables.printLog("DATA SENT",
									" CASE 12 (ERROR):COMMAND SENT : JPAN");

						}
						break;
					}
					case 13 :
					{
						if (DataGet.contains("SINK:") ) 
						{
							try
							{
								updateProgressStatus("Device Searched...",
										100);

								int i = DataGet.indexOf("SINK:");

								StaticVariables.printLog("msg", "index is  : " + DataGet.indexOf("SINK:"));
								StaticVariables.printLog("msg", "DATA LENGTH : " + DataGet.length());

								// Fetching sub string from data got
								String address = DataGet.substring((5 + i), (21 + i));

								if (address.length() == 16) {
									DeviceID = address;				//Storing Device Address in variable
									StaticVariables.printLog("address : ", "Device address : "+ DeviceID);

									//indicating that sink is searched 
									isSearchedCompleted=true; 

									//canceling search timer
									searchTimerHandler.removeCallbacks(searchTimerRunnable);
								}

								cmdCount = 15;		 	//Setting Command Count to 15

								DataGet="";							//Clearing Varaible
								StaticVariables.printLog("ACTION"," CASE 13  CANCELLING TIMERS") ;
								/*//*************** starting phase 2 **********************

								//Checking if Device is Already Existing
								try{
									String Result=WhouseDB.isDeviceExists(DeviceID);
									StaticVariables.printLog("RESULT","Result is : "+Result);

									if(!Result.equals("FALSE")){
										String PrevDevType=Result.substring(4,7);
										StaticVariables.printLog("RESULT","PrevDevType is : "+PrevDevType);

										//setting previous device number 
										try{
											//filtering device type from response contains devtype and prrvious devno 
											//i.e TRUE(3 char devtype)(4char device number) e.g TRUE0031111
											int prevDevNo=Integer.parseInt(Result.substring(7));
											StaticVariables.printLog("RESULT","PrevDevNumber is : "+prevDevNo);
											//assigning previous device number for current device
											DeviceNumber=prevDevNo;
										}catch(Exception e){
											e.printStackTrace();
										}

										//Fetching previous name of this already configured device
										String PrevDevName=previousDevicename(PrevDevType);
										//Showing Warning Dialog that Device Already Existing
										DeviceAlreadyExistsAlert(PrevDevName);
									}else {
										updateProgressStatus("Configuring Device...",70);
										ConfigurationProcessCall();
									}
								}catch(Exception e){
									e.printStackTrace();
								}



							} catch (Exception e) {
								e.printStackTrace();
							}

						} else if (DataGet.contains("ERROR")) {

							//update_sts("Searching Device...!");
							cmdCount = 13;							//Setting Command Count to 13
							DataGet = "";							//Clearing DataGet variable
							bluetooth.btWrite(SINK);				//Sending SINK Command
							StaticVariables.printLog("DATA SENT"," CASE 13 (ERROR):COMMAND SENT :SINK") ;
						} 

						break;
					}
					}

				}catch(Exception e){
					e.printStackTrace();
				}

				break;
			}

			case READ_BYTE:	{
				break;
			}

			case EXCEPTION:
			{
				final String Data=(String) msg.obj;
				StaticVariables.printLog("BT RESPONSE","DATA GET IN HANDLER:"+Data);
				exceptionOccured=true;
				break;
			}

			case BT_LOST:
			{
				runOnUiThread(new Runnable() {
					public void run() 
					{
						//showing bluetooth is Disconnected
						bt_status.setImageResource(BT_OFF);			
					}
				});
				break;
			}
			}
		}
	};*/

	//ConfigurationProccessCall
	private void ConfigurationProcessCall(){
		//sending configuration command
		ConfigurationCommand();			

		//starting configuration timer for devices  
		mainConfigurationTimer();

	}

	//Progress Bar
	private void ProgressDialog(final String msg)
	{
		runOnUiThread(new Runnable() {			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				if(pdlg==null){
					pdlg=new ProgressDialog(WirelessDeviceConfiguration.this,ProgressDialog.THEME_HOLO_LIGHT);
					pdlg.setMessage(msg);
					pdlg.setIndeterminate(false);
					pdlg.setCancelable(false);
					pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pdlg.setMax(100);
					Resources res = getResources();
					// Get the Drawable custom_progressbar                     
					Drawable customDrawable= res.getDrawable(R.drawable.progressbar);
					// set the drawable as progress drawavle
					pdlg.setProgressDrawable(customDrawable);
					pdlg.show();
				}

			}
		});

	}		

	void updateProgressStatus(final String msg,final int progress){
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				if(pdlg!=null){

					pdlg.setMessage(msg);
					pdlg.setProgress(progress);
				}
			}
		});
	}

	//Response Handling method
	private void ResponseDialog(final String responseType){
		try{

			StaticVariables.printLog("TAG","DeviceType in Response Dialog :"+DeviceType+" and responseType :"+responseType);

			//comparing response with device type selected by user
			if(responseType.equals(DeviceType)){

				//indicating that configuration completed
				isConfigCompleted=true; 

				//resetting search count
				searchAttempt=0;

				//canceling config timer
				configTimerHandler.removeCallbacks(configTimerRunnable);


				//getting integer value from variable
				int DevType=Integer.parseInt(DeviceType);

				boolean isSaved=WhouseDB.Update_CNFG_Table(DeviceID,DeviceName,DevType, DeviceNumber,ROOM_NAME_WLS,RoomNo,UserDeviceName);
				if(isSaved){

					if((DevType==DEVICE_TYPE_PIR)||(DevType==DEVICE_TYPE_PIR_2)){
						StaticVariables.printLog("PIR","PIR Device type"+DevType);
						boolean update=houseDB.UpdateMaster(DeviceName,""+DevType, DeviceNumber, DeviceID, ROOM_NAME_WLS, RoomNo, "PIR", DeviceGroupId,current_room_image_name,UserDeviceName);
						if(update){
							StaticVariables.printLog("PIR","PIR saved in Master Table");
						}else{
							StaticVariables.printLog("PIR","Unable to save PIR in master table");
						}
					}else{
						StaticVariables.printLog("PIR","Not a PIR Device");
					}

					//success dialog for other  configuration
					ConfigurationSuccessDialog(DEVICE_FULL_NAME);

					Update_db_version_number();

					try {
						LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
						locallist_adap = new LocalListArrangeTable(WirelessDeviceConfiguration.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
						locallist_adap.open();
						locallist_adap.deleteall();
						locallist_adap.close();
					}catch(Exception e){
						e.printStackTrace();

					}

				}else{
					//error dialog for other  configuration
					ConfigurationErrorDialog("Details Not Saved","Unable to Save Details for "+DEVICE_FULL_NAME+" Device.Try Configuring Again.");

				}	

			}else{ 
				//showing error dialog for device mismatch operation
				responseErrorDialog("Response Mismatched", "Device Type Response Mismatched.\nPlease try to configure again.");
				
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}		

	//showing Device already exists warning dialog
	private void DeviceAlreadyExistsAlert(final String DeviceName){
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//canceling progressbar
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Device Already Exists");
				dialog.setMessage("Device Already Configured As "+DeviceName+" Panel."
						+ "\nConfiguring This Device Again will Delete The Previous Records Related to This Device."
						+ "\nDo You Want To Continue Configuration Process?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(WirelessDeviceConfiguration.this,WirelessConfigurationDetails.class);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();
					}
				});

				dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ProgressDialog("Configuring Device...");
						updateProgressStatus("Configuring Device...", 70);
						ConfigurationProcessCall();		// starting configuration process
					}
				});

				dialog.show();
			}
		});
	}

	//Device Not Found Error Alert
	private void ConfigurationErrorDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//canceling progressbar
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						//indicating dialog disappeared from screen 
						dialog.dismiss();

					}
				});

				dialog.show();
			}
		});
	}

	//Device Configured Alert
	void ConfigurationSuccessDialog(final String devName)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//cancelling progressbar
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Configuration Completed"); 
				dialog.setMessage(DEVICE_FULL_NAME+" Panel Configured Successfully.");
				dialog.setMessage(DEVICE_FULL_NAME+" Panel Configured Successfully.");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Home", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(WirelessDeviceConfiguration.this,Main_Navigation_Activity.class);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();

					}
				});

				dialog.setNeutralButton("Configure Settings", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(WirelessDeviceConfiguration.this,OperatorSettingsMain.class);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();	
					}
				});

				dialog.setPositiveButton("Add New Device", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(WirelessDeviceConfiguration.this,WirelessConfigurationDetails.class);
						it.putExtra("isAddNewDevice", true);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();	

					}
				});

				dialog.show();
			}
		});
	}	


	//fetching Full name of previously configured device
	private String previousDevicename(String devType){

		String previousname=null;
		try{
			Resources res = getResources();	//getting instance of resource xml

			String[] fullNames = res.getStringArray(R.array.Full_DevNames_Wireless);		// getting full name  array
			List<String> Full_DevNames_List = Arrays.asList(fullNames); 		// converting array to list

			String[] DeviceTypes = res.getStringArray(R.array.Device_Type_Wireless);	// getting device type array
			List<String> DeciceTypesList = Arrays.asList(DeviceTypes);			// converting array to list
			int index=DeciceTypesList.indexOf(devType);				// getting index of prviously configured device

			previousname=Full_DevNames_List.get(index);	// getting full name of device based on  device type index

		}catch(Exception e){
			e.printStackTrace();
		}

		return previousname;	//returning full name of previously configured device 
	}

	//delay method
	private void delay(long time){
		try {
			Thread.sleep(time);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	//Bluetooth Connetion Task
	/*private class BluetoothConnect extends AsyncTask<String,Void, Boolean> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			ProgressDialog("Connecting To Configuration Tool..."); 	// Starting Progress Bar
		}

		@Override
		protected Boolean doInBackground(String... params) {
			boolean isBtConnected=false;
			if(!Bluetooth.btConnected){
				try{
					bluetooth=new Bluetooth(BtHandler);
					if(Bluetooth.btConnected){
						isBtConnected=true;
					}
				}catch(Exception e){
					e.printStackTrace();
				}
			}else{
				isBtConnected=true;
			}

			return isBtConnected;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result); 

			//canceling progressbar
			cancelDialog();

			if(result){
				bt_status.setImageResource(BT_ON);	//showing bluetooth is connected
				ProgressDialog("Searching Device...");
				//starting configuration process
				searchTimer();
			}else{
				if(Bluetooth.isBluetoothOff){
					//Showing Bluetooth off Alert if Mobile Bluetooth is off
					BluetoothOffErrorAlert(); 
				}else{
					// Showing Bluetooth not Connected Error Dialog if Bluetooth not Connected
					ErrorAlert();		
				}
			}
		}

	}	

	// Error Alert showing that Bluetooth is Off
	private void BluetoothOffErrorAlert() {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(WirelessDeviceConfiguration.this,AlertDialog.THEME_HOLO_DARK);
				dlg.setTitle("Bluetooth OFF");
				dlg.setMessage("Your Mobile's Bluetooth is Currently OFF.\nPlease Turn it ON and Try Again.");
				dlg.setCancelable(false);
				dlg.setIcon(android.R.drawable.ic_dialog_alert);

				dlg.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
					}
				});		

				AlertDialog d = dlg.create();
				d.show();
			}
		});
	}

	//Error Alert Showing Unable to Establish Bluetooth Connection
	private void ErrorAlert() {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() { 
				AlertDialog.Builder dlg = new AlertDialog.Builder(WirelessDeviceConfiguration.this,AlertDialog.THEME_HOLO_DARK);
				dlg.setTitle("Configuration Tool Not Connected");
				dlg.setMessage("Unable to Connect to Configuration Tool.Please Try Again.");
				dlg.setCancelable(false);
				dlg.setIcon(android.R.drawable.ic_dialog_alert);

				dlg.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,	int which) {
						dialog.dismiss();
					}
				});

				dlg.setPositiveButton("Try Again",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,	int which) {
						dialog.dismiss();
						// Again starting Bluetooth Connection Task 
						new BluetoothConnect().execute();
					}
				});		

				AlertDialog d = dlg.create();
				d.show();
			}
		});
	}	*/

	//Alert Showing that no calling bells are configured 
	private void noCLBconfiguredAlert(){
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Calling Bell Not Configured");
				dialog.setMessage("Please Configure Atleast One Calling Bell Before Configuring Calling Switch.");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(WirelessDeviceConfiguration.this,WirelessConfigurationDetails.class);
						it.putExtra("isAddNewDevice", false);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();
					}
				});

				dialog.show();
			}
		});
	}

	//cancel progress dialog
	void cancelDialog(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(pdlg!=null){
					pdlg.dismiss();	// Dismissing Progress Bar
					pdlg=null;
				}
			}
		});
	}

	//go back
	void goPrevious(){
		Intent it=new Intent(WirelessDeviceConfiguration.this,WirelessConfigurationDetails.class);
		startActivity(it);
		//adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();
	}

	//Back Press Event
	@Override
	public void onBackPressed() {
		goPrevious();
	}	

	@Override
	protected void onPause() { 
		super.onPause();
		unregisterReceiver(mUsbReceiver);
		unbindService(usbConnection);
		if(WirelessConfigurationAdapter.sdb!=null){
			if(HouseConfigurationAdapter.sdb.isOpen() && 
					WirelessConfigurationAdapter.sdb.isOpen()){
				houseDB.close();
				WhouseDB.close();

				StaticVariables.printLog("TAG","DB CLOSED ");
			}
		}

	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if(!WirelessConfigurationAdapter.sdb.isOpen()){
			WhouseDB.open();
		}
		if(!HouseConfigurationAdapter.sdb.isOpen()) {
			houseDB.open();
		}
		setFilters();  // Start listening notifications from UsbService
		startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
		/*if(!HouseConfigurationAdapter.sdb.isOpen() &&
				!WirelessConfigurationAdapter.sdb.isOpen()){
			houseDB.open();
			WhouseDB.open();

			StaticVariables.printLog("TAG","DB open ");
		}*/



	} 

	//Click Events
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.Configure :
		{    

			if(Tcp.tcpConnected){

				resetTimeout=false;
				searchtimeout=false;
				configTimeout=false;

				isSearchedCompleted=false;
				isResetCompleted=false;
				isConfigCompleted=false;

				//making config flag false
				ConfigFLAG=false;

				//showing progress bar
				ProgressDialog("Searching Device,waiting for Response....");

				//starting configuration
				searchTimer();

				/*if(Bluetooth.btConnected)
				{

					searchAttempt++;

					if(searchAttempt==3){
						//showing progress bar
						ProgressDialog("Resetting Configuration Tool,waiting for Response....");

						//starting resetting process
						resetTimer();

						searchAttempt--;
					}else{
						//showing progress bar
						ProgressDialog("Searching Device,waiting for Response....");

						//starting configuration
						searchTimer();
					}

				}else{
					//starting bluetooth connection if bluetooth is not connected
					new BluetoothConnect().execute();
				}*/

			}else{
				Toast.makeText(getApplicationContext(), 
						"Please Ensure that Server is Connected!",Toast.LENGTH_SHORT).show();
			} 

			break;	
		}
		case R.id.server_status:{
			if(!Tcp.tcpConnected){
				if(!isTcpConnecting){ 
					msg("connecting To Server...");
					RegainingConnection();

				}else{
					StaticVariables.printLog("TAG","already Connecting tcp");
				}
			}else{
				msg("server is already connected");
			}
			break;	
		}
		case R.id.imageView2:{
			goPrevious();
			break;
		}

		}

	}

	//showing toast message
	void msg(final String text){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), 
						text,Toast.LENGTH_SHORT).show();

			}
		});
	}

	//timer for searching sink of device
	void searchTimer(){
		StaticVariables.printLog("SEARCH TIMER","*********STARTING SEARCH TIMER*******************");

		cmdCount = 10;
		//bluetooth.btWrite(ATI);
		usbService.write(ATI.getBytes());
		searchTimerHandler=new Handler(); 
		searchTimerRunnable=new Runnable() { 
			@Override
			public void run() { 
				if(!isSearchedCompleted){ 

					isSearchedCompleted=false;
					searchtimeout=true;
					cmdCount=15;

					ConfigurationErrorDialog("Device Search Timeout","Unable to Search "+DEVICE_FULL_NAME+" Device.Try Configuring Again.");
					
					StaticVariables.printLog("SEARCH TIMER","***********TIMEOUT SEARCH TIMER*************");

				}else{ 
					StaticVariables.printLog("CONFIG","#######Sink Already Searched######");
				}

			}
		};

		//25 sec. time to search configuration module
		searchTimerHandler.postDelayed(searchTimerRunnable,25000);

	}

	//timer for final configuration response
	void mainConfigurationTimer(){
		StaticVariables.printLog("CONFIG TIMER","********STARTING CONFIG TIMER*********");

		configTimerHandler=new Handler(); 
		configTimerRunnable=new Runnable() { 
			@Override
			public void run() { 
				if(!isConfigCompleted){ 

					configTimeout=true;
					cmdCount=15;

					ConfigurationErrorDialog("Configuration Failed","Unable to Configure "+DEVICE_FULL_NAME+" Device.Try Again.");

					StaticVariables.printLog("Config TIMER","*********TIMEOUT CONFIG TIMER*****************");
				}else{ 
					StaticVariables.printLog("CONFIG","#######device already configured#######");
				}

			}
		};

		//15 sec. time to reset configuration module
		configTimerHandler.postDelayed(configTimerRunnable,15000);

	}

	//reset timer
	void resetTimer(){

		StaticVariables.printLog("RESET TIMER","******STARTING RESET TIMER*************");

		cmdCount = 20;
		//bluetooth.btWrite(ATF);
		usbService.write(ATF.getBytes());
		resetTimerHandler=new Handler(); 
		resetTimerRunnable=new Runnable() { 
			@Override
			public void run() { 
				if(!isResetCompleted){  

					resetTimeout=true;
					cmdCount=15;

					ConfigurationErrorDialog("Configurtaion Tool Reset Failed","Unable to Reset Configuration Tool.Try Configuring Again.");
					 
					StaticVariables.printLog("Reset TIMER","********TIMEOUT RESET TIMER*************");

				}else{ 
					searchAttempt=0;
					StaticVariables.printLog("reset","######configuration module already resetted######");
				}

			}
		};

		//15 sec. time to reset configuration module
		resetTimerHandler.postDelayed(resetTimerRunnable,15000);

	}

	//Device Not Found Error Alert
	private void responseErrorDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//cancel dialog
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

					}
				});

				dialog.show();
			}
		});
	}


	//Wrong Configuration Device type Error Alert
	private void deviceTypeErrorDialog(final String title,final String msg,final String devType)
	{
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//cancel dialog
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();

					}
				});

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent intent=new Intent(WirelessDeviceConfiguration.this,WirelessConfigurationDetails.class);
						intent.putExtra("CONFIG_DEVICE_TYPE", devType);
						startActivity(intent);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();

					}
				});

				dialog.show();
			}
		});
	}





	private void startService(Class<?> service, ServiceConnection serviceConnection, Bundle extras) {
		if (!UsbService.SERVICE_CONNECTED) {
			Intent startService = new Intent(this, service);
			if (extras != null && !extras.isEmpty()) {
				Set<String> keys = extras.keySet();
				for (String key : keys) {
					String extra = extras.getString(key);
					startService.putExtra(key, extra);
				}
			}
			startService(startService);
		}
		Intent bindingIntent = new Intent(this, service);
		bindService(bindingIntent, serviceConnection, Context.BIND_AUTO_CREATE);
	}

	private void setFilters() {
		IntentFilter filter = new IntentFilter();
		filter.addAction(UsbService.ACTION_USB_PERMISSION_GRANTED);
		filter.addAction(UsbService.ACTION_NO_USB);
		filter.addAction(UsbService.ACTION_USB_DISCONNECTED);
		filter.addAction(UsbService.ACTION_USB_NOT_SUPPORTED);
		filter.addAction(UsbService.ACTION_USB_PERMISSION_NOT_GRANTED);
		registerReceiver(mUsbReceiver, filter);
	}

	public void popup(String msg){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

		alertDialogBuilder.setTitle("INFO");
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {

						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void Update_db_version_number(){
		if(StaticVariabes_div.housename!=null) {
			try{
			ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
			sdadap = new ServerDetailsAdapter(WirelessDeviceConfiguration.this);
			sdadap.open();
			Cursor cursrno = sdadap.fetchrefno(1);
			String home_versionnumber= cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_db));
			cursrno.close();
			Float fvernum=Float.valueOf(home_versionnumber)+0.1f;
				String s_vernum = String.format("%.2f", fvernum);
			sdadap.update_versionnumber(s_vernum);
			sdadap.close();
		}catch(Exception e){

		}
		}
	}
}