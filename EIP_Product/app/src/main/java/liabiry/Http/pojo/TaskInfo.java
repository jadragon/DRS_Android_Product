package liabiry.Http.pojo;

public class TaskInfo {
    private String fileName;//文件名
    private String filePath;//链接

    public TaskInfo() {
    }

    public TaskInfo(String fileName,  String filePath) {
        this.fileName = fileName;
        this.filePath = filePath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }


    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }
}
