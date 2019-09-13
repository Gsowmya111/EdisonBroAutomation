package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: ChangeServerPassword.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to Change superadmin password.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Base64;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Local_Remote_DownloadActivity;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.operatorsettings.DownloadHouseDb;

public class ChangeServerPassword extends Activity implements TcpTransfer,OnClickListener{

	CheckBox showPassword; 
	Button changePassword_btn ,clear_btn;
	EditText userName,oldPassword,newPassword,confirmPassword;
	private ProgressDialog pdlg;
	ImageView serverStatus;//,navigateBack;
	Tcp tcp=null;
	final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,TCP_CONNECTED=6,ERR_USER=7,MAX_USER=8;
	String SERVER_PASSWORD=null ;

	int server_online= R.drawable.connected;
	int server_offline=R.drawable.not_connected;

	String uname=null,oldPass=null,newPass=null,confirmPass=null;
	boolean exceptionOccured=false,isServerAuntheticated=false ;
	boolean IS_SERVER_CONNECTED=false,isDialogOpened=false,IS_SERVER_NOT_AUNTHETICATED=false;	

	static boolean isTcpConnecting=false;

	Button navigateBack,btnconstatus;
	int sl;
	boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
	boolean isPwdChanged=false;

	private static final String TAG1="ChangeServerPassword - ";


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

		setContentView(R.layout.change_suprusr_pwd); //setting activity layout
		setupUI(findViewById(R.id.ChangePassLayout));	//setting activity view to method for hiding keyboard

		//serverStatus=(ImageView) findViewById(R.id.server_status);
		navigateBack=(Button) findViewById(R.id.imageView2);
	//	navigateBack=(ImageView) findViewById(R.id.imageView2);
		btnconstatus=(Button) findViewById(R.id.btnconstatus);


		userName=(EditText) findViewById(R.id.server_username);
		oldPassword=(EditText) findViewById(R.id.server_old_pass);
		newPassword=(EditText) findViewById(R.id.server_new_pass);
		confirmPassword=(EditText) findViewById(R.id.server_confirm_pass);
		showPassword=(CheckBox) findViewById(R.id.showPassword);
		changePassword_btn=(Button) findViewById(R.id.change_pass);
		clear_btn=(Button) findViewById(R.id.clear);



		//setting onclick listener
		changePassword_btn.setOnClickListener(this);
		clear_btn.setOnClickListener(this);	
		btnconstatus.setOnClickListener(this);
		navigateBack.setOnClickListener(this);

