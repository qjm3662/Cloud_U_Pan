package com.qjm3662.cloud_u_pan.WifiDirect.Socket;

import java.util.LinkedList;

/**
 * Created by qjm3662 on 2016/11/10 0010.
 */

public class ThreadPool extends ThreadGroup{
    private boolean isClosed = false;   //线程池是否关闭
    private LinkedList<Runnable> workQueue;   //表示工作队列
    private static int threadPoolID = 0;     //表示线程池ID
    private int threadID;    //表示工作线程ID

    public ThreadPool(int poolSize) {//poolSize指定线程池中的工作线程数目
        super("ThreadPool-" + (threadPoolID++));
        isClosed = false;
        setDaemon(true);
        workQueue = new LinkedList<Runnable>();         //创建工作队列
        for(int i = 0; i < poolSize; i++){
            new WorkThread().start();                   //创建并启动工作线程
        }
    }

    public synchronized void execute(Runnable task){
        if(isClosed){       //线程池关闭则抛出IllegalStateException异常
            throw new IllegalStateException();
        }
        if(task != null){
            workQueue.add(task);
            notify();       //唤醒正在getTask()方法中等待任务的工作线程
        }
    }

    /**
     * 从工作线程中取出一个任务，工作线程会调用此方法
     */
    protected synchronized Runnable getTask() throws InterruptedException{
        while(workQueue.size() == 0){
            if(isClosed)
                return null;
            wait();                       //如果工作队列中没有任务，就等待任务
        }
        return workQueue.removeFirst();
    }


    /**
     * 关闭线程池
     */
    public synchronized void close() {
        if(!isClosed) {
            isClosed = true;
            workQueue.clear();     //清空工作队列
            interrupt();           //中断所有的工作线程，该方法继承自ThreadGroup类
        }
    }


    public void join() {
        synchronized(this) {
            isClosed = true;
            notifyAll();        //唤醒还在getTask()方法中等待任务的工作线程
        }
        Thread[] threads = new Thread[activeCount()];       //activeCount()返回当前线程组中处于激活状态的线程数目

        int count = enumerate(threads);           //enumerate方法继承自ThreadGroup类，获得线程组中当前所有活着的工作线程
        for(int i = 0; i < count; i++) {           //等待所有工作线程运行结束
            try {
                threads[i].join();               //等待工作线程运行结束
            } catch(InterruptedException e) {

            }
        }
    }


    private class WorkThread extends Thread {
        public WorkThread() {
            super(ThreadPool.this, "WorkThread-" + (threadID++));
        }

        public void run() {
            while(!isInterrupted()) {       //isInterrupted()方法继承自Thread类，判断线程是否被中断
                Runnable task = null;
                try {
                    task = getTask();        //取出任务
                } catch(InterruptedException e) {
//                    System.out.println("取出任务失败");
                }

                /*
                 * 如果getTask()返回null或者线程执行getTask()时被中断，则结束此线程
                 */
                if(task == null) return;

                try {
                    task.run();      //运行任务，异常在catch代码块中捕获
                } catch(Throwable t) {
                    t.printStackTrace();
                }
            }
        }
    }

}
