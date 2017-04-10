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

import study.pmoreira.popularmovies.business.MovieUrlBuilder.ReviewAppender;
import study.pmoreira.popularmovies.entity.Review;
import study.pmoreira.popularmovies.utils.NetworkUtils;

public class ReviewBusiness extends BaseBusiness {

    private static final String TAG = ReviewBusiness.class.getName();

    public ReviewBusiness(Context context) {
        super(context);
    }

    public List<Review> findReviews(Long videoId) {
        String json = null;
        try {
            URL url = MovieUrlBuilder.buildMoviesUrl(new ReviewAppender(videoId));
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(TAG, "findReviews: ", e);
        }

        return extractReviewFromJson(json);
    }

    private List<Review> extractReviewFromJson(String json) {
        List<Review> reviews = new ArrayList<>();
        if (TextUtils.isEmpty(json)) {
            return reviews;
        }

        try {
            JSONObject root = new JSONObject((json));
            JSONArray results = root.getJSONArray(JSON_KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject result = results.getJSONObject(i);
                reviews.add(new Review(result));
            }

        } catch (JSONException e) {
            Log.e(TAG, "extractReviewFromJson: ", e);
        }

        return reviews;
    }
}
