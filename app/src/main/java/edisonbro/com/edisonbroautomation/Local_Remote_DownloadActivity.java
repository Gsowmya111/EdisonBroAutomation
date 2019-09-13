package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: Local_Remote_DownloadActivity.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:Activity to download database local or remotely.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.net.ParseException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Udp;
import edisonbro.com.edisonbroautomation.databasewireless.LocalDatabaseAdapter;
import edisonbro.com.edisonbroautomation.wiredconfig.DownloadDatabse;

public class Local_Remote_DownloadActivity extends AppCompatActivity implements TcpTransfer {

    EditText et_url,et_usrid,et_ip,et_port;
    TextView tv_url,tv_usrid,tv_ip,tv_port;
    Button connectdwn,b_back,b_home,b_editurl;
    RadioButton rb_url,rb_ip,rb_local,rb_remote,rb_admin,rb_user;
    LinearLayout layip,layurl,layrem,layloc,layeditbtn;


    //****************************************************************
    String ipadress,portno,remipweb,remportweb;
    static String Ipadress_Port = null;
    static InputStream is = null;
    String urledtext,usridedtext;
    static StringBuilder sb=null;
    static String result1=null;

    //********************Broad Cast Udp****************************
    TextView title;
    private ProgressDialog pdlg;
    EditText et_udpport;
    final int RESPONSE=1 ,EXCEPTION=4;
    Udp udp=null;
    String udpport;
    boolean ipFound=false,exceptionOccured=false,isDlgDisplayed=false,isInvalidIp=false,isHouseExits=false;
    LocalDatabaseAdapter localDB=null;

//***********************************User Selection***********************************************************

    Button admin_btn,user_btn;
    String ADMIN_PASS=null;
    EditText adminpass;

    ImageView serverStatus;
    Tcp tcp=null;
    final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION_US=3,TCP_LOST=4,TCP_CONNECTED=6,ERR_USER=7,MAX_USER=8;
    String SERVER_PASSWORD=null  ;

    int server_online=R.drawable.connected;
    int server_offline=R.drawable.disconnected;

    String uname=null,oldPass=null,newPass=null,confirmPass=null;
    boolean exceptionOccured_us=false;//,isHouseExits=false;
    boolean IS_SERVER_CONNECTED=false ,isDialogOpened=false,IS_SERVER_NOT_AUNTHETICATED=false;
    LinearLayout userlyt;
  //  LocalDatabaseAdapter localDB = null;
    private Tracker mTracker;
    String name ="Download Database";
    Context cntx;
    LayoutInflater inflater;
    View viewpopup;
    //*******************************************************************************************

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN|WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setContentView(R.layout.local_remote_home_download);

        Tcp_con mTcp = new Tcp_con(Local_Remote_DownloadActivity.this);
        Tcp_con.stacontxt=Local_Remote_DownloadActivity.this;
        Tcp_con.context=Local_Remote_DownloadActivity.this;

