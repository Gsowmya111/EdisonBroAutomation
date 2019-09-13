package edisonbro.com.edisonbroautomation.operatorsettings;


/**
 *  FILENAME: WirelessDeletePanelSettings.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to delete wireless panel and settings saved across wireless panel selected.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class WirelessDeletePanelSettings extends Activity implements OnItemSelectedListener,OnClickListener{

	TextView panelSwitchNo,DevRoomName,DevName,DevSwitchNo;
	Button Delete_btn,deletPanel_btn;

	Spinner  room_names,wireless_type;
	SpinnerAdapter  roomNameAdapter,wirelessAdapter;
	ImageView  serverStatus,navigateBack;

	final String SWB_TYPE="10";
	String PanelName = null ,ROOM_NAME_WLS=null;
	final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5;
	int selectedItemPosition=0;
	boolean exceptionOccured=false;

	ArrayList<String> UniqueRoomList;
	ArrayList<String> currentpanel_RoomList= new ArrayList<String>();;

	List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;
	List<String> Device_Type_WRLS_List,Short_DevNames_WRLS_List,
	Full_DevNames_WRLS_List;

	//lists for device types
	ArrayList<String>  finalDevName_List=new ArrayList<String>();
	ArrayList<String>  finalDevTypes_List=new ArrayList<String>();
	ArrayList<String>  finalDevID_List=new ArrayList<String>();
	ArrayList<String>  finalDevTypeName_List=new ArrayList<String>();
	ArrayList<Integer> finalDevNo_List =new ArrayList<Integer>();

	ArrayList<String> finalRoomName_List =new ArrayList<String>();
	ArrayList<String> finalDeviceSwitchNo_List =new ArrayList<String>();

	//lists for wireless panel spinner 
	ArrayList<String>  finalDevName_WRLS_List=new ArrayList<String>();
	ArrayList<String>  finalDevTypes_WRLS_List=new ArrayList<String>();
	ArrayList<String>  finalDevID_WRLS_List=new ArrayList<String>();
	ArrayList<String>  finalDevTypeName_WRLS_List=new ArrayList<String>();
	ArrayList<String>  finalRoomName_WRLS_List =new ArrayList<String>();
	ArrayList<Integer> finalDevNo_WRLS_List =new ArrayList<Integer>();
	ArrayList<Integer> finalPanelSwitchNo_WRLS_List =new ArrayList<Integer>();

	int server_online= R.drawable.connected;
	int server_offline=R.drawable.not_connected;

	int spinnerLayoutId=R.layout.spinnerlayout,listlayout = R.layout.panel_records_listview;

	Tcp tcp=null;
	WirelessConfigurationAdapter WhouseDB=null;
	HouseConfigurationAdapter houseDB=null;

	boolean IS_SERVER_CONNECTED=false;

	String RoomName=null,DevicType=null,PanelType=null;
	int DevType_Position=0,PanelType_Position=0;
	LinearLayout PanelRoomLyt;
	TableLayout parentTablelyt;
	TableRow.LayoutParams tableRowParams ;

	int Current_WRLS_DevNo=0,Current_DevNo=0;
	ViewGroup container=null;
	TableRow table_row=null;

	String PANEL_SWB_ID="10";
	static boolean isTcpConnecting=false;
	String PIR_DevType="WPS1",CURRENT_WLS_TYPE=null,PIR_DevType2="WPD1";

	boolean isDbExists=false;
	LocalListArrangeTable locallist_adap;
	private ServerDetailsAdapter sdadap;

	private static final String TAG1="Delete Panel - ";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.wireless_delete_panel_settings);

		room_names = (Spinner) findViewById(R.id.room_name_list);
		wireless_type = (Spinner) findViewById(R.id.panel_name_list); 
		serverStatus=(ImageView) findViewById(R.id.server_status);
		parentTablelyt=(TableLayout) findViewById(R.id.ParentTable); 
		deletPanel_btn=(Button) findViewById(R.id.deletepanel);
		navigateBack=(ImageView) findViewById(R.id.imageView2);

		Tcp.tcpHost= Tcp_con.tcpAddress;
		Tcp.tcpPort= Tcp_con.tcpPort;

		//setting on click listener for button
		deletPanel_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				String panelName=null;
				try{
					panelName=wireless_type.getSelectedItem().toString();
				}catch(Exception e){
					e.printStackTrace();
				}

				if(Current_WRLS_DevNo!=0 && (panelName!=null && !panelName.equals("Select Panel")))
					DeletePanelWarningDialog(Current_WRLS_DevNo);
				else
					Toast.makeText(WirelessDeletePanelSettings.this, "select a panel first!", Toast.LENGTH_SHORT).show();


			}
		});


		// setting item selected listener for spinner 
		room_names.setOnItemSelectedListener(this);
		wireless_type.setOnItemSelectedListener(this);

		//setting house name for wireless database
		StaticVariables.WHOUSE_NAME=StaticVariables.HOUSE_NAME+"_WLS";;
		//setting database name with which wireless database is to save
		StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";;



		try{ 
			WhouseDB=new WirelessConfigurationAdapter(this);
			WhouseDB.open();			//opening wireless database	


			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();			//opening house database
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

		}else {
			popupdev("Please go to the Configuration Settings and download wireless database First");
		}

		try{
			//Fetching list of All room names from database and adding to local array list
			UniqueRoomList=new ArrayList<String>();
			UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
			UniqueRoomList.add("Select Room");

		}catch(Exception e){
			e.printStackTrace();
		}

		//Loading data in room name spinner
		roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
		room_names.setAdapter(roomNameAdapter);	// Setting adapter to spinner
		// Displaying Last item of list
		room_names.setSelection(UniqueRoomList.size()-1);	


		//fetching device full names
		FullDevNameList();
		//fetching device types
		DevTypeList();
		//fetching device short names
		ShortDevNameList();



		//fetching wireless device full names
		FullDevNameList_WRLS();
		//fetching wireless device types
		DevTypeList_WRLS();
		//fetching wireless device short names
		ShortDevNameList_WRLS();

		//setting table row layout params
		tableRowParams = new TableRow.LayoutParams(0,LayoutParams.MATCH_PARENT,1.0f);
		tableRowParams.setMargins(1, 1, 1, 1);


		//calling connection in activity
		RegainingConnection();


		//setting on click listener
		serverStatus.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
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
			}
		});

		navigateBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				goPrevious();
			}
		});


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


	// Filling Device List with Short Names Which Will be used Internally 
	private void ShortDevNameList(){
		Resources res = getResources();
		String[] shortNames = res.getStringArray(R.array.Short_DevNames);
		Short_DevNames_List = Arrays.asList(shortNames);
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
		};
		thread.start();
	}

	public void ReadlineData( final String RL) {
		StaticVariabes_div.log("RL:- " + RL, TAG1);


		if(RL.contains("&011S@"))
		{
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(WirelessDeletePanelSettings.this, "Device Deleted From Gateway Successfully", Toast.LENGTH_SHORT).show();
					DeletWirelessPanel(Current_WRLS_DevNo); //Current_WRLS_DevNo
					Current_WRLS_DevNo=0;
				}
			});

		}else if (RL.contains("&011W@")) {

			runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Device Not Deleted Try Again");
				}
			});

		} else if (RL.contains("&011T@")) {

			runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Device Deleted.Timer Not Deleted.Please Delete From Timer List");
				}
			});

		} else if (RL.contains("&011WL@")) {

			runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Device Not Deleted From Wireless Table.Please Try Again");
				}
			});

		}
	}

	private void popupappdetails(final String txt) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(WirelessDeletePanelSettings.this);
				alert.setTitle("Info");
				//alert.setMessage("Enter Password");
				final TextView applnnameinfo = new TextView(WirelessDeletePanelSettings.this);
				applnnameinfo.setTextSize(20);
				alert.setView(applnnameinfo);
				applnnameinfo.setText(txt);
				alert.setNegativeButton("ok",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

							}
						});
				alert.show();
			}
		});

	}
	// TCP handler for handling TCP responses
	private Handler TcpHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case READ_LINE: {
				final String Data = (String) msg.obj;
				StaticVariables.printLog("TCP RESPONSE", "DATA GET FROM TCP SOCKET :" + Data);
				ReadlineData(Data);
				break;
			}

			case READ_BYTE: {

				break;
			}

			case TCP_LOST: {
				runOnUiThread(new Runnable() {
					public void run() {
						serverStatus.setImageResource(server_offline);
					}
				});
				break;
			}
			}
		}
	};

	//filling panel list based on room selected
	void FillPanelList(String roomname){
		try{


			//clearing all lists
			finalDevName_List.clear();
			finalDevTypes_List.clear();
			finalDevNo_List.clear();
			finalDevID_List.clear();
			finalDevTypeName_List.clear();

			//clearing list
			if(finalDevName_WRLS_List.size()>0){
				finalDevName_WRLS_List.clear(); 
				finalDevTypeName_WRLS_List.clear();
				finalDevTypes_WRLS_List.clear();
				finalDevNo_WRLS_List.clear();
				finalDevID_WRLS_List.clear();
				finalRoomName_WRLS_List.clear();

			}


			for(int i=0;i<Device_Type_WRLS_List.size();i++){
				//getting device type from list
				String devtype=Device_Type_WRLS_List.get(i);
				//getting device full name from list
				String DevName=Full_DevNames_WRLS_List.get(i);
				String devid=null,devtypename=null,usr_dev_nam=null ;
				int devno=0;

				//fetching data cursor from config table 
				//having records related to given rooms
				Cursor mcursor=WhouseDB.Rooms_panelData(roomname,devtype);

				//getting cursor count
				int mcount=mcursor.getCount();

				if(mcursor!=null && mcount!=0){
					//moving cursor to first position
					mcursor.moveToFirst();
					int j=0;
					do{
						//getting data from cursor
						devno=mcursor.getInt(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVNO));
						devid=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVID));
						devtypename=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_DEVNAME));
						usr_dev_nam=mcursor.getString(mcursor.getColumnIndex(WirelessConfigurationAdapter.CNFG_Data3));

						finalDevName_WRLS_List.add(usr_dev_nam+"--"+devno);
						//finalDevName_WRLS_List.add(DevName+"--"+devno);

						finalDevTypeName_WRLS_List.add(devtypename);
						finalDevTypes_WRLS_List.add(devtype);
						finalDevNo_WRLS_List.add(devno);
						finalDevID_WRLS_List.add(devid);
						finalRoomName_WRLS_List.add(roomname);

						StaticVariables.printLog("WRLS","DEVICE DETAILS :"+"\n DEV NAME:"+DevName+j+
								"\nDEV NO:"+devno+"\nDEV ID:"+devid+"\nDEV TYP:"+devtype+"\n"
								+roomname+"\n"+"usr_dev_nam"+usr_dev_nam+"\n");

						j++;
					}while(mcursor.moveToNext());
				}

				//closing the cursor
				if(mcursor!=null){
					mcursor.close();
				}
			}



			//adding last element in list
			finalDevName_WRLS_List.add("Select Panel");

			//Loading data in room name spinner
			wirelessAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, finalDevName_WRLS_List);
			wireless_type.setAdapter(wirelessAdapter);	// Setting adapter to spinner

			// Displaying Last item of list
			wireless_type.setSelection(finalDevName_WRLS_List.size()-1);	


		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//invalidate previous view of table
	void invalidateTableView(){
		int rowNum=parentTablelyt.getChildCount();
		for(int i = (rowNum-1); i >0;  i--){  
			TableRow row = (TableRow) parentTablelyt.getChildAt(i); 
			parentTablelyt.removeView(row); 

		}	
		//invalidating table view
		parentTablelyt.invalidate();
	}

	//filling the listview with items to be Edit by user
	private void FillTableView(String wdevid,int wdevno){
		try{

			//clearing list first
			if(currentpanel_RoomList.size()>0){
				currentpanel_RoomList.clear();
				finalPanelSwitchNo_WRLS_List.clear();
				finalDevName_List.clear(); 
				finalDevTypeName_List.clear();
				finalDevTypes_List.clear();
				finalDevNo_List.clear();
				finalDevID_List.clear();

				finalDeviceSwitchNo_List.clear();
				finalRoomName_List.clear();
				invalidateTableView();
			}

			//filling the  room list corresponding to currently selected panel
			currentpanel_RoomList.addAll(WhouseDB.Current_PanelsRoomNameList(wdevid, wdevno));
			int k;
			//running loop until we have unique rooms
			for(k=0;k<currentpanel_RoomList.size();k++){

				String currentRoom=currentpanel_RoomList.get(k);

				int i;
				for(i=0;i<Device_Type_List.size();i++){
					//getting device type from list
					String devtype=Device_Type_List.get(i);
					String DevName=Full_DevNames_List.get(i);

					String devid=null,devtypename=null,switchNumbers=null,usr_devnam_panl;
					int devno=0,panelSwitchNo=0;

					StaticVariables.printLog("TAG","WID :"+wdevid+" WDNO :"+wdevno+" CRoom : "+currentRoom+" Dtype :"+devtype);

					//fetching records related to device from database
					Cursor mcursor=WhouseDB.Current_Configured_Records(wdevid, wdevno,currentRoom,devtype);

					//checking if cursor is not empty
					if(mcursor!=null){
						//getting count of records in cursor
						int mcount=mcursor.getCount();

						StaticVariables.printLog("COUNT","CUSROR COUNT : "+mcount);

						//checking if cursor count is 0
						if(mcount!=0){
							//moving cursor to first position
							mcursor.moveToFirst();
							int j=0;
							do{	
								devno=mcursor.getInt(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.DevNo));

								devid=mcursor.getString(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.DevId));

								devtypename=mcursor.getString(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.DeviceName));

								switchNumbers=mcursor.getString(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.DevSwitchNo));

								panelSwitchNo=mcursor.getInt(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.WRLS_SwitchNo));

								usr_devnam_panl=mcursor.getString(mcursor.getColumnIndex
										(WirelessConfigurationAdapter.Data3));


								//finalDevName_List.add(DevName+"--"+devno);
								finalDevName_List.add(usr_devnam_panl+"--"+devno);

								//finalDevName_List.add(DevName);
								finalDevTypeName_List.add(devtypename);
								finalDevTypes_List.add(devtype);
								finalDevNo_List.add(devno);
								finalDevID_List.add(devid);

								finalDeviceSwitchNo_List.add(switchNumbers);
								finalRoomName_List.add(currentRoom);

								int panelno=0;
								try{
									//removing first two character of panel switch number 
									//i.e code fo switch number
									panelno=Integer.parseInt((""+panelSwitchNo).substring(2));	
								}catch(NumberFormatException e){
									e.printStackTrace();
								}

								finalPanelSwitchNo_WRLS_List.add(panelno);


								StaticVariables.printLog("INFO"+" count2 :"+mcount,"DEVICE DETAILS :"+"\n DEV NAME:"+DevName+j+
										"\nDEV NO:"+devno+"\nDEV ID:"+devid+"\nDEV TYP:"+devtype+
										"\n"+" Panel Switch : "+panelSwitchNo+"\n");

								j++;
							}while(mcursor.moveToNext());
						}		

					}	

					//closing the cursor
					if(mcursor!=null){
						mcursor.close();
					}

				}

			}







			int rowCount=finalPanelSwitchNo_WRLS_List.size();
			if(rowCount>0){

				String switchNo_wls=null,dev_roomname=null,dev_name=null,dev_switchno=null;

				for(int i=0;i<rowCount;i++){

					//fetching data of each panel switch one by one
					switchNo_wls=""+finalPanelSwitchNo_WRLS_List.get(i);
					dev_roomname=finalRoomName_List.get(i);
					dev_name=finalDevName_List.get(i);
					dev_switchno=finalDeviceSwitchNo_List.get(i);

					//creating table row
					TableRow tableRow = new TableRow(this);	 
					tableRow.setBackgroundResource(R.drawable.border);

					//displaying panel switch number
					panelSwitchNo=new TextView(this);
					panelSwitchNo.setText(switchNo_wls);
					panelSwitchNo.setGravity(Gravity.CENTER);
					panelSwitchNo.setBackgroundResource(R.drawable.border);
					panelSwitchNo.setTextSize(16);
					panelSwitchNo.setTextColor(Color.parseColor("#4282D0"));
					//panelSwitchNo.set
					tableRow.addView(panelSwitchNo,tableRowParams);

					//displaying device room name
					DevRoomName=new TextView(this);
					DevRoomName.setText(dev_roomname);
					DevRoomName.setGravity(Gravity.CENTER);
					DevRoomName.setTextSize(16);
					DevRoomName.setBackgroundResource(R.drawable.border);
					DevRoomName.setTextColor(Color.parseColor("#4282D0"));
					tableRow.addView(DevRoomName,tableRowParams);

					//displaying dev name configured to current panel
					DevName=new TextView(this);
					DevName.setText(dev_name);
					DevName.setGravity(Gravity.CENTER);
					DevName.setTextSize(18);
					DevName.setBackgroundResource(R.drawable.border);
					tableRow.addView(DevName,tableRowParams);

					//displaying device switch numbers configured to panel
					DevSwitchNo=new TextView(this);
					DevSwitchNo.setText(dev_switchno);
					DevSwitchNo.setGravity(Gravity.CENTER);
					DevSwitchNo.setTextSize(18);
					DevSwitchNo.setBackgroundResource(R.drawable.border);
					tableRow.addView(DevSwitchNo,tableRowParams);

					//displaying delete button to delete particular panel switch details
					Delete_btn=new Button(this);
					Delete_btn.setOnClickListener(this);
					Delete_btn.setText("Delete");
					Delete_btn.setBackgroundResource(R.drawable.delete_btn);
					Delete_btn.setTextSize(18);
					Delete_btn.setTextColor(Color.parseColor("#FFFFFF"));
					Delete_btn.setTag(i);
					tableRow.addView(Delete_btn,tableRowParams);

					parentTablelyt.addView(tableRow);

				}


			}else{ 

				StaticVariables.printLog("refresh","view count:"+ parentTablelyt.getChildCount());

				invalidateTableView();

			}


		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//on item click events
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch(parent.getId()){
		case R.id.room_name_list :{

			if(position!=UniqueRoomList.size()-1){

				String RoomName=UniqueRoomList.get(position);
				//filling panels list
				FillPanelList(RoomName);
			}

			break;
		}

		case R.id.panel_name_list :{

			if(position!=finalDevName_WRLS_List.size()-1){
				//getting currently selected item's device number and device id
				Current_WRLS_DevNo=finalDevNo_WRLS_List.get(position);
				String devid_wls=finalDevID_WRLS_List.get(position);
				CURRENT_WLS_TYPE=finalDevTypeName_WRLS_List.get(position);

				//filling the listview based on panel selected
				FillTableView(devid_wls,Current_WRLS_DevNo);
			}

			break;
		}
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		// TODO Auto-generated method stub

	}



	//on button click events
	@Override
	public void onClick(View v) {

		//getting instance of current table row 
		table_row=(TableRow) v.getParent();

		//getting textview instance of current row
		TextView switchno=(TextView) table_row.getChildAt(0); 

		//getting panel switch number from first text view
		int panelSwitchNo=Integer.parseInt(PANEL_SWB_ID+(switchno.getText().toString()));
		//getting tag position of current view button
		int getposition=(Integer) v.getTag();

		StaticVariables.printLog("TAG", "Panel Switch number : "+panelSwitchNo+"id of view : "+getposition);
		/*Toast.makeText(getApplicationContext(), "Panel Switch number : "+
			switchno.getText().toString(), Toast.LENGTH_SHORT).show();
		 */
		//fetching current selected device number
		Current_DevNo=finalDevNo_List.get(getposition);

		//getting table view instance
		container = ((ViewGroup) table_row.getParent());

		//calling delete warning dialog
		DeleteWarningDialog(panelSwitchNo);

	}


	//method to delete Existing Panel Settings from db
	void DeletSettings(int panelSwitchno){
		try{
			StaticVariables.printLog("TAG"," Deleete details : \nPsno : "+panelSwitchno+"\nWdevno : "+Current_WRLS_DevNo+"\ndevno :"+Current_DevNo );
			boolean isDeleted=WhouseDB.DeletePanelSwitchDetails(Current_WRLS_DevNo,
					Current_DevNo, panelSwitchno);

			if(isDeleted){ 

				if(container.getChildCount()==1){
					String RoomName= room_names.getSelectedItem().toString();
					//filling panels list
					FillPanelList(RoomName); 
				}

				// delete the row and invalidate your view so it gets redrawn
				container.removeView(table_row);
				container.invalidate();

				//displaying success dialog
				SuccessDialog("Deleted", "Selected Panel Switch Settings Deleted Successfully!");
			}else{
				//displaying error dialog
				ErrorDialog("Delete Failed", "Delete Operation Failed.Please Try Again.");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//Delete Warning dialog
	void DeleteWarningDialog(final int panelSwitchno)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeletePanelSettings.this, AlertDialog.THEME_HOLO_DARK);
				dialog.setTitle("Confirm Delete ");
				dialog.setMessage("Do You Really Want To Delete Selected Panel's Switch Details ?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//dismiss the dialog
						dialog.dismiss();
					}
				});
				dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//calling delete action
						DeletSettings(panelSwitchno);
						//dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}	


	//method to delete Existing Panel Settings from db
	void DeletWirelessPanel(int panelDevNo){
		try{
			StaticVariables.printLog("TAG"," Deleete details : \nPdevno : "+panelDevNo);
			boolean isDeleted=WhouseDB.DeletePanel(panelDevNo);

			if(isDeleted){ 
				String room=room_names.getSelectedItem().toString();
				if(room!=null && !room.equals("Select Room")){
					//filling panels list
					FillPanelList(room);
				}


				//deleting panel from wired database if it is PIR
				if(CURRENT_WLS_TYPE.equals(PIR_DevType)||CURRENT_WLS_TYPE.equals(PIR_DevType2)){
					//deleting pir 
					boolean isdeleted=houseDB.deleteDevice(panelDevNo);
					if(isdeleted){
						StaticVariables.printLog("TAG","PIR deleted from house database");
					}else{
						StaticVariables.printLog("TAG","Pir Not Deleted From House database");
					}
				}

				//invalidating view
				invalidateTableView();

				Update_db_version_number();

				try {
					LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
					locallist_adap = new LocalListArrangeTable(WirelessDeletePanelSettings.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
					locallist_adap.open();
					locallist_adap.deleteall();
					locallist_adap.close();
				}catch(Exception e){
					e.printStackTrace();

				}

				//displaying success dialog
				SuccessDialog("Deleted", "Selected Panel Deleted Successfully!");
			}else{
				//displaying error dialog
				ErrorDialog("Delete Failed", "Delete Operation Failed.Please Try Again.");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}	

	//Delete Warning dialog
	void DeletePanelWarningDialog(final int PanelDevNo)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeletePanelSettings.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Confirm Delete ");
				dialog.setMessage("Do You Really Want To Delete Selected Panel ?");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//dismiss the dialog
						dialog.dismiss();
					}
				});
				dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//calling delete action
					//	DeletWirelessPanel(PanelDevNo); //Current_WRLS_DevNo
						Thread th=new Thread(){ public void run() {
							Send_dat_aes(""+PanelDevNo, "<", "*");
						}};th.start();

						//dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}


	public void Send_dat_aes(String StrTimer,String starttokn,String endtokn){

		String tosend=null;
		tosend=starttokn+StrTimer+endtokn;

		tosend.replaceAll(" ","");
		String temp_str=tosend.replaceAll("\n","");
		//StaticVariabes_div.log(temp_str.length()+" Data "+temp_str,TAG1);
		Tcp.tcpWrite_aes(temp_str);
	}
	// error dialog
	void ErrorDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeletePanelSettings.this, AlertDialog.THEME_HOLO_DARK);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//dismiss the dialog
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}

	public void Update_db_version_number(){
		if(StaticVariabes_div.housename!=null) {
			try{
			ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
			sdadap = new ServerDetailsAdapter(WirelessDeletePanelSettings.this);
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

	//success dialog
	void SuccessDialog(final String title,final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				AlertDialog.Builder dialog=new AlertDialog.Builder(WirelessDeletePanelSettings.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(title);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());// exit fully from app
					}
				});

				dialog.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}


	@Override
	protected void onPause() { 
		super.onPause();
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
		try {
			if (!HouseConfigurationAdapter.sdb.isOpen() &&
					!WirelessConfigurationAdapter.sdb.isOpen()) {
				houseDB.open();
				WhouseDB.open();

				StaticVariables.printLog("TAG", "DB open ");
			}
		}catch(Exception e){

		}
	} 


	//go back
	void goPrevious(){
		// going back to admin page
		Intent it = new Intent(WirelessDeletePanelSettings.this,Configuration_Main.class);
		startActivity(it);
		// adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();
	}

	//Back Press Event
	@Override
	public void onBackPressed() {
		goPrevious();
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
						Intent it=new Intent(WirelessDeletePanelSettings.this,Configuration_Main.class);
						startActivity(it);
						//adding transition to activity exit
						//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

}
