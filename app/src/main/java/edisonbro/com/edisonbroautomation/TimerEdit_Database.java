package edisonbro.com.edisonbroautomation;


/**
 *  FILENAME: TimerEdit_Database.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to show timerlist and edit timer settings .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.os.StrictMode;
import android.renderscript.RenderScript;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import org.w3c.dom.Text;

import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.AlgorithmParameterSpec;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import edisonbro.com.edisonbroautomation.Connections.App;
import edisonbro.com.edisonbroautomation.Connections.Http_Connect;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Devices.A_rc;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewired.TimerDBAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.CustomSpinnerAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.DownloadHouseDb;
import edisonbro.com.edisonbroautomation.scrollfunction.ShaderSeekArc;



public class TimerEdit_Database extends AppCompatActivity {
    Button b_back,b_deleteall;
    HouseConfigurationAdapter houseDB = null;
    ArrayList<String> UniqueRoomList = new ArrayList<String>();
    SpinnerAdapter roomNameAdapter;
    public Spinner room_name_spinner;
    ListView simpleListView;
    CheckBox cb_sheer_select,cb_curtain_select,cb_ligsen_select,cb_pir_select;
    TextView txt_popup;
    Timer_Edit_Adap timr_adap;
    //********************************************************
    View timer_popup_view,timer_delete_view, layout_view;
    TextView tv_ontime, tv_offtime,tv_setoperation,tv_timerlist;
    Button b_sunday, b_monday, b_tuesday, b_wednesday, b_thursday, b_friday, b_saturday,
            b_setoperation, b_settimer, b_timerlist, b_tmr_pop_cancel;
    LinearLayout lay_repeat_linr_days;
    RadioButton rb_repeat_ondays, rb_cyclic, rb_selecteddate;//rb_repeat_ptern;
    CheckBox cb_repeat_ptern;
    LinearLayout lay_rep_ondays_A, lay_rep_ondays_B, lay_cyclic_A, lay_cyclic_B,
            lay_sel_date_A, lay_sel_date_B;
    NumberPicker np_frmtime_hrs, np_frmtime_min, np_totime_hrs, np_totime_min,
            np_ontime_cyc, np_offtime_cyc, np_date_seldate, np_month_seldate, np_year_seldate;
    Button btnarr_weekdays[];
    NumberPicker np_arr[];
    LinearLayout lay_all_arr[];
    public String[] displayedValues;
    String seldate_year, seldate_month, seldate_day;
    Button s1, s2, s3, s4, s5, fan_dec, fan_inc, save, cancel;
    Spinner fan_speed;
    ArrayList<Object> bulbon;
    Button btnarr_timer_switchs[];
    int int_Frm_time_hrs, int_Frm_time_min, int_To_time_hrs, int_To_time_min, int_Day_Selected;
    String Frm_time_hrs, Frm_time_min, To_time_hrs, To_time_min, Frm_Time, To_Time;

    private String fanspeed, rep_weekly, off_data, on_data, Timer_Data, TimerType,
            Day_Selected, Month_Selected, Year_Selected, Date_Selected, StartToken="[",EndToken;
    List ar_num_switch = new ArrayList();
    List ar_off_data = new ArrayList();
    List ar_on_data = new ArrayList();
    String devn, on_time_cyclic, off_time_cyclic, dev_typ_num, devtyp, dev_name,devid;
    android.support.v7.app.AlertDialog dialog1;
    int arr_weekdata[];
    Context context;
    public String room_num;
    String prev_oper_type;
    public  LayoutInflater inflater;
    public Integer[] days_images_grey = {R.drawable.week_mon_01, R.drawable.week_tues_thur_01, R.drawable.week_wed_01,
            R.drawable.week_tues_thur_01, R.drawable.week_fri_01, R.drawable.week_sun_sat_01, R.drawable.week_sun_sat_01};

    public Integer[] days_images_blue = {R.drawable.week_mon_02, R.drawable.week_tues_thur_02, R.drawable.week_wed_02,
            R.drawable.week_tues_thur_02, R.drawable.week_fri_02, R.drawable.week_sun_sat_02, R.drawable.week_sun_sat_02};


    ///////////popup rgb///////////////////////////////////////
    private A_rc colorPicker;
    private Button btn_Red, btn_Green, btn_Blue, btn_Pink, btn_White,
            btn_orange, btn_Flash, btn_Strobe, btn_Fade, btn_Smooth, btn_Speedp, btn_Speedm, btn_on, btn_off;
    private TextView rgbvalue, tv_speedval, tvtest;
    SeekBar sk;
    String rgbinst, rgbbrightinst, rgb_color_effect, rgbbright, rgb_speed, rgb_speedinst, rgb_onoff_repeat,
            diminst, dimvalue, ondatadmr, dmr_prev_bright,cur_prev_data;
    String dmr_data[];
    int ispeed = 120;
    int count = 0;

    String rgb_prev_color_efft = null, rgb_prev_color_r = null, rgb_prev_color_g = null, rgb_prev_color_b = null, rgb_prev_bright = null, rgb_prev_speed = null;

    private enum Effects {
        d104, d105, d106, d107,
    }

    public static Integer[] mThumbIds = {

            R.drawable.dim_level0, R.drawable.dim_level1, R.drawable.dim_level2, R.drawable.dim_level3, R.drawable.dim_level4, R.drawable.dim_level5, R.drawable.dim_level6,
            R.drawable.dim_level7, R.drawable.dim_level8, R.drawable.dim_level9,
    };
    int n = 0;
    ///////////////////////////////////////////
    private static final String TAG1 = "Timer_Edit";
    String trdata_Str_ON = "", trdata_Str_OFF = "", Switch_numdata = "";
////////////////////////////////////////////////////////

    public ImageView dim_img;
    ShaderSeekArc seekArc;
    Button btn_low, btn_medium, btn_high,b_home;

    private Tracker mTracker;

    ////////////////////////////////////
    private MasterAdapter mas_adap;
    private SwbAdapter swbadap;

    String name ="Timer 555 Edit";

  //  public Timer_Edit_Activity activitytime;

    public enum Effects_rgb {
        d104, d105, d106, d107,
    }
/////////////////////

    Object ont,offt,typ,day,datee,on,offff,devname,ondatare,offdatare,devtype,devicenum,switchnumbr,repeaweekly,timee,dateee;

    String  Frmtime_upd,Totime_upd,Ontime_upd,Offtime_upd,opertype_upd,days_upd,
            Devtype_upd,Data_upd,OffData_upd,DeviceNum_upd,DeviceName_upd,Date_upd,Switchnumbr_upd,DevicetypeNum_upd,repeaweekly_upd
           , Devid_upd;

    // ArrayList num,devicename,devicetype,type,switchnum,days,date,time,ontimerep,ondatarep,offdatarep,repeatweekly,ontime,offtime,offtimerep,devinum;

    ArrayList offtime3;
    String roomnumber;
    public 	ArrayList<String> num,devicename
            ,devicetype, operatedtype, switchnumber
            , days, date, time,fromTimeRep_date, toTimeRep_date
            , repeatAlways,ondataRep_date, offdataRep_date, ontimecyclic, offtimecyclic
            , oncyclicData, offCyclicData ,deviceNumber ,fromtimecyclic,totimecyclic,frmtimedisplay,totimedisplay,
            datatodisplay,offdatatodisplay,devicetypenumber;

    android.support.v7.app.AlertDialog dialog;

    android.support.v7.app.AlertDialog timerdialog;
    String SERVER_PASSWORD=null ;
    final String GET_DB_DOWNLOAD_TIMER = "$125&";
    final int READ_LINE=1 ,READ_BYTE=2,EXCEPTION=3,TCP_LOST=4,BT_LOST=5,ERR_USER=7,MAX_USER=8,TCP_CONNECTED=6;
    ImageView serverStatus;

    int server_online=R.drawable.connected;
    int server_offline=R.drawable.not_connected;

    boolean exceptionOccured=false,isServerAuntheticated=false,IS_SERVER_NOT_AUNTHETICATED=false,
            isDbConfigured=false,isAlertshown=false ,isDialogOpened=false;

    private ProgressDialog pdlg;
    Tcp tcp=null;
    TimerDBAdapter timeradap=null;
    int y=0;
    ProgressDialog pd;

    static String encryptionKey = "edisonbrosmartlabs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN );
        setContentView(R.layout.timer_edit_activity);


        //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
       // activitytime=Timer_Edit_Activity.this;

        inflater=getLayoutInflater();

        context = TimerEdit_Database.this;
        b_back = (Button) findViewById(R.id.btnback);
        room_name_spinner = (Spinner) findViewById(R.id.spinner);
        simpleListView = (ListView) findViewById(R.id.simpleListView);
        b_deleteall = (Button) findViewById(R.id.deleteall);
        b_home= (Button) findViewById(R.id.btnhome);

        serverStatus=(ImageView) findViewById(R.id.server_status);

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();
        try {
            houseDB = new HouseConfigurationAdapter(this);
            houseDB.open();            //opening house database


        } catch (Exception e) {
            e.printStackTrace();
        }


        b_home= (Button) findViewById(R.id.btnhome);
        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intnt=new Intent(TimerEdit_Database.this, Main_Navigation_Activity.class);
                intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intnt);
                finish();
            }
        });
        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intnt = new Intent(TimerEdit_Database.this, Main_Navigation_Activity.class);
                intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intnt);
                finish();
            }
        });



        b_deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                popupdelete_all();
            }
        });
        fillRoomNameList();

        Tcp.tcpHost= Tcp_con.tcpAddress;
        Tcp.tcpPort=Tcp_con.tcpPort;
       // ConnectAndDownload_wireless();


        if(StaticVariabes_div.timer_roomnum_edit==null)
            StaticVariabes_div.timer_roomnum_edit=StaticVariabes_div.room_n;

        ConnectAndDownload_timer();



     //    y=UniqueRoomList.indexOf(StaticVariabes_div.room_n);

      //  room_name_spinner.setSelection(y);

        room_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selected_Room = UniqueRoomList.get(i);


                StaticVariabes_div.log("HttpConnect", "roomno in spinner" + selected_Room);

                if (!(selected_Room.equals("Select Room"))) {
                    int r_n = houseDB.CurrentRoomNumber(selected_Room);

                    room_num = ""+r_n;

                    while  (room_num.length()<2) {
                        room_num = "0" + room_num;
                    }

                    StaticVariabes_div.log("HttpConnect", "roomno in spinner" + room_num);

                    StaticVariabes_div.timer_roomnum_edit=room_num;
                    StaticVariabes_div.spinnerroom=selected_Room;


                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                    StrictMode.setThreadPolicy(policy);

                    init_database(room_num);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void onBackPressed() {
        Intent intnt = new Intent(TimerEdit_Database.this, Main_Navigation_Activity.class);
        intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intnt);
        finish();
    }
    //filling room name list
    void fillRoomNameList() {

        UniqueRoomList.clear();

        //Fetching list of All room names from database and adding to local array list
        UniqueRoomList.addAll(houseDB.RoomNameList());
        UniqueRoomList.add("Select Room");


        //making adapter
        roomNameAdapter = new CustomSpinnerAdapter(this, R.layout.spinnerlayout, UniqueRoomList);
        room_name_spinner.setAdapter(roomNameAdapter);
       // y=UniqueRoomList.indexOf(StaticVariabes_div.room_n);

       // room_name_spinner.setSelection(y);
      //  room_name_spinner.setSelection(UniqueRoomList.size() - 1);

    }


    public void refresh_adapter(){

        StaticVariabes_div.timer_roomnum_edit=room_num;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        init_database(room_num);
        String res="Success";
        if (res.equals("Success")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Timer_Edit_Adapter.inflater= (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    simpleListView.setAdapter(new Timer_Edit_Adap(TimerEdit_Database.this,TimerEdit_Database.this, Http_Connect.Num,
                            Http_Connect.OperatedType, Http_Connect.DeviceName, Http_Connect.DeviceType,
                            Http_Connect.SwitchNumber, Http_Connect.Days, Http_Connect.Date, Http_Connect.Time,
                            Http_Connect.FromTimeRep_date, Http_Connect.ToTimeRep_date,
                            Http_Connect.OnDataRep_date, Http_Connect.OffDataRep_date, Http_Connect.RepeatAlways_rep_date, Http_Connect.ontime_cyclic,
                            Http_Connect.offtime_cyclic, Http_Connect.DeviceNumber, Http_Connect.OnCyclicData, Http_Connect.OffCyclicData, Http_Connect.FromTime_Todisplay, Http_Connect.ToTime_Todisplay
                            , Http_Connect.Data_Todisplay, Http_Connect.Off_Data_Todisplay,Http_Connect.Devicetype_number,Http_Connect.User_nam));
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    simpleListView.setAdapter(null);
                }
            });

        }

    }

    public void refresh_adapter_restart(){

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

       init_database(StaticVariabes_div.timer_roomnum_edit);
        String res="Success";
        if (res.equals("Success")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    //Timer_Edit_Adapter.inflater= (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
                    simpleListView.setAdapter(new Timer_Edit_Adap(TimerEdit_Database.this,TimerEdit_Database.this, Http_Connect.Num,
                            Http_Connect.OperatedType, Http_Connect.DeviceName, Http_Connect.DeviceType,
                            Http_Connect.SwitchNumber, Http_Connect.Days, Http_Connect.Date, Http_Connect.Time,
                            Http_Connect.FromTimeRep_date, Http_Connect.ToTimeRep_date,
                            Http_Connect.OnDataRep_date, Http_Connect.OffDataRep_date, Http_Connect.RepeatAlways_rep_date, Http_Connect.ontime_cyclic,
                            Http_Connect.offtime_cyclic, Http_Connect.DeviceNumber, Http_Connect.OnCyclicData, Http_Connect.OffCyclicData, Http_Connect.FromTime_Todisplay, Http_Connect.ToTime_Todisplay
                            , Http_Connect.Data_Todisplay, Http_Connect.Off_Data_Todisplay,Http_Connect.Devicetype_number,Http_Connect.User_nam));
                }
            });
        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    simpleListView.setAdapter(null);
                }
            });

        }

    }

    public void to_setval_days(Button b_set, int val) {
        if (b_set.getTag().equals(0)) {
            b_set.setBackgroundResource(days_images_blue[val]);
            b_set.setTag(1);
        } else {
            b_set.setBackgroundResource(days_images_grey[val]);
            b_set.setTag(0);
        }
    }

    public void timer_popup() {

        timer_popup_view = inflater.inflate(R.layout.popup_timersettng_lay, null);

        final TextView tv = (TextView) timer_popup_view.findViewById(R.id.tv1);
        tv_ontime = (TextView) timer_popup_view.findViewById(R.id.tv_ontime);
        tv_offtime = (TextView) timer_popup_view.findViewById(R.id.tv_offtime);
        //  tvvv = (TextView) timer_popup_view.findViewById(R.id.tv);
        lay_repeat_linr_days = (LinearLayout) timer_popup_view.findViewById(R.id.repeat_linear_lay_days);
        b_sunday = (Button) timer_popup_view.findViewById(R.id.btn_sunday);
        b_monday = (Button) timer_popup_view.findViewById(R.id.btn_monday);
        b_tuesday = (Button) timer_popup_view.findViewById(R.id.btn_tuesday);
        b_wednesday = (Button) timer_popup_view.findViewById(R.id.btn_wednesday);
        b_thursday = (Button) timer_popup_view.findViewById(R.id.btn_thursday);
        b_friday = (Button) timer_popup_view.findViewById(R.id.btn_friday);
        b_saturday = (Button) timer_popup_view.findViewById(R.id.btn_saturday);
        b_setoperation = (Button) timer_popup_view.findViewById(R.id.setoperation);
        b_timerlist = (Button) timer_popup_view.findViewById(R.id.timerlist);
        b_settimer = (Button) timer_popup_view.findViewById(R.id.settimer);
        b_tmr_pop_cancel = (Button) timer_popup_view.findViewById(R.id.timer_pop_cancel);
        tv.setPaintFlags(Paint.UNDERLINE_TEXT_FLAG);

        tv_setoperation = (TextView) timer_popup_view.findViewById(R.id.tv_setoper);
        tv_timerlist = (TextView) timer_popup_view.findViewById(R.id.tv_timerlist);

        rb_repeat_ondays = (RadioButton) timer_popup_view.findViewById(R.id.rb_repeat_ondays);
        rb_cyclic = (RadioButton) timer_popup_view.findViewById(R.id.rb_cyclic);
        rb_selecteddate = (RadioButton) timer_popup_view.findViewById(R.id.rb_selecteddate);
        // rb_repeat_ptern = (RadioButton) timer_popup_view.findViewById(R.id.rb_repeat_ptern);
        cb_repeat_ptern = (CheckBox) timer_popup_view.findViewById(R.id.rb_repeat_ptern);

        lay_rep_ondays_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld1);
        lay_rep_ondays_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld2);
        lay_cyclic_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld3);
        lay_cyclic_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld4);
        lay_sel_date_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld5);
        lay_sel_date_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld6);


        // np
        np_frmtime_hrs = (NumberPicker) timer_popup_view.findViewById(R.id.np_frmtime_hrs);
        // np1
        np_frmtime_min = (NumberPicker) timer_popup_view.findViewById(R.id.np_frmtime_min);
        // np2
        np_totime_hrs = (NumberPicker) timer_popup_view.findViewById(R.id.np_totime_hrs);
        // np3
        np_totime_min = (NumberPicker) timer_popup_view.findViewById(R.id.np_totime_min);
        // np5
        np_ontime_cyc = (NumberPicker) timer_popup_view.findViewById(R.id.np_ontime_cyc);
        //np6
        np_offtime_cyc = (NumberPicker) timer_popup_view.findViewById(R.id.np_offtime_cyc);

        // np7
        np_date_seldate = (NumberPicker) timer_popup_view.findViewById(R.id.np_date_seldate);
        // np8
        np_month_seldate = (NumberPicker) timer_popup_view.findViewById(R.id.np_month_seldate);
        // np9
        np_year_seldate = (NumberPicker) timer_popup_view.findViewById(R.id.np_year_seldate);

        // tvvv.setMovementMethod(new ScrollingMovementMethod());

        btnarr_weekdays = new Button[]{ b_monday, b_tuesday, b_wednesday, b_thursday, b_friday, b_saturday,b_sunday};

        np_arr = new NumberPicker[]{np_frmtime_hrs, np_frmtime_min, np_totime_hrs, np_totime_min, np_ontime_cyc, np_offtime_cyc,
                np_date_seldate, np_month_seldate, np_year_seldate};

        lay_all_arr = new LinearLayout[]{lay_rep_ondays_A, lay_rep_ondays_B, lay_cyclic_A, lay_cyclic_B, lay_sel_date_A, lay_sel_date_B};


        b_timerlist.setEnabled(false);
        b_timerlist.setVisibility(View.INVISIBLE);
        tv_timerlist.setVisibility(View.INVISIBLE);

        for (int h = 0; h < btnarr_weekdays.length; h++) {
            btnarr_weekdays[h].setTag(0);
        }


        android.support.v7.app.AlertDialog.Builder alert = new  android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);

        // AlertDialog.Builder alert = new AlertDialog.Builder(this, R.style.MyDialogTheme);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(timer_popup_view);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);
        timerdialog = alert.create();
       // final AlertDialog dialog = alert.create();
        // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().setLayout(400, 600);
        timerdialog.show();

        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.90);

        // final AlertDialog dialog = alert.create();

        timerdialog.getWindow().setLayout(width, height);
       // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        // dialog.getWindow().setLayout(620, 1100);
        timerdialog.show();

        numberpicker();



        devn = DeviceNum_upd;
        room_num = StaticVariabes_div.timer_roomnum_edit;
        dev_typ_num = DevicetypeNum_upd;
        dev_name = DeviceName_upd;
        devtyp = Devtype_upd;
        Switch_numdata=Switchnumbr_upd;
        rep_weekly=repeaweekly_upd;


        StaticVariabes_div.log("Frmtime_upd" + Frmtime_upd, TAG1);

        String splitfrmTime[] =Frmtime_upd.split(":");
        String frmhours = splitfrmTime[0];
        String frmminutes = splitfrmTime[1];
        np_frmtime_hrs.setValue(Integer.parseInt(frmhours));
        np_frmtime_min.setValue(Integer.parseInt(frmminutes));


        String splittoTime[] = Totime_upd.split(":");
        String tohours = splittoTime[0];
        String tominutes = splittoTime[1];
        np_totime_hrs.setValue(Integer.parseInt(tohours));
        np_totime_min.setValue(Integer.parseInt(tominutes));

        StaticVariabes_div.log("devn" + DeviceNum_upd, TAG1);

        StaticVariabes_div.log("dev_name" + dev_name, TAG1);

        StaticVariabes_div.log("room_num" + room_num, TAG1);

        while (room_num.length() < 2) {
            room_num = "0" + room_num;
        }

        while (devn.length() < 4) {
            devn = "0" + devn;
        }

        if (opertype_upd.equals("Repeat On Days")) {

            prev_oper_type = "rep";

            rb_repeat_ondays.setChecked(true);

            rb_repeat_ondays_func();

            StaticVariabes_div.log("days_upd" + days_upd, TAG1);

            String daysval[] = days_upd.split(",");

            for (int k = 0; k < btnarr_weekdays.length; k++) {

                if (daysval[k].equals("1")) {
                    btnarr_weekdays[k].setBackgroundResource(days_images_blue[k]);
                    btnarr_weekdays[k].setTag(1);
                } else {
                    btnarr_weekdays[k].setBackgroundResource(days_images_grey[k]);
                    btnarr_weekdays[k].setTag(0);
                }
            }

            if (repeaweekly_upd.equals("0")) {
                cb_repeat_ptern.setChecked(true);
                rep_weekly = repeaweekly_upd;
            } else {
                cb_repeat_ptern.setChecked(false);
                Calendar cal = Calendar.getInstance();
                rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

                while(rep_weekly.length()<2){
                    rep_weekly="0"+rep_weekly;
                }
            }



        } else if (opertype_upd.equals("Cyclic")) {

            prev_oper_type = "cyc";

            rb_cyclic.setChecked(true);

            rb_cyclic_func();

            StaticVariabes_div.log("Ontime_upd" + Ontime_upd, TAG1);
            StaticVariabes_div.log("Offtime_upd" + Offtime_upd, TAG1);

            String splitOnTime[] = Ontime_upd.split(":");
            String on_hours = splitOnTime[0];
            String on_minutes = splitOnTime[1];

            if (on_minutes.startsWith("0")) {
                on_minutes = on_minutes.substring(1);
            }

            for (int i = 0; i < displayedValues.length; i++) {

                if (displayedValues[i].equals(on_minutes))
                    np_ontime_cyc.setValue(i);
            }
            // np_ontime_cyc.setValue(displayedValues[Integer.parseInt(on_minutes)]);

            String splitOffTime[] = Offtime_upd.split(":");
            String off_hours = splitOffTime[0];
            String off_minutes = splitOffTime[1];

            if (off_minutes.startsWith("0")) {
                off_minutes = off_minutes.substring(1);
            }


            for (int i = 0; i < displayedValues.length; i++) {

                if (displayedValues[i].equals(off_minutes))
                    np_offtime_cyc.setValue(i);
            }


        } else {


            if (repeaweekly_upd.equals("0")) {
                cb_repeat_ptern.setChecked(true);
                rep_weekly = repeaweekly_upd;
            } else {
                cb_repeat_ptern.setChecked(false);
                Calendar cal = Calendar.getInstance();
                rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

                while(rep_weekly.length()<2){
                    rep_weekly="0"+rep_weekly;
                }
            }

            prev_oper_type = "repd";
            rb_selecteddate.setChecked(true);
            rb_selecteddate_func();

            String splitdate[] = Date_upd.split("-");
            String year = splitdate[0];
            String month = splitdate[1];
            String day = splitdate[2];
            np_date_seldate.setValue(Integer.parseInt(day));
            np_month_seldate.setValue(Integer.parseInt(month));
            np_year_seldate.setValue(Integer.parseInt(year));

        }


        trdata_Str_ON =Data_upd;
        trdata_Str_OFF =OffData_upd;


        if (Devtype_upd.equals("S020") || Devtype_upd.equals("S010") ||Devtype_upd.equals("S141") ||Devtype_upd.equals("S110") ||Devtype_upd.equals("S120") ||Devtype_upd.equals("S160") ||
               Devtype_upd.equals("S030") || Devtype_upd.equals("S080")||Devtype_upd.equals("S051")||Devtype_upd.equals("S021")
                ||Devtype_upd.equals("PSC1")||Devtype_upd.equals("CRS1")||Devtype_upd.equals("CLS1")||Devtype_upd.equals("SOSH")||Devtype_upd.equals("SWG1")||Devtype_upd.equals("SLG1")) {

            if(Devtype_upd.equals("S051")||Devtype_upd.equals("S021")){
                if(Switch_numdata.equals("98")){

                }else{
                    b_setoperation.setEnabled(false);
                    b_setoperation.setVisibility(View.INVISIBLE);
                    tv_setoperation.setVisibility(View.INVISIBLE);
                }

            }else {
                b_setoperation.setEnabled(false);
                b_setoperation.setVisibility(View.INVISIBLE);
                tv_setoperation.setVisibility(View.INVISIBLE);
            }
        }



        b_sunday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_setval_days(b_sunday, 6);

            }
        });


        b_monday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                to_setval_days(b_monday, 0);

            }
        });


        b_tuesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                to_setval_days(b_tuesday, 1);

            }
        });

        b_wednesday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                to_setval_days(b_wednesday, 2);

            }
        });

        b_thursday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                to_setval_days(b_thursday, 3);
            }
        });

        b_friday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                to_setval_days(b_friday, 4);

            }
        });

        b_saturday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                to_setval_days(b_saturday, 5);
            }
        });

        b_timerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Intent i = new Intent(getActivity(), Timer_Edit_Activity.class);
                startActivity(i);*/

            }
        });


        rb_repeat_ondays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_repeat_ondays.isChecked()) {
                    rb_repeat_ondays_func();
                }
            }
        });


        rb_cyclic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_cyclic.isChecked()) {
                    rb_cyclic_func();
                }
            }
        });

        rb_selecteddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_selecteddate.isChecked()) {
                    rb_selecteddate_func();
                }
            }
        });

     /*   rb_repeat_ptern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rb_repeat_ptern.isChecked()) {
                    //rb_repeat_ptern_func();
                    rep_weekly = "0";
                } else {
                    Calendar cal = Calendar.getInstance();
                    rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
                }
            }
        });*/

        cb_repeat_ptern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_repeat_ptern.isChecked()) {
                    //rb_repeat_ptern_func();
                    rep_weekly = "0";
                } else {

                    Calendar cal = Calendar.getInstance();
                    rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

                    while(rep_weekly.length()<2){
                        rep_weekly="0"+rep_weekly;
                    }
                }
            }
        });
        /////////////////////////////////////////////////////////////////////////////////////////////////////


        b_setoperation.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(final View view) {

                SetOperation_Switch(Devtype_upd);
            }

        });


        b_settimer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View view) {

                //String trdata_Str_ON = "", trdata_Str_OFF = "", Switch_numdata = "";

                int_Frm_time_hrs = np_frmtime_hrs.getValue();
                int_Frm_time_min = np_frmtime_min.getValue();


                if (int_Frm_time_hrs < 10) {
                    Frm_time_hrs = "0" + int_Frm_time_hrs;
                } else if (int_Frm_time_hrs >= 10) {
                    Frm_time_hrs = String.valueOf(int_Frm_time_hrs);
                }

                if (int_Frm_time_min < 10) {
                    Frm_time_min = "0" + int_Frm_time_min;
                } else if (int_Frm_time_min >= 10) {
                    Frm_time_min = String.valueOf(int_Frm_time_min);
                }

                int_To_time_hrs = np_totime_hrs.getValue();
                int_To_time_min = np_totime_min.getValue();

                if (int_To_time_hrs < 10) {
                    To_time_hrs = "0" + int_To_time_hrs;
                } else if (int_To_time_hrs >= 10) {
                    To_time_hrs = String.valueOf(int_To_time_hrs);
                }
                if (int_To_time_min < 10) {
                    To_time_min = "0" + int_To_time_min;
                } else if (int_To_time_min >= 10) {
                    To_time_min = String.valueOf(int_To_time_min);
                }


                Frm_Time = Frm_time_hrs + ":" + Frm_time_min;
                To_Time = To_time_hrs + ":" + To_time_min;


                 /*   while (room_num.length() < 2) {
                        room_num = "0" + room_num;
                    }

                    while (StaticVariabes_div.devnumber.length() < 4) {
                        StaticVariabes_div.devnumber = "0" + StaticVariabes_div.devnumber;
                    }*/

                if (!cb_repeat_ptern.isChecked()) {
                    Calendar cal = Calendar.getInstance();
                    rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

                    while(rep_weekly.length()<2){
                        rep_weekly="0"+rep_weekly;
                    }
                }else{
                    rep_weekly ="0";
                }

               // if(!(Frm_Time.equals("00:00")&&To_Time.equals("24:00"))) {
                if(!((Frm_Time.equals("00:00")&&To_Time.equals("24:00"))||(To_Time.equals("00:00")))){
                    arr_weekdata = new int[7];
                    if (rb_repeat_ondays.isChecked()) {


                        // TimerType = "rep";

                        switch (prev_oper_type) {
                            case "cyc": {
                                TimerType = "cyc_rep";
                                EndToken = "%";
                            }
                            break;
                            case "repd": {
                                TimerType = "repd_rep";
                                EndToken = "<";
                            }
                            break;
                            default: {
                                TimerType = "rep";
                                EndToken = "}";
                            }

                            break;
                        }

                        for (int v = 0; v < btnarr_weekdays.length; v++) {

                            if (btnarr_weekdays[v].getTag().equals(1)) {
                                arr_weekdata[v] = 1;
                            } else if (btnarr_weekdays[v].getTag().equals(0)) {
                                arr_weekdata[v] = 0;
                            }
                        }


                        if (trdata_Str_ON != null) {


                            if (!(Frm_Time.equals(To_Time))) {
                                if (arr_weekdata[0] != 0 || arr_weekdata[1] != 0 || arr_weekdata[2] != 0 || arr_weekdata[3] != 0 || arr_weekdata[4] != 0 || arr_weekdata[5] != 0 || arr_weekdata[6] != 0) {


                                    int devnn = Integer.parseInt(devn);

                                    String devid = fetch_deviceid("" + devnn);


                                    Timer_Data = "0,0,1,0,0000-00-00," + Frm_Time + "," + To_Time + "," + "0" + "," + arr_weekdata[0] + "," + arr_weekdata[1] + "," + arr_weekdata[2] + "," +
                                            arr_weekdata[3] + "," + arr_weekdata[4] + "," + arr_weekdata[5] + "," + arr_weekdata[6] + "," + rep_weekly + ","
                                            + devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + dev_typ_num +
                                            "," + devnn + "," + devid + "," + Switch_numdata + "," + room_num + "," + dev_name + "," + Frmtime_upd + "," + Totime_upd;


                                    StaticVariabes_div.log(TAG1, "http TimerType" + TimerType);

                                    StaticVariabes_div.log(TAG1, "http Timer_Data" + Timer_Data);

                                    StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                    StrictMode.setThreadPolicy(policy);


                                    Send_Timer_dat_aes(Timer_Data, StartToken, EndToken);

                                } else {
                                    Toast.makeText(view.getContext(), "Please select days", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                Toast.makeText(view.getContext(), "please Change TO time", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                        }

                    } else if (rb_cyclic.isChecked()) {

                        rep_weekly = "0";

                        // TimerType = "cyc";

                        switch (prev_oper_type) {
                            case "rep": {
                                TimerType = "rep_cyc";
                                EndToken = ">";
                            }
                            break;
                            case "repd": {
                                TimerType = "repd_cyc";
                                EndToken = ">";
                            }
                            break;
                            default: {
                                TimerType = "cyc";
                                EndToken = "~";
                            }
                            break;
                        }

                        on_time_cyclic = displayedValues[np_ontime_cyc.getValue()];
                        if (on_time_cyclic.length() == 1) {
                            on_time_cyclic = "0" + on_time_cyclic;
                        }
                        off_time_cyclic = displayedValues[np_offtime_cyc.getValue()];
                        if (off_time_cyclic.length() == 1) {
                            off_time_cyclic = "0" + off_time_cyclic;
                        }

                        on_time_cyclic = "00:" + on_time_cyclic;
                        off_time_cyclic = "00:" + off_time_cyclic;


                        if (trdata_Str_ON != null) {


                            if (!(Frm_Time.equals(To_Time))) {


                                int devnn = Integer.parseInt(devn);

                                String devid = fetch_deviceid(devn);


                                Timer_Data = Frm_Time + "," + To_Time + "," + on_time_cyclic + "," + off_time_cyclic + "," + Frm_Time + "," + "0000-00-00" + ","
                                        + "1" + "," + "0" + "," + "0" + "," + devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + dev_typ_num + "," + devn + ","
                                        + devid + "," + Switch_numdata + "," + room_num + "," + dev_name + "," + Frmtime_upd + "," + Totime_upd;

                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                Send_Timer_dat_aes(Timer_Data, StartToken, EndToken);


                            } else {
                                Toast.makeText(view.getContext(), "Please Change TO time", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                        }


                    } else if (rb_selecteddate.isChecked()) {
                        //TimerType = "repd";

                        switch (prev_oper_type) {
                            case "rep": {
                                TimerType = "rep_repd";
                                EndToken = "<";
                            }
                            break;
                            case "cyc": {
                                TimerType = "cyc_repd";
                                EndToken = "%";
                            }
                            break;
                            default: {
                                TimerType = "repd";
                                EndToken = "}";
                            }
                            break;
                        }

                        int_Day_Selected = np_date_seldate.getValue();

                    /*if (int_Day_Selected < 10) {
                        Day_Selected = "0" + int_Day_Selected;
                    } else if (int_Day_Selected >= 10) {
                        Day_Selected = String.valueOf(int_Day_Selected);
                    }*/
                        Day_Selected = String.valueOf(int_Day_Selected);

                        Month_Selected = "" + np_month_seldate.getValue();
                        Year_Selected = "" + np_year_seldate.getValue();

                        Date_Selected = Year_Selected + "-" + Month_Selected + "-" + Day_Selected;


                        if (trdata_Str_ON != null) {

                            if (!(Frm_Time.equals(To_Time))) {


                                int devnn = Integer.parseInt(devn);

                                String devid = fetch_deviceid(devn);

                                Timer_Data = "0,0,0,1," + Date_Selected + "," + Frm_Time + "," + To_Time + "," + "0" + "," + arr_weekdata[1] + "," + arr_weekdata[2] + "," + arr_weekdata[3] + "," +
                                        arr_weekdata[4] + "," + arr_weekdata[5] + "," + arr_weekdata[6] + "," + arr_weekdata[0] + "," + rep_weekly + ","
                                        + devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + dev_typ_num +
                                        "," + devnn + "," + devid + "," + Switch_numdata + "," + room_num + "," + dev_name + "," + Frmtime_upd + "," + Totime_upd;


                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                Send_Timer_dat_aes(Timer_Data, StartToken, EndToken);

                            } else {
                                Toast.makeText(view.getContext(), "Please Change TO time", Toast.LENGTH_LONG).show();
                            }
                        } else {
                            Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                        }


                    } else {
                        Toast.makeText(view.getContext(), "Set timer", Toast.LENGTH_LONG).show();
                    }

                }else {
                    Toast.makeText(view.getContext(), "Please Select Correct From and ToTime", Toast.LENGTH_LONG).show();
                }
                // refresh_adapter();

            }
        });


        b_tmr_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timerdialog.dismiss();

            }

        });


    }

    public void SetOperation_Switch(String Switch_model) {

        // devn= StaticVariabes_div.devnumber;
        while (devn.length() < 4) devn = "0" + devn;

        // final  AlertDialog dialog1 = null;

        // LayoutInflater inflater =getLayoutInflater();

        //Switch5+1
        if (Switch_model.equals("S051") || Switch_model.equals("S021")||Switch_model.equals("S141")) {

            if (Switchnumbr_upd.equals("98")) {

                View alertLayout_51 = inflater.inflate(R.layout.popup_swbfanspeed, null);
                android.support.v7.app.AlertDialog.Builder alert = new  android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);
                //  alert.setTitle("Login");
                // this is set the view from XML inside AlertDialog
                alert.setView(alertLayout_51);
                // disallow cancel of AlertDialog on click of back button and outside touch
                alert.setCancelable(true);

                dialog1 = alert.create();
                dialog1.show();


                int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.60);
                int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.35);
                dialog1.getWindow().setLayout(width, height);
                dialog1.show();

                s1 = (Button) alertLayout_51.findViewById(R.id.s1);
                s2 = (Button) alertLayout_51.findViewById(R.id.s2);
                s3 = (Button) alertLayout_51.findViewById(R.id.s3);
                s4 = (Button) alertLayout_51.findViewById(R.id.s4);
                s5 = (Button) alertLayout_51.findViewById(R.id.s5);
                fan_dec = (Button) alertLayout_51.findViewById(R.id.fan_dec);
                fan_inc = (Button) alertLayout_51.findViewById(R.id.fan_inc);
                fan_speed = (Spinner) alertLayout_51.findViewById(R.id.fan_speed);
                //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
                save = (Button) alertLayout_51.findViewById(R.id.save);
                cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

                final String[] str = {"0", "1", "2", "3", "4"};
                ArrayAdapter<String> adp2 = new ArrayAdapter<String>(timer_popup_view.getRootView().getContext(), R.layout.spinner_item, str);
                adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                fan_speed.setAdapter(adp2);


                btnarr_timer_switchs = new Button[]{s1, s2, s3, s4, s5};
                bulbon = new ArrayList<>();

                for (int h = 0; h < btnarr_timer_switchs.length; h++) {
                    btnarr_timer_switchs[h].setTag(0);
                }
                Log.d("TAG", Data_upd);

                String fanvals=""+Data_upd.charAt(14);

                if ((!(fanvals.equals("0"))|| !(fanvals.equals(""))||!(fanvals==null) )) {
                    int fnvalu=Integer.parseInt(fanvals);
                    // int position =(fnvalu-1);
                    fan_speed.setSelection(fnvalu);

                }

                s1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (s1.getTag().equals(0)) {
                            s1.setBackgroundResource(R.mipmap.bulb02);
                            s1.setTag(1);
                            bulbon.add(1);
                        } else {
                            s1.setBackgroundResource(R.mipmap.bulb01);
                            s1.setTag(0);
                            bulbon.remove(1);
                        }

                        Log.d("TAG", String.valueOf(bulbon));
                    }
                });

                s2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (s2.getTag().equals(0)) {
                            s2.setBackgroundResource(R.mipmap.bulb02);
                            s2.setTag(1);
                            bulbon.add(2);
                        } else {
                            s2.setBackgroundResource(R.mipmap.bulb01);
                            s2.setTag(0);
                            bulbon.remove(2);
                        }
                        Log.d("TAG", String.valueOf(bulbon));

                    }
                });
                s3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (s3.getTag().equals(0)) {
                            s3.setBackgroundResource(R.mipmap.bulb02);
                            s3.setTag(1);
                            bulbon.add(3);
                        } else {
                            s3.setBackgroundResource(R.mipmap.bulb01);
                            s3.setTag(0);
                            bulbon.remove(3);
                        }
                        //Log.d("TAG", String.valueOf(bulbon));

                    }
                });
                s4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (s4.getTag().equals(0)) {
                            s4.setBackgroundResource(R.mipmap.bulb02);
                            s4.setTag(1);
                            bulbon.add(4);
                        } else {
                            s4.setBackgroundResource(R.mipmap.bulb01);
                            s4.setTag(0);
                            bulbon.remove(4);
                        }
                        //Log.d("TAG", String.valueOf(bulbon));

                    }
                });
                s5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (s5.getTag().equals(0)) {
                            s5.setBackgroundResource(R.mipmap.bulb02);
                            s5.setTag(1);
                            bulbon.add(5);
                        } else {
                            s5.setBackgroundResource(R.mipmap.bulb01);
                            s5.setTag(0);
                            bulbon.remove(5);
                        }
                        //Log.d("TAG", String.valueOf(bulbon));

                    }
                });

                fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                        if (i == 0) {
                            fanspeed = "0";
                        } else {
                            fanspeed = "" + i;
                            bulbon.add("98");
                        }
                        Log.d("TAG", fanspeed);
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {
                    }
                });

                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        if (bulbon != null && bulbon.size() > 0) {
                            for (Object area : bulbon) {

                                if (area.equals("98")) {
                                    off_data = "723";
                                    on_data = "71" + fanspeed;

                                    //ar_num_switch.add(area);
                                    //  ar_off_data.add("001000" + devn + room_num + off_data + "000000000000000");
                                    // ar_on_data.add("001000" + devn + room_num + on_data + "000000000000000");
                                    Switch_numdata = "98";
                                    trdata_Str_ON ="001000" + devn + room_num + on_data + "000000000000000";
                                    trdata_Str_OFF="001000" + devn + room_num + off_data + "000000000000000";


                                   // Toast.makeText(view.getContext(), "Set inside 98" + " devn" + devn + " room_num " + room_num + " offdata" + off_data +"on_data"+on_data, Toast.LENGTH_LONG).show();


                                } else {
                                    off_data = "30" + area;
                                    on_data = "20" + area;

                                    ar_num_switch.add(area);
                                    ar_off_data.add("001000" + devn + room_num + off_data + "000000000000000");
                                    ar_on_data.add("001000" + devn + room_num + on_data + "000000000000000");
                                }

                            }

                           // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data +"on_data"+on_data, Toast.LENGTH_LONG).show();


                        } else {
                            Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                        }
                        dialog1.dismiss();

                    }
                });
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog1.dismiss();

                    }
                });
            }

        }else if (Switch_model.equals("RGB1")) {

            Setoper_rgb();

        } else if (Switch_model.equals("DMR1")) {

            Setoper_dmr();

        } else if (Switch_model.equals("CLNR")) {

            Setoper_Cur_nrsh();

        } else if (Switch_model.equals("WPS1")||Switch_model.equals("WPD1")) {

            Setoper_pir_light();

        }else {


            Toast.makeText(timer_popup_view.getRootView().getContext(), "Default Operation" + Switch_model, Toast.LENGTH_LONG).show();

        }
    }

    private void rb_repeat_ondays_func() {

        for (int h = 0; h < btnarr_weekdays.length; h++) {
            btnarr_weekdays[h].setEnabled(true);
        }

        for (int k = 2; k < lay_all_arr.length; k++) {
            lay_all_arr[k].setEnabled(false);
            lay_all_arr[k].setAlpha((float) 0.5);
        }

        for (int m = 4; m < np_arr.length; m++) {

            np_arr[m].setEnabled(false);
        }

        cb_repeat_ptern.setAlpha(1.0f);
        cb_repeat_ptern.setEnabled(true);
        lay_rep_ondays_A.setAlpha((float) 1.0);
        lay_rep_ondays_B.setAlpha((float) 1.0);
        rb_cyclic.setChecked(false);
        rb_selecteddate.setChecked(false);
    }


    private void rb_cyclic_func() {

        rb_cyclic.setChecked(true);

        for (int h = 0; h < btnarr_weekdays.length; h++) {
            btnarr_weekdays[h].setEnabled(false);
            btnarr_weekdays[h].setBackgroundResource(days_images_grey[h]);
        }


        rb_repeat_ondays.setChecked(false);
        rb_selecteddate.setChecked(false);
        cb_repeat_ptern.setChecked(false);

        for (int m = 4; m < np_arr.length; m++) {

            if (m == 4 || m == 5) {
                np_arr[m].setEnabled(true);
            } else {
                np_arr[m].setEnabled(false);
            }
        }

        for (int k = 0; k < lay_all_arr.length; k++) {

            if (k == 2 || k == 3) {
                lay_all_arr[k].setEnabled(true);
                lay_all_arr[k].setAlpha((float) 1.0);

            } else {

                lay_all_arr[k].setEnabled(false);
                lay_all_arr[k].setAlpha((float) 0.5);
            }
        }
        cb_repeat_ptern.setAlpha(0.5f);
        cb_repeat_ptern.setEnabled(false);
    }


    private void rb_selecteddate_func() {

        rb_selecteddate.setChecked(true);
        rb_repeat_ondays.setChecked(false);
        rb_cyclic.setChecked(false);

        for (int m = 4; m < np_arr.length; m++) {

            if (m == 6 || m == 7 || m == 8) {
                np_arr[m].setEnabled(true);
            } else {
                np_arr[m].setEnabled(false);
            }
        }

        for (int k = 0; k < lay_all_arr.length; k++) {

            if (k == 4 || k == 5) {
                lay_all_arr[k].setEnabled(true);
                lay_all_arr[k].setAlpha((float) 1.0);

            } else {

                lay_all_arr[k].setEnabled(false);
                lay_all_arr[k].setAlpha((float) 0.5);
            }
        }

        for (int h = 0; h < btnarr_weekdays.length; h++) {
            btnarr_weekdays[h].setEnabled(false);
            btnarr_weekdays[h].setBackgroundResource(days_images_grey[h]);
        }

        cb_repeat_ptern.setEnabled(true);
        cb_repeat_ptern.setAlpha(1.0f);


    }


    public void popupinfo_upd(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
              //  inflater=getLayoutInflater();

                if(timer_popup_view==null)
                    timer_popup_view = inflater.inflate(R.layout.popup_timersettng_lay, null);

              //  layout_view= inflater.inflate(R.layout.timer_edit_activity, null);

               AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(timer_popup_view.getRootView().getContext());

                // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParent());
               // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this.getActivity());

                alertDialogBuilder.setTitle("INFO");
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                                if (timerdialog != null)
                                    timerdialog.dismiss();

                                ConnectAndDownload_timer();

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                if (!TimerEdit_Database.this.isFinishing()){
                    alertDialog.show();
                }else {
                    Toast.makeText(TimerEdit_Database.this,msg,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(TimerEdit_Database.this, TimerEdit_Database.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);

               /*     ConnectAndDownload_timer();

                    if (timerdialog != null)
                        timerdialog.dismiss();
*/
                    /*if(timerdialog!=null)
                        timerdialog.dismiss();*/
                }
            }
        });

    }

    public void popupinfo(final String msg) {

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(timer_popup_view==null)
                    timer_popup_view = inflater.inflate(R.layout.popup_timersettng_lay, null);

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(timer_popup_view.getRootView().getContext());

               // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getParent());

                alertDialogBuilder.setTitle("INFO");
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                if (!TimerEdit_Database.this.isFinishing()){
                    alertDialog.show();
                }else {
                   Toast.makeText(TimerEdit_Database.this,msg,Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void popupinfodel(final String msg) {
       // if(timer_popup_view==null)
        //    timer_popup_view = inflater.inflate(R.layout.popup_timersettng_lay, null);
        //timer_delete_view = inflater.inflate(R.layout.timer_edit_activity, null);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimerEdit_Database.this);

                //AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(timer_delete_view.getRootView().getContext());
                alertDialogBuilder.setTitle("INFO");
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                dialog.cancel();
                                ConnectAndDownload_timer();

                            }
                        });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
               // alertDialog.show();

                if (!TimerEdit_Database.this.isFinishing()){
                    alertDialog.show();
                }else {
                    Toast.makeText(TimerEdit_Database.this,msg,Toast.LENGTH_LONG).show();

                    Intent intent = new Intent(TimerEdit_Database.this, TimerEdit_Database.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
                    startActivity(intent);
                }
            }
        });

    }


    void numberpicker() {

        np_frmtime_hrs.setMaxValue(23); // max value 100
        np_frmtime_hrs.setMinValue(0);   // min value zero
        np_frmtime_hrs.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        np_frmtime_min.setMaxValue(59); // max value 100
        np_frmtime_min.setMinValue(0);   // min value zero
        np_frmtime_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        np_totime_hrs.setMaxValue(24); // max value 100
        np_totime_hrs.setMinValue(0);   // min value zero
        np_totime_hrs.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_totime_min.setMaxValue(59); // max value 100
        np_totime_min.setMinValue(0);   // min value zero
        np_totime_min.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        int NUMBER_OF_VALUES = 30;
        int PICKER_RANGE = 2;
        displayedValues = new String[NUMBER_OF_VALUES];
        for (int i = 0; i < NUMBER_OF_VALUES; i++)
            displayedValues[i] = String.valueOf(PICKER_RANGE * (i + 1));

        np_ontime_cyc.setMinValue(0);
        np_ontime_cyc.setMaxValue(displayedValues.length - 1);
        np_ontime_cyc.setDisplayedValues(displayedValues);
        np_ontime_cyc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


       /* int NUMBER_OF_VALUES2 = 29;
        int PICKER_RANGE2 = 2;
        displayedValues2  = new String[NUMBER_OF_VALUES2];
        for(int i=0; i<NUMBER_OF_VALUES2; i++)
            displayedValues2[i] = String.valueOf(PICKER_RANGE2 * (i+1));*/

        np_offtime_cyc.setMinValue(0);
        np_offtime_cyc.setMaxValue(displayedValues.length - 1);
        np_offtime_cyc.setDisplayedValues(displayedValues);
        np_offtime_cyc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_date_seldate.setMaxValue(31); // max value 100
        np_date_seldate.setMinValue(1);   // min value zero
        np_date_seldate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        String[] arrayPicker = new String[]{"", "JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};


        //set max value from length array string reduced 1
        np_month_seldate.setMaxValue(arrayPicker.length - 1);
        //implement array string to number picker
        np_month_seldate.setDisplayedValues(arrayPicker);
        //disable soft keyboard
        np_month_seldate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);
        //set wrap true or false, try it you will know the difference

        np_year_seldate.setMaxValue(2106); // max value 100
        np_year_seldate.setMinValue(2006);   // min value zero
        np_year_seldate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        String date = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        String splitTime[] = date.split("-");
        seldate_year = splitTime[0];
        seldate_month = splitTime[1];
        seldate_day = splitTime[2];

        np_year_seldate.setValue(Integer.parseInt(seldate_year));
        np_month_seldate.setValue(Integer.parseInt(seldate_month));
        np_date_seldate.setValue(Integer.parseInt(seldate_day));

        for (int y = 0; y < np_arr.length - 1; y++) {

            np_arr[y].setFormatter(new NumberPicker.Formatter() {
                @Override
                public String format(int i) {
                    return String.format("%02d", i);
                }
            });
        }

        np_year_seldate.setFormatter(new NumberPicker.Formatter() {
            @Override
            public String format(int i) {
                return String.format("%04d", i);
            }
        });



        np_totime_hrs.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal==24){
                    np_totime_min.setEnabled(false);
                }else{
                    np_totime_min.setEnabled(true);
                }
            }
        });

        np_totime_min.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                if(newVal==24){
                    np_totime_min.setEnabled(false);
                }else{
                    np_totime_min.setEnabled(true);
                }
            }
        });
    }
    public void changebutton(boolean smooth ,boolean fade,boolean strobe,boolean flash ){

        StaticVariabes_div.log("inside change btn ",smooth+" "+fade);
        if(smooth)
            btn_Smooth.setBackgroundResource(R.mipmap.smooth1);
        else
            btn_Smooth.setBackgroundResource(R.mipmap.smooth);

        if(fade)
            btn_Fade.setBackgroundResource(R.mipmap.fade1);
        else
            btn_Fade.setBackgroundResource(R.mipmap.fade);

        if(strobe)
            btn_Strobe.setBackgroundResource(R.mipmap.strobe1);
        else
            btn_Strobe.setBackgroundResource(R.mipmap.strobe);

        if(flash)
            btn_Flash.setBackgroundResource(R.mipmap.flash1);
        else
            btn_Flash.setBackgroundResource(R.mipmap.flash);
    }
    public void Setoper_rgb() {

        View rgb_poplay = inflater.inflate(R.layout.popup_rgb_timer, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);

        alert.setView(rgb_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        btn_Red = (Button) rgb_poplay.findViewById(R.id.btn_red);
        btn_Green = (Button) rgb_poplay.findViewById(R.id.btn_green);
        btn_Blue = (Button) rgb_poplay.findViewById(R.id.btn_blue);
        btn_Pink = (Button) rgb_poplay.findViewById(R.id.btn_purple);
        btn_White = (Button) rgb_poplay.findViewById(R.id.btn_white);
        btn_orange = (Button) rgb_poplay.findViewById(R.id.btn_orange);

        btn_Fade = (Button) rgb_poplay.findViewById(R.id.btn_fade);
        btn_Flash = (Button) rgb_poplay.findViewById(R.id.btn_flash);
        btn_Smooth = (Button) rgb_poplay.findViewById(R.id.btn_smooth);
        btn_Strobe = (Button) rgb_poplay.findViewById(R.id.btn_strobe);
        btn_Speedp = (Button) rgb_poplay.findViewById(R.id.btn_speedup);
        btn_Speedm = (Button) rgb_poplay.findViewById(R.id.btn_speeddown);

        btn_on = (Button) rgb_poplay.findViewById(R.id.btn_rgbon);
        btn_off = (Button) rgb_poplay.findViewById(R.id.btn_rgboff);

        rgbvalue = (TextView) rgb_poplay.findViewById(R.id.textView1);
        colorPicker = (A_rc) rgb_poplay.findViewById(R.id.colorPicker);

        sk = (SeekBar) rgb_poplay.findViewById(R.id.brightseekBar);

        tv_speedval = (TextView) rgb_poplay.findViewById(R.id.tv_speedvalue);

        save = (Button) rgb_poplay.findViewById(R.id.save);
        cancel = (Button) rgb_poplay.findViewById(R.id.cancel);
        sk.setMax(10);

        String rgb_rep_data[];
        String prevdata = Data_upd;
        rgb_rep_data = prevdata.split(";");
        rgb_prev_color_efft = rgb_rep_data[1].substring(12, 15);

        if (rgb_prev_color_efft.equals("112")||rgb_prev_color_efft.equals("0")) {

            rgb_prev_color_r = rgb_rep_data[1].substring(15, 18);
            rgb_prev_color_g = rgb_rep_data[1].substring(18, 21);
            rgb_prev_color_b = rgb_rep_data[1].substring(21, 24);

            rgb_color_effect = "0," + rgb_prev_color_r + "," + rgb_prev_color_g + "," + rgb_prev_color_b + ",B";

            rgbvalue.setText("");
            rgbvalue.setBackgroundColor(Color.rgb(Integer.parseInt(rgb_prev_color_r), Integer.parseInt(rgb_prev_color_g), Integer.parseInt(rgb_prev_color_b)));

        } else {

            rgbvalue.setBackgroundColor(Color.BLACK);
            rgb_color_effect = rgb_prev_color_efft + ",0,0,0,A";

            switch (Effects.valueOf("d" + rgb_prev_color_efft)) {
                case d104:
                    rgbvalue.setText("Flash");
                    changebutton(false,false,false,true);
                    break;
                case d105:
                    rgbvalue.setText("Strobe");
                    changebutton(false,false,true,false);
                    break;
                case d106:
                    rgbvalue.setText("Smooth");
                    changebutton(true,false,false,false);
                    break;
                case d107:
                    rgbvalue.setText("Fade");
                    changebutton(false,true,false,false);
                    break;

                default:
                    rgbvalue.setText("");
                    break;
            }


        }

        rgb_prev_bright = rgb_rep_data[2].substring(12, 15);
        rgb_prev_speed = rgb_rep_data[3].substring(12, 15);
        rgb_speed = rgb_prev_speed + ",0,0,0,A";
        rgbbright = rgb_prev_bright + ",0,0,0,A";
        sk.setProgress(((Integer.parseInt(rgb_prev_bright)) - 130) * 10);
        // Log.d("TAG", "rgb_prev_speed int"+ Integer.parseInt(rgb_prev_speed));
        ispeed = Integer.parseInt(rgb_prev_speed);
        int sp = Integer.parseInt(rgb_prev_speed) - 120;
        tv_speedval.setText(sp + "");

        btn_Flash.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                rgbvalue.setText("Flash");
                rgbvalue.setBackgroundColor(Color.BLACK);
                rgb_color_effect = "104,000,000,000,A";
                changebutton(false,false,false,true);

            }
        });
        btn_Strobe.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                rgbvalue.setText("Strobe");
                rgbvalue.setBackgroundColor(Color.BLACK);
                rgb_color_effect = "105,000,000,000,A";
                changebutton(false,false,true,false);


            }
        });

        btn_Fade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                rgbvalue.setText("Fade");
                rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect = "107,000,000,000,A";
                changebutton(false,true,false,false);

            }
        });

        btn_Smooth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                rgbvalue.setText("Smooth");
                rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect = "106,000,000,000,A";
                changebutton(true,false,false,false);
            }
        });

        btn_Red.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.RED);
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.RED);
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Green.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(0, 255, 0));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(0, 255, 0));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Blue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(0, 0, 255));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(0, 0, 255));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });


        btn_Pink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(255, 0, 128));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });

        btn_White.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.WHITE);
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });

        btn_orange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(255, 165, 0));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Speedp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if (ispeed < 130) {
                    ispeed++;
                }
                rgb_speed = ispeed + ",000,000,000,A";
                tv_speedval.setText(String.valueOf(ispeed - 120));

            }
        });
        btn_Speedm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (ispeed > 121) {
                    ispeed--;
                }
                rgb_speed = ispeed + ",000,000,000,A";
                tv_speedval.setText(String.valueOf(ispeed - 120));

            }
        });
        colorPicker.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int color = colorPicker.getColor();

                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color), Color.green(color), Color.blue(color)));
                rgb_color_effect = "0," + Color.red(color) + "," + Color.green(color) + "," + Color.blue(color) + ",B";
                changebutton(false,false,false,false);
                return false;
            }
        });


        sk.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                count = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            public void onStopTrackingTouch(SeekBar seekBar) {

                if (count < 1) count = 1;
                rgbbright = count + 130 + ",000,000,000,A";
            }
        });


        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (rgb_color_effect != null || !rgb_color_effect.equals("") || rgb_color_effect != "") {
                    rgbinst = rgb_color_effect;
                } else {
                    rgbinst = "0,255,000,000,A";
                }


                String Splitdata[] = rgbinst.split(",");
                rgbinst = transmitdata(Splitdata[0], Splitdata[1], Splitdata[2], Splitdata[3], Splitdata[4]);

                if (rgbbright == null || rgbbright == "")
                    rgbbrightinst = "132,000,000,000,A";
                else
                    rgbbrightinst = rgbbright;

                String Splitdata1[] = rgbbrightinst.split(",");
                rgbbrightinst = transmitdata(Splitdata1[0], Splitdata1[1], Splitdata1[2], Splitdata1[3], Splitdata1[4]);

                if (rgb_speed == null || rgb_speed == "")
                    rgb_speedinst = "121,000,000,000,A";
                else
                    rgb_speedinst = rgb_speed;

                String Splitdata2[] = rgb_speedinst.split(",");
                rgb_speedinst = transmitdata(Splitdata2[0], Splitdata2[1], Splitdata2[2], Splitdata2[3], Splitdata2[4]);

                String ondatargbrep = "102,000,000,000,A";
                String Splitdata3[] = ondatargbrep.split(",");
                ondatargbrep = transmitdata(Splitdata3[0], Splitdata3[1], Splitdata3[2], Splitdata3[3], Splitdata3[4]);

                Switch_numdata = "0";
                trdata_Str_ON = ondatargbrep + ";" + rgbinst + ";" + rgbbrightinst + ";" + rgb_speedinst;
                String offdatargb = "103,000,000,000,A";
                String Splitdata4[] = offdatargb.split(",");
                offdatargb = transmitdata(Splitdata4[0], Splitdata4[1], Splitdata4[2], Splitdata4[3], Splitdata4[4]);
                trdata_Str_OFF = offdatargb;
                dialog1.dismiss();
            }
        });

    }

    String transmitdata(final String val, final String rc, final String gc, final String bc, String type) {

        String str;
        String str2 = "" + val;
        while (str2.length() < 3) str2 = "0" + str2;
        String redStr = "" + rc;
        while (redStr.length() < 3) redStr = "0" + redStr;
        StaticVariabes_div.log("redStr" + redStr, TAG1);
        String greenStr = "" + gc;
        while (greenStr.length() < 3) greenStr = "0" + greenStr;
        StaticVariabes_div.log("greenStr" + greenStr, TAG1);
        String blueStr = "" + bc;
        while (blueStr.length() < 3) blueStr = "0" + blueStr;
        StaticVariabes_div.log("blueStr" + blueStr, TAG1);
        StaticVariabes_div.log("Str" + str2, TAG1);
        StaticVariabes_div.log("devno" + devn, TAG1);

        // devn=StaticVariabes_div.devnumber;
        while (devn.length() < 4) devn = "0" + devn;

        //  room_num = StaticVariabes_div.room_n;
        while (room_num.length() < 2) room_num = "0" + room_num;

        if (type.equals("A")) {

            str = "0" + "01" + "000" + devn + room_num + str2 + "000000000000000";
            StaticVariabes_div.log("str" + str, TAG1);

        } else {

            str = "0" + "01" + "000" + devn + room_num + "112" + redStr + greenStr + blueStr + "000000";
            StaticVariabes_div.log("str" + str, TAG1);

        }

        return str;
    }
    public void Setoper_Cur_nrsh(){

        View crsh_poplay = inflater.inflate(R.layout.popup_crsh_checkbox_tmr, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);

        alert.setView(crsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        cb_sheer_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_sheer);
        cb_curtain_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_curtain);


        save = (Button) crsh_poplay.findViewById(R.id.save);
        cancel = (Button) crsh_poplay.findViewById(R.id.cancel);

        String cur_clnr_rep_data[];
        String prevdata = Data_upd;
        cur_clnr_rep_data = prevdata.split(";");

        if(cur_clnr_rep_data.length>1){
            cb_sheer_select.setChecked(true);
            cb_curtain_select.setChecked(true);
        }else{
            cur_prev_data = cur_clnr_rep_data[0].substring(12, 15);

            if(cur_prev_data.equals("101")){
                cb_curtain_select.setChecked(true);
            }else{
                cb_sheer_select.setChecked(true);
            }
            StaticVariabes_div.log(cur_clnr_rep_data[0]+"cur_prev_data"+cur_prev_data,TAG1);
        }


        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String  on_data_sh=null,off_data_sh=null,on_data_cur=null,off_data_cur=null;

                while (devn.length() < 4) devn = "0" + devn;

                on_data_cur="001000" + devn + room_num + "101" + "000000000000003";
                off_data_cur="001000" + devn + room_num+ "102" + "000000000000003";

                if(cb_sheer_select.isChecked()) {

                    String devnam = dev_name + "SH";
                    mas_adap=new MasterAdapter(timer_popup_view.getRootView().getContext());
                    mas_adap.open();
                    String shrdevno = mas_adap.getdevno_frmdevname(devnam);
                    mas_adap.close();

                    while (shrdevno.length() < 4) shrdevno = "0" + shrdevno;

                    on_data_sh = "001000" + shrdevno + room_num + "105" + "000000000000003";
                    off_data_sh = "001000" + shrdevno + room_num + "106" + "000000000000003";
                }

                if(cb_sheer_select.isChecked()&&cb_curtain_select.isChecked()) {


                    Switch_numdata = "0";
                    trdata_Str_ON = on_data_sh + ";" + on_data_cur;
                    trdata_Str_OFF = off_data_sh + ";" + off_data_cur;

                }else if(cb_sheer_select.isChecked()&&!(cb_curtain_select.isChecked())){

                    Switch_numdata = "0";
                    trdata_Str_ON = on_data_sh ;
                    trdata_Str_OFF = off_data_sh;

                }else if(cb_curtain_select.isChecked()&&!(cb_sheer_select.isChecked())){

                    Switch_numdata = "0";
                    trdata_Str_ON = on_data_cur;
                    trdata_Str_OFF =off_data_cur ;

                }else if(!(cb_curtain_select.isChecked())&&!(cb_sheer_select.isChecked())) {
                    // Toast.makeText(getActivity(),"Please Select Atleast One",Toast)
                }

                StaticVariabes_div.log(dev_name+"cur_on_data"+trdata_Str_ON,TAG1);
                StaticVariabes_div.log("cur_off_data"+trdata_Str_OFF,TAG1);

                dialog1.dismiss();
            }
        });

    }


    public void Setoper_pir_light(){

        View crsh_poplay = inflater.inflate(R.layout.popup_pir_ligh_chkbox_tmr, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);

        alert.setView(crsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        cb_pir_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_pir);
        cb_ligsen_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_ligsen);


        save = (Button) crsh_poplay.findViewById(R.id.save);
        cancel = (Button) crsh_poplay.findViewById(R.id.cancel);

        String pir_lig_rep_data[];
        String prevdata = Data_upd;
        pir_lig_rep_data = prevdata.split(";");

        if(pir_lig_rep_data.length>1){
            cb_ligsen_select.setChecked(true);
            cb_pir_select.setChecked(true);
        }else{
            cur_prev_data = pir_lig_rep_data[0].substring(12, 15);

            if(cur_prev_data.equals("101")){
                cb_ligsen_select.setChecked(true);
            }else{
                cb_pir_select.setChecked(true);
            }
            StaticVariabes_div.log(pir_lig_rep_data[0]+"pir_prev_data"+pir_lig_rep_data,TAG1);
        }


        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.50);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                String  on_data_ligsen=null;
                String  off_data_ligsen=null;

                String  on_data_pir=null;
                String  off_data_pir=null;

                while (devn.length() < 4) devn = "0" + devn;


                on_data_pir="001000" + devn + room_num + "909" + "000000000000003";
                off_data_pir="001000" + devn + room_num+ "910" + "000000000000003";


                if(cb_ligsen_select.isChecked()) {

                    on_data_ligsen = "001000" + devn + room_num + "101" + "000000000000003";
                    off_data_ligsen = "001000" + devn + room_num + "102" + "000000000000003";
                }


                if(cb_ligsen_select.isChecked()&&cb_pir_select.isChecked()) {


                    Switch_numdata = "0";
                    trdata_Str_OFF =off_data_ligsen + ";" + off_data_pir;
                    trdata_Str_ON =on_data_ligsen + ";" + on_data_pir;

                }else if(cb_ligsen_select.isChecked()&&!(cb_pir_select.isChecked())){

                    Switch_numdata = "0";
                    trdata_Str_OFF =off_data_ligsen  ;
                    trdata_Str_ON =on_data_ligsen ;

                }else if(cb_pir_select.isChecked()&&!(cb_ligsen_select.isChecked())){

                    Switch_numdata = "0";
                    trdata_Str_OFF =off_data_pir;
                    trdata_Str_ON =on_data_pir ;

                }else if(!(cb_pir_select.isChecked())&&!(cb_ligsen_select.isChecked())) {
                    // Toast.makeText(getActivity(),"Please Select Atleast One",Toast)
                }

                dialog1.dismiss();
            }
        });


    }
    public void Setoper_dmr() {


        View dmr_poplay = inflater.inflate(R.layout.popup_dimmer_timer, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);
        seekArc = (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);
        alert.setView(dmr_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) dmr_poplay.findViewById(R.id.save);
        cancel = (Button) dmr_poplay.findViewById(R.id.cancel);
        dim_img = (ImageView) dmr_poplay.findViewById(R.id.image);
        // seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);

        btn_low = (Button) dmr_poplay.findViewById(R.id.btn_dimlow);
        btn_medium = (Button) dmr_poplay.findViewById(R.id.btn_dimmedium);
        btn_high = (Button) dmr_poplay.findViewById(R.id.btn_dimhigh);
        dim_img.setImageResource(R.drawable.dim_level0);

        String prevdata = Data_upd;
        dmr_data = prevdata.split(";");
        dmr_prev_bright = dmr_data[1].substring(15, 18);
        dimvalue = dmr_prev_bright;

        //rgb_prev_bright=rgb_cyc_data[2].substring(12, 15);
        StaticVariabes_div.log("dmr_prev_bright"+dmr_prev_bright,TAG1);
        seekArc.setProgress(Integer.parseInt(dmr_prev_bright));
        // Log.d("TAG", "dmr_prev_bright int"+ Integer.parseInt(dmr_prev_bright));
        seekstatus(Integer.parseInt(dmr_prev_bright));

        btn_low.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue = "002";
                seekArc.setProgress(2);
                seekstatus(2);
            }
        });


        btn_medium.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue = "040";
                seekArc.setProgress(40);
                seekstatus(40);
            }
        });


        btn_high.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue = "255";
                seekArc.setProgress(100);
                seekstatus(255);
            }
        });

        seekArc.setOnSeekArcChangeListener(new ShaderSeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(ShaderSeekArc seekArc, float progress) {

                count = (int) seekArc.getProgress();

            }

            @Override
            public void onStartTrackingTouch(ShaderSeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(ShaderSeekArc seekArc) {
                //  if (count < 10) count = 2;
                //count = dimmrbright(count);
                dimvalue = "" + count;
                int cnt = Integer.parseInt(dimvalue);
                seekstatus(cnt);
            }
        });


        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();
        ;


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                diminst = dimvalue;

                if (diminst == null) diminst = "2";

                diminst = transmitdata_dmr(Integer.parseInt(diminst), "B");

                ondatadmr = transmitdata_dmr(102, "A");

                Switch_numdata = "0";
                trdata_Str_ON = ondatadmr + ";" + diminst;
                trdata_Str_OFF = transmitdata_dmr(103, "A");


                dialog1.dismiss();
            }
        });


    }

    void seekstatus(int i) {
        n = i;
        runOnUiThread(new Runnable() {
            public void run() {
                StaticVariabes_div.log("seekstatus" + n, TAG1);
                if (n == 0) {
                    dim_img.setImageResource(mThumbIds[0]);
                } else if (n >= 1 && n <= 10) {
                    dim_img.setImageResource(mThumbIds[1]);
                } else if (n > 10 && n <= 20) {
                    dim_img.setImageResource(mThumbIds[2]);
                } else if (n > 20 && n <= 40) {
                    dim_img.setImageResource(mThumbIds[3]);
                } else if (n > 40 && n <= 70) {
                    dim_img.setImageResource(mThumbIds[4]);
                } else if (n > 70 && n <= 100) {
                    dim_img.setImageResource(mThumbIds[5]);
                } else if (n > 100 && n <= 140) {
                    dim_img.setImageResource(mThumbIds[6]);
                } else if (n > 140 && n <= 180) {
                    dim_img.setImageResource(mThumbIds[7]);
                } else if (n > 180 && n <= 220) {
                    dim_img.setImageResource(mThumbIds[8]);
                } else if (n > 220 && n <= 258) {
                    dim_img.setImageResource(mThumbIds[9]);
                }

            }
        });

    }


    String transmitdata_dmr(int val3, String type) {
        String str, val4;
        val4 = "" + val3;
        // devn = StaticVariabes_div.devnumber;
        while (devn.length() < 4) devn = "0" + devn;
        while (val4.length() < 3) val4 = "0" + val4;

        if (type.equals("A")) {

            str = "0" + "01" + "000" + devn + room_num + val4 + "000000000000000";
            StaticVariabes_div.log("str" + str, TAG1);

        } else {

            str = "0" + "01" + "000" + devn + room_num + "112" + val4 + "000000000000";
            StaticVariabes_div.log("str" + str, TAG1);

        }

        return str;
    }


    public int dimmrbright(int i) {

        if (i <= 70) {
            return (int) map(i, 0, 70, 2, 50);
        } else if (i > 70 && i <= 100) {
            return (int) map(i, 71, 100, 51, 255);
        }
        return -1;
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max) {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }


    /////////////////delete////////////////////////////////////////////

    public void popupdelete() {
        timer_delete_view = inflater.inflate(R.layout.timer_edit_activity, null);
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(timer_delete_view.getRootView().getContext());
                alertDialogBuilder.setTitle("Delete Timer");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do You Really Want To Delete Timer")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletetimer_process();
                            }
                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

    }

    public void	deletetimer_process(){
        String ty,trdata,strtok,endtkn;
        while(DeviceNum_upd.length()<4){DeviceNum_upd="0"+DeviceNum_upd;
        }
        //Log.d("TAG", "trdata "+ trdata);
        // StaticVariabes_div.log("http"+Timer_Edit_Adapter.opertype_upd+"   trdata "+ trdata,TAG1);

        if(opertype_upd.equals("Repeat On Days")){
            ty="rep";strtok="[";endtkn="@";
            trdata=Frmtime_upd+","+Totime_upd+","+DeviceNum_upd+","+Switchnumbr_upd;

        }else if(opertype_upd.equals("Cyclic")){
            ty="cyc";strtok="[";endtkn="$";
            trdata=Frmtime_upd+","+Totime_upd+","+DeviceNum_upd+","+Switchnumbr_upd;

        }else{
            ty="repd";strtok="[";endtkn="@";
            trdata=Frmtime_upd+","+Totime_upd+","+DeviceNum_upd+","+Switchnumbr_upd;

        }

        Send_Timer_dat_aes(trdata,strtok,endtkn);

    }


    /////////////////delete all////////////////////////////////////////////

    public void popupdelete_all() {
        timer_delete_view = inflater.inflate(R.layout.timer_edit_activity, null);
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(timer_delete_view.getRootView().getContext());
               // AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(TimerEdit_Database.this);
                alertDialogBuilder.setTitle("Delete All Timer");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do You Really Want To Delete Timer")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete_all_timer_process();
                            }
                        });

                alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();

                    }
                });
                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                // show it
                alertDialog.show();
            }
        });

    }

    public void	delete_all_timer_process(){

        StaticVariabes_div.log("del all" + StaticVariabes_div.timer_roomnum_edit, TAG1);

        Send_Timer_dat_aes(StaticVariabes_div.timer_roomnum_edit,"[","&");
    }


    public void onResume() {
        super.onResume();

        Log.i(TAG1, "Setting screen name spinner: " + StaticVariabes_div.spinnerroom);

        if(StaticVariabes_div.spinnerroom==null||StaticVariabes_div.spinnerroom.length()<1) {

            y = UniqueRoomList.indexOf(StaticVariabes_div.roomname);

        }else{

            y = UniqueRoomList.indexOf(StaticVariabes_div.spinnerroom);
        }
        room_name_spinner.setSelection(y);

        Log.i(TAG1, "Setting screen name: " + name +" y"+y +"rmno"+StaticVariabes_div.timer_roomnum_edit);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }



    ////////////////////////////////////////////////////////////////////////////

    public class Timer_Edit_Adap extends BaseAdapter {

        Context context;

         Holder holder;


        ArrayList offtime3;
         String roomnumber;
       public 	ArrayList<String> num,devicename,usern
                ,devicetype, operatedtype, switchnumber
                , days, date, time,fromTimeRep_date, toTimeRep_date
                , repeatAlways,ondataRep_date, offdataRep_date, ontimecyclic, offtimecyclic
                , oncyclicData, offCyclicData ,deviceNumber ,fromtimecyclic,totimecyclic,frmtimedisplay,totimedisplay,
                datatodisplay,offdatatodisplay,devicetypenumber,device_id;


        public Timer_Edit_Adap(Context cnt,TimerEdit_Database mainActivity,ArrayList serialnum , ArrayList opertype ,ArrayList devicename,
                                  ArrayList devicetype, ArrayList switchnum, ArrayList days,
                                  ArrayList date,ArrayList time, ArrayList frmtimerep,ArrayList totimerep, ArrayList ondatarep, ArrayList offdatarep,
                                  ArrayList repeatweekly,ArrayList ontime,ArrayList offtime,ArrayList devinum,ArrayList oncycdata,ArrayList offcycdata
                , ArrayList frmtimedisp,ArrayList totimedisp,ArrayList data_todisp,ArrayList offdatato_disp,ArrayList devtynum,ArrayList usernm) {
            context=  cnt;
            this.operatedtype=opertype;

            this.num=serialnum;
            this.devicename=devicename;
            this.usern=usernm;
            this.devicetype=devicetype;


            this.switchnumber=switchnum;
            this.days=days;
            this.date=date;
            this.time=time;

            this.fromTimeRep_date=frmtimerep;
            this.toTimeRep_date=totimerep;
            this.ondataRep_date=ondatarep;
            this.offdataRep_date=offdatarep;


            this.repeatAlways=repeatweekly;

            this.fromtimecyclic=frmtimerep;
            this.totimecyclic=totimerep;
            this.ontimecyclic=ontime;
            this.offtimecyclic=offtime;

            this.oncyclicData=oncycdata;
            this.offCyclicData=offcycdata;

            this.deviceNumber=devinum;

            this.frmtimedisplay=frmtimedisp;
            this.totimedisplay=totimedisp;
            this.datatodisplay=data_todisp;
            this.offdatatodisplay=offdatato_disp;

            this.devicetypenumber=devtynum;

            // devicename=devicename;
            //inflater = ( LayoutInflater )context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            // inflater = LayoutInflater.from(context);
            //inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }




        @Override
        public int getCount() {

            return devicename.size();

        }

        @Override
        public Object getItem(int position) {

            return position;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }




        public  class Holder
        {
            TextView num;
            TextView devicename,switch1,fromtime,totime,type,data,usernme;
            ImageButton edit,delete;
        }
        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            holder=new Holder();
            final View rowView;
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.single_item_list, null);
            holder.num=(TextView) rowView.findViewById(R.id.num);
            holder.devicename=(TextView) rowView.findViewById(R.id.devicename);
            holder.usernme=(TextView) rowView.findViewById(R.id.usrnam);
            holder.switch1=(TextView) rowView.findViewById(R.id.switch1);
            holder.fromtime=(TextView) rowView.findViewById(R.id.ontime);
            holder.totime=(TextView) rowView.findViewById(R.id.offtime);
            holder.type=(TextView) rowView.findViewById(R.id.type1);
            holder.data=(TextView) rowView.findViewById(R.id.data);
            holder.edit=(ImageButton) rowView.findViewById(R.id.edit);
            holder.delete=(ImageButton) rowView.findViewById(R.id.delete);

            holder.num.setText((CharSequence) num.get(position));
            holder.devicename.setText((CharSequence) devicename.get(position));
            holder.usernme.setText((CharSequence) usern.get(position));
            holder.switch1.setText((CharSequence) switchnumber.get(position));
            holder.fromtime.setText((CharSequence) frmtimedisplay.get(position));
            holder.totime.setText((CharSequence) totimedisplay.get(position));
            holder.type.setText((CharSequence) operatedtype.get(position));
            holder.data.setText((CharSequence) devicetype.get(position));

            StaticVariabes_div.log("datatodisplay"+datatodisplay.get(position),"data");
            String c = datatodisplay.get(position);

            if(devicetype.get(position).equals("RGB1")) {

                StaticVariabes_div.log("datatodisplay"+c,"data rgb");

                String rgb_data[] = c.split(";");
                // if(rgb_data.length>1) {
                String rgb_prev_color_efft = rgb_data[1].substring(12, 15);

                if (rgb_prev_color_efft.equals("112")) {

                    String rgb_prev_color_r = rgb_data[1].substring(15, 18);
                    String rgb_prev_color_g = rgb_data[1].substring(18, 21);
                    String rgb_prev_color_b = rgb_data[1].substring(21, 24);

                    // rgb_color_effect = "0," + rgb_prev_color_r + "," + rgb_prev_color_g + "," + rgb_prev_color_b + ",B";

                    holder.switch1.setText("");
                    holder.switch1.setBackgroundColor(Color.rgb(Integer.parseInt(rgb_prev_color_r), Integer.parseInt(rgb_prev_color_g), Integer.parseInt(rgb_prev_color_b)));

                } else {

                    holder.switch1.setBackgroundColor(Color.WHITE);
                    //rgb_color_effect = rgb_prev_color_efft + ",0,0,0,A";

                    switch (Effects_rgb.valueOf("d" + rgb_prev_color_efft)) {
                        case d104:
                            holder.switch1.setText("Flash");
                            break;
                        case d105:
                            holder.switch1.setText("Strobe");
                            break;
                        case d106:
                            holder.switch1.setText("Smooth");
                            break;
                        case d107:
                            holder.switch1.setText("Fade");
                            break;

                        default:
                            holder.switch1.setText("");
                            break;
                    }


                }
                // }
            }else
            if(devicetype.get(position).equals("DMR1")) {


                StaticVariabes_div.log("datatodisplay" + c, "data dmr");
                String dmr_data[] = c.split(";");

                String  dmr_prev_brigh_efft = dmr_data[1].substring(12, 15);
                holder.switch1.setText(dmr_prev_brigh_efft);


            }else if(devicetype.get(position).equals("CRS1")||devicetype.get(position).equals("CLS1")) {

                StaticVariabes_div.log("datatodisplay" + c, "data cur");

                String curd = c.substring(12, 15);

                StaticVariabes_div.log("datatodisplay" + c, "data cur");
                if(curd.equals("101")) {
                    holder.switch1.setText("open");
                }else{
                    holder.switch1.setText("close");
                }

            }else if(devicetype.get(position).equals("CLNR")) {
                StaticVariabes_div.log("datatodisplay" + c, "data clnr");

                String cur_data[] = c.split(";");

                if(cur_data.length>1) {

                    holder.switch1.setText("cr/sh");

                }else{
                    String cr = cur_data[0].substring(12, 15);

                    if (cr.equals("101")){
                        holder.switch1.setText("cr");
                    }else{
                        holder.switch1.setText("sh");
                    }
                }

            }else if(devicetype.get(position).equals("S051")||devicetype.get(position).equals("S021")||devicetype.get(position).equals("S141")) {

                if(switchnumber.get(position).equals("98")) {
                    StaticVariabes_div.log("switchnumber.get(position)" + switchnumber.get(position), "data swb");
                    String fanspeed = c.substring(14, 15);
                    holder.switch1.setText("Fan-"+fanspeed);
                }

            }else if(devicetype.get(position).equals("WPS1")||devicetype.get(position).equals("WPD1")) {
                StaticVariabes_div.log("datatodisplay" + c, "data pir");

                String pir_data[] = c.split(";");

                if(pir_data.length>1) {

                    holder.switch1.setText("pir/lig");

                }else{
                    String cr = pir_data[0].substring(12, 15);

                    if (cr.equals("101")){
                        holder.switch1.setText("lig");
                    }else{
                        holder.switch1.setText("pir");
                    }
                }

            }
            holder.delete.setTag(position);
            holder.delete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(final View v) {
                    Integer index = (Integer) v.getTag();
                    Frmtime_upd=frmtimedisplay.get(index.intValue());
                    Totime_upd=totimedisplay.get(index.intValue());
                    DeviceNum_upd = deviceNumber.get(index.intValue());
                    Switchnumbr_upd = switchnumber.get(index.intValue());
                    opertype_upd = operatedtype.get(index.intValue());
                    Devtype_upd = devicetype.get(index.intValue());


                    popupdelete();




                }
            });



            holder.edit.setTag(position);

            holder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Integer index = (Integer) view.getTag();
                   // ont = fromTimeRep_date.get(index.intValue());
                    DeviceName_upd = devicename.get(index.intValue());
                    Devtype_upd = devicetype.get(index.intValue());
                    DeviceNum_upd = deviceNumber.get(index.intValue());
                    Switchnumbr_upd = switchnumber.get(index.intValue());


                  //  offt = toTimeRep_date.get(index.intValue());
                    opertype_upd = operatedtype.get(index.intValue());


                    if(opertype_upd.equals("Cyclic")) {
                        Ontime_upd = ontimecyclic.get(index.intValue());
                        Offtime_upd = offtimecyclic.get(index.intValue());
                    }

                    if(opertype_upd.equals("Repeat On Date")) {
                        Date_upd = date.get(index.intValue());
                    }

                    if(opertype_upd.equals("Repeat On Date")||opertype_upd.equals("Repeat On Days")) {
                        repeaweekly_upd = repeatAlways.get(index.intValue());
                        timee = time.get(index.intValue());
                        days_upd = days.get(index.intValue());

                        ondatare = ondataRep_date.get(index.intValue());
                        offdatare = offdataRep_date.get(index.intValue());

                        datee = date.get(index.intValue());
                    }


                    Frmtime_upd=frmtimedisplay.get(index.intValue());
                    Totime_upd=totimedisplay.get(index.intValue());
                    Data_upd =datatodisplay.get(index.intValue());
                    OffData_upd =offdatatodisplay.get(index.intValue());

                    DevicetypeNum_upd = devicetypenumber.get(index.intValue());

                    StaticVariabes_div.log("devn" + DeviceNum_upd, "httpconnect");
                    StaticVariabes_div.log("devnam" + DeviceName_upd, "httpconnect");

                    timer_popup();
                }
            });
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //  Toast.makeText(context, "You Clicked "+ result.get(position), Toast.LENGTH_LONG).show();
                }
            });
            return rowView;
        }

    }




    private void ConnectAndDownload_timer(){


        SERVER_PASSWORD=StaticVariabes_div.loggeduser+StaticVariabes_div.loggedpwd;
        StaticVariables.printLog("SERVER_PASSWORD","SERVER PASSWORD : "+SERVER_PASSWORD);

        //StaticVariables.HOUSE_NAME=StaticVariabes_div.housename;

        StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_TIMER";
        Thread connectionThread=new Thread(){
            public void run(){

                runOnUiThread(new Runnable() {
                    public void run() {
                        //ProgressDialog("Downloading Timer Data From Server....");
                        if(timerdialog!=null)
                            timerdialog.dismiss();
                        pd = new ProgressDialog(TimerEdit_Database.this);
                        pd.setMessage("loading");
                        pd.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                        if(!TimerEdit_Database.this.isFinishing()) {
                            pd.show();
                        }
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
                           /// updateProgress(100);	//updating progress
                            pd.dismiss();
                            break;
                        }else if(Tcp.tcpConnected){
                            i=10;
                            serverConnected=true;
                           Tcp.tcpWrite_aes(SERVER_PASSWORD);	//sending password
                            delay(800);	//delay to get ok response from server
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
                            StaticVariables.HOUSE_DB_NAME=StaticVariables.HOUSE_NAME+"_TIMER";
                        }
                    });

                    //Sending Db Download Command
                    delay(800);
                    Tcp.tcpWrite_aes(GET_DB_DOWNLOAD_TIMER,TimerEdit_Database.this);
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
                        //updateProgress(progress+=10);
                        StaticVariables.printLog("DOWNLOAD TIMER COUNT","DWD TIMER : "+counter1);
                    }

                    //update progress and making UI thread sleep for 1 sec
                    //to show final progress to user
                    //updateProgress(100);
                   // SleepUiThread(1000);

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
                           /* runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    room_name_spinner.setSelection(y);
                                }
                            });*/

                        }
                    }


                }else {

                    runOnUiThread(new Runnable() {
                        public void run() {
                       //     updateProgress(100);	//update task progress
                            //making UI thread sleep for 1 sec to show final progress to user
                            SleepUiThread(500);
                            //cancelDialog();		// Dismissing the progress dialog
                       //     DismissDialog();
                        }
                    });

                    if(exceptionOccured){

                        Tcp.tcpConnectionClose();	//closing connection
                        if(!isDialogOpened){
                            serverErrorDialog("Server UnReachable", "Please Check Your WiFi Settings And Check Whether Server is ON");

                        }
                    }

                    resetVariables();//resetting variables


                }
            }
        };
        connectionThread.start();

    }


    void serverErrorDialog(final String titleMsg,final String msg)
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                DismissDialog();
                AlertDialog.Builder dialog=new AlertDialog.Builder(TimerEdit_Database.this, AlertDialog.THEME_HOLO_LIGHT);
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
    void DownloadSuccessDialog_wireless()
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                pd.dismiss();
                refresh_adapter_restart();
            }
        });
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

    //Download error dialog
    void DownloadErrorDialog()
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                DismissDialog();
                pd.dismiss();
                AlertDialog.Builder dialog=new AlertDialog.Builder(TimerEdit_Database.this, AlertDialog.THEME_HOLO_LIGHT);
                dialog.setTitle("Download Failed Showing Details From Offline");
                dialog.setMessage("Possible Reasons :\n 1.Download Timeout Occured. "
                        +"\n 2.Unable to fetch Data from Server.");
                dialog.setIcon(android.R.drawable.ic_dialog_alert);
                dialog.setCancelable(false);


               /* dialog.setPositiveButton("Try Again",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        ConnectAndDownload_timer();
                    }
                });*/

                dialog.setNegativeButton("OK",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                       // ConnectAndDownload_timer();
                    }
                });
            /*    dialog.setNeutralButton("Exit",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        android.os.Process.killProcess(android.os.Process.myPid());//fully exit from app
                    }
                });
*/

                if (!TimerEdit_Database.this.isFinishing()){
                    dialog.show();
                }else{
                    Toast.makeText(TimerEdit_Database.this,"Download Failed Showing Details From Offline" +"\nPossible Reasons :\n 1.Download Timeout Occured. "
                            +"\n 2.Unable to fetch Data from Server.",Toast.LENGTH_LONG).show();
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
                    pdlg=new ProgressDialog(TimerEdit_Database.this, ProgressDialog.THEME_HOLO_LIGHT);
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

                    }else if(Data!=null && Data.startsWith("*") && Data.endsWith("#")&&Data.length()<8){

                        String resp=Data;
                        Thread t = new Thread() {
                            public void run() {
                                String resp=Data;
                                if (resp != null) {
                                    if (resp.equals("*UACK#")) {

                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
//                                        dialog.dismiss();
                                               // popup_show("Timer Updated Successfully");

                                                popupinfo_upd("Timer Updated Successfully");

                                               /* if (timerdialog != null)
                                                    timerdialog.dismiss();*/
                                            }
                                        });

                                        //refresh_adapter();

                               /* ConnectAndDownload_timer();*/

                                        // timr_adap.notifyDataSetChanged();


                                    } else if (resp.equals("*EACK#")) {

                                        popupinfo("Timer Already Exists,Please Add different Timing");

                                    } else if (resp.equals("*UNACK#")) {
                                        StaticVariables.printLog("Timer", "DATA SOCKET :" + Data);
                                        popupinfo_upd("Error Setting Timer");

                                    } else if (resp.equals("*DACK#")) {

                                        popupinfodel("Timer Deleted Successfully");
                                        //ConnectAndDownload_timer();

                                        //  refresh_adapter();

                                    } else if (resp.equals("*DNACK#")) {
                                        popupinfodel("Error Deleting Timer");
                                    }
                                }


                            }
                        };t.start();

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
                case TCP_CONNECTED:
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


    // Error Alert showing that tcp is Off
    private void TCPErrorAlert(final String title,final String msg) {
        runOnUiThread(new Runnable() {
            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run() {

                //dismiss dialog
                DismissDialog();

                AlertDialog.Builder dlg = new AlertDialog.Builder(TimerEdit_Database.this,AlertDialog.THEME_HOLO_LIGHT);
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

    void NoDatabaseErrorDialog()
    {
        runOnUiThread(new Runnable() {

            @TargetApi(Build.VERSION_CODES.HONEYCOMB)
            @Override
            public void run()
            {
                DismissDialog();
                AlertDialog.Builder dialog=new AlertDialog.Builder(TimerEdit_Database.this, AlertDialog.THEME_HOLO_LIGHT);
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

                        Intent it2=new Intent(TimerEdit_Database.this,CombFrag.class);
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


    public void init_database(String rn){
        try{
            TimerDBAdapter.OriginalDataBase=StaticVariabes_div.housename+"_TIMER.db";
            timeradap=new TimerDBAdapter(TimerEdit_Database.this);
            timeradap.open();
            timeradap.getall_timerdata(rn);

            String res="Success";
            if (res.equals("Success")) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        timr_adap=new Timer_Edit_Adap(TimerEdit_Database.this,TimerEdit_Database.this, Http_Connect.Num,
                                Http_Connect.OperatedType, Http_Connect.DeviceName, Http_Connect.DeviceType,
                                Http_Connect.SwitchNumber, Http_Connect.Days, Http_Connect.Date, Http_Connect.Time,
                                Http_Connect.FromTimeRep_date, Http_Connect.ToTimeRep_date,
                                Http_Connect.OnDataRep_date, Http_Connect.OffDataRep_date, Http_Connect.RepeatAlways_rep_date, Http_Connect.ontime_cyclic,
                                Http_Connect.offtime_cyclic, Http_Connect.DeviceNumber, Http_Connect.OnCyclicData, Http_Connect.OffCyclicData, Http_Connect.FromTime_Todisplay, Http_Connect.ToTime_Todisplay
                                , Http_Connect.Data_Todisplay, Http_Connect.Off_Data_Todisplay,Http_Connect.Devicetype_number,Http_Connect.User_nam);
                        simpleListView.setAdapter(timr_adap);

                    }
                });
            }else{

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        simpleListView.setAdapter(null);
                    }
                });
            }

            timeradap.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }


    public String fetch_deviceid(String dev_nm){
        String devid_device="";
        StaticVariabes_div.log("dev_nm"+dev_nm, TAG1);

        swbadap=new SwbAdapter(TimerEdit_Database.this);
        swbadap.open();
        devid_device = swbadap.getdevid_frmdevno(dev_nm);
        swbadap.close();

        StaticVariabes_div.log("devid_device"+devid_device, TAG1);

        mas_adap=new MasterAdapter(TimerEdit_Database.this);
        mas_adap.open();
        if (devid_device.equals("")) {
            StaticVariabes_div.log("devid_device null", TAG1);
            devid_device = mas_adap.getdevid_frmdevno(dev_nm);
        }
        mas_adap.close();

        StaticVariabes_div.log("devid_device"+devid_device, TAG1);

        return devid_device;

    }
    public void Send_Timer_dat_aes(String StrTimer,String starttokn,String endtokn){

        String tosend=null;
        tosend="["+StrTimer+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);
        Tcp.tcpWrite_aes(temp_str);
    }



    private byte[] xorWithKey(byte[] a, byte[] key) {
        byte[] out = new byte[a.length];
        StaticVariabes_div.log(key.length+"key length TimerData",TAG1);
        for (int i = 0; i < a.length; i++) {
            out[i] = (byte) (a[i] ^ key[i%key.length]);
        }
        return out;
    }


    public void popup_show(String msg){

        View crsh_poplay = inflater.inflate(R.layout.poup_show, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(timer_popup_view.getRootView().getContext(), R.style.popup_theme_timer);

        alert.setView(crsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        txt_popup=(TextView) crsh_poplay.findViewById(R.id.txtpopup);
       // cb_ligsen_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_ligsen);


        save = (Button) crsh_poplay.findViewById(R.id.save);
        cancel = (Button) crsh_poplay.findViewById(R.id.cancel);

        txt_popup.setText(msg);

        int width = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.50);
        int height = (int) (timer_popup_view.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dialog1.dismiss();

                if (timerdialog != null)
                    timerdialog.dismiss();

                ConnectAndDownload_timer();
            }
        });




        //dialog.getWindow().setLayout(450, 650);
        // dialog.dismiss();
    }
/*
    @Override
    public void onResume() {
        super.onResume();

        room_name_spinner.setSelection(1);
    }*/
}