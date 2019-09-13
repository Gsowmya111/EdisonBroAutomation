package edisonbro.com.edisonbroautomation.wiredconfig;

/**
 *  FILENAME: DownloadDatabse.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: class for downloading database from gateway .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edisonbro.com.edisonbroautomation.Local_Remote_DownloadActivity;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;

import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.ChangeServerPassword;
import edisonbro.com.edisonbroautomation.operatorsettings.Configuration_Main;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;


public class DownloadDatabse extends Activity implements View.OnClickListener
{
	CheckBox showPassword;
	Button download_btn,clear_btn;
	EditText userName,password,houseName;
	ImageView serverStatus;
	Button navigateBack;
	private ProgressDialog pdlg;

	Tcp tcp=null;
	int DownloadProgress=0;	
	int server_online=R.drawable.connected;
	int server_offline=R.drawable.not_connected;

	final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5,TCP_CONNECTED=6,ERR_USER=7,MAX_USER=8;
	final String GET_DB_STATUS="$119&" , GET_DB_DOWNLOAD="$118&";
	final String GET_DB_DOWNLOAD_WLS = "$121&";
	String SERVER_PASSWORD=null ; 

	String uname=null,upass=null,uhouse=null;
	boolean exceptionOccured=false,isServerAuntheticated=false,IS_SERVER_NOT_AUNTHETICATED=false,
			isDbConfigured=false,isAlertshown=false ,isDialogOpened=false;	
	//LocalDatabaseAdapter localDB=null;
	LocalDatabaseHelper localdb_helpr=null;

	TextWatcher textWatcher;
	String ACTIVITY_NAME=null;
	HouseConfigurationAdapter houseDB = null;
	ServerDetailsAdapter sdadap=null;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		setContentView(R.layout.download_home_db);
		//setContentView(R.layout.download_databases);
		//setContentView(R.layout.download_home_database);
		//setContentView(R.layout.testxml);
		//setupUI(findViewById(R.id.downloadLayout));	//setting activity view to method for hiding keyboard

		userName=(EditText) findViewById(R.id.server_username);
		password=(EditText) findViewById(R.id.server_pass);
		houseName=(EditText)findViewById(R.id.house_name);
		serverStatus=(ImageView) findViewById(R.id.server_status);
		showPassword=(CheckBox) findViewById(R.id.showPassword);
		download_btn=(Button) findViewById(R.id.download_db);
		clear_btn=(Button) findViewById(R.id.clear);

		//houseName.setImeOptions();

		//navigateBack=(ImageView) findViewById(R.id.imageView2);
		navigateBack=(Button) findViewById(R.id.imageView2);

		//setting on click listener for buttons
		download_btn.setOnClickListener(this);
		clear_btn.setOnClickListener(this);
		navigateBack.setOnClickListener(this);

		//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		//StrictMode.setThreadPolicy(policy);

		try{
			//Passing Context of Current activity and opening database
		//	localDB=new LocalDatabaseAdapter(this);
		//	localDB.opendb();

			localdb_helpr=new LocalDatabaseHelper(this);
			localdb_helpr.opendb();

		}catch(Exception e){
			e.printStackTrace();
		}

		if(Tcp.tcpConnected){
			Thread t=new Thread()
			{
				public void run()
				{
					/*runOnUiThread(new Runnable() {
						public void run() {
							userName.setText(StaticVariables.UserName);
							password.setText(StaticVariables.Password);
						}
					});*/
					Looper.prepare();
					tcp=new Tcp(TcpHandler);	//passing handler instance to tcp class
					Looper.loop();
					serverStatus.setImageResource(server_online);

				}
			};t.start();
		}else{
			serverStatus.setImageResource(server_offline);	
		}


		/*houseName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
			@Override
			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				Toast.makeText(DownloadDatabse.this,"ime action",Toast.LENGTH_SHORT).show();

				if(actionId== EditorInfo.IME_ACTION_DONE) {
					Toast.makeText(DownloadDatabse.this,"ime action done",Toast.LENGTH_SHORT).show();
					InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

					return true;
				}
				return  false;
			}
		});*/

		// Setting EditorActionListener for the EditText
		/*houseName.setOnEditorActionListener(new TextView.OnEditorActionListener() {


			public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
				//if(actionId== EditorInfo.IME_ACTION_DONE){
					Toast.makeText(DownloadDatabse.this,"imei action",Toast.LENGTH_SHORT).show();
					// Creating uri to be opened
					//InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
					//imm.showSoftInput(houseName, InputMethodManager.HIDE_IMPLICIT_ONLY);
					InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
					//InputMethodManager inputMethodManager = (InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
					//inputMethodManager.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				   return  true;
				//}
				//return false;
			}
		});
*/






		//setting on check listener for check box
		showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//showing password 
					password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					//hiding password
					password.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}

			}
		});		


		try{
			//getting activity name from intent of previous activity
			Intent activityIntent=getIntent();
			ACTIVITY_NAME=activityIntent.getStringExtra("activity_name");
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	//creating Handler for handling TCP connections
	private Handler TcpHandler=new Handler()
	{
		@Override
		public void handleMessage(Message msg)
		{
			switch(msg.what)
			{
			case READ_LINE:
			{
				final String Data=(String) msg.obj;
				StaticVariables.printLog("TCP RESPONSE","DATA GET FROM TCP SOCKET :"+Data);

				//getting database status i.e empty or configured
				if( Data!=null && Data.startsWith("$") && Data.endsWith("$")){

					//Filtering the required Data String
					String isDownloadPossible=Data.substring(1, Data.length()-1);  
					StaticVariables.printLog("DATABSE","Database available : "+isDownloadPossible);   

					//checking if databse available for download
					if(isDownloadPossible.equalsIgnoreCase("TRUE"))	{

						isDbConfigured=true;

					}else if(isDownloadPossible.equalsIgnoreCase("FALSE")){
						if(!isAlertshown){
							Tcp.tcpConnectionClose();	//closing Tcp connection
							NoDatabaseErrorDialog();	//Displaying No Database to download error dialog
							isAlertshown=true;
						}

					}

				}
				if( Data!=null && Data.equals("*OK#")){

				}

				break;
			}

			case READ_BYTE:	{

				break;
			}

			case EXCEPTION:
			{
				final String Data=(String) msg.obj;
				StaticVariables.printLog("TCP EXCEPTION","DATA GET FROM TCP SOCKET :"+Data);
				runOnUiThread(new Runnable() {
					public void run()
					{
						//TCP connection lost status updation in UI
						serverStatus.setImageResource(server_offline);

					}
				});
				exceptionOccured=true;		//exception occured 
				break;
			}

			case TCP_LOST:
			{ 
				runOnUiThread(new Runnable() {
					public void run() 
					{
						//TCP connection lost status updation in UI
						serverStatus.setImageResource(server_offline);
						//Tcp.tcpConnectionClose();
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
						isDialogOpened=true;
						serverStatus.setImageResource(server_offline);
						Tcp.tcpConnectionClose();
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
						isDialogOpened=true;
						serverStatus.setImageResource(server_offline);
						Tcp.tcpConnectionClose();
						TCPErrorAlert("Server Error","Server connections Limit Exceeded .Please disconnect some other client to connect with server.");

					}
				});
				break;
			}case TCP_CONNECTED:
			{
				final String Data=(String) msg.obj;
				if(Data.contains("SERVER CONNECTED"))
				runOnUiThread(new Runnable() {
					public void run()
					{
						serverStatus.setImageResource(server_online);
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

				//dismiss dialog
				DismissDialog();

				AlertDialog.Builder dlg = new AlertDialog.Builder(DownloadDatabse.this,AlertDialog.THEME_HOLO_LIGHT);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setCancelable(false);
				dlg.setIcon(android.R.drawable.ic_dialog_alert);

				dlg.setPositiveButton("OK",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog,
							int which) {
						dialog.dismiss();
						isDialogOpened=false;
					}
				});		

				AlertDialog d = dlg.create();
				d.show();
			}
		});
	}

	//connect and download thread
	private void ConnectAndDownload_wireless(){
		SERVER_PASSWORD=StaticVariabes_div.loggeduser+StaticVariabes_div.loggedpwd;
		StaticVariables.printLog("SERVER_PASSWORD","SERVER PASSWORD : "+SERVER_PASSWORD);

		//StaticVariables.HOUSE_NAME=StaticVariabes_div.housename;

		StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";
		//StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_DB_NAME+"_WLS";
		Thread connectionThread=new Thread(){
			public void run(){

				runOnUiThread(new Runnable() {
					public void run() {
						ProgressDialog("Downloading Wireless Data From Server....");
					}
				});
				byte cipher[]=null;
				int i=0;
				boolean serverConnected=false;

				if(!Tcp.tcpConnected)
				{
					tcp=new Tcp(TcpHandler); //passing handler instance

					while(i<5)
					{
						if(exceptionOccured) {
							updateProgress(100);	//updating progress
							break;
						}else if(Tcp.tcpConnected){
							i=10;
							//Tcp.tcpWrite(SERVER_PASSWORD);	//sending password

							try {
								cipher = encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),SERVER_PASSWORD.getBytes());
							} catch (Exception e) {
								e.printStackTrace();
							}

							//Tcp.tcpWrite(SERVER_PASSWORD);	//sending password
							Tcp.tcpWrite(cipher,true);

							delay(1000);	//delay to get ok response from server



							if(!IS_SERVER_NOT_AUNTHETICATED){

								IS_SERVER_NOT_AUNTHETICATED=false;

								serverConnected=true;
								isServerAuntheticated=true;
							}else{
								IS_SERVER_NOT_AUNTHETICATED=false;
							}
							break;
						}

						i++;
						int j=0;
						while(j<20)	{
							j++;
							if(Tcp.tcpConnected ){
								break;
							}else {
								delay(100);
							}
						}

					}
				}else {
					serverConnected=true;
				}

				if(serverConnected)	{

					runOnUiThread(new Runnable() {
						public void run() {
							//showing server is connected
							serverStatus.setImageResource(server_online);
							//setting House name to save database
							StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_WLS";
						}
					});

					//Sending Db Download Command
					//Tcp.tcpWrite(GET_DB_DOWNLOAD_WLS,DownloadDatabse.this);

					try {
						cipher = encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),GET_DB_DOWNLOAD_WLS.getBytes());
					} catch (Exception e) {
						e.printStackTrace();
					}

					Tcp.tcpWrite_bytes(cipher,DownloadDatabse.this);
					StaticVariables.printLog("DWD","server connected and download command send");

					// setting timeout loop for download task

					int counter1=1;
					int progress=0;
					while(counter1<=10){

						counter1++;
						int counter2=1;
						while(counter2<10){

							if(Tcp.MainDownoad_Completed){
								//setting outer loop counter value =20 and exit from inner loop
								counter1=20;
								break;
							}else{
								delay(100);
							}

							counter2++;
						}
						//updating progress
						updateProgress(progress+=10);
						StaticVariables.printLog("DOWNLOAD TIMER COUNT","DWD TIMER : "+counter1);
					}

					//update progress and making UI thread sleep for 1 sec
					//to show final progress to user
					updateProgress(100);
					SleepUiThread(1000);

					if(counter1<20 && !Tcp.MainDownoad_Completed){
						Tcp.MainDownoad_Completed=false;
						if(!isDialogOpened){
							DownloadErrorDialog();	//showing error dialog
						}

					}else if(Tcp.MainDownoad_Completed){
						Tcp.MainDownoad_Completed=false;
						if(!isDialogOpened){
							// DownloadSuccessDialog();	//showing success dialog
							DownloadSuccessDialog_wireless();
						}
					}


				}else {

					runOnUiThread(new Runnable() {
						public void run() {
							updateProgress(100);	//update task progress
							//making UI thread sleep for 1 sec to show final progress to user
							SleepUiThread(1000);
							//cancelDialog();		// Dismissing the progress dialog
                             DismissDialog();
						}
					});

					if(exceptionOccured){

						Tcp.tcpConnectionClose();	//closing connection
						if(!isDialogOpened){
							serverErrorDialog("Server UnReachable",
									"Please Check Your WiFi Settings And Check Whether Server is ON");

						}
					}

					resetVariables();//resetting variables


				}
			}
		};
		connectionThread.start();

	}
	void DownloadSuccessDialog_wireless()
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB)
			@Override
			public void run()
			{
				//dismissing dialog
				//cancelDialog();

				DismissDialog();

				//updateServerInfo(); // Updating Server Information

				AlertDialog.Builder dialog=new AlertDialog.Builder(DownloadDatabse.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Download Completed");
				dialog.setMessage("Wireless Settings Downloaded Successfully. Do You Want To Operate Devices?");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
					}
				});
				dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();

						//closing tcp connection
						Tcp.tcpConnectionClose();

						Intent it=new Intent(DownloadDatabse.this,Main_Navigation_Activity.class);
						it.setFlags(it.FLAG_ACTIVITY_NEW_TASK | it.FLAG_ACTIVITY_CLEAR_TASK);
						// adding transition to activity exit
						//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						startActivity(it);
						finish();
						/*
						Intent it=new Intent(DownloadDatabse.this,ConfigurationType.class);
						startActivity(it);
						finish(); */
					}
				});


				dialog.show();
			}
		});
	}

	//connect and download thread
	private void ConnectServer(){

		Thread connectionThread=new Thread(){
			public void run(){

				runOnUiThread(new Runnable() {
					public void run() {
						ProgressDialog("Downloading Data From Server....");
					}
				});

				int i=0;
				boolean serverConnected=false;

				if(!Tcp.tcpConnected)
				{
					tcp=new Tcp(TcpHandler); //passing handler instance

					while(i<5)
					{
						if(exceptionOccured) {
							updateProgress(100);	//updating progress
							break;
						}else if(Tcp.tcpConnected){
							i=10;

							byte cipher[]=null;
							try {
								cipher = encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),SERVER_PASSWORD.getBytes());
							} catch (Exception e) {
								e.printStackTrace();
							}

								//Tcp.tcpWrite(SERVER_PASSWORD);	//sending password
							Tcp.tcpWrite(cipher,true);

							delay(1000);
							if(!IS_SERVER_NOT_AUNTHETICATED){

								serverConnected=true;
								isServerAuntheticated=true;

								// getting database status i.e if it is configured or not
								getDBstatus();	
							}else{
								IS_SERVER_NOT_AUNTHETICATED=false;

							}

							break;
						}

						i++;
						int j=0;
						while(j<20)	{
							j++;
							if(Tcp.tcpConnected ){
								break;						
							}else {
								delay(100);
							}
						}

					}
				}else {
					serverConnected=true;	
					//sending command to check db is configured or not before giving download command
					getDBstatus();
				}

				if(serverConnected)	{

					runOnUiThread(new Runnable() {
						public void run() {
							serverStatus.setImageResource(server_online);	//showing server is connected
						}
					});
					int counter1=1;
					while(counter1<=10)
					{
						counter1++;
						int counter2=1;
						while(counter2<10)
						{
							if(isDbConfigured){
								counter1=20;
								break;
							}else{
								delay(100);
							}

							counter2++;
						}
						StaticVariables.printLog("DB","db status wait count : "+counter1);
					}

					if(counter1<20 && !isDbConfigured){
						isDbConfigured=false;
						if(!isAlertshown){
							DownloadErrorDialog();
							isAlertshown=true;
						}


					}else if(isDbConfigured){
						isDbConfigured=false;
						//setting House name to save database on sd card
						StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME;
						//StaticVariables.HOUSE_DB_NAME
						//starting db download task+
						downloadDatabase();
					}


				}else {

					updateProgress(100);
					DismissDialog();		// Dismissing the progress dialog

					if(exceptionOccured){

						Tcp.tcpConnectionClose();	//closing connection
						if(!isDialogOpened){
							serverErrorDialog("Server UnReachable",
									"Please Check Your WiFi Settings And Check Whether Server is ON");

						}
					}

					resetVariables();//resetting varaibles

				}		
			}
		};connectionThread.start();

	}

	public static byte[] encrypt(byte[] ivBytes, byte[] keyBytes, byte[] mes)
			throws NoSuchAlgorithmException,
			NoSuchPaddingException,
			InvalidKeyException,
			InvalidAlgorithmParameterException,
			IllegalBlockSizeException,
			BadPaddingException, IOException {

		AlgorithmParameterSpec ivSpec = new IvParameterSpec(ivBytes);
		SecretKeySpec newKey = new SecretKeySpec(keyBytes, "AES");
		Cipher cipher = null;
		cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		cipher.init(Cipher.ENCRYPT_MODE, newKey, ivSpec);

		return  cipher.doFinal(mes);

	}
	//connect and download thread
	private void downloadDatabase(){
		Thread downloadThread=new Thread(){
			public void run(){

				//StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				//StrictMode.setThreadPolicy(policy);

				//Tcp.tcpWrite(GET_DB_DOWNLOAD,DownloadDatabse.this); //Sending Db Download Command


				byte cipher[]=null;
				try {
					cipher = encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),GET_DB_DOWNLOAD.getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}

				Tcp.tcpWrite_bytes(cipher,DownloadDatabse.this);

				StaticVariables.printLog("DWD","server connected and download command send");

				int counter1=1;
				int progress=0;
				while(counter1<=10)
				{
					counter1++;
					int counter2=1;
					while(counter2<10)
					{
						if(Tcp.MainDownoad_Completed){
							counter1=20;
							break;
						}else{
							delay(100);
						}

						counter2++;
					}
					updateProgress(progress+10);
					StaticVariables.printLog("DOWNLOAD TIMER COUNT","DWD TIMER : "+counter1);
				}

				//update progress and making UI thread sleep for 1 sec
				//to show final progress to user
				updateProgress(100);
				SleepUiThread(1000);

				if(counter1<20 && !Tcp.MainDownoad_Completed){
					Tcp.MainDownoad_Completed=false;
					if(!isDialogOpened){
						DownloadErrorDialog();
					}

				}else if(Tcp.MainDownoad_Completed){
					Tcp.MainDownoad_Completed=false;
					if(!isDialogOpened){
						DownloadSuccessDialog();
					}
				}
			}
		};
		downloadThread.start();

	}


	//sending database status command i.e to check whether data is there to download
	void getDBstatus()
	{
		Thread t=new Thread()
		{
			public void run(){
				//Tcp.tcpWrite(GET_DB_STATUS);
				byte cipher[]=null;
				try {
					cipher = encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),GET_DB_STATUS.getBytes());
				} catch (Exception e) {
					e.printStackTrace();
				}

				Tcp.tcpWrite(cipher,true);
				StaticVariables.printLog("TAG","Db status command sent");
			}
		};t.start();
	}

	// resetting all variables 
	void resetVariables(){
		isAlertshown=false;
		exceptionOccured=false;
		isServerAuntheticated=false;

	}


	//method to update the current task progress
	void updateProgress(final int progress){
		runOnUiThread(new Runnable() {
			public void run() {
				if(pdlg!=null && progress<=100){
					pdlg.setProgress(progress);	//setting current progress
				}
			}
		});

	}


	//progress dialog method to show progress dialog
	private void ProgressDialog(final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				if(pdlg==null){
					pdlg=new ProgressDialog(DownloadDatabse.this,ProgressDialog.THEME_HOLO_LIGHT);
					pdlg.setMessage(msg);
					pdlg.setIndeterminate(false);
					pdlg.setCancelable(false);
					pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
					pdlg.setProgress(0);
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

	//server error dialog 
	void serverErrorDialog(final String titleMsg,final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				DismissDialog();
				AlertDialog.Builder dialog=new AlertDialog.Builder(DownloadDatabse.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle(titleMsg);
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);
				dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.show();
			}
		});
	}

	//Error if database is empty and user is trying to download it
	void NoDatabaseErrorDialog()
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				DismissDialog();
				AlertDialog.Builder dialog=new AlertDialog.Builder(DownloadDatabse.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("House Not Configured");
				dialog.setMessage("House Is Not Configured Yet. Please Proceed  And Change Superadmin password to Configure the House Devices ");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
					}
				});
				dialog.setPositiveButton("Proceed", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// passing intent to user Selection class
					//	goBack();

						Intent it2=new Intent(DownloadDatabse.this,ChangeServerPassword.class);
						startActivity(it2);
						//adding transition to activity exit
						//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						finish();

					}
				});

				dialog.show();
			}
		});
	}

	//Download error dialog
	void DownloadErrorDialog()
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				DismissDialog();
				AlertDialog.Builder dialog=new AlertDialog.Builder(DownloadDatabse.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Download Failed");
				dialog.setMessage("Possible Reasons :\n 1.Download Timeout Occured. "
						+"\n 2.Unable to fetch Data from Server."
						+"\n 3.Data Integrity Failed."
						+"\n Please Try Downloading Again !");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);


				dialog.setPositiveButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.setNeutralButton("Exit",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
					}
				});


			/*	dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Tcp.tcpReadLineEnabled=true;
						Tcp.tcpDownloading=false;
						resetVariables();//resetting varaibles

						ConnectServer(); 	//starting connection and checking db status again

					}
				});*/

				dialog.show();
			}
		});
	}

	//showing success dialog on completing download
	void DownloadSuccessDialog()
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				DismissDialog();
				insertHouse();		// Inserting house details in local database
				//updateServerInfo(); // Updating Server Information

				AlertDialog.Builder dialog=new AlertDialog.Builder(DownloadDatabse.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Download Completed");
				//dialog.setMessage("Download Successfull.\nDo You Want To Operate House Devices?");
				dialog.setMessage("Download Successfull.\nDo You Want To Download Wireless Settings?");
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
					}
				}); 
				dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
