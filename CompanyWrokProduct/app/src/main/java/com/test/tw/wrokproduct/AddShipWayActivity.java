package com.test.tw.wrokproduct;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class AddShipWayActivity extends AppCompatActivity {
    TextView addshipway_store;
    String deliver;
    String name;
    String address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_shipway);
        addshipway_store = findViewById(R.id.addshipway_store);
        addshipway_store.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddShipWayActivity.this, StoreWebViewActivity.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Bundle bundle = data.getExtras();
        deliver = bundle.getString("deliver");
        name = bundle.getString("name");
        address = bundle.getString("address");
        addshipway_store.setText(name);

    }
}
