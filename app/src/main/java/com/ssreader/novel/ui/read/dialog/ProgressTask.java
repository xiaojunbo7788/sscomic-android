package com.ssreader.novel.ui.read.dialog;

/**

 */
public abstract class ProgressTask implements Runnable {

    public ProgressTask() {

    }

    @Override
    public final void run() {
        doRun();
    }

    /**
     * 任务处理
     */
    public abstract void doRun();

}
