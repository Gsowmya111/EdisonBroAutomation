package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: EditRoomName.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to Edit Room Name.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;


import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edisonbro.com.edisonbroautomation.CombFrag;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class EditRoomName extends Activity implements OnItemSelectedListener {

	Button edit_btn; 
	EditText et_editroom;
	Spinner dev_type_spinner,room_spinner;
	HouseConfigurationAdapter houseDB=null;
	WirelessConfigurationAdapter WhouseDB=null;
	private MasterAdapter mas_adap;
	private SwbAdapter dev_adap;
	SpinnerAdapter deviceAdapter,roomNameAdapter;
	UpdateAdapter up_adap;
	ArrayList<String> UniqueRoomList=new ArrayList<String>();
	List<String> Device_Type_List,Short_DevNames_List,Full_DevNames_List;

	//lists for device types
	ArrayList<String>  finalDevName_List=new ArrayList<String>(); 
	ArrayList<Integer> finalDevNo_List =new ArrayList<Integer>();

	int CurrentRoomNo=0 ,DevType_Position=0;
	int spinnerLayoutId= R.layout.spinnerlayout;
    boolean roomaleadyexist=false,roomnameoldexists_conf=false,roomnameoldexists_wrls=false;
    boolean isDbExists=false;
	ImageView navigateBack;
	private ServerDetailsAdapter sdadap;
	LocalListArrangeTable locallist_adap;

	private static final String TAG1 = "Edit Room Name-";
	@Override
	protected void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.editroom);


		edit_btn=(Button) findViewById(R.id.edit_btn);
		room_spinner = (Spinner) findViewById(R.id.room_name_spin);      
		navigateBack=(ImageView) findViewById(R.id.imageView2);

		et_editroom=(EditText) findViewById(R.id.etwrlsroomname);
			 
		room_spinner.setOnItemSelectedListener(this);	


		//setting house name for wireless database
		StaticVariables.WHOUSE_NAME= StaticVariables.HOUSE_NAME+"_WLS";;
		//setting database name with which wireless database is to save
		StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";;

		SwbAdapter.OriginalDataBase= StaticVariabes_div.housename+".db";
		MasterAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";

			try{
				houseDB=new HouseConfigurationAdapter(this);
				houseDB.open();			//opening house database		
			
				WhouseDB=new WirelessConfigurationAdapter(this);
				WhouseDB.open();

				up_adap=new UpdateAdapter(this);

				//opening wireless   database

				mas_adap=new MasterAdapter(this);
				dev_adap = new SwbAdapter(this);



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
			UniqueRoomList.addAll(houseDB.RoomNameList());
			UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
			UniqueRoomList.add("Select Room");
			
			//Loading data in room name spinner
			//roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
			//room_spinner.setAdapter(roomNameAdapter);
			
           Set<String> temprr = new LinkedHashSet<String>(UniqueRoomList );
			
			ArrayList<String> setList=new ArrayList<String>(temprr); 
		
			UniqueRoomList=setList;
			
			//Loading data in room name spinner
			roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
			room_spinner.setAdapter(roomNameAdapter);
			// Displaying Last item of list
			//room_spinner.setSelection(UniqueRoomList.size()-1);	
			
			}else{

				fillRoomNameList();

			}

		StaticVariabes_div.log( "isDbExists"+isDbExists,TAG1  );
		//filling room name list
	//	fillRoomNameList();

		
		/*//Fetching list of All room names from database and adding to local array list
				UniqueRoomList=new ArrayList<String>();
				TreeSet<String> rooms_wls=new TreeSet<String>(); 
				try{
					rooms_wls=houseDB.RoomNameList();
					//Fetching list of All room names from database and adding to local array list
					if(rooms_wls.size()>0)
						UniqueRoomList.addAll(rooms_wls);
				}catch(Exception e){
					e.printStackTrace();
				}


				//UniqueRoomList.add("Add New Room");
				UniqueRoomList.add("Select Room");

				//Loading data in room name spinner
				roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
				room_spinner.setAdapter(roomNameAdapter);*/
				
		navigateBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				goPrevious();
			}
		});
		

		//setting on click listener on delete button
		edit_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				roomaleadyexist=false;
				String roomname=null;
				try{
					roomname=room_spinner.getSelectedItem().toString();

				}catch(Exception e){
					e.printStackTrace();
				}

				StaticVariabes_div.log( "roomnamold"+roomname,TAG1  );

				if((roomname!=null && !roomname.equals("Select Room")) ) 		
				{
					

				String value = et_editroom.getText().toString();

						StaticVariabes_div.log( "value"+value,TAG1  );

					if(value.length()<1){
					//value="Room Name";
					et_editroom.setError("Please Enter RoomName");

				    }else {

					StaticVariabes_div.log("value new " + value + " roomnamold " + roomname, TAG1);

					try {
						//	WhouseDB=new WirelessConfigurationAdapter(EditRoomName.this);

						if (isDbExists) {
							EditWiredRoomName(value, roomname);

							//EditWirelessRoomname(value,roomname);

							locallistupdate();
						} else {
							EditWiredRoomName(value, roomname);
							locallistupdate();
						}


					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				}else{
					Alert("Invalid Data","Please select Room Name");
				}

			}
		});

	}


	public void locallistupdate(){

		Update_db_version_number();

		try {
			LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
			locallist_adap = new LocalListArrangeTable(EditRoomName.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
			locallist_adap.open();
			locallist_adap.deleteall();
			locallist_adap.close();
		}catch(Exception e){
			e.printStackTrace();

		}
	}

	public void EditWirelessRoomname(String value ,String roomname ){
		if(!WirelessConfigurationAdapter.sdb.isOpen()){
			WhouseDB.open();
			StaticVariables.printLog("TAG","DB open ");
		}

		Cursor curss=WhouseDB.checkroomnameexistWrls(value);
		if(curss!=null){
			roomaleadyexist=true;
		}
		StaticVariables.printLog("TAG", roomaleadyexist+" roomaleadyexist 1");
		Cursor curscnf=WhouseDB.checkroomnameexistconfgWrls(value);
		if(curscnf!=null){
			roomaleadyexist=true;
		}

		StaticVariables.printLog("TAG", roomaleadyexist+" roomaleadyexist");

		if(!roomaleadyexist){

			Cursor curss_oldnam=WhouseDB.checkroomnameexistWrls(roomname);
			if(curss_oldnam!=null){
				roomnameoldexists_wrls=true;
			}else{
				roomnameoldexists_wrls=false;
			}

			Cursor curscnf_oldnam=WhouseDB.checkroomnameexistconfgWrls(roomname);
			if(curscnf_oldnam!=null){
				roomnameoldexists_conf=true;
			}else{
				roomnameoldexists_conf=false;
			}

			if(roomnameoldexists_wrls||roomnameoldexists_conf) {

				StaticVariables.printLog("TAG", roomnameoldexists_wrls+" wireless conf"+roomnameoldexists_conf);

				//update roomname data from wireless table
				boolean isWLS_updated = WhouseDB.EditRoomWireless(value, roomname);

				if (isWLS_updated) {
					StaticVariables.printLog("TAG", "wireless details updated");
					Alert("Info", "Updated Successfully");
					//roomnamelist();
					fillRoomNameList();
					et_editroom.setText("");
				} else {
					Alert("Error", "Error Updating Roomname,Try Again");
				}

			}else {
				fillRoomNameList();
				et_editroom.setText("");
				Alert("Info", "Updated Successfully");
			}

		}else{
			value="";
			Alert("Exists","Roomname Already Exists");

		}

	}
	public void popupinfo(String msg){

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
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}

	public void popupinfo_wired(String msg,final String value,final String roomname){

		AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
		alertDialogBuilder.setTitle("INFO");
		alertDialogBuilder
				.setMessage(msg)
				.setCancelable(false)
				.setPositiveButton("Proceed",new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog,int id) {
						dialog.cancel();

						EditWirelessRoomname(value,roomname);
					}
				});
		// create alert dialog
		AlertDialog alertDialog = alertDialogBuilder.create();
		// show it
		alertDialog.show();
	}
	public void EditWiredRoomName(String value,String roomname){



		StaticVariables.printLog("new value "+value," wired ");

			dev_adap.open();
			Cursor curss=dev_adap.checkroomnameexist(value);
			if(curss!=null){
				roomaleadyexist=true;
			}
			dev_adap.close();

		StaticVariables.printLog("new value"+value," wired ");

			mas_adap.open();
			Cursor curmass=mas_adap.checkroomnameexist(value);
			if(curmass!=null){
				roomaleadyexist=true;
			}
			mas_adap.close();





			if(!roomaleadyexist){

				StaticVariabes_div.log( "roomaleadyexist"+roomaleadyexist,TAG1  );
				up_adap.open();
				boolean roomnamchanged=up_adap.update_roomname_onlytwo(roomname, value);
				up_adap.close();
				if(roomnamchanged){

					if(isDbExists){
						popupinfo_wired(" Wired RoomName Updated Successfully,Proceed To Update Wireless RoomName", value, roomname);

					}else {
						popupinfo(" Wired RoomName Updated Successfully");
						fillRoomNameList();
						et_editroom.setText("");
					}

				}else{
					popupinfo("Error Updating Roomname,Try Again");
				}

			}else{
				popupinfo("Room already Exists");
			}

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

	
	//fill roomnm
	public void roomnamelist(){
		//Fetching list of All room names from database and adding to local array list
		UniqueRoomList=new ArrayList<String>();
		//UniqueRoomList.addAll(houseDB.WirelessRoomNameList());
		UniqueRoomList.addAll(WhouseDB.WirelessPanelsRoomNameList());
		UniqueRoomList.add("Select Room");
		
		//Loading data in room name spinner
		//roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
		//room_spinner.setAdapter(roomNameAdapter);
		
       Set<String> temprr = new LinkedHashSet<String>(UniqueRoomList );
		
		ArrayList<String> setList=new ArrayList<String>(temprr); 
	
		UniqueRoomList=setList;
		
		//Loading data in room name spinner
		roomNameAdapter=new CustomSpinnerAdapter(this, spinnerLayoutId, UniqueRoomList);
		room_spinner.setAdapter(roomNameAdapter);
	}
	// alert to show whether device deleted or not
	private void Alert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				AlertDialog.Builder dlg = new AlertDialog.Builder(EditRoomName.this,AlertDialog.THEME_HOLO_LIGHT);
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


	//go back
	void goPrevious(){
		//going back to admin page
		Intent it=new Intent(EditRoomName.this,Configuration_Main.class);
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
				sdadap = new ServerDetailsAdapter(EditRoomName.this);
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


