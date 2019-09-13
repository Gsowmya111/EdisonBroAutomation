package edisonbro.com.edisonbroautomation.databasewired;

/**
 *  FILENAME: SwbAdapter.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Database adapter class for accessing Timer table[contains details of Timer set for devices ] .
 *
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import edisonbro.com.edisonbroautomation.Connections.Http_Connect;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;

/**
 * Created by Divya on 3/31/2018.
 */

public class TimerDBAdapter {



    Context mcontext;
    private SQLiteDatabase dbs;

    public static final String KEY_ROWID = "_id";
    public static final String KEY_t1 = "t1";
    public static final String KEY_t2 = "t2";
    public static final String KEY_t3 = "t3";
    public static final String KEY_t4 = "t4";
    public static final String KEY_a = "a";
    public static final String KEY_b = "b";
    public static final String KEY_bo = "bo";
    public static final String KEY_ra = "ra";
    public static final String KEY_d1 = "d1";
    public static final String KEY_d2 = "d2";
    public static final String KEY_d3 = "d3";
    public static final String KEY_d4 = "d4";
    public static final String KEY_d5 = "d5";
    public static final String KEY_d6 = "d6";
    public static final String KEY_d7 = "d7";
    public static final String KEY_wd = "wd";
    public static final String KEY_dn = "dn";
    public static final String KEY_ds = "ds";
    public static final String KEY_dso = "dso";
    public static final String KEY_c = "c";
    public static final String KEY_d = "d";
    public static final String KEY_f = "f";
    public static final String KEY_swno = "swno";
    public static final String KEY_ba = "ba";
    public static final String KEY_bd = "bd";
    public static final String KEY_bdo ="bdo";
    public static final String KEY_ea = "ea";
    public static final String KEY_eb = "eb";
    public static final String KEY_ec = "ec";
    public static final String KEY_ed = "ed";
    public static final String KEY_ee = "ee";
    public static final String KEY_ef = "ef";
    public static final String KEY_eg = "eg";
    public static final String KEY_eh = "eh";
    public static final String KEY_ei = "ei";
    public static final String KEY_ej = "ej";

    public static String ORIGINAL_PATH =null;
    public static String OriginalDataBase = StaticVariabes_div.HOUSE_NAME+".db";
    private static final String DATABASE_TABLE = "TimerTable";
    private static final String TAG1="TimerDbAdapter  - ";



    private static final String DATABASE_CYCLIC_TABLE = "CyclicTable";

    public static final String KEY_ROWID_cyc = "_id";
    public static final String KEY_st = "st";
    public static final String KEY_et= "et";
    public static final String KEY_oni = "oni";
    public static final String KEY_ofi = "ofi";
    public static final String KEY_opt = "opt";
    public static final String KEY_opd = "opd";
    public static final String KEY_opf = "opf";
    public static final String KEY_ops = "ops";
    public static final String KEY_opc = "opc";
    public static final String KEY_dn_cyc = "dn";
    public static final String KEY_ds1 = "ds1";
    public static final String KEY_ds2 = "ds2";
    public static final String KEY_c_cyc = "c";
    public static final String KEY_d_cyc = "d";
    public static final String KEY_f_cyc = "f";
    public static final String KEY_swno_cyc = "swno";
    public static final String KEY_bd_cyc = "bd";
    public static final String KEY_bdo_cyc = "bdo";
    public static final String KEY_ea_cyc = "ea";
    public static final String KEY_eb_cyc = "eb";
    public static final String KEY_ec_cyc = "ec";
    public static final String KEY_ed_cyc = "ed";
    public static final String KEY_ee_cyc = "ee";
    public static final String KEY_ef_cyc = "ef";
    public static final String KEY_eg_cyc = "eg";
    public static final String KEY_eh_cyc = "eh";
    public static final String KEY_ei_cyc = "ei";
    public static final String KEY_ej_cyc = "ej";


