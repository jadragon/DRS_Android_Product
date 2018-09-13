package Utils;

public interface IDataCallBack<T> {
    /**
     * 任務執行前
     */
    void onTaskBefore();

    /**
     * 任務執行中
     */
    T onTasking(Void... params);

    /**
     * 任務執行後
     */
    void onTaskAfter(T t);
}
