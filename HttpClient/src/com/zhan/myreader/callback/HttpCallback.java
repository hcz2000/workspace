package com.zhan.myreader.callback;


import java.io.InputStream;

/**
 * Created by zhao on 2016/4/16.
 */
public interface HttpCallback {
    void onFinish(InputStream in);
    void onError(Exception e);
}
