package edisonbro.com.edisonbroautomation;


/**
 *  FILENAME: CombFrag.java
 *  DATE: 07-08-2018

 *  DESCRIPTION: Combined Fragment displaying room names and devices and to set mood and timer .

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 *
 *
 *  functions:
 *  transmitdata : To transmit rgb data through tcp.
 *  Track_button_event : To track event on button click.
 *  changebutton :  To update response of rgb in ui
 *  receiveddata : To receive data from tcp.
 *  Datain : To process the data received from tcp.
 *  popup : popup to display info.
 */

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.app.Fragment;
import android.os.StrictMode;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import edisonbro.com.edisonbroautomation.Connections.Http_Connect;
import edisonbro.com.edisonbroautomation.Connections.Tcp_con;
import edisonbro.com.edisonbroautomation.Devices.A_rc;
import edisonbro.com.edisonbroautomation.Devices.Ac_Frag;
import edisonbro.com.edisonbroautomation.Devices.Alexa_Frag;
import edisonbro.com.edisonbroautomation.Devices.Curtain_Frag;
import edisonbro.com.edisonbroautomation.Devices.Curtain_Group_Frag;
import edisonbro.com.edisonbroautomation.Devices.Curtain_Main_Fragment;
import edisonbro.com.edisonbroautomation.Devices.Device_Feature_Settings;
import edisonbro.com.edisonbroautomation.Devices.Dimmer_Frag;
import edisonbro.com.edisonbroautomation.Devices.Dimmer_Group_Frag;
import edisonbro.com.edisonbroautomation.Devices.Dimmer_Main_Frag;
import edisonbro.com.edisonbroautomation.Devices.GardenSprinkler_Frag;
import edisonbro.com.edisonbroautomation.Devices.GardenSprinkler_Group;
import edisonbro.com.edisonbroautomation.Devices.Garden_Main_Frag;
import edisonbro.com.edisonbroautomation.Devices.Pir_Frag;
import edisonbro.com.edisonbroautomation.Devices.Pir_Group_Frag;
import edisonbro.com.edisonbroautomation.Devices.Pir_Main_Frag;
import edisonbro.com.edisonbroautomation.Devices.ProjectorScreen_Fragment;
import edisonbro.com.edisonbroautomation.Devices.Rgb_Frag;
import edisonbro.com.edisonbroautomation.Devices.Rgb_Group_Frag;
import edisonbro.com.edisonbroautomation.Devices.Rgb_Main_Frag;
import edisonbro.com.edisonbroautomation.Devices.SlideGate_Fragment;
import edisonbro.com.edisonbroautomation.Devices.SmartDog_Frag;
import edisonbro.com.edisonbroautomation.Devices.SomphtScreen_Fragment;
import edisonbro.com.edisonbroautomation.Devices.SwingGate_Fragment;
import edisonbro.com.edisonbroautomation.Devices.SwitchBoard_All_Models_Frag;
import edisonbro.com.edisonbroautomation.Sdlv.Menu;
import edisonbro.com.edisonbroautomation.Sdlv.MenuItem;
import edisonbro.com.edisonbroautomation.Sdlv.SlideAndDragListView;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticStatus;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariables;
import edisonbro.com.edisonbroautomation.blaster.Blaster;
import edisonbro.com.edisonbroautomation.connectionswirelessir.Tcp;
import edisonbro.com.edisonbroautomation.databasewired.CamAdapter;
import edisonbro.com.edisonbroautomation.databasewired.LocalDatabaseHelper;
import edisonbro.com.edisonbroautomation.databasewired.LocalListArrangeTable;
import edisonbro.com.edisonbroautomation.databasewired.MasterAdapter;
import edisonbro.com.edisonbroautomation.databasewired.PredefsettngAdapter;
import edisonbro.com.edisonbroautomation.databasewired.ServerDetailsAdapter;
import edisonbro.com.edisonbroautomation.databasewired.SwbAdapter;
import edisonbro.com.edisonbroautomation.databasewired.UserTableAdapter;
import edisonbro.com.edisonbroautomation.databasewireless.IRB_Adapter_ir;
import edisonbro.com.edisonbroautomation.model.QQ;
import edisonbro.com.edisonbroautomation.scrollfunction.ShaderSeekArc;
import edisonbro.com.edisonbroautomation.usersettings.UserSettingActivity;


