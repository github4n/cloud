package com.iot.center.utils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ThreadUtil implements Runnable{

	private static final Logger log = LoggerFactory.getLogger(ThreadUtil.class);
	// 调用的class  
    private Class<?> className;  
      
    // 处理的方法名  
    private String methodName;  
      
    // 回调参数类型  
    private Class<?>[] paramtypes;  
    
    // 回调参数  
    private Object[] params;  
      
    /** 
     * 线程的构造方法 
     *  
     * @author zhy 
     * @param className 调用的类class 
     * @param methodName 回调的方法名 
     * @param params 回调传递的参数 
     */  
    public ThreadUtil(Class<?> className, String methodName,Class<?>[] paramtypes, Object[] params) {  
        this.className = className;  
        this.methodName = methodName;  
        this.paramtypes=paramtypes;
        this.params = params;  
    }  
    
	@Override
	public void run() {
		Method method = null;  
        try {  
        	invokeMethod(className,methodName,paramtypes,params);  
        } catch (NoSuchMethodException e) {  
            log.error("找不到该方法：" + e.getMessage(), e);  
        } catch (SecurityException e) {  
            log.error("安全出现异常：" + e.getMessage(), e);  
        } catch (IllegalAccessException e) {  
            log.error("非法进入异常：" + e.getMessage(), e);  
        } catch (IllegalArgumentException e) {  
            log.error("非法争议异常：" + e.getMessage(), e);  
        } catch (InvocationTargetException e) {  
            log.error("调用目标异常：" + e.getMessage(), e);  
        } catch (InstantiationException e) {  
            log.error("实例化异常：" + e.getMessage(), e);  
        } catch (Exception e) {
			e.printStackTrace();
		}  
	}

	
	private void invokeMethod(Class<?> clazz,String methodName,Class[] parameterClasses,Object[] methodParameters)
			throws Exception{  
			Method method = clazz.getMethod(methodName, parameterClasses);  
			method.invoke(clazz.newInstance(), methodParameters); 
    }  
	      
	 /**
	  * 调用示例
	  * @param args
	  */
    public static void main(String[] args) {  
//        ThreadUtil threadUtil = new ThreadUtil(Test.class, "click", new Class[]{String.class,int.class,List.class},new Object[]{"mmm",1,strList});  
//        Thread thread = new Thread(threadUtil);  
//        thread.start();  
    }  
}
