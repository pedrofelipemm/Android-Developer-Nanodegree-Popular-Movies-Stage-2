package study.pmoreira.popularmovies.entity;

import org.json.JSONObject;

public class Review {

    private static final String JSON_KEY_AUTHOR = "author";
    private static final String JSON_KEY_CONTENT = "content";
    private static final String JSON_KEY_URL = "url";

    private String author;
    private String content;
    private String url;

    public Review(JSONObject review) {
        author = review.optString(JSON_KEY_AUTHOR);
        content = review.optString(JSON_KEY_CONTENT);
        url = review.optString(JSON_KEY_URL);
    }

    public String getAuthor() {
        return author;
    }

    public String getContent() {
        return content;
    }

    public String getUrl() {
        return url;
    }
}