public class CombFrag extends Fragment implements AdapterView.OnItemLongClickListener, AdapterView.OnItemClickListener,
        SlideAndDragListView.OnDragListener, SlideAndDragListView.OnSlideListener,
        SlideAndDragListView.OnMenuItemClickListener, SlideAndDragListView.OnItemDeleteListener {

    private static final String TAG1="Combfrag - ";
    //************************************************************************
    public String devnoarr[],roomnoarr[],housenoarr[],masterroomname[],camroomname[],irroomname[], roomnameaccessArray[],
            roomnoaccessArray[],roomdevtypeaccessArray[],roommodtypeaccessArray[],roomgrouptypeaccessArray[],roomdevnamaccessArray[];
    String Loggeduser="A";
    //*******************************************************************************
    private SwbAdapter swbadap;
    private MasterAdapter mas_adap;
    private CamAdapter cam_adap;
    private IRB_Adapter_ir ir_adap;
    UserTableAdapter usr_adap;
    LocalListArrangeTable locallist_adap;
    PredefsettngAdapter pre_adap;
    private ServerDetailsAdapter sdadap;
    LocalDatabaseHelper dbhelp=null;

    private Tracker mTracker;
    String name ="EB Home";
    Main_Navigation_Activity mn_nav_obj;
    //**********************************************************************************
    int n=0,count=0,i=0;
    int countmastr=0,countcam=0,countir=0;
    String roomnamesdb[],imgnamesdb[],pressedimgsname[];
    int startpos,endos , previmg=0,prev_position=0;
    //********************************************************************
    Button btnwtype,btnconstatus,btsig;
    private Toast mToast;
    GridView gridView,gridtest;
    LinearLayout linear;
    View view,Swipe_lay;

    private ImageView prevselectedView = null;
    private  TextView prevselectedTextView=null;
    LinearLayout ly_slider;
    ImageView image_view,array_image[];
    Button btn_features_sett,btn_timer_sett,btn_features_back;


    FragmentTransaction fragmentTransaction1;
    FragmentManager fragmentManager;
    Fragment frag_previous;
    String frag_prev_id;
    FrameLayout fr_layt;
    LinearLayout lin_frag;
    //********************************************************************
    private static final String TAG = CombFrag.class.getSimpleName();
    private List<Menu> mMenuList;
    private List<QQ> mQQList;
    private SlideAndDragListView<QQ> mListView;

    public static ArrayList<Integer> listiconpressed;
    private ArrayList<Integer> listicon;
    private ArrayList<String> listname;

    String swdnoarr[], swdtypearr[],swdmodeltypearr[],swddevicenamearr[]
            ,swbnoarr[], swbtypearr[],swbmodeltypearr[],swbdevicenamearr[]
            ,rgbnoarr[],rgbtypearr[], rgbmodeltypearr[],rgbdevicenamearr[]
            ,dimmrnoarr[],dimmrtypearr[],dimmrmodeltypearr[],dimmrdevicenamearr[]
            ,curnoarr[],curtypearr[],curmodeltypearr[],curdevicenamearr[]
            ,projscrnoarr[],projscrtypearr[],projscrmodeltypearr[],projscrdevicenamearr[]
            ,projliftnoarr[], projlifttypearr[],projliftmodeltypearr[]
            ,wtrpmpnoarr[], wtrpmptypearr[], wtrpmpmodeltypearr[]
            ,fannoarr[], fantypearr[],fanmodeltypearr[],fandevicenamearr[]
            , camnoarr[],camtypearr[]
            ,irnoarr[], irtypearr[] , irmodeltypearr[]
            , gysernoarr[],gysertypearr[],gysermodeltypearr[],gyserdevicenamearr[]
            , acnoarr[],actypearr[], acmodeltypearr[],acdevicenamearr[]
            , pirnoarr[],pirtypearr[],pirmodeltypearr[],pirdevicenamearr[]
            ,gsknoarr[],gsktypearr[],gskmodeltypearr[],gskdevicenamearr[]
            , clbnoarr[],clbtypearr[],clbmodeltypearr[],clbdevicenamearr[]
            , dlsnoarr[],dlstypearr[],dlsmodeltypearr[],dlsdevicenamearr[]
            , fmnoarr[],fmtypearr[],fmmodeltypearr[],fmdevicenamearr[]
            ,aqunoarr[],aqutypearr[], aqumodeltypearr[]
           , smrtdognoarr[],smrtdogtypearr[], smrtdogmodeltypearr[],smrtdogdevicenamearr[]
            ,alexanoarr[],alexatypearr[], alexamodeltypearr[],alexadevicenamearr[],
            sompnoarr[],somptypearr[], sompmodeltypearr[],sompdevicenamearr[],
            slidenoarr[], slidetypearr[],  slidemodeltypearr[], slidedevicenamearr[],
            swingnoarr[],swingtypearr[], swingmodeltypearr[],swingdevicenamearr[]
            ;

    //********************************************************************
    private ArrayList<String> listswbno,listswbmod,listswbdevnam
            , listdmrno, listdmrmod, listdmrdevnam,listrgbno, listrgbmod,listrgbdevnam
            , listcurtno,listcurtmod,listcurtdevnam, listpscno, listpscmod,listpscdevnam
            , listplcno, listplcmod, listplcdevnam, listirno, listirmod,listirdevnam
            , listcamno, listcammod,listcamdevnam, listaquno, listaqumod,listaqudevnam
            , listfmno, listfmmod,listfmdevnam, listacno, listacmod, listacdevnam
            , listgsrno, listgsrmod,listgsrdevnam ,listgskno, listgskmod,listgskdevnam
            , listclbno, listclbmod, listclbdevnam, listdlsno, listdlsmod, listdlsdevnam
            , listfanno,listfanmod,listfandevnam,listwpcno,listwpcmod,listwpcdevnam
            , listpirno,listpirmod,listpirdevnam,listsdgno,listsdgmod,listsdgdevnam
            , listswdno,listswdmod,listswddevnam,listalexano,listalexamod,listalexadevnam,
            listsompycurtno,listsompycurtmod,listsompycurtdevnam
            ,listslidecurtno,listslidecurtmod,listslidecurtdevnam
            ,listswingcurtno,listswingcurtmod,listswingcurtdevnam
            ;

    //********************************************************************

    private static final int SWIPE_MIN_DISTANCE = 40;
    private static final int SWIPE_MAX_OFF_PATH = 250;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    private GestureDetector gestureDetector;
    //********************************************************************

    String Selected_gridtype;
    String[] groupdevnos,ids,devexists, groupuni,groupexists,groupdev_models,groupdev_names;
    String room_num;

    public enum types_dev {
        SwbI, SwbG, RgbI, RgbG, PirI, PirG, AcI, DmrI, DmrG , SdgI , CurI,SOSHI,SLG1I,SWG1I ,CurG , PscI ,GskI, GskG, AlxI
    };
    Typeface fontsanfrans;
    int Device_position,Devices_count;
    String Devices_num[], Devices_model_arr[], Devices_Name_arr[];

/////////////////////Timer/////////////////////////////////////////////////////////////////
    View timer_popup_view,main_layout_view;
    TextView tv_ontime,tv_offtime,tv_setoperation;
    Button b_sunday,b_monday,b_tuesday,b_wednesday,b_thursday,b_friday,b_saturday,
            b_setoperation,b_settimer,b_timerlist,b_tmr_pop_cancel;
    LinearLayout lay_repeat_linr_days;
    RadioButton rb_repeat_ondays,rb_cyclic,rb_selecteddate;//rb_repeat_ptern;
    CheckBox cb_repeat_ptern;
    LinearLayout lay_rep_ondays_A,lay_rep_ondays_B,lay_cyclic_A,lay_cyclic_B,
            lay_sel_date_A,lay_sel_date_B,lay_timerlist,lay_setoperation;
    NumberPicker np_frmtime_hrs,np_frmtime_min,np_totime_hrs,np_totime_min,
            np_ontime_cyc, np_offtime_cyc, np_date_seldate, np_month_seldate , np_year_seldate;
    Button btnarr_weekdays[];
    NumberPicker np_arr[];
    LinearLayout lay_all_arr[];
    private String[] displayedValues;
    String seldate_year,seldate_month,seldate_day;
    Button s1,s2,s3,s4,s5,s6,s7,s8,fan_dec,fan_inc,save,cancel,btn_moodlist;
    Spinner  fan_speed;
    ArrayList<Object> bulbon,bulbon_moodsett;
    Button btnarr_timer_switchs[];
    int int_Frm_time_hrs,int_Frm_time_min,int_To_time_hrs,int_To_time_min,int_Day_Selected;
    String Frm_time_hrs,Frm_time_min,To_time_hrs,To_time_min,Frm_Time,To_Time;

    private String fanspeed,rep_weekly,off_data,on_data,Timer_Data,TimerType,
            Day_Selected,Month_Selected,Year_Selected,Date_Selected;
    List ar_num_switch = new ArrayList();
    List ar_off_data = new ArrayList();
    List ar_on_data;//= new ArrayList();
    String devn,on_time_cyclic,off_time_cyclic;
    android.support.v7.app.AlertDialog dialog1;
    android.support.v7.app.AlertDialog dialog;
    int arr_weekdata[];

    public Integer[] days_images_grey2={R.drawable.week_mon_01,R.drawable.week_tues_thur_01,R.drawable.week_wed_01,
            R.drawable.week_tues_thur_01,R.drawable.week_fri_01,R.drawable.week_sun_sat_01,R.drawable.week_sun_sat_01};

    public Integer[] days_images_blue={R.drawable.week_sun_sat_02,R.drawable.week_mon_02,R.drawable.week_tues_thur_02,R.drawable.week_wed_02,
            R.drawable.week_tues_thur_02,R.drawable.week_fri_02,R.drawable.week_sun_sat_02};
    LayoutInflater inflater;

    ///////////popup rgb//////////////////////////////////////////////////////////
    private A_rc colorPicker;
    private Button btn_Red, btn_Green, btn_Blue, btn_Pink, btn_White,
            btn_orange,btn_Flash, btn_Strobe, btn_Fade, btn_Smooth, btn_Speedp,btn_Speedm,btn_on,btn_off;
    private TextView rgbvalue,tv_speedval,tvtest,tv_rgbvalue;
    SeekBar sk;
    String rgbinst,rgbbrightinst,rgb_color_effect,rgbbright,rgb_speed,rgb_speedinst,rgb_onoff_repeat,
            diminst,dimvalue,dmr_onoff_repeat,final_on_off_rgb,initt_rgbvalue,rgbspeedinst,rgb_moodsett_bright,final_on_off_dmr,
            final_on_off_pir,final_on_off_ligsen,curtinst,curtinst2,curtval,curtval2;
    int ispeed=120;
    ToggleButton tb_on_off;
    //////////////////pop dmr//////////////////////////////////////////////////
    public ImageView dim_img;
    ShaderSeekArc seekArc;
    Button btn_low,btn_medium,btn_high,btn_mood1_sett,btn_mood2_sett,btn_mood3_sett;
    String ondatadmr,offdatadmr;

    public static Integer[] mThumbIds = {

            R.drawable.dim_level0,R.drawable.dim_level1,R.drawable.dim_level2,R.drawable.dim_level3,R.drawable.dim_level4,R.drawable.dim_level5,R.drawable.dim_level6,
            R.drawable.dim_level7, R.drawable.dim_level8, R.drawable.dim_level9,
    };

    //////////////////pop cur//////////////////////////

    Button btn_open_sheer,btn_close_sheer,btn_open_curtain,btn_close_curtain;

    CheckBox cb_sheer_select,cb_curtain_select,cb_pir_select,cb_ligsen_select;

    /////////////////////////Mood Settings/////////////////////////////////////////////
    String MoodNumber_Sel,bulbs_to_on="",fan_value,final_bulbs_value;
    ProgressDialog progressBar;
   // AlertDialog dialog ;
    String status_swb,status_dmr,status_cur,status_rgb,status_pir;
    boolean Mood_longPress;


    Integer Normalgridimgs[],Pressedgridimgs[];
    View tempview,tempview_room,imgselgrid_view;
    int grid_prev_position=0;
    String newappPass,newappuser,logintype;

    ///////////////////////////////////////////////////////////////////////////////


    boolean load_first=false;
    String roomname_pressed=null;
    AlertDialog alertDialog=null;
    int mn=0;

    static String encryptionKey = "edisonbrosmartlabs";

    /////////////////

    //MyAppAdapter mAdapter;
/*
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";*/

 /*   // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
*/
//    private CombFrag.OnFragmentInteractionListener mListener;

    public CombFrag() {


        // Required empty public constructor
    }

/*
    // TODO: Rename and change types and number of parameters
    public static CombFrag newInstance(String param1, String param2) {
        CombFrag fragment = new CombFrag();
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
*/
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        view = inflater.inflate(R.layout.combined_frag,container, false);

        mn_nav_obj=(Main_Navigation_Activity) getActivity();

        fontsanfrans = Typeface.createFromAsset(getActivity().getApplicationContext().getAssets(), "fonts/San FranciscoRegular.ttf");
        btnconstatus=(Button) view.findViewById(R.id.btnconstatus);
        btsig=(Button) view.findViewById(R.id.btnsignal);

        ly_slider= (LinearLayout) view.findViewById(R.id.lay_slider);
        linear = (LinearLayout) view.findViewById(R.id.linear);
        fragmentManager =getChildFragmentManager(); //getFragmentManager();

        fr_layt= (FrameLayout) view.findViewById(R.id.fragment_place);
        lin_frag = (LinearLayout) view.findViewById(R.id.linear_fragment_place);

        btn_features_sett= (Button) view.findViewById(R.id.btnfeatures_sett);
        btn_timer_sett= (Button) view.findViewById(R.id.btntimer_sett);
        btn_features_back = (Button) view.findViewById(R.id.btnfeatures_back);

        btn_mood1_sett= (Button) view.findViewById(R.id.btnmood1);
        btn_mood2_sett= (Button) view.findViewById(R.id.btnmood2);
        btn_mood3_sett = (Button) view.findViewById(R.id.btnmood3);


        btn_features_sett.setVisibility(View.INVISIBLE);
        btn_timer_sett.setVisibility(View.INVISIBLE);

        dbhelp=new LocalDatabaseHelper(getActivity().getApplicationContext());
        sdadap=new ServerDetailsAdapter(getActivity().getApplicationContext());


        dbhelp.opendb();
        int housecount=dbhelp.getCount();
       if(housecount>0) {
            Cursor cursd = dbhelp.fetch_logintype(StaticVariabes_div.housenumber);

            if (cursd != null)
                logintype = cursd.getString(cursd.getColumnIndexOrThrow(LocalDatabaseHelper.LOGIN_ACCESS));
            dbhelp.close();


            if (logintype.equals("M")) {

                Loggin_Account();

            } else if (logintype.equals("AU")) {

                if ((StaticVariabes_div.housename == null) || (StaticVariabes_div.housename == "") || (StaticVariabes_div.housename.equals(""))) {


                } else {


                    initDataBases();
                    fetchvalues_fromdatabase();
                    fetchhomeref();
                    fetchversionDb();
                    initData();
                    initMenu();
                    initUiAndListener();

                }


            }

        }


        startZoominAnimation(linear);
        gridView = (GridView) view.findViewById(R.id.gridView);


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                tempview = parent.getChildAt(grid_prev_position);
                if(tempview!=null) {
                    StaticVariabes_div.log(" in first grid_prev_position" + grid_prev_position, TAG1);
                    ImageView imgprev = (ImageView) tempview.findViewById(R.id.grid_image);
                    imgprev.setImageResource(Normalgridimgs[grid_prev_position]);
                }else{

                    tempview = parent.getChildAt(0);
                    StaticVariabes_div.log(" in second grid_prev_position" + grid_prev_position, TAG1);
                    if(tempview!=null) {
                        ImageView imgprev = (ImageView) tempview.findViewById(R.id.grid_image);
                        imgprev.setImageResource(Normalgridimgs[0]);
                    }
                }

                imgselgrid_view= parent.getChildAt(position);
                ImageView img = (ImageView) imgselgrid_view.findViewById(R.id.grid_image);
                img.setImageResource(Pressedgridimgs[position]);
                grid_prev_position =position;

                if(position!=0) {
                    tempview = parent.getChildAt(0);
                    StaticVariabes_div.log(" in third grid_prev_position" + grid_prev_position, TAG1);
                    if (tempview != null) {
                        ImageView imgprev = (ImageView) tempview.findViewById(R.id.grid_image);
                        imgprev.setImageResource(Normalgridimgs[0]);
                    }
                }


                Selected_gridtype=listname.get(position);
                buttonAction(listname.get(position));


            }
        });



        gestureDetector = new GestureDetector(new MyGestureDetector());

        Swipe_lay=(View) view.findViewById(R.id.fragment_place);


        Swipe_lay.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (gestureDetector.onTouchEvent(event)) {
                    return true;
                }
                return false;
            }
        });

        mToast = Toast.makeText(getActivity().getApplicationContext(), "", Toast.LENGTH_SHORT);



        btn_features_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                StaticVariabes_div.Selected_device=Selected_gridtype;

                Track_bulb_event("Device Features & Settings", Selected_gridtype, Selected_gridtype+" Device Features & Settings");

                if(StaticVariabes_div.devtyp.equals("CLNR")){
                    popupsheer_curtain();
                }else {

                    loadf();

                }

            }
        });


        btn_features_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String fragidfirst;
                Fragment fragtoviewfirst=null;


                fragtoviewfirst= frag_previous;
                fragidfirst=frag_prev_id;

                if(fragmentTransaction1!=null)
                {


                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.replace(R.id.fragment_place, fragtoviewfirst); // f1_container is your FrameLayout container
                    fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                    fragmentTransaction1.addToBackStack(null);
                    fragmentTransaction1.commit();
                }
                else
                {

                    fragmentTransaction1 = fragmentManager.beginTransaction();
                    fragmentTransaction1.add(R.id.fragment_place, fragtoviewfirst, fragidfirst);
                    fragmentTransaction1.commit();

                }

            }
        });


        btn_timer_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Track_bulb_event("Timer Operation", Selected_gridtype, Selected_gridtype+" Add Timer");
                timer_popup();


            }
        });
        btn_mood1_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.contains("G"))) {
                    MoodNumber_Sel = "1";
                    Apply_moodsett();
                    Track_bulb_event("Mood 1 Press", "Mood 1 Apply", "Mood 1 shortclick");
                }else{
                    Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();
                }
                } else {
                    Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
                }
            }
        });



        btn_mood1_sett.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.contains("G"))) {
                    if(Mood_longPress) {
                        MoodNumber_Sel = "1";
                        StaticVariabes_div.Mood_sel_number = MoodNumber_Sel;
                        Moodsett_modelselection(StaticVariabes_div.devtyp, "mood1", "1");
                        Track_bulb_event("Mood 1 LongPress", "Add Mood 1", "Mood 1 longclick");
                    }

                }else{
                    Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
            }
                return false;
            }
        });



        btn_mood2_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.equals("G"))) {

                    MoodNumber_Sel = "2";
                    Apply_moodsett();
                    Track_bulb_event("Mood 2 Press", "Mood 2 Apply", "Mood 2 shortclick");
                }else{
                    Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();

                }
        } else {
            Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
        }

            }
        });



        btn_mood2_sett.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.equals("G"))) {
                    if (Mood_longPress) {
                        MoodNumber_Sel = "2";
                        StaticVariabes_div.Mood_sel_number = MoodNumber_Sel;
                        Moodsett_modelselection(StaticVariabes_div.devtyp, "mood2", "2");
                        Track_bulb_event("Mood 2 LongPress", "Add Mood 2", "Mood 2 longclick");
                    }
                }else{
                    Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();

                }

        } else {
        Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
        }
                return false;
            }
        });


        btn_mood3_sett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.equals("G"))) {
                    MoodNumber_Sel = "3";
                    Apply_moodsett();
                    Track_bulb_event("Mood 3 Press", "Mood 3 Apply", "Mood 3 shortclick");
                } else{
                        Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();
                }
                } else {
                    Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
                }

            }
        });



        btn_mood3_sett.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if(StaticVariabes_div.loggeduser_type!=null) {
                if(!(StaticVariabes_div.loggeduser_type.equals("G"))) {
                    if(Mood_longPress) {
                        MoodNumber_Sel = "3";
                        StaticVariabes_div.Mood_sel_number = MoodNumber_Sel;
                        Moodsett_modelselection(StaticVariabes_div.devtyp, "mood3", "3");
                        Track_bulb_event("Mood 3 LongPress", "Add Mood 3", "Mood 3 longclick");
                    }
                } else{
                    Toast.makeText(view.getContext(), "Access Denied", Toast.LENGTH_LONG).show();
                }
            } else {
                Toast.makeText(view.getContext(), "Please Login To Access", Toast.LENGTH_SHORT).show();
            }
                return false;
            }
        });




        Edisonbro_AnalyticsApplication application = (Edisonbro_AnalyticsApplication) getActivity().getApplication();
        mTracker = application.getDefaultTracker();

        return view;
    }


    public void popupsheer_curtain(){

        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch2, null);
        View alertselect = inflater.inflate(R.layout.blanklay, null);
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        builder.setView(alertselect);

        final CharSequence[] items = {"Curtain", "Sheer"};
       // AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),R.style.popup_theme_timer);
        builder.setTitle("Choose !")
                .setItems(items,new DialogInterface.OnClickListener() {
                    //.setSingleChoiceItems(items, 1, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialogInterface, int item) {
                        Toast.makeText(getActivity().getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();

                        if(items[item].equals("Curtain")){
                            StaticVariabes_div.sheerselected=false;
                            alertDialog.dismiss();
                            loadf();
                        }else{
                            StaticVariabes_div.sheerselected=true;
                            alertDialog.dismiss();
                            loadf();
                        }
                    }
                });

        alertDialog = builder.create();
        alertDialog.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.50);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.show();


    }

    public void loadf(){

        String fragidfirst;
        Fragment fragtoviewfirst=null;


        fragtoviewfirst= Device_Feature_Settings.newInstance("test1","test2");
        fragidfirst="Devicesetting"+"eb";

        if (fragmentTransaction1 != null) {


            fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.fragment_place, fragtoviewfirst); // f1_container is your FrameLayout container
            fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

            // array_image[0].setImageResource(R.drawable.fill_circle);
        } else {


            fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.add(R.id.fragment_place, fragtoviewfirst, fragidfirst);
            fragmentTransaction1.commit();

            //  array_image[0].setImageResource(R.drawable.fill_circle);

        }
    }

    private static String asciiToHex(String asciiValue)
    {
        char[] chars = asciiValue.toCharArray();
        StringBuffer hex = new StringBuffer();
        for (int i = 0; i < chars.length; i++)
        {
            hex.append(Integer.toHexString((int) chars[i]));
        }
        return hex.toString();
    }

    static String hexToAscii(String s) {
        int n = s.length();
        StringBuilder sb = new StringBuilder(n / 2);
        for (int i = 0; i < n; i += 2) {
            char a = s.charAt(i);
            char b = s.charAt(i + 1);
            sb.append((char) ((hexToInt(a) << 4) | hexToInt(b)));
        }
        return sb.toString();
    }
    private static int hexToInt(char ch) {
        if ('a' <= ch && ch <= 'f') { return ch - 'a' + 10; }
        if ('A' <= ch && ch <= 'F') { return ch - 'A' + 10; }
        if ('0' <= ch && ch <= '9') { return ch - '0'; }
        throw new IllegalArgumentException(String.valueOf(ch));
    }


    public  void Loggin_Account(){

        LayoutInflater inflater = LayoutInflater.from(getActivity().getApplicationContext());
        View subView = inflater.inflate(R.layout.popup_login, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(),AlertDialog.THEME_HOLO_LIGHT);
        final Button btn_cancel= (Button) subView.findViewById(R.id.buton_cancel);
        final Button btn_ok= (Button) subView.findViewById(R.id.buton_ok);
        // final TextView tv_usernam = (TextView) subView.findViewById(R.id.loggedusr);
        final EditText et_usernam = (EditText) subView.findViewById(R.id.et_newuser);
        final EditText et_pwd= (EditText) subView.findViewById(R.id.et_pass);

        builder.setView(subView);
        final AlertDialog alertDialog = builder.create();


        // tv_usernam.setText(StaticVariabes_div.loggeduser);

        btn_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();

            }
        });
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                newappuser = et_usernam.getText().toString();
                newappPass = et_pwd.getText().toString();


                if ((newappuser == null || newappuser.length() == 0 || newappuser.length() != 10)
                        || (newappPass == null || newappPass.length() == 0) || newappPass.length() != 8) {

                    // showpopApppass();


                    if (newappuser == null || newappuser.length() == 0) {
                        et_usernam.setError("Please Enter New UserName !");
                    } else if (newappuser.length() != 10) {
                        et_usernam.setError("UserName must be of 10 Characters!");
                    } else {
                        et_usernam.setError(null);
                        et_usernam.setText(newappuser);
                    }

                    if (newappPass == null || newappPass.length() == 0) {
                        et_pwd.setError("Please Enter Confirm Password !");
                    } else if (newappPass.length() != 8) {
                        et_pwd.setError("Password must be of 8 Characters!");
                    } else {
                        et_pwd.setError(null);
                        et_pwd.setText(newappPass);
                    }

                } else {
                    // clearing error marks from Text fields

                    et_usernam.setError(null);
                    et_pwd.setError(null);
                    String loggedusr = "",pwd_decrypted;byte[] loggedpwd;
                    ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
                    sdadap.open();

                    Cursor csrs = sdadap.fetchlogintype(newappuser);
                    if (csrs != null) {
                        loggedusr = csrs.getString(csrs.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                        loggedpwd= csrs.getBlob(csrs.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_ps));
                        try {
                            byte[] tempBuf2 = removeTrailingNulls(loggedpwd);
                            byte[]  decryptedd = StaticVariabes_div.decrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(), tempBuf2);
                            pwd_decrypted = new String(decryptedd);

                            StaticVariabes_div.log("login_acces pwd_decrypted"+pwd_decrypted, TAG1);

                            if(newappPass.equals(pwd_decrypted)){

                                byte cipher[]=null;
                                try {
                                    cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),newappPass.getBytes());
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                dbhelp.opendb();
                        Boolean isupdate = dbhelp.update_login_account(StaticVariabes_div.housenumber, newappuser, cipher, loggedusr);

                        StaticVariabes_div.log("housenum" + StaticVariabes_div.housenumber + " newappuser" + newappuser + " newappPass" + newappPass + " loggedusr" + loggedusr, TAG1);


                        if (isupdate) {


                            locallistupdate();


                            boolean manuallogup=dbhelp.updateLoginAccess(StaticVariabes_div.housenumber, "AU");
                            if(manuallogup) {
                                Toast.makeText(getActivity().getApplicationContext(), "Manual login", Toast.LENGTH_SHORT).show();


                                Intent intnt = new Intent(getActivity().getApplicationContext(), Main_Navigation_Activity.class);
                                startActivity(intnt);
                                mn_nav_obj.finish();


                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "Manual login not updated", Toast.LENGTH_SHORT).show();

                            }

                            Toast.makeText(getActivity().getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity().getApplicationContext(), "Not Updated Try Again", Toast.LENGTH_SHORT).show();
                        }

                            }else{
                                Toast.makeText(getActivity().getApplicationContext(), "User Not Authorized", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        dbhelp.close();

                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "User Not Authorized", Toast.LENGTH_LONG).show();
                    }
                    sdadap.close();



                    alertDialog.dismiss();
                }

                //   alertDialog.setCancelable(true);

            }
        });

        int width = (int)(getResources().getDisplayMetrics().widthPixels*0.65);
        int height = (int)(getResources().getDisplayMetrics().heightPixels*0.35);
        alertDialog.getWindow().setLayout(width, height);
        alertDialog.show();

    }
    public static byte[] removeTrailingNulls(byte[] source) {
        int i = source.length;
        while (source[i - 1] == 0x00) {
            i--;
        }

        byte[] result = new byte[i];
        System.arraycopy(source, 0, result, 0, i);

        return result;
    }
    public void locallistupdate(){
        try {
            LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
            locallist_adap = new LocalListArrangeTable(getActivity(), StaticVariabes_div.housename,StaticVariabes_div.housename);
            locallist_adap.open();
            locallist_adap.deleteall();
            locallist_adap.close();
        }catch(Exception e){
            e.printStackTrace();

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

    public void timer_popup(){



            final LayoutInflater inflater1 = getActivity().getLayoutInflater();
            timer_popup_view = inflater1.inflate(R.layout.popup_timersettng_lay, null);


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

            rb_repeat_ondays = (RadioButton) timer_popup_view.findViewById(R.id.rb_repeat_ondays);
            rb_cyclic = (RadioButton) timer_popup_view.findViewById(R.id.rb_cyclic);
            rb_selecteddate = (RadioButton) timer_popup_view.findViewById(R.id.rb_selecteddate);
            //rb_repeat_ptern = (RadioButton) timer_popup_view.findViewById(R.id.rb_repeat_ptern);
             cb_repeat_ptern = (CheckBox) timer_popup_view.findViewById(R.id.rb_repeat_ptern);

            lay_rep_ondays_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld1);
            lay_rep_ondays_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld2);
            lay_cyclic_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld3);
            lay_cyclic_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld4);
            lay_sel_date_A = (LinearLayout) timer_popup_view.findViewById(R.id.ld5);
            lay_sel_date_B = (LinearLayout) timer_popup_view.findViewById(R.id.ld6);


            lay_timerlist = (LinearLayout) timer_popup_view.findViewById(R.id.laytimerlist);
            lay_setoperation = (LinearLayout) timer_popup_view.findViewById(R.id.laysetoperation);

            np_frmtime_hrs= (NumberPicker) timer_popup_view.findViewById(R.id.np_frmtime_hrs);
            np_frmtime_min    = (NumberPicker) timer_popup_view.findViewById(R.id.np_frmtime_min);
            np_totime_hrs= (NumberPicker) timer_popup_view.findViewById(R.id.np_totime_hrs);
            np_totime_min  = (NumberPicker) timer_popup_view.findViewById(R.id.np_totime_min);
            np_ontime_cyc  = (NumberPicker) timer_popup_view.findViewById(R.id.np_ontime_cyc);
            np_offtime_cyc= (NumberPicker) timer_popup_view.findViewById(R.id.np_offtime_cyc);
            np_date_seldate  = (NumberPicker) timer_popup_view.findViewById(R.id.np_date_seldate);
            np_month_seldate = (NumberPicker) timer_popup_view.findViewById(R.id.np_month_seldate);
            np_year_seldate = (NumberPicker) timer_popup_view.findViewById(R.id.np_year_seldate);

        btnarr_weekdays=new Button[]{b_monday,b_tuesday,b_wednesday,b_thursday,b_friday,b_saturday,b_sunday};

        np_arr=new NumberPicker[]{np_frmtime_hrs,np_frmtime_min,np_totime_hrs,np_totime_min,np_ontime_cyc,np_offtime_cyc,
        np_date_seldate,np_month_seldate,np_year_seldate};

        lay_all_arr=new LinearLayout[]{lay_rep_ondays_A,lay_rep_ondays_B,lay_cyclic_A,lay_cyclic_B,lay_sel_date_A,lay_sel_date_B};


        for(int h=0;h<btnarr_weekdays.length;h++){
            btnarr_weekdays[h].setTag(0);
            btnarr_weekdays[h].setEnabled(false);
        }
        for(int k=1;k<lay_all_arr.length;){
            lay_all_arr[k].setEnabled(false);
            lay_all_arr[k].setAlpha((float) 0.5);
            k=k+2;
        }
        for(int m=4;m<np_arr.length;m++){

            np_arr[m].setEnabled(false);
        }

        cb_repeat_ptern.setAlpha(0.5f);
        cb_repeat_ptern.setEnabled(false);
            //AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);

        // this is set the view from XML inside AlertDialog
            alert.setView(timer_popup_view);
            // disallow cancel of AlertDialog on click of back button and outside touch
            alert.setCancelable(true);

            //final AlertDialog
       //final android.support.v7.app.AlertDialog
               dialog = alert.create();
           // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.85);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.90);
        //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().setLayout(width, height);
         dialog.show();


        if(StaticVariabes_div.devtyp.equals("WPS1")||StaticVariabes_div.devtyp.equals("WPD1")){

            ar_num_switch = new ArrayList();
            ar_off_data = new ArrayList();
            ar_on_data = new ArrayList();
            room_num=StaticVariabes_div.room_n;

            while(room_num.length()<2){
                room_num="0"+room_num;
            }

            while(StaticVariabes_div.devnumber.length()<4){
                StaticVariabes_div.devnumber="0"+StaticVariabes_div.devnumber;
            }
            devn=StaticVariabes_div.devnumber;


        }else if(StaticVariabes_div.devtyp.equals("CLNR")){

            room_num=StaticVariabes_div.room_n;

            while(room_num.length()<2){
                room_num="0"+room_num;
            }

            while(StaticVariabes_div.devnumber.length()<4){
                StaticVariabes_div.devnumber="0"+StaticVariabes_div.devnumber;
            }
            devn=StaticVariabes_div.devnumber;



        }else if(StaticVariabes_div.devtyp.equals("CLS1")||StaticVariabes_div.devtyp.equals("CRS1")
                ||StaticVariabes_div.devtyp.equals("PSC1")||StaticVariabes_div.devtyp.equals("SOSH")
                ||StaticVariabes_div.devtyp.equals("SWG1")||StaticVariabes_div.devtyp.equals("SLG1")){
             ar_num_switch = new ArrayList();
             ar_off_data = new ArrayList();
             ar_on_data = new ArrayList();

            room_num=StaticVariabes_div.room_n;

            while(room_num.length()<2){
                room_num="0"+room_num;
            }

            while(StaticVariabes_div.devnumber.length()<4){
                StaticVariabes_div.devnumber="0"+StaticVariabes_div.devnumber;
            }
            devn=StaticVariabes_div.devnumber;

            b_setoperation.setEnabled(false);
            b_setoperation.setVisibility(View.INVISIBLE);
            tv_setoperation.setVisibility(View.INVISIBLE);

            Setoper_Cur();

        }else if(StaticVariabes_div.devtyp.equals("GSK1")){
            ar_num_switch = new ArrayList();
            ar_off_data = new ArrayList();
            ar_on_data = new ArrayList();

            room_num=StaticVariabes_div.room_n;

            while(room_num.length()<2){
                room_num="0"+room_num;
            }

            while(StaticVariabes_div.devnumber.length()<4){
                StaticVariabes_div.devnumber="0"+StaticVariabes_div.devnumber;
            }
            devn=StaticVariabes_div.devnumber;
            b_setoperation.setEnabled(false);
            b_setoperation.setVisibility(View.INVISIBLE);
            tv_setoperation.setVisibility(View.INVISIBLE);

            Setoper_gsk();
        }
        b_sunday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (b_sunday.getTag().equals(0)) {
                        b_sunday.setBackgroundResource(R.drawable.week_sun_sat_02);
                        b_sunday.setTag(1);
                    } else {
                        b_sunday.setBackgroundResource(R.drawable.week_sun_sat_01);
                        b_sunday.setTag(0);
                    }
                }
            });


        b_monday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (b_monday.getTag().equals(0)) {
                        b_monday.setBackgroundResource(R.drawable.week_mon_02);
                        b_monday.setTag(1);
                    } else {
                        b_monday.setBackgroundResource(R.drawable.week_mon_01);
                        b_monday.setTag(0);
                    }
                }
            });


        b_tuesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (b_tuesday.getTag().equals(0)) {
                        b_tuesday.setBackgroundResource(R.drawable.week_tues_thur_02);
                        b_tuesday.setTag(1);
                    } else {
                        b_tuesday.setBackgroundResource(R.drawable.week_tues_thur_01);
                        b_tuesday.setTag(0);
                    }
                }
            });

        b_wednesday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if( b_wednesday.getTag().equals(0)) {
                        b_wednesday.setBackgroundResource(R.drawable.week_wed_02);
                        b_wednesday.setTag(1);
                    } else {
                        b_wednesday.setBackgroundResource(R.drawable.week_wed_01);
                        b_wednesday.setTag(0);
                    }
                }
            });

        b_thursday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (b_thursday.getTag().equals(0)) {
                        b_thursday.setBackgroundResource(R.drawable.week_tues_thur_02);
                        b_thursday.setTag(1);
                    } else {
                        b_thursday.setBackgroundResource(R.drawable.week_tues_thur_01);
                        b_thursday.setTag(0);
                    }
                }
            });

        b_friday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if ( b_friday.getTag().equals(0)) {
                        b_friday.setBackgroundResource(R.drawable.week_fri_02);
                        b_friday.setTag(1);
                    } else {
                        b_friday.setBackgroundResource(R.drawable.week_fri_01);
                        b_friday.setTag(0);
                    }
                }
            });

        b_saturday.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (b_saturday.getTag().equals(0)) {
                        b_saturday.setBackgroundResource(R.drawable.week_sun_sat_02);
                        b_saturday.setTag(1);
                    } else {
                        b_saturday.setBackgroundResource(R.drawable.week_sun_sat_01);
                        b_saturday.setTag(0);
                    }
                }
            });
            b_timerlist.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Tcp_con.stopClient();
                    Tcp_con.isClientStarted=false;
                    Tcp.tcpConnectionClose();
                    Tcp.tcpConnected=false;

                    Intent i = new Intent(getActivity(), TimerEdit_Database.class);
                   startActivity(i);

                }
            });

        lay_timerlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

                Tcp_con.stopClient();
                Tcp_con.isClientStarted=false;
                Intent i = new Intent(getActivity(), TimerEdit_Database.class);
                startActivity(i);

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



        cb_repeat_ptern.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (cb_repeat_ptern.isChecked()) {
                    //rb_repeat_ptern_func();
                    rep_weekly = "0";
                } else {

                    Calendar cal = Calendar.getInstance();
                    rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
                }
            }
        });

           numberpicker();
            /////////////////////////////////////////////////////////////////////////////////////////////////////



        b_setoperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ar_num_switch = new ArrayList();
                ar_off_data = new ArrayList();
                ar_on_data = new ArrayList();

                SetOperation_Switch(StaticVariabes_div.devtyp);
            }

    });

        lay_setoperation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ar_num_switch = new ArrayList();
                ar_off_data = new ArrayList();
                ar_on_data = new ArrayList();

                SetOperation_Switch(StaticVariabes_div.devtyp);
            }

        });







        b_settimer.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(final View view) {

                String trdata_Str_ON = "", trdata_Str_OFF = "", Switch_numdata = "";

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


                if (!cb_repeat_ptern.isChecked()) {
                    Calendar cal = Calendar.getInstance();
                    rep_weekly = String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));

                    while (rep_weekly.length() < 2) {
                        rep_weekly = "0" + rep_weekly;
                    }
                }

            if(!((Frm_Time.equals("00:00")&&To_Time.equals("24:00"))||(To_Time.equals("00:00")))){
                arr_weekdata = new int[7];
                if (rb_repeat_ondays.isChecked()) {

                    TimerType = "rep";

                    for (int v = 0; v < btnarr_weekdays.length; v++) {

                        if (btnarr_weekdays[v].getTag().equals(1)) {
                            arr_weekdata[v] = 1;
                        } else if (btnarr_weekdays[v].getTag().equals(0)) {
                            arr_weekdata[v] = 0;
                        }
                    }


                    if ((ar_on_data != null) && (ar_on_data.size() > 0)) {


                        if (!(Frm_Time.equals(To_Time))) {
                            if (arr_weekdata[0] != 0 || arr_weekdata[1] != 0 || arr_weekdata[2] != 0 || arr_weekdata[3] != 0 || arr_weekdata[4] != 0 || arr_weekdata[5] != 0 || arr_weekdata[6] != 0) {

                                for (int i = 0; i < ar_on_data.size(); i++) {
                                    trdata_Str_ON = trdata_Str_ON + ar_on_data.get(i) + "*";
                                }

                                for (int i = 0; i < ar_off_data.size(); i++) {

                                    trdata_Str_OFF = trdata_Str_OFF + ar_off_data.get(i) + "*";
                                }


                                for (int i = 0; i < ar_num_switch.size(); i++) {

                                    Switch_numdata = Switch_numdata + ar_num_switch.get(i) + "*";
                                }



                                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                                StrictMode.setThreadPolicy(policy);

                                String devid = fetch_deviceid(StaticVariabes_div.devnumber);

                                StaticVariabes_div.log(TAG1, "devid" + devid);


                                for (int i = 0; i < ar_on_data.size(); i++) {

                                    trdata_Str_ON = ar_on_data.get(i).toString();

                                    trdata_Str_OFF = ar_off_data.get(i).toString();

                                    Switch_numdata = ar_num_switch.get(i).toString();




                                    Timer_Data = "0,0,1,0,0000-00-00," + Frm_Time + "," + To_Time + "," + "0" + "," + arr_weekdata[0] + "," + arr_weekdata[1] + "," + arr_weekdata[2] + "," +
                                            arr_weekdata[3] + "," + arr_weekdata[4] + "," + arr_weekdata[5] + "," + arr_weekdata[6] + "," + rep_weekly + ","
                                            + StaticVariabes_div.devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + StaticVariabes_div.dev_typ_num +
                                            "," + StaticVariabes_div.devnumber + "," + devid + "," + Switch_numdata + "," + room_num + "," + StaticVariabes_div.dev_name;


                                  StaticVariabes_div.log("TimerData" + Timer_Data, TAG1);


                                    Send_Timer_dat_aes(Timer_Data, "[", "]");

                                    try {
                                        Thread.sleep(300);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }


                                }


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

                    TimerType = "cyc";

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


                    if (ar_on_data != null && ar_on_data.size() > 0) {


                        if (!(Frm_Time.equals(To_Time))) {


                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            String devid = fetch_deviceid(StaticVariabes_div.devnumber);

                            StaticVariabes_div.log(TAG1, "devid" + devid);


                            for (int i = 0; i < ar_on_data.size(); i++) {

                                trdata_Str_ON = ar_on_data.get(i).toString();

                                trdata_Str_OFF = ar_off_data.get(i).toString();

                                Switch_numdata = ar_num_switch.get(i).toString();



                                Timer_Data = Frm_Time + "," + To_Time + "," + on_time_cyclic + "," + off_time_cyclic + "," + Frm_Time + "," + "0000-00-00" + ","
                                        + "1" + "," + "0" + "," + "0" + "," + StaticVariabes_div.devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + StaticVariabes_div.dev_typ_num + "," + StaticVariabes_div.devnumber + ","
                                        + devid + "," + Switch_numdata + "," + room_num + "," + StaticVariabes_div.dev_name;


                                StaticVariabes_div.log("TimerData" + Timer_Data, TAG1);

                                Send_Timer_dat_aes(Timer_Data, "[", "!");
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }


                        } else {
                            Toast.makeText(view.getContext(), "Please Change TO time", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                    }


                } else if (rb_selecteddate.isChecked()) {
                    TimerType = "repd";

                    int_Day_Selected = np_date_seldate.getValue();

                    Day_Selected = String.valueOf(int_Day_Selected);

                    Month_Selected = "" + np_month_seldate.getValue();
                    Year_Selected = "" + np_year_seldate.getValue();

                    Date_Selected = Year_Selected + "-" + Month_Selected + "-" + Day_Selected;


                    if (ar_on_data != null && ar_on_data.size() > 0) {

                        if (!(Frm_Time.equals(To_Time))) {

                            for (int i = 0; i < ar_on_data.size(); i++) {
                                trdata_Str_ON = trdata_Str_ON + ar_on_data.get(i) + "*";
                            }

                            for (int i = 0; i < ar_off_data.size(); i++) {

                                trdata_Str_OFF = trdata_Str_OFF + ar_off_data.get(i) + "*";
                            }

                            for (int i = 0; i < ar_num_switch.size(); i++) {

                                Switch_numdata = Switch_numdata + ar_num_switch.get(i) + "*";
                            }


                            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                            StrictMode.setThreadPolicy(policy);

                            String devid = fetch_deviceid(StaticVariabes_div.devnumber);

                            StaticVariabes_div.log(TAG1, "devid" + devid);


                            for (int i = 0; i < ar_on_data.size(); i++) {

                                trdata_Str_ON = ar_on_data.get(i).toString();

                                trdata_Str_OFF = ar_off_data.get(i).toString();

                                Switch_numdata = ar_num_switch.get(i).toString();


                                Timer_Data = "0,0,0,1," + Date_Selected + "," + Frm_Time + "," + To_Time + "," + "0" + "," + arr_weekdata[0] + "," + arr_weekdata[1] + "," + arr_weekdata[2] + "," +
                                        arr_weekdata[3] + "," + arr_weekdata[4] + "," + arr_weekdata[5] + "," + arr_weekdata[6] + "," + rep_weekly + ","
                                        + StaticVariabes_div.devtyp + "," + trdata_Str_ON + "," + trdata_Str_OFF + "," + StaticVariabes_div.dev_typ_num +
                                        "," + StaticVariabes_div.devnumber + "," + devid + "," + Switch_numdata + "," + room_num + "," + StaticVariabes_div.dev_name;

                                StaticVariabes_div.log("TimerData" + Timer_Data, TAG1);

                                Send_Timer_dat_aes(Timer_Data, "[", "]");
                                try {
                                    Thread.sleep(300);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }

                            }

                        } else {
                            Toast.makeText(view.getContext(), "Please Change TO time", Toast.LENGTH_LONG).show();
                        }
                    } else {
                        Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
                    }

                } else {
                    Toast.makeText(view.getContext(), "Set timer", Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(view.getContext(), "Please Select Correct From and ToTime", Toast.LENGTH_LONG).show();
            }

            }
        });


        b_tmr_pop_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();

            }

        });


    }


    public String fetch_deviceid(String dev_nm){
        String devid_device="";

        swbadap.open();
        devid_device = swbadap.getdevid_frmdevno(dev_nm);
        swbadap.close();

        mas_adap.open();
        if (devid_device.equals("")) {
            StaticVariabes_div.log("roomno null", TAG1);
            devid_device = mas_adap.getdevid_frmdevno(dev_nm);
        }
        mas_adap.close();

        return devid_device;

    }

    public void popupinfo(final String msg){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("INFO");
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

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
    public void SetOperation_Switch(String Switch_model){

        room_num=StaticVariabes_div.room_n;

        while(room_num.length()<2){
            room_num="0"+room_num;
        }

        while(StaticVariabes_div.devnumber.length()<4){
            StaticVariabes_div.devnumber="0"+StaticVariabes_div.devnumber;
        }
        devn=StaticVariabes_div.devnumber;
      // final  AlertDialog dialog1 = null;

         inflater = getActivity().getLayoutInflater();

        StaticVariabes_div.log("Switch_model"+Switch_model,TAG1);

        //Switch5+1
        if(Switch_model.equals("S051")) {
            Setoper_S051();
        }else if(Switch_model.equals("S141")) {
            Setoper_S141();
        }else if(Switch_model.equals("S120")) {
            Setoper_S120();
        }

        else if(Switch_model.equals("S021")) {
            Setoper_S021();
        }else if(Switch_model.equals("S020")) {
            Setoper_S020();
        }
        else if(Switch_model.equals("S110")) {
            Setoper_S110();
        }else if(Switch_model.equals("S030")) {
            Setoper_S030();
        }else if(Switch_model.equals("S010")) {
            Setoper_S010();
        }else if(Switch_model.equals("S080")) {
            Setoper_S080();
        }
        else if(Switch_model.equals("S160")) {
            Setoper_S160();
        }else if(Switch_model.equals("RGB1")){
            Setoper_rgb();
        }else if(Switch_model.equals("DMR1")){
            Setoper_dmr();
        }else if(Switch_model.equals("CLNR")){
            Setoper_Cur_nrsh();
        }else if(Switch_model.equals("WPS1")||Switch_model.equals("WPD1")){
            Setoper_pir_light();
        }
        else{
            Toast.makeText(view.getContext(), "Set operation"+StaticVariabes_div.devtyp, Toast.LENGTH_LONG).show();
        }
    }

    private void rb_repeat_ondays_func() {

        for(int h=0;h<btnarr_weekdays.length;h++){
            btnarr_weekdays[h].setEnabled(true);
        }

        for(int k=2;k<lay_all_arr.length;k++){
            lay_all_arr[k].setEnabled(false);
            lay_all_arr[k].setAlpha((float) 0.5);
        }

        for(int m=4;m<np_arr.length;m++){

            np_arr[m].setEnabled(false);
        }

        cb_repeat_ptern.setAlpha(1.0f);
        cb_repeat_ptern.setEnabled(true);

        lay_rep_ondays_A.setAlpha((float) 1.0);
        lay_rep_ondays_B.setAlpha((float) 1.0);
        rb_cyclic.setChecked(false);
        rb_selecteddate.setChecked(false);
    }


     private  void rb_cyclic_func() {

         rb_cyclic.setChecked(true);

         for(int h=0;h<btnarr_weekdays.length;h++){
             btnarr_weekdays[h].setEnabled(false);
             btnarr_weekdays[h].setBackgroundResource(days_images_grey2[h]);
         }

         rb_repeat_ondays.setChecked(false);
         rb_selecteddate.setChecked(false);
         cb_repeat_ptern.setChecked(false);

         for(int m=4;m<np_arr.length;m++){

             if(m==4||m==5) {
                 np_arr[m].setEnabled(true);
             }else{
                 np_arr[m].setEnabled(false);
             }
         }

         for(int k=0;k<lay_all_arr.length;k++){

             if(k==2||k==3) {
                 lay_all_arr[k].setEnabled(true);
                 lay_all_arr[k].setAlpha((float) 1.0);

             }else{

                 lay_all_arr[k].setEnabled(false);
                 lay_all_arr[k].setAlpha((float) 0.5);
             }
         }
         cb_repeat_ptern.setAlpha(0.5f);
         cb_repeat_ptern.setEnabled(false);

     }


    private  void rb_selecteddate_func() {

        rb_selecteddate.setChecked(true);
        rb_repeat_ondays.setChecked(false);
        rb_cyclic.setChecked(false);

        for(int m=4;m<np_arr.length;m++){

            if(m==6||m==7||m==8) {
                np_arr[m].setEnabled(true);
            }else{
                np_arr[m].setEnabled(false);
            }
        }

        for(int k=0;k<lay_all_arr.length;k++){

            if(k==4||k==5) {
                lay_all_arr[k].setEnabled(true);
                lay_all_arr[k].setAlpha((float) 1.0);

            }else{

                lay_all_arr[k].setEnabled(false);
                lay_all_arr[k].setAlpha((float) 0.5);
            }
        }

        for(int h=0;h<btnarr_weekdays.length;h++){
            btnarr_weekdays[h].setEnabled(false);
            btnarr_weekdays[h].setBackgroundResource(days_images_grey2[h]);
        }

        cb_repeat_ptern.setEnabled(true);
        cb_repeat_ptern.setAlpha(1.0f);


    }



    public void fetchrooms(){
        List<String> roomlist=locallist_adap.getData1();
        roomnamesdb=roomlist.toArray(new String[roomlist.size()]);
    }

    public void fetchimgnames(){
        List<String> imglist=locallist_adap.getimagenames();
        imgnamesdb=imglist.toArray(new String[imglist.size()]);
    }


    public void fetchposi(int size){
        int newpositionslist[]=locallist_adap.getposvalues(size);

        for(int j=0;j<newpositionslist.length;j++){

           // Log.d("newpositionslist" , ""+newpositionslist[j]);

        }

    }
    public void initData() {

        fetchrooms();
        fetchimgnames();

        fetchposi(roomnamesdb.length);

        for(int j=0;j<roomnamesdb.length;j++){

           // Log.d("test","room"+j+"  "+roomnamesdb[j]);

        }

        pressedimgsname=new String[imgnamesdb.length];
        for(int j=0;j<imgnamesdb.length;j++){

          //  Log.d("test","imgname"+j+"  "+imgnamesdb[j]);
            pressedimgsname[j]=imgnamesdb[j]+"1";

        }



        mQQList = new ArrayList<>();

        for(int k=0;k<imgnamesdb.length;k++){

            String mDrawableName = imgnamesdb[k];
            int resID = getResources().getIdentifier(mDrawableName , "mipmap", getActivity().getApplicationContext().getPackageName());
            mQQList.add(new QQ("", roomnamesdb[k],"",resID));

           StaticVariabes_div.log("image nam"+imgnamesdb[k],TAG1);

        }




    }

    public void initMenu() {
        mMenuList = new ArrayList<>(2);
        Menu menu0 = new Menu(true, 0);
        menu0.addItem(new MenuItem.Builder().setWidth((int) getResources().getDimension(R.dimen.slv_item_bg_btn2_width))
                .setBackground(new ColorDrawable(Color.RED))
                .setText("DELETE")
                .setDirection(MenuItem.DIRECTION_RIGHT)
                .setTextColor(Color.WHITE)
                .setTextSize(10)
                .build());

        mMenuList.add(menu0);
        // mMenuList.add(menu1);
    }

    public void initUiAndListener() {
        mListView = (SlideAndDragListView) view.findViewById(R.id.lv_edit);
        mListView.setScrollContainer(false);
        mListView.setMenu(mMenuList);
        //mAdapter=new MyAppAdapter();
        ///mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setAdapter(mAdapter);

        mListView.setOnItemLongClickListener(this);
        mListView.setOnDragListener(this, mQQList);
        mListView.setOnItemClickListener(this);
        //   mListView.setOnSlideListener(this);
        //   mListView.setOnMenuItemClickListener(this);
       // mListView.setOnItemDeleteListener(this);
        // mListView.setDivider(new ColorDrawable(Color.GRAY));
        //  mListView.setDividerHeight(1);
       /* mListView.setSelection(0);
      mListView.getId().setItemChecked(0,true);*/

       // int defaultPositon = 0;
       // int justIgnoreId = 0;
       // mListView.setItemChecked(defaultPositon, true);
        //mListView.performItemClick(mListView.getSelectedView(), defaultPositon, justIgnoreId);
       // mListView.getAdapter().getView(0, null, null).performClick();
    }


    private BaseAdapter mAdapter = new BaseAdapter() {

        @Override
        public int getCount() {
            return mQQList.size();
        }

        @Override
        public Object getItem(int position) {
            return mQQList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return mQQList.get(position).hashCode();
        }

        @Override
        public int getItemViewType(int position) {
            if (mQQList.get(position).isQun()) {
                return 1;
            } else {
                return 0;
            }
           // return  position;
        }


        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
           final CustomViewHolder cvh ;

            if (convertView == null) {
                cvh = new CustomViewHolder();
                convertView = LayoutInflater.from(getActivity().getApplicationContext()).inflate(R.layout.item_qq, null);
                cvh.imgLogo = (ImageView) convertView.findViewById(R.id.img_item_qq);
                // cvh.txtName = (TextView) convertView.findViewById(R.id.txt_item_qq_title);
                cvh.txtContent = (TextView) convertView.findViewById(R.id.txt_item_qq_title);
                convertView.setTag(cvh);

                if (position == 0&&!load_first) {
                    StaticVariabes_div.log(" inside position zero " + position, "grid");



                    //@@@@@@
                    String selct = roomnamesdb[0];
                    StaticVariabes_div.roomname = selct;
                    StaticVariabes_div.spinnerroom=selct;

                    //@@@@
                    StaticVariabes_div.log(" gridfill once", "grid");
                   // if (convertView == null) {
                        gridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(), 0, gridfill(selct)));
                        //@@@@@@
                        if (listname.size() > 0) {
                            buttonAction(listname.get(0));
                            Selected_gridtype = listname.get(0);
                        }

                   // }
                }


            } else {

                cvh = (CustomViewHolder) convertView.getTag();

            }


                cvh.txtContent.setTextColor(Color.LTGRAY);
                QQ item = (QQ) this.getItem(position);
                cvh.imgLogo.setImageResource(item.getDrawableRes());
                cvh.txtContent.setText(item.getContent());
                cvh.txtContent.setTypeface(fontsanfrans);


                   String Samp=item.getContent();


           if(roomname_pressed!=null){


               if(roomname_pressed.equals(Samp)) {

                   for (int b = 0; b < roomnamesdb.length; b++) {
                       if (roomname_pressed.equals(roomnamesdb[b])) {
                           String nwimgname = pressedimgsname[position];
                           int nwimgnm = getResources().getIdentifier(nwimgname, "mipmap", getActivity().getApplicationContext().getPackageName());
                           cvh.imgLogo.setImageResource(nwimgnm);
                           cvh.txtContent.setTextColor(Color.rgb(66, 130, 208));

                       }
                   }

               }

            }else{

                if(position==0&&!load_first) {
                    String nwimgname = pressedimgsname[position];
                    int nwimgnm = getResources().getIdentifier(nwimgname, "mipmap", getActivity().getApplicationContext().getPackageName());
                    cvh.imgLogo.setImageResource(nwimgnm);
                    cvh.txtContent.setTextColor(Color.rgb(66, 130, 208));


                }else{

                    //Toast.makeText(getActivity(),"pos else "+position+" load_first"+load_first,Toast.LENGTH_SHORT).show();
                }
            }



            if (getItemViewType(position) == 0) {
                StaticVariabes_div.log("comes here",TAG1);
               // mListView.setSelection(0);
                // cvh.txtName.setText(item.getName());
            } else {
                // cvh.txtName.setText("(QQ群) " + item.getName() + "(" + item.getQunNumber() + ")");
            }


            return convertView;
        }

        class CustomViewHolder {
            public ImageView imgLogo;
            //public TextView txtName;
            public TextView txtContent;


        }

    };

    @Override
    public void onDragViewStart(int position) {
        mToast.setText("onDragViewStart   position--->" + position);
        mToast.show();
        Log.i(TAG, "onDragViewStart   " + position);
        startpos=position;
    }


    public void onDragViewMoving(int position) {
//        Toast.makeText(DifferentActivity.this, "onDragViewMoving   position--->" + position, Toast.LENGTH_SHORT).show();
        Log.i(TAG, "onDragViewMoving   " + position);
    }


    public void onDragViewDown(int position) {
        mToast.setText("onDragViewDown   position--->" + position);
        mToast.show();
        Log.i(TAG, "onDragViewDown   " + position);
        endos=position;

        //  Log.i(TAG, "startpos   " + startpos +"end pos"+endos);

        mToast.setText("startpos   " + startpos +"end pos"+endos);
        mToast.show();
        int listsize=mQQList.size();
        if(startpos>endos){
            for(int l=endos;l<startpos;l++) {
                // boolean insfg= db.updateroompos_whr_nameis(roomnamesdb[endos],""+(endos+1));
                boolean insfg= locallist_adap.updateroompos_whr_nameis(roomnamesdb[l],""+(l+1));
                // new_pos_array[y]=
            }
            locallist_adap.updateroompos_whr_nameis(roomnamesdb[startpos],""+(endos));
        }else{

            for(int l=startpos+1;l<=endos;l++) {
                boolean insfg= locallist_adap.updateroompos_whr_nameis(roomnamesdb[l],""+(l-1));
                // new_pos_array[y]=
            }
            locallist_adap.updateroompos_whr_nameis(roomnamesdb[startpos],""+(endos));
        }


        load_first=false;
        roomname_pressed=null;
        previmg=0;
        initData();
        initMenu();
        initUiAndListener();



    }


    public void onSlideOpen(View view, View parentView, int position, int direction) {
        mToast.setText("onSlideOpen   position--->" + position + "  direction--->" + direction);
        mToast.show();
        Log.i(TAG, "onSlideOpen   " + position);
    }


    public void onSlideClose(View view, View parentView, int position, int direction) {
        mToast.setText("onSlideClose   position--->" + position + "  direction--->" + direction);
        mToast.show();
        Log.i(TAG, "onSlideClose   " + position);
    }


    public int onMenuItemClick(View v, int itemPosition, int buttonPosition, int direction) {
        Log.i(TAG, "onMenuItemClick   " + itemPosition + "   " + buttonPosition + "   " + direction);
        int viewType = mAdapter.getItemViewType(itemPosition);
        switch (viewType) {
            case 0:
                return clickMenuBtn0(buttonPosition, direction);
            case 1:
                return clickMenuBtn1(buttonPosition, direction);
            default:
                return Menu.ITEM_NOTHING;
        }
    }

    private int clickMenuBtn0(int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        return Menu.ITEM_NOTHING;
                    case 2:
                        return Menu.ITEM_SCROLL_BACK;
                }
        }
        return Menu.ITEM_NOTHING;
    }

    private int clickMenuBtn1(int buttonPosition, int direction) {
        switch (direction) {
            case MenuItem.DIRECTION_LEFT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_SCROLL_BACK;
                }
                break;
            case MenuItem.DIRECTION_RIGHT:
                switch (buttonPosition) {
                    case 0:
                        return Menu.ITEM_DELETE_FROM_BOTTOM_TO_TOP;
                    case 1:
                        return Menu.ITEM_SCROLL_BACK;
                }
        }
        return Menu.ITEM_NOTHING;
    }


    public void onItemDeleteAnimationFinished(View view, int position) {
        mQQList.remove(position - mListView.getHeaderViewsCount());
        mAdapter.notifyDataSetChanged();
    }


    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //mListView.getId()
       // mToast.setText("onItemClick   position--->" + position);
       // mToast.show();
       // Log.i(TAG, "onItemClick   " + position);
        String selct=roomnamesdb[position];
        StaticVariabes_div.roomname=selct;
        StaticVariabes_div.spinnerroom=selct;
        roomname_pressed=selct;


        StaticVariabes_div.log("gridfill on item click room select","grid"+selct);
       // Log.i(TAG, "onItemClick   " +"grid"+selct);
        gridView.setAdapter(new ImageAdapter(getActivity().getApplicationContext(),0, gridfill(selct)));
