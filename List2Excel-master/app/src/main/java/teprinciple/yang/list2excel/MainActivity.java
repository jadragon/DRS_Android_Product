package teprinciple.yang.list2excel;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.shidian.excel.ExcelUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE = 0x01;
    private ArrayList<ArrayList<String>> recordList;
    private List<Student> students;
    private static String[] title = {"类别", "工序", "人员", "ID号", "", "日期", "时间"};
    private File file;
    private String fileName;
    private Calendar calendar;
    private static final byte YEAR = 0;
    private static final byte MONTH = 1;
    private static final byte DAY_OF_MONTH = 2;
    private static final byte HOUR_OF_DAY = 3;
    private static final byte MINUTE = 4;
    private static final byte SECOND = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
// Android 6.0相机动态权限检查
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {
            initData();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, REQUEST_WRITE_EXTERNAL_STORAGE);
        }


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            // 相机权限
            case REQUEST_WRITE_EXTERNAL_STORAGE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    initData();
                }
                break;
        }
    }
    private void initData() {
        calendar = Calendar.getInstance();
        //模拟数据集合
        students = new ArrayList<>();
        for (int i = 1; i <= 10; i++) {
            students.add(new Student("入场", "C102", "15010000" + i, "ESBC-RR05-A0001" + i, 1, getData(YEAR) + "/" + getData(MONTH) + "/" + getData(DAY_OF_MONTH), getData(HOUR_OF_DAY) + ":" + getData(MINUTE) + ":" + getData(SECOND)));
        }
    }

    public int getData(byte type) {
        switch (type) {
            //获取系统的日期
            case YEAR:
                //年
                return calendar.get(Calendar.YEAR);
            case MONTH:
                //月
                return calendar.get(Calendar.MONTH) + 1;
            case DAY_OF_MONTH:
                //日
                return calendar.get(Calendar.DAY_OF_MONTH);

            //获取系统时间
            case HOUR_OF_DAY:
                //小时
                return calendar.get(Calendar.HOUR_OF_DAY);

            case MINUTE:
                //分钟
                return calendar.get(Calendar.MINUTE);

            case SECOND:
                //秒
                return calendar.get(Calendar.SECOND);
            default:
                return 0;

        }


    }

    /**
     * 导出excel
     *
     * @param view
     */
    public void exportExcel(View view) {
        String filetitle = "入场-C102-15010000-20180610";
        file = new File(getSDPath() + "/Record");
        makeDir(file);
        ExcelUtils.initExcel(file.toString() + "/" + filetitle + ".xls", title);
        fileName = getSDPath() + "/Record" + "/" + filetitle + ".xls";
        ExcelUtils.writeObjListToExcel(getRecordData(), fileName, this);
    }

    /**
     * 将数据集合 转化成ArrayList<ArrayList<String>>
     *
     * @return
     */
    private ArrayList<ArrayList<String>> getRecordData() {
        recordList = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            Student student = students.get(i);
            ArrayList<String> beanList = new ArrayList<String>();
            beanList.add(student.classes);
            beanList.add(student.process);
            beanList.add(student.staff);
            beanList.add(student.id);
            beanList.add(student.number + "");
            beanList.add(student.date);
            beanList.add(student.time);
            recordList.add(beanList);
        }
        return recordList;
    }

    private String getSDPath() {
        File sdDir = null;
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();
        }
        String dir = sdDir.toString();
        return dir;
    }

    public void makeDir(File dir) {
        if (!dir.getParentFile().exists()) {
            makeDir(dir.getParentFile());
        }
        dir.mkdir();
    }
}
