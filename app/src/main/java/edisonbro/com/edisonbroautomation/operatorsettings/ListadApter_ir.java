package edisonbro.com.edisonbroautomation.operatorsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import edisonbro.com.edisonbroautomation.R;


public class ListadApter_ir extends ArrayAdapter<String>
{
	String[] values;
	Context context;
	int resourceId;

	public ListadApter_ir(Context context, String[] values, int layoutId)
	{
		super(context,layoutId, values);
		this.context=context;
		this.values=values;
		resourceId=layoutId;
	}

	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		if (convertView == null) 
		{
			LayoutInflater li =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = li.inflate(resourceId, null);

		}
		else 
		{
			view = convertView;
		}

		TextView tv = (TextView) view.findViewById(R.id.textview);
		tv.setText(values[position]);

		return view;

	}
}