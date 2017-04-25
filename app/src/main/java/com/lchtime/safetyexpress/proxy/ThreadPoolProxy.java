package com.lchtime.safetyexpress.proxy;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Dreamer on 2017/3/2.
 */

public class ThreadPoolProxy {


    private int mCorePoolSize;
    private int mMaximumPoolSize;

    private ThreadPoolExecutor mExecutor;


    public ThreadPoolProxy(int corePoolSize, int maximumPoolSize) {
        mCorePoolSize = corePoolSize;
        mMaximumPoolSize = maximumPoolSize;
    }

    private ThreadPoolExecutor initThreadPoolExcutor() {
        if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()){
            synchronized (ThreadPoolProxy.class) {
                if (mExecutor == null || mExecutor.isShutdown() || mExecutor.isTerminated()){
                    long keepAliveTime = 0;
                    TimeUnit unit = TimeUnit.MILLISECONDS;
                    BlockingQueue<Runnable> workQueue = new LinkedBlockingDeque<>();
                    ThreadFactory threadFactory = Executors.defaultThreadFactory();
                    RejectedExecutionHandler handler = new ThreadPoolExecutor.DiscardPolicy();
                    mExecutor = new ThreadPoolExecutor(mCorePoolSize, mMaximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
                }
            }
        }
        return mExecutor;
    }

    public void submit(Runnable task){
        mExecutor = initThreadPoolExcutor();
        mExecutor.submit(task);
    }

    public void execute(Runnable task){
        mExecutor = initThreadPoolExcutor();
        mExecutor.execute(task);
    }

    public void remove(Runnable task){
        mExecutor = initThreadPoolExcutor();
        mExecutor.remove(task);
    }

}
