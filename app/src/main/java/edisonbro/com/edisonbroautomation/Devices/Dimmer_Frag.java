package edisonbro.com.edisonbroautomation.Devices;



/**
 *  FILENAME: Dimmer_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Fragment to operate selected dimmer Device individually .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
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
import edisonbro.com.edisonbroautomation.scrollfunction.ShaderSeekArc;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Dimmer_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Dimmer_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */


/**
 *
 */
public class Dimmer_Frag extends Fragment implements TcpTransfer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG1="Dimmer Indi- ";
    //***************************************************************
    int n=0, count=0, intValue = 0;
    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01",devtypesett,model,Roomname;
    //***************************************************************
    private static final int READ_BYTE = 1, READ_LINE = 2
    ,ServStatus = 3, signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7,  UPDATE=8;
    //************************************************************************
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    boolean responseupdate=false;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    CombFrag combf;
    private Tracker mTracker;
    String name ="Dimmer Individual";
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************
    public ImageView dim_img;
    View view;
    ImageView i;
    ShaderSeekArc seekArc;
    Button btn_on,btn_off,btn_low,btn_medium,btn_high;
    TextView tvtest;

//****************************************************************

    public static Integer[] mThumbIds = {

            R.drawable.dim00,R.drawable.dim01,R.drawable.dim02,R.drawable.dim03,R.drawable.dim04,R.drawable.dim05,R.drawable.dim06,
            R.drawable.dim07, R.drawable.dim08, R.drawable.dim09, R.drawable.dim10, R.drawable.dim11, R.drawable.dim12,
            R.drawable.dim13,R.drawable.dim14,R.drawable.dim15,R.drawable.dim16,
            R.drawable.dim17, R.drawable.dim18, R.drawable.dim19,R.drawable.dim20
    };

   /* public static Integer[] mThumbIds = {

            R.drawable.dim_level0,R.drawable.dim_level1,R.drawable.dim_level2,R.drawable.dim_level3,R.drawable.dim_level4,R.drawable.dim_level5,R.drawable.dim_level6,
            R.drawable.dim_level7, R.drawable.dim_level8, R.drawable.dim_level9,
    };*/

    public static Integer[] mThumbIds_group = {

            R.drawable.dimer_grp_00,R.drawable.dimer_grp_01,R.drawable.dimer_grp_02,R.drawable.dimer_grp_03,R.drawable.dimer_grp_04,R.drawable.dimer_grp_05,R.drawable.dimer_grp_06,R.drawable.dimer_grp_07,
            R.drawable.dimer_grp_08, R.drawable.dimer_grp_09,
    };



    public Dimmer_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Dimmer_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Dimmer_Frag newInstance(String param1, String param2) {
        Dimmer_Frag fragment = new Dimmer_Frag();
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
        view =inflater.inflate(R.layout.fragment_dimmer_, container, false);
        dim_img= (ImageView) view.findViewById(R.id.image);
        seekArc= (ShaderSeekArc) view.findViewById(R.id.seek_arc);


        btn_on= (Button) view.findViewById(R.id.btnondim);
        btn_off= (Button) view.findViewById(R.id.btnoffdim);

        btn_low= (Button) view.findViewById(R.id.btn_dimlow);
        btn_medium= (Button) view.findViewById(R.id.btn_dimmedium);
        btn_high= (Button) view.findViewById(R.id.btn_dimhigh);

        dim_img.setImageResource(R.drawable.dim_level0);

        StaticVariabes_div.dev_typ_num="020";
        StaticVariabes_div.loaded_lay_Multiple=false;
        Tcp_con mTcp = new Tcp_con(this);

        if(Tcp_con.isClientStarted){
            //   Tcp_con.stacontxt = getActivity().getApplicationContext();
            //   Tcp_con.ins();

            // receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            // receiveddata(ServStatus,StaticStatus.Server_status,null);
            //  Tcp_con.stopClient();
            transmitdata(920,"A");

        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        tvtest=(TextView) view.findViewById(R.id.tvtest);

        if(StaticVariabes_div.dev_name!=null)
            tvtest.setText(StaticVariabes_div.dev_name);

        mn_nav_obj=(Main_Navigation_Activity) getActivity();

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        combf = ((CombFrag) this.getParentFragment());
        combf.feature_sett_enab_disable(true);

        btn_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                transmitdata(102,"A");
            }
        })	;


        btn_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata(103,"A");

            }
        })	;

        btn_low.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                transmitdata(002,"B");
            }
        })	;


        btn_medium.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                StaticVariabes_div.log("medium"+"040", TAG1);
                transmitdata(100,"B");

            }
        })	;


        btn_high.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata(255,"B");

            }
        })	;

        seekArc.setOnSeekArcChangeListener(new ShaderSeekArc.OnSeekArcChangeListener() {
            @Override
            public void onProgressChanged(ShaderSeekArc seekArc, float progress) {

                count=(int)seekArc.getProgress();
                //if(count<10)count=2;
                //count=dimmrbright(count);
                if(responseupdate){

                }else {
                    transmitdata(count, "B");

                }

            }

            @Override
            public void onStartTrackingTouch(ShaderSeekArc seekArc) {
                responseupdate=false;
            }

            @Override
            public void onStopTrackingTouch(ShaderSeekArc seekArc) {
                //if(count<10)count=2;
                //count=dimmrbright(count);
                transmitdata(count,"B");

               /* if(dimgrouppost.equals("0")){
                    seekstatus_group(count);
                }*/
            }
        });




        return  view;
    }

    public int dimmrbright(int i){

       /* if(i<=70){
            return (int)map(i,0,70,2,50);
        }else if(i>70&&i<=100){
            return (int)map(i,71,100,51,255);
        }*/
       if(i>=0&&i<=100){
            return (int)map(i,0,100,0,255);
        }
        return -1;
    }


    public int dimmrbright_dwngrade(int i){

       /* if(i<=70){
            return (int)map(i,0,70,2,50);
        }else if(i>70&&i<=100){
            return (int)map(i,71,100,51,255);
        }*/
        if(i>=0&&i<=255){
            return (int)map(i,0,255,0,100);
        }
        return -1;
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }

    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  TO Change dimmer bulb image $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
