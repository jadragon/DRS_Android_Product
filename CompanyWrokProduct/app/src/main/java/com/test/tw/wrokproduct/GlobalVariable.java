package com.test.tw.wrokproduct;

import android.app.Application;

public class GlobalVariable extends Application {
    private String token;     //User token

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

}