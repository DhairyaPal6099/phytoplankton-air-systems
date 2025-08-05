/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.ServiceRequest;

public class RequestServiceBottomSheet extends BottomSheetDialogFragment {

    private Spinner requestTypeSpinner;
    private EditText notesEditText;
    private Button submitButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fab_service_request, container, false);

        requestTypeSpinner = view.findViewById(R.id.request_type_spinner);
        notesEditText = view.findViewById(R.id.request_notes);
        submitButton = view.findViewById(R.id.submit_request_button);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.request_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestTypeSpinner.setAdapter(adapter);

        submitButton.setOnClickListener(v -> sendRequestToFirestore());

        return view;
    }
    private void sendRequestToFirestore() {
        String type = requestTypeSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString();

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String email = documentSnapshot.getString("email");
                        String name = documentSnapshot.getString("name");
                        Timestamp timestamp = Timestamp.now();

                        ServiceRequest request = new ServiceRequest(type, notes, email, name, timestamp);

                        FirebaseFirestore.getInstance()
                                .collection("serviceRequests")
                                .add(request)
                                .addOnSuccessListener(ref -> {
                                    Toast.makeText(getContext(), "Request submitted successfully!", Toast.LENGTH_SHORT).show();
                                    dismiss();
                                })
                                .addOnFailureListener(e -> {
                                    Toast.makeText(getContext(), "Failed to submit request. Try again.", Toast.LENGTH_SHORT).show();
                                });
                    } else {
                        Toast.makeText(getContext(), "User data not found.", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error retrieving user info.", Toast.LENGTH_SHORT).show();
                });
    }

    }
