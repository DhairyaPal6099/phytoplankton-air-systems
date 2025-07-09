package ca.algaerithms.inc.it.phytoplanktonairsystems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AchievementModel {
    private String title;
    private String message;
    private Date timestamp;

    public AchievementModel() {}

    public AchievementModel(String title, String message, Date timestamp) {
        this.title = title;
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getTitle() {
        return title;
    }

    public String getMessage() {
        return message;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public String getFormattedDate() {
        if (timestamp == null) return "";
        return new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(timestamp);
    }
}
