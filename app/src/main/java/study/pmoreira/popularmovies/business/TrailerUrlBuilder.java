package study.pmoreira.popularmovies.business;

import android.net.Uri;

class TrailerUrlBuilder {

    private static final String BASE_YOUTUBE_URL = "https://www.youtube.com";
    private static final String PATH_YOUTUBE_WATCH = "watch";
    private static final String PARAM_YOUTUBE_VIDEO = "v";

    private static final String BASE_YOUTUBE_THUMB_URL = "https://img.youtube.com/vi/";
    private static final String YOUTUBE_THUMB_DEFAULT_URL = "default.jpg";

    static String buildYoutubeUrl(String youtubeVideoId) {
        return Uri.parse(BASE_YOUTUBE_URL).buildUpon()
                .appendPath(PATH_YOUTUBE_WATCH)
                .appendQueryParameter(PARAM_YOUTUBE_VIDEO, youtubeVideoId)
                .build()
                .toString();
    }

    static String buildYoutubeThumbUrl(String youtubeVideoId) {
        return Uri.parse(BASE_YOUTUBE_THUMB_URL).buildUpon()
                .appendPath(youtubeVideoId)
                .appendPath(YOUTUBE_THUMB_DEFAULT_URL)
                .build()
                .toString();
    }
}
