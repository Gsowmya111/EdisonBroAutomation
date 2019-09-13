package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: Add_Alexa.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to Add Alexa by providing input details like roomname ,devicename,and id .
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
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
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
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

public class Add_Alexa extends Activity implements TcpTransfer {
    private static final String TAG1="Add Alexa - ";
    Spinner room_spinner;
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
    //**********************************************************************

    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************

    Button btsig;
    HouseConfigurationAdapter houseDB=null;
    WirelessConfigurationAdapter WhouseDB=null;
    SpinnerAdapter roomNameAdapter;
    private SwbAdapter swbadap;
    private MasterAdapter mas_adap;
    ArrayList<String> UniqueRoomList=new ArrayList<String>();
    int spinnerLayoutId=R.layout.spinnerlayout;
    EditText et_alexaid, et_user_dev_name;
    Button btn_add_alexa,btn_uploadalexasettng;
    String User_Alexa_Name,rname,User_Alexa_Id,matched_room_image_name,DeviceID,sPackName;
    int DeviceNumber,matched_room_number;
    private ServerDetailsAdapter sdadap;
    LocalListArrangeTable locallist_adap;
    ImageView im_back,btnconstatus;
    ProgressDialog progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        setContentView(R.layout.add_alexa_device);
        room_spinner = (Spinner) findViewById(R.id.room_name_spin);
        btn_add_alexa= (Button) findViewById(R.id.submit);
        et_user_dev_name= (EditText) findViewById(R.id.et_usrdevname);
        et_alexaid= (EditText) findViewById(R.id.et_alexaid);
        im_back= (ImageView) findViewById(R.id.imageView2);
        btnconstatus=(ImageView) findViewById(R.id.server_status);
        btn_uploadalexasettng= (Button) findViewById(R.id.upload_alexasett);

        Context context = getApplicationContext();
        sPackName = context.getPackageName();

        Tcp_con mTcp = new Tcp_con(Add_Alexa.this);


        if (Tcp_con.isClientStarted) {

            receiveddata(NetwrkType, StaticStatus.Network_Type, null);
            receiveddata(ServStatus, StaticStatus.Server_status, null);

        } else {
            Tcp_con.stacontxt = Add_Alexa.this;
            Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
            Tcp_con.registerReceivers(Add_Alexa.this);
        }


        try{
            houseDB=new HouseConfigurationAdapter(this);
            houseDB.open();			//opening house database

            swbadap=new SwbAdapter(this);
            mas_adap=new MasterAdapter(this);

        }catch(Exception e){
            e.printStackTrace();
        }


        fillRoomNameList();

