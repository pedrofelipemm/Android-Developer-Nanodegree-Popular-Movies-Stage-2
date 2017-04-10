package study.pmoreira.popularmovies.entity;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONObject;

import study.pmoreira.popularmovies.data.MovieContract.FavMovieEntry;
import study.pmoreira.popularmovies.utils.DateUtils;

public class Movie implements Parcelable {

    private static final String IMAGE_BASE_URL = "http://image.tmdb.org/t/p/";
    private static final String IMAGE_SIZE_185 = "w185";
    private static final Integer INVALID_RUNTIME = -1;

    private static final String RELEASE_DATE_PATTERN = "yyyy";

    private static final String JSON_KEY_ID = "id";
    private static final String JSON_KEY_TITLE = "original_title";
    private static final String JSON_KEY_POSTER_PATH = "poster_path";
    private static final String JSON_KEY_OVERVIEW = "overview";
    private static final String JSON_KEY_VOTE_AVERAGE = "vote_average";
    private static final String JSON_KEY_RELEASE_DATE = "release_date";

    public static final Movie EMPTY_MOVIE = new Movie();

    private Long id;
    private String title;
    private String posterPath;
    private String overview;
    private Double voteAverage;
    private String releaseDate;
    private Integer runtime;

    private Movie() {
    }

    public Movie(JSONObject movie) {
        id = movie.optLong(JSON_KEY_ID);
        title = movie.optString(JSON_KEY_TITLE);
        posterPath = movie.optString(JSON_KEY_POSTER_PATH);
        overview = movie.optString(JSON_KEY_OVERVIEW);
        voteAverage = movie.optDouble(JSON_KEY_VOTE_AVERAGE);
        releaseDate = DateUtils.formatDate(RELEASE_DATE_PATTERN, movie.optString(JSON_KEY_RELEASE_DATE));
    }

    public Movie(Cursor cursor) {
        id = cursor.getLong(cursor.getColumnIndex(FavMovieEntry._ID));
        title = cursor.getString(cursor.getColumnIndex(FavMovieEntry.COLUMN_TITLE));
        posterPath = cursor.getString(cursor.getColumnIndex(FavMovieEntry.COLUMN_POSTER_PATH));
        overview = cursor.getString(cursor.getColumnIndex(FavMovieEntry.COLUMN_SYNOPSIS));
        voteAverage = cursor.getDouble(cursor.getColumnIndex(FavMovieEntry.COLUMN_RATING));
        releaseDate = cursor.getString(cursor.getColumnIndex(FavMovieEntry.COLUMN_RELEASE_DATE));
        runtime = cursor.getInt(cursor.getColumnIndex(FavMovieEntry.COLUMN_RUNTIME));
    }

    private Movie(Parcel in) {
        id = in.readLong();
        title = in.readString();
        posterPath = in.readString();
        overview = in.readString();
        voteAverage = in.readDouble();
        releaseDate = in.readString();
        runtime = in.readInt();
        if (runtime.equals(INVALID_RUNTIME)) {
            runtime = null;
        }
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public Double getVoteAverage() {
        return voteAverage;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public Integer getRuntime() {
        return runtime;
    }

    public void setRuntime(Integer runtime) {
        this.runtime = runtime;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(title);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeDouble(voteAverage);
        dest.writeString(releaseDate);
        dest.writeInt(runtime != null ? runtime : INVALID_RUNTIME);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {

        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    public String getPosterUrl() {
        String posterUrl = "";
        if (posterPath != null) {
            posterUrl = IMAGE_BASE_URL + IMAGE_SIZE_185 + getPosterPath();
        }
        return posterUrl;
    }
}
