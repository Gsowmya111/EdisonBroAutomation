package edisonbro.com.edisonbroautomation.operatorsettings;


/**
 * FILENAME: WirelessDeviceMoodConfiguration.java
 * DATE: 07-08-2018
 * <p>
 * DESCRIPTION: Activity to configure devices across wireless panel selected.
 * <p>
 * Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
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

import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;


public class WirelessDeviceMoodConfiguration extends Activity implements OnClickListener,
        OnItemClickListener {

    Button saveSetting_btn, deletSetting_btn, CancelSwitch;

    int Current_RoomNo = 0, Current_DevNo = 0, Current_WRLS_DevNo = 0,
            Current_WRLS_DevType = 0, Current_WRLS_SWB_NO = 0;

    String Current_RoomName = null, Current_WRLS_DevName = null,
            Current_WRLS_RoomName = null, Current_WRLS_DevID = null,
            Current_DevName = null, Current_DevType = null, Current_GroupID = null,
            Current_DevID = null, Current_User_Dev_Name = null;

    ListView DeviceSwitch;
    String[] switchActionArray;
    ArrayList<String> DeviceSwitcheList = new ArrayList<String>();
    List<Model> ModelList = new ArrayList<Model>();

    final int READ_LINE = 1, READ_BYTE = 2, EXCEPTION = 3, TCP_LOST = 4,
            BT_LOST = 5;

    Tcp tcp = null;
    WirelessConfigurationAdapter WhouseDB = null;
    boolean IS_SERVER_CONNECTED = false;

    ListadApter_ir adapter;
    ListadApter_Toggle toggleAdapter;

    int itemclicked = R.drawable.wirles_set_backgroung_cb,
            itemunclicked = R.drawable.wirles_set_backgroung_cb1, spinnerLayoutId = R.layout.spinnerlayout,
            layoutID1 = R.layout.checkbox_listview,
            layoutID2 = R.layout.toggle_listview, server_online = R.drawable.connected,
            server_offline = R.drawable.disconnected;

    int PanelSwitchNo = -1, DeviceSwitchNo = -1;
    List<String> SwitchActionList, FanActionList, AllOnOffList, CallBellActionList,
            dimmerActionList, dimmerLevelList, doorActionList, curActionList, rgbActionList,
            rgbColour_EffectList, rgbBrightnessList, rgbSpeedList;
    int switchAction_ItemPostion = -1;

    String DEFAULT_SWITCH_ACTION_TEXT = "Set Action";

    //initialize fan speed  code logic will work for speed less than 10
    final int FAN_MAX_SPEED = 4;


    AlertDialog dialog;
    boolean isDilaogOpened = false, isPanelPreConfigured = false;

    // *********************************SWB COMMANDS******************************//
    //original format eg. ON="201" OFF="301" ON/OFF="101"
    //here only defining initial 2 char of command
    final String SWITCH_ON_CMD = "20", SWITCH_OFF_CMD = "30", SWITCH_ON_OFF_CMD = "10";
    //here only defining initial 2 char of command
    final String SWITCH_ON_START_CHAR = "2", SWITCH_OFF_START_CHAR = "3", SWITCH_ON_OFF_START_CHAR = "1";

    final String FAN1_ON_CMD = "722", FAN1_OFF_CMD = "723", FAN1_ON_OFF_CMD = "700",
            FAN2_ON_CMD = "752", FAN2_OFF_CMD = "753", FAN2_ON_OFF_CMD = "730",
            FAN3_ON_CMD = "782", FAN3_OFF_CMD = "783", FAN3_ON_OFF_CMD = "760",
            FAN4_ON_CMD = "812", FAN4_OFF_CMD = "813", FAN4_ON_OFF_CMD = "790";

    final String FAN1_SPEED = "71", FAN2_SPEED = "74", FAN3_SPEED = "77", FAN4_SPEED = "80";

    // FAN_SPEED_1="701",FAN_SPEED_9="709";
    final String ALL_ON_CMD = "901", ALL_OFF_CMD = "902";

    //door lock cmd
    final String DOOR_LOCK_CMD = "201";

    // *****************DMR COMMANDS****************************//

    final String DMR_ON_CMD = "102", DMR_OFF_CMD = "103",
            DMR_ON_OFF_CMD = "101";

    final String DMR_BRIGHTNESS_PROCESS_CMD = "112";

    final String DMR_HIGH_CMD = "255", DMR_MEDIUM_CMD = "040",
            DMR_LOW_CMD = "002";

    final String DMR_1_CMD = "003", DMR_2_CMD = "015", DMR_3_CMD = "030",
            DMR_4_CMD = "050", DMR_5_CMD = "071", DMR_6_CMD = "080",
            DMR_7_CMD = "100", DMR_8_CMD = "150", DMR_9_CMD = "200",
            DMR_10_CMD = "250";

    // *************************RGB COMMANDS************************//

    final String RGB_ON_CMD = "102", RGB_OFF_CMD = "103",
            RGB_ON_OFF_CMD = "101";

    final String RGB_COLOR_PROCESS_CMD = "112";

    final String RGB_FLASH_CMD = "104",
            RGB_STROBE_CMD = "105", RGB_SMOOTH_CMD = "106",
            RGB_FADE_CMD = "107";

    final String RGB_BRIGHTNESS_START_CHAR_CMD = "13",
            RGB_BRIGHTNESS10_CMD = "140";
    // RGB_BRIGHTNESS1_CMD="131" TO RGB_BRIGHTNESS10_CMD="140"

    final String RGB_SPEED_START_CHAR_CMD = "12",
            RGB_SPEED10_CMD = "130";
    // RGB_SPEED1_CMD="121" TO RGB_SPEED10_CMD="130" i.e low to high

    final String RGB_RED_CMD = "255000000000",
            RGB_GREEN_CMD = "000255000000", RGB_BLUE_CMD = "000000255000",
            RGB_ORANGE_CMD = "255165000000", RGB_WHITE_CMD = "255255255000",
            RGB_PINK_CMD = "255000128000";

    // *******************CUR COMMANDS*********************//

    final String CUR1_OPEN_CMD = "101", CUR1_CLOSE_CMD = "102",
            CUR1_STOP_CMD = "103";
    final String CUR2_OPEN_CMD = "105", CUR2_CLOSE_CMD = "106",
            CUR2_STOP_CMD = "107";

    //**********************GYSER CMDS******************************//
    String GYSER_ON_CMD = "201", GYSER_OFF_CMD = "301", GYSER_ON_OFF_CMD = "101";


    String PANEL_SWB_ID = "10";
    final String SWB_ALL_ON_OFF_TEXT = "ALL ON/OFF", SWB_FAN_TEXT1 = "Fan 1",
            SWB_FAN_TEXT2 = "Fan 2", SWB_FAN_TEXT3 = "Fan 3",
            SWB_FAN_TEXT4 = "Fan 4", DMR_ON_OFF_TEXT = "Power Option",
            DMR_LEVEL_TEXT = "Brightness Level",
            RGB_ON_OFF_TEXT = "Power Option",
            RGB_COLOURS_EFFECTS_TEXT = "Colours & Effects",
            RGB_BRIGHTNESS_TEXT = "Brightness Level",
            RGB_SPEED_TEXT = "Transition Speed",
            CUR1_ACTION_TEXT = "Curtain1 Action",
            CUR2_ACTION_TEXT = "Curtain2 Action";


    String switchData = null;

    int PanelSwitchNumber = 0, DeviceSwitchNumber = 0;

    ArrayList<Integer> Device_SelectedPositions = new ArrayList<Integer>();

    HashMap<String, String> Switch_CMDS = new HashMap<String, String>();

    // creating two string builder instances to save
    // switch number data temporarly
    StringBuilder FINAL_SWITCH_NUM_DATA = new StringBuilder();
    StringBuilder FINAL_SWITCH_DATA = new StringBuilder();
    StringBuilder FINAL_SWITCH_LOCAL_REF_DATA = new StringBuilder();

    static volatile boolean isActionNotSelected = false;
    static boolean isTcpConnecting = false;
    String PIR_SENSOR_DEVTYPE = "WPS1";

    final int RESULT_CODE = 1;

    ArrayList<Boolean> finalSwitchStatusList = null;
    int FAN_COUNT = 0, SWB_COUNT = 0, FAN1_VALUE = 0, FAN2_VALUE = 0, FAN3_VALUE = 0, FAN4_VALUE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.wireless_mood_configuration);

        DeviceSwitch = (ListView) findViewById(R.id.Device_Switches_list);
        saveSetting_btn = (Button) findViewById(R.id.savePanelSettings);
        deletSetting_btn = (Button) findViewById(R.id.DeletePanelSettings);
        CancelSwitch = (Button) findViewById(R.id.cancelPanelSettings);

        // adding click listener
        CancelSwitch.setOnClickListener(this);
        saveSetting_btn.setOnClickListener(this);
        deletSetting_btn.setOnClickListener(this);

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
            Current_WRLS_DevNo = it.getIntExtra("DevNo_WRLS", 0);
            Current_WRLS_DevType = it.getIntExtra("DevType_WRLS", 0);
            Current_WRLS_RoomName = it.getStringExtra("RoomName_WRLS");
            Current_WRLS_DevID = it.getStringExtra("DevId_WRLS");
            Current_WRLS_DevName = it.getStringExtra("DevName_WRLS");
            PanelSwitchNo = it.getIntExtra("PanelSwitchNo_WRLS", -1);
            Current_GroupID = it.getStringExtra("Current_GroupID");
            Current_WRLS_SWB_NO = it.getIntExtra("SWB_WRLS_NO", 0);

            Current_User_Dev_Name = it.getStringExtra("curDevusrnam");

            if (Current_GroupID == null) Current_GroupID = "000";

            StaticVariables.printLog("DETAILS", "DETAILS OF DEVICE PANEL :\n" + "RoomName : "
                    + Current_RoomName + "\n" + "RoomNo : " + Current_RoomNo
                    + "\n" + "DTYPE : " + Current_DevType + "\n" + "DNO : "
                    + Current_DevNo + "\n" + "DID : " + Current_DevID + "\n"
                    + "DEVNAME : " + Current_DevName + "\n" + "WDNO : "
                    + Current_WRLS_DevNo + "\n" + "WDTYPE: "
                    + Current_WRLS_DevType + "\n" + "WDID: "
                    + Current_WRLS_DevID + "\n" + "WDNAME : "
                    + Current_WRLS_DevName + "\n" + "WRoomName : "
                    + Current_WRLS_RoomName + "\n"
                    + "panel switch :" + PanelSwitchNo + "\n"
                    + "Current_GroupID :" + Current_GroupID + "\n"
                    + "current panel switch No :" + Current_WRLS_SWB_NO);

        } catch (Exception e) {
            e.printStackTrace();
        }

        //initialize final status list
        finalSwitchStatusList = new ArrayList<Boolean>();

        // fill switch's all commands in hash map
        fillHashMap();


        // clearing list before filling data into it
        DeviceSwitcheList.clear();

        // switching
        switch (devTypes.valueOf(Current_DevName)) {
            case SWD1:
                FillSWB_Data(5, 1);
                break;
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
            case CLB1: {
                // filling on and off commands for ac
                Fill_ActionList_CLB();
                DeviceSwitcheList.add("Bell Ring");
                break;
            }
            case DLS1: {

                Fill_ActionList_DRL();
                DeviceSwitcheList.add("Door Lock");

                break;
            }
            case GSR1:
            case GSK1:
            case WPS1:
            case ACR1: {
                //filling on and off commands for geyser/sprinkler/pir/AC
                Fill_ActionList_CLB();
                DeviceSwitcheList.add("Switch 1");
                break;
            }
            case PLC1:
            case PSC1:
            case SOSH:
            case SWG1:
            case SLG1:{
                //filling projector screen/lifter action list
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

        //checking and showing switch status if already present
        RefreshDeviceSwitchListView(PanelSwitchNo);

        // calling connection in activity
        RegainingConnection();

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
                            //serverStatus.setImageResource(server_online);
                            StaticVariables.printLog("TAG", "CONNECTED");

                        } else {
                            //serverStatus.setImageResource(server_offline);
                            StaticVariables.printLog("TAG", "NOT CONNECTED");
                        }
                        isTcpConnecting = false;
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


    // method to fill switch data in lists
    void FillSWB_Data(int switches, int fans) {


        // filling particular switch action lists
        if (switches > 0) {
            Fill_DeviceActionList_SWB();

            SWB_COUNT = switches;

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

            FAN_COUNT = fans;

            for (int i = 0; i < fans; i++) {
                DeviceSwitcheList.add("Fan " + (i + 1));
            }
        }

        // if switches are greater than one adding all ON/OFF option
        if (switches > 1) {

            Fill_AllOnOffList_SWB();
            DeviceSwitcheList.add("ALL ON/OFF");
        }

    }

    //added new to setaction for mood switch
    // Filling List with Switch Action Data
    private void Fill_DeviceActionList_SWB() {
        Resources res = getResources();
        String[] switchactions = res.getStringArray(R.array.SwitchData_Wireless);
        SwitchActionList = Arrays.asList(switchactions);
    }

    // Filling list with Fan Action data
    private void Fill_FanActionList_SWB() {

        FanActionList = new ArrayList<String>();

        for (int i = 1; i <= FAN_MAX_SPEED; i++) {
            FanActionList.add(String.valueOf(i));
        }

        FanActionList.add("ON");
        FanActionList.add("OFF");
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
        String[] switchactions = res.getStringArray(R.array.DMR_MoodActions_wireless);
        dimmerActionList = Arrays.asList(switchactions);
    }

    // Filling list with Fan Action data
    private void Fill_BrightnessListLevel_DMR() {
        Resources res = getResources();
        String[] brightnessLevel = res
                .getStringArray(R.array.DMR_BRIGHTNESS_LEVEL);
        dimmerLevelList = Arrays.asList(brightnessLevel);
    }

    // *********************RGB ACTION LIST*******************************//

    // Filling List with RGB Action Data
    private void Fill_DeviceActionList_RGB() {
        Resources res = getResources();
        String[] switchactions = res.getStringArray(R.array.RGB_MoodActions_wireless);
        rgbActionList = Arrays.asList(switchactions);
    }

    // Filling list with rgb Brightness data
    private void Fill_BrightnessListLevel_RGB() {
        Resources res = getResources();
        String[] brightnessLevel = res
                .getStringArray(R.array.RGB_BRIGHTNESS_LEVEL);
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
    private void Fill_ActionList_CUR() {
        Resources res = getResources();
        String[] actions = res.getStringArray(R.array.CUR_MoodActions_wireless);
        curActionList = Arrays.asList(actions);
    }

    // *********************DOOR LOCK ACTION LIST*************************//

    // Filling list with Curtain Action data
    private void Fill_ActionList_DRL() {
        Resources res = getResources();
        String[] actions = res.getStringArray(R.array.DoorLock_Actions);
        doorActionList = Arrays.asList(actions);
    }
    //*********************GEYSER ACTION LIST*************************//

    // Filling list with CLB Action data
    private void Fill_ActionList_CLB() {
        Resources res = getResources();
        String[] actions = res.getStringArray(R.array.CallBell_Actions);
        CallBellActionList = Arrays.asList(actions);
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
                case S110:
                    FillSWB_Hash_Data(2, 0);
                    break;
                case S030:
                    FillSWB_Hash_Data(3, 0);
                    break;
                case S120:
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
                case S160:
                    FillSWB_Hash_Data(7, 0);
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
                case S141:
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
                case DMR1: {
                    // storing original name of Dimmer commands in hash array

                    Switch_CMDS.put(DMR_ON_CMD, "ON");
                    Switch_CMDS.put(DMR_OFF_CMD, "OFF");

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
                        //setting brightness & speed text in hash map
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
                case CLS1:
                case CRS1:
                case PSC1:
                case PLC1:
                case SOSH:
                case SWG1:
                case SLG1:{
                    //using same commands for curtain and projector
                    Switch_CMDS.put(CUR1_OPEN_CMD, "OPEN");
                    Switch_CMDS.put(CUR1_CLOSE_CMD, "CLOSE");

                    break;
                }
                case CLD1:
                case CRD1: {
                    Switch_CMDS.put(CUR1_OPEN_CMD, "OPEN");
                    Switch_CMDS.put(CUR1_CLOSE_CMD, "CLOSE");

                    Switch_CMDS.put(CUR2_OPEN_CMD, "OPEN");
                    Switch_CMDS.put(CUR2_CLOSE_CMD, "CLOSE");

                    break;
                }
                case GSR1:
                case GSK1:
                case WPS1:
                case ACR1: {
                    //using same commands for geyser/sprinkler/pir/AC
                    Switch_CMDS.put(GYSER_ON_CMD, "ON");
                    Switch_CMDS.put(GYSER_OFF_CMD, "OFF");
                    break;
                }
                case CLB1: {
                    // AC command
                    Switch_CMDS.put(SWITCH_ON_CMD + "1", "ON");
                    Switch_CMDS.put(SWITCH_OFF_CMD + "1", "OFF");
                    break;
                }
                case DLS1: {

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

    //method to set switch board hash map data
    void FillSWB_Hash_Data(int switches, int fans) {

        //storing original name of switch commands in hash array
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
            //filling fans data
            for (int j = 1; j <= fans; j++) {
                switch (j) {
                    case 1: {
                        Switch_CMDS.put(FAN1_ON_CMD, "ON");
                        Switch_CMDS.put(FAN1_OFF_CMD, "OFF");

                        //fixing fan speed from 1- FAN_MAX_SPEED levels
                        for (int i = 1; i <= FAN_MAX_SPEED; i++) {
                            String speed = "" + i;
                            Switch_CMDS.put(FAN1_SPEED + i, speed);
                        }
                        break;
                    }
                    case 2: {
                        Switch_CMDS.put(FAN2_ON_CMD, "ON");
                        Switch_CMDS.put(FAN2_OFF_CMD, "OFF");

                        //fixing fan speed from 1-FAN_MAX_SPEED levels
                        for (int i = 1; i <= FAN_MAX_SPEED; i++) {
                            String speed = "" + i;
                            Switch_CMDS.put(FAN2_SPEED + i, speed);
                        }
                        break;
                    }
                    case 3: {
                        Switch_CMDS.put(FAN3_ON_CMD, "ON");
                        Switch_CMDS.put(FAN3_OFF_CMD, "OFF");

                        //fixing fan speed from 1- FAN_MAX_SPEED levels
                        for (int i = 1; i <= FAN_MAX_SPEED; i++) {
                            String speed = "" + i;
                            Switch_CMDS.put(FAN3_SPEED + i, speed);
                        }
                        break;
                    }
                    case 4: {
                        Switch_CMDS.put(FAN4_ON_CMD, "ON");
                        Switch_CMDS.put(FAN4_OFF_CMD, "OFF");

                        //fixing fan speed from 1- FAN_MAX_SPEED levels
                        for (int i = 1; i <= FAN_MAX_SPEED; i++) {
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


    // Delete Warning dialog
    void DeleteWarningDialog(final int panelSwitchno) {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        WirelessDeviceMoodConfiguration.this,
                        AlertDialog.THEME_HOLO_DARK);
                dialog.setTitle("Confirm Delete ");
                dialog.setMessage("Do You Really Want To Delete Selected Panel's Switch Details ?");
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
                                DeletSettings(panelSwitchno);
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
                        WirelessDeviceMoodConfiguration.this,
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
                AlertDialog.Builder dialog = new AlertDialog.Builder(WirelessDeviceMoodConfiguration.this,
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

                //clearing status list
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
                StaticVariables.printLog("TAG", "Selected item in list : " + sb.toString());


                if (isDeviceItemSelected) {


                    switch (devTypes.valueOf(Current_DevName)) {
                        case SWD1:
                        case S010:
                        case S020:
                        case S030:
                        case S040:
                        case S060:
                        case S080:
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
                        case S120:
                        case S160:
                        {
                            StaticVariables.printLog("TAG", "insertig details for swb and fan");
                            // inserting details for swb and fan
                            insertSwbDbData();
                            break;
                        }
                        default: {
                            StaticVariables.printLog("TAG", "insertig details for other devices");
                            //inserting details for all devices except swb and fan
                            insertAllDeviceData();
                            break;
                        }
                    }

                } else {
                    msg("Please select atleast one Item from list!");
                }


                break;
            }
            case R.id.DeletePanelSettings: {

                DeleteWarningDialog(PanelSwitchNo);
                break;
            }
            case R.id.server_status: {
                if (!Tcp.tcpConnected) {
                    if (!isTcpConnecting) {
                        msg("connecting To Server...");
                        RegainingConnection();
                    } else {
                        StaticVariables.printLog("TAG", "already Connecting tcp");
                    }
                } else {
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

    // showing toast message
    void msg(final String text) {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), text,
                        Toast.LENGTH_SHORT).show();

            }
        });
    }

    // method to delete Existing Panel Settings from db
    void DeletSettings(int panelSwitchno) {
        try {
            // appending panel switch id before switch number
            int panel_SwitchNumber = Integer.parseInt(PANEL_SWB_ID
                    + (PanelSwitchNo + 1));
            boolean isDeleted = WhouseDB.DeletePanelSwitchDetails(
                    Current_WRLS_DevNo, Current_DevNo, panel_SwitchNumber);

            if (isDeleted) {
                //displaying the current status of Device switches
                RefreshDeviceSwitchListView(panelSwitchno);
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
    void isPanelConfiguredToSwitch() {
        try {
            // checking if panel already configured with current device
            isPanelPreConfigured = WhouseDB.isPanelConfiguredToDevice(
                    Current_WRLS_DevNo, Current_DevNo);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // device type collection
    enum devTypes {
        SWD1, S010, S020, S030, S040, S060, S080, S021, S031, S042,
        S051, S141, S120, S160, S110, S052, S061, S062, S071, S102, S111, SLT1, SFN1, DFN1, DMR1,
        RGB1, ACR1, WOTS, WMD1, WMC1, WMB1, WBSO, WSS1, WBM1, WSM1,
        GSR1, DLS1, CLS1, CLD1, CRS1, CRD1, IRB1, RPR1, CLB1, CSW1,
        EGM1, SOSH,SWG1,SLG1, PSC1, PLC1, GSK1, WPS1
    }

    //constants for colors
    enum coloursAndEffects {
        RED, BLUE, GREEN, PINK, ORANGE, WHITE, FLASH, STROBE, SMOOTH, FADE
    }

    //method to process switch data
    String processSwitchData(String data) {
        int decimal = Integer.parseInt(new StringBuffer(data).reverse().toString(), 2);
        String hexStr = Integer.toString(decimal, 16);
        String output = new StringBuffer(hexStr).reverse().toString();
        StaticVariables.printLog("DATA", "DATA :" + output);
        //making output of length 3 chars as fixing three chars for switch data. i.e appending zeros at end
        while (output.length() < 3) {
            output = output + "0";
        }
        return output;
    }

    //method to make inputString for specific switchboard
    //to pass status of only switches to processswitchData method
    String makeSwitchInput(int count) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < count; i++) {
            if (finalSwitchStatusList.get(i)) {

                // getting data set on particular switch board's switch
                String WirelessSwitchData = ModelList.get(i).getButtonText();

                if (WirelessSwitchData.equals("ON")) {
                    //append 1 for true or ON status
                    sb.append("1");


                    if (i >= 9) {
                        //if switch is on then setting status of switch for status reflection purpose in db
                        FINAL_SWITCH_LOCAL_REF_DATA.append(SWITCH_ON_START_CHAR + (i + 1) + ",");
                    } else {

                        //if switch is on then setting status of switch for status reflection purpose in db
                        FINAL_SWITCH_LOCAL_REF_DATA.append(SWITCH_ON_CMD + (i + 1) + ",");
                    }
                } else if (WirelessSwitchData.equals("OFF")) {
                    //append 0 for OFF status
                    sb.append("0");

                    if (i >= 9) {
                        //if switch is on then setting status of switch for status reflection purpose in db
                        FINAL_SWITCH_LOCAL_REF_DATA.append(SWITCH_OFF_START_CHAR + (i + 1) + ",");
                    } else {

                        //if switch is on then setting status of switch for status reflection purpose in db
                        FINAL_SWITCH_LOCAL_REF_DATA.append(SWITCH_OFF_CMD + (i + 1) + ",");

                    }

                }

            } else {
                //append 0 for OFF status
                sb.append("0");
            }
        }
        return sb.toString();
    }

    //method to get final code to operate switch board
    String getSwitchOperationCode(String Current_DevName) {

        StringBuffer sb = new StringBuffer();
        String SwitchOperationData = null, f1 = "0", f2 = "0", f3 = "0", f4 = "0";
        String switchData = null, input = null, val1 = "0", val2 = "0";

        switch (devTypes.valueOf(Current_DevName)) {
            case SWD1: {
                input = makeSwitchInput(5);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(5)) {
                    //getting fan1 text positioned at index 5
                    val1 = ModelList.get(5).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            }

            case S010:
                input = makeSwitchInput(1);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S020:
                input = makeSwitchInput(2);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S110:
                input = makeSwitchInput(2);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S030:
                input = makeSwitchInput(3);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S120:
                input = makeSwitchInput(3);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S040:
                input = makeSwitchInput(4);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S060:
                input = makeSwitchInput(6);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            case S080:
                input = makeSwitchInput(8);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";

                break;
            case S160:
                input = makeSwitchInput(8);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";

                break;

            case S021:

                input = makeSwitchInput(2);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(2)) {
                    //getting fan1 text  positioned at index 2
                    val1 = ModelList.get(2).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            case S031:
                input = makeSwitchInput(3);
                switchData = processSwitchData(input);

                //getting text of fan1 positioned at index 3
                if (Device_SelectedPositions.contains(3)) {
                    //getting fan1 text positioned at index 3
                    val1 = ModelList.get(3).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            case S042:
                input = makeSwitchInput(4);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(4)) {
                    //getting fan1 text positioned at index 4
                    val1 = ModelList.get(4).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                if (Device_SelectedPositions.contains(5)) {
                    //getting fan2 text positioned at index 5
                    val2 = ModelList.get(5).getButtonText();
                    if (val2.equals("OFF") || val2.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan2 value is selected as OFF then setting constant for OFF command
                        f2 = "B";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_OFF_CMD + ",");

                    } else if (val2.equals("ON")) {
                        //if fan2 value is selected as ON then setting constant for ON command
                        f2 = "A";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_ON_CMD + ",");

                    } else {
                        f2 = val2;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_SPEED + f2 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + f2 + "00" + "000";

                break;
            case S051: {
                input = makeSwitchInput(5);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(5)) {
                    //getting fan1 text positioned at index 5
                    val1 = ModelList.get(5).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            }

            case S141: {
                input = makeSwitchInput(5);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(5)) {
                    //getting fan1 text positioned at index 5
                    val1 = ModelList.get(5).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            }
            case S052:
                input = makeSwitchInput(5);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(5)) {
                    //getting fan1 text positioned at index 5
                    val1 = ModelList.get(5).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                if (Device_SelectedPositions.contains(6)) {
                    //getting fan2 text positioned at index 6
                    val2 = ModelList.get(6).getButtonText();
                    if (val2.equals("OFF") || val2.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan2 value is selected as OFF then setting constant for OFF command
                        f2 = "B";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_OFF_CMD + ",");

                    } else if (val2.equals("ON")) {
                        //if fan2 value is selected as ON then setting constant for ON command
                        f2 = "A";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_ON_CMD + ",");

                    } else {
                        f2 = val2;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_SPEED + f2 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + f2 + "00" + "000";
                break;


            case S061: {
                input = makeSwitchInput(6);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(6)) {
                    //getting fan1 text positioned at index 6
                    val1 = ModelList.get(6).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";

                break;
            }
            case S062:
                input = makeSwitchInput(6);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(6)) {
                    //getting fan1 text  positioned at index 6
                    val1 = ModelList.get(6).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                if (Device_SelectedPositions.contains(7)) {
                    //getting fan2 text positioned at index 7
                    val2 = ModelList.get(7).getButtonText();
                    if (val2.equals("OFF") || val2.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan2 value is selected as OFF then setting constant for OFF command
                        f2 = "B";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_OFF_CMD + ",");

                    } else if (val2.equals("ON")) {
                        //if fan2 value is selected as ON then setting constant for ON command
                        f2 = "A";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_ON_CMD + ",");

                    } else {
                        f2 = val2;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_SPEED + f2 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + f2 + "00" + "000";
                break;
            case S071:
                input = makeSwitchInput(7);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(7)) {
                    //getting fan1 text  positioned at index 7
                    val1 = ModelList.get(7).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";
                break;
            case S102:
                input = makeSwitchInput(10);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(10)) {
                    //getting fan1 text  positioned at index 10
                    val1 = ModelList.get(10).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                if (Device_SelectedPositions.contains(11)) {
                    //getting fan2 text positioned at index 7
                    val2 = ModelList.get(11).getButtonText();
                    if (val2.equals("OFF") || val2.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan2 value is selected as OFF then setting constant for OFF command
                        f2 = "B";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_OFF_CMD + ",");

                    } else if (val2.equals("ON")) {
                        //if fan2 value is selected as ON then setting constant for ON command
                        f2 = "A";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_ON_CMD + ",");

                    } else {
                        f2 = val2;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_SPEED + f2 + ",");
                    }
                }

                SwitchOperationData = "" + switchData + "00000" + f1 + f2 + "00" + "000";

                break;
            case S111:
                input = makeSwitchInput(11);
                switchData = processSwitchData(input);

                if (Device_SelectedPositions.contains(11)) {
                    //getting fan1 text  positioned at index 11
                    val1 = ModelList.get(11).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + switchData + "00000" + f1 + "000" + "000";
                break;
            case SLT1: {
                input = makeSwitchInput(1);
                switchData = processSwitchData(input);
                SwitchOperationData = "" + switchData + "00000" + "0000" + "000";
                break;
            }
            case SFN1: {

                if (Device_SelectedPositions.contains(0)) {
                    //getting fan1 text  positioned at index 0
                    val1 = ModelList.get(0).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                SwitchOperationData = "" + "000" + "00000" + f1 + "000" + "000";
                break;
            }
            case DFN1: {
                if (Device_SelectedPositions.contains(0)) {
                    //getting fan1 text  positioned at index 0
                    val1 = ModelList.get(0).getButtonText();
                    if (val1.equals("OFF") || val1.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan1 value is selected as OFF then setting constant for OFF command
                        f1 = "B";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_OFF_CMD + ",");

                    } else if (val1.equals("ON")) {
                        //if fan1 value is selected as ON then setting constant for ON command
                        f1 = "A";

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_ON_CMD + ",");

                    } else {
                        f1 = val1;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN1_SPEED + f1 + ",");
                    }
                }
                if (Device_SelectedPositions.contains(1)) {
                    //getting fan2 text positioned at index 7
                    val2 = ModelList.get(1).getButtonText();
                    if (val2.equals("OFF") || val2.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        //if fan2 value is selected as OFF then setting constant for OFF command
                        f2 = "B";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_OFF_CMD + ",");

                    } else if (val2.equals("ON")) {
                        //if fan2 value is selected as ON then setting constant for ON command
                        f2 = "A";

                        //saving status of fan2 for local reference to show status of fan2
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_ON_CMD + ",");

                    } else {
                        f2 = val2;

                        //saving status of fan1 for local reference to show status of fan1
                        FINAL_SWITCH_LOCAL_REF_DATA.append(FAN2_SPEED + f2 + ",");
                    }
                }

                SwitchOperationData = "" + "000" + "00000" + f1 + f2 + "00" + "000";

                break;
            }
            default:
                break;
        }

		/*and changing lower case letters to upper case letters
		as rquired for switch data format
		 */
        SwitchOperationData = SwitchOperationData.toUpperCase();

        StaticVariables.printLog("", "FINAL DATA :" + SwitchOperationData + " data length:" + SwitchOperationData.length());

        return SwitchOperationData;
    }

    //method to insert data for all devices except switch boards
    void insertAllDeviceData() {
        try {
            //clearing string builder variables
            FINAL_SWITCH_DATA.delete(0, FINAL_SWITCH_DATA.length());
            FINAL_SWITCH_NUM_DATA.delete(0, FINAL_SWITCH_NUM_DATA.length());
            FINAL_SWITCH_LOCAL_REF_DATA.delete(0, FINAL_SWITCH_LOCAL_REF_DATA.length());


            //getting current  panel switch number
            try {
                PanelSwitchNumber = Integer.parseInt(PANEL_SWB_ID + (PanelSwitchNo + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }

            String WirelessSwitchData = null, LocalRefData = null;

            if (Device_SelectedPositions.size() > 0 && PanelSwitchNo != -1) {

                //loop to iterate switch numbers
                for (int k = 0; k < Device_SelectedPositions.size(); k++) {

                    //getting switch number from list
                    DeviceSwitchNo = Device_SelectedPositions.get(k);

                    //making current switch number incremented by 1 to save for db purpose
                    //as index start from 0
                    DeviceSwitchNumber = DeviceSwitchNo + 1;

                    //getting data set on particular switch board's switch
                    WirelessSwitchData = ModelList.get(DeviceSwitchNo).getButtonText();

                    //getting name of switch
                    String switchName = ModelList.get(DeviceSwitchNo).getName();

                    if (!WirelessSwitchData.equals(DEFAULT_SWITCH_ACTION_TEXT)) {

                        //making group id of 3 chars
                        while (Current_GroupID.length() < 3) {
                            Current_GroupID = "0" + Current_GroupID;
                        }

                        //making devno of 4 chars
                        String dno = ("" + Current_DevNo);
                        while (dno.length() < 4) {
                            dno = "0" + dno;
                        }

                        //making room no of 2 chars
                        String rno = ("" + Current_RoomNo);
                        while (rno.length() < 2) {
                            rno = "0" + rno;
                        }


                        switch (devTypes.valueOf(Current_DevName)) {
                            case GSR1:
                            case GSK1:
                            case WPS1:
                            case ACR1: {

                                //using same data for sprinkler/geyser/pir/AC

                                if (WirelessSwitchData.equals("ON")) {
                                    switchData = GYSER_ON_CMD;
                                } else if (WirelessSwitchData.equals("OFF")) {
                                    switchData = GYSER_OFF_CMD;
                                }
                                //assigning switch data to localRef variable for status reflection purpose
                                LocalRefData = switchData;

                                //making final data format for Gyser device
                                String format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";
                                switchData = format;

                                break;
                            }
                            case CLB1: {

                                if (WirelessSwitchData.equals("ON")) {
                                    switchData = SWITCH_ON_CMD + DeviceSwitchNumber;
                                } else if (WirelessSwitchData.equals("OFF")) {
                                    switchData = SWITCH_OFF_CMD + DeviceSwitchNumber;
                                }

                                // assigning switch data to localRef variable for
                                // status reflection purpose
                                LocalRefData = switchData;

                                // making final data format
                                String format = "0" + "01" + Current_GroupID + dno + rno + switchData
                                        + "000000000000000";
                                switchData = format;

                                break;
                            }
                            case DMR1: {
                                StaticVariables.printLog("TAG", "SWITCH ACTION NAME : " + switchName);

                                if (ModelList.get(0).isSelected()) {

                                    if (switchName.equals(DMR_LEVEL_TEXT)) {
                                        //checking if data contains Brightness count level
                                        for (int i = 1; i <= 10; i++) {
                                            //checking if Brightness count is selected
                                            if (WirelessSwitchData.equals("" + i)) {
                                                switch (i) {
                                                    case 1: {
                                                        //setting brightness level 1
                                                        switchData = DMR_1_CMD;
                                                        break;
                                                    }
                                                    case 2: {
                                                        //setting brightness level 2
                                                        switchData = DMR_2_CMD;
                                                        break;
                                                    }
                                                    case 3: {
                                                        //setting brightness level 3
                                                        switchData = DMR_3_CMD;
                                                        break;
                                                    }
                                                    case 4: {
                                                        //setting brightness level 4
                                                        switchData = DMR_4_CMD;
                                                        break;
                                                    }
                                                    case 5: {
                                                        //setting brightness level 5
                                                        switchData = DMR_5_CMD;
                                                        break;
                                                    }
                                                    case 6: {
                                                        //setting brightness level 6
                                                        switchData = DMR_6_CMD;
                                                        break;
                                                    }
                                                    case 7: {
                                                        //setting brightness level 7
                                                        switchData = DMR_7_CMD;
                                                        break;
                                                    }
                                                    case 8: {
                                                        //setting brightness level 8
                                                        switchData = DMR_8_CMD;
                                                        break;
                                                    }
                                                    case 9: {
                                                        //setting brightness level 9
                                                        switchData = DMR_9_CMD;
                                                        break;
                                                    }
                                                    case 10: {
                                                        //setting brightness level 10
                                                        switchData = DMR_10_CMD;
                                                        break;
                                                    }

                                                }

                                            }
                                        }
                                        //checking if data is to make fan ON
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
                                        }
                                    }

                                    //assigning switch data to localRef variable for status reflection purpose
                                    LocalRefData = switchData;


                                    String format = null;

                                    if (switchData.equals(DMR_ON_CMD) || switchData.equals(DMR_OFF_CMD)
                                            || switchData.equals(DMR_ON_OFF_CMD)) {

                                        //making final data format for dimmer device
                                        format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";
                                    } else {

                                        //making final data format for dimmer device and adding brightness characters
                                        format = "0" + "01" + Current_GroupID + dno + rno + DMR_BRIGHTNESS_PROCESS_CMD + switchData + "000000000000";
                                    }

                                    switchData = format;

                                } else {
                                    //Displaying Error Dialog
                                    ErrorDialog("Incomplete Details",
                                            "Please Select Atleast One Power Option !");

                                    //exiting from loop
                                    return;
                                }

                                break;
                            }
                            case RGB1: {
                                StaticVariables.printLog("TAG", "SWITCH ACTION NAME : " + switchName);
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
                                        //checking if data contains Brightness
                                        for (int i = 1; i <= 10; i++) {
                                            if (WirelessSwitchData.equals("" + i)) {
                                                //setting Rgb ON cmd and brightness level
                                                if (i != 10) {
                                                    switchData = RGB_BRIGHTNESS_START_CHAR_CMD + i;
                                                } else
                                                    switchData = RGB_BRIGHTNESS10_CMD;

                                            }
                                        }
                                    } else if (switchName.equals(RGB_SPEED_TEXT)) {
                                        //checking if data contains speed

                                        for (int i = 1; i <= 10; i++) {
                                            if (WirelessSwitchData.equals("" + i)) {
                                                //setting Rgb ON command and speed level
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
                                        }
                                    }

                                    //assigning switch data to localRef variable for status reflection purpose
                                    LocalRefData = switchData;

                                    String format = null;

                                    // making data format based on command selected for rgb
                                    if (switchData.length() == 3) {
                                        //making final data format for rgb device
                                        format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";

                                    } else {
                                        //making final data format for rgb device  i.e for colors
                                        format = "0" + "01" + Current_GroupID + dno + rno + RGB_COLOR_PROCESS_CMD + switchData + "000";

                                    }

                                    switchData = format;

                                } else {
                                    //Displaying Error Dialog
                                    ErrorDialog("Incomplete Details",
                                            "Please Select Atleast One Power Option !");

                                    //exiting from loop
                                    return;
                                }


                                break;
                            }
                            case CLS1:
                            case CRS1:
                            case CLD1:
                            case CRD1: {
                                //set command data for curtain 1
                                if (switchName.equals(CUR1_ACTION_TEXT)) {
                                    if (WirelessSwitchData.equals("OPEN")) {
                                        switchData = CUR1_OPEN_CMD;
                                    } else if (WirelessSwitchData.equals("CLOSE")) {
                                        switchData = CUR1_CLOSE_CMD;
                                    }
                                }
                                //set command data for curtain 2
                                if (switchName.equals(CUR2_ACTION_TEXT)) {
                                    if (WirelessSwitchData.equals("OPEN")) {
                                        switchData = CUR2_OPEN_CMD;
                                    } else if (WirelessSwitchData.equals("CLOSE")) {
                                        switchData = CUR2_CLOSE_CMD;
                                    }
                                }

                                //assigning switch data to localRef variable for status reflection purpose
                                LocalRefData = switchData;

                                //setting data for curtain board
                                String format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";
                                switchData = format;

                                break;
                            }
                            case DLS1: {

                                if (WirelessSwitchData.equals("OPEN")) {
                                    switchData = DOOR_LOCK_CMD;

                                }

                                // assigning switch data to localRef variable for
                                // status reflection purpose
                                LocalRefData = switchData;

                                // setting data for curtain board
                                String format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";
                                switchData = format;

                                break;
                            }
                            case PSC1:
                            case PLC1:
                            case SOSH:
                            case SWG1:
                            case SLG1:
                                //using curtain data commands for projector screen and projector lifter
                                if (WirelessSwitchData.equals("OPEN")) {
                                    switchData = CUR1_OPEN_CMD;
                                } else if (WirelessSwitchData.equals("CLOSE")) {
                                    switchData = CUR1_CLOSE_CMD;
                                }

                                //assigning switch data to localRef variable for status reflection purpose
                                LocalRefData = switchData;

                                //setting data for curtain board
                                String format = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";
                                switchData = format;

                                break;


                            default:
                                break;

                        }

                        StaticVariables.printLog("TAG", "data to set for SWB :" + switchData);

                        //checking if next record is there or not
                        if (k < Device_SelectedPositions.size() - 1) {
                            FINAL_SWITCH_DATA.append(switchData + ",");
                            FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber + ",");
                            FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData + ",");
                        } else {
                            FINAL_SWITCH_DATA.append(switchData);
                            FINAL_SWITCH_NUM_DATA.append(DeviceSwitchNumber);
                            FINAL_SWITCH_LOCAL_REF_DATA.append(LocalRefData);
                        }

                        isActionNotSelected = false;

                    } else {
                        StaticVariables.printLog("TAG", "select some action for Item");

                        //Displaying Error Dialog
                        ErrorDialog("Incomplete Details",
                                "Please Select Some Action For Selected Item!");

                        isActionNotSelected = true;
                        break;
                    }
                }

                if (!isActionNotSelected) {
                    StaticVariables.printLog("FINAL DATA  ", "final switch data :" + FINAL_SWITCH_DATA.toString() + " length :" + FINAL_SWITCH_DATA.toString().length()
                            + " switch Data in start : "
                            + "switch num data : "
                            + FINAL_SWITCH_NUM_DATA.toString());


                    //inserting record in database
                    try {
                        //converting string builder data in string format
                        String SwitchData_WLS = FINAL_SWITCH_DATA.toString();
                        String SwitchNumbers = FINAL_SWITCH_NUM_DATA.toString();
                        String LocalDataRef = FINAL_SWITCH_LOCAL_REF_DATA.toString();

                        // inserting panel configuration details in db
                        boolean isInserted = WhouseDB.Update_WRLS_details(Current_RoomNo, Current_RoomName,
                                Current_WRLS_DevName, Current_WRLS_DevNo,
                                Current_WRLS_DevType, Current_WRLS_DevID, Current_DevName, LocalDataRef,
                                PanelSwitchNumber, SwitchData_WLS, Current_DevType, Current_DevNo,
                                Current_DevID, SwitchNumbers, Current_WRLS_RoomName, Current_User_Dev_Name);

                        if (isInserted) {

                            Intent i = getIntent();
                            setResult((Activity.RESULT_OK), i);
                            finish();

                        } else {
                            //Displaying Error Dialog
                            ErrorDialog("Settings Not Saved", "Some Error Occured .Please Submit You Panel Setting Again!");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else {
                //Displaying Error Dialog
                ErrorDialog("Incomplete Details",
                        "Please select Atleast one Item from list!");

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    // method to insert data in db
    void insertSwbDbData() {
        try {
            // clearing string builder variables
            FINAL_SWITCH_NUM_DATA.delete(0, FINAL_SWITCH_NUM_DATA.length());
            FINAL_SWITCH_LOCAL_REF_DATA.delete(0, FINAL_SWITCH_LOCAL_REF_DATA.length());

            // getting current panel switch number
            try {
                PanelSwitchNumber = Integer.parseInt(PANEL_SWB_ID
                        + (PanelSwitchNo + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }


            String WirelessSwitchData = null;

            //if (Device_SelectedPositions.size() > 0 && PanelSwitchNo != -1)
            if (PanelSwitchNo != -1) {

                for (int i = 0; i < Device_SelectedPositions.size(); i++) {
                    // getting data set on particular switch board's switch
                    WirelessSwitchData = ModelList.get(Device_SelectedPositions.get(i))
                            .getButtonText();
                    if (WirelessSwitchData.equals(DEFAULT_SWITCH_ACTION_TEXT)) {
                        isActionNotSelected = true;
                        break;
                    }

                    //separate data with commas
                    if (i < Device_SelectedPositions.size() - 1)
                        FINAL_SWITCH_NUM_DATA.append((Device_SelectedPositions.get(i) + 1) + ",");
                    else if (i == Device_SelectedPositions.size() - 1) {
                        String switchName = ModelList.get(Device_SelectedPositions.get(i)).getName();
                        if (!switchName.equals(SWB_ALL_ON_OFF_TEXT))
                            FINAL_SWITCH_NUM_DATA.append((Device_SelectedPositions.get(i) + 1) + "");
                        else {
                            FINAL_SWITCH_NUM_DATA.append("0");
                            //setting command for all on/off action
                            if (WirelessSwitchData.equals("ALL ON")) {
                                switchData = ALL_ON_CMD;
                                FINAL_SWITCH_LOCAL_REF_DATA.append(switchData + ",");
                            } else if (WirelessSwitchData.equals("ALL OFF")) {
                                switchData = ALL_OFF_CMD;
                                FINAL_SWITCH_LOCAL_REF_DATA.append(switchData + ",");
                            }
                        }
                    }

                }

                if (!isActionNotSelected) {

                    //making group id of 3 chars
                    while (Current_GroupID.length() < 3) {
                        Current_GroupID = "0" + Current_GroupID;
                    }

                    //making devno of 4 chars
                    String dno = ("" + Current_DevNo);
                    while (dno.length() < 4) {
                        dno = "0" + dno;
                    }

                    //making room no of 2 chars
                    String rno = ("" + Current_RoomNo);
                    while (rno.length() < 2) {
                        rno = "0" + rno;
                    }


                    String DataFormat = null, swbData = null;

                    if (FINAL_SWITCH_NUM_DATA != null && FINAL_SWITCH_NUM_DATA.toString().equals("0")) {
						/*
							 setting data for all on/off switch

							 format e.g. here unicast(01) and bcast(02)

							  "0"(1 char) + ucast/broadcastMsg(2 char) + groupId(3 char) +
							 devno (4 char)+roomno(2 char)+ data(3char) + "000000000000000(15 char dummy)";
						 */
                        DataFormat = "0" + "01" + Current_GroupID + dno + rno + switchData + "000000000000000";

                    } else {

                        //format e.g.
                        //"0" + "01" + "000" + swbno +roomno+ "999" + cc1+cc2+cc3+"00000"+ff1+ff2+ff3+ff4+"000";

                        //arranging data for switchboard in desired format
                        swbData = getSwitchOperationCode(Current_DevName);

                        DataFormat = "0" + "01" + Current_GroupID + dno + rno + "999" + swbData;

                    }


                    //deleting last extra  token from FINAL_SWITCH_LOCAL_REF_DATA list
                    String LocalDataRef = (FINAL_SWITCH_LOCAL_REF_DATA.substring(0, FINAL_SWITCH_LOCAL_REF_DATA.length() - 1)).toString();
                    StaticVariables.printLog("TAG", "final local ref data :" + LocalDataRef);

                    StaticVariables.printLog("FINAL DATA  ", "final switch data :" + DataFormat + " length :" + DataFormat.length()
                            + " switch Data in start : "
                            + "switch num data : "
                            + FINAL_SWITCH_NUM_DATA.toString());

                    // inserting record in database
                    try {
                        // converting string builder data in string format
                        String SwitchData_WLS = DataFormat;

                        String SwitchNumbers = FINAL_SWITCH_NUM_DATA.toString();

                        if (SwitchData_WLS.length() == 30) {
                            // inserting panel configuration details in db
                            boolean isInserted = WhouseDB.Update_WRLS_details(
                                    Current_RoomNo, Current_RoomName,
                                    Current_WRLS_DevName, Current_WRLS_DevNo,
                                    Current_WRLS_DevType, Current_WRLS_DevID,
                                    Current_DevName, LocalDataRef, PanelSwitchNumber,
                                    SwitchData_WLS, Current_DevType, Current_DevNo,
                                    Current_DevID, SwitchNumbers,
                                    Current_WRLS_RoomName, Current_User_Dev_Name);

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
                        } else {
                            // Displaying Error Dialog
                            ErrorDialog("Incorrect Data Format",
                                    "Data Format is incorrect to save device settings.");

                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    StaticVariables.printLog("TAG", "select some action for Selected Item");
                    isActionNotSelected = false;
                    // Displaying Error Dialog
                    ErrorDialog("Incomplete Details",
                            "Please Select Some Switch Action For Selected Item!");
                }
            } else {
                isActionNotSelected = false;
                // Displaying Error Dialog
                ErrorDialog(
                        "Incomplete Details",
                        "Please select Atleast one Item from List!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    //method to refresh device switch list view to show status of switches
    //corresponding to panel switch
    void RefreshDeviceSwitchListView(int panelSwitchNumber) {

        //checking panel status
        isPanelConfiguredToSwitch();

        StaticVariables.printLog("TAG", "PANEL CONFIGURED STS : " + isPanelPreConfigured);
        //checking if panel is configured to currently selected device
        if (isPanelPreConfigured) {

            //method to uncheck model items
            unCheckModelItems();

            try {
                String[] wirlessDataList = null;
                String WswitchData = null, DswitchNo = null;

                //getting current panel switch number and incrementing it by 1
                //as index starts from 0
                int currentPswitchno = 0;
                //getting current  panel switch number
                try {
                    currentPswitchno = Integer.parseInt(PANEL_SWB_ID + (panelSwitchNumber + 1));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                //fetching data from database corresponding to panel switch
                Cursor mcursor = WhouseDB.ConfiguredPanelPreDetails(
                        Current_WRLS_DevNo, Current_DevNo, currentPswitchno);

                //checking if cursor have some data or not
                if (mcursor != null && mcursor.getCount() != 0) {
                    //moving cursor to first position
                    mcursor.moveToFirst();
                    //initialize array size
                    String[] DevSwitchNoList = new String[mcursor.getCount()];
                    //fetching  switch numbers already selected
                    //for currently selected panel
                    DswitchNo = mcursor.getString(mcursor.getColumnIndex(
                            WirelessConfigurationAdapter.DevSwitchNo));

                    StaticVariables.printLog("TAG", "Dev Switch Number : " + DswitchNo);

                    //fetching switch actions selected for switches configured to panel
                    WswitchData = mcursor.getString(mcursor.getColumnIndex(
                            WirelessConfigurationAdapter.DevLocalRef));

                    StaticVariables.printLog("TAG", "WswitchData  : " + WswitchData);

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
                                //setting item based on index decremented by 1 as list-
                                // view items index starts from 0
                                ModelList.get(SwitchIndex - 1).setSelected(true);

                                //getting key from list
                                key = wirlessDataList[i];

                                //fetching name (i.e key value ) of action
                                //selected for item from map
                                keyValue = Switch_CMDS.get(key);

                                StaticVariables.printLog("TAG", "DATA set :" + key + " keyval :" + keyValue + " i value :" + i);

                                //setting button text for selected item
                                ModelList.get(SwitchIndex - 1).setbuttontext(keyValue);

                            } else if (SwitchIndex == 0) {
                                //setting last item of list as selected i.e ALL ON/OFF
                                ModelList.get(ModelList.size() - 1).setSelected(true);

                                //getting key from list
                                key = wirlessDataList[wirlessDataList.length - 1];

                                //fetching name (i.e key value ) of action
                                //selected for item from map
                                keyValue = Switch_CMDS.get(key);

                                //setting button text for selected item
                                ModelList.get(ModelList.size() - 1).setbuttontext(keyValue);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                    //making delete button enable
                    deletSetting_btn.setEnabled(true);
                } else {
                    //making delete button disable
                    deletSetting_btn.setEnabled(false);
                }
                //checking if cursor is not null
                if (mcursor != null)
                    mcursor.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            //making delete button disable
            deletSetting_btn.setEnabled(false);
            //un checking model items
            unCheckModelItems();
        }
        //refreshing list view
        DeviceSwitch.invalidateViews();

    }


    // method to un check model item
    void unCheckModelItems() {
        // making list item unselected
        for (int s = 0; s < ModelList.size(); s++) {
            ModelList.get(s).setSelected(false);
            ModelList.get(s).setbuttontext(DEFAULT_SWITCH_ACTION_TEXT);
        }

    }

    // list item click listener
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position,
                            long id) {
        switch (parent.getId()) {

            case R.id.Device_Switches_list: {
                StaticVariables.printLog("TAG", "Clicked item position : " + position);
                // getting current position
                int getPosition = position;

                if (!ModelList.get(getPosition).isSelected()) {
                    // getting current check box instance
                    CheckBox cb = (CheckBox) view
                            .findViewById(R.id.checkBox_toggle);
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

                    DeviceSwitch.invalidateViews();
                    // String name = DeviceSwitcheList.get(position);


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
                            if (ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT1)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT2)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT3)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT4)) {
                                //un checking ALL ON/OFF  switch
                                if (ModelList.get(ModelList.size() - 1).getName()
                                        .equals(SWB_ALL_ON_OFF_TEXT)) {

                                    // Set the value of check box to maintain its state.
                                    ModelList.get(ModelList.size() - 1).setSelected(false);

                                }
                            } else if (ModelList.get(position).getName()
                                    .equals(SWB_ALL_ON_OFF_TEXT)) {

                                //un-checking all switches except all on/off switch
                                for (int j = 0; j < ModelList.size() - 1; j++) {
                                    // Set the value of check box to maintain its state.
                                    ModelList.get(j).setSelected(false);
                                }

                            } else {

                                //setting button text
                                button.setClickable(false);
                                button.setEnabled(false);

                                //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

                                // saving current text item state
                                //ModelList.get(position).setbuttontext("ON");
                                //button.setText("ON");

                                //un checking ALL ON/OFF  switch
                                if (ModelList.get(ModelList.size() - 1).getName()
                                        .equals(SWB_ALL_ON_OFF_TEXT)) {

                                    // Set the value of check box to maintain its state.
                                    ModelList.get(ModelList.size() - 1).setSelected(false);

                                }
                            }

                            //refresh list view
                            DeviceSwitch.invalidateViews();
                            break;
                        }
                        default:
                            break;

                    }

                } else {
                    // getting current check box instance
                    CheckBox cb = (CheckBox) view
                            .findViewById(R.id.checkBox_toggle);
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
                            if (ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT1)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT2)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT3)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_FAN_TEXT4)
                                    || ModelList.get(position).getName()
                                    .equals(SWB_ALL_ON_OFF_TEXT)) {
                                //do nothing
                            } else {
                                ModelList.get(getPosition).setbuttontext("OFF");
                                button.setText("OFF");
                            }
                            break;
                        }
                        default:
                            break;
                    }

                    DeviceSwitch.invalidateViews();
                }

                break;
            }
        }

    }

    // switch action alert dialog
    void ActionDialog(final List<String> list, final Button v,
                      final int itemPosition) {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                // Converting house array list to array
                switchActionArray = new String[list.size()];
                switchActionArray = list.toArray(switchActionArray);
                // making var true that dialog is open
                isDilaogOpened = true;
                AlertDialog.Builder dlg = new AlertDialog.Builder(
                        WirelessDeviceMoodConfiguration.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                dlg.setTitle("Select Switch Action");
                dlg.setCancelable(false);

                dlg.setSingleChoiceItems(switchActionArray, -1,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int pos) {
                                // getting current switch action position
                                switchAction_ItemPostion = pos;
                            }
                        });
                dlg.setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // making variable false
                                isDilaogOpened = false;
                                dialog.dismiss();
                            }
                        });
                dlg.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                // checking if item is selected or not
                                if (switchAction_ItemPostion != -1) {
                                    // getting selected item
                                    String btnText = switchActionArray[switchAction_ItemPostion];
                                    // setting selected item as text on selected
                                    // button
                                    v.setText(btnText);
                                    // saving current text item state
                                    ModelList.get(itemPosition).setbuttontext(
                                            btnText);
                                    // resetting variable
                                    switchAction_ItemPostion = -1;
                                } else {
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "Please Select Atleast One Action!",
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

    // ****************ADAPTER CLASS************************//

    class ListadApter_Toggle extends ArrayAdapter<Model> {
        private final List<Model> values;
        Context context;
        int resourceId;

        int itemclicked = R.drawable.wirles_set_backgroung_cb1;
        int itemunclicked = R.drawable.wirles_set_backgroung_cb;

        // adapter class constructor
        public ListadApter_Toggle(Context context, int layoutId,
                                  List<Model> values) {
            super(context, layoutId, values);
            this.context = context;
            this.values = values;
            resourceId = layoutId;

        }

        // current view method
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewHolder = null;

            if (convertView == null) {
                LayoutInflater inflator = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflator.inflate(resourceId, null);

                // initialize view holder class
                viewHolder = new ViewHolder();

                // fetching id of text view
                viewHolder.text = (TextView) convertView
                        .findViewById(R.id.textView_toggle);

                // fetching id of check box
                viewHolder.checkbox = (CheckBox) convertView
                        .findViewById(R.id.checkBox_toggle);

                viewHolder.mbutton = (Button) convertView
                        .findViewById(R.id.switchAction);

                // setting button click listener
                viewHolder.mbutton.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button btn = (Button) v;
                        int pos = (Integer) v.getTag();

                        // checking if item is selected
                        // before performing action from button
                        if (values.get(pos).isSelected() && !isDilaogOpened) {
                            StaticVariables.printLog("TAG", "Button clicked");

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
                                    if (values.get(pos).getName()
                                            .equals(SWB_FAN_TEXT1)
                                            || values.get(pos).getName()
                                            .equals(SWB_FAN_TEXT2)
                                            || values.get(pos).getName()
                                            .equals(SWB_FAN_TEXT3)
                                            || values.get(pos).getName()
                                            .equals(SWB_FAN_TEXT4)) {
                                        ActionDialog(FanActionList, btn, pos);
                                    } else if (values.get(pos).getName()
                                            .equals(SWB_ALL_ON_OFF_TEXT)) {
                                        ActionDialog(AllOnOffList, btn, pos);
                                    } else {
                                        ActionDialog(SwitchActionList, btn, pos);
                                    }

                                    break;
                                }
                                case GSR1:
                                case GSK1:
                                case WPS1:
                                case ACR1: {
                                    //showing sprinkler/geyser/pir/AC action list
                                    ActionDialog(CallBellActionList, btn, pos);
                                    break;
                                }
                                case CLB1: {
                                    // showing geyser action list
                                    ActionDialog(CallBellActionList, btn, pos);
                                    break;
                                }
                                case DMR1: {
                                    if (values.get(pos).getName()
                                            .equals(DMR_LEVEL_TEXT)) {
                                        ActionDialog(dimmerLevelList, btn, pos);
                                    } else {
                                        ActionDialog(dimmerActionList, btn, pos);
                                    }

                                    break;
                                }
                                case RGB1: {
                                    if (values.get(pos).getName()
                                            .equals(RGB_COLOURS_EFFECTS_TEXT)) {
                                        ActionDialog(rgbColour_EffectList, btn, pos);

                                    } else if (values.get(pos).getName()
                                            .equals(RGB_BRIGHTNESS_TEXT)) {
                                        ActionDialog(rgbBrightnessList, btn, pos);

                                    } else if (values.get(pos).getName()
                                            .equals(RGB_SPEED_TEXT)) {
                                        ActionDialog(rgbSpeedList, btn, pos);

                                    } else {
                                        ActionDialog(rgbActionList, btn, pos);
                                    }

                                    break;
                                }

                                case CLS1:
                                case CRS1:
                                case CLD1:
                                case CRD1: {
                                    if (values.get(pos).getName()
                                            .equals(CUR1_ACTION_TEXT)
                                            || values.get(pos).getName()
                                            .equals(CUR2_ACTION_TEXT)) {
                                        ActionDialog(curActionList, btn, pos);
                                    }
                                    break;
                                }

                                case PSC1:
                                case PLC1:
                                case SOSH:
                                case SWG1:
                                case SLG1:{
                                    //showing projector screen action list i.e same as curtain actions
                                    ActionDialog(curActionList, btn, pos);

                                    break;
                                }
                                case DLS1: {
                                    ActionDialog(doorActionList, btn, pos);

                                    break;
                                }
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

	/*		// set current position for check box
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
				viewHolder.mbutton
				.setText(values.get(position).getButtonText());
				// making button enable & clickable
				viewHolder.mbutton.setClickable(true);
				viewHolder.mbutton.setEnabled(true);

				switch (devTypes.valueOf(Current_DevName))
				{
				case SWD1:case S010:case S020:case S030:case S040:case S060:
				case S080:case S120:case S021:case S031:case S042:
				case S051:case S052:case S061:case S062:case S071:
				case S102:case S111:case SLT1:case SFN1:case DFN1: {
					// setting action list corresponding action
					// button
					if (values.get(position).getName()
							.equals(SWB_FAN_TEXT1)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT2)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT3)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT4)
							|| values.get(position).getName()
							.equals(SWB_ALL_ON_OFF_TEXT)) {
						//do nothing
					}else {
						viewHolder.mbutton.setClickable(false);
						viewHolder.mbutton.setEnabled(false);

					}

					break;
				}
				default :
					break;
				}


			} else {

				// making not selected item view unclicked
				convertView.setBackgroundResource(itemunclicked);

				// making button disable & unclickable
				viewHolder.mbutton.setClickable(false);
				viewHolder.mbutton.setEnabled(false);

				switch (devTypes.valueOf(Current_DevName))
				{
				case SWD1:case S010:case S020:case S030:case S040:case S060:
				case S080:case S120:case S021:case S031:case S042:
				case S051:case S052:case S061:case S062:case S071:
				case S102:case S111:case SLT1:case SFN1:case DFN1: {
					// setting action list corresponding action
					// button
					if (values.get(position).getName()
							.equals(SWB_FAN_TEXT1)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT2)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT3)
							|| values.get(position).getName()
							.equals(SWB_FAN_TEXT4)
							|| values.get(position).getName()
							.equals(SWB_ALL_ON_OFF_TEXT)) {

						// saving current text item state
						values.get(position).setbuttontext(DEFAULT_SWITCH_ACTION_TEXT);
						viewHolder.mbutton.setText(DEFAULT_SWITCH_ACTION_TEXT);

					}else{
						// saving current text item state
						values.get(position).setbuttontext("OFF");
						viewHolder.mbutton.setText("OFF");
					}

					break;
				}
				default :
					// setting button text in model
					values.get(position).setbuttontext(DEFAULT_SWITCH_ACTION_TEXT);

					// getting button text from model
					viewHolder.mbutton
					.setText(values.get(position).getButtonText());

					break;
				}


			}*/


            ////////////////////////////////////////////////////////////////////////
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

    // ******************ADAPTER CLASS ENDED*********************//


    //go back
    void goPrevious() {
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

}
