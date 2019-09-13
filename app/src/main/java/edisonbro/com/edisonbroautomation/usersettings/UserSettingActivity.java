package edisonbro.com.edisonbroautomation.usersettings;

/**
 *  FILENAME: UserSettingActivity.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity for user to provide options like change password ,change account ,download and delete home.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;



import edisonbro.com.edisonbroautomation.AppLockActivity;
import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;

import edisonbro.com.edisonbroautomation.Local_Remote_DownloadActivity;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.SplashScreen;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp_dwn_config;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.EditRoomName;
import edisonbro.com.edisonbroautomation.operatorsettings.OperatorSettingsMain;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;


public class UserSettingActivity extends Activity implements TcpTransfer {
    Button btn_pass,btn_app_pass,btn_cancel,btn_back,btn_change_account,btn_setemail,b_home,b_dwnldhome_updates;

    //***************************************************************
    String houseno,oldPass,newPass,confirmPass;
    String oldappPass,newappPass,confirmappPass,newappuser;
    //***********************************************
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
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //**********************************************************8
    Button btnconstatus,btsig;
    LocalDatabaseHelper db=null;
    boolean isValid=false;
    //*********************************************************
    CheckBox cb_applock;
    EditText et_emailid;
    LocalListArrangeTable locallist_adap;
    private static final String TAG1="UserSetting - ";
    String Prevemail,logintype;
    private ServerDetailsAdapter sdadap;
    private static SwbAdapter swbadap;
    Button b_delhom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
       // setContentView(R.layout.activity_usersett_final);
        setContentView(R.layout.activity_user_setting);

        btnconstatus=(Button) findViewById(R.id.btnconstatus);
        btsig=(Button) findViewById(R.id.btnsignal);

        b_dwnldhome_updates = (Button)findViewById(R.id.btn_downloadupdates);
        btn_pass = (Button)findViewById(R.id.btn_user_paswd);
        btn_app_pass = (Button)findViewById(R.id.btn_app_paswd);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_back  = (Button)findViewById(R.id.btnback);
        cb_applock= (CheckBox) findViewById(R.id.cb_applock);
        btn_change_account=(Button)findViewById(R.id.btn_signin_difact);
        btn_setemail= (Button) findViewById(R.id.btn_saveemail);
        et_emailid= (EditText) findViewById(R.id.ed_email);
        et_emailid.setVisibility(View.INVISIBLE);
        btn_setemail.setVisibility(View.INVISIBLE);
        b_delhom= (Button) findViewById(R.id.btndelhome);

        try{
            swbadap=new SwbAdapter(this);
              sdadap=new ServerDetailsAdapter(this);
            db=new LocalDatabaseHelper(this);
        }catch(Exception e){
            e.printStackTrace();
        }
        StaticVariables.udp_use=false;

        if((StaticVariabes_div.housename==null)||(StaticVariabes_div.housename=="")||(StaticVariabes_div.housename.equals("")) ){

            popup_dwn("Please Download House First");

        }else {


            db.opendb();
            Cursor cursd = db.fetch_logintype(StaticVariabes_div.housenumber);

            if (cursd != null)
                logintype = cursd.getString(cursd.getColumnIndexOrThrow(LocalDatabaseHelper.LOGIN_ACCESS));
            db.close();


            if (logintype.equals("M")) {

                popup_loginM("Please Login To Access");

            } else {

                Tcp_con mTcp = new Tcp_con(this);


                if (Tcp_con.isClientStarted) {

                    //  receiveddata(NetwrkType,StaticStatus.Network_Type,null);
                    //  receiveddata(ServStatus,StaticStatus.Server_status,null);

                } else {
                    Tcp_con.stacontxt = UserSettingActivity.this;
                    Tcp_con.serverdetailsfetch(UserSettingActivity.this, StaticVariabes_div.housename);
                    Tcp_con.registerReceivers(UserSettingActivity.this);
                }
            }

        }


        db.opendb();
        int applockcount = db.getCount_Applock();

        if(applockcount>0) {
            Cursor cursdb = db.fetch_Applockdetails(1);
            String isApplock = "";

            if (cursdb != null) {
                isApplock = cursdb.getString(cursdb.getColumnIndexOrThrow(LocalDatabaseHelper.VALUE));
                cursdb.close();
                StaticVariabes_div.log("isapplock" + isApplock, TAG1);

                if (isApplock.equals("true")) {
                    cb_applock.setChecked(true);
                    et_emailid.setVisibility(View.VISIBLE);
                    btn_setemail.setVisibility(View.VISIBLE);
                    Cursor cursemail = db.fetch_Applockdetails(3);
                    Prevemail = cursemail.getString(cursdb.getColumnIndexOrThrow(LocalDatabaseHelper.VALUE));
                    et_emailid.setText(Prevemail);
                    cursemail.close();
                } else {
                    cb_applock.setChecked(false);
                }
            }
        }

        db.close();

        b_home= (Button) findViewById(R.id.btnhome);
        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(UserSettingActivity.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });

        b_dwnldhome_updates.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Tcp_con.stopClient();
                Tcp_con.isClientStarted=false;


                Tcp_dwn_config.tcpHost=Tcp_con.tcpAddress;
                Tcp_dwn_config.tcpPort=Tcp_con.tcpPort;


                Intent intt=new Intent(UserSettingActivity.this,UpdateHome_Existing.class);
                intt.putExtra("isusersett","usersett");
                startActivity(intt);
            }
        });

        b_delhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupdelete();

            }
        });

        cb_applock.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                db.opendb();
                if(cb_applock.isChecked()){

                    et_emailid.setVisibility(View.VISIBLE);
                    btn_setemail.setVisibility(View.VISIBLE);


                } else{
                    et_emailid.setVisibility(View.INVISIBLE);
                    btn_setemail.setVisibility(View.INVISIBLE);
                    db.updateAppLockEna(1, "false");
                    Toast.makeText(UserSettingActivity.this, "Deactivated App Lock", Toast.LENGTH_SHORT).show();
                }

                  db.close();
            }
        });

        btn_setemail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* db.opendb();
                validateemail(et_emailid.getText().toString());

                if(isValid) {
                    db.updateAppLockEna(1, "true");
                    db.updateAppLockEna(3,et_emailid.getText().toString());
                    Toast.makeText(UserSettingActivity.this, "Activated App Lock", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(UserSettingActivity.this, "Email InValid Deactivated App Lock", Toast.LENGTH_SHORT).show();
                    db.updateAppLockEna(1, "false");
                    db.updateAppLockEna(3,"NoEmail");
                    cb_applock.setChecked(false);
                }
                db.close();*/
            }
        });

        btn_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeusr_pwd();
                //alertDialog.dismiss();
                //   alertDialog.setCancelable(true);
            }
        });

        btn_app_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeapplock_pwd();
                //alertDialog.dismiss();
                //   alertDialog.setCancelable(true);
            }
        });

        btn_change_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Change_Logged_Account();
            }
        });

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = getIntent();
                setResult((Activity.RESULT_OK), i);
                finish();
            }
        });

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               Intent intnt=new Intent(UserSettingActivity.this,Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });
    }

    public void popupdelete(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Delete House").setCancelable(false)

                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        boolean homedel=false;

                        try {
                            swbadap.open();
                           homedel = swbadap.deleteDatabase(StaticVariabes_div.housename + ".db");
                            swbadap.close();
                        }catch(NullPointerException ne){

                        }
                        if(homedel){
                            //  pre_adap.open();
                            db.opendb();

                            if(StaticVariabes_div.housename!=null){
                                boolean locwirelessdb=swbadap.deleteDatabase(StaticVariabes_div.housename+"_WLS.db");
                                StaticVariabes_div.log("locwirelessdb"+locwirelessdb, TAG1);
                                // boolean lochomelist=db.delete(Integer.parseInt(houseno));
                                boolean lochomelist=db.delete_hom(StaticVariabes_div.housename);
                                StaticVariabes_div.log("lochomelist"+lochomelist, TAG1);
                                //@@@@@                         //  pre_adap.deletevalforhouseno(houseno);
                                popuphomedeleted("house deleted");
                            }else{
                                popup("house not deleted,try Again");
                            }
                            db.close();
                            //    pre_adap.close();

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
    }

    public void popuphomedeleted(String msg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage(msg).setCancelable(false)

                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        StaticVariabes_div.myStringArr_Pos=0;
                        Intent i=new Intent(UserSettingActivity.this,Main_Navigation_Activity.class);
                        startActivity(i);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    private void popup_dwn(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingActivity.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(UserSettingActivity.this);
                applnnameinfo.setTextSize(20);
                alert.setView(applnnameinfo);
                applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                Intent intnt=new Intent(UserSettingActivity.this, Main_Navigation_Activity.class);
                                startActivity(intnt);
                                finish();
                            }
                        });
                alert.show();
            }
        });

    }
    private void popup_loginM(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingActivity.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(UserSettingActivity.this);
                applnnameinfo.setTextSize(20);
                alert.setView(applnnameinfo);
                applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intnt=new Intent(UserSettingActivity.this, Main_Navigation_Activity.class);
                                startActivity(intnt);
                                finish();
                            }
                        });
                alert.show();
            }
        });

    }
