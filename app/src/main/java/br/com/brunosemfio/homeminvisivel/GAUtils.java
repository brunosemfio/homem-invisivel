package br.com.brunosemfio.homeminvisivel;

import android.app.Activity;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

public class GAUtils {

    public static void setScreenName(Activity activity, String s) {

        try {

            final AnalyticsApplication app = (AnalyticsApplication) activity.getApplication();

            final Tracker tracker = app.getDefaultTracker();
            tracker.setScreenName(s);
            tracker.send(new HitBuilders.ScreenViewBuilder().build());

        } catch (Exception e) {

            e.printStackTrace();
        }
    }
}