package edisonbro.com.edisonbroautomation;

/**
 *  FILENAME: Edisonbro_AnalyticsApplication.java
 *  DATE: 07-08-2018

 *  DESCRIPTION:To add google analytics to events.

 *  Copyright (C) EdisonBro Smart Labs Pvt Ltd. All rights reserved.
 */


import android.app.Application;

/**
 * Created by Divya on 11/15/2017.
 */

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;


public class Edisonbro_AnalyticsApplication extends Application{

    private Tracker mTracker;

    /**
     * Gets the default {@link Tracker} for this {@link Application}.
     * @return tracker
     */
    synchronized public Tracker getDefaultTracker() {
        if (mTracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            // Setting mTracker to Analytics Tracker declared in our xml Folder
            mTracker = analytics.newTracker(R.xml.analytics_tracker);
        }
        return mTracker;
    }

}
