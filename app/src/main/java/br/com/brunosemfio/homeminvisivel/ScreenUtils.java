package br.com.brunosemfio.homeminvisivel;

import android.app.Activity;
import android.util.DisplayMetrics;

public class ScreenUtils {

    public static MaxSize getMaxSize(Activity activity) {

        final DisplayMetrics displayMetrics = new DisplayMetrics();

        activity.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        return new MaxSize(displayMetrics.widthPixels, displayMetrics.heightPixels);
    }
}