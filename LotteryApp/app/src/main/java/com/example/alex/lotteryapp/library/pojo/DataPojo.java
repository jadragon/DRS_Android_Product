package com.example.alex.lotteryapp.library.pojo;

/**
 * Created by Administrator on 2017/4/13.
 */

public class DataPojo {

    public String classes;
    public String process;
    public String staff;
    public String id;
    public int number;
    public String date;
    public String time;
    public boolean select;
    public DataPojo() {
    }

    public DataPojo(String classes, String process, String staff, String id, int number, String date, String time) {
        this.classes = classes;
        this.process = process;
        this.staff = staff;
        this.id = id;
        this.number = number;
        this.date = date;
        this.time = time;
    }
}
