package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: MoodSettingEdit.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Activity to show moodlist and edit mood settings .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;

import edisonbro.com.edisonbroautomation.Devices.A_rc;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.databasewired.PredefsettinHelper;
import edisonbro.com.edisonbroautomation.databasewired.PredefsettngAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.HouseConfigurationAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.CustomSpinnerAdapter;
import edisonbro.com.edisonbroautomation.scrollfunction.ShaderSeekArc;


public class MoodSettingEdit extends AppCompatActivity {
    Button b_back,deleteall;
    HouseConfigurationAdapter houseDB = null;
    ArrayList<String> UniqueRoomList = new ArrayList<String>();
    SpinnerAdapter roomNameAdapter;
    Spinner room_name_spinner;
    ListView simpleListView;
    String room_num;

    PredefsettinHelper pre_helper;
    private SQLiteDatabase dataBase;
    public ListView moodsettlist;

    ArrayList<String>  db_num= new ArrayList<String>();
    ArrayList<String>  db_housenum= new ArrayList<String>();
    ArrayList<String>  db_housename= new ArrayList<String>();
    ArrayList<String>  db_roomnum= new ArrayList<String>();
    ArrayList<String>  db_devicename= new ArrayList<String>();
    ArrayList<String>  db_devicenum= new ArrayList<String>();
    ArrayList<String>  db_switchnum= new ArrayList<String>();
    ArrayList<String>  db_status= new ArrayList<String>();
    ArrayList<String>  db_data= new ArrayList<String>();
    ArrayList<String>  db_dev_type= new ArrayList<String>();
    ArrayList<String>  db_type_name= new ArrayList<String>();
    ArrayList<String> db_data_dd= new ArrayList<String>();
    public static final String KEY_ROWID   = "_id";
    public static final String KEY_Hn      = "hn";
    public static final String KEY_Hna     = "hna";
    public static final String KEY_Rn      = "rn";
    public static final String KEY_Rna     = "rna";
    public static final String KEY_stno    = "stno";
    public static final String KEY_stna    = "stna";
    public static final String KEY_Dt      = "dt";
    public static final String KEY_Dna     = "dna";
    public static final String KEY_Dno     = "dno";
    public static final String KEY_wd      = "wd";
    public static final String KEY_DD      = "dd";
    public static final String KEY_ea      = "ea";
    public static final String KEY_eb      = "eb";
    public static final String KEY_ec      = "ec";
    public static final String KEY_ed      = "ed";
    public static final String KEY_ee      = "ee";
    public static final String KEY_ef      = "ef";
    public static final String KEY_eg      = "eg";
    public static final String KEY_eh      = "eh";
    public static final String KEY_ei      = "ei";
    public static final String KEY_ej      = "ej";

    String MoodNumber_Sel,bulbs_to_on="",fan_value,final_bulbs_value;
    AlertDialog dialog1;
    Button s1,s2,s3,s4,s5,s6,s7,s8,fan_dec,fan_inc,save,cancel,btn_moodlist;
    Button btnarr_mood_switchs[];
    ArrayList<Object> bulbon_moodsett;
    Spinner  fan_speed;
    String status_swb,fanspeed;
    String MoodNumber;
    View moodsett_delete_view;
    private static final String TAG1 ="moodsettedit";
    View alertLayout_21,alertLayout_51,alertLayout_20,alertLayout_30,alertLayout_10,alertLayout_80;
    PredefsettngAdapter pre_adap;

    ///////////popup rgb///////////////////////////////////////
    private A_rc colorPicker;
    private Button btn_Red, btn_Green, btn_Blue, btn_Pink, btn_White,
            btn_orange,btn_Flash, btn_Strobe, btn_Fade, btn_Smooth, btn_Speedp,btn_Speedm,btn_on,btn_off;
    private TextView rgbvalue,tv_speedval,tvtest,tv_rgbvalue;
    SeekBar sk;
    String rgbinst,rgbbrightinst,rgb_color_effect,rgbbright,rgb_speed,rgb_speedinst,rgb_onoff_repeat,status_rgb,
            diminst,dimvalue,dmr_onoff_repeat,final_on_off_rgb,initt_rgbvalue,rgbspeedinst,rgb_moodsett_bright,final_on_off_dmr,final_on_off_pir,final_on_off_ligsen,curtinst,curtinst2,curtval,curtval2;
    int ispeed=120,count=0;
    ToggleButton tb_on_off;
    //////////////////pop dmr//////////////////////////
    public ImageView dim_img;
    ShaderSeekArc seekArc;
    int n=0;
    Button btn_low,btn_medium,btn_high,btn_mood1_sett,btn_mood2_sett,btn_mood3_sett,b_home;
    String ondatadmr,offdatadmr,status_dmr;
    public static Integer[] mThumbIds = {

            R.drawable.dim_level0,R.drawable.dim_level1,R.drawable.dim_level2,R.drawable.dim_level3,R.drawable.dim_level4,R.drawable.dim_level5,R.drawable.dim_level6,
            R.drawable.dim_level7, R.drawable.dim_level8, R.drawable.dim_level9,
    };
    String selected_Room,status_pir;


    private Tracker mTracker;
    String name ="Mood Setting";

    Button btn_open_sheer,btn_close_sheer,btn_open_curtain,btn_close_curtain;
    String status_cur;

    public enum Effects_rgb {
        d104, d105, d106, d107,
    }

    public  LayoutInflater layoutInflater;