    public static	ArrayList<String> Num,DeviceName
            ,DeviceType, OperatedType, SwitchNumber
            , Days, Date, Time,FromTimeRep_date, ToTimeRep_date
            , ontime_cyclic, offtime_cyclic, RepeatAlways_rep_date
            ,OnDataRep_date, OffDataRep_date
            , OnCyclicData, OffCyclicData ,DeviceNumber ,FromTimeCyclic,ToTimeCyclic,
            FromTime_Todisplay,ToTime_Todisplay,Data_Todisplay,Off_Data_Todisplay,Devicetype_number,User_nam;


    public TimerDBAdapter(Context context) {
        mcontext = context;
        ORIGINAL_PATH="/data/data/"+mcontext.getPackageName()+"/databases/";
    }

    public void open() {
        try {
            String path = ORIGINAL_PATH + OriginalDataBase;
            StaticVariabes_div.log("Databse Path is " + path, TAG1);
            dbs = SQLiteDatabase.openDatabase(path, null,
                    SQLiteDatabase.OPEN_READWRITE
                            + SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void close() {
        try {
            if (dbs != null) {
                dbs.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //**************************************************************************************************88

    public int getCounttimerlist() {
        final int DEFAULT_BASE = 0;
        Cursor mCursor = dbs.rawQuery(
                "SELECT Count(*) FROM TimerTable", null);
        if (mCursor.getCount() == 0)
            return DEFAULT_BASE;
        else
            mCursor.moveToFirst();
        return (int) mCursor.getLong(0);
    }


    public void getall_timerdata(String roomn) {

        roomn="'"+roomn+"'";

        String D1,D2,D3,D4,D5,D6,D7;
        Num=new ArrayList();
        DeviceName = new ArrayList();
        User_nam=new ArrayList();
        DeviceType = new ArrayList();
        OperatedType = new ArrayList();
        SwitchNumber = new ArrayList();

        Days = new ArrayList();
        Date = new ArrayList();
        Time = new ArrayList();

        FromTimeRep_date = new ArrayList();
        ToTimeRep_date = new ArrayList();

        FromTimeCyclic= new ArrayList();
        ToTimeCyclic = new ArrayList();
        ontime_cyclic = new ArrayList();
        offtime_cyclic = new ArrayList();
        RepeatAlways_rep_date = new ArrayList();

        OnDataRep_date = new ArrayList();
        OffDataRep_date = new ArrayList();

        OnCyclicData = new ArrayList();
        OffCyclicData = new ArrayList();

        DeviceNumber=new ArrayList();

        FromTime_Todisplay=new ArrayList();
        ToTime_Todisplay=new ArrayList();

        Data_Todisplay=new ArrayList();
        Off_Data_Todisplay=new ArrayList();
        Devicetype_number=new ArrayList();
        Cursor mCursor = dbs.rawQuery(
                "SELECT _id,t1,t2,t3,t4,a,b,bo,ra,d1,d2,d3,d4,d5,d6,d7," +
                        "wd,dn,ds,dso,c,d,f,swno,ba,bd,bdo,ea,eb,ec" + " FROM TimerTable WHERE ea="+roomn, null);
       // String[] array = new String[count];

        StaticVariabes_div.log("roomn"+roomn,TAG1);


        int i = 0;
        while(mCursor.moveToNext()){


            Num.add(""+(i+1));
            User_nam.add(mCursor.getString(mCursor.getColumnIndex("ec")));
            DeviceName.add(mCursor.getString(mCursor.getColumnIndex("eb")));
            DeviceType.add(mCursor.getString(mCursor.getColumnIndex("dn")));
           // OperatedType.add(mCursor.getString(mCursor.getColumnIndex("t3")));
            String operty=mCursor.getString(mCursor.getColumnIndex("t3"));

            if(operty.equals("1")){
            OperatedType.add("Repeat On Days");
            }else{
                OperatedType.add("Repeat On Date");
            }

            SwitchNumber.add(mCursor.getString(mCursor.getColumnIndex("swno")));
            Devicetype_number.add(mCursor.getString(mCursor.getColumnIndex("c")));
                 D1=mCursor.getString(mCursor.getColumnIndex("d1"));
                 D2=mCursor.getString(mCursor.getColumnIndex("d2"));
                D3=mCursor.getString(mCursor.getColumnIndex("d3"));
                D4=mCursor.getString(mCursor.getColumnIndex("d4"));
                D5=mCursor.getString(mCursor.getColumnIndex("d5"));
                D6=mCursor.getString(mCursor.getColumnIndex("d6"));
                D7=mCursor.getString(mCursor.getColumnIndex("d7"));

           // Days.add(D1+","+D2+","+D3+","+D4+","+D5+","+D6+","+D7);
            Days.add(D1+","+D2+","+D3+","+D4+","+D5+","+D6+","+D7);

            StaticVariabes_div.log("days--"+D1+","+D2+","+D3+","+D4+","+D5+","+D6+","+D7, TAG1);
            Date.add(mCursor.getString(mCursor.getColumnIndex("a")));
            Time.add(mCursor.getString(mCursor.getColumnIndex("b")));

            FromTimeRep_date.add(mCursor.getString(mCursor.getColumnIndex("b")));
            ToTimeRep_date.add(mCursor.getString(mCursor.getColumnIndex("bo")));

            FromTimeCyclic.add("00:00");
            ToTimeCyclic.add("00:00");

            ontime_cyclic.add("00:00");
            offtime_cyclic.add("00:00");

            RepeatAlways_rep_date.add(mCursor.getString(mCursor.getColumnIndex("wd")));

            //String OperationData = (jsonObject.optString("OperationData").toString());

            OnDataRep_date.add(mCursor.getString(mCursor.getColumnIndex("ds")));
            OffDataRep_date.add(mCursor.getString(mCursor.getColumnIndex("dso")));

             OnCyclicData.add("00");
             OffCyclicData.add("00");



            DeviceNumber.add(mCursor.getString(mCursor.getColumnIndex("d")));



            FromTime_Todisplay.add(mCursor.getString(mCursor.getColumnIndex("b")));
            ToTime_Todisplay.add(mCursor.getString(mCursor.getColumnIndex("bo")));

            Data_Todisplay.add(mCursor.getString(mCursor.getColumnIndex("ds")));
            Off_Data_Todisplay.add(mCursor.getString(mCursor.getColumnIndex("dso")));


            i++;
        }



        Cursor mCursor_cyc = dbs.rawQuery(
                "SELECT _id,st,et,oni,ofi" +
                        ",dn,ds1,ds2,c,d,f,swno,ea,eb,ec" + " FROM CyclicTable WHERE ea="+roomn, null);
        int j= i;
        while(mCursor_cyc.moveToNext()){


            Num.add(""+(j+1));
            User_nam.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ec")));
            DeviceName.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("eb")));
            DeviceType.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("dn")));

                OperatedType.add("Cyclic");

            SwitchNumber.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("swno")));
            Devicetype_number.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("c")));

