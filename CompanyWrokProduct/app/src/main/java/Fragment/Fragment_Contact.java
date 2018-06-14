package Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.tw.wrokproduct.R;

import org.json.JSONObject;

import adapter.recyclerview.ContactRecyclerAdapter;

public class Fragment_Contact extends Fragment {
    RecyclerView recyclerView;
    JSONObject json;

    View v;

    public void setJson(JSONObject json) {
        this.json = json;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_contact_layout, container, false);
        recyclerView = v.findViewById(R.id.contact_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(new ContactRecyclerAdapter(getActivity(), json));
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);
        return v;
    }

}