        im_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchActivity();
            }
        });

        btn_uploadalexasettng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupupload();
            }
        });
        btn_add_alexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                rname=room_spinner.getSelectedItem().toString();
                StaticVariabes_div.log("RoomName"+rname,"Addnew");

                User_Alexa_Name=et_user_dev_name.getText().toString();
                String isdevnamUnique=houseDB.isDeviceName_Exists(User_Alexa_Name);
                StaticVariabes_div.log("isdevnamUnique"+isdevnamUnique,"add");

                User_Alexa_Id= et_alexaid.getText().toString();
                StaticVariabes_div.log("User_Alexa_Id"+User_Alexa_Id,"add");

                if (rname == null || rname.length() == 0 || (room_spinner.getSelectedItemPosition()==UniqueRoomList.size()-1) ){

                    Toast.makeText(getApplicationContext(),"Please Select a Room", Toast.LENGTH_SHORT).show();

                }else if(User_Alexa_Name == null || User_Alexa_Name.length() == 0){

                    et_user_dev_name.setError("Please Enter Alexa Name.");

                }else if(User_Alexa_Id == null || User_Alexa_Id.length() == 0 ){

                    et_alexaid.setError("Please Enter Alexa ID.");

                }else {



                        //Fetching Max Device Number From DataBase
                        DeviceNumber = houseDB.getUniqueDeviceNo();


                        StaticVariabes_div.log("DEV NO Check :", "DeviceNo check:" + DeviceNumber);

                        //fetching room number of current room
                        matched_room_number = houseDB.CurrentRoomNumber(rname);

                        //fetching room image of current room
                        matched_room_image_name = houseDB.CurrentRoomImageName(rname);

                        DeviceID = User_Alexa_Id;
                    String Result = houseDB.isDeviceExists(DeviceID);

                    if(!(isdevnamUnique.equals("TRUE"))) {
                        if (Result.equals("FALSE")) {

                            boolean isSaved = houseDB.UpdateMaster("ALXA", "057",
                                    DeviceNumber, DeviceID, rname, matched_room_number,
                                    "ALXA", "000", matched_room_image_name, User_Alexa_Name);

                            if (isSaved) {

                                Update_db_version_number();

                                //success dialog foe switch board configuration
                                ConfigurationSuccessDialog("Configuration Completed", "Alexa Configured Successfully.\n" + "Upload Alexa Settings For Changes To Reflect."); //Displaying success Dialog

                                try {
                                    LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
                                    locallist_adap = new LocalListArrangeTable(Add_Alexa.this, StaticVariabes_div.housename, StaticVariabes_div.housename);
                                    locallist_adap.open();
                                    locallist_adap.deleteall();
                                    locallist_adap.close();
                                } catch (Exception e) {
                                    e.printStackTrace();

                                }
                            } else {
                                //error dialog for switch board configuration
                                popupDialog("Details Not Saved", "Unable to Save Details for Alexa.Try Configuring Again.");

                            }

                        } else {
                            //error dialog for switch board configuration
                            DeviceAlreadyExistsAlert(User_Alexa_Name);

                        }
                    }else{

                        popupDialog("Alexa Name Exists", "Please Enter Other Name");

                    }
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

    public void popupupload() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Upload Settings").setCancelable(false)

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Tcp_con.isClientStarted) {
                            progressstart("Uploading Wired Settings.....");


                            long delayInMillis = 8000;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressstop ();
//                                    Toast.makeText(Add_Alexa.this,"Failed to Upload .Try Again",Toast.LENGTH_LONG).show();
                                }
                            }, delayInMillis);

                            Thread upThread = new Thread() {
                                public void run() {
                                    UploadDb(sPackName, StaticVariabes_div.housename + ".db", "$117&");
                                }
                            };upThread.start();
                            //tv_poperr.setText("Uploading Please wait ..........");
                        } else {
                            popup("Server Not Connected");
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

    public static void UploadDb(String PackageName, String DataBaseName,String cmd) {


        try {
          /*  if(ops!=null){
                ops.write("$117&\r\n".getBytes());
                ops.flush();
            }*/
            //  String command=cmd+"\r\n";
            String command=cmd;
            Tcp_con.WriteBytes(command.getBytes());
            StaticVariabes_div.log("outstream command"+ command,"tcpout");
            Thread.sleep(500);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String path1 = "//data//" + PackageName + "//databases//" + DataBaseName;
        //String path = "/data/data"+ PackageName +"/databases/"+DataBaseName;
        File data = Environment.getDataDirectory();
        File currentDB = new File(data, path1);
        int flen2 = (int) currentDB.length();
        StaticVariabes_div.log(" * file - "+DataBaseName+" size  "  + flen2, TAG1);


      /*  if(ops!=null){
            try {
                ops.write((flen2 + "\r\n").getBytes());
                ops.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/

        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        String flenth=flen2+"";
        Tcp_con.WriteBytes(flenth.getBytes());

        StaticVariabes_div.log("outstream flen2 "+ flen2,"tcpout");
        try {
            Thread.sleep(100);
        } catch (InterruptedException e1) {
            e1.printStackTrace();
        }

        FileInputStream fin = null;
        byte[] buf = new byte[flen2];
        try {
            fin = new FileInputStream(currentDB);
            BufferedInputStream bis = new BufferedInputStream(fin);
            bis.read(buf, 0, buf.length);
            //BufferedOutputStream bout = new BufferedOutputStream(ops);
            BufferedOutputStream bout = new BufferedOutputStream(Tcp_con.output);
            StaticVariabes_div.log("outstream buf"+ buf.length,"tcpout");
            bout.write(buf, 0, buf.length);
            bout.flush();

            Thread.sleep(500);

            bis.close();
            fin.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    //showing Device already exists warning dialog
    private void DeviceAlreadyExistsAlert(final String DeviceName){
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {

                AlertDialog.Builder dialog=new AlertDialog.Builder(Add_Alexa.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle("Device Already Exists");
                dialog.setMessage("Device Already Configured"
                        + "\nDo You Want To Update Details?");
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);

                dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });

                dialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        boolean isSaved = houseDB.UpdateMaster("ALXA", "057",
                                DeviceNumber, DeviceID, rname, matched_room_number,
                                "ALXA", "000", matched_room_image_name, User_Alexa_Name);

                        if (isSaved) {

                            Update_db_version_number();

                            //success dialog foe switch board configuration
                            ConfigurationSuccessDialog("Configuration Completed", "Alexa Configured Successfully.\n" + "Upload Alexa Settings For Changes to Reflect."); //Displaying success Dialog

                            try {
                                LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
                                locallist_adap = new LocalListArrangeTable(Add_Alexa.this, StaticVariabes_div.housename, StaticVariabes_div.housename);
                                locallist_adap.open();
                                locallist_adap.deleteall();
                                locallist_adap.close();
                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        } else {
                            //error dialog for switch board configuration
                            popupDialog("Details Not Saved", "Unable to Save Details for Alexa.Try Configuring Again.");

                        }

                    }
                });

                dialog.show();
            }
        });
    }
    private void popupDialog(final String title,final String msg)
    {
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {

                AlertDialog.Builder dialog=new AlertDialog.Builder(Add_Alexa.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //indicating dialog disappeared from screen

                        dialog.dismiss();

                    }
                });

                dialog.show();
            }
        });
    }
    void ConfigurationSuccessDialog(final String title,final String msg)
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {


                AlertDialog.Builder dialog=new AlertDialog.Builder(Add_Alexa.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setCancelable(false);

            /*    dialog.setNegativeButton("Home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent it=new Intent(Add_Alexa.this,Main_Navigation_Activity.class);
                        // adding transition to activity exit
                        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        startActivity(it);
                        finish();


                    }
                });

                dialog.setNeutralButton("Config Menu", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent it=new Intent(Add_Alexa.this,Configuration_Main.class);
                        startActivity(it);
                        //adding transition to activity exit
                        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        finish();
                    }
                });*/

                dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
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
                sdadap = new ServerDetailsAdapter(Add_Alexa.this);
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



                break;
            case NetwrkType:

                final String RemoteB = strdata;
                StaticStatus.Network_Type=RemoteB;
                StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);

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

    public void ReadlineData( final String RL) {
        StaticVariabes_div.log("RL:- " + RL, TAG1);


        if (RL != null && RL.startsWith("*") && RL.endsWith("$")) {
            if (RL.contains("*117$")) {
                // uploadstatus=true;

                runOnUiThread(new Runnable() {
                    public void run() {
                        // tv_poperr.setText("***");

                        try {
                            Thread.sleep(600);
                            progressstop();
                            popup("Alexa Settings Uploaded Successfully ");
                            Tcp_con.WriteBytes("{alexa@".getBytes());
                            StaticVariabes_div.log("outstream command  {alexa@ ","tcpout");

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                    }
                });

            } else if (RL.contains("*122$")) {

            }

        }
    }

    void progressstart(final String Messg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar==null){
                    progressBar = new ProgressDialog(Add_Alexa.this);
                    progressBar.setCancelable(false);
                    progressBar.setMessage(Messg);
                    // progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                }else{
                    if(progressBar!=null){
                        progressBar.dismiss();
                        progressBar=null;
                    }
                }
            }
        });


    }
    void progressstop(){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar!=null){
                    progressBar.dismiss();
                    progressBar=null;
                }
            }
        });
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
    //going back to previous activity
    @Override
    public void onBackPressed() {
        switchActivity();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(HouseConfigurationAdapter.sdb.isOpen()){
            houseDB.close();
            StaticVariables.printLog("TAG","DB CLOSED ");
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(!HouseConfigurationAdapter.sdb.isOpen()){
            houseDB.open();
            StaticVariables.printLog("TAG","DB open ");
        }
    }

    //switch activity
    void switchActivity(){
        Intent it=new Intent(Add_Alexa.this,Configuration_Main.class);
        startActivity(it);
        finish();
    }

}
