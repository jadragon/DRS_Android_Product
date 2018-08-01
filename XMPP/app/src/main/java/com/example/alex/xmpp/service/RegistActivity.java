package com.example.alex.xmpp.service;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.alex.xmpp.R;

import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Registration;

public class RegistActivity extends AppCompatActivity {
    EditText rg_account, rg_password;
    Button rg_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        rg_account = findViewById(R.id.rg_account);
        rg_password = findViewById(R.id.rg_password);
        rg_button = findViewById(R.id.rg_button);
        rg_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Registration reg = new Registration();
//设置类型
                reg.setType(IQ.Type.SET);
//发送到服务器
                reg.setTo(IMService.connection.getServiceName());
//设置用户名
                reg.setUsername(rg_account.getText().toString());
//设置密码
                reg.setPassword(rg_password.getText().toString());
/*
//设置其余属性 不填可能会报500异常 连接不到服务器 asmack一个Bug
//设置昵称（其余属性）
        reg.addAttribute("name", name);
//设置邮箱（其余属性）
        reg.addAttribute("email", email);
//设置android端注册
        reg.addAttribute("android", "geolo_createUser_android");
        */

//创建包过滤器
                PacketFilter filter = new AndFilter(new PacketIDFilter(reg
                        .getPacketID()), new PacketTypeFilter(IQ.class));
//创建包收集器
                PacketCollector collector = IMService.connection
                        .createPacketCollector(filter);
//发送包
                IMService.connection.sendPacket(reg);
//获取返回信息
                IQ result = (IQ) collector.nextResult(SmackConfiguration
                        .getPacketReplyTimeout());
// 停止请求results（是否成功的结果）
                collector.cancel();
//通过返回信息判断
                if (result == null) {   //无返回，连接不到服务器
                } else if (result.getType() == IQ.Type.ERROR) {     //错误状态
                    if (result.getError().toString()
                            .equalsIgnoreCase("conflict(409)")) {   //账户存在 409判断
                    } else {
                    }
                } else if (result.getType() == IQ.Type.RESULT) {//注册成功跳转登录

                }
            }
        });


    }
}
