package edisonbro.com.edisonbroautomation.operatorsettings;



/**
 *  FILENAME: Wireless_Panel_Selection.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to select wireless panel to which devices has to be configured. .

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
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class Wireless_Panel_Selection extends Activity implements View.OnClickListener,AdapterView.OnItemSelectedListener

{
    String ligvalue,Pirlightvalue="0";

    //TextView roomName_wls;
    Button submit_btn ,b_home;
    Spinner wireless_type,wireless_room;
    SpinnerAdapter wirelessAdapter;
    ImageView serverStatus,navigateBack,btn_signal;


    String ROOM_NAME_WLS=null;
    final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5;

    boolean exceptionOccured=false;

    private static final String TAG1="WirelessPanelSelection - ";

    List<String> Device_Type_WRLS_List,Short_DevNames_WRLS_List,
            Full_DevNames_WRLS_List;

    //lists for wireless panel spinner
    ArrayList<String>  finalDevName_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalDevTypes_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalDevID_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalDevTypeName_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalRoomName_WRLS_List =new ArrayList<String>();
    ArrayList<Integer> finalDevNo_WRLS_List =new ArrayList<Integer>();

    ArrayList<String>  finalroomDevName_WRLS_List=new ArrayList<String>();
    ArrayList<String>  uniqueRoomName_WRLS_List =new ArrayList<String>();

    //ArrayList<String>  finalDevName_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalroomDevTypes_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalroomDevID_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalroomDevTypeName_WRLS_List=new ArrayList<String>();
    ArrayList<String>  finalroomRoomName_WRLS_List =new ArrayList<String>();
    ArrayList<Integer> finalroomDevNo_WRLS_List =new ArrayList<Integer>();

    int server_online=R.drawable.connected;
    int server_offline=R.drawable.not_connected;

    int spinnerLayoutId=R.layout.spinnerlayout,layoutID1 = R.layout.checkbox_listview
            ,layoutID2  = R.layout.list_item_explist;// R.layout.expand_view; //R.layout.list_item_explist;//

    Tcp tcp=null;
    HouseConfigurationAdapter houseDB=null;
    WirelessConfigurationAdapter WhouseDB=null;
    boolean IS_SERVER_CONNECTED=false;

    String PanelType=null;
    int PanelType_Position=-1;

    final int SWB_CONSTANT=1,
            CUR1_CONSTANT=2,CUR2_CONSTANT=3,
            FAN1_CONSTANT=4,FAN2_CONSTANT=5,
            DMR_CONSTANT=6, RGB_CONSTANT=7,
            ZR_CONSTANT=8,GSR_CONSTANT=9,
            CBS_CONSTANT=10,MSS_CONSTANT=11;


    final String CUR_LRS_DevType="CLS1",CUR_RS_DevType="CRS1"
            ,CUR_LRD_DevType="CLD1",CUR_RD_DevType="CRD1"
            ,SINGLE_FAN_DevType="SFN1",DUAL_FAN_DevType="DFN1"
            ,RGB_DevType="RGB1",DMR_DevType="DMR1",GSR_DevType="GSR1",
            CLS_DevType="CSW1",CB_DevType="CLB1",WLS_MS_DevType="WSS1";

    static boolean isTcpConnecting=false;
    String classname="config",housename,houseno;
    LocalDatabaseAdapter localDB=null;
    boolean isDbExists=false;


    ListView PanelSwitch;
    ExpandableListView listview;
    CheckBox Panel_Switch_1,Panel_Switch_2,Panel_Switch_3,Panel_Switch_4,Panel_Switch_5,Panel_Switch_6,
            Panel_Switch_7,Panel_Switch_8,Panel_Switch_9,Panel_Switch_10,Panel_Switch_11;        //*************************
    LinearLayout lay_Panel_Switch_1,lay_Panel_Switch_2,lay_Panel_Switch_3,lay_Panel_Switch_4,lay_Panel_Switch_5,
            lay_Panel_Switch_6,lay_Panel_Switch_7,lay_Panel_Switch_8,lay_Panel_Switch_9,lay_Panel_Switch_10,lay_Panel_Switch_11;


    CheckBox Panel_Switch_Array[];
    LinearLayout lay_Panel_Switch_Array[];

    final int REQUEST_CODE=1;

    ArrayList<String> UniqueRoomList;



    String PANEL_SWB_ID="10";
    int PanelSwitchNo = -1;


    final String PIR_FULL_NAME="PIR Sensor",PIR_DEVTYPE="718",PIR_SHORT_NAME="WPS1";
    final String PIR_FULL_NAME_2="PIR Direct Sensor",PIR_DEVTYPE_2="720",PIR_SHORT_NAME_2="WPD1";

    String PIR_SENSOR_DEVTYPE = "WPS1", NFC_TAG_DEVTYPE = "WNFC" , PIR_SENSOR_DEVTYPE_2 = "WPD1";

    String[] PanelSwitchesList;

    List<String> groupList, childList;

    Map<String, Map<String,String>> allItems;

    Map<String, Map<String,String>> allItems_devtyp;

    ExpandableListAdapter adp;

    Map<Integer,List<String>> selectionDataList ;

    String Current_RoomName = null, Current_WRLS_DevName = null,
            Current_WRLS_RoomName = null, Current_WRLS_DevID = null,
            Current_DevName = null, Current_DevType = null,Current_GroupID=null,
            Current_DevID = null,Current_Dev_UsrName = null;

    int Current_RoomNo = 0, Current_DevNo = 0, Current_WRLS_DevNo = 0,
            Current_WRLS_DevType = 0;

    boolean staticpanel;
    String panel_type_selected;
    int panel_swtch_count=0;
    private ServerDetailsAdapter sdadap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.activity_wireless_panel_selection);
        classname= getIntent().getStringExtra("classname");
        housename=getIntent().getStringExtra("housename");
        houseno=getIntent().getStringExtra("houseno");

        if((StaticVariabes_div.classname==null)||(StaticVariabes_div.classname.equals(""))){
            StaticVariabes_div.classname="config";
        }/*else{
			StaticVariabes_div.classname=classname;
		}*/

		/*//setting house name in static variables
		StaticVariables.HOUSE_NAME=housename;
		StaticVariables.HOUSE_NUMBER=houseno;*/

        //roomName_wls=(TextView) findViewById(R.id.PanelRoomName);
        wireless_type = (Spinner) findViewById(R.id.panel_type_list);
        serverStatus=(ImageView) findViewById(R.id.server_status);
        submit_btn = (Button) findViewById(R.id.submit_btn);
        navigateBack=(ImageView) findViewById(R.id.imageView2);
        wireless_room = (Spinner) findViewById(R.id.panel_room_list);
        btn_signal= (ImageView) findViewById(R.id.btnsignal);
        //setting click listener button
        submit_btn.setOnClickListener(this);
        //setting click listener for server status image
        serverStatus.setOnClickListener(this);
        navigateBack.setOnClickListener(this);

        serverStatus.setVisibility(View.INVISIBLE);
        btn_signal.setVisibility(View.INVISIBLE);

        b_home= (Button) findViewById(R.id.btnhome);
        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(Wireless_Panel_Selection.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });

        // setting item selected listener for spinner
        wireless_type.setOnItemSelectedListener(this);
        //	wireless_type.setEnabled(false);

        //setting item select listener for romnames
        wireless_room.setOnItemSelectedListener(this);


