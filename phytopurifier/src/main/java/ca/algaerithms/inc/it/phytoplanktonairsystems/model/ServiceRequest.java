/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */


package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import com.google.firebase.Timestamp;

public class ServiceRequest {
    private String requestType;
    private String notes;
    private String userEmail;
    private Timestamp timestamp;

    public ServiceRequest() {
        // Required empty constructor for Firestore
    }

    public ServiceRequest(String requestType, String notes, String userEmail, Timestamp timestamp) {
        this.requestType = requestType;
        this.notes = notes;
        this.userEmail = userEmail;
        this.timestamp = timestamp;
    }

    public String getRequestType() {
        return requestType;
    }

    public String getNotes() {
        return notes;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }
}
