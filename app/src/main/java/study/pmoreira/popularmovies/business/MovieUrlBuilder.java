package study.pmoreira.popularmovies.business;

import android.net.Uri;
import android.text.TextUtils;

import java.net.MalformedURLException;
import java.net.URL;

import study.pmoreira.popularmovies.BuildConfig;

public class MovieUrlBuilder {

    private static final String BASE_URL = "http://api.themoviedb.org/3/movie/";
    private static final String PATH_VIDEOS = "videos";
    private static final String PATH_REVIEWS = "reviews";
    private static final String PARAM_API_KEY = "api_key";

    public static final String ORDER_BY_MOST_POPULAR = "popular";
    public static final String ORDER_BY_TOP_RATED = "top_rated";

    private MovieUrlBuilder() {
    }

    static URL buildMoviesUrl(Appender appender) throws MalformedURLException {
        Uri.Builder builder = Uri.parse(BASE_URL).buildUpon();

        appender.append(builder);

        Uri uri = builder.appendQueryParameter(PARAM_API_KEY, BuildConfig.MOVIEDB_API_KEY).build();

        return new URL(uri.toString());
    }

    static class OrderByAppender extends Appender {

        OrderByAppender(String path) {
            super(path);
        }

        @Override
        public void append(Uri.Builder builder) {
            switch (mPath) {
                case ORDER_BY_TOP_RATED:
                    builder.appendPath(ORDER_BY_TOP_RATED);
                    break;
                case ORDER_BY_MOST_POPULAR:
                default:
                    builder.appendPath(ORDER_BY_MOST_POPULAR);
                    break;
            }
        }
    }

    static class VideoAppender extends Appender {

        VideoAppender(Long movieId) {
            super(String.valueOf(movieId));
        }

        @Override
        public void append(Uri.Builder builder) {
            if (!TextUtils.isEmpty(mPath)) {
                builder.appendPath(mPath);
                builder.appendPath(PATH_VIDEOS);
            }
        }
    }

    static class ReviewAppender extends Appender {

        ReviewAppender(Long movieId) {
            super(String.valueOf(movieId));
        }

        @Override
        public void append(Uri.Builder builder) {
            if (!TextUtils.isEmpty(mPath)) {
                builder.appendPath(mPath);
                builder.appendPath(PATH_REVIEWS);
            }
        }
    }

    static class MovieAppender extends Appender {

        MovieAppender(Long movieId) {
            super(String.valueOf(movieId));
        }

        @Override
        public void append(Uri.Builder builder) {
            if (!TextUtils.isEmpty(mPath)) {
                builder.appendPath(mPath);
            }
        }
    }

    private static abstract class Appender {

        String mPath;

        private Appender(String path) {
            validate(path);
            mPath = path;
        }

        private void validate(String path) {
            if (path == null) {
                throw new IllegalArgumentException("Parameter path cannot be null.");
            }
        }

        protected abstract void append(Uri.Builder builder);
    }
}
