package edisonbro.com.edisonbroautomation.operatorsettings;


/**
 *  FILENAME: GatewaySettings_Tcp.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to fetch date time , ssid, version number, ebcode  and to set ssid and time of gateway.
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

public class GatewaySettings_Tcp extends AppCompatActivity implements AdapterView.OnItemSelectedListener,View.OnClickListener ,TcpTransfer {
    private int AtYear, AtMonth, AtDay,Atdayofweek2;
    String Atdate;
    Button b_edit,b_viewtime,b_settime,b_setssid,b_setdate,b_back,b_home,b_viewssid,b_reboot;
    TextView tv_time;TextView tvroomname,tv_versionnum,tv_ebcode;
    EditText et_date,et_ssid;
    String Username,password,devno,roomno,typeselected,resp;
    Spinner time_hrs,time_min;
    String time_hrs_spinr,time_min_spinr;String housename,houseno;
    ArrayList<String> repeathrs=new ArrayList<String>();
    ArrayList<String> repeatmin=new ArrayList<String>();
    LinearLayout linr_setdate,linr_selctdate;
    //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>

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
    Button btnconstatus,btsig,b_viewversion,b_viewebcode;
    static String encryptionKey = "edisonbrosmartlabs";
    private static final String TAG1="View Edit Server Time - ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_gateway_settings);

        btnconstatus=(Button) findViewById(R.id.btnconstatus);
        btsig=(Button) findViewById(R.id.btnsignal);

        b_viewtime=(Button) findViewById(R.id.btnvietime);
        b_settime=(Button) findViewById(R.id.btnsetdatetime);
        b_setssid=(Button) findViewById(R.id.btnselectssid);
        b_setdate=(Button) findViewById(R.id.btnselectdate);
        b_back=(Button) findViewById(R.id.btnback);
        b_home=(Button) findViewById(R.id.btnhome);
        tv_time=(TextView) findViewById(R.id.tv_time);
        time_hrs=(Spinner) findViewById(R.id.timehrs);
        time_min=(Spinner) findViewById(R.id.timemin);

        b_edit=(Button) findViewById(R.id.btn_edit);
        b_viewssid=(Button) findViewById(R.id.btngetssid);
        b_reboot=(Button) findViewById(R.id.btnrebootservr);
        b_viewversion=(Button) findViewById(R.id.btnversionnum);
        tv_versionnum=(TextView) findViewById(R.id.tv_vernum);
        b_viewebcode=(Button) findViewById(R.id.btnebcode);
        tv_ebcode=(TextView) findViewById(R.id.tv_ebcode);

        et_date=(EditText) findViewById(R.id.et_datevalue);
        et_ssid=(EditText) findViewById(R.id.et_ssidvalue);

        linr_setdate= (LinearLayout) findViewById(R.id.linearsetdate);
        linr_selctdate= (LinearLayout) findViewById(R.id.Linear_selcdate);

        linr_setdate.setVisibility(View.INVISIBLE);
        linr_selctdate.setVisibility(View.INVISIBLE);



        for(int i=0;i<=23;i++)
        {
            String ival=Integer.toString(i);
            if(ival.length()==1){
                repeathrs.add("0"+i);
            }else{
                repeathrs.add(""+i);
            }
        }

        for(int i=0;i<=60;i++)
        {
            //int j=i%2;
            //if(j==0)
            //{
            String ival=Integer.toString(i);

            if(ival.length()==1){
                repeatmin.add("0"+i);
            }else{
                repeatmin.add(""+i);
            }

            //}

        }



        Tcp_con mTcp = new Tcp_con(GatewaySettings_Tcp.this);


        if (Tcp_con.isClientStarted) {
            receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            receiveddata(ServStatus,StaticStatus.Server_status,null);
            View_Time();

        } else {
            Tcp_con.stacontxt = GatewaySettings_Tcp.this;
            Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
            Tcp_con.registerReceivers(GatewaySettings_Tcp.this);
        }




        b_setdate.setOnClickListener(GatewaySettings_Tcp.this);
        b_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                linr_setdate.setVisibility(View.VISIBLE);
                linr_selctdate.setVisibility(View.VISIBLE);
            }
        });

        time_hrs.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) GatewaySettings_Tcp.this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> c = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,repeathrs);
        c.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        time_hrs.setAdapter(c);


        time_min.setOnItemSelectedListener((AdapterView.OnItemSelectedListener) GatewaySettings_Tcp.this);

        //Creating the ArrayAdapter instance having the country list
        ArrayAdapter<String> acb = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,repeatmin);
        acb.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        time_min.setAdapter(acb);





        b_setssid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               final String ssid=et_ssid.getText().toString();

                if(ssid.length()>=1) {
                    StaticVariabes_div.log(ssid, TAG1);
                    Thread t = new Thread() {
                        public void run() {
                            Send_Timer_dat_aes(ssid,"{","*");
                        }
                    };
                    t.start();


                }else{

                    Toast.makeText(GatewaySettings_Tcp.this,"SSID Cannot Be Empty",Toast.LENGTH_SHORT);
                }

            }
        });

        b_settime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Atdate=et_date.getText().toString();
               final String trdata=Atdate+" "+time_hrs_spinr+":"+time_min_spinr+":00";
                StaticVariabes_div.log(trdata, TAG1);
                Thread t = new Thread() {
                    public void run() {
                        Send_Timer_dat_aes(trdata,"{","^");
                    }
                };
                t.start();


            }
        });

        b_viewversion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {
                        Send_Timer_dat_aes("153", "{", "%");
                    }
                };
                t.start();
            }
        });

        b_viewebcode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {
                        Send_Timer_dat_aes("154", "{", "!");
                    }
                };
                t.start();
            }
        });

        b_viewtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {
                        Send_Timer_dat_aes("150", "{", "?");
                    }
                };
                t.start();
            }
        });

        b_viewssid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {

                        Send_Timer_dat_aes("151","{","#");
                    }

                };
                t.start();

            }
        });


        b_reboot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {

                        Send_Timer_dat_aes("152","{","$");

                    }

                };
                t.start();

            }
        });

        //^^^^HOME BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^


        b_back.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent i=new Intent(GatewaySettings_Tcp.this,OperatorSettingsMain.class);
                i.putExtra("housename",housename);
                i.putExtra("houseno",houseno);
                startActivity(i);
                finish();

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
    public void ReadlineData( final String RL) {
        StaticVariabes_div.log("RL:- " + RL, TAG1);


        if (RL.startsWith("(")&& RL.endsWith(")")) {
            runOnUiThread(new Runnable() {
                public void run() {
                  //  popupappdetails("User Already Exists");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                    StaticVariabes_div.log("RL:- " + RL, TAG1);

                    String RT= RL.replace("_",":");
                    String RLtest = RT.substring(1, RT.length()-1);
                            tv_time.setText(RLtest);
                }
            });

        } else if (RL.startsWith("!")&& RL.endsWith("$")) {
            runOnUiThread(new Runnable() {
                public void run() {

                    String RLtest = RL.substring(1, RL.length()-1);
                    et_ssid.setText(RLtest);
                    //popupappdetails("Error Creating User,Please Try Again Later");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.startsWith("<")&& RL.endsWith(">")) {
            runOnUiThread(new Runnable() {
                public void run() {
                    popupappdetails("Rebooting.......");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                }
            });

        }else if(RL.startsWith("?")&& RL.endsWith("#")){

            runOnUiThread(new Runnable() {
                public void run() {

                    String RLtest = RL.substring(1, RL.length()-1);
                    tv_versionnum.setText(RLtest);
                }
            });
        }else if(RL.startsWith("[")&& RL.endsWith("]")){

            runOnUiThread(new Runnable() {
                public void run() {

                    String RLtest = RL.substring(1, RL.length()-1);
                    tv_ebcode.setText(RLtest);
                }
            });
        }
    }
    public void receiveddata(int msg,String strdata,byte[] bytestatus){

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                // DataIn(readBuf);
                if(readBuf!=null)
                if(readBuf.length>0) {
                    final String readMessage = new String(readBuf, 0, readBuf.length);
                    StaticVariabes_div.log("msg read bytes:- " + readMessage + " msg", TAG1);
                    ReadlineData(readMessage);
                }
                //  tvtest.setText(readMessage);
                // DataIn(readBuf);

                break;
            case READ_LINE:
                //  readMessage2 = (String) msg.obj;
                StaticVariabes_div.log("msg read string" + strdata, TAG1);
                readMessage2 =strdata;
                if(readMessage2.equals("*OK#")){
                    serv_status(true);
                    View_Time();
                    //ButtonOut("920");

                }else {

                    ReadlineData(readMessage2);
                }
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




    void View_Time(){
        Thread t = new Thread() {
            public void run() {

                Send_Timer_dat_aes("150","{","?");

            }

        };
        t.start();
    }
    private void popupappdetails(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(GatewaySettings_Tcp.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(GatewaySettings_Tcp.this);
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
    public void onClick(View v) {

        if (v == b_setdate) {



            // Process to get Current Date
            final Calendar c = Calendar.getInstance();
            AtYear = c.get(Calendar.YEAR);
            AtMonth = c.get(Calendar.MONTH);
            AtDay = c.get(Calendar.DAY_OF_MONTH);
            Atdayofweek2=c.get(Calendar.DAY_OF_WEEK);

            // Launch Date Picker Dialog
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            //StaticVariables.frmdate=dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year;
                            String mont=""+(monthOfYear + 1);
                            if(mont.length()<2)mont="0"+mont;

                            String dateval=""+(dayOfMonth);
                            if(dateval.length()<2)dateval="0"+dateval;

                            Atdate=year+ "-" +mont + "-" +dateval;
                            // Display Selected date in textbox
                            //et_date.setText(dayOfMonth + "-"+ (monthOfYear + 1) + "-" + year);
                            et_date.setText(Atdate);

                            //dateset=true;

                        }
                    }, AtYear, AtMonth, AtDay);
            dpd.show();
        }
    }

    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        switch (parent.getId()) {

            case R.id.timehrs:

                time_hrs_spinr=repeathrs.get(position);

                break;

            case R.id.timemin:

                time_min_spinr=repeatmin.get(position);

                break;

            default:
                break;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // TODO Auto-generated method stub

    }

    void tost(String a)
    {
        Toast.makeText(getApplicationContext(), a, Toast.LENGTH_LONG).show();
    }


    public void onBackPressed() {
        //super.onBackPressed();
        Intent i=new Intent(GatewaySettings_Tcp.this,OperatorSettingsMain.class);
        i.putExtra("housename",housename);
        i.putExtra("houseno",houseno);
        startActivity(i);
        finish();
    }

    public void Send_Timer_dat_aes(String StrTimer,String starttokn,String endtokn){

        String tosend=null;
        tosend=starttokn+StrTimer+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);
        Tcp_con.WriteString(temp_str);
    }


}
