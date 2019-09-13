package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 *  FILENAME: Configuration_Main.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity for configuration actions like download home,upload home and option
 *  to switch to activites like add device,delete device,delete room.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Config;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.Timer;
import java.util.TimerTask;
import java.util.TreeSet;
import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp_dwn_config;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;


public class Configuration_Main extends AppCompatActivity implements TcpTransfer {
    RadioButton rb_wired,rb_wireless,rb_camera,rb_alexa;
    private ProgressDialog pdlg;
    Button b_back,b_home;
    String tempdbPath, tempdbPath2;
    //Wired
    Button b_adddevice, b_deldevice, b_delpanel, b_uploadHome, b_addpanel,
            b_downloadhome,b_delhom,b_panelsettins,b_delroom,b_editroom,b_editdevname,b_editroom_icons,
            b_addalexa,b_editalexa;
    int sta,en;
    private static final String TAG1 = "Config_Main - ";
    ProgressDialog progressBar;
    //************************************************************************

    private static final int READ_BYTE = 1;
    private static final int READ_LINE = 2;
    private static final int ServStatus = 3;
    private static final int signallevel = 4;
    private static final int NetwrkType = 5;
    private static final int MAXUSER = 6;
    private static final int ERRUSER = 7;

    //**********************************************
    int sl;
    boolean check = true, statusserv, remoteconn, remoteconn3g, nonetwork;
    String servpreviousstate, remoteconprevstate, rs, readMessage2, inp;
    //************************************************************************
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************
    Button btnconstatus, btsig;
    String sPackName;
    boolean hom_downloadcomplete,dwnloaddb=false,downloadcomplete=false;
    LocalDatabaseHelper db=null;
    private static SwbAdapter swbadap;
    RadioButton rb_array[];
    TextView tv_versnnum;
    private ServerDetailsAdapter sdadap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN | WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.configuration);

        Context context = getApplicationContext(); // this.getContext(); getApplicationContext(); etc.
        sPackName = context.getPackageName();
        b_back = (Button) findViewById(R.id.btnback);
        b_adddevice = (Button) findViewById(R.id.btnadddevice);
        b_addpanel = (Button) findViewById(R.id.btnaddpanel);
        b_uploadHome = (Button) findViewById(R.id.btnuploadhome);
        b_downloadhome = (Button) findViewById(R.id.btndwnhome);
        b_delhom= (Button) findViewById(R.id.btndelhome);
        b_panelsettins= (Button) findViewById(R.id.btnpanelsett);
        b_deldevice= (Button) findViewById(R.id.btndeldevice);
        b_delpanel= (Button) findViewById(R.id.btndelpanel);
        b_delroom= (Button) findViewById(R.id.btndelroom);
        b_editroom= (Button) findViewById(R.id.btneditroom);
        b_editroom_icons= (Button) findViewById(R.id.btneditroom_icons);
        b_editdevname= (Button) findViewById(R.id.btneditdevnam);
        b_addalexa= (Button) findViewById(R.id.btnaddalexa);
        b_editalexa= (Button) findViewById(R.id.btneditalexa);

        rb_wired= (RadioButton) findViewById(R.id.radioButton_wired);
        rb_wireless= (RadioButton) findViewById(R.id.radioButton_wirless);
        rb_camera= (RadioButton) findViewById(R.id.radioButton_camera);
        rb_alexa= (RadioButton) findViewById(R.id.radioButton_alexa);
        tv_versnnum= (TextView) findViewById(R.id.tv_versno);


        rb_array=new RadioButton[]{rb_wired,rb_wireless,rb_camera,rb_alexa};


        b_adddevice.setEnabled(false);
        b_deldevice.setEnabled(false);

        b_addpanel.setEnabled(false);
        b_panelsettins.setEnabled(false);
        b_delpanel.setEnabled(false);

        b_addalexa.setEnabled(false);
        b_editalexa.setEnabled(false);

        b_adddevice.setBackgroundResource(R.drawable.config1);
        b_deldevice.setBackgroundResource(R.drawable.config1);

        b_addpanel.setBackgroundResource(R.drawable.config1);
        b_panelsettins.setBackgroundResource(R.drawable.config1);
        b_delpanel.setBackgroundResource(R.drawable.config1);

        b_addalexa.setBackgroundResource(R.drawable.config1);
        b_editalexa.setBackgroundResource(R.drawable.config1);



        try{
            swbadap=new SwbAdapter(this);
            db=new LocalDatabaseHelper(this);

        }catch(Exception e){
            e.printStackTrace();
        }

        StaticVariables.udp_use=false;
        Update_db_version_number();
        btnconstatus = (Button) findViewById(R.id.btnconstatus);
        btsig = (Button) findViewById(R.id.btnsignal);
        b_home= (Button) findViewById(R.id.btnhome);

        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(Configuration_Main.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });


        b_editdevname.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(Configuration_Main.this, EditDeviceName.class);
                startActivity(intnt);
                finish();
            }
        });

        btnconstatus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(!Tcp_con.isClientStarted){
                    Tcp_con.stacontxt=Configuration_Main.this;
                    Tcp_con.serverdetailsfetch(Configuration_Main.this, StaticVariabes_div.housename);
                    Tcp_con.registerReceivers(Configuration_Main.this);

                }else{
                    Toast.makeText(getApplicationContext(),"server is already connected ",Toast.LENGTH_SHORT).show();
                }


            }
        });
        rb_wired.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_wired.isChecked())
                {
                    CheckedStateUpdate(true,false,false,false);
                    colorchange(0);
                }


            }
        });
        rb_wireless.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_wireless.isChecked())
                {
                    CheckedStateUpdate(false,true,false,false);
                    colorchange(1);
                }


            }
        });

        rb_camera.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_camera.isChecked())
                {
                    CheckedStateUpdate(false,false,true,false);
                    colorchange(2);
                }


            }
        });

        rb_alexa.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_alexa.isChecked())
                {
                    CheckedStateUpdate(false,false,false,true);
                    colorchange(3);

                }


            }
        });


        b_addalexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(Configuration_Main.this, Add_Alexa.class);
                startActivity(intnt);
                finish();
            }
        });

        b_editalexa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(Configuration_Main.this, Edit_Alexa.class);
                startActivity(intnt);
                finish();
            }
        });

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intnt = new Intent(Configuration_Main.this, OperatorSettingsMain.class);
                startActivity(intnt);
                finish();
            }
        });

        b_adddevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(Configuration_Main.this, AddNewDevice.class);
                startActivity(intnt);
                finish();
            }
        });

        b_addpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(Configuration_Main.this, WirelessConfigurationDetails.class);
                startActivity(intnt);
                finish();
            }
        });
        b_panelsettins.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt = new Intent(Configuration_Main.this, Wireless_Panel_Selection.class);
                startActivity(intnt);
                finish();
            }
        });
        b_uploadHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupupload();

            }
        });

        b_delhom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupdelete();

            }
        });

        b_delpanel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inttt=new Intent(Configuration_Main.this,WirelessDeletePanelSettings.class);
                startActivity(inttt);
                finish();

            }
        });

        b_deldevice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inttt=new Intent(Configuration_Main.this,DeleteDevice.class);
                startActivity(inttt);
                finish();

            }
        });

        b_delroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inttt=new Intent(Configuration_Main.this,DeleteRoom.class);
                startActivity(inttt);
                finish();

            }
        });

        b_editroom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inttt=new Intent(Configuration_Main.this,EditRoomName.class);
                startActivity(inttt);
                finish();

            }
        });


        b_editroom_icons.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent inttt=new Intent(Configuration_Main.this,EditRoomIcons.class);
                startActivity(inttt);
                finish();

            }
        });
        b_downloadhome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /*    if (Tcp_con.isClientStarted) {
                    b_downloadhome.setEnabled(false);
                    // tv_poperr.setText("Creating folder....");
                    boolean checkfolder = Createfolder(sPackName, StaticVariabes_div.housename, "HomeEdisonDB");
                    if (checkfolder) {
                        //  tv_poperr.setText("Folder Created....");
                        downloadDatabase();
						*//* if(out!=null){
					    tv_poperr.setText("writing out....");
						out.write("$118&");
						out.flush();
					} *//*
                    }
                } else {
                    popup("Server Not Connected");
                }*/

                Tcp_dwn_config.tcpHost=Tcp_con.tcpAddress;
                Tcp_dwn_config.tcpPort=Tcp_con.tcpPort;

                Intent intt=new Intent(Configuration_Main.this,UpdateHome_Existing.class);
                intt.putExtra("isusersett","false");
                startActivity(intt);
               // finish();


            }
        });


        Tcp_con mTcp = new Tcp_con(this);


        if (Tcp_con.isClientStarted) {

            receiveddata(NetwrkType, StaticStatus.Network_Type, null);
            receiveddata(ServStatus, StaticStatus.Server_status, null);


        } else {
            Tcp_con.stacontxt =Configuration_Main.this;
            Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
            Tcp_con.registerReceivers(this);
        }


    }
    public void popupdelete(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Delete House").setCancelable(false)

                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {


                        swbadap.open();
                        boolean homedel=swbadap.deleteDatabase(StaticVariabes_div.housename+".db");
                        swbadap.close();

                        if(homedel){
                          //  pre_adap.open();
                            db.opendb();

                            if(StaticVariabes_div.housename!=null){
                                boolean locwirelessdb=swbadap.deleteDatabase(StaticVariabes_div.housename+"_WLS.db");
                                StaticVariabes_div.log("locwirelessdb"+locwirelessdb, TAG1);
                               // boolean lochomelist=db.delete(Integer.parseInt(houseno));
                                boolean lochomelist=db.delete_hom(StaticVariabes_div.housename);
                                StaticVariabes_div.log("lochomelist"+lochomelist, TAG1);
                                //@@@@@ //pre_adap.deletevalforhouseno(houseno);
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
                        Intent i=new Intent(Configuration_Main.this,Main_Navigation_Activity.class);
                        startActivity(i);
                        finish();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void popup_configalexa() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Config Alexa To Rooms").setCancelable(false)

                .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



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

    @Override
    public void read(final int type, final String stringData, final byte[] byteData) {

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

    public void receiveddata(int msg, String strdata, byte[] bytestatus) {

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                // DataIn(readBuf);
                final String readMessage = new String(readBuf, 0, readBuf.length);
                StaticVariabes_div.log("msg read bytes:- " + readMessage + " msg", TAG1);
                //  tvtest.setText(readMessage);
                // DataIn(readBuf);

                break;
            case READ_LINE:
                //  readMessage2 = (String) msg.obj;
                StaticVariabes_div.log("msg read string" + strdata, TAG1);
                readMessage2 = strdata;
                if (readMessage2.equals("*OK#")) {
                    serv_status(true);
                    //ButtonOut("920");

                }
                //  tvtest.setText(readMessage2);

                if (strdata != null ) {
                    if (strdata.startsWith("$") && (strdata.charAt((strdata.length() - 3)) == '&')) {

                        sta = strdata.indexOf("$");
                        en = strdata.indexOf("&");
                        inp = strdata.substring(++sta, en);
                        final int lengthdb = Integer.parseInt(inp);
                        dwnloaddb = true;
                        write_db_to_temFile(lengthdb, tempdbPath);

                    }
                }
                if (strdata != null && strdata.startsWith("*") && strdata.endsWith("$")) {
                    if (strdata.contains("*117$")) {
                        // uploadstatus=true;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                // tv_poperr.setText("***");

                                try {
                                    Thread.sleep(1000);
                                    progressstop();
                                    popup_wired("Wired Settings Uploaded Successfully ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                                // uploadstatus=false;
                                // serv_status(false);
                            }
                        });

                    } else if (strdata.contains("*122$")) {
                        // uploadstatus=true;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                try {
                                    Thread.sleep(200);
                                progressstop();
                                // tv_poperr.setText("***");
                                popup("Wireless Settings Uploaded Successfully ");
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                // uploadstatus=false;
                                // serv_status(false);
                            }
                        });

                    }else if (strdata.contains("*000$")) {
                        // uploadstatus=false;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //tv_poperr.setText("***");
                                progressstop();
                                popup("Wired Settings Not Uploaded Try Again");
                            }
                        });
                    }
                }

                if (strdata != null && strdata.startsWith("*") && strdata.contains(":")) {
                    if (strdata.contains("*000:")) {
                        // uploadstatus=false;
                        runOnUiThread(new Runnable() {
                            public void run() {
                                //tv_poperr.setText("***");
                                progressstop();
                                popup("Wireless Settings Not Uploaded Try Again");
                            }
                        });
                    }
                }

                //   if(RL.startsWith("*")&&(RL.charAt((RL.length()-3))=='$')){


                break;
            case ServStatus:
                //final String ServerStatusB = (String) msg.obj;
                final String ServerStatusB = strdata;
                StaticStatus.Server_status = strdata;
                StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
                if (ServerStatusB != null) {
                    if (ServerStatusB.equals("TRUE")) {
                        StaticStatus.Server_status_bool = true;
                        statusserv = true;
                        servpreviousstate = "TRUE";
                        nonetwork = false;
                        // Cc.dataswb = true;
                        //  ButtonOut("920");
                        // Cc.TcpReadLine = false;

                    } else {
                        StaticStatus.Server_status_bool = false;
                        statusserv = false;
                        servpreviousstate = "FALSE";
                    }
                } else {
                    StaticStatus.Server_status_bool = false;
                    statusserv = false;
                    servpreviousstate = "FALSE";
                }

                serv_status(statusserv);

                break;
            case signallevel:
             /*   //    final String signallevelB = (String) msg.obj;
                final String signallevelB = strdata;
               // StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
                if (signallevelB != null) {
                    sl = Integer.parseInt(signallevelB);
                    rs = signallevelB;
                    if (!remoteconn3g) {
                        if (!nonetwork) {

                            network_signal(sl, StaticStatus.Server_status_bool);

                        }
                    } else {
                        remoteconn = true;
                        remoteconn3g = true;
                        remoteconprevstate = "TRUE3G";
                        nonetwork = false;
                        if (timer != null) {
                            timer.cancel();
                            timer = null;
                        }

                        network_signal(sl, remoteconn3g);

                    }
                }*/

                final String signallevelB = strdata;
                //  StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
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
                //final String RemoteB = (String) msg.obj;
                final String RemoteB = strdata;
                StaticStatus.Network_Type = RemoteB;
                StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
                if (RemoteB.equals("TRUE")) {
                    nonetwork = false;
                    remoteconn = true;
                    remoteconn3g = false;
                    remoteconprevstate = "TRUE";

                    network_signal(sl, remoteconn3g);

                    if (timer == null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            public void run() {
                                Tcp_con.rssirec();  // display the data

                            }
                        }, delay, period);
                    }
                } else if (RemoteB.equals("TRUE3G")) {
                    nonetwork = false;
                    remoteconn = true;
                    remoteconn3g = true;
                    remoteconprevstate = "TRUE3G";
                    nonetwork = false;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    network_signal(sl, remoteconn3g);

                } else if (RemoteB.equals("NONET")) {
                    statusserv = false;
                    servpreviousstate = "FALSE";
                    nonetwork = true;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    remoteconn = false;
                    remoteconn3g = false;

                    network_signal(sl, remoteconn3g);


                } else {
                    nonetwork = false;
                    remoteconn = false;
                    remoteconn3g = false;
                    remoteconprevstate = "FALSE";
                    if (timer == null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            public void run() {
                                Tcp_con.rssirec();  // display the data

                            }
                        }, delay, period);
                    }

                    network_signal(sl, remoteconn3g);

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
            case ERRUSER:
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

    public void serv_status(final boolean serv) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (serv) {
                    btnconstatus.setBackgroundResource(R.drawable.connected);
                } else
                    btnconstatus.setBackgroundResource(R.drawable.not_connected);
            }
        });
    }

    public void popup(String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        //  onShift();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }


    public void popup_wired(String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                        //  onShift();
                        if (Tcp_con.isClientStarted) {
                            progressstart("Uploading Wireless Settings.....");

                            long delayInMillis = 8000;
                            Timer timer = new Timer();
                            timer.schedule(new TimerTask() {
                                @Override
                                public void run() {
                                    progressstop ();
                                }
                            }, delayInMillis);

                            Thread upwrlsThread = new Thread() {
                                public void run() {
                                    UploadDb(sPackName, StaticVariabes_div.housename+"_WLS" + ".db","$122&");
                                }
                            };upwrlsThread.start();

                            //tv_poperr.setText("Uploading Please wait ..........");
                        }

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    boolean Createfolder(String PackageName, String DataBaseName, String SDFolderName) {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            String newFolder = SDFolderName;
            String extStorageDirectory = sd.toString();
            File myNewFolder = new File(extStorageDirectory + "//" + newFolder);
            if (!myNewFolder.exists()) {
                if (sd.canWrite())
                    myNewFolder.mkdir();
            }

            if (sd.canRead()) {
                tempdbPath = extStorageDirectory + "/" + SDFolderName + "/" + DataBaseName + ".db";
                tempdbPath2 = SDFolderName + "//" + DataBaseName + ".db";
                File backupDB = new File(sd, tempdbPath2);
                FileChannel dst = new FileOutputStream(backupDB).getChannel();
                dst.close();
                //Toast.makeText(getBaseContext(), backupDB.toString(), Toast.LENGTH_LONG).show();
                return true;
            }
        } catch (Exception e) {
            Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }

    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::

    //connect and download thread
    private void downloadDatabase() {
        Thread downloadThread = new Thread() {
            public void run() {

               /* if(out!=null){
                    //  tv_poperr.setText("writing out....");
                    out.write("$118&");
                    out.flush();
                }*/

                try {
                    Tcp_con.WriteBytes("$118&".getBytes());
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                StaticVariabes_div.log("server connected and download command send", TAG1);
                runOnUiThread(new Runnable() {
                    public void run() {
                        ProgressDialog("Downloading Data From Server....");
                    }
                });
                int counter1 = 1;
                int progress = 0;
                while (counter1 <= 10) {
                    counter1++;
                    int counter2 = 1;
                    while (counter2 < 10) {
                        if (hom_downloadcomplete) {
                            counter1 = 20;
                            break;
                        } else {
                            delay(100);
                        }

                        counter2++;
                    }
                    updateProgress(progress + 10);
                    StaticVariabes_div.log("DOWNLOAD TIMER COUNT" + "DWD TIMER : " + counter1, TAG1);
                }

                //update progress and making UI thread sleep for 1 sec
                //to show final progress to user
                updateProgress(100);
                SleepUiThread(1000);

                if (counter1 < 20 && !hom_downloadcomplete) {
                    hom_downloadcomplete = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //updateProgress(100);
                            DismissDialog();
                            b_downloadhome.setEnabled(true);
                           // tv_poperr.setText("***");
                            popup("UPDATE FAILED TRY AGAIN");
                        }
                    });


                } else if (hom_downloadcomplete) {
                    hom_downloadcomplete = false;
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            DismissDialog();
                            popup("UPDATED SUCCESSFULLY ");
                        }
                    });

                }
            }
        };
        downloadThread.start();

    }

    //dismissing dialog
    void DismissDialog() {
        runOnUiThread(new Runnable() {

            @Override
            public void run() {
                if (pdlg != null) {
                    pdlg.dismiss();        // Dismissing the progress dialog
                    pdlg = null;
                }
            }
        });
    }

    void delay(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    //method to update the current task progress
    void updateProgress(final int progress) {
        runOnUiThread(new Runnable() {
            public void run() {
                if (pdlg != null && progress <= 100) {
                    pdlg.setProgress(progress);    //setting current progress
                }
            }
        });

    }

    //delay for ui thread
    void SleepUiThread(final long delay) {
        runOnUiThread(new Runnable() {
            public void run() {
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    //progress dialog method to show progress dialog
    private void ProgressDialog(final String msg) {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                pdlg = new ProgressDialog(Configuration_Main.this, ProgressDialog.THEME_HOLO_LIGHT);
                pdlg.setMessage(msg);
                pdlg.setIndeterminate(false);
                pdlg.setCancelable(false);
                pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                pdlg.setProgress(0);
                pdlg.setMax(100);
                Resources res = getResources();
                // Get the Drawable custom_progressbar
                Drawable customDrawable = res.getDrawable(R.drawable.progressbar);
                // set the drawable as progress drawavle
                pdlg.setProgressDrawable(customDrawable);
                pdlg.show();
            }
        });


    }

    public  void write_db_to_temFile(int flength,String pathtemp) {
        try {

            String path = pathtemp;
            FileOutputStream fout = new FileOutputStream(path);
            byte[] buffer = new byte[1024];
            int bytesRead = 0, counter = 0;
            BufferedInputStream bin =new BufferedInputStream(Tcp_con.ins());
            Thread.sleep(1200);
            while (bytesRead >= 0) {

                bytesRead = bin.read(buffer);

                if (bytesRead >= 0) {
                    fout.write(buffer, 0, bytesRead);
                    counter += bytesRead;

                    if (counter == flength) {
                        downloadcomplete=true;
                        fout.flush();
                        fout.close();
                        bytesRead=0;
                        counter=0;
                        break;
                    }
                    else if(counter>flength)
                    {
                        downloadcomplete=false;
                        fout.flush();
                        fout.close();
                        bytesRead=0;
                        counter=0;

                        runOnUiThread(new Runnable() {
                            public void run() {
                                b_downloadhome.setEnabled(true);
                            }
                        });

                        break;
                    }
                }
            }


            File fsz=new File(path);

            if(downloadcomplete && (fsz.length()==flength) )
            {
                StaticVariabes_div.log("Downloaded! home", TAG1);
                hom_downloadcomplete=UPDATEDB(sPackName, StaticVariabes_div.housename, "HomeEdisonDB");
                StaticVariabes_div.log("Downloaded! updated home"+hom_downloadcomplete, TAG1);
                runOnUiThread(new Runnable() {
                    public void run() {
                        b_downloadhome.setEnabled(true);
                    }
                });
                downloadcomplete=false;
            }


        }catch (Exception e) {
            hom_downloadcomplete=false;
            e.printStackTrace();
            runOnUiThread(new Runnable() {
                public void run() {
                    b_downloadhome.setEnabled(true);
                }
            });
            StaticVariabes_div.log("file sts"+"Error on downloading file!", TAG1);

        }
    }


    //:::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::::
    boolean UPDATEDB(String PackageName, String DataBaseName, String SDFolderName)
    {
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canRead()) {
                String currentDBPath = SDFolderName+"//"+DataBaseName+".db";
                // String updateDBPath = "//data//"+ PackageName +"//databases//"+DataBaseName;
                String updateDBPath = "/data/"+ PackageName +"/databases/"+DataBaseName+".db";
                File currentDB = new File(sd, currentDBPath);
                File updateDB = new File(data, updateDBPath);

                FileChannel src = new FileInputStream(currentDB).getChannel();
                FileChannel dst = new FileOutputStream(updateDB).getChannel();
                dst.transferFrom(src, 0, src.size());
                src.close();
                dst.close();
                // Toast.makeText(getBaseContext(), updateDB.toString(), Toast.LENGTH_LONG).show();
                runOnUiThread(new Runnable() {
                    public void run() {
                        //tv_poperr.setText("***");
                        // popup("UPDATED SUCCESSFULLY ");
                    }
                });


                return true;

            }
        } catch (Exception e) {
            StaticVariabes_div.log("updated excep"+e.toString(), TAG1);
            // Toast.makeText(getBaseContext(), e.toString(), Toast.LENGTH_LONG).show();
            return false;
        }
        return false;
    }


    public void CheckedStateUpdate(boolean wiredstate,boolean wirelssstate,boolean camstate,boolean alexa){

        rb_wired.setChecked(wiredstate);
        rb_wireless.setChecked(wirelssstate);
        rb_camera.setChecked(camstate);
        rb_alexa.setChecked(alexa);


        if(wiredstate){
            b_adddevice.setEnabled(true);
            b_deldevice.setEnabled(true);
            b_adddevice.setBackgroundResource(R.drawable.config_2);
            b_deldevice.setBackgroundResource(R.drawable.config_2);

        }else{
            b_adddevice.setEnabled(false);
            b_deldevice.setEnabled(false);
            b_adddevice.setBackgroundResource(R.drawable.config1);
            b_deldevice.setBackgroundResource(R.drawable.config1);
        }

        if(wirelssstate){
            b_addpanel.setEnabled(true);
            b_panelsettins.setEnabled(true);
            b_delpanel.setEnabled(true);
            b_addpanel.setBackgroundResource(R.drawable.config_2);
            b_panelsettins.setBackgroundResource(R.drawable.config_2);
            b_delpanel.setBackgroundResource(R.drawable.config_2);

        }else{
            b_addpanel.setEnabled(false);
            b_panelsettins.setEnabled(false);
            b_delpanel.setEnabled(false);
            b_addpanel.setBackgroundResource(R.drawable.config1);
            b_panelsettins.setBackgroundResource(R.drawable.config1);
            b_delpanel.setBackgroundResource(R.drawable.config1);

        }

        if(alexa){
            b_addalexa.setEnabled(true);
            b_editalexa.setEnabled(true);
            b_addalexa.setBackgroundResource(R.drawable.config_2);
            b_editalexa.setBackgroundResource(R.drawable.config_2);

        }else{
            b_addalexa.setEnabled(false);
            b_editalexa.setEnabled(false);

            b_addalexa.setBackgroundResource(R.drawable.config1);
            b_editalexa.setBackgroundResource(R.drawable.config1);

        }

    }



    public void colorchange(int posit){

        for(int k=0;k<rb_array.length;k++){
            rb_array[k].setTextColor(Color.BLACK);
        }

        rb_array[posit].setTextColor(Color.rgb(66,130,208));

    }


    void progressstart(final String Messg){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar==null){
                    progressBar = new ProgressDialog(Configuration_Main.this);
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


    public void Update_db_version_number(){
        if(StaticVariabes_div.housename!=null) {
            try {
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                sdadap = new ServerDetailsAdapter(Configuration_Main.this);
                sdadap.open();
                Cursor cursrno = sdadap.fetchrefno(1);
                String home_versionnumber = cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_db));
                cursrno.close();
                Float fvernum = Float.valueOf(home_versionnumber);
                String s_vernum = String.format("%.2f", fvernum);
                tv_versnnum.setText(s_vernum);
            }catch(Exception e){

            }
        }
    }
    //////////////////////////////
    public void onBackPressed() {
        Intent intnt=new Intent(Configuration_Main.this, OperatorSettingsMain.class);
        startActivity(intnt);
        finish();
    }
}