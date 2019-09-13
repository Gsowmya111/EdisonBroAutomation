package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: DeviceConfiguration.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to configure  wired devices
 *
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

public class DeviceConfiguration extends Activity implements OnClickListener{
	TextView deviceInfo;
	Button configure,b_home;
	ImageView bt_status,serverStatus,navigateBack,img_config;
	ProgressDialog pdlg;
	LocalListArrangeTable locallist_adap;

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

	String ABORT="%ABORT#";

	public static int cmdCount = 0;
	String DeviceName=null,RoomName=null,Bluetooth_ID = null,CordinatorJPAN=null,
			CordintorID=null,DeviceID=null,DEVICE_FULL_NAME=null;
	int DeviceNumber=0 ,RoomNo=0;
	String switchCount=null, fanCount=null ,socketcount=null,DeviceType=null ;

	boolean exceptionOccured=false ,IS_SERVER_NOT_AUNTHETICATED=false;
	final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5,ERR_USER=7,MAX_USER=8;

	//Hard coded value for  CLS device Type
	final String CLS_TYPE="040";
	final String CLS_NAME="CSW1";
	long CLB_Counts=0;
	int CLB_DevID_INDEX=0;

    //value for device Type
	final String CUR_Type="051";
	final String CUR_Name="CLSH";
	long Cur_Counts=0;
	int Cur_DevID_INDEX=0;

	String DevIdList[] =null,bellsDevInfo[]=null;
	String curDevNameList[] =null;

	HouseConfigurationAdapter houseDB=null;
	LocalDatabaseAdapter localDB=null;
	//Bluetooth bluetooth;
	Tcp tcp=null;
	boolean isDevSwb=false, isDialogOpened=false,  isDevSwd=false;	
	boolean IS_SERVER_CONNECTED=false ;

	String DEVICE_TYPE_TEXT=null,DeviceGroupId="000";

	static boolean isSearchedCompleted=false,isResetCompleted=false,isConfigCompleted
			,resetTimeout=false,searchtimeout=false,configTimeout=false,isDeviceResponseTimeout=false;

	static int searchAttempt=0;

	Handler searchTimerHandler=null; 
	Runnable searchTimerRunnable=null;

	Handler configTimerHandler=null;
	Runnable configTimerRunnable=null;

	Handler resetTimerHandler=null;
	Runnable resetTimerRunnable=null;

	Handler responseWaitTimerHandler=null; 
	Runnable responseWaitTimerRunnable=null;

	static boolean isTcpConnecting=false ;

	String MUD_DevType="WMD1" ,MUC_DevType="WMC1",MUB_DevType="WMB1",MBSO_DevType="WBSO"
			,BM_DevType="WBM1" ,SM_DevType="WSM1",
			SS_DevType="WSS1",OTS_DevType="WOTS";


	ArrayList<String> finalDeviceIdList=new ArrayList<String>(); 
	List<String> Full_DevNames_List;
	List<String> Device_Type_List;

	boolean ConfigFLAG=false;

	String RoomIconName , UserDeviceName;
	String current_room_image_name=null;
	//matched_room_number

	private static final String TAG1="DeviceConfig - ";

	boolean isCurtainselected=false;
	String Curtainselected="";
	boolean Usb_device_connected=false;
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

		//setContentView(R.layout.deviceconfiguration);
		setContentView(R.layout.device_configuration_layout);

		serverStatus=(ImageView) findViewById(R.id.server_status);
		bt_status=(ImageView) findViewById(R.id.bt_status);
		deviceInfo=(TextView) findViewById(R.id.Device_Info);
		configure=(Button) findViewById(R.id.Configure);
		navigateBack=(ImageView) findViewById(R.id.imageView2);
		img_config=(ImageView) findViewById(R.id.img_remotekeys);

		Tcp.tcpHost= Tcp_con.tcpAddress;
		Tcp.tcpPort= Tcp_con.tcpPort;


		mHandler = new MyHandler(this);

		// Setting on click listener
		configure.setOnClickListener(this); 
		serverStatus.setOnClickListener(this); 
		navigateBack.setOnClickListener(this);

		b_home= (Button) findViewById(R.id.btnhome);
		b_home.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intnt=new Intent(DeviceConfiguration.this, Main_Navigation_Activity.class);
				startActivity(intnt);
				finish();
			}
		});

		FullDevNameList();				// Assigning Full names For Devices 
		DevTypeList();					// Assigning Device Types For Devices


		try{
			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();			//opening house database	

			localDB=new LocalDatabaseAdapter(this);
			localDB.opendb();

		}catch(Exception e){
			e.printStackTrace();
		}		

