package tw.com.lccnet.app.designateddriving.Utils;

import android.os.AsyncTask;

public class AsyncTaskUtils {

    public static <T> void doAsync(final IDataCallBack<T> callBack) {
        new AsyncTask<Void, Void, T>() {
            /*
            @Override
            protected void onPreExecute() {
                callBack.onTaskBefore();
            }
*/
            @Override
            protected T doInBackground(Void... voids) {
                return callBack.onTasking(voids);
            }

            @Override
            protected void onPostExecute(T t) {
                callBack.onTaskAfter(t);
            }
        }.execute();
    }
}
