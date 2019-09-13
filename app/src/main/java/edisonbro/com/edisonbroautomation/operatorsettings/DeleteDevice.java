package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: DeleteDevice.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to delete device.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;


public class DeleteDevice extends Activity implements OnItemSelectedListener,TcpTransfer {

	Button delete_btn; 
	Spinner dev_type_spinner,room_spinner;
	HouseConfigurationAdapter houseDB=null;
	WirelessConfigurationAdapter WhouseDB=null;
	SpinnerAdapter deviceAdapter,roomNameAdapter;

	ArrayList<String> UniqueRoomList=new ArrayList<String>();
	List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;

	//lists for device types
	ArrayList<String>  finalDevName_List=new ArrayList<String>(); 
	ArrayList<Integer> finalDevNo_List =new ArrayList<Integer>();

	int CurrentRoomNo=0 ,DevType_Position=0;
	int spinnerLayoutId= R.layout.spinnerlayout;

	ImageView navigateBack;
	LocalListArrangeTable locallist_adap;
	private ServerDetailsAdapter sdadap;

	private static final int READ_BYTE = 1;
	private static final int READ_LINE = 2;
	private static final int ServStatus = 3;
	private static final int signallevel = 4;
	private static final int NetwrkType = 5;
	private static final int MAXUSER = 6;
	private static final int ERRUSER = 7;

	//**********************************************
	int sl;
	boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
	String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
	//************************************************************************
	//********************************************************************************
	int delay = 0; // delay for 1 sec.
	int period = 1000;
	Timer timer = null;
	//********************************************************************
	Button btnconstatus;
	int devic_todelete;

