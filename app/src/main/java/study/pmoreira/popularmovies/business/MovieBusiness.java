package study.pmoreira.popularmovies.business;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import study.pmoreira.popularmovies.business.MovieUrlBuilder.MovieAppender;
import study.pmoreira.popularmovies.business.MovieUrlBuilder.OrderByAppender;
import study.pmoreira.popularmovies.data.MovieContract.FavMovieEntry;
import study.pmoreira.popularmovies.data.MovieDao;
import study.pmoreira.popularmovies.entity.Movie;
import study.pmoreira.popularmovies.utils.NetworkUtils;

public class MovieBusiness extends BaseBusiness {

    private static final String TAG = MovieBusiness.class.getName();

    private static final String JSON_KEY_RUNTIME = "runtime";

    private MovieDao mMovieDao;

    public MovieBusiness(Context context) {
        super(context);
        mMovieDao = new MovieDao(context);
    }

    public List<Movie> findMovies(String orderBy) {
        String json = null;
        try {
            URL url = MovieUrlBuilder.buildMoviesUrl(new OrderByAppender(orderBy));
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(TAG, "findMovies: ", e);
        }

        return extractMoviesFromJson(json);
    }

    public Integer findRuntime(Long movieId) {
        String json = null;
        try {
            URL url = MovieUrlBuilder.buildMoviesUrl(new MovieAppender(movieId));
            json = NetworkUtils.getResponseFromHttpUrl(url);
        } catch (IOException e) {
            Log.e(TAG, "findRuntime: ", e);
        }

        return extractRuntimeFromJson(json);
    }

    public List<Movie> findAllFavoriteMovie() {
        Cursor cursor = mMovieDao.findAll((String[]) FavMovieEntry.ALL_COLUMNS.toArray(), FavMovieEntry.ORDER_BY);
        return extractProductList(cursor);
    }

    public boolean exists(String title) {
        return mMovieDao.exists(title);
    }

    public boolean insert(Movie movie) {
        ContentValues contentValues = new ContentValues(FavMovieEntry.ALL_COLUMNS.size());
        contentValues.put(FavMovieEntry.COLUMN_ID, movie.getId());
        contentValues.put(FavMovieEntry.COLUMN_POSTER_PATH, movie.getPosterPath());
        contentValues.put(FavMovieEntry.COLUMN_RATING, movie.getVoteAverage());
        contentValues.put(FavMovieEntry.COLUMN_RELEASE_DATE, movie.getReleaseDate());
        contentValues.put(FavMovieEntry.COLUMN_RUNTIME, movie.getRuntime());
        contentValues.put(FavMovieEntry.COLUMN_SYNOPSIS, movie.getOverview());
        contentValues.put(FavMovieEntry.COLUMN_TITLE, movie.getTitle());

        return mMovieDao.insert(contentValues) != null;
    }

    public Boolean delete(Long id) {
        return mMovieDao.delete(FavMovieEntry.WHERE_ID_EQUALS, new String[]{String.valueOf(id)}) > 0;
    }

    private List<Movie> extractMoviesFromJson(String moviesJson) {
        List<Movie> movies = new ArrayList<>();
        if (TextUtils.isEmpty(moviesJson)) {
            return movies;
        }

        try {
            JSONObject root = new JSONObject(moviesJson);
            JSONArray results = root.getJSONArray(JSON_KEY_RESULTS);

            for (int i = 0; i < results.length(); i++) {
                JSONObject movie = results.getJSONObject(i);
                movies.add(new Movie(movie));
            }

        } catch (JSONException e) {
            Log.e(TAG, "extractMoviesFromJson: ", e);
        }

        return movies;
    }

    private Integer extractRuntimeFromJson(String moviesJson) {
        Integer runtime = 0;
        if (TextUtils.isEmpty(moviesJson)) {
            return runtime;
        }

        try {
            JSONObject root = new JSONObject(moviesJson);
            runtime = root.getInt(JSON_KEY_RUNTIME);

        } catch (JSONException e) {
            Log.e(TAG, "extractMoviesFromJson: ", e);
        }

        return runtime;
    }

    private List<Movie> extractProductList(Cursor cursor) {
        List<Movie> movies = new ArrayList<>();

        if (cursor != null) {
            while (cursor.moveToNext()) {
                movies.add(new Movie(cursor));
            }
            cursor.close();
        }

        return movies;
    }
}
