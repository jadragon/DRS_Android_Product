package tw.com.lccnet.app.designateddriving.Component;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.AppCompatEditText;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;

import tw.com.lccnet.app.designateddriving.R;

public class CustomPlaceAutoCompleteFragment extends PlaceAutocompleteFragment {
    Place place;
    AppCompatEditText search_input;

    @Override
    public View onCreateView(LayoutInflater layoutInflater, ViewGroup viewGroup, Bundle bundle) {
        return super.onCreateView(layoutInflater, viewGroup, bundle);

    }

    @Override
    public void onActivityResult(int i, int i1, Intent intent) {
        super.onActivityResult(i, i1, intent);
        search_input = getView().findViewById(R.id.place_autocomplete_search_input);
        search_input.setText(place.getAddress());
    }
}