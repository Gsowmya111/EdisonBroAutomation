package edisonbro.com.edisonbroautomation.operatorsettings;

import android.content.Context;

import java.util.List;
import java.util.Map;

public class Model {

	private String name,buttonText;
	private boolean selected;
	Context context;


	public Model(String name) {
		this.name = name;
	}

	//constructor for expandable list adapter 
	public Model(Map<Integer,List<String>> map) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void setbuttontext(String text){
		buttonText=text;
	}

	public String getButtonText(){
		return buttonText;
	}

}
