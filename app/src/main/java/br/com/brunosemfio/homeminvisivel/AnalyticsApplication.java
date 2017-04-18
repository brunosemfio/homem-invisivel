package br.com.brunosemfio.homeminvisivel;

import android.app.Application;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Tracker;

public class AnalyticsApplication extends Application {

    private Tracker tracker;

    synchronized public Tracker getDefaultTracker() {
        
        if (tracker == null) {
            
            final GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            
            tracker = analytics.newTracker("UA-96999201-1");
        }
        
        return tracker;
    }

    @Override
    public void onCreate() {

        super.onCreate();

        Tracker t = getDefaultTracker();
        t.enableAutoActivityTracking(false);
        t.enableAdvertisingIdCollection(true);
        t.enableExceptionReporting(true);
        t.setSessionTimeout(300L);
    }
}