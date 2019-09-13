package edisonbro.com.edisonbroautomation.Devices;


/**
 *  FILENAME: Rgb_Group_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Group Fragment to operate selected group rgb Devices together using master layout and
 *  option to operate each device in group individually .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *   functions:
 *  transmitdata : To transmit rgb data through tcp.
 *  Track_button_event : To track event on button click.
 *  changebutton :  To update response of rgb in ui
 *  receiveddata : To receive data from tcp.
 *  Datain : To process the data received from tcp.
 *  popup : popup to display info.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

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
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Rgb_Group_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Rgb_Group_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Rgb_Group_Frag extends Fragment implements TcpTransfer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;



    private static final String TAG1="RGB Group- ";
    //**********************************************

    int count=0;
    int ispeed=121;
    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01",devtypesett,model,Roomname;
    ///////////////////////////////////////////////////////////////////
    private static final int READ_BYTE = 1, READ_LINE = 2, ServStatus = 3
    , signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7,  UPDATE=8;
    //**********************************************
    int sl;
    boolean rgbstatus=false;
    boolean groupmaster=false;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    View view;
    CombFrag combf;
    private Tracker mTracker;
    String name ="RGB Group";
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************
    private A_rc colorPicker;
    private Button btn_Red, btn_Green, btn_Blue, btn_Pink, btn_White,
            btn_orange,btn_Flash, btn_Strobe, btn_Fade, btn_Smooth, btn_Speedp,btn_Speedm,btn_on,btn_off;
    private TextView rgbvalue,tv_speedval,tvtest;
    SeekBar sk;
    ImageView rgb_img_grp;




    public Rgb_Group_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Rgb_Group_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Rgb_Group_Frag newInstance(String param1, String param2) {
        Rgb_Group_Frag fragment = new Rgb_Group_Frag();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view= inflater.inflate(R.layout.rgb_group_frag, container, false);
        rgb_img_grp=(ImageView) view.findViewById(R.id.img_grp);
        btn_Red=(Button) view.findViewById(R.id.btn_red);
        btn_Green=(Button) view.findViewById(R.id.btn_green);
        btn_Blue=(Button) view.findViewById(R.id.btn_blue);
        btn_Pink=(Button) view.findViewById(R.id.btn_purple);
        btn_White=(Button) view.findViewById(R.id.btn_white);
        btn_orange=(Button) view.findViewById(R.id.btn_orange);

        btn_Fade=(Button) view.findViewById(R.id.btn_fade);
        btn_Flash=(Button) view.findViewById(R.id.btn_flash);
        btn_Smooth=(Button) view.findViewById(R.id.btn_smooth);
        btn_Strobe=(Button) view.findViewById(R.id.btn_strobe);
        btn_Speedp=(Button) view.findViewById(R.id.btn_speedup);
        btn_Speedm=(Button) view.findViewById(R.id.btn_speeddown);

        btn_on=(Button) view.findViewById(R.id.btn_rgbon);
        btn_off=(Button) view.findViewById(R.id.btn_rgboff);

        rgbvalue = (TextView) view.findViewById(R.id.textView1);
        colorPicker = (A_rc) view.findViewById(R.id.colorPicker);


        tv_speedval=(TextView)  view.findViewById(R.id.tv_speedvalue);

        Typeface led_font = Typeface.createFromAsset(getActivity().getAssets(), "fonts/ledfont.ttf");
        tv_speedval.setTypeface(led_font);
        tv_speedval.setTextSize(16.0f);

        sk = (SeekBar) view.findViewById(R.id.brightseekBar);
        sk.setMax(10);


        devno= StaticVariabes_div.devnumber;
        while (devno.length() < 4)devno = "0" + devno;
        roomno= StaticVariabes_div.room_n;
        while(roomno.length()<2)roomno="0"+roomno;

        combf = ((CombFrag) this.getParentFragment());

        if(StaticVariabes_div.load_group_master) {
            if (StaticVariabes_div.rgbpst == 0) {
                rgbstatus = true;
                groupmaster = true;
                rgb_img_grp.setVisibility(View.VISIBLE);
                rgb_img_grp.setImageResource(R.drawable.group_icon_rgb);
                StaticVariabes_div.loaded_lay_Multiple = false;
                combf.feature_sett_enab_disable(false);
            } else {
                rgbstatus = false;
                groupmaster = false;
                rgb_img_grp.setVisibility(View.INVISIBLE);
                StaticVariabes_div.loaded_lay_Multiple = false;
                combf.feature_sett_enab_disable(true);
            }
        }else{
            rgbstatus = false;
            groupmaster = false;
            rgb_img_grp.setVisibility(View.INVISIBLE);
            StaticVariabes_div.loaded_lay_Multiple = false;
            combf.feature_sett_enab_disable(true);
        }
        tvtest=(TextView) view.findViewById(R.id.tvtest);

        if(StaticVariabes_div.dev_name!=null)
            tvtest.setText(StaticVariabes_div.dev_name);

        StaticVariabes_div.dev_typ_num="021";

        Tcp_con mTcp = new Tcp_con(this);

        if(Tcp_con.isClientStarted){

            // receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            // receiveddata(ServStatus,StaticStatus.Server_status,null);
            transmitdata(920,000,000,000,"A");

        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        mn_nav_obj=(Main_Navigation_Activity) getActivity();

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        btn_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                transmitdata(102,000,000,000,"A");
                Track_button_event("RGB Group","RGB ON","RGB Shortclick");
            }
        })	;

        btn_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                transmitdata(103,000,000,000,"A");
                Track_button_event("RGB Group","RGB OFF","RGB Shortclick");
            }
        })	;


        btn_Flash.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    rgbvalue.setText("Flash");
                    rgbvalue.setBackgroundColor(Color.BLACK);
                    transmitdata(104,000,000,000,"A");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Flash","Rgb Shortclick");
            }
        })	;

        btn_Strobe.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    rgbvalue.setText("Strobe");
                    rgbvalue.setBackgroundColor(Color.BLACK);
                    transmitdata(105,000,000,000,"A");

                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Strobe","Rgb Shortclick");
            }
        })	;

        btn_Fade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    rgbvalue.setText("Fade");
                    rgbvalue.setBackgroundColor(Color.BLACK);
                    transmitdata(107,000,000,000,"A");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Fade","Rgb Shortclick");
            }
        })	;

        btn_Smooth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    rgbvalue.setText("Smooth");
                    rgbvalue.setBackgroundColor(Color.BLACK);
                    transmitdata(106,000,000,000,"A");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Smooth","Rgb Shortclick");
            }
        })	;

        btn_Red.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.RED);
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.RED);
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Red","Rgb Shortclick");
            }
        });

        btn_Green.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.rgb(0,255, 0));
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(0,255, 0));
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Green","Rgb Shortclick");
            }
        });

        btn_Blue.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.rgb(0, 0, 255));
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(0, 0, 255));
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Blue","Rgb Shortclick");
            }
        });


        btn_Pink.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.rgb(255, 0, 128));
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Pink","Rgb Shortclick");
            }
        });

        btn_White.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.WHITE);
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","White","Rgb Shortclick");
            }
        });

        btn_orange.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rgbstatus){
                    colorPicker.setColor(Color.rgb(255, 165, 0));
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                    transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                    //transmit(0,c.red(color),c.green(color),c.blue(color));
                }else{
                    Toast.makeText(getActivity().getApplicationContext(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Orange","Rgb Shortclick");
            }
        });



        btn_Speedp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rgbstatus){
                    if(ispeed<130){
                        ispeed++;
                    }
                    // transmit(ispeed);
                    transmitdata(ispeed,000,000,000,"A");
                    tv_speedval.setText(String.valueOf(ispeed-120));
                }else{
                    Toast.makeText(getActivity(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Speed up","Rgb Shortclick");
            }
        })	;
        btn_Speedm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rgbstatus){
                    if(ispeed>121){
                        ispeed--;
                    }
                    //transmit(ispeed);
                    transmitdata(ispeed,000,000,000,"A");
                    tv_speedval.setText(String.valueOf(ispeed-120));
                }else{
                    Toast.makeText(getActivity(), "please switch on the device first", Toast.LENGTH_SHORT).show();
                }
                Track_button_event("RGB Group","Speed down","Rgb Shortclick");
            }
        })	;

        colorPicker.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                int color = colorPicker.getColor();

                String rgbString = "R:" + Color.red(color) + " G:"
                        + Color.green(color) + " B:" + Color.blue(color);
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
                transmitdata(0,Color.red(color),Color.green(color),Color.blue(color),"B");
                Track_button_event("RGB Group","Color picker","Rgb Shortclick");

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
                if(count<1)count=1;
                //count=count/10;
                if(count<1)count=1;
                transmitdata(count+130,000,000,000,"A");
                Track_button_event("RGB Group","Brightness Operation","Rgb Shortclick");

            }
        });
        return  view;
    }


    //////////////////////////////////////////////////////////////////////////////////////
    void transmitdata(final int val, final int rc,final int gc,final int bc,String type)
    {

        String str=null;
        String str2 = ""+val;
        while(str2.length()<3)str2="0"+str2;
        String redStr = ""+rc;
        while(redStr.length()<3)redStr="0"+redStr;
        StaticVariabes_div.log("redStr"+redStr, TAG1);
        String greenStr = ""+gc;
        while(greenStr.length()<3)greenStr="0"+greenStr;
        StaticVariabes_div.log("greenStr"+greenStr, TAG1);
        String blueStr = ""+bc;
        while(blueStr.length()<3)blueStr="0"+blueStr;
        StaticVariabes_div.log("blueStr"+blueStr, TAG1);
        StaticVariabes_div.log("Str"+str2, TAG1);
        StaticVariabes_div.log("devno"+devno, TAG1);

        while(devno.length()<4)devno="0"+devno;

        if(type.equals("A")) {

            if(StaticVariabes_div.rgbpst==0&&StaticVariabes_div.load_group_master) {
                str = "0" + "10" + StaticVariabes_div.rgbgrpsarr[0] + "0000" + roomno+val+"000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            }else{
                str = "0" + "01" + "000" + devno + roomno + str2 + "000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            }

        }else {


            if(StaticVariabes_div.rgbpst==0&&StaticVariabes_div.load_group_master) {

                str = "0" + "10" + StaticVariabes_div.rgbgrpsarr[0] + "0000"  + roomno+"112"+redStr+greenStr+blueStr+"000000";
                StaticVariabes_div.log("str"+str, TAG1);

            }else{
                str = "0" + "01" + "000" + devno + roomno+"112"+redStr+greenStr+blueStr+"000000";
                StaticVariabes_div.log("str"+str, TAG1);
            }


        }

       // byte[] op= Blaster.WriteData(str, 1);
        byte[] op=str.getBytes();
        byte[] result = new byte[32];
        result[0] = (byte) '*';
        result[31] = (byte) '#';
        for (int i = 1; i < 31; i++)
            result[i] = op[(i - 1)];
        StaticVariabes_div.log("bout" + result + "$$$" + val, TAG1);
        Tcp_con.WriteBytes(result);

    }
    public void Track_button_event(String catagoryname,String actionname,String labelname){
        Tracker t = ((Edisonbro_AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(catagoryname)
                .setAction(actionname)
                .setLabel(labelname)
                .build());
    }

    @Override
    public void read(final int type, final String stringData, final byte[] byteData)
    {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               try{
                receiveddata(type,stringData,byteData);
                }catch(Exception e){
                e.printStackTrace();
                }
            }
        });
    }

    public void receiveddata(int msg,String data,byte[] bytestatus){

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                final String readMessage = new String(readBuf, 0,readBuf.length);
                StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);
                DataIn(readBuf);

                break;
            case READ_LINE:
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 =data;
                if(readMessage2.equals("*OK#")){
                    mn_nav_obj.serv_status(true);
                }else{
                    combf.timerresponse(readMessage2);
                }
                break;
            case ServStatus:
                final String ServerStatusB =data;
                StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
                if(ServerStatusB!=null){
                    if (ServerStatusB.equals("TRUE")) {
                        StaticStatus.Server_status_bool=true;
                        statusserv = true;
                        servpreviousstate="TRUE";
                        nonetwork=false;
                        //  ButtonOut("920");

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
               // StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
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
                StaticVariabes_div.log("erruser swb" + erruser, TAG1);

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

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity().getApplicationContext());

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
//$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  TO CHECK INPUT RESPONSE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$

    void DataIn(byte byt[])
    {

        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));
            inp=new String(input);//Blaster.ReadData(input, 1);
            String DevType = inp.substring(1, 4);
            String Dev = inp.substring(4, 8);
            StaticVariabes_div.log("input"+byt.length, TAG1);
            String DS =inp.substring(8, 10);

            final String brigval =inp.substring(10, 11);
            final String speedval =inp.substring(11, 12);
            final String effectsval =inp.substring(12, 13);
            StaticVariabes_div.log("DS"+DS+"Devno"+Dev+"devno"+devno+"brigval"+brigval+"speedval"+speedval+"inp"+inp, TAG1);

            String redval="00",greenval="00",blueval="00";
            if(effectsval.equals("0")){
                redval =inp.substring(13, 15);
                greenval =inp.substring(15, 17);
                blueval =inp.substring(17, 19);

                StaticVariabes_div.log("redval "+redval+" greenval "+greenval+" blueval "+blueval, TAG1);



            }

            if(DevType.equals("021")){
                if(Dev.equals(devno)){

                    if(DS.equals("01"))
                    {
                        final String finalRedval = redval;
                        final String finalGreenval = greenval;
                        final String finalBlueval = blueval;
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                rgbstatus=true;
                                if(brigval.equals("A")){
                                    sk.setProgress(10);
                                }else if(brigval.equals("F")) {
                                    StaticVariabes_div.log("bright", TAG1);

                                } else {
                                    int brightint = 0;
                                    try {
                                        brightint = Integer.parseInt(brigval);
                                        sk.setProgress(brightint);
                                        StaticVariabes_div.log("brightint"+brightint, TAG1);
                                    } catch (Exception e) {

                                    }

                                }

                                if(speedval.equals("A")){
                                    tv_speedval.setText("10");
                                    ispeed=120+10;

                                }else if(speedval.equals("F")||speedval.equals("0")) {
                                    StaticVariabes_div.log("speed", TAG1);
                                    tv_speedval.setText("0");

                                } else {
                                    int speedint = 0;
                                    try {
                                        speedint = Integer.parseInt(speedval);
                                        tv_speedval.setText(""+speedint);
                                        ispeed=120+speedint;
                                        StaticVariabes_div.log("speedint"+speedint, TAG1);
                                    } catch (Exception e) {

                                    }
                                }



                                if (effectsval.equals("1")) {
                                    rgbvalue.setText("");
                                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                                    changebutton(true,false,false,false);

                                } else if (effectsval.equals("2")) {
                                    rgbvalue.setText("");
                                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                                    changebutton(false,true,false,false);

                                } else if (effectsval.equals("3")) {
                                    rgbvalue.setText("");
                                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                                    changebutton(false,false,true,false);


                                }else if (effectsval.equals("4")) {
                                    rgbvalue.setText("");
                                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                                    changebutton(false,false,false,true);

                                }else if (effectsval.equals("0")){
                                    rgbvalue.setText("");
                                    rgbvalue.setBackgroundColor(Color.parseColor("#"+ finalRedval + finalGreenval + finalBlueval));
                                    changebutton(false,false,false,false);
                                }

                            }
                        });




                    }else if(DS.equals("02"))
                    {
                        getActivity().runOnUiThread(new Runnable() {
                            public void run() {
                                rgbstatus=false;
                            }
                        });
                    }



                }
            }
        }
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


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

   /* @Override
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

    public void onResume() {
        super.onResume();
        Log.i(TAG1, "Setting screen name: " + name);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