/////***************************display******************************************
        listview = (ExpandableListView) findViewById(R.id.HomeDeviceList);
        Panel_Switch_1= (CheckBox) findViewById(R.id.checkbox1);
        Panel_Switch_2= (CheckBox) findViewById(R.id.checkbox2);
        Panel_Switch_3= (CheckBox) findViewById(R.id.checkbox3);
        Panel_Switch_4= (CheckBox) findViewById(R.id.checkbox4);
        Panel_Switch_5= (CheckBox) findViewById(R.id.checkbox5);
        Panel_Switch_6= (CheckBox) findViewById(R.id.checkbox6);

        Panel_Switch_7= (CheckBox) findViewById(R.id.checkbox7);
        Panel_Switch_8= (CheckBox) findViewById(R.id.checkbox8);
        Panel_Switch_9= (CheckBox) findViewById(R.id.checkbox9);
        Panel_Switch_10= (CheckBox) findViewById(R.id.checkbox10);
        Panel_Switch_11= (CheckBox) findViewById(R.id.checkbox11);

        lay_Panel_Switch_1= (LinearLayout) findViewById(R.id.lay_check1);
        lay_Panel_Switch_2= (LinearLayout) findViewById(R.id.lay_check2);
        lay_Panel_Switch_3= (LinearLayout) findViewById(R.id.lay_check3);
        lay_Panel_Switch_4= (LinearLayout) findViewById(R.id.lay_check4);
        lay_Panel_Switch_5= (LinearLayout) findViewById(R.id.lay_check5);
        lay_Panel_Switch_6= (LinearLayout) findViewById(R.id.lay_check6);

        lay_Panel_Switch_7= (LinearLayout) findViewById(R.id.lay_check7);
        lay_Panel_Switch_8= (LinearLayout) findViewById(R.id.lay_check8);
        lay_Panel_Switch_9= (LinearLayout) findViewById(R.id.lay_check9);
        lay_Panel_Switch_10= (LinearLayout) findViewById(R.id.lay_check10);
        lay_Panel_Switch_11= (LinearLayout) findViewById(R.id.lay_check11);


        Panel_Switch_Array=new CheckBox[]{Panel_Switch_1,Panel_Switch_2,Panel_Switch_3,Panel_Switch_4,Panel_Switch_5,Panel_Switch_6,
                Panel_Switch_7,Panel_Switch_8,Panel_Switch_9,Panel_Switch_10,Panel_Switch_11};
        lay_Panel_Switch_Array=new LinearLayout[]{lay_Panel_Switch_1,lay_Panel_Switch_2,lay_Panel_Switch_3,lay_Panel_Switch_4,lay_Panel_Switch_5,lay_Panel_Switch_6,
                lay_Panel_Switch_7,lay_Panel_Switch_8,lay_Panel_Switch_9,lay_Panel_Switch_10,lay_Panel_Switch_11};

        for(int r=0;r<Panel_Switch_Array.length;r++) {
            Panel_Switch_Array[r].setVisibility(View.GONE);
            Panel_Switch_Array[r].setEnabled(false);
            lay_Panel_Switch_Array[r].setVisibility(View.INVISIBLE);
        }
        //  PanelSwitch = (ListView) findViewById(R.id.Panel_Switches_list);


        Panel_Switch_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=0){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_1.isChecked()){
                            lay_Panel_Switch_1.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 0;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(0);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_1.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                //PanelSwitchNo = 0;

                  /*  //refreshing listview
                    adp=null;
                    adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                    RefreshExpandableDeviceListView(0);

                    // setting adapter in list view
                    listview.setAdapter(adp);*/
            }
        });

        Panel_Switch_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=1){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_2.isChecked()){
                            lay_Panel_Switch_2.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 1;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(1);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_2.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);

                        }
                    }
                }

                //setting currently selected panel switch number
                //PanelSwitchNo = 1;


            }
        });
        Panel_Switch_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=2){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_3.isChecked()){
                            lay_Panel_Switch_3.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 2;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(2);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_3.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                //PanelSwitchNo = 2;


            }
        });

        Panel_Switch_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=3){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_4.isChecked()){
                            lay_Panel_Switch_4.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 3;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(3);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_4.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                //PanelSwitchNo = 3;


            }
        });

        Panel_Switch_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=4){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_5.isChecked()){
                            lay_Panel_Switch_5.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 4;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(4);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_5.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 4;


            }
        });

        Panel_Switch_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=5){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_6.isChecked()){
                            lay_Panel_Switch_6.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 5;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(5);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_6.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });

        Panel_Switch_7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=6){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_7.isChecked()){
                            lay_Panel_Switch_7.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 6;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(6);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_7.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });
        Panel_Switch_8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=7){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_8.isChecked()){
                            lay_Panel_Switch_8.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 7;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(7);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_8.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });

        Panel_Switch_9.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=8){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_9.isChecked()){
                            lay_Panel_Switch_9.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 8;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(8);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_9.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });

        Panel_Switch_10.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=9){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_10.isChecked()){
                            lay_Panel_Switch_10.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 9;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(9);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_10.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });
        Panel_Switch_11.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                for(int m=0;m<panel_swtch_count;m++){

                    if(m!=10){
                        lay_Panel_Switch_Array[m].setBackgroundResource(R.drawable.wls_switch_off);
                        Panel_Switch_Array[m].setChecked(false);
                    }else{
                        if(Panel_Switch_11.isChecked()){
                            lay_Panel_Switch_11.setBackgroundResource(R.drawable.display_button);
                            PanelSwitchNo = 10;

                            //refreshing listview
                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            RefreshExpandableDeviceListView(10);

                            // setting adapter in list view
                            listview.setAdapter(adp);
                        }else {
                            lay_Panel_Switch_11.setBackgroundResource(R.drawable.wls_switch_off);
                            PanelSwitchNo=-1;

                            adp=null;
                            adp = new ExpandedListAdapter(Wireless_Panel_Selection.this, groupList, allItems,allItems_devtyp);

                            listview.setAdapter(adp);
                        }
                    }
                }

                //setting currently selected panel switch number
                // PanelSwitchNo = 5;


            }
        });
