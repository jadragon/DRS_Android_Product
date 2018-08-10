package net.xprinter.example4wifi;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import java.io.UnsupportedEncodingException;

public class MainActivity extends Activity {
    private Button buttonCon = null;
    private Button buttonPf = null;
    private Button buttonCash = null;
    private Button buttonCut = null;
    private EditText mTextIp = null;
    private EditText mprintfData = null;
    private EditText mprintfLog = null;
    private Socketmanager mSockManager;
    private Button buttonTest=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonCon = (Button) findViewById(R.id.conTest);
        buttonPf = (Button) findViewById(R.id.printf);
        buttonCash = (Button) findViewById(R.id.buttonCash);
        buttonTest=(Button) findViewById(R.id.buttonTest);
        buttonCut = (Button) findViewById(R.id.buttonCut);
        mTextIp = (EditText) findViewById(R.id.printerIp);
        mprintfData = (EditText) findViewById(R.id.printfData);
        mprintfLog = (EditText) findViewById(R.id.printfLog);
        ButtonListener buttonListener = new ButtonListener();
        buttonCon.setOnClickListener(buttonListener);
        buttonPf.setOnClickListener(buttonListener);
        buttonCash.setOnClickListener(buttonListener);
        buttonCut.setOnClickListener(buttonListener);
        buttonTest.setOnClickListener(buttonListener);
        mSockManager = new Socketmanager(MainActivity.this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    class ButtonListener implements OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.conTest:
                    if (conTest(mTextIp.getText().toString())) {
                        PrintfLog("連接成功...");
                        buttonCon.setText("已連接...");
                        buttonPf.setEnabled(true);
                        buttonCash.setEnabled(true);
                        buttonCut.setEnabled(true);
                        buttonTest.setEnabled(true);
                    } else {
                        PrintfLog("連接失敗...");
                        buttonCon.setText("未連接...");
                        buttonPf.setEnabled(false);
                        buttonCash.setEnabled(false);
                        buttonCut.setEnabled(false);
                        buttonTest.setEnabled(false);
                    }
                    break;
                case R.id.printf:
                    try {
                       // mprintfData.getText().toString()
                        if (PrintfData(( "一二三日五六七八九十一二三四五"+ "\n\n\n\n\n\n").getBytes("GBK"))) {
                            PrintfLog("打印成功...");
                        } else {
                            PrintfLog("打印失败...");
                            buttonPf.setEnabled(false);
                        }
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        PrintfLog("數據發送錯誤...");
                    }
                    break;
                case R.id.buttonCash:
                    byte SendCash[] = {0x1b, 0x70, 0x00, 0x1e, (byte) 0xff, 0x00};
                    if (PrintfData(SendCash)) {
                        PrintfLog("打開成功...");
                    } else {
                        PrintfLog("打開失敗...");
                    }
                    break;
                case R.id.buttonCut:
                    byte SendCut[] = {0x0a, 0x0a, 0x1d, 0x56, 0x01};
                    if (PrintfData(SendCut)) {
                        PrintfLog("切紙成功...");
                    } else {
                        PrintfLog("切纸失败...");
                    }
                    break;
                case R.id.buttonTest:
                    /*
                    if (PrintfData(EscPosUtil.printBarcode(EscPosUtil.BarCode.CODE39,"86516163"))) {
                        PrintfLog("測試成功...");
                    } else {
                        PrintfLog("測試失败...");
                    }
                    */
                        EscPosUtil.main();

                    break;
            }

        }
    }

    public boolean conTest(String printerIp) {
        mSockManager.mPort = 9100;
        mSockManager.mstrIp = printerIp;
        mSockManager.threadconnect();
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mSockManager.getIstate()) {
            return true;
        } else {
            return false;
        }
    }

    public void PrintfLog(String logString) {
        mprintfLog.setText(logString);
    }

    public boolean PrintfData(byte[] data) {
        mSockManager.threadconnectwrite(data);
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (mSockManager.getIstate()) {
            return true;
        } else {
            return false;
        }
    }

}
