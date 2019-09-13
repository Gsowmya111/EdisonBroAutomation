package edisonbro.com.edisonbroautomation.operatorsettings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;



import java.util.List;

import edisonbro.com.edisonbroautomation.R;

//spinner adapter class
public class CustomSpinnerAdapter extends ArrayAdapter<String>
{
	List<String> values;
	Context context;
	int resourceId;

	//class constructor
	public CustomSpinnerAdapter(Context context, int layoutId , List<String> values )
	{
		super(context,layoutId, values);
		this.context=context;
		this.values=values;
		resourceId=layoutId;
	}

	//getting view for spinner
	public View getView(int position, View convertView, ViewGroup parent)
	{
		View view;
		if (convertView == null) {
			LayoutInflater li =(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = li.inflate(resourceId, null);

		}else {
			view = convertView;
		}

		TextView tv = (TextView) view.findViewById(R.id.devnames);
		tv.setText(values.get(position));

		return view;

	}

	@Override
	public View getDropDownView(int position, View cnvtView, ViewGroup prnt) {
		return getView(position, cnvtView, prnt);
	}

	@Override
	public int getCount() {
		//if count is 0 displaying last item else displaying all items except last one
		int count = super.getCount();
		return count > 0 ? count - 1 : count;
	}


}



