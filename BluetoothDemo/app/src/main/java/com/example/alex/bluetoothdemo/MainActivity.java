package com.example.alex.bluetoothdemo;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    BluetoothAdapter mBtAdapter;
    private static int REQUEST_ENABLE_BT = 123;
    private Set<BluetoothDevice> pairedDevices;
    private ListView lv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBtAdapter = BluetoothAdapter.getDefaultAdapter();
        lv = findViewById(R.id.lv);

        // 檢查藍牙是否開啟
        if (!mBtAdapter.isEnabled()) {
            Intent enableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableIntent, REQUEST_ENABLE_BT);
        }
        // 搜尋到藍牙裝置時，呼叫我們的函式


    }

    public void list(View view) {
        pairedDevices = mBtAdapter.getBondedDevices();

        ArrayList<String> list = new ArrayList<>();
        for (BluetoothDevice bt : pairedDevices) {
            list.add(bt.getName());
        }
        Toast.makeText(getApplicationContext(), "Showing Paired Devices",
                Toast.LENGTH_SHORT).show();
        ArrayAdapter adapter = new ArrayAdapter
                (this, android.R.layout.simple_list_item_1, list);
        lv.setAdapter(adapter);


        // 取得 server 監聽的 socket 接口

            //UUID格式一般是"xxxxxxxx-xxxx-xxxx-xxxx-xxxxxxxxxxxx"可到
            //http://www.uuidgenerator.com 申請

          //  BluetoothSocket mmSocket = mBtAdapter.getRemoteDevice(list.get(0).getAddress()).createRfcommSocketToServiceRecord();
         //   mmSocket.connect();  // blocking call


    }


}