              FromTimeCyclic.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("st")));
              ToTimeCyclic.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("et")));

              ontime_cyclic.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("oni")));
              offtime_cyclic.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ofi")));


             OnCyclicData.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ds1")));
             OffCyclicData.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ds2")));

            DeviceNumber.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("d")));


            FromTime_Todisplay.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("st")));
            ToTime_Todisplay.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("et")));

            Data_Todisplay.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ds1")));
            Off_Data_Todisplay.add(mCursor_cyc.getString(mCursor_cyc.getColumnIndex("ds2")));



            RepeatAlways_rep_date.add("0");
            Days.add(0+","+0+","+0+","+0+","+0+","+0+","+0);
            Date.add("00-00-0000");
            Time.add("00:00");

            FromTimeRep_date.add("00:00");
            ToTimeRep_date.add("00:00");


            j++;
        }




        Http_Connect.Num=Num;
        Http_Connect.DeviceName=DeviceName;
        Http_Connect.User_nam=User_nam;
        Http_Connect.SwitchNumber=SwitchNumber;
        Http_Connect.ToTimeRep_date=ToTimeRep_date;
        Http_Connect.FromTimeRep_date=FromTimeRep_date;
        Http_Connect.DeviceNumber=DeviceNumber;
        Http_Connect.DeviceType=DeviceType;
        Http_Connect.OperatedType = OperatedType;
        Http_Connect.Days=Days;
        Http_Connect.RepeatAlways_rep_date=RepeatAlways_rep_date;
        Http_Connect.Time=Time;
        Http_Connect.Date=Date;
        Http_Connect.OnDataRep_date=OnDataRep_date;
        Http_Connect.OffDataRep_date=OffDataRep_date;
        Http_Connect.Devicetype_number=Devicetype_number;

        Http_Connect.ontime_cyclic=ontime_cyclic;
        Http_Connect.offtime_cyclic=offtime_cyclic;
        Http_Connect.OnCyclicData=OnCyclicData;
        Http_Connect.OffCyclicData=OffCyclicData;


