package library;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

/**
 * Created by user on 2017/7/20.
 */

public class MySingleton {
    private static MySingleton mInstance;
    private RequestQueue requestQueue;
    private static Context mCtxt;

    public MySingleton(Context context){
        mCtxt=context;
        requestQueue=getRequestQueue();
    }

    private RequestQueue getRequestQueue() {

        if(requestQueue==null){
            requestQueue= Volley.newRequestQueue(mCtxt.getApplicationContext());
        }
        return requestQueue;
    }


    public static synchronized  MySingleton getInstance(Context context){
        if(mInstance==null){
            mInstance=new MySingleton(context);
        }
        return mInstance;
    }


    public<T> void addToRequestQue(Request<T> request){

        getRequestQueue().add(request);
    }
}
