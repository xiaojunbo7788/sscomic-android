package com.ssreader.novel.ui.read.dialog;

import com.ssreader.novel.ui.read.dialog.ProgressTask;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 倒计时任务队列<p>
 * Created by  on 2018/7/17.
 */
class ProgressTaskQueue {

    public boolean mQuiting = false;
    private Queue<ProgressTask> mQueue = null;



    protected ProgressTaskQueue() {
        mQuiting = false;
        mQueue = new LinkedList<ProgressTask>();
    }

    /**
     * 加一个任务
     *
     * @param task
     */
    protected void put(ProgressTask task) {
        if (task == null) {
            return;
        }
        synchronized (this) {
            if (mQuiting) {
                return;
            }
            mQueue.add(task);
            notifyAll();
        }
    }

    /**
     * 取下一个任务
     *
     * @return
     */
    protected ProgressTask next() {
        synchronized (this) {
            if (mQueue.isEmpty()) {
                return null;
            }
            return mQueue.poll();
        }
    }

    /**
     * 清队列
     */
    protected void clear() {
        synchronized (this) {
            mQueue.clear();
        }
    }

    /**
     * 队列终止，不再接收新任务
     */
    protected final void quit() {
        synchronized (this) {
            if (mQuiting) {
                return;
            }
            mQuiting = true;
            notifyAll();
        }
    }
}
