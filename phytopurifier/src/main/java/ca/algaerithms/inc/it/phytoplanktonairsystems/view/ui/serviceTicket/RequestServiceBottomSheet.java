/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.serviceTicket;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;
import ca.algaerithms.inc.it.phytoplanktonairsystems.model.ServiceRequest;
import ca.algaerithms.inc.it.phytoplanktonairsystems.utils.NetworkUtils;
import ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui.MainActivity;

public class RequestServiceBottomSheet extends BottomSheetDialogFragment {

    private Spinner requestTypeSpinner;
    private EditText notesEditText;
    private Button submitButton;

    private EditText fullNameEditText;
    private EditText emailEditText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fab_service_request, container, false);

        // Bind views
        fullNameEditText = view.findViewById(R.id.full_name);
        emailEditText = view.findViewById(R.id.email);
        requestTypeSpinner = view.findViewById(R.id.request_type_spinner);
        notesEditText = view.findViewById(R.id.notes);
        submitButton = view.findViewById(R.id.submit_request_button);

        // Setup request type dropdown
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.request_types,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestTypeSpinner.setAdapter(adapter);

        // Autofill user info from Firestore
        fetchUserInfo();

        // Handle submit
        submitButton.setOnClickListener(v -> {
            // Network check
            if (!NetworkUtils.isConnected(requireContext())) {
            offlineSnackbar(requireView());
            return; // stop here if no internet
            }
            sendRequestToFirestore();
        });

        return view;
    }

    public void offlineSnackbar(View view) {
        View fabLayout = view.findViewById(R.id.fabButton);
        fabLayout.setVisibility(View.GONE);

        Snackbar snackbar = Snackbar.make(
                        view,
                        R.string.no_internet_connection,
                        Snackbar.LENGTH_INDEFINITE
                )
                .setBackgroundTint(requireContext().getColor(R.color.faded_red))
                .setTextColor(requireContext().getColor(R.color.white))
                .setActionTextColor(requireContext().getColor(R.color.light_blue))
                .setAction(R.string.dismiss, v -> {
                    fabLayout.setVisibility(View.VISIBLE);
                });

        snackbar.show();
    }

    private void fetchUserInfo() {
        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(uid)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String name = documentSnapshot.getString("name");
                        String email = documentSnapshot.getString("email");

                        if (name != null) fullNameEditText.setText(name);
                        if (email != null) emailEditText.setText(email);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to fetch user info", Toast.LENGTH_SHORT).show();
                });
    }

    private void sendRequestToFirestore() {
        String type = requestTypeSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString();
        String email = emailEditText.getText().toString();
        String name = fullNameEditText.getText().toString();
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
    }
}
