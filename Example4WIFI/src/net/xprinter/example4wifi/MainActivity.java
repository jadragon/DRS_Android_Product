package net.xprinter.example4wifi;


import java.io.UnsupportedEncodingException;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity {
	private Button buttonCon=null;
	private Button buttonPf=null;
	private Button buttonCash=null;
	private Button buttonCut=null;
	private EditText mTextIp=null;
	private EditText mprintfData=null;
	private EditText mprintfLog=null;
	private Socketmanager mSockManager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		buttonCon=(Button)findViewById(R.id.conTest);
		buttonPf=(Button)findViewById(R.id.printf);
		buttonCash=(Button)findViewById(R.id.buttonCash);
		buttonCut=(Button)findViewById(R.id.buttonCut);
		mTextIp=(EditText)findViewById(R.id.printerIp);
		mprintfData=(EditText)findViewById(R.id.printfData);
		mprintfLog=(EditText)findViewById(R.id.printfLog);
		ButtonListener buttonListener=new ButtonListener();
		buttonCon.setOnClickListener(buttonListener);
		buttonPf.setOnClickListener(buttonListener);
		buttonCash.setOnClickListener(buttonListener);
		buttonCut.setOnClickListener(buttonListener);
		mSockManager=new Socketmanager(MainActivity.this);		
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	class ButtonListener implements OnClickListener{

		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.conTest:
				if (conTest(mTextIp.getText().toString())) {
					PrintfLog("���ӳɹ�...");
					buttonCon.setText("������...");
					buttonPf.setEnabled(true);
					buttonCash.setEnabled(true);
					buttonCut.setEnabled(true);
				}
				else {
					PrintfLog("����ʧ��...");
					buttonCon.setText("δ����...");
					buttonPf.setEnabled(false);
					buttonCash.setEnabled(false);
					buttonCut.setEnabled(false);
				}
				break;
			case R.id.printf:
				try {
					if (PrintfData((mprintfData.getText().toString()+"\nExample4WIFI-XPrinter\n").getBytes("GBK"))) {
						PrintfLog("��ӡ�ɹ�...");
					}
					else {
						PrintfLog("��ӡʧ��...");
						buttonPf.setEnabled(false);
					}					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					PrintfLog("���ݷ��ʹ���...");
				}
				break;
			case R.id.buttonCash:
				byte SendCash[]={0x1b,0x70,0x00,0x1e,(byte)0xff,0x00};
				if (PrintfData(SendCash)) {
					PrintfLog("�򿪳ɹ�...");
				}
				else {
					PrintfLog("��ʧ��...");
				}
				break;
			case R.id.buttonCut:
				byte SendCut[]={0x0a,0x0a,0x1d,0x56,0x01};	
				if (PrintfData(SendCut)) {
					PrintfLog("��ֽ�ɹ�...");
				}
				else {
					PrintfLog("��ֽʧ��...");
				}
				break;
			default:
				break;
			}
			
		}		
			}
	public boolean conTest(String printerIp) {
		mSockManager.mPort=9100;
		mSockManager.mstrIp=printerIp;
		mSockManager.threadconnect();
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mSockManager.getIstate()) {
			return true;
		}
		else {
			return false;
		}				
	}
	public void PrintfLog(String logString) {
		mprintfLog.setText(logString);
	}
	public boolean PrintfData(byte[]data) {
		mSockManager.threadconnectwrite(data);
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (mSockManager.getIstate()) {
			return true;
		}
		else {
			return false;
		}
	}

}