	private static final String TAG1="Delete Device - ";
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.delete_home_device);
		btnconstatus=(Button) findViewById(R.id.btnconstatus);
		delete_btn=(Button) findViewById(R.id.delete_btn);
		dev_type_spinner = (Spinner) findViewById(R.id.devtype_spin); 
		room_spinner = (Spinner) findViewById(R.id.room_name_spin);

		navigateBack=(ImageView) findViewById(R.id.imageView2);


		// setting item selected listener for spinner
		dev_type_spinner.setOnItemSelectedListener(this);	 
		room_spinner.setOnItemSelectedListener(this);	


		try{
			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();			//opening house database

			
			//initialize wireless db
			WhouseDB=new WirelessConfigurationAdapter(DeleteDevice.this);
			
		}catch(Exception e){
			e.printStackTrace();
		} 


		//filling room name list
		fillRoomNameList();

		//fetching device full names
		FullDevNameList();
		//fetching device types
		DevTypeList();
		//fetching device short names
		ShortDevNameList();

		Tcp_con mTcp = new Tcp_con(DeleteDevice.this);


		if (Tcp_con.isClientStarted) {

			receiveddata(NetwrkType, StaticStatus.Network_Type, null);
			receiveddata(ServStatus, StaticStatus.Server_status, null);

		} else {
			Tcp_con.stacontxt = DeleteDevice.this;
			Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
			Tcp_con.registerReceivers(DeleteDevice.this);
		}
		navigateBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goPrevious();
			}
		});
		

		//setting on click listener on delete button
		delete_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				String roomname=null,devtype=null;
				try{
					roomname=room_spinner.getSelectedItem().toString();
					devtype=dev_type_spinner.getSelectedItem().toString();

				}catch(Exception e){
					e.printStackTrace();
				}
				if((roomname!=null && !roomname.equals("Select Room")) &&
						(devtype!=null && !devtype.equals("Select Device")) ) {

					popupdelete();
				}else{
					Alert("Invalid Data","Please select Room Name/Device Type First !");
				}
		/*		String roomname=null,devtype=null;
				try{
					roomname=room_spinner.getSelectedItem().toString();
					devtype=dev_type_spinner.getSelectedItem().toString();

				}catch(Exception e){
					e.printStackTrace();
				}
				if((roomname!=null && !roomname.equals("Select Room")) &&
						(devtype!=null && !devtype.equals("Select Device")) ) 		
				{
					int deviceNo=finalDevNo_List.get(DevType_Position);
					boolean isdeleted=houseDB.deleteDevice(deviceNo);
					if(isdeleted){

						try{
							if(WirelessConfigurationAdapter.sdb!=null){
								if(!WirelessConfigurationAdapter.sdb.isOpen()){
									WhouseDB.open();

									StaticVariables.printLog("TAG","DB open ");
								}
							}
							//delete data from wireless table regarding the deleted wired device
							boolean isWLS_Deleted=WhouseDB.DeleteWiredDeviceDetails(deviceNo);

							if(isWLS_Deleted){
								StaticVariables.printLog("TAG","wireless details deleted");
							}

						}catch(Exception e){
							e.printStackTrace();
						}


						Alert("Deleted","Device Deleted Successfully!");

						//checking number of devices  in room
						HashMap<String,String> devMap = null;

						try{
							//getting hashmap of devno-devname key pairs
							devMap=houseDB.getRoomDevices(CurrentRoomNo);
							if(devMap.size()==0){
								fillRoomNameList();
							}
						}catch(Exception e){
							e.printStackTrace();
						}

						//filling device spinner 
						FillDeviceList();

					}else{
						Alert("Failed","Unable to Delete Device!");
					}
				}else{
					Alert("Invalid Data","Please select Room Name/Device Type First !");
				}*/

			}
		});

	}

	public void read(final int type, final String stringData, final byte[] byteData)
	{
		runOnUiThread(new Runnable() {
			@Override
			public void run() {

				receiveddata(type,stringData,byteData);

			}
		});
	}


	public void ReadlineData( final String RL) {
		StaticVariabes_div.log("RL:- " + RL, TAG1);


		if(RL.contains("&011S@"))
		{
			runOnUiThread(new Runnable() {
				public void run() {
					Toast.makeText(DeleteDevice.this, "Device Deleted From Gateway Successfully", Toast.LENGTH_SHORT).show();
					DeleteDeviceLocal(devic_todelete);
					devic_todelete=0;
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
				AlertDialog.Builder alert = new AlertDialog.Builder(DeleteDevice.this);
				alert.setTitle("Info");
				//alert.setMessage("Enter Password");
				final TextView applnnameinfo = new TextView(DeleteDevice.this);
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
	public void receiveddata(int msg,String strdata,byte[] bytestatus){

		switch (msg) {
			case READ_BYTE:
				byte[] readBuf = bytestatus;
				// DataIn(readBuf);
				final String readMessage = new String(readBuf, 0,readBuf.length);
				StaticVariabes_div.log("msg read bytes:- " + readMessage + " msg", TAG1);
				//  tvtest.setText(readMessage);
				// DataIn(readBuf);

				break;
			case READ_LINE:
				//  readMessage2 = (String) msg.obj;
				StaticVariabes_div.log("msg read string" + strdata, TAG1);
				readMessage2 =strdata;
				if(readMessage2.equals("*OK#")){
					serv_status(true);
					//ButtonOut("920");

				}
				ReadlineData(readMessage2);
				//  tvtest.setText(readMessage2);
				break;
			case ServStatus:
				//final String ServerStatusB = (String) msg.obj;
				final String ServerStatusB =strdata;
				StaticStatus.Server_status=strdata;

				StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
				if(ServerStatusB!=null){
					if (ServerStatusB.equals("TRUE")) {
						StaticStatus.Server_status_bool=true;
						statusserv = true;
						servpreviousstate="TRUE";
						nonetwork=false;
						// Cc.dataswb = true;
						//  ButtonOut("920");
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

				serv_status(statusserv);

				break;
			case signallevel:
				//    final String signallevelB = (String) msg.obj;
				final String signallevelB = strdata;
				// StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);

				// final String signallevelB = data;
				//  StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);


				/*if(signallevelB!=null) {
					sl = Integer.parseInt(signallevelB);
					rs = signallevelB;


					if ((StaticStatus.Network_Type.equals("TRUE") || (StaticStatus.Network_Type.equals("TRUE3G")))) {

						network_signal(sl, true);

						if (StaticStatus.Network_Type.equals("TRUE3G") || StaticStatus.Network_Type.equals("NONET")) {
							if (timer != null) {
								timer.cancel();
								timer = null;
							}
						}

					} else {

						network_signal(sl, false);
					}
				}
*/

				break;
			case NetwrkType:

				final String RemoteB = strdata;
				StaticStatus.Network_Type=RemoteB;
				StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
				/*if (RemoteB.equals("TRUE")) {
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

*/
				break;
			case MAXUSER:
				final String maxuser = strdata;
				StaticVariabes_div.log("maxuser swb" + maxuser, TAG1);

				if (maxuser.equals("TRUE")) {
					popup("User Exceeded");
					serv_status(false);
				} else {

				}

				break;
			case  ERRUSER:
				final String erruser = strdata;
				StaticVariabes_div.log("erruser swb" + erruser, TAG1);

				if (erruser.equals("TRUE")) {
					popup("INVALID USER/PASSWORD");
					serv_status(false);
				} else {

				}

				break;
		}
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


	//filling room name list
	void fillRoomNameList(){

		UniqueRoomList.clear();

		//Fetching list of All room names from database and adding to local array list
		UniqueRoomList.addAll(houseDB.RoomNameList()); 
		UniqueRoomList.add("Select Room");


		//making adapter
		roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList); 
		room_spinner.setAdapter(roomNameAdapter);
		room_spinner.setSelection(UniqueRoomList.size()-1);

	}

	// alert to show whether device deleted or not
	private void Alert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(DeleteDevice.this,AlertDialog.THEME_HOLO_LIGHT);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setCancelable(false);
				dlg.setIcon(android.R.drawable.ic_dialog_info);

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


	// Filling Device List with Short Names Which Will be used Internally 
	private void ShortDevNameList(){
		Resources res = getResources();
		String[] shortNames = res.getStringArray(R.array.Short_DevNames);
		Short_DevNames_List = Arrays.asList(shortNames);
	}	




	//spinner on item click listener
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		switch(parent.getId()){
		case R.id.room_name_spin:{
			if(position!=UniqueRoomList.size()-1){

				//getting current room name
				String roomName=room_spinner.getSelectedItem().toString();
				//getting current room number
				CurrentRoomNo=houseDB.CurrentRoomNumber(roomName);
				//filling device spinner 
				FillDeviceList();

			}
			break;
		}
		case R.id.devtype_spin:{
			if(position!=finalDevName_List.size()-1){
				DevType_Position=dev_type_spinner.getSelectedItemPosition();
				StaticVariables.printLog("TAG","Selected Device Position "+DevType_Position);
			}
			break;
		}
		default:
			break;
		}
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent) {

	}

	//filling device list  with all details
	private void FillDeviceList(){

		//clearing all lists
		finalDevName_List.clear(); 
		finalDevNo_List.clear(); 

		HashMap<String,String> devMap = null;

		try{
			//getting hashmap of devno-devname key pairs
			//devMap=houseDB.getRoomDevices(CurrentRoomNo);
			devMap=houseDB.getRoomDevices_usrdevnames(CurrentRoomNo);

		}catch(Exception e){
			e.printStackTrace();
		}


		try{

			try{

				List<Integer> keys=new ArrayList<Integer>();
				for(Map.Entry m:devMap.entrySet()){
					//adding elements to list
					keys.add(Integer.parseInt((String)m.getKey()));
				}  

				List<String> sortedKeys=new ArrayList<String>();
				//sorting keys in list
				Collections.sort(keys);

				for(int j=0;j<keys.size();j++){
					//adding sorted keys
					sortedKeys.add(""+keys.get(j));
					Log.d("TAG","dno key :"+""+keys.get(j));
				}


				String key=null,value=null;
				int dno=0;
				for(int i=0;i<sortedKeys.size();i++){
					//getting key values
					key=(String) sortedKeys.get(i);
					//getting value of current key
					value=devMap.get(key);
					//Log.d("TAG","dno :"+key+" devName :"+value);


					//parsing string to integer
					dno=Integer.parseInt(key);

					//StaticVariables.printLog("TAG","dno :"+key+" devName :"+value);
					Log.d("TAG","dno :"+key+" devName :"+value);

					//int index=Short_DevNames_List.indexOf(value);
					//getting full name of devtype
					//String DevName=Full_DevNames_List.get(index);

					//filling devno and devname list
					finalDevNo_List.add(dno);
					finalDevName_List.add(value+"--"+dno);
					//finalDevName_List.add(DevName+"--"+dno);

				}

			}catch(Exception e){
				e.printStackTrace();
			}


			//adding last element in list
			finalDevName_List.add("Select Device");
			// Loading data into custom spinner adapter
			deviceAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, finalDevName_List); 
			// Setting adapter to spinner
			dev_type_spinner.setAdapter(deviceAdapter);			
			// Displaying Last item of list
			dev_type_spinner.setSelection(finalDevName_List.size()-1);		

		}catch(Exception e){
			e.printStackTrace();
		}
	}

	//go back
	void goPrevious(){
		//going back to admin page
		Intent it=new Intent(DeleteDevice.this,Configuration_Main.class);
		startActivity(it);
		//adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();
	}

	//backpress event
	@Override
	public void onBackPressed() {
		goPrevious();
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
		super.onResume();
		if(WirelessConfigurationAdapter.sdb!=null){
			if(!HouseConfigurationAdapter.sdb.isOpen() && 
					!WirelessConfigurationAdapter.sdb.isOpen()){
				houseDB.open();
				WhouseDB.open();

				StaticVariables.printLog("TAG","DB open ");
			}
		}

	}


	public void popupdelete(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("INFO");
		alertDialogBuilder.setMessage("Are You Sure You Want To Delete Device").setCancelable(false)

				.setPositiveButton("yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {


						String roomname=null,devtype=null;
						try{
							roomname=room_spinner.getSelectedItem().toString();
							devtype=dev_type_spinner.getSelectedItem().toString();

						}catch(Exception e){
							e.printStackTrace();
						}
						if((roomname!=null && !roomname.equals("Select Room")) &&
								(devtype!=null && !devtype.equals("Select Device")) )
						{
							int deviceNo=finalDevNo_List.get(DevType_Position);
							devic_todelete=deviceNo;
							Send_dat_aes(""+deviceNo, "<", "*");

						}else{
							Alert("Invalid Data","Please select Room Name/Device Type First !");
						}

					}
				});
				alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int whichButton) {
						dialog.cancel();
					}
				});
		AlertDialog alertDialog = alertDialogBuilder.create();
		alertDialog.show();
	}

	public void Send_dat_aes(String StrTimer,String starttokn,String endtokn){

		String tosend=null;
		tosend=starttokn+StrTimer+endtokn;

		tosend.replaceAll(" ","");
		String temp_str=tosend.replaceAll("\n","");

		StaticVariabes_div.log(temp_str.length()+" Data "+temp_str,TAG1);
		Tcp_con.WriteBytes(temp_str.getBytes());
	}