		//setting on check listener for check box
		showPassword.setOnCheckedChangeListener(new OnCheckedChangeListener() {			
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if(isChecked){
					//showing password 
					oldPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					newPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
					confirmPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
				}else{
					//hiding password
					oldPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					newPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
					confirmPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
				}

			}
		});


		//calling connection in activity
	//	RegainingConnection();

		/*if(Tcp_con.isClientStarted){

			//  receiveddata(NetwrkType,StaticStatus.Network_Type,null);
			// receiveddata(ServStatus,StaticStatus.Server_status,null);

		}else{
			Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
			Tcp_con.registerReceivers(this.getApplicationContext());
		}*/

	}

	//method to get auto connection in class
	void RegainingConnection(){

		Thread thread =new Thread(){
			public void run(){
				isTcpConnecting=true;


				IS_SERVER_CONNECTED=Tcp.EstablishConnection(TcpHandler);
				runOnUiThread(new Runnable() {
					public void run() 
					{
						if(IS_SERVER_CONNECTED){

							IS_SERVER_CONNECTED=false; 

							if(!IS_SERVER_NOT_AUNTHETICATED){

								IS_SERVER_NOT_AUNTHETICATED=false;
								//serverStatus.setImageResource(server_online);
								btnconstatus.setBackgroundResource(server_online);
								StaticVariables.printLog("TAG","CONNECTED");

							}else{

								IS_SERVER_NOT_AUNTHETICATED=false;

								//serverStatus.setImageResource(server_offline);
								btnconstatus.setBackgroundResource(server_offline);
								StaticVariables.printLog("TAG","NOT CONNECTED");

							}

						}else{

							//serverStatus.setImageResource(server_offline);
							btnconstatus.setBackgroundResource(server_offline);
							StaticVariables.printLog("TAG","NOT CONNECTED");

						}
						isTcpConnecting=false;
					}
				});

			}
		};thread.start();
	}

	//handler for handling server responses
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



				if( Data!=null && Data.startsWith("&") && Data.endsWith("@")){
					// Success : &500A@ or &500U@  Error : &005@
					String result=Data.substring(1,Data.length()-1);


					if(result.equals("110S")){
						isPwdChanged=true;
						// setting username and password in static fields
						//StaticVariables.UserName=uname;
						//StaticVariables.Password=newPass;

						StaticVariabes_div.loggeduser=uname;
						StaticVariabes_div.loggedpwd=newPass;

						//StaticVariables.HOUSE_NAME=uhouse;	//saving house name as static field
						StaticVariables.UserName_Used=uname;
						StaticVariables.Password_Used=newPass;
						if(!isDialogOpened){
							ChangePasswordSuccessDialog("A","Password Changed Successfully.Continue To Configure House?");
						}
					}else if(result.equals("500U")){
						isPwdChanged=true;
						if(!isDialogOpened){
							ChangePasswordSuccessDialog("U","Password Changed Successfully.\nPlease Go Back And Login As Admin to Configure House.");
						}
					}						
					else if(result.equals("005")){
						isPwdChanged=false;
						if(!isDialogOpened){
							ChangePasswordErrorDialog();
						}
					}
				}else{
					isPwdChanged=false;
				}


				break;
			}

			case READ_BYTE:	{

				break;
			}

			case EXCEPTION:
			{
				final String Data=(String) msg.obj;
				StaticVariables.printLog("TCP RESPONSE","DATA GET FROM TCP SOCKET :"+Data);
				exceptionOccured=true;
				break;
			}

			case TCP_CONNECTED:{
				runOnUiThread(new Runnable() {
					public void run() 
					{
					//	serverStatus.setImageResource(server_online);
						btnconstatus.setBackgroundResource(server_online);
					}
				});
				break;
			}
			case TCP_LOST:
			{
				runOnUiThread(new Runnable() {
					public void run() 
					{
						//serverStatus.setImageResource(server_offline);
						btnconstatus.setBackgroundResource(server_offline);
						//dismissig progress dialog
						dismissProgressDialog();

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
						//serverStatus.setImageResource(server_offline);
						btnconstatus.setBackgroundResource(server_offline);
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
						//serverStatus.setImageResource(server_offline);
						btnconstatus.setBackgroundResource(server_offline);
						TCPErrorAlert("Server Error","Server connections Limit Exceeded .Please disconnect some other client to connect with server.");

					}
				});
				break;
			}
			}
		}
	};




	/*public void receiveddata(int msg,String strdata,byte[] bytestatus){

		switch (msg) {
			case READ_BYTE:
				byte[] readBuf = bytestatus;
				// DataIn(readBuf);
				final String readMessage = new String(readBuf, 0,readBuf.length);
				StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);

				//DataIn(readBuf);

				break;
			case READ_LINE:
				//  readMessage2 = (String) msg.obj;
				StaticVariabes_div.log("msg read A_s" + strdata, TAG1);


				//readMessage2 =data;
				final String Data=strdata;
				StaticVariables.printLog("TCP RESPONSE","DATA GET FROM TCP SOCKET :"+Data);

				if( Data!=null && Data.startsWith("&") && Data.endsWith("@")){
					// Success : &500A@ or &500U@  Error : &005@
					String result=Data.substring(1,Data.length()-1);
					if(result.equals("110")){
						// setting username and password in static fields
						StaticVariables.UserName=uname;
						StaticVariables.Password=newPass;
						if(!isDialogOpened){
							ChangePasswordSuccessDialog("A","Password Chnaged Successfully.Continue To Configure House?");
						}
					}else if(result.equals("500U")){
						if(!isDialogOpened){
							ChangePasswordSuccessDialog("U","Password Chnaged Successfully.\nPlease Go Back And Login As Admin to Configure House.");
						}
					}
					else if(result.equals("011")){
						if(!isDialogOpened){
							ChangePasswordErrorDialog();
						}
					}
				}

				if(strdata.equals("*OK#")){
					// runOnUiThread(new Runnable() {
					//     public void run() {
					serv_status(true);

					//    }
					// });
				}

				break;
			case ServStatus:
				//final String ServerStatusB = (String) msg.obj;
				final String ServerStatusB =data;
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
				//runOnUiThread(new Runnable() {
				//    public void run() {
				serv_status(statusserv);
				//  }
				// });
				break;


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

	// Error Alert showing that tcp is Off
	private void TCPErrorAlert(final String title,final String msg) {
		runOnUiThread(new Runnable() {
			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {

				//dismissig progress dialog
				dismissProgressDialog();

				AlertDialog.Builder dlg = new AlertDialog.Builder(ChangeServerPassword.this,AlertDialog.THEME_HOLO_DARK);
				dlg.setTitle(title);
				dlg.setMessage(msg);
				dlg.setCancelable(true);
				dlg.setCancelable(true);
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

	//connection thread
	private void ConnectToServer(){
		Thread connectionThread=new Thread(){
			public void run(){
				runOnUiThread(new Runnable() {
					public void run() {
						//starting progress bar 
						ProgressDialog("Changing SuperAdmin Password...");
					}
				});

				int progress=0, i=0;
				boolean serverConnected=false;
				byte[] cipher=null;

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
								cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),SERVER_PASSWORD.getBytes());
							} catch (Exception e) {
								e.printStackTrace();
							}

							Tcp.tcpWrite(cipher,true);
							delay(1000);	//giving delay of 1 sec
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
						updateProgress(progress+=20); //Incrementing progress
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
					StaticVariables.printLog("TAG","server already connected");
				}

				if(serverConnected)	{
					runOnUiThread(new Runnable() {
						public void run() {
							//serverStatus.setImageResource(server_online);
							btnconstatus.setBackgroundResource(server_online);//showing server is connected
						}
					});

					changePassword(); //sending change password  request with necessary data

					int counter1=1;
					while(counter1<=10)
					{
						counter1++;
						int counter2=1;
						while(counter2<10)
						{
							if(isPwdChanged){
								counter1=20;
								break;
							}else{
								delay(100);
							}

							counter2++;
						}
						StaticVariables.printLog("DB","db status wait count : "+counter1);
					}


					if(counter1<20 && !isPwdChanged){
						isPwdChanged=false;
						runOnUiThread(new Runnable() {
							public void run() {
								updateProgress(100); //setting progress to 100
								delay(100);	//delay of 100 ms

								//dismissig progress dialog
								dismissProgressDialog();


							}
						});


						if(!isDialogOpened){
							//serverErrorDialog("Password not changed "," Try Again");
							runOnUiThread(new Runnable() {
											  public void run() {
							Toast.makeText(ChangeServerPassword.this,"Password not changed  Try Again",Toast.LENGTH_SHORT).show();
											  }
							});
											  }
					}


					StaticVariables.printLog("METHOD RESULT","server connected");
				}else {

					runOnUiThread(new Runnable() {
						public void run() {
							updateProgress(100); //setting progress to 100
							delay(100);	//delay of 100 ms

							//dismissig progress dialog
							dismissProgressDialog();


						}
					});

					if(exceptionOccured){
						Tcp.tcpConnectionClose();	//closing tcp connection
						if(!isDialogOpened){
							serverErrorDialog("Server UnReachable",
									"Please Check Your WiFi Settings And Check Whether Server is ON");
						}

					}



					exceptionOccured=false;
					isServerAuntheticated=false;
				}
			}
		};
		connectionThread.start();

	}

	//creating progress bar 
	private void ProgressDialog(final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() {
				if(pdlg==null){
					pdlg=new ProgressDialog(ChangeServerPassword.this,ProgressDialog.THEME_HOLO_LIGHT);
					pdlg.setMessage(msg);
					pdlg.setIndeterminate(false);
					pdlg.setCancelable(true);
					pdlg.setCanceledOnTouchOutside(false);
					pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
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

	//Error dialog if password not changed
	void ChangePasswordErrorDialog()
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				//dismissig progress dialog
				dismissProgressDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(ChangeServerPassword.this, AlertDialog.THEME_HOLO_DARK);
				dialog.setTitle("Password Not Changed");
				dialog.setMessage("Please Ensure That username/password/old password\nFields Are Correct And Try Again!");
				dialog.setIcon(android.R.drawable.ic_dialog_alert);
				dialog.setCancelable(false);

				dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
				dialog.setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						ConnectToServer();	//again trying to change password						
					}
				});

				dialog.show();
			}
		});
	}	

	//dismiss dialog
	void dismissProgressDialog(){

		if(pdlg!=null){
			pdlg.setProgress(100);
			delay(100);
			pdlg.dismiss();		// Dismissing the progress dialog 
			pdlg=null;
		}
	}

	//success dialog if password changed
	void ChangePasswordSuccessDialog(final String UserType,final String msg)
	{
		runOnUiThread(new Runnable() {

			@TargetApi(Build.VERSION_CODES.HONEYCOMB) 
			@Override
			public void run() 
			{
				//dismissig progress dialog
				dismissProgressDialog();

				AlertDialog.Builder dialog=new AlertDialog.Builder(ChangeServerPassword.this, AlertDialog.THEME_HOLO_LIGHT);
				dialog.setTitle("Password Changed");
				dialog.setMessage(msg);
				dialog.setIcon(android.R.drawable.ic_dialog_info);
				dialog.setCancelable(false);

				dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});

				dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();	
						if(UserType.equals("A")){
							Intent it=new Intent(ChangeServerPassword.this,DownloadHouseDb.class);
							startActivity(it);
							//adding transition to activity exit
							overridePendingTransition(R.anim.slideup, R.anim.slidedown);
							finish();
						}else if(UserType.equals("U")){
							Tcp.tcpConnectionClose(); //closing tcp connection
							Intent it=new Intent(ChangeServerPassword.this,Main_Navigation_Activity.class);
							startActivity(it);
							//adding transition to activity exit
							overridePendingTransition(R.anim.slideup, R.anim.slidedown);
							finish();
						}

					}
				});

				dialog.show();
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
				AlertDialog.Builder dialog=new AlertDialog.Builder(ChangeServerPassword.this, AlertDialog.THEME_HOLO_DARK);
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

	//method to send data to change passsword
	void changePassword()
	{
		if(Tcp.tcpConnected){
			Thread t=new Thread()
			{
				public void run()
				{
					// creating change password data format
					//String changePasswordData="<"+uname+oldPass+newPass+"%";
					String changePasswordData=uname+oldPass+newPass;
					StaticVariables.printLog("PassWord Data","Password Data : "+changePasswordData);
					//Tcp.tcpWrite(changePasswordData);	//sending change password command
					//Send_Timer_dat(changePasswordData,"<","%");
					Send_Timer_dat_aes(changePasswordData,"<","%");

				}
			};t.start();
		}else{
			runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "Server Not Connectd!", Toast.LENGTH_SHORT).show();
				}
			});

		}
	}


	public void Send_Timer_dat(String StrTimer,String starttokn,String endtokn){

		StaticVariabes_div.log(StrTimer.length()+"TimerData"+StrTimer,TAG1);

		//  byte[]  cipher = xorWithKey(StrTimer.getBytes(), encryptionKey.getBytes());
		byte[] encodeValue = Base64.encode(StrTimer.getBytes(), Base64.DEFAULT);

		String tosend=new String(encodeValue);
		tosend=starttokn+tosend+endtokn;

		tosend.replaceAll(" ","");
		String temp_str=tosend.replaceAll("\n","");

		StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);

		//Tcp_con.WriteString(temp_str);
		 Tcp.tcpWrite(temp_str);
		//Tcp_con.WriteBytes(temp_str.getBytes());
	}


	public void Send_Timer_dat_aes(String StrTimer,String starttokn,String endtokn){

		StaticVariabes_div.log(StrTimer.length()+"TimerData"+StrTimer,TAG1);
		String tosend=null;
		byte cipher[]=null;

		tosend=starttokn+StrTimer+endtokn;
		tosend.replaceAll(" ","");
		String temp_str=tosend.replaceAll("\n","");

		StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);

		try {
			cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),temp_str.getBytes());
		} catch (Exception e) {
			e.printStackTrace();
		}

		Tcp.tcpWrite(cipher,true);

	}
	//method to provide delay 
	void delay(long delay)
	{
		try {
			Thread.sleep(delay);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}


	// Click Events	
	@Override
	public void onClick(View v) {
		switch(v.getId())
		{
		case R.id.change_pass:
		{	
			uname=userName.getText().toString();
			oldPass=oldPassword.getText().toString();
			newPass=newPassword.getText().toString();
			confirmPass=confirmPassword.getText().toString();

			if((uname==null || uname.length()==0  || uname.length()!=10)
					|| (oldPass==null || oldPass.length()==0|| oldPass.length()!=8)
					|| (newPass==null || newPass.length()==0 || newPass.length()!=8) 
					||(confirmPass==null || confirmPass.length()==0) ||confirmPass.length()!=8 || !confirmPass.equals(newPass) )
			{

				if(uname==null  || uname.length()==0 )	{
					userName.setError("Please Enter UserName !");
				}else if(uname.length()!=10){
					userName.setError("UserName must be of 10 Characters!");
				}else{
					userName.setError(null);
					userName.setText(uname);
				}


				if(oldPass==null || oldPass.length()==0){
					oldPassword.setError("Please Enter Old Password !");
				}else if(oldPass.length()!=8){
					oldPassword.setError("Password must be of 8 Characters!");
				}else{
					oldPassword.setError(null);
					oldPassword.setText(oldPass);
				}


				if(newPass==null || newPass.length()==0){
					newPassword.setError("Please Enter New Password !");
				}else if(newPass.length()!=8){
					newPassword.setError("Password must be of 8 Characters!");
				}else{
					newPassword.setError(null);
					newPassword.setText(newPass);
				}

				if(confirmPass==null || confirmPass.length()==0){
					confirmPassword.setError("Please Enter Confirm Password !");
				}else if(confirmPass.length()!=8){
					confirmPassword.setError("Password must be of 8 Characters!");
				}else if(!confirmPass.equals(newPass)){
					confirmPassword.setError("Confirm Password Not Matched With New Password!");
				}else{
					confirmPassword.setError(null);
					confirmPassword.setText(confirmPass);
				}

			}
			else {						
				// clearing error marks from Text fields
				userName.setError(null);
				oldPassword.setError(null);
				newPassword.setError(null);
				confirmPassword.setError(null);

				// setting user name and password for authentication and starting connection 
				SERVER_PASSWORD=uname+oldPass; 
				StaticVariables.printLog("SERVER_PASSWORD","SERVER PASSWORD : "+SERVER_PASSWORD);

				//StaticVariables.Host=StaticVariables.Host_udp;
				//StaticVariables.Port=StaticVariables.Port_udp;

				//starting connection task
				ConnectToServer();
			}

			break;
		}

		case R.id.clear:
		{
			// clearing error marks from Text fields
			userName.setText("");
			oldPassword.setText("");
			newPassword.setText("");
			confirmPassword.setText("");

			// clearing error marks from Text fields
			userName.setError(null);
			oldPassword.setError(null);
			newPassword.setError(null);
			confirmPassword.setError(null);
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
					hideSoftKeyboard(ChangeServerPassword.this);

					return false;
				}

			});
		}
	}			

	//go back
	void goPrevious(){
		//closing Tcp Connection 
		Tcp.tcpConnectionClose();

		//going back to admin page
		//Intent it=new Intent(ChangeServerPassword.this,AdminPanel.class);
		Intent it=new Intent(ChangeServerPassword.this,Local_Remote_DownloadActivity.class);
		startActivity(it);
		//adding transition to activity exit
		//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		finish();
	}

	//Back Press Event
	@Override
	public void onBackPressed() {
		goPrevious();
	}

	@Override
	public void read(int type, String stringData, byte[] byteData) {

	}
}