//*********************************************************************************************


        //setting house name for wireless database
        StaticVariables.WHOUSE_NAME=StaticVariables.HOUSE_NAME+"_WLS";;
        //setting database name with which wireless database is to save
        StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";;

        try{
            houseDB=new HouseConfigurationAdapter(this);
            houseDB.open();			//opening house database

            WhouseDB=new WirelessConfigurationAdapter(this);
            WhouseDB.open();		//opening wireless   database
        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            isDbExists=WhouseDB.checkdb();
        } catch (IOException e) {
            //e.printStackTrace();
            StaticVariables.printLog("TAG","unable to open database");
        }

        if(isDbExists){

            //Fetching list of All room names from database and adding to local array list
            UniqueRoomList=new ArrayList<String>();
            UniqueRoomList.addAll(houseDB.WirelessRoomNameList());
            UniqueRoomList.add("Select Room");


            //fetching wireless device full names
            FullDevNameList_WRLS();
            //fetching wireless device types
            DevTypeList_WRLS();
            //fetching wireless device short names
            ShortDevNameList_WRLS();

            //filling wireless device spinner from database
            FillDeviceList_WRLS();

            //calling connection in activity
            RegainingConnection();

        }else{

            popupdev("Please go to the setting and download wireless database First");


        }
    }

    private void PanelSwitch_Visible_Invisible(){

    }


    // Filling Device Name List with Full Names which will Visible to End User
    private void FullDevNameList_WRLS(){
        Resources res = getResources();
        String[] fullNames = res.getStringArray(R.array.Full_DevNames_Wireless);
        Full_DevNames_WRLS_List = Arrays.asList(fullNames);
    }

    // Filling Device List with Short Names Which Will be used Internally
    private void ShortDevNameList_WRLS(){
        Resources res = getResources();
        String[] shortNames = res.getStringArray(R.array.Short_DevNames_Wireless);
        Short_DevNames_WRLS_List = Arrays.asList(shortNames);
    }

    // Filling Device Type Which Will be used Internally
    private void DevTypeList_WRLS(){
        Resources res = getResources();
        String[] devType = res.getStringArray(R.array.Device_Type_Wireless);
        Device_Type_WRLS_List = Arrays.asList(devType);

    }

    //method to fetch ip port
    public void fetchipportdetails(){
        try{
            //fetching server table cursor data to get ip and port
            Cursor mcursor=houseDB.fetchData(HouseConfigurationAdapter.SERVER_TABLE);
            if(mcursor!=null){
                String serverIP=mcursor.getString(
                        mcursor.getColumnIndex(HouseConfigurationAdapter.SERVER_IP));
                int server_PORT=mcursor.getInt(
                        mcursor.getColumnIndex(HouseConfigurationAdapter.SERVER_PORT));

                String ssid=mcursor.getString(
                        mcursor.getColumnIndex(HouseConfigurationAdapter.SERVER_SSID));

                StaticVariables.printLog("TAG","IP:"+serverIP+" PORT:"+server_PORT);
                //setting ip and port from database

                Tcp.tcpHost=serverIP;
                Tcp.tcpPort=server_PORT;
                //StaticVariables.Host=serverIP;
                // StaticVariables.Port=server_PORT;

                //fetching server password
                //String username=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SERVER_ADMIN_USERNAME));
                // String password=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SERVER_ADMIN_PASSWORD));

                //setting user name and password in static fields
                //StaticVariables.UserName=username;
                //StaticVariables.Password=password;

                // StaticVariabes_div.loggeduser=username;
                // StaticVariabes_div.loggedpwd=password;

                //checking whether connection is local or remote type
             /*   if(JsonHandler_ir.isRemoteConnection(getApplicationContext(),ssid)){

                    Toast.makeText(getApplicationContext(),
                            "Remote Connection",Toast.LENGTH_SHORT).show();

                }*/
            }

            //closing cursor
            if(mcursor!=null){
                mcursor.close();
            }

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //method to get auto TCP connection in class
    void RegainingConnection(){
        // get app context from database variables
        localDB=new LocalDatabaseAdapter(this);
        fetchipportdetails();
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
                            serverStatus.setImageResource(server_offline);	//showing server is disconnected
                        }
                    });
                    break;
                }
            }
        }
    };

    //filling wireless device list  with all details
    private void FillDeviceList_WRLS(){
        try{

            for(int i=0;i<Device_Type_WRLS_List.size();i++){
                //getting device type from list
                String devtype=Device_Type_WRLS_List.get(i);
                String DevName=Full_DevNames_WRLS_List.get(i);
                String devid=null,devtypename=null,roomName=null,usr_dev_nam=null;
                int devno=0;

                //fetching records related to device from database
                Cursor mcursor=WhouseDB.CNFG_DeviceRecords(devtype);

                int mcount=mcursor.getCount();
                if( mcursor!=null && mcount>0){
                    mcursor.moveToFirst();
                    int j=0;
                    do{

                        devno=mcursor.getInt(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVNO));
                        devid=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVID));
                        devtypename=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVNAME));
                        roomName=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_ROOMNAME));
                        usr_dev_nam=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_Data3));


                        // finalDevName_WRLS_List.add(DevName+"--"+devno);
                        finalDevName_WRLS_List.add(usr_dev_nam+"--"+devno);
                        finalDevTypeName_WRLS_List.add(devtypename);
                        finalDevTypes_WRLS_List.add(devtype);
                        finalDevNo_WRLS_List.add(devno);
                        finalDevID_WRLS_List.add(devid);
                        finalRoomName_WRLS_List.add(roomName);

                        StaticVariables.printLog("WRLS","DEVICE DETAILS :"+"\n DEV NAME:"+DevName+j+
                                "\nDEV NO:"+devno+"\nDEV ID:"+devid+"\nDEV TYP:"+devtype+"\n" +roomName+"\n");

                        j++;

                    }while(mcursor.moveToNext());
                }

                //closing cursor
                if(mcursor!=null){
                    mcursor.close();
                }
            }





            //adding last element in list
            //	finalDevName_WRLS_List.add("Select Panel");


            //Loading data in room name spinner
            //	wirelessAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, finalDevName_WRLS_List);
            //	wireless_type.setAdapter(wirelessAdapter);	// Setting adapter to spinner
            // Displaying Last item of list
            //	wireless_type.setSelection(finalDevName_WRLS_List.size()-1);



            finalRoomName_WRLS_List.add("Select Room");

            Set<String> temprr = new LinkedHashSet<String>(finalRoomName_WRLS_List );

            ArrayList<String> setList=new ArrayList<String>(temprr);

            uniqueRoomName_WRLS_List=setList;

            wirelessAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, uniqueRoomName_WRLS_List);
            wireless_room.setAdapter(wirelessAdapter);	// Setting adapter to spinner
            // Displaying Last item of list
            wireless_room.setSelection(uniqueRoomName_WRLS_List.size()-1);







        }catch(Exception e){
            e.printStackTrace();
        }
    }


    // device type collection
    enum devTypes {
        WS01,WS02,WS03,WS04,WS05,WS06,WC01,WC02,WF01,WF02,
        WD01,WDD1,WR01,WZ01,WSS1,WBS1,WGS1,WPS1,WNFC ,WPD1,WS21,WS51,WS71,WS81,WM03,WM06
    }

    //method to check which wireless device is selected
    int WirlessDeviceConstant(String devtype){
        int constant=0;
        switch(Wireless_Panel_Selection.devTypes.valueOf(devtype)){
            case WS01 : case WS02 : case WS03 :
            case WS04 : case WS05 : case WS06 : case WPS1: case WNFC: case WPD1:
            case WS21:case WS51:case WS71:case WS81: case WM03:case WM06:
                constant=SWB_CONSTANT;
                break;
            case WC01:
                constant=CUR1_CONSTANT;
                break;
            case WC02 :
                constant=CUR2_CONSTANT;
                break;
            case WF01:
                constant=FAN1_CONSTANT;
                break;
            case WF02:
                constant=FAN2_CONSTANT;
                break;
            case WD01:
                constant=DMR_CONSTANT;
                break;
            case WR01:
                constant=RGB_CONSTANT;
                break;
            case WZ01:
                constant=ZR_CONSTANT;
                break;
            case WGS1:
                constant=GSR_CONSTANT;
                break;
            case WBS1:
                constant=CBS_CONSTANT;
                break;
            case WSS1:
                constant=MSS_CONSTANT;
                break;
            default:
                constant=0;
                break;
        }

        return constant;
    }

    //Button click events
    @Override
    public void onClick(View v) {
        switch(v.getId())
        {

            case R.id.submit_btn:{
                for(int r=0;r<Panel_Switch_Array.length;r++) {
                    Panel_Switch_Array[r].setVisibility(View.GONE);
                    Panel_Switch_Array[r].setEnabled(false);
                    Panel_Switch_Array[r].setChecked(false);
                    lay_Panel_Switch_Array[r].setBackgroundResource(R.drawable.wls_switch_off);
                    lay_Panel_Switch_Array[r].setVisibility(View.INVISIBLE);
                }
                try{
                    PanelType=wireless_type.getSelectedItem().toString();

                }catch(Exception e){
                    e.printStackTrace();
                }

                if((PanelType==null ||PanelType.equals("Select Panel")))
                {
                    if(PanelType==null  ||PanelType.equals("Select Panel")){
                        DisplayErrorToast("Please Select Atleast One Wireless Panel");
                    }

                }else {
                    Current_WRLS_DevNo=0;
                    Current_WRLS_DevType=0;

                    Current_WRLS_DevName=null;
                    Current_WRLS_DevID=null;Current_WRLS_RoomName=null;

                    try{

                        //getting wireless panel data
                        Current_WRLS_DevNo=finalroomDevNo_WRLS_List.get(PanelType_Position);
                        Current_WRLS_DevType=Integer.parseInt(finalroomDevTypes_WRLS_List.get(PanelType_Position));
                        Current_WRLS_DevID=finalroomDevID_WRLS_List.get(PanelType_Position);
                        Current_WRLS_DevName=finalroomDevTypeName_WRLS_List.get(PanelType_Position);
                        Current_WRLS_RoomName=finalroomRoomName_WRLS_List.get(PanelType_Position);

                    }catch(Exception e){
                        e.printStackTrace();
                    }

                    StaticVariables.printLog("TAG"," cpdev :"+Current_WRLS_DevName);

                    try {
                        //switch activity-
                        switchActivity(Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID, Current_WRLS_DevName, Current_WRLS_RoomName);

                    }catch(Exception en){

                    }
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

    //switching to mood setting activity
    void prepareIntent(Intent it, int Current_WRLS_DevNo, int Current_WRLS_DevType,
                       String Current_WRLS_DevID, String Current_WRLS_DevName, String Current_WRLS_RoomName){
        try{

            //sending necessary data in intent
            it.putExtra("DevNo_WRLS", Current_WRLS_DevNo);
            it.putExtra("DevType_WRLS", Current_WRLS_DevType);
            it.putExtra("DevId_WRLS", Current_WRLS_DevID);
            it.putExtra("DevName_WRLS", Current_WRLS_DevName);
            it.putExtra("RoomName_WRLS", Current_WRLS_RoomName);

            startActivity(it);
            //adding transition to activity exit
            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            finish();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    //switching to other setting activity
    void switchActivity(int Current_WRLS_DevNo,int Current_WRLS_DevType,
                        String Current_WRLS_DevID,String Current_WRLS_DevName,String Current_WRLS_RoomName){

        Intent it=null;

        switch(WirlessDeviceConstant(Current_WRLS_DevName)){
            case 1: {
                staticpanel=false;
                panel_type_selected="other";
                Wireless_OtherDeviceSettings();

                //   it=new Intent(WirelessPanelConfiguration.this,WirelessOtherDeviceSettings.class);
                //   prepareIntent(it, Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID, Current_WRLS_DevName, Current_WRLS_RoomName);
                break;
            }
            case 2:case 3:case 4:case 5:case 6:case 7:case 8:case 9:case 10:{
                staticpanel=true;
                panel_type_selected="staticpanel";
                Wireless_Staticpanel_settings();
                //	it=new Intent(WirelessPanelConfiguration.this,WirelessStaticDeviceSettings.class);
                //	prepareIntent(it, Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID, Current_WRLS_DevName, Current_WRLS_RoomName);

                break;
            }
            case 11:{
                staticpanel=false;
                panel_type_selected="moodpanel";
                Wireless_MoodPanel_Settings();
                //	it=new Intent(WirelessPanelConfiguration.this,WirelessMoodSettings.class);
                //	prepareIntent(it, Current_WRLS_DevNo, Current_WRLS_DevType, Current_WRLS_DevID, Current_WRLS_DevName, Current_WRLS_RoomName);

                break;
            }
            default:
                DisplayErrorToast("No Configuration Defined for Selected type");
                break;
        }

    }

//*************************************************************************************************************************

    public void Wireless_OtherDeviceSettings(){

        // set indicator
        listview.setGroupIndicator(null);

        // get data from previous activity
        StaticVariables.printLog("DETAILS", "DETAILS OF DEVICE PANEL :\n" +"WDNO : "
                + Current_WRLS_DevNo + "\n" + "WDTYPE: "
                + Current_WRLS_DevType + "\n" + "WDID: "
                + Current_WRLS_DevID + "\n" + "WDNAME : "
                + Current_WRLS_DevName + "\n" + "WRoomName : "
                + Current_WRLS_RoomName + "\n" + "WSwitch Number : "+ "\n"
        );


        try {

            // checking whether device is PIR sensor
            if (Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE)||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){
                Lightvalue();
            }


            // checking whether device is PIR sensor or switch board type
            if (Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE) || Current_WRLS_DevName.equals(NFC_TAG_DEVTYPE)||
                    Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)) {
                StaticVariables.printLog("SWITCH COUNT", "PANEL SWITCH TYPE :" + 1);

                if (Current_WRLS_DevName.equals(NFC_TAG_DEVTYPE)) {
                    PanelSwitchesList = new String[1];
                    // adding number of switches in list view according panel
                    // type
                    PanelSwitchesList[0] = ("NFC Tag");
                } else {
                    PanelSwitchesList = new String[2];
                    // adding switches in list view for pir on and off state
                    PanelSwitchesList[0] = ("Switch ON");
                    PanelSwitchesList[1] = ("Switch OFF");
                }

            }else {
                // getting count from panel switch type from last two characters
                // of device type

                String Switch_fancount=Current_WRLS_DevName.substring(2);
                if(Switch_fancount.startsWith("0")){
                    int panelswitchCount = Integer.parseInt(Current_WRLS_DevName.substring(2));
                    StaticVariables.printLog("SWITCH COUNT", "PANEL SWITCH TYPE :" + panelswitchCount);
                    PanelSwitchesList = new String[panelswitchCount];
                    for (int i = 0; i < panelswitchCount; i++) {
                        // adding switches in list view according panel type
                        PanelSwitchesList[i] = ("Switch " + (i + 1));
                    }
                }else{
                    int Switchcount= Integer.parseInt(""+Current_WRLS_DevName.charAt(2));
                    int fancount=Integer.parseInt(""+Current_WRLS_DevName.charAt(3));

                    int panelswitchCount =Switchcount+3;
                    StaticVariables.printLog("SWITCH COUNT with Fan", "PANEL SWITCH TYPE :" + panelswitchCount);

                    PanelSwitchesList = new String[panelswitchCount];
                    for (int i = 0; i < panelswitchCount; i++) {
                        // adding switches in list view according panel type
                        if(i<Switchcount) {
                            PanelSwitchesList[i] = ("S" + (i + 1));
                        }else{

                            if(fancount==1) {
                                if (i == panelswitchCount - 3) {
                                    PanelSwitchesList[i] = ("F");
                                } else if (i == panelswitchCount - 2) {
                                    PanelSwitchesList[i] = ("F +");
                                } else if (i == panelswitchCount - 1) {
                                    PanelSwitchesList[i] = ("F -");
                                }
                            }
                        }
                    }
                }


            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        if(PanelSwitchesList!=null){

            if( (Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE))||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){

                panel_swtch_count=2;
                for(int r=0;r<2;r++) {
                    Panel_Switch_Array[r].setVisibility(View.VISIBLE);
                    Panel_Switch_Array[r].setEnabled(true);
                    lay_Panel_Switch_Array[r].setVisibility(View.VISIBLE);
                }

                Panel_Switch_1.setText(PanelSwitchesList[0]);
                Panel_Switch_2.setText(PanelSwitchesList[1]);



            }else{
                panel_swtch_count=PanelSwitchesList.length;

                for(int r=0;r<PanelSwitchesList.length;r++) {
                    lay_Panel_Switch_Array[r].setVisibility(View.VISIBLE);
                    Panel_Switch_Array[r].setVisibility(View.VISIBLE);
                    Panel_Switch_Array[r].setEnabled(true);
                    Panel_Switch_Array[r].setText(PanelSwitchesList[r]);

                }

            }
               /* // loading data to list adapter
                adapter = new ListadApter_ir(this, PanelSwitchesList, layoutID1);

                // setting adapter to list view
                PanelSwitch.setAdapter(adapter);*/

        }


        // filling groups in list
        fillGroupList_OtherDeviceSettings();

        // filling child items in list
        childList_OtherDeviceSettings();

        // preparing adapter
        adp = new ExpandedListAdapter(this, groupList, allItems,allItems_devtyp);
        // setting adapter in list view
        listview.setAdapter(adp);


    }

    // method to create group list
    void fillGroupList_OtherDeviceSettings() {
        //Fetching list of All room names from database and adding to local array list
        groupList = new ArrayList<String>();
        groupList.addAll(houseDB.WirelessRoomNameList());

    }

    // method to fill child list
    void childList_OtherDeviceSettings() {
        String roomName=null;
        int CurrentRoomNo=0;

        allItems = new LinkedHashMap<String, Map<String,String>>();
        allItems_devtyp = new LinkedHashMap<String, Map<String,String>>();

        //getting all the devices from each room
        for(int i=0;i<groupList.size();i++){

            //getting current room name
            roomName=groupList.get(i);

            //getting current room number
            CurrentRoomNo=houseDB.CurrentRoomNumber(roomName);

            //  HashMap<String,String> map=houseDB.getMoodDevices(CurrentRoomNo,Current_WRLS_DevName);
            HashMap<String,String> map=houseDB.getMoodDevices_userdevicenames(CurrentRoomNo,Current_WRLS_DevName);
            HashMap<String,String> map_devty=houseDB.getMoodDevices(CurrentRoomNo,Current_WRLS_DevName);

            if(map.size()>0){
                // making key and value pair
                allItems.put(roomName, map);
                allItems_devtyp.put(roomName, map_devty);
            }else{
                //remove room name from group list
                groupList.remove(roomName);
                i--;
            }


        }

    }
//*************************************************************************************************************

    public  void  Wireless_MoodPanel_Settings(){

        // set indicator
        listview.setGroupIndicator(null);
        try {

            //making array of panel switches
            PanelSwitchesList = new String[3];
            // adding number of switches in list view according panel type
            PanelSwitchesList[0] = ("Mood 1");
            PanelSwitchesList[1] = ("Mood 2");
            PanelSwitchesList[2] = ("Mood 3");


        } catch (Exception e) {
            e.printStackTrace();
        }

        // loading data to list adapter
        //adapter = new ListadApter_ir(this, PanelSwitchesList, layoutID1);
        // setting adapter to list view
        // PanelSwitch.setAdapter(adapter);

        panel_swtch_count=3;
        for(int r=0;r<3;r++) {
            Panel_Switch_Array[r].setVisibility(View.VISIBLE);
            Panel_Switch_Array[r].setEnabled(true);
            lay_Panel_Switch_Array[r].setVisibility(View.VISIBLE);
        }

        Panel_Switch_1.setText(PanelSwitchesList[0]);
        Panel_Switch_2.setText(PanelSwitchesList[1]);
        Panel_Switch_3.setText(PanelSwitchesList[2]);

        // filling groups in list
        fillGroupList_mood_panel();

        // filling child items in list
        childList_mood_panel();

        // preparing adapter
        adp = new ExpandedListAdapter(this, groupList, allItems,allItems_devtyp);

        // setting adapter in list view
        listview.setAdapter(adp);

    }


    // method to create group list
    void fillGroupList_mood_panel() {
        //Fetching list of All room names from database and adding to local array list
        groupList = new ArrayList<String>();
        groupList.addAll(houseDB.WirelessRoomNameList());

    }

    // method to fill child list
    void childList_mood_panel() {
        String roomName=null;
        int CurrentRoomNo=0;

        allItems = new LinkedHashMap<String, Map<String,String>>();
        allItems_devtyp = new LinkedHashMap<String, Map<String,String>>();
        //getting all the devices from each room
        for(int i=0;i<groupList.size();i++){

            //getting current room name
            roomName=groupList.get(i);

            //getting current room number
            CurrentRoomNo=houseDB.CurrentRoomNumber(roomName);

            HashMap<String,String> map=houseDB.getMoodDevices(CurrentRoomNo,Current_WRLS_DevName);
            HashMap<String,String> map_devt=houseDB.getspecificMoodDevices(CurrentRoomNo,Current_WRLS_DevName);
            if(map.size()>0){
                // making key and value pair
                allItems.put(roomName, map);
                allItems_devtyp.put(roomName, map_devt);

            }else{
                //remove room name from group list
                groupList.remove(roomName);
                i--;
            }

        }
    }


    //****************************************************************************************************************


    public void Wireless_Staticpanel_settings(){

        // set indicator
        listview.setGroupIndicator(null);

        // filling groups in list
        fillGroupList_staticpanel();

        // filling child items in list
        childList_staticpanel();

        // preparing adapter
        adp = new ExpandedListAdapter(this, groupList, allItems,allItems_devtyp);

        RefreshExpandableDeviceListView_staticpanel();

        // setting adapter in list view
        listview.setAdapter(adp);

    }

    // method to create group list
    void fillGroupList_staticpanel() {
        //Fetching list of All room names from database and adding to local array list
        groupList = new ArrayList<String>();
        groupList.addAll(houseDB.WirelessRoomNameList());

    }

    // method to fill child list
    void childList_staticpanel() {
        String roomName=null;
        int CurrentRoomNo=0;

        allItems = new LinkedHashMap<String, Map<String,String>>();
        allItems_devtyp= new LinkedHashMap<String, Map<String,String>>();
        //getting all the devices from each room
        for(int i=0;i<groupList.size();i++){

            //getting current room name
            roomName=groupList.get(i);

            //getting current room number
            CurrentRoomNo=houseDB.CurrentRoomNumber(roomName);

            HashMap<String,String> map=houseDB.getspecificMoodDevices(CurrentRoomNo,Current_WRLS_DevName);
            HashMap<String,String> map_devt=houseDB.getspecificMoodDevices(CurrentRoomNo,Current_WRLS_DevName);

            if(map.size()>0){
                // making key and value pair
                allItems.put(roomName, map);
                allItems_devtyp.put(roomName, map_devt);
            }else{
                //remove room name from group list
                groupList.remove(roomName);
                i--;
            }
        }

    }


    // method to refresh device switch list view to show status of switches
    // corresponding to panel switch
    void RefreshExpandableDeviceListView_staticpanel() {

        try {

            Set<String> parentKeys=allItems.keySet();

            String roomName;
            for(int k=0;k<parentKeys.size();k++){

                roomName=groupList.get(k);

                // fetching data from database corresponding to panel switch
                ArrayList<String> deviceNumbers = WhouseDB.ConfiguredStaticPanelDeviceNumberList(Current_WRLS_DevNo,roomName);


                //getting map
                Map map=allItems.get(groupList.get(k));


                HashMap<String,Integer> devNubmerPosition=new HashMap<String,Integer>();
                List<String> dataList=new ArrayList<String>();

                //getting key set
                Set<String> keys=map.keySet();

                int index=0;
                //iterating map to get key and values
                for(Iterator i = keys.iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    String value = (String) map.get(key);

                    StaticVariables.printLog("TAG","*******"+key + " = " + value);

                    devNubmerPosition.put(key, index);

                    //filling default data in selection list
                    dataList.add("0");

                    ++index;
                }

                String dno;
                int position;
                //filling all child default data
                for(int m=0;m<deviceNumbers.size();m++){
                    dno=deviceNumbers.get(m);
                    if(map.containsKey(dno)){
                        position=devNubmerPosition.get(dno);
                        dataList.set(position,"1");
                    }
                }
                //setting data in selection list & temporary list
                selectionDataList.put(k, dataList);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }


















    //*****************************************************************************************************************
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



    //Displaying Error Toast
    void DisplayErrorToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }


    //spinner item selection events
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position,
                               long id) {

        switch(parent.getId()){

            case R.id.panel_type_list:{
                if(position!=finalroomDevName_WRLS_List.size()-1){

                    PanelType_Position=position;

                    //getting selected pane room name and display in text view
                    //	String roomname=finalroomRoomName_WRLS_List.get(PanelType_Position);
                    //	roomName_wls.setText(roomname);

                }

                break;
            }

            case R.id.panel_room_list:{
                if(position!=uniqueRoomName_WRLS_List.size()-1){

                    PanelType_Position=position;

                    //getting selected pane room name and display in text view
                    String roomname=uniqueRoomName_WRLS_List.get(PanelType_Position);
                    //roomName_wls.setText(roomname);
                    onroomselected(roomname);
                }

                break;
            }

        }

    }


    public void onroomselected(String selectedroomname){
        finalroomDevTypeName_WRLS_List.clear();
        finalroomDevTypes_WRLS_List.clear();
        finalroomDevNo_WRLS_List.clear();
        finalroomDevID_WRLS_List.clear();
        finalroomRoomName_WRLS_List.clear();
        finalroomDevName_WRLS_List.clear();

        for(int i=0;i<finalRoomName_WRLS_List.size();i++){
            //getting device type from list
            //String devtype=Device_Type_WRLS_List.get(i);
            //String DevName=Full_DevNames_WRLS_List.get(i);
            //String devid=null,devtypename=null,roomName=null;
            //int devno=0;

            if((finalRoomName_WRLS_List.get(i)).equals(selectedroomname)){

                finalroomDevName_WRLS_List.add(finalDevName_WRLS_List.get(i));

                finalroomDevTypeName_WRLS_List.add(finalDevTypeName_WRLS_List.get(i));
                finalroomDevTypes_WRLS_List.add(finalDevTypes_WRLS_List.get(i));
                finalroomDevNo_WRLS_List.add(finalDevNo_WRLS_List.get(i));
                finalroomDevID_WRLS_List.add(finalDevID_WRLS_List.get(i));
                finalroomRoomName_WRLS_List.add(finalRoomName_WRLS_List.get(i));

            }
        }

        //adding last element in list
        finalroomDevName_WRLS_List.add("Select Panel");


        //Loading data in room name spinner
        wirelessAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, finalroomDevName_WRLS_List);
        wireless_type.setAdapter(wirelessAdapter);	// Setting adapter to spinner
        // Displaying Last item of list
        wireless_type.setSelection(finalroomDevName_WRLS_List.size()-1);

    }



    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    //open db
    void openWlsDb(){

        //opening database again if it is closed
        if(!WirelessConfigurationAdapter.sdb.isOpen()){
            WhouseDB.open();
            StaticVariables.printLog("TAG","DB opened again ");
        }

    }


    public void popupdev(String msg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {
                        dialog.cancel();
                        goPrevious();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
/*
	@Override
	protected void onPause() {
		super.onPause();
		if(HouseConfigurationAdapter.sdb.isOpen() &&
				WirelessConfigurationAdapter.sdb.isOpen()){
			houseDB.close();
			WhouseDB.close();

			StaticVariables.printLog("TAG","DB CLOSED ");
		}
	}*/

    protected void onPause() {
        super.onPause();
        if(houseDB!=null)
            if(HouseConfigurationAdapter.sdb.isOpen()){
                houseDB.close();
                StaticVariables.printLog("TAG","DB CLOSED ");
            }
        if(WhouseDB!=null){
            if(WirelessConfigurationAdapter.sdb!=null)
                if(WirelessConfigurationAdapter.sdb.isOpen()){
                    WhouseDB.close();
                    StaticVariables.printLog("TAG","DB CLOSED ");
                }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!HouseConfigurationAdapter.sdb.isOpen() &&
                !WirelessConfigurationAdapter.sdb.isOpen()){
            houseDB.open();
            WhouseDB.open();

            StaticVariables.printLog("TAG","DB open ");
        }
    }

    //go back
    void goPrevious(){
      /*  if(StaticVariabes_div.classname.equals("roomlist")){
            //going back to admin page
            Intent it=new Intent(WirelessPanelConfiguration.this,Roomlist.class);
            it.putExtra("housename",housename);
            it.putExtra("houseno",houseno);
            startActivity(it);
            //adding transition to activity exit
            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
            finish();
        }else {*/
        //going back to admin page
        Intent it=new Intent(Wireless_Panel_Selection.this,Configuration_Main.class);
        startActivity(it);
        //adding transition to activity exit
        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
        finish();
        //  }
    }

    //Back Press Event
    @Override
    public void onBackPressed() {
        goPrevious();
    }




    //*************************Expandable List Adapter**************************//

    public class ExpandedListAdapter extends BaseExpandableListAdapter {

        Activity mContext;
        List<String> groupItems;
        Map<String, Map<String,String>> keyMap;
        Map<String, Map<String,String>> keyMap_devtyp;
        Map<Integer,List<String>> parentChildList;

        List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;

        int resourceId;
        //  int itemclicked = R.drawable.btn_color_n;
        //  int itemunclicked = R.drawable.btn_color_p;

        int itemclicked = R.drawable.wirles_set_backgroung_cb1;
        int itemunclicked = R.drawable.wirles_set_backgroung_cb;

        //Drawable indicator_Min = getResources().getDrawable(android.R.drawable.button_onoff_indicator_on);
        // Drawable indicator_Max = getResources().getDrawable(android.R.drawable.ic_input_add);

        // Drawable indicator_Min = getResources().getDrawable(R.drawable.expnd_minus);
        // Drawable indicator_Max = getResources().getDrawable(R.drawable.expnd_plus);

        Drawable indicator_Min = getResources().getDrawable(R.drawable.exp_minus);
        Drawable indicator_Max = getResources().getDrawable(R.drawable.exp_plus);

        public ExpandedListAdapter(Activity context, List<String> group,
                                   Map<String, Map<String,String>> map ,Map<String, Map<String,String>> map_devtyp ) {
            mContext = context;
            groupItems=group ;
            keyMap = map;
            keyMap_devtyp=map_devtyp;
            resourceId=layoutID2;

            //initialize map list
            selectionDataList=new HashMap<Integer, List<String>>();

            //fetching device full names
            FullDevNameList();
            //fetching device types
            DevTypeList();
            //fetching device short names
            ShortDevNameList();

            //fill parent-child mapping
            fillParentChildList();

        }

        @Override
        public int getGroupCount() {
            return groupItems.size();
        }

        @Override
        public int getChildrenCount(int groupPosition) {
            return keyMap.get(groupItems.get(groupPosition)).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return groupItems.get(groupPosition);
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {

            String val =parentChildList.get(groupPosition).get(childPosition);
            return val;
        }



        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded,
                                 View convertView, ViewGroup parent) {

            String groupName = (String) getGroup(groupPosition);
            LayoutInflater inflater = (LayoutInflater) mContext
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            if (convertView == null) {
                // convertView = inflater.inflate(R.layout.groupitem,parent, false);
                convertView = inflater.inflate(R.layout.list_group,parent, false);
            }

            TextView groupText = (TextView) convertView.findViewById(R.id.groupitem);
            LinearLayout layout=(LinearLayout) convertView.findViewById(R.id.groupLaoyut);

            ImageView indicator=(ImageView) convertView.findViewById(R.id.imageView1);

            groupText.setText(groupName);

            layout.setBackgroundResource(itemunclicked);

            if(isExpanded){
                indicator.setImageDrawable(indicator_Min);
            }else{
                indicator.setImageDrawable(indicator_Max);
            }

            return convertView;
        }

        @Override
        public View getChildView(int groupPosition,int childPosition,
                                 boolean isLastChild, View convertView, ViewGroup parent) {

            String childName = (String)getChild(groupPosition, childPosition);

            ViewHolder viewHolder = null;

            if (convertView == null) {
                LayoutInflater inflator = (LayoutInflater) mContext
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflator.inflate(resourceId, null);

                // initialize view holder class
                viewHolder = new ViewHolder();

                // fetching id of text view
                viewHolder.text = (TextView) convertView.findViewById(R.id.textView_toggle);

                // fetching id of check box
                viewHolder.checkbox = (CheckBox) convertView.findViewById(R.id.checkBox_toggle);



                //setting on item click listener for expandable list items
                listview.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

                    @Override
                    public boolean onChildClick(ExpandableListView parent, View v,
                                                int groupPosition, int childPosition, long id) {

                        StaticVariabes_div.log("panel_type_selected"+panel_type_selected,TAG1);

                        if(panel_type_selected.equals("other")) {
                            if (PanelSwitchNo != -1) {

                                //getting current check box instance
                                CheckBox cb1 = (CheckBox) v.findViewById(R.id.checkBox_toggle);

                                if (!cb1.isChecked()) {
                                    // making check box checked
                                    cb1.setChecked(true);

                                    // setting current selected item's background
                                    v.setBackgroundResource(itemclicked);

                                    //setting status of check box & data set or not
                                    String data = "1";

                                    //saving state of child list items
                                    saveOrUpdateSelection(groupPosition, childPosition, data);

                                    String ItemData = getSelectionData(groupPosition, childPosition);
                                    StaticVariables.printLog("TAG", "Button clicked1 :" + ItemData);

                                    //fetching device type of clicked item
                                    Current_DevName = getDeviceType(groupPosition, childPosition);
                                    String switchName = parentChildList.get(groupPosition).get(childPosition);

                                    //checking if item is selected before performing action from button
                                    if (!data.equals("0") && PanelSwitchNo != -1)// && !isDilaogOpened)
                                    {

                                        //getting device number of selected device
                                        String selectedDevNo = switchName.substring(switchName.indexOf("---")).replace("---", "");
                                        //getting device number from string
                                        int devNo = Integer.parseInt(selectedDevNo);

                                        StaticVariables.printLog("TAG", "device number selected :" + devNo);

                                        Cursor mcursor = null;
                                        //fetching necessary details of selected device from database
                                        try {

                                            mcursor = houseDB.wiredDeviceRecords(devNo);
                                            if (mcursor != null && mcursor.getCount() > 0) {
                                                //moving cursor to first place
                                                mcursor.moveToFirst();

                                                switch (devTypes_devices.valueOf(Current_DevName)) {
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
                                                    case S141:
                                                    case S120:
                                                    case S110:
                                                    case S160:
                                                    case S052:
                                                    case S061:
                                                    case S062:
                                                    case S071:
                                                    case S102:
                                                    case S111:
                                                    case SLT1: {
                                                        //fetching necessary details from switchboard table
                                                        Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomName));
                                                        Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomNo));
                                                        Current_DevType = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceType));
                                                        Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceNo));
                                                        Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceId));
                                                        Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceName));
                                                        Current_GroupID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_GroupId));
                                                        Current_Dev_UsrName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_Usr_dev_Name));
                                                    }

                                                    default: {
                                                        //fetching necessary details from Master table
                                                        Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomName));
                                                        Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomNo));
                                                        Current_DevType = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceType));
                                                        Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceNo));
                                                        Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceId));
                                                        Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceName));
                                                        Current_GroupID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_GroupId));
                                                        Current_Dev_UsrName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_Usr_dev_Name));

                                                        break;
                                                    }
                                                }

                                            }
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                        //closing cursor if not null
                                        if (mcursor != null)
                                            mcursor.close();
                                        StaticVariables.printLog("TAG","Current_GroupID  :"+Current_GroupID);

                                        if(Current_GroupID==null){
                                            Current_GroupID="000";
                                        }
                                        //sending necessary data in intent
                                        Intent it=new Intent(Wireless_Panel_Selection.this,WirelessPanelFinalConfiguration.class);

                                        it.putExtra("RoomName", Current_RoomName);
                                        it.putExtra("DeviceGroupId", Current_GroupID);
                                        it.putExtra("RoomNo", Current_RoomNo);
                                        it.putExtra("DevType", Current_DevType);
                                        it.putExtra("DevNo", Current_DevNo);
                                        it.putExtra("DevID", Current_DevID);
                                        it.putExtra("DevTypeName", Current_DevName);
                                        it.putExtra("DevNo_WRLS", Current_WRLS_DevNo);
                                        it.putExtra("DevType_WRLS", Current_WRLS_DevType);
                                        it.putExtra("DevId_WRLS", Current_WRLS_DevID);
                                        it.putExtra("DevName_WRLS", Current_WRLS_DevName);
                                        it.putExtra("RoomName_WRLS", Current_WRLS_RoomName);
                                        it.putExtra("PanelSwitchNo_WRLS", PanelSwitchNo);
                                        it.putExtra("Pirlightsensorval_WRLS", Pirlightvalue);
                                        it.putExtra("curDevusrnam", Current_Dev_UsrName);
                                        //start activity for result
                                        startActivityForResult(it, REQUEST_CODE);
                                        overridePendingTransition(R.anim.railup, R.anim.railup);

                                        // ErrorDialog("success", "panel final");

                                    } else {
                                        // Displaying Error Dialog
                                        ErrorDialog(
                                                "Incomplete Details",
                                                "Please select Atleast one switch from Each List.To Update Existing Settings OR To Make New Settings !");

                                    }


                                } else {
                                    // making check box checked
                                    cb1.setChecked(false);

                                    // setting current selected item's background
                                    v.setBackgroundResource(itemunclicked);

                                    //setting status of check box & button Text
                                    String data = "0";

                                    //saving state of child list items
                                    saveOrUpdateSelection(groupPosition, childPosition, data);

                                }


                            } else {
                                // Displaying Error Dialog
                                ErrorDialog(
                                        "Incomplete Details",
                                        "Please select panel switch first!");

                            }
                        }else  if(panel_type_selected.equals("moodpanel")) {
                            if(PanelSwitchNo!=-1){

                                //getting current check box instance
                                CheckBox cb1 = (CheckBox) v.findViewById(R.id.checkBox_toggle);

                                if(!cb1.isChecked()){
                                    // making check box checked
                                    cb1.setChecked(true);

                                    // setting current selected item's background
                                    v.setBackgroundResource(itemclicked);

                                    //setting status of check box & data set or not
                                    String data="1";

                                    //saving state of child list items
                                    saveOrUpdateSelection(groupPosition,childPosition,data);

                                    String ItemData=getSelectionData(groupPosition,childPosition);
                                    StaticVariables.printLog("TAG","Button clicked1 :"+ItemData);

                                    //fetching device type of clicked item
                                    Current_DevName=getDeviceType(groupPosition,childPosition);
                                    String switchName= parentChildList.get(groupPosition).get(childPosition);

                                    //checking if item is selected before performing action from button
                                    if(!data.equals("0") && PanelSwitchNo!=-1)// && !isDilaogOpened)
                                    {

                                        //getting device number of selected device
                                        String selectedDevNo=switchName.substring(switchName.indexOf("---")).replace("---","");
                                        //getting device number from string
                                        int devNo=Integer.parseInt(selectedDevNo);

                                        StaticVariables.printLog("TAG","device number selected :"+devNo);

                                        Cursor mcursor=null;
                                        //fetching necessary details of selected device from database
                                        try{

                                            mcursor=houseDB.wiredDeviceRecords(devNo);
                                            if(mcursor!=null && mcursor.getCount()>0){
                                                //moving cursor to first place
                                                mcursor.moveToFirst();

                                                switch(devTypes_devices.valueOf(Current_DevName))
                                                {
                                                    case SWD1: case S010: 	case S020:  case S030: 	case S040: 	case S060: 	case S080:
                                                    case S021: 	case S031: 	case S042: 	case S051:   case S141: 	case S110: 	case S120: 	case S160:
                                                    case S052: 	case S061:	case S062: 	case S071: 	case S102: 	case S111: 	case SLT1:
                                                {
                                                    //fetching necessary details from switchboard table
                                                    Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomName));
                                                    Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomNo));
                                                    Current_DevType =mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceType));
                                                    Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceNo));
                                                    Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceId));
                                                    Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceName));
                                                    Current_GroupID=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_GroupId));
                                                }

                                                    default :{
                                                        //fetching necessary details from Master table
                                                        Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomName));
                                                        Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomNo));
                                                        Current_DevType =mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceType));
                                                        Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceNo));
                                                        Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceId));
                                                        Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceName));
                                                        Current_GroupID=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_GroupId));

                                                        break;
                                                    }
                                                }

                                            }
                                        }catch(Exception e){
                                            e.printStackTrace();
                                        }

                                        //closing cursor if not null
                                        if(mcursor!=null)
                                            mcursor.close();


                                        //sending data to mood setting activity
                                        //sending necessary data in intent
                                        Intent it=new Intent(Wireless_Panel_Selection.this,WirelessDeviceMoodConfiguration.class);

                                        it.putExtra("RoomName", Current_RoomName);
                                        it.putExtra("Current_GroupID", Current_GroupID);
                                        it.putExtra("RoomNo", Current_RoomNo);
                                        it.putExtra("DevType", Current_DevType);
                                        it.putExtra("DevNo", Current_DevNo);
                                        it.putExtra("DevID", Current_DevID);
                                        it.putExtra("DevTypeName", Current_DevName);
                                        it.putExtra("DevNo_WRLS", Current_WRLS_DevNo);
                                        it.putExtra("DevType_WRLS", Current_WRLS_DevType);
                                        it.putExtra("DevId_WRLS", Current_WRLS_DevID);
                                        it.putExtra("DevName_WRLS", Current_WRLS_DevName);
                                        it.putExtra("RoomName_WRLS", Current_WRLS_RoomName);
                                        it.putExtra("PanelSwitchNo_WRLS", PanelSwitchNo);

                                        //start activity for result
                                        startActivityForResult(it, REQUEST_CODE);
                                        overridePendingTransition(R.anim.railup, R.anim.railup);

                                    }else{
                                        // Displaying Error Dialog
                                        ErrorDialog(
                                                "Incomplete Details",
                                                "Please select Atleast one switch from Each List.To Update Existing Settings OR To Make New Settings !");

                                    }


                                }else{
                                    // making check box checked
                                    cb1.setChecked(false);

                                    // setting current selected item's background
                                    v.setBackgroundResource(itemunclicked);

                                    //setting status of check box & button Text
                                    String data="0";

                                    //saving state of child list items
                                    saveOrUpdateSelection(groupPosition,childPosition,data);

                                }


                            }else{
                                // Displaying Error Dialog
                                ErrorDialog(
                                        "Incomplete Details",
                                        "Please select panel switch first!");

                            }


                        }else  if(panel_type_selected.equals("staticpanel")) {

                            //getting current check box instance
                            CheckBox cb1 = (CheckBox) v.findViewById(R.id.checkBox_toggle);

                            if(!cb1.isChecked()){
                                // making check box checked
                                cb1.setChecked(true);

                                // setting current selected item's background
                                v.setBackgroundResource(itemclicked);

                                //setting status of check box & data set or not
                                String data="1";

                                //saving state of child list items
                                saveOrUpdateSelection(groupPosition,childPosition,data);

                                String ItemData=getSelectionData(groupPosition,childPosition);
                                StaticVariables.printLog("TAG","Button clicked1 :"+ItemData);

                                //fetching device type of clicked item
                                Current_DevName=getDeviceType(groupPosition,childPosition);
                                String switchName= parentChildList.get(groupPosition).get(childPosition);

                                //getting device number of selected device
                                String selectedDevNo=switchName.substring(switchName.indexOf("---")).replace("---","");
                                //getting device number from string
                                int devNo=Integer.parseInt(selectedDevNo);

                                StaticVariables.printLog("TAG","device number selected :"+devNo);

                                Cursor mcursor=null;
                                //fetching necessary details of selected device from database
                                try{

                                    mcursor=houseDB.wiredDeviceRecords(devNo);
                                    if(mcursor!=null && mcursor.getCount()>0){
                                        //moving cursor to first place
                                        mcursor.moveToFirst();

                                        switch(devTypes_devices.valueOf(Current_DevName))
                                        {
                                            case S010: 	case S020:  case S030: 	case S040: 	case S060: 	case S080:
                                           	case S021: 	case S031: 	case S042: 	case S051:  case S141: 	case S110: 	case S120: 	case S160:
                                            case S052: 	case S061: 	case S062: 	case S071: 	case S102: 	case S111: 	case SLT1:
                                        {
                                            //fetching necessary details from switchboard table
                                            Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomName));
                                            Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_RoomNo));
                                            Current_DevType =mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceType));
                                            Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceNo));
                                            Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceId));
                                            Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_DeviceName));
                                            Current_GroupID=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.SWB_GroupId));
                                        }

                                            default :{
                                                //fetching necessary details from Master table
                                                Current_RoomName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomName));
                                                Current_RoomNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_RoomNo));
                                                Current_DevType =mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceType));
                                                Current_DevNo = mcursor.getInt(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceNo));
                                                Current_DevID = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceId));
                                                Current_DevName = mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_DeviceName));
                                                Current_GroupID=mcursor.getString(mcursor.getColumnIndex(HouseConfigurationAdapter.MASTER_GroupId));

                                                break;
                                            }
                                        }

                                    }
                                }catch(Exception e){
                                    e.printStackTrace();
                                }

                                //closing cursor if not null
                                if(mcursor!=null)
                                    mcursor.close();


                                //sending necessary data in intent
                                Intent it=new Intent(Wireless_Panel_Selection.this,WirelessStaticPanelConfiguration.class);

                                it.putExtra("RoomName", Current_RoomName);
                                it.putExtra("DeviceGroupId", Current_GroupID);
                                it.putExtra("RoomNo", Current_RoomNo);
                                it.putExtra("DevType", Current_DevType);
                                it.putExtra("DevNo", Current_DevNo);
                                it.putExtra("DevID", Current_DevID);
                                it.putExtra("DevTypeName", Current_DevName);
                                it.putExtra("DevNo_WRLS", Current_WRLS_DevNo);
                                it.putExtra("DevType_WRLS", Current_WRLS_DevType);
                                it.putExtra("DevId_WRLS", Current_WRLS_DevID);
                                it.putExtra("DevName_WRLS", Current_WRLS_DevName);
                                it.putExtra("RoomName_WRLS", Current_WRLS_RoomName);

                                //start activity for result
                                startActivityForResult(it, REQUEST_CODE);
                                overridePendingTransition(R.anim.railup, R.anim.railup);

                            }else{
                                // making check box checked
                                cb1.setChecked(false);

                                // setting current selected item's background
                                v.setBackgroundResource(itemunclicked);

                                //setting status of check box & button Text
                                String data="0";

                                //saving state of child list items
                                saveOrUpdateSelection(groupPosition,childPosition,data);

                            }

                        }

                        return false;
                    }
                });


                // saving current view tag
                convertView.setTag(viewHolder);
                convertView.setTag(R.id.textView_toggle, viewHolder.text);
                convertView.setTag(R.id.checkBox_toggle, viewHolder.checkbox);

            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }


            // set current position for check box
            viewHolder.checkbox.setTag(groupPosition+childPosition);

            // setting current values for all controls in list row
            viewHolder.text.setText(childName);


            //displaying the selected item in action button
            if(!getSelectionData(groupPosition,childPosition).equals("0")){

                //showing item state selected
                convertView.setBackgroundResource(itemclicked);

                //making check box ticked
                viewHolder.checkbox.setChecked(true);

                String dt=getSelectionData(groupPosition,childPosition);
                StaticVariables.printLog("TAG","DATA IN SELECTION LIST :"+dt);

            } else{
                //making check box ticked
                viewHolder.checkbox.setChecked(false);

                //making not selected item view unclicked
                convertView.setBackgroundResource(itemunclicked);

            }

            return convertView;
        }

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }


        //view holder class
        class ViewHolder {
            protected TextView text;
            protected CheckBox checkbox;

        }

        //fetching device type
        String getDeviceType(int groupPosition,int childPosition){

            //getting map
            // Map map=keyMap.get(groupItems.get(groupPosition));

            //**??
            Map map=keyMap_devtyp.get(groupItems.get(groupPosition));
            //getting key set
            Set<String> keys=map.keySet();

            int j=0;
            //iterating map to get key and values
            for(Iterator i = keys.iterator(); i.hasNext();) {
                String key = (String) i.next();
                String value = (String) map.get(key);

                if(j==childPosition){
                    StaticVariables.printLog("TAG","group--------"+groupPosition+"---- child :"+j);
                    return value;
                }

                j++;
            }
            return null;
        }

        //filling parent-child mapping list
        void fillParentChildList(){

            Set<String> parentKeys=keyMap.keySet();

            parentChildList=new HashMap<Integer, List<String>>();

            for(int k=0;k<parentKeys.size();k++){
                List<String> devNames=new ArrayList<String>();
                //getting map
                Map map=keyMap.get(groupItems.get(k));
                //getting key set
                Set<String> keys=map.keySet();

                Map map_devtyp=keyMap_devtyp.get(groupItems.get(k));
                Set<String> keys_devtyp=map_devtyp.keySet();

                StaticVariables.printLog("TAG","group--------"+k);
                //iterating map to get key and values
                for(Iterator i = keys.iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    String value = (String) map.get(key);

                    StaticVariables.printLog("TAG","*******"+key + " = " + value);

                    ///////////////////////////////////////////////
                    // int index=Short_DevNames_List.indexOf(value);

                    // StaticVariables.printLog("TAG",index+"*******"+Full_DevNames_List.get(index) + " = " + value);
                    // String fullName=Full_DevNames_List.get(index);



                    //  String data=fullName+"---"+key;
                    // data.substring(data.indexOf("---"));
                    // devNames.add(data);

                    //////////////////////////////////////////////
                    devNames.add(value+"---"+key);
                }

                for(Iterator i = keys_devtyp.iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    String value = (String) map_devtyp.get(key);

                    StaticVariables.printLog("TAG","*******"+key + " = " + value);

                    ///////////////////////////////////////////////
                    int index=Short_DevNames_List.indexOf(value);

                    StaticVariables.printLog("TAG",index+"*******"+Full_DevNames_List.get(index) + " = " + value);
                    String fullName=Full_DevNames_List.get(index);



                    String data=fullName+"---"+key;
                    data.substring(data.indexOf("---"));
                    /// devNames.add(data);

                    //////////////////////////////////////////////
                    // devNames.add(value+"---"+key);
                }
                //adding parent child mapping
                parentChildList.put(k, devNames);

                //filling default data in selection list
                List<String> dataList=new ArrayList<String>();
                //filling all child default data
                for(int m=0;m<devNames.size();m++){
                    dataList.add("0");
                }
                //setting data in selection list & temporary list
                selectionDataList.put(k, dataList);
            }


        }

        // Filling Device Name List with Full Names which will Visible to End User
        private void FullDevNameList(){

            Full_DevNames_List=new ArrayList<String>();

            Resources res = mContext.getResources();
            String[] fullNames = res.getStringArray(R.array.Full_DevNames);
            Full_DevNames_List .addAll(Arrays.asList(fullNames));

            //adding pir full name
            Full_DevNames_List.add(PIR_FULL_NAME);
            Full_DevNames_List.add(PIR_FULL_NAME_2);
        }

        // Filling Device Type Which Will be used Internally
        private void DevTypeList(){

            Device_Type_List=new ArrayList<String>();

            Resources res = mContext.getResources();
            String[] devType = res.getStringArray(R.array.Device_Type);
            Device_Type_List.addAll(Arrays.asList(devType));

            //adding pir devtype
            Device_Type_List.add(PIR_DEVTYPE);
            Device_Type_List.add(PIR_DEVTYPE_2);
        }


        // Filling Device List with Short Names Which Will be used Internally
        private void ShortDevNameList(){

            Short_DevNames_List=new ArrayList<String>();

            Resources res = mContext.getResources();
            String[] shortNames = res.getStringArray(R.array.Short_DevNames);
            Short_DevNames_List.addAll(Arrays.asList(shortNames));

            //adding pir type
            Short_DevNames_List.add(PIR_SHORT_NAME);
            Short_DevNames_List.add(PIR_SHORT_NAME_2);

        }

    }


    //***********************ADAPTER CLASS ENDED***************************************//
    //method to save user selection data
    void saveOrUpdateSelection(int groupPosition,int chilPosition,String data){
        selectionDataList.get(groupPosition).set(chilPosition, data);
        StaticVariables.printLog("TAG","Data set for group :"+groupPosition+" child:"+chilPosition+" is :"+getSelectionData(groupPosition,chilPosition));
    }

    //method to get selection data
    String getSelectionData(int groupPosition,int chilPosition){
        return selectionDataList.get(groupPosition).get(chilPosition);
    }

    //device type collection
    enum devTypes_devices {
        SWD1,S010,S020,S030,S040,S060,S080,S021,S031,S042,S051,S052,S061,
        S062,S071,S102,S111,S141,S120,S110,S160,SWG1,SLG1,SLT1,SFN1,DFN1,DMR1,RGB1,ACR1,WOTS,WMD1,WMC1,WMB1,WBSO,WSS1,WBM1,
        WSM1,GSR1,DLS1,CLS1,CLD1,CRS1,CRD1,IRB1,RPR1,CLB1,CSW1,EGM1,SOSH,PSC1,PLC1,GSK1,
        WPS1,WPD1,CLNR,CLSH
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==REQUEST_CODE){

            if(resultCode==RESULT_OK){
                if ( (Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE)) || Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)) {

                    // Displaying Success Dialog
                    SuccessDialog("PIR Settings Saved",
                            "To operate House Devices please Go to WIRED and WIRELESS Configuration menu and upload house First.");

                } else {

                    // Displaying Success Dialog
                    SuccessDialog("Settings Saved", "Wireless Panel Settings Saved Successfully! \n"
                            + "To operate House Devices please Go to Wireless configuration menu and upload house First.");

                }

                Update_db_version_number();

            }else if(resultCode==RESULT_CANCELED){

                //msg("Dialog cancelled");

            }

        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    // success dialog
    void SuccessDialog(final String title, final String msg) {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(Wireless_Panel_Selection.this,
                        AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setCancelable(false);

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                dialog.setPositiveButton("Wireless Config Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent it = new Intent(Wireless_Panel_Selection.this, Configuration_Main.class);
                        startActivity(it);
                        // adding transition to activity exit
                        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        finish();

                    }
                });

                dialog.show();
            }
        });
    }

    // method to refresh device switch list view to show status of switches
    // corresponding to panel switch
    void RefreshExpandableDeviceListView(int panelSwitchNumber) {

        try {
            // getting current panel switch number and incrementing it by 1
            // as index starts from 0
            int currentPswitchno = 0;
            // getting current panel switch number
            try {
                currentPswitchno = Integer.parseInt(PANEL_SWB_ID + (panelSwitchNumber + 1));
            } catch (Exception e) {
                e.printStackTrace();
            }


            Set<String> parentKeys=allItems.keySet();

            String roomName;
            for(int k=0;k<parentKeys.size();k++){

                roomName=groupList.get(k);

                ArrayList<String> deviceNumbers;

                if ( (Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE))||Current_WRLS_DevName.equals(PIR_SENSOR_DEVTYPE_2)){
                    deviceNumbers = WhouseDB.ConfiguredPanelDeviceNumberList_PIR(Current_WRLS_DevNo,currentPswitchno,roomName,Pirlightvalue);
                }else{
                    // fetching data from database corresponding to panel switch
                    deviceNumbers = WhouseDB.ConfiguredPanelDeviceNumberList(Current_WRLS_DevNo,currentPswitchno,roomName);
                }

                //getting map
                Map map=allItems.get(groupList.get(k));


                HashMap<String,Integer> devNubmerPosition=new HashMap<String,Integer>();
                List<String> dataList=new ArrayList<String>();

                //getting key set
                Set<String> keys=map.keySet();

                int index=0;
                //iterating map to get key and values
                for(Iterator i = keys.iterator(); i.hasNext();) {
                    String key = (String) i.next();
                    String value = (String) map.get(key);

                    StaticVariables.printLog("TAG","*******"+key + " = " + value);

                    devNubmerPosition.put(key, index);

                    //filling default data in selection list
                    dataList.add("0");

                    ++index;
                }

                String dno;
                int position;
                //filling all child default data
                for(int m=0;m<deviceNumbers.size();m++){
                    dno=deviceNumbers.get(m);
                    if(map.containsKey(dno)){
                        position=devNubmerPosition.get(dno);
                        dataList.set(position,"1");
                    }
                }
                //setting data in selection list & temporary list
                selectionDataList.put(k, dataList);
            }




        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void Lightvalue() {

        final	AlertDialog.Builder alert = new AlertDialog.Builder(this);
        //alert.setTitle("Setting");
        //alert.setMessage("Enter The Time Value");

        LayoutInflater inflater = getLayoutInflater();
        View checkboxLayout = inflater.inflate(R.layout.pop_pirlightvaluesett, null);

        alert.setView(checkboxLayout);

        final Spinner spinnerlightval = (Spinner)checkboxLayout.findViewById(R.id.spinlightval);

        final String[]paths = {"0","1", "2", "3", "4","5","6"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(Wireless_Panel_Selection.this,
                android.R.layout.simple_spinner_item,paths);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerlightval.setAdapter(adapter);


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                goPrevious();

            }
        });
        //^^^^Set time BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ligvalue=spinnerlightval.getSelectedItem()+"";


                if(ligvalue!=null){
                    Pirlightvalue=ligvalue;
                    //StaticVariabes_div.log("ligvalue"+ligvalue+"length"+ligvalue.length(), TAG1);
                }else{
                    Lightvalue();
                }


            }
        });
        AlertDialog d = alert.create();
        d.setCancelable(false);
        d.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        d.show();
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);
        d.getWindow().setLayout(width, height);
        d.show();

    }





    // error dialog
    void ErrorDialog(final String title, final String msg) {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                AlertDialog.Builder dialog = new AlertDialog.Builder(
                        Wireless_Panel_Selection.this,
                        AlertDialog.THEME_HOLO_LIGHT);
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

    public void Update_db_version_number(){
        if(StaticVariabes_div.housename!=null) {
            try {
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                sdadap = new ServerDetailsAdapter(Wireless_Panel_Selection.this);
                sdadap.open();
                Cursor cursrno = sdadap.fetchrefno(1);
                String home_versionnumber = cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_db));
                cursrno.close();
                Float fvernum = Float.valueOf(home_versionnumber) + 0.1f;
                String s_vernum = String.format("%.2f", fvernum);
                sdadap.update_versionnumber(s_vernum);
                sdadap.close();
            }catch(Exception e){

            }
        }
    }
}