/*
						//closing tcp connection 
						Tcp.tcpConnectionClose();

						Intent it=new Intent(DownloadDatabse.this,Main_Navigation_Activity.class);
						// adding transition to activity exit
						overridePendingTransition(R.anim.slideup, R.anim.slidedown);
						startActivity(it);
						finish();*/



						ConnectAndDownload_wireless();



						/*
						Intent it=new Intent(DownloadDatabse.this,ConfigurationType.class);
						startActivity(it);
						finish(); */
					}
				});	

				dialog.show();
			}
		});
	}


	//dismissing dialog
	void DismissDialog(){
		runOnUiThread(new Runnable() {

			@Override
			public void run() {
				if(pdlg!=null){
					pdlg.dismiss();		// Dismissing the progress dialog 
					pdlg=null;
				}
			}
		});
	}

	// Method to insert House Number  And House Name in Local Database
	private void insertHouse(){
		try{
			//getting current db name
			String House_Name=StaticVariables.HOUSE_NAME;
			StaticVariabes_div.HOUSE_NAME=StaticVariables.HOUSE_NAME;
			int HNO=localdb_helpr.fetchMaxHouseNo();

			//int HNO=localDB.fetchMaxHouseNo();	//fetching last max house no. from local database table
			StaticVariables.printLog("DB","Last Max HNO : "+HNO);

			ServerDetailsAdapter.OriginalDataBase =StaticVariables.HOUSE_NAME+".db";
			sdadap=new ServerDetailsAdapter(this);
			sdadap.open();
			Cursor cr=sdadap.fetchadminpass(StaticVariables.UserName_Used,StaticVariables.Password_Used);

			if(cr!=null){
				StaticVariables.Loggeduser_Used=cr.getString(cr.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
				Toast.makeText(this,"loggeduser"+StaticVariables.Loggeduser_Used,Toast.LENGTH_SHORT);

				/*if(StaticVariables.Loggeduser_Used!=null) {
				}*/

			}
            sdadap.close();

			byte cipher[]=null;
			try {
				cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),StaticVariables.Password_Used.getBytes());
			} catch (Exception e) {
				e.printStackTrace();
			}
			String Encryptd_password=new String(cipher);

			StaticVariables.printLog("Encryptd_password","D"+Encryptd_password);


			if(HNO==0)  {
				//insreting record in db first time
				localdb_helpr.insert_hp(1,House_Name,StaticVariables.UserName_Used,cipher,StaticVariables.Loggeduser_Used);
				//localdb_helpr.insert_hp(1,House_Name,StaticVariables.UserName_Used,StaticVariables.Password_Used,StaticVariables.Loggeduser_Used);

				StaticVariables.printLog("DB","First Time Record Insert");
			}
			else {
				//incrementing house number by 1
				int hnoGet=HNO+1;
				//inserting record in db next time
				localdb_helpr.insert_hp(hnoGet,House_Name,StaticVariables.UserName_Used,cipher,StaticVariables.Loggeduser_Used);
				//localdb_helpr.insert_hp(hnoGet,House_Name,StaticVariables.UserName_Used,StaticVariables.Password_Used,StaticVariables.Loggeduser_Used);
				StaticVariables.printLog("DB","Next Record Inserted");
			}

		}catch(Exception e){
			e.printStackTrace();
		}
	}


	//updating server table information
	private void updateServerInfo(){
		try{

			try{
				houseDB=new HouseConfigurationAdapter(this);
				houseDB.open();			//opening house database

			}catch(Exception e){
				e.printStackTrace();
			}

			//int port= StaticVariables.Port;
			int port =Tcp.tcpPort;

			try{
				boolean isUpdated=houseDB.updateServerPort_ServerTable(port); // updating server info
				if(isUpdated)
					StaticVariables.printLog("DB","server info updated");
			}catch(Exception e){
				e.printStackTrace();
			}

			houseDB.close();  //closing house database

		}catch(Exception e){
			e.printStackTrace();			
		}
	}


	//delay for ui thread
	void SleepUiThread(final long delay)
	{
		runOnUiThread(new  Runnable() {
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		});

	}

	//delay for normal threads
	void delay(long delay)
	{
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}		



	// Button click events	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.download_db:
		{ 
			uname=userName.getText().toString();
			upass=password.getText().toString();
			uhouse=houseName.getText().toString();

			boolean houseNameExists=false;
			boolean numeric=false,startswithnumber=false;
			//checking if  house name existing in Db or not
			if(uhouse!=null && uhouse.length()>0){

				//trimming leading and trailing space
				uhouse=uhouse.trim();

				houseNameExists=localdb_helpr.HouseNameExists(uhouse);

				numeric=uhouse.matches("[0-9]+");

				StaticVariables.printLog("TAG","numeric "+numeric);

				startswithnumber=Character.isDigit(uhouse.charAt(0));
			}




			//Data validation at client side
			if((uname==null || uname.length()==0 || uname.length()!=10) || (upass==null || upass.length()==0|| upass.length()!=8)
					|| (uhouse==null || uhouse.length()==0) || houseNameExists ||numeric||startswithnumber)
			{
				//username validations
				if(uname==null  || uname.length()==0)	{
					userName.setError("Please Enter UserName !");
				}else if(uname.length()!=10){
					userName.setError("UserName must be of 10 Characters!");
				}else{
					userName.setError(null);
					userName.setText(uname);
				}

				//user password validations
				if(upass==null || upass.length()==0){
					password.setError("Please Enter Password !");
				}else if(upass.length()!=8){
					password.setError("Password must be of 8 Characters!");
				}else{
					password.setError(null);
					password.setText(upass);
				}

				//house name validations
				if(uhouse==null || uhouse.length()==0){
					houseName.setError("Please Enter House Name !");
				}else if(houseNameExists){
					houseName.setError("House  Already Exists With This Name.Try Another Name!");
				}else if(numeric){
					houseName.setError("House Name Should be alpha numeric.Try Another Name!");
				}else if(startswithnumber){
					houseName.setError("House Name cant start with number.Try Another Name!");
				}else{
					houseName.setError(null);
					houseName.setText(uhouse);
				}

			}else {

				//removing error marks from text fields
				userName.setError(null);
				password.setError(null);
				houseName.setError(null);

				//if filled data is correct then starting download
				StaticVariables.HOUSE_NAME=uhouse;	//saving house name as static field
				//StaticVariables.HOUSE_DB_NAME=uhouse;
				StaticVariables.UserName_Used=uname;
				StaticVariables.Password_Used=upass;

				//StaticVariabes_div.loggeduser=uname;
				//StaticVariabes_div.loggedpwd=upass;

				SERVER_PASSWORD=uname+upass;		//setting username and passsword
				StaticVariabes_div.log("TAG",StaticVariables.HOUSE_NAME+" uhouse server password : "+SERVER_PASSWORD);
				resetVariables();				//resetting variables

				ConnectServer(); 	//starting connect and download task


			/*	new AsyncTask<Void, Void, Void>() {
					@Override
					protected Void doInBackground(Void... params) {

						ConnectServer();

						return null;
					}

					@Override
					protected void onPostExecute(Void aVoid) {

						super.onPostExecute(aVoid);
					}
				}.execute();*/



		}

			break;
		}	
		case R.id.clear:
		{
			userName.setText("");
			password.setText("");
			houseName.setText("");

			userName.setError(null);
			password.setError(null);
			houseName.setError(null);

			break;
		}
		case R.id.imageView2:{
			goBack();
			break;
		}
		}

	}	

