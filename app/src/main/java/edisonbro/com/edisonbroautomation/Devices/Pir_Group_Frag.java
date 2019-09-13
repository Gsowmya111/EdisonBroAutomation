package edisonbro.com.edisonbroautomation.Devices;

/**
 *  FILENAME: Pir_Group_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Group Fragment to operate selected group pir Devices together using master layout and
 *  option to operate each device in group individually .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *  functions:
 *  transmitdata : To transmit curtain data through tcp.
 *  transmitdata_Sheer : To transmit sheer data through tcp.
 *  Track_button_event : To track event on button click.
 *  Statusupdatelight : To update light sensor status.
 *  Statusupdate2 : To update pir sensor status.
 *  Datain : To process the data received from tcp.
 *  popup : popup to display info.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Arrays;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pir_Group_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pir_Group_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pir_Group_Frag extends Fragment  implements TcpTransfer,View.OnClickListener{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG1="Pir Group- ";
    //************************************************************************

    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01", devtypesett,model,Roomname;
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    //************************************************************************

    private static final int READ_BYTE = 1, READ_LINE = 2
   , ServStatus = 3,signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7, UPDATE=8;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    private Tracker mTracker;
    String name ="PIR Group";
    CombFrag combf;
    //********************************************************************************

    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //******************************************************************************

    View view;
    public String pirgrouppost="0";
    TextView tvtest;
    boolean groupmaster=false;
    ImageView pir_img;
    //*******************************************************************************
    RadioButton rb_pir,rb_ligsensor;
    TextView tv_lightvalue,tv_priority;
    LinearLayout lay_pir,lay_lig;
    ImageView imgpir,imglig,pir_img_grp;
    Button btn_lig_enable,btn_lig_disable,btn_pir_enable,btn_pir_disable,btn_timesetpir;
    String timevalue,timeva,lightvalue;
    EditText ed_timevalue;
    private EditText et_time;
    private Button bt_one,bt_two,bt_three,bt_four,bt_five,bt_six,bt_seven,bt_eight,bt_nine,bt_zero,bt_cancel,bt_clear,bt_set;
    private AlertDialog dialog;

    //***********************************************



    public Pir_Group_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pir_Group_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Pir_Group_Frag newInstance(String param1, String param2) {
        Pir_Group_Frag fragment = new Pir_Group_Frag();
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
        view= inflater.inflate(R.layout.pir_group_frag, container, false);
        pir_img_grp= (ImageView) view.findViewById(R.id.img_pir);

        tv_lightvalue=(TextView) view.findViewById(R.id.et_lightval);
        imgpir=(ImageView) view.findViewById(R.id.imgpir);
        imglig=(ImageView) view.findViewById(R.id.imglightsensr);
        btn_lig_enable=(Button) view.findViewById(R.id.b_lig_enable);
        btn_lig_disable=(Button) view.findViewById(R.id.b_lig_disable);
        btn_pir_enable=(Button) view.findViewById(R.id.b_ena_pir);
        btn_pir_disable=(Button) view.findViewById(R.id.b_dis_pir);

        rb_pir=(RadioButton) view.findViewById(R.id.rb_pir);
        rb_ligsensor=(RadioButton) view.findViewById(R.id.rb_ligsensor);


        lay_pir=(LinearLayout) view.findViewById(R.id.laypir);
        lay_lig=(LinearLayout) view.findViewById(R.id.laylig);

        btn_timesetpir=(Button) view.findViewById(R.id.pirsettime2);

        tvtest= (TextView) view.findViewById(R.id.tvtest);

        StaticVariabes_div.dev_typ_num="718";
        StaticVariabes_div.loaded_lay_Multiple=false;

        StaticVariabes_div.log("devtyp" + StaticVariabes_div.devtyp, TAG1);


        if(StaticVariabes_div.dev_name!=null) {
            tvtest.setText(StaticVariabes_div.dev_name);
        }


        //^^^^Time Pir BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        btn_timesetpir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Timervalue(timeva);
                timesett_pirpopup(timeva);
            }
        });
        //^^^^Enable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_pir_enable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("909","A");
                Track_button_event("PIR Group","MotionSensor ON","MotionSensor Shortclick");

            }
        });

        //^^^^Disable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_pir_disable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("910","A");
                Track_button_event("PIR Group","MotionSensor OFF","MotionSensor Shortclick");

            }
        });

        //^^^^light Enable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_lig_enable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("101","A");
                Track_button_event("PIR Group","LightSensor ON","MotionSensor Shortclick");

            }
        });

        //^^^^light Disable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_lig_disable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("102","A");
                Track_button_event("PIR Group","LightSensor OFF","MotionSensor Shortclick");

            }
        });
        //^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        rb_pir.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                if(rb_pir.isChecked())
                {
                    transmitdata("1","C");
                    rb_ligsensor.setChecked(false);
                }
            }
        });

        rb_ligsensor.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if(rb_ligsensor.isChecked())
                {
                    transmitdata("2","C");
                    rb_pir.setChecked(false);
                }
            }
        });


        combf = ((CombFrag) this.getParentFragment());
        pirgrouppost=""+StaticVariabes_div.pirpst;

      //  if(pirgrouppost!=null){

        if(StaticVariabes_div.load_group_master) {
            if (StaticVariabes_div.pirpst == 0) {
                //  tvtest.setText("Group Master");
                groupmaster = true;
                pir_img_grp.setVisibility(View.VISIBLE);
                pir_img_grp.setImageResource(R.drawable.group_icon_pir);
                combf.feature_sett_enab_disable(false);
                StaticVariabes_div.loaded_lay_Multiple = false;


                    StaticVariabes_div.dev_typ_num = "718";
                imgpir.setImageResource(R.drawable.motion);

            } else {
                // tvtest.setText("Group Slave - "+pirgrouppost);
                groupmaster = false;
                pir_img_grp.setVisibility(View.INVISIBLE);
                combf.feature_sett_enab_disable(true);
                StaticVariabes_div.loaded_lay_Multiple = false;
                //pir_img.setImageResource(R.drawable.dim_level0);
                if(StaticVariabes_div.devtyp.equals("WPD1")) {
                    StaticVariabes_div.dev_typ_num = "720";
                    imgpir.setImageResource(R.drawable.direc_pir_sen_off);
                }else{
                    StaticVariabes_div.dev_typ_num = "718";
                    imgpir.setImageResource(R.drawable.motion);
                }
            }
            // }
        }else{
            groupmaster = false;
            pir_img_grp.setVisibility(View.INVISIBLE);
            combf.feature_sett_enab_disable(true);
            StaticVariabes_div.loaded_lay_Multiple = false;
            if(StaticVariabes_div.devtyp.equals("WPD1")) {
                StaticVariabes_div.dev_typ_num = "720";
                imgpir.setImageResource(R.drawable.direc_pir_sen_off);
            }else{
                StaticVariabes_div.dev_typ_num = "718";
                imgpir.setImageResource(R.drawable.motion);
            }
        }

        Toast.makeText(getActivity().getApplicationContext(),"pirgrouppost"+pirgrouppost,Toast.LENGTH_SHORT).show();



        Tcp_con mTcp = new Tcp_con(this);

        if(Tcp_con.isClientStarted){
            //   Tcp_con.stacontxt = getActivity().getApplicationContext();
            //   Tcp_con.ins();

            // receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            // receiveddata(ServStatus,StaticStatus.Server_status,null);
            //  Tcp_con.stopClient();

            //if(StaticVariabes_div.pirpst!=0)
            transmitdata("920","A");

        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        mn_nav_obj=(Main_Navigation_Activity) getActivity();


        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();


        return  view;
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
    public void timesett_pirpopup(String prevtext) {


        LayoutInflater inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.pirtimesett_dialog, null);
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setView(view);
        et_time =(EditText) alertLayout.findViewById(R.id.et_time);
        bt_one =(Button) alertLayout.findViewById(R.id.bt_one);
        bt_two = (Button)alertLayout.findViewById(R.id.bt_two);
        bt_three =(Button) alertLayout.findViewById(R.id.bt_three);
        bt_four = (Button)alertLayout.findViewById(R.id.bt_four);
        bt_five =(Button) alertLayout.findViewById(R.id.bt_five);
        bt_six = (Button)alertLayout.findViewById(R.id.bt_six);
        bt_seven = (Button)alertLayout.findViewById(R.id.bt_seven);
        bt_eight = (Button)alertLayout.findViewById(R.id.bt_eight);
        bt_nine = (Button)(Button)alertLayout.findViewById(R.id.bt_nine);
        bt_zero =(Button) alertLayout.findViewById(R.id.bt_zero);
        bt_cancel = (Button)alertLayout.findViewById(R.id.bt_cancel);
        bt_clear = (Button)alertLayout.findViewById(R.id.bt_clear);
        bt_set = (Button) alertLayout.findViewById(R.id.bt_set);

        bt_one.setOnClickListener(this);
        bt_two.setOnClickListener(this);
        bt_three.setOnClickListener(this);
        bt_four.setOnClickListener(this);
        bt_five.setOnClickListener(this);
        bt_six.setOnClickListener(this);
        bt_seven.setOnClickListener(this);
        bt_eight.setOnClickListener(this);
        bt_nine.setOnClickListener(this);
        bt_zero.setOnClickListener(this);
        bt_cancel.setOnClickListener(this);
        bt_clear.setOnClickListener(this);
        bt_set.setOnClickListener(this);


        TextView title = new TextView(getActivity());
        // You Can Customise your Title here
        title.setText("Set Time");
        title.setBackgroundColor(Color.parseColor("#0E4D92"));
        title.setPadding(10, 10, 10, 10);
        title.setGravity(Gravity.CENTER);
        title.setTextColor(Color.WHITE);
        title.setTextSize(20);

        alert.setCustomTitle(title);
        alert.setView(alertLayout);
        alert.setCancelable(false);
        dialog = alert.create();
        dialog.show();


        if(prevtext==null){
            prevtext="0000";
        }
        et_time.setText(prevtext);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_one:
                et_time.append("1");
                break;
            case R.id.bt_two:
                et_time.append("2");
                break;
            case R.id.bt_three:
                et_time.append("3");
                break;
            case R.id.bt_four:
                et_time.append("4");
                break;
            case R.id.bt_five:
                et_time.append("5");
                break;
            case R.id.bt_six:
                et_time.append("6");
                break;
            case R.id.bt_seven:
                et_time.append("7");
                break;
            case R.id.bt_eight:
                et_time.append("8");
                break;
            case R.id.bt_nine:
                et_time.append("9");
                break;
            case R.id.bt_zero:
                et_time.append("0");
                break;
            case R.id.bt_cancel:
                dialog.cancel();
                break;
            case R.id.bt_clear:
                et_time.setText("");
                break;
            case R.id.bt_set:
                Track_button_event("PIR Group","MotionSensor change value","MotionSensor Shortclick");
                timevalue = et_time.getText().toString();
                //  tv.setText(data);
                while(timevalue.length()<4)timevalue="0"+timevalue;
                if(isValidtime(timevalue)){
                    StaticVariabes_div.log("timevalue"+timevalue+"length"+timevalue.length(), TAG1);

                    if(timevalue!=null)
                        transmitdata(timevalue,"B");

                    dialog.cancel();
                }else{
                    timesett_pirpopup(timevalue);
                    et_time.setError("Max Length 4digit");

                }
                break;
        }

    }


    private boolean isValidtime(String time) {
        if (time != null && time.length()<5) {

            return true;
        }
        return false;
    }

    void transmitdata(String val4,String type)
    {  String str=null;
       // val4=""+val3;
        devno=StaticVariabes_div.pirgrpsarr[StaticVariabes_div.pirpst];

        roomno= StaticVariabes_div.room_n;
        while(devno.length()<4)devno="0"+devno;

        while(roomno.length()<2)roomno="0"+roomno;

        pirgrouppost=""+StaticVariabes_div.pirpst;

        if(type.equals("A")) {
            while(val4.length()<3)val4="0"+val4;

            if (pirgrouppost.equals("0")&&StaticVariabes_div.load_group_master) {
                str = "0" + "13" + StaticVariabes_div.pirgrpsarr[0] + "0000" + roomno+val4+"000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            } else {

                str = "0" + "01" + "000" + devno + roomno+val4+"000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            }

        }  else if(type.equals("C")) {

        while(val4.length()<1)val4="0";


        if (pirgrouppost.equals("0")&&StaticVariabes_div.load_group_master) {
            str = "0" + "13" + StaticVariabes_div.pirgrpsarr[0] + "0000" + roomno+"103"+"0000"+val4+"0000000000";
            StaticVariabes_div.log("str" + str, TAG1);
        } else {
            str = "0" + "01" + "000" + devno + roomno+"103"+"0000"+val4+"0000000000";
            StaticVariabes_div.log("str" + str, TAG1);
        }


        StaticVariabes_div.log("str val"+str, TAG1);

    }else {

            while(val4.length()<4)val4="0"+val4;

            if (pirgrouppost.equals("0")&&StaticVariabes_div.load_group_master) {
                str = "0" + "13" + StaticVariabes_div.pirgrpsarr[0] + "0000" + roomno+"000"+val4+"00000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            } else {
                str = "0" + "01" + "000" + devno + roomno+"000"+val4+"00000000000";
                StaticVariabes_div.log("str" + str, TAG1);
            }

        }

       // byte[] op= Blaster.WriteData(str, 1);
        byte[] op=str.getBytes();
        byte[] result = new byte[32];
        result[0] = (byte) '*';
        result[31] = (byte) '#';
        for (int i = 1; i < 31; i++)
            result[i] = op[(i - 1)];
        StaticVariabes_div.log("bout" + result + "$$$" + val4, TAG1);
        Tcp_con.WriteBytes(result);


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

                    if(StaticVariabes_div.pirpst!=0)
                    transmitdata("920","A");
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

                        if(!StaticVariabes_div.load_group_master)
                        transmitdata("920","A");

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
              //  StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
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


    void DataIn(byte[] byt)
    {
        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));
            try{
                inp=new String(input);//Blaster.ReadData(input, 1);
                if(inp!=null){

                    StaticVariabes_div.log("input length"+byt.length+"input full  "+inp, TAG1);
                    String DevType = inp.substring(1, 4);
                    String Dev = inp.substring(4, 8);
                    String DS =inp.substring(8, 10);
                    String dval =inp.substring(8, 12);
                    final String priorityval =inp.substring(12, 13);
                    String lightenval =inp.substring(13, 14);
                    String lightval =inp.substring(14, 15);
                    char E1 = inp.charAt(28);
                    char E2 = inp.charAt(29);


                    StaticVariabes_div.log("DS"+DS+"Devno"+Dev+"devno"+devno+"DevType"+DevType, TAG1);
                    StaticVariabes_div.log("DS"+DS+"Devno"+Dev+"devno"+devno+"dval"+dval+"inp"+inp, TAG1);

                    StaticVariabes_div.log("only light val"+lightval, TAG1);

                    if(DevType.equals("718")||DevType.equals("720")){

                      //  if(Dev.equals(devno)){

                            if(StaticVariabes_div.load_group_master&&(pirgrouppost.equals("0"))) {

                                ArrayList list = new ArrayList(Arrays.asList(StaticVariabes_div.pirgrpsarr ));
                                String tempdev=""+Integer.parseInt(Dev);
                                StaticVariabes_div.log("contains-tempdev " + tempdev, TAG1);
                                if (list.contains(tempdev)) {
                                    StaticVariabes_div.log("contains- " + Dev, TAG1);
                                   // cur_sheer(DS,DevType);
                                    timeva=dval;
                                    lightvalue=lightval;
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            if(priorityval.equals("1")){
                                                rb_pir.setChecked(true);
                                                rb_ligsensor.setChecked(false);
                                            }else{
                                                rb_pir.setChecked(false);
                                                rb_ligsensor.setChecked(true);
                                            }
                                            tv_lightvalue.setText(lightvalue);
                                            btn_timesetpir.setText(timeva);

                                        }
                                    });

                                    StaticVariabes_div.log("inside ds"+E2, TAG1);
                                    switchstatus2("S"+E2);
                                    Statusupdatelight(lightenval);

                                    String E=String.valueOf(E2);

                                    if((lightenval.equals("0"))||(E.equals("0"))|| (E.equals("1"))||(E.equals("4"))||E.equals("5")){
                                        invisible();
                                    }else{
                                        visible();
                                    }

                                }

                            }else{
                            //if((StaticVariabes_div.pirpst)!=0){
                               // powerstatus=false;

                                if(Dev.equals(devno)) {
                                    timeva = dval;
                                    lightvalue = lightval;
                                    getActivity().runOnUiThread(new Runnable() {

                                        @Override
                                        public void run() {
                                            if (priorityval.equals("1")) {
                                                rb_pir.setChecked(true);
                                                rb_ligsensor.setChecked(false);
                                            } else {
                                                rb_pir.setChecked(false);
                                                rb_ligsensor.setChecked(true);
                                            }
                                            tv_lightvalue.setText(lightvalue);
                                            btn_timesetpir.setText(timeva);

                                        }
                                    });

                                    StaticVariabes_div.log("inside ds" + E2, TAG1);
                                    switchstatus2("S" + E2);
                                    Statusupdatelight(lightenval);

                                    String E = String.valueOf(E2);

                                    if ((lightenval.equals("0")) || (E.equals("0")) || (E.equals("1")) || (E.equals("4")) || E.equals("5")) {
                                        invisible();
                                    } else {
                                        visible();
                                    }

                                }
                            }
                       // }
                    }
                }
            }catch(Exception e){
                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                       // Toast.makeText(getActivity().getApplicationContext(),"Invalid data recieved",Toast.LENGTH_SHORT).show();
                    }
                });
            }



        }

    }
    /////////////////////////////////////////////////////////////////////////////
    public void Statusupdatelight(final String hardware){

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(hardware.equals("1")){
                    imglig.setImageResource(R.drawable.lightsens_on);
                    //visible();
                }else{
                    imglig.setImageResource(R.drawable.light);
                    //invisible();
                }
            }
        });

    }

    private enum Statusset2 {
        S0, S1, S2, S3, S4, S5, S6, S7,
    }
    public void switchstatus2(String n){
        switch (Statusset2.valueOf(n)) {
            case S0:
                StaticVariabes_div.log("swb 0", TAG1);
                Statusupdate2(false);
                break;

            case S1:
                Statusupdate2(false);
                StaticVariabes_div.log("swb 1", TAG1);
                break;

            case S2:
                StaticVariabes_div.log("swb 2", TAG1);
                Statusupdate2(true);
                break;

            case S3:
                StaticVariabes_div.log("swb 3", TAG1);
                Statusupdate2(true);
                break;

            case S4:
                StaticVariabes_div.log("swb 4", TAG1);
                Statusupdate2(false);
                break;

            case S5:
                StaticVariabes_div.log("swb 5", TAG1);
                Statusupdate2(false);
                break;

            case S6:
                StaticVariabes_div.log("swb 6", TAG1);
                Statusupdate2(true);
                break;

            case S7:
                StaticVariabes_div.log("swb 7", TAG1);
                Statusupdate2(true);
                break;
            default:
                System.out.println("Not matching");
        }
    }

    public void Statusupdate2(final boolean hardware){

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                if(hardware){

                    if(StaticVariabes_div.devtyp.equals("WPD1")) {
                        imgpir.setImageResource(R.drawable.direc_pir_sen_on);
                    }else{
                        imgpir.setImageResource(R.drawable.pir_sens_on);
                    }
                }else{
                    if(StaticVariabes_div.devtyp.equals("WPD1")) {
                        imgpir.setImageResource(R.drawable.direc_pir_sen_off);
                    }else{
                        imgpir.setImageResource(R.drawable.motion);
                    }
                }
            }
        });


    }

    public void visible(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                lay_pir.setVisibility(View.VISIBLE);
                lay_lig.setVisibility(View.VISIBLE);
                rb_pir.setVisibility(View.VISIBLE);
                rb_ligsensor.setVisibility(View.VISIBLE);
                tv_priority.setVisibility(View.VISIBLE);
            }
        });
    }

    public void invisible(){
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                lay_pir.setVisibility(View.INVISIBLE);
                lay_lig.setVisibility(View.INVISIBLE);

                rb_pir.setVisibility(View.INVISIBLE);
                rb_ligsensor.setVisibility(View.INVISIBLE);
                tv_priority.setVisibility(View.INVISIBLE);
            }
        });

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
