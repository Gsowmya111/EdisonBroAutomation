package edisonbro.com.edisonbroautomation;


import android.content.Context;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import java.util.ArrayList;

public class ImageAdapter extends BaseAdapter {
    private Context context;
    static Integer[] images;
    public Integer[] images1;
    private ImageView selectedView = null;
    static Integer[] pressedimages;
    Integer []prev_pressed_image = new Integer[1];
public  static  ImageView selectedview_zeropos=null;

   //public Integer[] images1={R.mipmap.bell_grid,R.mipmap.ac_grid,R.mipmap.dimmer_grid,R.mipmap.ac_grid,R.mipmap.switchboard_grid,};

   // private ArrayList<Integer> images;

    public ImageAdapter(Context c, int index,ArrayList<Integer> listFlag)
    {
        context=c;
       images1 = listFlag.toArray(new Integer[listFlag.size()]);
        pressedimages=CombFrag.listiconpressed.toArray(new Integer[listFlag.size()]);

        if(index==0){
            this.images=images1;
        }  /* else if(index==1){
            this.images=images2;
        }
        else if(index==2){
            this.images=images3;
        }
        else if(index==3){
            this.images=images4;
        }
        else if(index==4){
            this.images=images5;
        }*/

    }


/*
    public ImageAdapter(Context c, int index)
    {
        context=c;

      if(index==0){
            this.images=images1;
        }  */
/* else if(index==1){
            this.images=images2;
        }
        else if(index==2){
            this.images=images3;
        }
        else if(index==3){
            this.images=images4;
        }
        else if(index==4){
            this.images=images5;
        }*//*


    }
*/

    @Override
    public int getCount() {

        return images.length;
    }

    @Override
    public Object getItem(int position) {
        return images[position];
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {




       // ImageView imageView = new ImageView(context);
      //  imageView.setImageResource(images[position]);
       // imageView.setScaleType(ImageView.ScaleType.FIT_XY);
      //  ViewGroup.LayoutParams lp=new ViewGroup.LayoutParams(150,80);
        //gridView.setLayoutParams(lp);
       // imageView.setLayoutParams(lp);
      //  convertView.setItemChecked(0,true);

       /* imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setLayoutParams(new GridView.LayoutParams(240,240));*/
       // return imageView;
///////////////////////test//////////////////////////////////////////////////////////


        ///////////////// previous working//////////////////


        View viw = null;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // ImageView imageView = new ImageView(context);
        if (convertView == null) {
           // viw = new View(context);
            viw = inflater.inflate(R.layout.single_grid_item, null);
            final ImageView imageView = (ImageView) viw.findViewById(R.id.grid_image);

            //imageView.setImageResource(images[position]);



            /*imageView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {

                    if(prev_pressed_image[0]!=null)
                    {
                        selectedView.setImageResource(prev_pressed_image[0]);

                    }else{

                        selectedview_zeropos.setImageResource(images[0]);
                    }


                    imageView.setImageResource(pressedimages[position]);
                    prev_pressed_image[0]=images[position];
                    selectedView=imageView;

                    return false;
                }
            });*/

            imageView.setImageResource(images[position]);

            if(position==0){
                imageView.setImageResource(pressedimages[0]);

                //imageView.setSelected(true);
                selectedview_zeropos=imageView;
                // prev_pressed_image[0]=images[0];
               //  selectedView=imageView;
            }
        }
         else {
            viw = (View) convertView;
        }

/////////////////////////////////////////////////////////////////////////////////////////

        return  viw;
        }



   // public void creationofpressedstateimage
}
