package com.ssreader.novel.ui.read.dialog;

import android.os.Looper;

/**
 * 倒计时任务管理类<p>
 * Created by  on 2018/7/17.
 */
public class ProgressTaskManager {

    private ProgressTaskQueue mTaskQueue = null;
    private static ProgressTaskManager mTaskManager;

    public static ProgressTaskManager getInstance() {
        if (mTaskManager == null) {
            mTaskManager = new ProgressTaskManager();
        }
        return mTaskManager;
    }

    public ProgressTaskManager() {
        mTaskQueue = new ProgressTaskQueue();
        new CountDownThread().start();
    }

    /**
     * 添加任务
     *
     * @param task
     */
    public void addQueueTask(ProgressTask task) {
        mTaskQueue.put(task);
    }

    /**
     * 队列终止
     */
    public void onDestroy() {
        if (mTaskQueue != null) {
            mTaskQueue.quit();
        }
        mTaskManager = null;
    }

    class CountDownThread extends Thread {

        @Override
        public void run() {
            if (Looper.myLooper() == null) {
                Looper.prepare();
            }
            try {
                for (; ; ) {
                    if (mTaskQueue.mQuiting) {
                        mTaskQueue.clear();
                        mTaskQueue = null;
                        return;
                    }
                    ProgressTask task = mTaskQueue.next();
                    if (task == null) {
                        synchronized (mTaskQueue) {
                            try {
                                mTaskQueue.wait();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        continue;
                    }
                    task.run();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (mTaskQueue != null && !mTaskQueue.mQuiting) {
                    new CountDownThread().start();
                }
            }
        }
    }
}
