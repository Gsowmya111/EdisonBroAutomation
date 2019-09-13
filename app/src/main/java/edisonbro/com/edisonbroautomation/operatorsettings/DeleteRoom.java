package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: DeleteRoom.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to delete room.
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
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class DeleteRoom extends Activity implements OnItemSelectedListener,TcpTransfer {

	Button delete_btn; 
	Spinner dev_type_spinner,room_spinner;
	HouseConfigurationAdapter houseDB=null;
	WirelessConfigurationAdapter WhouseDB=null;
	SpinnerAdapter deviceAdapter,roomNameAdapter;
	UpdateAdapter up_adap;
	ArrayList<String> UniqueRoomList=new ArrayList<String>();
	List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;

	//lists for device types
	ArrayList<String>  finalDevName_List=new ArrayList<String>(); 
	ArrayList<Integer> finalDevNo_List =new ArrayList<Integer>();

	int CurrentRoomNo=0 ,DevType_Position=0;
	int spinnerLayoutId=R.layout.spinnerlayout;

	ImageView navigateBack;
    String housename,houseno,Room_selected;
	LocalListArrangeTable locallist_adap;
	private ServerDetailsAdapter sdadap;
	private SwbAdapter swbadap;
	private MasterAdapter mas_adap;

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

	private static final String TAG1="Delete Room - ";

	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.delete_room);
		housename=getIntent().getStringExtra("housename");
		houseno=getIntent().getStringExtra("houseno");
		btnconstatus=(Button) findViewById(R.id.btnconstatus);
		
		delete_btn=(Button) findViewById(R.id.delete_btn);
		dev_type_spinner = (Spinner) findViewById(R.id.devtype_spin); 
		room_spinner = (Spinner) findViewById(R.id.room_name_spin);

		navigateBack=(ImageView) findViewById(R.id.imageView2);

		up_adap=new UpdateAdapter(this);
		// setting item selected listener for spinner
		dev_type_spinner.setOnItemSelectedListener(this);	 
		room_spinner.setOnItemSelectedListener(this);

		//setting house name for wireless database
		StaticVariables.WHOUSE_NAME=StaticVariables.HOUSE_NAME+"_WLS";;
		//setting database name with which wireless database is to save
		StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";;


		try{
			houseDB=new HouseConfigurationAdapter(this);
			houseDB.open();			//opening house database
			//initialize wireless db
			WhouseDB=new WirelessConfigurationAdapter(DeleteRoom.this);
			swbadap=new SwbAdapter(this);
			mas_adap=new MasterAdapter(this);

		}catch(Exception e){
			e.printStackTrace();
		} 


		//filling room name list
		fillRoomNameList();

		//fetching device full names
		//FullDevNameList();
		//fetching device types
		//DevTypeList();
		//fetching device short names
		//ShortDevNameList();


		Tcp_con mTcp = new Tcp_con(DeleteRoom.this);


		if (Tcp_con.isClientStarted) {

			receiveddata(NetwrkType, StaticStatus.Network_Type, null);
			receiveddata(ServStatus, StaticStatus.Server_status, null);

		} else {
			Tcp_con.stacontxt = DeleteRoom.this;
			Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
			Tcp_con.registerReceivers(DeleteRoom.this);
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
				Room_selected="";
				String roomname=null,devtype=null;
				try{
					roomname=room_spinner.getSelectedItem().toString();
					//	devtype=dev_type_spinner.getSelectedItem().toString();

				}catch(Exception e){
					e.printStackTrace();
				}
				if((roomname!=null && !roomname.equals("Select Room")) ) {

					popupdelete();
				}else{
					Alert("Invalid Data","Please select Room Name");
				}


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
	private void popupappdetails(final String txt) {
		runOnUiThread(new Runnable() {
			@Override
			public void run() {
				AlertDialog.Builder alert = new AlertDialog.Builder(DeleteRoom.this);
				alert.setTitle("Info");
				//alert.setMessage("Enter Password");
				final TextView applnnameinfo = new TextView(DeleteRoom.this);
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

	public void ReadlineData( final String RL) {
		StaticVariabes_div.log("RL:- " + RL, TAG1);


		if(RL.contains("&010"))
		{
			StaticVariabes_div.log("RL inside:- " + RL, TAG1);
			final StringBuilder sbbuild=new StringBuilder();

			runOnUiThread(new Runnable() {
				public void run() {

					char s14 = RL.charAt(4);

					StaticVariabes_div.log("RL s14:- " + s14, TAG1);

					if(s14=='S'){
						sbbuild.append("Wired Room Deleted Successfully.");
						popup_fromlocalDB_wired(Room_selected);

					}else if(s14=='F'){

						sbbuild.append("Wired Room Deleted Successfully.");
					}else if(s14=='X'){

						sbbuild.append("Wired Room Does Not Exist.");
					}


					char s15 = RL.charAt(5);
					StaticVariabes_div.log("RL s15:- " + s15, TAG1);

					if(s15=='S'){

						sbbuild.append("Wireless Room Deleted Successfully.");
						popup_fromlocaldb_wireless(Room_selected);

					}else if(s15=='F'){

						sbbuild.append("Wireless Room Deleted Successfully.");

					}else if(s15=='X'){

						sbbuild.append("Wireless Room Does Not Exist.");

					}


					char s16 = RL.charAt(6);
					StaticVariabes_div.log("RL s16:- " + s16, TAG1);

					if(s16=='S'){

						sbbuild.append("Timer Deleted Successfully.");

					}else if(s16=='F'){

						sbbuild.append("Timer Deleted Successfully.");

					}else if(s16=='X'){

						sbbuild.append("Timer Does Not Exist.");

					}


					//Toast.makeText(DeleteRoom.this, "Room Deleted From Gateway Successfully", Toast.LENGTH_SHORT).show();
					//popup_fromlocalDB(Room_selected);


					Alert("info", sbbuild.toString());

					HashMap<String, String> devMap = null;

					try {

						fillRoomNameList();

					} catch (Exception e) {
						e.printStackTrace();
					}

					Room_selected="";
				}
			});

		}/*else if (RL.contains("&010W@")) {

			runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Room Not Deleted Try Again");
				}
			});

		} else if (RL.contains("&010T@")) {

			runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Room Deleted.Timer Not Deleted.Please Delete From Timer List");
				}
			});

		} else if (RL.contains("&010WL@")) {

				runOnUiThread(new Runnable() {
				public void run() {
					StaticVariabes_div.log("RL:- " + RL, TAG1);
					popupappdetails("Room Not Deleted From Wireless Table.Please Try Again");
				}
			});

		}*/
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

	/*public void network_signal(int signal1, final boolean serv) {

		if (serv) {

			if (StaticStatus.Network_Type.equals("TRUE3G")) {
				btsig.setBackgroundResource(R.drawable.mobiledata);
			}else  if (btsig!=null) {
				if (signal1 <= 1)
					btsig.setBackgroundResource(R.drawable.remote_sig_1);
				else if (signal1 <= 2)
					btsig.setBackgroundResource(R.drawable.remote_sig_2);
				else if (signal1 <= 3)
					btsig.setBackgroundResource(R.drawable.remote_sig_3);
				else if (signal1 <= 4)
					btsig.setBackgroundResource(R.drawable.remote_sig_4);
			}

		} else {

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

	}*/

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


	public String fetch_roomno(String roomname){
		swbadap.open();
		String roomno=swbadap.getroomno(roomname);
		swbadap.close();


		mas_adap.open();
		if(roomno.equals("")){
			StaticVariabes_div.log("roomno null", TAG1);
			roomno=mas_adap.getroomno(roomname);
		}
		else{
			StaticVariabes_div.log("room selected"+"roomname"+roomname+"houseno"+"roomno"+roomno, TAG1);
		}
		mas_adap.close();

	return  roomno;


	}
	public void popupdelete(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("INFO");
		alertDialogBuilder.setMessage("Are You Sure You Want To Delete Room").setCancelable(false)

				.setPositiveButton("yes",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						String roomname=null,devtype=null;
						try{
							roomname=room_spinner.getSelectedItem().toString();
							//	devtype=dev_type_spinner.getSelectedItem().toString();

						}catch(Exception e){
							e.printStackTrace();
						}
						if((roomname!=null && !roomname.equals("Select Room")) ) {
							Room_selected=roomname;
							String roomno=fetch_roomno(roomname);
							if(roomno!=null) {
								Send_dat_aes(roomno, "<", "_");
							}

						}else{
						Alert("Invalid Data","Please select Room Name");
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


	public  void popup_fromlocalDB(String roomname) {
		up_adap.open();
		boolean roomdeleted = up_adap.deleteroom(roomname);
		up_adap.close();

		Update_db_version_number();

		try {
			LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
			locallist_adap = new LocalListArrangeTable(DeleteRoom.this, StaticVariabes_div.housename, StaticVariabes_div.housename);
			locallist_adap.open();
			locallist_adap.deleteall();
			locallist_adap.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		if (roomdeleted) {

			try {
				if (WirelessConfigurationAdapter.sdb != null) {
					if (!WirelessConfigurationAdapter.sdb.isOpen()) {
						WhouseDB.open();
						StaticVariables.printLog("TAG", "DB open ");
					}

					//delete data from wireless table regarding the deleted wired device
					//	boolean isWLS_Deleted=WhouseDB.DeleteWiredroomDetails(roomname);
					boolean isWLS_Deleted = WhouseDB.deleteroomtest(roomname);

					if (isWLS_Deleted) {
						StaticVariables.printLog("TAG", "wireless details deleted");
						Alert("wrls Deleted", "Wreless Room Deleted Successfully!");
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
			}


			Alert("Deleted", "Room Deleted Successfully!");

			//checking number of devices  in room
			HashMap<String, String> devMap = null;

			try {

				fillRoomNameList();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			Alert("Failed", "Unable to Delete Room!");
		}
	}

	public  boolean popup_fromlocaldb_wireless(String roomname){
		boolean isWLS_Deleted=false;
		try {
			if (WirelessConfigurationAdapter.sdb != null) {
				if (!WirelessConfigurationAdapter.sdb.isOpen()) {
					WhouseDB.open();
					StaticVariables.printLog("TAG", "DB open ");
				}

				//delete data from wireless table regarding the deleted wired device
				//	boolean isWLS_Deleted=WhouseDB.DeleteWiredroomDetails(roomname);
			 isWLS_Deleted = WhouseDB.deleteroomtest(roomname);

				if (isWLS_Deleted) {
					StaticVariables.printLog("TAG", "wireless details deleted");

					Update_db_version_number();

					try {
						LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
						locallist_adap = new LocalListArrangeTable(DeleteRoom.this, StaticVariabes_div.housename, StaticVariabes_div.housename);
						locallist_adap.open();
						locallist_adap.deleteall();
						locallist_adap.close();
					} catch (Exception e) {
						e.printStackTrace();

					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
return  isWLS_Deleted;
	}
	public  boolean popup_fromlocalDB_wired(String roomname) {
		boolean roomdeleted=false;
		up_adap.open();
		roomdeleted = up_adap.deleteroom(roomname);
		up_adap.close();

		Update_db_version_number();

		try {
			LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
			locallist_adap = new LocalListArrangeTable(DeleteRoom.this, StaticVariabes_div.housename, StaticVariabes_div.housename);
			locallist_adap.open();
			locallist_adap.deleteall();
			locallist_adap.close();
		} catch (Exception e) {
			e.printStackTrace();

		}
		return roomdeleted;

	}


	/*public void popupdelete(){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this); 
		alertDialogBuilder.setTitle("INFO");
		alertDialogBuilder.setMessage("Are You Sure You Want To Delete Room").setCancelable(false)

		.setPositiveButton("yes",new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog,int id) {


				swbadap.open();
				boolean homedel=swbadap.deleteDatabase(housename+".db");
				swbadap.close();	

				if(homedel){
					pre_adap.open();
					db.opendb();

					if(houseno!=null){
						boolean locwirelessdb=swbadap.deleteDatabase(housename+"_WLS.db");
						StaticVariabes_div.log("locwirelessdb"+locwirelessdb, TAG1);
						boolean lochomelist=db.delete(Integer.parseInt(houseno));
						StaticVariabes_div.log("lochomelist"+lochomelist, TAG1);
						pre_adap.deletevalforhouseno(houseno);
						popuphomedeleted("house deleted");
					}else{
						popup("house not deleted,try Again");
					}
					db.close();
					pre_adap.close();

				}else{
					popup("house not deleted");
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
	}*/
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
				AlertDialog.Builder dlg = new AlertDialog.Builder(DeleteRoom.this,AlertDialog.THEME_HOLO_LIGHT);
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
			//	FillDeviceList();

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
			devMap=houseDB.getRoomDevices(CurrentRoomNo);

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
				}


				String key=null,value=null;
				int dno=0;
				for(int i=0;i<sortedKeys.size();i++){
					//getting key values
					key=(String) sortedKeys.get(i);
					//getting value of current key
					value=devMap.get(key);
					//parsing string to integer
					dno=Integer.parseInt(key);
					StaticVariables.printLog("TAG","dno :"+key+" devName :"+value);
					int index=Short_DevNames_List.indexOf(value);
					//getting full name of devtype
					String DevName=Full_DevNames_List.get(index);

					//filling devno and devname list
					finalDevNo_List.add(dno);
					finalDevName_List.add(DevName+"--"+dno);

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
		Intent it=new Intent(DeleteRoom.this,Configuration_Main.class);
		it.putExtra("houseno",houseno);	
		it.putExtra("housename",housename);	
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

	public void Update_db_version_number(){
		if(StaticVariabes_div.housename!=null) {
			try {
				ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
				sdadap = new ServerDetailsAdapter(DeleteRoom.this);
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

