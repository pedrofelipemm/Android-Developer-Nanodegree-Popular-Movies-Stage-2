package study.pmoreira.popularmovies.utils;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class DateUtils {

    private static final String TAG = DateUtils.class.getName();

    private DateUtils() {
    }

    public static String formatDate(String datePatten, String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat(datePatten, Locale.US);
        try {
            return sdf.format(sdf.parse(dateString));
        } catch (ParseException e) {
            Log.e(TAG, "formatDate: ", e);
            return "";
        }
    }

}
