package com.lchtime.safetyexpress.proxy;

/**
 * Created by Dreamer on 2017/3/2.
 */

public class ThreadPoolFactory {

    static ThreadPoolProxy mNorMalThreadPoolProxy;
    static ThreadPoolProxy mDownLoadThreadPoolProxy;

    public static ThreadPoolProxy getNormalThreadPoolProxy(){
        if (mNorMalThreadPoolProxy == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mNorMalThreadPoolProxy == null) {
                    mNorMalThreadPoolProxy = new ThreadPoolProxy(5, 5);
                }
            }
        }
        return mNorMalThreadPoolProxy;
    }
    public static ThreadPoolProxy getDownLoadThreadPoolProxy(){
        if (mDownLoadThreadPoolProxy == null) {
            synchronized (ThreadPoolFactory.class) {
                if (mDownLoadThreadPoolProxy == null) {
                    mDownLoadThreadPoolProxy = new ThreadPoolProxy(3, 3);
                }
            }
        }
        return mDownLoadThreadPoolProxy;
    }
}
