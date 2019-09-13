package edisonbro.com.edisonbroautomation.Devices;


/**
 *  FILENAME: SwitchBoard_All_Models_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Switchboard Fragment displaying switchboard based on model selected .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *  functions:
 *  ButtonOut : To transmit switch data through tcp.
 *  Track_button_event : To track event on button click.
 *  Changeiconbulb :  popup to change bulb icon.
 *  switch1_0 : To initialize one switch model.
 *  switch2_0 : To initialize two switch model.
 *  switch3_0 : To initialize three switch model.
 *  switch8_0 : To initialize eight switch model.
 *  switch2_1 : To initialize two switch and fan  model.
 *  switch5_1 : To initialize two switch and fan  model.
 *  fetchdata : To fetch all data of switchboard selected from database.
 *  initBtn  : To initialize all arrays at start.
 *  listener :To initialize click event for switches
 *  onstart2 :To set icons based on values from database.
 *  allonoffinit :To initialize click event for allon/alloff.
 *  receiveddata : To receive data from tcp.
 *  Datain : To process the data received from tcp.
 *  switchStatusUi : To update response of switchs in ui.
 *  updateFanState: To update response of fan in ui.
 *  popup : popup to display info.
 */
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
//import android.support.v4.app.Fragment;
import android.app.Fragment;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.sql.Struct;
import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.CombFrag;
import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Edisonbro_AnalyticsApplication;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp_dwn_config;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;
import edisonbro.com.edisonbroautomation.usersettings.UserSettingActivity;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SwitchBoard_All_Models_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SwitchBoard_All_Models_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwitchBoard_All_Models_Frag extends Fragment implements TcpTransfer,View.OnClickListener,View.OnLongClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String TAG1="SwitchBoard - ";

    //*****************************************************************//
    String icon_txt[]; Integer micons_img[];
    public static Integer[] icons_off_types = {
            R.drawable.icn_bulb_off, R.drawable.icn_tv_off,R.drawable.icn_name_board_off,
            R.drawable.icn_rgb_off,R.drawable.icn_curt_off, R.drawable.icn_dim_off,
            R.drawable.icn_door_lck_off,
            R.drawable.icn_bed_lamp_off,R.drawable.icn_aqu_off, R.drawable.icn_speaker_off,
            R.drawable.icn_tubelight_off,R.drawable.icn_cfl_off, R.drawable.icn_socket_off,R.drawable.icn_exhaust_fan_off,
            R.drawable.icn_ebulb_off,R.mipmap.sockt_off
    };
    public static Integer[] icon_on_types = {
            R.drawable.icn_bulb_on, R.drawable.icn_tv_on,R.drawable.icn_name_board_on,
            R.drawable.icn_rgb_on,R.drawable.icn_curt_on, R.drawable.icn_dim_on,
            R.drawable.icn_door_lck_on,
            R.drawable.icn_bed_lamp_on,R.drawable.icn_aqu_on, R.drawable.icn_speaker_on,
            R.drawable.icn_tubelight_on,R.drawable.icn_cfl_on, R.drawable.icn_socket_on, R.drawable.icn_exhaust_fan_on,
            R.drawable.icn_ebulb_on,R.mipmap.sockt_on
    };

    public String[] icon_names_text = {
            "bulb","tv","disp",
            "rgb","curtain", "dimmr","door",
            "bedlamp","aqua",
            "speaker", "tubelight","cfl","socket","exhaustfan","ebulb"
    };

    public String[] icon_names_text_hvac = {

           "bulb" , "ac", "geyser",
    };
    public static Integer[] icons_on_types_hvac = {
            R.drawable.high_volt_bulb01, R.drawable.icn_ac_on2, R.drawable.icn_geyser_on,

    };

    public static Integer[] icons_off_types_hvac = {
           R.drawable.high_volt_bulb, R.drawable.icn_ac_off2, R.drawable.icn_geyser_off,

    };
    Button btnarray_changeicon[];
    //*****************************************************************//
    volatile boolean fBool;
    String strtext;
    String strtemp="test";
    boolean powerstatus=false;
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01",devtypesett,model,Roomname;
    //********************************************************************************
    private static final int READ_BYTE = 1, READ_LINE = 2
    , ServStatus = 3, signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7,  UPDATE=8;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    private SwbAdapter Swb_Adapter;
    LocalDatabaseHelper db=null;
    CombFrag combf;
    private Tracker mTracker;
    String name ="Smart Switch";
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************

    TextView tvtest;
    View view;
    AlertDialog dialog;
    ImageView img_fan;
    Button  btn_fan ;

    //*******************************************************************************

    static Button b_bulb1, b_bulb2, b_bulb3, b_bulb4, b_bulb5, b_bulb6, b_bulb7,
            b_bulb8, b_bulb9, b_bulb10, b_bulb11, b_bulb12, b_fan, b_allon,
            b_alloff, b_leddis, b_incr, b_dcr, b_back, b_leddis2, b_incr2,
            b_dcr2,b_home;
    static LinearLayout lay_fan;
    /*  static Button btnei, btnconstatus, btsig, btremote, btnwtype;
      static Button btnnextright, btnprevleft;
      static TextView tvroomname,tvmodel, tv_bulb1, tv_bulb2, tv_bulb3, tv_bulb4, tv_bulb5,
              tv_bulb6, tv_bulb7, tv_bulb8, tv_bulb9, tv_bulb10, tv_bulb11,tv_bulb12,tv_allon,tv_alloff;*/
    //***************************************************************************************************
    volatile boolean b1state, b2state, b3state, b4state, b5state, b6state, b7state,
            b8state, b9state, b10state, b11state, b12state, b13state, b14state, b15state, b16state, b17state, b18state, b19state,
            b20state, b21state, b22state, b23state, b24state, b25state, b26state, b27state, b28state, b29state, b30state, b31state,
            b32state, f1state, f2state, f3state, f4state;
    volatile int f1speed, f2speed, f3speed, f4speed;
    String b1icon, b2icon, b3icon, b4icon, b5icon, b6icon, b7icon, b8icon,
            bulb1name, bulb2name, bulb3name, bulb4name, bulb5name, bulb6name,
            bulb7name, bulb8name, bulb9name, bulb10name, bulb11name, bulb12name;
    static String b9icon, b10icon, b11icon, b12icon;
    String bulbicontypearr1_4[] = { b1icon, b2icon, b3icon, b4icon };
    String bulbicontypearr5_8[] = { b5icon, b6icon, b7icon, b8icon };
    String bulbicontypearr9_12[] = {b9icon, b10icon, b11icon, b12icon };
    static String deviceType = null;
    Button bulbiconarr1_4[],bulbiconarr5_8[],bulbiconarr9_12[];
    String bulbnamestrarr[];
    boolean bulbstate1_4[] = { false, false, false, false };
    boolean bulbstate5_8[] = { false, false, false, false };
    boolean bulbstate9_12[] = { false, false, false, false };

    volatile boolean fn_Status[] = {f1state, f2state, f3state, f4state};

    volatile int fn_Speed[] = {f1speed, f2speed, f3speed, f4speed};

    boolean bstateui1_4[] = { b1state, b2state, b3state, b4state };
    boolean bstateui5_8[] = { b5state, b6state, b7state, b8state};
    boolean bstateui9_12[] = { b9state, b10state, b11state,b12state };

    boolean bulbstateui1_4[] = { false, false, false, false };
    boolean bulbstateui5_8[] = { false, false, false, false };
    boolean bulbstateui9_12[] = { false, false, false, false };

    static Button btnarr[],bulblistenerbtnarr[];
    static boolean bulbarr[];


    public static Integer[] fState = { R.mipmap.fanspeed_0, R.mipmap.fanspeed_1,
            R.mipmap.fanspeed_2, R.mipmap.fanspeed_3, R.mipmap.fanspeed_4,
            R.mipmap.fanspeed_5 };

    //**********************************************************************************************************



    public SwitchBoard_All_Models_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SwitchBoard_All_Models_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static SwitchBoard_All_Models_Frag newInstance(String param1, String param2) {
        SwitchBoard_All_Models_Frag fragment = new SwitchBoard_All_Models_Frag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

  /*  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_switch_board_all_models, container, false);


    }*/



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        strtext= StaticVariabes_div.devtyp;

        if(strtext==null){
            strtext=strtemp;
        }

        StaticVariabes_div.log("strtext "+strtext, TAG1);
try {

    switch (Models.valueOf(strtext)) {
        case SLT1:
        case S010:
            view = inflater.inflate(R.layout.fragment_switch_board_1, container, false);
            StaticVariabes_div.log("swb 1_0", TAG1);
            switch1_0();
            break;

        case S020:
            view = inflater.inflate(R.layout.fragment_switch_board_2, container, false);
            StaticVariabes_div.log("swb 2_0", TAG1);
            switch2_0();
            break;

        case S110:
            view = inflater.inflate(R.layout.fragment_switch_board_2, container, false);
            StaticVariabes_div.log("swb 1_1s", TAG1);
            switch1_1s();
            break;
        case S030:
            view = inflater.inflate(R.layout.fragment_switch_board_3, container, false);
            StaticVariabes_div.log("swb 3_0", TAG1);
            switch3_0();
            break;
        case S120:
            view = inflater.inflate(R.layout.fragment_switch_board_2_plus1sockt, container, false);
            StaticVariabes_div.log("swb 2_1s", TAG1);
            switch2_1s();
            break;

        case S060:

            StaticVariabes_div.log("swb 6_0", TAG1);
            //switch6_0();
            break;

        case S080:
            view = inflater.inflate(R.layout.fragment_switch_board_8, container, false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            StaticVariabes_div.log("swb 8_0", TAG1);
            switch8_0();
            break;

        case S160:
            view = inflater.inflate(R.layout.fragment_switch_board_6plus1, container, false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            StaticVariabes_div.log("swb 6_1s", TAG1);
            switch6_1s();
            break;
       /* case S120:
            view = inflater.inflate(R.layout.fragment_switch_board_12, container, false);
            StaticVariabes_div.log("swb 12_0", TAG1);
            switch12_0();
            break;*/

        case S021:
            view = inflater.inflate(R.layout.fragment_switch_board_2plus1, container, false);
            StaticVariabes_div.log("swb 2_1", TAG1);
            switch2_1();
            break;

        case S031:

            StaticVariabes_div.log("swb 3_1", TAG1);
            // switch3_1();
            break;
        case WPS1:

            StaticVariabes_div.log("swb wps", TAG1);
            // switch3_1();
            break;
        case test:

            StaticVariabes_div.log("swb test", TAG1);
            // switch3_1();
            break;

        case S051:
            //view = inflater.inflate(R.layout.fragment_swb_frag5n1_model, container, false);
            view = inflater.inflate(R.layout.fragment_switch_board_5plus1, container, false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            StaticVariabes_div.log("swb 5_1", TAG1);
            switch5_1();
            break;
        case S141:
            //view = inflater.inflate(R.layout.fragment_swb_frag5n1_model, container, false);
            view = inflater.inflate(R.layout.fragment_switch_board_4plus1_1, container, false);
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            StaticVariabes_div.log("swb 4_1_1", TAG1);
            switch4_1_1();
            break;


        default:
            view = inflater.inflate(R.layout.fragment_switch_board_8, container, false);
            System.out.println("Not matching");
    }

}catch (Exception ee){

       Toast.makeText(getActivity().getApplicationContext(),"msg"+strtext+" num"+ee.toString(),Toast.LENGTH_SHORT).show();

}
     //   Toast.makeText(getActivity().getApplicationContext(),"msg"+strtext+" num"+StaticVariabes_div.devnumber,Toast.LENGTH_SHORT).show();


        devno=StaticVariabes_div.devnumber;
        while (devno.length() < 4)devno = "0" + devno;

        deviceType=StaticVariabes_div.devtyp;

        StaticVariabes_div.loaded_lay_Multiple=false;

        combf = ((CombFrag) this.getParentFragment());
        combf.feature_sett_enab_disable(true);

        Tcp_con mTcp = new Tcp_con(this);


        if(Tcp_con.isClientStarted){

            //  receiveddata(NetwrkType,StaticStatus.Network_Type,null);
           //  receiveddata(ServStatus,StaticStatus.Server_status,null);

        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(),StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        mn_nav_obj=(Main_Navigation_Activity) getActivity();

        //tvtest = (TextView) view.findViewById(R.id.tvtest);
            try {
                tvtest = (TextView) view.findViewById(R.id.tvtest);
            } catch (Exception e) {
                Toast.makeText(getActivity().getApplicationContext(),"msg"+strtext+" tv"+e.toString(),Toast.LENGTH_SHORT).show();

            }
        if(tvtest!=null) {
            if (StaticVariabes_div.dev_name != null)
                tvtest.setText(StaticVariabes_div.dev_name);
        }
        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        return view;

    }

    ////////////////////initializing listeners////////////////////////////////////////////////////////////////////
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnbulb1:
                if(b_bulb1!=null) {
                    ButtonOut("101");
                    Track_bulb_event("Switch Operation","Bulb1 Operation","Bulb1 Shortclick");
                   // Toast.makeText(getActivity().getApplicationContext(),"bulb1"+StaticVariabes_div.devtyp+" Frag devno"+StaticVariabes_div.devnumber,Toast.LENGTH_SHORT).show();

                }
                break;
            case R.id.btnbulb2:
                if(b_bulb2!=null) {
                    ButtonOut("102");
                    Track_bulb_event("Switch Operation","Bulb2 Operation","Bulb2 Shortclick");
                }
                break;
            case R.id.btnbulb3:
                if(b_bulb3!=null) {
                    ButtonOut("103");
                    Track_bulb_event("Switch Operation","Bulb3 Operation","Bulb3 Shortclick");
                }
                break;
            case R.id.btnbulb4:
                if(b_bulb4!=null) {
                    ButtonOut("104");
                    Track_bulb_event("Switch Operation","Bulb4 Operation","Bulb4 Shortclick");
                }
                break;
            case R.id.btnbulb5:
                if(b_bulb5!=null) {
                    ButtonOut("105");
                    Track_bulb_event("Switch Operation","Bulb5 Operation","Bulb5 Shortclick");
                }
                break;
            case R.id.btnbulb6:
                if(b_bulb6!=null) {
                    ButtonOut("106");
                    Track_bulb_event("Switch Operation","Bulb6 Operation","Bulb6 Shortclick");
                }
                break;
            case R.id.btnbulb7:
                if(b_bulb7!=null) {
                    ButtonOut("107");
                    Track_bulb_event("Switch Operation","Bulb7 Operation","Bulb7 Shortclick");
                }
                break;
            case R.id.btnbulb8:
                if(b_bulb8!=null) {
                    ButtonOut("108");
                   Track_bulb_event("Switch Operation","Bulb8 Operation","Bulb8 Shortclick");
                }
                break;
            case R.id.btnbulb9:
                if(b_bulb9!=null)
                    ButtonOut("109");
                break;
            case R.id.btnbulb10:
                if(b_bulb10!=null)
                    ButtonOut("110");
                break;
            case R.id.btnbulb11:
                if(b_bulb11!=null)
                    ButtonOut("111");
                break;
            case R.id.btnbulb12:
                if(b_bulb12!=null)
                    ButtonOut("112");
                break;
            case R.id.btnallon:
                if(b_allon!=null) {
                    ButtonOut("901");
                    Track_bulb_event("Switch Operation","AllOn Operation","AllOn Shortclick");
                }
                break;
            case R.id.btnalloff:
                if(b_alloff!=null) {
                    ButtonOut("902");
                    Track_bulb_event("Switch Operation","AllOff Operation","AllOff Shortclick");
                }
                break;
            case R.id.img_leddisp1:
                if(img_fan!=null) {
                    ButtonOut("700");
                }
                break;
            case R.id.btnfan:
                if(img_fan!=null) {
                    ButtonOut("700");
                    Track_bulb_event("Fan Operation","Fan Operation","Fan Shortclick");
                }
                break;
            case R.id.btnfanup1:
                if(b_incr!=null) {
                    ButtonOut("720");
                    Track_bulb_event("Fan Operation","Speed Up Operation","Fan Speedup Shortclick");
                }
                break;

            case R.id.btnfandwn1:
                if(b_dcr!=null) {
                    ButtonOut("721");
                    Track_bulb_event("Fan Operation","Speed Down Operation","Fan Speeddown Shortclick");
                }
                break;

        }
    }


    public void Track_bulb_event(String catagoryname,String actionname,String labelname){
        Tracker t = ((Edisonbro_AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(catagoryname)
                .setAction(actionname)
                .setLabel(labelname)
                .build());
    }
    ///////////////////////////////////////////////////////////////////////////////////////
    public boolean onLongClick(View v) {
        switch (v.getId()) {
            case R.id.btnbulb1:
                if(b_bulb1!=null) {
                    Changeiconbulb("bulb1", 0);
                    //ButtonOut("920");
                    Track_bulb_event("Switch Operation","Bulb1 Changeicon","Bulb1 Longclick");
                }
                break;


            case R.id.btnbulb2:
                if(b_bulb2!=null) {
                    Changeiconbulb("bulb2", 1);
                   // ButtonOut("920");
                    Track_bulb_event("Switch Operation","Bulb2 Changeicon","Bulb2 Longclick");
                }
                break;
            case R.id.btnbulb3:
                if(b_bulb3!=null) {
                    Changeiconbulb("bulb3", 2);
                    Track_bulb_event("Switch Operation","Bulb3 Changeicon","Bulb3 Longclick");
                }
                break;
            case R.id.btnbulb4:
                if(b_bulb4!=null) {
                    Changeiconbulb("bulb4", 3);
                    Track_bulb_event("Switch Operation","Bulb4 Changeicon","Bulb4 Longclick");
                }
                break;
            case R.id.btnbulb5:
                if(b_bulb5!=null) {
                    Changeiconbulb("bulb5", 4);
                    Track_bulb_event("Switch Operation","Bulb5 Changeicon","Bulb5 Longclick");
                }
                break;
            case R.id.btnbulb6:
                if(b_bulb6!=null) {
                    Changeiconbulb("bulb6", 5);
                    Track_bulb_event("Switch Operation","Bulb6 Changeicon","Bulb6 Longclick");
                }
                break;
            case R.id.btnbulb7:
                if(b_bulb7!=null) {
                    Changeiconbulb("bulb7", 6);
                    Track_bulb_event("Switch Operation","Bulb7 Changeicon","Bulb7 Longclick");
                }
                break;
            case R.id.btnbulb8:
                if(b_bulb8!=null) {
                    Changeiconbulb("bulb8", 7);
                    Track_bulb_event("Switch Operation","Bulb8 Changeicon","Bulb8 Longclick");
                }
                break;
            case R.id.btnbulb9:
                if(b_bulb9!=null)
                Changeiconbulb("bulb9",8);
                break;
            case R.id.btnbulb10:
                if(b_bulb10!=null)
                Changeiconbulb("bulb10",9);
                break;
            case R.id.btnbulb11:
                if(b_bulb11!=null)
                Changeiconbulb("bulb11",10);
                break;
            case R.id.btnbulb12:
                if(b_bulb12!=null)
                Changeiconbulb("bulb12",11);
                break;

          /*  case R.id.im_roomimg:
			*//*Intent intent = new Intent();
        	intent.setType("image*//*");
        	intent.setAction(Intent.ACTION_GET_CONTENT);*//*
                Intent intent = new Intent(
                        Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),SELECT_IMAGE);
                return true;*/

        }
        return false;
    }

   public void Changeiconbulb(final String bulbname ,final int pos){

     //  final String icon_txt[]; Integer micons_img[];
        AlertDialog.Builder builder;
        Context mContext = getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.categeory,(ViewGroup) view.findViewById(R.id.layout_root));
        final GridView gridview = (GridView)layout.findViewById(R.id.gridview);

       if(strtext.equals("S080")) {

           if(pos==6||pos==7){

               icon_txt=icon_names_text_hvac;
               micons_img=icons_off_types_hvac;

           }else{
               icon_txt=icon_names_text;
               micons_img=icons_off_types;
           }

       }else if(strtext.equals("S051")) {

           if(pos==3||pos==4){

               icon_txt=icon_names_text_hvac;
               micons_img=icons_off_types_hvac;

           }else{
               icon_txt=icon_names_text;
               micons_img=icons_off_types;
           }

       }else  if(strtext.equals("S030")) {

           if(pos==2){

               icon_txt=icon_names_text_hvac;
               micons_img=icons_off_types_hvac;

           }else{
               icon_txt=icon_names_text;
               micons_img=icons_off_types;
           }

       }else if((strtext.equals("S020"))||(strtext.equals("S021"))) {

           if(pos==1){

               icon_txt=icon_names_text_hvac;
               micons_img=icons_off_types_hvac;

           }else{
               icon_txt=icon_names_text;
               micons_img=icons_off_types;
           }

       }else if(strtext.equals("S010")) {

           if(pos==0){

               icon_txt=icon_names_text_hvac;
               micons_img=icons_off_types_hvac;

           }else{
               icon_txt=icon_names_text;
               micons_img=icons_off_types;
           }


       }else {
           icon_txt=icon_names_text;
           micons_img=icons_off_types;

       }


       gridview.setAdapter(new Image_Icon_Adapter(getActivity(), micons_img, icon_txt));

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id) {

                String clickedText = icon_names_text[position];
                dialog.dismiss();
                Swb_Adapter.open();
                boolean upd= Swb_Adapter.updateiconsnw(devno,icon_txt[position],bulbname,roomno);
                Swb_Adapter.close();

                if(upd){

                    longclick_officon(btnarray_changeicon[pos],icon_txt[position],pos);
                }

            }
        });

        builder = new AlertDialog.Builder(mContext);
        builder.setView(layout);
        dialog = builder.create();
       dialog.show();

    }
    /////////////////////////////////////////////////////////////////////////////////////////////////

    void longclick_officon(final Button bulbic, final String type,final int posi ) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                StaticVariabes_div.log(strtext+"posi"+posi,TAG1);

                for (int n = 0; n < micons_img.length; n++) {
                    if (type.equals(icon_txt[n])) {


                        StaticVariabes_div.log(icon_txt[n]+strtext+"posi"+posi,TAG1);

                            if(strtext.equals("S080")) {

                                if(posi==6||posi==7){
                                    test_icnchange(bulbic,n);
                                }else{

                                    bulbic.setBackgroundResource(icons_off_types[n]);
                                }

                            }else if(strtext.equals("S051")) {
                                StaticVariabes_div.log("posi"+posi,TAG1);
                                if(posi==3||posi==4){

                                    test_icnchange(bulbic,n);

                                }else{

                                    bulbic.setBackgroundResource(icons_off_types[n]);
                                }

                            }else  if(strtext.equals("S030")) {

                                if(posi==2){

                                    test_icnchange(bulbic,n);
                                }else{

                                    bulbic.setBackgroundResource(icons_off_types[n]);
                                }

                            }else if((strtext.equals("S020"))||(strtext.equals("S021"))) {

                                if(posi==1){

                                    test_icnchange(bulbic,n);

                                }else{

                                    bulbic.setBackgroundResource(icons_off_types[n]);
                                }

                            }else if(strtext.equals("S010")) {

                                if(posi==0){

                                    test_icnchange(bulbic,n);

                                }else{

                                    bulbic.setBackgroundResource(icons_off_types[n]);
                                }

                            }else {
                                bulbic.setBackgroundResource(icons_off_types[n]);
                            }
                    }
                }

                onstart2();
                initBtn();

               // ButtonOut("920");

            }
        });
    }


    public void test_icnchange(Button bulbic,int n ){
        if(icon_txt[n].equals("bulb")) {
            bulbic.setBackgroundResource(R.drawable.high_volt_bulb);
        }else  if(icon_txt[n].equals("ac")) {
            bulbic.setBackgroundResource(R.drawable.icn_ac_off);
        }else  if(icon_txt[n].equals("geyser")) {
            bulbic.setBackgroundResource(R.drawable.icn_geyser_off);
        }
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////
    private enum Models {
        S010, S020, S030, S060, S080, S120, S021, S031,  S051,S141,S160,S110, SLT1, WPS1 ,test
    }

    public void switch1_0() {
        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button)  view.findViewById(R.id.btnbulb1);
        int dv=Integer.parseInt(devno);

        btnarray_changeicon=new Button[]{b_bulb1};
       // tvmodel.setText("S010-"+dv);
        devtypesett="001";
        model="S010-"+dv;
        initBtn();
        onstart2();
        listener(1);
        StaticVariabes_div.dev_typ_num=devtypesett;
    }

    public void switch2_0() {

        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;
        b_bulb1 = (Button)  view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button)  view.findViewById(R.id.btnbulb2);

        b_allon = (Button)  view.findViewById(R.id.btnallon);
        b_alloff = (Button)  view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2};

        initBtn();
        allonoffinit();
        listener(2);
        onstart2();
        int dv=Integer.parseInt(devno);
        devtypesett="002";
        model="S020-"+dv;

        StaticVariabes_div.dev_typ_num=devtypesett;
    }

    public void switch1_1s() {

        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;
        b_bulb1 = (Button)  view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button)  view.findViewById(R.id.btnbulb2);

        b_allon = (Button)  view.findViewById(R.id.btnallon);
        b_alloff = (Button)  view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2};

        initBtn();
        allonoffinit();
        listener(2);
        onstart2();
        int dv=Integer.parseInt(devno);
        devtypesett="063";
        model="S110-"+dv;

        StaticVariabes_div.dev_typ_num=devtypesett;
    }



    public void switch2_1() {

        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);

        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        img_fan = (ImageView) view.findViewById(R.id.img_leddisp1);
        btn_fan = (Button) view.findViewById(R.id.btnfan);
        b_incr = (Button) view.findViewById(R.id.btnfanup1);
        b_dcr = (Button) view.findViewById(R.id.btnfandwn1);
        int dv=Integer.parseInt(devno);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2};

        devtypesett="008";
        model="S021-"+dv;

        StaticVariabes_div.dev_typ_num=devtypesett;

        initBtn();
        listener(2);
        allonoffinit();
        fanoneinit();
        onstart2();


    }
    public void switch3_0() {


        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button) view.findViewById(R.id.btnbulb3);

        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3};
        int dv=Integer.parseInt(devno);
        devtypesett="003";
        model="S030-"+dv;

        StaticVariabes_div.dev_typ_num=devtypesett;

        initBtn();
        allonoffinit();
        listener(3);
        onstart2();
    }
    public void switch2_1s() {


        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button) view.findViewById(R.id.btnbulb3);

        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3};
        int dv=Integer.parseInt(devno);
        devtypesett="062";
        model="S120-"+dv;

        StaticVariabes_div.dev_typ_num=devtypesett;

        initBtn();
        allonoffinit();
        listener(3);
        onstart2();
    }

    public void switch5_1() {

        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button)  view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button)  view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button)  view.findViewById(R.id.btnbulb3);
        b_bulb4 = (Button)  view.findViewById(R.id.btnbulb4);
        b_bulb5 = (Button)  view.findViewById(R.id.btnbulb5);


        b_allon = (Button)  view.findViewById(R.id.btnallon);
        b_alloff = (Button)  view.findViewById(R.id.btnalloff);

        //lay_fan=(LinearLayout)  view.findViewById(R.id.layfan1);

        img_fan = (ImageView)  view.findViewById(R.id.img_leddisp1);
        btn_fan = (Button) view.findViewById(R.id.btnfan);
        b_incr = (Button)  view.findViewById(R.id.btnfanup1);
        b_dcr = (Button)  view.findViewById(R.id.btnfandwn1);


         btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3,b_bulb4,b_bulb5};


        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;
        int dv=Integer.parseInt(devno);

        devtypesett="011";
        model="S051-"+dv;
        initBtn();
        listener(5);
        allonoffinit();
        fanoneinit();
        onstart2();

        StaticVariabes_div.dev_typ_num=devtypesett;

    }
    public void switch4_1_1() {

        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button)  view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button)  view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button)  view.findViewById(R.id.btnbulb3);
        b_bulb4 = (Button)  view.findViewById(R.id.btnbulb4);
        b_bulb5 = (Button)  view.findViewById(R.id.btnbulb5);


        b_allon = (Button)  view.findViewById(R.id.btnallon);
        b_alloff = (Button)  view.findViewById(R.id.btnalloff);

        //lay_fan=(LinearLayout)  view.findViewById(R.id.layfan1);

        img_fan = (ImageView)  view.findViewById(R.id.img_leddisp1);
        btn_fan = (Button) view.findViewById(R.id.btnfan);
        b_incr = (Button)  view.findViewById(R.id.btnfanup1);
        b_dcr = (Button)  view.findViewById(R.id.btnfandwn1);


        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3,b_bulb4,b_bulb5};


        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;
        int dv=Integer.parseInt(devno);

        devtypesett="061";
        model="S141-"+dv;
        initBtn();
        listener(5);
        allonoffinit();
        fanoneinit();
        onstart2();

        StaticVariabes_div.dev_typ_num=devtypesett;

    }
    public void switch8_0() {
        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button) view.findViewById(R.id.btnbulb3);
        b_bulb4 = (Button) view.findViewById(R.id.btnbulb4);
        b_bulb5 = (Button) view.findViewById(R.id.btnbulb5);
        b_bulb6 = (Button) view.findViewById(R.id.btnbulb6);
        b_bulb7 = (Button) view.findViewById(R.id.btnbulb7);
        b_bulb8 = (Button) view.findViewById(R.id.btnbulb8);

        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3,b_bulb4,b_bulb5,b_bulb6,b_bulb7,b_bulb8};

        int dv=Integer.parseInt(devno);

        devtypesett="006";
        model="S080-"+dv;
        initBtn();
        listener(8);
        allonoffinit();
        onstart2();

        StaticVariabes_div.dev_typ_num=devtypesett;

    }

    public void switch6_1s() {
        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button) view.findViewById(R.id.btnbulb3);
        b_bulb4 = (Button) view.findViewById(R.id.btnbulb4);
        b_bulb5 = (Button) view.findViewById(R.id.btnbulb5);
        b_bulb6 = (Button) view.findViewById(R.id.btnbulb6);
        b_bulb7 = (Button) view.findViewById(R.id.btnbulb7);


        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3,b_bulb4,b_bulb5,b_bulb6,b_bulb7};

        int dv=Integer.parseInt(devno);

        devtypesett="064";
        model="S160-"+dv;
        initBtn();
        listener(7);
        allonoffinit();
        onstart2();

        StaticVariabes_div.dev_typ_num=devtypesett;

    }

    public void switch12_0() {
        devno=StaticVariabes_div.devnumber;
        deviceType=StaticVariabes_div.devtyp;

        b_bulb1 = (Button) view.findViewById(R.id.btnbulb1);
        b_bulb2 = (Button) view.findViewById(R.id.btnbulb2);
        b_bulb3 = (Button) view.findViewById(R.id.btnbulb3);
        b_bulb4 = (Button) view.findViewById(R.id.btnbulb4);
        b_bulb5 = (Button) view.findViewById(R.id.btnbulb5);
        b_bulb6 = (Button) view.findViewById(R.id.btnbulb6);
        b_bulb7 = (Button) view.findViewById(R.id.btnbulb7);
        b_bulb8 = (Button) view.findViewById(R.id.btnbulb8);
        b_bulb9 = (Button) view.findViewById(R.id.btnbulb9);
        b_bulb10 = (Button) view.findViewById(R.id.btnbulb10);
        b_bulb11 = (Button) view.findViewById(R.id.btnbulb11);
        b_bulb12 = (Button) view.findViewById(R.id.btnbulb12);

        b_allon = (Button) view.findViewById(R.id.btnallon);
        b_alloff = (Button) view.findViewById(R.id.btnalloff);

        btnarray_changeicon=new Button[]{b_bulb1,b_bulb2,b_bulb3,b_bulb4,b_bulb5,b_bulb6,
                b_bulb7,b_bulb8,b_bulb9,b_bulb10,b_bulb11,b_bulb12};
        int dv=Integer.parseInt(devno);
        devtypesett="007";
        model="S120-"+dv;
        initBtn();
        listener(12);
        allonoffinit();
        onstart2();

        StaticVariabes_div.dev_typ_num=devtypesett;

    }
    //on icon change code
    public void onstart2() {

        try {
            fetchdata(devno, deviceType);
        }catch (Exception e){
            e.printStackTrace();
        }

        int mn=4;
        switch (SwitchBoard_All_Models_Frag.Models.valueOf(deviceType)) {

            case S010:

                StaticVariabes_div.log("on start swb 1", TAG1);
                for (int y = 0; y < 1; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;

            case S020:

                StaticVariabes_div.log("on start swb 2", TAG1);
                for (int y = 0; y < 2; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;

            case S110:

                StaticVariabes_div.log("on start swb 2", TAG1);
                for (int y = 0; y < 2; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;


            case S030:

                StaticVariabes_div.log("on start swb 3_0", TAG1);
                for (int y = 0; y < 3; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;
            case S120:

                StaticVariabes_div.log("on start swb 2_1s", TAG1);
                for (int y = 0; y < 3; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;

            case S060:

                StaticVariabes_div.log("on start swb 6_0", TAG1);
                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                for (int y = 0; y < 2; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+y);

                }

                break;
            case S080:

               // int j=4;
                StaticVariabes_div.log("on start swb 8_0", TAG1);
                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+mn);
                    mn++;
                }

                break;

            case S160:

                // int j=4;
                StaticVariabes_div.log("on start swb 8_0", TAG1);
                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+mn);
                    mn++;
                }

                break;

          /*  case S120:

                StaticVariabes_div.log("on start swb 12_0", TAG1);
                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+y);
                }

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr9_12[y], bulbiconarr9_12[y],""+y);
                }


                break;*/


            case S021:

                StaticVariabes_div.log("on start swb 2_1", TAG1);
                for (int y = 0; y < 2; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }

                break;

            case S031:

                StaticVariabes_div.log("on start swb 3_1", TAG1);
                for (int y = 0; y < 3; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }

                break;


            case S051:

                StaticVariabes_div.log("on start swb 5_1", TAG1);

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                for (int y = 0; y < 1; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+mn);
                    mn++;
                }

                break;
            case S141:

                StaticVariabes_div.log("on start swb 4_1_1", TAG1);

                for (int y = 0; y < 4; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                for (int y = 0; y < 1; y++) {
                    seticonsOFF(bulbicontypearr5_8[y], bulbiconarr5_8[y],""+mn);
                    mn++;
                }

                break;

            case SLT1:

                StaticVariabes_div.log("on start swb SLT1", TAG1);
                for (int y = 0; y < 1; y++) {
                    seticonsOFF(bulbicontypearr1_4[y], bulbiconarr1_4[y],""+y);
                }
                break;
            default:
                System.out.println("Not matching");
        }
        ButtonOut("920");

    }

    public void fetchdata(String dev, final String devTyp) {

        StaticVariabes_div.log("dev msg "+dev+" devtyp"+devTyp+"dev msg no" + devno +"roomno"+roomno, TAG1);
        Swb_Adapter = new SwbAdapter(getActivity().getApplicationContext());
        Swb_Adapter.open();
        roomno= Swb_Adapter.getroomno(StaticVariabes_div.roomname);

        Cursor cu1 = Swb_Adapter.fetch_rowdevnoroomnohouseno(devno, roomno);

        if(cu1!=null)
        Roomname = cu1.getString(cu1.getColumnIndexOrThrow(SwbAdapter.KEY_b));

        StaticVariabes_div.log("room img path" + SwitchBoard_All_Models_Frag.Models.valueOf(devTyp)+"dev msg type" + SwitchBoard_All_Models_Frag.Models.valueOf(devTyp), TAG1);

        switch (SwitchBoard_All_Models_Frag.Models.valueOf(devTyp)) {
            case S010:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbnamestrarr[0] =bulb1name= cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                break;

            case S020:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));

                bulbnamestrarr[0] =bulb1name= cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] =bulb2name= cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                break;


            case S030:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                break;

            case S120:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                break;

            case S110:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                break;



            case S060:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));
                bulbicontypearr5_8[1] = b6icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi6));

                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));
                bulbnamestrarr[5] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn6));
                break;

            case S080:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));
                bulbicontypearr5_8[1] = b6icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi6));
                bulbicontypearr5_8[2] = b7icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi7));
                bulbicontypearr5_8[3] = b8icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi8));

                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));
                bulbnamestrarr[5] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn6));
                bulbnamestrarr[6] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn7));
                bulbnamestrarr[7] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn8));
                break;


            case S160:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));
                bulbicontypearr5_8[1] = b6icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi6));
                bulbicontypearr5_8[2] = b7icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi7));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));
                bulbnamestrarr[5] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn6));
                bulbnamestrarr[6] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn7));

                break;


            case S021:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));

                break;

            case S031:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));

                break;


            case S051:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));

                break;
            case S141:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));

                break;


           /* case S120:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbicontypearr1_4[1] = b2icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi2));
                bulbicontypearr1_4[2] = b3icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi3));
                bulbicontypearr1_4[3] = b4icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi4));

                bulbicontypearr5_8[0] = b5icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi5));
                bulbicontypearr5_8[1] = b6icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi6));
                bulbicontypearr5_8[2] = b7icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi7));
                bulbicontypearr5_8[3] = b8icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi8));

                bulbicontypearr9_12[0] = b9icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi9));
                bulbicontypearr9_12[1] = b10icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi10));
                bulbicontypearr9_12[2] = b11icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi11));
                bulbicontypearr9_12[3] = b12icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi12));


                bulbnamestrarr[0] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                bulbnamestrarr[1] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn2));
                bulbnamestrarr[2] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn3));
                bulbnamestrarr[3] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn4));
                bulbnamestrarr[4] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn5));
                bulbnamestrarr[5] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn6));
                bulbnamestrarr[6] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn7));
                bulbnamestrarr[7] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn8));
                bulbnamestrarr[8] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn9));
                bulbnamestrarr[9] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn10));
                bulbnamestrarr[10] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn11));
                bulbnamestrarr[11] = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn12));

                break;*/

            case SLT1:
                bulbicontypearr1_4[0] = b1icon = cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bi1));
                bulbnamestrarr[0] =bulb1name= cu1.getString(cu1
                        .getColumnIndexOrThrow(SwbAdapter.KEY_bn1));
                break;
            default:

			/*for(int y=0;y<=bulbnamestrarr.length;y++){
				Log.d("msg","bulbnamestrarr"+bulbnamestrarr[y]);
			}*/
        }

        cu1.close();
        Swb_Adapter.close();

    }
    void initBtn()
    {

        for(int n=0;n<bulbstate1_4.length;n++){
            bulbstate1_4[0] =false ;
            bulbstate5_8[0] =false ;
            bulbstate9_12[0] =false ;
        }
        b1state=b2state=b3state=b4state=b5state=b6state=b7state=b8state=b9state=b10state=b11state=b12state=false;
        f1state=f2state=f3state=f4state=powerstatus=false;
        bstateui1_4[0]=b1state ;bstateui1_4[1] =b2state ;bstateui1_4[2] =b3state ;bstateui1_4[3] =b4state;
        bstateui5_8[0]=b5state ;bstateui5_8[1] =b6state ;bstateui5_8[2] =b7state ;bstateui5_8[3] =b8state;
        bstateui9_12[0]=b9state ;bstateui9_12[1] =b10state ;bstateui9_12[2] =b11state ;bstateui9_12[3] =b12state;
        fn_Status[0]=f1state;fn_Status[0]=f2state;fn_Status[0]=f3state;fn_Status[0]=f4state;
        fn_Speed[0]=f1speed;fn_Speed[1]=f2speed;fn_Speed[2]=f3speed;fn_Speed[3]=f4speed;

        bulbnamestrarr = new String[] { bulb1name, bulb2name, bulb3name,
                bulb4name, bulb5name, bulb6name, bulb7name, bulb8name, bulb9name, bulb10name, bulb11name, bulb12name };
        btnarr = new Button[] { b_bulb1, b_bulb2, b_bulb3, b_bulb4, b_bulb5,
                b_bulb6, b_bulb7, b_fan, b_allon, b_alloff, b_incr, b_dcr,
                b_leddis };
        bulbarr = new boolean[] { false, false, false, false, false, false,
                false, false, false, false, false, false, false };

        bulbiconarr1_4 = new Button[] { b_bulb1, b_bulb2, b_bulb3, b_bulb4 };
        bulbiconarr5_8 = new Button[] { b_bulb5, b_bulb6, b_bulb7, b_bulb8 };
        bulbiconarr9_12 = new Button[] { b_bulb9, b_bulb10, b_bulb11, b_bulb12 };

        bulblistenerbtnarr= new Button[] { b_bulb1, b_bulb2, b_bulb3, b_bulb4 ,b_bulb5,
                b_bulb6, b_bulb7,b_bulb8,b_bulb9, b_bulb10,b_bulb11,b_bulb12};
    }
    public void listener(int j){
        for(int k=0;k<j;k++){
            bulblistenerbtnarr[k].setOnClickListener(this);
            bulblistenerbtnarr[k].setOnLongClickListener(this);

        }

    }
    public void fanoneinit(){
        btn_fan.setOnClickListener(this);
        img_fan.setOnClickListener(this);
        b_incr.setOnClickListener(this);
        b_dcr.setOnClickListener(this);

    }

    public void allonoffinit(){
        b_allon.setOnClickListener(this);
        b_alloff.setOnClickListener(this);
    }

    public void seticonsON(String bval, Button bset, String bulb_num) {
        if(bval!=null)
            if (bval.equals("tv")) {
                ifonicon(bset, "tv");
            } else if (bval.equals("rgb")) {
                ifonicon(bset, "rgb");
            } else if (bval.equals("dimmr")) {
                ifonicon(bset, "dimmr");
            } else if (bval.equals("curtain")) {
                ifonicon(bset, "curtain");
            } else if (bval.equals("door")) {
                ifonicon(bset, "door");
            } else if (bval.equals("sprinkler")) {
                ifonicon(bset, "sprinkler");
            } else if (bval.equals("geyser")) {
                ifonicon_hvac(bset, "geyser");
            } else if (bval.equals("ac")) {
                ifonicon_hvac(bset, "ac");
            } else if (bval.equals("aqua")) {
                ifonicon(bset, "aqua");
            } else if (bval.equals("disp")) {
                ifonicon(bset, "disp");
            }else if (bval.equals("bedlamp")) {
                ifonicon(bset, "bedlamp");
            }else if (bval.equals("speaker")) {
                ifonicon(bset, "speaker");
            }else if (bval.equals("tubelight")) {
                ifonicon(bset, "tubelight");
            }else if (bval.equals("cfl")) {
                ifonicon(bset, "cfl");
            }else if (bval.equals("socket")) {
                ifonicon(bset, "socket");
            }else if(bval.equals("exhaustfan")){
                ifonicon(bset, "exhaustfan");
            } else if(bval.equals("ebulb")){
                ifonicon(bset, "ebulb");
            }  else {

                if(strtext.equals("S080")){
                    if(  (bulb_num.equals("6")) ||  (bulb_num.equals("7")) ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);
                }
                if(strtext.equals("S160")){
                    if(  (bulb_num.equals("6")) ){
                        bulbon1_hvac_sockt(bset);
                    }else
                        bulbon1(bset);
                }
                else if(strtext.equals("S051")){

                    if(  (bulb_num.equals("3")) ||  (bulb_num.equals("4")) ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);

                }
                else if(strtext.equals("S141")){

                    if(  (bulb_num.equals("4")) ){
                        bulbon1_hvac_sockt(bset);
                    }else
                        bulbon1(bset);

                }
                else if(strtext.equals("S030")){
                    if (bulb_num.equals("2") ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);
                }
                else if(strtext.equals("S120")){
                    if (bulb_num.equals("2") ){
                        bulbon1_hvac_sockt(bset);
                    }else
                        bulbon1(bset);
                }else if(strtext.equals("S020")){

                    if (bulb_num.equals("1") ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);

                }
                else if(strtext.equals("S110")){

                    if (bulb_num.equals("1") ){
                        bulbon1_hvac_sockt(bset);
                    }else
                        bulbon1(bset);

                }
                else if(strtext.equals("S021")){

                    if (bulb_num.equals("1") ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);

                } else if(strtext.equals("S010")){
                    if (bulb_num.equals("0") ){
                        bulbon1_hvac(bset);
                    }else
                        bulbon1(bset);
                }else {
                    bulbon1(bset);
                }
            }

    }

    public void seticonsOFF(String bval, Button bset,String bulb_num) {

        if(bval!=null)
            if (bval.equals("tv")) {
                ifofficon(bset, "tv");
            } else if (bval.equals("rgb")) {
                ifofficon(bset, "rgb");
            } else if (bval.equals("dimmr")) {
                ifofficon(bset, "dimmr");
            } else if (bval.equals("curtain")) {
                ifofficon(bset, "curtain");
            } else if (bval.equals("door")) {
                ifofficon(bset, "door");
            } else if (bval.equals("sprinkler")) {
                ifofficon(bset, "sprinkler");
            } else if (bval.equals("geyser")) {
                ifofficon_hvac(bset, "geyser");
            } else if (bval.equals("ac")) {
                ifofficon_hvac(bset, "ac");
            } else if (bval.equals("aqua")) {
                ifofficon(bset, "aqua");
            }else if (bval.equals("disp")) {
                ifofficon(bset, "disp");
            }else if (bval.equals("bedlamp")) {
                ifofficon(bset, "bedlamp");
            }else if (bval.equals("speaker")) {
                ifofficon(bset, "speaker");
            }else if (bval.equals("tubelight")) {
                ifofficon(bset, "tubelight");
            }else if (bval.equals("cfl")) {
                ifofficon(bset, "cfl");
            }else if (bval.equals("socket")) {
                ifofficon(bset, "socket");
            }else if (bval.equals("exhaustfan")) {
                ifofficon(bset, "exhaustfan");
            }else if (bval.equals("ebulb")) {
                ifofficon(bset, "ebulb");
            }else {

                if(strtext.equals("S080")){
                    if(  (bulb_num.equals("6")) ||  (bulb_num.equals("7")) ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);

                }
                if(strtext.equals("S160")){
                    if(  (bulb_num.equals("6"))){
                        bulboff1_hvac_sockt(bset);
                    }else
                        bulboff1(bset);

                }
                else if(strtext.equals("S051")){

                    if(  (bulb_num.equals("3")) ||  (bulb_num.equals("4")) ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);

                }
                else if(strtext.equals("S141")){

                    if(  (bulb_num.equals("4")) ){
                        bulboff1_hvac_sockt(bset);
                    }else
                        bulboff1(bset);

                }


                else if(strtext.equals("S030")){
                    if (bulb_num.equals("2") ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);
                }else if(strtext.equals("S120")){
                    if (bulb_num.equals("2") ){
                        bulboff1_hvac_sockt(bset);
                    }else
                        bulboff1(bset);
                }

                else if(strtext.equals("S020")){

                    if (bulb_num.equals("1") ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);
                }
                else if(strtext.equals("S110")){

                    if (bulb_num.equals("1") ){
                        bulboff1_hvac_sockt(bset);
                    }else
                        bulboff1(bset);
                }

                else if(strtext.equals("S021")){

                    if (bulb_num.equals("1") ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);
                } else if(strtext.equals("S010")){
                    if (bulb_num.equals("0") ){
                        bulboff1_hvac(bset);
                    }else
                        bulboff1(bset);
                }else {
                    bulboff1(bset);
                }

            }
    }

    // BulB ON method1 TO CHANGE BULB IMAGE
    public void bulbon1(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.drawable.bulb02);
            }
        });

    }

    public void bulboff1(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.drawable.bulb01);
            }
        });

    }

    // BulB ON method1 TO CHANGE HVAC BULB IMAGE
    public void bulbon1_hvac(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.mipmap.high_volt_bulb01);
            }
        });

    }

    public void bulbon1_hvac_sockt(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.mipmap.sockt_on);
            }
        });

    }

    public void bulboff1_hvac(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.mipmap.high_volt_bulb);
            }
        });

    }
    public void bulboff1_hvac_sockt(final Button b) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                b.setBackgroundResource(R.mipmap.sockt_off);
            }
        });

    }

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    void ifonicon(final Button bulbic, final String type) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int n = 0; n < icon_on_types.length; n++) {
                    if (type.equals(icon_names_text[n])) {
                        bulbic.setBackgroundResource(icon_on_types[n]);
                    }
                }
            }
        });
    }

    void ifofficon(final Button bulbic, final String type) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int n = 0; n < icons_off_types.length; n++) {
                    if (type.equals(icon_names_text[n])) {
                        bulbic.setBackgroundResource(icons_off_types[n]);
                    }
                }
            }
        });
    }

    void ifonicon_hvac(final Button bulbic, final String type) {

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int n = 0; n < icons_on_types_hvac.length; n++) {
                    if (type.equals(icon_names_text_hvac[n])) {
                        bulbic.setBackgroundResource(icons_on_types_hvac[n]);
                    }
                }
            }
        });
    }

    void ifofficon_hvac(final Button bulbic, final String type) {
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                for (int n = 0; n < icons_off_types_hvac.length; n++) {
                    if (type.equals(icon_names_text_hvac[n])) {
                        bulbic.setBackgroundResource(icons_off_types_hvac[n]);
                    }
                }
            }
        });
    }

    @Override
    public void read(final int type, final String stringData, final byte[] byteData)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                try {
                    receiveddata(type, stringData, byteData);
                }catch (Exception e){
                    e.printStackTrace();

                    /*if ((e.toString().contains("nullpointer"))) {
                        Tcp_con.stacontxt = getActivity().getApplicationContext();
                        Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
                        Tcp_con.registerReceivers(getActivity().getApplicationContext());
                    }*/
                }

            }
        });
    }

    public void receiveddata(int msg,String data,byte[] bytestatus){

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
               // final String readMessage = new String(readBuf, 0,readBuf.length);
                StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);
                DataIn(readBuf);

                break;
            case READ_LINE:
                //  readMessage2 = (String) msg.obj;
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 =data;
                if(readMessage2.contains("*OK#")){
                   // Tcp_con.WriteBytes(("$115&").getBytes());
                  //  mn_nav_obj.serv_status(true);

                   /* try {
                        Thread.sleep(100);
                        ButtonOut("920");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
*/

                }else{

                    combf.timerresponse(readMessage2);
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
                        ButtonOut("920");
                        // Cc.TcpReadLine = false;

                      /*  try {
                            Thread.sleep(500);
                            Tcp_con.WriteBytes(("$115&").getBytes());
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/

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

                mn_nav_obj.serv_status(statusserv);

                break;
            case signallevel:

                final String signallevelB = data;
                if(signallevelB!=null){
                    sl = Integer.parseInt(signallevelB);
                    rs=signallevelB;

                    if((StaticStatus.Network_Type.equals("TRUE")||(StaticStatus.Network_Type.equals("TRUE3G")))){

                        mn_nav_obj.network_signal(sl,true);

                        if(StaticStatus.Network_Type.equals("TRUE3G")||StaticStatus.Network_Type.equals("NONET")){
                            if(timer!=null){
                                timer.cancel();
                                timer=null;
                            }
                        }

                    }else{

                        mn_nav_obj.network_signal(sl,false);

                    }

                }
                break;
            case NetwrkType:
                //final String RemoteB = (String) msg.obj;
                final String RemoteB = data;
                StaticStatus.Network_Type=RemoteB;
                StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
                if (RemoteB.equals("TRUE")) {
                    nonetwork=false;
                    remoteconn = true;
                    remoteconn3g = false;
                    remoteconprevstate="TRUE";

                    mn_nav_obj.network_signal(sl,remoteconn);

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

                    mn_nav_obj.network_signal(sl,remoteconn);

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
                    mn_nav_obj.network_signal(sl,remoteconn);


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

                    mn_nav_obj.network_signal(sl,remoteconn);

                }

                break;
            case MAXUSER:
                final String maxuser = data;
                StaticVariabes_div.log("maxuser swb" + maxuser, TAG1);
                if (maxuser.equals("TRUE")) {
                    popup("User Exceeded");
                    mn_nav_obj.serv_status(false);
                } else {

                }

                break;
            case  ERRUSER:
                final String erruser = data;
                StaticVariabes_div.log("erruser swb testing" + erruser, TAG1);

                if (erruser.equals("TRUE")) {
                    popup("INVALID USER/PASSWORD");
                    mn_nav_obj.serv_status(false);
                } else {

                }
                break;
            case  UPDATE:
                final String update = data;
                StaticVariabes_div.log("StaticVariabes_div.House_dbver_num_gateway" + StaticVariabes_div.House_dbver_num_gateway, TAG1);
                StaticVariabes_div.log("StaticVariabes_div.House_dbver_num_local" + StaticVariabes_div.House_dbver_num_local, TAG1);

                if (update.equals("TRUE")) {

                    if (Float.valueOf(StaticVariabes_div.House_dbver_num_gateway) > Float.valueOf(StaticVariabes_div.House_dbver_num_local)) {
                        //popup("UPDDATE");
                        Tcp_con.stopClient();
                        Tcp_con.isClientStarted=false;

                        Tcp_dwn_config.tcpHost=Tcp_con.tcpAddress;
                        Tcp_dwn_config.tcpPort=Tcp_con.tcpPort;


                        Intent intt=new Intent(getActivity(),UpdateHome_Existing.class);
                        intt.putExtra("isusersett","updatesett");
                        startActivity(intt);
                    } if (Float.valueOf(StaticVariabes_div.House_dbver_num_gateway) < Float.valueOf(StaticVariabes_div.House_dbver_num_local)) {
                   Toast.makeText(getActivity(),"App Database Version is Higher Than Gateway Version",Toast.LENGTH_LONG).show();
                        //popup("App Database Version is Higher Than Gateway Version");
                    }else
                    {
                       // popup("No UPDDATE");
                    }
                }
                break;
        }
    }


    public void popup(String msg){

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

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


    void ButtonOut(String bo) {

        devno=StaticVariabes_div.devnumber;

//        Toast.makeText(getActivity().getApplicationContext(),"devty"+StaticVariabes_div.devtyp+"  devno"+StaticVariabes_div.devnumber,Toast.LENGTH_SHORT);
        // logTimeStamp("Before executing time");
        if (Tcp_con.isClientStarted) {
            while (devno.length() < 4)devno = "0" + devno;
            while(roomno.length()<2)roomno="0"+roomno;
            String str = "0" + broadcastMsg + groupId + devno +roomno+ bo + "000000000000000";
            StaticVariabes_div.log("out" + str, TAG1);
            //byte[] op= Blaster.WriteData(str, 1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            //String str
            StaticVariabes_div.log(result.length+" bout"+ bo, TAG1);
           // Log.d(result+"op switch"+op,TAG1);

            powerstatus=true;
            //  currtime=powertime();
            // Cc.tcpOUT(result, true);
            Tcp_con.WriteBytes(result);
        }
    }

    // $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$ TO CHECK INPUT RESPONSE// $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    public void DataIn(byte[] byt) {
        // logTimeStamp("After data in");
        StaticVariabes_div.log("swb len" + byt.length, TAG1);



        devno=StaticVariabes_div.devnumber;
        while (devno.length() < 4)devno = "0" + devno;

        deviceType=StaticVariabes_div.devtyp;

        if (byt != null && byt.length >= 32) {//34

          if(byt.length == 32) {
              byte[] input = new byte[30];
              System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));///4
              inp = new String(input);
              StaticVariabes_div.log("swb test inp" + inp, TAG1);
          }else {

              getActivity().runOnUiThread(new Runnable() {
                  public void run() {
                      Toast.makeText(getActivity().getApplicationContext(),"Invalid data length ",Toast.LENGTH_SHORT).show();
                  }
              });
              byte[] tempBuf2 = null;
              //byte[] inputest = new byte[ byt.length];
              //String inptemp = new String(inputest);
              // StaticVariabes_div.log("swb inptemp"+inptemp, TAG1);
              //System.arraycopy(byt, 1, inputest, 0, ((byt.length) - 2));
              // StaticVariabes_div.log("swb test inp"+inp, TAG1);
              StaticVariabes_div.log("swb length else", TAG1);
              byte[] inputest = new byte[byt.length];
              System.arraycopy(byt, 1, inputest, 0, (byt.length)-2);
              String inptemp = new String(inputest);
              StaticVariabes_div.log("swb inptemp"+inptemp, TAG1);
              int posStart = -1;
              int posEnd = -1;
              int actualRec = 0;
              int dataToRead = byt.length-2;
              if (dataToRead > 1) {
                  for (int j = dataToRead; j >= 0; j--) {
                      if (inputest[j] == (byte) 0x23)
                          posEnd = j;
                      if (posEnd != 0 && inputest[j] == (byte) 0x2A) {
                          posStart = j;
                          break;
                      }
                  }

                  if (posStart != -1 && posEnd != -1) {
                      actualRec = 1 + (posEnd - posStart);
                      tempBuf2 = new byte[actualRec];
                      System.arraycopy(inputest, posStart, tempBuf2, 0, actualRec);
                      inp = new String(tempBuf2);
                      StaticVariabes_div.log("swb test length more inp" + inp, TAG1);

                  }
              }
          }
            try{
              //  inp=Blaster.ReadData(input, 1);
                StaticVariabes_div.log("swb inp"+inp, TAG1);
                String DevType = inp.substring(1, 4);
                String Dev = inp.substring(4, 8);
                char s14 = inp.charAt(8);
                char s58 = inp.charAt(9);
                char s912 = inp.charAt(10);
				/*char s1316 = inp.charAt(11);
				char s1720 = inp.charAt(12);
				char s2124 = inp.charAt(13);
				char s2528 = inp.charAt(14);
				char s2932 = inp.charAt(15);*/
                char F1 = inp.charAt(16);
                char F2 = inp.charAt(17);
				/*char F3 = inp.charAt(18);
				char F4 = inp.charAt(19);
				char E1 = inp.charAt(28);
				char E2 = inp.charAt(29);*/
                StaticVariabes_div.log("DevType" + DevType, TAG1);
                StaticVariabes_div.log("deviceType" + deviceType, TAG1);
                StaticVariabes_div.log("inpfull" + inp, TAG1);
                StaticVariabes_div.log("s14" + s14, TAG1);
                StaticVariabes_div.log("s58" + s58, TAG1);
                StaticVariabes_div.log("s912" + s912, TAG1);
                StaticVariabes_div.log("F1" + F1, TAG1);
                StaticVariabes_div.log("F2" + F2, TAG1);


                if(DevType!=null||!(DevType.equals("")))
                {
                    if (Dev!=null&&Dev.equals(devno)) {
                        powerstatus=false;
                        StaticVariabes_div.log("input full type - " + inp, TAG1);
                        switch (SwitchBoard_All_Models_Frag.Models.valueOf(deviceType)) {
                            case S010:
                                if(DevType.equals("001")){
                                    StaticVariabes_div.log("device type status-S010"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j,TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;
                            case SLT1:
                                if(DevType.equals("017")){
                                    StaticVariabes_div.log("device type status-SLT1"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j,TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;
                            case S020:
                                if(DevType.equals("002")){
                                    StaticVariabes_div.log("device type status-S020"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j,TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;

                            case S110:
                                if(DevType.equals("063")){
                                    StaticVariabes_div.log("device type status-S020"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j,TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;


                            case S030:
                                if(DevType.equals("003")){
                                    StaticVariabes_div.log("device type status-S030"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j, TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;

                            case S120:
                                if(DevType.equals("062")){
                                    StaticVariabes_div.log("device type status-S030"+"device status"+s14, TAG1);
                                    for(int j=0;j<bulbstate1_4.length;j++){
                                        StaticVariabes_div.log(bulbstate1_4[j]+"bulbstate1_4"+j, TAG1);
                                    }
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                }
                                break;



                            case S060:
                                if(DevType.equals("005")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                }
                                break;

                            case S080:
                                if(DevType.equals("006")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                }
                                break;

                            case S160:
                                if(DevType.equals("064")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                }
                                break;

                            /*case S120:
                                if(DevType.equals("007")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                    switchStatus(s912, bulbstate9_12);
                                    switchStatusUi9_12();
                                }
                                break;
*/

                            case S021:
                                if(DevType.equals("008")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    if(fanStatus(F1,0))
                                    {
                                        updateFanState(fn_Status[0], fn_Speed[0], img_fan );
                                    }
                                }
                                break;

                            case S031:
                                if(DevType.equals("009")){
                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    if(fanStatus(F1,0))
                                    {
                                        updateFanState(fn_Status[0], fn_Speed[0], img_fan );
                                    }
                                }
                                break;

                            case S051:
                                if(DevType.equals("011")){
                                    StaticVariabes_div.log("device type status-S051"+"device status"+s14, TAG1);

                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                    //fanStatus(F1,1);
                                    if(fanStatus(F1,0))
                                    {
                                        updateFanState(fn_Status[0], fn_Speed[0], img_fan );
                                    }

                                }
                                break;

                            case S141:
                                if(DevType.equals("061")){
                                    StaticVariabes_div.log("device type status-S141"+"device status"+s14, TAG1);

                                    switchStatus(s14, bulbstate1_4);
                                    switchStatusUi1_4();
                                    switchStatus(s58, bulbstate5_8);
                                    switchStatusUi5_8();
                                    //fanStatus(F1,1);
                                    if(fanStatus(F1,0))
                                    {
                                        updateFanState(fn_Status[0], fn_Speed[0], img_fan );
                                    }

                                }
                                break;

                           default:
                                System.out.println("Not matching");
                        }
                    }


                    //logTimeStamp("After executing");
                }
            }catch(Exception e){

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),"Invalid data recieved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{
           // ButtonOut("920");
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid data length recieved",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    boolean fanStatus(char c, int fanNo) {

        switch (c) {

            case '1': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 1))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 1;
                    return true;
                }
                break;
            }
            case '2': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 2))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 2;
                    return true;
                }
                break;
            }
            case '3': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 3))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 3;
                    return true;
                }
                break;
            }
            case '4': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 4))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 4;
                    return true;
                }
                break;
            }
            case '5': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 5))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 5;
                    return true;
                }
                break;
            }
            case '6': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 6))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 6;
                    return true;
                }
                break;
            }
            case '7': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 7))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 7;
                    return true;
                }
                break;
            }
            case '8': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 8))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 8;
                    return true;
                }
                break;
            }
            case '9': {
                if((fn_Status[fanNo]!=true) || (fn_Speed[fanNo] != 9))
                {
                    fn_Status[fanNo] = true;
                    fn_Speed[fanNo] = 9;
                    return true;
                }
                break;
            }
            case 'A': {
                if((fn_Status[fanNo]!=true))
                {
                    fn_Status[fanNo] = true;
                    return true;
                }
                break;
            }
            case 'B': {

                if((fn_Status[fanNo]!=false))
                {
                    fn_Status[fanNo] = false;
                    return true;
                }
                break;
            }
            case 'C': {
                break;
            }
            case 'D': {
                break;
            }
            case 'E': {
                break;
            }
            case 'F': {
                break;
            }
            case '0': {
                if((fn_Status[fanNo]!=false))
                {
                    fn_Status[fanNo] = false;
                    return true;
                }
                break;
            }
            default: {
                System.out.println("Enter a valid value.");
            }
        } // END of switch
        return false;
    }

    void updateFanState(boolean fanState, final int fanSpeed, final ImageView fanImageView )
    {
        if (fanState) {
            fBool = true;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    btn_fan.setBackgroundResource(R.mipmap.fan02);
                }
            });
        } else {
            fBool = false;
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    btn_fan.setBackgroundResource(R.mipmap.fan01);
                    fanImageView.setBackgroundResource(fState[0]);
                }
            });
        }

        if (fBool)
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    fanImageView.setBackgroundResource(fState[fanSpeed]);
                }
            });
    }

    // ................................................................................................
    void switchStatus(char c, boolean bsa[]) {

        switch (c) {

            case '1': {

                bsa[0] = true;
                bsa[1] = false;
                bsa[2] = false;
                bsa[3] = false;
                break;
            }
            case '2': {

                bsa[0] = false;
                bsa[1] = true;
                bsa[2] = false;
                bsa[3] = false;
                break;
            }
            case '3': {

                bsa[0] = true;
                bsa[1] = true;
                bsa[2] = false;
                bsa[3] = false;
                break;
            }
            case '4': {

                bsa[0] = false;
                bsa[1] = false;
                bsa[2] = true;
                bsa[3] = false;
                break;
            }
            case '5': {
                bsa[0] = true;
                bsa[1] = false;
                bsa[2] = true;
                bsa[3] = false;
                break;
            }
            case '6': {
                bsa[0] = false;
                bsa[1] = true;
                bsa[2] = true;
                bsa[3] = false;
                break;
            }
            case '7': {
                bsa[0] = true;
                bsa[1] = true;
                bsa[2] = true;
                bsa[3] = false;
                break;
            }
            case '8': {
                bsa[0] = false;
                bsa[1] = false;
                bsa[2] = false;
                bsa[3] = true;
                break;
            }
            case '9': {
                bsa[0] = true;
                bsa[1] = false;
                bsa[2] = false;
                bsa[3] = true;
                break;
            }
            case 'A': {
                bsa[0] = false;
                bsa[1] = true;
                bsa[2] = false;
                bsa[3] = true;
                break;
            }
            case 'B': {
                bsa[0] = true;
                bsa[1] = true;
                bsa[2] = false;
                bsa[3] = true;
                break;
            }
            case 'C': {
                bsa[0] = false;
                bsa[1] = false;
                bsa[2] = true;
                bsa[3] = true;
                break;
            }
            case 'D': {
                bsa[0] = true;
                bsa[1] = false;
                bsa[2] = true;
                bsa[3] = true;
                break;
            }
            case 'E': {
                bsa[0] = false;
                bsa[1] = true;
                bsa[2] = true;
                bsa[3] = true;
                break;
            }
            case 'F': {
                bsa[0] = true;
                bsa[1] = true;
                bsa[2] = true;
                bsa[3] = true;
                break;
            }
            case '0': {
                bsa[0] = false;
                bsa[1] = false;
                bsa[2] = false;
                bsa[3] = false;
                break;
            }
            default: {
                System.out.println("Enter a valid value.");
            }
        } // END of switch
        //return bsa;
    }

    // .........................................................................................
    void switchStatusUi1_4() {
    int m=0;
        for (int j = 0; j < 4; j++) {
            if(bulbiconarr1_4[j]!=null){

                StaticVariabes_div.log("switch bulbstate1_4"+bulbstate1_4[j]+"  j"+j+"   bulbstateui1_4"+bulbstateui1_4[j]+"    bstateui1_4"+bstateui1_4[j], TAG1);
                updateUI2(bulbstate1_4[j], j, bulbstateui1_4[j],
                        bulbicontypearr1_4[j], bulbiconarr1_4[j], bstateui1_4,m);
                m++;
            }
        }
    }

    void switchStatusUi5_8() {
        int m=4;
        for (int j = 0; j < 4; j++) {
            if(bulbiconarr5_8[j]!=null)
                updateUI2(bulbstate5_8[j], j, bulbstateui5_8[j],
                        bulbicontypearr5_8[j], bulbiconarr5_8[j], bstateui5_8,m);
            m++;
        }
    }

    void switchStatusUi9_12() {
        int m=8;
        for (int j = 0; j < 4; j++) {
            if(bulbiconarr9_12[j]!=null)
                updateUI2(bulbstate9_12[j], j, bulbstateui9_12[j],
                        bulbicontypearr9_12[j], bulbiconarr9_12[j], bstateui9_12,m);
            m++;
        }
    }

    // ........................................................................................................
    void updateUI2(boolean b, final int k, final boolean bulbstateui,
                   final String bulbicontypearr, final Button bulbiconarr,
                   final boolean bstateui[],int m) {
        if (b) {
            if (!bstateui[k]) {
                seticonsON(bulbicontypearr, bulbiconarr,""+m);
            }
            bstateui[k] = true;
        }

        else {

            if (bstateui[k]) {
                seticonsOFF(bulbicontypearr, bulbiconarr,""+m);
            }
            bstateui[k] = false;
        }

    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

 /*   @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }*/

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private boolean _hasLoadedOnce= false; // your boolean field

    @Override
    public void setUserVisibleHint(boolean isFragmentVisible_) {
        super.setUserVisibleHint(true);


        if (this.isVisible()) {
            // we check that the fragment is becoming visible
            if (isFragmentVisible_ && !_hasLoadedOnce) {
              //  tvtest.setText("hi");
                ButtonOut("920");
                _hasLoadedOnce = true;
            }
        }
    }


    ///////////////////////////////////////////////////////////////////////////////

    public class Image_Icon_Adapter extends BaseAdapter {
        private Context mContext;
        private LayoutInflater mInflater;
        Integer mThumbIds[];
        String categoryContent[];

        public Image_Icon_Adapter(Context c,Integer icon_imgs[],String icon_txts[]) {
            mInflater = LayoutInflater.from(c);
            mContext = c;
            mThumbIds=icon_imgs;
            categoryContent=icon_txts;

        }
        public int getCount() {
            return mThumbIds.length;
        }
        public Object getItem(int position) {
            return null;
        }
        public long getItemId(int position) {
            return 0;
        }
        // create a new ImageView for each item referenced by the
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {  // if it's not recycled,
                convertView = mInflater.inflate(R.layout.grid_custom_item, null);
                convertView.setLayoutParams(new GridView.LayoutParams(160, 160));
                holder = new ViewHolder();
                holder.title = (TextView) convertView.findViewById(R.id.categoryText);
                holder.icon = (ImageView )convertView.findViewById(R.id.categoryimage);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.icon.setAdjustViewBounds(true);
            holder.icon.setScaleType(ImageView.ScaleType.CENTER_CROP);
            holder.icon.setPadding(2, 2, 2, 2);
            holder.title.setText(categoryContent[position]);
            holder.icon.setImageResource(mThumbIds[position]);
            return convertView;
        }
        class ViewHolder {
            TextView title;
            ImageView icon;
        }


    }

    public void onResume() {
        super.onResume();
        Log.i(TAG1, "Setting screen name: " + name);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }

}