/*        if(oper.equals("Cyclic")){
            FromTime_Todisplay.add(jsonObject.optString("FromTimecyclic").toString());
            ToTime_Todisplay.add(jsonObject.optString("ToTimecyclic").toString());

            // if(data_ty.contains("RGB")) {
            Data_Todisplay.add(jsonObject.optString("OnCyclicData").toString());
            Off_Data_Todisplay.add(jsonObject.optString("OffCyclicData").toString());
            // }
        }else if(oper.equals("Repeat On Days")){
            FromTime_Todisplay.add(jsonObject.optString("FromTimeRep").toString());
            ToTime_Todisplay.add(jsonObject.optString("ToTimeRep").toString());

            // if(data_ty.contains("RGB")) {
            Data_Todisplay.add(jsonObject.optString("OnDataRep").toString());
            Off_Data_Todisplay.add(jsonObject.optString("OffDataRep").toString());
            // }
        }*/

        Http_Connect.Data_Todisplay=Data_Todisplay;
        Http_Connect.Off_Data_Todisplay=Off_Data_Todisplay;
        Http_Connect.FromTime_Todisplay=FromTime_Todisplay;
        Http_Connect.ToTime_Todisplay=ToTime_Todisplay;

        // return array;
    }




    public String getroomno(String roomname) {
        final int DEFAULT_BASE = 0; String uname="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT a FROM MasterTable WHERE b='"+roomname+"'", null);
        if (mCursor.getCount() == 0){

        }
        else{
            mCursor.moveToFirst();
            uname = mCursor.getString(mCursor.getColumnIndex("a"));
        }

        if(mCursor!=null)mCursor.close();
        return uname;
    }


    public String getroomname(String roomno) {
        final int DEFAULT_BASE = 0; String roomname="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT b FROM MasterTable WHERE a='"+roomno+"'", null);
        if (mCursor.getCount() == 0){
        }
        else{
            mCursor.moveToFirst();
            roomname = mCursor.getString(mCursor.getColumnIndex("b"));
        }
        return roomname;
    }


    public String getroomno_devno(String devno) {
        final int DEFAULT_BASE = 0; String roomno="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT a FROM MasterTable WHERE d='"+devno+"'", null);
        if (mCursor.getCount() == 0){
        }
        else{
            mCursor.moveToFirst();
            roomno = mCursor.getString(mCursor.getColumnIndex("a"));
        }
        return roomno;
    }

    public String getdevid_frmdevno(String devno) {
        final int DEFAULT_BASE = 0; String roomno="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT f FROM MasterTable WHERE d='"+devno+"'", null);
        if (mCursor.getCount() == 0){
        }
        else{
            mCursor.moveToFirst();
            roomno = mCursor.getString(mCursor.getColumnIndex("f"));
        }
        return roomno;
    }


    public String getdevtype_devno(String devno) {
        final int DEFAULT_BASE = 0; String roomno="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT da FROM MasterTable WHERE d='"+devno+"'", null);
        if (mCursor.getCount() == 0){
        }
        else{
            mCursor.moveToFirst();
            roomno = mCursor.getString(mCursor.getColumnIndex("da"));
        }
        return roomno;
    }



    public String getgrouptype_devno(String devno) {
        final int DEFAULT_BASE = 0; String roomno="";
        Cursor mCursor = dbs.rawQuery(
                "SELECT ea FROM MasterTable WHERE d='"+devno+"'", null);
        if (mCursor.getCount() == 0){
        }
        else{
            mCursor.moveToFirst();
            roomno = mCursor.getString(mCursor.getColumnIndex("ea"));
        }
        return roomno;
    }




}

