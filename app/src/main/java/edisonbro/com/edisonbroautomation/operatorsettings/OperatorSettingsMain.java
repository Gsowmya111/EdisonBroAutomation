package edisonbro.com.edisonbroautomation.operatorsettings;


/**
 *  FILENAME: OperatorSettingsMain.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to view and set ip and option to add user ,edit user,delete user
 *  and to switch to activities like configuration main and gateway settings .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteCantOpenDatabaseException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.databasewired.CamAdapter;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewired.UserTableAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.IRB_Adapter_ir;
import edisonbro.com.edisonbroautomation.databasewireless.WirelessConfigurationAdapter;

import static edisonbro.com.edisonbroautomation.Connections.App.getActivity;

public class OperatorSettingsMain extends AppCompatActivity implements TcpTransfer,TotalListener{

    private static final String TAG1= " OperSettings - ";
    RadioButton rb_ipsettings,rb_usersettings,rb_configsettings,rb_irconfig,rb_serversettings;
    Button b_adduser,b_edituser,b_deluser,b_back,b_save,b_home;

    String netmask,gateway,strfull,ipnetgateway,etip,etport,etssid,strportfull,portserv,porttimer,portwireless,allports;

    /*private ServerDetailsAdapter sdadap;
    private static ServerAdapter servadap;
    private static UserTableAdapter usrtabadap;*/
    //*************************************************************************

    String wirelessport,timerportno, ipdb,portdb,ssiddb;

    String etusr,etpwd,logintype,usrtyp_old;
    String usrselected_fordel,tosendusr_todel,usrselected_forchange;
    boolean isuserselected_fordel;
    String Users_Array[];String Users_Type_Array[];
    Integer[] img_usertype;
    RadioButton rb_array[];


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
    Button btnconstatus,btsig;

    static String encryptionKey = "edisonbrosmartlabs";
    //********************************************************************
    private ArrayList<String> listdevices,listdevicesnumbers,listdevicesnames;
    String rgbnoarr[],rgbtypearr[], rgbmodeltypearr[],rgbdevnamearr[];
    String dimmrnoarr[],dimmrtypearr[],dimmrmodeltypearr[],dimmrdevnamearr[];
    String curnoarr[],curtypearr[],curmodeltypearr[],curdevnamearr[];
    String projscrnoarr[],projscrtypearr[],projscrmodeltypearr[],projscrdevnamearr[];
    String projliftnoarr[], projlifttypearr[],projliftmodeltypearr[],projliftdevnamearr[];
    String wtrpmpnoarr[], wtrpmptypearr[], wtrpmpmodeltypearr[], wtrpmpdevnamearr[];
    String fannoarr[], fantypearr[],fanmodeltypearr[],fandevnameearr[];
    String swbnoarr[], swbtypearr[],swbmodeltypearr[],swbdevnamearr[];
    String camnoarr[],camtypearr[],camdevnamarr[];
    String irnoarr[], irtypearr[] , irmodeltypearr[],irdevnamearr[];
    String gysernoarr[],gysertypearr[],gysermodeltypearr[],gyserdevnamearr[];
    String acnoarr[],actypearr[], acmodeltypearr[],acdevnamearr[];
    String pirnoarr[],pirtypearr[],pirmodeltypearr[],pirdevnamearr[];
    String gsknoarr[],gsktypearr[],gskmodeltypearr[],gskdevnamearr[];
    String clbnoarr[],clbtypearr[],clbmodeltypearr[],clbdevnamearr[];
    String dlsnoarr[],dlstypearr[],dlsmodeltypearr[],dlsdevnamearr[];
    String fmnoarr[],fmtypearr[],fmmodeltypearr[],fmdevnamearr[];
    String aqunoarr[],aqutypearr[], aqumodeltypearr[],aqudevnamearr[];
    String alexanoarr[],alexatypearr[], alexamodeltypearr[],alexadevnamearr[];
    String swdnoarr[], swdtypearr[],swdmodeltypearr[],swddevnamearr[];

    ArrayList<Integer> DeviceNumbersList = new ArrayList<Integer>();
    //*************************************************************************

    List<String> groupList, childList;
    Map<String, Map<String,String>> allItems;
    HouseConfigurationAdapter houseconfigDB;
    //****************************************************
    private static ServerAdapter servadap;
    private static UserTableAdapter usrtabadap;
    private SwbAdapter swbadap;
    private MasterAdapter mas_adap;
    private CamAdapter cam_adap;
    private IRB_Adapter_ir ir_adap;
    private ServerDetailsAdapter sdadap;
    //*****************************************************
    public ArrayList<ArrayList<String>> mGroupListmain_devnames;
    public ArrayList<ArrayList<String>> mGroupListmain;// = new ArrayList<>();
    public ArrayList<ArrayList<String>> mGroupListmainDev;// = new ArrayList<>();

    ////////////////////////////////////////////////////

    TextView mTextView;
    ExpandableListView exp_listView;
    Button btntest;
    ExpandListAdapter adapter;
    LocalDatabaseHelper dbhelp;
    //////////////////////////////////////
    String usrselected,usrtodelete,useraccess_roomnumbers,useraccess_devicenumbers,usrtoedit,usrseltoedit,usertyptoedit;
    boolean isusrselected,isusrselected_todelete,isusrselected_tochangerole;
    String[] Userrole_arr;Integer img_usertype_roll[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_operator_setting_app_nw);

      //  usrtabadap=new UserTableAdapter(this);
        btnconstatus=(Button) findViewById(R.id.btnconstatus);
        btsig=(Button) findViewById(R.id.btnsignal);
        b_home= (Button) findViewById(R.id.btnhome);

        rb_ipsettings= (RadioButton) findViewById(R.id.rb_ipsettings);
        rb_usersettings= (RadioButton) findViewById(R.id.rb_usersettings);
        rb_configsettings= (RadioButton) findViewById(R.id.rb_configsettings);
        rb_irconfig= (RadioButton) findViewById(R.id.rb_irsettings);
        rb_serversettings= (RadioButton) findViewById(R.id.rb_serversettings);


        rb_array=new RadioButton[]{rb_ipsettings,rb_usersettings,rb_configsettings,rb_irconfig,rb_serversettings};

        b_adduser= (Button) findViewById(R.id.btnadduser);
        b_edituser= (Button) findViewById(R.id.btnedituser);
        b_deluser= (Button) findViewById(R.id.btndeleteuser);
        b_back= (Button) findViewById(R.id.btnback);

        b_adduser.setEnabled(false);
        b_edituser.setEnabled(false);
        b_deluser.setEnabled(false);

        StaticVariables.udp_use=false;

        Disable_All();

        if((StaticVariabes_div.housename==null)||(StaticVariabes_div.housename=="")||(StaticVariabes_div.housename.equals("")) ){

            popup_dwn("Please Download House First");

        }else{

        dbhelp=new LocalDatabaseHelper(OperatorSettingsMain.this);


        dbhelp.opendb();
        Cursor cursd=dbhelp.fetch_logintype(StaticVariabes_div.housenumber);

        if(cursd!=null)
            logintype=cursd.getString(cursd.getColumnIndexOrThrow(LocalDatabaseHelper.LOGIN_ACCESS));
        dbhelp.close();


        if(logintype.equals("M")){

            popup_loginM("Please Login To Access");
        }
         else {

            if ((StaticVariabes_div.housename == null) || (StaticVariabes_div.housename == "") || (StaticVariabes_div.housename.equals(""))) {

                popup_dwn("Please Download House First");

            } else {
                CamAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                SwbAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                MasterAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                UserTableAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";

                try {
                    sdadap = new ServerDetailsAdapter(this);
                    swbadap = new SwbAdapter(this);
                    usrtabadap = new UserTableAdapter(this);
                    mas_adap = new MasterAdapter(this);
                    cam_adap = new CamAdapter(this);
                    // ir_adap = new IRB_Adapter_ir(this);
                } catch (Exception e) {
                    e.printStackTrace();
                }


                try {
                    houseconfigDB = new HouseConfigurationAdapter(this);
                    houseconfigDB.open();            //opening house database

                    servadap = new ServerAdapter(this);
                    ServerAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";

                    mas_adap.open();
                    swbadap.open();
                    usrtabadap.open();
                    // ir_adap.open();

                } catch (Exception e) {
                    e.printStackTrace();
                }

                Tcp_con mTcp = new Tcp_con(this);


                if (Tcp_con.isClientStarted) {

                    receiveddata(NetwrkType, StaticStatus.Network_Type, null);
                    receiveddata(ServStatus, StaticStatus.Server_status, null);

                } else {
                    Tcp_con.stacontxt = OperatorSettingsMain.this;
                    Tcp_con.serverdetailsfetch(this, StaticVariabes_div.housename);
                    Tcp_con.registerReceivers(OperatorSettingsMain.this);
                }


                if (StaticVariabes_div.loggeduser_type.equals("SA")) {
                    Enable_All();
                } else if (StaticVariabes_div.loggeduser_type.equals("A")) {
                    Enable_Admin();
                } else {
                    Disable_All();
                }

            }
         }

        }

        b_back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                usrtabadap.close();
                mas_adap.close();
                swbadap.close();
                Intent intnt=new Intent(OperatorSettingsMain.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });

        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                usrtabadap.close();
                mas_adap.close();
                swbadap.close();
                Intent intnt=new Intent(OperatorSettingsMain.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });

        rb_ipsettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_ipsettings.isChecked())
                {
                    CheckedStateUpdate(true,false,false,false,false);
                   colorchange(0);
                }

                serverdetailsfetch(StaticVariabes_div.housename);
                servernetmask(StaticVariabes_div.housename);
                popup_ip();


            }
        });


        rb_usersettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_usersettings.isChecked())
                {
                    CheckedStateUpdate(false,true,false,false,false);
                    colorchange(1);
                }


            }
        });


        b_adduser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                popup_user();
            }
        });


        b_edituser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

               // fetchusers();

                if(StaticVariabes_div.loggeduser_type.equals("SA")){
                    fetchusers_admin();
                }else {
                    fetchusers();
                }

               popusers_toedit("Select User To Edit");
            }
        });

        b_deluser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if(StaticVariabes_div.loggeduser_type.equals("SA")){
                    fetchusers_admin();
                }else {
                    fetchusers();
                }
               // Showallusers();

                popusers_todelete();
            }
        });



        rb_configsettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_configsettings.isChecked())
                {
                    CheckedStateUpdate(false,false,true,false,false);
                    colorchange(2);
                }

                Intent intt=new Intent(OperatorSettingsMain.this,Configuration_Main.class);
                startActivity(intt);
                finish();


            }
        });

        rb_irconfig.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_irconfig.isChecked())
                {
                    CheckedStateUpdate(false,false,false,true,false);
                    colorchange(3);
                }
            }
        });

        rb_serversettings.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_serversettings.isChecked())
                {
                    CheckedStateUpdate(false,false,false,false,true);
                    colorchange(4);
                }


               // Tcp_con.stopClient();
               // Tcp_con.isClientStarted = false;
                Intent intt=new Intent(OperatorSettingsMain.this,GatewaySettings_Tcp.class);
                startActivity(intt);
                finish();
            }
        });

        btnconstatus.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

            if(!Tcp_con.isClientStarted){
                Tcp_con.stacontxt=OperatorSettingsMain.this;
                Tcp_con.serverdetailsfetch(OperatorSettingsMain.this, StaticVariabes_div.housename);
                Tcp_con.registerReceivers(OperatorSettingsMain.this);

            }else{
                Toast.makeText(getApplicationContext(),"server is already connected ",Toast.LENGTH_SHORT).show();
            }


            }
        });



    }

    public void Enable_All(){
        rb_ipsettings.setEnabled(true);
        rb_usersettings.setEnabled(true);
        rb_configsettings.setEnabled(true);
        rb_irconfig.setEnabled(true);
        rb_serversettings.setEnabled(true);
    }

    public void Disable_All(){
        rb_ipsettings.setEnabled(false);
        rb_usersettings.setEnabled(false);
        rb_configsettings.setEnabled(false);
        rb_irconfig.setEnabled(false);
        rb_serversettings.setEnabled(false);
    }
    public void Enable_Admin(){
        rb_ipsettings.setEnabled(false);
        rb_ipsettings.setAlpha((float) 0.2);
        rb_usersettings.setEnabled(true);
       // rb_usersettings.setAlpha((float) 0.2);
        rb_configsettings.setEnabled(false);
        rb_configsettings.setAlpha((float) 0.2);
        rb_irconfig.setEnabled(false);
        rb_irconfig.setAlpha((float) 0.2);
        rb_serversettings.setEnabled(false);
        rb_serversettings.setAlpha((float) 0.2);
    }

    //************************************IP*********************************************


    public  void popup_ip(){


            LayoutInflater inflater = getLayoutInflater();
            View alertLayout = inflater.inflate(R.layout.popup_serverip, null);
            //final TextView et_ip = (TextView) alertLayout.findViewById(R.id.etip);
            final EditText et_ip = (EditText) alertLayout.findViewById(R.id.etip);
            final EditText et_port = (EditText) alertLayout.findViewById(R.id.etport);
            final EditText et_ssid = (EditText) alertLayout.findViewById(R.id.etssid);
            final Button b_save = (Button) alertLayout.findViewById(R.id.servsav);
            final Button b_cancel = (Button) alertLayout.findViewById(R.id.btncancel);

            AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.popup_theme_moodsett);

            // this is set the view from XML inside AlertDialog
            alert.setView(alertLayout);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(false);

            //  alert.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
            final AlertDialog dialog = alert.create();
            dialog.show();

            int width = (int)(getResources().getDisplayMetrics().widthPixels*0.90);
            int height = (int)(getResources().getDisplayMetrics().heightPixels*0.50);

            dialog.getWindow().setLayout(width, height);
            //   dialog.getWindow().setLayout(600, 700);
            et_port.setEnabled(false);
            et_ip.setText(ipdb);
            et_port.setText(portdb);
            et_ssid.setText(ssiddb);

            b_cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        b_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                etssid=et_ssid.getText().toString();
                etip=et_ip.getText().toString();
                if(etssid.length()>0&&etip.length()>0){

                    if(etip.length()>3&&etip.contains(".")){

                    StaticVariabes_div.log("etssid"+etssid, TAG1);
                    servadap.open();
                   boolean isupd= servadap.updateServerTable_ip_ssid(etip,etssid);
                   // boolean isupd= servadap.update_ssid_ServerTable(etssid);
                    servadap.close();

                        if(isupd){
                            popup("Updated Successfully");
                        }
                    }else{

                        popup("Enter Valid Ip");
                    }

                }else{

                    popup("Enter Valid Ssid And Ip");
                }


            }
        });

        }


    public  void serverdetailsfetch(String housename ){

        servadap.open();

        Cursor curem= servadap.fetch_detdb(1);

        if(curem!=null){

            ipdb  =curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_i));
            portdb=curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_p));
            ssiddb=curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_ss));

            wirelessport=curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_ea));
            timerportno=curem.getString(curem.getColumnIndexOrThrow(ServerAdapter.KEY_eb));

          /*  if(wirelessport!=null)
                StaticVariables.WIRELSESS_SERVER_PORT=Integer.parseInt(wirelessport);*/

            if(portdb!=null)
                StaticVariables.MAIN_SERVER_PORT=Integer.parseInt(portdb);


        }
        curem.close();
        servadap.close();

    }



    public  void servernetmask(String housename ){

        ServerDetailsAdapter.OriginalDataBase=housename+".db";
        sdadap=new ServerDetailsAdapter(this);
        sdadap.open();

        Cursor cur= sdadap.fetch_sd(1);

        if(cur!=null){

            strfull  =cur.getString(cur.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_dg));
            StaticVariabes_div.log("strfull"+strfull, TAG1);
            //String strA[] = SPLITDATA(strfull,3,3);
            /*try{
                String strA[] = strfull.split(":");
                netmask=strA[1];
                gateway=strA[2];
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Invalid Data ",Toast.LENGTH_SHORT).show();

            }
            StaticVariabes_div.log("netmask"+netmask+"gateway"+gateway, TAG1);*/
            strportfull  =cur.getString(cur.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_pn));
            StaticVariabes_div.log("strportfull"+strportfull, TAG1);
            //String strportA[] = SPLITDATA(strportfull,3,3);

            try{
                String strportA[] = strportfull.split(";");
                portserv=strportA[0];
                porttimer=strportA[1];
                portwireless=strportA[2];
                StaticVariabes_div.log("portserv"+portserv+"porttimer"+porttimer+"portwireless"+portwireless, TAG1);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Invalid Data ",Toast.LENGTH_SHORT).show();

            }

        }
        cur.close();
        sdadap.close();

    }

    public void CheckedStateUpdate(boolean ipstate,boolean userstate,boolean configstate,boolean irconfigstate,boolean serversettstate){

        rb_ipsettings.setChecked(ipstate);
        rb_usersettings.setChecked(userstate);
        rb_configsettings.setChecked(configstate);
        rb_irconfig.setChecked(irconfigstate);
        rb_serversettings.setChecked(serversettstate);

        if(userstate){
            b_adduser.setEnabled(true);
            b_edituser.setEnabled(true);
            b_deluser.setEnabled(true);
            b_adduser.setBackgroundResource(R.drawable.config_2);
            b_edituser.setBackgroundResource(R.drawable.config_2);
            b_deluser.setBackgroundResource(R.drawable.config_2);

        }else{
            b_adduser.setEnabled(false);
            b_edituser.setEnabled(false);
            b_deluser.setEnabled(false);
            b_adduser.setBackgroundResource(R.drawable.config1);
            b_edituser.setBackgroundResource(R.drawable.config1);
            b_deluser.setBackgroundResource(R.drawable.config1);
        }

    }



    public void colorchange(int posit){

        for(int k=0;k<rb_array.length;k++){
            rb_array[k].setTextColor(Color.BLACK);
        }

        rb_array[posit].setTextColor(Color.rgb(66,130,208));

    }

    public void read(final int type, final String stringData, final byte[] byteData)
    {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
        try {
            receiveddata(type, stringData, byteData);
        }catch(Exception e){

        }

            }
        });
    }







    ///****************************USER SETTINGS**************************************************************

    public  void popup_user(){


        final String[] usertype = {null};
        final String roomselected=null;

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.popup_adduser, null);

        final EditText et_user = (EditText) alertLayout.findViewById(R.id.etuser);
        final EditText et_pwd = (EditText) alertLayout.findViewById(R.id.etpwd);

        final Button b_submit = (Button) alertLayout.findViewById(R.id.btnsubmit);
        final Button b_cancel = (Button) alertLayout.findViewById(R.id.btncancel);



        final RadioButton rb_supradmin = (RadioButton) alertLayout.findViewById(R.id.rb_sa);
        final RadioButton rb_admin = (RadioButton) alertLayout.findViewById(R.id.rb_a);
        final RadioButton rb_user = (RadioButton) alertLayout.findViewById(R.id.rb_u);
        final RadioButton rb_guest = (RadioButton) alertLayout.findViewById(R.id.rb_g);

        final RadioGroup rb_group = (RadioGroup) alertLayout.findViewById(R.id.usergroup);
        //final RadioButton selectedRadioButton;

        AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);

        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(false);

        //  alert.getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.custom_title);
        final AlertDialog dialog = alert.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.97);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.40);

        dialog.getWindow().setLayout(width, height);
        //   dialog.getWindow().setLayout(600, 700);

        rb_supradmin.setEnabled(false);
        rb_supradmin.setVisibility(View.INVISIBLE);

        b_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();


                etusr=et_user.getText().toString();
                etpwd=et_pwd.getText().toString();

                if(etusr!=null&&etpwd!=null) {
                    if (etusr.length() == 10 && etpwd.length() == 8) {

                        if(rb_group.getCheckedRadioButtonId()==-1)
                        {
                            Toast.makeText(getApplicationContext(), "Please select usertype", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            // get selected radio button from radioGroup
                            int selectedId = rb_group.getCheckedRadioButtonId();

                            RadioButton selectedRadioButton = (RadioButton)alertLayout.findViewById(selectedId);
                           // Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" is selected", Toast.LENGTH_SHORT).show();

                            usertype[0] =selectedRadioButton.getText().toString();

                            String type_user="U";

                            if(usertype[0] !=null)
                                if(usertype[0].equals("User")){

                                    type_user="U";

                                } else if(usertype[0].equals("Guest")) {

                                    type_user="G";

                                } else if(usertype[0].equals("Admin")) {

                                    type_user="A";

                                } else if(usertype[0].equals("SuperAdmin")) {

                                    type_user="SA";
                                }


                            if(StaticVariabes_div.loggeduser_type.equals("A")&&(type_user.equals("U")||type_user.equals("G"))){
                               // Tcp_con.WriteBytes(("<"+etusr+etpwd+type_user+"@\r\n").getBytes());
                                String trrdat=etusr+etpwd+type_user;
                                Send_dat_aes(trrdat,"<","@");
                                StaticVariabes_div.log("Add Users "+"<"+etusr+etpwd+type_user,TAG1);
                                dialog.dismiss();
                            }else if(StaticVariabes_div.loggeduser_type.equals("SA")&&(type_user.equals("A")||type_user.equals("U")||type_user.equals("G"))){
                                //Tcp_con.WriteBytes(("<"+etusr+etpwd+type_user+"@\r\n").getBytes());
                                String trrdat=etusr+etpwd+type_user;
                                Send_dat_aes(trrdat,"<","@");
                                StaticVariabes_div.log("Add Users "+"<"+etusr+etpwd+type_user,TAG1);
                                dialog.dismiss();
                            }else{
                                Toast.makeText(getApplicationContext(), selectedRadioButton.getText().toString()+" Cannot Be Added Access Denied", Toast.LENGTH_LONG).show();

                            }




                        }



                    } else {

                        if(etusr.length() != 10) {
                            et_user.setError("username must be 10chars");
                        }
                        if(etpwd.length() != 8)
                        et_pwd.setError("Password  8chars");

                    }
                }else{
                    et_user.setError("username must be 10chars");
                    et_pwd.setError("Password  8chars");
                }


            }
        });


    }

    public void fetchusers_admin(){
        ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        sdadap=new ServerDetailsAdapter(this);

        sdadap.open();
        int usrcount=sdadap.getCount_allusers_admin();
        Users_Array = new String[usrcount];
        Users_Array=sdadap.getallusers_admin(usrcount);

        Users_Type_Array = new String[usrcount];
        for(int v=0;v<Users_Array.length;v++) {
            Cursor curem= sdadap.getallusers_types(Users_Array[v]);

            if(curem!=null) {
                Users_Type_Array[v] = curem.getString(curem.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                StaticVariabes_div.log("Users_Type_Array"+Users_Type_Array[v],TAG1);
            }

            curem.close();
        }

        sdadap.close();


        img_usertype=new Integer[usrcount];
        for(int v=0;v<Users_Type_Array.length;v++) {

            if(Users_Type_Array[v].equals("SA")){
                img_usertype[v]= R.drawable.typ_super_admin;
            }else if(Users_Type_Array[v].equals("A")){
                img_usertype[v]=R.drawable.typ_admin;
            }else if(Users_Type_Array[v].equals("U")){
                img_usertype[v]= R.drawable.typ_user;
            }else if(Users_Type_Array[v].equals("G")){
                img_usertype[v]= R.drawable.typ_guest;
            }

        }

    }
    public void fetchusers(){
        ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        sdadap=new ServerDetailsAdapter(this);

        sdadap.open();
        int usrcount=sdadap.getCount_allusers();
        Users_Array = new String[usrcount];
        Users_Array=sdadap.getallusers(usrcount);

        Users_Type_Array = new String[usrcount];
       for(int v=0;v<Users_Array.length;v++) {
           Cursor curem= sdadap.getallusers_types(Users_Array[v]);

           if(curem!=null) {
               Users_Type_Array[v] = curem.getString(curem.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
               StaticVariabes_div.log("Users_Type_Array"+Users_Type_Array[v],TAG1);
           }

           curem.close();
       }

        sdadap.close();


        img_usertype=new Integer[usrcount];
        for(int v=0;v<Users_Type_Array.length;v++) {

            if(Users_Type_Array[v].equals("SA")){
                img_usertype[v]= R.drawable.typ_super_admin;
            }else if(Users_Type_Array[v].equals("A")){
                img_usertype[v]=R.drawable.typ_admin;
            }else if(Users_Type_Array[v].equals("U")){
                img_usertype[v]= R.drawable.typ_user;
            }else if(Users_Type_Array[v].equals("G")){
                img_usertype[v]= R.drawable.typ_guest;
            }

        }

    }

    public  void popusers_toedit(String Msgg){
        isusrselected=false;
        ListView list;
        final TextView[] prevsel = new TextView[1];
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(Msgg);

        LayoutInflater inflater = getLayoutInflater();
        View usrLayout = inflater.inflate(R.layout.listlayout, null);
        alert.setView(usrLayout);

        CustomList cust_adapter = new
                CustomList(OperatorSettingsMain.this, Users_Array,img_usertype);
        list=(ListView) usrLayout.findViewById(R.id.list);


        list.setAdapter(cust_adapter);
        final int[] save = {-1};

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
              //  Toast.makeText(OperatorSettingsMain.this, Users_Type_Array[position]+"You Clicked at " +Users_Array[position], Toast.LENGTH_SHORT).show();

               // usrselected_temp_edit=Users_Array[position];
                isusrselected=true;
                usrseltoedit=Users_Array[position];
                usertyptoedit=Users_Type_Array[position];
                etusr=usrseltoedit;
                TextView tv = (TextView) view.findViewById(R.id.txt);


                if(prevsel[0]!=null)
                    prevsel[0].setTextColor(Color.GRAY);


                //tv.setTextColor(Color.BLUE);
                tv.setTextColor(Color.parseColor("#4282D0"));
                prevsel[0] =tv;

               /* if(parent.getChildAt(position)!=null)
                parent.getChildAt(position).setBackgroundColor(Color.CYAN);


                if (save[0] != -1 && save[0] != position){
                    if(parent.getChildAt(position)!=null)
                    parent.getChildAt(save[0]).setBackgroundColor(Color.WHITE);
                }

                save[0] = position;*/
            }
        });


        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        alert.setPositiveButton("EDIT", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                /*StaticVariabes_div.log("Edit selected users"+usertyptoedit, TAG1);
                if(isusrselected) {
                    if(!(usertyptoedit.equals("A"))) {
                        Create_popup_room_devices("EDIT","A");
                    }else{
                        Toast.makeText(OperatorSettingsMain.this, "Only User Access Can Be Edited", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_LONG).show();
                }*/
            }
        });




        alert.setNeutralButton("EDIT ROLES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

               /* StaticVariabes_div.log("Edit roles users", TAG1);
                if(isusrselected) {
                    sdadap.open();
                    Cursor curem= sdadap.getallusers_types(usrseltoedit);


                    if(curem!=null) {
                        usrtyp_old = curem.getString(curem.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                        StaticVariabes_div.log("usrtyp_old"+usrtyp_old,TAG1);
                       // StaticVariabes_div.loggeduser_type.equals("SA")

                        if(StaticVariabes_div.loggeduser_type.equals("A")){

                            if(usrtyp_old.equals("A")) {
                                img_usertype_roll=new Integer[2];
                                Userrole_arr=new String[2];
                                img_usertype_roll[0]= R.drawable.typ_user;
                                img_usertype_roll[1]= R.drawable.typ_guest;

                                Userrole_arr[0]="USER";
                                Userrole_arr[1]="GUEST";

                            }else if(usrtyp_old.equals("U")) {
                                img_usertype_roll=new Integer[1];
                                Userrole_arr=new String[1];
                                img_usertype_roll[0]= R.drawable.typ_guest;
                                Userrole_arr[0]="GUEST";

                            }else if(usrtyp_old.equals("G")) {

                                img_usertype_roll=new Integer[1];
                                Userrole_arr=new String[1];
                                img_usertype_roll[0]= R.drawable.typ_user;
                                Userrole_arr[0]="USER";
                            }

                        }else if(StaticVariabes_div.loggeduser_type.equals("SA")){


                            img_usertype_roll=new Integer[2];
                            Userrole_arr=new String[2];

                            if(usrtyp_old.equals("G")) {
                                img_usertype_roll[0] = R.drawable.typ_admin;
                                img_usertype_roll[1] = R.drawable.typ_user;


                                Userrole_arr[0] = "ADMIN";
                                Userrole_arr[1] = "USER";

                            }else if(usrtyp_old.equals("U")){
                                img_usertype_roll[0] = R.drawable.typ_admin;
                                img_usertype_roll[1] = R.drawable.typ_guest;

                                Userrole_arr[0] = "ADMIN";
                                Userrole_arr[1] = "GUEST";

                            }else if(usrtyp_old.equals("A")){
                                img_usertype_roll[0] = R.drawable.typ_user;
                                img_usertype_roll[1] = R.drawable.typ_guest;

                                Userrole_arr[0] = "USER";
                                Userrole_arr[1] = "GUEST";
                            }

                        }

                       popup_tochangeroles(usrseltoedit,usrtyp_old,Userrole_arr,img_usertype_roll);
                    }
                    sdadap.close();
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_SHORT).show();
                    popusers_toedit("Select User To Edit");
                }*/
            }
        });

        //alert.show();
        final AlertDialog mdialog=alert.create();
        mdialog.show();
        mdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariabes_div.log("Edit selected users"+usertyptoedit, TAG1);
                if(isusrselected) {
                    if(!(usertyptoedit.equals("A"))) {
                        mdialog.dismiss();
                        Create_popup_room_devices("EDIT","A");
                    }else{
                       // mdialog.dismiss();
                        Toast.makeText(OperatorSettingsMain.this, "Admin Access Cannot Be Edited", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_LONG).show();
                }
            }
        });


        mdialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariabes_div.log("Edit roles users", TAG1);
                if(isusrselected) {
                    sdadap.open();
                    Cursor curem= sdadap.getallusers_types(usrseltoedit);


                    if(curem!=null) {
                        usrtyp_old = curem.getString(curem.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                        StaticVariabes_div.log("usrtyp_old"+usrtyp_old,TAG1);
                        // StaticVariabes_div.loggeduser_type.equals("SA")

                        if(StaticVariabes_div.loggeduser_type.equals("A")){

                            if(usrtyp_old.equals("A")) {
                                img_usertype_roll=new Integer[2];
                                Userrole_arr=new String[2];
                                img_usertype_roll[0]= R.drawable.typ_user;
                                img_usertype_roll[1]= R.drawable.typ_guest;

                                Userrole_arr[0]="USER";
                                Userrole_arr[1]="GUEST";

                            }else if(usrtyp_old.equals("U")) {
                                img_usertype_roll=new Integer[1];
                                Userrole_arr=new String[1];
                                img_usertype_roll[0]= R.drawable.typ_guest;
                                Userrole_arr[0]="GUEST";

                            }else if(usrtyp_old.equals("G")) {

                                img_usertype_roll=new Integer[1];
                                Userrole_arr=new String[1];
                                img_usertype_roll[0]= R.drawable.typ_user;
                                Userrole_arr[0]="USER";
                            }

                        }else if(StaticVariabes_div.loggeduser_type.equals("SA")){


                            img_usertype_roll=new Integer[2];
                            Userrole_arr=new String[2];

                            if(usrtyp_old.equals("G")) {
                                img_usertype_roll[0] = R.drawable.typ_admin;
                                img_usertype_roll[1] = R.drawable.typ_user;


                                Userrole_arr[0] = "ADMIN";
                                Userrole_arr[1] = "USER";

                            }else if(usrtyp_old.equals("U")){
                                img_usertype_roll[0] = R.drawable.typ_admin;
                                img_usertype_roll[1] = R.drawable.typ_guest;

                                Userrole_arr[0] = "ADMIN";
                                Userrole_arr[1] = "GUEST";

                            }else if(usrtyp_old.equals("A")){
                                img_usertype_roll[0] = R.drawable.typ_user;
                                img_usertype_roll[1] = R.drawable.typ_guest;

                                Userrole_arr[0] = "USER";
                                Userrole_arr[1] = "GUEST";
                            }

                        }
                        mdialog.dismiss();
                        popup_tochangeroles(usrseltoedit,usrtyp_old,Userrole_arr,img_usertype_roll);
                    }
                    sdadap.close();
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_SHORT).show();
                   // popusers_toedit("Select User To Edit");
                }
            }
        });
    }

    public  void popup_tochangeroles(final String title, final String messg, final String rolesarray[], Integer imgarray[]){
        isusrselected_tochangerole=false;
        usrselected_forchange="";
        ListView list;
        final TextView[] prevsel = new TextView[1];
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Change Role Of : "+title);
        alert.setMessage("Present Role : "+messg);


        LayoutInflater inflater = getLayoutInflater();
        View usrLayout = inflater.inflate(R.layout.listlayout, null);
        alert.setView(usrLayout);

        CustomList cust_adapter = new
                CustomList(OperatorSettingsMain.this, rolesarray,imgarray);
        list=(ListView) usrLayout.findViewById(R.id.list);


        list.setAdapter(cust_adapter);
        final int[] save = {-1};

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(OperatorSettingsMain.this, "You Clicked at " +rolesarray[position], Toast.LENGTH_SHORT).show();

                if(rolesarray[position].equals("ADMIN")){
                    usrselected_forchange="A";
                }else if(rolesarray[position].equals("GUEST")){
                    usrselected_forchange="G";
                }else if(rolesarray[position].equals("USER")){
                    usrselected_forchange="U";
                }

                //usrselected_forchange=rolesarray[position];
                isusrselected_tochangerole=true;

                TextView tv = (TextView) view.findViewById(R.id.txt);


                if(prevsel[0]!=null)
                    prevsel[0].setTextColor(Color.GRAY);


                // tv.setTextColor(Color.BLUE);
                tv.setTextColor(Color.parseColor("#4282D0"));
                prevsel[0] =tv;

               /* if(parent.getChildAt(position)!=null)
                parent.getChildAt(position).setBackgroundColor(Color.CYAN);


                if (save[0] != -1 && save[0] != position){
                    if(parent.getChildAt(position)!=null)
                    parent.getChildAt(save[0]).setBackgroundColor(Color.WHITE);
                }

                save[0] = position;*/
            }
        });

        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        alert.setPositiveButton("Change", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                Toast.makeText(OperatorSettingsMain.this, "change role", Toast.LENGTH_SHORT).show();

               /*if(isusrselected_tochangerole) {
                    String tosen=title+":"+messg+":"+usrselected_forchange;
                Send_dat_aes(tosen,"<",")");
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select Role First", Toast.LENGTH_SHORT).show();
                }*/

            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
       // alert.show();

        final AlertDialog mdialog=alert.create();
        mdialog.show();
        mdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isusrselected_tochangerole) {
                    String tosen=title+":"+messg+":"+usrselected_forchange;
                    Send_dat_aes(tosen,"<",")");
                    mdialog.dismiss();
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select Role First", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public  void popusers_todelete(){
        isusrselected_todelete=false;
        ListView list;
        final TextView[] prevsel = new TextView[1];
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select User To Delete");

        LayoutInflater inflater = getLayoutInflater();
        View usrLayout = inflater.inflate(R.layout.listlayout, null);
        alert.setView(usrLayout);

        CustomList cust_adapter = new
                CustomList(OperatorSettingsMain.this, Users_Array,img_usertype);
        list=(ListView) usrLayout.findViewById(R.id.list);


        list.setAdapter(cust_adapter);
        final int[] save = {-1};

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(OperatorSettingsMain.this, "You Clicked at " +Users_Array[position], Toast.LENGTH_SHORT).show();

                usrselected=Users_Array[position];
                isusrselected_todelete=true;

                TextView tv = (TextView) view.findViewById(R.id.txt);


                if(prevsel[0]!=null)
                    prevsel[0].setTextColor(Color.GRAY);


               // tv.setTextColor(Color.BLUE);
                tv.setTextColor(Color.parseColor("#4282D0"));
                prevsel[0] =tv;

               /* if(parent.getChildAt(position)!=null)
                parent.getChildAt(position).setBackgroundColor(Color.CYAN);


                if (save[0] != -1 && save[0] != position){
                    if(parent.getChildAt(position)!=null)
                    parent.getChildAt(save[0]).setBackgroundColor(Color.WHITE);
                }

                save[0] = position;*/
            }
        });

        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        alert.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

              /*  if(isusrselected_todelete) {
                    popupdelete();
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_SHORT).show();
                }*/
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });


        //alert.show();
        final AlertDialog mdialog=alert.create();
        mdialog.show();
        mdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isusrselected_todelete) {
                    popupdelete();
                    mdialog.dismiss();
                }else{
                    Toast.makeText(OperatorSettingsMain.this, "Select User First", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
    public void popupdelete(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Delete User "+usrselected).setCancelable(false)

                .setPositiveButton("yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

if(StaticVariabes_div.loggeduser_type.equals("A")||StaticVariabes_div.loggeduser_type.equals("SA")) {
    popupdelete_timers();
}else{
    if(isusrselected_todelete){

        usrtodelete=usrselected;
        //   Tcp_con.WriteBytes(("<"+StaticVariabes_div.loggedpwd+usrtodelete+"?"+"\r\n").getBytes());

        StaticVariabes_div.log("delete"+"<"+StaticVariabes_div.loggedpwd+usrtodelete+"?", TAG1);

        String trrdat=StaticVariabes_div.loggedpwd+usrtodelete;
        Send_dat_aes(trrdat,"<","?");

        //clears the array used to store checked state
        isusrselected_todelete=false;
        usrselected="";
        //used to dismiss the dialog upon user selection.
        dialog.dismiss();
    }else{


        Toast.makeText(getApplicationContext(),
                "Please Select user", Toast.LENGTH_LONG).show();
    }



    dialog.cancel();
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


    public void popupdelete_timers(){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Delete User And Timers Set By User "+usrselected+" ? Then Select Yes Else Select No to Delete Only User.").setCancelable(false)

                .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {


                        if(isusrselected_todelete){

                            usrtodelete=usrselected;
                            //   Tcp_con.WriteBytes(("<"+StaticVariabes_div.loggedpwd+usrtodelete+"?"+"\r\n").getBytes());

                            StaticVariabes_div.log("delete"+"<"+StaticVariabes_div.loggedpwd+usrtodelete+"#", TAG1);

                            String trrdat=StaticVariabes_div.loggedpwd+usrtodelete;
                            Send_dat_aes(trrdat,"<","#");

                            //clears the array used to store checked state
                            isusrselected_todelete=false;
                            usrselected="";
                            //used to dismiss the dialog upon user selection.
                            dialog.dismiss();
                        }else{


                            Toast.makeText(getApplicationContext(),
                                    "Please Select user", Toast.LENGTH_LONG).show();
                        }

                    }
                });
        alertDialogBuilder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                dialog.cancel();

            }
        });

        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                if(isusrselected_todelete){

                    usrtodelete=usrselected;
                    //   Tcp_con.WriteBytes(("<"+StaticVariabes_div.loggedpwd+usrtodelete+"?"+"\r\n").getBytes());

                    StaticVariabes_div.log("delete"+"<"+StaticVariabes_div.loggedpwd+usrtodelete+"?", TAG1);

                    String trrdat=StaticVariabes_div.loggedpwd+usrtodelete;
                    Send_dat_aes(trrdat,"<","?");

                    //clears the array used to store checked state
                    isusrselected_todelete=false;
                    usrselected="";
                    //used to dismiss the dialog upon user selection.
                    dialog.dismiss();
                }else{


                    Toast.makeText(getApplicationContext(),
                            "Please Select user", Toast.LENGTH_LONG).show();
                }



                dialog.cancel();

            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
/*    public void Showallusers() {

        AlertDialog.Builder builder2=new AlertDialog.Builder(OperatorSettingsMain.this);

       // SimpleAdapter simpleAdapter=new SimpleAdapter(this, Users_Array, R.layout.list_item, new String[] {"U","G"}, new int[] {R.id.name,R.id.count});

       // SimpleAdapter simpleAdapter=new SimpleAdapter(this, Users_Array, R.layout.list_item, new String[] {"name","count"}, new int[] {R.id.name,R.id.count});

        builder2.setTitle("Choose a User")

                .setSingleChoiceItems(Users_Array, -1, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getApplicationContext(),
                                "user selected  is "+Users_Array[which], Toast.LENGTH_LONG).show();
                        usrselected_fordel=Users_Array[which];
                        isuserselected_fordel=true;
                        //dismissing the dialog when the user makes a selection.
                        //dialog.dismiss();
                    }
                })


                ///////////////////////////////////////////////////////////////////////
                .setPositiveButton("DELETE", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        if(isuserselected_fordel){
                            Toast.makeText(getApplicationContext(),
                                    "in del user", Toast.LENGTH_LONG).show();
                            tosendusr_todel="<"+"12345678"+usrselected_fordel+"?";
                            Tcp_con.WriteBytes((tosendusr_todel+"\r\n").getBytes());
                           // usrtodelete=usrselected;
                            *//*if(ops!=null){
                                try {
                                    ops.write((tosendusrselecteduser+"\r\n").getBytes());
                                    ops.flush();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }*//*

                            //clears the array used to store checked state
                            isuserselected_fordel=false;
                            usrselected_fordel="";
                            //used to dismiss the dialog upon user selection.
                            dialog.dismiss();
                        }else{


                            Toast.makeText(getApplicationContext(),
                                    "Please Select user", Toast.LENGTH_LONG).show();
                        }
                    }
                });

        ///////////////////////////////////////////////////////////////////////
        builder2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
                dialog.dismiss();
            }
        });

        AlertDialog alertdialog2=builder2.create();
        alertdialog2.show();

    }*/
    //&&&&&&&&&&&&&&&&&&&&&&&&&


public void ReadlineData( String RL){
    StaticVariabes_div.log("RL oper:- "+RL, TAG1);

    if(RL.startsWith("&")&&(RL.charAt((RL.length()-1))=='@')) {
        StaticVariabes_div.log("RL ins :- " + RL, TAG1);
        if (RL.contains("&004E@")) {
            runOnUiThread(new Runnable() {
                public void run() {
                    popupappdetails("User Already Exists");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.contains("&004B@")) {
            runOnUiThread(new Runnable() {
                public void run() {
                    popupappdetails("Error Creating User,Please Try Again Later");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.contains("&004D@")) {
            runOnUiThread(new Runnable() {
                public void run() {
                    popupappdetails("Error Adding User,Please Try Again Later");
                    //Toast.makeText(AddDelUsr.this, "Error Creating User", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.contains("&400U@")) {
            byte cipher[]=null;
            try {
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),etpwd.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdadap.open();
            sdadap.insertData("null", "null", "null", "null", cipher, "null", "null", "null", etusr, "null", "0", "U", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    //@@@@@onRoomCreateDialog();
                    Create_popup_room_devices("ADD", "A");

                    Toast.makeText(OperatorSettingsMain.this, etusr + " User Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();
        } else if (RL.contains("&400G@")) {
            byte cipher[]=null;
            try {
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),etpwd.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdadap.open();
            sdadap.insertData("null", "null", "null", "null", cipher, "null", "null", "null", etusr, "null", "0", "G", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    //@@@@@ onRoomCreateDialog();
                    Create_popup_room_devices("ADD", "A");
                    Toast.makeText(OperatorSettingsMain.this, etusr + " GuestUser Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();

        } else if (RL.contains("&400A@")) {
            byte cipher[]=null;
            try {
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),etpwd.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdadap.open();
            sdadap.insertData("null", "null", "null", "null", cipher, "null", "null", "null", etusr, "null", "0", "A", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    //onRoomCreateDialog();
                    Toast.makeText(OperatorSettingsMain.this, etusr + " AdminUser Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();
        } else if (RL.contains("&400SA@")) {
            byte cipher[]=null;
            try {
                cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),etpwd.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }
            sdadap.open();
            sdadap.insertData("null", "null", "null", "null", cipher, "null", "null", "null", etusr, "null", "0", "SA", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");

            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    // onRoomCreateDialog();
                    Toast.makeText(OperatorSettingsMain.this, etusr + " SuperAdmin Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();

        } else if (RL.contains("&700@")) {

            StaticVariabes_div.log("RL:- " + RL, TAG1);
            StaticVariabes_div.log("etusradd" + etusr, TAG1);
            StaticVariabes_div.log("useraccess_roomnumbers" + useraccess_roomnumbers, TAG1);
            // StaticVariabes_div.log("usrtoedit"+usrtoedit, TAG1);

            //   usrtabadap.open();

            Cursor usrcur = usrtabadap.fetch_User(usrtoedit);

            if (usrcur == null) {
                final long jk = usrtabadap.insertvalue(usrtoedit, useraccess_roomnumbers, useraccess_devicenumbers, "null", "null", "null", "null", "null", "null", "null", "null", "null");
                runOnUiThread(new Runnable() {
                    public void run() {
                        StaticVariabes_div.log(jk + "Room Access Added" + useraccess_roomnumbers, TAG1);
                        StaticVariabes_div.devswitchsselectedforuser = "";
                        StaticVariabes_div.devselectedforuser = "";
                        useraccess_devicenumbers = "";
                        useraccess_roomnumbers = "";
                        popupappdetails("Room Access Added Succesfully");
                    }
                });

            } else {
                final boolean upt = usrtabadap.updateuserAccess(usrtoedit, useraccess_roomnumbers, useraccess_devicenumbers);

                runOnUiThread(new Runnable() {
                    public void run() {
                        StaticVariabes_div.log(upt + "Room Access Updated" + useraccess_roomnumbers + "usr" + usrtoedit, TAG1);
                        StaticVariabes_div.devswitchsselectedforuser = "";
                        StaticVariabes_div.devselectedforuser = "";
                        useraccess_devicenumbers = "";
                        useraccess_roomnumbers = "";
                        popupappdetails("Room Access Updated Succesfully");
                        // Toast.makeText(AddDelUsr.this, upt+"Room Access Updated"+usrselectednumbers+"usr"+usrtoedit, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (usrcur != null)
                usrcur.close();
            // usrtabadap.close();


        } else if (RL.contains("&007B@")) {


            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    popupappdetails("Room Not Added Try Again");
                    StaticVariabes_div.devswitchsselectedforuser = "";
                    StaticVariabes_div.devselectedforuser = "";
                    useraccess_devicenumbers = "";
                    useraccess_roomnumbers = "";
                    //Toast.makeText(AddDelUsr.this, "Room Not Added Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.contains("&800@")) {

            StaticVariabes_div.log("RL val" + RL, TAG1);
            sdadap.open();
            boolean usrlocal = sdadap.deleteusr(usrtodelete);
            sdadap.close();
            //  usrtabadap.open();
            boolean usrlocalroom = usrtabadap.deleteusrrooms(usrtodelete);
            //  usrtabadap.close();

            StaticVariabes_div.log("usern" + usrtodelete + "usrlocal" + usrlocal + "usrlocalroom" + usrlocalroom, TAG1);
            if (usrlocal && usrlocalroom) {

                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User Deleted Successfully");
                        //Toast.makeText(AddDelUsr.this, "User Deleted", Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User Deleted Download Updated house");
                        //Toast.makeText(AddDelUsr.this, "User Deleted Download Updated house", Toast.LENGTH_SHORT).show();
                    }
                });
            }

        } else if (RL.contains("&008@")) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("User not Deleted Try Again");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (RL.contains("&110@")) {

            sdadap.open();
            boolean usrlocal = sdadap.deleteusr(usrtodelete);
            sdadap.close();
            //  usrtabadap.open();
            boolean usrlocalroom = usrtabadap.deleteusrrooms(usrtodelete);
            //  usrtabadap.close();

            StaticVariabes_div.log("usern" + usrtodelete + "usrlocal" + usrlocal + "usrlocalroom" + usrlocalroom, TAG1);
            if (usrlocal && usrlocalroom) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User And Timer Deleted Successfully");
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User And Timer Deleted Download Updated house");
                    }
                });
            }
        } else if (RL.contains("&011@")) {


            sdadap.open();
            boolean usrlocal = sdadap.deleteusr(usrtodelete);
            sdadap.close();
            //  usrtabadap.open();
            boolean usrlocalroom = usrtabadap.deleteusrrooms(usrtodelete);
            //  usrtabadap.close();

            StaticVariabes_div.log("usern" + usrtodelete + "usrlocal" + usrlocal + "usrlocalroom" + usrlocalroom, TAG1);
            if (usrlocal && usrlocalroom) {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User Deleted But Timer Was Not Deleted .Please Delete Timer From TimerList");
                    }
                });
            } else {
                runOnUiThread(new Runnable() {
                    public void run() {
                        popupappdetails("User Deleted But Timer Was Not Deleted .Please Delete Timer From TimerList. Download Updated house");
                    }
                });
            }

        } else if (RL.contains("&101@")) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("User And Timer Was Not Deleted .Please Try Again");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (RL.contains("&120A@")) {
            StaticVariabes_div.log("RL val" + RL, TAG1);
            sdadap.open();
            sdadap.update_useraccess(etusr, "A");
            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("Role Changed To Admin Successfully");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();

            Cursor usrcur = usrtabadap.fetch_User(etusr);

            if (usrcur == null) {

            } else {
                final boolean upt = usrtabadap.updateuserAccess(etusr, null, null);

                runOnUiThread(new Runnable() {
                    public void run() {
                        StaticVariabes_div.log(upt + "Room Access Updated to null usr" + etusr, TAG1);
                        // Toast.makeText(AddDelUsr.this, upt+"Room Access Updated"+usrselectednumbers+"usr"+usrtoedit, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (usrcur != null)
                usrcur.close();
        } else if (RL.contains("&120W@")) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("Error Role Not Changed");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (RL.contains("&012W@")) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("User Not Present");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (RL.contains("&120WL@")) {

            runOnUiThread(new Runnable() {
                public void run() {
                    //	onCreateDialog();
                    popupappdetails("Error Role Not Changed");
                    //Toast.makeText(AddDelUsr.this, "User not Deleted Try Again", Toast.LENGTH_SHORT).show();
                }
            });
        } else if (RL.contains("&120U@")) {
            sdadap.open();
            //sdadap.insertData("null", "null", "null", "null", etpwd, "null", "null", "null", etusr, "null", "0", "U", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            sdadap.update_useraccess(etusr, "U");

            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    //@@@@@onRoomCreateDialog();
                    Create_popup_room_devices("ADD", "B");

                    Toast.makeText(OperatorSettingsMain.this, etusr + " User Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();
        } else if (RL.contains("&120G@")) {

            sdadap.open();
            //  sdadap.insertData("null", "null", "null", "null", etpwd, "null", "null", "null", etusr, "null", "0", "G", "null", "null", "0", "0", "null", "null", "null", "null", "null", "null", "null", "null", "null", "null");
            sdadap.update_useraccess(etusr, "G");
            runOnUiThread(new Runnable() {
                public void run() {
                    Create_popup_room_devices("ADD", "B");
                    Toast.makeText(OperatorSettingsMain.this, etusr + " GuestUser Created Successfully", Toast.LENGTH_SHORT).show();
                }
            });
            sdadap.close();

        } else if (RL.contains("&120@")) {

            StaticVariabes_div.log("RL:- " + RL, TAG1);
            StaticVariabes_div.log("etusradd" + etusr, TAG1);
            StaticVariabes_div.log("useraccess_roomnumbers" + useraccess_roomnumbers, TAG1);
            // StaticVariabes_div.log("usrtoedit"+usrtoedit, TAG1);

            //   usrtabadap.open();

            Cursor usrcur = usrtabadap.fetch_User(usrtoedit);

            if (usrcur == null) {
                final long jk = usrtabadap.insertvalue(usrtoedit, useraccess_roomnumbers, useraccess_devicenumbers, "null", "null", "null", "null", "null", "null", "null", "null", "null");

                runOnUiThread(new Runnable() {
                    public void run() {
                        StaticVariabes_div.log(jk + "Room Access Added" + useraccess_roomnumbers, TAG1);
                        StaticVariabes_div.devswitchsselectedforuser = "";
                        StaticVariabes_div.devselectedforuser = "";
                        useraccess_devicenumbers = "";
                        useraccess_roomnumbers = "";
                        popupappdetails("Room Access Added Succesfully");
                    }
                });

            } else {
                final boolean upt = usrtabadap.updateuserAccess(usrtoedit, useraccess_roomnumbers, useraccess_devicenumbers);

                runOnUiThread(new Runnable() {
                    public void run() {
                        StaticVariabes_div.log(upt + "Room Access Updated" + useraccess_roomnumbers + "usr" + usrtoedit, TAG1);
                        StaticVariabes_div.devswitchsselectedforuser = "";
                        StaticVariabes_div.devselectedforuser = "";
                        useraccess_devicenumbers = "";
                        useraccess_roomnumbers = "";
                        popupappdetails("Room Access Updated Succesfully");
                        // Toast.makeText(AddDelUsr.this, upt+"Room Access Updated"+usrselectednumbers+"usr"+usrtoedit, Toast.LENGTH_SHORT).show();
                    }
                });
            }
            if (usrcur != null)
                usrcur.close();
            // usrtabadap.close();


        } else if (RL.contains("&120B@")) {


            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    popupappdetails("Room Not Added Try Again");
                    StaticVariabes_div.devswitchsselectedforuser = "";
                    StaticVariabes_div.devselectedforuser = "";
                    useraccess_devicenumbers = "";
                    useraccess_roomnumbers = "";
                    //Toast.makeText(AddDelUsr.this, "Room Not Added Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        } else if (RL.contains("&120ID@")) {


            runOnUiThread(new Runnable() {
                public void run() {
                    //onCreateDialog();
                    popupappdetails("Invalid Data");
                    StaticVariabes_div.devswitchsselectedforuser = "";
                    StaticVariabes_div.devselectedforuser = "";
                    useraccess_devicenumbers = "";
                    useraccess_roomnumbers = "";
                    //Toast.makeText(AddDelUsr.this, "Room Not Added Try Again", Toast.LENGTH_SHORT).show();
                }
            });

        }
    }
}
    private void popup_loginM(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(OperatorSettingsMain.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(OperatorSettingsMain.this);
                applnnameinfo.setTextSize(20);
                alert.setView(applnnameinfo);
                applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                Intent intnt=new Intent(OperatorSettingsMain.this, Main_Navigation_Activity.class);
                                startActivity(intnt);
                                finish();
                            }
                        });
                alert.show();
            }
        });

    }
    private void popup_dwn(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(OperatorSettingsMain.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(OperatorSettingsMain.this);
                applnnameinfo.setTextSize(20);
                alert.setView(applnnameinfo);
                applnnameinfo.setText(txt);

                alert.setNegativeButton("ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                usrtabadap.close();
                                mas_adap.close();
                                swbadap.close();
                                Intent intnt=new Intent(OperatorSettingsMain.this, Main_Navigation_Activity.class);
                                startActivity(intnt);
                                finish();
                            }
                        });
                alert.show();
            }
        });

    }

    private void popupappdetails(final String txt) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alert = new AlertDialog.Builder(OperatorSettingsMain.this);
                alert.setTitle("Info");
                //alert.setMessage("Enter Password");
                final TextView applnnameinfo = new TextView(OperatorSettingsMain.this);
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
/*
    public void network_signal(int signal1, final boolean serv) {

        if (serv) {
            //  btnwtype.setText("Remote");

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

            if (StaticStatus.Network_Type.equals("TRUE3G")) {
                btsig.setBackgroundResource(R.drawable.mobiledata);
            }

        } else {
            // btnwtype.setText("local");

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
        }

        if(StaticStatus.Network_Type.equals("NONET")){
            btsig.setBackgroundResource(R.drawable.no_network);
            // btnwtype.setText("no-net");
            btnconstatus.setBackgroundResource(R.drawable.not_connected);
        }

    }
*/

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



    public void testalert(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Select Room Access");
        LayoutInflater inflater = getLayoutInflater();
        View usrLayout = inflater.inflate(R.layout.pop_sel_dev_users, null);
        alert.setView(usrLayout);

        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                StaticVariabes_div.log("selected edit devices", TAG1);


            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
        alert.show();
    }

    public void Create_popup_room_devices(String Add_Edit, final String Add_update){
        adapter=null;
        exp_listView=null;
        mGroupListmain = new ArrayList<>();
        mGroupListmainDev= new ArrayList<>();
        mGroupListmain_devnames=new ArrayList<>();
        //groupList=null;

        StaticVariabes_div.log("createpopup",TAG1);

        for (int test=0;test< StaticVariabes_div.roomnameusrlistarr.length;test++){

            StaticVariabes_div.log("roomnameusrlistarr"+StaticVariabes_div.roomnameusrlistarr[test],TAG1);
        }

        fill_Group();

        Fill_Child();

             final AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Select Room Access");
            LayoutInflater inflater = getLayoutInflater();
            View usrLayout = inflater.inflate(R.layout.pop_sel_dev_users, null);
            alert.setView(usrLayout);

        btntest= (Button) usrLayout.findViewById(R.id.button);
        mTextView = (TextView) usrLayout.findViewById(R.id.header);
        exp_listView = (ExpandableListView) usrLayout.findViewById(R.id.expandable_list);
      //  adapter = new ExpandListAdapter(this,mGroupListmain,StaticVariabes_div.roomnamearr);
       // adapter = new ExpandListAdapter(this,mGroupListmain,StaticVariabes_div.roomnameusrlistarr,Add_Edit);
        adapter = new ExpandListAdapter(this,mGroupListmain_devnames,StaticVariabes_div.roomnameusrlistarr,Add_Edit);
        adapter.setmListener(this);
        exp_listView.setAdapter(adapter);

        final String[] test = {""};
       final String[] dev = {""};


       //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
            alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                 /*   StaticVariabes_div.log("selected edit devices", TAG1);

                    ArrayList<Boolean> arr=adapter.selectedChildCheckBoxStates.get(0);
                    Set<String> roomnum = new LinkedHashSet<>();
                    Set<String> devnum = new LinkedHashSet<>();

                StaticVariabes_div.log("size selected"+adapter.selectedChildCheckBoxStates.get(0).size(), TAG1);

                    for(int l = 0 ; l < groupList.size(); l++) {
                        StaticVariabes_div.log("roomnumb" + houseconfigDB.CurrentRoomNumber(groupList.get(l)), TAG1);

                        for (int i = 0; i < adapter.selectedChildCheckBoxStates.get(l).size(); i++) {
                            StaticVariabes_div.log("TAG", "I = " + i);

                            if (adapter.selectedChildCheckBoxStates.get(l).get(i)) {
                                test[0] = mGroupListmain.get(l).get(i) + test[0];
                                devnum.add(mGroupListmainDev.get(l).get(i));

                                //  dev[0]=mGroupListmainDev.get(0).get(i)+dev[0]+";";
                                roomnum.add("" + houseconfigDB.CurrentRoomNumber(groupList.get(l)));
                            }
                        }
                    }
                    StaticVariabes_div.log("test"+test[0],TAG1);
                    StaticVariabes_div.log("dev"+dev[0],TAG1);

                    String[] movieArray = roomnum.toArray(new String[roomnum.size()]);

                    String roomnumbrs="0";
                    for (int i = 0; i < movieArray.length; i++) {

                        StaticVariabes_div.log("roomnum"+movieArray[i],TAG1);
                        roomnumbrs=movieArray[i]+";";
                    }

                    String devnumarr[]=devnum.toArray(new String[devnum.size()]);

                    for (int i = 0; i < devnumarr.length; i++) {

                        StaticVariabes_div.log("devnum"+devnumarr[i],TAG1);
                        dev[0]= dev[0]+devnumarr[i]+";";
                    }

                    //String datatoaddaccess=
                    useraccess_roomnumbers=roomnumbrs;
                    useraccess_devicenumbers=dev[0];

                    usrtoedit=etusr;

                 //   Tcp_con.WriteBytes(("<"+etusr+":"+roomnumbrs+":"+dev[0]+"}\r\n").getBytes());

                    String trrdat=etusr+":"+roomnumbrs+":"+dev[0];

                    if(roomnumbrs.equals("0")||(dev[0].equals("0"))) {
                        Toast.makeText(OperatorSettingsMain.this,"Please Select Atleast one device",Toast.LENGTH_LONG).show();

                    }else{
                        if (Add_update.equals("A")) {
                            Send_dat_aes(trrdat, "<", "}");
                        } else {
                            Send_dat_aes(trrdat, "<", "$");
                        }
                    }
                    StaticVariabes_div.log("datatoaddaccess"+"<"+etusr+":"+roomnumbrs+":"+dev[0]+"}",TAG1);*/

                }
            });

            alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {


                }
            });
           // alert.show();

        final AlertDialog mdialog=alert.create();
        mdialog.show();
        mdialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StaticVariabes_div.log("selected edit devices", TAG1);

                ArrayList<Boolean> arr=adapter.selectedChildCheckBoxStates.get(0);
                Set<String> roomnum = new LinkedHashSet<>();
                Set<String> devnum = new LinkedHashSet<>();

                StaticVariabes_div.log("size selected"+adapter.selectedChildCheckBoxStates.get(0).size(), TAG1);

                for(int l = 0 ; l < groupList.size(); l++) {
                    StaticVariabes_div.log("roomnumb" + houseconfigDB.CurrentRoomNumber(groupList.get(l)), TAG1);

                    for (int i = 0; i < adapter.selectedChildCheckBoxStates.get(l).size(); i++) {
                        StaticVariabes_div.log("TAG", "I = " + i);

                        if (adapter.selectedChildCheckBoxStates.get(l).get(i)) {
                            test[0] = mGroupListmain.get(l).get(i) + test[0];
                            devnum.add(mGroupListmainDev.get(l).get(i));

                            //  dev[0]=mGroupListmainDev.get(0).get(i)+dev[0]+";";
                            roomnum.add("" + houseconfigDB.CurrentRoomNumber(groupList.get(l)));
                        }
                    }
                }
                StaticVariabes_div.log("test"+test[0],TAG1);
                StaticVariabes_div.log("dev"+dev[0],TAG1);

                String[] movieArray = roomnum.toArray(new String[roomnum.size()]);

                String roomnumbrs="0";
                for (int i = 0; i < movieArray.length; i++) {

                    StaticVariabes_div.log("roomnum"+movieArray[i],TAG1);
                    roomnumbrs=movieArray[i]+";";
                }

                String devnumarr[]=devnum.toArray(new String[devnum.size()]);

                for (int i = 0; i < devnumarr.length; i++) {

                    StaticVariabes_div.log("devnum"+devnumarr[i],TAG1);
                    dev[0]= dev[0]+devnumarr[i]+";";
                }

                //String datatoaddaccess=
                useraccess_roomnumbers=roomnumbrs;
                useraccess_devicenumbers=dev[0];

                usrtoedit=etusr;

                //   Tcp_con.WriteBytes(("<"+etusr+":"+roomnumbrs+":"+dev[0]+"}\r\n").getBytes());

                String trrdat=etusr+":"+roomnumbrs+":"+dev[0];

                if(roomnumbrs.equals("0")||(dev[0].equals("0"))) {
                    Toast.makeText(OperatorSettingsMain.this,"Please Select Atleast one device",Toast.LENGTH_LONG).show();

                }else{
                    if (Add_update.equals("A")) {
                        Send_dat_aes(trrdat, "<", "}");
                    } else {
                        Send_dat_aes(trrdat, "<", "$");
                    }
                    mdialog.dismiss();
                }
                StaticVariabes_div.log("datatoaddaccess"+"<"+etusr+":"+roomnumbrs+":"+dev[0]+"}",TAG1);
            }
        });
    }


   /* public void Send_Timer_dat(String StrData,String starttokn,String endtokn){

        StaticVariabes_div.log(StrData.length()+"StrData"+StrData,TAG1);

        //byte[]  cipher = xorWithKey(StrData.getBytes(), encryptionKey.getBytes());
        byte[] encodeValue = Base64.encode(StrData.getBytes(), Base64.DEFAULT);

        String tosend=new String(encodeValue);
        tosend=starttokn+tosend+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);

        Tcp_con.WriteBytes(temp_str.getBytes());
    }*/

    public void Send_dat_aes(String StrTimer,String starttokn,String endtokn){

        String tosend=null;
        tosend=starttokn+StrTimer+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);
        Tcp_con.WriteBytes(temp_str.getBytes());
    }

    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }

    public  void fill_Group(){

        StaticVariabes_div.log("fill grp","Create_popup_room_devices");
       groupList = new ArrayList<String>();
       // groupList.addAll(Arrays.asList(StaticVariabes_div.roomnameusrlistarr)); StaticVariabes_div.roomnamearr
        groupList.addAll(Arrays.asList(StaticVariabes_div.roomnameusrlistarr));
    }

    public void Fill_Child(){
        StaticVariabes_div.log("child popup","Create_popup_room_devices");
        String roomName=null;
        int CurrentRoomNo=0;
       // allItems = new LinkedHashMap<String, Map<String,String>>();
        //getting all the devices from each room
        for(int i=0;i<groupList.size();i++){

            //getting current room name
            roomName=groupList.get(i);

            //getting current room number
//          CurrentRoomNo=houseconfigDB.CurrentRoomNumber(roomName);

           // String rn=Integer.toString(CurrentRoomNo);



            try {
                String rn = swbadap.getroomno(roomName);
                StaticVariabes_div.log("roomno in switchboard rn 1 "+rn, TAG1);
                if ((rn == null)||(rn.length()<1)) {
                    rn = mas_adap.getroomno(roomName);
                    StaticVariabes_div.log("roomno in master rn 1 "+rn, TAG1);
                }
                StaticVariabes_div.log("Edit room number before wireless 1 " + rn, TAG1);

                if (rn == null||(rn.length()<1)) {
                    StaticVariabes_div.log("Edit room number in wireless 1 " + rn, TAG1);
                    WirelessConfigurationAdapter WhouseDB = new WirelessConfigurationAdapter(OperatorSettingsMain.this);

                    boolean isDbExists = WhouseDB.checkdb();
                    if (isDbExists) {
                        //opening database
                        WhouseDB.open();

                        //getting room number from wireless database
                        rn = ""+WhouseDB.wirlessRoomNumber(roomName);
                        StaticVariabes_div.log("roomno in wireless rn 1 "+rn, TAG1);
                        //closing database
                        WhouseDB.close();
                    }else{
                        StaticVariabes_div.log("Edit room number db not exist 1 " + rn, TAG1);
                    }

                }

                if (rn != null)
                    prepareList(rn);
            }catch (SQLiteCantOpenDatabaseException sce){
               Toast.makeText(OperatorSettingsMain.this,"excep fill child database",Toast.LENGTH_LONG).show();
              /*  mas_adap.open();
                swbadap.open();
                try {
                    prepareList(rn);
                }catch (SQLiteCantOpenDatabaseException sde) {

                }*/
            }catch(Exception e){


            }

            ArrayList<String> dev_names = new ArrayList<>();
            for(int j = 0; j < StaticVariabes_div.roomdevicesnamelistarr.length; j++) {
                dev_names.add(StaticVariabes_div.roomdevicesnamelistarr[j]);
            }
            mGroupListmain_devnames.add(i, dev_names);

                ArrayList<String> prices = new ArrayList<>();
                for(int j = 0; j < StaticVariabes_div.roomdeviceslistarr.length; j++) {
                    prices.add(StaticVariabes_div.roomdeviceslistarr[j]);
                }
                mGroupListmain.add(i, prices);


                ArrayList<String> devnums = new ArrayList<>();
                for(int j = 0; j < StaticVariabes_div.roomdevicesnumberlistarr.length; j++) {
                    devnums.add(StaticVariabes_div.roomdevicesnumberlistarr[j]);
                }

                mGroupListmainDev.add(i,devnums);



          //  }

           /* if(map.size()>0){
                // making key and value pair
                allItems.put(roomName, map);
            }else{
                //remove room name from group list
                groupList.remove(roomName);
                i--;
            }*/
        }
    }

    public void prepareList(String roomno)
    {

        StaticVariabes_div.log("Edit in preparelist roomno"+roomno, TAG1);
        listdevices = new ArrayList<String>();
        listdevicesnumbers = new ArrayList<String>();
        listdevicesnames = new ArrayList<String>();

        String[] finallistarr=null;
        String[] finaldevnumlistarr=null;
        String[]finaldevnamelistarr=null;
        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
/*       // ir_adap.open();
        int rno=Integer.parseInt(roomno);
        DeviceNumbersList = ir_adap.fetchDeviceNumberList(rno);
        StaticVariabes_div.log("ir DeviceNumbersList"+DeviceNumbersList.size(), TAG1);

	*//*if(DeviceNumbersList.size()>0){
			listname.add("ir");
			listicon.add(R.drawable.ic_ir);
	}*//*

        String[] irnumbArr = new String[DeviceNumbersList.size()];

        List<String> irstrings = Arrays.asList(DeviceNumbersList.toString().replaceAll("\\[(.*)\\]", "$1").split(", "));

        //irnumbArr = DeviceNumbersList.toArray(irnumbArr);
        irnumbArr = irstrings.toArray(irnumbArr);
        String irmodels[]=new String[irnumbArr.length];
        String irnames[]=new String[irnumbArr.length];

        for(int k=0;k<irnumbArr.length;k++){
            irmodels[k]="ir"+k+"-"+irnumbArr[k];
            irnames[k]="ir"+irnumbArr[k];
            StaticVariabes_div.log("irmodels"+irmodels[k], TAG1);
        }

        if(DeviceNumbersList.size()!=0){
            listdevices.addAll(Arrays.asList(irmodels));
            listdevicesnumbers.addAll(Arrays.asList(irnumbArr));
            listdevicesnames.addAll(Arrays.asList(irnames));
        }

       // ir_adap.close();*/

//****************************************************************************************************
       // swbadap.open();
        int swbcount=swbadap.getCount_housenoroomnodevtypename(roomno,"SWB");
        swbnoarr=new String[swbcount];
        swbtypearr=new String[swbcount];
        swbmodeltypearr=new String[swbcount];
        swbdevnamearr=new String[swbcount];

        swbnoarr=swbadap.getall_housenoroomnodevicetypename(swbcount, roomno, "SWB");
        swbmodeltypearr=swbadap.getall_devicetypemodel(swbcount, roomno, "SWB");
        swbdevnamearr=swbadap.getall_devicenames(swbcount, roomno, "SWB");
       // swbadap.getdevnam_frmdevno()

        StaticVariabes_div.log("swb count"+swbcount, TAG1);
        String swbmodels[]=new String[swbmodeltypearr.length];

        for(int k=0;k<swbmodeltypearr.length;k++){
            swbmodels[k]=swbmodeltypearr[k]+"-"+swbnoarr[k];
            StaticVariabes_div.log("swbmodel"+swbmodels[k], TAG1);
        }

        if(swbcount!=0){
            listdevices.addAll(Arrays.asList(swbmodels));
            listdevicesnumbers.addAll(Arrays.asList(swbnoarr));
            listdevicesnames.addAll(Arrays.asList(swbdevnamearr));
        }

        int swdcount=swbadap.getCount_housenoroomnodevtypename(roomno,"SWD");
        swdnoarr=new String[swdcount];
        swdtypearr=new String[swdcount];
        swdmodeltypearr=new String[swdcount];
        swddevnamearr=new String[swdcount];

        swdnoarr=swbadap.getall_housenoroomnodevicetypename(swdcount, roomno, "SWD");
        swdmodeltypearr=swbadap.getall_devicetypemodel(swdcount, roomno, "SWD");
        swddevnamearr=swbadap.getall_devicenames(swbcount, roomno, "SWD");


        StaticVariabes_div.log("swd count"+swdcount, TAG1);
        String swdmodels[]=new String[swdmodeltypearr.length];

        for(int k=0;k<swdmodeltypearr.length;k++){
            swdmodels[k]=swdmodeltypearr[k]+"-"+swdnoarr[k];
            StaticVariabes_div.log("swdmodel"+swdmodels[k], TAG1);
        }

        if(swdcount!=0){
            listdevices.addAll(Arrays.asList(swdmodels));
            listdevicesnumbers.addAll(Arrays.asList(swdnoarr));
            listdevicesnames.addAll(Arrays.asList(swddevnamearr));
        }
       // swbadap.close();
        //***********************************************************************************

  /*      int cameracount=0;
        cam_adap.open();
        cameracount=cam_adap.getCountroomno(roomno);
        camnoarr=new String[cameracount];
        camtypearr=new String[cameracount];
       // camdevnamesearr=new String[cameracount];

        if(cameracount>0){
            camnoarr=cam_adap.getallcamnoroomno(cameracount, roomno);
            camtypearr=cam_adap.getallcamnameroomno(cameracount, roomno);
            //camdevnamesearr=cam_adap.getallcamnameroomno(cameracount, roomno);
        }
        StaticVariabes_div.log("cam count"+cameracount+"roomno"+roomno, TAG1);
        String cammodels[]=new String[camnoarr.length];
        String camnames[]=new String[camnoarr.length];
        for(int k=0;k<camnoarr.length;k++){
            cammodels[k]=camtypearr[k]+"-"+camnoarr[k];
            camnames[k]="cam"+"-"+camnoarr[k];
            StaticVariabes_div.log("cammodel"+cammodels[k], TAG1);
        }

        if(cameracount!=0){
            listdevices.addAll(Arrays.asList(cammodels));
            listdevicesnumbers.addAll(Arrays.asList(camnoarr));
            listdevicesnames.addAll(Arrays.asList(camnames));
        }
        cam_adap.close();*/

        //>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
       // mas_adap.open();


        //................................................................................
        int rgbcount=mas_adap.getCount_housenoroomnodevtypename(roomno,"RGB");
        rgbnoarr=new String[rgbcount];
        rgbtypearr=new String[rgbcount];
        rgbmodeltypearr=new String[rgbcount];
        rgbdevnamearr=new String[rgbcount];

        rgbnoarr=mas_adap.getall_housenoroomnodevicetypename(rgbcount, roomno, "RGB");
        rgbmodeltypearr=mas_adap.getall_devicetypemodel(rgbcount, roomno, "RGB");
        rgbdevnamearr=mas_adap.getall_roomnodevicenames(rgbcount, roomno, "RGB");
        StaticVariabes_div.log("rgb count"+rgbcount, TAG1);

        String rgbmodels[]=new String[rgbmodeltypearr.length];

        for(int k=0;k<rgbmodeltypearr.length;k++){
            rgbmodels[k]=rgbmodeltypearr[k]+"-"+rgbnoarr[k];
            StaticVariabes_div.log("rgbmodel"+rgbmodels[k], TAG1);
        }


        if(rgbcount!=0){
            listdevices.addAll(Arrays.asList(rgbmodels));
            listdevicesnumbers.addAll(Arrays.asList(rgbnoarr));
            listdevicesnames.addAll(Arrays.asList(rgbdevnamearr));
        }
        //................................................................................
        int dmrcount=mas_adap.getCount_housenoroomnodevtypename(roomno,"DMR");
        dimmrnoarr=new String[dmrcount];
        dimmrtypearr=new String[dmrcount];
        dimmrmodeltypearr=new String[dmrcount];
        dimmrdevnamearr=new String[dmrcount];

        dimmrnoarr=mas_adap.getall_housenoroomnodevicetypename(dmrcount, roomno, "DMR");
        dimmrmodeltypearr=mas_adap.getall_devicetypemodel(dmrcount, roomno, "DMR");
        dimmrdevnamearr=mas_adap.getall_roomnodevicenames(dmrcount, roomno, "DMR");

        StaticVariabes_div.log("dimmr count"+dmrcount, TAG1);
        String dmrmodels[]=new String[dimmrmodeltypearr.length];

        for(int k=0;k<dimmrmodeltypearr.length;k++){
            dmrmodels[k]=dimmrmodeltypearr[k]+"-"+dimmrnoarr[k];
            StaticVariabes_div.log("dmrmodels"+dmrmodels[k], TAG1);
        }
        if(dmrcount!=0){
            listdevices.addAll(Arrays.asList(dmrmodels));
            listdevicesnumbers.addAll(Arrays.asList(dimmrnoarr));
            listdevicesnames.addAll(Arrays.asList(dimmrdevnamearr));
        }
        //................................................................................

        int curcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"CUR");
        curnoarr=new String[curcount];
        curtypearr=new String[curcount];
        curmodeltypearr=new String[curcount];
        curdevnamearr=new String[curcount];


        curnoarr=mas_adap.getall_housenoroomnodevicetypename(curcount, roomno,"CUR");
        curmodeltypearr=mas_adap.getall_devicetypemodel(curcount, roomno,"CUR");
        curdevnamearr=mas_adap.getall_roomnodevicenames(curcount, roomno,"CUR");

        StaticVariabes_div.log("cur count"+curcount, TAG1);

        String curtmodels[]=new String[curmodeltypearr.length];

        for(int k=0;k<curmodeltypearr.length;k++){
            curtmodels[k]=curmodeltypearr[k]+"-"+curnoarr[k];
            StaticVariabes_div.log("curtmodels"+curtmodels[k], TAG1);
        }
        if(curcount!=0){
            listdevices.addAll(Arrays.asList(curtmodels));
            listdevicesnumbers.addAll(Arrays.asList(curnoarr));
            listdevicesnames.addAll(Arrays.asList(curdevnamearr));

        }

        for(int k=0;k<curmodeltypearr.length;k++){
            StaticVariabes_div.log("cur curmodeltypearr"+curmodeltypearr[k], TAG1);
        }
        for(int k=0;k<curnoarr.length;k++){
            StaticVariabes_div.log("cur curnoarr"+curnoarr[k], TAG1);
        }
        //................................................................................

		/*int ircount=mas_adap.getCount_housenoroomnodevtypename(roomno,"IRB");
		    	irnoarr=new String[ircount];
		    	irtypearr=new String[ircount];
		    	irmodeltypearr=new String[ircount];
		    	irnoarr=mas_adap.getall_housenoroomnodevicetypename(ircount, roomno, "IRB");
		    	irmodeltypearr=mas_adap.getall_devicetypemodel(ircount, roomno, "IRB");
		    	Log.d("msg", "ir count"+ircount);

		    	if(ircount!=0){
		       		   listname.add("ir");
	         		   listicon.add(R.drawable.dimmer_icon);
		        }  	*/
		/*irb_adapter.open();
		int rno=Integer.parseInt(roomno);
		DeviceNumbersList = irb_adapter
				.fetchDeviceNumberList(rno);
		Log.d("msg", "ir DeviceNumbersList"+DeviceNumbersList.size());
		if(DeviceNumbersList.size()>0){
			listname.add("ir");
			listicon.add(R.drawable.ic_ir);
		}

		irb_adapter.close();*/
        //................................................................................
        int fancount=mas_adap.getCount_housenoroomnodevtypename( roomno,"FAN");
        fannoarr=new String[fancount];
        fantypearr=new String[fancount];
        fanmodeltypearr=new String[fancount];
        fandevnameearr=new String[fancount];

        fannoarr=mas_adap.getall_housenoroomnodevicetypename(fancount, roomno,"FAN");
        fanmodeltypearr=mas_adap.getall_devicetypemodel(fancount, roomno,"FAN");
        fandevnameearr=mas_adap.getall_roomnodevicenames(fancount, roomno,"FAN");
        StaticVariabes_div.log("fan count"+fancount, TAG1);
        String fanmodels[]=new String[fanmodeltypearr.length];

        for(int k=0;k<fanmodeltypearr.length;k++){
            fanmodels[k]=fanmodeltypearr[k]+"-"+fannoarr[k];
            StaticVariabes_div.log("fanmodels"+fanmodels[k], TAG1);
        }

        if(fancount!=0){
            listdevices.addAll(Arrays.asList(fanmodels));
            listdevicesnumbers.addAll(Arrays.asList(fannoarr));
            listdevicesnames.addAll(Arrays.asList(fandevnameearr));

        }
        //...........................................................................................
 /*       int wtrpmpcount=mas_adap.wtrgetCount_housenoroomnodevtypename( roomno,"WPC","WSM1","WBM1","WSS1","WOTS");
        wtrpmpnoarr=new String[wtrpmpcount];
        wtrpmptypearr=new String[wtrpmpcount];
        wtrpmpmodeltypearr=new String[wtrpmpcount];

        wtrpmpnoarr=mas_adap.wtrgetall_housenoroomnodevicetypename(wtrpmpcount, roomno,"WPC","WSM1","WBM1","WSS1","WOTS");
        wtrpmpmodeltypearr=mas_adap.wtrgetall_devicetypemodel(wtrpmpcount, roomno,"WPC","WSM1","WBM1","WSS1","WOTS");
        StaticVariabes_div.log("wtr count", TAG1);
        String wtrpmpmodels[]=new String[wtrpmpmodeltypearr.length];

        for(int k=0;k<wtrpmpmodeltypearr.length;k++){
            wtrpmpmodels[k]=wtrpmpmodeltypearr[k]+"-"+wtrpmpnoarr[k];
            StaticVariabes_div.log("wtrpmpmodels"+wtrpmpmodels[k], TAG1);
        }

        if(wtrpmpcount!=0){
            listdevices.addAll(Arrays.asList(wtrpmpmodels));
            listdevicesnumbers.addAll(Arrays.asList(wtrpmpnoarr));

        }
        for(int k=0;k<wtrpmpmodeltypearr.length;k++){
            StaticVariabes_div.log("wtr wtrpmpmodeltypearr"+wtrpmpmodeltypearr[k], TAG1);
        }
        for(int k=0;k<wtrpmpnoarr.length;k++){
            StaticVariabes_div.log("wtr wtrpmpnoarr"+wtrpmpnoarr[k], TAG1);
        }*/
        //..................................................................................................
        int geysercount=mas_adap.getCount_housenoroomnodevtypename( roomno,"GSR");
        gysernoarr=new String[geysercount];
        gysertypearr=new String[geysercount];
        gysermodeltypearr=new String[geysercount];
        gyserdevnamearr=new String[geysercount];

        gysernoarr=mas_adap.getall_housenoroomnodevicetypename(geysercount, roomno,"GSR");
        gysermodeltypearr=mas_adap.getall_devicetypemodel(geysercount, roomno,"GSR");
        gyserdevnamearr=mas_adap.getall_roomnodevicenames(geysercount, roomno,"GSR");
        StaticVariabes_div.log("geysercount"+geysercount, TAG1);

        String gysermodels[]=new String[gysermodeltypearr.length];

        for(int k=0;k<gysermodeltypearr.length;k++){
            gysermodels[k]=gysermodeltypearr[k]+"-"+gysernoarr[k];
            StaticVariabes_div.log("gysermodels"+gysermodels[k], TAG1);
        }

        if(geysercount!=0){
            listdevices.addAll(Arrays.asList(gysermodels));
            listdevicesnumbers.addAll(Arrays.asList(gysernoarr));
            listdevicesnames.addAll(Arrays.asList(gyserdevnamearr));

        }
        //...........................................................................................
        int account=mas_adap.getCount_housenoroomnodevtypename( roomno,"ACR");
        acnoarr=new String[account];
        actypearr=new String[account];
        acmodeltypearr=new String[account];
        acdevnamearr=new String[account];

        acnoarr=mas_adap.getall_housenoroomnodevicetypename(account, roomno,"ACR");
        acmodeltypearr=mas_adap.getall_devicetypemodel(account, roomno,"ACR");
        acdevnamearr=mas_adap.getall_roomnodevicenames(account, roomno,"ACR");
        StaticVariabes_div.log("account"+account, TAG1);
        String acmodels[]=new String[acmodeltypearr.length];

        for(int k=0;k<acmodeltypearr.length;k++){
            acmodels[k]=acmodeltypearr[k]+"-"+acnoarr[k];
            StaticVariabes_div.log("acmodels"+acmodels[k], TAG1);
        }

        if(account!=0){
            listdevices.addAll(Arrays.asList(acmodels));
            listdevicesnumbers.addAll(Arrays.asList(acnoarr));
            listdevicesnames.addAll(Arrays.asList(acdevnamearr));
        }
        for(int k=0;k<acmodeltypearr.length;k++){
            StaticVariabes_div.log("ac acmodeltypearr"+acmodeltypearr[k], TAG1);
        }
        for(int k=0;k<acnoarr.length;k++){
            StaticVariabes_div.log("ac acnoarr"+acnoarr[k], TAG1);
        }
        //............................................................................

        int pircount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PIR");
        pirnoarr=new String[pircount];
        pirtypearr=new String[pircount];
        pirmodeltypearr=new String[pircount];
        pirdevnamearr=new String[pircount];

        pirnoarr=mas_adap.getall_housenoroomnodevicetypename(pircount, roomno,"PIR");
        pirmodeltypearr=mas_adap.getall_devicetypemodel(pircount, roomno,"PIR");
        pirdevnamearr=mas_adap.getall_roomnodevicenames(pircount, roomno,"PIR");
        StaticVariabes_div.log("pircount"+pircount, TAG1);
        String pirmodels[]=new String[pirmodeltypearr.length];

        for(int k=0;k<pirmodeltypearr.length;k++){
            pirmodels[k]=pirmodeltypearr[k]+"-"+pirnoarr[k];
            StaticVariabes_div.log("pirmodels"+pirmodels[k], TAG1);
        }

        if(pircount!=0){
            listdevices.addAll(Arrays.asList(pirmodels));
            listdevicesnumbers.addAll(Arrays.asList(pirnoarr));
            listdevicesnames.addAll(Arrays.asList(pirdevnamearr));

        }
        //............................................................................

        int clbcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"CLB");
        clbnoarr=new String[clbcount];
        clbtypearr=new String[clbcount];
        clbmodeltypearr=new String[clbcount];
        clbdevnamearr=new String[clbcount];

        clbnoarr=mas_adap.getall_housenoroomnodevicetypename(clbcount, roomno,"CLB");
        clbmodeltypearr=mas_adap.getall_devicetypemodel(clbcount, roomno,"CLB");
        clbdevnamearr=mas_adap.getall_roomnodevicenames(clbcount, roomno,"CLB");
        StaticVariabes_div.log("clbcount"+clbcount, TAG1);
        String clbmodels[]=new String[clbmodeltypearr.length];

        for(int k=0;k<clbmodeltypearr.length;k++){
            clbmodels[k]=clbmodeltypearr[k]+"-"+clbnoarr[k];
            StaticVariabes_div.log("clbmodels"+clbmodels[k], TAG1);
        }

        if(clbcount!=0){
            listdevices.addAll(Arrays.asList(clbmodels));
            listdevicesnumbers.addAll(Arrays.asList(clbnoarr));
            listdevicesnames.addAll(Arrays.asList(clbdevnamearr));

        }
        //............................................................................

        int dlscount=mas_adap.getCount_housenoroomnodevtypename( roomno,"DLS");
        dlsnoarr=new String[dlscount];
        dlstypearr=new String[dlscount];
        dlsmodeltypearr=new String[dlscount];
        dlsdevnamearr=new String[dlscount];

        dlsnoarr=mas_adap.getall_housenoroomnodevicetypename(dlscount, roomno,"DLS");
        dlsmodeltypearr=mas_adap.getall_devicetypemodel(dlscount, roomno,"DLS");
        dlsdevnamearr=mas_adap.getall_roomnodevicenames(dlscount, roomno,"DLS");
        StaticVariabes_div.log("dlscount"+dlscount, TAG1);
        String dlsmodels[]=new String[dlsmodeltypearr.length];

        for(int k=0;k<dlsmodeltypearr.length;k++){
            dlsmodels[k]=dlsmodeltypearr[k]+"-"+dlsnoarr[k];
            StaticVariabes_div.log("dlsmodels"+dlsmodels[k], TAG1);
        }

        if(dlscount!=0){
            listdevices.addAll(Arrays.asList(dlsmodels));
            listdevicesnumbers.addAll(Arrays.asList(dlsnoarr));
            listdevicesnames.addAll(Arrays.asList(dlsdevnamearr));

        }
        //............................................................................

        int fmcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"FMD");
        StaticVariabes_div.log("fmcount"+fmcount, TAG1);
        fmnoarr=new String[fmcount];
        fmtypearr=new String[fmcount];
        fmmodeltypearr=new String[fmcount];
        fmdevnamearr=new String[fmcount];

        fmnoarr=mas_adap.getall_housenoroomnodevicetypename(fmcount, roomno,"FMD");
        fmmodeltypearr=mas_adap.getall_devicetypemodel(fmcount, roomno,"FMD");
        fmdevnamearr=mas_adap.getall_roomnodevicenames(fmcount, roomno,"FMD");
        StaticVariabes_div.log("fmcount"+fmcount, TAG1);

        String fmmodels[]=new String[fmmodeltypearr.length];

        for(int k=0;k<fmmodeltypearr.length;k++){
            fmmodels[k]=fmmodeltypearr[k]+"-"+fmnoarr[k];
            StaticVariabes_div.log("fmmodels"+fmmodels[k], TAG1);
        }

        if(fmcount!=0){
            listdevices.addAll(Arrays.asList(fmmodels));
            listdevicesnumbers.addAll(Arrays.asList(fmnoarr));
            listdevicesnames.addAll(Arrays.asList(fmdevnamearr));

        }
        for(int k=0;k<fmmodeltypearr.length;k++){
            StaticVariabes_div.log("fm fmmodeltypearr"+fmmodeltypearr[k], TAG1);
        }
        for(int k=0;k<fmnoarr.length;k++){
            StaticVariabes_div.log("fm fmnoarr"+fmnoarr[k], TAG1);
        }
        //...................................................................................................

        int projscrcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PSC");
        projscrnoarr=new String[projscrcount];
        projscrtypearr=new String[projscrcount];
        projscrmodeltypearr=new String[projscrcount];
        projscrdevnamearr=new String[projscrcount];

        projscrnoarr=mas_adap.getall_housenoroomnodevicetypename(projscrcount, roomno,"PSC");
        projscrmodeltypearr=mas_adap.getall_devicetypemodel(projscrcount, roomno,"PSC");
        projscrdevnamearr=mas_adap.getall_roomnodevicenames(projscrcount, roomno,"PSC");
        StaticVariabes_div.log("projscrcount"+projscrcount, TAG1);
        String projscrmodels[]=new String[projscrmodeltypearr.length];

        for(int k=0;k<projscrmodeltypearr.length;k++){
            projscrmodels[k]=projscrmodeltypearr[k]+"-"+projscrnoarr[k];
            StaticVariabes_div.log("projscrmodels"+projscrmodels[k], TAG1);
        }

        if(projscrcount!=0){
            listdevices.addAll(Arrays.asList(projscrmodels));
            listdevicesnumbers.addAll(Arrays.asList(projscrnoarr));
            listdevicesnames.addAll(Arrays.asList(projscrdevnamearr));

        }
        //...................................................................................................

        int projliftcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"PLC");
        projliftnoarr=new String[projliftcount];
        projlifttypearr=new String[projliftcount];
        projliftdevnamearr=new String[projliftcount];

        projliftnoarr=mas_adap.getall_housenoroomnodevicetypename(projliftcount, roomno,"PLC");
        projliftmodeltypearr=mas_adap.getall_devicetypemodel(projliftcount, roomno,"PLC");
        projliftdevnamearr=mas_adap.getall_roomnodevicenames(projliftcount, roomno,"PLC");
        StaticVariabes_div.log("projliftcount"+projliftcount, TAG1);
        String projliftmodels[]=new String[projliftmodeltypearr.length];

        for(int k=0;k<projliftmodeltypearr.length;k++){
            projliftmodels[k]=projliftmodeltypearr[k]+"-"+projliftnoarr[k];
            StaticVariabes_div.log("projliftmodels"+projliftmodels[k], TAG1);
        }

        if(projliftcount!=0){
            listdevices.addAll(Arrays.asList(projliftmodels));
            listdevicesnumbers.addAll(Arrays.asList(projliftnoarr));
            listdevicesnames.addAll(Arrays.asList(projliftdevnamearr));

        }
        //..................................................................................................

        int gskcount=mas_adap.getCount_housenoroomnodevtypename( roomno,"GSK");
        gsknoarr=new String[gskcount];
        gsktypearr=new String[gskcount];
        gskmodeltypearr=new String[gskcount];
        gskdevnamearr=new String[gskcount];

        gsknoarr=mas_adap.getall_housenoroomnodevicetypename(gskcount, roomno,"GSK");
        gskmodeltypearr=mas_adap.getall_devicetypemodel(gskcount, roomno,"GSK");
        gskdevnamearr=mas_adap.getall_roomnodevicenames(gskcount, roomno,"GSK");
        StaticVariabes_div.log("gskcount"+gskcount, TAG1);
        String gskmodels[]=new String[gskmodeltypearr.length];

        for(int k=0;k<gskmodeltypearr.length;k++){
            gskmodels[k]=gskmodeltypearr[k]+"-"+gsknoarr[k];
            StaticVariabes_div.log("gskmodels"+gskmodels[k], TAG1);
        }

        if(gskcount!=0){
            listdevices.addAll(Arrays.asList(gskmodels));
            listdevicesnumbers.addAll(Arrays.asList(gsknoarr));
            listdevicesnames.addAll(Arrays.asList(gskdevnamearr));

        }
        //...........................................................................................
        int aqucount=mas_adap.getCount_housenoroomnodevtypename( roomno,"AQU");
        aqunoarr=new String[aqucount];
        aqutypearr=new String[aqucount];
        aqumodeltypearr=new String[aqucount];
        aqudevnamearr=new String[aqucount];

        aqunoarr=mas_adap.getall_housenoroomnodevicetypename(aqucount, roomno,"AQU");
        aqumodeltypearr=mas_adap.getall_devicetypemodel(aqucount, roomno,"AQU");
        aqudevnamearr=mas_adap.getall_roomnodevicenames(aqucount, roomno,"AQU");
        StaticVariabes_div.log("aqucount"+aqucount, TAG1);
        String aqumodels[]=new String[aqumodeltypearr.length];

        for(int k=0;k<aqumodeltypearr.length;k++){
            aqumodels[k]=aqumodeltypearr[k]+"-"+aqunoarr[k];
            StaticVariabes_div.log("aqumodels"+aqumodels[k], TAG1);
        }

        if(aqucount!=0){
            listdevices.addAll(Arrays.asList(aqumodels));
            listdevicesnumbers.addAll(Arrays.asList(aqunoarr));
            listdevicesnames.addAll(Arrays.asList(aqudevnamearr));

        }
        for(int k=0;k<aqumodeltypearr.length;k++){
            StaticVariabes_div.log("aqu aqumodeltypearr"+aqumodeltypearr[k], TAG1);
        }
        for(int k=0;k<aqunoarr.length;k++){
            StaticVariabes_div.log("aqu aqunoarr"+aqunoarr[k], TAG1);

        }

        //...........................................................................................



        int alexacount=mas_adap.getCount_housenoroomnodevtypename(roomno,"ALXA");
        alexanoarr=new String[alexacount];
        alexatypearr=new String[alexacount];
        alexamodeltypearr=new String[alexacount];
        alexadevnamearr=new String[alexacount];

        alexanoarr=mas_adap.getall_housenoroomnodevicetypename(alexacount, roomno, "ALXA");
        alexamodeltypearr=mas_adap.getall_devicetypemodel(alexacount, roomno, "ALXA");
        alexadevnamearr=mas_adap.getall_roomnodevicenames(alexacount, roomno, "ALXA");

        StaticVariabes_div.log("alexa count"+alexacount, TAG1);
        String alexamodels[]=new String[alexamodeltypearr.length];

        for(int k=0;k<alexamodeltypearr.length;k++){
            alexamodels[k]=alexamodeltypearr[k]+"-"+alexanoarr[k];
            StaticVariabes_div.log("alexamodels"+alexamodels[k], TAG1);
        }
        if(alexacount!=0){
            listdevices.addAll(Arrays.asList(alexamodels));
            listdevicesnumbers.addAll(Arrays.asList(alexanoarr));
            listdevicesnames.addAll(Arrays.asList(alexadevnamearr));
        }
        //...........................................................................................

        // mas_adap.close();

        finallistarr = 	listdevices.toArray( new String[listdevices.size()] );
        finaldevnumlistarr=listdevicesnumbers.toArray( new String[listdevicesnumbers.size()] );
        finaldevnamelistarr=listdevicesnames.toArray( new String[listdevicesnames.size()] );

        if(finallistarr!=null){
            for(int r=0;r<finallistarr.length;r++){
                StaticVariabes_div.log("device list final arr"+finallistarr[r], TAG1);
            }
            StaticVariabes_div.roomdeviceslistarr=finallistarr;
            StaticVariabes_div.roomdevicesnumberlistarr=finaldevnumlistarr;
            StaticVariabes_div.roomdevicesnamelistarr=finaldevnamelistarr;

        }else{

        }

    }

