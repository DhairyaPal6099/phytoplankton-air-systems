package ca.algaerithms.inc.it.phytoplanktonairsystems;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class NotificationModel {
    private String title;
    private String message;
    private Date timestamp;

    public NotificationModel() {}

    public NotificationModel(String title, String message, Date timestamp) {
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
        return new SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(timestamp);
    }
}
