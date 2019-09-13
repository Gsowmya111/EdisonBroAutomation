package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: AddNewDevice.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to provide input details like roomname, device type ,devicename to configure  wired devices
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
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Connections.UsbService;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;


public class AddNewDevice extends Activity implements TcpTransfer,OnClickListener ,
OnItemSelectedListener,OnCheckedChangeListener{
	EditText room_name , group_Id, et_user_dev_name;
	Button submit_btn,btnwtype,btsig,b_home;
	Spinner dev_type,room_names_list,groupId,sp_icons,sp_iconstype;
	ImageView bt_status,navigateBack,btnconstatus;

	SpinnerAdapter deviceAdapter,roomNameAdapter,groupIdAdapter;
	ArrayAdapter<String> autoAdapter;
	ProgressDialog pdlg;

	CheckBox groupCheck;

	int spinnerLayoutId= R.layout.spinnerlayout;
	String RoomName = null, DeviceType = null, Bluetooth_ID = null;
	String User_Device_Name,Room_Icon_Image;
	final int READ_LINE_BT=1 ,READ_BYTE_BT=2,EXCEPTION_BT=3,TCP_LOST_BT=4,BT_LOST_BT=5,ERR_USER=7,MAX_USER=8;


	int selectedItemPosition=0;
	boolean exceptionOccured=false;

	List<String>  Short_DevNames_List;
	List<String> Full_DevNames_List;
	List<String> Device_Type_List;
	ArrayList<String> UniqueRoomList;
	List<String> deviceGroupIdList;

	List<String>  Short_DevNames_List_ref;
	Tcp tcp=null;

	HouseConfigurationAdapter houseDB=null;
	LocalDatabaseAdapter locdb=null;

	int BT_ON  = R.drawable.usb_icon_on;
	int BT_OFF = R.drawable.usb_icon_off;

	int server_online=R.drawable.connected;
	int server_offline=R.drawable.not_connected;
	LinearLayout roomname_lyaout,groupIdLayout ,devnameLayout;
	boolean IS_SERVER_CONNECTED=false;

	static volatile int CMD_COUNT=1;
	boolean isGroupIdChecked=false ,IS_SERVER_NOT_AUNTHETICATED=false;	
	String DEFAULT_GROUPID="0", selectedGroupId=DEFAULT_GROUPID;

	static boolean isTcpConnecting=false;
	String MUD_DevType="WMD1" ,MUC_DevType="WMC1",MUB_DevType="WMB1",MBSO_DevType="WBSO"
			,BM_DevType="WBM1" ,SM_DevType="WSM1",
			SS_DevType="WSS1",OTS_DevType="WOTS";

	// Declaring the Integer Array with resourse Id's of Images for the Spinners


	Integer[] images;

	Integer[] ALL = { 0, R.drawable.im_entrance3, R.drawable.im_conference3,
			R.drawable.im_reception3, R.drawable.im_bedroom3, R.drawable.im_diningroom3,
			R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_studyroom3,R.drawable.im_playroom3 ,R.drawable.im_dgm3,
			R.drawable.im_cgm3,R.drawable.im_gm3,R.drawable.im_ps3,R.drawable.im_civilroom3,R.drawable.im_corridor3,R.drawable.im_visitorroom3,
			R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
			R.drawable.im_toiletf3,R.drawable.im_toiletm3,R.drawable.im_accounts3,R.drawable.im_bathroomm3,R.drawable.im_chef3,R.drawable.im_childcare3,R.drawable.im_cross3,R.drawable.im_entertain3,R.drawable.im_finance3,
			R.drawable.im_garden3,R.drawable.im_guest3,R.drawable.im_gym3,R.drawable.im_hall3,R.drawable.im_laundryy3,R.drawable.im_liftspace3,R.drawable.im_movie3,R.drawable.im_multimedia3,R.drawable.im_om3,R.drawable.im_pa3,R.drawable.im_poojaa3,R.drawable.im_psaccount3,R.drawable.im_psec3,
			R.drawable.im_recept3,R.drawable.im_servent3,R.drawable.im_sikh3,R.drawable.im_storeroom3,R.drawable.im_swimmingpool3,R.drawable.im_workstation3
	};


	Integer[] Home = { 0, R.drawable.im_entrance3,
			R.drawable.im_bedroom3, R.drawable.im_diningroom3,
			R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_studyroom3,R.drawable.im_playroom3 ,
			R.drawable.im_corridor3,R.drawable.im_visitorroom3,
			R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
			R.drawable.im_toiletf3,R.drawable.im_toiletm3,R.drawable.im_accounts3,R.drawable.im_bathroomm3,R.drawable.im_chef3,R.drawable.im_childcare3,R.drawable.im_cross3,R.drawable.im_entertain3,
			R.drawable.im_garden3,R.drawable.im_guest3,R.drawable.im_gym3,R.drawable.im_hall3,R.drawable.im_laundryy3,R.drawable.im_liftspace3,R.drawable.im_movie3,R.drawable.im_multimedia3,R.drawable.im_om3,R.drawable.im_poojaa3,
			R.drawable.im_servent3,R.drawable.im_sikh3,R.drawable.im_storeroom3,R.drawable.im_swimmingpool3,R.drawable.im_workstation3
	};

	Integer[] Office = { 0, R.drawable.im_entrance3, R.drawable.im_conference3,
			R.drawable.im_reception3, R.drawable.im_diningroom3,
			R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_playroom3 ,R.drawable.im_dgm3,
			R.drawable.im_cgm3,R.drawable.im_gm3,R.drawable.im_ps3,R.drawable.im_civilroom3,R.drawable.im_corridor3,R.drawable.im_visitorroom3,
			R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
			R.drawable.im_toiletf3,R.drawable.im_toiletm3,R.drawable.im_accounts3,R.drawable.im_entertain3,R.drawable.im_finance3,
			R.drawable.im_liftspace3,R.drawable.im_movie3,R.drawable.im_multimedia3,R.drawable.im_pa3,R.drawable.im_psaccount3,R.drawable.im_psec3,
			R.drawable.im_recept3,R.drawable.im_storeroom3,R.drawable.im_workstation3
   };

	Integer[] Hotel = { 0, R.drawable.im_entrance3, R.drawable.im_conference3,
			R.drawable.im_reception3, R.drawable.im_bedroom3, R.drawable.im_diningroom3,
			R.drawable.im_kitchen3,R.drawable.im_livingroom3,R.drawable.im_playroom3 ,R.drawable.im_corridor3,R.drawable.im_visitorroom3,
			R.drawable.im_serverroom3,R.drawable.im_liftlobby3,R.drawable.im_stairs3,R.drawable.im_toiletcorridor3,R.drawable.im_toiletdis3,
			R.drawable.im_toiletf3,R.drawable.im_toiletm3,R.drawable.im_accounts3,R.drawable.im_bathroomm3,R.drawable.im_chef3,R.drawable.im_childcare3,R.drawable.im_cross3,R.drawable.im_entertain3,R.drawable.im_finance3,
			R.drawable.im_garden3,R.drawable.im_guest3,R.drawable.im_gym3,R.drawable.im_hall3,R.drawable.im_laundryy3,R.drawable.im_liftspace3,R.drawable.im_movie3,R.drawable.im_multimedia3,
			R.drawable.im_recept3,R.drawable.im_servent3,R.drawable.im_storeroom3,R.drawable.im_swimmingpool3,
	};
	private static final int READ_BYTE = 1;
	private static final int READ_LINE = 2;
	private static final int ServStatus = 3;
	private static final int signallevel = 4;
	private static final int NetwrkType = 5;
	private static final int MAXUSER = 6;
	private static final int ERRUSER = 7;
	private static final int TCP_LOST = 8;

	//**********************************************
	int sl;
	boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
	String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
	//************************************************************************
	int delay = 0; // delay for 1 sec.
	int period = 1000;
	Timer timer = null;
	//********************************************************************

	boolean Usb_device_connected=false;
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
	private TextView display;
	private EditText editText;
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



	//****************************************************************
	private static final String TAG1="AddNewDevice - ";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		//setContentView(R.layout.newconfig);
		setContentView(R.layout.addnewdevice_wired);
		mHandler = new MyHandler(this);
		//setting activity view to method for hiding keyboard
		setupUI(findViewById(R.id.NewConfigLayout));

		room_names_list = (Spinner) findViewById(R.id.room_name_spin);
		room_name =  (EditText) findViewById(R.id.room_name);
		group_Id= (EditText) findViewById(R.id.gid);
		dev_type = (Spinner) findViewById(R.id.devtype);
		submit_btn = (Button) findViewById(R.id.submit_btn);
		bt_status=(ImageView) findViewById(R.id.bt_status);
		btnconstatus=(ImageView) findViewById(R.id.btnconstatus);
		btsig=(Button) findViewById(R.id.btnsignal);
		sp_icons= (Spinner) findViewById(R.id.spinner_icons);
		sp_iconstype= (Spinner) findViewById(R.id.spinner_iconstype);

		navigateBack=(ImageView) findViewById(R.id.imageView2);

		et_user_dev_name= (EditText) findViewById(R.id.et_usrdevname);

		roomname_lyaout=(LinearLayout) findViewById(R.id.roomNameLyt);
		groupIdLayout=(LinearLayout) findViewById(R.id.groupCheckBoxLayout);
		devnameLayout=(LinearLayout) findViewById(R.id.devnamlay);

		groupId=(Spinner) findViewById(R.id.groupID); 
		groupCheck=(CheckBox) findViewById(R.id.groupCheck);

		//setting text field layout visibility gone
		roomname_lyaout.setVisibility(View.GONE);

		//check change listener for checkbox
		groupCheck.setOnCheckedChangeListener(this);

		//setting click listeners
		submit_btn.setOnClickListener(this);
		//submit_btn.setOnClickListener(this);
		btnconstatus.setOnClickListener(this);
		dev_type.setOnItemSelectedListener(this);	
		room_names_list.setOnItemSelectedListener(this);	
		groupId.setOnItemSelectedListener(this);
		sp_icons.setOnItemSelectedListener(this);



		navigateBack.setOnClickListener(this);


		b_home= (Button) findViewById(R.id.btnhome);
		b_home.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				Intent intnt=new Intent(AddNewDevice.this, Main_Navigation_Activity.class);
				startActivity(intnt);
				finish();
			}
		});


		//initialize grouplist
		deviceGroupIdList=new ArrayList<String>();

		//making group id spinner invisible
		disableCheckBox();

		try{
			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();			//opening house database

			locdb=new LocalDatabaseAdapter(this);
			locdb.opendb();
		}catch(Exception e){
			e.printStackTrace();
		}


		//Fetching list of All room names from database and adding to local array list
		UniqueRoomList=new ArrayList<String>();
		UniqueRoomList.addAll(houseDB.RoomNameList());

		UniqueRoomList.add("Add New Room");
		UniqueRoomList.add("Select Room");


		//making adapter for auto complete text view
		autoAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList); 
		//setting adapter to auto complete text view
		room_names_list.setAdapter(autoAdapter);




		// Setting a Custom Adapter to the Spinner
		//sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, images));
		sp_icons.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

				if(position!=0) {
					String imageName = getResources().getResourceName(images[position]);
					//int actual=imageName.lastIndexOf(":drawable/",0);
					int actual=imageName.indexOf(":drawable/",0);
					String actimg=imageName.substring(actual+10,imageName.length()-1);
					//Toast.makeText(AddNewDevice.this, actimg+"  name " + imageName, Toast.LENGTH_SHORT).show();
					Room_Icon_Image=actimg;
				}else{

					Room_Icon_Image="select";
				}

			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		// Setting a Custom Adapter to the Spinner
		//sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, images));
		sp_iconstype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


					String swbicnsselectd=String.valueOf(sp_iconstype.getSelectedItem());

				    if(swbicnsselectd.equals("Home")) {
						sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, Home));
						images=Home;
					}else if(swbicnsselectd.equals("Hotel")) {
						sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, Hotel));
						images=Hotel;
					}else if(swbicnsselectd.equals("Office")) {
						sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, Office));
						images=Office;
					}else{

						sp_icons.setAdapter(new MyAdapter(AddNewDevice.this, R.layout.custom, ALL));
						images=ALL;
					}


			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {

			}
		});

		//fetching intent
		Intent DataPacket=getIntent();				
		boolean isAddNewDevice=false;

		try{
			if(DataPacket!=null)
				isAddNewDevice=DataPacket.getBooleanExtra("isAddNewDevice",false);
		}catch(Exception e){
			e.printStackTrace();
		}

		//making current room name selected
		if(isAddNewDevice){
			String currentRoom= StaticVariables.CurrentConfigurationRoom;
			int index=UniqueRoomList.indexOf(currentRoom);

			room_names_list.setSelection(index);
		}else{

			//setting selection in spinner
			if(UniqueRoomList.size()>2){ 
				room_names_list.setSelection(UniqueRoomList.size()-1); 
			}else{ 
				room_names_list.setSelection(UniqueRoomList.size()-2); 
			}

		}


		FullDevNameList();				// Assigning Full names For Devices
		ShortDevNameList();				// Assigning short names For Devices
		DevTypeList();					// Assigning Device Types For Devices

		// Loading data into custom spinner adapter
		deviceAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, Full_DevNames_List); 
		dev_type.setAdapter(deviceAdapter);						// Setting adapter to spinner
		setDeviceNameSelection();
		


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


		bt_status.setOnLongClickListener(new OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				//setBluetoothAddress();
				usbService.write(("AT" + "\r\n").getBytes());
				return false;
			}
		});


		//calling connection in activity
		//RegainingConnection();
		Tcp_con mTcp = new Tcp_con(this);

		if(Tcp_con.isClientStarted){

			 receiveddata(NetwrkType,StaticStatus.Network_Type,null);
			 receiveddata(ServStatus,StaticStatus.Server_status,null);

		}else{
			Tcp_con.stacontxt =AddNewDevice.this;
			Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
			Tcp_con.registerReceivers(this.getApplicationContext());
		}