//***********************************************************************************************************************

    public class ExpandListAdapter extends BaseExpandableListAdapter {

        private ArrayList<ArrayList<String>> mGroupList = new ArrayList<>();

        /*
         *  Raw Data
         */
       // String[] testChildData =  {"10","20","30", "40", "50"};
        String[] testgroupData;// =  {"Apple","Banana","Mango", "Orange", "Pineapple", "Strawberry"};
        Context mContext;
        ArrayList<ArrayList<Boolean>> selectedChildCheckBoxStates = new ArrayList<>();
        ArrayList<Boolean> selectedParentCheckBoxesState = new ArrayList<>();
        TotalListener mListener;

        public void setmListener(TotalListener mListener) {
            this.mListener = mListener;
        }

        public void setmGroupList(ArrayList<ArrayList<String>> mGroupList) {
            this.mGroupList = mGroupList;
        }

        class ViewHolder {
            public CheckBox groupName;
            public TextView dummyTextView; // View to expand or shrink the list
            public CheckBox childCheckBox;
            public ImageView img;
        }

        public ExpandListAdapter(Context context,ArrayList<ArrayList<String>> mGroupLi,String[] grouproomnames,String addedit) {
            mContext = context;

            //Add raw data into Group List Array
          /*  for(int i = 0; i < testgroupData.length; i++){
                ArrayList<String> prices = new ArrayList<>();
                for(int j = 0; j < testChildData.length; j++) {
                    prices.add(testChildData[j]);
                }
                mGroupList.add(i, prices);
            }*/
            /*testgroupData=new String[grouproomnames.length];
            for(int i = 0; i < grouproomnames.length; i++){
                testgroupData[i]=grouproomnames[i];
            }
*/
            this.testgroupData =grouproomnames;

            StaticVariabes_div.log("grouproomnames len"+grouproomnames.length, TAG1);
            StaticVariabes_div.log("testgroupData len"+testgroupData.length, TAG1);

            mGroupList=mGroupLi;

            if(addedit.equals("ADD")) {
                //initialize default check states of checkboxes
                initCheckStates(false);
            }else{

                try {
                    initCheckStates_EDIT(false, grouproomnames);
                }catch(Exception Ao){
                    StaticVariabes_div.log(""+Ao.toString(), TAG1);
                }
            }
        }

        /**
         * Called to initialize the default check states of items
         * @param defaultState : false
         */
        private void initCheckStates(boolean defaultState) {
            for(int i = 0 ; i < mGroupList.size(); i++){
                selectedParentCheckBoxesState.add(i, defaultState);

                ArrayList<Boolean> childStates = new ArrayList<>();
                for(int j = 0; j < mGroupList.get(i).size(); j++){
                    childStates.add(defaultState);
                }

                selectedChildCheckBoxStates.add(i, childStates);
            }
        }

        private void initCheckStates_EDIT(boolean defaultState,String grouproomnam[]) {

        String rname, devicenoaccessdb=null;int rno;boolean devicesaccessbooleanArray[] = new boolean[0];String[] devicenoaccessdbArray = new String[0];

            try {
                // usrtabadap.open();
                StaticVariabes_div.log("user before fetch"+usrseltoedit, TAG1);
                Cursor usrcur=usrtabadap.fetch_User(usrseltoedit);
                StaticVariabes_div.log("usersel"+usrseltoedit, TAG1);

                if(usrcur!=null){
                    String roomnoaccessdb=usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_roomno));
                    String usrdb=usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_username));
                    devicenoaccessdb=usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_ea));

                    if(devicenoaccessdb!=null) {
                        devicenoaccessdbArray = devicenoaccessdb.split(";");

                    }

                }
                usrcur.close();
                //usrtabadap.close();
            }catch (SQLiteCantOpenDatabaseException sde) {
                Toast.makeText(OperatorSettingsMain.this,"excep initCheckStates_EDIT database",Toast.LENGTH_LONG).show();
            }



            for(int i = 0 ; i < mGroupList.size(); i++){
                selectedParentCheckBoxesState.add(i, defaultState);

              //  for(int b=0;i<groupList.size();b++){

                    //getting current room name
                    rname=grouproomnam[i];

                    //getting current room number
                   // rno=houseconfigDB.CurrentRoomNumber(rname);

                String rn=swbadap.getroomno(rname);
                StaticVariabes_div.log("roomno in switchboard rn 2  "+rn, TAG1);

                if((rn==null)||(rn.length()<1)){
                    rn=mas_adap.getroomno(rname);
                    StaticVariabes_div.log("roomno in master rn 2  "+rn, TAG1);
                }

                if (rn == null||(rn.length()<1)) {
                    StaticVariabes_div.log("Edit room number before wireless  2  " + rn, TAG1);
                    WirelessConfigurationAdapter WhouseDB = new WirelessConfigurationAdapter(OperatorSettingsMain.this);

                    boolean isDbExists = false;
                    try {
                        isDbExists = WhouseDB.checkdb();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (isDbExists) {
                        //opening database
                        WhouseDB.open();

                        //getting room number from wireless database
                        rn = ""+WhouseDB.wirlessRoomNumber(rname);
                        StaticVariabes_div.log("roomno in wireless rn "+rn, TAG1);
                        //closing database
                        WhouseDB.close();
                    }else{
                        StaticVariabes_div.log("Edit room number dbnot exists" + rn, TAG1);
                    }

                }

                   // String rn=Integer.toString(rno);
                   // prepareList(rn);
                try {
                    if(rn!=null)
                    prepareList(rn);
                }catch (SQLiteCantOpenDatabaseException sce){

                    Toast.makeText(OperatorSettingsMain.this,"excep prepareList database",Toast.LENGTH_LONG).show();
                   /* mas_adap.open();
                    swbadap.open();
                    try {
                        prepareList(rn);
                    }catch (SQLiteCantOpenDatabaseException sde) {

                    }*/
                }

                   ArrayList<String> devnums = new ArrayList<>();
                    for(int j = 0; j < StaticVariabes_div.roomdevicesnumberlistarr.length; j++) {
                        devnums.add(StaticVariabes_div.roomdevicesnumberlistarr[j]);
                    }
                    devicesaccessbooleanArray=new boolean[StaticVariabes_div.roomdevicesnumberlistarr.length];

                    if(devicenoaccessdb!=null) {
                        for (int d = 0; d <= devicenoaccessdbArray.length - 1; d++) {
                            for (int j = 0; j <= StaticVariabes_div.roomdevicesnumberlistarr.length - 1; j++) {
                                //StaticVariabes_div.log("devicenoaccessdbArray"+devicenoaccessdbArray[i]+"Alldevicesnumbers_inroom"+Alldevicesnumbers_inroom[j], TAG1);
                                if (devicenoaccessdbArray[d].equals(StaticVariabes_div.roomdevicesnumberlistarr[j])) {
                                    devicesaccessbooleanArray[j] = true;
                                    //y++;
                                } else {
                                    // roomnameaccessbooleanArray[y]=false;
                                    // y++;
                                }
                            }
                        }


                    } else{
                        devicesaccessbooleanArray=new boolean[StaticVariabes_div.roomdeviceslistarr.length];
                        for(int m=0;m<devicesaccessbooleanArray.length;m++){
                            devicesaccessbooleanArray[m]=false;
                        }

                    }
                   // mGroupListmainDev.add(i,devnums);



         //   }



                ArrayList<Boolean> childStates = new ArrayList<>();
                for(int j = 0; j < mGroupList.get(i).size(); j++){

                   // childStates.add(defaultState);
                    childStates.add(devicesaccessbooleanArray[j]);
                }

                selectedChildCheckBoxStates.add(i, childStates);
            }
        }



        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mGroupList.get(groupPosition).get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }
        @Override
        public int getChildrenCount(int groupPosition) {
            return mGroupList.get(groupPosition).size();
        }

        @Override
        public Object getGroup(int groupPosition) {
            return mGroupList.get(groupPosition);
        }

        @Override
        public int getGroupCount() {
            return mGroupList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }


        @Override
        public boolean hasStableIds() {
            return false;
        }

        @Override
        public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, ViewGroup parent) {
            ViewHolder holder;

            if(convertView == null||convertView.equals("null")) {
                StaticVariabes_div.log("inside convertView null"+convertView, TAG1);
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                //convertView = inflater.inflate(R.layout.group_layout, null);
                convertView =  inflater.inflate(R.layout.group_layout, parent, false);
                //convertView.setEllipsize(null);
                holder = new ViewHolder();
                holder.groupName = (CheckBox) convertView.findViewById(R.id.group_chk_box);
                holder.dummyTextView = (TextView) convertView.findViewById(R.id.dummy_txt_view);
                holder.img= (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            }else{
                StaticVariabes_div.log("inside convertView not null"+convertView, TAG1);
                holder = (ViewHolder) convertView.getTag();
            }



       /*     LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //convertView = inflater.inflate(R.layout.group_layout, null);
            convertView =  inflater.inflate(R.layout.group_layout, parent, false);
            //convertView.setEllipsize(null);
            holder = new ViewHolder();
            holder.groupName = (CheckBox) convertView.findViewById(R.id.group_chk_box);
            holder.dummyTextView = (TextView) convertView.findViewById(R.id.dummy_txt_view);
            holder.img= (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(holder);*/


            if (isExpanded) {
                holder.img.setImageResource(R.drawable.minus);
            } else {
                holder.img.setImageResource(R.drawable.plus);
            }

            try {
                StaticVariabes_div.log(groupPosition + "testgroupData[groupPosition]" + testgroupData[groupPosition], TAG1);
                holder.groupName.setText(testgroupData[groupPosition]);
            }catch (ArrayIndexOutOfBoundsException ae){
                ae.printStackTrace();
                StaticVariabes_div.log( "error"+ae.toString(), TAG1);

            }

            if(selectedParentCheckBoxesState.size() <= groupPosition){
                selectedParentCheckBoxesState.add(groupPosition, false);
            }else {
                holder.groupName.setChecked(selectedParentCheckBoxesState.get(groupPosition));
            }



            holder.groupName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                //Callback to expansion of group item
                    //  if(!isExpanded)
                    //   mListener.expandGroupEvent(groupPosition, isExpanded);

                    boolean state = selectedParentCheckBoxesState.get(groupPosition);
                    Log.d("TAG", "STATE = " + state);
                    selectedParentCheckBoxesState.remove(groupPosition);
                    selectedParentCheckBoxesState.add(groupPosition, state ? false : true);

                    for (int i = 0; i < mGroupList.get(groupPosition).size(); i++) {

                        selectedChildCheckBoxStates.get(groupPosition).remove(i);
                        selectedChildCheckBoxStates.get(groupPosition).add(i, state ? false : true);
                    }
                    notifyDataSetChanged();
                    //showTotal(groupPosition);
                }
            });


            //callback to expand or shrink list view from dummy text click
            holder.dummyTextView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                //Callback to expansion of group item
                    mListener.expandGroupEvent(groupPosition, isExpanded);

               /* if(isExpanded){
                   holder.dummyTextView.setText("test");
                }*/
                }
            });

            holder.img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                //Callback to expansion of group item
                    mListener.expandGroupEvent(groupPosition, isExpanded);

               /* if(isExpanded){
                   holder.dummyTextView.setText("test");
                }*/
                }
            });

            return convertView;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {

            final ViewHolder holder;
            if(convertView == null){
                LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.child_layout, null);
                holder = new ViewHolder();
                holder.childCheckBox = (CheckBox) convertView.findViewById(R.id.child_check_box);
                convertView.setTag(holder);
            }else{
                holder = (ViewHolder) convertView.getTag();
            }


            holder.childCheckBox.setText(mGroupList.get(groupPosition).get(childPosition));
            if(selectedChildCheckBoxStates.size() <= groupPosition) {
                ArrayList<Boolean> childState = new ArrayList<>();
                for(int i= 0; i < mGroupList.get(groupPosition).size(); i++){
                    if(childState.size() > childPosition)
                        childState.add(childPosition, false);
                    else
                        childState.add(false);
                }
                if(selectedChildCheckBoxStates.size() > groupPosition) {
                    selectedChildCheckBoxStates.add(groupPosition, childState);
                }else
                    selectedChildCheckBoxStates.add(childState);
            }else{
                holder.childCheckBox.setChecked(selectedChildCheckBoxStates.get(groupPosition).get(childPosition));
            }
            holder.childCheckBox.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    //holder.groupName.setChecked(false);

                    boolean state = selectedChildCheckBoxStates.get(groupPosition).get(childPosition);

                    /*if(state){
                        boolean parentstate=false;
                        selectedParentCheckBoxesState.remove(groupPosition);
                        selectedParentCheckBoxesState.add(groupPosition, parentstate ? false : true);
                        notifyDataSetChanged();
                    }*/

                    selectedChildCheckBoxStates.get(groupPosition).remove(childPosition);
                    selectedChildCheckBoxStates.get(groupPosition).add(childPosition, state ? false : true);

                    // showTotal(groupPosition);

                }
            });

            return convertView;
        }

        /**
         * Called to reflect the sum of checked prices
         * @param groupPosition : group position of list
         */
      /*  private void showTotal(int groupPosition) {
            //Below code is to get the sum of checked prices
            int sum = 0;
            for(int j = 0 ; j < selectedChildCheckBoxStates.size(); j++) {
                Log.d("TAG", "J = " + j);
                for (int i = 0; i < selectedChildCheckBoxStates.get(groupPosition).size(); i++) {
                    Log.d("TAG", "I = " + i);

                    if (selectedChildCheckBoxStates.get(j).get(i)) {
                        sum += Integer.parseInt(mGroupList.get(j).get(i));
                    }
                }
            }
            mListener.onTotalChanged(sum);
        }*/

        @Override
        public boolean isChildSelectable(int groupPosition, int childPosition) {
            return true;
        }
    }


    //**********************************************************************************8

    @Override
    public void onTotalChanged(int sum) {
        mTextView.setText("Total = " + sum);
    }

    @Override
    public void expandGroupEvent(int groupPosition, boolean isExpanded) {
        if(isExpanded)
            exp_listView.collapseGroup(groupPosition);
        else
            exp_listView.expandGroup(groupPosition);
    }



    //////////////////////////////////////////////

    public void onBackPressed() {
        usrtabadap.close();
        mas_adap.close();
        swbadap.close();
        Intent intnt=new Intent(OperatorSettingsMain.this, Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }

    public void Update_db_version_number(){
        if(StaticVariabes_div.housename!=null) {
            try{
                ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
                sdadap = new ServerDetailsAdapter(OperatorSettingsMain.this);
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
}
