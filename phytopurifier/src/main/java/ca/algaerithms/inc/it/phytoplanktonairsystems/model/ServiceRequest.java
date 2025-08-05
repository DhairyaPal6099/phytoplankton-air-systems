/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.model;

import com.google.firebase.Timestamp;

public class ServiceRequest {
    private String requestType;
    private String notes;
    private String email;
    private String name;
    private Timestamp timestamp;

    public ServiceRequest() {
        // Required empty constructor for Firestore
    }

    public ServiceRequest(String requestType, String notes, String email, String name, Timestamp timestamp) {
        this.requestType = requestType;
        this.notes = notes;
        this.email = email;
        this.name = name;
        this.timestamp = timestamp;
    }

    public String getRequestType() { return requestType; }
    public String getNotes() { return notes; }
    public String getEmail() { return email; }
    public String getName() { return name; }
    public Timestamp getTimestamp() { return timestamp; }
}
