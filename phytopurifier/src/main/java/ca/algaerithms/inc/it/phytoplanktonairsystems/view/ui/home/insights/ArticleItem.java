package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.home.insights;

public class ArticleItem {
    private String title;
    private String snippet;
    private String meta;
    private int imageResId; // assuming local drawable resource
    private String url;

    public ArticleItem(String title, String snippet, String meta, int imageResId, String url) {
        this.title = title;
        this.snippet = snippet;
        this.meta = meta;
        this.imageResId = imageResId;
        this.url = url;
    }
    public String getUrl() { return url; }

    // Getters
    public String getTitle() { return title; }
    public String getSnippet() { return snippet; }
    public String getMeta() { return meta; }
    public int getImageResId() { return imageResId; }
}
