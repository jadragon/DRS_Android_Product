package Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class FileUtils {

    private String SDPATH;

    public String getSDPATH() {
        return SDPATH;
    }

    public FileUtils() {
        //得到當前SDCARD存儲設備的目錄 /SDCARD, Environment.getExternalStorageDirectory()這個方法比較通用
        SDPATH = Environment.getExternalStorageDirectory() + "/";
    }

    public FileUtils(String directory) {
        //得到當前SDCARD存儲設備的目錄 /SDCARD, Environment.getExternalStorageDirectory()這個方法比較通用
        SDPATH = directory + "/";
    }

    /**
     * 在SD卡上創建文件
     */
    public File creatSDFile(String fileName) throws IOException {
        File file = new File(SDPATH + fileName);
        file.createNewFile();
        return file;
    }

    /**
     * 在SD卡上創建目錄
     */
    public File creatSDDir(String dirName) {
        File dir = new File(SDPATH + dirName);
        dir.mkdir();
        SDPATH = SDPATH + dirName + "/";
        return dir;
    }

    /**
     * 判斷SD卡上的文件夾是否存在
     */
    public boolean isFileExist(String fileName, int download_lenth) {
        File file = new File(SDPATH + fileName);
        return file.exists() && file.length() == download_lenth;
    }

    /**
     * 判斷SD卡上的文件夾是否存在且刪除
     */
    public boolean isFileExistDelete(String fileName) {
        File file = new File(SDPATH + fileName);
        return file.delete();
    }

    /**
     * 將一個InputStream裡面的數據寫入到SD卡中
     */
    public File write2SDFromInput(String path, String fileName, InputStream input) {
        File file = null;
        OutputStream output = null;
        try
        //InputStream裡面的數據寫入到SD卡中的固定方法
        {
            creatSDDir(path);
            file = creatSDFile(path + fileName);
            output = new FileOutputStream(file);
            byte buffer[] = new byte[4 * 1024];
            while ((input.read(buffer)) != -1) {
                output.write(buffer);
            }
            output.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                output.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return file;
    }
}