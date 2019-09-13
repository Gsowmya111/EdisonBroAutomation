package edisonbro.com.edisonbroautomation.operatorsettings;

/**
 * Created by ABHISHEK on 5/5/2015.
 */
public interface TotalListener {
    public void onTotalChanged(int sum);

    public void expandGroupEvent(int groupPosition, boolean isExpanded);
}