//		usbService.write(("AT" + "\r\n").getBytes());
	}


	
	//set device name in spinner 
	void setDeviceNameSelection(){

		
		//getting inten from previous activiy if evice mismatch happened
		try{
			
			Intent intent=getIntent();
			if(intent!=null){
				
				String devType=intent.getStringExtra("CONFIG_DEVICE_TYPE");
				
				if(devType!=null){
					
					// getting index of previously configured device
					int index=Device_Type_List.indexOf(devType);				

					// getting full name of device based on  device type index 
					String deviceNname=Full_DevNames_List.get(index);	
					
					StaticVariables.printLog("Tag","Device Name :"+deviceNname);
					
					//setting device name spinner selection
					dev_type.setSelection(index);		
					
				}else{
					// Displaying Last item of list
					dev_type.setSelection(Full_DevNames_List.size()-1);		

				}
			}else{
				// Displaying Last item of list
				dev_type.setSelection(Full_DevNames_List.size()-1);		

			}
			
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
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

	// Filling Device List with Short Names Which Will be used Internally 
	private void ShortDevNameList(){
		Resources res = getResources();
		String[] shortNames = res.getStringArray(R.array.Short_DevNames);
		Short_DevNames_List = Arrays.asList(shortNames);
		String[] shortNames_ref = res.getStringArray(R.array.Short_DevNames_references);
		Short_DevNames_List_ref = Arrays.asList(shortNames_ref);
	}

	// Filling Device Type Which Will be used Internally 
	private void DevTypeList(){
		Resources res = getResources();
		String[] devType = res.getStringArray(R.array.Device_Type);
		Device_Type_List = Arrays.asList(devType);

	}



	// Error Alert showing that tcp is Off
	private void TCPErrorAlert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(AddNewDevice.this,AlertDialog.THEME_HOLO_LIGHT);
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





	// creating Bluetooth Handler to handle Bluetooth Socket Data
public class MyHandler extends Handler {
		private final WeakReference<AddNewDevice> mActivity;

		public MyHandler(AddNewDevice activity) {
			mActivity = new WeakReference<>(activity);
		}

		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{

				case UsbService.MESSAGE_FROM_SERIAL_PORT:
				{
					final String Data=(String) msg.obj;
					StaticVariables.printLog("BT RESPONSE","DATA GET IN HANDLER:"+Data);
					runOnUiThread(new Runnable() {
						public void run()
						{
							Toast.makeText(AddNewDevice.this,"Usb RESPONSE"+Data,Toast.LENGTH_SHORT).show();
						}
					});

					if(Data.contains("OK")){

						bt_status.setImageResource(BT_ON);
				     }

				/*	if(Data.contains("CMD") && CMD_COUNT==1){
						//writing cmd to check current baudrate of bt device
						//bluetooth.btWrite(BT_CHECK_BAUDRATE);
						//usbService.write(BT_CHECK_BAUDRATE.getBytes());
						//set cmd count 2
						CMD_COUNT=2;
					}else if(!Data.contains("19.2") && CMD_COUNT==2){
					*//*if baudrate is not set to standard
						communication value then setting baudrate   *//*
						//bluetooth.btWrite(BT_SET_BAUDRATE);
						//set cmd count 3
						CMD_COUNT=3;
					}else if(Data.contains("19.2") && CMD_COUNT==2){
						//if baudrate is already set then exit from cmd mode
						bluetooth.btWrite(BT_EXIT_CONFIG_MODE);
						//set cmd count 4
						CMD_COUNT=4;
					}else if(Data.contains("AOK") && CMD_COUNT==3){
						//if su cmd set baudrate  successfully then exting from cmd mode
						bluetooth.btWrite(BT_EXIT_CONFIG_MODE);
						CMD_COUNT=4;
					}else if(Data.contains("ERR") && CMD_COUNT==3){
						pdlg.setProgress(100);
						if(pdlg!=null){
							pdlg.dismiss();	// Dismissing Progress Bar
							pdlg=null;
						}
						//reset cmd count
						CMD_COUNT=0;
						// Showing Configuration Dialog if Bluetooth Connected
						ErrorAlert("ERROR","Unable to Configure Configuration Tool.");
					}else if(Data.contains("END") && CMD_COUNT==4){
						//if baudrate set successfully then showing succcess dialog
						runOnUiThread(new Runnable() {
							public void run() {
								pdlg.setProgress(100);
								if(pdlg!=null){
									pdlg.dismiss();	// Dismissing Progress Bar
									pdlg=null;
								}
								//reset cmd count
								CMD_COUNT=0;

								//bluetooth connected successful
								//ConnectionSuccessDialog();

								//switching activity as bluetooth connected
								SwitchActivity();
							}
						});
					}*/

					break;
				}

				case READ_BYTE_BT:
				{

					break;
				}

				case EXCEPTION_BT:
				{
					final String Data=(String) msg.obj;
					StaticVariables.printLog("BT RESPONSE","DATA GET IN HANDLER:"+Data);
					exceptionOccured=true;
					break;
				}

				case BT_LOST_BT:
				{
					runOnUiThread(new Runnable() {
						public void run()
						{
							//showing bluetooth is Disconnected
							bt_status.setImageResource(BT_OFF);
							if(pdlg!=null){
								pdlg.dismiss();	// Dismissing Progress Bar
								pdlg=null;
							}
						}
					});
					break;
				}
			}
		}
	};



	//Error Alert Showing Unable to Establish tcp Connection
	private void ErrorAlert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(AddNewDevice.this,AlertDialog.THEME_HOLO_LIGHT);
				dlg.setTitle(title);
				dlg.setMessage(msg);
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
						// Again starting tcp Connection Task
						startBluetooth();
					}
				});		

				AlertDialog d = dlg.create();
				d.show();
			}
		});
	}

	// switch activity
	private void SwitchActivity()
	{
		// Fetching Device's Short Name from Device Name List
		String deviceName=Short_DevNames_List.get(selectedItemPosition);

		// Fetching Device's Short Name from Device Name List
		String deviceFullName=Full_DevNames_List.get(selectedItemPosition);

		// Fetching Device Type from Device Type List
		String deviceType=Device_Type_List.get(selectedItemPosition);

		/*

		//setting group id for WPC if selected
		if(deviceName.equals(BM_DevType)||deviceName.equals(SM_DevType)||
				deviceName.equals(MUD_DevType) || deviceName.equals(MUB_DevType)||
				deviceName.equals(MUC_DevType)||deviceName.equals(MBSO_DevType)||
				deviceName.equals(SS_DevType)||	deviceName.equals(OTS_DevType))
		{
			//getting unique id  from house database
			selectedGroupId= houseDB.getWPCgroupId();
		} 		

		 */

		//Sending intent with Data
		Intent it=new Intent(AddNewDevice.this,DeviceConfiguration.class);//,DeviceConfiguration.class
		it.putExtra("roomName",RoomName);
		it.putExtra("devType", deviceType);
		it.putExtra("devName", deviceName);
		it.putExtra("devFullName", deviceFullName); 
		it.putExtra("groupId", selectedGroupId);
		it.putExtra("roomiconnam", Room_Icon_Image);
		it.putExtra("usrdevnam", User_Device_Name);
		startActivity(it);
		//adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();				

	}	



	//click events
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.submit_btn:
		{
			//if(Tcp.tcpConnected){
			if(Tcp_con.isClientStarted){

				boolean isGroupChecked=false;
				String rname=null;

				try{
					DeviceType = dev_type.getSelectedItem().toString();
					rname=room_names_list.getSelectedItem().toString();
					isGroupChecked=groupCheck.isChecked();

					StaticVariabes_div.log("DeviceType"+DeviceType,"Addnew");

				}catch(Exception e){
					e.printStackTrace();
				}

                boolean newroomselected=false;

				//checking if user has selected Add New Room option
				if(rname!=null && room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-2 ){
					RoomName=room_name.getText().toString().trim();
					newroomselected=true;
				}else{
					RoomName=rname.trim();
					newroomselected=false;
				}

				StaticVariabes_div.log("RoomName"+RoomName,"Addnew");
				User_Device_Name=et_user_dev_name.getText().toString();

				String isdevnamUnique=houseDB.isDeviceName_Exists(User_Device_Name);
				StaticVariabes_div.log("isdevnamUnique"+isdevnamUnique,TAG1);

				String isroomnamexists=houseDB.isRoomName_Exists(RoomName);
				StaticVariabes_div.log("isroomnamexists"+isroomnamexists,TAG1);

				if(isGroupChecked){
					//checking if Add New option is selected
					if(groupId.getSelectedItemPosition()==deviceGroupIdList.size()-2)
					{
						selectedGroupId=group_Id.getText().toString(); 
					}else{
						selectedGroupId=groupId.getSelectedItem().toString(); 
					}
				} 

				if ((RoomName == null || RoomName.length() == 0 || room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-1) ||
						selectedItemPosition==Full_DevNames_List.size()-1||selectedItemPosition==Full_DevNames_List.size()-1) 	{

					//checking if no room is selected
					if(room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-1){
						Toast.makeText(getApplicationContext(),"Please Select a Room", Toast.LENGTH_SHORT).show();
					}else if(RoomName==null || RoomName.length()==0 ){
						room_name.setError("Please Enter Room Name.");
					}else if((RoomName.length()>0)&& room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-2){

						room_name.setError(null);

					}


					if(selectedItemPosition==Full_DevNames_List.size()-1){
						msg("Please Select a Device");
					}



				}else if(newroomselected&&(RoomName.length()>0)&&(isroomnamexists.equals("TRUE")) ){
					room_name.setError("Room Name Already Exists");
				} else if((newroomselected)&&((Room_Icon_Image==null) ||(Room_Icon_Image.length()==0)||Room_Icon_Image.equals("select")) ){
					msg("Please Select a Icon");
				} else if(!(DeviceType.equals("Curtain Sheer"))&&(User_Device_Name==null || User_Device_Name.length()==0) ){
					//msg("Please provide a Valid DeviceName");
					et_user_dev_name.setError("Please Enter Device Name.");
				}else if(!(DeviceType.equals("Curtain Sheer"))&&(User_Device_Name!=null) &&(User_Device_Name.length()>0) && (isdevnamUnique.equals("TRUE")) ){
					et_user_dev_name.setError("Device Name Already Exists");
				}else if(isGroupChecked && (selectedGroupId.equals(DEFAULT_GROUPID)|| groupId.getSelectedItemPosition()==deviceGroupIdList.size()-1 || selectedGroupId.length()==0)){
					msg("Please provide a Valid Device Group ID");
				}else   {

					if(isGroupChecked){
						boolean isGroupIdInteger=false;
						//checking valid group id is given or not
						try{
							int groupId=Integer.parseInt(selectedGroupId);
							isGroupIdInteger=true;
						}catch(Exception e){
							e.printStackTrace();
						}

						if(isGroupIdInteger){
							/*checking wether given group id is not belonging 
							 * to some other device type i.e other than selected device type
							 */
							//String devname=Short_DevNames_List.get(selectedItemPosition);
							String devname=Short_DevNames_List_ref.get(selectedItemPosition);

							//boolean isGroupIdUnique=houseDB.isGroupIdUnique(devname, selectedGroupId);
							boolean isGroupIdUnique=houseDB.isGroupIdUnique_type(devname, selectedGroupId);
							if(!isGroupIdUnique){

								msg(devname+"Device Group ID is Already in Use by Some Other Device");

							}else{
								StaticVariabes_div.log("start1"+User_Device_Name,"Addnew");
								//starting bluetooth connection
								startBluetooth();
							}

						}else{
							msg("Group Id must be a Number");
						}

					}else{
						StaticVariabes_div.log("start2"+User_Device_Name,"Addnew");
						//starting bluetooth connection
						startBluetooth();
					}


				}
			}else{
				StaticStatus.Server_status="FALSE";
				receiveddata(ServStatus, "FALSE", null);
				Toast.makeText(getApplicationContext(), 
						"Please Ensure that Server is ON!",Toast.LENGTH_SHORT).show();
			}

			break;
		}
		case R.id.btnconstatus:{
			/*if(!Tcp.tcpConnected){
				if(!isTcpConnecting){ 
					msg("connecting To Server...");
					RegainingConnection();

				}else{
					StaticVariables.printLog("TAG","already Connecting tcp");
				}*/
			if(!Tcp_con.isClientStarted){
				Tcp_con.stacontxt=AddNewDevice.this;
				Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
				Tcp_con.registerReceivers(this.getApplicationContext());

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


	//method to connect bluetooth device
	void startBluetooth(){


		if(!UsbService.serialPortConnected){
			popup("Usb not connected 3");
		}else{
			if (StaticStatus.Network_Type.equals("TRUE3G")||StaticStatus.Network_Type.equals("TRUE")) {
				popup("Device Cannot Be Configured In Remote Connection");
			}else {
				SwitchActivity();
			}
		}
	}	


	//methods for hiding the soft input keyboard
	public void hideSoftKeyboard(Activity activity) {

		try{

			if(roomname_lyaout.isShown()|| groupCheck.isChecked()){
				InputMethodManager inputMethodManager = (InputMethodManager) activity
						.getSystemService(Activity.INPUT_METHOD_SERVICE);

				inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);

			} 

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public  void setupUI(View view) 
	{
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof AutoCompleteTextView)) 
		{

			view.setOnTouchListener(new OnTouchListener() 
			{

				public boolean onTouch(View v, MotionEvent event) 
				{
					hideSoftKeyboard(AddNewDevice.this);

					return false;
				}

			});
		}
	}
	//exit confirmation dialog
		void ExitDialog()
		{
			runOnUiThread(new Runnable() {

				@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
				@Override
				public void run() 
				{
					AlertDialog.Builder dialog=new AlertDialog.Builder(AddNewDevice.this, AlertDialog.THEME_HOLO_LIGHT);
					dialog.setTitle("Exit");
					dialog.setMessage("Do You Really Want To Exit from Configuration?");
					dialog.setIcon(android.R.drawable.ic_dialog_alert);
					dialog.setCancelable(false);

					dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
					dialog.setNeutralButton("Config Menu", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss() ;

							try{
								//closing connection 
								//bluetooth.btConnectionClose();

							}catch(Exception e){
								e.printStackTrace();
							}

							Intent it=new Intent(AddNewDevice.this,Configuration_Main.class);
							startActivity(it);
							//adding transition to activity exit
							overridePendingTransition(R.anim.slideup, R.anim.slidedown);
							finish();			

						}
					});
					dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();

							//closing tcp connection 
							Tcp.tcpConnectionClose();

							Intent it=new Intent(AddNewDevice.this,Main_Navigation_Activity.class);
							// adding transition to activity exit
							overridePendingTransition(R.anim.slideup, R.anim.slidedown);
							startActivity(it);
							finish();


						}
					});

					dialog.show();
				}
			});
		}

	//go back
	void goPrevious(){

		//deciding where to move on back press action
		String activity_name=StaticVariables.ACTIVITY_NAME;

		StaticVariables.printLog("TAG", " Activity : "+activity_name);

		if(activity_name!=null && activity_name.length()>0){
			Intent it=new Intent(AddNewDevice.this,Configuration_Main.class);
			startActivity(it);
			//adding transition to activity exit
			overridePendingTransition(R.anim.slideup, R.anim.slidedown);
			finish();
		}else{
			ExitDialog();	
		}	
	}

	@Override
	public void onBackPressed() {
		goPrevious();
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
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		setFilters();  // Start listening notifications from UsbService
		startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
		if(!HouseConfigurationAdapter.sdb.isOpen()){
			houseDB.open();
			StaticVariables.printLog("TAG","DB open ");
		}
	}	

	// selected spinner item listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,long id) {
		switch(parent.getId())
		{
		case R.id.room_name_spin:{
			//checking if selected item is "Other"
			if(position==UniqueRoomList.size()-2){
				//displaying the text field
				roomname_lyaout.setVisibility(View.VISIBLE);
				room_name.setError(null);
				StaticVariables.printLog("TAG","layout  visible +"+room_names_list.getSelectedItem().toString());
			}else{
				//making text filed disappear
				roomname_lyaout.setVisibility(View.GONE);
				StaticVariables.printLog("TAG","layout invisible"+room_names_list.getSelectedItem().toString());
			}
			//refresh group id spinner
			refreshGroupIdSpinner();
			break;
		}
		case R.id.devtype:{
			selectedItemPosition=position;
			String deviceName=null;

			if(selectedItemPosition!=Full_DevNames_List.size()-1){
				// Fetching Device's Short Name from Device Name List
				deviceName=Short_DevNames_List.get(selectedItemPosition);


				//enable/disable group check box
				switch(isGroupIdApplicable.valueOf(deviceName)){

					case DMR1:case RGB1:case GSK1:case CLS1:case CRS1:case CLNR:{

					groupId.setVisibility(View.INVISIBLE);
					group_Id.setVisibility(View.INVISIBLE);
					groupCheck.setEnabled(true);

					//making groupcheckbox visible
					groupIdLayout.setVisibility(View.VISIBLE);

					//refresh group id spinner
					refreshGroupIdSpinner();

						devnameLayout.setVisibility(View.VISIBLE);

					break;
				}
					case CLSH:{


						//making groupcheckbox visible
						devnameLayout.setVisibility(View.INVISIBLE);

						break;
					}
				default : {

					//making check box unchecked & disabled
					disableCheckBox();

					devnameLayout.setVisibility(View.VISIBLE);

					break;
				}
				}
			}



			break;
		}
		case R.id.groupID:{
			if(groupId.getSelectedItemPosition()==deviceGroupIdList.size()-2){
				group_Id.setVisibility(View.VISIBLE);	
			}else{ 
				group_Id.setText("");
				group_Id.setVisibility(View.INVISIBLE);
			}
			break;
		}

		}

	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}	

	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		String rname=null; 
		rname =room_names_list.getSelectedItem().toString();  

		//checking if user has selected other
		if(rname!=null && room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-2){
			rname=room_name.getText().toString();
		} 

		if((rname!=null && rname.trim().length()>0 && room_names_list.getSelectedItemPosition()!=UniqueRoomList.size()-1) && 
				selectedItemPosition!=Full_DevNames_List.size()-1){
			if(isChecked){
				groupId.setVisibility(View.VISIBLE);
				group_Id.setVisibility(View.INVISIBLE);

				//filling group id spinner
				fillGroupIdSpinner(rname);	

			}else{
				groupId.setVisibility(View.INVISIBLE);
				group_Id.setVisibility(View.INVISIBLE); 
			}
		}else{


			//making check box unchecked
			groupCheck.setChecked(false); 

			groupId.setVisibility(View.INVISIBLE);
			group_Id.setVisibility(View.INVISIBLE);

			if(rname==null ||rname.trim().length()==0 || room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-1 || 
					selectedItemPosition!=Full_DevNames_List.size()-1){
				Toast.makeText(getApplicationContext(),
						"Please Provide Room Name And Device Type.", Toast.LENGTH_SHORT).show();

			} 
		}
	}



	//disable checkbox
	void disableCheckBox(){

		//making groupid value clear
		group_Id.setText("");

		//making check box unchecked & disabled
		groupCheck.setChecked(false);
		groupCheck.setEnabled(false);

		//making groupcheckbox layout invisible
		groupIdLayout.setVisibility(View.INVISIBLE);

	}

	//method to fill group id's of selected room
	void fillGroupIdSpinner(String RoomName){
		try{
			// Fetching Device Type from Device Type List
			String deviceType=Device_Type_List.get(selectedItemPosition);

			//clearing group id list
			deviceGroupIdList.clear();

			deviceGroupIdList=houseDB.groupIdList(RoomName, deviceType);

			//adding last items in list for choosing other option
			deviceGroupIdList.add("Add New");
			deviceGroupIdList.add("Select");

			groupIdAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, deviceGroupIdList);
			// Setting adapter to spinner
			groupId.setAdapter(groupIdAdapter);
			StaticVariables.printLog("Tag","fill group list size :"+deviceGroupIdList.size());

			if(deviceGroupIdList.size()>2){
				//setting select as default item in spinner
				groupId.setSelection(deviceGroupIdList.size()-1); 

			}else{
				/*setting other as selected item in spinner 
				as no group id found related to selected device */
				groupId.setSelection(deviceGroupIdList.size()-2);

			}

		}catch(Exception e){
			e.printStackTrace();
		}

	}

	//refreshing group id spinner
	void refreshGroupIdSpinner(){
		if(groupCheck.isChecked()){
			String rname=null; 
			rname =room_names_list.getSelectedItem().toString();  

			//checking if user has selected other
			if(rname!=null && room_names_list.getSelectedItemPosition()==UniqueRoomList.size()-2){ 
				rname=room_name.getText().toString();
			} 

			if((rname!=null && rname.trim().length()>0) && selectedItemPosition!=Full_DevNames_List.size()-1){

				groupId.setVisibility(View.VISIBLE);
				group_Id.setVisibility(View.INVISIBLE);

				//filling group id spinner
				fillGroupIdSpinner(rname);	

			}else{

				//making check box unchecked
				groupCheck.setChecked(false); 

				groupId.setVisibility(View.INVISIBLE);
				group_Id.setVisibility(View.INVISIBLE);

			} 
		}
	}

	// Address Dialog
	void setBluetoothAddress(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				AlertDialog.Builder dialog = new AlertDialog.Builder(AddNewDevice.this,AlertDialog.THEME_HOLO_LIGHT);
				dialog.setCancelable(false);
				LayoutInflater inflater = getLayoutInflater();
				final View v=inflater.inflate(R.layout.bluetooth_dialog, null);
				dialog.setTitle("Set Configuration Tool MacId");
				dialog.setView(v);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				final EditText btAddress = (EditText)v.findViewById(R.id.btMacId);

				if(Bluetooth_ID!=null){
					btAddress.setText(Bluetooth_ID);
				}else{
					btAddress.setText("00:06:66:64:B2:EF");
				}

				dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});	

				dialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {

						dialog.dismiss(); 

						String btMacId=null;
						btMacId=btAddress.getText().toString(); 

						if(btMacId!=null && btMacId.length()==17){

							StaticVariables.Bluetooth_MacID=btMacId; 
							try{ 
								houseDB.updateConfigTable_MacID(btMacId);
								Bluetooth_ID=btMacId;

								msg("Configuration Tool MacId Updated");

							}catch(Exception e){
								e.printStackTrace();
							}

						}else{
							setBluetoothAddress();
							btAddress.setError("Invalid Configuration Tool address!");
						}


					}
				});	
				AlertDialog d = dialog.create();
				d.getWindow().setSoftInputMode(
						WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
				d.show();
			}
		});
	}
 
	//device types
	private enum isGroupIdApplicable{
		S010,S020,S030,S040,S060,S080,S120,S141,S110,S160,S021,S031,S042,S051,S052,S061,
		S062,S071,S102,S111,SLT1,SFN1,DFN1,DMR1,RGB1,ACR1,WOTS,WMD1,WMC1,WMB1,WBSO,WSS1,WBM1,
		WSM1,GSR1,DLS1,CLS1,CLD1,CRS1,CRD1,IRB1,RPR1,CLB1,CSW1,EGM1,PSC1,SOSH,SWG1,SLG1,PLC1,GSK1,FMD1,AQU1,SDG1,SWD1,CLNR,CLSH
	};



	public class MyAdapter extends ArrayAdapter {

		public MyAdapter(Context context, int textViewResourceId,
						 Integer[] objects) {
			super(context, textViewResourceId, objects);
		}

		public View getCustomView(int position, View convertView,
								  ViewGroup parent) {

			// Inflating the layout for the custom Spinner
			LayoutInflater inflater = getLayoutInflater();
			View layout = inflater.inflate(R.layout.custom, parent, false);

			// Declaring and Typecasting the textview in the inflated layout
			TextView tvLanguage = (TextView) layout.findViewById(R.id.tvLanguage);

			// Setting the text using the array
			//tvLanguage.setText("Select images");
			//tvLanguage.setTextColor(Color.rgb(75, 180, 225));

			// Declaring and Typecasting the imageView in the inflated layout
			ImageView img = (ImageView) layout.findViewById(R.id.imgLanguage);

			// Setting an image using the id's in the array
			img.setImageResource(images[position]);

			// Setting Special atrributes for 1st element
			if (position == 0) {
				tvLanguage.setText("Select");
				// Removing the image view
				img.setVisibility(View.GONE);
				// Setting the size of the text
				tvLanguage.setTextSize(20f);
				// Setting the text Color
				tvLanguage.setTextColor(Color.BLACK);

			}
			else{
				tvLanguage.setVisibility(View.GONE);


			}

			return layout;
		}

		// It gets a View that displays in the drop down popup the data at the specified position
		@Override
		public View getDropDownView(int position, View convertView,
									ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}

		// It gets a View that displays the data at the specified position
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			return getCustomView(position, convertView, parent);
		}
	}

	@Override
	public void read(int type, String stringData, byte[] byteData) {

		receiveddata(type,stringData,byteData);

	}

	public void receiveddata(int msg,String data,byte[] bytestatus){


		switch(msg)
		{
			case TCP_LOST:
			{
				/*runOnUiThread(new Runnable() {
					public void run()
					{
						btnconstatus.setBackgroundResource(server_offline);
					}
				});*/
				break;
			}
			case ERRUSER:
			{
				runOnUiThread(new Runnable() {
					public void run()
					{
						IS_SERVER_NOT_AUNTHETICATED=true;
						btnconstatus.setBackgroundResource(server_offline);
						TCPErrorAlert("Aunthetication Failed","Invalid UserName/Password.Please check and try again.");
					}
				});
				break;
			}
			case MAXUSER:
			{
				runOnUiThread(new Runnable() {
					public void run()
					{
						IS_SERVER_NOT_AUNTHETICATED=true;
						btnconstatus.setBackgroundResource(server_offline);
						TCPErrorAlert("Server Error","Server connections Limit Exceeded .Please disconnect some other client to connect with server.");

					}
				});
				break;
			}
			case ServStatus:
				//final String ServerStatusB = (String) msg.obj;
				final String ServerStatusB =data;
				StaticStatus.Server_status=data;
				StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
				if(ServerStatusB!=null){
					if (ServerStatusB.equals("TRUE")) {
						StaticStatus.Server_status_bool=true;
						statusserv = true;
						servpreviousstate="TRUE";
						nonetwork=false;
						// Cc.dataswb = true;
						//ButtonOut("920");
						// Cc.TcpReadLine = false;

					}else {
						StaticStatus.Server_status_bool=false;
						statusserv = false;
						servpreviousstate="FALSE";
					}
				}else{
					StaticStatus.Server_status_bool=false;
					statusserv = false;
					servpreviousstate="FALSE";
				}
				runOnUiThread(new Runnable() {
				    public void run() {
				serv_status(statusserv);
				  }
				 });
				break;
			case signallevel:
				//    final String signallevelB = (String) msg.obj;
				final String signallevelB = data;
				//  StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
				if(signallevelB!=null){
					sl = Integer.parseInt(signallevelB);
					rs=signallevelB;

					if((StaticStatus.Network_Type.equals("TRUE")||(StaticStatus.Network_Type.equals("TRUE3G")))){

						network_signal(sl,true);

						if(StaticStatus.Network_Type.equals("TRUE3G")||StaticStatus.Network_Type.equals("NONET")){
							if(timer!=null){
								timer.cancel();
								timer=null;
							}
						}

					}else{

						network_signal(sl,false);
					}

				}
				break;
			case NetwrkType:
				//final String RemoteB = (String) msg.obj;
				final String RemoteB = data;
				StaticStatus.Network_Type=RemoteB;
				StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
				if (RemoteB.equals("TRUE")) {
					nonetwork=false;
					remoteconn = true;
					remoteconn3g = false;
					remoteconprevstate="TRUE";

					network_signal(sl,remoteconn);

					if(timer==null){
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask()
						{
							public void run()
							{
								Tcp_con.rssirec();  // display the data

							}
						}, delay, period);
					}
				}else if(RemoteB.equals("TRUE3G")){
					nonetwork=false;
					remoteconn = true;
					remoteconn3g = true;
					remoteconprevstate="TRUE3G";
					nonetwork=false;
					if(timer!=null){
						timer.cancel();
						timer=null;
					}

					network_signal(sl,remoteconn);

				} else if (RemoteB.equals("NONET"))
				{
					statusserv = false;
					servpreviousstate="FALSE";
					nonetwork=true;
					if(timer!=null){
						timer.cancel();
						timer=null;
					}
					remoteconn = false;
					remoteconn3g = false;

					network_signal(sl,remoteconn);

				}else {
					nonetwork=false;
					remoteconn = false;
					remoteconn3g = false;
					remoteconprevstate="FALSE";
					if(timer==null){
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask()
						{
							public void run()
							{
								Tcp_con.rssirec();  // display the data

							}
						}, delay, period);
					}
					network_signal(sl,remoteconn);

				}

				break;
			/*case signallevel:
				//    final String signallevelB = (String) msg.obj;
				final String signallevelB = data;
				//StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
				if(signallevelB!=null){
					sl = Integer.parseInt(signallevelB);
					rs=signallevelB;
					if(!remoteconn3g){
						if(!nonetwork){
							//runOnUiThread(new Runnable() {
							//    public void run() {
							network_signal(sl, StaticStatus.Server_status_bool);
							//  }
							// });
						}
					}else{
						remoteconn = true;
						remoteconn3g = true;
						remoteconprevstate="TRUE3G";
						nonetwork=false;
						if(timer!=null){
							timer.cancel();
							timer=null;
						}
						// runOnUiThread(new Runnable() {
						//   public void run() {
						network_signal(sl,remoteconn3g);
						//  }
						//});
					}
				}
				break;
			case NetwrkType:
				//final String RemoteB = (String) msg.obj;
				final String RemoteB = data;
				StaticStatus.Network_Type=RemoteB;
				StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
				if (RemoteB.equals("TRUE")) {
					nonetwork=false;
					remoteconn = true;
					remoteconn3g = false;
					remoteconprevstate="TRUE";
					//  runOnUiThread(new Runnable() {
					//      public void run() {
					// serv_3g(remoteconn);
					network_signal(sl,remoteconn3g);
					//      }
					//  });
					if(timer==null){
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask()
						{
							public void run()
							{
								Tcp_con.rssirec();  // display the data

							}
						}, delay, period);
					}
				}else if(RemoteB.equals("TRUE3G")){
					nonetwork=false;
					remoteconn = true;
					remoteconn3g = true;
					remoteconprevstate="TRUE3G";
					nonetwork=false;
					if(timer!=null){
						timer.cancel();
						timer=null;
					}
					//   runOnUiThread(new Runnable() {
					//       public void run() {
					// serv_3g(remoteconn3g);
					network_signal(sl,remoteconn3g);
					//      }
					//  });

				} else if (RemoteB.equals("NONET"))
				{
					statusserv = false;
					servpreviousstate="FALSE";
					nonetwork=true;
					if(timer!=null){
						timer.cancel();
						timer=null;
					}
					remoteconn = false;
					remoteconn3g = false;
					//  runOnUiThread(new Runnable() {
					//      public void run() {
					//  serv_3g(remoteconn);
					network_signal(sl,remoteconn3g);
					//     }
					//  });

				}else {
					nonetwork=false;
					remoteconn = false;
					remoteconn3g = false;
					remoteconprevstate="FALSE";
					if(timer==null){
						timer = new Timer();
						timer.scheduleAtFixedRate(new TimerTask()
						{
							public void run()
							{
								Tcp_con.rssirec();  // display the data

							}
						}, delay, period);
					}
					// runOnUiThread(new Runnable() {
					//     public void run() {
					// serv_3g(remoteconn);
					network_signal(sl,remoteconn3g);
					//     }
					//  });
				}*/

				//break;
		}
	}






	public void network_signal(final int signal1, final boolean serv) {

		runOnUiThread(new Runnable() {
			public void run() {

		if (serv) {
			//  btnwtype.setText("Remote");


			if (btsig!=null) {
				if (signal1 <= 1)
					btsig.setBackgroundResource(R.drawable.remote_sig_1);
				else if (signal1 <= 2)
					btsig.setBackgroundResource(R.drawable.remote_sig_2);
				else if (signal1 <= 3)
					btsig.setBackgroundResource(R.drawable.remote_sig_3);
				else if (signal1 <= 4)
					btsig.setBackgroundResource(R.drawable.remote_sig_4);
			}




			if (StaticStatus.Network_Type.equals("TRUE3G")) {
				btsig.setBackgroundResource(R.drawable.mobiledata);
			}

		} else {
			// btnwtype.setText("local");

			if (btsig!=null) {
				if (signal1 <= 1)
					btsig.setBackgroundResource(R.drawable.local_sig_1);
				else if (signal1 <= 2)
					btsig.setBackgroundResource(R.drawable.local_sig_2);
				else if (signal1 <= 3)
					btsig.setBackgroundResource(R.drawable.local_sig_3);
				else if (signal1 <= 4)
					btsig.setBackgroundResource(R.drawable.local_sig_4);
			}
		}

		if(StaticStatus.Network_Type.equals("NONET")){
			btsig.setBackgroundResource(R.drawable.no_network);
			// btnwtype.setText("no-net");
			btnconstatus.setBackgroundResource(R.drawable.not_connected);
		}

			}
		});
/*		if (serv) {
			//  btnwtype.setText("Remote");

			if (btsig!=null) {
				if (signal1 <= 1)
					btsig.setBackgroundResource(R.drawable.local_sig_1);
				else if (signal1 <= 2)
					btsig.setBackgroundResource(R.drawable.local_sig_2);
				else if (signal1 <= 3)
					btsig.setBackgroundResource(R.drawable.local_sig_3);
				else if (signal1 <= 4)
					btsig.setBackgroundResource(R.drawable.local_sig_4);
			}

			if (StaticStatus.Network_Type.equals("TRUE3G")) {
				btsig.setBackgroundResource(R.drawable.mobiledata);
			}

		} else {
			// btnwtype.setText("local");

			if (btsig!=null) {
				if (signal1 <= 1)
					btsig.setBackgroundResource(R.drawable.remote_sig_1);
				else if (signal1 <= 2)
					btsig.setBackgroundResource(R.drawable.remote_sig_2);
				else if (signal1 <= 3)
					btsig.setBackgroundResource(R.drawable.remote_sig_3);
				else if (signal1 <= 4)
					btsig.setBackgroundResource(R.drawable.remote_sig_4);
			}
		}

		if(StaticStatus.Network_Type.equals("NONET")){
			btsig.setBackgroundResource(R.drawable.no_network);
			// btnwtype.setText("no-net");
			btnconstatus.setBackgroundResource(R.drawable.not_connected);
		}*/

	}

	public void serv_status(final boolean serv)
	{
		runOnUiThread(new Runnable() {
			public void run() {
				if(serv){
					btnconstatus.setBackgroundResource(R.drawable.connected);
				}
				else
					btnconstatus.setBackgroundResource(R.drawable.not_connected);
			}
		});
	}



	////////////////////////////////////////

/*	@Override
	public void onResume() {
		super.onResume();
		setFilters();  // Start listening notifications from UsbService
		startService(UsbService.class, usbConnection, null); // Start UsbService(if it was not started before) and Bind it
	}

	@Override
	public void onPause() {
		super.onPause();
		unregisterReceiver(mUsbReceiver);
		unbindService(usbConnection);
	}*/

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
		}else{
			//bt_status.setImageResource(BT_ON);
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
						/*Intent it=new Intent(AddNewDevice.this,Configuration_Main.class);
						startActivity(it);
						//adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();*/
						//  onShift();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}




}