/*		if(Bluetooth.btConnected){
			//passing bluetooth handler instance
			bluetooth=new Bluetooth(BtHandler);
			bt_status.setImageResource(BT_ON);
		}*/



		//Getting Values From Intent
		Intent DataPacket=getIntent();
		if(DataPacket!=null){
			DeviceType=DataPacket.getStringExtra("devType");
			DeviceName=DataPacket.getStringExtra("devName");
			RoomName=DataPacket.getStringExtra("roomName");
			DEVICE_FULL_NAME=DataPacket.getStringExtra("devFullName");
			DeviceGroupId=DataPacket.getStringExtra("groupId");
			RoomIconName=DataPacket.getStringExtra("roomiconnam");
			UserDeviceName=DataPacket.getStringExtra("usrdevnam");

		}

		StaticVariables.printLog("DEVICE CONFIG 1 :","DeviceName :"+DeviceName+" groupId :"+DeviceGroupId);

		StaticVariabes_div.log("icnnam  "+RoomIconName,TAG1);
		StaticVariabes_div.log("UserDeviceName  "+UserDeviceName,TAG1);

		//enabling configuration button
		configure.setEnabled(true);

		//setting current room as static field
		StaticVariables.CurrentConfigurationRoom=RoomName;

		//Fetching Max Device Number From DataBase
		DeviceNumber=houseDB.getUniqueDeviceNo();	 

		StaticVariables.printLog("DEV INFO :","DeviceNo :"+DeviceNumber);

		//Fetching coordinator Device ID from Database
		CordintorID=houseDB.CordinatorID();	


		//Fetching coordinator Device JPAN from Database
		CordinatorJPAN=houseDB.CordinatorJPAN();


		//getting room number for current room
		RoomNo=setCurrentRoomNumber(RoomName);
		if(RoomNo>99){
			configure.setEnabled(false);
			msg("Room Numbers Length Exceeded.Please Choose old room name."); 
		}


		//calculating number of calling bells already configured before starting configuration for calling switch 
		CLB_Counts=houseDB.CountCallingBells();



		if(DeviceName.equals(CUR_Name)){
			Cur_Counts=houseDB.CountCurtain_Normal(""+RoomNo);

			if(Cur_Counts<1){

				//calling alert showing that presently no calling bell configured
				noMasterAlert("Curtain Normal Not Configured","Please Configure Atleast One Curtain Before Configuring Curtain Sheer.");

			}else{

				//Creating CUR Device ID's Array having size equals to Cur counts
				curDevNameList = new String[(int)Cur_Counts];


				//getting cur device names
				curDevNameList = houseDB.Curtain_Normal_DeviceName((int)Cur_Counts,""+RoomNo);

				String curtnorm_free=null;

				ArrayList<String> listnew=new ArrayList<String>();

				for(int m=0;m<curDevNameList.length;m++){
                 String test=curDevNameList[m]+"SH";

					curtnorm_free=houseDB.Curtain_Free_Normal(test);


					if(curtnorm_free!=null){


					}else{

						listnew.add(curDevNameList[m]);
					}

				}

               String arry_cur_norm[]=listnew.toArray(new String[listnew.size()]);

				//showing bell selection id
				selectCurtainListDialog(arry_cur_norm);
			}
		} else
		//checking if selected device is Calling Switch		
		if(DeviceName.equals(CLS_NAME)){
			if(CLB_Counts<1){
				//calling alert showing that presently no calling bell configured
				noMasterAlert("Calling Bell Not Configured","Please Configure Atleast One Calling Bell Before Configuring Calling Switch.");

			}else{
				//Creating CLB Device ID's Array having size equals to CLB counts
				DevIdList = new String[(int)CLB_Counts];
				bellsDevInfo= new String[(int)CLB_Counts];

				//getting bells info
				bellsDevInfo=houseDB.callingBells_Info(); 

				//getting bell device ids
				DevIdList = houseDB.callingBells_DeviceID((int)CLB_Counts);

				//showing bell selection id
				selectBellListDialog(bellsDevInfo);
			}
		}else if(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType) 
				||DeviceName.equals(SS_DevType) || DeviceName.equals(OTS_DevType)){


			//check if master unit is searched or not
			boolean isConfigured=houseDB.isMasterDeviceConfigured();

			if(!isConfigured){

				//error alert to search master unit first
				noMasterAlert("No Master Unit Available","Please Search Water Master Unit Before Configuring Motor Or Sensor Device.");

			} 

		}

	/*	try{
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
		}*/


		//Setting Text For Particular Device From Resources
		String DeviceText=DeviceConfigText(DeviceName);
		//Setting configuration information for device
		deviceInfo.setText(DeviceText);



		//resetting search attempt
		searchAttempt=0;

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

		//calling connection in activity
		RegainingConnection();
		
	}

	//method to get auto TCP connection in class
	void RegainingConnection(){
		//starting a background thread to make connection
		Thread thread =new Thread(){
			public void run(){
				isTcpConnecting=true;
				IS_SERVER_CONNECTED=Tcp.EstablishConnection(TcpHandler);
				runOnUiThread(new Runnable() {
					public void run() 
					{
						if(IS_SERVER_CONNECTED){

							IS_SERVER_CONNECTED=false; 

							if(!IS_SERVER_NOT_AUNTHETICATED){

								IS_SERVER_NOT_AUNTHETICATED=false;
								serverStatus.setImageResource(server_online);  
								StaticVariables.printLog("TAG","CONNECTED");

							}else{

								IS_SERVER_NOT_AUNTHETICATED=false;

								serverStatus.setImageResource(server_offline);	

								StaticVariables.printLog("TAG","NOT CONNECTED");

							}

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

	//method to set room number  i.e if room exists then taking old room number and if 
	//room not exists then giving incremented room number
	private int setCurrentRoomNumber(String roomName){
		int currentRoomNo=0;
		int matched_room_number=0;


		String matched_room_image_name=null;
		try{
			ArrayList<String> UniqueRoomList=new ArrayList<String>();
			TreeSet<String> rooms=new TreeSet<String>(); 
			try{
				rooms=houseDB.RoomNameList();
				//Fetching list of All room names from database and adding to local array list
				if(rooms.size()>0)
					UniqueRoomList.addAll(rooms);
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

						StaticVariables.printLog("DB ROOM NUMBER",matched_room_image_name+"Room Matched with db and Room No:"+matched_room_number);
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


		}catch(Exception e){
			e.printStackTrace();
		}

		return currentRoomNo;
	}

	//method to set switch configuration text and switch& fan count of selected switch type
	String setSwitchDetails(int switches,int fans){
		switchCount=""+switches;
		fanCount=""+fans;
		isDevSwb=true;
		DEVICE_TYPE_TEXT="SWB";
		Resources res = getResources();
		return res.getString(R.string.SWB_Details);
	}
	//method to set switch configuration text ,switch socket and switch& fan count of selected switch type
	String setSwitchDetails(int switches,int fans,int socket){
		switchCount=""+switches;
		fanCount=""+fans;
		socketcount=""+socket;
		isDevSwb=true;
		DEVICE_TYPE_TEXT="SWB";
		Resources res = getResources();
		return res.getString(R.string.SWB_Details);
	}

	//method to set switch dimmer configuration text and switch& fan count of selected switch type

	String setSwitchdimmerDetails(int switches,int fans){
		switchCount=""+switches;
		fanCount=""+fans;
		isDevSwd=true;
		DEVICE_TYPE_TEXT="SWD";
		Resources res = getResources();
		return res.getString(R.string.SWD_Details);
	}
	//method to set default text for device
	String setDefaultText(String device){
		DEVICE_TYPE_TEXT=device;
		configure.setEnabled(false);
		Resources res = getResources();
		return 	res.getString(R.string.Default_Details);

	}

	//Device Configuration Information For user
	private String DeviceConfigText(String device){
		String text="";
		Resources res = getResources();
		//assigning value for text variable depends upon device selected

		switch(deviceNames.valueOf(device)){

			case S010:
				text=setSwitchDetails(1,0);
				break;
			case S020:
				text=setSwitchDetails(2,0);
				break;
			case S030:
				text=setSwitchDetails(3,0);
				break;
			case S040:
				text=setSwitchDetails(4,0);
				break;
			case S060:
				text=setSwitchDetails(6,0);
				break;
			case S080:
				text=setSwitchDetails(8,0);
				break;
			case S120:
				text=setSwitchDetails(2,0);
				break;
			case S021:
				text=setSwitchDetails(2,1);
				break;
			case S031:
				text=setSwitchDetails(3,1);
				break;
			case S042:
				text=setSwitchDetails(4,2);
				break;
			case S051:
				text=setSwitchDetails(5,1);
				break;
			case S052:
				text=setSwitchDetails(5,2);
				break;
			case S061:
				text=setSwitchDetails(6,1);
				break;
			case S062:
				text=setSwitchDetails(6,2);
				break;
			case S071:
				text=setSwitchDetails(7,1);
				break;
			case S102:
				text=setSwitchDetails(10,2);
				break;
			case S111:
				text=setSwitchDetails(11,1);
				break;
			case S141:
				text=setSwitchDetails(4,1,1);
				break;
			case S110:
				text=setSwitchDetails(1,0,1);
				break;
			case S160:
				text=setSwitchDetails(6,0,1);
				break;
			case SLT1:
				text=setSwitchDetails(1,0);
				break;
			case SFN1:
				DEVICE_TYPE_TEXT="FAN";
				text=res.getString(R.string.Normal_Details);
				break;
			case DFN1:
				DEVICE_TYPE_TEXT="FAN";
				text=res.getString(R.string.Normal_Details);
				break;
			case DMR1:
				DEVICE_TYPE_TEXT="DMR";
				text=res.getString(R.string.DMR_Details);
				img_config.setImageResource(R.drawable.remote_config_keys_dmr);

				break;
			case RGB1:
				DEVICE_TYPE_TEXT="RGB";
				text=res.getString(R.string.RGB_Details);
				img_config.setImageResource(R.drawable.remote_config_keys_rgb);
				break;
			case ACR1:
				DEVICE_TYPE_TEXT="ACR";
				text=res.getString(R.string.Normal_Details);
				break;
			case WOTS:case WMD1:case WMC1:case WMB1:case WBSO:case WSS1:case WBM1:case WSM1:
				DEVICE_TYPE_TEXT="WPC";
				text=res.getString(R.string.WPC_Details);
				break;

			case GSR1:
				DEVICE_TYPE_TEXT="GSR";
				text=res.getString(R.string.Normal_Details);
				break;

			case CLS1: case CLD1: case CRD1:
				DEVICE_TYPE_TEXT="CUR";
				text=res.getString(R.string.CUR_Details);
				img_config.setImageResource(R.drawable.remote_config_keys_cls);
				break;

			    case CRS1:
				DEVICE_TYPE_TEXT="CUR";
				text=res.getString(R.string.CUR_Details);
				img_config.setImageResource(R.drawable.remote_config_keys_crs);
				break;

				case RPR1:
					DEVICE_TYPE_TEXT="RPR";
					text=res.getString(R.string.RPR_Details);
					break;

				case CLB1 :
					DEVICE_TYPE_TEXT="CLB";
					text=res.getString(R.string.Normal_Details);
					break;

				case CSW1:
					DEVICE_TYPE_TEXT="CSW";
					text=res.getString(R.string.Normal_Details);
					break;

				case EGM1:
					DEVICE_TYPE_TEXT="EGM";
					text=res.getString(R.string.Normal_Details);
					break;
				case PSC1 :
					DEVICE_TYPE_TEXT="PSC";
					text=res.getString(R.string.Normal_Details);
					break;
			case SOSH :
				DEVICE_TYPE_TEXT="SOSH";
				text=res.getString(R.string.Normal_Details);
				break;
			case SWG1 :
				DEVICE_TYPE_TEXT="SWG1";
				text=res.getString(R.string.Normal_Details);
				break;
			case SLG1 :
				DEVICE_TYPE_TEXT="SLG1";
				text=res.getString(R.string.Normal_Details);
				break;

				case PLC1 :
					DEVICE_TYPE_TEXT="PLC";
					text=res.getString(R.string.Normal_Details);
					break;
				case GSK1 :
					DEVICE_TYPE_TEXT="GSK";
					text=res.getString(R.string.Normal_Details);
					break;
				case DLS1:
					DEVICE_TYPE_TEXT="DLS";
					text=res.getString(R.string.Normal_Details);
					break;
				case FMD1 :
					DEVICE_TYPE_TEXT="FMD";
					text=res.getString(R.string.Normal_Details);
					break;
				case AQU1 :
					DEVICE_TYPE_TEXT="AQU";
					text=res.getString(R.string.Normal_Details);
					break;

			     case SDG1 :
					DEVICE_TYPE_TEXT="SDG";
					text=res.getString(R.string.Normal_Details);
					break;

				    case SWD1 :
					DEVICE_TYPE_TEXT="SWD";
					text=setSwitchdimmerDetails(5,0);
					break;

					case CLNR :
						DEVICE_TYPE_TEXT="CUR";
						text=res.getString(R.string.CUR_Details);
						img_config.setImageResource(R.drawable.remote_config_keys_clnr);

						break;

					case CLSH :
						DEVICE_TYPE_TEXT="CUR";
						text=res.getString(R.string.CUR_Details);
						img_config.setImageResource(R.drawable.remote_config_keys_clsh);

						break;
		default:
			text=res.getString(R.string.Default_Details);
			configure.setEnabled(false);
			break;

			//still to add description of devices
		}

		return text;
	}
	//device types
	private enum deviceNames{
		S010,S020,S030,S040,S060,S080,S021,S031,S042,S051,S052,S061,
		S062,S071,S102,S111,S141,S120,S110,S160,SWG1,SLG1,SLT1,SFN1,DFN1,DMR1,RGB1,ACR1,WOTS,WMD1,WMC1,WMB1,WBSO,WSS1,WBM1,
		WSM1,GSR1,DLS1,CLS1,CLD1,CRS1,CRD1,IRB1,RPR1,CLB1,CSW1,EGM1,PSC1,SOSH,PLC1,GSK1,FMD1,AQU1,SDG1,SWD1,CLNR,CLSH,
	};

	//making data format that we have send to configuration device
	private String DataToSend(){
		String DataToSend=null;
		try{
			// Making A String With Desire format i.e 
			/*
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

			//checking if device type is of calling switch
			if(DEV_TYPE.equals(CLS_TYPE)){

				/*
				 * "@"+DeviceType(3char)+DeviceNumber(4char)+
					BellCount(3char)+RoomNo(2char)+DevicID(16char)+"$"
				 */ 
				String BELL_COUNTS=""+finalDeviceIdList.size();
				//Making calling bell count of length three char if required
				while(BELL_COUNTS.length()<3){		   
					BELL_COUNTS="0"+BELL_COUNTS;
				}

				//Checking all parameters and making final string of length 30char
				if(DEV_TYPE.length()==3 && DEV_NUMBER.length()==4 
						&& BELL_COUNTS.length()==3 && ROOM_NO.length()==2  && DEV_ID.length()==16){
					DataToSend="@"+DEV_TYPE+DEV_NUMBER+BELL_COUNTS+ROOM_NO+DEV_ID+"$";
				}

				StaticVariables.printLog("CLS","caling switch data to send :"+DataToSend);
			}else{

				//Checking all parameters and making final string of length 30char
				if(DEV_TYPE.length()==3 && DEV_NUMBER.length()==4 
						&& DeviceGroupId.length()==3 && ROOM_NO.length()==2  && DEV_ID.length()==16){
					DataToSend="@"+DEV_TYPE+DEV_NUMBER+DeviceGroupId+ROOM_NO+DEV_ID+"$";
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

			//Making Final Command Format
			if(DeviceID.length()==16 && data_to_send.length()==30 ){
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
			case ERR_USER:
			{
				runOnUiThread(new Runnable() {
					public void run() 
					{
						IS_SERVER_NOT_AUNTHETICATED=true;
						serverStatus.setImageResource(server_offline);
						TCPErrorAlert("Aunthetication Failed","Invalid UserName/Password.Please check and try again.");
					}
				});
				break;
			}
			case MAX_USER:
			{
				runOnUiThread(new Runnable() {
					public void run() 
					{
						IS_SERVER_NOT_AUNTHETICATED=true;
						serverStatus.setImageResource(server_offline);	
						TCPErrorAlert("Server Error","Server connections Limit Exceeded .Please disconnect some other client to connect with server.");

					}
				});
				break;
			}
			}
		}
	};

	// Error Alert showing that Bluetooth is Off
	private void TCPErrorAlert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(DeviceConfiguration.this,AlertDialog.THEME_HOLO_LIGHT);
				dlg.setTitle(title);
				dlg.setMessage(msg);
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




	// Filling Device Name List with Full Names which will Visible to End User
	private void FullDevNameList(){

		Full_DevNames_List=new ArrayList<String>();

		Resources res = getResources();
		String[] fullNames = res.getStringArray(R.array.Full_DevNames);
		Full_DevNames_List .addAll(Arrays.asList(fullNames));

		//adding select option item in list
		Full_DevNames_List.add("Select Device");


	}


	// Filling Device Type Which Will be used Internally 
	private void DevTypeList(){
		Resources res = getResources();
		String[] devType = res.getStringArray(R.array.Device_Type);
		Device_Type_List = Arrays.asList(devType);

	}




	 //creating Bluetooth Handler to handle Bluetooth Socket Data
	 public class MyHandler extends Handler {
		 private final WeakReference<DeviceConfiguration> mActivity;

		 public MyHandler(DeviceConfiguration activity) {
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
					StaticVariables.printLog("response", DataGet.length()+" len data :   "+DataGet);
					if(ConfigFLAG) {
						StaticVariables.printLog("ConfigFLAG", "true :" + DataGet);
					}
					if(!configTimeout){
						try{

							if(DataGet!=null && DataGet.contains("*ERROR,") && DataGet.endsWith("#")){


								StaticVariables.printLog("data", "1");
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

								StaticVariables.printLog("data", "2");
								String DATA=null;
								//sending coordinator jpan id
								String datatoSend="@"+CordinatorJPAN+"$";

								//checking if jpan id is not null
								if(datatoSend!=null && datatoSend.length()>0){
									DATA = "AT+UCAST:" + DeviceID + "="+datatoSend + "\r\n";

									updateProgressStatus("Configuring Device...",70);

									//sending data to device
									//bluetooth.btWrite(DATA);
									usbService.write(DATA.getBytes());
									StaticVariables.printLog("response sent after 70", "data :"+DATA);

								}else{
									StaticVariables.printLog("TAG","Invalid jpan id");
									break;
								}

								//making config flag true
								ConfigFLAG=true;

								//breaking further processing
								break;
							}
							else if(ConfigFLAG && DataGet!=null && DataGet.contains("UCAST") && DataGet.length() == 35)

							//if (ConfigFLAG && DataGet!=null && DataGet.startsWith("UCAST") && DataGet.endsWith("#")
								//	&& DataGet.length() == 35 && (!DataGet.contains(",OK#")))
							{
								StaticVariables.printLog("data three", "3");
								StaticVariables.printLog("RESPONSE a", "data length : " + DataGet.length());
								String Res_type = DataGet.substring(31, DataGet.length()-1);
								StaticVariables.printLog("RESPONSE b", "Device type recievd : " + Res_type);
								if(DeviceType.equals(CLS_TYPE)){
									ResponseDialog(Res_type);
								}else{
									ResponseDialog(Res_type);
								}


								//breaking further processing
								break;

							}  else if (ConfigFLAG && DataGet!=null && DataGet.contains(",OK#") || DataGet.endsWith("*OK#") )
							//Handling Calling Switch Responses
							{
								StaticVariables.printLog("data", "4");
								//filtering desired data from string
								String Res_type = DataGet.substring(26);

								StaticVariables.printLog("TAG","Response cls :"+Res_type);

								//invoking method to send response to calling switch
								calling_switch_Response(Res_type);

								//breaking further processing
								break;
							}


							StaticVariables.printLog("TAG","DeviceName :"+DeviceName+" tout:"+configTimeout);

							if(ConfigFLAG && DataGet.contains("@OK$") &&
									(DeviceName.equals(MUD_DevType)||
											DeviceName.equals(MUC_DevType)||
											DeviceName.equals(MUB_DevType)||
											DeviceName.equals(MBSO_DevType)))//"@OK$"
							{
								StaticVariables.printLog("data", "5");
								updateProgressStatus("Configuring Device...", 80);
								String DataToSend=null;
								//response from master unit.
								//now sending moror's id to master unit
								try{
									Cursor mcursor1=null,mcursor2=null;
									String motorID1=null,motorID2=null;

									mcursor1=houseDB.getDeviceTableDetails(SM_DevType);

									if(mcursor1!=null && mcursor1.getCount()>0){
										motorID1=mcursor1.getString(
												mcursor1.getColumnIndex(
														HouseConfigurationAdapter.WPC_DEV_ID));

									}

									//closing cursor
									if(mcursor1!=null){
										mcursor1.close();
									}

									mcursor2=houseDB.getDeviceTableDetails(BM_DevType);

									if(mcursor2!=null && mcursor2.getCount()>0){
										motorID2=mcursor2.getString(
												mcursor2.getColumnIndex(
														HouseConfigurationAdapter.WPC_DEV_ID));

									}

									//closing cursor
									if(mcursor2!=null){
										mcursor2.close();
									}

									//getting id's
									if(motorID1!=null && motorID2!=null ){
										//if both motors are there then setting first SUMP id and then Borewell id
										//sequence should not change as order is fixed at embedded side also
										DataToSend="@"+motorID1+motorID2+"$";
									}else{
										//if any one motor is there
										if(motorID1!=null){
											//adding 16 zeros for second motor id2 as no second motor to configure
											motorID2="0000000000000000";

											DataToSend="@"+motorID1+motorID2+"$";
										}else if(motorID2!=null){
											//adding 16 zeros for second motor id1 as no second motor to configure
											motorID1="0000000000000000";

											DataToSend="@"+motorID1+motorID2+"$";
										}
									}


									//sending data to master unit
									String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n";

									//giving delay of 2 seconds
									delay(2000);

									//sending data to device
									//bluetooth.btWrite(DATA);
									usbService.write(DATA.getBytes());
									StaticVariables.printLog("sent ", "data :"+DATA);

								}catch(Exception e){
									e.printStackTrace();
								}

								break;

							}else if(ConfigFLAG && DataGet.contains("@OK$") &&
									(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType))){
								StaticVariables.printLog("data", "6");
								updateProgressStatus("Configuring Device...", 80);
								//response from water motors.
								//now sending master unit id
								try{
									Cursor mcursor=null;
									String masterID=null;

									mcursor=houseDB.getMasterUnitDetails();

									if(mcursor!=null && mcursor.getCount()>0){
										masterID=mcursor.getString(
												mcursor.getColumnIndex(
														HouseConfigurationAdapter.WPC_DEV_ID));

									}
									//closing cursor
									if(mcursor!=null){
										mcursor.close();
									}

									if(masterID!=null && masterID.length()==16){
										//sending data to motor
										//i.e "@"+Master Unit ID(16chars)+"$"
										String DataToSend="@"+masterID+"$";
										String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n";

										//giving delay of 1 seconds
										delay(1000);

										//bluetooth.btWrite(DATA);
										usbService.write(DATA.getBytes());
										StaticVariables.printLog("sent ", "data :"+DATA);
									} else{
										msg("Error in Master Unit Data");
									}
								}catch(Exception e){
									e.printStackTrace();
								}

								break;
							}
							else if(ConfigFLAG && DataGet.contains("@OK$") &&
									(DeviceName.equals(SS_DevType) || DeviceName.equals(OTS_DevType))){

								StaticVariables.printLog("data", "7");
								updateProgressStatus("Configuring Device...", 80);
								//response from water motors.
								//now sending master unit id
								try{
									Cursor mcursor=null;
									String masterID=null;

									mcursor=houseDB.getMasterUnitDetails();

									if(mcursor!=null && mcursor.getCount()>0){
										masterID=mcursor.getString(
												mcursor.getColumnIndex(
														HouseConfigurationAdapter.WPC_DEV_ID));

									}

									//closing cursor
									if(mcursor!=null){
										mcursor.close();
									}

									if(masterID!=null && masterID.length()==16){
										//sending data to motor
										//i.e "@"+Master Unit ID(16chars)+"$"
										String DataToSend="@"+masterID+"$";
										String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n";

										//giving delay of 1 seconds
										delay(1000);

										//sending data to device
										//bluetooth.btWrite(DATA);
										usbService.write(DATA.getBytes());
										StaticVariables.printLog("sent ", "data :"+DATA);
									} else{
										msg("Error in Master Unit Data");
									}
								}catch(Exception e){
									e.printStackTrace();
								}

								break;
							}

							else if(!isDeviceResponseTimeout && DataGet.contains("#OK*") &&
									(DeviceName.equals(MUD_DevType)||
											DeviceName.equals(MUC_DevType)||
											DeviceName.equals(MUB_DevType)||
											DeviceName.equals(MBSO_DevType))){

								StaticVariables.printLog("data", "8");
								//cancel response wait timer
								responseWaitTimerHandler.removeCallbacks(responseWaitTimerRunnable);

								//insert or update  motor unit id in local water pump table
								boolean isAdded=houseDB.insertOrUpdateMasterUnit(DeviceName,
										DeviceType, DeviceID );

								if(isAdded){
									//prompt to search at least one motor
									NoMotorDialog("Configuration Aborted","No Water motors are available to configure.\nPlease search atleast one water motor.");

								}else{
									errorDialog();
								}

								break;

							}else if(!isDeviceResponseTimeout && DataGet.contains("#OK*") &&
									(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType)
											||(DeviceName.equals(SS_DevType) || DeviceName.equals(OTS_DevType)))){
								StaticVariables.printLog("data", "9");

								//cancel response wait timer
								responseWaitTimerHandler.removeCallbacks(responseWaitTimerRunnable);

								NoMotorDialog("Configuration Aborted","No Master Unit available to control motors.\nPlease search Water Master Unit First.");

								break;

							}

						}catch(Exception e){
							e.printStackTrace();
						}


					}
					else{
						StaticVariables.printLog("TAG","already configuration timeout occured");
					}


					StaticVariables.printLog("BT RESPONSE","cmd count :"+cmdCount);
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
									StaticVariables.printLog("sent 20", "data :"+ECO);
									updateProgressStatus("Resetting Configuration Tool...",20);

								} else if (DataGet.contains("ERROR")) {

									cmdCount = 20;
									DataGet = "";
									//bluetooth.btWrite(ATF);
									usbService.write(ATF.getBytes());
									StaticVariables.printLog("sent after 20", "data :"+ATF);

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
									StaticVariables.printLog("sent after 40", "data :"+Link);

								} else if (DataGet.contains("ERROR")) {
									cmdCount = 21;
									DataGet = "";
									//bluetooth.btWrite(ECO);
									usbService.write(ECO.getBytes());
									StaticVariables.printLog("sent after 40", "data :"+ECO);
								}
								break;

							}
							case 22: {

								if 	(DataGet.contains("OK")) {
									cmdCount = 23;
									DataGet = "";
									//bluetooth.btWrite(Security);
									usbService.write(Security.getBytes());
									StaticVariables.printLog("sent before 60", "data :"+Security);
									updateProgressStatus("Resetting Configuration Tool...",60);
								} else if (DataGet.contains("ERROR")) {
									cmdCount = 22;
									DataGet = "";
									//bluetooth.btWrite(Link);
									usbService.write(Link.getBytes());
									StaticVariables.printLog("sent after 60", "data :"+Link);

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
									StaticVariables.printLog("sent ", "data :"+Security);
								}
								break;
							}



							case 10: {
								if (DataGet.contains("OK")) {

									updateProgressStatus("Searching Device...",20);

									cmdCount = 11; // Setting Command Count to 11
									DataGet = ""; // Clearing DataGet variable
									//bluetooth.btWrite(DPAN); // Sending DPAN Command
									usbService.write(DPAN.getBytes());
									StaticVariables.printLog("DATA SENT", " CASE 10 :COMMAND SENT : DPAN");

								} else if (DataGet.contains("ERROR")) {
									updateProgressStatus("Searching Device...",20);
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

									updateProgressStatus("Searching Device...",50);

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
							case 13: {
								if (DataGet.contains("SINK:") ) {
									updateProgressStatus("Device Searched...",100);

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

									DataGet="";		     	//Clearing Variable
									StaticVariables.printLog("ACTION"," CASE 13  CANCELLING TIMERS") ;

									//***************** checking for water pump controller **********************//

									//checking if device type is of water pump controller
									if(DeviceName.equals(MUD_DevType)||
											DeviceName.equals(MUC_DevType)||
											DeviceName.equals(MUB_DevType)||
											DeviceName.equals(MBSO_DevType)){

										checkMotors();

										//to break further processing
										break;
									}else if((DeviceName.equals(BM_DevType)
											|| DeviceName.equals(SM_DevType))||(DeviceName.equals(SS_DevType)
											|| DeviceName.equals(OTS_DevType))){

										//checking master unit status
										checkMasterUnit();

										//to break further processing
										break;
									}

									//***************** Starting phase 2 **********************//

									//Checking if Device is Already Existing
									try
									{
										String Result=houseDB.isDeviceExists(DeviceID);

										//Fetching Max Device Number From DataBase
										DeviceNumber=houseDB.getUniqueDeviceNo();

										StaticVariables.printLog("DEV NO Check :","DeviceNo check:"+DeviceNumber);

										StaticVariables.printLog("RESULT","Result is : "+Result);
										//checking if device exists
										if(!Result.equals("FALSE")){
											//filtering device type from response contains dev type and previous devno
											//i.e TRUE(3 char dev type)(4char device number) e.g TRUE0031111
											String PrevDevType=Result.substring(4,7);
											StaticVariables.printLog("RESULT","PrevDevType is : "+PrevDevType);

											try{
												int prevDevNo=Integer.parseInt(Result.substring(7));
												StaticVariables.printLog("RESULT","PrevDevNumber is : "+prevDevNo);
												DeviceNumber=prevDevNo;
											}catch(Exception e){
												e.printStackTrace();
											}
											//Fetching previous name of this already configured device
											String PrevDevName=previousDevicename(PrevDevType);
											//Showing Warning Dialog that Device Already Existing
											DeviceAlreadyExistsAlert(PrevDevName);
										}else {
											updateProgressStatus("Configuring Device......",60);
											ConfigurationProcessCall();
										}
									}catch(Exception e){
										e.printStackTrace();
									}


								} else if (DataGet.contains("ERROR")) {

									//update_sts("Searching Device...!");
									cmdCount = 13;							//Setting Command Count to 13
									DataGet = "";							//Clearing DataGet variable
									//bluetooth.btWrite(SINK);
									usbService.write(SINK.getBytes());//Sending SINK Command
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

	/* creating Bluetooth Handler to handle Bluetooth Socket Data
	private  Handler BtHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case READ_LINE:
			{
				String DataGet=(String) msg.obj;
				 
				if(!configTimeout){
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

							//checking if jpan id is not null
							if(datatoSend!=null && datatoSend.length()>0){
								DATA = "AT+UCAST:" + DeviceID + "="+datatoSend + "\r\n"; 

								updateProgressStatus("Configuring Device...",70);

								//sending data to device
								bluetooth.btWrite(DATA); 

							}else{
								StaticVariables.printLog("TAG","Invalid jpan id");
								break;
							}

							//making config flag true
							ConfigFLAG=true;

							//breaking further processing
							break;
						}								
						else if (ConfigFLAG && DataGet!=null && DataGet.startsWith("UCAST") && DataGet.endsWith("#")		
								&& DataGet.length() == 35 && (!DataGet.contains(",OK#"))) 
						{
							StaticVariables.printLog("RESPONSE ", "data length : " + DataGet.length());
							String Res_type = DataGet.substring(31, DataGet.length()-1);
							StaticVariables.printLog("RESPONSE", "Device type recievd : " + Res_type);
							if(DeviceType.equals(CLS_TYPE)){
								ResponseDialog(Res_type);
							}else{ 
								ResponseDialog(Res_type);
							}


							//breaking further processing
							break;

						}  else if (ConfigFLAG && DataGet!=null && DataGet.contains(",OK#") || DataGet.endsWith("*OK#") ) 
							//Handling Calling Switch Responses
						{
							//filtering desired data from string
							String Res_type = DataGet.substring(26);

							StaticVariables.printLog("TAG","Response cls :"+Res_type);

							//invoking method to send response to calling switch
							calling_switch_Response(Res_type);

							//breaking further processing
							break;
						}


						StaticVariables.printLog("TAG","DeviceName :"+DeviceName+" tout:"+configTimeout);

						if(ConfigFLAG && DataGet.contains("@OK$") &&
								(DeviceName.equals(MUD_DevType)||
										DeviceName.equals(MUC_DevType)||
										DeviceName.equals(MUB_DevType)||
										DeviceName.equals(MBSO_DevType)))//"@OK$"
						{
							updateProgressStatus("Configuring Device...", 80);
							String DataToSend=null;
							//response from master unit.
							//now sending moror's id to master unit
							try{
								Cursor mcursor1=null,mcursor2=null;
								String motorID1=null,motorID2=null;

								mcursor1=houseDB.getDeviceTableDetails(SM_DevType);

								if(mcursor1!=null && mcursor1.getCount()>0){
									motorID1=mcursor1.getString(
											mcursor1.getColumnIndex(
													HouseConfigurationAdapter.WPC_DEV_ID));

								}

								//closing cursor
								if(mcursor1!=null){
									mcursor1.close();
								}

								mcursor2=houseDB.getDeviceTableDetails(BM_DevType);

								if(mcursor2!=null && mcursor2.getCount()>0){
									motorID2=mcursor2.getString(
											mcursor2.getColumnIndex(
													HouseConfigurationAdapter.WPC_DEV_ID));

								}

								//closing cursor
								if(mcursor2!=null){
									mcursor2.close();
								}

								//getting id's  
								if(motorID1!=null && motorID2!=null ){
									//if both motors are there then setting first SUMP id and then Borewell id
									//sequence should not change as order is fixed at embedded side also
									DataToSend="@"+motorID1+motorID2+"$";
								}else{
									//if any one motor is there
									if(motorID1!=null){
										//adding 16 zeros for second motor id2 as no second motor to configure
										motorID2="0000000000000000";

										DataToSend="@"+motorID1+motorID2+"$";
									}else if(motorID2!=null){
										//adding 16 zeros for second motor id1 as no second motor to configure
										motorID1="0000000000000000";

										DataToSend="@"+motorID1+motorID2+"$";
									}
								}


								//sending data to master unit
								String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n"; 

								//giving delay of 2 seconds
								delay(2000);

								//sending data to device
								bluetooth.btWrite(DATA); 


							}catch(Exception e){
								e.printStackTrace();
							}

							break;

						}else if(ConfigFLAG && DataGet.contains("@OK$") &&  
								(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType))){
							updateProgressStatus("Configuring Device...", 80);
							//response from water motors.
							//now sending master unit id
							try{
								Cursor mcursor=null;
								String masterID=null;

								mcursor=houseDB.getMasterUnitDetails();

								if(mcursor!=null && mcursor.getCount()>0){
									masterID=mcursor.getString(
											mcursor.getColumnIndex(
													HouseConfigurationAdapter.WPC_DEV_ID));

								}
								//closing cursor
								if(mcursor!=null){
									mcursor.close();
								}

								if(masterID!=null && masterID.length()==16){
									//sending data to motor
									//i.e "@"+Master Unit ID(16chars)+"$"
									String DataToSend="@"+masterID+"$";
									String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n"; 

									//giving delay of 1 seconds
									delay(1000);

									bluetooth.btWrite(DATA);  
								} else{
									msg("Error in Master Unit Data");
								}
							}catch(Exception e){
								e.printStackTrace();
							}

							break;
						}
						else if(ConfigFLAG && DataGet.contains("@OK$") &&  
								(DeviceName.equals(SS_DevType) || DeviceName.equals(OTS_DevType))){
							updateProgressStatus("Configuring Device...", 80);
							//response from water motors.
							//now sending master unit id
							try{
								Cursor mcursor=null;
								String masterID=null;

								mcursor=houseDB.getMasterUnitDetails();

								if(mcursor!=null && mcursor.getCount()>0){
									masterID=mcursor.getString(
											mcursor.getColumnIndex(
													HouseConfigurationAdapter.WPC_DEV_ID));

								}

								//closing cursor
								if(mcursor!=null){
									mcursor.close();
								}

								if(masterID!=null && masterID.length()==16){
									//sending data to motor
									//i.e "@"+Master Unit ID(16chars)+"$"
									String DataToSend="@"+masterID+"$";
									String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n"; 

									//giving delay of 1 seconds
									delay(1000);

									//sending data to device
									bluetooth.btWrite(DATA);  
								} else{
									msg("Error in Master Unit Data");
								}
							}catch(Exception e){
								e.printStackTrace();
							}

							break;
						}

						else if(!isDeviceResponseTimeout && DataGet.contains("#OK*") &&
								(DeviceName.equals(MUD_DevType)||
										DeviceName.equals(MUC_DevType)||
										DeviceName.equals(MUB_DevType)||
										DeviceName.equals(MBSO_DevType))){

							//cancel response wait timer 
							responseWaitTimerHandler.removeCallbacks(responseWaitTimerRunnable); 

							//insert or update  motor unit id in local water pump table
							boolean isAdded=houseDB.insertOrUpdateMasterUnit(DeviceName, 
									DeviceType, DeviceID );

							if(isAdded){
								//prompt to search at least one motor
								NoMotorDialog("Configuration Aborted","No Water motors are available to configure.\nPlease search atleast one water motor.");

							}else{ 
								errorDialog(); 
							}

							break;

						}else if(!isDeviceResponseTimeout && DataGet.contains("#OK*") &&  
								(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType) 
										||(DeviceName.equals(SS_DevType) || DeviceName.equals(OTS_DevType)))){

							//cancel response wait timer 
							responseWaitTimerHandler.removeCallbacks(responseWaitTimerRunnable); 

							NoMotorDialog("Configuration Aborted","No Master Unit available to control motors.\nPlease search Water Master Unit First.");

							break;

						}

					}catch(Exception e){
						e.printStackTrace();
					}


				}
				else{
					StaticVariables.printLog("TAG","already configuration timeout occured");
				}


				StaticVariables.printLog("BT RESPONSE","cmd count :"+cmdCount);
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

							updateProgressStatus("Searching Device...",20);

							cmdCount = 11; // Setting Command Count to 11
							DataGet = ""; // Clearing DataGet variable
							bluetooth.btWrite(DPAN); // Sending DPAN Command
							StaticVariables.printLog("DATA SENT", " CASE 10 :COMMAND SENT : DPAN");

						} else if (DataGet.contains("ERROR")) {
							updateProgressStatus("Searching Device...",20);
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

							updateProgressStatus("Searching Device...",50);

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
					case 13: {
						if (DataGet.contains("SINK:") ) {
							updateProgressStatus("Device Searched...",100);

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

							DataGet="";		     	//Clearing Variable
							StaticVariables.printLog("ACTION"," CASE 13  CANCELLING TIMERS") ;

							/*//***************** checking for water pump controller **********************//*/

							//checking if device type is of water pump controller
							if(DeviceName.equals(MUD_DevType)||
									DeviceName.equals(MUC_DevType)||
									DeviceName.equals(MUB_DevType)||
									DeviceName.equals(MBSO_DevType)){

								checkMotors(); 

								//to break further processing
								break;														
							}else if((DeviceName.equals(BM_DevType) 
									|| DeviceName.equals(SM_DevType))||(DeviceName.equals(SS_DevType)
											|| DeviceName.equals(OTS_DevType))){

								//checking master unit status
								checkMasterUnit();

								//to break further processing
								break;	
							} 

							/*//***************** Starting phase 2 **********************//*/

							//Checking if Device is Already Existing
							try
							{
								String Result=houseDB.isDeviceExists(DeviceID);

								//Fetching Max Device Number From DataBase
								DeviceNumber=houseDB.getUniqueDeviceNo();	 

								StaticVariables.printLog("DEV NO Check :","DeviceNo check:"+DeviceNumber);

								StaticVariables.printLog("RESULT","Result is : "+Result);
								//checking if device exists
								if(!Result.equals("FALSE")){
									//filtering device type from response contains dev type and previous devno 
									//i.e TRUE(3 char dev type)(4char device number) e.g TRUE0031111
									String PrevDevType=Result.substring(4,7);
									StaticVariables.printLog("RESULT","PrevDevType is : "+PrevDevType);

									try{
										int prevDevNo=Integer.parseInt(Result.substring(7));
										StaticVariables.printLog("RESULT","PrevDevNumber is : "+prevDevNo);
										DeviceNumber=prevDevNo;
									}catch(Exception e){
										e.printStackTrace();
									}
									//Fetching previous name of this already configured device
									String PrevDevName=previousDevicename(PrevDevType);
									//Showing Warning Dialog that Device Already Existing
									DeviceAlreadyExistsAlert(PrevDevName);
								}else {
									updateProgressStatus("Configuring Device......",60);
									ConfigurationProcessCall();
								}
							}catch(Exception e){
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


	//check master unit is configured or not
	void checkMasterUnit(){
		boolean isConfigured=houseDB.isMasterDeviceConfigured();

		if(isConfigured){

			//configuring water motor
			WPC_ConfigurationProcessCall();

		}else{
			abortConfiguration();

		}

	}


	//check for motors
	void checkMotors(){
		//check whether motors are configured or not
		boolean isMotorsConfigured1=houseDB.isDeviceConfigured(BM_DevType); 
		boolean isMotorsConfigured2=houseDB.isDeviceConfigured(SM_DevType);

		if(isMotorsConfigured1 && isMotorsConfigured2){

			//prompt to configure master unit 
			MasterUnitDialog("Configuration alert",
					"Water Motors are available."
							+ "\nDo you want to configure water master unit? ");


		}else if(isMotorsConfigured1 || isMotorsConfigured2){

			WpcDialog("Configuration Alert",
					"One water motor is available."
							+ "\nDo you want to configure Water Master Unit ?");
		}else{
			abortConfiguration();
		}
	}

	//method to abort configuration
	void abortConfiguration(){
		//sending abort to come out device from configuration process
		String DATA = "AT+UCAST:" + DeviceID + "=" + ABORT + "\r\n"; 
		//bluetooth.btWrite(DATA);
		usbService.write(DATA.getBytes());
		//starting response wait timeout timer
		cmdCount=15;
		responseWaitTimer();

	}

	//WPC dialog
	private void WpcDialog(final String title,final String msg){
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				cancelDialog();

				AlertDialog.Builder dlg=new AlertDialog.Builder(DeviceConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);  
				dlg.setCancelable(false);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setIcon(android.R.drawable.ic_dialog_info);

				dlg.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switchActivity(false);
					}
				});

				dlg.setPositiveButton("Configure",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) { 
						dialog.dismiss();

						WPC_ConfigurationProcessCall();

					}
				});

				dlg.show();
			}
		});


	}

	//configure water Master Unit
	private void MasterUnitDialog(final String title,final String msg){
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				cancelDialog();

				AlertDialog.Builder dlg=new AlertDialog.Builder(DeviceConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);  
				dlg.setCancelable(false);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setIcon(android.R.drawable.ic_dialog_info);

				dlg.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						switchActivity(false);
					}
				}); 
				dlg.setPositiveButton("Configure",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						WPC_ConfigurationProcessCall();

					}
				});

				dlg.show();
			}
		});


	}

	//configure water Master Unit
	private void NoMotorDialog(final String title,final String msg){
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				cancelDialog();

				AlertDialog.Builder dlg=new AlertDialog.Builder(DeviceConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);  
				dlg.setCancelable(false);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setIcon(android.R.drawable.ic_dialog_info);
				dlg.setNegativeButton("Home",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						finish();
					}
				});
				dlg.setPositiveButton("Search",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						switchActivity(false);
					}
				});

				dlg.show();
			}
		});


	}	

	//configure water Master Unit
	private void errorDialog(){
		runOnUiThread(new Runnable() { 
			@Override
			public void run() {
				cancelDialog();

				AlertDialog.Builder dlg=new AlertDialog.Builder(DeviceConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);  
				dlg.setCancelable(false);
				dlg.setTitle("ERROR");
				dlg.setMessage("Data Insertion Error.Please Try Again");
				dlg.setIcon(android.R.drawable.ic_dialog_alert);

				dlg.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss();
						switchActivity(false);
					}
				});

				dlg.show();
			}
		});


	}	


	//switch activity
	void switchActivity(boolean isAddNewDevice){
		Intent it=new Intent(DeviceConfiguration.this,AddNewDevice.class);
		it.putExtra("isAddNewDevice", isAddNewDevice);
		startActivity(it);
		//adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();
	}

	//ConfigurationProccessCall
	private void ConfigurationProcessCall(){
		//sending configuration command
		ConfigurationCommand();			

		if(DeviceType.equals(CLS_TYPE)){

			//starting configuration timer for calling switch  i.e time=2 min.  
			mainConfigurationTimer(120000);
			StaticVariables.printLog("TAG","calliing switch timer start");
		}else{
			//starting configuration timer for other devices  i.e time=15 sec.
			mainConfigurationTimer(15000);
		}

	}

	//WPC Configuration Process Call
	private void WPC_ConfigurationProcessCall(){
		//sending configuration command
		WPC_ConfigurationCommand();			

		//showing progress
		if(pdlg==null){
			ProgressDialog("Configuring Device..."); 
			updateProgressStatus("Configuring Device...", 60); 
		}

		//starting configuration timer i.e time=10 sec
		mainConfigurationTimer(10000);	

	}	

	//Sending Configuration Data to Searched Device
	private void WPC_ConfigurationCommand(){

		//making data pattern for Master Unit
		String DataToSend=null;
		try{
			// Making A String With Desire format i.e 
			/*
			 * "@"+DeviceType(3char)+DeviceNumber(4char)+
				DeviceGroupId(3char)+RoomNo(2char)+DevicID(16char)+"$"
			 */

			//checking if present device is already present in database
			boolean result=checkWPCDeviceExists(DeviceID);
			if(result){
				StaticVariables.printLog("","device already existing!!");
			}
			else{
				StaticVariables.printLog("","device not existing!!");
			}


			String DEV_TYPE=null,DEV_NUMBER=null,DEV_ID=null,ROOM_NO=null;

			DEV_TYPE=DeviceType;
			DEV_NUMBER=""+DeviceNumber;
			DEV_ID=CordintorID; 
			ROOM_NO=""+RoomNo;

			//making room number length of two character
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

			//Checking all parameters and making final string of length 30char
			if(DEV_TYPE.length()==3 && DEV_NUMBER.length()==4 
					&& DeviceGroupId.length()==3  && ROOM_NO.length()==2  && DEV_ID.length()==16){
				DataToSend="@"+DEV_TYPE+DEV_NUMBER+DeviceGroupId+ROOM_NO+DEV_ID+"$";
			}

		}catch(Exception e){
			e.printStackTrace();
		}

		//Making Final Command Format
		if(DeviceID.length()==16 && DataToSend.length()==30 ){
			String DATA = "AT+UCAST:" + DeviceID + "=" + DataToSend + "\r\n";	
			StaticVariables.printLog("DATA TO SEND","Data to send : "+DATA);
			//bluetooth.btWrite(DATA);	//Sending data to device
			usbService.write(DATA.getBytes());
		}else{ 
			Toast.makeText(getApplicationContext(), "Invalid Data Format", Toast.LENGTH_SHORT).show();
		}		

	}	

	//Progress Bar
	private void ProgressDialog(final String msg)
	{
		runOnUiThread(new Runnable() {			
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				if(pdlg==null){
					pdlg=new ProgressDialog(DeviceConfiguration.this,ProgressDialog.THEME_HOLO_LIGHT);
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

	//timer for searching sink of device
	void searchTimer(){
		StaticVariables.printLog("SEARCH TIMER","*********************STARTING SEARCH TIMER****************************");

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
					
					StaticVariables.printLog("SEARCH TIMER","**********TIMEOUT SEARCH TIMER***********");
				}else{ 
					StaticVariables.printLog("CONFIG","##########Sink Already Searched#########");
				}

			}
		};

		//25 sec. time to search configuration module
		searchTimerHandler.postDelayed(searchTimerRunnable,25000);

	}

	//timer for final configuration response
	void mainConfigurationTimer(final long timerTime){
		StaticVariables.printLog("CONFIG TIMER","*************STARTING CONFIG TIMER*************");

		configTimerHandler=new Handler(); 
		configTimerRunnable=new Runnable() { 
			@Override
			public void run() { 
				if(!isConfigCompleted){  
					configTimeout=true;
					cmdCount=15;
					ConfigurationErrorDialog("Configuration Failed","Unable to Configure "+DEVICE_FULL_NAME+" Device.Try Again.");
					StaticVariables.printLog("Config TIMER","**********TIMEOUT CONFIG TIMER**********");
				}else{ 
					StaticVariables.printLog("CONFIG","#######device already configured#######");
				}

			}
		};
		
		configTimerHandler.postDelayed(configTimerRunnable,timerTime);

	}

	//reset timer
	void resetTimer(){

		StaticVariables.printLog("RESET TIMER","*********STARTING RESET TIMER*****************");

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
					StaticVariables.printLog(" RESET TIMER","********TIMEOUT RESET TIMER***********");
					 
					ConfigurationErrorDialog("Configurtaion Tool Reset Failed","Unable to Reset Configuration Tool.Try Configuring Again.");
					
				}else{ 

					searchAttempt=0;
					StaticVariables.printLog("CONFIG","#######configuration module already resetted######");
				}

			}
		};

		//15 sec. time to reset configuration module
		resetTimerHandler.postDelayed(resetTimerRunnable,15000);

	}


	//wait timer for abort command response
	void responseWaitTimer(){
		StaticVariables.printLog("Wait TIMER","*********STARTING Wait TIMER***********");

		responseWaitTimerHandler=new Handler(); 
		responseWaitTimerRunnable=new Runnable() { 
			@Override
			public void run() { 

				if(!isDeviceResponseTimeout){
					isDeviceResponseTimeout=true;
					responseErrorDialog("Response Timeout", "Device Response Timeout.\nTry To Configure Again!");
				}

			}
		};

		//7 sec. time to wait for embed device response
		responseWaitTimerHandler.postDelayed(responseWaitTimerRunnable,7000);

	}



	//Response Handling method
	private void ResponseDialog(final String responseType){
		try{

			StaticVariables.printLog("TAG","DeviceType in Response Dialog :"+DeviceType+" and responseType :"+responseType);

			//comparing response with device type selected by user
			if(responseType.equals(DeviceType) ){

				//updating house config status
				updateServerinfo();

				//indicating that configuration completed
				isConfigCompleted=true;
				//reset serach attempt
				searchAttempt=0;

				//canceling config timer
				configTimerHandler.removeCallbacks(configTimerRunnable);

				StaticVariabes_div.log("current_room_image_name  "+current_room_image_name,TAG1);
				StaticVariabes_div.log("UserDeviceName  "+UserDeviceName,TAG1);

				if(isDevSwb){
					StaticVariables.printLog("TAG","SWB type"); 
					//Saving details in Database
					/*boolean isSaved=houseDB.UpdateSwitch(DeviceName,DeviceType,
							DeviceNumber, DeviceID,switchCount, fanCount, 
							RoomName, RoomNo,DEVICE_TYPE_TEXT,DeviceGroupId);*/

					boolean isSaved=houseDB.UpdateSwitch(DeviceName,DeviceType,
							DeviceNumber, DeviceID,switchCount, fanCount,
							RoomName, RoomNo,DEVICE_TYPE_TEXT,DeviceGroupId,current_room_image_name,UserDeviceName);


					if(isSaved){

						Update_db_version_number();
						//success dialog foe switch board configuration
						ConfigurationSuccessDialog("Configuration Completed",DEVICE_FULL_NAME+" Configured Successfully.\n"+"To operate House Devices please Go to configuration menu and upload house First."); //Displaying success Dialog
						try {
							LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
							locallist_adap = new LocalListArrangeTable(DeviceConfiguration.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
							locallist_adap.open();
							locallist_adap.deleteall();
							locallist_adap.close();
						}catch(Exception e){
							e.printStackTrace();

						}
					}else{
						//error dialog for switch board configuration 
						ConfigurationErrorDialog("Details Not Saved","Unable to Save Details for "+DEVICE_FULL_NAME+" Device.Try Configuring Again.");
						
					}

				}else {
					
					if(isDevSwd){
						StaticVariables.printLog("TAG","SWD type"); 
						//Saving details in Database
						boolean isSaved=houseDB.UpdateSwitch(DeviceName,DeviceType, 
								DeviceNumber, DeviceID,switchCount, fanCount, 
								RoomName, RoomNo,DEVICE_TYPE_TEXT,DeviceGroupId,current_room_image_name,UserDeviceName);
						if(isSaved){
							Update_db_version_number();
							LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
							locallist_adap = new LocalListArrangeTable(DeviceConfiguration.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
							locallist_adap.open();
							locallist_adap.deleteall();
							locallist_adap.close();
							//success dialog foe switch board configuration
							ConfigurationSuccessDialog("Configuration Completed",DEVICE_FULL_NAME+" Configured Successfully.\n"+"To operate House Devices please Go to configuration menu and upload house First."); //Displaying success Dialog
						}else{
							//error dialog for switch board configuration 
							ConfigurationErrorDialog("Details Not Saved","Unable to Save Details for "+DEVICE_FULL_NAME+" Device.Try Configuring Again.");
							
						}

					}else{


					
					StaticVariables.printLog("TAG","other dev type"); 
					boolean isSaved=houseDB.UpdateMaster(DeviceName,DeviceType,
							DeviceNumber, DeviceID, RoomName, RoomNo,
							DEVICE_TYPE_TEXT,DeviceGroupId,current_room_image_name,UserDeviceName);

					if(isSaved){

						if(DeviceName.equals(BM_DevType) || DeviceName.equals(SM_DevType) ||
								DeviceName.equals(SS_DevType)|| DeviceName.equals(OTS_DevType)){

							//insert or update  motor unit id
							boolean isAdded=houseDB.insertOrUpdateMotors(DeviceName, 
									DeviceType, DeviceID);
							if(isAdded){
								StaticVariables.printLog("TAG",DeviceName+" data inserted in wp table"); 
							}else{
								msg("data not inserted in WPC table"); 
							}

							boolean isMasterConfigured=houseDB.isMasterUnitConfigured();
							if(!isMasterConfigured){

								//dialog if master unit not configured
								ConfigurationSuccessDialog("Configuration Completed",DEVICE_FULL_NAME+" Configured Successfully.\nConfigure master unit if no more motor or sensor configuration pending.");  

							}else{

								//dialog if master unit alredy configured
								ConfigurationSuccessDialog("Configuration Completed",DEVICE_FULL_NAME+" Configured Successfully.\n"+"To operate House Devices please Go to configuration menu and upload house First."); //Displaying success Dialog

							}

						}else{

							Update_db_version_number();

							LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
							locallist_adap = new LocalListArrangeTable(DeviceConfiguration.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
							locallist_adap.open();
							locallist_adap.deleteall();
							locallist_adap.close();

							//success dialog foe switch board configuration
							ConfigurationSuccessDialog("Configuration Completed",DEVICE_FULL_NAME+" Configured Successfully.\n"+"To operate House Devices please Go to configuration menu and upload house First."); //Displaying success Dialog
						
						}

					}else{ 
						
						//error dialog for other master devices 
						ConfigurationErrorDialog("Details Not Saved","Unable to Save Details for "+DEVICE_FULL_NAME+" Device.Try Configuring Again.");
						
					}
					
				}
				}
			}else{ 
				//showing error dialog for device mismatch operation
				responseErrorDialog("Response Mismatched", "Device Type Response Mismatched.\nPlease try to configure again.");

			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}		


	// method to update config. status on server details table
	void updateServerinfo(){

		//update server details table to check  a particular  house's  configuration status
		try{
			boolean isupdated=houseDB.UpdateHouseConfigStatus();
			if(isupdated)
				StaticVariables.printLog("DB","house config status updated");
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//Calling Switch ResponseDialog
	public void calling_switch_Response(String response) 
	{ 
		if(!configTimeout){
			String data_to_send =null;

			//Handling Response from calling switch i.e sequence that calling switch received
			if (response.contains("*OK#") || (response.contains(",OK#") && response.length()==8) ) 
			{
				// giving delay of half sec.
				delay(500);						
				// incrementing array index
				//int index = CLB_DevID_INDEX++;	
				int index = 0;	
				StaticVariables.printLog("CLS","LAST RESPONSE :"+response);
				if((response.contains(",OK#") && response.length()==8)){
					//filtering data *seqNo(3char),OK#
					try{
						String seqIndex=response.substring(1,4);
						int lastIndex=Integer.parseInt(seqIndex);
						StaticVariables.printLog("CLS","LAST RESPONSE SUBSTRING :"+lastIndex);
						index=lastIndex;
					}catch(Exception e){
						e.printStackTrace();
						index=CLB_DevID_INDEX++;
					} 
				}else if(response.contains("*OK#")){
					index=CLB_DevID_INDEX++;	
				}

				//incrementing sequence  by 1 for embed device reference
				//to send device id we will use index less by 1
				String BELL_SEQUENCE=""+(index+1);

				//making bell sequence of three char
				while(BELL_SEQUENCE.length()<3){
					BELL_SEQUENCE="0"+BELL_SEQUENCE;
				}

				StaticVariables.printLog("TAG", " index is : " + index	+ " count ok : " + CLB_DevID_INDEX);	

				//checking if index is reached to its end position  
				if (index <finalDeviceIdList.size()) 	// checking if index become equals to number of calling bells 
				{ 
					// making command to send device id 
					data_to_send= "AT+UCAST:" + DeviceID + "=" + "@"+BELL_SEQUENCE+","+ finalDeviceIdList.get(index) + "$" + "\r\n";
					// sending device id to calling switch
					//bluetooth.btWrite(data_to_send);
					usbService.write(data_to_send.getBytes());
				}else{ 
					StaticVariables.printLog("TAG", "all device id sent already"); 
					CLB_DevID_INDEX=0;
				}

			}else{
				StaticVariables.printLog("TAG","incorrect data format :"+response);
			}


		}else{
			StaticVariables.printLog("TAG","already configuration timeout");
		}


	}

	//showing Device already exists warning dialog
	private void DeviceAlreadyExistsAlert(final String DeviceName){
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				//cancel progress dialog
				cancelDialog(); 

				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Device Already Exists");
				dialog.setMessage("Device Already Configured As "+DeviceName+"."
						+ "\nConfiguring This Device Again will Delete The Previous Records Related to This Device."
						+ "\nDo You Want To Continue Configuration Process?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(DeviceConfiguration.this,AddNewDevice.class);
						it.putExtra("isAddNewDevice", true);
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
						updateProgressStatus("Configuring Device...", 50);
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
				//cancel dialog
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
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

	//Error Alert
	private void responseErrorDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{ 
				//cancel dialog
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
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

				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
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

						Intent intent=new Intent(DeviceConfiguration.this,AddNewDevice.class);
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

	//Device Configured Alert
	void ConfigurationSuccessDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				cancelDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Home", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
 
						Tcp.tcpConnectionClose(); 
						Intent it=new Intent(DeviceConfiguration.this,Main_Navigation_Activity.class);
						// adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						startActivity(it);
						finish();
 

					}
				});

				dialog.setNeutralButton("Config Menu", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						Intent it=new Intent(DeviceConfiguration.this,Configuration_Main.class);
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
						switchActivity(true);
					}
				});

				dialog.show();
			}
		});
	}	

	//cancel dialog
	void cancelDialog(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(pdlg!=null){
					pdlg.dismiss();
					pdlg=null;
				}
			}
		});
	}

	//Alert Showing that no master are configured 
	private void noMasterAlert(final String title,final String msg){
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						Intent it=new Intent(DeviceConfiguration.this,AddNewDevice.class);
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


	//fetching Full name of previously configured device
	private String previousDevicename(String devType){

		String previousname=null;
		try{
			Resources res = getResources();	//getting instance of resource xml

			String[] fullNames = res.getStringArray(R.array.Full_DevNames);		// getting full name  array
			List<String> Full_DevNames_List = Arrays.asList(fullNames); 		// converting array to list

			String[] DeviceTypes = res.getStringArray(R.array.Device_Type);	// getting device type array
			List<String> DeciceTypesList = Arrays.asList(DeviceTypes);			// converting array to list
			int index=DeciceTypesList.indexOf(devType);				// getting index of previously configured device

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

/*	//Bluetooth Connection Task
	private class BluetoothConnect extends AsyncTask<String,Void, Boolean> {

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
			if(pdlg!=null){
				pdlg.setProgress(100);
			}

			//cancelling progress bar
			cancelDialog();

			if(result){
				bt_status.setImageResource(BT_ON);	//showing bluetooth is connected
				ProgressDialog("Searching Device,Waiting for Response...");
				//starting configuration process
				searchTimer();
			}else{
				if(Bluetooth.isBluetoothOff){
					//Showing Bluetooth off Alert if Mobile Bluetooth is off
					BluetoothOffErrorAlert(); 	
				}else{
					//Showing Bluetooth not Connected Error Dialog if Bluetooth not Connected
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
				AlertDialog.Builder dlg = new AlertDialog.Builder(DeviceConfiguration.this,AlertDialog.THEME_HOLO_DARK);
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
				AlertDialog.Builder dlg = new AlertDialog.Builder(DeviceConfiguration.this,AlertDialog.THEME_HOLO_DARK);
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
	}*/

	//going back to previous activity
	@Override
	public void onBackPressed() {
		switchActivity(false);
	}

	@Override
	protected void onPause() { 
		super.onPause();
		unregisterReceiver(mUsbReceiver);
		unbindService(usbConnection);
		if(HouseConfigurationAdapter.sdb.isOpen()){
			houseDB.close();
			StaticVariables.printLog("TAG","DB CLOSED ");
		}

		if(LocalDatabaseAdapter.sdb.isOpen()){
			localDB.close();
		}
	}

	@Override
	protected void onResume() { 
		super.onResume();
		setFilters();  // Start listening notifications from UsbService
		startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
		if(!HouseConfigurationAdapter.sdb.isOpen()){
			houseDB.open();
			StaticVariables.printLog("TAG","DB open ");
		}

		if(!LocalDatabaseAdapter.sdb.isOpen()){
			localDB.opendb();
		}
	}	

	//Click Events
	@Override
	public void onClick(View v) {

		switch(v.getId()){
		case R.id.Configure :
		{     

			//checking tcp connection is present or not
			if(Tcp.tcpConnected)
			{
				resetTimeout=false;
				searchtimeout=false;
				configTimeout=false;
				isDeviceResponseTimeout=false;

				//making calling bell index zero
				CLB_DevID_INDEX=0;

				//making flag false
				ConfigFLAG=false;

				isSearchedCompleted=false;
				isResetCompleted=false;
				isConfigCompleted=false;

				//showing progress bar
				ProgressDialog("Searching Device,Waiting for Response....");

				//starting configuration
				searchTimer();

			/*	if(Bluetooth.btConnected)
				{ 
					searchAttempt++;

					StaticVariables.printLog("","search attempt :"+searchAttempt);
					
					if(searchAttempt==3){
						//showing progress bar
						ProgressDialog("Resetting Configuration Tool,Waiting for Response....");

						//starting resetting process
						resetTimer();

						searchAttempt--;
					}else{
						//showing progress bar
						ProgressDialog("Searching Device,Waiting for Response....");

						//starting configuration
						searchTimer();
					}

				}else{
					//starting bluetooth connection if bluetooth is not connected
					new BluetoothConnect().execute();
				} */
			}else{
				Toast.makeText(getApplicationContext(), 
						"Please Ensure that Server is Connected!",Toast.LENGTH_SHORT).show();
			}


			break;
		}
		case R.id.server_status: {
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
			switchActivity(false);
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

	//checking if water pump device is already existing
	boolean checkWPCDeviceExists(String devid){
		//Checking if Device is Already Existing
		try
		{
			String Result=houseDB.isDeviceExists(devid);

			//Fetching Max Device Number From DataBase
			DeviceNumber=houseDB.getUniqueDeviceNo();	 

			StaticVariables.printLog("DEV NO Check :","DeviceNo check:"+DeviceNumber);

			StaticVariables.printLog("RESULT","Result is : "+Result);
			//checking if device exists
			if(!Result.equals("FALSE")){
				//filtering device type from response contains dev type and previous devno 

				try{
					int prevDevNo=Integer.parseInt(Result.substring(7));
					StaticVariables.printLog("RESULT","PrevDevNumber is : "+prevDevNo);
					DeviceNumber=prevDevNo;
				}catch(Exception e){
					e.printStackTrace();
				}

				return true;
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		return false;
	}


	// method to show bell switch dialog
	void selectBellListDialog(final String[] BellsData) {
		try { 

			final ArrayList<Integer> SelectedBellDeviceIdList=new ArrayList<Integer>();

			AlertDialog.Builder dlg = new AlertDialog.Builder(
					DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
			dlg.setTitle("Choose Calling Bells");
			dlg.setCancelable(false);
			dlg.setIcon(android.R.drawable.ic_dialog_info);

			dlg.setMultiChoiceItems(BellsData, null,
					new DialogInterface.OnMultiChoiceClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int index,
						boolean isChecked) {
					if (isChecked) {
						// If the user checked the item, add it to the selected items
						SelectedBellDeviceIdList.add(index);
					} else if (SelectedBellDeviceIdList.contains(index)) {
						// Else, if the item is already in the array, remove it 
						SelectedBellDeviceIdList.remove(Integer.valueOf(index));
					}
				}
			});

			dlg.setNegativeButton("Back",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {
					dialog.dismiss();
					switchActivity(false);
				}
			});

			dlg.setNeutralButton("Submit",
					new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int arg1) {

					int selectedItemSize=SelectedBellDeviceIdList.size();

					StaticVariables.printLog("Tag","size of selectedItem :"+selectedItemSize);

					if(selectedItemSize>0){

						int index=0; 
						//adding selected device id in final list
						for(int i=0;i<SelectedBellDeviceIdList.size();i++){
							index=SelectedBellDeviceIdList.get(i);

							String devID=DevIdList[index]; 
							finalDeviceIdList.add(devID);
						}

						StaticVariables.printLog("Tag","bell list size :"+finalDeviceIdList.size());


					}else{
						finalDeviceIdList.clear();
						//showing dialog again
						selectBellListDialog(bellsDevInfo);
						msg("Please select atleast one calling bell!");
					}


				}
			});
			dlg.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}



	// method to show bell switch dialog
	void selectCurtainListDialog(final String[] CurtainsData) {


		try {

			final ArrayList<Integer> SelectedBellDeviceIdList=new ArrayList<Integer>();

			AlertDialog.Builder dlg = new AlertDialog.Builder(
					DeviceConfiguration.this, AlertDialog.THEME_HOLO_LIGHT);
			dlg.setTitle("Choose Curtain to configure Sheer");
			dlg.setCancelable(false);
			dlg.setIcon(android.R.drawable.ic_dialog_info);




			dlg.setSingleChoiceItems(CurtainsData, -1, new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Toast.makeText(getApplicationContext(),
									"user selected  is "+CurtainsData[which], Toast.LENGTH_LONG).show();
							Curtainselected=CurtainsData[which];
							isCurtainselected=true;
							//dismissing the dialog when the user makes a selection.
							//dialog.dismiss();
						}
					});


	/*		dlg.setMultiChoiceItems(CurtainsData, null,
					new DialogInterface.OnMultiChoiceClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int index,
											boolean isChecked) {
							if (isChecked) {
								// If the user checked the item, add it to the selected items
								SelectedBellDeviceIdList.add(index);

							} else if (SelectedBellDeviceIdList.contains(index)) {
								// Else, if the item is already in the array, remove it
								SelectedBellDeviceIdList.remove(Integer.valueOf(index));
							}
						}
					});*/

			dlg.setNegativeButton("Back",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {
							dialog.dismiss();
							switchActivity(false);
						}
					});

			dlg.setNeutralButton("Submit",
					new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int arg1) {

							int selectedItemSize=SelectedBellDeviceIdList.size();

							StaticVariables.printLog("Tag","size of selectedItem :"+selectedItemSize);

							if(isCurtainselected){

								UserDeviceName=Curtainselected+"SH";
								DeviceGroupId=houseDB.Curtain_Free_Normal_groupid(Curtainselected);


							}else{
								finalDeviceIdList.clear();
								//showing dialog again
								selectCurtainListDialog(curDevNameList);
								msg("Please select a Curtain!");
							}


						}
					});
			dlg.show();
		} catch (Exception e) {
			e.printStackTrace();
		}

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
						//  onShift();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void Update_db_version_number(){
		if(StaticVariabes_div.housename!=null) {
			try{
			ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
			sdadap = new ServerDetailsAdapter(DeviceConfiguration.this);
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
