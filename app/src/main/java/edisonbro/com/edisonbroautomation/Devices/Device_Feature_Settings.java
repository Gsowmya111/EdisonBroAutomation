package edisonbro.com.edisonbroautomation.Devices;

/**
 *  FILENAME: Device_Feature_Settings.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Fragment to enable/disable  settings like touch,ir of all devices types
 *  and set serial number to switch board of device individually .
 *
 *  update_version : To update device version number received through gateway into database.
 *  Track_button_event : To track event on button click.
 *  ButtonOut  :  To send data through tcp to gateway.
 *  switchstatus : Switch case to pass response data to Statusupdate method.
 *  Statusupdate : To update status of memory , touch, buzzer, ir state in ui.
 *  switchstatus2 :  Switch case to pass response data to Statusupdate2 method.
 *  Statusupdate2 : To update status of hardware,config, manual override state in ui.
 *  popup : popup to display info.
 *  popup_check : popup to make sure from user to enter device into config mode or not.
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import edisonbro.com.edisonbroautomation.Connections.TcpTransfer;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Edisonbro_AnalyticsApplication;
import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.operatorsettings.UpdateAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Device_Feature_Settings.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Device_Feature_Settings#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Device_Feature_Settings extends Fragment implements TcpTransfer {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;


    private static final String TAG1 = "Device Settings - ";
    //*****************************************************************

    String devno, roomno = "0", housename, houseno, roomname, groupId = "000",
            broadcastMsg = "01", devtypesett, model, Roomname;
    boolean powerstatus = false;
    int speed1 = 0, speed2 = 0, speed3 = 0, speed4 = 0,dmr_min_br=0,dmr_max_br=0;
    String fspeed1="0",fspeed2="0",fspeed3="0",fspeed4="0",dmr_min_b="0",dmr_max_b="0";

    //*************************************************************************
    private static final int READ_BYTE = 1, READ_LINE = 2,
            ServStatus = 3, signallevel = 4, NetwrkType = 5, MAXUSER = 6, ERRUSER = 7;
    //*************************************************************************
    int sl;
    boolean check = true, statusserv, remoteconn, remoteconn3g, nonetwork;
    String servpreviousstate, remoteconprevstate, rs, readMessage2, inp;
    //************************************************************************
    Main_Navigation_Activity mn_nav_obj;
    private Tracker mTracker;
    private SwbAdapter swbadap;
    private MasterAdapter mas_adap;
    UpdateAdapter up_adap;
    String name = "Device Feature And Settings";
    //********************************************************************************
    int delay = 0; // delay for 1 sec.
    int period = 1000;
    Timer timer = null;
    //********************************************************************
    ToggleButton b_memory, b_touch, b_buzzer, b_ir, b_config, b_hardware, b_manual_override, b_curtinverse;
    Spinner sp_swbnum;
    Button b_swbnumset, b_pirdirset, b_resetir, b_fanpwmset ,b_brightdmr;
    View view;
    TextView tv_versionnumber;
    ToggleButton[] btnarr;
    LinearLayout lnr_curt, lnr_irset ,lnr_dmrset;
    LayoutInflater inflater;
    android.support.v7.app.AlertDialog dialog1;
    TextView et_one, et_two, et_three, et_four, main;
    Button btn_1_inc, btn_1_dec, btn_2_inc, btn_2_dec, btn_3_inc, btn_3_dec, btn_4_inc, btn_4_dec,cancel, save, b_resetbright;
    EditText et_min,et_max;

    //*******************************************************************************


    public Device_Feature_Settings() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Device_Feature_Settings.
     */
    // TODO: Rename and change types and number of parameters
    public static Device_Feature_Settings newInstance(String param1, String param2) {
        Device_Feature_Settings fragment = new Device_Feature_Settings();
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
        view = inflater.inflate(R.layout.device_feature_settings_frag, container, false);
        b_memory = (ToggleButton) view.findViewById(R.id.ToggleMemory);
        b_touch = (ToggleButton) view.findViewById(R.id.Toggletouch);
        b_ir = (ToggleButton) view.findViewById(R.id.Toggleir);
        b_buzzer = (ToggleButton) view.findViewById(R.id.ToggleBuzzer);
        b_config = (ToggleButton) view.findViewById(R.id.Toggleconfig);
        b_hardware = (ToggleButton) view.findViewById(R.id.Togglehardware);
        b_manual_override = (ToggleButton) view.findViewById(R.id.Togglemanual);


        b_swbnumset = (Button) view.findViewById(R.id.btnswbnumset);
        sp_swbnum = (Spinner) view.findViewById(R.id.spinner_swbnum);
        b_fanpwmset = (Button) view.findViewById(R.id.btnfanpwm);

        b_pirdirset = (Button) view.findViewById(R.id.btnpirdirset);
        b_resetir = (Button) view.findViewById(R.id.btnresetir);
        b_curtinverse = (ToggleButton) view.findViewById(R.id.btncurtset);

        lnr_curt = (LinearLayout) view.findViewById(R.id.lnr_curt);
        lnr_irset = (LinearLayout) view.findViewById(R.id.lnr_irset);
        lnr_dmrset = (LinearLayout) view.findViewById(R.id.lnr_dmrset);

        b_brightdmr = (Button) view.findViewById(R.id.btnbrightdmr);

        tv_versionnumber= (TextView) view.findViewById(R.id.tv_versionnum);

        b_fanpwmset.setEnabled(false);
        b_fanpwmset.setVisibility(View.INVISIBLE);

        btnarr = new ToggleButton[]{b_memory, b_touch, b_ir, b_buzzer, b_config, b_hardware};

        roomno = StaticVariabes_div.room_n;
        while (roomno.length() < 2) roomno = "0" + roomno;
        devno = StaticVariabes_div.devnumber;
        while (devno.length() < 4) devno = "0" + devno;
        devtypesett = StaticVariabes_div.dev_typ_num;
        while (devtypesett.length() < 3) devtypesett = "0" + devtypesett;

        if (StaticVariabes_div.devtyp.equals("CLNR")) {
            if (StaticVariabes_div.sheerselected) {
                devno = StaticVariabes_div.sheerdevno;
                while (devno.length() < 4) devno = "0" + devno;
                devtypesett = "051";
                StaticVariabes_div.log("sheer true" + devno, TAG1);
            }

        }

        if (StaticVariabes_div.devtyp.equals("S051") || StaticVariabes_div.devtyp.equals("S021")) {
            b_fanpwmset.setEnabled(true);
            b_fanpwmset.setVisibility(View.VISIBLE);

        }

        if (StaticVariabes_div.Selected_device.equals("Swb") || StaticVariabes_div.Selected_device.equals("Swd")) {
            b_swbnumset.setEnabled(true);
            b_swbnumset.setVisibility(View.VISIBLE);
            sp_swbnum.setEnabled(true);
            sp_swbnum.setVisibility(View.VISIBLE);
            //  tv_setnum.setEnabled(true);
            //  tv_setnum.setVisibility(View.VISIBLE);
            b_resetir.setEnabled(true);
            b_resetir.setVisibility(View.VISIBLE);
            b_manual_override.setEnabled(true);
            b_manual_override.setVisibility(View.VISIBLE);

            lnr_curt.setEnabled(false);
            lnr_curt.setVisibility(View.GONE);
            lnr_irset.setEnabled(true);
            lnr_irset.setVisibility(View.VISIBLE);
        } else {
            b_swbnumset.setEnabled(false);
            b_swbnumset.setVisibility(View.INVISIBLE);
            sp_swbnum.setEnabled(false);
            sp_swbnum.setVisibility(View.INVISIBLE);
            //  tv_setnum.setEnabled(false);
            //   tv_setnum.setVisibility(View.INVISIBLE);
            b_resetir.setEnabled(false);
            b_resetir.setVisibility(View.GONE);
            // b_manual_override.setEnabled(false);
            // b_manual_override.setAlpha((float) 0.2);
            // b_manual_override.setVisibility(View.INVISIBLE);
        }

        if (StaticVariabes_div.Selected_device.equals("Clb") || StaticVariabes_div.Selected_device.equals("Pir") || StaticVariabes_div.Selected_device.equals("Wpc")
                || StaticVariabes_div.Selected_device.equals("Pirgrplay") || StaticVariabes_div.Selected_device.equals("Cur") || StaticVariabes_div.Selected_device.equals("Psc")) {
            b_memory.setEnabled(false);
            b_touch.setEnabled(false);
            b_ir.setEnabled(false);
            b_ir.setAlpha((float) 0.2);
            b_touch.setAlpha((float) 0.2);
            b_memory.setAlpha((float) 0.2);

        }


        if (StaticVariabes_div.Selected_device.equals("Dmr") || StaticVariabes_div.Selected_device.equals("Rgb")
                || StaticVariabes_div.Selected_device.equals("Gsk")) {
            b_ir.setEnabled(false);
            b_touch.setEnabled(false);
            b_ir.setAlpha((float) 0.1);
            b_touch.setAlpha((float) 0.2);
        }

        if (StaticVariabes_div.Selected_device.equals("Pir") || StaticVariabes_div.Selected_device.equals("Pirgrplay")) {
            b_manual_override.setEnabled(false);
            b_manual_override.setAlpha((float) 0.2);
            b_buzzer.setEnabled(false);
            b_buzzer.setAlpha((float) 0.2);

        }

        if (StaticVariabes_div.Selected_device.equals("Pir")) {
            if (StaticVariabes_div.devtyp.equals("WPD1")) {

                b_pirdirset.setEnabled(true);
                b_pirdirset.setVisibility(View.VISIBLE);
            }
        }

        if (StaticVariabes_div.Selected_device.equals("Cur")) {
            if (StaticVariabes_div.devtyp.equals("CLNR") || StaticVariabes_div.devtyp.equals("CLS1")) {

                lnr_curt.setEnabled(true);
                lnr_curt.setVisibility(View.VISIBLE);
                lnr_irset.setEnabled(false);
                lnr_irset.setVisibility(View.GONE);
            }
        }


        if (StaticVariabes_div.Selected_device.equals("Dmr")) {
            lnr_dmrset.setEnabled(true);
            lnr_dmrset.setVisibility(View.VISIBLE);
            lnr_irset.setEnabled(false);
            lnr_irset.setVisibility(View.GONE);

        }
        if (StaticVariabes_div.loggeduser_type.equals("SA")) {
            b_config.setEnabled(true);
            b_config.setVisibility(View.VISIBLE);
        } else {
            b_config.setEnabled(false);
            b_config.setAlpha((float) 0.2);
        }

        b_fanpwmset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Setpwm_fan();
            }
        });

        b_pirdirset.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                devno = StaticVariabes_div.devnumber;
                while (devno.length() < 4) devno = "0" + devno;
                String str = "$116," + devno + "&";
                StaticVariabes_div.log("out" + str, TAG1);
                if (Tcp_con.isClientStarted) {
                    StaticVariabes_div.log("started" + str, TAG1);
                    Tcp_con.WriteBytes(str.getBytes());
                } else {
                    StaticVariabes_div.log("not started" + str, TAG1);
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Dir Set", "Dir Set Shortclick");

            }
        });

        b_resetir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonOut("930");
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Reset IR", "Reset IR Shortclick");
            }
        });

        b_config.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    popup_tocheck();
                } else {
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Config Mode", "Config Mode Shortclick");

            }
        });

        b_manual_override.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("931");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("932");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Manual Override", "Manual Override Shortclick");

            }
        });

        b_memory.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("907");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("908");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Memory", "Memory Shortclick");

            }
        });
        b_touch.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("911");
                    delaytime(300);
                    ButtonOut("920");

                } else {

                    ButtonOut("912");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Touch", "Touch Shortclick");


            }
        });

        b_buzzer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("913");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("914");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Buzzer", "Buzzer Shortclick");

            }
        });

        b_ir.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("915");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("916");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " IR", " IR Shortclick");

            }
        });

        b_hardware.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("909");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("910");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Hardware", "Hardware Shortclick");

            }
        });

        b_swbnumset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String swbnumfromspinnr = String.valueOf(sp_swbnum.getSelectedItem());
                StaticVariabes_div.log("swbnumfromspinnr" + swbnumfromspinnr, TAG1);
                ButtonOut("92" + swbnumfromspinnr);
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Num Set", "Num Set Shortclick");


            }
        });
        b_curtinverse.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                boolean on = ((ToggleButton) v).isChecked();
                if (on) {
                    ButtonOut("933");
                    delaytime(300);
                    ButtonOut("920");
                } else {
                    ButtonOut("934");
                    delaytime(300);
                    ButtonOut("920");
                }
                Track_button_event("Device Features & Settings", StaticVariabes_div.Selected_device + " Hardware", "Hardware Shortclick");

            }
        });

        b_brightdmr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Setdmr_val();
            }
        });

        Tcp_con mTcp = new Tcp_con(this);

        if (Tcp_con.isClientStarted) {

            ButtonOut("935");
            delaytime(200);
            ButtonOut("920");


        } else {
            Tcp_con.stacontxt = getActivity().getApplicationContext();
            Tcp_con.serverdetailsfetch(getActivity(), StaticVariabes_div.housename);
            Tcp_con.registerReceivers(getActivity().getApplicationContext());
        }

        mn_nav_obj = (Main_Navigation_Activity) getActivity();

        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        try{

            swbadap=new SwbAdapter(getActivity().getApplicationContext());
            mas_adap=new MasterAdapter(getActivity().getApplicationContext());

        }catch(Exception e){
            e.printStackTrace();
        }


        swbadap.open();
        String devver = swbadap.getdevversion_frmdevno(StaticVariabes_div.devnumber);
        swbadap.close();

        mas_adap.open();
        if (devver.equals("")) {
            StaticVariabes_div.log("roomno null", TAG1);
            devver = mas_adap.getdevversion_devno(StaticVariabes_div.devnumber);
        }
        mas_adap.close();

        if(devver!=null)
            tv_versionnumber.setText("*"+devver);


        return view;


    }
    public boolean update_version(String devname,String version ){
        up_adap=new UpdateAdapter(getActivity());
        up_adap.open();
        boolean devversupdated=up_adap.update_dev_version(devname, version);
        up_adap.close();
        return  devversupdated;
    }

    public void Track_button_event(String catagoryname, String actionname, String labelname) {
        Tracker t = ((Edisonbro_AnalyticsApplication) getActivity().getApplication()).getDefaultTracker();

        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder()
                .setCategory(catagoryname)
                .setAction(actionname)
                .setLabel(labelname)
                .build());
    }

    public void popup_tocheck() {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder.setMessage("Are You Sure You Want To Activate Config Mode ").setCancelable(false)

                .setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        ButtonOut("919");
                        delaytime(300);
                        ButtonOut("920");

                    }
                });
        alertDialogBuilder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                ButtonOut("920");
                dialog.cancel();
            }
        });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    public void delaytime(int dlytime) {
        try {
            Thread.sleep(300);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    void ButtonOut(String bo) {

        //devno=StaticVariabes_div.devnumber;

        Toast.makeText(getActivity().getApplicationContext(), "devty" + StaticVariabes_div.devtyp + "  devno" + StaticVariabes_div.devnumber, Toast.LENGTH_SHORT);
        // logTimeStamp("Before executing time");
        if (Tcp_con.isClientStarted) {
            while (devno.length() < 4) devno = "0" + devno;
            while (roomno.length() < 2) roomno = "0" + roomno;
            String str = "0" + broadcastMsg + groupId + devno + roomno + bo + "000000000000000";
            StaticVariabes_div.log("out" + str, TAG1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            StaticVariabes_div.log("bout" + result + "$$$" + bo, TAG1);

            powerstatus = true;
            //  currtime=powertime();
            // Cc.tcpOUT(result, true);
            Tcp_con.WriteBytes(result);
        }
    }


    @Override
    public void read(final int type, final String stringData, final byte[] byteData) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
               try{
                receiveddata(type, stringData, byteData);
               }catch(Exception e){
                   e.printStackTrace();
               }


            }
        });
    }


    public void receiveddata(int msg, String data, byte[] bytestatus) {

        switch (msg) {
            case READ_BYTE:
                byte[] readBuf = bytestatus;
                final String readMessage = new String(readBuf, 0, readBuf.length);
                StaticVariabes_div.log("msg read :- " + data + " msg", TAG1);
                DataIn(readBuf);

                break;
            case READ_LINE:
                StaticVariabes_div.log("msg read A_s" + data, TAG1);
                readMessage2 = data;
                if (readMessage2.equals("*OK#")) {
                    mn_nav_obj.serv_status(true);
                    ButtonOut("920");
                }
                break;
            case ServStatus:
                final String ServerStatusB = data;
                StaticVariabes_div.log("serv status swb" + ServerStatusB, TAG1);
                if (ServerStatusB != null) {
                    if (ServerStatusB.equals("TRUE")) {
                        StaticStatus.Server_status_bool = true;
                        statusserv = true;
                        servpreviousstate = "TRUE";
                        nonetwork = false;
                        ButtonOut("920");

                    } else {
                        StaticStatus.Server_status_bool = false;
                        statusserv = false;
                        servpreviousstate = "FALSE";
                    }
                } else {
                    StaticStatus.Server_status_bool = false;
                    statusserv = false;
                    servpreviousstate = "FALSE";
                }

                mn_nav_obj.serv_status(statusserv);

                break;
            case signallevel:
                final String signallevelB = data;
                //  StaticVariabes_div.log("servsignallevel swb" + signallevelB, TAG1);
                if (signallevelB != null) {
                    sl = Integer.parseInt(signallevelB);
                    rs = signallevelB;

                    if ((StaticStatus.Network_Type.equals("TRUE") || (StaticStatus.Network_Type.equals("TRUE3G")))) {

                        mn_nav_obj.network_signal(sl, true);

                        if (StaticStatus.Network_Type.equals("TRUE3G") || StaticStatus.Network_Type.equals("NONET")) {
                            if (timer != null) {
                                timer.cancel();
                                timer = null;
                            }
                        }


                    } else {

                        mn_nav_obj.network_signal(sl, false);


                    }

                }
                break;
            case NetwrkType:
                final String RemoteB = data;
                StaticStatus.Network_Type = RemoteB;
                StaticVariabes_div.log("serv Remote swb" + RemoteB, TAG1);
                if (RemoteB.equals("TRUE")) {
                    nonetwork = false;
                    remoteconn = true;
                    remoteconn3g = false;
                    remoteconprevstate = "TRUE";

                    mn_nav_obj.network_signal(sl, remoteconn);

                    if (timer == null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            public void run() {
                                Tcp_con.rssirec();  // display the data

                            }
                        }, delay, period);
                    }
                } else if (RemoteB.equals("TRUE3G")) {
                    nonetwork = false;
                    remoteconn = true;
                    remoteconn3g = true;
                    remoteconprevstate = "TRUE3G";
                    nonetwork = false;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }

                    mn_nav_obj.network_signal(sl, remoteconn);


                } else if (RemoteB.equals("NONET")) {
                    statusserv = false;
                    servpreviousstate = "FALSE";
                    nonetwork = true;
                    if (timer != null) {
                        timer.cancel();
                        timer = null;
                    }
                    remoteconn = false;
                    remoteconn3g = false;

                    mn_nav_obj.network_signal(sl, remoteconn);

                } else {
                    nonetwork = false;
                    remoteconn = false;
                    remoteconn3g = false;
                    remoteconprevstate = "FALSE";
                    if (timer == null) {
                        timer = new Timer();
                        timer.scheduleAtFixedRate(new TimerTask() {
                            public void run() {
                                Tcp_con.rssirec();  // display the data

                            }
                        }, delay, period);
                    }

                    mn_nav_obj.network_signal(sl, remoteconn);

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
            case ERRUSER:
                final String erruser = data;
                StaticVariabes_div.log("erruser swb" + erruser, TAG1);

                if (erruser.equals("TRUE")) {
                    popup("INVALID USER/PASSWORD");
                    mn_nav_obj.serv_status(false);
                } else {

                }

                break;
        }
    }


    public void popup(String msg) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        alertDialogBuilder.setTitle("INFO");
        alertDialogBuilder
                .setMessage(msg)
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    void DataIn(byte[] byt) {
        StaticVariabes_div.log(" input len" + byt.length, TAG1);

        if (byt != null && byt.length == 32) {
            byte[] input = new byte[30];
            System.arraycopy(byt, 1, input, 0, ((byt.length) - 2));

            inp= new String(input);
            StaticVariabes_div.log("swb sett inp" + inp, TAG1);

            try {
               // inp = Blaster.ReadData(input, 1);
                StaticVariabes_div.log("swb inp" + inp, TAG1);
                String DevType = inp.substring(1, 4);
                String Dev = inp.substring(4, 8);
                String dimmin = inp.substring(13, 16);
                String dimmax = inp.substring(16, 19);
                char Identifier = inp.charAt(20);

                String Identify=""+Identifier;

                char v1 = '0',v2='0',v3='0',Fs1='0',Fs2='0',Fs3='0',Fs4='0';

                if(Identify.equals("A")){

                     v1 = inp.charAt(21);
                     v2 = inp.charAt(22);
                     v3 = inp.charAt(23);
                    StaticVariabes_div.log("version dev" + v1+v2+v3, TAG1);


                }else{
                     Fs1 = inp.charAt(21);
                     Fs2 = inp.charAt(22);
                     Fs3 = inp.charAt(23);
                     Fs4 = inp.charAt(24);
                }

                char cur = inp.charAt(25);
                char G1 = inp.charAt(27);
                char E1 = inp.charAt(28);
                char E2 = inp.charAt(29);

                StaticVariabes_div.log("swb sett cur" + cur, TAG1);
                StaticVariabes_div.log("swb sett G1" + G1, TAG1);
                StaticVariabes_div.log("swb sett E1" + E1, TAG1);
                StaticVariabes_div.log("swb sett E2" + E2, TAG1);

                StaticVariabes_div.log("DevType" + DevType + "devtypesett" + devtypesett, TAG1);
                StaticVariabes_div.log("Dev" + Dev + "devno" + devno, TAG1);


                if (DevType.equals(devtypesett)) {
                    if (Dev != null && Dev.equals(devno)) {
                        powerstatus = false;
                        StaticVariabes_div.log("device type - " + inp, TAG1);


                        StaticVariabes_div.log("inpfull " + inp, TAG1);

                        StaticVariabes_div.log("cur" + cur, TAG1);

                        StaticVariabes_div.log("switch status" + G1, TAG1);

                        StaticVariabes_div.log("E1" + E1, TAG1);
                        StaticVariabes_div.log("E2" + E2, TAG1);

                        Switchnumberstatus("" + G1);
                        switchstatus("S" + E1);
                        switchstatus2("S" + E2);

                        StaticVariabes_div.log("version " + Identify, TAG1);


                        if(Identify.equals("A")) {
                            StaticVariabes_div.log("version " + Identify, TAG1);
                            boolean upd=update_version(StaticVariabes_div.dev_name,v1 + "." + v2 + v3);
                            StaticVariabes_div.log("version upd" + upd, TAG1);

                            swbadap.open();
                            String devver = swbadap.getdevversion_frmdevno(StaticVariabes_div.devnumber);
                            swbadap.close();

                            mas_adap.open();
                            if (devver.equals("")) {
                                StaticVariabes_div.log("roomno null", TAG1);
                                devver = mas_adap.getdevversion_devno(StaticVariabes_div.devnumber);
                            }
                            mas_adap.close();

                            if(devver!=null)
                                tv_versionnumber.setText(devver);
                        }

                        if (devtypesett.equals("033") || devtypesett.equals("050") || devtypesett.equals("051")) {
                            if (cur == '1') {
                                b_curtinverse.setChecked(true);
                            } else {
                                b_curtinverse.setChecked(false);
                            }
                        }


                        if (devtypesett.equals("011") || devtypesett.equals("008")) {
                            if(Identify.equals("B")){
                                StaticVariabes_div.log("fan speed" + Identify, TAG1);

                                fspeed1=""+Fs1;
                                fspeed2=""+Fs2;
                                fspeed3=""+Fs3;
                                fspeed4=""+Fs4;

                                StaticVariabes_div.log("fspeed1 " + fspeed1+" fspeed2 "+fspeed2+" fspeed3 "+fspeed3+" fspeed4 "+fspeed4, TAG1);

                            }
                        }

                        if (devtypesett.equals("020")) {

                            dmr_min_b=dimmin;
                            dmr_max_b=dimmax;

                          //  Toast.makeText(getActivity().getApplicationContext(), dimmin+" data recieved "+dimmax, Toast.LENGTH_SHORT).show();

                        }

                    }
                }

            } catch (Exception e) {

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {
                        Toast.makeText(getActivity().getApplicationContext(), "Invalid data recieved", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    public void Statusupdate2(boolean serial, boolean hardware, boolean config, boolean manual) {

        b_hardware.setChecked(hardware);
        b_config.setChecked(config);
        b_manual_override.setChecked(manual);

    }

    private enum Statusset2 {
        S0, S1, S2, S3, S4, S5, S6, S7,
    }

    public void switchstatus2(String n) {
        StaticVariabes_div.log("Switch status 2", TAG1);
        switch (Statusset1.valueOf(n)) {
            case S0:
                StaticVariabes_div.log("swb 0", TAG1);
                Statusupdate2(false, false, false, false);
                break;

            case S1:
                Statusupdate2(true, false, false, false);
                StaticVariabes_div.log("swb 1", TAG1);
                break;

            case S2:

                StaticVariabes_div.log("swb 2", TAG1);
                Statusupdate2(false, true, false, false);
                break;

            case S3:
                StaticVariabes_div.log("swb 3", TAG1);
                Statusupdate2(true, true, false, false);
                break;

            case S4:
                StaticVariabes_div.log("swb 4", TAG1);
                Statusupdate2(false, false, true, false);
                break;

            case S5:
                StaticVariabes_div.log("swb 5", TAG1);
                Statusupdate2(true, false, true, false);
                break;

            case S6:
                StaticVariabes_div.log("swb 6", TAG1);
                Statusupdate2(false, true, true, false);
                break;

            case S7:
                StaticVariabes_div.log("swb 7", TAG1);
                Statusupdate2(true, true, true, false);
                break;

            case S8:
                StaticVariabes_div.log("swb 8", TAG1);
                Statusupdate2(false, false, false, true);
                break;

            case S9:
                StaticVariabes_div.log("swb 9", TAG1);
                Statusupdate2(true, false, false, true);
                break;

            case SA:
                StaticVariabes_div.log("swb A", TAG1);
                Statusupdate2(false, true, false, true);
                break;
            case SB:
                StaticVariabes_div.log("swb b", TAG1);
                Statusupdate2(true, true, false, true);
                break;

            case SC:
                StaticVariabes_div.log("swb C", TAG1);
                Statusupdate2(false, false, true, true);
                break;

            case SD:
                StaticVariabes_div.log("swb D", TAG1);
                Statusupdate2(true, false, true, true);
                break;

            case SE:
                StaticVariabes_div.log("swb E", TAG1);
                Statusupdate2(false, true, true, true);
                break;

            case SF:
                StaticVariabes_div.log("swbF", TAG1);
                Statusupdate2(true, true, true, true);
                break;
            default:
                System.out.println("Not matching");
        }
    }

    public void Switchnumberstatus(String gval) {
        sp_swbnum.setSelection(getIndex(sp_swbnum, gval));

        //private method of your class

    }

    public void switchstatus(String n) {
        StaticVariabes_div.log("Switch status", TAG1);
        switch (Statusset1.valueOf(n)) {
            case S0:
                StaticVariabes_div.log("swb 0", TAG1);
                Statusupdate(false, false, false, false);
                break;

            case S1:
                Statusupdate(true, false, false, false);
                StaticVariabes_div.log("swb 1", TAG1);
                break;

            case S2:
                StaticVariabes_div.log("swb 2", TAG1);
                Statusupdate(false, true, false, false);
                break;

            case S3:
                StaticVariabes_div.log("swb 3", TAG1);
                Statusupdate(true, true, false, false);
                break;

            case S4:
                StaticVariabes_div.log("swb 4", TAG1);
                Statusupdate(false, false, true, false);
                break;

            case S5:
                StaticVariabes_div.log("swb 5", TAG1);
                Statusupdate(true, false, true, false);
                break;

            case S6:
                StaticVariabes_div.log("swb 6", TAG1);
                Statusupdate(false, true, true, false);
                break;

            case S7:
                StaticVariabes_div.log("swb 7", TAG1);
                Statusupdate(true, true, true, false);
                break;

            case S8:
                StaticVariabes_div.log("swb 8", TAG1);
                Statusupdate(false, false, false, true);
                break;

            case S9:
                StaticVariabes_div.log("swb 9", TAG1);
                Statusupdate(true, false, false, true);
                break;

            case SA:
                StaticVariabes_div.log("swb A", TAG1);
                Statusupdate(false, true, false, true);
                break;
            case SB:
                StaticVariabes_div.log("swb b", TAG1);
                Statusupdate(true, true, false, true);
                break;

            case SC:
                StaticVariabes_div.log("swb C", TAG1);
                Statusupdate(false, false, true, true);
                break;

            case SD:
                StaticVariabes_div.log("swb D", TAG1);
                Statusupdate(true, false, true, true);
                break;

            case SE:
                StaticVariabes_div.log("swb E", TAG1);
                Statusupdate(false, true, true, true);
                break;

            case SF:
                StaticVariabes_div.log("swbF", TAG1);
                Statusupdate(true, true, true, true);
                break;

            default:
                System.out.println("Not matching");
        }

    }

    private int getIndex(Spinner spinner, String myString) {
        int index = 0;

        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).toString().equalsIgnoreCase(myString)) {
                index = i;
                break;
            }
        }
        return index;
    }

    public void Statusupdate(boolean memory, boolean touch, boolean buzzer, boolean ir) {
        b_memory.setChecked(memory);
        b_touch.setChecked(touch);
        b_buzzer.setChecked(buzzer);
        b_ir.setChecked(ir);

    }

    private enum Statusset1 {
        S0, S1, S2, S3, S4, S5, S6, S7, S8, S9, SA, SB, SC, SD, SE, SF
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }




    public void Setdmr_val() {

        inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_dmr_bright, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        // dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel = (Button) alertLayout.findViewById(R.id.cancel);
        save = (Button) alertLayout.findViewById(R.id.save);
        b_resetbright = (Button) alertLayout.findViewById(R.id.reset_dmr_bright);

        et_min = (EditText) alertLayout.findViewById(R.id.et_min);
        et_max = (EditText) alertLayout.findViewById(R.id.et_max);


        try {
           // dmr_min_br = Integer.parseInt(dmr_min_b);
           // dmr_max_br = Integer.parseInt(dmr_max_b);

            Toast.makeText(view.getContext(), dmr_min_b+" values  in popup"+dmr_max_b, Toast.LENGTH_LONG).show();

            et_min.setText(dmr_min_b);
            et_max.setText(dmr_max_b);

        }catch(Exception e){
         e.printStackTrace();
        }

        b_resetbright.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ButtonOut_brghtnss("937","000","000");
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                dmr_min_br=Integer.parseInt(et_min.getText().toString());
                dmr_max_br=Integer.parseInt(et_max.getText().toString());



                if (dmr_min_br != 0 || dmr_max_br != 0) {


                    boolean checksort = false;

                    if(dmr_min_br>dmr_max_br){
                        checksort=false;
                    }else{
                        checksort=true;
                    }

                    String input[]=new String[2];
                    input[0]="" + dmr_min_br;
                    input[1]="" + dmr_max_br;

                    Toast.makeText(view.getContext(), dmr_min_br+" values "+dmr_max_br, Toast.LENGTH_LONG).show();


                    boolean checkduplicates=checkDuplicateUsingSet(input);

                    String dmr_min_br_str="" + dmr_min_br;
                    String dmr_max_br_str="" + dmr_max_br;

                     while(dmr_min_br_str.length()<3){
                         dmr_min_br_str="0"+dmr_min_br_str;
                     }

                    while(dmr_max_br_str.length()<3){
                        dmr_max_br_str="0"+dmr_max_br_str;
                    }


                    if (checksort && !checkduplicates) {

                       ButtonOut_brghtnss("936",dmr_min_br_str,dmr_max_br_str);
                        dialog1.dismiss();

                    }else if (checksort && checkduplicates) {
                        Toast.makeText(view.getContext(), "Duplicate values not allowed", Toast.LENGTH_LONG).show();


                    }else if (!checksort && !checkduplicates) {

                        Toast.makeText(view.getContext(), "min value cannot be higher than max value", Toast.LENGTH_LONG).show();

                    }else if (!checksort && checkduplicates) {

                        Toast.makeText(view.getContext(), "Set min value less than max value ,Duplicate values not allowed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Set values", Toast.LENGTH_LONG).show();
                }


            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                dmr_min_br = 0;
                dmr_max_br = 0;
            }
        });
    }

    public void Setpwm_fan() {

        inflater = getActivity().getLayoutInflater();
        View alertLayout = inflater.inflate(R.layout.popup_pwmfansetting, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        // dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        cancel = (Button) alertLayout.findViewById(R.id.cancel);

        save = (Button) alertLayout.findViewById(R.id.save);

        btn_1_inc = (Button) alertLayout.findViewById(R.id.btn_1_plus);
        btn_1_dec = (Button) alertLayout.findViewById(R.id.btn_1_minus);

        btn_2_inc = (Button) alertLayout.findViewById(R.id.btn_2_plus);
        btn_2_dec = (Button) alertLayout.findViewById(R.id.btn_2_minus);

        btn_3_inc = (Button) alertLayout.findViewById(R.id.btn_3_plus);
        btn_3_dec = (Button) alertLayout.findViewById(R.id.btn_3_minus);

        btn_4_inc = (Button) alertLayout.findViewById(R.id.btn_4_plus);
        btn_4_dec = (Button) alertLayout.findViewById(R.id.btn_4_minus);


        et_one = (TextView) alertLayout.findViewById(R.id.et_1);
        et_two = (TextView) alertLayout.findViewById(R.id.et_2);
        et_three = (TextView) alertLayout.findViewById(R.id.et_3);
        et_four = (TextView) alertLayout.findViewById(R.id.et_4);



        StaticVariabes_div.log("fspeed1"+fspeed1 , TAG1);
        StaticVariabes_div.log("fspeed2"+fspeed2 , TAG1);
        StaticVariabes_div.log("fspeed3"+fspeed3 , TAG1);
        StaticVariabes_div.log("fspeed4"+fspeed4 , TAG1);

        try {
            speed1 = Integer.parseInt(fspeed1);
            speed2 = Integer.parseInt(fspeed2);
            speed3 = Integer.parseInt(fspeed3);
            speed4 = Integer.parseInt(fspeed4);

            et_one.setText(fspeed1);
            et_two.setText(fspeed2);
            et_three.setText(fspeed3);
            et_four.setText(fspeed4);
        }catch(Exception e){

        }


        btn_1_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (speed1 < 6) {
                    speed1 = speed1 + 1;
                    String mint = String.valueOf(speed1);
                    et_one.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value below 7", Toast.LENGTH_SHORT).show();
                }


            }
        });
        btn_1_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed1 > 1) {
                    speed1 = speed1 - 1;
                    String mint = String.valueOf(speed1);
                    et_one.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value above 1", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_2_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed2 < 7) {
                    speed2 = speed2 + 1;
                    String mint = String.valueOf(speed2);
                    et_two.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value below 8", Toast.LENGTH_SHORT).show();
                }
            }
        });
        btn_2_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed2 > 1) {
                    speed2 = speed2 - 1;
                    String mint = String.valueOf(speed2);
                    et_two.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value above 1", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_3_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed3 < 8) {
                    speed3 = speed3 + 1;
                    String mint = String.valueOf(speed3);
                    et_three.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value below 9", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_3_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed3 > 1) {
                    speed3 = speed3 - 1;
                    String mint = String.valueOf(speed3);
                    et_three.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value above 1", Toast.LENGTH_SHORT).show();
                }

            }
        });


        btn_4_inc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (speed4 < 9) {
                    speed4 = speed4 + 1;
                    String mint = String.valueOf(speed4);
                    et_four.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value below 10", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_4_dec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (speed4 > 1) {
                    speed4 = speed4 - 1;
                    String mint = String.valueOf(speed4);
                    et_four.setText(mint);
                } else {
                    Toast.makeText(view.getContext(), "Select value above 1", Toast.LENGTH_SHORT).show();
                }

            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (speed1 != 0 || speed2 != 0 || speed3 != 0 || speed4 != 0) {

                    ArrayList<String> al = new ArrayList<String>();
                    al.add("" + speed1);
                    al.add("" + speed2);
                    al.add("" + speed3);
                    al.add("" + speed4);
                    boolean checksort = checkSorting(al);

                    String input[]=new String[4];
                    input[0]="" + speed1;
                    input[1]="" + speed2;
                    input[2]="" + speed3;
                    input[3]="" + speed4;


                    boolean checkduplicates=checkDuplicateUsingSet(input);


                    if (checksort && !checkduplicates) {

                        ButtonOut_speed("" + speed1, "" + speed2, "" + speed3, "" + speed4);

                        delaytime(300);
                        ButtonOut("935");
                        dialog1.dismiss();

                    }else if (checksort && checkduplicates) {
                        Toast.makeText(view.getContext(), "Duplicate values not allowed", Toast.LENGTH_LONG).show();


                    }else if (!checksort && !checkduplicates) {

                        Toast.makeText(view.getContext(), "Set values in incremental order", Toast.LENGTH_LONG).show();

                    }else if (!checksort && checkduplicates) {

                        Toast.makeText(view.getContext(), "Set values in incremental order ,Duplicate values not allowed", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(view.getContext(), "Set values", Toast.LENGTH_LONG).show();
                }


            }
        });


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                speed1 = 0;
                speed2 = 0;
                speed3 = 0;
                speed4 = 0;
            }
        });
    }

    public static boolean checkDuplicateUsingSet(String[] input) {
        List inputList = Arrays.asList(input);
        Set inputSet = new HashSet(inputList);
        if (inputSet.size() < inputList.size())
            return true;


        return false;
    }


    boolean checkSorting(ArrayList< String > arraylist){
        boolean isSorted=true;
        for(int i=1;i < arraylist.size();i++){
            if(arraylist.get(i-1).compareTo(arraylist.get(i)) > 0){
                isSorted= false;
                break;
            }
        }
        return isSorted;
    }


    void ButtonOut_speed(String sp1,String sp2,String sp3,String sp4) {

        //devno=StaticVariabes_div.devnumber;

        Toast.makeText(getActivity().getApplicationContext(),"devty"+StaticVariabes_div.devtyp+"  devno"+StaticVariabes_div.devnumber,Toast.LENGTH_SHORT);
        // logTimeStamp("Before executing time");
        if (Tcp_con.isClientStarted) {
            while (devno.length() < 4)devno = "0" + devno;
            while(roomno.length()<2)roomno="0"+roomno;
            String str = "0" + broadcastMsg + groupId + devno +roomno+ "997" +sp1+sp2+sp3+sp4+ "00000000000";
            StaticVariabes_div.log("out" + str, TAG1);
            byte[] op= str.getBytes();//Blaster.WriteData(str, 1);
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
           // StaticVariabes_div.log("bout" + result + "$$$" + bo, TAG1);

            powerstatus=true;
            //  currtime=powertime();
            // Cc.tcpOUT(result, true);
            Tcp_con.WriteBytes(result);
        }
    }

    void ButtonOut_brghtnss(String bo,String br_min,String br_max) {


        Toast.makeText(getActivity().getApplicationContext(),"devty"+StaticVariabes_div.devtyp+"  devno"+StaticVariabes_div.devnumber,Toast.LENGTH_SHORT);

        if (Tcp_con.isClientStarted) {
            while (devno.length() < 4)devno = "0" + devno;
            while(roomno.length()<2)roomno="0"+roomno;
            String str = "0" + broadcastMsg + groupId + devno +roomno+ bo +br_min+br_max+ "000000000";
            StaticVariabes_div.log("out" + str, TAG1);
            byte[] op= str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            // StaticVariabes_div.log("bout" + result + "$$$" + bo, TAG1);
            powerstatus=true;
            Tcp_con.WriteBytes(result);
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

    public void onResume() {
        super.onResume();
        Log.i(TAG1, "Setting screen name: " + name);
        //using tracker variable to set Screen Name
        mTracker.setScreenName(name);
        //sending the screen to analytics using ScreenViewBuilder() method
        mTracker.send(new HitBuilders.ScreenViewBuilder().build());
    }
}
