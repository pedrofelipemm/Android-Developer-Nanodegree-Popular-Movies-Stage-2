package study.pmoreira.popularmovies.utils;

import android.content.Context;
import android.content.res.Configuration;

import study.pmoreira.popularmovies.R;

public class ScreenUtils {

    private static Boolean isTablet;

    private ScreenUtils() {
    }

    public static boolean isTablet(Context context) {
        if (isTablet == null) {
            isTablet = context.getResources().getBoolean(R.bool.isTablet);
        }

        return isTablet;
    }

    public static boolean isLandscape(Context context) {
        return context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    public static boolean isTabletLandscape(Context context) {
        return ScreenUtils.isTablet(context) && ScreenUtils.isLandscape(context);
    }

}