//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

        // if(position==0) {
        if(listname.size()>0) {
            buttonAction(listname.get(0));
            Selected_gridtype = listname.get(0);
        }


        if(position!=0){

          //  prevselectedView_zero.setImageResource(previmgzero);
          //  prevselectedTextView_zero.setTextColor(Color.LTGRAY);
        }


        if(previmg!=0)
        {

          // if(prev_position==0){
                int pos_t = parent.getFirstVisiblePosition();
                int last_post=parent.getLastVisiblePosition();



              //  Toast.makeText(getActivity(),"pos_t= "+pos_t,Toast.LENGTH_SHORT).show();

            if(pos_t==0){
                tempview_room = parent.getChildAt(0);
                ImageView img = (ImageView) tempview_room.findViewById(R.id.img_item_qq);
                TextView txt = (TextView) tempview_room.findViewById(R.id.txt_item_qq_title);
                txt.setTextColor(Color.LTGRAY);
                String mDrawableName = imgnamesdb[0];

                int resID = getResources().getIdentifier(mDrawableName , "mipmap", getActivity().getApplicationContext().getPackageName());
                img.setImageResource(resID);
               // Toast.makeText(getActivity(),"previmg==0",Toast.LENGTH_SHORT).show();
            }


            if(prev_position<=last_post&&prev_position>=pos_t) {
                if (prevselectedView != null) {
                    prevselectedView.setImageResource(previmg);
                    prevselectedTextView.setTextColor(Color.LTGRAY);
                    // Toast.makeText(getActivity(), "previmg!=0", Toast.LENGTH_SHORT).show();
                } else {
                    //Toast.makeText(getActivity(), "previmg!=0  view null", Toast.LENGTH_SHORT).show();
                }
            }

            load_first=true;



        }else{
            int pos_tt = parent.getFirstVisiblePosition();

            if(pos_tt==0) {
                tempview_room = parent.getChildAt(0);
                ImageView img = (ImageView) tempview_room.findViewById(R.id.img_item_qq);
                TextView txt = (TextView) tempview_room.findViewById(R.id.txt_item_qq_title);
                txt.setTextColor(Color.LTGRAY);
                String mDrawableName = imgnamesdb[0];
                int resID = getResources().getIdentifier(mDrawableName, "mipmap", getActivity().getApplicationContext().getPackageName());
                img.setImageResource(resID);
            }
           // Toast.makeText(getActivity(),"previmg==0",Toast.LENGTH_SHORT).show();
            load_first=true;



        }

        String nwimgname = pressedimgsname[position];
        int nwimgnm= getResources().getIdentifier(nwimgname , "mipmap", getActivity().getApplicationContext().getPackageName());
        // View vw=parent.getChildAt(position);
        ImageView imgzero = (ImageView) view.findViewById(R.id.img_item_qq);
        TextView txtzero = (TextView) view.findViewById(R.id.txt_item_qq_title);
        imgzero.setImageResource(nwimgnm);
        txtzero.setTextColor(Color.rgb(66,130,208));
        String imgname = imgnamesdb[position];
        previmg= getResources().getIdentifier(imgname , "mipmap", getActivity().getApplicationContext().getPackageName());
        prevselectedView=imgzero;
        prevselectedTextView=txtzero;
        prev_position=position;



    }


    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        mToast.setText("onItemLongClick   position--->" + position);
        mToast.show();
      //  Log.i(TAG, "onItemLongClick   " + position);


        return false;
    }

    private enum roomdevtypes {
        SWB, RGB, DMR, CUR, IR, CAM, PIR, GSK, PSC,SOSH,SLG1,SWG1, PLC, AQU, FMD, GSR, ACR, CLB, DLS, FAN, WPC, WSM1, WBM1, WSS1, WOTS, SDG ,SWD, ALXA;
    }

