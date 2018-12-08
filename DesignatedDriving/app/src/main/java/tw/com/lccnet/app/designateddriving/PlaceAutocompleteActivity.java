package tw.com.lccnet.app.designateddriving;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;

import tw.com.lccnet.app.designateddriving.Component.CustomPlaceAutoCompleteFragment;

public class PlaceAutocompleteActivity extends AppCompatActivity {

    private static final String TAG = "PlaceAutocomplete";
    private CustomPlaceAutoCompleteFragment autocompleteFragment;
    private String address = "";
   // private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_autocomplete);
        initFragment();
    }

    private void initFragment() {
        autocompleteFragment = (CustomPlaceAutoCompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);
      //  editText=autocompleteFragment.getView().findViewById(R.id.place_autocomplete_search_input);
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {

            @Override
            public void onPlaceSelected(Place place) {
                address = place.getAddress().toString();
                autocompleteFragment.setText(address);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!address.equals("")) {
            setResult(0, getIntent().putExtra("address", address));
        }
        super.onBackPressed();
    }
}
