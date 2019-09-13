package edisonbro.com.edisonbroautomation.loganalytics;

/**
 *  FILENAME: LogAnalyticsActivity.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:  Activity to display logs based on option selected like logs of operation based on room selected
 *  or whole house etc
 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import edisonbro.com.edisonbroautomation.Main_Navigation_Activity;
import edisonbro.com.edisonbroautomation.R;
import edisonbro.com.edisonbroautomation.StaticClasses.StaticVariabes_div;
import edisonbro.com.edisonbroautomation.SupportActivity;
import edisonbro.com.edisonbroautomation.operatorsettings.SmartSettings;
import lecho.lib.hellocharts.listener.BubbleChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.BubbleChartData;
import lecho.lib.hellocharts.model.BubbleValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.BubbleChartView;
import lecho.lib.hellocharts.view.Chart;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;


public class LogAnalyticsActivity extends AppCompatActivity {


    private static final int BUBBLES_NUM = 3;
    private String a;

    private BubbleChartView chart;
    private BubbleChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    private ValueShape shape = ValueShape.CIRCLE;
    private boolean hasLabels = true;
    private boolean hasLabelForSelected = true;
    private JSONArray jsonarray;
    private JSONArray Roomes;
    private String room_name;
    private Float oper_data;
    private ArrayList<Integer> colors;
    private Integer data_cal;

    ArrayList<Float> percentage_cal=new ArrayList<>();
    private float sum;
    private Button day,week,month,year,quater1,quater2,quater3,quater4;
    private String type;
    //  private int ij[]={200,120,300,300,600,120,300,800,2000,2000,2000,1000};
    private float percent;
    private String label;
    String[] arraySpinner = new String[] {"Home Analytics","Room Analytics", "Single Room Analytics", "Room Device Analytics", "Home Device Analytics"};
    String[] home_label = new String[] {"App", "Touch","Remote", "PIR", "Timer"};
    String[] home_key = new String[] {"app", "touch","remote", "pir", "timer"};
    String[] home_label1 = new String[] {"SwitchBoard", "Curtain","RGB", "Dimmer", "AC"};
    String[] device_key = new String[] {"SwitchBoard", "Curtain","RGB", "Dimmer", "AC"};
    String[] horizontalList = new String[] {"Day", "Week","Month", "Quater1", "Quater2","Quater3","Quater4","Year"};
    String[] btn_type_arr = new String[] {"day", "week","month", "q1", "q2","q3","q4","year"};
    String[] btn_name_arr = new String[] {"Day", "Week","Month", "Quater1", "Quater2","Quater3","Quater4","Year"};
    String[] json_obj_name_arr = new String[] {"Home", "Rooms","Rooms", "Rooms", "TDevice"};
    String[] room_dev_arr = new String[] {"","Rooms", "Single room", "Single Room Device data",""};

    private RecyclerView horizontal_recycler_view;
    private HorizontalAdapter horizontalAdapter;

    private String json_obj_name;
    private BubbleValue value;
    private Spinner spinner1;
    private String room_dev;
    ArrayList<String> spinner2_arr = new ArrayList<>();
    private String position_name;
    private ArrayList<Float> percentage_cal2=new ArrayList<>();
    private LinearLayout linear_lay,main_linear_lay;
    private AlphaAnimation buttonClick = new AlphaAnimation(1F, 2.8F);
    private ArrayList<Object> array=new ArrayList<>();
    private String btn_name;
    private Button btn_name2,btn_back;
    private int pos;
    private LinearLayout row_linearlay;
    private String[]  home_dev_arr = new String[] {"Home", "TDevice"};
    private TransparentProgressDialog pd;
    private Handler h;
    private Runnable r;
    private static final String TAG1="LogAnalytics - ";
    private int selectedPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().addFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_analytics);

        chart = (BubbleChartView) findViewById(R.id.chart);

        horizontal_recycler_view= (RecyclerView) findViewById(R.id.horizontal_recycler_view);
        row_linearlay=(LinearLayout)findViewById(R.id.row_linearlay);
        horizontalAdapter=new HorizontalAdapter(horizontalList);
        LinearLayoutManager horizontalLayoutManagaer
                = new LinearLayoutManager(LogAnalyticsActivity.this, LinearLayoutManager.HORIZONTAL, false);
        horizontal_recycler_view.setLayoutManager(horizontalLayoutManagaer);

        //   vertical_recycler_view.setAdapter(verticalAdapter);
        horizontal_recycler_view.setAdapter(horizontalAdapter);


        linear_lay=(LinearLayout)findViewById(R.id.linear_lay);
        // simpleProgressBar = (ProgressBar) findViewById(R.id.simpleProgressBar);
        main_linear_lay=(LinearLayout)findViewById(R.id.main_linear_lay);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner1 = (Spinner) findViewById(R.id.spinner2);

        btn_back= (Button) findViewById(R.id.btnback);

        btn_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intnt=new Intent(LogAnalyticsActivity.this, Main_Navigation_Activity.class);
                startActivity(intnt);
                finish();
            }
        });



        spinner.setAdapter(new MyAdapter(LogAnalyticsActivity.this, R.layout.bubble_spinner_item, arraySpinner));
        h = new Handler();
        //pd = new TransparentProgressDialog(this, R.drawable.aa);
        pd = new TransparentProgressDialog(this, R.drawable.loading_pgbar);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                json_obj_name=json_obj_name_arr[position];
                room_dev=room_dev_arr[position];
                type="day";
                selectedPosition=0;
                horizontal_recycler_view.setAdapter(null);
                horizontal_recycler_view.setAdapter(horizontalAdapter);
                if(position==0|position==1|position==4) {
                    spinner1.setVisibility(View.INVISIBLE);
                    chart.setBubbleChartData(null);
                } else if(position==2|position==3) {
                    spinner1.setVisibility(View.VISIBLE);
                   // linear_lay.setVisibility(View.INVISIBLE);
                    chart.setBubbleChartData(null);
                }
                progress_bar_connect();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner1.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //position_name =spinner1.getSelectedItem().toString();
                position_name= spinner1.getItemAtPosition(position).toString();
                chart.setBubbleChartData(null);
                type="day";
                selectedPosition=0;
                horizontal_recycler_view.setAdapter(null);
                horizontal_recycler_view.setAdapter(horizontalAdapter);

                progress_bar_connect();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    public void progress_bar_connect()
    {
        Thread t = new Thread() {
            public void run() {
                try {
                    post();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
        pd.show();


    }

    private void tag() {
       /* for (int i = 0; i < array.size(); i++) {
            if (i==array.size()-1) {
                btn_name.setBackgroundResource(R.drawable.button3);

                chart.setBubbleChartData(null);
                linear_lay.setVisibility(View.VISIBLE);
                progress_bar();
                Thread t = new Thread() {
                    public void run() {
                        try {
                            post();
                        } catch (JSONException | IOException e) {
                            e.printStackTrace();
                        }
                    }
                };
                t.start();
            } else {
                btn_name2.setBackgroundResource(R.drawable.button2);
            }
*/


        chart.setBubbleChartData(null);
        linear_lay.setVisibility(View.VISIBLE);
        progress_bar();
        Thread t = new Thread() {
            public void run() {
                try {
                    post();
                } catch (JSONException | IOException e) {
                    e.printStackTrace();
                }
            }
        };
        t.start();
    }




    private void generateData() {

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis().setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("Room numbers");
                axisY.setName("Values");
                axisX.setTextSize(20);
                axisY.setTextSize(20);
                axisX.setTextColor(R.color.holo_blue_dark);
                axisY.setTextColor(R.color.holo_blue_dark);
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        chart.setBubbleChartData(data);

    }

    /**
     * To animate values you have to change targets values and then call {@link Chart#startDataAnimation()}
     * method(don't confuse with View.animate()).
     */


    private class ValueTouchListener implements BubbleChartOnValueSelectListener {

        @Override
        public void onValueSelected(int bubbleIndex, BubbleValue value) {
            //  Toast.makeText(getActivity(), "Selected: " + value, Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }
    }











    public void post() throws JSONException, IOException {

        // Creating HTTP client
        HttpClient httpClient = new DefaultHttpClient();
        // Creating HTTP Post
        HttpPost httpPost = new HttpPost("http://edisonbro.in/createfolder/logs2.php");
        List<NameValuePair> nameValuePair = new ArrayList<>(2);
        StaticVariabes_div.log("House_reference_num"+StaticVariabes_div.House_reference_num,TAG1);
        nameValuePair.add(new BasicNameValuePair("cid", StaticVariabes_div.House_reference_num));
        nameValuePair.add(new BasicNameValuePair("type",type));
        // Url Encoding the POST parameters
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair));
        } catch (UnsupportedEncodingException e) {

            e.printStackTrace();
        }
        try {
            final HttpResponse response = httpClient.execute(httpPost);
            String responseBody = null;
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                responseBody = EntityUtils.toString(entity);
                JSONObject jsonObj = new JSONObject(responseBody);
                Roomes = jsonObj.getJSONArray(json_obj_name);
                percentage_cal.clear();
                sum=0;
                List<BubbleValue> values = new ArrayList<BubbleValue>();


                for (int i = 0; i < Roomes.length(); i++) {
                    JSONObject c = Roomes.getJSONObject(i);
                    if(json_obj_name.equals("Home")) {
                        for (String aHome_key : home_key) {
                            percentage_cal.add(Float.parseFloat(c.getString(aHome_key)));
                        }
                    }
                    else if(json_obj_name.equals("Rooms")) {
                        if(room_dev.equals("Rooms")) {
                            room_name = c.getString("roomname");
                            oper_data = Float.valueOf(c.getString("oper"));
                            percentage_cal.add(oper_data);
                        }
                        else if(room_dev.equals("Single room") && spinner1.getCount()!=Roomes.length() ) {
                            room_name = c.getString("roomname");
                            spinner2_arr.add(room_name);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                   /* ArrayAdapter<String> adapter = new ArrayAdapter<String>(BubbleChart.this, android.R.layout.simple_spinner_item, spinner2_arr);
                                    adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
                                    spinner1.setAdapter(adapter);*/
                                    spinner1.setAdapter(new MyAdapter2(LogAnalyticsActivity.this, R.layout.bubble_spinner_item, spinner2_arr));


                                }
                            });
                        }
                        else if(room_dev.equals("Single Room Device data") && spinner1.getCount()!=Roomes.length() ) {
                            room_name = c.getString("roomname");
                            spinner2_arr.add(room_name);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    spinner1.setAdapter(new MyAdapter2(LogAnalyticsActivity.this, R.layout.bubble_spinner_item, spinner2_arr));
                                }
                            });
                        }
                    }
                    if(json_obj_name.equals("TDevice")) {

                        for (String aHome_key : device_key) {
                            percentage_cal.add(Float.parseFloat(c.getString(aHome_key)));
                        }
                    }
                }

                for (float j:percentage_cal) {
                    sum+=j;
                }


                for (int i = 0; i < Roomes.length(); i++) {
                    JSONObject c = Roomes.getJSONObject(i);


                    for (String aHome_dev_arr : home_dev_arr) {
                        if (json_obj_name.equals(aHome_dev_arr)) {
                            if (sum > 0) {
                                for (int j = 0; j < percentage_cal.size(); j++) {
                                    value = new BubbleValue(j, (100 * percentage_cal.get(j)) / sum, (100 * percentage_cal.get(j)) / sum);
                                    percent = ((100 * percentage_cal.get(j)) / sum);
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    String formatted = df.format(percent);
                                    if(json_obj_name.equals("Home"))
                                    {  label = home_label[j] + " \n " + formatted + "%";
                                    } else if(json_obj_name.equals("TDevice"))
                                    {  label = home_label1[j] + " \n " + formatted + "%";
                                    }
                                    value.setColor(ChartUtils.nextColor());
                                    value.setShape(shape);
                                    value.setLabel(label);
                                    values.add(value);
                                }
                            } else {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(LogAnalyticsActivity.this, "No Data", Toast.LENGTH_LONG).show();

                                    }
                                });

                            }
                        }
                    }

                    if(json_obj_name.equals("Rooms")) {
                        switch (room_dev) {
                            case "Rooms":
                                room_name = c.getString("roomname");
                                oper_data = Float.valueOf(c.getString("oper"));
                                if(sum>0) {
                                    value = new BubbleValue(i + 1, (100 * oper_data) / sum, (100 * oper_data) / sum);
                                    percent = (100 * oper_data) / sum;
                                    DecimalFormat df = new DecimalFormat("#.##");
                                    String formatted = df.format(percent);
                                    label = room_name + " \n " + formatted + "%";
                                    value.setColor(ChartUtils.nextColor());
                                    value.setShape(shape);
                                    value.setLabel(label);
                                    values.add(value);
                                }
                                else
                                {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(LogAnalyticsActivity.this,"No Data", Toast.LENGTH_LONG).show();

                                        }
                                    });

                                }
                                break;
                            case "Single room":
                                percentage_cal2.clear();
                                room_name = c.getString("roomname");
                                if (room_name.equals(position_name)) {
                                    for (String aHome_key : home_key) {
                                        percentage_cal2.add(Float.parseFloat(c.getString(aHome_key)));
                                    }

                                    for (float j : percentage_cal2) {
                                        sum += j;
                                    }

                                    if(sum>0) {
                                        for (int j = 0; j < percentage_cal2.size(); j++) {

                                            value = new BubbleValue(j, (100 * percentage_cal2.get(j)) / sum, (100 * percentage_cal2.get(j)) / sum);
                                            percent = ((100 * percentage_cal2.get(j)) / sum);
                                            DecimalFormat df1 = new DecimalFormat("#.##");
                                            String formatted1 = df1.format(percent);
                                            label = home_label[j] + " \n " + formatted1 + "%";
                                            value.setColor(ChartUtils.nextColor());
                                            value.setShape(shape);
                                            value.setLabel(label);
                                            values.add(value);
                                        }
                                    }
                                    else
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LogAnalyticsActivity.this,"No Data", Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    }

                                }

                                break;
                            case "Single Room Device data":
                                percentage_cal2.clear();
                                room_name = c.getString("roomname");
                                if (room_name.equals(position_name)) {

                                    for (String aHome_key : device_key) {
                                        percentage_cal2.add(Float.parseFloat(c.getString(aHome_key)));
                                    }
                                    for (float j : percentage_cal2) {
                                        sum += j;
                                    }

                                    if(sum>0) {
                                        for (int j = 0; j < percentage_cal2.size(); j++) {

                                            value = new BubbleValue(j, (100 * percentage_cal2.get(j)) / sum, (100 * percentage_cal2.get(j)) / sum);
                                            percent = ((100 * percentage_cal2.get(j)) / sum);
                                            DecimalFormat df2 = new DecimalFormat("#.##");
                                            String formatted2 = df2.format(percent);
                                            label = home_label1[j] + " \n " + formatted2 + "%";
                                            value.setColor(ChartUtils.nextColor());
                                            value.setShape(shape);
                                            value.setLabel(label);
                                            values.add(value);
                                        }
                                    }
                                    else
                                    {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(LogAnalyticsActivity.this,"No Data", Toast.LENGTH_LONG).show();

                                            }
                                        });

                                    }

                                }

                                break;
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(pd!=null) {
                            pd.dismiss();
                        }

                    }
                });

                data = new BubbleChartData(values);
                data.setHasLabels(hasLabels);
                data.setHasLabelsOnlyForSelected(hasLabelForSelected);
                chart.setValueSelectionEnabled(true);
            }
            Log.d("TAG", "Http post Response: " + responseBody);
            Log.d("TAG", responseBody);
            generateData();
        } catch (IOException e) {
            e.printStackTrace();

        }

    }

    public void progress_bar()
    {
        pd.show();

    }

    public class HorizontalAdapter extends RecyclerView.Adapter<HorizontalAdapter.MyViewHolder> {

        private String[] horizontalList2;

        public class MyViewHolder extends RecyclerView.ViewHolder {
            public TextView txtView;
            Button button;

            public MyViewHolder(View view) {
                super(view);
                // txtView = (TextView) view.findViewById(R.id.txtView);
                button = (Button) view.findViewById(R.id.btn);

            }
        }


        public HorizontalAdapter(String[] horizontalList) {
            this.horizontalList2 = horizontalList;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.singleitem_recy, parent, false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(final MyViewHolder holder, final int position) {

           // holder.button.setText(horizontalList[position]);
            //holder.button.setBackgroundResource(R.color.holo_blue);

            holder.button.setText(horizontalList[position]);

            if(selectedPosition==position) {
                holder.button.setBackgroundColor(Color.parseColor("#ffffff"));
                holder.button.setTextColor(Color.parseColor("#33B5E5"));
            }
            else {
                holder.button.setBackgroundResource(R.color.holo_blue);
                holder.button.setTextColor(Color.parseColor("#ffffff"));
            }

            holder.button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //v.setSelected(false);
                    //Toast.makeText(BubbleChart.this,holder.button.getText().toString(),Toast.LENGTH_SHORT).show();
/*
                    if(pos!=0) {
                        holder.button.setText(horizontalList[pos]);
                        row_linearlay.setBackgroundResource(R.color.holo_blue);
                        holder.button.setText(horizontalList[position]);

                    }*/
                    selectedPosition=position;
                    notifyDataSetChanged();

                    if(holder.button.getText().toString().equals(btn_name_arr[position])) {
                        btn_name = btn_type_arr[position];
                        type = btn_type_arr[position];
                        //  tag();
                        chart.setBubbleChartData(null);
                        linear_lay.setVisibility(View.VISIBLE);
                        progress_bar_connect();
                    }

                  /*  if(holder.button.getText().toString().equals(btn_name_arr[position])){
                        pos=position;
                        btn_name = btn_type_arr[position];
                        type=btn_type_arr[position];
                        // holder.button.setBackgroundResource(R.color.holo_white);
                        tag();
                    }*/

                }
            });



        }



        @Override
        public int getItemCount() {
            return horizontalList.length;
        }
    }



    public class MyAdapter extends ArrayAdapter {

        public MyAdapter(Context context, int textViewResourceId, String[] objects) {
            super(context, textViewResourceId, objects);

        }

        public MyAdapter(LogAnalyticsActivity context, int bubble_spinner_item, ArrayList<String> spinner2_arr) {
            super(context, bubble_spinner_item, spinner2_arr);

        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.bubble_spinner_item, parent, false);
            TextView tvLanguage = (TextView) layout.findViewById(R.id.textView);
            tvLanguage.setText(arraySpinner[position]);
            // tvLanguage.setText(spinner2_arr.get(position));

            //tvLanguage.setTextColor(Color.rgb(75, 180, 225));
            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }



    public class MyAdapter2 extends ArrayAdapter {

        public MyAdapter2(LogAnalyticsActivity context, int bubble_spinner_item, ArrayList<String> spinner2_arr) {
            super(context, bubble_spinner_item, spinner2_arr);

        }

        public View getCustomView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = getLayoutInflater();
            View layout = inflater.inflate(R.layout.bubble_spinner_item, parent, false);
            TextView tvLanguage = (TextView) layout.findViewById(R.id.textView);
            // tvLanguage.setText(arraySpinner[position]);
            tvLanguage.setText(spinner2_arr.get(position));

            //tvLanguage.setTextColor(Color.rgb(75, 180, 225));
            return layout;
        }

        // It gets a View that displays in the drop down popup the data at the specified position
        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
        // It gets a View that displays the data at the specified position
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            return getCustomView(position, convertView, parent);
        }
    }


    private class TransparentProgressDialog extends Dialog {

        private ImageView iv;

        public TransparentProgressDialog(Context context, int resourceIdOfImage) {
            super(context, R.style.TransparentProgressDialog);
            WindowManager.LayoutParams wlmp = getWindow().getAttributes();
            wlmp.gravity = Gravity.CENTER_HORIZONTAL;
            getWindow().setAttributes(wlmp);
            setTitle(null);
            setCancelable(true);
            setOnCancelListener(null);
            LinearLayout layout = new LinearLayout(context);
            layout.setOrientation(LinearLayout.VERTICAL);
            //  LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(150, 150);
            iv = new ImageView(context);
            iv.setImageResource(resourceIdOfImage);
            layout.addView(iv, params);
            addContentView(layout, params);
        }

        @Override
        public void onBackPressed() {
           pd.dismiss();
        }
        @Override
        public void show() {
            super.show();
            RotateAnimation anim = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, .5f, Animation.RELATIVE_TO_SELF, .5f);
            anim.setInterpolator(new LinearInterpolator());
            anim.setRepeatCount(Animation.INFINITE);
            anim.setDuration(3000);
            setCancelable(true);
            iv.setAnimation(anim);
            iv.startAnimation(anim);
        }
    }

    public void onBackPressed() {
        Intent intnt=new Intent(LogAnalyticsActivity.this, Main_Navigation_Activity.class);
        startActivity(intnt);
        finish();
    }

}
