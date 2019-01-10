package com.iot.video.queue;

import com.iot.common.util.JsonUtil;
import com.iot.video.util.UUIDUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 项目名称：cloud
 * 功能描述：使用ArrayList的实现队列功能
 * 创建人： yeshiyuan & shengxiang
 * 创建时间：2018/8/3 11:45
 * 修改人： yeshiyuan & shengxiang
 * 修改时间：2018/8/3 11:45
 * 修改描述：
 */
public class ArrayListBlockingQueue<T> implements IListBlockingQueue<T> {

    private static Logger logger = LoggerFactory.getLogger(ArrayListBlockingQueue.class);

    /**
     * 最大数据组个数
     */
    private Integer maxGroupSize = 50;

    /**
     * 单一集合最大长度
     */
    private Integer maxSingleSize = 50;

    /**
     * 队列名称
     */
    private String queueName;

    /**
     * 存储原始数据的集合
     */
    private ArrayList<T> arrayList = null;

    /**
     * 数据组集合
     */
    private ArrayList<ArrayList<T>>  groupList = null;

    /**
     * 锁
     */
    private ReentrantLock lock = new ReentrantLock();
    private Condition readCondition = lock.newCondition();
    private Condition writeCondition = lock.newCondition();

    public ArrayListBlockingQueue() {
        arrayList = new ArrayList<>(maxSingleSize);
        groupList = new ArrayList<>(maxGroupSize);
    }

    /**
     * 创建阻塞队列
     * @param maxSingleSize 每组数据包含原始数据最大条数
     * @param maxGroupSize 该阻塞队列最大包含的组数，存储的数据超过该值，会一直阻塞，直到有数据被取出
     */
    public ArrayListBlockingQueue(Integer maxSingleSize, Integer maxGroupSize, String queueName) {
        if (maxSingleSize <1 || maxGroupSize<1)
            throw new IllegalArgumentException("the two params must be greater than or equal to 1.");
        this.maxGroupSize = maxGroupSize;
        this.maxSingleSize = maxSingleSize;
        arrayList = new ArrayList<>(maxSingleSize);
        groupList = new ArrayList<>(maxGroupSize);
        this.queueName = queueName;
    }