public  ArrayList<Integer>  gridfill(String roomname){
    listname = new ArrayList<String>();
    listicon = new ArrayList<Integer>();
    listiconpressed=new  ArrayList<Integer>();

    swbadap.open();
    String roomno=swbadap.getroomno(roomname);
    swbadap.close();


    mas_adap.open();
    if(roomno.equals("")){
        StaticVariabes_div.log("roomno null", TAG1);
        roomno=mas_adap.getroomno(roomname);
    }
    else{
        StaticVariabes_div.log("room selected"+"roomname"+roomname+"houseno"+"roomno"+roomno, TAG1);
    }
    mas_adap.close();


    cam_adap.open();
    if(roomno.equals("")){
        StaticVariabes_div.log("cam roomno null", TAG1);
        roomno=cam_adap.getroomno(roomname);
    }
    else{
        StaticVariabes_div.log("room selected"+"roomname"+roomname+"houseno"+"roomno"+roomno, TAG1);
    }
    cam_adap.close();


    ir_adap.open();
    if(roomno.equals("")){
        StaticVariabes_div.log("ir roomno null", TAG1);
        roomno=ir_adap.getroomno(roomname);
    }
    else{
        StaticVariabes_div.log("room selected"+"roomname"+roomname+"houseno"+"roomno"+roomno, TAG1);
    }
    ir_adap.close();


    room_num =roomno;
    StaticVariabes_div.room_n=roomno;


    if(StaticVariabes_div.loggeduser_type.equals("U")||StaticVariabes_div.loggeduser_type.equals("G")){

        listswbno = new ArrayList<String>();
        listswbmod = new ArrayList<String>();
        listswbdevnam = new ArrayList<String>();

        listswdno = new ArrayList<String>();
        listswdmod = new ArrayList<String>();
        listswddevnam = new ArrayList<String>();

        listrgbno = new ArrayList<String>();
        listrgbmod = new ArrayList<String>();
        listrgbdevnam = new ArrayList<String>();

        listdmrno = new ArrayList<String>();
        listdmrmod = new ArrayList<String>();
        listdmrdevnam = new ArrayList<String>();

        listcurtno = new ArrayList<String>();
        listcurtmod = new ArrayList<String>();
        listcurtdevnam = new ArrayList<String>();

        listsompycurtno = new ArrayList<String>();
        listsompycurtmod = new ArrayList<String>();
        listsompycurtdevnam = new ArrayList<String>();

        listslidecurtno = new ArrayList<String>();
        listslidecurtmod = new ArrayList<String>();
        listslidecurtdevnam = new ArrayList<String>();

        listswingcurtno = new ArrayList<String>();
        listswingcurtmod = new ArrayList<String>();
        listswingcurtdevnam = new ArrayList<String>();



        listpscno = new ArrayList<String> ();
        listpscmod = new ArrayList<String> ();
        listpscdevnam = new ArrayList<String> ();

        listplcno = new ArrayList<String> ();
        listplcmod = new ArrayList<String> ();
        listplcdevnam = new ArrayList<String> ();

        listirno = new ArrayList<String> ();
        listirmod = new ArrayList<String>() ;
        listirdevnam = new ArrayList<String>() ;

        listcamno = new ArrayList<String> ();
        listcammod = new ArrayList<String>() ;
        listcamdevnam = new ArrayList<String>() ;

        listaquno = new ArrayList<String> () ;
        listaqumod = new ArrayList<String> () ;
        listaqudevnam = new ArrayList<String> () ;

        listfmno = new ArrayList<String> ();
        listfmmod = new ArrayList<String> () ;
        listfmdevnam = new ArrayList<String> () ;

        listgskno = new ArrayList<String> ();
        listgskmod = new ArrayList<String> () ;
        listgskdevnam = new ArrayList<String> () ;

        listacno = new ArrayList<String> ();
        listacmod = new ArrayList<String> () ;
        listacdevnam= new ArrayList<String> () ;

        listgsrno = new ArrayList<String> ();
        listgsrmod = new ArrayList<String> () ;
        listgsrdevnam = new ArrayList<String> () ;

        listpirno = new ArrayList<String> ();
        listpirmod = new ArrayList<String> () ;
        listpirdevnam = new ArrayList<String> () ;

        listclbno = new ArrayList<String> ();
        listclbmod = new ArrayList<String> () ;
        listclbdevnam = new ArrayList<String> () ;

        listdlsno = new ArrayList<String> ();
        listdlsmod = new ArrayList<String> () ;
        listdlsdevnam = new ArrayList<String> () ;

        listwpcno = new ArrayList<String> ();
        listwpcmod = new ArrayList<String> () ;
        listwpcdevnam = new ArrayList<String> () ;

        listfanno = new ArrayList<String> ();
        listfanmod = new ArrayList<String> () ;
        listfandevnam = new ArrayList<String> () ;

        listsdgno = new ArrayList<String> ();
        listsdgmod = new ArrayList<String> () ;
        listsdgdevnam = new ArrayList<String> () ;

        listalexano = new ArrayList<String> ();
        listalexamod = new ArrayList<String> () ;
        listalexadevnam = new ArrayList<String> () ;

        //

        StaticVariabes_div.log("roomdevicesnumberaccessarr len" + StaticVariabes_div.roomdevicesnumberaccessarr.length, TAG1);

        for(int k=0;k<StaticVariabes_div.roomdevicesnumberaccessarr.length;k++){

            while (roomno.length() < 2)roomno = "0" + roomno;
            String arrroomno=StaticVariabes_div.roomnoarr[k];
            while (arrroomno.length() < 2)arrroomno = "0" + arrroomno;

            if(roomno.equals(arrroomno)){
                switch(roomdevtypes.valueOf(StaticVariabes_div.roomdevicestypearr[k])) {
                    case SWB:
                        listswbno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listswbmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listswbdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case RGB:
                        listrgbno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listrgbmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listrgbdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case DMR:
                        listdmrno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listdmrmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listdmrdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case CUR:
                        listcurtno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listcurtmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listcurtdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
//......................sompy
                    case SOSH:
                        listsompycurtno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listsompycurtmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listsompycurtdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;

  //...........................slide.------

                    case SWG1:
                        listswingcurtno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listswingcurtmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listswingcurtdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;


                    //------------swing-----------

                    case SLG1:
                        listslidecurtno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listslidecurtmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listslidecurtdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;

                    //---------------


                    case PSC:
                        listpscno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listpscmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listpscdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case PLC:
                        listplcno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listplcmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listplcdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case IR:
                        listirno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listirmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listirdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case CAM:
                        listcamno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listcammod.add(StaticVariabes_div.roommodletypearr[k]);
                        listcamdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case AQU:
                        listaquno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listaqumod.add(StaticVariabes_div.roommodletypearr[k]);
                        listaqudevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case FMD:
                        listfmno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listfmmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listfmdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case GSK:
                        listgskno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listgskmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listgskdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case ACR:
                        listacno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listacmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listacdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case GSR:
                        listgsrno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listgsrmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listgsrdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case FAN:
                        listfanno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listfanmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listfandevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case CLB:
                        listclbno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listclbmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listclbdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case DLS:
                        listdlsno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listdlsmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listdlsdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case PIR:
                        listpirno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listpirmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listpirdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case WPC:case WSM1:case WBM1: case WSS1: case WOTS:
                        listwpcno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listwpcmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listwpcdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case SDG:
                        listsdgno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listsdgmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listsdgdevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case SWD:
                        listswdno.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listswdmod.add(StaticVariabes_div.roommodletypearr[k]);
                        listswddevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    case ALXA:
                        listalexano.add(StaticVariabes_div.roomdevicesnumberaccessarr[k]);
                        listalexamod.add(StaticVariabes_div.roommodletypearr[k]);
                        listalexadevnam.add(StaticVariabes_div.roomdevnamearr[k]);
                        break;
                    default:
                        break;

                }


            }

        }


//@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@
        listname = new ArrayList<String>();
        listicon = new ArrayList<Integer>();

//****************************************************************************************************


        int swbcount = listswbno.size();
        swbnoarr = new String[swbcount];
        swbtypearr = new String[swbcount];
        swbmodeltypearr = new String[swbcount];
        swbdevicenamearr = new String[swbcount];

        swbnoarr = listswbno.toArray( new String[listswbno.size()] );
        swbmodeltypearr = listswbmod.toArray( new String[listswbmod.size()] );
        swbdevicenamearr =listswbdevnam.toArray( new String[listswbmod.size()] );
        // Toast.makeText(this,"")
        StaticVariabes_div.log("swbdevicenamearr" + swbdevicenamearr.length, TAG1);
        StaticVariabes_div.log("swb count" + swbcount, TAG1);

        if (swbcount != 0) {
            StaticVariabes_div.Swbinit(swbcount, swbnoarr, swbmodeltypearr, swbdevicenamearr);
            listname.add("Swb");
            listicon.add(R.drawable.switchboard_grid_n);
            listiconpressed.add(R.drawable.switchboard_grid_p);
        }


        int swdcount = listswdno.size();
        swdnoarr = new String[swdcount];
        swdtypearr = new String[swdcount];
        swdmodeltypearr = new String[swdcount];
        swddevicenamearr = new String[swdcount];

        swdnoarr = listswdno.toArray( new String[listswdno.size()] );
        swdmodeltypearr = listswdmod.toArray( new String[listswdmod.size()] );
        swbdevicenamearr =listswddevnam.toArray( new String[listswddevnam.size()] );

        StaticVariabes_div.log("swd count" + swdcount, TAG1);

        if (swdcount != 0) {
            StaticVariabes_div.Swdinit(swdcount, swdnoarr, swdmodeltypearr);
            listname.add("Swd");
            listicon.add(R.mipmap.bell_grid);
            listiconpressed.add(R.mipmap.bell_grid);
        }


        swbadap.close();



//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
        mas_adap.open();
        //................................................................................
        int rgbcount=listrgbno.size();
        rgbnoarr=new String[rgbcount];
        rgbtypearr=new String[rgbcount];
        rgbmodeltypearr=new String[rgbcount];
        rgbdevicenamearr = new String[rgbcount];

        rgbnoarr=listrgbno.toArray( new String[listrgbno.size()] );
        rgbmodeltypearr=listrgbmod.toArray( new String[listrgbmod.size()] );
        rgbdevicenamearr=listrgbdevnam.toArray( new String[listrgbdevnam.size()] );

        StaticVariabes_div.log("rgb count"+rgbcount, TAG1);
        StaticVariabes_div.log("rgb count" + rgbcount, TAG1);
        if (rgbcount != 0) {
            StaticVariabes_div.rgbbinit(rgbcount, rgbnoarr, rgbmodeltypearr, rgbdevicenamearr);
            listname.add("Rgb");
            listicon.add(R.drawable.rgb_grid_n);
            listiconpressed.add(R.drawable.rgb_grid_p);
        }
        //................................................................................

        int dmrcount=listdmrno.size();
        dimmrnoarr=new String[dmrcount];
        dimmrtypearr=new String[dmrcount];
        dimmrmodeltypearr=new String[dmrcount];
        dimmrdevicenamearr = new String[dmrcount];

        dimmrnoarr=listdmrno.toArray( new String[listdmrno.size()] );
        dimmrmodeltypearr=listdmrmod.toArray( new String[listdmrmod.size()] );
        dimmrdevicenamearr=listdmrdevnam.toArray( new String[listdmrdevnam.size()] );

        StaticVariabes_div.log("dimmr count"+dmrcount, TAG1);

        if (dmrcount != 0) {
            StaticVariabes_div.log("called from usr check"+dmrcount, TAG1);
            StaticVariabes_div.dmmrinit(dmrcount, dimmrnoarr, dimmrmodeltypearr, dimmrdevicenamearr);
            listname.add("Dmr");
            listicon.add(R.drawable.dimmer_grid_n);
            listiconpressed.add(R.drawable.dimmer_grid_p);
        }
        //................................................................................

        int curcount=listcurtno.size();
        curnoarr=new String[curcount];
        curtypearr=new String[curcount];
        curmodeltypearr=new String[curcount];
        curdevicenamearr = new String[curcount];

        curnoarr=listcurtno.toArray( new String[listcurtno.size()] );
        curmodeltypearr=listcurtmod.toArray( new String[listcurtmod.size()] );
        curdevicenamearr=listcurtdevnam.toArray( new String[listcurtdevnam.size()] );
        StaticVariabes_div.log("cur count"+curcount, TAG1);

        if (curcount != 0) {
            StaticVariabes_div.Curtinit(curcount, curnoarr, curmodeltypearr, curdevicenamearr);
            listname.add("Cur");
            listicon.add(R.drawable.curtain_grid_n);
            listiconpressed.add(R.drawable.curtain_grid_p);
        }
        for(int k=0;k<curmodeltypearr.length;k++){
            StaticVariabes_div.log("cur curmodeltypearr"+curmodeltypearr[k], TAG1);
        }
        for(int k=0;k<curnoarr.length;k++){
            StaticVariabes_div.log("cur curnoarr"+curnoarr[k], TAG1);
        }



        int fancount=listfanno.size();
        fannoarr=new String[fancount];
        fantypearr=new String[fancount];
        fanmodeltypearr=new String[fancount];
        fandevicenamearr = new String[fancount];

        fannoarr=listfanno.toArray( new String[listfanno.size()] );
        fanmodeltypearr=listfanmod.toArray( new String[listfanmod.size()] );
        fandevicenamearr=listfandevnam.toArray( new String[listfandevnam.size()] );

        StaticVariabes_div.log("fan count"+fancount, TAG1);

        if (fancount != 0) {
            StaticVariabes_div.faninit(fancount, fannoarr, fanmodeltypearr);
            listname.add("Fan");
            listicon.add(R.mipmap.fan_grid);
            listiconpressed.add(R.mipmap.fan_grid);
        }


        //..................................................................................................

        int geysercount=listgsrno.size();
        gysernoarr=new String[geysercount];
        gysertypearr=new String[geysercount];
        gysermodeltypearr=new String[geysercount];
        gyserdevicenamearr = new String[geysercount];

        gysernoarr=listgsrno.toArray( new String[listgsrno.size()] );
        gysermodeltypearr=listgsrmod.toArray( new String[listgsrmod.size()] );
        gyserdevicenamearr=listgsrdevnam.toArray( new String[listgsrdevnam.size()] );

        StaticVariabes_div.log("geysercount"+geysercount, TAG1);
        if (geysercount != 0) {
            StaticVariabes_div.geyserinit(geysercount, gysernoarr, gysermodeltypearr);
            listname.add("Gsr");
            listicon.add(R.mipmap.geyser_grid_n);
            listiconpressed.add(R.mipmap.geyser_grid_p);
        }

        //...........................................................................................
        int account=listacno.size();
        acnoarr=new String[account];
        actypearr=new String[account];
        acmodeltypearr=new String[account];
        acdevicenamearr = new String[account];

        acnoarr=listacno.toArray( new String[listacno.size()] );
        acmodeltypearr=listacmod.toArray( new String[listacmod.size()] );
        acdevicenamearr=listacdevnam.toArray( new String[listacdevnam.size()] );

        StaticVariabes_div.log("account"+account, TAG1);
        if (account != 0) {
            StaticVariabes_div.acinit(account, acnoarr, acmodeltypearr, acdevicenamearr);
            listname.add("Acr");
            listicon.add(R.mipmap.ac_grid_n);
            listiconpressed.add(R.mipmap.ac_grid_p);
        }

        //...................................................................

        //............................................................................

        int pircount=listpirno.size();
        pirnoarr=new String[pircount];
        pirtypearr=new String[pircount];
        pirmodeltypearr=new String[pircount];
        pirdevicenamearr = new String[pircount];

        pirnoarr=listpirno.toArray( new String[listpirno.size()] );
        pirmodeltypearr=listpirmod.toArray( new String[listpirmod.size()] );
        pirdevicenamearr=listpirdevnam.toArray( new String[listpirdevnam.size()] );

        StaticVariabes_div.log("pircount"+pircount, TAG1);
        if (pircount != 0) {
            StaticVariabes_div.pirinit(pircount, pirnoarr, pirmodeltypearr, pirdevicenamearr);
            /// StaticVariabes_div.pirinit(pircount,pirnoarr);
            listname.add("Pir");
            listicon.add(R.drawable.pir_grid_n);
            listiconpressed.add(R.drawable.pir_grid_p);
        }
        //............................................................................

        int clbcount=listclbno.size();
        clbnoarr=new String[clbcount];
        clbtypearr=new String[clbcount];
        clbmodeltypearr=new String[clbcount];
        clbdevicenamearr = new String[clbcount];

        clbnoarr=listclbno.toArray( new String[listclbno.size()] );
        clbmodeltypearr=listclbmod.toArray( new String[listclbmod.size()] );
        clbdevicenamearr=listacdevnam.toArray( new String[listclbdevnam.size()] );

        StaticVariabes_div.log("clbcount"+clbcount, TAG1);

        if (clbcount != 0) {
            StaticVariabes_div.clbinit(clbcount, clbnoarr, clbmodeltypearr);
            listname.add("Clb");
            listicon.add(R.mipmap.bell_grid_n);
            listiconpressed.add(R.mipmap.bell_grid_p);
        }
        //............................................................................
        int dlscount=listdlsno.size();
        dlsnoarr=new String[dlscount];
        dlstypearr=new String[dlscount];
        dlsmodeltypearr=new String[dlscount];
        dlsdevicenamearr = new String[dlscount];

        dlsnoarr=listdlsno.toArray( new String[listdlsno.size()] );
        dlsmodeltypearr=listdlsmod.toArray( new String[listdlsmod.size()] );
        dlsdevicenamearr=listacdevnam.toArray( new String[listdlsdevnam.size()] );

        StaticVariabes_div.log("dlscount"+dlscount, TAG1);

        if (dlscount != 0) {
            StaticVariabes_div.dlsinit(dlscount, dlsnoarr, dlsmodeltypearr);
            listname.add("Dls");
            listicon.add(R.mipmap.doorlock_grid_n);
            listiconpressed.add(R.mipmap.doorlock_grid_p);
        }

        //............................................................................

        int fmcount=listfmno.size();
        StaticVariabes_div.log("fmcount"+fmcount, TAG1);
        fmnoarr=new String[fmcount];
        fmtypearr=new String[fmcount];
        fmmodeltypearr=new String[fmcount];

        fmnoarr=listfmno.toArray( new String[listfmno.size()] );
        fmmodeltypearr=listfmmod.toArray( new String[listfmmod.size()] );
        fmdevicenamearr=listacdevnam.toArray( new String[listfmdevnam.size()] );

        StaticVariabes_div.log("fmcount"+fmcount, TAG1);

        if (fmcount != 0) {
            StaticVariabes_div.fminit(fmcount, fmnoarr, fmmodeltypearr);
            listname.add("Fmd");
            listicon.add(R.mipmap.fm_grid_n);
            listiconpressed.add(R.mipmap.fm_grid_p);
        }

        //....................................somp

        int sompcurcount=listsompycurtno.size();
        sompnoarr=new String[sompcurcount];
        somptypearr=new String[sompcurcount];
        sompmodeltypearr=new String[sompcurcount];
        sompdevicenamearr = new String[sompcurcount];

        sompnoarr=listsompycurtno.toArray( new String[listsompycurtno.size()] );
        sompmodeltypearr=listsompycurtmod.toArray( new String[listsompycurtmod.size()] );
        sompdevicenamearr=listsompycurtdevnam.toArray( new String[listsompycurtdevnam.size()] );
        StaticVariabes_div.log("soshcount"+sompcurcount, TAG1);

        if (sompcurcount != 0) {
            StaticVariabes_div.sompyscrinit(sompcurcount, sompnoarr, sompmodeltypearr, sompdevicenamearr);
            listname.add("SOSH");
            listicon.add(R.drawable.roller1);
            listiconpressed.add(R.drawable.roller);
        }
        for(int k=0;k<sompmodeltypearr.length;k++){
            StaticVariabes_div.log("somp sompmodeltypearr"+sompmodeltypearr[k], TAG1);
        }
        for(int k=0;k<sompnoarr.length;k++){
            StaticVariabes_div.log("somp sompnoarr"+sompnoarr[k], TAG1);
        }


        //..........................slide.................................


        int slidecurcount=listslidecurtno.size();
        slidenoarr=new String[slidecurcount];
        slidetypearr=new String[slidecurcount];
        slidemodeltypearr=new String[slidecurcount];
        slidedevicenamearr = new String[slidecurcount];

        slidenoarr=listslidecurtno.toArray( new String[listslidecurtno.size()] );
        slidemodeltypearr=listslidecurtmod.toArray( new String[listslidecurtmod.size()] );
        slidedevicenamearr=listslidecurtdevnam.toArray( new String[listslidecurtdevnam.size()] );
        StaticVariabes_div.log("slidecount"+slidecurcount, TAG1);

        if (slidecurcount != 0) {
            StaticVariabes_div.slidescrinit(slidecurcount, slidenoarr, slidemodeltypearr, slidedevicenamearr);
            listname.add("SLG1");
            listicon.add(R.drawable.slide_close_off);
            listiconpressed.add(R.drawable.slide_close_on);
        }
        for(int k=0;k<slidemodeltypearr.length;k++){
            StaticVariabes_div.log("slide slidemodeltypearr"+slidemodeltypearr[k], TAG1);
        }
        for(int k=0;k<slidenoarr.length;k++){
            StaticVariabes_div.log("slide slidenoarr"+slidenoarr[k], TAG1);


        }
//....................................swing.............


        int swingcurcount=listswingcurtno.size();
        swingnoarr=new String[swingcurcount];
        swingtypearr=new String[swingcurcount];
        swingmodeltypearr=new String[swingcurcount];
        swingdevicenamearr = new String[swingcurcount];

        swingnoarr=listswingcurtno.toArray( new String[listswingcurtno.size()] );
        swingmodeltypearr=listswingcurtmod.toArray( new String[listswingcurtmod.size()] );
        swingdevicenamearr=listswingcurtdevnam.toArray( new String[listswingcurtdevnam.size()] );
        StaticVariabes_div.log("swingcount"+swingcurcount, TAG1);

        if (swingcurcount != 0) {
            StaticVariabes_div.swingscrinit(swingcurcount, swingnoarr, swingmodeltypearr, swingdevicenamearr);
            listname.add("SWG1");
            listicon.add(R.drawable.swing_off_close);
            listiconpressed.add(R.drawable.swing_on_close);
        }
        for(int k=0;k<swingmodeltypearr.length;k++){
            StaticVariabes_div.log("swing swingmodeltypearr"+swingmodeltypearr[k], TAG1);
        }
        for(int k=0;k<swingnoarr.length;k++){
            StaticVariabes_div.log("swing swingnoarr"+swingnoarr[k], TAG1);
        }


        //...................................................................................................

        int projscrcount=listpscno.size();
        projscrnoarr=new String[projscrcount];
        projscrtypearr=new String[projscrcount];
        projscrmodeltypearr=new String[projscrcount];
        projscrdevicenamearr = new String[projscrcount];

        projscrnoarr=listpscno.toArray( new String[listpscno.size()] );
        projscrmodeltypearr=listpscmod.toArray( new String[listpscmod.size()] );
        projscrdevicenamearr=listpscdevnam.toArray( new String[listpscdevnam.size()] );

        StaticVariabes_div.log("projscrcount"+projscrcount, TAG1);
        if (projscrcount != 0) {
            StaticVariabes_div.projscrinit(projscrcount, projscrnoarr, projscrmodeltypearr,projscrdevicenamearr);
            listname.add("Psc");
            listicon.add(R.mipmap.projctr_grid_n);
            listiconpressed.add(R.mipmap.projctr_grid_p);
        }
        //...................................................................................................

        int projliftcount=listplcno.size();
        projliftnoarr=new String[projliftcount];
        projlifttypearr=new String[projliftcount];
        projliftmodeltypearr=new String[projliftcount];

        projliftnoarr=listplcno.toArray( new String[listplcno.size()] );
        projliftmodeltypearr=listplcno.toArray( new String[listplcno.size()] );
        StaticVariabes_div.log("projliftcount"+projliftcount, TAG1);
        if (projliftcount != 0) {
            StaticVariabes_div.projliftinit(projliftcount, projliftnoarr, projliftmodeltypearr);
            listname.add("Plc");
            listicon.add(R.mipmap.projectorlift_grid_n);
            listiconpressed.add(R.mipmap.projectorlift_grid_p);
        }
        //..................................................................................................

        int gskcount=listgskno.size();
        gsknoarr=new String[gskcount];
        gsktypearr=new String[gskcount];
        gskmodeltypearr=new String[gskcount];
        gskdevicenamearr = new String[gskcount];

        gsknoarr=listgskno.toArray( new String[listgskno.size()] );
        gskmodeltypearr=listgskmod.toArray( new String[listgskmod.size()] );
        gskdevicenamearr = listgskmod.toArray( new String[listgskmod.size()] );

        StaticVariabes_div.log("gskcount"+gskcount, TAG1);
        if (gskcount != 0) {
            StaticVariabes_div.gskinit(gskcount, gsknoarr, gskmodeltypearr,gskdevicenamearr);
            listname.add("Gsk");
            listicon.add(R.mipmap.sprinkler_grid_n);
            listiconpressed.add(R.mipmap.sprinkler_grid_p);
        }
        //...........................................................................................

        int sdgcount=listsdgno.size();
        smrtdognoarr=new String[sdgcount];
        smrtdogtypearr=new String[sdgcount];
        smrtdogmodeltypearr=new String[sdgcount];
        smrtdogdevicenamearr=new String[sdgcount];

        smrtdognoarr=listsdgno.toArray( new String[listsdgno.size()] );
        smrtdogmodeltypearr=listsdgmod.toArray( new String[listsdgmod.size()] );
        smrtdogdevicenamearr=listsdgmod.toArray( new String[listsdgmod.size()] );
        StaticVariabes_div.log("sdgcount"+sdgcount, TAG1);
        if (sdgcount != 0) {
            StaticVariabes_div.smrtdoginit(sdgcount, smrtdognoarr, smrtdogmodeltypearr, smrtdogdevicenamearr);
            listname.add("Sdg");
            listicon.add(R.drawable.dog_grid_n);
            listiconpressed.add(R.drawable.dog_grid_p);
        }

        //................................................................................................
        int alxacount=listalexano.size();
        alexanoarr=new String[alxacount];
        alexatypearr=new String[alxacount];
        alexamodeltypearr=new String[alxacount];
        alexadevicenamearr=new String[alxacount];

        alexanoarr=listalexano.toArray( new String[listalexano.size()] );
        alexamodeltypearr=listalexamod.toArray( new String[listalexamod.size()] );
        alexadevicenamearr=listalexamod.toArray( new String[listalexamod.size()] );
        StaticVariabes_div.log("alxacount"+alxacount, TAG1);
        if (alxacount != 0) {
            StaticVariabes_div.alexainit(alxacount, alexanoarr, alexamodeltypearr, alexadevicenamearr);
            listname.add("Alx");
            listicon.add(R.drawable.alxa_grid_n);
            listiconpressed.add(R.drawable.alxa_grid_p);
        }

        //................................................................................................
        mas_adap.close();

    }else {


        StaticVariabes_div.log("gridfill else part Admin" , TAG1);
        swbadap.open();
        //  String roomno=swbadap.getroomno(roomname);
        int swbcount = swbadap.getCount_housenoroomnodevtypename(roomno, "SWB");
        swbnoarr = new String[swbcount];
        swbtypearr = new String[swbcount];
        swbmodeltypearr = new String[swbcount];
        swbdevicenamearr = new String[swbcount];

        swbnoarr = swbadap.getall_housenoroomnodevicetypename(swbcount, roomno, "SWB");
        swbmodeltypearr = swbadap.getall_devicetypemodel(swbcount, roomno, "SWB");
        swbdevicenamearr = swbadap.getall_devicenames(swbcount, roomno, "SWB");
        // Toast.makeText(this,"")
        StaticVariabes_div.log("swbdevicenamearr" + swbdevicenamearr.length, TAG1);
        StaticVariabes_div.log("swb count" + swbcount, TAG1);

        if (swbcount != 0) {
            StaticVariabes_div.Swbinit(swbcount, swbnoarr, swbmodeltypearr, swbdevicenamearr);
            listname.add("Swb");
            listicon.add(R.drawable.switchboard_grid_n);
            listiconpressed.add(R.drawable.switchboard_grid_p);
        }


        int swdcount = swbadap.getCount_housenoroomnodevtypename(roomno, "SWD");
        swdnoarr = new String[swdcount];
        swdtypearr = new String[swdcount];
        swdmodeltypearr = new String[swdcount];

        swdnoarr = swbadap.getall_housenoroomnodevicetypename(swdcount, roomno, "SWD");
        swdmodeltypearr = swbadap.getall_devicetypemodel(swdcount, roomno, "SWD");
        StaticVariabes_div.log("swd count" + swdcount, TAG1);

        if (swdcount != 0) {
            StaticVariabes_div.Swdinit(swdcount, swdnoarr, swdmodeltypearr);
            listname.add("Swd");
            listicon.add(R.mipmap.bell_grid);
            listiconpressed.add(R.mipmap.bell_grid);
        }


        swbadap.close();


        mas_adap.open();
        //................................................................................
        int rgbcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "RGB");
        rgbnoarr = new String[rgbcount];
        rgbtypearr = new String[rgbcount];
        rgbmodeltypearr = new String[rgbcount];
        rgbdevicenamearr = new String[rgbcount];

        rgbnoarr = mas_adap.getall_housenoroomnodevicetypename(rgbcount, roomno, "RGB");
        rgbmodeltypearr = mas_adap.getall_devicetypemodel(rgbcount, roomno, "RGB");
        rgbdevicenamearr = mas_adap.getall_roomnodevicenames(rgbcount, roomno, "RGB");

        StaticVariabes_div.log("rgb count" + rgbcount, TAG1);
        if (rgbcount != 0) {
            StaticVariabes_div.rgbbinit(rgbcount, rgbnoarr, rgbmodeltypearr, rgbdevicenamearr);
            listname.add("Rgb");
            listicon.add(R.drawable.rgb_grid_n);
            listiconpressed.add(R.drawable.rgb_grid_p);
        }
        //................................................................................
        int dmrcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "DMR");
        dimmrnoarr = new String[dmrcount];
        dimmrtypearr = new String[dmrcount];
        dimmrmodeltypearr = new String[dmrcount];
        dimmrdevicenamearr = new String[dmrcount];

        dimmrnoarr = mas_adap.getall_housenoroomnodevicetypename(dmrcount, roomno, "DMR");
        dimmrmodeltypearr = mas_adap.getall_devicetypemodel(dmrcount, roomno, "DMR");
        dimmrdevicenamearr = mas_adap.getall_roomnodevicenames(dmrcount, roomno, "DMR");
        StaticVariabes_div.log("dimmr count" + dmrcount, TAG1);

        if (dmrcount != 0) {
            StaticVariabes_div.dmmrinit(dmrcount, dimmrnoarr, dimmrmodeltypearr, dimmrdevicenamearr);
            listname.add("Dmr");
            listicon.add(R.drawable.dimmer_grid_n);
            listiconpressed.add(R.drawable.dimmer_grid_p);
        }
        //................................................................................

        int curcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "CUR");
        curnoarr = new String[curcount];
        curtypearr = new String[curcount];
        curmodeltypearr = new String[curcount];
        curdevicenamearr = new String[curcount];

        curnoarr = mas_adap.getall_housenoroomnodevicetypename(curcount, roomno, "CUR");
        curmodeltypearr = mas_adap.getall_devicetypemodel(curcount, roomno, "CUR");
        curdevicenamearr = mas_adap.getall_roomnodevicenames(curcount, roomno, "CUR");
        StaticVariabes_div.log("cur count" + curcount, TAG1);

        if (curcount != 0) {
            StaticVariabes_div.Curtinit(curcount, curnoarr, curmodeltypearr, curdevicenamearr);
            listname.add("Cur");
            listicon.add(R.drawable.curtain_grid_n);
            listiconpressed.add(R.drawable.curtain_grid_p);
        }


        //................................................................................

        int fancount = mas_adap.getCount_housenoroomnodevtypename(roomno, "FAN");
        fannoarr = new String[fancount];
        fantypearr = new String[fancount];
        fanmodeltypearr = new String[fancount];
        fandevicenamearr = new String[fancount];

        fannoarr = mas_adap.getall_housenoroomnodevicetypename(fancount, roomno, "FAN");
        fanmodeltypearr = mas_adap.getall_devicetypemodel(fancount, roomno, "FAN");
        fandevicenamearr = mas_adap.getall_roomnodevicenames(fancount, roomno, "FAN");
        StaticVariabes_div.log("fan count" + fancount, TAG1);

        if (fancount != 0) {
            StaticVariabes_div.faninit(fancount, fannoarr, fanmodeltypearr);
            listname.add("Fan");
            listicon.add(R.mipmap.fan_grid);
            listiconpressed.add(R.mipmap.fan_grid);
        }


        //..................................................................................................
        int geysercount = mas_adap.getCount_housenoroomnodevtypename(roomno, "GSR");
        gysernoarr = new String[geysercount];
        gysertypearr = new String[geysercount];
        gysermodeltypearr = new String[geysercount];
        gyserdevicenamearr = new String[geysercount];

        gysernoarr = mas_adap.getall_housenoroomnodevicetypename(geysercount, roomno, "GSR");
        gysermodeltypearr = mas_adap.getall_devicetypemodel(geysercount, roomno, "GSR");
        gyserdevicenamearr = mas_adap.getall_roomnodevicenames(fancount, roomno, "GSR");
        StaticVariabes_div.log("geysercount" + geysercount, TAG1);

        if (geysercount != 0) {
            StaticVariabes_div.geyserinit(geysercount, gysernoarr, gysermodeltypearr);
            listname.add("Gsr");
            listicon.add(R.mipmap.geyser_grid_n);
            listiconpressed.add(R.mipmap.geyser_grid_p);
        }
        //...........................................................................................
        int account = mas_adap.getCount_housenoroomnodevtypename(roomno, "ACR");
        acnoarr = new String[account];
        actypearr = new String[account];
        acmodeltypearr = new String[account];
        acdevicenamearr = new String[account];

        acnoarr = mas_adap.getall_housenoroomnodevicetypename(account, roomno, "ACR");
        acmodeltypearr = mas_adap.getall_devicetypemodel(account, roomno, "ACR");
        acdevicenamearr = mas_adap.getall_roomnodevicenames(account, roomno, "ACR");
        StaticVariabes_div.log("account" + account, TAG1);

        if (account != 0) {
            StaticVariabes_div.acinit(account, acnoarr, acmodeltypearr, acdevicenamearr);
            listname.add("Acr");
            listicon.add(R.mipmap.ac_grid_n);
            listiconpressed.add(R.mipmap.ac_grid_p);
        }
        for (int k = 0; k < acmodeltypearr.length; k++) {
            StaticVariabes_div.log("ac acmodeltypearr" + acmodeltypearr[k], TAG1);
        }
        for (int k = 0; k < acnoarr.length; k++) {
            StaticVariabes_div.log("ac acnoarr" + acnoarr[k], TAG1);
        }
        //............................................................................

        int pircount = mas_adap.getCount_housenoroomnodevtypename(roomno, "PIR");
        pirnoarr = new String[pircount];
        pirtypearr = new String[pircount];
        pirmodeltypearr = new String[pircount];
        pirdevicenamearr = new String[pircount];

        pirnoarr = mas_adap.getall_housenoroomnodevicetypename(pircount, roomno, "PIR");
        pirmodeltypearr = mas_adap.getall_devicetypemodel(pircount, roomno, "PIR");
        pirdevicenamearr = mas_adap.getall_roomnodevicenames(pircount, roomno, "PIR");
        StaticVariabes_div.log("pircount test" + pircount, TAG1);

        if (pircount != 0) {
            StaticVariabes_div.pirinit(pircount, pirnoarr, pirmodeltypearr, pirdevicenamearr);
            /// StaticVariabes_div.pirinit(pircount,pirnoarr);
            listname.add("Pir");
            listicon.add(R.drawable.pir_grid_n);
            listiconpressed.add(R.drawable.pir_grid_p);
        }
        //............................................................................

        int clbcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "CLB");
        clbnoarr = new String[clbcount];
        clbtypearr = new String[clbcount];
        clbmodeltypearr = new String[clbcount];

        clbnoarr = mas_adap.getall_housenoroomnodevicetypename(clbcount, roomno, "CLB");
        clbmodeltypearr = mas_adap.getall_devicetypemodel(clbcount, roomno, "CLB");
        clbdevicenamearr = mas_adap.getall_roomnodevicenames(clbcount, roomno, "CLB");
        StaticVariabes_div.log("clbcount" + clbcount, TAG1);

        if (clbcount != 0) {
            StaticVariabes_div.clbinit(clbcount, clbnoarr, clbmodeltypearr);
            listname.add("Clb");
            listicon.add(R.mipmap.bell_grid_n);
            listiconpressed.add(R.mipmap.bell_grid_p);
        }
        //............................................................................

        int dlscount = mas_adap.getCount_housenoroomnodevtypename(roomno, "DLS");
        dlsnoarr = new String[dlscount];
        dlstypearr = new String[dlscount];
        dlsmodeltypearr = new String[dlscount];

        dlsnoarr = mas_adap.getall_housenoroomnodevicetypename(dlscount, roomno, "DLS");
        dlsmodeltypearr = mas_adap.getall_devicetypemodel(dlscount, roomno, "DLS");
        StaticVariabes_div.log("dlscount" + dlscount, TAG1);

        if (dlscount != 0) {
            StaticVariabes_div.dlsinit(dlscount, dlsnoarr, dlsmodeltypearr);
            listname.add("Dls");
            listicon.add(R.mipmap.doorlock_grid_n);
            listiconpressed.add(R.mipmap.doorlock_grid_p);
        }
        //............................................................................

        int fmcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "FMD");
        StaticVariabes_div.log("fmcount" + fmcount, TAG1);

        fmnoarr = new String[fmcount];
        fmtypearr = new String[fmcount];
        fmmodeltypearr = new String[fmcount];

        fmnoarr = mas_adap.getall_housenoroomnodevicetypename(fmcount, roomno, "FMD");
        fmmodeltypearr = mas_adap.getall_devicetypemodel(fmcount, roomno, "FMD");
        StaticVariabes_div.log("fmcount" + fmcount, TAG1);

        if (fmcount != 0) {
            StaticVariabes_div.fminit(fmcount, fmnoarr, fmmodeltypearr);
            listname.add("Fmd");
            listicon.add(R.mipmap.fm_grid_n);
            listiconpressed.add(R.mipmap.fm_grid_p);
        }

        for (int k = 0; k < fmmodeltypearr.length; k++) {
            StaticVariabes_div.log("fm fmmodeltypearr" + fmmodeltypearr[k], TAG1);
        }
        for (int k = 0; k < fmnoarr.length; k++) {
            StaticVariabes_div.log("fm fmnoarr" + fmnoarr[k], TAG1);
        }
        //...................................................................................................

        int projscrcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "PSC");
        projscrnoarr = new String[projscrcount];
        projscrtypearr = new String[projscrcount];
        projscrmodeltypearr = new String[projscrcount];
        projscrdevicenamearr = new String[projscrcount];

        projscrnoarr = mas_adap.getall_housenoroomnodevicetypename(projscrcount, roomno, "PSC");
        projscrmodeltypearr = mas_adap.getall_devicetypemodel(projscrcount, roomno, "PSC");
        projscrdevicenamearr = mas_adap.getall_roomnodevicenames(projscrcount, roomno, "PSC");

        StaticVariabes_div.log("projscrcount" + projscrcount, TAG1);

        if (projscrcount != 0) {
            StaticVariabes_div.projscrinit(projscrcount, projscrnoarr, projscrmodeltypearr,projscrdevicenamearr);
            listname.add("Psc");
            listicon.add(R.mipmap.projctr_grid_n);
            listiconpressed.add(R.mipmap.projctr_grid_p);
        }

        //---------------------sompy

        int sompyscrcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "SOSH");
        sompnoarr = new String[sompyscrcount];
        somptypearr = new String[sompyscrcount];
        sompmodeltypearr = new String[sompyscrcount];
        sompdevicenamearr = new String[sompyscrcount];

        sompnoarr = mas_adap.getall_housenoroomnodevicetypename(sompyscrcount, roomno, "SOSH");
        sompmodeltypearr = mas_adap.getall_devicetypemodel(sompyscrcount, roomno, "SOSH");
        sompdevicenamearr = mas_adap.getall_roomnodevicenames(sompyscrcount, roomno, "SOSH");

        StaticVariabes_div.log("sompyscrcount" + sompyscrcount, TAG1);

        if (sompyscrcount != 0) {
            StaticVariabes_div.sompyscrinit(sompyscrcount, sompnoarr, sompmodeltypearr,sompdevicenamearr);
            listname.add("SOSH");
            listicon.add(R.drawable.roller1);
            listiconpressed.add(R.drawable.roller);
        }

       //----------slide

        int slidescrcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "SLG1");
        slidenoarr = new String[slidescrcount];
        slidetypearr = new String[slidescrcount];
        slidemodeltypearr = new String[slidescrcount];
        slidedevicenamearr = new String[slidescrcount];

        slidenoarr = mas_adap.getall_housenoroomnodevicetypename(slidescrcount, roomno, "SLG1");
        slidemodeltypearr = mas_adap.getall_devicetypemodel(slidescrcount, roomno, "SLG1");
        slidedevicenamearr = mas_adap.getall_roomnodevicenames(slidescrcount, roomno, "SLG1");

        StaticVariabes_div.log("slidescrcount" + slidescrcount, TAG1);

        if (slidescrcount != 0) {
            StaticVariabes_div.slidescrinit(slidescrcount, slidenoarr, slidemodeltypearr,slidedevicenamearr);
            listname.add("SLG1");
            listicon.add(R.drawable.slide_close_off);
            listiconpressed.add(R.drawable.slide_close_on);
        }



        //--------swing

        int swingscrcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "SWG1");
        swingnoarr = new String[swingscrcount];
        swingtypearr = new String[swingscrcount];
        swingmodeltypearr = new String[swingscrcount];
        swingdevicenamearr = new String[swingscrcount];

        swingnoarr = mas_adap.getall_housenoroomnodevicetypename(swingscrcount, roomno, "SWG1");
        swingmodeltypearr = mas_adap.getall_devicetypemodel(swingscrcount, roomno, "SWG1");
        swingdevicenamearr = mas_adap.getall_roomnodevicenames(swingscrcount, roomno, "SWG1");

        StaticVariabes_div.log("swingscrcount" + swingscrcount, TAG1);

        if (swingscrcount != 0) {
            StaticVariabes_div.swingscrinit(swingscrcount, swingnoarr, swingmodeltypearr,swingdevicenamearr);
            listname.add("SWG1");
            listicon.add(R.drawable.swing_off_close);
            listiconpressed.add(R.drawable.swing_on_close);
        }
        //...................................................................................................

        int projliftcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "PLC");
        projliftnoarr = new String[projliftcount];
        projlifttypearr = new String[projliftcount];
        projliftmodeltypearr = new String[projliftcount];

        projliftnoarr = mas_adap.getall_housenoroomnodevicetypename(projliftcount, roomno, "PLC");
        projliftmodeltypearr = mas_adap.getall_devicetypemodel(projliftcount, roomno, "PLC");
        StaticVariabes_div.log("projliftcount" + projliftcount, TAG1);

        if (projliftcount != 0) {
            StaticVariabes_div.projliftinit(projliftcount, projliftnoarr, projliftmodeltypearr);
            listname.add("Plc");
            listicon.add(R.mipmap.projectorlift_grid_n);
            listiconpressed.add(R.mipmap.projectorlift_grid_p);
        }
        //..................................................................................................

        int gskcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "GSK");
        gsknoarr = new String[gskcount];
        gsktypearr = new String[gskcount];
        gskmodeltypearr = new String[gskcount];
        gskdevicenamearr = new String[gskcount];

        gsknoarr = mas_adap.getall_housenoroomnodevicetypename(gskcount, roomno, "GSK");
        gskmodeltypearr = mas_adap.getall_devicetypemodel(gskcount, roomno, "GSK");
        gskdevicenamearr = mas_adap.getall_roomnodevicenames(gskcount, roomno, "GSK");

        StaticVariabes_div.log("gskcount" + gskcount, TAG1);

        if (gskcount != 0) {
            StaticVariabes_div.gskinit(gskcount, gsknoarr, gskmodeltypearr,gskdevicenamearr);
            listname.add("Gsk");
            listicon.add(R.mipmap.sprinkler_grid_n);
            listiconpressed.add(R.mipmap.sprinkler_grid_p);
        }

        //..................................................................................................
        int smrtdogcount = mas_adap.getCount_housenoroomnodevtypename(roomno, "SDG");
        smrtdognoarr = new String[smrtdogcount];
        smrtdogtypearr = new String[smrtdogcount];
        smrtdogmodeltypearr = new String[smrtdogcount];
        smrtdogdevicenamearr = new String[smrtdogcount];

        smrtdognoarr = mas_adap.getall_housenoroomnodevicetypename(smrtdogcount, roomno, "SDG");
        smrtdogmodeltypearr = mas_adap.getall_devicetypemodel(smrtdogcount, roomno, "SDG");
        smrtdogdevicenamearr = mas_adap.getall_roomnodevicenames(smrtdogcount, roomno, "SDG");
        StaticVariabes_div.log("smrtdogcount" + smrtdogcount, TAG1);

        if (smrtdogcount != 0) {
            StaticVariabes_div.smrtdoginit(smrtdogcount, smrtdognoarr, smrtdogmodeltypearr, smrtdogdevicenamearr);
            listname.add("Sdg");
            listicon.add(R.drawable.dog_grid_n);
            listiconpressed.add(R.drawable.dog_grid_p);
        }

        //...........................................................................................

        int alexacount = mas_adap.getCount_housenoroomnodevtypename(roomno, "ALXA");
        alexanoarr = new String[alexacount];
        alexatypearr = new String[alexacount];
        alexamodeltypearr = new String[alexacount];
        alexadevicenamearr = new String[alexacount];

        alexanoarr = mas_adap.getall_housenoroomnodevicetypename(alexacount, roomno, "ALXA");
        alexamodeltypearr = mas_adap.getall_devicetypemodel(alexacount, roomno, "ALXA");
        alexadevicenamearr = mas_adap.getall_roomnodevicenames(alexacount, roomno, "ALXA");
        StaticVariabes_div.log("alexacount" + smrtdogcount, TAG1);

        if (alexacount != 0) {
            StaticVariabes_div.alexainit(alexacount, alexanoarr, alexamodeltypearr, alexadevicenamearr);
            listname.add("Alx");
            listicon.add(R.drawable.alxa_grid_n);
            listiconpressed.add(R.drawable.alxa_grid_p);
        }

        //...........................................................................................

        mas_adap.close();
    }

    Pressedgridimgs=listiconpressed.toArray(new Integer[listiconpressed.size()]);
    Normalgridimgs=listicon.toArray(new Integer[listicon.size()]);
    return listicon;
}



    ///////////////////////////////////////////////////////////////////
    public void startZoomOutAnimation(LinearLayout linear) {
        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_out_animation);
        linear.startAnimation(zoomOutAnimation);
    }


    public void startZoominAnimation(LinearLayout linear) {

        Animation zoomOutAnimation = AnimationUtils.loadAnimation(getActivity().getApplicationContext(), R.anim.zoom_in_animation);
        linear.startAnimation(zoomOutAnimation);

    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////
   /* // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }*/

   /* @Override
    public void onAttach(Context context) {
        super.onAttach(context);
      *//*  if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }*//*
    }*/

  /*  @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/



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
  /*  public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
*/





    ///////////////////////////////////////////fl///////////////////////////////////////////////////

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




    public static String[] clean(final String[] v) {
        List<String> list = new ArrayList<String>(Arrays.asList(v));
        list.removeAll(Collections.singleton(null));
        list.removeAll(Collections.singleton(""));
        return list.toArray(new String[list.size()]);
    }

    public  void  initDataBases(){


        try{

            swbadap=new SwbAdapter(getActivity().getApplicationContext());
            cam_adap=new CamAdapter(getActivity().getApplicationContext());
            ir_adap=new IRB_Adapter_ir(getActivity().getApplicationContext());
            usr_adap=new UserTableAdapter(getActivity().getApplicationContext());
            mas_adap=new MasterAdapter(getActivity().getApplicationContext());

            pre_adap=new PredefsettngAdapter(getActivity().getApplicationContext());


        }catch(Exception e){
            e.printStackTrace();
        }

        try {
            LocalListArrangeTable.TABLE_NAME = StaticVariabes_div.housename;
            locallist_adap = new LocalListArrangeTable(getActivity().getApplicationContext(), StaticVariabes_div.housename,StaticVariabes_div.housename);
        }catch(Exception e){
            e.printStackTrace();

        }

        CamAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        SwbAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        MasterAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
        UserTableAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";

      // deltable_val();

    }



 /*  public void deltable_val(){
        swbadap.open();
        boolean homedel=swbadap.deleteDatabase("F1_BESCOM"+".db");
        swbadap.close();

        if(homedel) {
            //  pre_adap.open();
            dbhelp.opendb();

            if (StaticVariabes_div.housename != null) {
                boolean locwirelessdb = swbadap.deleteDatabase("F1_BESCOM"+ "_WLS.db");
                StaticVariabes_div.log("locwirelessdb" + locwirelessdb, TAG1);

                // boolean lochomelist=db.delete(Integer.parseInt(houseno));
                boolean lochomelist = dbhelp.delete_hom("F1_BESCOM");
                StaticVariabes_div.log("lochomelist" + lochomelist, TAG1);
                //@@@@@                         //  pre_adap.deletevalforhouseno(houseno);
                //popuphomedeleted("house deleted");
            } else {
                popup("house not deleted,try Again");
            }
            dbhelp.close();

        }
    }*/



    public void fetchhomeref(){
        if(StaticVariabes_div.housename!=null) {
            ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
            sdadap = new ServerDetailsAdapter(getActivity().getApplicationContext());
            sdadap.open();
            Cursor cursrno = sdadap.fetchrefno(1);
            StaticVariabes_div.House_reference_num = cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_rno));
            cursrno.close();
            sdadap.close();
        }
    }

    public void fetchversionDb(){
        if(StaticVariabes_div.housename!=null) {
            ServerDetailsAdapter.OriginalDataBase = StaticVariabes_div.housename + ".db";
            sdadap.open();

            Cursor cursrno = sdadap.fetchrefno(1);
            StaticVariabes_div.House_dbver_num_local = cursrno.getString(cursrno.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_db));
            cursrno.close();
            sdadap.close();
      StaticVariabes_div.log("version"+ StaticVariabes_div.House_dbver_num_local,TAG1);
        }
    }


    public  void fetchvalues_fromdatabase(){

        Loggeduser=StaticVariabes_div.loggeduser_type;
        StaticVariabes_div.log("loggeduser_type "+StaticVariabes_div.loggeduser_type, TAG1);

        if(Loggeduser==null){
            ServerDetailsAdapter.OriginalDataBase=StaticVariabes_div.housename+".db";
            sdadap=new ServerDetailsAdapter(getActivity().getApplicationContext());
            sdadap.open();
            //Cursor cr=sdadap.fetchadminpass(StaticVariabes_div.loggeduser,StaticVariabes_div.loggedpwd);
            Cursor cr=sdadap.fetchlogintype(StaticVariabes_div.loggeduser);
            if(cr!=null){
                StaticVariabes_div.loggeduser_type=cr.getString(cr.getColumnIndexOrThrow(ServerDetailsAdapter.KEY_da));
                byte cipher[]=null;
                try {
                    cipher = StaticVariabes_div.encrypt(StaticVariabes_div.IV.getBytes(), StaticVariabes_div.encryptionKey.getBytes(),StaticVariabes_div.loggedpwd.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dbhelp.opendb();
                Boolean isupdate = dbhelp.update_login_account(StaticVariabes_div.housenumber,StaticVariabes_div.loggeduser,cipher,  StaticVariabes_div.loggeduser_type);
                dbhelp.close();


                Loggeduser=StaticVariabes_div.loggeduser_type;

                cr.close();



            }else{
                if(StaticVariabes_div.loggeduser_type==null){
                    StaticVariabes_div.loggeduser_type="A";
                }
            }
            sdadap.close();
        }


        if(Loggeduser!=null)
        if(Loggeduser.equals("U")||Loggeduser.equals("G")){




            usr_adap.open();


            Cursor usrcur=usr_adap.fetch_User(StaticVariabes_div.loggeduser);
            StaticVariabes_div.log("loggedusrname "+StaticVariabes_div.loggeduser, TAG1);
            String roomno_access,devnoaccess = null;
            if(usrcur!=null)
            {
                //	String roomnoaccess=usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_roomno));
                roomno_access = usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_roomno));
                String usrdb = usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_username));
                devnoaccess = usrcur.getString(usrcur.getColumnIndexOrThrow(UserTableAdapter.KEY_ea));
                StaticVariabes_div.log(usrdb + "devnoaccess" + devnoaccess , TAG1);
            }


            usr_adap.close();


            if(devnoaccess!=null) {

                if (!devnoaccess.equals("NULL")) {
                    String[] devnoaccessArraytemp = devnoaccess.split(";");
                    String[] devnoaccessArray = clean(devnoaccessArraytemp);
                    StaticVariabes_div.roomdevicesnumberaccessarr = devnoaccessArray;
                    // StaticVariabes_div.roomdeviceswitchnumberaccess=usrswitcsdb;
                    roomnoaccessArray = new String[devnoaccessArray.length];
                    roomdevtypeaccessArray = new String[devnoaccessArray.length];
                    roommodtypeaccessArray = new String[devnoaccessArray.length];
                    roomgrouptypeaccessArray = new String[devnoaccessArray.length];
                    roomdevnamaccessArray = new String[devnoaccessArray.length];
                    String usrroomno, roomdevtype, roomdevmodeltype, roomgrouptype,roomdevnams;


                    StaticVariabes_div.log("devnoaccessArray length "+devnoaccessArray.length, TAG1);
                    for (int m = 0; m < devnoaccessArray.length; m++) {
                        StaticVariabes_div.log("devnoaccessArray "+devnoaccessArray[m], TAG1);

                        swbadap.open();
                        usrroomno = swbadap.getroomno_frmdevno(devnoaccessArray[m]);
                        roomdevtype = swbadap.getdevtype_frmdevno(devnoaccessArray[m]);
                        roomdevmodeltype = swbadap.getmodtype_frmdevno(devnoaccessArray[m]);
                        roomgrouptype = swbadap.getgrouptype_frmdevno(devnoaccessArray[m]);
                        roomdevnams= swbadap.getdevnam_frmdevno(devnoaccessArray[m]);
                        swbadap.close();

                        mas_adap.open();
                        if (usrroomno.equals("")) {
                            StaticVariabes_div.log("roomno null", TAG1);
                            usrroomno = mas_adap.getroomno_devno(devnoaccessArray[m]);
                            roomdevtype = mas_adap.getdevtype_devno(devnoaccessArray[m]);
                            roomdevmodeltype = mas_adap.getmodtype_devno(devnoaccessArray[m]);
                            roomgrouptype = mas_adap.getgrouptype_devno(devnoaccessArray[m]);
                            roomdevnams= mas_adap.getdevnam_frmdevno(devnoaccessArray[m]);
                        }
                        mas_adap.close();



                        roomnoaccessArray[m] = usrroomno;
                        roomdevtypeaccessArray[m] = roomdevtype;
                        roommodtypeaccessArray[m] = roomdevmodeltype;
                        roomgrouptypeaccessArray[m] = roomgrouptype;
                        roomdevnamaccessArray[m] = roomdevnams;

                        usrroomno = "";
                        roomdevtype = "";
                        roomdevmodeltype = "";
                        roomgrouptype = "";
                        roomdevnams="";
                    }
                    StaticVariabes_div.roomnoarr = roomnoaccessArray;
                    StaticVariabes_div.roomdevicestypearr = roomdevtypeaccessArray;
                    StaticVariabes_div.roommodletypearr = roommodtypeaccessArray;
                    StaticVariabes_div.roomgrouptypearr = roomgrouptypeaccessArray;
                    StaticVariabes_div.roomdevnamearr = roomdevnamaccessArray;
                    //String[] roomnoaccessArray = roomnoaccess.split(";");
                    roomnameaccessArray = new String[roomnoaccessArray.length];
                    for (int m = 0; m < roomnoaccessArray.length; m++) {
                        swbadap.open();
                        String usrroomname = swbadap.getroomname(roomnoaccessArray[m]);
                        swbadap.close();

                        mas_adap.open();
                        if (usrroomname.equals("")) {
                            StaticVariabes_div.log("roomno null", TAG1);
                            usrroomname = mas_adap.getroomname(roomnoaccessArray[m]);
                        }
                        mas_adap.close();


                        roomnameaccessArray[m] = usrroomname;
                    }

                    for (int m = 0; m < roomnameaccessArray.length; m++) {
                        StaticVariabes_div.log("roomnameaccess" + roomnameaccessArray[m], TAG1);
                    }


                    //			}

                    List<String> listusr = new ArrayList<String>();
                    if (roomnameaccessArray != null) {
                        listusr.addAll(Arrays.asList(roomnameaccessArray));
                    }

                    Set<String> templist = new LinkedHashSet<String>(listusr);
                    roomnameaccessArray = templist.toArray(new String[templist.size()]);



                    if (roomnameaccessArray != null) {
                        for (int r = 0; r < roomnameaccessArray.length; r++) {
                            StaticVariabes_div.log("list final arr user test" + roomnameaccessArray[r], TAG1);
                        }
                        StaticVariabes_div.roomnameusrlistarr = roomnameaccessArray;


                        String[] finallistarr_user=null;
                        String[] finalimgnamearr_user=null;

                        finallistarr_user= roomnameaccessArray;

                        finalimgnamearr_user=new String[finallistarr_user.length];

                        if(finallistarr_user!=null){
                            for(int r=0;r<finallistarr_user.length;r++){
                                finalimgnamearr_user[r]=fetch_imagenames(finallistarr_user[r]);
                                StaticVariabes_div.log("list final arr user"+finallistarr_user[r], TAG1);
                            }
                            StaticVariabes_div.roomnameusrlistarr=finallistarr_user;
                            locallist_adap.open();
                            int rowcount= locallist_adap.numberOfRows(StaticVariabes_div.housename);

                            if(rowcount<1){
                                for(int r=0;r<finallistarr_user.length;r++) {
                                    int j=1;
                                    locallist_adap.insertData(finallistarr_user[r], "1", j+"", finalimgnamearr_user[r]);
                                    StaticVariabes_div.log(finalimgnamearr_user[r]+"  list final arr user "+finallistarr_user[r], TAG1);
                                    j++;
                                }
                            }

                        }
                    } else {


                        mn_nav_obj.btnconstatus.setVisibility(View.INVISIBLE);
                        mn_nav_obj.btsig.setVisibility(View.INVISIBLE);
                        Toast.makeText(getActivity().getApplicationContext(), "NO ROOMS", Toast.LENGTH_LONG).show();
                        StaticVariabes_div.log("in  no roomlist", TAG1);

                    }

                }
            }


        }else{
         //   btnwireless.setVisibility(View.VISIBLE);

           Devicenos(StaticVariabes_div.housenumber);

            Set<String> temprr = new LinkedHashSet<String>( Arrays.asList(StaticVariabes_div.roomnamearr ) );
            String[] roomnamearruni = temprr.toArray( new String[temprr.size()] );

            for(int r=0;r<StaticVariabes_div.roomnamearr.length;r++){
                StaticVariabes_div.log("swb name arr"+StaticVariabes_div.roomnamearr[r], TAG1);
            }
            for(int r=0;r<StaticVariabes_div.roomnoa.length;r++){
                StaticVariabes_div.log("swb no arr"+StaticVariabes_div.roomnoa[r], TAG1);
            }

            for(int r=0;r<roomnamearruni.length;r++){
                StaticVariabes_div.log("swbr"+roomnamearruni[r], TAG1);
            }

            String[] irresult = null;
            String[] camresult = null;
            String[] result = null;
            String[] finallistarr=null;
            String[] finalimgnamearr=null;
            if((countmastr!=0)||(countcam!=0)||(countir!=0)||roomnamearruni.length>0){// if(masterroomname!=null){

                if(masterroomname.length!=0){
                    Set<String> temp = new LinkedHashSet<String>( Arrays.asList( masterroomname ) );
                    result = temp.toArray( new String[temp.size()] );

                    for(int r=0;r<result.length;r++){
                        StaticVariabes_div.log("uq master"+result[r], TAG1);
                    }
                }
                if(camroomname.length!=0){
                    Set<String> temp = new LinkedHashSet<String>( Arrays.asList( camroomname ) );
                    camresult = temp.toArray( new String[temp.size()] );

                    for(int r=0;r<camresult.length;r++){
                        StaticVariabes_div.log("uq cam"+camresult[r], TAG1);
                    }
                }

                if(irroomname.length!=0){
                    Set<String> temp = new LinkedHashSet<String>( Arrays.asList( irroomname ) );
                    irresult = temp.toArray( new String[temp.size()] );

                    for(int r=0;r<irresult.length;r++){
                        StaticVariabes_div.log("uq ir"+irresult[r], TAG1);
                    }
                }

                List<String> list=new ArrayList<String>();
                if(roomnamearruni!=null){
                    list.addAll(Arrays.asList(roomnamearruni));
                }

                if(result!=null){
                    list.addAll(Arrays.asList(result));
                }

                if(camresult!=null){
                    list.addAll(Arrays.asList(camresult));
                }

                if(irresult!=null){
                    list.addAll(Arrays.asList(irresult));
                }


                for (String string : list) {
                    StaticVariabes_div.log("list full"+string, TAG1);
                }

                Set<String> templist = new LinkedHashSet<String>( list );
                finallistarr = templist.toArray( new String[templist.size()] );
                finalimgnamearr=new String[finallistarr.length];

                if(finallistarr!=null){
                    for(int r=0;r<finallistarr.length;r++){
                        finalimgnamearr[r]=fetch_imagenames(finallistarr[r]);
                        StaticVariabes_div.log("list final arr"+finallistarr[r], TAG1);
                    }
                   StaticVariabes_div.roomnameusrlistarr=finallistarr;
                    locallist_adap.open();
                   int rowcount= locallist_adap.numberOfRows(StaticVariabes_div.housename);

                    if(rowcount<1){
                        for(int r=0;r<finallistarr.length;r++) {
                            int j=1;
                            locallist_adap.insertData(finallistarr[r], "1", j+"", finalimgnamearr[r]);
                            StaticVariabes_div.log(finalimgnamearr[r]+"  list final arr  "+finallistarr[r], TAG1);
                            j++;
                        }
                    }

                }else{


                   // mn_nav_obj.btnconstatus.setVisibility(View.INVISIBLE);


                }



            }else{
                mn_nav_obj.btnconstatus.setVisibility(View.INVISIBLE);
                mn_nav_obj.btsig.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity().getApplicationContext(), "NO ROOMS", Toast.LENGTH_LONG).show();
                StaticVariabes_div.log("in  no roomlist", TAG1);

            }

        }
    }
    String fetch_imagenames(String roomname){
     String img_name;
        swbadap.open();
        img_name=  swbadap.get_image_name(roomname);
        swbadap.close();
        StaticVariabes_div.log("from mastr not null"+img_name,"Master");

       if((img_name==null)||img_name==""||img_name.length()<1){
           StaticVariabes_div.log("from mastr null"+img_name,"Master");
            mas_adap.open();
            img_name=mas_adap.get_image_name(roomname);
           StaticVariabes_div.log("from mastr null 2"+img_name,"Master");
           mas_adap.close();
        }

      return img_name;
    }

    void Devicenos(String houseno){
        swbadap.open();
        int count=swbadap.getCounthouseno(houseno);
        StaticVariabes_div.counts=count;
        StaticVariabes_div.log("swb counts"+count, TAG1);

        mas_adap.open();
        countmastr=mas_adap.getCounthouseno();
        StaticVariabes_div.log("mastrcount"+countmastr, TAG1);

        cam_adap.open();
        countcam=cam_adap.getCounthouseno();
        StaticVariabes_div.log("countcam"+countcam, TAG1);

        ir_adap.open();
        countir=ir_adap.getCounthouseno();
        StaticVariabes_div.log("countir"+countir, TAG1);
		{
            // Log.d("msg","count no in main"+count);
            devnoarr = new String[count];
            roomnoarr = new String[count];
            StaticVariabes_div.devnoaitem=StaticVariabes_div.devnoa=devnoarr=swbadap.getalldevnohome(count, houseno);
            StaticVariabes_div.roomnoa=roomnoarr=swbadap.getallroomnohome(count,houseno);


            masterroomname = new String[countmastr];
            masterroomname = mas_adap.getall_roomname(countmastr);


            camroomname = new String[countcam];
            camroomname = cam_adap.getall_roomname(countcam);

            irroomname = new String[countir];
            irroomname = ir_adap.getall_roomname(countir);

            for(int v=0;v<count;v++){
                StaticVariabes_div.log("dev"+devnoarr[v], TAG1);
                StaticVariabes_div.log("room"+roomnoarr[v], TAG1);
            }

            for(int x=0;x<countmastr;x++){
                StaticVariabes_div.log("mastr room"+masterroomname[x], TAG1);
            }

            for(int x=0;x<countcam;x++){
                StaticVariabes_div.log("cam room"+camroomname[x], TAG1);
            }

            for(int x=0;x<countir;x++){
                StaticVariabes_div.log("ir room"+irroomname[x], TAG1);
            }
        }
        StaticVariabes_div.base=count;
        StaticVariabes_div.roomimgarr1();
        if(count>0){

            String roomname[] = new String[count];
            StaticVariabes_div.roomnamearr=roomname = swbadap.getall_roomnameswb(count);

        }
        ir_adap.close();
        cam_adap.close();
        mas_adap.close();
        swbadap.close();

    }


    public void buttonAction(String btnName){

        ly_slider.removeAllViewsInLayout();


        if(btnName.contains("Swb")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.swbcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.swbnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.swbmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.swbdevicenames[0];
            StaticVariabes_div.swbpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst=SwitchBoard_All_Models_Frag.newInstance("test1","test2");
          //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="Swb"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);

        } else

        if(btnName.contains("Ac")) {

            StaticVariabes_div.frag_num=StaticVariabes_div.accounts;

            StaticVariabes_div.devnumber=StaticVariabes_div.acnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.acmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.acdevicenames[0];
            StaticVariabes_div.acpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;


            fragtoviewfirst=Ac_Frag.newInstance("test1","test2");
            fragidfirst="Ac"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;

            frag_load(fragtoviewfirst,fragidfirst);

        } else

        if(btnName.contains("Dmr")) {

           // StaticVariabes_div.ModelName="DMR";
            String returnedtype=decideclass("DMR");
            StaticVariabes_div.log(returnedtype, TAG1);
            StaticVariabes_div.dimgrplen=groupuni.length;
            StaticVariabes_div.dimindilen=ids.length;

            String fragidfirst;
            Fragment fragtoviewfirst=null;


            if(returnedtype.equals("both")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Dimmer_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";


            }else if(returnedtype.equals("mulgroup")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Dimmer_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulgroup";

            }else if(returnedtype.equals("mulindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.dmmrcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.dmmrnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.dmmrmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.dmmrdevicenames[0];
                StaticVariabes_div.dmmrpst=0;
                fragtoviewfirst= Dimmer_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulindi";

            }else if(returnedtype.equals("singlegroup")){


                StaticVariabes_div.frag_num=StaticVariabes_div.dimunder_grpcount;
                StaticVariabes_div.dimpst=0;

                StaticVariabes_div.devnumber=StaticVariabes_div.dimgrpsarr[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.dimgrpsmodarr[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.dimgrpsnamarr[0];
                StaticVariabes_div.dmmrpst=0;
                fragtoviewfirst= Dimmer_Group_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singlegroup";

            }else if(returnedtype.equals("singleindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.dmmrcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.dmmrnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.dmmrmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.dmmrdevicenames[0];
                StaticVariabes_div.dmmrpst=0;
                fragtoviewfirst= Dimmer_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singleindi";

            }else{
                StaticVariabes_div.frag_num=1;

                fragtoviewfirst= Dimmer_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";
            }

            fragidfirst="Dmr"+"eb";

            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;

            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }

            frag_load(fragtoviewfirst,fragidfirst);
        }
        else

        if(btnName.contains("Rgb")) {

            String returnedtype=decideclass("RGB");
            StaticVariabes_div.log(returnedtype, TAG1);
            StaticVariabes_div.rgbgrplen=groupuni.length;
            StaticVariabes_div.rgbindilen=ids.length;

            String fragidfirst;
            Fragment fragtoviewfirst=null;


            if(returnedtype.equals("both")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Rgb_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";
                StaticVariabes_div.devtyp="RGB1";

            }else if(returnedtype.equals("mulgroup")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Rgb_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulgroup";
                StaticVariabes_div.devtyp="RGB1";

            }else if(returnedtype.equals("mulindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.rgbbcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.rgbbnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.rgbbmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.rgbbdevicenames[0];
                StaticVariabes_div.rgbbpst=0;
                fragtoviewfirst= Rgb_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulindi";

            }else if(returnedtype.equals("singlegroup")){

                StaticVariabes_div.frag_num=StaticVariabes_div.rgbunder_grpcount;
                StaticVariabes_div.rgbpst = 0;
                StaticVariabes_div.devnumber = StaticVariabes_div.rgbgrpsarr[0];
                StaticVariabes_div.devtyp = StaticVariabes_div.rgbgrpsmodarr[0];
                StaticVariabes_div.dev_name = StaticVariabes_div.rgbgrpsnamarr[0];
                StaticVariabes_div.rgbbpst = 0;
                fragtoviewfirst= Rgb_Group_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singlegroup";

            }else if(returnedtype.equals("singleindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.rgbbcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.rgbbnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.rgbbmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.rgbbdevicenames[0];
                StaticVariabes_div.rgbbpst=0;
                fragtoviewfirst= Rgb_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singleindi";

            }else{

                StaticVariabes_div.frag_num=1;
                StaticVariabes_div.devtyp="RGB1";
                fragtoviewfirst= Rgb_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";

            }

            fragidfirst="Rgb"+"eb";

            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }

            frag_load(fragtoviewfirst,fragidfirst);

        }else

        if(btnName.contains("Pir")) {

            String returnedtype=decideclass("PIR");
            StaticVariabes_div.log(returnedtype, TAG1);
            StaticVariabes_div.pirgrplen=groupuni.length;
            StaticVariabes_div.pirindilen=ids.length;

            String fragidfirst;
            Fragment fragtoviewfirst=null;


            if(returnedtype.equals("both")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Pir_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";


            }else if(returnedtype.equals("mulgroup")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Pir_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulgroup";

            }else if(returnedtype.equals("mulindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.pircounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.pirnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.pirmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.pirdevicenames[0];
                StaticVariabes_div.pirrpst=0;
                fragtoviewfirst= Pir_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulindi";

            }else if(returnedtype.equals("singlegroup")){


                StaticVariabes_div.frag_num=StaticVariabes_div.pirunder_grpcount;
                StaticVariabes_div.pirrpst=0;

                StaticVariabes_div.devnumber=StaticVariabes_div.pirgrpsarr[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.pirgrpsmodarr[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.pirgrpsnamarr[0];

                StaticVariabes_div.pirpst=0;
                fragtoviewfirst= Pir_Group_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singlegroup";

            }else if(returnedtype.equals("singleindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.pircounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.pirnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.pirmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.pirdevicenames[0];
                StaticVariabes_div.pirrpst=0;
                fragtoviewfirst= Pir_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singleindi";

            }else{
                StaticVariabes_div.frag_num=1;

                fragtoviewfirst= Pir_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";

            }

            fragidfirst="Pir"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;

            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }

            frag_load(fragtoviewfirst,fragidfirst);

        } else  if(btnName.contains("Cur")) {

            String returnedtype=decideclass("CUR");
            StaticVariabes_div.log(returnedtype, TAG1);
            StaticVariabes_div.curgrplen=groupuni.length;
            StaticVariabes_div.curindilen=ids.length;

            String fragidfirst;
            Fragment fragtoviewfirst=null;


            if(returnedtype.equals("both")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Curtain_Main_Fragment.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";
                StaticVariabes_div.devtyp="CUR1";

            }else if(returnedtype.equals("mulgroup")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Curtain_Main_Fragment.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulgroup";
                StaticVariabes_div.devtyp="CUR1";

            }else if(returnedtype.equals("mulindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.curtcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.curtnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.curtmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.curtdevicenames[0];
                StaticVariabes_div.curtpst=0;
                fragtoviewfirst= Curtain_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulindi";

            }else if(returnedtype.equals("singlegroup")){


                StaticVariabes_div.frag_num=StaticVariabes_div.curunder_grpcount;
                StaticVariabes_div.curpst=0;

                StaticVariabes_div.devnumber=StaticVariabes_div.curgrpsarr[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.curgrpsmodarr[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.curgrpsnamarr[0];
                StaticVariabes_div.curtpst=0;
                fragtoviewfirst= Curtain_Group_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singlegroup";

            }else if(returnedtype.equals("singleindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.curtcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.curtnoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.curtmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.curtdevicenames[0];
                StaticVariabes_div.curtpst=0;
                fragtoviewfirst= Curtain_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singleindi";

            }else{
                StaticVariabes_div.frag_num=1;
                StaticVariabes_div.devtyp="CUR1";
                fragtoviewfirst= Curtain_Main_Fragment.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";

            }

            fragidfirst="Cur"+"eb";

            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;

            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }

            frag_load(fragtoviewfirst,fragidfirst);

        }else if(btnName.contains("Sdg")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.smrtdogcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.smrtdognoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.smrtdogmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.smrtdogdevicenames[0];
            StaticVariabes_div.smrtdogpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst=SmartDog_Frag.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="Sdg"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);


        }

        else if(btnName.contains("SOSH")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.sompyscrcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.sompyscrnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.sompyscrmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.sompyscrdevicenames[0];
            StaticVariabes_div.sompyscrpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst= SomphtScreen_Fragment.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="SOSH"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);

        }


        else if(btnName.contains("SWG1")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.swingscrcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.swingscrnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.swingscrmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.swingscrdevicenames[0];
            StaticVariabes_div.swingscrpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst= SwingGate_Fragment.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="SWG1"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);

        }


        else if(btnName.contains("SLG1")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.slidescrcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.slidescrnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.slidescrmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.slidescrdevicenames[0];
            StaticVariabes_div.slidescrpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst= SlideGate_Fragment.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="SLG1"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);

        }

        else if(btnName.contains("Psc")) {


            StaticVariabes_div.frag_num=StaticVariabes_div.projscrcounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.projscrnoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.projscrmodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.projscrdevicenames[0];
            StaticVariabes_div.projscrpst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst= ProjectorScreen_Fragment.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="Psc"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);

        }else if(btnName.contains("Gsk")) {

            String returnedtype=decideclass("GSK");
            StaticVariabes_div.log(returnedtype, TAG1);
            StaticVariabes_div.gskgrplen=groupuni.length;
            StaticVariabes_div.gskindilen=ids.length;

            String fragidfirst;
            Fragment fragtoviewfirst=null;

            StaticVariabes_div.log("dev Gsk "+returnedtype, TAG1);

            if(returnedtype.equals("both")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Garden_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";

            }else if(returnedtype.equals("mulgroup")){
                StaticVariabes_div.frag_num=1;
                fragtoviewfirst= Garden_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulgroup";

            }else if(returnedtype.equals("mulindi")){

                StaticVariabes_div.frag_num=StaticVariabes_div.gskcounts;
                StaticVariabes_div.devnumber=StaticVariabes_div.gsknoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.gskmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.gskdevicenames[0];
                StaticVariabes_div.gskpst=0;
                fragtoviewfirst= GardenSprinkler_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="mulindi";

            }else if(returnedtype.equals("singlegroup")){

                StaticVariabes_div.frag_num=StaticVariabes_div.gskunder_grpcount;
                StaticVariabes_div.gskrpst=0;
                StaticVariabes_div.devnumber=StaticVariabes_div.gskgrpsarr[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.gskgrpsmodarr[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.gskgrpsnamarr[0];

                StaticVariabes_div.gskpst=0;
                fragtoviewfirst= GardenSprinkler_Group.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singlegroup";

            }else if(returnedtype.equals("singleindi")){
                StaticVariabes_div.frag_num=StaticVariabes_div.gskcounts;

                StaticVariabes_div.devnumber=StaticVariabes_div.gsknoa[0];
                StaticVariabes_div.devtyp=StaticVariabes_div.gskmodela[0];
                StaticVariabes_div.dev_name=StaticVariabes_div.gskdevicenames[0];
                StaticVariabes_div.gskrpst=0;
                fragtoviewfirst= GardenSprinkler_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="singleindi";

            }else{
                StaticVariabes_div.frag_num=1;

                fragtoviewfirst= Garden_Main_Frag.newInstance("test1","test2");
                StaticVariabes_div.typegrpindi="both";

            }

            fragidfirst="Gsk"+"eb";

            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;

            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            frag_load(fragtoviewfirst,fragidfirst);


        }else if(btnName.contains("Alx")){

            StaticVariabes_div.frag_num=StaticVariabes_div.alexacounts;
            StaticVariabes_div.devnumber=StaticVariabes_div.alexanoa[0];
            StaticVariabes_div.devtyp=StaticVariabes_div.alexamodela[0];
            StaticVariabes_div.dev_name=StaticVariabes_div.alexadevicenames[0];
            StaticVariabes_div.alexapst=0;


            LinearLayout.LayoutParams mylayout=new LinearLayout.LayoutParams(10, 10);
            mylayout.gravity=Gravity.CENTER;
            array_image =new ImageView[StaticVariabes_div.frag_num];
            for(i=0;i<StaticVariabes_div.frag_num;i++)
            {

                image_view = new ImageView(ly_slider.getContext());
                mylayout.height=20;
                mylayout.width=20;
                image_view.setLayoutParams(mylayout);
                mylayout.leftMargin=10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i]=image_view;
                ly_slider.addView(array_image[i]);
            }


            String fragidfirst;
            Fragment fragtoviewfirst=null;

            fragtoviewfirst= Alexa_Frag.newInstance("test1","test2");
            //  fragidfirst=StaticVariabes_div.devtyp+"eb";
            fragidfirst="Alx"+"eb";


            frag_previous=fragtoviewfirst;
            frag_prev_id=fragidfirst;
            //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@

            frag_load(fragtoviewfirst,fragidfirst);


        }





    }


    public void frag_load( Fragment fragtoviewfirst,String fragidfirst){

        if(fragmentTransaction1!=null)
        {


            fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.fragment_place, fragtoviewfirst); // f1_container is your FrameLayout container
            fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

            array_image[0].setImageResource(R.drawable.fill_circle);
        }
        else
        {


            fragmentTransaction1 = fragmentManager.beginTransaction();
            //fragmentTransaction1 = getChildFragmentManager().beginTransaction();
            fragmentTransaction1.add(R.id.fragment_place, fragtoviewfirst, fragidfirst);
            fragmentTransaction1.commit();

            array_image[0].setImageResource(R.drawable.fill_circle);

        }
    }

    public String decideclass(String typeofdevice ){
        String typeg = null,typei = null,typeclass=null;

        if(typeofdevice!=null){
            mas_adap=new MasterAdapter(getActivity().getApplicationContext());
            mas_adap.open();
            //groupuni=mas_adap.fetchUniquegroupids("KEY_ea",typeofdevice, roomno);
            groupexists=mas_adap.fetchUniquegroupids("KEY_ea",typeofdevice, room_num);
            mas_adap.close();

           if(StaticVariabes_div.loggeduser_type.equals("U")||StaticVariabes_div.loggeduser_type.equals("G")){
                for(int t=0;t<groupexists.length;t++){
                    StaticVariabes_div.log("dev group id"+groupexists[t], TAG1);
                }
                //test
                List<String> listgrupnum=new ArrayList<String>();
                for(int m=0;m<groupexists.length;m++){
                    if(Arrays.asList(StaticVariabes_div.roomgrouptypearr).contains(groupexists[m])){
                        listgrupnum.add(groupexists[m]);
                    }
                }
                groupuni=listgrupnum.toArray(new String[listgrupnum.size()]);
            }else{
                groupuni=groupexists;
            }

            if(groupuni.length==1){
                typeg="sgroup";
                loaddata_group(groupuni[0],typeofdevice);
            }
            else if(groupuni.length>1)
                typeg="mgroup";

            if(groupuni.length==0)
                typeg="nogroup";


            mas_adap=new MasterAdapter(getActivity().getApplicationContext());
            mas_adap.open();
            //ids=mas_adap.fetchdevnoundergroup("000",typeofdevice, roomno);

            StaticVariabes_div.log("typeofdevice "+typeofdevice+" room_num "+room_num, TAG1);
            devexists=mas_adap.fetchdevnoundergroup("000",typeofdevice, room_num);
            mas_adap.close();

           if(StaticVariabes_div.loggeduser_type.equals("U")||StaticVariabes_div.loggeduser_type.equals("G")){
                List<String> listdevnum=new ArrayList<String>();
                for(int m=0;m<devexists.length;m++){
                    if(Arrays.asList(StaticVariabes_div.roomdevicesnumberaccessarr).contains(devexists[m])){
                        listdevnum.add(devexists[m]);
                    }
                }
                ids=listdevnum.toArray(new String[listdevnum.size()]);
            }else{
                ids=devexists;
            }


            if(ids.length==1)
                typei="sindi";

            else if(ids.length>1)
                typei="mindi";

            if(ids.length==0)
                typei="noindi";
        }
        StaticVariabes_div.log("typeg "+typeg+" typei "+typei, TAG1);

        if(!(typeg.equals("nogroup"))&&!(typei.equals("noindi"))){
            typeclass="both";
        }else if(typeg.equals("sgroup")&&typei.equals("noindi")){
            typeclass="singlegroup";
        }else if(typeg.equals("mgroup")&&typei.equals("noindi")){
            typeclass="mulgroup";
        }else if(typeg.equals("nogroup")&&typei.equals("sindi")){
            typeclass="singleindi";
        }else if(typeg.equals("nogroup")&&typei.equals("mindi")){
            typeclass="mulindi";
        }

        return typeclass;

    }

    public void loaddata_group(String group,String typdev){
        StaticVariabes_div.log("load group data called", TAG1);

        if(group!=null){
            mas_adap.open();
          String  groupdevnos_sampls[]=mas_adap.fetchdevnoundergroup(group,typdev,room_num);
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
                        grpdevnams[j]=typdev +" Group";
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

            if(typdev.contains("RGB")){
                StaticVariabes_div.Rgbinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }else if(typdev.contains("CUR")){
                StaticVariabes_div.Curinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }else if(typdev.contains("DMR")){
                StaticVariabes_div.Dmrinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }else if(typdev.contains("PIR")){
                StaticVariabes_div.Pirinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }else if(typdev.contains("GSK")){
                StaticVariabes_div.Gskinit_group(devtotalcnt, grpdevnos,grpmoddevnos,grpdevnams);
            }
        }
    }

    class MyGestureDetector extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {

            if(!(StaticVariabes_div.loaded_lay_Multiple)) {
                if (Selected_gridtype!=null){

                if (Math.abs(e1.getY() - e2.getY()) > SWIPE_MAX_OFF_PATH) {
                    return false;
                }

                String type_grp_indi;


                if (Selected_gridtype.equals("Swb") || Selected_gridtype.equals("Pir") || Selected_gridtype.equals("Rgb")
                        || Selected_gridtype.equals("Dmr") || Selected_gridtype.equals("Cur") || Selected_gridtype.equals("Sdg")
                   || Selected_gridtype.equals("Psc")||Selected_gridtype.equals("Gsk")||Selected_gridtype.equals("Alx")) {


                    if (Selected_gridtype.equals("Swb") || Selected_gridtype.equals("Ac") || Selected_gridtype.equals("Sdg")||Selected_gridtype.equals("Psc")
                            || Selected_gridtype.equals("Alx")) {

                        type_grp_indi = "I";

                    } else {


                        if (StaticVariabes_div.typegrpindi != null) {
                            if (StaticVariabes_div.typegrpindi.equals("singlegroup") || StaticVariabes_div.typegrpindi.equals("mulgroup")) {

                                type_grp_indi = "G";

                            } else if (StaticVariabes_div.typegrpindi.equals("both")) {

                                if (StaticVariabes_div.typ_G_I.equals("grp"))
                                    type_grp_indi = "G";
                                else
                                    type_grp_indi = "I";

                            } else {

                                type_grp_indi = "I";
                            }
                        } else {
                            type_grp_indi = "I";
                        }

                    }
                    SwitchtoDeviceSettings(Selected_gridtype, type_grp_indi);


                    if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {


                        StaticVariabes_div.log("A", TAG1);
                        if (Device_position < (Devices_count - 1)) {
                            Device_position = Device_position + 1;
                            StaticVariabes_div.log("pst after +" + Device_position + "counts" + Devices_count, TAG1);
                            // loadlayout(StaticVariabes_div.swbnoa[StaticVariabes_div.swbpst], roomno, housename, roomname, houseno,  StaticVariabes_div.swbmodela[StaticVariabes_div.swbpst]);

                            /// SwitchtoDeviceFragment(Selected_gridtype,type_grp_indi,Devices_num[Device_position],StaticVariabes_div.roomname,Devices_model_arr[Device_position],Device_position);
                            // loadlayout(StaticVariabes_div.swbnoa[StaticVariabes_div.swbpst], StaticVariabes_div.roomname, StaticVariabes_div.swbmodela[StaticVariabes_div.swbpst], StaticVariabes_div.swbpst);

                        } else if (Device_position == (Devices_count - 1)) {

                            StaticVariabes_div.log("B", TAG1);
                            Device_position = 0;
                            //loadlayout(StaticVariabes_div.swbnoa[StaticVariabes_div.swbpst], StaticVariabes_div.roomname, StaticVariabes_div.swbmodela[StaticVariabes_div.swbpst], StaticVariabes_div.swbpst);
                            //  SwitchtoDeviceFragment(Selected_gridtype,type_grp_indi,Devices_num[Device_position],StaticVariabes_div.roomname,Devices_model_arr[Device_position],Device_position);

                        }
                        SwitchtoDeviceFragment(Selected_gridtype, type_grp_indi, Devices_num[Device_position], StaticVariabes_div.roomname, Devices_model_arr[Device_position], Devices_Name_arr[Device_position], Device_position);


                    } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                            && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {

                        if (Device_position > 0) {

                            StaticVariabes_div.log("C", TAG1);
                            Device_position = Device_position - 1;
                            // loadlayout(StaticVariabes_div.swbnoa[StaticVariabes_div.swbpst], StaticVariabes_div.roomname, StaticVariabes_div.swbmodela[StaticVariabes_div.swbpst], StaticVariabes_div.swbpst);
                            //  SwitchtoDeviceFragment(Selected_gridtype,type_grp_indi,Devices_num[Device_position],StaticVariabes_div.roomname,Devices_model_arr[Device_position],Device_position);

                        } else if (Device_position == 0) {

                            StaticVariabes_div.log("D", TAG1);
                            StaticVariabes_div.log("Device_position" + Device_position, TAG1);
                            Device_position = Devices_count - 1;
                            // loadlayout(StaticVariabes_div.swbnoa[StaticVariabes_div.swbpst], StaticVariabes_div.roomname, StaticVariabes_div.swbmodela[StaticVariabes_div.swbpst], StaticVariabes_div.swbpst);
                            //  SwitchtoDeviceFragment(Selected_gridtype,type_grp_indi,Devices_num[Device_position],StaticVariabes_div.roomname,Devices_model_arr[Device_position],Device_position);

                        }

                        SwitchtoDeviceFragment(Selected_gridtype, type_grp_indi, Devices_num[Device_position], StaticVariabes_div.roomname, Devices_model_arr[Device_position], Devices_Name_arr[Device_position], Device_position);

                        // SwitchtoDeviceFragment(Selected_gridtype, type_grp_indi,Devices_num);

                    }
                }

               }
            }


            return false;
        }

        // It is necessary to return true from onDown for the onFling event to
        // register
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

    }




    public void SwitchtoDeviceSettings(String typ,String mtype){

       switch (CombFrag.types_dev.valueOf(typ+mtype)) {
           case SwbI:
               Device_position=StaticVariabes_div.swbpst;
               Devices_num=StaticVariabes_div.swbnoa;
               Devices_count=StaticVariabes_div.swbcounts;
               Devices_model_arr=StaticVariabes_div.swbmodela;
               Devices_Name_arr=StaticVariabes_div.swbdevicenames;

                break;

           case AcI:

               Device_position=StaticVariabes_div.acpst;
               Devices_num=StaticVariabes_div.acnoa;
               Devices_count=StaticVariabes_div.accounts;
               Devices_model_arr=StaticVariabes_div.acmodela;
               Devices_Name_arr=StaticVariabes_div.acdevicenames;
               break;

           case RgbI:

               Device_position=StaticVariabes_div.rgbbpst;
               Devices_num=StaticVariabes_div.rgbbnoa;
               Devices_count=StaticVariabes_div.rgbbcounts;
               Devices_model_arr=StaticVariabes_div.rgbbmodela;
               Devices_Name_arr=StaticVariabes_div.rgbbdevicenames;

               break;

           case RgbG:

               Device_position=StaticVariabes_div.rgbpst;
               Devices_num=StaticVariabes_div.rgbgrpsarr;
               Devices_count=StaticVariabes_div.rgbunder_grpcount;
               Devices_model_arr=StaticVariabes_div.rgbgrpsmodarr;
               Devices_Name_arr=StaticVariabes_div.rgbgrpsnamarr;


               break;

           case DmrI:
               Device_position=StaticVariabes_div.dmmrpst;
               Devices_num=StaticVariabes_div.dmmrnoa;
               Devices_count=StaticVariabes_div.dmmrcounts;
               Devices_model_arr=StaticVariabes_div.dmmrmodela;
               Devices_Name_arr=StaticVariabes_div.dmmrdevicenames;

               break;

           case DmrG:
               Device_position=StaticVariabes_div.dimpst;
               Devices_num=StaticVariabes_div.dimgrpsarr;
               Devices_count=StaticVariabes_div.dimunder_grpcount;
               Devices_model_arr=StaticVariabes_div.dimgrpsmodarr;
               Devices_Name_arr=StaticVariabes_div.dimgrpsnamarr;

               break;

           case PirI:

               Device_position=StaticVariabes_div.pirrpst;
               Devices_num=StaticVariabes_div.pirnoa;
               Devices_count=StaticVariabes_div.pircounts;
               Devices_model_arr=StaticVariabes_div.pirmodela;
               Devices_Name_arr=StaticVariabes_div.pirdevicenames;
               break;

           case PirG:
               Device_position=StaticVariabes_div.pirpst;
               Devices_num=StaticVariabes_div.pirgrpsarr;
               Devices_count=StaticVariabes_div.pirunder_grpcount;
               Devices_model_arr=StaticVariabes_div.pirgrpsmodarr;
               Devices_Name_arr=StaticVariabes_div.pirgrpsnamarr;
               break;

           case CurI:
               Device_position=StaticVariabes_div.curtpst;
               Devices_num=StaticVariabes_div.curtnoa;
               Devices_count=StaticVariabes_div.curtcounts;
               Devices_model_arr=StaticVariabes_div.curtmodela;
               Devices_Name_arr=StaticVariabes_div.curtdevicenames;
               break;

           case CurG:
               Device_position=StaticVariabes_div.curpst;
               Devices_num=StaticVariabes_div.curgrpsarr;
               Devices_count=StaticVariabes_div.curunder_grpcount;
               Devices_model_arr=StaticVariabes_div.curgrpsmodarr;
               Devices_Name_arr=StaticVariabes_div.curgrpsnamarr;
               break;

           case SdgI:
               Device_position=StaticVariabes_div.smrtdogpst;
               Devices_num=StaticVariabes_div.smrtdognoa;
               Devices_count=StaticVariabes_div.smrtdogcounts;
               Devices_model_arr=StaticVariabes_div.smrtdogmodela;
               Devices_Name_arr=StaticVariabes_div.smrtdogdevicenames;

               break;

           case PscI:
               Device_position=StaticVariabes_div.projscrpst;
               Devices_num=StaticVariabes_div.projscrnoa;
               Devices_count=StaticVariabes_div.projscrcounts;
               Devices_model_arr=StaticVariabes_div.projscrmodela;
               Devices_Name_arr=StaticVariabes_div.projscrdevicenames;

               break;
           case SOSHI:
               Device_position=StaticVariabes_div.sompyscrpst;
               Devices_num=StaticVariabes_div.sompyscrnoa;
               Devices_count=StaticVariabes_div.sompyscrcounts;
               Devices_model_arr=StaticVariabes_div.sompyscrmodela;
               Devices_Name_arr=StaticVariabes_div.sompyscrdevicenames;

               break;
           case SWG1I:
               Device_position=StaticVariabes_div.swingscrpst;
               Devices_num=StaticVariabes_div.swingscrnoa;
               Devices_count=StaticVariabes_div.swingscrcounts;
               Devices_model_arr=StaticVariabes_div.swingscrmodela;
               Devices_Name_arr=StaticVariabes_div.swingscrdevicenames;

               break;
           case SLG1I:
           Device_position=StaticVariabes_div.slidescrpst;
           Devices_num=StaticVariabes_div.slidescrnoa;
           Devices_count=StaticVariabes_div.slidescrcounts;
           Devices_model_arr=StaticVariabes_div.slidescrmodela;
           Devices_Name_arr=StaticVariabes_div.slidescrdevicenames;

           break;
           case GskI:

               Device_position=StaticVariabes_div.gskrpst;
               Devices_num=StaticVariabes_div.gsknoa;
               Devices_count=StaticVariabes_div.gskcounts;
               Devices_model_arr=StaticVariabes_div.gskmodela;
               Devices_Name_arr=StaticVariabes_div.gskdevicenames;
               break;

           case GskG:
               Device_position=StaticVariabes_div.gskpst;
               Devices_num=StaticVariabes_div.gskgrpsarr;
               Devices_count=StaticVariabes_div.gskunder_grpcount;
               Devices_model_arr=StaticVariabes_div.gskgrpsmodarr;
               Devices_Name_arr=StaticVariabes_div.gskgrpsnamarr;
               break;

           case AlxI:
               Device_position=StaticVariabes_div.alexapst;
               Devices_num=StaticVariabes_div.alexanoa;
               Devices_count=StaticVariabes_div.alexacounts;
               Devices_model_arr=StaticVariabes_div.alexamodela;
               Devices_Name_arr=StaticVariabes_div.alexadevicenames;

               break;
            default:


                break;
        }


    }


    public void SwitchtoDeviceFragment(String typ,String mtype,String Dev_num,String room_nam,String mod_el,String dev_nam,int posi_tion){
        Fragment frg; String Frg_i;
        switch (CombFrag.types_dev.valueOf(typ+mtype)) {
            case SwbI:
                StaticVariabes_div.swbpst=posi_tion;
                frg=SwitchBoard_All_Models_Frag.newInstance("test1","test2");
                Frg_i="Swb"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case AcI:
                StaticVariabes_div.acpst=posi_tion;
                frg=Ac_Frag.newInstance("test1","test2");
                Frg_i="Ac"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case RgbI:
                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.rgbbcounts;
                LinearLayout.LayoutParams rgblayout = new LinearLayout.LayoutParams(10, 10);
                rgblayout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    rgblayout.height = 20;
                    rgblayout.width = 20;
                    image_view.setLayoutParams(rgblayout);
                    rgblayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.rgbbpst=posi_tion;
                frg=Rgb_Frag.newInstance("test1","test2");
                Frg_i="Rgb"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case RgbG:

                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.rgbunder_grpcount;
                LinearLayout.LayoutParams myrgblayout = new LinearLayout.LayoutParams(10, 10);
                myrgblayout.gravity=Gravity.CENTER;

                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    myrgblayout.height = 20;
                    myrgblayout.width = 20;
                    image_view.setLayoutParams(myrgblayout);
                    myrgblayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.rgbpst=posi_tion;
                frg=Rgb_Group_Frag.newInstance("test1","test2");

                Frg_i="Rgb"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case DmrI:
                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.dmmrcounts;
                LinearLayout.LayoutParams dmrlayout = new LinearLayout.LayoutParams(10, 10);
                dmrlayout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    dmrlayout.height = 20;
                    dmrlayout.width = 20;
                    image_view.setLayoutParams(dmrlayout);
                    dmrlayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.dmmrpst=posi_tion;
                frg=Dimmer_Frag.newInstance("test1","test2");
                Frg_i="Dmr"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case DmrG:
                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.dimunder_grpcount;
                LinearLayout.LayoutParams mydmrlayout = new LinearLayout.LayoutParams(10, 10);
                mydmrlayout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    mydmrlayout.height = 20;
                    mydmrlayout.width = 20;
                    image_view.setLayoutParams(mydmrlayout);
                    mydmrlayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.dimpst=posi_tion;
                frg=Dimmer_Group_Frag.newInstance("test1","test2");

                Frg_i="Dmr"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case PirI:

                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.pircounts;
                LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(10, 10);
                layout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    layout.height = 20;
                    layout.width = 20;
                    image_view.setLayoutParams(layout);
                    layout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.pirrpst=posi_tion;
                frg=Pir_Frag.newInstance("test1","test2");
                 Frg_i="Pir"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case PirG:

               // if(array_image!=null){
//
              //  }else {
                ly_slider.removeAllViewsInLayout();
                    StaticVariabes_div.frag_num=StaticVariabes_div.pirunder_grpcount;
                    LinearLayout.LayoutParams mylayout = new LinearLayout.LayoutParams(10, 10);
                    mylayout.gravity=Gravity.CENTER;
                    array_image = new ImageView[StaticVariabes_div.frag_num];
                    for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                        image_view = new ImageView(ly_slider.getContext());
                        mylayout.height = 20;
                        mylayout.width = 20;
                        image_view.setLayoutParams(mylayout);
                        mylayout.leftMargin = 10;

                        ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                        image_view.setBackgroundResource(R.drawable.holo_circle);
                        image_view.setId(Integer.parseInt("23"));
                        array_image[i] = image_view;
                        ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.pirpst=posi_tion;
                frg=Pir_Group_Frag.newInstance("test1","test2");

                Frg_i="Pir"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case SdgI:
                StaticVariabes_div.smrtdogpst=posi_tion;
                frg= SmartDog_Frag.newInstance("test1","test2");
                Frg_i="Sdg"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case CurI:
                StaticVariabes_div.curtpst=posi_tion;
                frg= Curtain_Frag.newInstance("test1","test2");
                Frg_i="Cur"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case CurG:
                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.curunder_grpcount;
                LinearLayout.LayoutParams mycurlayout = new LinearLayout.LayoutParams(10, 10);
                mycurlayout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    mycurlayout.height = 20;
                    mycurlayout.width = 20;
                    image_view.setLayoutParams(mycurlayout);
                    mycurlayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.curpst=posi_tion;
                frg=Curtain_Group_Frag.newInstance("test1","test2");

                Frg_i="Cur"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case SOSHI:
                 StaticVariabes_div.sompyscrpst=posi_tion;
                frg=SomphtScreen_Fragment.newInstance("test1","test2");
               // settinglayout.setVisibility(View.GONE);
                Frg_i="SOSH"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;
            case SWG1I:
                StaticVariabes_div.swingscrpst=posi_tion;
                frg=SwingGate_Fragment.newInstance("test1","test2");
                // settinglayout.setVisibility(View.GONE);
                Frg_i="SWG1"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;
            case SLG1I:
                StaticVariabes_div.slidescrpst=posi_tion;
                frg=SlideGate_Fragment.newInstance("test1","test2");
                // settinglayout.setVisibility(View.GONE);
                Frg_i="SLG1"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;

            case PscI:

                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.projscrcounts;
                LinearLayout.LayoutParams psc_layout1 = new LinearLayout.LayoutParams(10, 10);
                psc_layout1.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];

                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    psc_layout1.height = 20;
                    psc_layout1.width = 20;
                    image_view.setLayoutParams(psc_layout1);
                    psc_layout1.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);

                    //}
                }

                StaticVariabes_div.projscrcounts=posi_tion;
                frg=ProjectorScreen_Fragment.newInstance("test1","test2");
                Frg_i="Psc"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;

            case GskG:
            ly_slider.removeAllViewsInLayout();
            StaticVariabes_div.frag_num=StaticVariabes_div.gskunder_grpcount;
            LinearLayout.LayoutParams mylayoutgsk = new LinearLayout.LayoutParams(10, 10);
            mylayoutgsk.gravity=Gravity.CENTER;
            array_image = new ImageView[StaticVariabes_div.frag_num];
            for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                image_view = new ImageView(ly_slider.getContext());
                mylayoutgsk.height = 20;
                mylayoutgsk.width = 20;
                image_view.setLayoutParams(mylayoutgsk);
                mylayoutgsk.leftMargin = 10;

                ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                image_view.setBackgroundResource(R.drawable.holo_circle);
                image_view.setId(Integer.parseInt("23"));
                array_image[i] = image_view;
                ly_slider.addView(array_image[i]);
                //}
            }
            StaticVariabes_div.gskpst=posi_tion;
            frg=GardenSprinkler_Group.newInstance("test1","test2");

            Frg_i="Gsk"+"eb";
            loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
            break;


            case GskI:

                ly_slider.removeAllViewsInLayout();
                StaticVariabes_div.frag_num=StaticVariabes_div.gskcounts;
                LinearLayout.LayoutParams gsklayout = new LinearLayout.LayoutParams(10, 10);
                gsklayout.gravity=Gravity.CENTER;
                array_image = new ImageView[StaticVariabes_div.frag_num];
                for (i = 0; i < StaticVariabes_div.frag_num; i++) {

                    image_view = new ImageView(ly_slider.getContext());
                    gsklayout.height = 20;
                    gsklayout.width = 20;
                    image_view.setLayoutParams(gsklayout);
                    gsklayout.leftMargin = 10;

                    ly_slider.setGravity(Gravity.CENTER_HORIZONTAL);
                    image_view.setBackgroundResource(R.drawable.holo_circle);
                    image_view.setId(Integer.parseInt("23"));
                    array_image[i] = image_view;
                    ly_slider.addView(array_image[i]);
                    //}
                }
                StaticVariabes_div.gskrpst=posi_tion;
                frg=GardenSprinkler_Frag.newInstance("test1","test2");
                Frg_i="Gsk"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);

                break;
            case AlxI:
                StaticVariabes_div.alexapst=posi_tion;
                frg= Alexa_Frag.newInstance("test1","test2");
                Frg_i="Alx"+"eb";
                loadlayout_AllDevices(Dev_num,room_nam,mod_el,dev_nam,posi_tion,frg,Frg_i);
                break;


            default:


                break;
        }


    }




    public void loadlayout_AllDevices(String device_num,String room_name,String dev_type,String devnam,int posis,Fragment frag,String frg_id){
        StaticVariabes_div.devnumber=device_num;
        StaticVariabes_div.devtyp=dev_type;
        StaticVariabes_div.dev_name=devnam;
        String fragid;
        Fragment fragtoview=frag; //SwitchBoard_All_Models_Frag.newInstance("test1","test2");
        // fragid=dev_type+"eb";
        fragid=frg_id;//"Swb"+"eb";
        //  if(fragmentTransaction1!=null)
        //      fragmentTransaction1.remove(swb_all_frag);


        for(int l=0;l<array_image.length;l++){
            array_image[l].setImageResource(R.drawable.holo_circle);
        }



        if(fragmentTransaction1!=null)
        {

            fragmentTransaction1 = fragmentManager.beginTransaction();
            fragmentTransaction1.replace(R.id.fragment_place, fragtoview);// f1_container is your FrameLayout container
            fragmentTransaction1.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            fragmentTransaction1.addToBackStack(null);
            fragmentTransaction1.commit();

            array_image[posis].setImageResource(R.drawable.fill_circle);
        }
        else
        {

            fragmentTransaction1 = fragmentManager.beginTransaction();
           // fragmentTransaction1 = getChildFragmentManager().beginTransaction();
            fragmentTransaction1.add(R.id.fragment_place, fragtoview, fragid);
            fragmentTransaction1.commit();
            array_image[posis].setImageResource(R.drawable.fill_circle);

        }



    }


    public void onBackPressed() {
        Toast.makeText(view.getContext(), "Set operation", Toast.LENGTH_LONG).show();
        popup_exit("Do You Really Want To Exit?");
        //exitapp();
        //finish();
       // android.os.Process.killProcess(android.os.Process.myPid());
    }

    void exitapp(){
        android.os.Process.killProcess(android.os.Process.myPid());
      //  System.gc();
      //  getActivity().finish();
      //  System.exit(0);
    }

    public void popup_exit(final String msg){

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                alertDialogBuilder.setTitle("INFO");
                // set dialog message
                alertDialogBuilder
                        .setMessage(msg)
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                dialog.cancel();
                              //  exitapp();

                            }
                        });
                alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int id) {

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

    ////////////Timer //////////////////////////////////////////////////////

    void  numberpicker(){

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
        int PICKER_RANGE =2;
        displayedValues  = new String[NUMBER_OF_VALUES];
        for(int i=0; i<NUMBER_OF_VALUES; i++)
            displayedValues[i] = String.valueOf(PICKER_RANGE * (i+1));

        np_ontime_cyc.setMinValue(0);
        np_ontime_cyc.setMaxValue(displayedValues.length-1);
        np_ontime_cyc.setDisplayedValues(displayedValues);
        np_ontime_cyc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


       /* int NUMBER_OF_VALUES2 = 29;
        int PICKER_RANGE2 = 2;
        displayedValues2  = new String[NUMBER_OF_VALUES2];
        for(int i=0; i<NUMBER_OF_VALUES2; i++)
            displayedValues2[i] = String.valueOf(PICKER_RANGE2 * (i+1));*/

        np_offtime_cyc.setMinValue(0);
        np_offtime_cyc.setMaxValue(displayedValues.length-1);
        np_offtime_cyc.setDisplayedValues(displayedValues);
        np_offtime_cyc.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

        np_date_seldate.setMaxValue(31); // max value 100
        np_date_seldate.setMinValue(1);   // min value zero
        np_date_seldate.setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);


        String[] arrayPicker = new String[]{"","JAN", "FEB", "MAR", "APR", "MAY", "JUN", "JUL", "AUG", "SEP", "OCT", "NOV", "DEC"};


        //set max value from length array string reduced 1
        np_month_seldate.setMaxValue(arrayPicker.length-1);
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

        for(int y=0;y<np_arr.length-1;y++){

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
    }


    public void feature_sett_enab_disable(boolean val){

        if(val){
        if(StaticVariabes_div.loggeduser_type.equals("A")||StaticVariabes_div.loggeduser_type.equals("SA")) {
            btn_features_sett.setEnabled(true);
            btn_features_sett.setVisibility(View.VISIBLE);
            btn_timer_sett.setEnabled(true);
            btn_timer_sett.setVisibility(View.VISIBLE);
            Mood_longPress = true;
         }else

            if(StaticVariabes_div.loggeduser_type.equals("U")){
                Mood_longPress = true;
            }


        }else{
            btn_features_sett.setEnabled(false);
            btn_features_sett.setVisibility(View.INVISIBLE);
            btn_timer_sett.setEnabled(false);
            btn_timer_sett.setVisibility(View.INVISIBLE);
            Mood_longPress=false;

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
    public void Setoper_rgb(){

       // View rgb_poplay = inflater.inflate(R.layout.popup_rgb, null);
        View rgb_poplay = inflater.inflate(R.layout.popup_rgb_timer, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);

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

        btn_on=(Button) rgb_poplay.findViewById(R.id.btn_rgbon);
        btn_off=(Button) rgb_poplay.findViewById(R.id.btn_rgboff);

        rgbvalue = (TextView) rgb_poplay.findViewById(R.id.textView1);
        colorPicker = (A_rc) rgb_poplay.findViewById(R.id.colorPicker);

        sk = (SeekBar) rgb_poplay.findViewById(R.id.brightseekBar);

        tv_speedval=(TextView)  rgb_poplay.findViewById(R.id.tv_speedvalue);

        save = (Button) rgb_poplay.findViewById(R.id.save);
        cancel = (Button) rgb_poplay.findViewById(R.id.cancel);
        sk.setMax(10);


        btn_Flash.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                   // rgbvalue.setText("Flash");
                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="104,000,000,000,A";
                changebutton(false,false,false,true);

            }
        })	;
        btn_Strobe.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                   // rgbvalue.setText("Strobe");
                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="105,000,000,000,A";
                changebutton(false,false,true,false);


            }
        })	;

        btn_Fade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    //rgbvalue.setText("Fade");
                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="107,000,000,000,A";
                changebutton(false,true,false,false);
                    //transmit(107);

            }
        })	;

        btn_Smooth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                   // rgbvalue.setText("Smooth");
                    rgbvalue.setBackgroundColor(Color.TRANSPARENT);

                rgb_color_effect="106,000,000,000,A";
                changebutton(true,false,false,false);
                    //transmit(106);

            }
        })	;

        btn_Red.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    colorPicker.setColor(Color.RED);
                    int color = colorPicker.getColor();
                    String rgbString = "R:" + Color.red(color) + " G:"
                            + Color.green(color) + " B:" + Color.blue(color);
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.RED);
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
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(0,255, 0));
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
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(0, 0, 255));
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
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
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
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
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
                    rgbvalue.setText("");
                    rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
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
                rgbvalue.setText("");
                rgbvalue.setBackgroundColor(Color.rgb(Color.red(color),Color.green(color), Color.blue(color)));
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

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
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
                if(rgb_color_effect!=null||!rgb_color_effect.equals("")||rgb_color_effect!=""){
                    rgbinst=rgb_color_effect;
                }
                else{
                    rgbinst="0,255,000,000,A";
                }


                String Splitdata[]=rgbinst.split(",");
                rgbinst=transmitdata(Splitdata[0], Splitdata[1], Splitdata[2], Splitdata[3], Splitdata[4]);

                if(rgbbright==null||rgbbright=="")
                    rgbbrightinst="132,000,000,000,A";
                else
                    rgbbrightinst=rgbbright;

                String Splitdata1[]=rgbbrightinst.split(",");
                rgbbrightinst=transmitdata(Splitdata1[0], Splitdata1[1], Splitdata1[2], Splitdata1[3], Splitdata1[4]);

                if(rgb_speed==null||rgb_speed=="")
                    rgb_speedinst="121,000,000,000,A";
                else
                    rgb_speedinst=rgb_speed;

                String Splitdata2[]=rgb_speedinst.split(",");
                rgb_speedinst=transmitdata(Splitdata2[0], Splitdata2[1], Splitdata2[2], Splitdata2[3], Splitdata2[4]);

                String ondatargbrep="102,000,000,000,A";
                String Splitdata3[]=ondatargbrep.split(",");
                ondatargbrep=transmitdata(Splitdata3[0], Splitdata3[1], Splitdata3[2], Splitdata3[3],Splitdata3[4]);

                ar_num_switch.add(0);
                ar_on_data.add( ondatargbrep+";"+rgbinst+";"+rgbbrightinst+";"+rgb_speedinst);
                String offdatargb="103,000,000,000,A";
                String Splitdata4[]=offdatargb.split(",");
                offdatargb=transmitdata(Splitdata4[0], Splitdata4[1], Splitdata4[2], Splitdata4[3],Splitdata4[4]);
                ar_off_data.add(offdatargb);

                dialog1.dismiss();
            }
        });




        //dialog.getWindow().setLayout(450, 650);
        // dialog.dismiss();
        }

    String transmitdata(final String val, final String rc,final String gc,final String bc,String type) {

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

        devn=StaticVariabes_div.devnumber;
        while (devn.length() < 4) devn = "0" + devn;


        if (type.equals("A")) {

            str = "0" + "01" + "000" + devn + room_num + str2 + "000000000000003";
            StaticVariabes_div.log("str" + str, TAG1);

        } else {

            str = "0" + "01" + "000" + devn + room_num + "112" + redStr + greenStr + blueStr + "000003";
            StaticVariabes_div.log("str" + str, TAG1);

        }

        return  str;
    }


    public void Setoper_dmr(){

       // View dmr_poplay  = inflater.inflate(R.layout.popup_dmr_timer, null);
        View dmr_poplay  = inflater.inflate(R.layout.popup_dimmer_timer, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);
        alert.setView(dmr_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) dmr_poplay.findViewById(R.id.save);
        cancel = (Button) dmr_poplay.findViewById(R.id.cancel);
        dim_img= (ImageView) dmr_poplay.findViewById(R.id.image);
       // seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);

        btn_low= (Button) dmr_poplay.findViewById(R.id.btn_dimlow);
        btn_medium= (Button) dmr_poplay.findViewById(R.id.btn_dimmedium);
        btn_high= (Button) dmr_poplay.findViewById(R.id.btn_dimhigh);
        dim_img.setImageResource(R.drawable.dim_level0);

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
                seekArc.setProgress(255);
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
               // if(count<10)count=2;
               // count=dimmrbright(count);
                dimvalue=""+count;
                int cnt=Integer.parseInt(dimvalue);
                seekstatus(cnt);
            }
        });




        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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
                diminst=dimvalue;

                if(diminst==null)diminst="2";

                diminst=transmitdata_dmr(Integer.parseInt(diminst), "B");

                ar_num_switch.add(0);

                ondatadmr=transmitdata_dmr(102,"A");

                ar_on_data.add(ondatadmr+";"+diminst);

                offdatadmr=transmitdata_dmr(103,"A");

                ar_off_data.add(offdatadmr);


                dialog1.dismiss();
            }
        });


    }

    void seekstatus(int i){
        n = i;
        getActivity().runOnUiThread(new Runnable() {
            public void run() {

                try {

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
                    } else if (n > 220 && n <= 255) {
                        dim_img.setImageResource(mThumbIds[9]);
                    }
                }catch(Exception eo){

                }

            }
        });

    }


    String transmitdata_dmr(int val3,String type) {
        String str, val4;
        val4 = "" + val3;
        devn=StaticVariabes_div.devnumber;
        while (devn.length() < 4) devn = "0" + devn;
        while (val4.length() < 3) val4 = "0" + val4;

        if (type.equals("A")) {

            str = "0" + "01" + "000" + devn + room_num + val4 + "000000000000003";
            StaticVariabes_div.log("str" + str, TAG1);

        } else {

            str = "0" + "01" + "000" + devn + room_num + "112" + val4 + "000000000003";
            StaticVariabes_div.log("str" + str, TAG1);

        }

        return str;
    }


    public int dimmrbright(int i){

        if(i<=70){
            return (int)map(i,0,70,2,50);
        }else if(i>70&&i<=100){
            return (int)map(i,71,100,51,255);
        }
        return -1;
    }

    long map(long x, long in_min, long in_max, long out_min, long out_max)
    {
        return (x - in_min) * (out_max - out_min) / (in_max - in_min) + out_min;
    }



    public void Setoper_S141(){

        View alertLayout_51 = inflater.inflate(R.layout.popup_timer_switch4plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){

            if(h==4){
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                    //bulbon.remove(1);
                   // bulbon.remove(bulbon.indexOf(1));
                }

               // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                   // bulbon.remove(bulbon.indexOf(2));
                 bulbon.remove("2");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.bulb02);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.bulb01);
                    s3.setTag(0);
                    bulbon.remove("3");
                   // bulbon.remove(bulbon.indexOf(3));
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s4.getTag().equals(0)) {
                    s4.setBackgroundResource(R.mipmap.bulb02);
                    s4.setTag(1);
                    bulbon.add("4");
                } else {
                    s4.setBackgroundResource(R.mipmap.bulb01);
                    s4.setTag(0);
                   bulbon.remove("4");
                   // bulbon.remove(bulbon.indexOf(1));
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s5.getTag().equals(0)) {
                    s5.setBackgroundResource(R.mipmap.socket_blue);
                    s5.setTag(1);
                    bulbon.add("5");
                } else {
                    s5.setBackgroundResource(R.mipmap.grey_socket);
                    s5.setTag(0);
                    bulbon.remove("5");
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });

        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon.add("98");
                }
              //  Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                  //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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




    public void Setoper_S051(){

        View alertLayout_51 = inflater.inflate(R.layout.popup_timer_switch5plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                    //bulbon.remove(1);
                    // bulbon.remove(bulbon.indexOf(1));
                }

                // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    // bulbon.remove(bulbon.indexOf(2));
                    bulbon.remove("2");
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.bulb02);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.bulb01);
                    s3.setTag(0);
                    bulbon.remove("3");
                    // bulbon.remove(bulbon.indexOf(3));
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s4.getTag().equals(0)) {
                    s4.setBackgroundResource(R.mipmap.bulb02);
                    s4.setTag(1);
                    bulbon.add("4");
                } else {
                    s4.setBackgroundResource(R.mipmap.bulb01);
                    s4.setTag(0);
                    bulbon.remove("4");
                    // bulbon.remove(bulbon.indexOf(1));
                }
                //  Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s5.getTag().equals(0)) {
                    s5.setBackgroundResource(R.mipmap.bulb02);
                    s5.setTag(1);
                    bulbon.add("5");
                } else {
                    s5.setBackgroundResource(R.mipmap.bulb01);
                    s5.setTag(0);
                    bulbon.remove("5");
                }
                //  Log.d("TAG", String.valueOf(bulbon));

            }
        });

        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon.add("98");
                }
                //  Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                    //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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




    public void Setoper_S021(){
        View alertLayout_21 = inflater.inflate(R.layout.popup_timer_switch2plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_21);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
       // dialog1.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
         dialog1.show();

        s1 = (Button) alertLayout_21.findViewById(R.id.s1);
        s2 = (Button) alertLayout_21.findViewById(R.id.s2);

        fan_dec = (Button) alertLayout_21.findViewById(R.id.fan_dec);
        fan_inc = (Button) alertLayout_21.findViewById(R.id.fan_inc);
        fan_speed = (Spinner) alertLayout_21.findViewById(R.id.fan_speed);
        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_21.findViewById(R.id.save);
        cancel = (Button) alertLayout_21.findViewById(R.id.cancel);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(timer_popup_view.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon = new ArrayList<>();


        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

              //  Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    bulbon.remove("2");
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });


        fan_speed.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (i == 0) {
                    fanspeed = "0";
                    bulbon.remove("98");
                } else  {
                    fanspeed = ""+i;
                    bulbon.add("98");
                }
               // Log.d("TAG",fanspeed);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                  //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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



    public void Setoper_S120(){
        View alertLayout_20 = inflater.inflate(R.layout.popup_timer_switch2plus1plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);
        s3 = (Button) alertLayout_20.findViewById(R.id.s3);

        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2,s3};
        bulbon = new ArrayList<>();



        for(int h=0;h<btnarr_timer_switchs.length;h++){

            if(h==2){
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }


           // btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

                // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    bulbon.remove("2");
                }
                //  Log.d("TAG", String.valueOf(bulbon));

            }
        });


        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.socket_blue);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.grey_socket);
                    s3.setTag(0);
                    bulbon.remove("3");
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                    // Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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




    public void Setoper_S030(){
        View alertLayout_20 = inflater.inflate(R.layout.popup_timer_switch3, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);
        s3 = (Button) alertLayout_20.findViewById(R.id.s3);

        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2,s3};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

               // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    bulbon.remove("2");
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });


        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.bulb02);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.bulb01);
                    s3.setTag(0);
                    bulbon.remove("3");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                   // Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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

    public void Setoper_S110(){
        View alertLayout_20 = inflater.inflate(R.layout.popup_timer_switch1plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

                // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.socket_blue);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.grey_socket);
                    s2.setTag(0);
                    bulbon.remove("2");
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                    //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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


    public void Setoper_S020(){
        View alertLayout_20 = inflater.inflate(R.layout.popup_timer_switch2, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_20);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_20.findViewById(R.id.s1);
        s2 = (Button) alertLayout_20.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_20.findViewById(R.id.save);
        cancel = (Button) alertLayout_20.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

               // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    bulbon.remove("2");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });




        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                  //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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


    public void Setoper_S010(){
        View alertLayout_10 = inflater.inflate(R.layout.popup_timer_switch1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_10);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_10.findViewById(R.id.s1);

        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_10.findViewById(R.id.save);
        cancel = (Button) alertLayout_10.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                }

              //  Log.d("TAG", String.valueOf(bulbon));
            }
        });


        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                  //  Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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


    public  void Setoper_S160(){

        View alertLayout_80 = inflater.inflate(R.layout.popup_timer_switch6plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_80);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_80.findViewById(R.id.s1);
        s2 = (Button) alertLayout_80.findViewById(R.id.s2);
        s3 = (Button) alertLayout_80.findViewById(R.id.s3);
        s4 = (Button) alertLayout_80.findViewById(R.id.s4);
        s5 = (Button) alertLayout_80.findViewById(R.id.s5);
        s6 = (Button) alertLayout_80.findViewById(R.id.s6);
        s7 = (Button) alertLayout_80.findViewById(R.id.s7);



        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_80.findViewById(R.id.save);
        cancel = (Button) alertLayout_80.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                    //bulbon.remove(1);
                    // bulbon.remove(bulbon.indexOf(1));
                }

                // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    // bulbon.remove(bulbon.indexOf(2));
                    bulbon.remove("2");
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.bulb02);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.bulb01);
                    s3.setTag(0);
                    bulbon.remove("3");
                    // bulbon.remove(bulbon.indexOf(3));
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s4.getTag().equals(0)) {
                    s4.setBackgroundResource(R.mipmap.bulb02);
                    s4.setTag(1);
                    bulbon.add("4");
                } else {
                    s4.setBackgroundResource(R.mipmap.bulb01);
                    s4.setTag(0);
                    bulbon.remove("4");
                    // bulbon.remove(bulbon.indexOf(1));
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s5.getTag().equals(0)) {
                    s5.setBackgroundResource(R.mipmap.bulb02);
                    s5.setTag(1);
                    bulbon.add("5");
                } else {
                    s5.setBackgroundResource(R.mipmap.bulb01);
                    s5.setTag(0);
                    bulbon.remove("5");
                }
                //  Log.d("TAG", String.valueOf(bulbon));

            }
        });

        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s6.getTag().equals(0)) {
                    s6.setBackgroundResource(R.mipmap.bulb02);
                    s6.setTag(1);
                    bulbon.add("6");
                } else {
                    s6.setBackgroundResource(R.mipmap.bulb01);
                    s6.setTag(0);
                    bulbon.remove("6");
                }
                //  Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s7.getTag().equals(0)) {
                    s7.setBackgroundResource(R.mipmap.socket_blue);
                    s7.setTag(1);
                    bulbon.add("7");
                } else {
                    s7.setBackgroundResource(R.mipmap.grey_socket);
                    s7.setTag(0);
                    bulbon.remove("7");
                }
                // Log.d("TAG", String.valueOf(bulbon));

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                    // Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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



    public  void Setoper_S080(){

        View alertLayout_80 = inflater.inflate(R.layout.popup_timer_switch8, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_80);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.60);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.35);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_80.findViewById(R.id.s1);
        s2 = (Button) alertLayout_80.findViewById(R.id.s2);
        s3 = (Button) alertLayout_80.findViewById(R.id.s3);
        s4 = (Button) alertLayout_80.findViewById(R.id.s4);
        s5 = (Button) alertLayout_80.findViewById(R.id.s5);
        s6 = (Button) alertLayout_80.findViewById(R.id.s6);
        s7 = (Button) alertLayout_80.findViewById(R.id.s7);
        s8 = (Button) alertLayout_80.findViewById(R.id.s8);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_80.findViewById(R.id.save);
        cancel = (Button) alertLayout_80.findViewById(R.id.cancel);




        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7,s8};
        bulbon = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
        }



        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s1.getTag().equals(0)) {
                    s1.setBackgroundResource(R.mipmap.bulb02);
                    s1.setTag(1);
                    bulbon.add("1");
                } else {
                    s1.setBackgroundResource(R.mipmap.bulb01);
                    s1.setTag(0);
                    bulbon.remove("1");
                    //bulbon.remove(1);
                    // bulbon.remove(bulbon.indexOf(1));
                }

               // Log.d("TAG", String.valueOf(bulbon));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s2.getTag().equals(0)) {
                    s2.setBackgroundResource(R.mipmap.bulb02);
                    s2.setTag(1);
                    bulbon.add("2");
                } else {
                    s2.setBackgroundResource(R.mipmap.bulb01);
                    s2.setTag(0);
                    // bulbon.remove(bulbon.indexOf(2));
                    bulbon.remove("2");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s3.getTag().equals(0)) {
                    s3.setBackgroundResource(R.mipmap.bulb02);
                    s3.setTag(1);
                    bulbon.add("3");
                } else {
                    s3.setBackgroundResource(R.mipmap.bulb01);
                    s3.setTag(0);
                    bulbon.remove("3");
                    // bulbon.remove(bulbon.indexOf(3));
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s4.getTag().equals(0)) {
                    s4.setBackgroundResource(R.mipmap.bulb02);
                    s4.setTag(1);
                    bulbon.add("4");
                } else {
                    s4.setBackgroundResource(R.mipmap.bulb01);
                    s4.setTag(0);
                    bulbon.remove("4");
                    // bulbon.remove(bulbon.indexOf(1));
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s5.getTag().equals(0)) {
                    s5.setBackgroundResource(R.mipmap.bulb02);
                    s5.setTag(1);
                    bulbon.add("5");
                } else {
                    s5.setBackgroundResource(R.mipmap.bulb01);
                    s5.setTag(0);
                    bulbon.remove("5");
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });

        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s6.getTag().equals(0)) {
                    s6.setBackgroundResource(R.mipmap.bulb02);
                    s6.setTag(1);
                    bulbon.add("6");
                } else {
                    s6.setBackgroundResource(R.mipmap.bulb01);
                    s6.setTag(0);
                    bulbon.remove("6");
                }
              //  Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s7.getTag().equals(0)) {
                    s7.setBackgroundResource(R.mipmap.bulb02);
                    s7.setTag(1);
                    bulbon.add("7");
                } else {
                    s7.setBackgroundResource(R.mipmap.bulb01);
                    s7.setTag(0);
                    bulbon.remove("7");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });
        s8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (s8.getTag().equals(0)) {
                    s8.setBackgroundResource(R.mipmap.bulb02);
                    s8.setTag(1);
                    bulbon.add("8");
                } else {
                    s8.setBackgroundResource(R.mipmap.bulb01);
                    s8.setTag(0);
                    bulbon.remove("8");
                }
               // Log.d("TAG", String.valueOf(bulbon));

            }
        });



        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if (bulbon != null && bulbon.size()>0) {
                    for (Object area : bulbon) {

                        if (area.equals("98")) {
                            off_data = "723";
                            on_data="71" + fanspeed;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000"+devn+ room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000"+devn +room_num + on_data + "000000000000003");

                        }else {
                            off_data = "30"+area;
                            on_data = "20"+area;

                            ar_num_switch.add(area);
                            ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
                            ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");
                        }

                    }

                   // Toast.makeText(view.getContext(), "Set"+" devn"+devn+" room_num "+room_num+" offdata"+off_data, Toast.LENGTH_LONG).show();


                }
                else {
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


    public void SetData_Pir(){

        on_data="909";
        off_data ="910";

        ar_num_switch.add(0);
        ar_off_data.add("001000" + devn + room_num+ off_data + "000000000000003");
        ar_on_data.add("001000" + devn + room_num + on_data + "000000000000003");

    }


    public void Setoper_gsk(){
        String  on_data_gsk=null;
        String  off_data_gsk=null;

        while (devn.length() < 4) devn = "0" + devn;


        on_data_gsk="001000" + devn + room_num + "201" + "000000000000003";
        off_data_gsk="001000" + devn + room_num+ "301" + "000000000000003";

        ar_num_switch.add(0);
        ar_off_data.add(off_data_gsk);
        ar_on_data.add(on_data_gsk);
    }
    public void Setoper_Cur_nrsh(){

            View crsh_poplay = inflater.inflate(R.layout.popup_crsh_checkbox_tmr, null);
            android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);

            alert.setView(crsh_poplay);
            alert.setCancelable(true);
            final android.support.v7.app.AlertDialog dialog1 = alert.create();
            dialog1.show();

            cb_sheer_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_sheer);
           cb_curtain_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_curtain);


            save = (Button) crsh_poplay.findViewById(R.id.save);
            cancel = (Button) crsh_poplay.findViewById(R.id.cancel);
            //sk.setMax(10);


            // dialog1.getWindow().setLayout(400, 450);
            //dialog1.show();;

            int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
            int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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



                    String  on_data_sh=null;
                    String  off_data_sh=null;

                    String  on_data_cur=null;
                    String  off_data_cur=null;

                    while (devn.length() < 4) devn = "0" + devn;


                    on_data_cur="001000" + devn + room_num + "101" + "000000000000003";
                    off_data_cur="001000" + devn + room_num+ "102" + "000000000000003";


                    if(cb_sheer_select.isChecked()) {

                        String devnam = StaticVariabes_div.dev_name + "SH";

                        mas_adap.open();
                        String shrdevno = mas_adap.getdevno_frmdevname(devnam);
                        mas_adap.close();

                        while (shrdevno.length() < 4) shrdevno = "0" + shrdevno;

                        on_data_sh = "001000" + shrdevno + room_num + "105" + "000000000000003";
                        off_data_sh = "001000" + shrdevno + room_num + "106" + "000000000000003";
                    }

                    if(cb_sheer_select.isChecked()&&cb_curtain_select.isChecked()) {


                        ar_num_switch.add(0);
                        ar_off_data.add(off_data_sh + ";" + off_data_cur);
                        ar_on_data.add(on_data_sh + ";" + on_data_cur);

                    }else if(cb_sheer_select.isChecked()&&!(cb_curtain_select.isChecked())){

                        ar_num_switch.add(0);
                        ar_off_data.add(off_data_sh  );
                        ar_on_data.add(on_data_sh );

                    }else if(cb_curtain_select.isChecked()&&!(cb_sheer_select.isChecked())){

                        ar_num_switch.add(0);
                        ar_off_data.add( off_data_cur);
                        ar_on_data.add(on_data_cur  );

                    }else if(!(cb_curtain_select.isChecked())&&!(cb_sheer_select.isChecked())) {
                         // Toast.makeText(getActivity(),"Please Select Atleast One",Toast)
                    }

                    dialog1.dismiss();
                }
            });




            //dialog.getWindow().setLayout(450, 650);
            // dialog.dismiss();
        }



    public void Setoper_pir_light(){

        View crsh_poplay = inflater.inflate(R.layout.popup_pir_ligh_chkbox_tmr, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_timer);

        alert.setView(crsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        cb_pir_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_pir);
        cb_ligsen_select=(CheckBox) crsh_poplay.findViewById(R.id.cb_ligsen);


        save = (Button) crsh_poplay.findViewById(R.id.save);
        cancel = (Button) crsh_poplay.findViewById(R.id.cancel);
        //sk.setMax(10);


        // dialog1.getWindow().setLayout(400, 450);
        //dialog1.show();;

        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.50);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);
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


                    ar_num_switch.add(0);
                    ar_off_data.add(off_data_ligsen + ";" + off_data_pir);
                    ar_on_data.add(on_data_ligsen + ";" + on_data_pir);

                }else if(cb_ligsen_select.isChecked()&&!(cb_pir_select.isChecked())){

                    ar_num_switch.add(0);
                    ar_off_data.add(off_data_ligsen  );
                    ar_on_data.add(on_data_ligsen );

                }else if(cb_pir_select.isChecked()&&!(cb_ligsen_select.isChecked())){

                    ar_num_switch.add(0);
                    ar_off_data.add(off_data_pir);
                    ar_on_data.add(on_data_pir );

                }else if(!(cb_pir_select.isChecked())&&!(cb_ligsen_select.isChecked())) {
                    // Toast.makeText(getActivity(),"Please Select Atleast One",Toast)
                }

                dialog1.dismiss();
            }
        });




        //dialog.getWindow().setLayout(450, 650);
        // dialog.dismiss();
    }

    public void Setoper_Cur(){


        String  on_data_cur=null;
        String  off_data_cur=null;

        while (devn.length() < 4) devn = "0" + devn;


        on_data_cur="001000" + devn + room_num + "101" + "000000000000003";
        off_data_cur="001000" + devn + room_num+ "102" + "000000000000003";

        ar_num_switch.add(0);
        ar_off_data.add(off_data_cur);
        ar_on_data.add(on_data_cur);

    }
    /*public void to_setval_days(Button b_set,int val){
        if (b_set.getTag().equals(0)) {
            b_set.setBackgroundResource(days_images_blue[val]);
            b_set.setTag(1);
        } else {
            b_set.setBackgroundResource(days_images_grey[val]);
            b_set.setTag(0);
        }
    }*/

