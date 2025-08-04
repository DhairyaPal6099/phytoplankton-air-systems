/* Julian Imperial – N01638310
   Dhairya Pal – N01576099
   Sanskriti Mansotra – N01523183
   Dharmik Shah – N01581796 */

package ca.algaerithms.inc.it.phytoplanktonairsystems.view.ui;


import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
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

import ca.algaerithms.inc.it.phytoplanktonairsystems.R;

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

        submitButton.setOnClickListener(v -> sendServiceRequest());

        return view;
    }

    private void sendServiceRequest() {
        String type = requestTypeSpinner.getSelectedItem().toString();
        String notes = notesEditText.getText().toString();

        String subject = "Service Request – " + type;
        String body = "Request Type: " + type + "\n\nNotes:\n" + notes + "\n\nSent from Phytoplankton Air Systems app.";

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // use mail client only
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"algaerithms@gmail.com"});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send request via..."));
            dismiss(); // close bottom sheet
        } catch (ActivityNotFoundException e) {
            Toast.makeText(getContext(), "No email app found.", Toast.LENGTH_SHORT).show();
        }
    }
}
