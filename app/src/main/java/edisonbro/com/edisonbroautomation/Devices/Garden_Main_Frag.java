package edisonbro.com.edisonbroautomation.Devices;

/**
 *  FILENAME: Garden_Main_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Main Fragment to operate entire room sprinklers Devices and
 *  option to switch to group and individuals .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
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
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Garden_Main_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Garden_Main_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Garden_Main_Frag extends Fragment implements TcpTransfer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    MasterAdapter mas_adap;
    Spinner grp_spinner;
    Button btn_individuals;
    CombFrag combf;

    String groupexists[],groupdevnos[],devexists[],devmodtyp[],devnams[],groupdev_models[],groupdev_names[];
    View view;
    ImageView i;
    int count=0;
    private static final String TAG1="Sprinkler Main- ";
    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "37",devtypesett,model,Roomname;
    ///////////////////////////////////////////////////////////////////
    private static final int READ_BYTE = 1;
    private static final int READ_LINE = 2;
    private static final int ServStatus = 3;
    private static final int signallevel = 4;
    private static final int NetwrkType = 5;
    private static final int MAXUSER = 6;
    private static final int ERRUSER = 7;
    private static final int  UPDATE=8;
    //**********************************************
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************
    private Tracker mTracker;
    String name ="Sprinkler Main";
    //************************************************************************
    Button btn_on,btn_off;
    TextView tv_onOff,tvtest;

    public Garden_Main_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Garden_Main_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Garden_Main_Frag newInstance(String param1, String param2) {
        Garden_Main_Frag fragment = new Garden_Main_Frag();
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
        view = inflater.inflate(R.layout.fragment_garden_main, container, false);

        btn_individuals= (Button) view.findViewById(R.id.btn_indi);
        grp_spinner= (Spinner) view.findViewById(R.id.spinner);
        combf = ((CombFrag) this.getParentFragment());

        btn_on=(Button) view.findViewById(R.id.btnongsk);
        btn_off=(Button) view.findViewById(R.id.btnoffgsk);
        tv_onOff= (TextView) view.findViewById(R.id.tv_on_off);
        tvtest= (TextView) view.findViewById(R.id.tvtest);


        StaticVariabes_div.log("in main : "+StaticVariabes_div.typegrpindi,TAG1);

        StaticVariabes_div.dev_typ_num="044";
        StaticVariabes_div.loaded_lay_Multiple=true;
        combf.feature_sett_enab_disable(false);


        if(StaticVariabes_div.typegrpindi!=null){
            if(StaticVariabes_div.typegrpindi.equals("singlegroup")||StaticVariabes_div.typegrpindi.equals("mulgroup")){
                btn_individuals.setEnabled(false);
                btn_individuals.setVisibility(View.INVISIBLE);
                grp_spinner.setEnabled(true);
                grp_spinner.setVisibility(View.VISIBLE);
            }
            if(StaticVariabes_div.typegrpindi.equals("singleindi")||StaticVariabes_div.typegrpindi.equals("mulindi")){

                btn_individuals.setEnabled(true);
                btn_individuals.setVisibility(View.VISIBLE);
                grp_spinner.setEnabled(false);
                grp_spinner.setVisibility(View.INVISIBLE);
            }

            if(StaticVariabes_div.typegrpindi.equals("both")){
                btn_individuals.setEnabled(true);
                btn_individuals.setVisibility(View.VISIBLE);
                grp_spinner.setEnabled(true);
                grp_spinner.setVisibility(View.VISIBLE);
            }

        }

        mas_adap=new MasterAdapter(getActivity().getApplicationContext());
        mas_adap.open();

        groupexists=mas_adap.fetchUniquegroupids("KEY_ea","GSK", StaticVariabes_div.room_n);
        mas_adap.close();


        List<String> finlList= new ArrayList<String>();
        finlList.add("GROUP");
        for(int j=0;j<groupexists.length;j++){
            finlList.add(groupexists[j]);
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_spinner_item, finlList);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        grp_spinner.setAdapter(dataAdapter);

        grp_spinner.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            public void onGlobalLayout() {
                ((TextView) grp_spinner.getSelectedView()).setTextColor(Color.WHITE);

            }
        });

        grp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Track_button_event("GSK Main","Group Operation","Sprinkler Shortclick");
                String items=grp_spinner.getSelectedItem().toString();
                StaticVariabes_div.log("Selected item : "+items,TAG1);

                if(items.equals("GROUP")){

                }else {
                    loaddata(items, "GSK");

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }

        });


        btn_individuals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track_button_event("GSK Main","Individual Operation","Sprinkler Shortclick");
                mas_adap.open();
                devexists=mas_adap.fetchdevnoundergroup("000","GSK", StaticVariabes_div.room_n);

                int devtotalcnt=devexists.length;
                devmodtyp=new String[devtotalcnt];
                devnams=new String[devtotalcnt];

                for(int y=0;y<devtotalcnt;y++) {
                    devmodtyp[y] = mas_adap.getmodtype_devno(devexists[y]);
                    devnams[y] = mas_adap.getgrp_devname(devexists[y]);
                }
                mas_adap.close();

                StaticVariabes_div.gskinit(devtotalcnt,devexists,devmodtyp,devnams);
                StaticVariabes_div.typ_G_I="ind";
                combf.SwitchtoDeviceFragment("Gsk", "I", devexists[0], StaticVariabes_div.roomname, devmodtyp[0],devnams[0], 0);
            }
        });



        Tcp_con mTcp = new Tcp_con(this);

        if(Tcp_con.isClientStarted){
            // receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            // receiveddata(ServStatus,StaticStatus.Server_status,null);
        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }



        mn_nav_obj=(Main_Navigation_Activity) getActivity();

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();



        //^^^^^^^^^^^^^ON/OFF Button^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_on.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Track_button_event("Sprinkler Main","Sprinkler ON","Sprinkler Shortclick");
                ButtonOut("201");
            }
        })	;
        btn_off.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Track_button_event("Sprinkler Main","Sprinkler OFF","Sprinkler Shortclick");
                ButtonOut("301");
            }
        })	;


        return  view;
    }
    public void loaddata(String group,String typdev){
        StaticVariabes_div.log("load group data called", TAG1);

        if(group!=null){
            mas_adap.open();
            String  groupdevnos_sampls[]=mas_adap.fetchdevnoundergroup(group,typdev,StaticVariabes_div.room_n);
            mas_adap.close();

            if(StaticVariabes_div.loggeduser_type.equals("U")||StaticVariabes_div.loggeduser_type.equals("G")){
                List<String> listdevnum=new ArrayList<String>();
                for(int m=0;m<groupdevnos_sampls.length;m++){
                    if(Arrays.asList(StaticVariabes_div.roomdevicesnumberaccessarr).contains(groupdevnos_sampls[m])){
                        listdevnum.add(groupdevnos_sampls[m]);
                    }
                }
                StaticVariabes_div.log("user loggd", TAG1);
                groupdevnos=listdevnum.toArray(new String[listdevnum.size()]);
            }else{
                StaticVariabes_div.log("Admin loggd", TAG1);
                groupdevnos=groupdevnos_sampls;
            }


            groupdev_models=new String[groupdevnos.length];
            groupdev_names=new String[groupdevnos.length];

            mas_adap.open();
            for(int h=0;h<groupdevnos.length;h++) {
                groupdev_models[h] = mas_adap.getmodtype_devno(groupdevnos[h]);
                groupdev_names[h]= mas_adap.getgrp_devname(groupdevnos[h]);
            }
            mas_adap.close();
            if(StaticVariabes_div.loggeduser_type.equals("U")||StaticVariabes_div.loggeduser_type.equals("G")) {
                for (int k = 0; k < StaticVariabes_div.roomdevicesnumberaccessarr.length; k++) {
                    StaticVariabes_div.log("StaticVariabes_div.roomdevicesnumberaccessarr  k=" + k + " " + StaticVariabes_div.roomdevicesnumberaccessarr[k], TAG1);
                }
            }
            for(int k=0;k<groupdevnos.length;k++){
                StaticVariabes_div.log("groupdevnos  k="+k+" "+groupdevnos[k], TAG1);
            }

            String grpdevnams[],grpdevnos[],grpmoddevnos[];
            int devtotalcnt=0;
            if(groupdevnos.length==groupdevnos_sampls.length){

                StaticVariabes_div.load_group_master=true;

                devtotalcnt=groupdevnos.length+1;
                StaticVariabes_div.log("devtotalcnt"+devtotalcnt, TAG1);

                grpdevnos=new String[devtotalcnt];
                for(int j=0;j<devtotalcnt;j++){
                    StaticVariabes_div.log("devtotal j="+j, TAG1);
                    if(j==0){
                        grpdevnos[j]=group;
                    }else{
                        grpdevnos[j]=groupdevnos[j-1];
                    }

                }


                grpmoddevnos=new String[devtotalcnt];
                for(int j=0;j<devtotalcnt;j++){
                    StaticVariabes_div.log("devtotal j="+j, TAG1);
                    if(j==0){
                        grpmoddevnos[j]=typdev;
                    }else{
                        grpmoddevnos[j]=groupdev_models[j-1];
                    }

                }

                grpdevnams=new String[devtotalcnt];
                for(int j=0;j<devtotalcnt;j++){

                    if(j==0){
                        grpdevnams[j]=typdev+ " Group";
                    }else{
                        grpdevnams[j]=groupdev_names[j-1];
                        StaticVariabes_div.log("groupdev_names j="+groupdev_names[j-1], TAG1);
                    }

                }
            }else {
                StaticVariabes_div.load_group_master=false;

                devtotalcnt=groupdevnos.length;
                StaticVariabes_div.log("devtotalcnt"+devtotalcnt, TAG1);

                grpdevnos=new String[devtotalcnt];

                grpdevnos=groupdevnos;


                grpmoddevnos=new String[devtotalcnt];

                grpmoddevnos=groupdev_models;

                grpdevnams=new String[devtotalcnt];

                grpdevnams=groupdev_names;


            }


            for(int k=0;k<devtotalcnt;k++){
                StaticVariabes_div.log("grpdevnos  k="+k+" "+grpdevnos[k], TAG1);
            }

            if(typdev.contains("GSK")){
                StaticVariabes_div.Gskinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }

            StaticVariabes_div.typ_G_I="grp";
            combf.SwitchtoDeviceSettings("Gsk", "G");
            combf.SwitchtoDeviceFragment("Gsk", "G", grpdevnos[0], StaticVariabes_div.roomname, grpmoddevnos[0],grpdevnams[0], 0);
        }
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
               // DataIn(readBuf);

                break;
            case READ_LINE:
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 =data;
                if(readMessage2.equals("*OK#")){
                    mn_nav_obj.serv_status(true);
                  //  ButtonOut("920");

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
                       // ButtonOut("920");
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
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }
    //*******************************************************************************************
    void ButtonOut(String bo) {
        if (Tcp_con.isClientStarted) {
            devno="0000";
            roomno= StaticVariabes_div.room_n;
            while(roomno.length()<2)roomno="0"+roomno;
            String str = "0" + broadcastMsg + "000" + devno +roomno+ bo + "000000000000000";
            StaticVariabes_div.log( "out" + str, TAG1);
            //byte[] op= Blaster.WriteData(str, 1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            StaticVariabes_div.log("bout" + result + "$$$" + bo, TAG1);
            Tcp_con.WriteBytes(result);
        }
    }


    //$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$  TO CHECK INPUT RESPONSE $$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$
    void DataIn(byte[] byt)
    {
        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));
            try{
                inp= new String(input);//Blaster.ReadData(input, 1);
                StaticVariabes_div.log("input"+inp, TAG1);
                String DevType = inp.substring(1, 4);
                StaticVariabes_div.log( "DevType ac"+DevType, TAG1);
                String Dev = inp.substring(4, 8);
                char s14 = inp.charAt(8);
                String DS=String.valueOf(s14);

                StaticVariabes_div.log(" DS"+DS+" Devno"+Dev+" devno"+devno+" inp"+inp, TAG1);

                if(DevType.equals("022")){
                    if(Dev.equals(devno)){

                        if(DS.equals("1"))
                        {
                            StaticVariabes_div.log( "DS value 1"+DS, TAG1);
                            R1s(true);

                        }
                        else if(DS.equals("0"))
                        {
                            R1s(false);

                        }
                    }
                }


            }catch(Exception e){

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(),"Invalid data recieved",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }else{

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    Toast.makeText(getActivity().getApplicationContext(),"Invalid data length recieved",Toast.LENGTH_SHORT).show();
                }
            });
        }

    }
    void R1s(boolean b)
    {
        if(b)
            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    tv_onOff.setText("ON");
                    tv_onOff.setTextColor(Color.GREEN);

                }
            });
        else
            getActivity().runOnUiThread(new Runnable() {
                public void run() {

                    tv_onOff.setText("OFF");
                    tv_onOff.setTextColor(Color.RED);
                }
            });
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
}
