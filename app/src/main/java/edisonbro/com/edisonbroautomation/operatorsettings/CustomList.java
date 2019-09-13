package edisonbro.com.edisonbroautomation.operatorsettings;



import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import edisonbro.com.edisonbroautomation.R;

/**
 * Created by Divya on 8/22/2017.
 */

public class CustomList extends ArrayAdapter<String> {

    private final Activity context;
    private final String[] web;
    private final Integer[] imageId;
    public CustomList(Activity context,
                      String[] web, Integer[] imageId) {
        super(context, R.layout.list_single, web);
        this.context = context;
        this.web = web;
        this.imageId = imageId;

    }

   // final int[] save = {-1};

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView= inflater.inflate(R.layout.list_single, null, true);
        TextView txtTitle = (TextView) rowView.findViewById(R.id.txt);
      //  Button btradio=(Button) rowView.findViewById(R.id.button2);
        ImageView imageView = (ImageView) rowView.findViewById(R.id.img);
        txtTitle.setText(web[position]);


        imageView.setImageResource(imageId[position]);

        /*btradio.setBackgroundResource(R.drawable.radio01);

        if (save[0] != -1 && save[0] != position){
            btradio.setBackgroundResource(R.drawable.radio);
        }

        save[0] = position;*/



        return rowView;
    }
}
