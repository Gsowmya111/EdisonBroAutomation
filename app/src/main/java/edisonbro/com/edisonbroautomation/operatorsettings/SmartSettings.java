package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: SmartSettings.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to switch on and off lights of whole house .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.usersettings.UserSettingActivity;

public class SmartSettings extends AppCompatActivity implements TcpTransfer {
    //**********************************************************8
    Button btnconstatus,btsig,btn_back,btn_all_switch_on,btn_all_switch_off,btn_all_switch_fan_on,btn_all_switch_fan_off;
    LocalDatabaseHelper db=null;
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
    String logintype,tosend;
    private static final String TAG1="SmartSetting - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_smart_settings);

        btnconstatus=(Button) findViewById(R.id.btnconstatus);
        btsig=(Button) findViewById(R.id.btnsignal);
        btn_back  = (Button)findViewById(R.id.btnback);
        btn_all_switch_on =(Button)findViewById(R.id.btn_all_switchs_on);
        btn_all_switch_off =(Button)findViewById(R.id.btn_all_switchs_off);
        btn_all_switch_fan_on =(Button)findViewById(R.id.btn_all_switchs_fan_on);
        btn_all_switch_fan_off =(Button)findViewById(R.id.btn_all_switchs_fan_off);

        try{

            db=new LocalDatabaseHelper(this);
        }catch(Exception e){
            e.printStackTrace();
        }

        if((StaticVariabes_div.housename==null)||(StaticVariabes_div.housename=="")||(StaticVariabes_div.housename.equals("")) ){

            popup_loginM("Please Download House First");

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
                    Tcp_con.stacontxt = SmartSettings.this;
                    Tcp_con.serverdetailsfetch(SmartSettings.this, StaticVariabes_div.housename);
                    Tcp_con.registerReceivers(SmartSettings.this);
                }
            }

        }

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intnt=new Intent(SmartSettings.this,Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });


        btn_all_switch_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tosend="0" + "03" + "000" + "0000" +"000"+ "000" + "000000000000000";
                ButtonOut(tosend);
            }
        });

        btn_all_switch_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tosend="0" + "04" + "000" + "0000" +"000"+ "000" + "000000000000000";
                ButtonOut(tosend);

            }
        });

        btn_all_switch_fan_on.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tosend="0" + "02" + "000" + "0000" +"000"+ "000" + "000000000000000";
                ButtonOut(tosend);
            }
        });

        btn_all_switch_fan_off.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tosend="0" + "05" + "000" + "0000" +"000"+ "000" + "000000000000000";
                ButtonOut(tosend);
            }
        });
       // tosend="0" + "02" + "000" + "0000" +"000"+ "000" + "000000000000000";



    }


    void ButtonOut(String tosenddata) {

        if (Tcp_con.isClientStarted) {

            String str = tosenddata;
            StaticVariabes_div.log("out" + str, TAG1);
           // byte[] op= Blaster.WriteData(str, 1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];

            Tcp_con.WriteBytes(result);
        }
    }

    private void popup_loginM(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(SmartSettings.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(SmartSettings.this);
                applnnameinfo.setTextSize(20);
                alert.setView(applnnameinfo);
                applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intnt=new Intent(SmartSettings.this, Main_Navigation_Activity.class);
                                startActivity(intnt);
                                finish();
                            }
                        });
                alert.show();
            }
        });

    }

    @Override
    public void read(final int type, final String stringData, final byte[] byteData) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    receiveddata(type, stringData, byteData);
                }catch(Exception ee){
                    Tcp_con.stacontxt = SmartSettings.this;
                    Tcp_con.serverdetailsfetch(SmartSettings.this, StaticVariabes_div.housename);
                    Tcp_con.registerReceivers(SmartSettings.this);
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
               // ReadlineData(readMessage2);

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

    public void onBackPressed() {
        Intent intnt=new Intent(SmartSettings.this, Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }
}
