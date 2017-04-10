package study.pmoreira.popularmovies.business;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import study.pmoreira.popularmovies.business.MovieUrlBuilder.VideoAppender;
import study.pmoreira.popularmovies.entity.Trailer;
import study.pmoreira.popularmovies.utils.NetworkUtils;

public class TrailerBusiness extends BaseBusiness {

    private static final String TAG = TrailerBusiness.class.getName();

    private static final String JSON_KEY_KEY = "key";

    public TrailerBusiness(Context context) {
        super(context);
    }

    public List<Trailer> findTrailers(Long videoId) {
        String json = null;
        try {
            URL url = MovieUrlBuilder.buildMoviesUrl(new VideoAppender(videoId));
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(TAG, "findTrailers: ", e);
        }

        return extractTrailerFromJson(json);
    }

    private List<Trailer> extractTrailerFromJson(String json) {
        List<Trailer> trailers = new ArrayList<>();
        if (TextUtils.isEmpty(json)) {
            return trailers;
        }

        try {
            JSONObject root = new JSONObject((json));
            JSONArray results = root.getJSONArray(JSON_KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                String key = result.getString(JSON_KEY_KEY);

                trailers.add(new Trailer(
                        TrailerUrlBuilder.buildYoutubeUrl(key),
                        TrailerUrlBuilder.buildYoutubeThumbUrl(key)));
            }

        } catch (JSONException e) {
            Log.e(TAG, "extractTrailerFromJson: ", e);
        }

        return trailers;
    }
}
