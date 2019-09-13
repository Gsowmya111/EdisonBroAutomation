package edisonbro.com.edisonbroautomation.Devices;

/**
 *  FILENAME: Pir_Main_Frag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Main Fragment to operate entire room pir Devices and
 *  option to switch to group and individuals .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *  functions:
 *  transmitdata : To transmit pir data through tcp.
 *  Track_button_event : To track event on button click.
 *  receiveddata : To receive data from tcp.
 *  popup : popup to display info.
 *  loaddata : To load fragment based on group selected from spinner.
 */

import android.app.AlertDialog;
import android.app.FragmentManager;
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
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Pir_Main_Frag.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Pir_Main_Frag#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Pir_Main_Frag extends Fragment implements TcpTransfer,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String TAG1="Pir_Main - ";
//************************************************************************
    String groupexists[],groupdevnos[],devexists[],devmodtyp[],devnams[],groupdev_models[],groupdev_names[];

    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01", devtypesett,model,Roomname;

    String timevalue,timeva,lightvalue;
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    //************************************************************************
    private static final int READ_BYTE = 1, READ_LINE = 2
   , ServStatus = 3, signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7,  UPDATE=8;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    private Tracker mTracker;
    CombFrag combf;
    MasterAdapter mas_adap;
    String name ="PIR Main";
    //********************************************************************************

    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //******************************************************************************

    View view;
    RadioButton rb_pir,rb_ligsensor;
    TextView tv_lightvalue,tv_priority;
    LinearLayout lay_pir,lay_lig;
    ImageView imgpir,imglig,pir_img_grp;
    Button btn_lig_enable,btn_lig_disable,btn_pir_enable,btn_pir_disable,btn_timesetpir;
    EditText ed_timevalue;
    Spinner grp_spinner;
    Button btn_individuals;
    private EditText et_time;
    private Button bt_one,bt_two,bt_three,bt_four,bt_five,bt_six,bt_seven,bt_eight,bt_nine,bt_zero,bt_cancel,bt_clear,bt_set;
    private AlertDialog dialog;

    //*********************************************************************************




    public Pir_Main_Frag() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Pir_Main_Frag.
     */
    // TODO: Rename and change types and number of parameters
    public static Pir_Main_Frag newInstance(String param1, String param2) {
        Pir_Main_Frag fragment = new Pir_Main_Frag();
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
        view= inflater.inflate(R.layout.pir_main_frag, container, false);
        btn_individuals= (Button) view.findViewById(R.id.btn_pirindi);
        grp_spinner= (Spinner) view.findViewById(R.id.spinner);

        combf = ((CombFrag) this.getParentFragment());

        StaticVariabes_div.dev_typ_num="718";
        StaticVariabes_div.loaded_lay_Multiple=true;


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



        combf.feature_sett_enab_disable(false);

            mas_adap=new MasterAdapter(getActivity().getApplicationContext());
            mas_adap.open();

            groupexists=mas_adap.fetchUniquegroupids("KEY_ea","PIR", StaticVariabes_div.room_n);
            mas_adap.close();

     // List<String> list =  Arrays.asList(groupexists);
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
                //((TextView) grp_spinner.getSelectedView()).setTextSize(15.0f);
            }
        });

        grp_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                Track_button_event("PIR Main","Group Operation","MotionSensor Shortclick");
                String items=grp_spinner.getSelectedItem().toString();
                StaticVariabes_div.log("Selected item : "+items,TAG1);

                if(items.equals("GROUP")){

                }else {
                    loaddata(items, "PIR");

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
                Track_button_event("PIR Main","Individual Operation","MotionSensor Shortclick");
                mas_adap.open();
                devexists=mas_adap.fetchdevnoundergroup("000","PIR", StaticVariabes_div.room_n);

                int devtotalcnt=devexists.length;
                devmodtyp=new String[devtotalcnt];
                devnams=new String[devtotalcnt];

                for(int y=0;y<devtotalcnt;y++) {
                    devmodtyp[y] = mas_adap.getmodtype_devno(devexists[y]);
                    devnams[y] = mas_adap.getgrp_devname(devexists[y]);
                }
                mas_adap.close();

                StaticVariabes_div.pirinit(devtotalcnt,devexists,devmodtyp,devnams);
                StaticVariabes_div.typ_G_I="ind";
                combf.SwitchtoDeviceFragment("Pir", "I", devexists[0], "test", devmodtyp[0],devnams[0], 0);
            }
        });


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

        //^^^^Time Pir BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        btn_timesetpir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Timervalue(timeva);
                timesett_pirpopup(timeva);
            }
        });
        //^^^^Enable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_pir_enable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("909","A");
                Track_button_event("PIR Main","MotionSensor ON","MotionSensor Shortclick");

            }
        });

        //^^^^Disable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_pir_disable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("910","A");
                Track_button_event("PIR Main","MotionSensor OFF","MotionSensor Shortclick");

            }
        });

        //^^^^light Enable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_lig_enable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("101","A");
                Track_button_event("PIR Main","LightSensor ON","MotionSensor Shortclick");

            }
        });

        //^^^^light Disable BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
        btn_lig_disable.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                transmitdata("102","A");
                Track_button_event("PIR Main","MotionSensor OFF","MotionSensor Shortclick");

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

        Tcp_con mTcp = new Tcp_con(this);

        if(Tcp_con.isClientStarted){
            //   Tcp_con.stacontxt = getActivity().getApplicationContext();
            //   Tcp_con.ins();

            // receiveddata(NetwrkType,StaticStatus.Network_Type,null);
            // receiveddata(ServStatus,StaticStatus.Server_status,null);
            //  Tcp_con.stopClient();


           // transmitdata("920","A");

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
                // groupdev_models[h]= mas_adap.fetchmodelunderdevtype(typdev, room_num,groupdev_models[h] );
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
                        grpdevnams[j]=typdev + " Group";
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

           if(typdev.contains("PIR")){
                StaticVariabes_div.Pirinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }

            StaticVariabes_div.typ_G_I="grp";
            combf.SwitchtoDeviceSettings("Pir", "G");
           // combf.SwitchtoDeviceFragment("Pir", "G", "0", "test", "PIR","PIRGROUP", 0);
            combf.SwitchtoDeviceFragment("Pir", "G", grpdevnos[0], StaticVariabes_div.roomname, grpmoddevnos[0],grpdevnams[0], 0);
           // SwitchtoDeviceFragment(Selected_gridtype, type_grp_indi, Devices_num[Device_position], StaticVariabes_div.roomname, Devices_model_arr[Device_position], Devices_Name_arr[Device_position], Device_position);

        }
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
                Track_button_event("PIR Main","MotionSensor change value","MotionSensor Shortclick");
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


    private void Timervalue(String prevtext) {

        final	AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        //alert.setTitle("Setting");
        //alert.setMessage("Enter The Time Value");

        Context mContext = getActivity();
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(LAYOUT_INFLATER_SERVICE);
        // View checkboxLayout = inflater.inflate(R.layout.pop_pirtimersett,(ViewGroup) view.findViewById(R.id.laypir));

        // LayoutInflater inflater = getLayoutInflater();
        View checkboxLayout = inflater.inflate(R.layout.pop_pirtimersett, null);
        alert.setView(checkboxLayout);

        ed_timevalue = (EditText) checkboxLayout.findViewById(R.id.ed_pirtime);

        if(prevtext==null){
            prevtext="0000";
        }
        ed_timevalue.setText(prevtext);

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {


            }
        });
        //^^^^Set time BUTTON^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

        alert.setPositiveButton("Set", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                timevalue=ed_timevalue.getText().toString();
                while(timevalue.length()<4)timevalue="0"+timevalue;
                if(isValidtime(timevalue)){
                    StaticVariabes_div.log("timevalue"+timevalue+"length"+timevalue.length(), TAG1);

                    if(timevalue!=null)
                        transmitdata(timevalue,"B");

                }else{
                    Timervalue(timevalue);
                    ed_timevalue.setError("Max Length 4digit");

                }


            }
        });
        AlertDialog d = alert.create();
        d.getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        d.show();
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
        devno="0000";

        roomno= StaticVariabes_div.room_n;
        while(devno.length()<4)devno="0"+devno;

        while(roomno.length()<2)roomno="0"+roomno;


        if(type.equals("A")) {
            while(val4.length()<3)val4="0"+val4;

            str = "0"+"35"+"000"+"0000"+roomno+val4+"000000000000000";
            StaticVariabes_div.log("str"+str, TAG1);


        }  else if(type.equals("C")) {

            while(val4.length()<1)val4="0";


            str = "0" + "35" + "000" + devno + roomno+"103"+"0000"+val4+"0000000000";
            StaticVariabes_div.log("str" + str, TAG1);


        }else {

            while(val4.length()<4)val4="0"+val4;

            str = "0"+"35"+"000"+"0000"+roomno+"000"+val4+"0000000000000";
            StaticVariabes_div.log("str"+str, TAG1);

        }

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

                receiveddata(type,stringData,byteData);

            }
        });
    }

    public void receiveddata(int msg,String data,byte[] bytestatus){

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                final String readMessage = new String(readBuf, 0,readBuf.length);
                StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);
              //  DataIn(readBuf);

                break;
            case READ_LINE:
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 =data;
                if(readMessage2.equals("*OK#")){
                    mn_nav_obj.serv_status(true);
                    transmitdata("920","A");
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
