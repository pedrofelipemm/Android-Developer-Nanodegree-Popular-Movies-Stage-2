package study.pmoreira.popularmovies.data;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.List;

public class MovieContract {

    static final String CONTENT_AUTHORITY = "study.pmoreira.popularmovies";

    private static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    static final String PATH_MOVIE = "movies";

    public static final class FavMovieEntry implements BaseColumns {

        static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        static final String TABLE_NAME = "fav_movie";

        static final String CONTENT_LIST_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String COLUMN_ID = _ID;
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_POSTER_PATH = "poster_path";
        public static final String COLUMN_RATING = "rating";
        public static final String COLUMN_RELEASE_DATE = "release_date";
        public static final String COLUMN_SYNOPSIS = "synopsis";
        public static final String COLUMN_RUNTIME = "runtime";

        public static final List<String> ALL_COLUMNS = Arrays.asList(_ID, COLUMN_TITLE, COLUMN_POSTER_PATH,
                COLUMN_RATING, COLUMN_RELEASE_DATE, COLUMN_SYNOPSIS, COLUMN_RUNTIME);

        public static final String WHERE_ID_EQUALS = COLUMN_ID + "=?";
        static final String WHERE_TITLE_EQUALS = COLUMN_TITLE + "=?";

        public static final String ORDER_BY = COLUMN_TITLE + " ASC";
    }
}