/*    void seekstatus(int i){
        n = i;
        getActivity().runOnUiThread(new Runnable() {
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

    }*/
    void seekstatus(int i){
        n = i;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                StaticVariabes_div.log("seekstatus"+n, TAG1);
                if(n==0){
                    dim_img.setImageResource(mThumbIds[0]);
                }else
                if(n>=1&&n<=13){
                    dim_img.setImageResource(mThumbIds[1]);
                }else if(n>13&&n<=26){
                    dim_img.setImageResource(mThumbIds[2]);
                }else if(n>26&&n<=39){
                    dim_img.setImageResource(mThumbIds[3]);
                }else if(n>39&&n<=42){
                    dim_img.setImageResource(mThumbIds[4]);
                }else if(n>42&&n<=55){
                    dim_img.setImageResource(mThumbIds[5]);
                }else if(n>55&&n<=68){
                    dim_img.setImageResource(mThumbIds[6]);
                }else if(n>68&&n<=81){
                    dim_img.setImageResource(mThumbIds[7]);
                }else if(n>81&&n<=94){
                    dim_img.setImageResource(mThumbIds[8]);
                }else if(n>94&&n<=107){
                    dim_img.setImageResource(mThumbIds[9]);
                }else if(n>107&&n<=120){
                    dim_img.setImageResource(mThumbIds[10]);
                }else if(n>120&&n<=133){
                    dim_img.setImageResource(mThumbIds[11]);
                }else if(n>133&&n<=146){
                    dim_img.setImageResource(mThumbIds[12]);
                }else if(n>146&&n<=159){
                    dim_img.setImageResource(mThumbIds[13]);
                }else if(n>159&&n<=172){
                    dim_img.setImageResource(mThumbIds[14]);
                }else if(n>172&&n<=185){
                    dim_img.setImageResource(mThumbIds[15]);
                }else if(n>185&&n<=198){
                    dim_img.setImageResource(mThumbIds[16]);
                }else if(n>198&&n<=211){
                    dim_img.setImageResource(mThumbIds[17]);
                }else if(n>211&&n<=224){
                    dim_img.setImageResource(mThumbIds[18]);
                }else if(n>224&&n<=237){
                    dim_img.setImageResource(mThumbIds[19]);
                }else if(n>237&&n<=255){
                    dim_img.setImageResource(mThumbIds[20]);
                }

            }
        });

    }

    void seekstatus_group(int i){
        n = i;

        getActivity().runOnUiThread(new Runnable() {
            public void run() {
                StaticVariabes_div.log("seekstatus"+n, TAG1);
                if(n==0){
                    dim_img.setImageResource(mThumbIds_group[0]);
                }else
                if(n>=1&&n<=10){
                    dim_img.setImageResource(mThumbIds_group[1]);
                }else if(n>10&&n<=20){
                    dim_img.setImageResource(mThumbIds_group[2]);
                }else if(n>20&&n<=40){
                    dim_img.setImageResource(mThumbIds_group[3]);
                }else if(n>40&&n<=70){
                    dim_img.setImageResource(mThumbIds_group[4]);
                }else if(n>70&&n<=100){
                    dim_img.setImageResource(mThumbIds_group[5]);
                }else if(n>100&&n<=140){
                    dim_img.setImageResource(mThumbIds_group[6]);
                }else if(n>140&&n<=180){
                    dim_img.setImageResource(mThumbIds_group[7]);
                }else if(n>180&&n<=220){
                    dim_img.setImageResource(mThumbIds_group[8]);
                }else if(n>220&&n<=255){
                    dim_img.setImageResource(mThumbIds_group[9]);
                }

            }
        });

    }

    void transmitdata(int val3,String type)
    {  String str,val4;

        val4=""+val3;
        StaticVariabes_div.log("val3" + val3, TAG1);
        devno=StaticVariabes_div.dmmrnoa[StaticVariabes_div.dmmrpst];

        roomno= StaticVariabes_div.room_n;
        while(devno.length()<4)devno="0"+devno;
        while(val4.length()<3)val4="0"+val4;
        while(roomno.length()<2)roomno="0"+roomno;

        StaticVariabes_div.log("val4" + val4, TAG1);

        if(type.equals("A")) {

                str = "0" + "01" + "000" + devno + roomno + val4 + "000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);

        }else {

                str = "0" + "01" + "000" + devno + roomno + "112" + val4 + "000000000000";
                StaticVariabes_div.log("str" + str, TAG1);

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
        try {
            receiveddata(type, stringData, byteData);
        }catch(Exception e){

        }

            }
        });
    }

    public void receiveddata(int msg,String data,byte[] bytestatus){

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                // DataIn(readBuf);
                final String readMessage = new String(readBuf, 0,readBuf.length);
                StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);

                DataIn(readBuf);

                break;
            case READ_LINE:
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 =data;
                if(readMessage2.equals("*OK#")){
                    mn_nav_obj.serv_status(true);
                    transmitdata(920,"A");
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
                        transmitdata(920,"A");

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

    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  TO CHECK INPUT RESPONSE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    void DataIn(byte[] byt)
    {
        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));
            inp=new String(input);//Blaster.ReadData(input, 1);
            StaticVariabes_div.log("input f"+inp, TAG1);
            String DevType = inp.substring(1, 4);
            StaticVariabes_div.log("DevType dim"+DevType, TAG1);
            String Dev = inp.substring(4, 8);
            StaticVariabes_div.log("input"+byt.length, TAG1);
            String DS =inp.substring(8, 10);
            String dval =inp.substring(10, 13);
            StaticVariabes_div.log("DS"+DS+"Devno"+Dev+"devno"+devno+"dval"+dval+"inp"+inp, TAG1);


            if(DevType.equals("020")){
                if(Dev.equals(devno)){

                    if(DS.equals("01"))
                    {

                        StaticVariabes_div.log("DS value ON"+DS, TAG1);
                        StaticVariabes_div.log("dval"+dval, TAG1);

                        try{

                            intValue = Integer.parseInt(dval);

                        }catch(NumberFormatException ne){

                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    Toast.makeText(getActivity().getApplicationContext(),"Invalid data recieved",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }


                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    //  enable();
                                    //  b_onoff.setText("ON");
                                    // img.setBackgroundResource(R.drawable.dim_level5);
                                    StaticVariabes_div.log("dim intValue" + intValue, TAG1);
                                    seekstatus(intValue);
                                   // int tt=dimmrbright_dwngrade(intValue);
                                    //StaticVariabes_div.log("dim tt" + tt, TAG1);

                                    responseupdate=true;
                                    seekArc.setProgress(intValue);
                                }
                            });



                    }
                    else if(DS.equals("02"))
                    {


                            StaticVariabes_div.log("DS value 0FF"+DS, TAG1);
                            getActivity().runOnUiThread(new Runnable() {
                                public void run() {
                                    seekstatus(0);
                                    seekArc.setProgress(0);
                                    //img.setBackgroundResource(R.drawable.dim_level1);
                                }
                            });


                        }

                    }
                }
            }


    }
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

  /*  @Override
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
