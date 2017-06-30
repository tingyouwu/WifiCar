package com.wty.app.wificar.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TaskManager {
	private static TaskManager taskManager = null;
	
	private ExecutorService singleTaskPool = null;  //队列线程池，逐个完成
	private ExecutorService parallelTaskPool = null;//线程并列完成
	
	
	public static synchronized TaskManager getInstance(){
		if(taskManager==null){
			taskManager= new TaskManager();
		}
		return taskManager;
	}
	
	
	//队列线程池，只允许单个线程逐个逐个完成
	public ExecutorService getSingleTaskPool(){
		if(singleTaskPool==null){
			singleTaskPool = Executors.newSingleThreadExecutor();
		}
		return singleTaskPool;
	}
	
	
	//并行线程池，支持同时执行10条线程任务
	public ExecutorService getParallelTaskPool(){
		if(parallelTaskPool == null){
			parallelTaskPool = Executors.newFixedThreadPool(10);
		}
		return parallelTaskPool;
	}

    /**
     * 线程池关闭 -- 停止所有运行或待运行线程
     */
    public void shutdown(){
        if(singleTaskPool!=null){
            singleTaskPool.shutdownNow();
        }
        if(parallelTaskPool!=null){
            parallelTaskPool.shutdownNow();
        }
        parallelTaskPool = null;
        singleTaskPool = null;
    }
}
