package ca.algaerithms.inc.it.phytoplanktonairsystems.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FeedbackFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FeedbackFragment extends Fragment {

    EditText etName, etPhone, etEmail, etComment;
    RatingBar ratingBar;
    Button btnSubmit;
    String deviceModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public FeedbackFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment FeedbackFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FeedbackFragment newInstance(String param1, String param2) {
        FeedbackFragment fragment = new FeedbackFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        etName = view.findViewById(R.id.etName);
        etPhone = view.findViewById(R.id.etPhone);
        etEmail = view.findViewById(R.id.etEmail);
        etComment = view.findViewById(R.id.etComment);
        ratingBar = view.findViewById(R.id.ratingBar);
        btnSubmit = view.findViewById(R.id.btnSubmit);

        // Get device model
        deviceModel = android.os.Build.MODEL;

        prefillUserData();

        btnSubmit.setOnClickListener(v -> submitFeedback());

        return view;
    }

    private void prefillUserData() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            etEmail.setText(user.getEmail());

            // Fetch name from Firestore if stored in /users/{uid}/name
            FirebaseFirestore.getInstance().collection("users")
                    .document(user.getUid())
                    .get()
                    .addOnSuccessListener(snapshot -> {
                        if (snapshot.exists()) {
                            String name = snapshot.getString("name");
                            if (name != null) {
                                etName.setText(name);
                            }
                        }
                    });
        }
    }

    private void submitFeedback() {
        String name = etName.getText().toString().trim();
        String phone = etPhone.getText().toString().trim();
        String email = etEmail.getText().toString().trim();
        String comment = etComment.getText().toString().trim();
        float rating = ratingBar.getRating();

        boolean hasError = false;

        if (name.isEmpty()) {
            etName.setError("Name is required");
            hasError = true;
        }

        if (phone.isEmpty()) {
            etPhone.setError("Phone number is required");
            hasError = true;
        } else if (!phone.matches("\\d{10}")) {
            etPhone.setError("Enter a valid 10-digit phone number");
            hasError = true;
        }

        if (email.isEmpty()) {
            etEmail.setError("Email is required");
            hasError = true;
        } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("Invalid email address");
            hasError = true;
        }

        if (comment.isEmpty()) {
            etComment.setError("Comment is required");
            hasError = true;
        }

        if (rating == 0) {
            Toast.makeText(getContext(), "Please select a rating.", Toast.LENGTH_SHORT).show();
            hasError = true;
        }

        if (hasError) return;

//        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
//        if (currentUser == null) {
//            Toast.makeText(getContext(), "You must be signed in to submit feedback.", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Map<String, Object> feedback = new HashMap<>();
        feedback.put("name", name);
        feedback.put("phone", phone);
        feedback.put("email", email);
        feedback.put("comment", comment);
        feedback.put("rating", rating);
        feedback.put("deviceModel", deviceModel);
        //feedback.put("userId", currentUser.getUid());

        FirebaseFirestore.getInstance().collection("feedback")
                .add(feedback)
                .addOnSuccessListener(doc -> {
                    Toast.makeText(getContext(), "Feedback submitted!", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }

    private void clearFields() {
        etName.setText("");
        etPhone.setText("");
        etEmail.setText("");
        etComment.setText("");
        ratingBar.setRating(0);
    }
}