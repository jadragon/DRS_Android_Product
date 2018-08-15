package net.xprinter.example4wifi;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import net.xprinter.example4wifi.Utils.XprinterCodeUtilByte;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetSocketAddress;
import java.net.Socket;

import static net.xprinter.example4wifi.Utils.XprinterCodeUtilByte.barcode_height;
import static net.xprinter.example4wifi.Utils.XprinterCodeUtilByte.feedpapercut;
import static net.xprinter.example4wifi.Utils.XprinterCodeUtilByte.justification_center;
import static net.xprinter.example4wifi.Utils.XprinterCodeUtilByte.printBarcode;
import static net.xprinter.example4wifi.Utils.XprinterCodeUtilByte.set_bar_code_width;

public class MainActivity extends Activity {
    private Button test_btn = null;
    private EditText test_edit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_layout);
        test_btn = (Button) findViewById(R.id.test_btn);
        test_edit = (EditText) findViewById(R.id.test_edit);
        test_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                           main();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            }
        });

    }


    public void main() throws IOException {
        String ip = test_edit.getText().toString();
        int port = 9100;
        String code = "1234567890";
        int skip = 2;
        Socket client = new java.net.Socket();
        OutputStreamWriter outSW = null;
        client.connect(new InetSocketAddress(ip, port), 10000); // 创建一个 socket
//        socketWriter = new PrintWriter(client.getOutputStream());// 创建输入输出数据流
        outSW = new OutputStreamWriter(client.getOutputStream(), "GBK");
        BufferedWriter socketWriter = new BufferedWriter(outSW);
/*
        socketWriter.write(new String(justification_center(), "UTF-8"));
        socketWriter.write("***测试***\n");
        socketWriter.write("快来买吧\n");
        socketWriter.write(new String(justification_left(), "UTF-8"));
        socketWriter.write("订单号：170426543103\n");
        socketWriter.write("哈哈哈\n");
        socketWriter.write("-------------------------------------------\n");
        socketWriter.write(new String(emphasized_on(), "UTF-8"));
        socketWriter.write(new String(double_height_width_on(), "UTF-8"));
        socketWriter.write("367#085_1300\n");
        socketWriter.write(new String(double_height_width_off(), "UTF-8"));
        socketWriter.write(new String(emphasized_off(), "UTF-8"));
        socketWriter.write("测试人：王大帅哥\n");
        socketWriter.write("手机：13052235269\n");
        socketWriter.write(new String(emphasized_on(), "UTF-8"));
        socketWriter.write(new String(set_chinese_super_on(), "UTF-8"));
        socketWriter.write(new String(double_height_width_on(), "UTF-8"));
        socketWriter.write("你说我是不是帅哥\n");
        socketWriter.write(new String(double_height_width_off(), "UTF-8"));
        socketWriter.write(new String(set_chinese_super_off(), "UTF-8"));
        socketWriter.write(new String(emphasized_off(), "UTF-8"));
          socketWriter.write("\n\n");
        socketWriter.write(new String(set_bar_code_width(2), "UTF-8"));
        socketWriter.write(new String(print_bar_code128(BarCode.CODE128, "{Bcb7099132890012345"), "UTF-8"));
        socketWriter.write("\n");
        socketWriter.write("cb7099132890012345");

        for (int i = 0; i < skip; i++) {
            socketWriter.write("\n");
        }
        socketWriter.write(new String(feedpapercut(), "UTF-8"));
        socketWriter.flush();
        socketWriter.close();
*/
        socketWriter.write(new String(justification_center(), "UTF-8"));
        socketWriter.write("商品名稱:綠藻碇\n");
        socketWriter.write("規格:50入\n");
        socketWriter.write("vt-525897\n");
        socketWriter.write(new String(set_bar_code_width(2), "UTF-8"));
        socketWriter.write(new String(barcode_height((byte) 80), "UTF-8"));
        socketWriter.write(new String(printBarcode(XprinterCodeUtilByte.BarCode.CODE39,"1234567891012"), "UTF-8"));

        socketWriter.write("1234567891012");
        for (int i = 0; i < skip; i++) {
            socketWriter.write("\n");
        }
        socketWriter.write(new String(feedpapercut(), "UTF-8"));
        socketWriter.flush();
        socketWriter.close();

    }

}