/*
	//methods for hiding the soft input keyboard
	public static void hideSoftKeyboard(Activity activity) {
		InputMethodManager inputMethodManager = (InputMethodManager) activity
				.getSystemService(Activity.INPUT_METHOD_SERVICE);
		inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getWindowToken(), 0);

	}

	public  void setupUI(View view) 
	{
		// Set up touch listener for non-text box views to hide keyboard.
		if (!(view instanceof EditText)) 
		{

			view.setOnTouchListener(new OnTouchListener() 
			{

				public boolean onTouch(View v, MotionEvent event) 
				{
					hideSoftKeyboard(DownloadDatabse.this);

					return false;
				}

			});
		}
	}	
*/

	@Override
	public void onBackPressed() {
		goBack();
	}


	//activity return function
	void goBack(){
		/*//going back to activity which called this download activity
		Intent it=null;
		switch(activitynames.valueOf(ACTIVITY_NAME)){
		case UserSelection:{
			Tcp.tcpConnectionClose();
			it=new Intent(DownloadDatabse.this,Local_Remote_DownloadActivity.class);
			break;
		}
		case AdminPanel:{
			//it=new Intent(DownloadDatabse.this,AdminPanel.class);
			break;
		}
		case Remoteconndwn:{
			it=new Intent(DownloadDatabse.this,Local_Remote_DownloadActivity.class);
			break;
		}
		}
		startActivity(it);
		//adding transition to activity exit
		overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();	*/

		//closing tcp connection
		Tcp.tcpConnectionClose();

		Intent it=new Intent(DownloadDatabse.this,Main_Navigation_Activity.class);
		it.setFlags(it.FLAG_ACTIVITY_NEW_TASK | it.FLAG_ACTIVITY_CLEAR_TASK);
		// adding transition to activity exit
		//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		startActivity(it);
		finish();
	}

	@Override
	protected void onPause() { 
		super.onPause();
		/*if(LocalDatabaseAdapter.sdb.isOpen()){
			localDB.close();
			StaticVariables.printLog("TAG","DB CLOSED ");
		}*/
		if(LocalDatabaseHelper.sdb.isOpen()){
			localdb_helpr.close();
			StaticVariables.printLog("TAG","DB CLOSED ");
		}
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		/*if(!LocalDatabaseAdapter.sdb.isOpen()){
			localDB.opendb();
			StaticVariables.printLog("TAG","DB open ");
		}*/
		if(!LocalDatabaseHelper.sdb.isOpen()){
			localdb_helpr.opendb();
			StaticVariables.printLog("TAG","DB open ");
		}
	} 


	//enumeration class for actvity names
	private enum activitynames{
		UserSelection,AdminPanel,Remoteconndwn
	}

}