/////////////// Mood Setting//////////////////////////////////////////////////////
public void setbulbtag(Button btset,String val){
    if (btset.getTag().equals(0)) {
        btset.setBackgroundResource(R.mipmap.bulb02);
        btset.setTag(1);
        bulbon_moodsett.add(val);
    } else {
        btset.setBackgroundResource(R.mipmap.bulb01);
        btset.setTag(0);
        bulbon_moodsett.remove(val);

        StaticVariabes_div.log("swb removed"+val, TAG1);
        StaticVariabes_div.log("swb removed"+String.valueOf(bulbon_moodsett), TAG1);
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
        S010, S020, S030, S080, S021, S051, S141,S120,S110,S160,DMR1, RGB1, CUR1, WPS1, SDG1, CLNR, CRS1, CLS1, WPD1, SOSH,SWG1,SLG1,PSC1,GSK1
    }
/////////////////////////////////////////////////////////////////////////////////////////////////////
public void Moodsett_modelselection( String deTY,String moodtyp,String moodnum){

    switch (Models.valueOf(deTY)) {
        case S010:

            StaticVariabes_div.log("swb 1_0", TAG1);
            Setmood_S010(moodtyp,moodnum,"Swb");
            break;

        case S020:

            StaticVariabes_div.log("swb 2_0", TAG1);
            Setmood_S020(moodtyp,moodnum,"Swb");
            break;
        case S110:

            StaticVariabes_div.log("swb 1_1_0", TAG1);
            Setmood_S110(moodtyp,moodnum,"Swb");
            break;

        case S030:

            StaticVariabes_div.log("swb 3_0", TAG1);
            Setmood_S030(moodtyp,moodnum,"Swb");
            break;

        case S120:

            StaticVariabes_div.log("swb 2_1_0", TAG1);
            Setmood_S120(moodtyp,moodnum,"Swb");
            break;

        case S080:

            StaticVariabes_div.log("swb 8_0", TAG1);
            Setmood_S080(moodtyp,moodnum,"Swb");
            break;
        case S160:

            StaticVariabes_div.log("swb 6_1_0", TAG1);
            Setmood_S160(moodtyp,moodnum,"Swb");
            break;


        case S021:

            StaticVariabes_div.log("swb 2_1", TAG1);
            Setmood_S021(moodtyp,moodnum,"Swb");
            break;


        case S051:

            StaticVariabes_div.log("swb 5_1", TAG1);
            Setmood_S051(moodtyp,moodnum,"Swb");

            break;

        case S141:

            StaticVariabes_div.log("swb 1_4_1", TAG1);
            Setmood_S141(moodtyp,moodnum,"Swb");

            break;

        case DMR1:
            SetMood_dmr(moodtyp,moodnum,"Dmr");
            StaticVariabes_div.log("dmr", TAG1);

            break;

        case RGB1:
            SetMood_rgb(moodtyp,moodnum,"Rgb");
            StaticVariabes_div.log("rgb ", TAG1);

            break;

        case CLNR:

            StaticVariabes_div.log("cur", TAG1);
            SetMood_Cur(moodtyp,moodnum,"CLNR");

            break;
        case CLS1:
            StaticVariabes_div.log("cur", TAG1);
            SetMood_Cur_sing(moodtyp,moodnum,"CLS1");
            break;
        case CRS1:

            StaticVariabes_div.log("cur", TAG1);
            SetMood_Cur_sing(moodtyp,moodnum,"CRS1");

            break;
        case WPD1:
        case WPS1:

          //  SetMood_Pir(moodtyp,moodnum,"Pir");
            StaticVariabes_div.log("pir", TAG1);
           // switch8_0();
            break;


        case SDG1:

            StaticVariabes_div.log("sdg", TAG1);
            //switch2_1();
            break;

        case PSC1:

            StaticVariabes_div.log("psc", TAG1);
            //switch2_1();
            SetMood_Psc_single(moodtyp,moodnum,"PSC1");
            break;

        case SOSH:

            StaticVariabes_div.log("somp", TAG1);
            //switch2_1();
            SetMood_Sompy_single(moodtyp,moodnum,"SOSH");
            break;
        case SWG1:

            StaticVariabes_div.log("swing", TAG1);
            //switch2_1();
            SetMood_swing_single(moodtyp,moodnum,"SWG1");
            break;
        case SLG1:

            StaticVariabes_div.log("slide", TAG1);
            //switch2_1();
            SetMood_slide_single(moodtyp,moodnum,"SLG1");
            break;


        case GSK1:

            StaticVariabes_div.log("Gsk", TAG1);

          //  SetMood_Psc_single(moodtyp,moodnum,"GSK1");
            break;

        /*case SLT1:

            StaticVariabes_div.log("swb SLT1", TAG1);
            switchSLT1_0();

            break;*/
        default:
            System.out.println("Not matching");
    }
}




    public void Setmood_S141(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        fan_value="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        // View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch_51, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb4plus1_1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(view.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){

            if(h==4){
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {

                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("http"+"houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else {
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_timer_switchs[position].setTag(1);
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
                // Intent i = new Intent(getActivity(), MoodSetting_Edit_Activity.class);
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                //  Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag_sock(s5,"5");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                //  Log.d("TAG",fanspeed);
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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                }
                if ((final_bulbs_value == null) || (final_bulbs_value.equals(""))) {
                    final_bulbs_value = "0";

                }

                if ((fan_value == null) || (fan_value.equals(""))) {
                    fan_value = "0";
                }


                if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                    status_swb="OFF";
                }else{
                    status_swb="ON";
                }

                StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                if(cswb!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated"+StaticVariabes_div.dev_name, TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted"+StaticVariabes_div.dev_name, TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }











    public void Setmood_S051(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        fan_value="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
       // View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch_51, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb4plus1_1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
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

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(view.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("http"+"houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
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
               // Intent i = new Intent(getActivity(), MoodSetting_Edit_Activity.class);
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });


        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
              //  Log.d("TAG",fanspeed);
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
                        Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
                    }
                        if ((final_bulbs_value == null) || (final_bulbs_value.equals(""))) {
                            final_bulbs_value = "0";

                        }

                        if ((fan_value == null) || (fan_value.equals(""))) {
                            fan_value = "0";
                        }


                        if(fan_value.equals("0")&&final_bulbs_value.equals("0")){
                            status_swb="OFF";
                        }else{
                            status_swb="ON";
                        }

                        StaticVariabes_div.log("bulbval"+final_bulbs_value, TAG1);
                        StaticVariabes_div.log("fanspeedvals"+fan_value, TAG1);

                        Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                        if(cswb!=null) {

                            pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                            StaticVariabes_div.log("updated"+StaticVariabes_div.dev_name, TAG1);

                        }
                        else{

                            pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                            StaticVariabes_div.log("inserted"+StaticVariabes_div.dev_name, TAG1);
                        }


                       // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                    dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }


    public void Setmood_S021(final String Moodtype,final String MoodNumber,final String tyname){
        fan_value="";
        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch_21, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb21, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();



        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);

        fan_dec = (Button) alertLayout_51.findViewById(R.id.fan_dec);
        fan_inc = (Button) alertLayout_51.findViewById(R.id.fan_inc);
        fan_speed = (Spinner) alertLayout_51.findViewById(R.id.fan_speed);
        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);
        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        final String[] str = {"0", "1", "2", "3", "4"};
        ArrayAdapter<String> adp2 = new ArrayAdapter<String>(view.getRootView().getContext(), R.layout.spinner_item, str);
        adp2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        fan_speed.setAdapter(adp2);


        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
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
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
               // Log.d("TAG",fanspeed);
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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                    Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                    if(cswb!=null) {

                        pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("updated", TAG1);

                    }
                    else{

                        pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                        StaticVariabes_div.log("inserted", TAG1);
                    }


                   // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }



    public void Setmood_S110(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch2, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb1plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){

            if(h==1){
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else{
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }

        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else{
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }

                    }

                }
            }




        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                //Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag_sock(s2,"2");
                //Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                if(cswb!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                //  Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }





    public void Setmood_S020(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch2, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb2, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                //Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                //Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                    Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                    if(cswb!=null) {

                        pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("updated", TAG1);

                    }
                    else{

                        pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                        StaticVariabes_div.log("inserted", TAG1);
                    }


                  //  Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }






    public void Setmood_S120(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch3, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb2plus1sockt, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);
        s3 = (Button) alertLayout_51.findViewById(R.id.s3);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2,s3};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            if(h==2){
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
                btnarr_timer_switchs[h].setTag(0);

            }else{
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }




        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else {
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }

                       /* btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);*/
                    }

                }
            }




        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                //  Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag_sock(s3,"3");
                //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                if(cswb!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }












    public void Setmood_S030(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch3, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb3, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);
        s3 = (Button) alertLayout_51.findViewById(R.id.s3);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2,s3};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                    Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                    if(cswb!=null) {

                        pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("updated", TAG1);

                    }
                    else{

                        pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                        StaticVariabes_div.log("inserted", TAG1);
                    }


                   // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }


    public void Setmood_S010(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch1, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        //  TextView tvv = (TextView) alertLayout1.findViewById(R.id.tv);
        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);



        btnarr_timer_switchs=new Button[]{s1};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }




        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });

        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

               // Log.d(TAG1, String.valueOf(bulbon_moodsett));
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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                    Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                    if(cswb!=null) {

                        pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("updated", TAG1);

                    }
                    else{

                        pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                        StaticVariabes_div.log("inserted", TAG1);
                    }


                   // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }





    public void Setmood_S160(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch8, null);
        View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb6plus1, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);
        s3 = (Button) alertLayout_51.findViewById(R.id.s3);
        s4 = (Button) alertLayout_51.findViewById(R.id.s4);
        s5 = (Button) alertLayout_51.findViewById(R.id.s5);
        s6 = (Button) alertLayout_51.findViewById(R.id.s6);
        s7 = (Button) alertLayout_51.findViewById(R.id.s7);

        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){

            if(h==6){
                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.grey_socket);
            }else {

                btnarr_timer_switchs[h].setTag(0);
                btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
            }


            /*btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);*/
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.socket_blue);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add(""+bulbvalue);
                        }else {
                            btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                            btnarr_timer_switchs[position].setTag(1);
                            bulbon_moodsett.add("" + bulbvalue);
                        }

                      /*  btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);*/
                    }

                }
            }





        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

                // Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
                //   Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
                //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s6,"6");
                //Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag_sock(s7,"7");
                // Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                if(cswb!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }


                // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }









    public void Setmood_S080(final String Moodtype,final String MoodNumber,final String tyname){

        final_bulbs_value="";
        bulbs_to_on="";
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View alertLayout_51 = inflater.inflate(R.layout.popup_moodswitch8, null);
      View alertLayout_51 = inflater.inflate(R.layout.popup_moodsett_swb8, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        //  alert.setTitle("Login");
        // this is set the view from XML inside AlertDialog
        alert.setView(alertLayout_51);
        // disallow cancel of AlertDialog on click of back button and outside touch
        alert.setCancelable(true);

        dialog1 = alert.create();
        dialog1.show();


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();

        s1 = (Button) alertLayout_51.findViewById(R.id.s1);
        s2 = (Button) alertLayout_51.findViewById(R.id.s2);
        s3 = (Button) alertLayout_51.findViewById(R.id.s3);
        s4 = (Button) alertLayout_51.findViewById(R.id.s4);
        s5 = (Button) alertLayout_51.findViewById(R.id.s5);
        s6 = (Button) alertLayout_51.findViewById(R.id.s6);
        s7 = (Button) alertLayout_51.findViewById(R.id.s7);
        s8 = (Button) alertLayout_51.findViewById(R.id.s8);

        save = (Button) alertLayout_51.findViewById(R.id.save);
        cancel = (Button) alertLayout_51.findViewById(R.id.cancel);

        btn_moodlist= (Button) alertLayout_51.findViewById(R.id.mood_list);


        btnarr_timer_switchs=new Button[]{s1,s2,s3,s4,s5,s6,s7,s8};
        bulbon_moodsett = new ArrayList<>();

        for(int h=0;h<btnarr_timer_switchs.length;h++){
            btnarr_timer_switchs[h].setTag(0);
            btnarr_timer_switchs[h].setBackgroundResource(R.mipmap.bulb01);
        }
        pre_adap.open();
        Cursor cswbprev=pre_adap.fetch_devicerow( StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);

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
                        btnarr_timer_switchs[position].setBackgroundResource(R.mipmap.bulb02);
                        btnarr_timer_switchs[position].setTag(1);
                        bulbon_moodsett.add(""+bulbvalue);
                    }

                }
            }





        }
        pre_adap.close();


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
        s1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s1,"1");

               // Log.d(TAG1, String.valueOf(bulbon_moodsett));
            }
        });

        s2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s2,"2");
             //   Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s3,"3");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s4,"4");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s5,"5");
              //  Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                setbulbtag(s6,"6");
                //Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s7,"7");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

            }
        });
        s8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setbulbtag(s8,"8");
               // Log.d(TAG1, String.valueOf(bulbon_moodsett));

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
                    Toast.makeText(view.getContext(), "OFF operation Set", Toast.LENGTH_LONG).show();
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

                    Cursor cswb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Swb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Swb no"+StaticVariabes_div.devnumber, TAG1);


                    if(cswb!=null) {

                        pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Swb", StaticVariabes_div.devnumber, fan_value,final_bulbs_value,status_swb,StaticVariabes_div.loggeduser);
                        StaticVariabes_div.log("updated", TAG1);

                    }
                    else{

                        pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Swb",tyname,StaticVariabes_div.devnumber,final_bulbs_value,fan_value,StaticVariabes_div.devtyp,status_swb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                        StaticVariabes_div.log("inserted", TAG1);
                    }


                   // Toast.makeText(view.getContext(), "Set" + " devn" + devn + " room_num " + room_num + " offdata" + off_data, Toast.LENGTH_LONG).show();

                dialog1.dismiss();
                pre_adap.close();

            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();

            }
        });


    }

    void progressstart(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar==null){
                    progressBar = new ProgressDialog(getActivity());
                    progressBar.setCancelable(false);
                    progressBar.setMessage("Applying Settings ...");
                    // progressBar.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressBar.setProgress(0);
                    progressBar.setMax(100);
                    progressBar.show();
                }else{
                    if(progressBar!=null){
                        progressBar.dismiss();
                        progressBar=null;
                    }
                }
            }
        });


    }
    void progressstop(){
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(progressBar!=null){
                    progressBar.dismiss();
                    progressBar=null;
                }
            }
        });
    }

    ///////////////////////////////////////////////////////////////////

    static char processSwitchData(boolean sw1, boolean sw2, boolean sw3, boolean sw4)
    {
        String base2 = "" + ((sw4) ? 1 : 0) + ((sw3) ? 1 : 0) + ((sw2) ? 1 : 0) + ((sw1) ? 1 : 0);
        base2 = String.format("%02X", (0xFF & (Integer.parseInt(base2, 2))));
        return base2.charAt(1);
    }

    ////////////////////////DELAY///////////////////////////////////////////////////////////////////
    public void delaytime(int delayval){
        try {
            Thread.sleep(delayval);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    //@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@


    void ButtonOutprocess(String swbno,char cc1,char cc2,char cc3,char ff1,char ff2,char ff3,char ff4) {
        if (Tcp_con.isClientStarted) {
            StaticVariabes_div.log("button out process connest", TAG1);
            while (swbno.length() < 4)
                swbno = "0" + swbno;
            while(StaticVariabes_div.room_n.length()<2)StaticVariabes_div.room_n="0"+StaticVariabes_div.room_n;

            String str = "0" + "01" + "000" +  swbno +StaticVariabes_div.room_n+ "999" + cc1+cc2+cc3+"00000"+ff1+ff2+ff3+ff4+"000";
            StaticVariabes_div.log("out swb/fan" + str, TAG1);

            //byte[] op= Blaster.WriteData(str, 1);
            byte[] op=str.getBytes();
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            StaticVariabes_div.log("bout" + result + "$$$" + "999", TAG1);

                Tcp_con.WriteBytes(result);


        }
    }


    public void SetMood_rgb(final String Moodtype,final String MoodNumber,final String tyname){
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        //View rgb_poplay = inflater.inflate(R.layout.popup_moodsettng_rgb, null);
       // View rgb_poplay = inflater.inflate(R.layout.popup_moodsett_rgb, null);
        View rgb_poplay = inflater.inflate(R.layout.popup_moodsettings_rgb, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);

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

        sk.setMax(10);

        tb_on_off=(ToggleButton) rgb_poplay.findViewById(R.id.tb_rgb_on_off);


        pre_adap.open();


        Cursor crgbprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Rgb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Rgb no"+StaticVariabes_div.devnumber, TAG1);

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
                    //tv_rgbvalue.setText("flash");
                    changebutton(false,false,false,true);
                } else if (rgbfirstelementArray[0].equals("105")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                    //tv_rgbvalue.setText("strobe");
                    changebutton(false,false,true,false);
                } else if (rgbfirstelementArray[0].equals("106")) {

                    tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                   // tv_rgbvalue.setText("smooth");
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

        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
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

               // tv_rgbvalue.setText("Flash");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="104,000,000,000,A";
                changebutton(false,false,false,true);

            }
        })	;
        btn_Strobe.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                //tv_rgbvalue.setText("Strobe");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="105,000,000,000,A";
                changebutton(false,false,true,false);


            }
        })	;

        btn_Fade.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

               // tv_rgbvalue.setText("Fade");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="107,000,000,000,A";
                changebutton(false,true,false,false);
            }
        })	;

        btn_Smooth.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

               // tv_rgbvalue.setText("Smooth");
                tv_rgbvalue.setBackgroundColor(Color.TRANSPARENT);
                rgb_color_effect="106,000,000,000,A";
                changebutton(true,false,false,false);
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
                rgb_color_effect="0,"+Color.red(color)+","+Color.green(color)+","+Color.blue(color)+",B";
                changebutton(false,false,false,false);
            }
        });

        btn_Speedp.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if(ispeed<130){
                    ispeed++;
                }
                rgb_speed=ispeed+",000,000,000,A";
                tv_speedval.setText(String.valueOf(ispeed-120));

            }
        })	;
        btn_Speedm.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {


                if(ispeed>121){
                    ispeed--;
                }
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
                rgbbright=count+130+",000,000,000,A";
            }
        });


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
         int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.55);
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


                if(rgbbright==null||rgbbright=="")
                    rgbbrightinst="132,000,000,000,A";
                else
                    rgbbrightinst=rgbbright;


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
                Cursor crgb=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Rgb",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Rgb no"+StaticVariabes_div.devnumber+" final_rgbdata"+final_rgbdata, TAG1);


                if(crgb!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Rgb", StaticVariabes_div.devnumber, final_rgbdata,final_on_off_rgb,status_rgb,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Rgb",tyname,StaticVariabes_div.devnumber,final_on_off_rgb,final_rgbdata,StaticVariabes_div.devtyp,status_rgb,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();

                dialog1.dismiss();
            }
        });


        //dialog.getWindow().setLayout(450, 650);
        // dialog.dismiss();
    }



    void transmitdata_rgb(String rgbno,String val, final String rc,final String gc,final String bc,String type)
    {

        if (Tcp_con.isClientStarted) {
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
            StaticVariabes_div.log("rgbno" + rgbno, TAG1);

            while (rgbno.length() < 4) rgbno = "0" + rgbno;

            while (StaticVariabes_div.room_n .length() < 2) StaticVariabes_div.room_n  = "0" + StaticVariabes_div.room_n ;

            if (type.equals("A")) {

                str = "0" + "01" + "000" + rgbno + StaticVariabes_div.room_n + str2 + "000000000000000";
                StaticVariabes_div.log("str" + str, TAG1);

            } else {

                str = "0" + "01" + "000" + rgbno + StaticVariabes_div.room_n + "112" + redStr + greenStr + blueStr + "000000";
                StaticVariabes_div.log("str" + str, TAG1);

            }
            byte[] op=str.getBytes();
           // byte[] op = Blaster.WriteData(str, 1);
            byte[] result = new byte[32];
            result[0] = (byte) '*';
            result[31] = (byte) '#';
            for (int i = 1; i < 31; i++)
                result[i] = op[(i - 1)];
            StaticVariabes_div.log("bout" + result + "$$$" + val, TAG1);
            Tcp_con.WriteBytes(result);
        }

    }


    public void SetMood_dmr(final String Moodtype,final String MoodNumber,final String tyname){
        final LayoutInflater inflater = getActivity().getLayoutInflater();
      //  View dmr_poplay  = inflater.inflate(R.layout.popup_moodsett_dmr, null);
        View dmr_poplay  = inflater.inflate(R.layout.popup_moodseting_dimmr, null);
       // android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyDialogTheme1);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);
        alert.setView(dmr_poplay);
        alert.setCancelable(true);
       // final android.support.v7.app.AlertDialog dialog1 = alert.create();
       // dialog1.show();

        dialog1 = alert.create();
        dialog1.show();


       // int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.75);
       // int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.65);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();


        save = (Button) dmr_poplay.findViewById(R.id.save);
        cancel = (Button) dmr_poplay.findViewById(R.id.cancel);
        dim_img= (ImageView) dmr_poplay.findViewById(R.id.image);
        // seekArc= (ShaderSeekArc) dmr_poplay.findViewById(R.id.seek_arc);

        btn_low= (Button) dmr_poplay.findViewById(R.id.btn_dimlow);
        btn_medium= (Button) dmr_poplay.findViewById(R.id.btn_dimmedium);
        btn_high= (Button) dmr_poplay.findViewById(R.id.btn_dimhigh);
        dim_img.setImageResource(R.drawable.dim_level0);
        tb_on_off=(ToggleButton) dmr_poplay.findViewById(R.id.tb_dmr_on_off);
       // sk.setProgress(0);

        btn_moodlist= (Button) dmr_poplay.findViewById(R.id.mood_list);


        pre_adap.open();


        Cursor cdmrprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Dmr",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Dmr no"+StaticVariabes_div.devnumber, TAG1);


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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });
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




      /*  int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.80);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;*/


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

                Cursor cdmr=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Dmr",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Dmr no"+StaticVariabes_div.devnumber+" final_dmrdata"+diminst, TAG1);


                if(cdmr!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Dmr", StaticVariabes_div.devnumber, diminst,final_on_off_dmr,status_dmr,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);

                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Dmr",tyname,StaticVariabes_div.devnumber,final_on_off_dmr,diminst,StaticVariabes_div.devtyp,status_dmr,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }


    void transmitdata_dmr_mood(String dmrno,int val3,String type)
    {  String str,val4;
        val4=""+val3;

       String room= StaticVariabes_div.room_n;
        while(dmrno.length()<4)dmrno="0"+dmrno;
        while(val4.length()<3)val4="0"+val4;
        while(room.length()<2)room="0"+room;

        if(type.equals("A")) {

            str = "0" + "01" + "000" + dmrno + room + val4 + "000000000000000";
            StaticVariabes_div.log("str" + str, TAG1);

        }else {

            str = "0" + "01" + "000" + dmrno + room + "112" + val4 + "000000000000";
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



    public void SetMood_Pir(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View pir_poplay = inflater.inflate(R.layout.popup_moodsett_pir, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.MyDialogTheme1);
        alert.setView(pir_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) pir_poplay.findViewById(R.id.save);
        cancel = (Button) pir_poplay.findViewById(R.id.cancel);
        tb_on_off=(ToggleButton) pir_poplay.findViewById(R.id.tb_pir_on_off);

        btn_moodlist= (Button) pir_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.30);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"Pir",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Pir no"+StaticVariabes_div.devnumber, TAG1);

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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog1.dismiss();
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                i.putExtra("Mood_selected_num",MoodNumber_Sel);
                startActivity(i);
            }
        });

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

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Pir",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Pir no"+StaticVariabes_div.devnumber+" final_pirdata"+final_on_off_pir, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Pir", StaticVariabes_div.devnumber, final_on_off_ligsen,final_on_off_pir,status_pir,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Pir",tyname,StaticVariabes_div.devnumber,final_on_off_pir,final_on_off_ligsen,null,status_pir,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted", TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }



    void transmitdata_pirmood(String pirno,String val4,String type)
    {

        String str=null;

        String roomn= StaticVariabes_div.room_n;
        while(pirno.length()<4)pirno="0"+pirno;

        while(roomn.length()<2)roomn="0"+roomn;


        if(type.equals("A")) {
            while(val4.length()<3)val4="0"+val4;

            str = "0" + "01" + "000" + pirno + roomn+val4+"000000000000000";
            StaticVariabes_div.log("str" + str, TAG1);


        }  else if(type.equals("C")) {

            while(val4.length()<1)val4="0";


            str = "0" + "01" + "000" + pirno + roomn+"103"+"0000"+val4+"0000000000";
            StaticVariabes_div.log("str" + str, TAG1);


        }else {

            while(val4.length()<4)val4="0"+val4;

            str = "0" + "01" + "000" + pirno + roomn+"000"+val4+"00000000000";
            StaticVariabes_div.log("str" + str, TAG1);


        }

        //byte[] op= Blaster.WriteData(str, 1);
        byte[] op=str.getBytes();
        byte[] result = new byte[32];
        result[0] = (byte) '*';
        result[31] = (byte) '#';
        for (int i = 1; i < 31; i++)
            result[i] = op[(i - 1)];
        StaticVariabes_div.log("bout" + result + "$$$" + val4, TAG1);
        Tcp_con.WriteBytes(result);


    }
    public void SetMood_Cur_sing(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
       // View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_sin_cur, null);
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_singlecurt, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);



        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"Cur",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Cur no"+StaticVariabes_div.devnumber, TAG1);

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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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
                if(curtinst==null)curtinst="102";


                if(curtinst.equals("102")){
                    status_cur="OFF";
                }else{
                    status_cur="ON";
                }

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Cur",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Cur no"+StaticVariabes_div.devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Cur", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Cur",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }
    public void SetMood_Cur(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_nrsh_cur, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);


        btn_open_sheer = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_sheer);
        btn_close_sheer = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_sheer);

        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.50);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"Cur",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Cur no"+StaticVariabes_div.devnumber, TAG1);

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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"Cur",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"Cur no"+StaticVariabes_div.devnumber+" final_curdata"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "Cur", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Cur",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "Cur"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }

    void transmitcurt(String curdevno, String val)
    {
        String str,val2;
        val2=""+val;
        String room= StaticVariabes_div.room_n;
        while(curdevno.length()<4)curdevno="0"+curdevno;
        while(val2.length()<3)val2="0"+val2;
        while(room.length()<2)room="0"+room;

        StaticVariabes_div.log("roomno"+room, TAG1);
        StaticVariabes_div.log("val2"+val2, TAG1);

        str = "0"+"01"+"000"+curdevno+room+val2+"000000000000000";
        StaticVariabes_div.log(" curt str"+str, TAG1);


        //byte[] op= Blaster.WriteData(str, 1);
        byte[] op=str.getBytes();
        byte[] result = new byte[32];
        result[0] = (byte) '*';
        result[31] = (byte) '#';
        for (int i = 1; i < 31; i++)
            result[i] = op[(i - 1)];
        StaticVariabes_div.log("bout" + result + "$$$" + val2, TAG1);
        Tcp_con.WriteBytes(result);
    }
    //--------------
    //sompy


    public void SetMood_Sompy_single(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        // View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_sin_cur, null);
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_single_sompy, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);



        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"SOSH",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"somp no"+StaticVariabes_div.devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("somp curtain"+curtdata, TAG1);
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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"SOSH",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"SOSH no"+StaticVariabes_div.devnumber+" final_SOSHdata"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "SOSH", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"SOSH",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "SOSH"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }

