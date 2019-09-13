package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: WirelessStaticPanelConfiguration.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to configure devices across wireless panel selected.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;



import java.util.ArrayList;
import java.util.List;

import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;


public class WirelessStaticPanelConfiguration extends Activity implements
OnClickListener  {

	Button saveSetting_btn, deletSetting_btn,cancel_btn;

	int Current_RoomNo = 0, Current_DevNo = 0, Current_WRLS_DevNo = 0,
			Current_WRLS_DevType = 0;

	String Current_RoomName = null, Current_WRLS_DevName = null,
			Current_WRLS_RoomName = null, Current_WRLS_DevID = null,
			Current_DevName = null, Current_DevType = null,Current_DevGroupId=null,
			Current_DevID = null ,Current_User_Dev_Name = null;

	List<String> dimmerActionList, dimmerLevelList, curActionList,
	rgbActionList, rgbColour_EffectList, rgbBrightnessList,
	rgbSpeedList;

	ListView PanelSwitch ;
	String[] PanelSwitchesList ;

	final int READ_LINE = 1, READ_BYTE = 2, EXCEPTION = 3, TCP_LOST = 4,
			BT_LOST = 5;

	Tcp tcp = null;
	WirelessConfigurationAdapter WhouseDB = null;
	boolean IS_SERVER_CONNECTED = false;

	ListadApter_ir adapter; 

	int  ItemListView=R.layout.staticpanel_listview,
			server_online = R.drawable.connected,
			server_offline = R.drawable.disconnected;


	int selectedPosition = -1, switchAction_ItemPostion = -1;

	String DEFAULT_SWITCH_ACTION_TEXT = "Set Action"; 

	// *********************************DMR COMMANDS*************************//

	final String DMR_Brightness_Plus = "110", DMR_Brightness_Minus = "111",
			DMR_ON_OFF_CMD = "101";

	final String DMR_HIGH_CMD="255" ,DMR_MEDIUM_CMD="040" ,DMR_LOW_CMD="002";

	final String DMR_1_CMD = "003", DMR_2_CMD = "015", DMR_3_CMD = "030",
			DMR_4_CMD = "050", DMR_5_CMD = "071", DMR_6_CMD = "080",
			DMR_7_CMD = "100", DMR_8_CMD = "150", DMR_9_CMD = "200",
			DMR_10_CMD = "250";

	// *********************************RGB COMMANDS************************//

	final String RGB_Brightness_Plus = "110", RGB_Brightness_Minus = "111",
			RGB_Speed_Plus="108", RGB_Speed_Minus="109", RGB_ON_OFF_CMD = "101"; 


	final String RGB_MODES_CMD = "202",RGB_COLORS_CMD = "201";

	final String RGB_FLASH_CMD="104"  , RGB_STROBE_CMD="105",
			RGB_SMOOTH_CMD="106" , RGB_FADE_CMD="107";

	final String RGB_RED_CMD = "255000000000",  RGB_GREEN_CMD = "000255000000", 
			RGB_BLUE_CMD = "000000255000",  RGB_ORANGE_CMD = "255165000000";

	final String RGB_COLOR_PROCESS_CMD = "112";

	// ***************************CUR COMMANDS *******************//

	final String CUR1_OPEN_CMD="101" ,CUR1_CLOSE_CMD="102",CUR1_STOP_CMD="103";
	final String CUR2_OPEN_CMD="105" ,CUR2_CLOSE_CMD="106",CUR2_STOP_CMD="107";


	//****************************FAN COMMANDS*************************//
	final String FAN1_MINUS_CMD="721" ,FAN1_PLUS_CMD="720" ,FAN1_ON_OFF_CMD="700";
	final String FAN2_MINUS_CMD="751" ,FAN2_PLUS_CMD="750" ,FAN2_ON_OFF_CMD="730";

	//*************************Geyser Commands**************************//
	final String GEYSER_ON_CMD="201" ,GEYSER_OFF_CMD="301";

	//*************************Bell switch Commands**************************//
	final String BELL_RING_CMD="101" ;


	String PANEL_SWB_ID = "10";

	String switchData = null;

	ArrayList<String> Switch_CMDS = new ArrayList<String>();

	// creating two string builder instances to save
	// switch data and switch number data temporarly
	StringBuilder FINAL_SWITCH_DATA = new StringBuilder();
	StringBuilder FINAL_SWITCH_NUM_DATA = new StringBuilder();

	static volatile boolean isActionNotSelected = false;
	static boolean isTcpConnecting=false;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(
				WindowManager.LayoutParams.FLAG_FULLSCREEN
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.wireless_staticpanelconfiguration);

		PanelSwitch = (ListView) findViewById(R.id.Panel_Switches_list); 
		saveSetting_btn = (Button) findViewById(R.id.savePanelSettings);
		deletSetting_btn = (Button) findViewById(R.id.DeletePanelSettings);
		cancel_btn= (Button) findViewById(R.id.cancelPanelSettings);

		// adding click listener
		saveSetting_btn.setOnClickListener(this);
		deletSetting_btn.setOnClickListener(this);
		cancel_btn.setOnClickListener(this);

		// making delete button disable
		deletSetting_btn.setEnabled(false);

		try {
			WhouseDB = new WirelessConfigurationAdapter(this);
			// opening wireless database
			WhouseDB.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			Intent it = getIntent();
			if (it != null) {
				Current_RoomName = it.getStringExtra("RoomName");
				Current_RoomNo = it.getIntExtra("RoomNo", 0);
				Current_DevType = it.getStringExtra("DevType");
				Current_DevNo = it.getIntExtra("DevNo", 0);
				Current_DevID = it.getStringExtra("DevID");
				Current_DevName = it.getStringExtra("DevTypeName");
				Current_DevGroupId=it.getStringExtra("DeviceGroupId");  

				Current_WRLS_DevNo = it.getIntExtra("DevNo_WRLS", 0);
				Current_WRLS_DevType = it.getIntExtra("DevType_WRLS", 0);
				Current_WRLS_RoomName = it.getStringExtra("RoomName_WRLS");
				Current_WRLS_DevID = it.getStringExtra("DevId_WRLS");
				Current_WRLS_DevName = it.getStringExtra("DevName_WRLS");

				StaticVariables.printLog("DETAILS", "DETAILS OF DEVICE PANEL :\n" + "RoomName : "
						+ Current_RoomName + "\n" + "RoomNo : "
						+ Current_RoomNo + "\n" + "DTYPE : " + Current_DevType
						+ "\n" + "DNO : " + Current_DevNo + "\n" + "DID : "
						+ Current_DevID + "\n" + "DEVNAME : " + Current_DevName
						+ "\n" + "WDNO : " + Current_WRLS_DevNo + "\n"
						+ "WDTYPE: " + Current_WRLS_DevType + "\n" + "WDID: "
						+ Current_WRLS_DevID + "\n" + "WDNAME : "
						+ Current_WRLS_DevName + "\n" + "WRoomName : "
						+ Current_WRLS_RoomName + "\n"+ "GroupID : "+Current_DevGroupId+"\n");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}


		// getting panel switch data based on panel type 

		switch (devTypes.valueOf(Current_WRLS_DevName)) {

		case WD01:{

			PanelSwitchesList=new String[3];
			PanelSwitchesList[0]="Brightness Decrease";
			PanelSwitchesList[1]="ON/OFF";
			PanelSwitchesList[2]="Brightness Increase";

			//filling panel command list for DIMMER type A 
			Switch_CMDS.add(DMR_Brightness_Minus); 
			Switch_CMDS.add(DMR_ON_OFF_CMD); 
			Switch_CMDS.add(DMR_Brightness_Plus);

			break;
		}
		/*	
		case WD01:

			PanelSwitchesList=new String[6];
			PanelSwitchesList[0]="ON/OFF";
			PanelSwitchesList[1]="LOW";
			PanelSwitchesList[2]="MEDIUM";
			PanelSwitchesList[3]="HIGH";
			PanelSwitchesList[4]="Brightness Increase";
			PanelSwitchesList[5]="Brightness Decrease";

			//filling panel command list for DIMMER A 
			Switch_CMDS.add(DMR_ON_OFF_CMD); 
			Switch_CMDS.add(DMR_LOW_CMD);
			Switch_CMDS.add(DMR_MEDIUM_CMD);
			Switch_CMDS.add(DMR_HIGH_CMD); 
			Switch_CMDS.add(DMR_Brightness_Plus);
			Switch_CMDS.add(DMR_Brightness_Minus); 

			break;

		 case WD02:
			PanelSwitchesList=new String[5];
			PanelSwitchesList[0]="ON/OFF";
			PanelSwitchesList[1]="LOW";
			PanelSwitchesList[2]="MEDIUM";
			PanelSwitchesList[3]="HIGH";  
			PanelSwitchesList[4]="BRIGHTNESS SPINNER";  

			//filling panel command list for DIMMER B 
			Switch_CMDS.add(DMR_ON_OFF_CMD);    
			Switch_CMDS.add(DMR_LOW_CMD);
			Switch_CMDS.add(DMR_MEDIUM_CMD);
			Switch_CMDS.add(DMR_HIGH_CMD);

			Switch_CMDS.add(DMR_1_CMD);
			Switch_CMDS.add(DMR_2_CMD);
			Switch_CMDS.add(DMR_3_CMD);
			Switch_CMDS.add(DMR_4_CMD);
			Switch_CMDS.add(DMR_5_CMD);
			Switch_CMDS.add(DMR_6_CMD);
			Switch_CMDS.add(DMR_7_CMD);
			Switch_CMDS.add(DMR_8_CMD);
			Switch_CMDS.add(DMR_9_CMD);
			Switch_CMDS.add(DMR_10_CMD);

			break;
		case WD03:
			PanelSwitchesList=new String[3];
			PanelSwitchesList[0]="ON/OFF"; 
			PanelSwitchesList[1]="Brightness Increase";
			PanelSwitchesList[2]="Brightness Decrease"; 

			//filling panel command list for DIMMER C 
			Switch_CMDS.add(DMR_ON_OFF_CMD); 
			Switch_CMDS.add(DMR_Brightness_Plus);
			Switch_CMDS.add(DMR_Brightness_Minus);  

			break;

		 */
		/*		
		case WR01:{
			PanelSwitchesList=new String[5];

			PanelSwitchesList[0]="Brightness Increase";
			PanelSwitchesList[1]="Brightness Decrease";
			PanelSwitchesList[2]="ON/OFF";
			PanelSwitchesList[3]="EFFECTS"; 
			PanelSwitchesList[4]="COLOURS";  


			//filling command list for RGB A
			Switch_CMDS.add(RGB_Brightness_Plus); 
			Switch_CMDS.add(RGB_Brightness_Minus);
			Switch_CMDS.add(RGB_ON_OFF_CMD);  
			Switch_CMDS.add(RGB_MODES_CMD);  
			Switch_CMDS.add(RGB_COLORS_CMD); 

			break;
		}

		 */
		case WR01:{

			/*
			PanelSwitchesList=new String[7];

			PanelSwitchesList[0]="Brightness Increase"; 
			PanelSwitchesList[1]="MODE"; 
			PanelSwitchesList[2]="Brightness Decrease";
			PanelSwitchesList[3]="ON/OFF";
			PanelSwitchesList[4]="RED";  
			PanelSwitchesList[5]="GREEN";  
			PanelSwitchesList[6]="BLUE";  

			//filling command list for RGB A
			Switch_CMDS.add(RGB_Brightness_Plus);  
			Switch_CMDS.add(RGB_MODES_CMD); 
			Switch_CMDS.add(RGB_Brightness_Minus);
			Switch_CMDS.add(RGB_ON_OFF_CMD);  
			Switch_CMDS.add(RGB_RED_CMD); 
			Switch_CMDS.add(RGB_GREEN_CMD); 
			Switch_CMDS.add(RGB_BLUE_CMD); 

			 */

			PanelSwitchesList=new String[6];

			PanelSwitchesList[0]="Brightness Increase"; 
			PanelSwitchesList[1]="MODE"; 
			PanelSwitchesList[2]="Brightness Decrease"; 
			PanelSwitchesList[3]="RED";  
			PanelSwitchesList[4]="GREEN";  
			PanelSwitchesList[5]="BLUE";  

			//filling command list for RGB A
			Switch_CMDS.add(RGB_Brightness_Plus);  
			Switch_CMDS.add(RGB_MODES_CMD); 
			Switch_CMDS.add(RGB_Brightness_Minus);  
			Switch_CMDS.add(RGB_RED_CMD); 
			Switch_CMDS.add(RGB_GREEN_CMD); 
			Switch_CMDS.add(RGB_BLUE_CMD); 

			break;
		}

		/*
		case WR01:
			PanelSwitchesList=new String[11];
			PanelSwitchesList[0]="ON/OFF"; 
			PanelSwitchesList[1]="RED"; 
			PanelSwitchesList[2]="GREEN"; 
			PanelSwitchesList[3]="BLUE"; 
			PanelSwitchesList[4]="WHITE";  
			PanelSwitchesList[5]="Brightness Increase";
			PanelSwitchesList[6]="Brightness Decrease";
			PanelSwitchesList[7]="COLOURS"; 
			PanelSwitchesList[8]="EFFECTS"; 
			PanelSwitchesList[9]="Speed Increase";
			PanelSwitchesList[10]="Speed Decrease";


			//filling command list for RGB A
			Switch_CMDS.add(RGB_ON_OFF_CMD); 
			Switch_CMDS.add(RGB_RED_CMD);
			Switch_CMDS.add(RGB_GREEN_CMD);
			Switch_CMDS.add(RGB_BLUE_CMD);
			Switch_CMDS.add(RGB_WHITE_CMD); 
			Switch_CMDS.add(RGB_Brightness_Plus);
			Switch_CMDS.add(RGB_Brightness_Minus); 
			Switch_CMDS.add(RGB_COLORS_CMD);
			Switch_CMDS.add(RGB_MODES_CMD); 
			Switch_CMDS.add(RGB_Speed_Plus);
			Switch_CMDS.add(RGB_Speed_Minus);

			break;
		 */

		/*case WR02:
			PanelSwitchesList=new String[12];
			PanelSwitchesList[0]="ON/OFF"; 

			PanelSwitchesList[1]="FLASH";
			PanelSwitchesList[2]="SMOOTH";
			PanelSwitchesList[3]="STROBE";
			PanelSwitchesList[4]="FADE";

			PanelSwitchesList[5]="RED";
			PanelSwitchesList[6]="GREEN";
			PanelSwitchesList[7]="BLUE"; 
			PanelSwitchesList[8]="WHITE";
			PanelSwitchesList[9]="Speed Increase"; 
			PanelSwitchesList[10]="Speed Decrease";
			PanelSwitchesList[11]="BRIGHTNESS SLIDER";


			//filling command list for RGB B
			Switch_CMDS.add(RGB_ON_OFF_CMD); 
			Switch_CMDS.add(RGB_FLASH_CMD); 
			Switch_CMDS.add(RGB_SMOOTH_CMD); 
			Switch_CMDS.add(RGB_STROBE_CMD); 
			Switch_CMDS.add(RGB_FADE_CMD); 

			Switch_CMDS.add(RGB_RED_CMD);
			Switch_CMDS.add(RGB_GREEN_CMD);
			Switch_CMDS.add(RGB_BLUE_CMD);
			Switch_CMDS.add(RGB_WHITE_CMD);
			Switch_CMDS.add(RGB_Speed_Plus);
			Switch_CMDS.add(RGB_Speed_Minus);

			Switch_CMDS.add(RGB_Brightness_Plus);

			break; 
		 */
		case WC01:{
			PanelSwitchesList=new String[3];
			PanelSwitchesList[0]="CLOSE";  
			PanelSwitchesList[1]="STOP";
			PanelSwitchesList[2]="OPEN";

			//filling command list for CUR SINGLE
			Switch_CMDS.add(CUR1_CLOSE_CMD); 
			Switch_CMDS.add(CUR1_STOP_CMD); 
			Switch_CMDS.add(CUR1_OPEN_CMD); 
			break;
		}
		case WC02:{
			PanelSwitchesList=new String[6];


			PanelSwitchesList[0]="Curtain1 OPEN";
			PanelSwitchesList[1]="Curtain1 STOP";
			PanelSwitchesList[2]="Curtain1 CLOSE"; 


			PanelSwitchesList[3]="Curtain2 OPEN";
			PanelSwitchesList[4]="Curtain2 STOP";
			PanelSwitchesList[5]="Curtain2 CLOSE"; 


			//filling command list for CUR SINGLE

			Switch_CMDS.add(CUR1_OPEN_CMD);  
			Switch_CMDS.add(CUR1_STOP_CMD); 
			Switch_CMDS.add(CUR1_CLOSE_CMD); 

			Switch_CMDS.add(CUR2_OPEN_CMD);  
			Switch_CMDS.add(CUR2_STOP_CMD); 
			Switch_CMDS.add(CUR2_CLOSE_CMD); 

			break;
		}
		case WF01:{
			PanelSwitchesList=new String[3];
			PanelSwitchesList[0]="Speed Decrease";  
			PanelSwitchesList[1]="ON/OFF";
			PanelSwitchesList[2]="Speed Increase";

			//filling command list for FAN SINGLE
			Switch_CMDS.add(FAN1_MINUS_CMD); 
			Switch_CMDS.add(FAN1_ON_OFF_CMD); 
			Switch_CMDS.add(FAN1_PLUS_CMD); 
			break;
		}
		case WF02:{
			PanelSwitchesList=new String[6]; 
			PanelSwitchesList[0]="Fan1 Speed Decrease";  
			PanelSwitchesList[1]="Fan1 ON/OFF";
			PanelSwitchesList[2]="Fan1 Speed Increase";

			PanelSwitchesList[3]="Fan2 Speed Decrease";  
			PanelSwitchesList[4]="Fan2 ON/OFF";
			PanelSwitchesList[5]="Fan2 Speed Increase";

			//filling command list for FAN1  
			Switch_CMDS.add(FAN1_MINUS_CMD); 
			Switch_CMDS.add(FAN1_ON_OFF_CMD);  
			Switch_CMDS.add(FAN1_PLUS_CMD); 


			//filling command list for FAN2
			Switch_CMDS.add(FAN2_MINUS_CMD);
			Switch_CMDS.add(FAN2_ON_OFF_CMD); 
			Switch_CMDS.add(FAN2_PLUS_CMD); 

			break;
		}

		/*

		case WGS1:{
			PanelSwitchesList=new String[2]; 
			PanelSwitchesList[0]="Geyser ON"; 
			PanelSwitchesList[1]="Geyser OFF"; 

			//filling command list for Geyser
			Switch_CMDS.add(GEYSER_ON_CMD);
			Switch_CMDS.add(GEYSER_OFF_CMD);

			break;
		}
		case WBS1:{
			PanelSwitchesList=new String[1]; 
			PanelSwitchesList[0]="Ring Bell";  

			//filling command list for Bell
			Switch_CMDS.add(BELL_RING_CMD); 

			break;
		}

		 */
		default:
			break;
		} 


		// loading data to list adapter
		adapter = new ListadApter_ir(this, PanelSwitchesList, ItemListView);
		// setting adapter to list view
		PanelSwitch.setAdapter(adapter);

		boolean isConfigured=isPanelConfiguredToSwitch();
		if(isConfigured){
			deletSetting_btn.setEnabled(true);
			ErrorDialog("Alert","Panel Already Configured To Selected Device");
		}
		// calling connection in activity
		RegainingConnection();

	}

	// method to get auto TCP connection in class
	void RegainingConnection() {
		// starting a background thread to make connection
		Thread thread = new Thread() {
			public void run() {
				isTcpConnecting=true;
				IS_SERVER_CONNECTED=Tcp.EstablishConnection(TcpHandler);
				runOnUiThread(new Runnable() {
					public void run() 
					{
						if(IS_SERVER_CONNECTED){
							//serverStatus.setImageResource(server_online);	
							StaticVariables.printLog("TAG","CONNECTED");

						}else{
							//serverStatus.setImageResource(server_offline);	
							StaticVariables.printLog("TAG","NOT CONNECTED");
						}
						isTcpConnecting=false;
					}
				});

			}
		};
		thread.start();
	} 

	// TCP handler for handling TCP responses
	private Handler TcpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case READ_LINE: {
				final String Data = (String) msg.obj;
				StaticVariables.printLog("TCP RESPONSE", "DATA GET FROM TCP SOCKET :" + Data);

				break;
			}

			case READ_BYTE: {

				break;
			}

			case TCP_LOST: {
				runOnUiThread(new Runnable() {
					public void run() {
						//serverStatus.setImageResource(server_offline);
					}
				});
				break;
			}
			}
		}
	};

	// Delete Warning dialog
	void DeleteWarningDialog() {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						WirelessStaticPanelConfiguration.this,
						AlertDialog.THEME_HOLO_DARK);
				dialog.setTitle("Confirm Delete ");
				dialog.setMessage("Do You Really Want To Delete Panel Details ?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// dismiss the dialog
						dialog.dismiss();
					}
				});
				dialog.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// calling delete action
						DeleteSettings();
						// dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}

	// error dialog
	void ErrorDialog(final String title, final String msg) {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(
						WirelessStaticPanelConfiguration.this,
						AlertDialog.THEME_HOLO_DARK);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK",
						new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						// dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}

	//delete succes dialog
	void alertDialog(final String title, final String msg) {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(WirelessStaticPanelConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}


	// method to delete Existing Panel Settings from db
	void DeleteSettings() {
		try {

			boolean isDeleted = WhouseDB.DeleteStaticPanelDevice(
					Current_WRLS_DevNo, Current_DevNo);

			if (isDeleted) { 
				// displaying success dialog
				deletSetting_btn.setEnabled(false);

				// displaying success dialog
				alertDialog("Deleted", "Selected Panel Switch Settings Deleted Successfully!");


			} else {
				// displaying error dialog
				ErrorDialog("Delete Failed",
						"Delete Operation Failed.Please Try Again.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to check if device is configured or not to current panel
	boolean isPanelConfiguredToSwitch() {
		boolean isPanelPreConfigured=false;
		try {
			// checking if panel already configured with current device
			isPanelPreConfigured =WhouseDB.isPanelConfiguredToDevice(
					Current_WRLS_DevNo, Current_DevNo);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return isPanelPreConfigured;
	}

	// device type collection
	enum devTypes {
		WS01,WS02,WS03,WS04,WS05,WS06,WC01,WC02,WF01,WF02,
		WD01,WDD1,WR01,WZ01,WSS1,WBS1,WGS1,WPS1,WNFC 
	}


	// method to insert data in db
	void insertDbData() {

		boolean inserted=false;
		String DevSwitchNumber=null;
		int loopCount=Switch_CMDS.size();
		int SwitchNo=0,PanelSwitchNumber = 0 ;

		for(int i=0;i<loopCount;i++){
			// getting current panel switch number
			try {
				SwitchNo=(i+1);
				DevSwitchNumber=""+SwitchNo;
				PanelSwitchNumber = Integer.parseInt(PANEL_SWB_ID+ SwitchNo);
				//inserting data to database
				inserted=insertFinalData(PanelSwitchNumber, 
						DevSwitchNumber, Switch_CMDS.get(i));

			} catch (Exception e) {
				e.printStackTrace();
				break;
			}	

			if(inserted && i==(loopCount-1)){
				//post result back to calling activity
				Intent it = getIntent(); 
				setResult((Activity.RESULT_OK), it); 
				finish();
				
			}else if(!inserted){
				ErrorDialog("Configuration Failed", "Unable To Configure The Wireless Panel.");
				break;
			}
		}

	}

	//method to insert data in database
	boolean insertFinalData(int PanelSwitchNumber,String SwitchNumber,String switchData){
		boolean isInserted =false;
		// inserting record in database


		try { 

			//making group id of 3 chars
			while(Current_DevGroupId.length()<3){
				Current_DevGroupId="0"+Current_DevGroupId;
			}

			//making devno of 4 chars
			String dno=(""+Current_DevNo);
			while(dno.length()<4){
				dno="0"+dno;
			}

			//making room no of 2 chars
			String rno=(""+Current_RoomNo);
			while(rno.length()<2){
				rno="0"+rno;
			}

			// making data format based on command selected for device type
			if(switchData.length()==3){
				//making final data format for other device types
				String format = "0"+"01"+Current_DevGroupId+dno+rno+switchData+"000000000000000"; 
				switchData=format;


			}
			else{
				// making final data format for rgb device i.e
				// for colors  
				//String format = "0"+"01"+Current_DevGroupId+dno+rno+switchData+"000000";

				String format = "0" + "01" + Current_DevGroupId + dno + rno + RGB_COLOR_PROCESS_CMD	+ switchData + "000";
				switchData=format;

			}


			if(switchData.length()==30){

				isInserted = WhouseDB.insertOrUpdateStaticpanelData(
						Current_RoomNo, Current_RoomName,
						Current_WRLS_DevName, Current_WRLS_DevNo,
						Current_WRLS_DevType, Current_WRLS_DevID,
						Current_DevName,"DefaultData", PanelSwitchNumber,
						switchData, Current_DevType, Current_DevNo,
						Current_DevID, SwitchNumber,
						Current_WRLS_RoomName);
			}else{
				msg("Incorrect Data Format Settings!");
				return isInserted;
			}


		} catch (Exception e) {
			e.printStackTrace();
		}

		return isInserted;
	}


	// ************************ click events ****************************//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.savePanelSettings: {
			// inserting details
			insertDbData();

			break;
		}
		case R.id.DeletePanelSettings: { 
			DeleteWarningDialog();
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
		/*case R.id.imageView2:{
			goPrevious();
			break;
		}*/
		case R.id.cancelPanelSettings: {
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


	// destroying activity
	@Override
	protected void onDestroy() {
		super.onDestroy();
		// closing database
		WhouseDB.close();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (WirelessConfigurationAdapter.sdb.isOpen()) {
			WhouseDB.close();
			StaticVariables.printLog("TAG", "DB CLOSED ");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		if (!WirelessConfigurationAdapter.sdb.isOpen()) {
			WhouseDB.open();

			StaticVariables.printLog("TAG", "DB open ");
		}
	}


	//go back
	void goPrevious(){ 
		finish();
	}

	//Back Press Event
	@Override
	public void onBackPressed() {
		goPrevious();
	}	

	
}
