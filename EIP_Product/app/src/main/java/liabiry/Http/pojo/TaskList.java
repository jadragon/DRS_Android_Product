package liabiry.Http.pojo;

import java.util.List;

public class TaskList {

    private List<TaskInfo> taskInfos;
    private long contentLen;//文件总长度
    /**
     * 迄今为止java虚拟机都是以32位作为原子操作，而long与double为64位，当某线程
     * 将long/double类型变量读到寄存器时需要两次32位的操作，如果在第一次32位操作
     * 时变量值改变，其结果会发生错误，简而言之，long/double是非线程安全的，volatile
     * 关键字修饰的long/double的get/set方法具有原子性。
     */
    private volatile long completedLen;//已完成长度

    public TaskList(List<TaskInfo> taskInfos, long contentLen, long completedLen) {
        this.taskInfos = taskInfos;
        this.contentLen = contentLen;
        this.completedLen = completedLen;
    }

    public List<TaskInfo> getTaskInfos() {
        return taskInfos;
    }

    public void setTaskInfos(List<TaskInfo> taskInfos) {
        this.taskInfos = taskInfos;
    }

    public long getContentLen() {
        return contentLen;
    }

    public void setContentLen(long contentLen) {
        this.contentLen = contentLen;
    }

    public long getCompletedLen() {
        return completedLen;
    }

    public void setCompletedLen(long completedLen) {
        this.completedLen = completedLen;
    }
}
