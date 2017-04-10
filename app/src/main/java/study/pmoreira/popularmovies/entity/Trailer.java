package study.pmoreira.popularmovies.entity;

public class Trailer {

    private String videoUrl;
    private String thumbUrl;

    public Trailer(String videoUrl, String thumbUrl) {
        this.videoUrl = videoUrl;
        this.thumbUrl = thumbUrl;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getThumbUrl() {
        return thumbUrl;
    }
}