        cntx=Local_Remote_DownloadActivity.this;
         inflater = (LayoutInflater) cntx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );

        userlyt=(LinearLayout) findViewById(R.id.lay_loc);

       // b_back= (Button) findViewById(R.id.btnback);

        et_url=(EditText) findViewById(R.id.et_url);
        et_usrid=(EditText) findViewById(R.id.et_homref);

        tv_url=(TextView) findViewById(R.id.tv_url);
        tv_usrid=(TextView) findViewById(R.id.tv_homref);

        et_ip=(EditText) findViewById(R.id.et_ip);
        et_port=(EditText) findViewById(R.id.et_port);
        tv_ip=(TextView) findViewById(R.id.tv_ip);
        tv_port=(TextView) findViewById(R.id.tv_port);

        rb_url=(RadioButton) findViewById(R.id.radioButtonURL);
        rb_ip=(RadioButton) findViewById(R.id.radioButtonIP);
        connectdwn=(Button) findViewById(R.id.bt_conntdwnrem);
        b_back=(Button) findViewById(R.id.btnback);
        b_home=(Button) findViewById(R.id.btnhome);

        layip=(LinearLayout) findViewById(R.id.lay_ip);
        layurl=(LinearLayout) findViewById(R.id.lay_url);

        rb_local= (RadioButton) findViewById(R.id.radioButtonlocal);
        rb_admin= (RadioButton) findViewById(R.id.radioButtonadmin);
        rb_user= (RadioButton) findViewById(R.id.radioButtonUser);

        rb_remote= (RadioButton) findViewById(R.id.radioButtonremote);

        layrem= (LinearLayout) findViewById(R.id.lay_rem);
        layloc= (LinearLayout) findViewById(R.id.lay_loc);

        layeditbtn= (LinearLayout) findViewById(R.id.layeditbtn);

        b_editurl= (Button) findViewById(R.id.edit_url_btn);
        tv_url= (TextView) findViewById(R.id.tv_url);

        et_url.setText(StaticVariabes_div.Home_ip_download_url);
        et_url.setVisibility(View.INVISIBLE);

        tv_url.setVisibility(View.INVISIBLE);


        rb_remote.setChecked(true);
        loc_rem_vis_invis(false);
        StaticVariables.udp_use=false;


       // StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        //StrictMode.setThreadPolicy(policy);

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        rb_url.setChecked(true);
        if(rb_url.isChecked())
        {
            urlvis();
        }

        b_editurl.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                et_url.setVisibility(View.VISIBLE);
                tv_url.setVisibility(View.VISIBLE);
            }
        });

        rb_local.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_local.isChecked())
                {
                    loc_rem_vis_invis(true);
                }
                StaticVariables.udp_use=true;
                serversearchstart();
              /*  if(!isDlgDisplayed){
                    //starting progress on starting of application
                    ProgressDialog("Searching Server....");
                    new Local_Remote_DownloadActivity.SearchServer().execute();
                }*/
            }
        });

        rb_remote.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_remote.isChecked())
                {
                    loc_rem_vis_invis(false);
                }
                StaticVariables.udp_use=false;
            }
        });

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
                startActivity(it);
                finish();
            }
        });




        rb_url.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_url.isChecked())
                {
                    urlvis();
                }
            }
        });

        rb_ip.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rb_ip.isChecked())
                {
                    ipvis();
                }
            }
        });


        rb_admin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                rb_user.setChecked(false);

                adminPasswordCheck();

            }
        });

        rb_user.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                rb_admin.setChecked(false);

              /*  Intent it2=new Intent(Local_Remote_DownloadActivity.this,DownloadDatabse.class);
                it2.putExtra("activity_name", "UserSelection");
                startActivity(it2);
                //adding transition to activity exit
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                finish();*/
            }
        });

        b_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent il=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
                startActivity(il);
                finish();

            }
        });


        connectdwn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(rb_url.isChecked())
                {
                    String urlet=et_url.getText().toString();
                    String usrid=et_usrid.getText().toString();

                    if(usrid.length()>0&&urlet.length()>0) {
                        urledtext = urlet;
                        usridedtext = usrid;
                        Thread th = new Thread() {

                            public void run() {
                                StaticVariabes_div.log("msg", "fetch remote ip");
                                //connect();
                                connect(urledtext, usridedtext);
                            }
                        };
                        th.start();
                    }else
                        Toast.makeText(Local_Remote_DownloadActivity.this,"Please Enter Valid Details",Toast.LENGTH_LONG).show();

                }else {

                    String rmip = et_ip.getText().toString();
                    String rmport = et_port.getText().toString();

                    remipweb = rmip;
                    remportweb = rmport;


                    if (remipweb == null || remportweb == null || remipweb.length() < 1 || remportweb.length() < 1 || !remipweb.contains(".")){

                        if(remipweb==null||remipweb.length()<1){
                            et_ip.setError("Please Enter IP address");
                        }else if(!remipweb.contains(".")) {
                            et_ip.setError("Please Enter Valid IP address");
                        }else{
                            et_ip.setError(null);
                        }

                        if(remportweb==null||remportweb.length()<1){
                            et_port.setError("Please Enter Port number");
                        }else{
                            et_port.setError(null);
                        }

                   }else{

                        et_ip.setError(null);
                        et_port.setError(null);


                   // StaticVariables.Host = remipweb;
                   // StaticVariables.Port = Integer.parseInt(remportweb);

                        Tcp.tcpHost    = remipweb;
                        Tcp.tcpPort    = Integer.parseInt(remportweb);


                    Intent it1 = new Intent(Local_Remote_DownloadActivity.this, DownloadDatabse.class);
                    it1.putExtra("activity_name", "Remoteconndwn");
                    startActivity(it1);
                    //adding transition to activity exit
                    //overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                    finish();
                   }
                }

            }
        });


    }


        public void loc_rem_vis_invis(boolean vis){

        if(vis){
            layrem.setVisibility(View.INVISIBLE);
            layloc.setVisibility(View.VISIBLE);
            rb_remote.setChecked(false);
            rb_user.setVisibility(View.INVISIBLE);
            rb_admin.setVisibility(View.INVISIBLE);
            rb_user.setChecked(false);
            rb_admin.setChecked(false);
        }else{

            layrem.setVisibility(View.VISIBLE);
            layloc.setVisibility(View.INVISIBLE);
            rb_local.setChecked(false);
            rb_user.setChecked(false);
            rb_admin.setChecked(false);
            rb_user.setVisibility(View.INVISIBLE);
            rb_admin.setVisibility(View.INVISIBLE);
        }


        }


    public void connect(String urled,String str)
    {


        String str1=urled+str;
        String str2 = Fetchremoteip(str1);
        if(str2!=null){
            if(str2.equals("server error")){
                runOnUiThread(new Runnable() {
                    public void run() {
                        popup("could not connect to server,try again later or check url and userid");
                    }
                });


            }else{
                StringTokenizer tokens = new StringTokenizer(str2, ",");

                ipadress = tokens.nextToken();
                //Object separated;
                String porttemp=tokens.nextToken();
                String[] separated = porttemp.split(";");
                portno=separated[0];
                remipweb=ipadress;
                remportweb=portno;

                //StaticVariables.Host=remipweb;
               // StaticVariables.Port=Integer.parseInt(remportweb);
                Tcp.tcpConnectionClose();
                Tcp.tcpHost=remipweb;
                Tcp.tcpPort=Integer.parseInt(remportweb);

		   Intent it1=new Intent(Local_Remote_DownloadActivity.this,DownloadDatabse.class);
		it1.putExtra("activity_name", "Remoteconndwn");
		startActivity(it1);
		//adding transition to activity exit
		//overridePendingTransition(R.anim.slideup, R.anim.slidedown);
		//finish();

            }

        }else{

            runOnUiThread(new Runnable() {
                public void run() {
                    popup("could not connect to server,try again later or check url");
                }
            });

        }
    }


    public static String Fetchremoteip(String str)
    {
        try{
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost((str));
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("IP", null));
            nameValuePairs.add(new BasicNameValuePair("PORT", null));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        }catch(Exception e){
            e.printStackTrace();
            Ipadress_Port="server error";
        }
        try{
            BufferedReader reader = new BufferedReader(new InputStreamReader(is,"iso-8859-1"),8);
            sb = new StringBuilder();
            sb.append(reader.readLine() + "\n");
            String line="0";
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result1=sb.toString();
            StaticVariables.printLog("result1",result1);

        }catch(Exception e){
            e.printStackTrace();
            Ipadress_Port="server error";
        }
        try{
            if(result1!=null||result1.length()>0){
                JSONArray jArray = new JSONArray(result1);
                JSONObject json_data=null;
                for(int i=0;i<jArray.length();i++){
                    json_data = jArray.getJSONObject(i);
                    Ipadress_Port = json_data.getString("IP");
                    Ipadress_Port += ",";
                    Ipadress_Port += json_data.getString("PORT");
                }
            }else{
                Ipadress_Port="server error";
            }
        }catch(JSONException e1){
            Ipadress_Port="server error";
        }catch (ParseException e1){
            Ipadress_Port="server error";
        }catch(Exception ee){
            Ipadress_Port="server error";
        }
        return Ipadress_Port;
    }
    public void popup(String msg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this,AlertDialog.THEME_HOLO_LIGHT);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage(msg).setCancelable(false)

                .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

                        dialog.cancel();
                    }
                });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    public void urlvis(){

        layurl.setEnabled(true);
        layurl.setVisibility(View.VISIBLE);
        layeditbtn.setVisibility(View.VISIBLE);

        layip.setEnabled(false);
        layip.setVisibility(View.INVISIBLE);
        rb_ip.setChecked(false);
    }


    public void ipvis(){
        layurl.setEnabled(false);
        layurl.setVisibility(View.INVISIBLE);
        layeditbtn.setVisibility(View.INVISIBLE);
        rb_url.setChecked(false);

        layip.setEnabled(true);
        layip.setVisibility(View.VISIBLE);
    }


    public void onBackPressed() {
       // finish();
        //android.os.Process.killProcess(android.os.Process.myPid());
        Intent it=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
        startActivity(it);
        finish();
    }




    public  void serversearchstart() {


        //passing handler and activity context instance to udp class
        udp=new Udp(BroadcastHandler,Local_Remote_DownloadActivity.this);

        try{
            //Passing Context of Current activity and opening database
            localDB=new LocalDatabaseAdapter(this);
            localDB.opendb();

            isHouseExits=localDB.HouseNamesList().size()>0;

            localDB.close();




        }catch(Exception e){
            e.printStackTrace();
        }


        if(!isDlgDisplayed){
            //starting progress on starting of application
            ProgressDialog("Searching Server....");
            new Local_Remote_DownloadActivity.SearchServer().execute();
        }
    }


 /*   @Override
    protected void onStart() {
        super.onStart();
        if(!isDlgDisplayed){
            //starting progress on starting of application
            ProgressDialog("Searching Server....");
            new Local_Remote_DownloadActivity.SearchServer().execute();
        }
    }*/

    //Handler to handle udp response
    private Handler BroadcastHandler=new Handler()
    {
        @Override
        public void handleMessage(Message msg)
        {
            switch(msg.what)
            {
                case RESPONSE:
                {
                    final String Data=(String) msg.obj;
                    runOnUiThread(new Runnable()
                    {
                        public void run() {

                            //udp format
                            //*IP:ServerPort;TimerServerPort;WirelessServerPort#

                            int mainServerPortNumber=0;

                            if( Data!=null && Data.startsWith("*")&& Data.endsWith("#") && !Data.equals("*NULL#"))
                            {
                                String ip=null;
                                try{
                                    String mainData=Data.substring(1, Data.length()-1);

                                    //getting array of main data
                                    String[] dataItems=mainData.split(":");

                                    //getting main server ip
                                    ip=dataItems[0];

                                    //splitting port data
                                    String[]  ports=dataItems[1].split(";");

                                    String mainServerPort=ports[0];
                                    try{

                                        //converting string port to integer type
                                        mainServerPortNumber=Integer.parseInt(mainServerPort);

                                    }catch(Exception e){
                                        e.printStackTrace();
                                    }


                                }catch(Exception e){
                                    e.printStackTrace();
                                }


                                StaticVariables.printLog("UDP","Server IP  :"+ip +"\nServer Port:"+mainServerPortNumber);

                                if(ip!=null && !ip.equals("0.0.0.0")){
                                    ipFound=true;

                                    //setting ip & port in static fields
                                    //StaticVariables.Host=ip;
                                    StaticVariables.Host_udp=ip;
                                    Tcp.tcpHost=ip;

                                    //checking if port is not 0
                                    if(mainServerPortNumber!=0){

                                        //setting main server port in static variables
                                        StaticVariables.MAIN_SERVER_PORT=mainServerPortNumber;
                                       // StaticVariables.Port=mainServerPortNumber;
                                        StaticVariables.Port_udp=mainServerPortNumber;
                                        Tcp.tcpPort=mainServerPortNumber;
                                    }
                                }else{
                                    msg("Invaid Server Ip");
                                    isInvalidIp=true;
                                }
                            }else if(Data.equals("*NULL#")){
                                isInvalidIp=true;
                            }
                        }
                    });
                    break;
                }

                case EXCEPTION:
                {
                    final String Data=(String) msg.obj;
                    exceptionOccured=true;
                    break;
                }

            }
        }
    };

    @Override
    public void read(int type, String stringData, byte[] byteData) {

    }

    //searching server
    private class SearchServer extends AsyncTask<String, Integer, Boolean>
    {
        @Override
        protected void onPreExecute() {
            //making variable true i.e progressbar is present on screen
            isDlgDisplayed=true;

            if(pdlg!=null){
                pdlg.setMessage("Searching Server....");
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params)
        {
            boolean serverFound=false;

            int i=0,progress=0;
            while(!ipFound && i<5)
            {
                if(!exceptionOccured)	{
                    try{
                        delay(1000);
                        udp.udpWrite("getip\r\n");
                    }catch(Exception e){
                        e.printStackTrace();
                    }

                }
                else{
                    //canceling task
                    new Local_Remote_DownloadActivity.SearchServer().cancel(true);
                    udp.udpConnectionClose();
                    break;
                }
                i++;
                StaticVariables.printLog("connection attempts","server connection attempts :"+i);

                progress+=20;
                publishProgress(progress);	//publishing current progress of search attempts
                int j=0;

                //checking if server has sent invalid ip
                if(isInvalidIp){
                    serverFound=false;
                    break;
                }

                while(j<20)
                {
                    j++;

                    if(ipFound)	{
                        serverFound=true;
                        if(pdlg!=null){
                            pdlg.setProgress(100);
                        }
                        break;

                    }else {
                        delay(100);	//giving delay
                    }
                }
            }

            return serverFound;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            super.onPostExecute(result);
            if(pdlg!=null){
                pdlg.dismiss();
                pdlg=null;
            }
            if(result)
            {
                //Displaying Success Dialog
                ServerFoundDialog();

            }else {
                //closing udp connection
                udp.udpConnectionClose();

                if(isInvalidIp){
                    serverErrorDialog("Invalid Server IP",
                            "Invalid Ip from Server.Please check Server Settings!");
                }else{
                    //Displaying Error Dialog
                    serverErrorDialog("Server Not Found",
                            "Please Check Your WiFi Settings And Check Whether Server is ON");
                }

            }

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int progress1=values[0];
            if(pdlg!=null && progress1<=100){
                pdlg.setProgress(progress1);
            }
        }
    }

    //progress dialog
    private void ProgressDialog(final String msg)
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                if(pdlg==null){
                    pdlg=new ProgressDialog(Local_Remote_DownloadActivity.this,ProgressDialog.THEME_HOLO_LIGHT);
                    pdlg.setMessage(msg);
                    pdlg.setIndeterminate(false);
                    pdlg.setCancelable(false);
                    pdlg.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    pdlg.setMax(100);
                    Resources res = getResources();
                    // Get the Drawable custom_progressbar
                    Drawable customDrawable= res.getDrawable(R.drawable.progressbar);
                    // set the drawable as progress drawable
                    pdlg.setProgressDrawable(customDrawable);
                    pdlg.show();
                }
            }
        });

    }

    //error dialog for server not searched
    void serverErrorDialog(final String title,final String msg)
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                //making variable true i.e popup is present on screen
                isDlgDisplayed=true;

                AlertDialog.Builder dialog=new AlertDialog.Builder(Local_Remote_DownloadActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle(title);
                dialog.setMessage(msg);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);

                if(isHouseExits){

                    dialog.setNegativeButton("Home",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent it=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
                            // adding transition to activity exit
                            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                            startActivity(it);
                            finish();

                        }
                    });

                }else{
                    dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
                        }
                    });
                }


                dialog.setNeutralButton("Download Remotely",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                        Intent it=new Intent(Local_Remote_DownloadActivity.this,Local_Remote_DownloadActivity.class);
                       startActivity(it);
                        //adding transition to activity exit
                       // overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        finish();
                    }
                });

                dialog.setPositiveButton("Edit Port and Try Again", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        SetUdpPort();

                    }
                });

                dialog.show();
            }
        });
    }


    private void SetUdpPort() {

        final	AlertDialog.Builder alert = new AlertDialog.Builder(Local_Remote_DownloadActivity.this,AlertDialog.THEME_HOLO_LIGHT);
        alert.setTitle("Enter Udp Port");
        //alert.setMessage("Enter The Time Value");

      //  LayoutInflater inflater = getLayoutInflater();
        //LayoutInflater inflater = (LayoutInflater) cntx.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        //LayoutInflater inflater = LayoutInflater.from(Local_Remote_DownloadActivity.this);
        //View checkboxLayout = inflater.inflate(R.layout.pop_udpport, null);
       // View
       // alert.setView(checkboxLayout);



        viewpopup =inflater.inflate(R.layout.popup_port_udp,null);
        alert.setView(viewpopup);

        et_udpport = (EditText) viewpopup.findViewById(R.id.et_udpport);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
        //^^^^Set time BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        alert.setPositiveButton("Set And Try", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                udpport=et_udpport.getText().toString();

                if(isValidtime(udpport)){
                    //StaticVariabes_div.log("udpport"+udpport+"length"+udpport.length(), TAG1);
                    StaticVariables.UDP_SERVER_PORT=Integer.parseInt(udpport);

                    ProgressDialog("Connecting to Server....");
                    exceptionOccured=false;
                    ipFound=false;
                    isInvalidIp=false;
                    //making variable false i.e popup is not present on screen
                    isDlgDisplayed=true;

                    //passing handler and activity context instance to udp class
                    udp=new Udp(BroadcastHandler,Local_Remote_DownloadActivity.this);

                    new Local_Remote_DownloadActivity.SearchServer().execute();
                }else{
                    SetUdpPort();
                    et_udpport.setError("Length 4digit");

                }


            }
        });
        AlertDialog d = alert.create();
        d.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        d.show();
    }


    private boolean isValidtime(String port) {
        if (port != null && port.length()==4) {

            return true;
        }
        return false;
    }

    // server searched successful dialog
    private void ServerFoundDialog()
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                //making variable true i.e popup is present on screen
                isDlgDisplayed=true;

                AlertDialog.Builder dialog=new AlertDialog.Builder(Local_Remote_DownloadActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle("Server Found");
                dialog.setMessage("Server Found Successfully.Proceed To Next Step?");
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setCancelable(false);
                if(isHouseExits){

                    dialog.setNegativeButton("Home",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();

                            Intent it=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
                            // adding transition to activity exit
                           // overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                            startActivity(it);
                            finish();

                        }
                    });

                }else{
                    dialog.setNegativeButton("Exit",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
                        }
                    });
                }

                dialog.setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                       // StaticVariables.Host=StaticVariables.Host_udp;
                       // StaticVariables.Port=StaticVariables.Port_udp;
                        //Tcp_con.tcpAddress=StaticVariables.Host_udp;
                        //Tcp_con.tcpPort=StaticVariables.Port_udp;
                        Tcp.tcpHost=StaticVariables.Host_udp;
                        Tcp.tcpPort=StaticVariables.Port_udp;

                        Intent it2=new Intent(Local_Remote_DownloadActivity.this,DownloadDatabse.class);
                        it2.putExtra("activity_name", "UserSelection");
                        startActivity(it2);
                        //adding transition to activity exit
                        //overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        finish();

                        //Sending intent with Data
                      /*  Intent it=new Intent(Local_Remote_DownloadActivity.this,UserSelection.class);
                        startActivity(it);
                        //adding transition to activity exit
                        overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                        finish();*/
                    }
                });

                dialog.show();
            }
        });
    }

    //delay method
    void delay(long delay)
    {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
        exceptionOccured=false;
        ipFound=false;
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


//*****************************************USER SELECTION*************************************************************************


    //method to get auto connection in class
    void RegainingConnection(){

        Thread thread =new Thread(){
            public void run(){
                IS_SERVER_CONNECTED=Tcp.EstablishConnection(TcpHandler);
                runOnUiThread(new Runnable() {
                    public void run()
                    {
                        if(IS_SERVER_CONNECTED){

                            IS_SERVER_CONNECTED=false;

                            //setting error as null
                            adminpass.setError(null);

                            if(!IS_SERVER_NOT_AUNTHETICATED){

                                serverStatus.setImageResource(server_online);

                                StaticVariables.printLog("TAG","CONNECTED");

                                //switching activity
                             //   Intent it1=new Intent(Local_Remote_DownloadActivity.this,AdminPanel.class);
                              //  startActivity(it1);
                                //adding transition to activity exit
                               // overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                               // finish();
                            }else{

                                IS_SERVER_NOT_AUNTHETICATED=false;

                                serverStatus.setImageResource(server_offline);

                                StaticVariables.printLog("TAG","NOT CONNECTED");

                            }

                        }else{

                            serverStatus.setImageResource(server_offline);

                            StaticVariables.printLog("TAG","NOT CONNECTED");

                            if(!isDialogOpened){
                                //displaying dialog with error
                                serverErrorDialog_userselection("Login Failed", "Please Check Whether Wifi setting is Correct and Server is ON.");

                            }

                        }
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

                    break;
                }

                case READ_BYTE:	{

                    break;
                }

                case EXCEPTION_US:
                {
                    final String Data=(String) msg.obj;
                    StaticVariables.printLog("TCP RESPONSE","DATA GET FROM TCP SOCKET :"+Data);
                    exceptionOccured_us=true;
                    break;
                }

                case TCP_CONNECTED:{
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            serverStatus.setImageResource(server_online);
                        }
                    });
                    break;
                }
                case TCP_LOST:
                {
                    runOnUiThread(new Runnable() {
                        public void run()
                        {
                            StaticVariables.printLog("TAG","Tcp Lost");
                            serverStatus.setImageResource(server_offline);
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
                            TCPErrorAlert("Server Error","Server connections Limit Exceeded .Please disconnect some other client to connect with server.");

                        }
                    });
                    break;
                }
            }
        }
    };

    // Error Alert showing that tcp is Off
    private void TCPErrorAlert(final String title,final String msg) {
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userlyt.getWindowToken(), 0);

                AlertDialog.Builder dlg = new AlertDialog.Builder(Local_Remote_DownloadActivity.this,AlertDialog.THEME_HOLO_LIGHT);
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
                d.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                d.show();
            }
        });
    }


 /*   //onclick events
    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.admin:
                adminPasswordCheck();
                break;

            case R.id.user:
                Intent it2=new Intent(Local_Remote_DownloadActivity.this,DownloadDatabse.class);
                it2.putExtra("activity_name", "UserSelection");
                startActivity(it2);
                //adding transition to activity exit
                overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                finish();
                break;
        }
    }*/
    //server error dialog
    void serverErrorDialog_userselection(final String titleMsg,final String msg)
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(userlyt.getWindowToken(), 0);

                AlertDialog.Builder dialog=new AlertDialog.Builder(Local_Remote_DownloadActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle(titleMsg);
                dialog.setMessage(msg);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);
                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                dialog.setPositiveButton("Try Again",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        adminPasswordCheck();
                    }
                });

                AlertDialog d = dialog.create();
                d.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                d.show();
            }
        });
    }
    public void hidepopupkeyboard(EditText input){
        InputMethodManager im = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        im.hideSoftInputFromWindow(input.getWindowToken(), 0);
    }
    //Admin Authority Check Dialog
    void adminPasswordCheck(){
        runOnUiThread(new Runnable() {

            @Override
            public void run() {

                IS_SERVER_NOT_AUNTHETICATED=false;

                AlertDialog.Builder dialog = new AlertDialog.Builder(Local_Remote_DownloadActivity.this,AlertDialog.THEME_HOLO_LIGHT);
                dialog.setCancelable(false);
                LayoutInflater inflater = getLayoutInflater();
                final View v=inflater.inflate(R.layout.adminpassword, null);
                dialog.setTitle("Login Password");
                dialog.setView(v);
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                adminpass = (EditText)v.findViewById(R.id.adminpassword);

                int maxLengthofEditText = 8;
                adminpass.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxLengthofEditText)});


                dialog.setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        hidepopupkeyboard(adminpass);
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        dialog.dismiss();

                        String password=null;
                        password=adminpass.getText().toString();

                        if(password!=null && password.length()>0 && password.length()==8){
                            //setting admin password to initiate and check  admin aunthetication
                           // StaticVariables.Password=password;

                            StaticVariabes_div.loggedpwd=password;
                            RegainingConnection();
                        }else{
                            adminPasswordCheck();
                            adminpass.setError("Please Enter 8 Characters Valid Login Password!");
                        }


                    }
                });
                AlertDialog d = dialog.create();
                d.getWindow().setSoftInputMode(
                        WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                d.show();
            }
        });
    }


    //exit confirmation dialog
    void ExitDialog()
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                AlertDialog.Builder dialog=new AlertDialog.Builder(Local_Remote_DownloadActivity.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle("Exit");
                dialog.setMessage("Do You Really Want To Exit from Configuration?");
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);

                dialog.setNegativeButton("NO",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                dialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        if(isHouseExits){
                            Intent it=new Intent(Local_Remote_DownloadActivity.this,Main_Navigation_Activity.class);
                            // adding transition to activity exit
                            overridePendingTransition(R.anim.slideup, R.anim.slidedown);
                            startActivity(it);
                            finish();
                        }else{
                            android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app

                        }
                    }
                });

                dialog.show();
            }
        });
    }


    public void onResume() {
        super.onResume();
        Log.i("local_remote_download", "Setting screen name: " + name);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


}
