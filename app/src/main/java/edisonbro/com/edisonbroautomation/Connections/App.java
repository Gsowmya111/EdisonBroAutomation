package edisonbro.com.edisonbroautomation.Connections;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class App extends Application {

    protected static Context context = null;
    protected static Activity act = null;
    protected static String currentActivity = null;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        registerActivityLifecycleCallbacks(new MyActivityLifecycleCallbacks());
    }

    public static Context getContext() {
        return context;
    }

    public static String getActivity() {
        return currentActivity;
    }

    public static Activity getAct() {
        return act;
    }

    private static final class MyActivityLifecycleCallbacks implements ActivityLifecycleCallbacks {

        public void onActivityCreated(Activity activity, Bundle bundle) {
            //Log.e("","onActivityCreated:" + activity.getLocalClassName());
        }

        public void onActivityDestroyed(Activity activity) {
            //Log.e("","onActivityDestroyed:" + activity.getLocalClassName());
        }

        public void onActivityPaused(Activity activity) {
            //Log.e("","onActivityPaused:" + activity.getLocalClassName());
        }

        public void onActivityResumed(Activity activity) {
            act = activity;
            currentActivity = activity.getLocalClassName();
            android.util.Log.e("", "onActivityResumed:" + activity.getLocalClassName());
        }

        public void onActivitySaveInstanceState(Activity activity,
                                                Bundle outState) {
            //Log.e("","onActivitySaveInstanceState:" + activity.getLocalClassName());
        }

        public void onActivityStarted(Activity activity) {
            //Log.e("","onActivityStarted:" + activity.getLocalClassName());
        }

        public void onActivityStopped(Activity activity) {
            //Log.e("","onActivityStopped:" + activity.getLocalClassName());
        }

    }
}