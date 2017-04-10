package study.pmoreira.popularmovies.business;

import android.content.Context;

abstract class BaseBusiness {

    static final String JSON_KEY_RESULTS = "results";

    final Context mContext;

    BaseBusiness(Context context) {
        mContext = context;
    }
}