    String moodsett_houseno_upd,moodsett_roomno_upd,moodsett_housename_upd,moodsett_num_upd,
            moodsett_devicename_upd,moodsett_devicenum_upd,moodsett_status_upd,moodsett_devtyp_upd,moodsett_data_upd,moodsett_typename_upd;
    Context mContext;
    String strtext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_mood_setting_edit);

        layoutInflater=getLayoutInflater();

        mContext=MoodSettingEdit.this;

        b_back = (Button) findViewById(R.id.btnback);
        room_name_spinner = (Spinner) findViewById(R.id.spinner);
        simpleListView = (ListView) findViewById(R.id.simpleListView);
        deleteall=(Button) findViewById(R.id.deleteall);

        b_home= (Button) findViewById(R.id.btnhome);
        b_home.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                Intent intnt=new Intent(MoodSettingEdit.this, Main_Navigation_Activity.class);
                intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intnt);
                finish();
            }
        });

        MoodNumber= StaticVariabes_div.Mood_sel_number;

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getApplication();
        mTracker = application.getDefaultTracker();

        try {
            houseDB = new HouseConfigurationAdapter(this);
            houseDB.open();            //opening house database


            //     pre_adap=new PredefsettngAdapter(this);

            pre_helper=new PredefsettinHelper(this);

            //  pre_adap=new PredefsettngAdapter(this);
            // pre_adap.open();
            dataBase=pre_helper.getWritableDatabase();

        } catch (Exception e) {
            e.printStackTrace();
        }

        fillRoomNameList();

        int y=UniqueRoomList.indexOf(StaticVariabes_div.room_n);

        room_name_spinner.setSelection(y);
        // displayData_mood(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,StaticVariabes_div.Mood_sel_number,StaticVariabes_div.devtyp,StaticVariabes_div.devnumber);

        strtext= StaticVariabes_div.devtyp;

        b_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intnt = new Intent(MoodSettingEdit.this, Main_Navigation_Activity.class);
                intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intnt);
                finish();
            }
        });

        deleteall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(selected_Room.equals("Select Room"))) {
                    layoutInflater = getLayoutInflater();
                    moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
                    MoodNumber=StaticVariabes_div.Mood_sel_number;
                    pre_adap = new PredefsettngAdapter(moodsett_delete_view.getRootView().getContext());
                    pre_adap.open();
                    int delmoodcount= pre_adap.getCount_housenoroomnomoodnum(StaticVariabes_div.housenumber, StaticVariabes_div.mood_roomnum_edit, MoodNumber);
                    pre_adap.close();

                    if(delmoodcount>0) {
                        popupdelete_all(StaticVariabes_div.housenumber, StaticVariabes_div.mood_roomnum_edit);
                    }else{
                        popup("No Mood Setting To Delete");
                    }

                }else{
                    Toast.makeText(MoodSettingEdit.this, "select Room to delete", Toast.LENGTH_SHORT).show();

                }
            }
        });




        room_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                //simpleListView.removeAllViews();
                selected_Room = UniqueRoomList.get(i);


                StaticVariabes_div.log("HttpConnect", "roomno in spinner" + selected_Room);

                if (!(selected_Room.equals("Select Room"))) {
                    int r_n = houseDB.CurrentRoomNumber(selected_Room);

                    room_num = ""+r_n;

                    StaticVariabes_div.mood_roomnum_edit=room_num;
                   /* while  (room_num.length()<2) {
                        room_num = "0" + room_num;
                    }*/

                    StaticVariabes_div.log("HttpConnect", "roomno in spinner" + room_num);

                    //  Toast.makeText(MoodSetting_Edit_Activity.this, "selected_Room" + selected_Room, Toast.LENGTH_SHORT).show();


                    displayData_mood(StaticVariabes_div.housenumber,room_num,StaticVariabes_div.Mood_sel_number,StaticVariabes_div.devtyp,StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);



                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    public void refresh_adapter(){
        StaticVariabes_div.mood_roomnum_edit=room_num;
        displayData_mood(StaticVariabes_div.housenumber,room_num,StaticVariabes_div.Mood_sel_number,StaticVariabes_div.devtyp,StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
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
        room_name_spinner.setSelection(UniqueRoomList.size() - 1);

    }

    private void clear_arraylist() {
        db_num.clear();
        db_housenum.clear();
        db_housename.clear();
        db_roomnum.clear();
        db_devicename.clear();
        db_devicenum.clear();
        db_dev_type.clear();
        db_status.clear();
        db_data.clear();
        db_data_dd.clear();
        //amood_type.clear();
    }

    /**
     * displays data from SQLite
     */
    public void displayData_mood(String hn,String rn,String setno,String dt,String dno,String usrlogd) {
        clear_arraylist();
        //    pre_helper=new PredefsettinHelper(this);

        //  pre_adap=new PredefsettngAdapter(this);
        // pre_adap.open();
        //    dataBase=pre_helper.getWritableDatabase();
        int z=1;
        // pre_adap.open();
        StaticVariabes_div.log("HttpConnect", "displayData_mood" );
        hn="'"+hn+"'";
        setno="'"+setno+"'";
        rn="'"+rn+"'";
        dt="'"+dt+"'";
        dno="'"+dno+"'";
        usrlogd="'"+usrlogd+"'";

        //+"AND "+KEY_Dno +"=" +dno

        /*if(dataBase==null){
            dataBase=pre_helper.getWritableDatabase();
        }*/

        StaticVariabes_div.log("HttpConnect", "hn" +hn +" setno "+setno+" rn"+rn+" dt"+dt+" dno"+dno);
        Cursor mCursor = dataBase.query(true, "PredefDetails_table", new String[] {
                        KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
                        ,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
                        ,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
                        ,KEY_ei,KEY_ej},
                KEY_Rn + "=" +rn+"AND "+KEY_Hn +"="+hn+"AND "+KEY_stno +"=" +setno+"AND "+KEY_ed +"=" +usrlogd, null, null, null, null, null);
        StaticVariabes_div.log("HttpConnect", "displayData_mood cursor"+mCursor.getCount() );
        if(mCursor.getCount() > 0) {
            //fetch each record
            if (mCursor.moveToFirst()) {
                do {

                    //get data from field

                    db_housenum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Hn)));
                    db_housename.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Hna)));
                    db_roomnum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Rn)));

                    db_num.add(""+z);
                    db_devicename.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_ec)));
                    db_devicenum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Dno)));
                    db_dev_type.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_ea)));
                    db_status.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_eb)));
                    db_data.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_wd)));
                    db_type_name.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Dt)));
                    db_data_dd.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_DD)));

                    z++;
                } while (mCursor.moveToNext());
                // dataBase.close();
            }
            //do above till data exhausted
            mCursor.close();
        }
        if (db_num.size() > 0 && db_devicename.size() > 0 && db_devicenum.size() > 0 && db_housenum.size() > 0 && db_data.size() > 0) {

            MoodSetting_Adap disadpt = new MoodSetting_Adap(MoodSettingEdit.this, db_num, db_devicename,db_devicenum, db_dev_type, db_status, db_data,db_housenum,db_housename,db_roomnum,db_type_name,db_data_dd);
          //  disadpt.notifyDataSetChanged();
            simpleListView.setAdapter(disadpt);
        } else {
            // MoodSetting_Adapter disadpt = new MoodSetting_Adapter(MoodSetting_Edit_Activity.this, db_num, db_devicename,db_devicenum, db_dev_type, db_status, db_data,db_housenum,db_housename,db_roomnum);
            simpleListView.setAdapter(null);
            Toast.makeText(MoodSettingEdit.this, "no data", Toast.LENGTH_LONG).show();
        }

        //  pre_adap.close();
        // pre_helper.close();
    }//end displayData



    /**
     * displays data from SQLite
     */
    private void displayData_mood_afterdel(String hn,String rn,String setno,String dt,String dno) {
        clear_arraylist();
        // pre_helper=new PredefsettinHelper(moodsett_delete_view.getRootView().getContext());

        //  pre_adap=new PredefsettngAdapter(this);
        // pre_adap.open();

        PredefsettinHelper pre_helper_local=new PredefsettinHelper(this);
        SQLiteDatabase dataBase_local=pre_helper_local.getWritableDatabase();
        int z=1;
        // pre_adap.open();
        StaticVariabes_div.log("HttpConnect", "displayData_mood" );
        hn="'"+hn+"'";
        setno="'"+setno+"'";
        rn="'"+rn+"'";
        dt="'"+dt+"'";
        dno="'"+dno+"'";

        //+"AND "+KEY_Dno +"=" +dno

        StaticVariabes_div.log("HttpConnect", "hn" +hn +" setno "+setno+" rn"+rn+" dt"+dt+" dno"+dno);
        Cursor mCursor = dataBase_local.query(true, "PredefDetails_table", new String[] {
                        KEY_ROWID,KEY_Hn,KEY_Hna,KEY_Rn,KEY_Rna,KEY_stno,KEY_stna,KEY_Dt
                        ,KEY_Dna,KEY_Dno,KEY_wd,KEY_DD,KEY_ea,KEY_eb,KEY_ec
                        ,KEY_ed,KEY_ee,KEY_ef,KEY_eg,KEY_eh
                        ,KEY_ei,KEY_ej},
                KEY_Rn + "=" +rn+"AND "+KEY_Hn +"="+hn+"AND "+KEY_stno +"=" +setno, null, null, null, null, null);
        StaticVariabes_div.log("HttpConnect", "displayData_mood"+mCursor.getCount() );
        if(mCursor.getCount() > 0) {
            //fetch each record
            if (mCursor.moveToFirst()) {
                do {

                    //get data from field

                    db_housenum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Hn)));
                    db_housename.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Hna)));
                    db_roomnum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Rn)));

                    db_num.add(""+z);
                    db_devicename.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_ec)));
                    db_devicenum.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Dno)));
                    db_dev_type.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_ea)));
                    db_status.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_eb)));
                    db_data.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_wd)));
                    db_type_name.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_Dt)));
                    db_data_dd.add(mCursor.getString(mCursor.getColumnIndex(PredefsettngAdapter.KEY_DD)));

                    z++;
                } while (mCursor.moveToNext());
                dataBase_local.close();
            }
            //do above till data exhausted
            mCursor.close();
        }
        if (db_num.size() > 0 && db_devicename.size() > 0 && db_devicenum.size() > 0 && db_housenum.size() > 0 && db_data.size() > 0) {

            MoodSetting_Adap disadpt = new MoodSetting_Adap(MoodSettingEdit.this, db_num, db_devicename,db_devicenum, db_dev_type, db_status, db_data,db_housenum,db_housename,db_roomnum,db_type_name,db_data_dd);
            simpleListView.setAdapter(disadpt);
        } else {
            // MoodSetting_Adapter disadpt = new MoodSetting_Adapter(MoodSetting_Edit_Activity.this, db_num, db_devicename,db_devicenum, db_dev_type, db_status, db_data,db_housenum,db_housename,db_roomnum);
            simpleListView.setAdapter(null);
            Toast.makeText(MoodSettingEdit.this, "no data", Toast.LENGTH_LONG).show();
        }

        //  pre_adap.close();
        pre_helper_local.close();
    }//end displayData


    public void setbulbtag(Button btset,String val) {


        if (btset.getTag().equals(0)) {
            btset.setBackgroundResource(R.mipmap.bulb02);
            btset.setTag(1);
            bulbon_moodsett.add(val);
        } else {
            btset.setBackgroundResource(R.mipmap.bulb01);
            btset.setTag(0);
            bulbon_moodsett.remove(val);

            StaticVariabes_div.log("swb removed" + val, TAG1);
            StaticVariabes_div.log("swb removed" + String.valueOf(bulbon_moodsett), TAG1);
        }
    }

    public void setbulbtag_sock(Button btset,String val){
        if (btset.getTag().equals(0)) {
            btset.setBackgroundResource(R.mipmap.socket_blue);
            btset.setTag(1);
            bulbon_moodsett.add(val);
        } else {
            btset.setBackgroundResource(R.mipmap.grey_socket);
            btset.setTag(0);
            bulbon_moodsett.remove(val);

            StaticVariabes_div.log("swb removed"+val, TAG1);
            StaticVariabes_div.log("swb removed"+String.valueOf(bulbon_moodsett), TAG1);
        }
    }


    private enum Models {
        S010, S020, S030, S080, S021, S051,S141,S110,S120,S160, DMR1, RGB1, CRS1, CLS1, WPS1, SDG1, CLNR, PSC1,SOSH,SLG1,SWG1
    }

    public void Moodsett_modelselection(String devtyp_upd,String devtynam,String houseno_upd,String roomno_upd,String devicenum_upd ){

        //  pre_adap=new PredefsettngAdapter(this);

        switch (MoodSettingEdit.Models.valueOf(devtyp_upd)) {
            case S010:

                StaticVariabes_div.log("swb 1_0", TAG1);
                Setmood_S010("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;

            case S020:

                StaticVariabes_div.log("swb 2_0", TAG1);
                Setmood_S020("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;
            case S110:

                StaticVariabes_div.log("swb 1_1_0", TAG1);
                Setmood_S110("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;

            case S030:

                StaticVariabes_div.log("swb 3_0", TAG1);
                Setmood_S030("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;
            case S120:

                StaticVariabes_div.log("swb 2_1_0", TAG1);
                Setmood_S120("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;

            case S080:

                StaticVariabes_div.log("swb 8_0", TAG1);
                Setmood_S080("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;
            case S160:

                StaticVariabes_div.log("swb 6_1_0", TAG1);
                Setmood_S160("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;


            case S021:

                StaticVariabes_div.log("swb 2_1", TAG1);
                Setmood_S021("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;


            case S051:

                StaticVariabes_div.log("swb 5_1", TAG1);
                Setmood_S051("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;

            case S141:

                StaticVariabes_div.log("swb 1_4_1", TAG1);
                Setmood_S141("Swb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                break;

            case DMR1:
                SetMood_dmr("Dmr",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                StaticVariabes_div.log("dmr", TAG1);
                break;

            case RGB1:
                SetMood_rgb("Rgb",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);
                StaticVariabes_div.log("rgb ", TAG1);
                break;
            case CLS1:
            case CRS1:

                StaticVariabes_div.log("cur", TAG1);
                SetMood_Cur_sing("Cur",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;
            case CLNR:

                StaticVariabes_div.log("cur", TAG1);
                SetMood_Cur("Cur",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;
            case PSC1:

                StaticVariabes_div.log("psc", TAG1);
                SetMood_Psc_single("Psc",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;
            case SOSH:

                StaticVariabes_div.log("SOSH", TAG1);
                SetMood_Sompy_single("SOSH",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;
            case SWG1:

                StaticVariabes_div.log("SWG1", TAG1);
                SetMood_swing_single("SWG1",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;
            case SLG1:

                StaticVariabes_div.log("SLG1", TAG1);
                SetMood_slide_single("SLG1",houseno_upd,roomno_upd,devtyp_upd,devicenum_upd);

                break;

            case WPS1:
                // SetMood_Pir(moodtyp,moodnum,"Pir");
                StaticVariabes_div.log("pir", TAG1);

                break;


            case SDG1:

                StaticVariabes_div.log("sdg", TAG1);

                break;



        /*case SLT1:

            StaticVariabes_div.log("swb SLT1", TAG1);
            switchSLT1_0();

            break;*/
            default:
                System.out.println("Not matching");

               // refresh_adapter();
        }
    }
    ////////////////////////  S051  /////////////////////////////////
    public void Setmood_S051(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";
        // final LayoutInflater inflater = getLayoutInflater();

       // alertLayout_51 = layoutInflater.inflate(R.layout.popup_moodswitch_51, null);
        alertLayout_51 = layoutInflater.inflate(R.layout.popup_moodsett_swb51, null);
        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_51.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();


        int width = (int) (alertLayout_51.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_51.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

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

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(alertLayout_51.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_mood_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }

        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap=new PredefsettngAdapter(alertLayout_51.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("http"+"houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }


            if ((!(fanvals.equals("0"))|| !(fanvals.equals(""))||!(fanvals==null) )) {
                int fnvalu=Integer.parseInt(fanvals);
                // int position =(fnvalu-1);
                fan_speed.setSelection(fnvalu);
                bulbon_moodsett.add("98");
            }


        }
        pre_adap.close();

        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent i = new Intent(getActivity(), MoodSetting_Edit_Activity.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);*/
            }
        });


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon_moodsett.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon_moodsett.add("98");
                }
                Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_51.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";

                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }

                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool51=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool51){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //  pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted"+StaticVariabes_div.dev_name, TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


    }




    public void Setmood_S141(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";
        // final LayoutInflater inflater = getLayoutInflater();

        // alertLayout_51 = layoutInflater.inflate(R.layout.popup_moodswitch_51, null);
        alertLayout_51 = layoutInflater.inflate(R.layout.popup_moodsett_swb4plus1_1, null);
        final android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_51.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog = alert.create();
        dialog.show();


        int width = (int) (alertLayout_51.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_51.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog.getWindow().setLayout(width, height);
        dialog.show();

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

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(alertLayout_51.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_mood_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            if(h==4){
                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {

                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }

           /* btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);*/
        }

        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap=new PredefsettngAdapter(alertLayout_51.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("http"+"houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;

                        if(position==4){
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }else {
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }
                    }

                }
            }


            if ((!(fanvals.equals("0"))|| !(fanvals.equals(""))||!(fanvals==null) )) {
                int fnvalu=Integer.parseInt(fanvals);
                // int position =(fnvalu-1);
                fan_speed.setSelection(fnvalu);
                bulbon_moodsett.add("98");
            }


        }
        pre_adap.close();

        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              /*  Intent i = new Intent(getActivity(), MoodSetting_Edit_Activity.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);*/
            }
        });


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag_sock(s5,"5");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon_moodsett.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon_moodsett.add("98");
                }
                Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_51.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";

                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }

                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool51=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool51){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //  pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted"+StaticVariabes_div.dev_name, TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }
        });


    }







    public void Setmood_S021(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        //alertLayout_21 = layoutInflater.inflate(R.layout.popup_moodswitch_21, null);
        alertLayout_21 = layoutInflater.inflate(R.layout.popup_moodsett_swb21, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_21.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_21);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog21 = alert.create();
        dialog21.show();


        int width = (int) (alertLayout_21.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_21.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog21.getWindow().setLayout(width, height);
        dialog21.show();

        s1 = (Button) alertLayout_21.findViewById(R.id.s1);
        s2 = (Button) alertLayout_21.findViewById(R.id.s2);

        fan_dec = (Button) alertLayout_21.findViewById(R.id.fan_dec);
        fan_inc = (Button) alertLayout_21.findViewById(R.id.fan_inc);
        fan_speed = (Spinner) alertLayout_21.findViewById(R.id.fan_speed);
        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_21.findViewById(R.id.save);
        cancel = (Button) alertLayout_21.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_21.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(alertLayout_21.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_mood_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }

        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap =new PredefsettngAdapter(alertLayout_21.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }


            if ((!(fanvals.equals("0"))|| !(fanvals.equals(""))||!(fanvals==null) )) {
                int fnvalu=Integer.parseInt(fanvals);
                // int position =(fnvalu-1);
                fan_speed.setSelection(fnvalu);
                bulbon_moodsett.add("98");
            }


        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });


        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon_moodsett.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon_moodsett.add("98");
                }
                Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_21.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                  boolean swb_bool21=  pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool21){
                        popup("Updated Successfully");
                    }
                }
                else{

                    //  pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog21.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog21.dismiss();

            }
        });


    }






    public void Setmood_S110(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        // alertLayout_20 = layoutInflater.inflate(R.layout.popup_moodswitch2, null);
        alertLayout_20 = layoutInflater.inflate(R.layout.popup_moodsett_swb1plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_20.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final  android.support.v7.app.AlertDialog dialog20 = alert.create();
        dialog20.show();


        int width = (int) (alertLayout_20.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_20.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog20.getWindow().setLayout(width, height);
        dialog20.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_20.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){

            if(h==1){
                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else{
                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }
           /* btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);*/
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap=new PredefsettngAdapter(alertLayout_20.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;

                        if(position==1){
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else{
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }
                       /* btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);*/
                    }

                }
            }




        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag_sock(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_20.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool20= pre_adap.updatevalfordevtyp(housenumber,room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool20){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //pre_adap.insert(housenumber, housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog20.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog20.dismiss();

            }
        });


    }





    public void Setmood_S020(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

       // alertLayout_20 = layoutInflater.inflate(R.layout.popup_moodswitch2, null);
        alertLayout_20 = layoutInflater.inflate(R.layout.popup_moodsett_swb2, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_20.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final  android.support.v7.app.AlertDialog dialog20 = alert.create();
        dialog20.show();


        int width = (int) (alertLayout_20.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_20.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog20.getWindow().setLayout(width, height);
        dialog20.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_20.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap=new PredefsettngAdapter(alertLayout_20.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_20.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                   boolean swb_bool20= pre_adap.updatevalfordevtyp(housenumber,room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool20){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //pre_adap.insert(housenumber, housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog20.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog20.dismiss();

            }
        });


    }



    public void Setmood_S120(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        alertLayout_30 = layoutInflater.inflate(R.layout.popup_moodsett_swb2plus1sockt, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_30.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_30);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog30 = alert.create();
        dialog30.show();


        int width = (int) (alertLayout_30.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_30.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog30.getWindow().setLayout(width, height);
        dialog30.show();

        s1 = (Button) alertLayout_30.findViewById(R.id.s1);
        s2 = (Button) alertLayout_30.findViewById(R.id.s2);
        s3 = (Button) alertLayout_30.findViewById(R.id.s3);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_30.findViewById(R.id.save);
        cancel = (Button) alertLayout_30.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_30.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2,s3};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){

            if(h==2){
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
                btnarr_mood_switchs[h].setTag(0);

            }else{
                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }

           /* btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);*/
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(alertLayout_30.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;

                        if(position==2){
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else {
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }
                      /*  btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);*/
                    }

                }
            }




        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag_sock(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_30.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }

                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool20=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool20){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                //Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog30.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog30.dismiss();

            }
        });


    }





    public void Setmood_S030(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        alertLayout_30 = layoutInflater.inflate(R.layout.popup_moodsett_swb3, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_30.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_30);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog30 = alert.create();
        dialog30.show();


        int width = (int) (alertLayout_30.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_30.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog30.getWindow().setLayout(width, height);
        dialog30.show();

        s1 = (Button) alertLayout_30.findViewById(R.id.s1);
        s2 = (Button) alertLayout_30.findViewById(R.id.s2);
        s3 = (Button) alertLayout_30.findViewById(R.id.s3);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_30.findViewById(R.id.save);
        cancel = (Button) alertLayout_30.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_30.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2,s3};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(alertLayout_30.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_30.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }

                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool20=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool20){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                //Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog30.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog30.dismiss();

            }
        });


    }


    public void Setmood_S010(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        alertLayout_10 = layoutInflater.inflate(R.layout.popup_moodsett_swb1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_10.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_10);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog10 = alert.create();
        dialog10.show();


        int width = (int) (alertLayout_10.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_10.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog10.getWindow().setLayout(width, height);
        dialog10.show();

        s1 = (Button) alertLayout_10.findViewById(R.id.s1);

        btn_moodlist= (Button) alertLayout_10.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_10.findViewById(R.id.save);
        cancel = (Button) alertLayout_10.findViewById(R.id.cancel);



        btnarr_mood_switchs=new Button[]{s1};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(alertLayout_10.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;

                } else {
                    Toast.makeText(alertLayout_10.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }

                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }

                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool10=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool10){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //  pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog10.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog10.dismiss();

            }
        });


    }





    public void Setmood_S160(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

        // alertLayout_80 = layoutInflater.inflate(R.layout.popup_moodswitch8, null);
        alertLayout_80 = layoutInflater.inflate(R.layout.popup_moodsett_swb6plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_80.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_80);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog80 = alert.create();
        dialog80.show();


        int width = (int) (alertLayout_80.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_80.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog80.getWindow().setLayout(width, height);
        dialog80.show();

        s1 = (Button) alertLayout_80.findViewById(R.id.s1);
        s2 = (Button) alertLayout_80.findViewById(R.id.s2);
        s3 = (Button) alertLayout_80.findViewById(R.id.s3);
        s4 = (Button) alertLayout_80.findViewById(R.id.s4);
        s5 = (Button) alertLayout_80.findViewById(R.id.s5);
        s6 = (Button) alertLayout_80.findViewById(R.id.s6);
        s7 = (Button) alertLayout_80.findViewById(R.id.s7);


        save = (Button) alertLayout_80.findViewById(R.id.save);
        cancel = (Button) alertLayout_80.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_80.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){

            if(h==6){
                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {

                btnarr_mood_switchs[h].setTag(0);
                btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }
        /*    btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);*/
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(alertLayout_80.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber+"StaticVariabes_div.loggeduser"+StaticVariabes_div.loggeduser, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        if(position==6){
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else {
                            btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_mood_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }

                        /*btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);*/
                    }

                }
            }





        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s6,"6");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag_sock(s7,"7");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;
                } else {
                    Toast.makeText(alertLayout_80.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }

                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool80=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool80){
                        popup("Updated Successfully");
                    }

                }
                else{

                    // pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                //Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog80.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog80.dismiss();

            }
        });


    }








    public void Setmood_S080(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        final_bulbs_value="";
        bulbs_to_on="";

       // alertLayout_80 = layoutInflater.inflate(R.layout.popup_moodswitch8, null);
        alertLayout_80 = layoutInflater.inflate(R.layout.popup_moodsett_swb8, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(alertLayout_80.getRootView().getContext(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_80);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog80 = alert.create();
        dialog80.show();


        int width = (int) (alertLayout_80.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (alertLayout_80.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog80.getWindow().setLayout(width, height);
        dialog80.show();

        s1 = (Button) alertLayout_80.findViewById(R.id.s1);
        s2 = (Button) alertLayout_80.findViewById(R.id.s2);
        s3 = (Button) alertLayout_80.findViewById(R.id.s3);
        s4 = (Button) alertLayout_80.findViewById(R.id.s4);
        s5 = (Button) alertLayout_80.findViewById(R.id.s5);
        s6 = (Button) alertLayout_80.findViewById(R.id.s6);
        s7 = (Button) alertLayout_80.findViewById(R.id.s7);
        s8 = (Button) alertLayout_80.findViewById(R.id.s8);

        save = (Button) alertLayout_80.findViewById(R.id.save);
        cancel = (Button) alertLayout_80.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_80.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btnarr_mood_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7,s8};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_mood_switchs.length;h++){
            btnarr_mood_switchs[h].setTag(0);
            btnarr_mood_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(alertLayout_80.getRootView().getContext());
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber+"StaticVariabes_div.loggeduser"+StaticVariabes_div.loggeduser, TAG1);

        if(cswbprev!=null) {

            String bulbvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String fanvals = cswbprev.getString(cswbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("bulbvals" + bulbvals, TAG1);
            StaticVariabes_div.log("fanvals" + fanvals, TAG1);

            String[] bulbvalssArray = bulbvals.split(";");
            //  String[] fanvaluesArray = fanvals.split(";");

            if (!(bulbvals.equals("0"))) {
                for (int k = 0; k < bulbvalssArray.length; k++) {
                    StaticVariabes_div.log("bulbarray" + bulbvalssArray[k], TAG1);
                    int bulbvalue;
                    if (bulbvals != null) {
                        bulbvalue = Integer.parseInt(bulbvalssArray[k]);
                        int position = (bulbvalue - 1);
                        //tbs[position].setChecked(true);
                        // bulbstatus[position] = true;
                        btnarr_mood_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_mood_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }





        }
        pre_adap.close();
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s6,"6");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s7,"7");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s8,"8");
                Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pre_adap.open();

                if (bulbon_moodsett != null && bulbon_moodsett.size() > 0) {
                    for (Object area : bulbon_moodsett) {

                        if(area.equals("98")){
                            fan_value=fanspeed;
                        }else {
                            // bulbs_to_on = area + ";";
                            bulbs_to_on=bulbs_to_on+area+";";
                        }
                    }

                    final_bulbs_value=bulbs_to_on;
                } else {
                    Toast.makeText(alertLayout_80.getRootView().getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if((final_bulbs_value==null)||(final_bulbs_value.equals(""))){
                    final_bulbs_value="0";
                }

                if((fan_value==null)||(fan_value.equals(""))){
                    fan_value="0";
                }

                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }
                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Swb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Swb no"+devnumber, TAG1);


                if(cswb!=null) {

                    boolean swb_bool80=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Swb", devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(swb_bool80){
                        popup("Updated Successfully");
                    }

                }
                else{

                    // pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,null,status_swb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                //Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog80.dismiss();
                pre_adap.close();

                refresh_adapter();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog80.dismiss();

            }
        });


    }


    public void SetMood_Cur_sing(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {

        View cur_sing_poplay = layoutInflater.inflate(R.layout.popup_moodsett_singlecurt, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(cur_sing_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(cur_sing_poplay);

        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_sing_poplay.findViewById(R.id.save);
        cancel = (Button) cur_sing_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btn_open_curtain = (Button) cur_sing_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_sing_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(cur_sing_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber, room_n, MoodNumber,"Cur",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Cur no"+devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("curt dual"+"curtdata"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.close);
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
                //wddimmr="0";
            }



        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });


        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_curtain.setBackgroundResource(R.drawable.close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                // curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                // if(curtinst2==null)curtinst2="106";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Cur",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Cur no"+devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                   boolean cursing_bool= pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Cur",devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(cursing_bool){
                        popup("Updated Successfully");
                    }
                }
                else{

                    // pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Cur",tyname,devnumber,curtinst2,curtinst,devtyp,status_cur,dev_name,null,null,null,null,null,null,null);
                    //  StaticVariabes_div.log("inserted "+housenumber+","+ housename+","+ room_n+","+ roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });


    }

    public void SetMood_Cur(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {
        // final LayoutInflater inflater = getActivity().getLayoutInflater();

        View cur_nrsh_poplay = layoutInflater.inflate(R.layout.popup_moodsett_nrsh_cur, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(cur_nrsh_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btn_open_sheer = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_sheer);
        btn_close_sheer = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_sheer);

        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (cur_nrsh_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (cur_nrsh_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        MoodNumber =StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(cur_nrsh_poplay.getRootView().getContext());
        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Cur",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Cur no"+devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log(curtdata2+"curt dual"+"curtdata"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.close);
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
                //wddimmr="0";
            }

            if(curtdata2.equals("105")){
                curtval2="105";

                btn_close_sheer.setBackgroundResource(R.drawable.close);
                btn_open_sheer.setBackgroundResource(R.drawable.cur_open_p);
                //wddimmr="1";

            }else{
                curtval2="106";

                btn_open_sheer.setBackgroundResource(R.drawable.open);
                btn_close_sheer.setBackgroundResource(R.drawable.cur_close_p);

            }


        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });



        btn_open_sheer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval2="105";
                btn_open_sheer.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_sheer.setBackgroundResource(R.drawable.close);
            }
        });
        btn_close_sheer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval2="106";
                btn_open_sheer.setBackgroundResource(R.drawable.open);
                btn_close_sheer.setBackgroundResource(R.drawable.cur_close_p);
            }
        });

        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_curtain.setBackgroundResource(R.drawable.close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                if(curtinst2==null)curtinst2="106";





                if(curtinst.equals("102")){
                    status_cur="CLOSE";
                }else{
                    status_cur="OPEN";
                }

                Cursor ccur=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Cur",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Cur no"+devnumber+" final_curdata"+curtinst+" "+curtinst2, TAG1);


                if(ccur!=null) {

                   boolean cur_bool= pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Cur", devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(cur_bool){
                        popup("Updated Successfully");
                    }
                }
                else{

                    //  pre_adap.insert(housenumber, housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Cur",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    //  StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });




    }



    public void SetMood_rgb(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){
        //final LayoutInflater inflater = getActivity().getLayoutInflater();
        View rgb_poplay = layoutInflater.inflate(R.layout.popup_moodsettings_rgb, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(rgb_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);

        alert.setView(rgb_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        btn_Red=(Button) rgb_poplay.findViewById(R.id.btn_red);
        btn_Green=(Button) rgb_poplay.findViewById(R.id.btn_green);
        btn_Blue=(Button) rgb_poplay.findViewById(R.id.btn_blue);
        btn_Pink=(Button) rgb_poplay.findViewById(R.id.btn_purple);
        btn_White=(Button) rgb_poplay.findViewById(R.id.btn_white);
        btn_orange=(Button) rgb_poplay.findViewById(R.id.btn_orange);

        btn_Fade=(Button) rgb_poplay.findViewById(R.id.btn_fade);
        btn_Flash=(Button) rgb_poplay.findViewById(R.id.btn_flash);
        btn_Smooth=(Button) rgb_poplay.findViewById(R.id.btn_smooth);
        btn_Strobe=(Button) rgb_poplay.findViewById(R.id.btn_strobe);
        btn_Speedp=(Button) rgb_poplay.findViewById(R.id.btn_speedup);
        btn_Speedm=(Button) rgb_poplay.findViewById(R.id.btn_speeddown);

        tv_rgbvalue = (TextView) rgb_poplay.findViewById(R.id.textView1);
        colorPicker = (A_rc) rgb_poplay.findViewById(R.id.colorPicker);

        sk = (SeekBar) rgb_poplay.findViewById(R.id.brightseekBar);

        tv_speedval=(TextView)  rgb_poplay.findViewById(R.id.tv_speedvalue);

        save = (Button) rgb_poplay.findViewById(R.id.save);
        cancel = (Button) rgb_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) rgb_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        sk.setMax(10);

        tb_on_off=(ToggleButton) rgb_poplay.findViewById(R.id.tb_rgb_on_off);



        MoodNumber =StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(rgb_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor crgbprev=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Rgb",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Rgb no"+devnumber, TAG1);

        if(crgbprev!=null) {
            String rgbvalsdb=crgbprev.getString(crgbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String rgbcolor=crgbprev.getString(crgbprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            StaticVariabes_div.log("Rgb "+"rgbcolor prev"+rgbcolor, TAG1);

            String[] rgbcolorArray = rgbcolor.split(";");
            String rgbfirstelement=rgbcolorArray[0];

            rgbinst=rgbfirstelement;
            initt_rgbvalue=rgbinst;
            StaticVariabes_div.log("Rgb "+"rgbfirstelement"+rgbfirstelement, TAG1);

            if(rgbvalsdb.equals("1")) {

                rgbbrightinst = rgbcolorArray[1];
                String rgbsecondelement = rgbcolorArray[1];
                String rgbsecondelementarray[] = rgbsecondelement.split(",");
                StaticVariabes_div.log("Rgb " + "rgbsecondelement" + rgbbrightinst, TAG1);


                rgbspeedinst = rgbcolorArray[2];
                String rgbthirdelement = rgbcolorArray[2];
                String rgbthirdelementarray[] = rgbthirdelement.split(",");
                StaticVariabes_div.log("Rgb " + "rgbthirdelement" + rgbspeedinst, TAG1);


                String[] rgbfirstelementArray = rgbfirstelement.split(",");


                if (rgbfirstelementArray[0].equals("0")) {
                    tv_rgbvalue.setText("");
                    tv_rgbvalue.setBackgroundColor(Color.rgb(Integer.parseInt(rgbfirstelementArray[1]), Integer.parseInt(rgbfirstelementArray[2]), Integer.parseInt(rgbfirstelementArray[3])));
                } else if (rgbfirstelementArray[0].equals("104")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                   // tv_rgbvalue.setText("flash");
                    changebutton(false,false,false,true);
                } else if (rgbfirstelementArray[0].equals("105")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                   // tv_rgbvalue.setText("strobe");
                    changebutton(false,false,true,false);
                } else if (rgbfirstelementArray[0].equals("106")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                    //tv_rgbvalue.setText("smooth");
                    changebutton(true,false,false,false);
                } else if (rgbfirstelementArray[0].equals("107")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                   // tv_rgbvalue.setText("fade");
                    changebutton(false,true,false,false);
                }


                ispeed = Integer.parseInt(rgbthirdelementarray[0]);

                if (Integer.parseInt(rgbthirdelementarray[0]) > 120 && (Integer.parseInt(rgbthirdelementarray[0]) < 131)) {

                    tv_speedval.setText(String.valueOf(ispeed - 120));
                }


                rgb_moodsett_bright=rgbsecondelementarray[0];
                sk.setProgress(((Integer.parseInt(rgb_moodsett_bright)) - 130));
            }




            StaticVariabes_div.log("Rgb "+"rgbvalsdb"+rgbvalsdb, TAG1);




            if(rgbvalsdb.equals("1")){
                tb_on_off.setChecked(true);
                final_on_off_rgb="1";

            }else{
                tb_on_off.setChecked(false);
                final_on_off_rgb="0";
            }

        }

        tb_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    final_on_off_rgb="1";
                }else{
                    final_on_off_rgb="0";
                }

            }
        });

        btn_Flash.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                tv_rgbvalue.setText("Flash");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="104,000,000,000,A";
                changebutton(false,false,false,true);

            }
        })	;
        btn_Strobe.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                tv_rgbvalue.setText("Strobe");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="105,000,000,000,A";
                changebutton(false,false,true,false);


            }
        })	;

        btn_Fade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                tv_rgbvalue.setText("Fade");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="107,000,000,000,A";
                changebutton(false,true,false,false);
                //transmit(107);

            }
        })	;

        btn_Smooth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                tv_rgbvalue.setText("Smooth");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                changebutton(true,false,false,false);
                rgb_color_effect="106,000,000,000,A";
                //transmit(106);

            }
        })	;

        btn_Red.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.RED);
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.RED);
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Green.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(0,255, 0));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(0,255, 0));
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Blue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(0, 0, 255));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(0, 0, 255));
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });


        btn_Pink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(255, 0, 128));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });

        btn_White.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.WHITE);
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });

        btn_orange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                colorPicker.setColor(Color.rgb(255, 165, 0));
                int color = colorPicker.getColor();
                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                //transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                //transmit(0,c.red(color),c.green(color),c.blue(color));
                changebutton(false,false,false,false);
            }
        });

        btn_Speedp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if(ispeed<130){
                    ispeed++;
                }
                // transmit(ispeed);
                // transmitdata(ispeed,000,000,000,"A");
                rgb_speed=ispeed+",000,000,000,A";
                tv_speedval.setText(String.valueOf(ispeed-120));

            }
        })	;
        btn_Speedm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if(ispeed>121){
                    ispeed--;
                }
                //transmit(ispeed);
                // transmitdata(ispeed,000,000,000,"A");
                rgb_speed=ispeed+",000,000,000,A";
                tv_speedval.setText(String.valueOf(ispeed-120));

            }
        })	;
        colorPicker.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int color = colorPicker.getColor();

                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                tv_rgbvalue.setText("");
                tv_rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                //  transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
                return false;
            }
        });


        sk.setOnSeekBarChangeListener( new SeekBar.OnSeekBarChangeListener()
        {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {
                count = progress;

            }

            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            public void onStopTrackingTouch(SeekBar seekBar)
            {

                // count=count/10;
                if(count<1)count=1;
                // transmitdata(count+130,000,000,000,"A");
                rgbbright=count+130+",000,000,000,A";
            }
        });



        // dialog1.getWindow().setLayout(400, 450);
        //dialog1.show();;

        int width = (int) (rgb_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (rgb_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.55);
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

                pre_adap.open();
                String final_rgbdata;

                if(rgb_color_effect!=null){
                    if(!(rgb_color_effect.equals(""))||rgb_color_effect!="") {
                        rgbinst = rgb_color_effect;
                    }else{
                        rgbinst="0,255,000,000,A";
                    }
                }
                else{
                    rgbinst="0,255,000,000,A";
                }


                //String Splitdata[]=rgbinst.split(",");
                // rgbinst=transmitdata(Splitdata[0], Splitdata[1], Splitdata[2], Splitdata[3], Splitdata[4]);

                if(rgbbright==null||rgbbright=="")
                    rgbbrightinst="132,000,000,000,A";
                else
                    rgbbrightinst=rgbbright;

                //String Splitdata1[]=rgbbrightinst.split(",");
                // rgbbrightinst=transmitdata(Splitdata1[0], Splitdata1[1], Splitdata1[2], Splitdata1[3], Splitdata1[4]);

                if(rgb_speed==null||rgb_speed=="")
                    rgb_speedinst="121,000,000,000,A";
                else
                    rgb_speedinst=rgb_speed;


                if((final_on_off_rgb==null)||(final_on_off_rgb.equals("")))final_on_off_rgb="0";

                if(final_on_off_rgb.equals("0")){
                    //  final_rgbdata="103,000,000,000,A";
                    final_rgbdata="0";
                }else{
                    final_rgbdata=rgbinst+";"+rgbbrightinst+";"+rgb_speedinst;
                }

                if(final_on_off_rgb.equals("0")){
                    status_rgb="OFF";
                }else{
                    status_rgb="ON";
                }
                Cursor crgb=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Rgb",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Rgb no"+devnumber+" final_rgbdata"+final_rgbdata, TAG1);


                if(crgb!=null) {

                    boolean uprgb=pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Rgb", devnumber, final_rgbdata,final_on_off_rgb,status_rgb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(uprgb){
                        popup("Updated Successfully");
                    }
                }
                else{

                    // pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Rgb",tyname,StaticVariabes_div.devnumber,final_on_off_rgb,final_rgbdata,null,status_rgb,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();

                dialog1.dismiss();



                refresh_adapter();
            }
        });




        //dialog.getWindow().setLayout(450, 650);
        // dialog.dismiss();
    }

    public void changebutton(boolean smooth ,boolean fade,boolean strobe,boolean flash ){
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
    public void SetMood_dmr(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber){

        View dmr_poplay  = layoutInflater.inflate(R.layout.popup_moodsett_dmr, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(dmr_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);
        alert.setView(dmr_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) dmr_poplay.findViewById(R.id.save);
        cancel = (Button) dmr_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) dmr_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        dim_img= (ImageView) dmr_poplay.findViewById(R.id.image);
        // seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);

        btn_low= (Button) dmr_poplay.findViewById(R.id.btn_dimlow);
        btn_medium= (Button) dmr_poplay.findViewById(R.id.btn_dimmedium);
        btn_high= (Button) dmr_poplay.findViewById(R.id.btn_dimhigh);
        dim_img.setImageResource(R.drawable.dim_level0);
        tb_on_off=(ToggleButton) dmr_poplay.findViewById(R.id.tb_dmr_on_off);
        // sk.setProgress(0);


        MoodNumber =StaticVariabes_div.Mood_sel_number;
        pre_adap= new PredefsettngAdapter(dmr_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor cdmrprev=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Dmr",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Dmr no"+devnumber, TAG1);


        if(cdmrprev!=null) {

            String dmrvalsdb=cdmrprev.getString(cdmrprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String dmrdata=cdmrprev.getString(cdmrprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));

            diminst=dmrdata;
            StaticVariabes_div.log("Dimmer"+"dmrdata"+dmrdata, TAG1);


            if(dmrvalsdb.equals("1")){
                tb_on_off.setChecked(true);
                final_on_off_dmr="1";
                seekArc.setProgress(Integer.parseInt(diminst));
                seekstatus(Integer.parseInt(diminst));
            }else{
                tb_on_off.setChecked(false);
                final_on_off_dmr="0";
            }

        }

        pre_adap.close();
        tb_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    final_on_off_dmr="1";
                }else{
                    final_on_off_dmr="0";
                }

            }
        });

        btn_low.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue="002";
                seekArc.setProgress(2);
                seekstatus(2);
            }
        })	;


        btn_medium.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue="040";
                seekArc.setProgress(40);
                seekstatus(40);
            }
        })	;


        btn_high.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                dimvalue="255";
                seekArc.setProgress(100);
                seekstatus(255);
            }
        })	;

        seekArc.setOnSeekArcChangeListener(new ShaderSeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(ShaderSeekArc seekArc, float progress) {

                count=(int)seekArc.getProgress();

            }

            @Override
            public void onStartTrackingTouch(ShaderSeekArc seekArc) {

            }

            @Override
            public void onStopTrackingTouch(ShaderSeekArc seekArc) {
                //if(count<10)count=2;
                //count=dimmrbright(count);
                dimvalue=""+count;
                int cnt=Integer.parseInt(dimvalue);
                seekstatus(cnt);
            }
        });




        int width = (int) (dmr_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.75);
        int height = (int) (dmr_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();
                diminst=dimvalue;

                if(diminst==null)diminst="2";

                if((final_on_off_dmr==null)||(final_on_off_dmr.equals("")))final_on_off_dmr="0";
                //   diminst=transmitdata_dmr(Integer.parseInt(diminst), "B");


                //  ondatadmr=transmitdata_dmr(201,"A");
                if(final_on_off_dmr.equals("0")){
                    status_dmr="OFF";
                }else{
                    status_dmr="ON";
                }

                Cursor cdmr=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Dmr",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Dmr no"+devnumber+" final_dmrdata"+diminst, TAG1);


                if(cdmr!=null) {

                  boolean dmrbool=  pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Dmr", devnumber, diminst,final_on_off_dmr,status_dmr,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                    if(dmrbool){
                        popup("Updated Successfully");
                    }

                }
                else{

                    //  pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Dmr",tyname,devnumber,final_on_off_dmr,diminst,null,status_dmr,dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });


    }

    void seekstatus(int i){
        n = i;
        runOnUiThread(new Runnable() {
            public void run() {
                StaticVariabes_div.log("seekstatus"+n, TAG1);
                if(n==0){
                    dim_img.setImageResource(mThumbIds[0]);
                }else
                if(n>=1&&n<=10){
                    dim_img.setImageResource(mThumbIds[1]);
                }else if(n>10&&n<=20){
                    dim_img.setImageResource(mThumbIds[2]);
                }else if(n>20&&n<=40){
                    dim_img.setImageResource(mThumbIds[3]);
                }else if(n>40&&n<=70){
                    dim_img.setImageResource(mThumbIds[4]);
                }else if(n>70&&n<=100){
                    dim_img.setImageResource(mThumbIds[5]);
                }else if(n>100&&n<=140){
                    dim_img.setImageResource(mThumbIds[6]);
                }else if(n>140&&n<=180){
                    dim_img.setImageResource(mThumbIds[7]);
                }else if(n>180&&n<=220){
                    dim_img.setImageResource(mThumbIds[8]);
                }else if(n>220&&n<=255){
                    dim_img.setImageResource(mThumbIds[9]);
                }

            }
        });

    }

    public void SetMood_Psc_single(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {
        View psc_poplay  = layoutInflater.inflate(R.layout.popup_moodsett_singlepsc, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(psc_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(psc_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) psc_poplay.findViewById(R.id.save);
        cancel = (Button) psc_poplay.findViewById(R.id.cancel);


        btn_open_curtain = (Button) psc_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) psc_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) psc_poplay.findViewById(R.id.mood_list);


        btn_moodlist= (Button) psc_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (psc_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (psc_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        MoodNumber =StaticVariabes_div.Mood_sel_number;
        pre_adap= new PredefsettngAdapter(psc_poplay.getRootView().getContext());
        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"Psc",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Psc no"+StaticVariabes_div.devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("curt dual"+"curtdata"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.close);
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
                //wddimmr="0";
            }



        }

        pre_adap.close();


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_curtain.setBackgroundResource(R.drawable.close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                // curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                // if(curtinst2==null)curtinst2="106";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Psc",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Psc no"+StaticVariabes_div.devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Psc", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }

                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }
//--------------------swing------------



    public void SetMood_swing_single(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {

        View cur_sing_poplay = layoutInflater.inflate(R.layout.popup_moodset_single_swing_gate, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(cur_sing_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(cur_sing_poplay);

        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_sing_poplay.findViewById(R.id.save);
        cancel = (Button) cur_sing_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btn_open_curtain = (Button) cur_sing_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_sing_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(cur_sing_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber, room_n, MoodNumber,"SWG1",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SWG1 no"+devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("swing gate"+"swing gate"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.swing_off_close);
                btn_open_curtain.setBackgroundResource(R.drawable.swing_on_open);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.swing_off_open);
                btn_close_curtain.setBackgroundResource(R.drawable.swing_on_close);
                //wddimmr="0";
            }




        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });


        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.swing_on_open);
                btn_close_curtain.setBackgroundResource(R.drawable.swing_off_close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.swing_off_close);
                btn_close_curtain.setBackgroundResource(R.drawable.swing_on_close);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                // curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                // if(curtinst2==null)curtinst2="106";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"SWG1",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SWG1 no"+devnumber+" SWG1"+curtinst, TAG1);


                if(cpir!=null) {

                    boolean cursing_bool= pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "SWG1",devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(cursing_bool){
                        popup("Updated Successfully");
                    }
                }
                else{

                    // pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Cur",tyname,devnumber,curtinst2,curtinst,devtyp,status_cur,dev_name,null,null,null,null,null,null,null);
                    //  StaticVariabes_div.log("inserted "+housenumber+","+ housename+","+ room_n+","+ roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });


    }




    //--------------slide----------------






    public void SetMood_slide_single(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {

        View cur_sing_poplay = layoutInflater.inflate(R.layout.popup_moodset_single_slide_gate, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(cur_sing_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(cur_sing_poplay);

        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_sing_poplay.findViewById(R.id.save);
        cancel = (Button) cur_sing_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btn_open_curtain = (Button) cur_sing_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_sing_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(cur_sing_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber, room_n, MoodNumber,"SLG1",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SLG1 no"+devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("curt dual"+"curtdata"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.slide_close_off);
                btn_open_curtain.setBackgroundResource(R.drawable.slide_open_on);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.slide_open_off);
                btn_close_curtain.setBackgroundResource(R.drawable.slide_close_on);
                //wddimmr="0";
            }



        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });


        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_curtain.setBackgroundResource(R.drawable.close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                // curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                // if(curtinst2==null)curtinst2="106";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"SLG1",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SLG1 no"+devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                    boolean cursing_bool= pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "SLG1",devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(cursing_bool){
                        popup("Updated Successfully");
                    }
                }
                else{

                    // pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Cur",tyname,devnumber,curtinst2,curtinst,devtyp,status_cur,dev_name,null,null,null,null,null,null,null);
                    //  StaticVariabes_div.log("inserted "+housenumber+","+ housename+","+ room_n+","+ roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });


    }






    //---------------somphy
    public void SetMood_Sompy_single(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {

        View cur_sing_poplay = layoutInflater.inflate(R.layout.popup_moodsett_singlecurt, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(cur_sing_poplay.getRootView().getContext(), R.style.popup_theme_moodsett);
        alert.setView(cur_sing_poplay);

        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_sing_poplay.findViewById(R.id.save);
        cancel = (Button) cur_sing_poplay.findViewById(R.id.cancel);

        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        btn_open_curtain = (Button) cur_sing_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_sing_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_sing_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (cur_sing_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;


        MoodNumber=StaticVariabes_div.Mood_sel_number;
        pre_adap = new PredefsettngAdapter(cur_sing_poplay.getRootView().getContext());
        pre_adap.open();


        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber, room_n, MoodNumber,"SOSH",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SOSH no"+devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("curt dual"+"curtdata"+curtdata, TAG1);
            curtinst2=curtdata2;

            if(curtdata.equals("101")){
                curtval="101";

                btn_close_curtain.setBackgroundResource(R.drawable.close);
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                //wddimmr="1";

            }else{
                curtval="102";

                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
                //wddimmr="0";
            }



        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });


        btn_open_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="101";
                btn_open_curtain.setBackgroundResource(R.drawable.cur_open_p);
                btn_close_curtain.setBackgroundResource(R.drawable.close);
            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.open);
                btn_close_curtain.setBackgroundResource(R.drawable.cur_close_p);
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();

                curtinst=curtval;
                // curtinst2=curtval2;
                if(curtinst==null)curtinst="102";
                // if(curtinst2==null)curtinst2="106";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"SOSH",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"SOSH no"+devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                    boolean cursing_bool= pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "SOSH",devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                    if(cursing_bool){
                        popup("Updated Successfully");
                    }
                }
                else{

                    // pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Cur",tyname,devnumber,curtinst2,curtinst,devtyp,status_cur,dev_name,null,null,null,null,null,null,null);
                    //  StaticVariabes_div.log("inserted "+housenumber+","+ housename+","+ room_n+","+ roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();

                refresh_adapter();
            }
        });


    }

    //---------
    public void SetMood_Pir(final String tyname, final String housenumber, final String room_n, String devty, final String devnumber) {

        View pir_poplay = layoutInflater.inflate(R.layout.popup_moodsett_pir, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(pir_poplay.getRootView().getContext(), R.style.MyDialogTheme1);
        alert.setView(pir_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) pir_poplay.findViewById(R.id.save);
        cancel = (Button) pir_poplay.findViewById(R.id.cancel);
        tb_on_off=(ToggleButton) pir_poplay.findViewById(R.id.tb_pir_on_off);


        btn_moodlist= (Button) pir_poplay.findViewById(R.id.mood_list);
        btn_moodlist.setVisibility(View.INVISIBLE);

        int width = (int) (pir_poplay.getRootView().getContext().getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (pir_poplay.getRootView().getContext().getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(housenumber,room_n, MoodNumber,"Pir",devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Pir no"+devnumber, TAG1);

        if(ccurtprev!=null) {


            String pironoff = ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String lightonoff = ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));



            StaticVariabes_div.log("pirdata" + pironoff, TAG1);

            if (pironoff.equals("909")) {
                final_on_off_pir = "909";

                tb_on_off.setChecked(true);

            } else {
                final_on_off_pir = "910";

                tb_on_off.setChecked(false);


            }
        }

        pre_adap.close();




        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
            }
        });

        tb_on_off.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if(isChecked){
                    final_on_off_pir="909";
                }else{
                    final_on_off_pir="910";
                }

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                pre_adap.open();
                final_on_off_ligsen="0";


                if((final_on_off_pir==null)||(final_on_off_pir.equals("")))final_on_off_pir="910";

                if(final_on_off_pir.equals("910")){
                    status_pir="OFF";
                }else{
                    status_pir="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(housenumber,room_n,MoodNumber,"Pir",devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+housenumber+"roomno"+room_n+"setno"+MoodNumber+"Pir no"+devnumber+" final_pirdata"+final_on_off_pir, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(housenumber, room_n, MoodNumber, "Pir", devnumber, final_on_off_ligsen,final_on_off_pir,status_pir,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    //  pre_adap.insert(housenumber, housename,room_n,roomname,MoodNumber,Moodtype,"Pir",tyname,devnumber,final_on_off_pir,final_on_off_ligsen,null,status_pir,StaticVariabes_div.dev_name,null,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });




    }

    public void popupdelete(final String del_housno,final String del_roomno,final String del_typname,final String del_devno) {
        moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(moodsett_delete_view.getRootView().getContext());
                alertDialogBuilder.setTitle("Delete Mood Setting");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do You Really Want To Delete Mood Setting")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                deletemood_process(del_housno,del_roomno,del_typname,del_devno);
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

    public void deletemood_process(String dele_housno,String dele_roomno,String dele_typnam,String dele_devnum){
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
        pre_adap = new PredefsettngAdapter(moodsett_delete_view.getRootView().getContext());
        pre_adap.open();
        boolean  delmood= pre_adap.deletevalfordevtyp(dele_housno, dele_roomno, MoodNumber,dele_typnam, dele_devnum);
        pre_adap.close();

        if(delmood){
            popup("Deleted Successfully");

            refresh_adapter();

        }else{
            popup("Try Again");

            refresh_adapter();
        }

        //  displayData_mood_afterdel(StaticVariabes_div.housenumber,dele_roomno,StaticVariabes_div.Mood_sel_number,StaticVariabes_div.devtyp,StaticVariabes_div.devnumber);


    }



    public void popupdelete_all(final String del_housno,final String del_roomno) {
        moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
        runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(moodsett_delete_view.getRootView().getContext());
                alertDialogBuilder.setTitle("Delete Mood Setting");
                // set dialog message
                alertDialogBuilder
                        .setMessage("Do You Really Want To Delete Mood Setting")
                        .setCancelable(false)
                        .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                delete_all_mood_process(del_housno,del_roomno);
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

    public void delete_all_mood_process(String dele_housno,String dele_roomno){
        MoodNumber=StaticVariabes_div.Mood_sel_number;
        moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
        pre_adap = new PredefsettngAdapter(moodsett_delete_view.getRootView().getContext());
        pre_adap.open();
        boolean delmood= pre_adap.delete_all_valforroom(dele_housno, dele_roomno, MoodNumber);
        pre_adap.close();
        if(delmood){
            refresh_adapter();
            popup("Deleted Successfully");

        }
    }
    public void popup(String msg){
        moodsett_delete_view = layoutInflater.inflate(R.layout.activity_mood_setting_edit, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(moodsett_delete_view.getRootView().getContext());

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

    public void onResume() {
        super.onResume();
        Log.i(TAG1, "Setting screen name: " + name);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }


    public class MoodSetting_Adap extends BaseAdapter {
        Holder mHolder;
         Context mContext;
     //   static LayoutInflater layoutInflater;

        //list fields to be displayed
        public ArrayList<String> arr_num;
        public ArrayList<String> arr_housenum;
        public ArrayList<String> arr_housename;
        public ArrayList<String> arr_roomnum;
        public ArrayList<String> arr_devicename;
        public ArrayList<String> arr_devicenum;
        public ArrayList<String> arr_devtyp;
        public ArrayList<String> arr_status;
        public ArrayList<String> arr_data;
        public ArrayList<String> arr_typ_name;
        public ArrayList<String> arr_data_dd;
        //private ArrayList<String> mood_type;
        private String one,two,three,four;
        private Object fsfs;
       //  String aanum,aahousenum,aahousename,aaroomnum,aadevicename,aadevicenum,aaswitch1,aastatus,aadata,aamood_type;

       /* String moodsett_houseno_upd,moodsett_roomno_upd,moodsett_housename_upd,moodsett_num_upd,
                moodsett_devicename_upd,moodsett_devicenum_upd,moodsett_status_upd,moodsett_devtyp_upd,moodsett_data_upd,moodsett_typename_upd;
*/

      /*  static String device_name;

        private enum Effects_rgb {
            d104, d105, d106, d107,
        }*/
        public MoodSetting_Adap(Context c, ArrayList<String> num,  ArrayList<String> dev_name, ArrayList<String> devicenum, ArrayList<String> devtyp,  ArrayList<String> status, ArrayList<String> data,ArrayList<String> ahousenum, ArrayList<String> ahousename,
                                   ArrayList<String> aroomnum , ArrayList<String> typ_name,ArrayList<String> data_dd) {
            this.mContext = c;
            //transfer content from database to temporary memory

            this.arr_housenum = ahousenum;
            this.arr_housename = ahousename;
            this.arr_roomnum = aroomnum;

            this.arr_num = num;
            this.arr_devicename = dev_name;
            this.arr_devicenum = devicenum;
            this.arr_status = status;
            this.arr_devtyp = devtyp;
            this.arr_data = data;
            this.arr_typ_name=typ_name;
            this.arr_data_dd=data_dd;

           // LayoutInflater  layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }



        public int getCount() {
            // TODO Auto-generated method stub
            return arr_num.size();
        }

        public Object getItem(int position) {
            // TODO Auto-generated method stub
            return null;
        }

        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return 0;
        }

        @Override
        public int getViewTypeCount() {

            return getCount();
        }

        @Override
        public int getItemViewType(int position) {

            return position;
        }

        public View getView(final int pos, View child, ViewGroup parent) {

            final View rowView;


            mHolder = new MoodSetting_Adap.Holder();
            LayoutInflater  layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = layoutInflater.inflate(R.layout.mood_list_layout, null);
            mHolder.num = (TextView) rowView.findViewById(R.id.num);
            mHolder.devicename = (TextView) rowView.findViewById(R.id.devicename);
            mHolder.devno = (TextView) rowView.findViewById(R.id.tv_devno);
            mHolder.devtype = (TextView) rowView.findViewById(R.id.tv_detyp);
            mHolder.status = (TextView) rowView.findViewById(R.id.tv_status);
            mHolder.data = (TextView) rowView.findViewById(R.id.tv_data);
            mHolder.edit = (ImageButton) rowView.findViewById(R.id.edit);
            mHolder.delete = (ImageButton) rowView.findViewById(R.id.delete);
            rowView.setTag(mHolder);



            //transfer to TextView in screen
            mHolder.num.setText(arr_num.get(pos));
            mHolder.devicename.setText(arr_devicename.get(pos));
            mHolder.devno.setText(arr_devicenum.get(pos));
            mHolder.devtype.setText(arr_devtyp.get(pos));
            mHolder.status.setText(arr_status.get(pos));
            mHolder.data.setText(arr_data.get(pos));

            StaticVariabes_div.log(arr_data_dd.get(pos)+"datatodisplay" + arr_data.get(pos), "data b");
            StaticVariabes_div.log(arr_devtyp.get(pos)+"datatodisplay" + arr_devicename.get(pos), "data b");

            if(arr_devtyp.get(pos).equals("RGB1")) {

                if( arr_data.get(pos).equals("1")) {
                    String c = arr_data_dd.get(pos);
                    StaticVariabes_div.log("datatodisplay" + c, "data rgb");

                    String rgb_data[] = c.split(";");
                    String rgb_data_val[] = rgb_data[0].split(",");
                    String rgb_prev_color_efft = rgb_data_val[0];

                    if (rgb_prev_color_efft.equals("112")||rgb_prev_color_efft.equals("0")) {

                        String rgb_prev_color_r = rgb_data_val[1];
                        String rgb_prev_color_g = rgb_data_val[2];
                        String rgb_prev_color_b = rgb_data_val[3];

                        // rgb_color_effect = "0," + rgb_prev_color_r + "," + rgb_prev_color_g + "," + rgb_prev_color_b + ",B";

                        mHolder.data.setText("");
                        mHolder.data.setBackgroundColor(Color.rgb(Integer.parseInt(rgb_prev_color_r), Integer.parseInt(rgb_prev_color_g), Integer.parseInt(rgb_prev_color_b)));

                    } else {

                        mHolder.data.setBackgroundColor(Color.WHITE);
                        //rgb_color_effect = rgb_prev_color_efft + ",0,0,0,A";

                        switch (MoodSettingEdit.Effects_rgb.valueOf("d" + rgb_prev_color_efft)) {
                            case d104:
                                mHolder.data.setText("Flash");
                                break;
                            case d105:
                                mHolder.data.setText("Strobe");
                                break;
                            case d106:
                                mHolder.data.setText("Smooth");
                                break;
                            case d107:
                                mHolder.data.setText("Fade");
                                break;

                            default:
                                mHolder.data.setText("");
                                break;
                        }


                    }
                }else{
                    mHolder.data.setText("OFF");
                }


            } else
            if(arr_devtyp.get(pos).equals("DMR1")) {


                if( arr_data.get(pos).equals("1")) {

                    String c = arr_data_dd.get(pos);
                    StaticVariabes_div.log("datatodisplay" + c, "data dmr");
                    mHolder.data.setText(c);
                }else{

                    mHolder.data.setText("OFF");

                }



            }else if(arr_devtyp.get(pos).equals("CRS1")||arr_devtyp.get(pos).equals("CLS1")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data cur");
                if(c.equals("101")) {
                    mHolder.data.setText("open");
                }else{
                    mHolder.data.setText("close");
                }

            }else if(arr_devtyp.get(pos).equals("CLNR")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data cur");
                if(c.equals("101")&&cn.equals("105")) {

                    mHolder.data.setText("cr-open/sh-open");
                }else if(c.equals("101")&&cn.equals("106")){
                    mHolder.data.setText("cr-open/sh-close");
                } else if(c.equals("102")&&cn.equals("105")) {

                    mHolder.data.setText("cr-close/sh-open");
                }else if(c.equals("102")&&cn.equals("106")){
                    mHolder.data.setText("cr-close/sh-close");
                }

            }else if(arr_devtyp.get(pos).equals("S051")||arr_devtyp.get(pos).equals("S021")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                if(c.equals("0")) {
                    mHolder.data.setText(cn);
                }else{
                    mHolder.data.setText(cn+" F-"+c);
                }
                StaticVariabes_div.log(cn+" datatodisplay" + c, "data swb");

            }else if(arr_devtyp.get(pos).equals("PSC1")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data psc");
                if(c.equals("101")) {
                    mHolder.data.setText("open");
                }else{
                    mHolder.data.setText("close");
                }

            }else if(arr_devtyp.get(pos).equals("SOSH")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data psc");
                if(c.equals("101")) {
                    mHolder.data.setText("open");
                }else{
                    mHolder.data.setText("close");
                }

            }else if(arr_devtyp.get(pos).equals("SWG1")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data psc");
                if(c.equals("101")) {
                    mHolder.data.setText("open");
                }else{
                    mHolder.data.setText("close");
                }

            }
            else if(arr_devtyp.get(pos).equals("SLG1")) {

                String c = arr_data_dd.get(pos);
                String cn = arr_data.get(pos);
                StaticVariabes_div.log("datatodisplay" + c, "data psc");
                if(c.equals("101")) {
                    mHolder.data.setText("open");
                }else{
                    mHolder.data.setText("close");
                }

            }


      /*  mHolder.delete.setTag(pos);
        mHolder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Integer index = (Integer) view.getTag();
                aanum = anum.get(index.intValue());
                aahousenum = ahousenum.get(index.intValue());
                aahousename = ahousename.get(index.intValue());
                aaroomnum = aroomnum.get(index.intValue());

                aadevicename = adevicename.get(index.intValue());
                aaswitch1 = aswitch1.get(index.intValue());
                aastatus = astatus.get(index.intValue());
                aadata = adata.get(index.intValue());
                DisplayList r=new DisplayList();
                DatabaseHelper db = new DatabaseHelper(view.getContext());
                db.getWritableDatabase();
                db.delete(aanum);
            }
        });
        pre_adap.deletevalfordevtyp(houseno, roomno, setno,"Pir", devn);
*/




            mHolder.delete.setTag(pos);
            mHolder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = (Integer) view.getTag();
                    moodsett_houseno_upd = arr_housenum.get(index.intValue());
                    moodsett_housename_upd = arr_housename.get(index.intValue());
                    moodsett_roomno_upd = arr_roomnum.get(index.intValue());

                    moodsett_devicename_upd = arr_devicename.get(index.intValue());
                    moodsett_devicenum_upd = arr_devicenum.get(index.intValue());
                    moodsett_devtyp_upd = arr_devtyp.get(index.intValue());
                    moodsett_status_upd = arr_status.get(index.intValue());
                    moodsett_data_upd = arr_data.get(index.intValue());
                    moodsett_typename_upd=arr_typ_name.get(index.intValue());



                   popupdelete(moodsett_houseno_upd,moodsett_roomno_upd,moodsett_typename_upd,moodsett_devicenum_upd);


                 /*   arr_num.remove(pos);
                    arr_devicename.remove(pos);
                    arr_devicenum.remove(pos);
                    arr_devtyp.remove(pos);
                    arr_status.remove(pos);
                    arr_data.remove(pos);
                    notifyDataSetChanged();*/
//                mood_sett_actvty.displayData_mood(moodsett_houseno_upd,moodsett_roomno_upd,StaticVariabes_div.Mood_sel_number,moodsett_typename_upd,moodsett_devicenum_upd);


                }
            });
            mHolder.edit.setTag(pos);
            mHolder.edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Integer index = (Integer) view.getTag();
                    moodsett_houseno_upd = arr_housenum.get(index.intValue());
                    moodsett_housename_upd = arr_housename.get(index.intValue());
                    moodsett_roomno_upd = arr_roomnum.get(index.intValue());

                    moodsett_devicename_upd = arr_devicename.get(index.intValue());
                    moodsett_devicenum_upd = arr_devicenum.get(index.intValue());
                    moodsett_devtyp_upd = arr_devtyp.get(index.intValue());
                    moodsett_status_upd = arr_status.get(index.intValue());
                    moodsett_data_upd = arr_data.get(index.intValue());
                    moodsett_typename_upd=arr_typ_name.get(index.intValue());

                    Moodsett_modelselection(moodsett_devtyp_upd,moodsett_typename_upd,moodsett_houseno_upd,moodsett_roomno_upd,moodsett_devicenum_upd);

                   // refresh_adapter();

                }
            });


            return rowView;
        }




        public class Holder {
            TextView num;
            TextView devicename;
            TextView switch1;
            public TextView status,devno,devtype;
            public TextView data;
            public ImageButton edit;
            public ImageButton delete;
        }
    }

    public void onBackPressed() {
        Intent intnt = new Intent(MoodSettingEdit.this, Main_Navigation_Activity.class);
        intnt.setFlags(intnt.FLAG_ACTIVITY_NEW_TASK | intnt.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intnt);
        finish();
    }

}
