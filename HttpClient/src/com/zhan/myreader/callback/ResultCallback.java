package com.zhan.myreader.callback;


/**
 * Created by zhao on 2016/10/25.
 */

public interface ResultCallback {

    void onFinish(Object o, int code);

    default void onError(Exception e){ e.printStackTrace();};

}
