package edisonbro.com.edisonbroautomation.Devices;

/**
 *  FILENAME: ProjectorScreen_Fragment.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Fragment to operate selected projector screen device individually .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *  functions:
 *  transmitdata : To transmit pir data through tcp.
 *  Track_button_event : To track event on button click.
 *  listnerinitsingle :To initialize on click event for projectorscreen.
 *  proj_crs : To initialize curtain roller model.
 *  receiveddata : To receive data from tcp.
 *  Datain : To process the data received from tcp.
 *  popup : popup to display info.
 */


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateHome_Existing;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProjectorScreen_Fragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProjectorScreen_Fragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProjectorScreen_Fragment extends Fragment implements TcpTransfer,View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    private static final String TAG1="ProjectorScreen frag- ";
    //************************************************************************
    String strtext;
    String strtemp="test";
    static String deviceType = null;
    int sl;
    boolean check=true,statusserv,remoteconn,remoteconn3g,nonetwork;
    String servpreviousstate,remoteconprevstate ,rs, readMessage2,inp;
    String devno, roomno="0", housename, houseno, roomname, groupId = "000", broadcastMsg = "01", devtypesett,model,Roomname;
    //************************************************************************

    private static final int READ_BYTE = 1, READ_LINE = 2
   , ServStatus = 3, signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7,  UPDATE=8;

    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    CombFrag combf;
    private MasterAdapter mas_adap;
    private Tracker mTracker;
    String name ="Projector Individual";
    //********************************************************************************

    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //******************************************************************************
    Button b_Open,b_Close,b_stop;
    TextView tvtest;
    View view;



    public ProjectorScreen_Fragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProjectorScreen_Fragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProjectorScreen_Fragment newInstance(String param1, String param2) {
        ProjectorScreen_Fragment fragment = new ProjectorScreen_Fragment();
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

        mas_adap = new MasterAdapter(getActivity().getApplicationContext());
        strtext = StaticVariabes_div.devtyp;

        if (strtext == null) {
            strtext = strtemp;
        }

        StaticVariabes_div.log("strtext " + strtext, TAG1);

        switch (Models.valueOf(strtext)) {

            case PSC1:
                view = inflater.inflate(R.layout.frag_projector_screen, container, false);
                StaticVariabes_div.log("PSC1", TAG1);
                proj_crs();
                break;


        }

        tvtest=(TextView) view.findViewById(R.id.tvtest);
        if(StaticVariabes_div.dev_name!=null)
            tvtest.setText(StaticVariabes_div.dev_name);

        combf = ((CombFrag) this.getParentFragment());
        combf.feature_sett_enab_disable(true);

        Tcp_con mTcp = new Tcp_con(this);


        if(Tcp_con.isClientStarted){
            transmitdata(920);

        }else{
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(),StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        mn_nav_obj=(Main_Navigation_Activity) getActivity();


        tvtest=(TextView) view.findViewById(R.id.tvtest);

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        return view;

    }


        private enum Models {
            PSC1
        }

    private void proj_crs() {

        b_Open=(Button) view.findViewById(R.id.opencurnorm);
        b_Close=(Button) view.findViewById(R.id.closecurnorm);
        b_stop=(Button) view.findViewById(R.id.stopcurnorm);

        devtypesett="042";
        StaticVariabes_div.dev_typ_num=devtypesett;

        listnerinitsingle();

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
                StaticVariabes_div.log("msg read :- " + readMessage + " msg", TAG1);
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
    void DataIn(byte[] byt)
    {
        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));
            inp=new String(input);//Blaster.ReadData(input, 1);
            StaticVariabes_div.log("cur inp" + inp, TAG1);
            String DevType=inp.substring(1, 4);
            String Dev=inp.substring(4, 8);
            String DS =inp.substring(8, 10);
            String DS2 =inp.substring(10, 12);
            StaticVariabes_div.log("cur DevType-"+ DevType, TAG1);
            StaticVariabes_div.log("cur Dev- " + Dev, TAG1);
            StaticVariabes_div.log("cur ds1- " + DS+ "cur ds2- " + DS2, TAG1);

            if(DevType!=null||!(DevType.equals("")))
            {
                if (Dev!=null&&Dev.equals(devno)) {
                    StaticVariabes_div.log("device type - " + inp, TAG1);
                    projscreen_update(DS,DevType);
                }


            }

        }
    }


    public void projscreen_update( final String status ,final  String DevType_num){
        StaticVariabes_div.log("device type static - " + StaticVariabes_div.devtyp, TAG1);

        switch (Models.valueOf(StaticVariabes_div.devtyp)) {

            case PSC1:
                if(DevType_num.equals("042")){
                    if (status.equals("01")) {
                        b_Open.setBackgroundResource(R.drawable.cur_open_p);
                        b_Close.setBackgroundResource(R.drawable.close);
                        b_stop.setBackgroundResource(R.drawable.stop);
                    } else if (status.equals("02")) {
                        b_Close.setBackgroundResource(R.drawable.cur_close_p);
                        b_Open.setBackgroundResource(R.drawable.open);
                        b_stop.setBackgroundResource(R.drawable.stop);
                    } else if (status.equals("03")) {
                        b_stop.setBackgroundResource(R.drawable.stop_p);
                        b_Close.setBackgroundResource(R.drawable.close);
                        b_Open.setBackgroundResource(R.drawable.open);
                    }
                }
                break;

        }
    }

    ///////////////////initializing listeners////////////////////////////////////////////////////////////////////
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.opencurnorm:
                if(b_Open!=null) {
                    transmitdata(101);
                    Track_button_event("Curtain Individual","Open Curtain","Curtain Shortclick");
                }
                break;
            case R.id.closecurnorm:
                if(b_Close!=null) {
                    transmitdata(102);
                    Track_button_event("Curtain Individual","Close Curtain","Curtain Shortclick");
                }
                break;
            case R.id.stopcurnorm:
                if(b_stop!=null) {
                    transmitdata(103);
                    Track_button_event("Curtain Individual","Stop Curtain","Curtain Shortclick");
                }
                break;

        }
    }


    void transmitdata(int val)
    {
        String str,val4;
        val4=""+val;
        devno=StaticVariabes_div.projscrnoa[StaticVariabes_div.projscrpst];

        roomno= StaticVariabes_div.room_n;
        while(devno.length()<4)devno="0"+devno;
        while(val4.length()<3)val4="0"+val4;
        while(roomno.length()<2)roomno="0"+roomno;

        if (Tcp_con.isClientStarted) {

            str = "0" + "01" + "000" + devno + roomno+val4+"000000000000000";
            StaticVariabes_div.log("str" + str, TAG1);

           // byte[] op = Blaster.WriteData(str, 1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            StaticVariabes_div.log("bout" + result + "$$$" + val, TAG1);
            Tcp_con.WriteBytes(result);
        }
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

    public void listnerinitsingle(){
        b_Open.setOnClickListener(this);
        b_Close.setOnClickListener(this);
        b_stop.setOnClickListener(this);

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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

/*    @Override
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