/*    public void validateemail(String email){
        try {
            // Create InternetAddress object and validated the supplied
            // address which is this case is an email address.
            InternetAddress internetAddress = new InternetAddress(email);
            internetAddress.validate();
            isValid = true;
            Toast.makeText(this, "Valid", Toast.LENGTH_SHORT).show();
        } catch (AddressException e) {
            Toast.makeText(this, "Email Id Not Valid", Toast.LENGTH_SHORT).show();

            e.printStackTrace();
        }
    }*/

    public  void changeusr_pwd(){
        LayoutInflater inflater = LayoutInflater.from(UserSettingActivity.this);
        View subView = inflater.inflate(R.layout.popup_user_pwd, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Button btn_cancel= (Button) subView.findViewById(R.id.buton_cancel);
        final Button btn_ok= (Button) subView.findViewById(R.id.buton_ok);
        final EditText et_oldpwd = (EditText) subView.findViewById(R.id.et_oldpass);
        final EditText et_newpwd = (EditText) subView.findViewById(R.id.et_newpass);
        final EditText et_confpwd = (EditText) subView.findViewById(R.id.et_conpass);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //   alertDialog.setCancelable(true);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                oldPass=et_oldpwd.getText().toString();
                newPass=et_newpwd.getText().toString();
                confirmPass=et_confpwd.getText().toString();


                if((oldPass==null || oldPass.length()==0|| oldPass.length()!=8)
                        || (newPass==null || newPass.length()==0 || newPass.length()!=8)
                        ||(confirmPass==null || confirmPass.length()==0) ||confirmPass.length()!=8 || !confirmPass.equals(newPass) )
                {

                    //changeusr_pwd();

                    if(oldPass==null || oldPass.length()==0){
                        et_oldpwd.setError("Please Enter Old Password !");
                    }else if(oldPass.length()!=8){
                        et_oldpwd.setError("Password must be of 8 Characters!");
                    }else{
                        et_oldpwd.setError(null);
                        et_oldpwd.setText(oldPass);
                    }


                    if(newPass==null || newPass.length()==0){
                        et_newpwd.setError("Please Enter New Password !");
                    }else if(newPass.length()!=8){
                        et_newpwd.setError("Password must be of 8 Characters!");
                    }else{
                        et_newpwd.setError(null);
                        et_newpwd.setText(newPass);
                    }

                    if(confirmPass==null || confirmPass.length()==0){
                        et_confpwd.setError("Please Enter Confirm Password !");
                    }else if(confirmPass.length()!=8){
                        et_confpwd.setError("Password must be of 8 Characters!");
                    }else if(!confirmPass.equals(newPass)){
                        et_confpwd.setError("Confirm Password Not Matched With New Password!");
                    }else{
                        et_confpwd.setError(null);
                        et_confpwd.setText(confirmPass);
                    }

                }
                else {
                    // clearing error marks from Text fields
                    et_oldpwd.setError(null);
                    et_newpwd.setError(null);
                    et_confpwd.setError(null);
                    StaticVariabes_div.log("noerror" + " msg", TAG1);

                    if(Tcp_con.isClientStarted){
                        StaticVariabes_div.log("msg sent" + "<"+ StaticVariabes_div.loggeduser+oldPass+newPass+"%\r\n" + " msg", TAG1);
                    //    Tcp_con.WriteBytes(("<"+ StaticVariabes_div.loggeduser+oldPass+newPass+"%\r\n").getBytes());
                        Send_Timer_dat_aes(StaticVariabes_div.loggeduser+oldPass+newPass,"<","%");
                    }else{

                        Toast.makeText(UserSettingActivity.this,"Gateway Not Connected",Toast.LENGTH_SHORT).show();

                            Tcp_con.stacontxt = UserSettingActivity.this;
                            Tcp_con.serverdetailsfetch(UserSettingActivity.this,StaticVariabes_div.housename);
                            Tcp_con.registerReceivers(UserSettingActivity.this);
                        if(Tcp_con.isClientStarted) {

                            //Tcp_con.WriteBytes(("<" + StaticVariabes_div.loggeduser + oldPass + newPass + "%\r\n").getBytes());
                            Send_Timer_dat_aes(StaticVariabes_div.loggeduser+oldPass+newPass,"<","%");


                        }
                    }
                    alertDialog.dismiss();
                }


                //   alertDialog.setCancelable(true);
            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.75);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.show();

    }

    public void Send_Timer_dat_aes(String StrTimer,String starttokn,String endtokn){

        String tosend=null;
        tosend=starttokn+StrTimer+endtokn;

        tosend.replaceAll(" ","");
       final String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);

        Thread t = new Thread() {
            public void run() {

                Tcp_con.WriteString(temp_str);
            }

        };
        t.start();

    }
   /* public void Send_Timer_dat(String StrTimer,String starttokn,String endtokn){

        StaticVariabes_div.log(StrTimer.length()+"TimerData"+StrTimer,TAG1);

        //  byte[]  cipher = xorWithKey(StrTimer.getBytes(), encryptionKey.getBytes());
        byte[] encodeValue = Base64.encode(StrTimer.getBytes(), Base64.DEFAULT);

        String tosend=new String(encodeValue);
        tosend=starttokn+tosend+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);

        Tcp_con.WriteBytes(temp_str.getBytes());
    }*/

    public  void changeapplock_pwd(){

        LayoutInflater inflater = LayoutInflater.from(UserSettingActivity.this);
        View subView = inflater.inflate(R.layout.popup_user_sett_app_pwd, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Button btn_cancel= (Button) subView.findViewById(R.id.buton_cancel);
        final Button btn_ok= (Button) subView.findViewById(R.id.buton_ok);
        final EditText et_oldpwd = (EditText) subView.findViewById(R.id.et_oldpass);
        final EditText et_newpwd = (EditText) subView.findViewById(R.id.et_newpass);
        final EditText et_confpwd = (EditText) subView.findViewById(R.id.et_conpass);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();


        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //   alertDialog.setCancelable(true);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                oldappPass = et_oldpwd.getText().toString();
                newappPass = et_newpwd.getText().toString();
                confirmappPass = et_confpwd.getText().toString();


                if ((oldappPass == null || oldappPass.length() == 0 || oldappPass.length() != 4)
                        || (newappPass == null || newappPass.length() == 0 || newappPass.length() != 4)
                        || (confirmappPass == null || confirmappPass.length() == 0) || confirmappPass.length() != 4 || !confirmappPass.equals(newappPass)) {

                   // showpopApppass();

                    if (oldappPass == null || oldappPass.length() == 0) {
                        et_oldpwd.setError("Please Enter Old Password !");
                    } else if (oldappPass.length() != 4) {
                        et_oldpwd.setError("Password must be of 4 Characters!");
                    } else {
                        et_oldpwd.setError(null);
                        et_oldpwd.setText(oldappPass);
                    }


                    if (newappPass == null || newappPass.length() == 0) {
                        et_newpwd.setError("Please Enter New Password !");
                    } else if (newappPass.length() != 4) {
                        et_newpwd.setError("Password must be of 4 Characters!");
                    } else {
                        et_newpwd.setError(null);
                        et_newpwd.setText(newappPass);
                    }

                    if (confirmappPass == null || confirmappPass.length() == 0) {
                        et_confpwd.setError("Please Enter Confirm Password !");
                    } else if (confirmappPass.length() != 4) {
                        et_confpwd.setError("Password must be of 4 Characters!");
                    } else if (!confirmappPass.equals(newappPass)) {
                        et_confpwd.setError("Confirm Password Not Matched With New Password!");
                    } else {
                        et_confpwd.setError(null);
                        et_confpwd.setText(confirmappPass);
                    }

                } else {
                    // clearing error marks from Text fields
                    et_oldpwd.setError(null);
                    et_newpwd.setError(null);
                    et_confpwd.setError(null);

                    db.opendb();
                    Cursor curs = db.fetch_Applockdetails(2);
                    String oldpwd = "";
                    if (curs != null) {
                        oldpwd = curs.getString(curs.getColumnIndexOrThrow(LocalDatabaseHelper.VALUE));

                        if (oldappPass.equals(oldpwd)) {
                            StaticVariabes_div.log("newappps" + newappPass, TAG1);

                            boolean isupdate = db.updateApplockPwd(2, newappPass);
                            StaticVariabes_div.log("isupdate" + isupdate, TAG1);

                            Toast.makeText(UserSettingActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(UserSettingActivity.this, "Old password Not Correct", Toast.LENGTH_SHORT).show();
                        }
                    }
                    db.close();
                    alertDialog.dismiss();
                    //   alertDialog.setCancelable(true);
                }
            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.75);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.show();

    }

    public  void Change_Logged_Account(){

        LayoutInflater inflater = LayoutInflater.from(UserSettingActivity.this);
        View subView = inflater.inflate(R.layout.popup_change_account, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final Button btn_cancel= (Button) subView.findViewById(R.id.buton_cancel);
        final Button btn_ok= (Button) subView.findViewById(R.id.buton_ok);
        final TextView tv_usernam = (TextView) subView.findViewById(R.id.loggedusr);
        final EditText et_usernam = (EditText) subView.findViewById(R.id.et_newuser);
        final EditText et_pwd= (EditText) subView.findViewById(R.id.et_pass);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();


        tv_usernam.setText(StaticVariabes_div.loggeduser);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
                //   alertDialog.setCancelable(true);
            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //oldappPass = et_oldpwd.getText().toString();
                newappuser = et_usernam.getText().toString();
                newappPass = et_pwd.getText().toString();


                if ((newappuser == null || newappuser.length() == 0 || newappuser.length() != 10)
                        || (newappPass == null || newappPass.length() == 0) || newappPass.length() != 8) {

                    // showpopApppass();


                    if (newappuser == null || newappuser.length() == 0) {
                        et_usernam.setError("Please Enter New UserName !");
                    } else if (newappuser.length() != 10) {
                        et_usernam.setError("UserName must be of 10 Characters!");
                    } else {
                        et_usernam.setError(null);
                        et_usernam.setText(newappuser);
                    }

                    if (newappPass == null || newappPass.length() == 0) {
                        et_pwd.setError("Please Enter Confirm Password !");
                    } else if (newappPass.length() != 8) {
                        et_pwd.setError("Password must be of 8 Characters!");
                    } else {
                        et_pwd.setError(null);
                        et_pwd.setText(newappPass);
                    }

                } else {
                    // clearing error marks from Text fields

                    et_usernam.setError(null);
                    et_pwd.setError(null);
                    String loggedusr = "",pwd_decrypted;
                    ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
                    sdadap.open();
                     Cursor csrs = sdadap.fetchlogintype(newappuser);
                   // Cursor csrs = sdadap.checkuser_exist(newappuser,newappPass);
                    if (csrs != null) {
                        loggedusr = csrs.getString(csrs.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                       byte loggedpwd[]= csrs.getBlob(csrs.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_ps));
                       //String loggedpwd= csrs.getString(csrs.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_ps));
                       String pwd_de = new String(loggedpwd);
                        pwd_de = pwd_de.replaceAll("\0", "");
                        StaticVariabes_div.log(pwd_de.length()+" login_acces pwd_de "+pwd_de, TAG1);

                        try {
                            byte[] tempBuf2 = removeTrailingNulls(loggedpwd);

                          //  byte tempBuf2[] = new byte[loggedpwd.length-1];
                           // System.arraycopy(tempBuf2, 0, tempBuf2, 0, loggedpwd.length-1);
                           // byte tempBuf2[]=new String(loggedpwd).replaceAll("\0", "").getBytes();
                            //byte tempBuf2[]=new String(loggedpwd).replaceAll("\0+$", "").getBytes();

                            String temp_de = new String(tempBuf2);

                            StaticVariabes_div.log(temp_de.length()+" login_acces temp_de "+temp_de, TAG1);

                          byte[]  decryptedd= StaticVariabes_div.decrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(), tempBuf2);
                            pwd_decrypted = new String(decryptedd);

                            StaticVariabes_div.log(pwd_decrypted.length()+"login_acces pwd_decrypted"+pwd_decrypted, TAG1);

                            if(newappPass.equals(pwd_decrypted)){

                                byte cipher[]=null;
                                try {
                                    cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newappPass.getBytes());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                db.opendb();
                                Boolean isupdate = db.update_login_account(StaticVariabes_div.housenumber, newappuser, cipher, loggedusr);

                                StaticVariabes_div.log("housenum" + StaticVariabes_div.housenumber + " newappuser" + newappuser + " newappPass" + newappPass + " loggedusr" + loggedusr, TAG1);


                                if (isupdate) {


                                    locallistupdate();
                                    StaticVariabes_div.log("isupdate" + isupdate, TAG1);

                                    Toast.makeText(UserSettingActivity.this, "Password Updated", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(UserSettingActivity.this, "Not Updated Try Again", Toast.LENGTH_SHORT).show();
                                }

                                db.close();

                            }else{
                                Toast.makeText(UserSettingActivity.this, "User Not Authorized", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }



                    } else {
                        Toast.makeText(UserSettingActivity.this, "User Not Authorized", Toast.LENGTH_SHORT).show();
                    }
                    sdadap.close();

                    alertDialog.dismiss();
                }

                //   alertDialog.setCancelable(true);

            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.75);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.55);
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.show();

    }

    public static byte[] removeTrailingNulls(byte[] source) {
        int i = source.length;
        while (source[i - 1] == 0x00) {
            i--;
        }

        byte[] result = new byte[i];
        System.arraycopy(source, 0, result, 0, i);

        return result;
    }




    public void locallistupdate(){
        try {
            LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
            locallist_adap = new LocalListArrangeTable(UserSettingActivity.this, StaticVariabes_div.housename,StaticVariabes_div.housename);
            locallist_adap.open();
            locallist_adap.deleteall();
            locallist_adap.close();
        }catch(Exception e){
            e.printStackTrace();

        }
    }

    public void read(final int type, final String stringData, final byte[] byteData)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    receiveddata(type, stringData, byteData);
                }catch(Exception e){
                    e.printStackTrace();
                }

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
                String readMessage2 =strdata;
                if(readMessage2.equals("*OK#")){
                   // serv_status(true);
                    //ButtonOut("920");

                }
                ReadlineData(readMessage2);

                break;
            case ServStatus:

                final String ServerStatusB =strdata;
                StaticStatus.Server_status=strdata;

                StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
                if(ServerStatusB!=null){
                    if (ServerStatusB.equals("TRUE")) {
                        StaticStatus.Server_status_bool=true;
                        statusserv = true;
                        servpreviousstate="TRUE";
                        nonetwork=false;


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

                final String signallevelB = strdata;

                if(signallevelB!=null) {
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


                break;
            case NetwrkType:

                final String RemoteB = strdata;
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

    public void network_signal(int signal1, final boolean serv) {

        if (serv) {
            //  btnwtype.setText("Remote");

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
                        //  onShift();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void ReadlineData( String RL){
        StaticVariabes_div.log("RL:- "+RL, TAG1);
        Log.d("RL:- "+RL, TAG1);

        ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        sdadap=new ServerDetailsAdapter(this);

        if(RL.startsWith("&")&&(RL.charAt((RL.length()-1))=='@')){
            if(RL.contains("&005@"))
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("Error Updating User");
                        //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                    }
                });

            }else  if(RL.contains("&500U@"))
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        byte cipher[]=null;
                        try {
                            cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newPass.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sdadap.open();
                        sdadap.updatePassword(StaticVariabes_div.loggeduser, cipher);
                        sdadap.close();

                        db.opendb();
                        db.updateuserpass(StaticVariabes_div.housenumber,StaticVariabes_div.loggeduser, cipher);
                        db.close();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                popupappdetails("User Password Updated");
                            }
                        });
                    }
                });

            }else  if(RL.contains("&500A@"))
            {
                byte cipher[]=null;
                try {
                    cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newPass.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                db.opendb();
                db.updateuserpass(StaticVariabes_div.housenumber,StaticVariabes_div.loggeduser, cipher);
                db.close();

                sdadap.open();
                sdadap.updatePassword(StaticVariabes_div.loggeduser, cipher);
                sdadap.close();

                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("Admin Password Updated");

                    }
                });

            }else if(RL.contains("&110S@"))
            {
                byte cipher[]=null;
                try {
                    cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newPass.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                sdadap.open();
                sdadap.updatePassword(StaticVariabes_div.loggeduser, cipher);
                sdadap.close();

                db.opendb();
                db.updateuserpass(StaticVariabes_div.housenumber,StaticVariabes_div.loggeduser, cipher);
                db.close();

                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("SuperAdmin Password Updated");

                    }
                });
            }else  if(RL.contains("&500G@"))
            {
                runOnUiThread(new Runnable() {
                    public void run() {
                        byte cipher[]=null;
                        try {
                            cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newPass.getBytes());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        sdadap.open();
                        sdadap.updatePassword(StaticVariabes_div.loggeduser, cipher);
                        sdadap.close();

                        db.opendb();
                        db.updateuserpass(StaticVariabes_div.housenumber,StaticVariabes_div.loggeduser, cipher);
                        db.close();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                popupappdetails("Guest Password Updated");
                            }
                        });
                    }
                });

            }
        }

    }


    private void popupappdetails(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(UserSettingActivity.this);
                alert.setTitle("Info");
                alert.setMessage(txt);
               // final TextView applnnameinfo = new TextView(UserSettingActivity.this);
               // applnnameinfo.setTextSize(20);
                //alert.setView(applnnameinfo);
               // applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        });
                alert.show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Intent intnt=new Intent(UserSettingActivity.this,Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }
}