//------------swing-----//



    public void SetMood_swing_single(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        // View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_sin_cur, null);
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodset_single_swing_gate, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);



        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"SWG1",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"swing no"+StaticVariabes_div.devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("swing gate "+curtdata, TAG1);
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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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
                btn_open_curtain.setBackgroundResource(R.drawable.swing_off_open);
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

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"SWG1",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"SWG1 no"+StaticVariabes_div.devnumber+" final_SWG1data"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "SWG1", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"SWG1",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "SWG1"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }





  //-------slide---------------//


    public void SetMood_slide_single(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        // View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_sin_cur, null);
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodset_single_slide_gate, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);



        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

        pre_adap.open();

        Cursor ccurtprev=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber,"SLG1",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
        StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"slide no"+StaticVariabes_div.devnumber, TAG1);

        if(ccurtprev!=null) {
            String curtdata2=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
            String curtdata=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
            String curt_typ=ccurtprev.getString(ccurtprev.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
            curtinst=curtdata;
            StaticVariabes_div.log("slide gate "+curtdata, TAG1);
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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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
                btn_close_curtain.setBackgroundResource(R.drawable.slide_close_off);
                btn_open_curtain.setBackgroundResource(R.drawable.slide_open_on);



            }
        });

        btn_close_curtain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                curtval="102";
                btn_open_curtain.setBackgroundResource(R.drawable.slide_open_off);
                btn_close_curtain.setBackgroundResource(R.drawable.slide_close_on);
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

                Cursor cpir=pre_adap.fetch_devicerow(StaticVariabes_div.housenumber,StaticVariabes_div.room_n,MoodNumber,"SLG1",StaticVariabes_div.devnumber,StaticVariabes_div.loggeduser);
                StaticVariabes_div.log("houseno"+StaticVariabes_div.housenumber+"roomno"+StaticVariabes_div.room_n+"setno"+MoodNumber+"SLG1 no"+StaticVariabes_div.devnumber+" final_SLG1data"+curtinst, TAG1);


                if(cpir!=null) {

                    pre_adap.updatevalfordevtyp(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber, "SLG1", StaticVariabes_div.devnumber, curtinst,curtinst2,status_cur,StaticVariabes_div.loggeduser);
                    StaticVariabes_div.log("updated", TAG1);
                }
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"SLG1",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "SLG1"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }



    //------------------------
    public void SetMood_Psc_single(final String Moodtype,final String MoodNumber,final String tyname) {
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        // View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_sin_cur, null);
        View cur_nrsh_poplay = inflater.inflate(R.layout.popup_moodsett_singlepsc, null);
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity(), R.style.popup_theme_moodsett);
        alert.setView(cur_nrsh_poplay);
        alert.setCancelable(true);
        final android.support.v7.app.AlertDialog dialog1 = alert.create();
        dialog1.show();

        save = (Button) cur_nrsh_poplay.findViewById(R.id.save);
        cancel = (Button) cur_nrsh_poplay.findViewById(R.id.cancel);



        btn_open_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btopen_curtain);
        btn_close_curtain = (Button) cur_nrsh_poplay.findViewById(R.id.btclose_curtain);


        btn_moodlist= (Button) cur_nrsh_poplay.findViewById(R.id.mood_list);


        int width = (int) (getResources().getDisplayMetrics().widthPixels * 0.70);
        int height = (int) (getResources().getDisplayMetrics().heightPixels * 0.40);
        dialog1.getWindow().setLayout(width, height);
        dialog1.show();;

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


        btn_moodlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(), MoodSettingEdit.class);
                startActivity(i);
            }
        });

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
                else{

                    pre_adap.insert(StaticVariabes_div.housenumber, StaticVariabes_div.housename,StaticVariabes_div.room_n,StaticVariabes_div.roomname,MoodNumber,Moodtype,"Psc",tyname,StaticVariabes_div.devnumber,curtinst2,curtinst,StaticVariabes_div.devtyp,status_cur,StaticVariabes_div.dev_name,StaticVariabes_div.loggeduser,null,null,null,null,null,null);
                    StaticVariabes_div.log("inserted "+StaticVariabes_div.housenumber+","+ StaticVariabes_div.housename+","+ StaticVariabes_div.room_n+","+ StaticVariabes_div.roomname+","+ MoodNumber+","+ Moodtype+","+ "Psc"+","+ tyname+","+ StaticVariabes_div.devnumber+","+ curtinst2+","+ curtinst+","+ null+","+ status_cur+","+ StaticVariabes_div.dev_name, TAG1);
                }
                pre_adap.close();
                dialog1.dismiss();
            }
        });


    }

    void Apply_moodsett() {

        if (Tcp_con.isClientStarted) {

            pre_adap.open();
            int moodcount= pre_adap.getCount_housenoroomnomoodnum(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel);
            pre_adap.close();

            if(moodcount>0) {
                Thread th = new Thread() {
                    public void run() {
                        progressstart();
                        //switchboard//
                        pre_adap.open();
                        int swbcount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Swb");
                        StaticVariabes_div.log("swbcount" + swbcount, TAG1);

                        swbnoarr = new String[swbcount];
                        swbnoarr = pre_adap.getall_housenoroomnodevicetype(swbcount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Swb");

                        for (int y = 0; y < swbnoarr.length; y++) {
                            StaticVariabes_div.log("swbnoarr" + swbnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Swb", swbnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String swbonoff = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String fandata = null;
                                fandata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                StaticVariabes_div.log("swbonoff" + swbonoff, TAG1);
                                StaticVariabes_div.log("fandata" + fandata, TAG1);

                                String[] swbonoffArray = swbonoff.split(";");
                                String[] fanvaluesArray = fandata.split(";");
                                boolean bulbstatus[] = {false, false, false, false, false, false, false, false, false, false, false, false, false, false};
                                boolean fanstatus[] = {false, false, false, false};
                                char fanspeedtosend[] = {'0', '0', '0', '0'};
                                if (swbonoff.equals("0")) {
                                    StaticVariabes_div.log("swbonoff zero" + swbonoff, TAG1);
                                } else {
                                    for (int k = 0; k < swbonoffArray.length; k++) {
                                        StaticVariabes_div.log("swbonoffarray" + swbonoffArray[k], TAG1);
                                        int bulbvalue;
                                        if (swbonoff != null && swbonoffArray != null) {
                                            bulbvalue = Integer.parseInt(swbonoffArray[k]);
                                            int position = (bulbvalue - 1);
                                            bulbstatus[position] = true;
                                        }

                                    }
                                }


                                char c1 = processSwitchData(bulbstatus[0], bulbstatus[1], bulbstatus[2], bulbstatus[3]);
                                StaticVariabes_div.log("c1=" + c1 + "--bulb1=" + bulbstatus[0] + "--bulb2=" + bulbstatus[1] + "--bulb3=" + bulbstatus[2] + "--bulb4=" + bulbstatus[3], TAG1);
                                char c2 = processSwitchData(bulbstatus[4], bulbstatus[5], bulbstatus[6], bulbstatus[7]);
                                char c3 = processSwitchData(bulbstatus[8], bulbstatus[9], bulbstatus[10], bulbstatus[11]);
                                char f1 = '0';
                                char f2 = '0';
                                char f3 = '0';
                                char f4 = '0';
                                char fv1 = '0';
                                char fv2 = '0';
                                char fv3 = '0';
                                char fv4 = '0';

                                if ((fandata != null) || (fandata != "") || (fandata.equals(""))) {

                                    if (fandata.equals("0")) {
                                        f1 = 'B';
                                    } else {
                                        f1 = fandata.charAt(0);
                                    }
                                }


                                ButtonOutprocess(swbnoarr[y], c1, c2, c3, f1, f2, f3, f4);

                                delaytime(300);
                            }
                            if (cr != null)
                                cr.close();
                        }

                        //RGB//
                        //##########################################################################//

                        int rgbcount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Rgb");
                        StaticVariabes_div.log("rgb count" + rgbcount, TAG1);
                        rgbnoarr = new String[rgbcount];
                        rgbnoarr = pre_adap.getall_housenoroomnodevicetype(rgbcount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Rgb");

                        for (int y = 0; y < rgbnoarr.length; y++) {
                            StaticVariabes_div.log("rgbnoarr" + rgbnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Rgb", rgbnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String rgbonoff = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String rgbdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String[] rgbcolorArray = rgbdata.split(";");


                                if (rgbonoff != null)
                                    if (rgbonoff.equals("1")) {

                                        String rgbondataelement = "102,0,0,0,A";

                                        String rgb_color_element = rgbcolorArray[0];
                                        String rgb_brightness_element = rgbcolorArray[1];
                                        String rgbdata_speed_element = rgbcolorArray[2];


                                        StaticVariabes_div.log("rgb0data" + rgbondataelement + "rgbnoarr" + rgbnoarr[y], TAG1);
                                        StaticVariabes_div.log("rgb1data" + rgb_color_element + "rgbnoarr" + rgbnoarr[y], TAG1);
                                        StaticVariabes_div.log("rgb2data" + rgb_brightness_element + "rgbnoarr" + rgbnoarr[y], TAG1);
                                        StaticVariabes_div.log("rgb3data" + rgbdata_speed_element + "rgbnoarr" + rgbnoarr[y], TAG1);

                                        String datarg[] = rgbondataelement.split(",");
                                        //transmitcolorrgb(0,"102,0,0,0",rgbnoarr[y]);
                                        transmitdata_rgb(rgbnoarr[y], datarg[0], datarg[1], datarg[2], datarg[3], datarg[4]);
                                        delaytime(250);
                                        String datargbcol[] = rgb_color_element.split(",");
                                        transmitdata_rgb(rgbnoarr[y], datargbcol[0], datargbcol[1], datargbcol[2], datargbcol[3], datargbcol[4]);
                                        // transmitcolorrgb(0,rgbdatafirstelement,rgbnoarr[y]);
                                        delaytime(250);
                                        String datargbri[] = rgb_brightness_element.split(",");
                                        transmitdata_rgb(rgbnoarr[y], datargbri[0], datargbri[1], datargbri[2], datargbri[3], datargbri[4]);
                                        // transmitcolorrgb(0,rgbdatafirstelement,rgbnoarr[y]);
                                        delaytime(250);
                                        String datargbspeed[] = rgbdata_speed_element.split(",");
                                        transmitdata_rgb(rgbnoarr[y], datargbspeed[0], datargbspeed[1], datargbspeed[2], datargbspeed[3], datargbspeed[4]);


                                    } else {
                                        String rgboffdataelement = "103,0,0,0,A";
                                        StaticVariabes_div.log("rgbdataoff" + rgboffdataelement + "rgbnoarr" + rgbnoarr[y], TAG1);
                                        // transmitcolorrgb(0,"103,0,0,0,0",rgbnoarr[y]);
                                        String datarg[] = rgboffdataelement.split(",");
                                        transmitdata_rgb(rgbnoarr[y], datarg[0], datarg[1], datarg[2], datarg[3], datarg[4]);
                                    }

                                delaytime(100);

                            }
                        }

                        //DIMMER//
                        //###############################################################################//
                        int dmrcount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Dmr");
                        StaticVariabes_div.log("dmr count" + dmrcount, TAG1);

                        dimmrnoarr = new String[dmrcount];
                        dimmrnoarr = pre_adap.getall_housenoroomnodevicetype(dmrcount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Dmr");

                        for (int y = 0; y < dimmrnoarr.length; y++) {
                            StaticVariabes_div.log("dimnoarr" + dimmrnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Dmr", dimmrnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String dmronoff = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String dmrdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                int intdmrdata = Integer.parseInt(dmrdata);
                                StaticVariabes_div.log("intdmrdata" + intdmrdata + "dmronoff" + dmronoff, TAG1);

                                if (dmronoff.equals("1")) {
                                    StaticVariabes_div.log("intdmrdata" + intdmrdata + "dimnoarr" + dimmrnoarr[y], TAG1);
                                    transmitdata_dmr_mood(dimmrnoarr[y], 102, "A");
                                    delaytime(250);
                                    transmitdata_dmr_mood(dimmrnoarr[y], intdmrdata, "B");

                                } else {
                                    transmitdata_dmr_mood(dimmrnoarr[y], 103, "A");
                                    StaticVariabes_div.log("intdmrdata" + intdmrdata + "dimnoarr" + dimmrnoarr[y], TAG1);
                                }
                                delaytime(100);

                            }
                        }

                        //PIR//
                        //###############################################################################//
                        int pircount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Pir");
                        StaticVariabes_div.log("pir count" + pircount, TAG1);

                        pirnoarr = new String[pircount];
                        pirnoarr = pre_adap.getall_housenoroomnodevicetype(pircount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Pir");

                        for (int y = 0; y < pirnoarr.length; y++) {
                            StaticVariabes_div.log("pirnoarr" + pirnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Pir", pirnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String onlypirdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String onlylightdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                int intpirdata = Integer.parseInt(onlypirdata);
                                StaticVariabes_div.log("intpirdata" + intpirdata + "pironoff" + onlypirdata, TAG1);

                                if (onlypirdata.equals("909")) {
                                    StaticVariabes_div.log("pirdata" + onlypirdata + "pirnoarr" + pirnoarr[y], TAG1);
                                    transmitdata_pirmood(pirnoarr[y], "909", "A");

                                } else {
                                    transmitdata_pirmood(pirnoarr[y], "910", "A");
                                    StaticVariabes_div.log("pirdata" + onlypirdata + "pirnoarr" + pirnoarr[y], TAG1);
                                }

                                delaytime(100);


                            }
                        }
//#########################################################################################
                        //Curtain//
                        int curtcount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Cur");
                        StaticVariabes_div.log("curt count" + curtcount, TAG1);

                        curnoarr = new String[curtcount];
                        curnoarr = pre_adap.getall_housenoroomnodevicetype(curtcount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Cur");

                        for (int y = 0; y < curnoarr.length; y++) {
                            StaticVariabes_div.log("curtnoarr" + curnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Cur", curnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String curtdata2 = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String curtdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String curt_typ = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
                                String curt_name = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_ec));

                                StaticVariabes_div.log("curtdata" + curtdata + "curtdata2" + curtdata2, TAG1);
                                StaticVariabes_div.log("curtdata" + curtdata + "curtnoarr" + curnoarr[y], TAG1);
                                transmitcurt(curnoarr[y], curtdata);
                                delaytime(350);
                                if (curt_typ.equals("CLNR")) {
                                    String devnam = curt_name + "SH";

                                    mas_adap.open();
                                    String shrdevno = mas_adap.getdevno_frmdevname(devnam);
                                    mas_adap.close();
                                    if (curtdata2 != null) {
                                        transmitcurt(shrdevno, curtdata2);
                                    }
                                }
                                delaytime(200);

                            }
                        }




                        //#########################################################################################

                        //Somphy shutter screen//
                        int soshccount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SOSH");
                        StaticVariabes_div.log("psc count" + soshccount, TAG1);

                        sompnoarr = new String[soshccount];
                        sompnoarr = pre_adap.getall_housenoroomnodevicetype(soshccount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SOSH");

                        for (int y = 0; y < sompnoarr.length; y++) {
                            StaticVariabes_div.log("sompnoarr" + sompnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SOSH", sompnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String soshdata2 = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String soshdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String sosh_typ = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
                                String sosh_name = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_ec));

                                StaticVariabes_div.log("soshdata" + soshdata + "soshdata2" + soshdata2, TAG1);
                                StaticVariabes_div.log("soshdata" + soshdata + "sompnoarr" + sompnoarr[y], TAG1);
                                transmitcurt(sompnoarr[y], soshdata);
                                delaytime(350);

                            }
                        }



                        //#########################################################################################

                        //Slide gate//
                        int slideccount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SLG1");
                        StaticVariabes_div.log("psc count" + slideccount, TAG1);

                        slidenoarr = new String[slideccount];
                        slidenoarr = pre_adap.getall_housenoroomnodevicetype(slideccount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SLG1");

                        for (int y = 0; y < slidenoarr.length; y++) {
                            StaticVariabes_div.log("slidenoarr" + slidenoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SLG1", slidenoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String slidedata2 = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String slidedata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String slide_typ = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
                                String slide_name = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_ec));

                                StaticVariabes_div.log("slidedata" + slidedata + "slidedata2" + slidedata2, TAG1);
                                StaticVariabes_div.log("slidedata" + slidedata + "slidenoarr" + slidenoarr[y], TAG1);
                                transmitcurt(slidenoarr[y], slidedata);
                                delaytime(350);

                            }
                        }



                        //#########################################################################################

                        //Swing gate//
                        int swingccount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SWG1");
                        StaticVariabes_div.log("psc count" + swingccount, TAG1);

                        swingnoarr = new String[swingccount];
                        swingnoarr = pre_adap.getall_housenoroomnodevicetype(swingccount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SWG1");

                        for (int y = 0; y < swingnoarr.length; y++) {
                            StaticVariabes_div.log("swingnoarr" + swingnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "SWG1", swingnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String swingdata2 = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String swingdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String swing_typ = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
                                String swing_name = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_ec));

                                StaticVariabes_div.log("swingdata" + swingdata + "swingdata2" + swingdata2, TAG1);
                                StaticVariabes_div.log("swingdata" + swingdata + "swingnoarr" + swingnoarr[y], TAG1);
                                transmitcurt(swingnoarr[y], swingdata);
                                delaytime(350);

                            }
                        }



                        //####################################################
                        //Projector screen//
                        int psccount = pre_adap.getCount_housenoroomnodevtype(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Psc");
                        StaticVariabes_div.log("psc count" + psccount, TAG1);

                        projscrnoarr = new String[psccount];
                        projscrnoarr = pre_adap.getall_housenoroomnodevicetype(psccount, StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Psc");

                        for (int y = 0; y < projscrnoarr.length; y++) {
                            StaticVariabes_div.log("projscrnoarr" + projscrnoarr[y], TAG1);
                            Cursor cr = pre_adap.fetch_devicerow(StaticVariabes_div.housenumber, StaticVariabes_div.room_n, MoodNumber_Sel, "Psc", projscrnoarr[y], StaticVariabes_div.loggeduser);
                            if (cr != null) {
                                String pscdata2 = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_wd));
                                String pscdata = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_DD));
                                String psc_typ = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_Dna));
                                String psc_name = cr.getString(cr.getColumnIndexOrThrow(PredefsettngAdapter.KEY_ec));

                                StaticVariabes_div.log("pscdata" + pscdata + "pscdata2" + pscdata2, TAG1);
                                StaticVariabes_div.log("pscdata" + pscdata + "projscrnoarr" + projscrnoarr[y], TAG1);
                                transmitcurt(projscrnoarr[y], pscdata);
                                delaytime(350);

                            }
                        }
                        //#######################################################################################
                        pre_adap.close();
                        progressstop();

                    }
                };
                th.start();

            }else{

                Toast.makeText(view.getContext(), "Mood Setting Not Added", Toast.LENGTH_LONG).show();

            }
        }else{

            Toast.makeText(view.getContext(), "Gateway Not Connected", Toast.LENGTH_LONG).show();

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


    public void timerresponse(String respnse) {
        if (respnse != null) {
            if (respnse.contains("*IACK#")) {


                mn++;
                StaticVariabes_div.log("TimerData IACK mn= "+mn,TAG1);

                        if(ar_on_data.size()==mn) {


                            popupinfo("Timer Set Successfully");

                            if(dialog!=null) {
                                if(dialog.isShowing())
                                    dialog.dismiss();
                            }
                            ar_on_data = null;
                            ar_off_data = null;
                            ar_num_switch = null;
                            mn=0;
                        }

            } else if (respnse.equals("*EACK#")) {

                popupinfo("Timer Already Exists,Please Add different Timing");

            } else if (respnse.equals("*INACK#")){

                popupinfo("Error Setting Timer");

            }
        }
    }


    public void Send_Timer_dat_aes(String StrTimer,String starttokn,String endtokn){

        String tosend=null;
        tosend="["+StrTimer+endtokn;

        tosend.replaceAll(" ","");
        String temp_str=tosend.replaceAll("\n","");

        StaticVariabes_div.log(temp_str.length()+"TimerData"+temp_str,TAG1);
        Tcp_con.WriteString(temp_str);
    }



    //////////////////////////////////////////////////////////////////////////////////

//End of class
}