package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

public class VideoItem {
    private String title;
    private String videoId; // Only the YouTube ID (not full URL)

    public VideoItem(String title, String videoId) {
        this.title = title;
        this.videoId = videoId;
    }

    public String getTitle() { return title; }
    public String getVideoId() { return videoId; }

    public String getThumbnailUrl() {
        return "https://img.youtube.com/vi/" + videoId + "/0.jpg";
    }

    public String getVideoUrl() {
        return "https://www.youtube.com/watch?v=" + videoId;
    }
}