public  void DeleteDeviceLocal(int deviceNo){

	boolean isdeleted=houseDB.deleteDevice(deviceNo);
	if(isdeleted){

		try{
			if(WirelessConfigurationAdapter.sdb!=null){
				if(!WirelessConfigurationAdapter.sdb.isOpen()){
					WhouseDB.open();

					StaticVariables.printLog("TAG","DB open ");
				}
			}
			//delete data from wireless table regarding the deleted wired device
			boolean isWLS_Deleted=WhouseDB.DeleteWiredDeviceDetails(deviceNo);

			if(isWLS_Deleted){
				StaticVariables.printLog("TAG","wireless details deleted");
			}

		}catch(Exception e){
			e.printStackTrace();
		}


		Alert("Deleted","Device Deleted Successfully!");
		Update_db_version_number();

		try {
			LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
			locallist_adap = new LocalListArrangeTable(DeleteDevice.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
			locallist_adap.open();
			locallist_adap.deleteall();
			locallist_adap.close();
		}catch(Exception e){
			e.printStackTrace();

		}

		//checking number of devices  in room
		HashMap<String,String> devMap = null;

		try{
			//getting hashmap of devno-devname key pairs
			devMap=houseDB.getRoomDevices(CurrentRoomNo);
			if(devMap.size()==0){
				fillRoomNameList();
			}
		}catch(Exception e){
			e.printStackTrace();
		}

		//filling device spinner
		FillDeviceList();

	}else{
		Alert("Failed","Unable to Delete Device!");
	}
}
	public void Update_db_version_number(){
		if(StaticVariabes_div.housename!=null) {
			try {
				ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
				sdadap = new ServerDetailsAdapter(DeleteDevice.this);
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