    /**
     * 添加数据到缓存，存储的数据超一定值，该方法会一直阻塞，直到有数据被取出才返回
     * @param o
     */
    @Override
    public void add(T o) {
        if (o==null)
            return ;
        lock.lock();
        try {
            while (groupList.size() >= maxGroupSize){
                try {
                    logger.info("the {} is full, please to consumer", queueName);
                    writeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            arrayList.add(o);
            if (arrayList.size() >= maxSingleSize){
                groupList.add(arrayList);
                arrayList = new ArrayList<>(maxSingleSize);
            }
            readCondition.signal();
        }finally {
            lock.unlock();
        }
    }

    /**
     * 批量添加数据到缓存，存储的数据超一定值，该方法会一直阻塞，直到有数据被取出才返回
     * @param os
     */
    @Override
    public void add(List os) {
        if (os == null || os.isEmpty()){
            return;
        }
        lock.lock();
        try {
            while (groupList.size() >= maxGroupSize){
                try {
                    writeCondition.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            arrayList.addAll(os);
            if (arrayList.size() >= maxSingleSize){
                List<ArrayList<T>> subss = subList(arrayList);
                subss.forEach( e-> groupList.add(e));
                arrayList.clear();
            }
            readCondition.signal();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isEmpty() {
        lock.lock();
        try {
            return groupList.isEmpty() && arrayList.isEmpty();
        }finally {
            lock.unlock();
        }
    }

    @Override
    public boolean isFull() {
        lock.lock();
        try {
            return groupList.size() >= maxGroupSize;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取并删除数据
     * @param mills 最长等待时间，单位：ms
     * @return
     */
    @Override
    public List<T> poll(long mills) {
        lock.lock();
        try {
            Long start = System.currentTimeMillis();
            while (mills >= System.currentTimeMillis() - start){
                if (groupList.isEmpty() && arrayList.isEmpty()){
                    try {
                        readCondition.await(10, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!groupList.isEmpty()){
                    ArrayList<T> result= groupList.remove(0);
                    writeCondition.signal();
                    return result;
                }
                if (!arrayList.isEmpty()){
                    ArrayList<T> result = arrayList;
                    arrayList = new ArrayList<>(maxSingleSize);
                    writeCondition.signal();
                    return result;
                }
            }
        }finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public List<T> poll() {
        lock.lock();
        try {
            while (true){
                if (groupList.isEmpty() && arrayList.isEmpty()){
                    try {
                        readCondition.await();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if (!groupList.isEmpty()){
                    ArrayList<T> result= groupList.remove(0);
                    writeCondition.signal();
                    return result;
                }
                if (!arrayList.isEmpty()){
                    ArrayList<T> result = arrayList;
                    arrayList = new ArrayList<>(maxSingleSize);
                    writeCondition.signal();
                    return result;
                }
            }
        }finally {
            lock.unlock();
        }
    }

    /**
     * 获取缓冲区中所有元素
     * @return
     */
    @Override
    public List getAll() {
        lock.lock();
        try {
            ArrayList<T> allList=new ArrayList<T>();
            allList.addAll(arrayList);
            for (ArrayList<T> l : groupList){
                allList.addAll(l);
            }
            return allList;
        }finally {
            lock.unlock();
        }
    }

    /**
     * 删除缓冲区中所有元素
     * @return
     */
    @Override
    public List<T> removeAll() {
        lock.lock();
        try {
            List<T> list = new ArrayList<>();
            groupList.forEach(o -> {
                list.addAll(o);
            });
            list.addAll(arrayList);
            arrayList.clear();
            groupList.clear();
            writeCondition.signalAll();
            return list;
        }finally {
            lock.unlock();
        }
    }

    @Override
    public long getSizeInByte() {
        int size = 0;
        ByteArrayOutputStream outstr = null;
        ObjectOutputStream objstr = null;
        lock.lock();
        try{
            outstr = new ByteArrayOutputStream();
            objstr = new ObjectOutputStream(outstr);
            objstr.writeObject(groupList);
            objstr.writeObject(arrayList);
            size = outstr.size();
        }catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                if (objstr != null){
                    objstr.close();
                }
                if (outstr != null){
                    outstr.close();
                }
            }catch (IOException e){
                e.printStackTrace();
            }
            lock.unlock();
        }
        return size;
    }

    @Override
    public void clear() {
        lock.lock();
        try{
            arrayList.clear();
            for (ArrayList<T> l:groupList){
                l.clear();
            }
            groupList.clear();
            writeCondition.signalAll();
        }finally {
            lock.unlock();
        }
    }

    /*-------------------------------- get set method-----------------------------------------*/

    public Integer getMaxGroupSize() {
        return maxGroupSize;
    }

    public void setMaxGroupSize(Integer maxGroupSize) {
        this.maxGroupSize = maxGroupSize;
    }

    public Integer getMaxSingleSize() {
        return maxSingleSize;
    }

    public void setMaxSingleSize(Integer maxSingleSize) {
        this.maxSingleSize = maxSingleSize;
    }

    public ArrayList<T> getArrayList() {
        return arrayList;
    }

    public void setArrayList(ArrayList<T> arrayList) {
        this.arrayList = arrayList;
    }

    public ArrayList<ArrayList<T>> getGroupList() {
        return groupList;
    }

    public void setGroupList(ArrayList<ArrayList<T>> groupList) {
        this.groupList = groupList;
    }

    private List<ArrayList<T>> subList(List<T> os){
        int size = os.size();
        List<ArrayList<T>> ss = new ArrayList<>();
        if (size <= maxSingleSize){
            ss.add(new ArrayList<>(os));
            return ss;
        }
        int index = 0;
        while (index < size){
            int endIndex = Math.min(index + maxSingleSize, size);
            List<T> subs = os.subList(index, endIndex);
            ss.add(new ArrayList<>(subs));
            index = endIndex;
        }
        return ss;
    }

    public static void main(String agrs[]){
        ArrayListBlockingQueue<String> queue = new ArrayListBlockingQueue<>(10, 50,"testQueue");
        AtomicInteger count=new AtomicInteger(0);
        //消费数据
        for (int i = 0; i < 10; i++){
            Thread t=new Thread(new Runnable(){
                public void run(){
                    for (int j = 0; j<100; j++){
                        queue.add(UUIDUtil.getUUID());
                        System.out.println("queue info : -> groupSize:" + queue.getGroupList().size() + "singleSize:" + queue.getArrayList().size());
                    }
                }
            });
            t.setName("writeThread"+i);
            t.start();
        }
        //消费数据
        for (int i = 0; i < 5; i++){
            Thread t=new Thread(new Runnable(){
                public void run(){
                    while (true){
                        List<String> list=queue.poll(1000);
                        if (list!=null&&!list.isEmpty()){
                            count.addAndGet(list.size());
                            System.out.println("thread="+Thread.currentThread().getName()+", totalcount="+count+", count="+list.size() + "\ndata: ->" + JsonUtil.toJson(list));
                        }
                    }
                }
            });
            t.setName("readThread"+i);
            t.start();
        }
    }

}
