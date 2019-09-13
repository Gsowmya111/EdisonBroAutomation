package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: WirelessPanelFinalConfiguration.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to configure devices across wireless panel selected.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class WirelessPanelFinalConfiguration extends Activity implements OnClickListener, OnItemClickListener {

	Button saveSetting_btn, deletSetting_btn,CancelSwitch;

	int Current_RoomNo = 0, Current_DevNo = 0, Current_WRLS_DevNo = 0, Current_WRLS_DevType = 0;

	String Current_RoomName = null, Current_WRLS_DevName = null, Current_WRLS_RoomName = null,
			Current_WRLS_DevID = null, Current_DevName = null, Current_DevType = null, Current_DevID = null,
			Current_DevGroupId = null,Current_Pirlightsensorval_WRLS="0" ,Current_Dev_Type_Name,Current_User_Dev_Name = null;

	ListView  DeviceSwitch ;
	String[] switchActionArray;
	ArrayList<String> DeviceSwitcheList = new ArrayList<String>();
	List<Model> ModelList = new ArrayList<Model>();

	final int READ_LINE = 1, READ_BYTE = 2, EXCEPTION = 3, TCP_LOST = 4, BT_LOST = 5;

	Tcp tcp = null;
	WirelessConfigurationAdapter WhouseDB = null;
	boolean IS_SERVER_CONNECTED = false;

	ListadApter_Toggle toggleAdapter;


	int itemclicked = R.drawable.wirles_set_backgroung_cb, itemunclicked = R.drawable.wirles_set_backgroung_cb1;

	int layoutID2 = R.layout.toggle_listview

			, server_online = R.drawable.connected, server_offline = R.drawable.not_connected;

	int PanelSwitchNo = -1, DeviceSwitchNo = -1;
	List<String> SwitchActionList, FanActionList, AllOnOffList, dimmerActionList, dimmerLevelList,drlActionList, curActionList,
			CallBellActionList, rgbActionList, rgbColour_EffectList, rgbBrightnessList, rgbSpeedList;
	int switchAction_ItemPostion = -1;

	String DEFAULT_SWITCH_ACTION_TEXT = "Set Action";

	//initialize fan speed  code logic will work for speed less than 10
	final int FAN_MAX_SPEED=4;


	AlertDialog dialog;
	boolean isDilaogOpened = false, isPanelPreConfigured = false;

	ArrayList<Boolean> finalSwitchStatusList = null;
	int FAN_COUNT = 0, SWB_COUNT = 0, FAN1_VALUE = 0, FAN2_VALUE = 0, FAN3_VALUE = 0, FAN4_VALUE = 0;

	// *********************************SWB COMMANDS******************************//

	// original format eg. ON="201" OFF="301" ON/OFF="101"

	// here only defining initial 1 char of command
	final String SWITCH_ON_START_CHAR = "2", SWITCH_OFF_START_CHAR = "3", SWITCH_ON_OFF_START_CHAR = "1";

	// here only defining initial 2 char of command
	final String SWITCH_ON_CMD = "20", SWITCH_OFF_CMD = "30", SWITCH_ON_OFF_CMD = "10";

	final String FAN1_ON_CMD = "722", FAN1_OFF_CMD = "723", FAN1_ON_OFF_CMD = "700",FAN1_PLUS_CMD = "720",
			FAN1_MINUS_CMD = "721", FAN2_ON_CMD = "752",
			FAN2_OFF_CMD = "753", FAN2_ON_OFF_CMD = "730", FAN3_ON_CMD = "782", FAN3_OFF_CMD = "783",
			FAN3_ON_OFF_CMD = "760", FAN4_ON_CMD = "812", FAN4_OFF_CMD = "813", FAN4_ON_OFF_CMD = "790";

	final String FAN1_SPEED = "71", FAN2_SPEED = "74", FAN3_SPEED = "77", FAN4_SPEED = "80";

	// FAN_SPEED_1="701",FAN_SPEED_9="709";
	final String ALL_ON_CMD = "901", ALL_OFF_CMD = "902";

	//door lock cmd
	final String DOOR_LOCK_CMD="201";

	// *********************************DMR COMMANDS********************************//

	final String DMR_ON_CMD = "102", DMR_OFF_CMD = "103", DMR_ON_OFF_CMD = "101";

	final String DMR_BRIGHTNESS_PROCESS_CMD = "112";

	final String DMR_HIGH_CMD = "255", DMR_MEDIUM_CMD = "040", DMR_LOW_CMD = "002";

	final String DMR_1_CMD = "003", DMR_2_CMD = "015", DMR_3_CMD = "030", DMR_4_CMD = "050", DMR_5_CMD = "071",
			DMR_6_CMD = "080", DMR_7_CMD = "100", DMR_8_CMD = "150", DMR_9_CMD = "200", DMR_10_CMD = "250";

	// *********************************RGB COMMANDS*******************************//

	final String RGB_ON_CMD = "102", RGB_OFF_CMD = "103", RGB_ON_OFF_CMD = "101";

	final String RGB_COLOR_PROCESS_CMD = "112";

	final String RGB_FLASH_CMD = "104", RGB_STROBE_CMD = "105", RGB_SMOOTH_CMD = "106", RGB_FADE_CMD = "107";

	final String RGB_BRIGHTNESS_START_CHAR_CMD = "13", RGB_BRIGHTNESS10_CMD = "140";
	// RGB_BRIGHTNESS1_CMD="0131" TO RGB_BRIGHTNESS10_CMD="0140"

	final String RGB_SPEED_START_CHAR_CMD = "12", RGB_SPEED10_CMD = "130";
	// RGB_SPEED1_CMD="121" TO RGB_SPEED10_CMD="130" i.e low to high

	final String RGB_RED_CMD = "255000000000", RGB_GREEN_CMD = "000255000000", RGB_BLUE_CMD = "000000255000",
			RGB_ORANGE_CMD = "255165000000", RGB_WHITE_CMD = "255255255000", RGB_PINK_CMD = "255000128000";

	// *********************************CUR COMMANDS******************************************//

	final String CUR1_OPEN_CMD = "101", CUR1_CLOSE_CMD = "102", CUR1_STOP_CMD = "103";
	final String CUR2_OPEN_CMD = "105", CUR2_CLOSE_CMD = "106", CUR2_STOP_CMD = "107";

	String PANEL_SWB_ID = "10";
	final String SWB_ALL_ON_OFF_TEXT = "ALL ON/OFF", SWB_FAN_TEXT1 = "Fan 1", SWB_FAN_TEXT2 = "Fan 2",
			SWB_FAN_TEXT3 = "Fan 3", SWB_FAN_TEXT4 = "Fan 4", DMR_ON_OFF_TEXT = "Power Option",
			DMR_LEVEL_TEXT = "Brightness Level", RGB_ON_OFF_TEXT = "Power Option",
			RGB_COLOURS_EFFECTS_TEXT = "Colours & Effects", RGB_BRIGHTNESS_TEXT = "Brightness Level",
			RGB_SPEED_TEXT = "Transition Speed", CUR1_ACTION_TEXT = "Curtain1 Action",
			CUR2_ACTION_TEXT = "Curtain2 Action";

	String switchData = null;

	int PanelSwitchNumber = 0, DeviceSwitchNumber = 0;

	ArrayList<Integer> Device_SelectedPositions = new ArrayList<Integer>();

	HashMap<String, String> Switch_CMDS = new HashMap<String, String>();

	// creating two string builder instances to save
	// switch data and switch number data temporarly
	StringBuilder FINAL_SWITCH_DATA = new StringBuilder();
	StringBuilder FINAL_SWITCH_NUM_DATA = new StringBuilder();
	StringBuilder FINAL_SWITCH_LOCAL_REF_DATA = new StringBuilder();

	static volatile boolean isActionNotSelected = false;
	static boolean isTcpConnecting = false;
	String PIR_SENSOR_DEVTYPE = "WPS1", NFC_TAG_DEVTYPE = "WNFC",PIR_SENSOR_DEVTYPE_2 = "WPD1";

	String datatype="0";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow()
				.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.wireless_panel_final_configuration);

		DeviceSwitch = (ListView) findViewById(R.id.Device_Switches_list);
		saveSetting_btn = (Button) findViewById(R.id.savePanelSettings);
		deletSetting_btn = (Button) findViewById(R.id.DeletePanelSettings);
		CancelSwitch = (Button) findViewById(R.id.cancelPanelSettings);
		//navigateBack = (ImageView) findViewById(R.id.imageView2);

		// adding click listener
		saveSetting_btn.setOnClickListener(this);
		deletSetting_btn.setOnClickListener(this);
		CancelSwitch.setOnClickListener(this);
		// setting click listener for server status image
		//	serverStatus.setOnClickListener(this);
		//	navigateBack.setOnClickListener(this);

		// setting item click listener in list view
		DeviceSwitch.setOnItemClickListener(this);

		// making delete button disable
		deletSetting_btn.setEnabled(false);

		try {
			WhouseDB = new WirelessConfigurationAdapter(this);
			// opening wireless database
			WhouseDB.open();
		} catch (Exception e) {
			e.printStackTrace();
		}

		// get data from previous activity
		try {
			Intent it = getIntent();
			Current_RoomName = it.getStringExtra("RoomName");
			Current_RoomNo = it.getIntExtra("RoomNo", 0);
			Current_DevType = it.getStringExtra("DevType");
			Current_DevNo = it.getIntExtra("DevNo", 0);
			Current_DevID = it.getStringExtra("DevID");
			Current_DevName = it.getStringExtra("DevTypeName");
			Current_DevGroupId = it.getStringExtra("DeviceGroupId");
			Current_WRLS_DevNo = it.getIntExtra("DevNo_WRLS", 0);
			Current_WRLS_DevType = it.getIntExtra("DevType_WRLS", 0);
			Current_WRLS_RoomName = it.getStringExtra("RoomName_WRLS");
			Current_WRLS_DevID = it.getStringExtra("DevId_WRLS");
			Current_WRLS_DevName = it.getStringExtra("DevName_WRLS");

			PanelSwitchNo= it.getIntExtra("PanelSwitchNo_WRLS",-1);

			Current_Pirlightsensorval_WRLS = it.getStringExtra("Pirlightsensorval_WRLS");

			Current_User_Dev_Name = it.getStringExtra("curDevusrnam");


			Log.d("DETAILS", "DETAILS OF DEVICE PANEL :\n" + "RoomName : " + Current_RoomName + "\n" + "RoomNo : "
					+ Current_RoomNo + "\n" + "DTYPE : " + Current_DevType + "\n" + "DNO : " + Current_DevNo + "\n"
					+ "DID : " + Current_DevID + "\n" + "DEVNAME : " + Current_DevName + "\n" + "GroupID : "
					+ Current_DevGroupId + "\n" + "WDNO : " + Current_WRLS_DevNo + "\n" + "WDTYPE: "
					+ Current_WRLS_DevType + "\n" + "WDID: " + Current_WRLS_DevID + "\n" + "WDNAME : "
					+ Current_WRLS_DevName + "\n" + "WRoomName : " + Current_WRLS_RoomName + "\n"
					+ "Current_Pirlightsensorval_WRLS : " + Current_Pirlightsensorval_WRLS + "\n");

		} catch (Exception e) {
			e.printStackTrace();
		}

		// initialize final status list
		finalSwitchStatusList = new ArrayList<Boolean>();

		// fill switch's all commands in hash map
		fillHashMap();

		// clearing list before filling data into it
		DeviceSwitcheList.clear();

		// switching
		switch (devTypes.valueOf(Current_DevName)) {
			case SWD1:
				FillSWB_Data(5, 1);
			case S010:
				FillSWB_Data(1, 0);
				break;
			case S020:
				FillSWB_Data(2, 0);
				break;
			case S110:
				FillSWB_Data(2, 0);
				break;
			case S030:
				FillSWB_Data(3, 0);
				break;
			case S120:
				FillSWB_Data(3, 0);
				break;
			case S040:
				FillSWB_Data(4, 0);
				break;
			case S060:
				FillSWB_Data(6, 0);
				break;
			case S080:
				FillSWB_Data(8, 0);
				break;
			case S160:
				FillSWB_Data(7, 0);
				break;

			case S021:
				FillSWB_Data(2, 1);
				break;
			case S031:
				FillSWB_Data(3, 1);
				break;
			case S042:
				FillSWB_Data(4, 2);
				break;
			case S051:
				FillSWB_Data(5, 1);
				break;
			case S141:
				FillSWB_Data(5, 1);
				break;
			case S052:
				FillSWB_Data(5, 2);
				break;
			case S061:
				FillSWB_Data(6, 1);
				break;
			case S062:
				FillSWB_Data(6, 2);
				break;
			case S071:
				FillSWB_Data(7, 1);
				break;
			case S102:
				FillSWB_Data(10, 2);
				break;
			case S111:
				FillSWB_Data(11, 1);
				break;
			case SLT1: {
				FillSWB_Data(1, 0);
				break;
			}
			case SFN1: {
				// filling fan actions
				Fill_FanActionList_SWB();
				// single fan
				DeviceSwitcheList.add("Fan 1");
				break;
			}
			case DFN1: {
				// filling fan actions
				Fill_FanActionList_SWB();
				// dual fan
				DeviceSwitcheList.add("Fan 1");
				DeviceSwitcheList.add("Fan 2");
				break;
			}
			case DMR1: {
				// filling Dimmer action lists
				Fill_DeviceActionList_DMR();
				Fill_BrightnessListLevel_DMR();

				// Adding Data To List which is to be display
				DeviceSwitcheList.add(DMR_ON_OFF_TEXT);
				DeviceSwitcheList.add(DMR_LEVEL_TEXT);

				break;
			}
			case RGB1: {
				// Filling RGB action Lists
				Fill_DeviceActionList_RGB();
				Fill_Colour_EffectList_RGB();
				Fill_BrightnessListLevel_RGB();
				Fill_SpeedLevel_RGB();

				// Adding Data To List which is to be display in list view
				DeviceSwitcheList.add(RGB_ON_OFF_TEXT);
				DeviceSwitcheList.add(RGB_COLOURS_EFFECTS_TEXT);
				DeviceSwitcheList.add(RGB_SPEED_TEXT);
				DeviceSwitcheList.add(RGB_BRIGHTNESS_TEXT);

				break;
			}
			case CLSH:{
				// filling curtain action list
				Fill_ActionList_CUR();

				DeviceSwitcheList.add(CUR2_ACTION_TEXT);

				StaticVariabes_div.log("inside switching clsh","DETAILS");
				break;
			}
			case CLNR:
			case CLS1:
			case CRS1: {
				// filling curtain action list
				Fill_ActionList_CUR();

				// Adding Data To List which is to be display in list view
				DeviceSwitcheList.add(CUR1_ACTION_TEXT);

				break;
			}
			case CLD1:
			case CRD1: {
				// filling curtain action list
				Fill_ActionList_CUR();

				// Adding Data To List which is to be display in list view
				DeviceSwitcheList.add(CUR1_ACTION_TEXT);
				DeviceSwitcheList.add(CUR2_ACTION_TEXT);

				break;
			}
			case WPD1:
			case WPS1:
			case GSK1:
			case GSR1:
			case ACR1: {
				Fill_DeviceActionList_SWB();
				DeviceSwitcheList.add("Switch 1");

				break;
			}
			case CLB1: {
				// filling on and off commands for ac
				Fill_ActionList_CLB();
				DeviceSwitcheList.add("Bell Ring");
				break;
			}
			case PLC1:
			case PSC1:case SOSH :case SWG1 :case SLG1:{
				// filling projector screen/lifter action list
				Fill_ActionList_CUR();

				if (Current_DevName.equals("PSC1")||Current_DevName.equals("SOSH")||Current_DevName.equals("SWG1")
						||Current_DevName.equals("SLG1")) {
					// Adding Data To List which is to be display in list view
					DeviceSwitcheList.add("Projector Screen");
				} else {
					// Adding Data To List which is to be display in list view
					DeviceSwitcheList.add("Projector Lift");
				}

				break;
			}
			case DLS1 :{
				Fill_ActionList_DRL();
				DeviceSwitcheList.add("Door Lock");
				break;
			}
			default:
				break;
		}

		// loading data to list adapter
		for (int i = 0; i < DeviceSwitcheList.size(); i++) {
			// filling model class element list
			ModelList.add(new Model(DeviceSwitcheList.get(i)));
		}

		toggleAdapter = new ListadApter_Toggle(this, layoutID2, ModelList);
		// setting adapter to list view
		DeviceSwitch.setAdapter(toggleAdapter);



		if(Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE)||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){
			RefreshDeviceSwitchListView_PIR(PanelSwitchNo);
		}else
			RefreshDeviceSwitchListView(PanelSwitchNo);


		// calling connection in activity
		RegainingConnection();




	}

	// method to fill switch data in lists
	void FillSWB_Data(int switches, int fans) {
		// filling particular switch action lists
		if (switches > 0) {
			Fill_DeviceActionList_SWB();
			Fill_AllOnOffList_SWB();

			// getting count from Device Board
			for (int i = 0; i < switches; i++) {
				// adding number of switches in list view
				// according switch board type
				DeviceSwitcheList.add("Switch " + (i + 1));
			}
		}

		// checking for fan
		if (fans > 0) {
			Fill_FanActionList_SWB();
			for (int i = 0; i < fans; i++) {
				DeviceSwitcheList.add("Fan " + (i + 1));
			}
		}

		// if switches are greater than one adding all ON/OFF option
		if (switches > 1)
			DeviceSwitcheList.add("ALL ON/OFF");

	}

	// **********************SWB ACTION LIST************************//

	// Filling List with Switch Action Data
	private void Fill_DeviceActionList_SWB() {
		String[] switchactions;
		Resources res = getResources();
		if(Current_WRLS_DevName.equals("WPS1")||Current_WRLS_DevName.equals("WPD1")){
			switchactions = res.getStringArray(R.array.SwitchData_Wireless_pir);
		}else {
			switchactions = res.getStringArray(R.array.SwitchData_Wireless);
		}
		SwitchActionList = Arrays.asList(switchactions);
	}

	// Filling list with Fan Action data
	private void Fill_FanActionList_SWB() {
		FanActionList=new ArrayList<String>();

		for(int i=1;i<=FAN_MAX_SPEED;i++){
			FanActionList.add(String.valueOf(i));
		}
		if(Current_WRLS_DevName.equals("WPS1")||Current_WRLS_DevName.equals("WPD1")){
			FanActionList.add("ON");
			FanActionList.add("OFF");
		}else {
			FanActionList.add("ON");
			FanActionList.add("OFF");
			FanActionList.add("ON/OFF");
			FanActionList.add("PLUS");
			FanActionList.add("MINUS");
		}

	}

	// Filling list with all on/off actions
	private void Fill_AllOnOffList_SWB() {
		Resources res = getResources();
		String[] onoff = res.getStringArray(R.array.ALL_ON_OFF_Data_Wireless);
		AllOnOffList = Arrays.asList(onoff);
	}

	// **********************DMR ACTION LIST*******************************//

	// Filling List with Dimmer Action Data
	private void Fill_DeviceActionList_DMR() {
		Resources res = getResources();
		String[] switchactions = res.getStringArray(R.array.DMR_ACTIONS);
		dimmerActionList = Arrays.asList(switchactions);
	}

	// **********************FAN ACTION LIST*******************************//

	// Filling list with Fan Action data
	private void Fill_BrightnessListLevel_DMR() {
		Resources res = getResources();
		String[] brightnessLevel = res.getStringArray(R.array.DMR_BRIGHTNESS_LEVEL);
		dimmerLevelList = Arrays.asList(brightnessLevel);
	}

	// *********************RGB ACTION LIST*******************************//

	// Filling List with RGB Action Data
	private void Fill_DeviceActionList_RGB() {
		Resources res = getResources();
		String[] switchactions = res.getStringArray(R.array.RGB_ACTIONS);
		rgbActionList = Arrays.asList(switchactions);
	}

	// Filling list with rgb Brightness data
	private void Fill_BrightnessListLevel_RGB() {
		Resources res = getResources();
		String[] brightnessLevel = res.getStringArray(R.array.RGB_BRIGHTNESS_LEVEL);
		rgbBrightnessList = Arrays.asList(brightnessLevel);
	}

	// Filling list with rgb speed data
	private void Fill_SpeedLevel_RGB() {
		Resources res = getResources();
		String[] speedLevel = res.getStringArray(R.array.RGB_SPEED_LEVEL);
		rgbSpeedList = Arrays.asList(speedLevel);
	}

	// Filling list with rgb color & effects data
	private void Fill_Colour_EffectList_RGB() {
		Resources res = getResources();
		String[] colours = res.getStringArray(R.array.RGB_COLOURS_Effect);
		rgbColour_EffectList = Arrays.asList(colours);
	}

	// *********************CURTAIN ACTION LIST*************************//

	// Filling list with Curtain Action data
	private void Fill_ActionList_DRL() {
		Resources res = getResources();
		String[] actions = res.getStringArray(R.array.DoorLock_Actions);
		drlActionList = Arrays.asList(actions);
	}

	// *********************CURTAIN ACTION LIST*************************//

	// Filling list with Curtain Action data
	private void Fill_ActionList_CUR() {
		Resources res = getResources();
		String[] actions = res.getStringArray(R.array.CUR_ACTIONS);
		curActionList = Arrays.asList(actions);
	}

	// *********************GEYSER ACTION LIST*************************//

	// Filling list with Curtain Action data
	private void Fill_ActionList_CLB() {
		Resources res = getResources();
		String[] actions = res.getStringArray(R.array.CallBell_Actions);
		CallBellActionList = Arrays.asList(actions);
	}

	// method to get auto TCP connection in class
	void RegainingConnection() {
		// starting a background thread to make connection
		Thread thread = new Thread() {
			public void run() {
				isTcpConnecting = true;
				IS_SERVER_CONNECTED = Tcp.EstablishConnection(TcpHandler);
				runOnUiThread(new Runnable() {
					public void run() {
						if (IS_SERVER_CONNECTED) {
							//	serverStatus.setImageResource(server_online);
							Log.d("TAG", "CONNECTED");

						} else {
							//	serverStatus.setImageResource(server_offline);
							Log.d("TAG", "NOT CONNECTED");
						}
						isTcpConnecting = false;
					}
				});

			}
		};
		thread.start();
	}

	// method to set switch board hash map data
	void FillSWB_Hash_Data(int switches, int fans) {

		// storing original name of switch commands in hash array
		for (int i = 1; i <= switches; i++) {

			if (i >= 10) {
				Switch_CMDS.put(SWITCH_ON_START_CHAR + i, "ON");
				Switch_CMDS.put(SWITCH_OFF_START_CHAR + i, "OFF");
				Switch_CMDS.put(SWITCH_ON_OFF_START_CHAR + i, "ON/OFF");
			} else {
				Switch_CMDS.put(SWITCH_ON_CMD + i, "ON");
				Switch_CMDS.put(SWITCH_OFF_CMD + i, "OFF");
				Switch_CMDS.put(SWITCH_ON_OFF_CMD + i, "ON/OFF");
			}
		}

		if (fans > 0) {
			// filling fans data
			for (int j = 1; j <= fans; j++) {
				switch (j) {
					case 1: {
						Switch_CMDS.put(FAN1_ON_CMD, "ON");
						Switch_CMDS.put(FAN1_OFF_CMD, "OFF");
						Switch_CMDS.put(FAN1_ON_OFF_CMD, "ON/OFF");
						Switch_CMDS.put(FAN1_PLUS_CMD, "PLUS");
						Switch_CMDS.put(FAN1_MINUS_CMD, "MINUS");

						// fixing fan speed from 1-FAN_MAX_SPEED levels
						for (int i = 1; i<=FAN_MAX_SPEED; i++) {
							String speed = "" + i;
							Switch_CMDS.put(FAN1_SPEED + i, speed);
						}
						break;
					}
					case 2: {
						Switch_CMDS.put(FAN2_ON_CMD, "ON");
						Switch_CMDS.put(FAN2_OFF_CMD, "OFF");
						Switch_CMDS.put(FAN2_ON_OFF_CMD, "ON/OFF");

						// fixing fan speed from 1-<=FAN_MAX_SPEED levels
						for (int i = 1; i <=FAN_MAX_SPEED; i++) {
							String speed = "" + i;
							Switch_CMDS.put(FAN2_SPEED + i, speed);
						}
						break;
					}
					case 3: {
						Switch_CMDS.put(FAN3_ON_CMD, "ON");
						Switch_CMDS.put(FAN3_OFF_CMD, "OFF");
						Switch_CMDS.put(FAN3_ON_OFF_CMD, "ON/OFF");

						// fixing fan speed from 1- FAN_MAX_SPEED levels
						for (int i = 1; i <=FAN_MAX_SPEED; i++) {
							String speed = "" + i;
							Switch_CMDS.put(FAN3_SPEED + i, speed);
						}
						break;
					}
					case 4: {
						Switch_CMDS.put(FAN4_ON_CMD, "ON");
						Switch_CMDS.put(FAN4_OFF_CMD, "OFF");
						Switch_CMDS.put(FAN4_ON_OFF_CMD, "ON/OFF");

						// fixing fan speed from 1- FAN_MAX_SPEED levels
						for (int i = 1; i <=FAN_MAX_SPEED; i++) {
							String speed = "" + i;
							Switch_CMDS.put(FAN4_SPEED + i, speed);
						}
						break;
					}
					default:
						break;
				}
			}
		}

		if (switches > 1) {
			Switch_CMDS.put(ALL_ON_CMD, "ALL ON");
			Switch_CMDS.put(ALL_OFF_CMD, "ALL OFF");
		}
	}

	// method to fill data in hash map
	private void fillHashMap() {
		try {
			// clearing hash map before filling current wired device data
			Switch_CMDS.clear();

			switch (devTypes.valueOf(Current_DevName)) {
				case SWD1:
					FillSWB_Hash_Data(5, 1);
					break;
				case S010:
					FillSWB_Hash_Data(1, 0);
					break;
				case S020:
					FillSWB_Hash_Data(2, 0);
					break;
				case S030:
					FillSWB_Hash_Data(3, 0);
					break;
				case S040:
					FillSWB_Hash_Data(4, 0);
					break;
				case S060:
					FillSWB_Hash_Data(6, 0);
					break;
				case S080:
					FillSWB_Hash_Data(8, 0);
					break;
				case S021:
					FillSWB_Hash_Data(2, 1);
					break;
				case S031:
					FillSWB_Hash_Data(3, 1);
					break;
				case S042:
					FillSWB_Hash_Data(4, 2);
					break;
				case S051:
					FillSWB_Hash_Data(5, 1);
					break;
				case S052:
					FillSWB_Hash_Data(5, 2);
					break;
				case S061:
					FillSWB_Hash_Data(6, 1);
					break;
				case S062:
					FillSWB_Hash_Data(6, 2);
					break;
				case S071:
					FillSWB_Hash_Data(7, 1);
					break;
				case S102:
					FillSWB_Hash_Data(10, 2);
					break;
				case S111:
					FillSWB_Hash_Data(11, 1);
				case SLT1: {
					FillSWB_Hash_Data(1, 0);
					break;
				}
				case SFN1: {
					FillSWB_Hash_Data(0, 1);
					break;
				}
				case DFN1: {
					FillSWB_Hash_Data(0, 2);
					break;
				}
				case S110:
					FillSWB_Data(2, 0);
					break;

				case S120:
					FillSWB_Data(3, 0);
					break;

				case S160:
					FillSWB_Data(7, 0);
					break;


				case S141:
					FillSWB_Data(5, 1);
					break;
				case DMR1: {
					// storing original name of Dimmer commands in hash array

					Switch_CMDS.put(DMR_ON_CMD, "ON");
					Switch_CMDS.put(DMR_OFF_CMD, "OFF");
					Switch_CMDS.put(DMR_ON_OFF_CMD, "ON/OFF");

					Switch_CMDS.put(DMR_HIGH_CMD, "HIGH");
					Switch_CMDS.put(DMR_MEDIUM_CMD, "MEDIUM");
					Switch_CMDS.put(DMR_LOW_CMD, "LOW");

					Switch_CMDS.put(DMR_1_CMD, "1");
					Switch_CMDS.put(DMR_2_CMD, "2");
					Switch_CMDS.put(DMR_3_CMD, "3");
					Switch_CMDS.put(DMR_4_CMD, "4");
					Switch_CMDS.put(DMR_5_CMD, "5");
					Switch_CMDS.put(DMR_6_CMD, "6");
					Switch_CMDS.put(DMR_7_CMD, "7");
					Switch_CMDS.put(DMR_8_CMD, "8");
					Switch_CMDS.put(DMR_9_CMD, "9");
					Switch_CMDS.put(DMR_10_CMD, "10");

					break;
				}
				case RGB1: {
					// storing original name of RGB commands in hash array

					Switch_CMDS.put(RGB_ON_CMD, "ON");
					Switch_CMDS.put(RGB_OFF_CMD, "OFF");
					Switch_CMDS.put(RGB_ON_OFF_CMD, "ON/OFF");

					Switch_CMDS.put(RGB_FLASH_CMD, "FLASH");
					Switch_CMDS.put(RGB_STROBE_CMD, "STROBE");
					Switch_CMDS.put(RGB_SMOOTH_CMD, "SMOOTH");
					Switch_CMDS.put(RGB_FADE_CMD, "FADE");

					Switch_CMDS.put(RGB_RED_CMD, "RED");
					Switch_CMDS.put(RGB_GREEN_CMD, "GREEN");
					Switch_CMDS.put(RGB_BLUE_CMD, "BLUE");
					Switch_CMDS.put(RGB_PINK_CMD, "PINK");
					Switch_CMDS.put(RGB_ORANGE_CMD, "ORANGE");
					Switch_CMDS.put(RGB_WHITE_CMD, "WHITE");

					for (int i = 1; i <= 10; i++) {
						String value = "" + i;
						// setting brightness & speed text in hash map
						if (i != 10) {
							Switch_CMDS.put(RGB_BRIGHTNESS_START_CHAR_CMD + i, value);
							Switch_CMDS.put(RGB_SPEED_START_CHAR_CMD + i, value);
						} else {
							Switch_CMDS.put(RGB_BRIGHTNESS10_CMD, value);
							Switch_CMDS.put(RGB_SPEED10_CMD, value);
						}

					}

					break;
				}
				case CLNR:
				case CLS1:
				case CRS1:
				case PSC1:
				case PLC1:case SOSH : case SWG1 :case SLG1:{
					StaticVariabes_div.log("inside clnr","DETAILS");
					// using same commands for curtain and projector
					Switch_CMDS.put(CUR1_OPEN_CMD, "OPEN");
					Switch_CMDS.put(CUR1_CLOSE_CMD, "CLOSE");
					Switch_CMDS.put(CUR1_STOP_CMD, "STOP");

					break;
				}
				case CLSH:
				{
					StaticVariabes_div.log("inside clsh","DETAILS");
					Switch_CMDS.put(CUR2_OPEN_CMD, "OPEN");
					Switch_CMDS.put(CUR2_CLOSE_CMD, "CLOSE");
					Switch_CMDS.put(CUR2_STOP_CMD, "STOP");

					break;
				}
				case CLD1:
				case CRD1: {
					Switch_CMDS.put(CUR1_OPEN_CMD, "OPEN");
					Switch_CMDS.put(CUR1_CLOSE_CMD, "CLOSE");
					Switch_CMDS.put(CUR1_STOP_CMD, "STOP");

					Switch_CMDS.put(CUR2_OPEN_CMD, "OPEN");
					Switch_CMDS.put(CUR2_CLOSE_CMD, "CLOSE");
					Switch_CMDS.put(CUR2_STOP_CMD, "STOP");

					break;
				}
				case WPD1:
				case WPS1:
				case GSK1:
				case GSR1:
				case ACR1: {
					// using same commands for sprinkler and geyser command
					Switch_CMDS.put(SWITCH_ON_CMD + "1", "ON");
					Switch_CMDS.put(SWITCH_OFF_CMD + "1", "OFF");
					Switch_CMDS.put(SWITCH_ON_OFF_CMD + "1", "ON/OFF");
					break;
				}
				case CLB1: {
					// AC command
					Switch_CMDS.put(SWITCH_ON_CMD + "1", "ON");
					Switch_CMDS.put(SWITCH_OFF_CMD + "1", "OFF");
					break;
				}
				case DLS1: {
					// Door Lock command
					Switch_CMDS.put(DOOR_LOCK_CMD, "OPEN");
					break;
				}
				default:
					break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// TCP handler for handling TCP responses
	private Handler TcpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
				case READ_LINE: {
					final String Data = (String) msg.obj;
					Log.d("TCP RESPONSE", "DATA GET FROM TCP SOCKET :" + Data);

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
	void DeleteWarningDialog(final int panelSwitchno) {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(WirelessPanelFinalConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Confirm Delete ");
				dialog.setMessage("Do You Really Want To Delete Selected Panel's Switch Details ?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// dismiss the dialog
						dialog.dismiss();
					}
				});
				dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						if(Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE)||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){
							DeletSettings_PIR(panelSwitchno);
						}else{
							// calling delete action
							DeletSettings(panelSwitchno);
						}


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
				AlertDialog.Builder dialog = new AlertDialog.Builder(WirelessPanelFinalConfiguration.this,
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

	//delete succes dialog
	void alertDialog(final String title, final String msg) {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(WirelessPanelFinalConfiguration.this,
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


	// ************************ click events ****************************//
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.savePanelSettings: {

				boolean isDeviceItemSelected = false;

				StringBuilder sb = new StringBuilder();
				// making list clear
				Device_SelectedPositions.clear();

				// clearing status list
				finalSwitchStatusList.clear();

				// loop to fill selected item positions
				for (int i = 0; i < ModelList.size(); i++) {
					if (ModelList.get(i).isSelected()) {
						sb.append("\n" + (i + 1));
						// adding item position in list
						Device_SelectedPositions.add(i);
						finalSwitchStatusList.add(true);
						isDeviceItemSelected = true;
					} else {
						finalSwitchStatusList.add(false);
					}
				}
				Log.d("TAG", "Selected item in list : " + sb.toString());

				if (PanelSwitchNo != -1 && isDeviceItemSelected) {

					// inserting details
					insertIndividaulaCmdData();

				} else {
					msg("Please select atleast one option from list!");
				}

				break;
			}
			case R.id.DeletePanelSettings: {
				// int panelSwitchNo=PanelSwitchNo+1;
				DeleteWarningDialog(PanelSwitchNo);
				break;
			}
			case R.id.server_status: {
				if (!Tcp.tcpConnected) {
					if (!isTcpConnecting) {
						msg("connecting To Server...");
						RegainingConnection();
					} else {
						Log.d("TAG", "already Connecting tcp");
					}
				} else {
					msg("server is already connected");
				}
				break;
			}

			case R.id.cancelPanelSettings: {
				goPrevious();
				break;
			}
		}

	}

	// showing toast message
	void msg(final String text) {
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();

			}
		});
	}

	// method to delete Existing Panel Settings from db
	void DeletSettings(int panelSwitchno) {
		try {
			// appending panel switch id before switch number
			int panel_SwitchNumber = Integer.parseInt(PANEL_SWB_ID + (PanelSwitchNo + 1));
			boolean isDeleted = WhouseDB.DeletePanelSwitchDetails(Current_WRLS_DevNo, Current_DevNo,
					panel_SwitchNumber);

			if (isDeleted) {
				// displaying the current status of Device switches
				RefreshDeviceSwitchListView(panelSwitchno);
				// displaying success dialog
				alertDialog("Deleted", "Selected Panel Switch Settings Deleted Successfully!");
			} else {
				// displaying error dialog
				ErrorDialog("Delete Failed", "Delete Operation Failed.Please Try Again.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	void DeletSettings_PIR(int panelSwitchno) {
		try {
			// appending panel switch id before switch number
			int panel_SwitchNumber = Integer.parseInt(PANEL_SWB_ID + (PanelSwitchNo + 1));
			boolean isDeleted = WhouseDB.DeletePanelSwitchDetails_PIR(Current_WRLS_DevNo, Current_DevNo,
					panel_SwitchNumber,Current_Pirlightsensorval_WRLS);

			if (isDeleted) {
				// displaying the current status of Device switches
				RefreshDeviceSwitchListView_PIR(panelSwitchno);
				// displaying success dialog
				alertDialog("Deleted", "Selected Panel Switch Settings Deleted Successfully!");
			} else {
				// displaying error dialog
				ErrorDialog("Delete Failed", "Delete Operation Failed.Please Try Again.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to check if device is configured or not to current panel
	void isPanelConfiguredToSwitch() {
		try {
			// checking if panel already configured with current device
			isPanelPreConfigured = WhouseDB.isPanelConfiguredToDevice(Current_WRLS_DevNo, Current_DevNo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// method to check if device is configured or not to current panel PIR
	void isPanelConfiguredToSwitch_PIR() {
		try {
			// checking if panel already configured with current device
			isPanelPreConfigured = WhouseDB.isPanelConfiguredToDevice(Current_WRLS_DevNo, Current_DevNo);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// device type collection
	enum devTypes {
		SWD1 ,S010, S020,S141,S110,S120,S160, S030, S040, S060, S080,  S021, S031, S042, S051, S052, S061, S062, S071, S102, S111, SLT1, SFN1,
		DFN1, DMR1, RGB1, ACR1, WOTS, WMD1, WMC1, WMB1, WBSO, WSS1, WBM1, WSM1, GSR1, DLS1, CLNR, CLSH,
		CLS1, CLD1, CRS1, CRD1, IRB1, RPR1, CLB1, CSW1, EGM1, PSC1,SOSH, SLG1,SWG1,PLC1, GSK1, WPS1 ,WPD1 ,SDG1,
	}

	enum coloursAndEffects {
		RED, BLUE, GREEN, PINK, ORANGE, WHITE, FLASH, STROBE, SMOOTH, FADE
	}

	// method to insert data in db
	void insertIndividaulaCmdData() {
		try {
			// clearing string builder variables
			FINAL_SWITCH_DATA.delete(0, FINAL_SWITCH_DATA.length());
			FINAL_SWITCH_NUM_DATA.delete(0, FINAL_SWITCH_NUM_DATA.length());
			FINAL_SWITCH_LOCAL_REF_DATA.delete(0, FINAL_SWITCH_LOCAL_REF_DATA.length());

			// getting current panel switch number
			try {
				PanelSwitchNumber = Integer.parseInt(PANEL_SWB_ID + (PanelSwitchNo + 1));
			} catch (Exception e) {
				e.printStackTrace();
			}

			String WirelessSwitchData = null, LocalRefData = null;

			if (Device_SelectedPositions.size() > 0 && PanelSwitchNo != -1) {

				// loop to iterate switch numbers
				for (int k = 0; k < Device_SelectedPositions.size(); k++) {

					// getting switch number from list
					DeviceSwitchNo = Device_SelectedPositions.get(k);

					// making current switch number incremented by 1 to save for
					// db purpose
					// as index start from 0
					DeviceSwitchNumber = DeviceSwitchNo + 1;

					// getting data set on particular switch board's switch
					WirelessSwitchData = ModelList.get(DeviceSwitchNo).getButtonText();

					// getting name of switch
					String switchName = ModelList.get(DeviceSwitchNo).getName();

					if (!WirelessSwitchData.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
						// making group id of 3 chars
						while (Current_DevGroupId.length() < 3) {
							Current_DevGroupId = "0" + Current_DevGroupId;
						}

						// making devno of 4 chars
						String dno = ("" + Current_DevNo);
						while (dno.length() < 4) {
							dno = "0" + dno;
						}

						// making room no of 2 chars
						String rno = ("" + Current_RoomNo);
						while (rno.length() < 2) {
							rno = "0" + rno;
						}

						if((Current_WRLS_DevType==718)){
							datatype="4";
						}else
						if((Current_WRLS_DevType==720)){
							datatype="5";
						}else{
							datatype="6";
						}

						switch (devTypes.valueOf(Current_DevName)) {
							case SWD1:
							case S010:
							case S020:
							case S030:
							case S040:
							case S060:
							case S080:
							case S120:
							case S021:
							case S031:
							case S042:
							case S051:
							case S052:
							case S061:
							case S062:
							case S071:
							case S102:
							case S111:
							case SLT1:
							case SFN1:
							case DFN1:
							case S141:
							case S110:
							case S160:{

								Current_Dev_Type_Name="Swb";

								if (switchName.equals(SWB_FAN_TEXT1) || switchName.equals(SWB_FAN_TEXT2)
										|| switchName.equals(SWB_FAN_TEXT3) || switchName.equals(SWB_FAN_TEXT4)) {

									if (switchName.equals(SWB_FAN_TEXT1)) {

										// checking if data contains speed i.e 1-9
										// levels
										for (int i = 1; i < 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting fan1 on command and speed
												switchData = FAN1_SPEED + i;
											}
										}

										// checking if data is to make fan ON
										if (WirelessSwitchData.equals("ON")) {
											switchData = FAN1_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = FAN1_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = FAN1_ON_OFF_CMD;
										}else if (WirelessSwitchData.equals("PLUS")) {
											switchData = FAN1_PLUS_CMD;
										}else if (WirelessSwitchData.equals("MINUS")) {
											switchData = FAN1_MINUS_CMD;
										}

									} else if (switchName.equals(SWB_FAN_TEXT2)) {
										// checking if data contains speed i.e 1-9
										// levels
										for (int i = 1; i < 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting fan2 on command and speed
												switchData = FAN2_SPEED + i;
											}
										}

										// checking if data is to make fan ON
										if (WirelessSwitchData.equals("ON")) {
											switchData = FAN2_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = FAN2_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = FAN2_ON_OFF_CMD;
										}

									} else if (switchName.equals(SWB_FAN_TEXT3)) {
										// checking if data contains speed i.e 1-9
										// levels
										for (int i = 1; i < 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting fan3 on command and speed
												switchData = FAN3_SPEED + i;
											}
										}

										// checking if data is to make fan ON
										if (WirelessSwitchData.equals("ON")) {
											switchData = FAN3_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = FAN3_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = FAN3_ON_OFF_CMD;
										}

									} else if (switchName.equals(SWB_FAN_TEXT4)) {
										// checking if data contains speed i.e 1-9
										// levels
										for (int i = 1; i < 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting fan4 on command and speed
												switchData = FAN4_SPEED + i;
											}
										}

										// checking if data is to make fan ON
										if (WirelessSwitchData.equals("ON")) {
											switchData = FAN4_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = FAN4_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = FAN4_ON_OFF_CMD;
										}
									}

								} else if (switchName.equals(SWB_ALL_ON_OFF_TEXT)) {
									DeviceSwitchNumber = 0;
									if (WirelessSwitchData.equals("ALL ON")) {
										switchData = ALL_ON_CMD;
									} else if (WirelessSwitchData.equals("ALL OFF")) {
										switchData = ALL_OFF_CMD;
									}
								} else {

									if (WirelessSwitchData.equals("ON")) {
										if (DeviceSwitchNumber >= 10) {
											switchData = SWITCH_ON_START_CHAR + DeviceSwitchNumber;
										} else {
											switchData = SWITCH_ON_CMD + DeviceSwitchNumber;
										}

									} else if (WirelessSwitchData.equals("OFF")) {
										if (DeviceSwitchNumber >= 10) {
											switchData = SWITCH_OFF_START_CHAR + DeviceSwitchNumber;
										} else {
											switchData = SWITCH_OFF_CMD + DeviceSwitchNumber;
										}

									} else if (WirelessSwitchData.equals("ON/OFF")) {
										if (DeviceSwitchNumber >= 10) {
											switchData = SWITCH_ON_OFF_START_CHAR + DeviceSwitchNumber;
										} else {
											switchData = SWITCH_ON_OFF_CMD + DeviceSwitchNumber;
										}

									}
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// making final data format for swb/fan device
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;

								switchData = format;

								break;
							}
							case WPD1:
							case WPS1:
							case GSK1:
							case GSR1:
							case ACR1: {
								Current_Dev_Type_Name="other";
								if (WirelessSwitchData.equals("ON")) {
									switchData = SWITCH_ON_CMD + DeviceSwitchNumber;
								} else if (WirelessSwitchData.equals("OFF")) {
									switchData = SWITCH_OFF_CMD + DeviceSwitchNumber;
								} else if (WirelessSwitchData.equals("ON/OFF")) {
									switchData = SWITCH_ON_OFF_CMD + DeviceSwitchNumber;
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// making final data format
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;
								switchData = format;

								break;
							}
							case CLB1: {
								Current_Dev_Type_Name="other";
								if (WirelessSwitchData.equals("ON")) {
									switchData = SWITCH_ON_CMD + DeviceSwitchNumber;
								} else if (WirelessSwitchData.equals("OFF")) {
									switchData = SWITCH_OFF_CMD + DeviceSwitchNumber;
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// making final data format
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;
								switchData = format;

								break;
							}
							case DMR1: {
								Log.d("TAG", "SWITCH ACTION NAME : " + switchName);
								Current_Dev_Type_Name="other";
								// checking if power option is selected or not
								if (ModelList.get(0).isSelected()) {

									if (switchName.equals(DMR_LEVEL_TEXT)) {
										// checking if data contains Brightness
										// count level
										for (int i = 1; i <= 10; i++) {
											// checking if Brightness count is
											// selected
											if (WirelessSwitchData.equals("" + i)) {
												switch (i) {
													case 1: {
														// setting brightness level 1
														switchData = DMR_1_CMD;
														break;
													}
													case 2: {
														// setting brightness level 2
														switchData = DMR_2_CMD;
														break;
													}
													case 3: {
														// setting brightness level 3
														switchData = DMR_3_CMD;
														break;
													}
													case 4: {
														// setting brightness level 4
														switchData = DMR_4_CMD;
														break;
													}
													case 5: {
														// setting brightness level 5
														switchData = DMR_5_CMD;
														break;
													}
													case 6: {
														// setting brightness level 6
														switchData = DMR_6_CMD;
														break;
													}
													case 7: {
														// setting brightness level 7
														switchData = DMR_7_CMD;
														break;
													}
													case 8: {
														// setting brightness level 8
														switchData = DMR_8_CMD;
														break;
													}
													case 9: {
														// setting brightness level 9
														switchData = DMR_9_CMD;
														break;
													}
													case 10: {
														// setting brightness level 10
														switchData = DMR_10_CMD;
														break;
													}

												}

											}
										}
										// checking if data is to make fan ON
										if (WirelessSwitchData.equals("HIGH")) {
											switchData = DMR_HIGH_CMD;
										} else if (WirelessSwitchData.equals("MEDIUM")) {
											switchData = DMR_MEDIUM_CMD;
										} else if (WirelessSwitchData.equals("LOW")) {
											switchData = DMR_LOW_CMD;
										}
									} else {
										if (WirelessSwitchData.equals("ON")) {
											switchData = DMR_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = DMR_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = DMR_ON_OFF_CMD;
										}
									}
								} else {
									// Displaying Error Dialog
									ErrorDialog("Incomplete Details", "Please Select Atleast One Power Option !");

									// exiting from loop
									return;
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;
								String format = null;

								if (switchData.equals(DMR_ON_CMD) || switchData.equals(DMR_OFF_CMD)
										|| switchData.equals(DMR_ON_OFF_CMD)) {

									// making final data format for dimmer device
									format = "0" + "01" + Current_DevGroupId + dno + rno + switchData + "00000000000000"+datatype;
								} else {

									// making final data format for dimmer device
									// and adding brightness characters
									format = "0" + "01" + Current_DevGroupId + dno + rno + DMR_BRIGHTNESS_PROCESS_CMD
											+ switchData + "00000000000"+datatype;
								}

								switchData = format;

								break;
							}
							case RGB1: {
								Current_Dev_Type_Name="other";
								Log.d("TAG", "SWITCH ACTION NAME : " + switchName);
								// checking if power option is selected or not
								if (ModelList.get(0).isSelected()) {
									if (switchName.equals(RGB_COLOURS_EFFECTS_TEXT)) {

										switch (coloursAndEffects.valueOf(WirelessSwitchData)) {
											case RED: {
												switchData = RGB_RED_CMD;
												break;
											}
											case BLUE: {
												switchData = RGB_BLUE_CMD;
												break;
											}
											case GREEN: {
												switchData = RGB_GREEN_CMD;
												break;
											}
											case PINK: {
												switchData = RGB_PINK_CMD;
												break;
											}
											case ORANGE: {
												switchData = RGB_ORANGE_CMD;
												break;
											}
											case WHITE: {
												switchData = RGB_WHITE_CMD;
												break;
											}
											case FLASH: {
												switchData = RGB_FLASH_CMD;
												break;
											}
											case STROBE: {
												switchData = RGB_STROBE_CMD;
												break;
											}
											case SMOOTH: {
												switchData = RGB_SMOOTH_CMD;
												break;
											}
											case FADE: {
												switchData = RGB_FADE_CMD;
												break;
											}
										}

									} else if (switchName.equals(RGB_BRIGHTNESS_TEXT)) {
										// checking if data contains Brightness
										for (int i = 1; i <= 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting Rgb ON cmd and brightness
												// level
												if (i != 10) {
													switchData = RGB_BRIGHTNESS_START_CHAR_CMD + i;
												} else {
													switchData = RGB_BRIGHTNESS10_CMD;
												}
											}
										}
									} else if (switchName.equals(RGB_SPEED_TEXT)) {
										// checking if data contains speed
										for (int i = 1; i <= 10; i++) {
											if (WirelessSwitchData.equals("" + i)) {
												// setting Rgb ON command and speed
												// level
												if (i != 10) {
													switchData = RGB_SPEED_START_CHAR_CMD + i;
												} else {
													switchData = RGB_SPEED10_CMD;
												}
											}
										}
									} else {
										if (WirelessSwitchData.equals("ON")) {
											switchData = RGB_ON_CMD;
										} else if (WirelessSwitchData.equals("OFF")) {
											switchData = RGB_OFF_CMD;
										} else if (WirelessSwitchData.equals("ON/OFF")) {
											switchData = RGB_ON_OFF_CMD;
										}
									}
								} else {
									// Displaying Error Dialog
									ErrorDialog("Incomplete Details", "Please Select Atleast One Power Option !");

									// exiting from loop
									return;
								}
								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								String format = null;

								// making data format based on command selected for
								// rgb
								if (switchData.length() == 3) {
									// making final data format for rgb device
									format = "0" + "01" + Current_DevGroupId + dno + rno + switchData + "00000000000000"+datatype;

								} else {
									// making final data format for rgb device i.e
									// for colors
									format = "0" + "01" + Current_DevGroupId + dno + rno + RGB_COLOR_PROCESS_CMD
											+ switchData + "00"+datatype;

								}

								switchData = format;

								break;
							}
							case CLNR:
							case CLSH:
							case CLS1:
							case CRS1:
							case CLD1:
							case CRD1: {
								Current_Dev_Type_Name="other";
								// set command data for curtain 1
								if (switchName.equals(CUR1_ACTION_TEXT)) {
									if (WirelessSwitchData.equals("OPEN")) {
										switchData = CUR1_OPEN_CMD;
									} else if (WirelessSwitchData.equals("CLOSE")) {
										switchData = CUR1_CLOSE_CMD;
									} else if (WirelessSwitchData.equals("STOP")) {
										switchData = CUR1_STOP_CMD;
									}
								}
								// set command data for curtain 2
								if (switchName.equals(CUR2_ACTION_TEXT)) {
									if (WirelessSwitchData.equals("OPEN")) {
										switchData = CUR2_OPEN_CMD;
									} else if (WirelessSwitchData.equals("CLOSE")) {
										switchData = CUR2_CLOSE_CMD;
									} else if (WirelessSwitchData.equals("STOP")) {
										switchData = CUR2_STOP_CMD;
									}
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// setting data for curtain board
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;
								switchData = format;

								break;
							}

							case PSC1:
							case PLC1:
							case SOSH :case SWG1 :case SLG1:{
								Current_Dev_Type_Name="other";
								// using curtain data commands for projector screen
								// and projector lifter
								if (WirelessSwitchData.equals("OPEN")) {
									switchData = CUR1_OPEN_CMD;
								} else if (WirelessSwitchData.equals("CLOSE")) {
									switchData = CUR1_CLOSE_CMD;
								} else if (WirelessSwitchData.equals("STOP")) {
									switchData = CUR1_STOP_CMD;
								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// setting data for curtain board
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;
								switchData = format;

								break;
							}
							case DLS1 :{
								Current_Dev_Type_Name="other";
								if (WirelessSwitchData.equals("OPEN")) {
									switchData = DOOR_LOCK_CMD;

								}

								// assigning switch data to localRef variable for
								// status reflection purpose
								LocalRefData = switchData;

								// setting data for curtain board
								String format = "0" + "01" + Current_DevGroupId + dno + rno + switchData
										+ "00000000000000"+datatype;
								switchData = format;

								break;
							}
							default:
								Current_Dev_Type_Name="other";
								break;

						}

						if(Current_WRLS_DevName.equals("WPS1")&&Current_Dev_Type_Name.equals("Swb")){

							if (k < Device_SelectedPositions.size() - 1) {
								//FINAL_SWITCH_DATA.append(switchData + ",");
								FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber + ",");
								FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData + ",");


							} else {
								//FINAL_SWITCH_DATA.append(switchData);
								if(LocalRefData.equals("901")||LocalRefData.equals("902")) {

									FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber);
									FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData);
									String formatd = "0" + "01" + "000" + dno + rno + LocalRefData
											+ "00000000000000"+datatype;
									FINAL_SWITCH_DATA.append(formatd);

								}else{
									FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber);
									FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData);

									String SwitchNumbers = FINAL_SWITCH_NUM_DATA.toString();
									String local_ref_data_onoff = FINAL_SWITCH_LOCAL_REF_DATA.toString();

									StaticVariabes_div.log("SwitchNumbers" + SwitchNumbers + "local_ref_data_onoff" + local_ref_data_onoff, "wireless");


									String[] swbonoffArray = SwitchNumbers.split(",");
									String[] local_ref_data_onoff_array = local_ref_data_onoff.split(",");
									String fandata = null;
									//String[] fanvaluesArray = fandata.split(";");


									boolean bulbstatus[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
									boolean bulbstatus_offswitchs[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
									String typ = null;

									for (int t = 0; t < swbonoffArray.length; t++) {
										StaticVariabes_div.log("swbonoffarray" + swbonoffArray[t], "wireless");
										int bulbvalue;
										if (swbonoffArray != null) {

											bulbvalue = Integer.parseInt(swbonoffArray[t]);
											int position = (bulbvalue - 1);

											if (local_ref_data_onoff_array[t].startsWith("20"))
												bulbstatus[position] = true;
											else if (local_ref_data_onoff_array[t].startsWith("30"))
												bulbstatus_offswitchs[position] = true;
											else if (local_ref_data_onoff_array[t].startsWith("7"))
												fandata = local_ref_data_onoff_array[t];
										}

									}

									char c1 = processSwitchData(bulbstatus[0], bulbstatus[1], bulbstatus[2], bulbstatus[3]);
									StaticVariabes_div.log("c1=" + c1 + "--bulb1=" + bulbstatus[0] + "--bulb2=" + bulbstatus[1] + "--bulb3=" + bulbstatus[2] + "--bulb4=" + bulbstatus[3], "wireless");
									char c2 = processSwitchData(bulbstatus[4], bulbstatus[5], bulbstatus[6], bulbstatus[7]);
									char c3 = processSwitchData(bulbstatus[8], bulbstatus[9], bulbstatus[10], bulbstatus[11]);
									char f1 = '0';
									char f2 = '0';
									char f3 = '0';
									char f4 = '0';
									char fv1 = '0';
									char fv2 = '0';
									char fv3 = '0';
									char fv4 = '0';

									if ((fandata != null) && (fandata.length() > 0)) {

										if (fandata.equals("723")) {
											f1 = 'B';
										} else {
											f1 = fandata.charAt(2);
										}
									}

									typ = "on";
									String dat1 = ButtonOutprocess("" + Current_DevNo, c1, c2, c3, f1, f2, f3, f4, "" + PanelSwitchNumber, typ);


									char d1 = processSwitchData(bulbstatus_offswitchs[0], bulbstatus_offswitchs[1], bulbstatus_offswitchs[2], bulbstatus_offswitchs[3]);
									StaticVariabes_div.log("bulbstatus_offswitchs d1=" + d1 + "--bulb1=" + bulbstatus_offswitchs[0] + "--bulb2=" + bulbstatus_offswitchs[1] + "--bulb3=" + bulbstatus_offswitchs[2] + "--bulb4=" + bulbstatus_offswitchs[3], "wireless");
									char d2 = processSwitchData(bulbstatus_offswitchs[4], bulbstatus_offswitchs[5], bulbstatus_offswitchs[6], bulbstatus_offswitchs[7]);
									char d3 = processSwitchData(bulbstatus_offswitchs[8], bulbstatus_offswitchs[9], bulbstatus_offswitchs[10], bulbstatus_offswitchs[11]);
									char e1 = '0';
									char e2 = '0';
									char e3 = '0';
									char e4 = '0';
									char ev1 = '0';
									char ev2 = '0';
									char ev3 = '0';
									char ev4 = '0';

							/*	if ((fandata != null) || (fandata != "") || (fandata.equals(""))) {

									if (fandata.equals("0")) {
										f1 = 'B';
									} else {
										f1 = fandata.charAt(0);
									}
								}*/
									typ = "off";
									String dat2 = ButtonOutprocess("" + Current_DevNo, d1, d2, d3, e1, e2, e3, e4, "" + PanelSwitchNumber, typ);
									FINAL_SWITCH_DATA.append(dat1 + "," + dat2);

									StaticVariabes_div.log("dat1" + dat1, "wireless");
									StaticVariabes_div.log("dat2" + dat2, "wireless");

								}

							}



						}else {
							// checking if next record is there or not
							if (k < Device_SelectedPositions.size() - 1) {
								FINAL_SWITCH_DATA.append(switchData + ",");
								FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber + ",");
								FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData + ",");

							} else {
								FINAL_SWITCH_DATA.append(switchData);
								FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber);
								FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData);

							}
						}
						isActionNotSelected = false;

					} else {
						Log.d("TAG", "select some action for switch");

						// Displaying Error Dialog
						ErrorDialog("Incomplete Details", "Please Select Some Action For Selected Item!");

						isActionNotSelected = true;
						break;
					}
				}

				if (!isActionNotSelected) {
					Log.d("FINAL DATA TO SEND", "switch Data in start : " + FINAL_SWITCH_DATA.toString() + "\n"
							+ "switch num data : " + FINAL_SWITCH_NUM_DATA.toString());


					StaticVariabes_div.log("PanelSwitchNumber  " +PanelSwitchNumber,"wireless");
					// inserting record in database
					try {

						// converting string builder data in string format
						String SwitchData_WLS = FINAL_SWITCH_DATA.toString();
						String SwitchNumbers = FINAL_SWITCH_NUM_DATA.toString();
						String LocalDataRef = FINAL_SWITCH_LOCAL_REF_DATA.toString();

						boolean isInserted ;
						if(Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE)||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){

							// inserting panel configuration details PIR in db
							isInserted = WhouseDB.Update_WRLS_details_PIR(Current_RoomNo, Current_RoomName,
									Current_WRLS_DevName, Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID,
									Current_DevName, LocalDataRef, PanelSwitchNumber, SwitchData_WLS, Current_DevType,
									Current_DevNo, Current_DevID, SwitchNumbers, Current_WRLS_RoomName,Current_Pirlightsensorval_WRLS,Current_User_Dev_Name);
							StaticVariabes_div.log("Current_Pirlightsensorval_WRLS  " +Current_Pirlightsensorval_WRLS,"wireless");
						}else{
							// inserting panel configuration details in db
							isInserted = WhouseDB.Update_WRLS_details(Current_RoomNo, Current_RoomName,
									Current_WRLS_DevName, Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID,
									Current_DevName, LocalDataRef, PanelSwitchNumber, SwitchData_WLS, Current_DevType,
									Current_DevNo, Current_DevID, SwitchNumbers, Current_WRLS_RoomName,Current_User_Dev_Name);


						}
						if (isInserted) {

							//post result back to calling activity
							Intent i = getIntent();
							setResult((Activity.RESULT_OK), i);
							finish();

						} else {
							// Displaying Error Dialog
							ErrorDialog("Settings Not Saved",
									"Some Error Occured .Please Submit You Panel Setting Again!");

						}

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} else {
				// Displaying Error Dialog
				ErrorDialog("Incomplete Details",
						"Please select Atleast one Item from given List!");

			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}



	///////////////////////////////////////////////////////////////////

	static char processSwitchData(boolean sw1, boolean sw2, boolean sw3, boolean sw4)
	{
		String base2 = "" + ((sw4) ? 1 : 0) + ((sw3) ? 1 : 0) + ((sw2) ? 1 : 0) + ((sw1) ? 1 : 0);
		base2 = String.format("%02X", (0xFF & (Integer.parseInt(base2, 2))));
		return base2.charAt(1);
	}

	////////////////////////DELAY///////////////////////////////////////////////////////////////////
	public void delaytime(int delayval){
		try {
			Thread.sleep(delayval);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


	String ButtonOutprocess(String swbno,char cc1,char cc2,char cc3,char ff1,char ff2,char ff3,char ff4,String panelswitch,String type) {
		String str=null;
		StaticVariabes_div.log("button out process connest", "wireless");
		while (swbno.length() < 4)
			swbno = "0" + swbno;
		String rum_num=""+Current_RoomNo;
		while(rum_num.length()<2)rum_num="0"+rum_num;

		if(panelswitch.equals("101")) {

			if(type.equals("on")) {
				str = "0" + "01" + "000" + swbno + rum_num + "401" + cc1 + cc2 + cc3 + "00000" + ff1 + ff2 + ff3 + ff4 + "004";
				StaticVariabes_div.log("out swb/fan" + str, "wireless");
			}else{
				str = "0" + "01" + "000" + swbno + rum_num + "402" + cc1 + cc2 + cc3 + "00000" + ff1 + ff2 + ff3 + ff4 + "004";
				StaticVariabes_div.log("out swb/fan" + str, "wireless");
			}
		}else{

			if(type.equals("on")) {
				str = "0" + "01" + "000" + swbno + rum_num + "501" + cc1 + cc2 + cc3 + "00000" + ff1 + ff2 + ff3 + ff4 + "004";
				StaticVariabes_div.log("out swb/fan" + str, "wireless");
			}else{
				str = "0" + "01" + "000" + swbno + rum_num + "502" + cc1 + cc2 + cc3 + "00000" + ff1 + ff2 + ff3 + ff4 + "004";
				StaticVariabes_div.log("out swb/fan" + str, "wireless");
			}
		}
		return str;

	}

	// method to un check model item
	void unCheckModelItems() {
		// making list item unselected
		for (int s = 0; s < ModelList.size(); s++) {
			ModelList.get(s).setSelected(false);
			ModelList.get(s).setbuttontext(DEFAULT_SWITCH_ACTION_TEXT);
		}

	}

	// method to refresh device switch list view to show status of switches
	// corresponding to panel switch
	void RefreshDeviceSwitchListView(int panelSwitchNumber) {

		// checking panel status
		isPanelConfiguredToSwitch();

		Log.d("TAG", "PANEL CONFIGURED STS : " + isPanelPreConfigured);
		// checking if panel is configured to currently selected device
		if (isPanelPreConfigured) {

			// method to uncheck model items
			unCheckModelItems();

			try {
				String[] wirlessDataList = null;
				String WswitchData = null, DswitchNo = null;

				// getting current panel switch number and incrementing it by 1
				// as index starts from 0
				int currentPswitchno = 0;
				// getting current panel switch number
				try {
					currentPswitchno = Integer.parseInt(PANEL_SWB_ID + (panelSwitchNumber + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// fetching data from database corresponding to panel switch
				Cursor mcursor = WhouseDB.ConfiguredPanelPreDetails(Current_WRLS_DevNo, Current_DevNo,
						currentPswitchno);

				// checking if cursor have some data or not
				if (mcursor != null && mcursor.getCount() != 0) {
					// moving cursor to first position
					mcursor.moveToFirst();
					// initialize array size
					String[] DevSwitchNoList = new String[mcursor.getCount()];
					// fetching switch numbers already selected
					// for currently selected panel
					DswitchNo = mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.DevSwitchNo));

					Log.d("TAG", "Dev Switch Number : " + DswitchNo);

					// fetching switch actions selected for switches configured
					// to panel
					WswitchData = mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.DevLocalRef));

					Log.d("TAG", "WswitchData  : " + WswitchData);

					try {
						DevSwitchNoList = (DswitchNo.split(","));
						wirlessDataList = (WswitchData.split(","));

					} catch (Exception e) {
						e.printStackTrace();
					}

					int SwitchIndex = -1;
					String keyValue = null, key = null;
					for (int i = 0; i < DevSwitchNoList.length; i++) {
						try {
							SwitchIndex = Integer.parseInt(DevSwitchNoList[i]);
							if (SwitchIndex > 0) {
								// setting item based on index decremented by 1
								// as list-
								// view items index starts from 0
								ModelList.get(SwitchIndex - 1).setSelected(true);

								// getting key from list
								key = wirlessDataList[i];

								// fetching name (i.e key value ) of action
								// selected for item from map
								keyValue = Switch_CMDS.get(key);

								Log.d("TAG", "DATA set :" + key + " keyval :" + keyValue + " i value :" + i);

								// setting button text for selected item
								ModelList.get(SwitchIndex - 1).setbuttontext(keyValue);

							} else if (SwitchIndex == 0) {
								// setting last item of list as selected i.e ALL
								// ON/OFF
								ModelList.get(ModelList.size() - 1).setSelected(true);

								// getting key from list
								key = wirlessDataList[wirlessDataList.length - 1];

								// fetching name (i.e key value ) of action
								// selected for item from map
								keyValue = Switch_CMDS.get(key);

								// setting button text for selected item
								ModelList.get(ModelList.size() - 1).setbuttontext(keyValue);

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					// making delete button enable
					deletSetting_btn.setEnabled(true);
				} else {
					// making delete button disable
					deletSetting_btn.setEnabled(false);
				}
				// checking if cursor is not null
				if (mcursor != null)
					mcursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// making delete button disable
			deletSetting_btn.setEnabled(false);
			// un checking model items
			unCheckModelItems();
		}
		// refreshing list view
		DeviceSwitch.invalidateViews();

	}


	// method to refresh device switch list view to show status of switches
	// corresponding to panel switch
	void RefreshDeviceSwitchListView_PIR(int panelSwitchNumber) {

		// checking panel status
		isPanelConfiguredToSwitch_PIR();

		Log.d("TAG", "PANEL CONFIGURED STS : " + isPanelPreConfigured);
		// checking if panel is configured to currently selected device
		if (isPanelPreConfigured) {

			// method to uncheck model items
			unCheckModelItems();

			try {
				String[] wirlessDataList = null;
				String WswitchData = null, DswitchNo = null;

				// getting current panel switch number and incrementing it by 1
				// as index starts from 0
				int currentPswitchno = 0;
				// getting current panel switch number
				try {
					currentPswitchno = Integer.parseInt(PANEL_SWB_ID + (panelSwitchNumber + 1));
				} catch (Exception e) {
					e.printStackTrace();
				}
				// fetching data from database corresponding to panel switch
				Cursor mcursor = WhouseDB.ConfiguredPanelPreDetails_PIR(Current_WRLS_DevNo, Current_DevNo,
						currentPswitchno,Current_Pirlightsensorval_WRLS);

				// checking if cursor have some data or not
				if (mcursor != null && mcursor.getCount() != 0) {
					// moving cursor to first position
					mcursor.moveToFirst();
					// initialize array size
					String[] DevSwitchNoList = new String[mcursor.getCount()];
					// fetching switch numbers already selected
					// for currently selected panel
					DswitchNo = mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.DevSwitchNo));

					Log.d("TAG", "Dev Switch Number : " + DswitchNo);

					// fetching switch actions selected for switches configured
					// to panel
					WswitchData = mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.DevLocalRef));

					Log.d("TAG", "WswitchData  : " + WswitchData);

					try {
						DevSwitchNoList = (DswitchNo.split(","));
						wirlessDataList = (WswitchData.split(","));

					} catch (Exception e) {
						e.printStackTrace();
					}

					int SwitchIndex = -1;
					String keyValue = null, key = null;
					for (int i = 0; i < DevSwitchNoList.length; i++) {
						try {
							SwitchIndex = Integer.parseInt(DevSwitchNoList[i]);
							if (SwitchIndex > 0) {
								// setting item based on index decremented by 1
								// as list-
								// view items index starts from 0
								ModelList.get(SwitchIndex - 1).setSelected(true);

								// getting key from list
								key = wirlessDataList[i];

								// fetching name (i.e key value ) of action
								// selected for item from map
								keyValue = Switch_CMDS.get(key);

								Log.d("TAG", "DATA set :" + key + " keyval :" + keyValue + " i value :" + i);

								// setting button text for selected item
								ModelList.get(SwitchIndex - 1).setbuttontext(keyValue);

							} else if (SwitchIndex == 0) {
								// setting last item of list as selected i.e ALL
								// ON/OFF
								ModelList.get(ModelList.size() - 1).setSelected(true);

								// getting key from list
								key = wirlessDataList[wirlessDataList.length - 1];

								// fetching name (i.e key value ) of action
								// selected for item from map
								keyValue = Switch_CMDS.get(key);

								// setting button text for selected item
								ModelList.get(ModelList.size() - 1).setbuttontext(keyValue);

							}

						} catch (Exception e) {
							e.printStackTrace();
						}

					}
					// making delete button enable
					deletSetting_btn.setEnabled(true);
				} else {
					// making delete button disable
					deletSetting_btn.setEnabled(false);
				}
				// checking if cursor is not null
				if (mcursor != null)
					mcursor.close();
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			// making delete button disable
			deletSetting_btn.setEnabled(false);
			// un checking model items
			unCheckModelItems();
		}
		// refreshing list view
		DeviceSwitch.invalidateViews();

	}


	// list item click listener
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		switch (parent.getId()) {

			case R.id.Device_Switches_list: {
				Log.d("TAG", "Clicked item position : " + position);
				// getting current position
				int getPosition = position;

				if (!ModelList.get(getPosition).isSelected()) {
					// getting current check box instance
					CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox_toggle);
					// Set the value of check box to maintain its state.
					ModelList.get(getPosition).setSelected(true);
					// making check box checked
					cb.setChecked(true);

					// setting current selected item's background
					view.setBackgroundResource(itemclicked);

					// getting current button instance
					Button button = (Button) view.findViewById(R.id.switchAction);
					// making button enable and clickable
					button.setClickable(true);
					button.setEnabled(true);

					switch (devTypes.valueOf(Current_DevName)) {
						case SWD1:
						case S010:
						case S020:
						case S030:
						case S040:
						case S060:
						case S080:
						case S120:
						case S021:
						case S031:
						case S042:
						case S051:
						case S052:
						case S061:
						case S062:
						case S071:
						case S102:
						case S111:
						case SLT1:
						case SFN1:
						case DFN1:
						case S141:
						case S110:
						case S160:{
							// setting action list corresponding action
							// button
							if (ModelList.get(position).getName().equals(SWB_FAN_TEXT1)
									|| ModelList.get(position).getName().equals(SWB_FAN_TEXT2)
									|| ModelList.get(position).getName().equals(SWB_FAN_TEXT3)
									|| ModelList.get(position).getName().equals(SWB_FAN_TEXT4)) {

								// un checking ALL ON/OFF switch
								if (ModelList.get(ModelList.size() - 1).getName().equals(SWB_ALL_ON_OFF_TEXT)) {

									// Set the value of check box to maintain its state.
									ModelList.get(ModelList.size() - 1).setSelected(false);

								}
							} else if (ModelList.get(position).getName().equals(SWB_ALL_ON_OFF_TEXT)) {

								// un-checking all switches except all on/off switch
								for (int j = 0; j < ModelList.size() - 1; j++) {
									// Set the value of check box to maintain its state.
									ModelList.get(j).setSelected(false);
								}

							} else {

								// un checking ALL ON/OFF switch
								if (ModelList.get(ModelList.size() - 1).getName().equals(SWB_ALL_ON_OFF_TEXT)) {

									// Set the value of check box to maintain its state.
									ModelList.get(ModelList.size() - 1).setSelected(false);

								}
							}

							// refresh list view
							DeviceSwitch.invalidateViews();
							break;
						}

					}

				} else {
					// getting current check box instance
					CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox_toggle);
					// Set the value of check box to maintain its state.
					ModelList.get(getPosition).setSelected(false);
					// making check box checked
					cb.setChecked(false);

					// setting current selected item's baground
					view.setBackgroundResource(itemunclicked);

					// getting current button instance
					Button button = (Button) view.findViewById(R.id.switchAction);
					// making button enable and clickable
					button.setClickable(false);
					button.setEnabled(false);

				}
				// refresh list view
				DeviceSwitch.invalidateViews();

				break;
			}
		}

	}

	// switch action alert dialog
	void ActionDialog(final List<String> list, final Button v, final int itemPosition) {
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run() {
				// Converting house array list to array
				switchActionArray = new String[list.size()];
				switchActionArray = list.toArray(switchActionArray);
				// making var true that dialog is open
				isDilaogOpened = true;
				AlertDialog.Builder dlg = new AlertDialog.Builder(WirelessPanelFinalConfiguration.this,
						AlertDialog.THEME_HOLO_LIGHT);
				dlg.setTitle("Select Switch Action");
				dlg.setCancelable(false);

				dlg.setSingleChoiceItems(switchActionArray, -1, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int pos) {
						// getting current switch action postion
						switchAction_ItemPostion = pos;
					}
				});
				dlg.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// making variable false
						isDilaogOpened = false;
						dialog.dismiss();
					}
				});
				dlg.setPositiveButton("OK", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						// checking if item is selected or not
						if (switchAction_ItemPostion != -1) {
							// getting selected item
							String btnText = switchActionArray[switchAction_ItemPostion];
							// setting selected item as text on selected button
							v.setText(btnText);
							// saving current text item state
							ModelList.get(itemPosition).setbuttontext(btnText);
							// resetting variable
							switchAction_ItemPostion = -1;
						} else {
							Toast.makeText(getApplicationContext(), "Please Select Atleast One Action!",
									Toast.LENGTH_SHORT).show();
						}

						// making variable false
						isDilaogOpened = false;

						dialog.dismiss();
					}
				});

				dialog = dlg.create();
				dialog.show();
			}
		});
	}

	// *********ADAPTER CLASS**************//

	class ListadApter_Toggle extends ArrayAdapter<Model> {
		private final List<Model> values;
		Context context;
		int resourceId;

		int itemclicked = R.drawable.wirles_set_backgroung_cb1;
		int itemunclicked = R.drawable.wirles_set_backgroung_cb;

		// adapter class constructor
		public ListadApter_Toggle(Context context, int layoutId, List<Model> values) {
			super(context, layoutId, values);
			this.context = context;
			this.values = values;
			resourceId = layoutId;

		}

		// current view method
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;

			if (convertView == null) {
				LayoutInflater inflator = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				convertView = inflator.inflate(resourceId, null);

				// initialize view holder class
				viewHolder = new ViewHolder();

				// fetching id of text view
				viewHolder.text = (TextView) convertView.findViewById(R.id.textView_toggle);

				// fetching id of check box
				viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_toggle);

				viewHolder.mbutton = (Button) convertView.findViewById(R.id.switchAction);

				// setting button click listener
				viewHolder.mbutton.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						Button btn = (Button) v;
						int pos = (Integer) v.getTag();

						// checking if item is selected
						// before performing action from button
						if (values.get(pos).isSelected() && !isDilaogOpened) {
							Log.d("TAG", "Button clicked");

							switch (devTypes.valueOf(Current_DevName)) {
								case SWD1:
								case S010:
								case S020:
								case S030:
								case S040:
								case S060:
								case S080:
								case S120:
								case S021:
								case S031:
								case S042:
								case S051:
								case S052:
								case S061:
								case S062:
								case S071:
								case S102:
								case S111:
								case SLT1:
								case SFN1:
								case DFN1:
								case S141:
								case S110:
								case S160:{
									// setting action list corresponding action button
									if (values.get(pos).getName().equals(SWB_FAN_TEXT1)
											|| values.get(pos).getName().equals(SWB_FAN_TEXT2)
											|| values.get(pos).getName().equals(SWB_FAN_TEXT3)
											|| values.get(pos).getName().equals(SWB_FAN_TEXT4)) {
										ActionDialog(FanActionList, btn, pos);
									} else if (values.get(pos).getName().equals(SWB_ALL_ON_OFF_TEXT)) {
										ActionDialog(AllOnOffList, btn, pos);
									} else {
										ActionDialog(SwitchActionList, btn, pos);
									}

									break;
								}
								case WPD1:
								case WPS1:
								case GSK1:
								case GSR1:
								case ACR1: {
									ActionDialog(SwitchActionList, btn, pos);
									break;
								}
								case CLB1: {
									// showing geyser action list
									ActionDialog(CallBellActionList, btn, pos);
									break;
								}
								case DMR1: {
									if (values.get(pos).getName().equals(DMR_LEVEL_TEXT)) {
										ActionDialog(dimmerLevelList, btn, pos);
									} else {
										ActionDialog(dimmerActionList, btn, pos);
									}

									break;
								}
								case RGB1: {
									if (values.get(pos).getName().equals(RGB_COLOURS_EFFECTS_TEXT)) {
										ActionDialog(rgbColour_EffectList, btn, pos);

									} else if (values.get(pos).getName().equals(RGB_BRIGHTNESS_TEXT)) {
										ActionDialog(rgbBrightnessList, btn, pos);

									} else if (values.get(pos).getName().equals(RGB_SPEED_TEXT)) {
										ActionDialog(rgbSpeedList, btn, pos);

									} else {
										ActionDialog(rgbActionList, btn, pos);
									}

									break;
								}

								case CLNR:
								case CLSH:
								case CLS1:
								case CRS1:
								case CLD1:
								case CRD1: {
									if (values.get(pos).getName().equals(CUR1_ACTION_TEXT)
											|| values.get(pos).getName().equals(CUR2_ACTION_TEXT)) {
										ActionDialog(curActionList, btn, pos);
									}
									break;
								}

								case PSC1:
								case PLC1:case SOSH:case SWG1 :case SLG1:
									// showing projector screen action list i.e same
									// as curtain actions
									ActionDialog(curActionList, btn, pos);
									break;
								case DLS1:
									ActionDialog(drlActionList, btn, pos);
									break;
								default:
									break;
							}

						}

					}
				});

				// saving current view tag
				convertView.setTag(viewHolder);
				convertView.setTag(R.id.textView_toggle, viewHolder.text);
				convertView.setTag(R.id.checkBox_toggle, viewHolder.checkbox);
				convertView.setTag(R.id.switchAction, viewHolder.mbutton);
			} else {
				viewHolder = (ViewHolder) convertView.getTag();
			}

			// set current position for check box
			viewHolder.checkbox.setTag(position);
			// set current position for spinner
			viewHolder.mbutton.setTag(position);

			// setting current values for all controls in list row
			viewHolder.text.setText(values.get(position).getName());

			// making check box checked
			viewHolder.checkbox.setChecked(values.get(position).isSelected());

			// displaying the selected item in action button
			if (values.get(position).isSelected()) {

				// showing item state selected
				convertView.setBackgroundResource(itemclicked);

				// getting button text from model
				viewHolder.mbutton.setText(values.get(position).getButtonText());
				// making button enable & clickable
				viewHolder.mbutton.setClickable(true);
				viewHolder.mbutton.setEnabled(true);

			} else {

				// making not selected item view unclicked
				convertView.setBackgroundResource(itemunclicked);

				// setting button text in model
				values.get(position).setbuttontext(DEFAULT_SWITCH_ACTION_TEXT);

				// getting button text from model
				viewHolder.mbutton.setText(values.get(position).getButtonText());

				// making button disable & unclickable
				viewHolder.mbutton.setClickable(false);
				viewHolder.mbutton.setEnabled(false);

			}

			return convertView;

		}

		// view holder class
		private class ViewHolder {
			protected TextView text;
			protected CheckBox checkbox;
			protected Button mbutton;
		}
	}

	// ************ADAPTER CLASS ENDED******************//

	// go back
	void goPrevious() {
		finish();
	}

	// Back Press Event
	@Override
	public void onBackPressed() {
		goPrevious();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (WirelessConfigurationAdapter.sdb.isOpen()) {
			WhouseDB.close();
			Log.d("TAG", "DB CLOSED ");
		}
	}

	@Override
	protected void onResume() {

		super.onResume();
		if (!WirelessConfigurationAdapter.sdb.isOpen()) {
			WhouseDB.open();

			Log.d("TAG", "DB open ");
		}
	}

}
