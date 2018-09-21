package teprinciple.yang.list2excel;

/**
 * Created by Administrator on 2017/4/13.
 */

public class Student {

    String classes;
    String process;
    String staff;
    String id;
    int number;
    String date;
    String time;

    public Student(String classes, String process, String staff, String id, int number, String date, String time) {
        this.classes = classes;
        this.process = process;
        this.staff = staff;
        this.id = id;
        this.number = number;
        this.date = date;
        this.time = time;
    }
}
