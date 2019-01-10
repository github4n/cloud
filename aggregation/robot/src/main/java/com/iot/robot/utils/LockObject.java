package com.iot.robot.utils;

/**
 * @Descrpiton: 锁对象
 * @Author: yuChangXing
 * @Date: 2018/10/8 17:45
 * @Modify by:
 */
public class LockObject {
    private String id;
    private long startTime;

    public LockObject(String id) {
        this.id = id;
        this.startTime = System.currentTimeMillis();
    }

    public String getId() {
        return id;
    }

    public String costTime() {
        long endTime = System.currentTimeMillis();
        long subtract = endTime - startTime;
        double second = (subtract*1.0)/(1000);
        return String.format("%.2f", second) + "s";
    }

    /*public static void main(String[] args) throws InterruptedException {
        LockObject lc = new LockObject("111");

        Thread.sleep(5036L);

        System.out.println(lc.costTime());
    }*/
}
