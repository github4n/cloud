package com.iot.shcs.common.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {
	
	private ThreadPoolUtil() {}  
	
	private static ExecutorService pool = null;
	
	public static ExecutorService instance(){
		pool = instance(5);
		
		return pool;
	}
	
	public static ExecutorService instance(int poolSize){
		if(pool==null){
			pool = Executors.newFixedThreadPool(poolSize);
		}
		
		return pool;
	}
	
}
