package com.iot.design.optical.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.common.util.SpringUtil;
import com.iot.design.optical.vo.LightSource;
import com.iot.design.optical.constants.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * 坐标帮助类
 *
 * @author fenglijian
 */
@Component
public class CoordinateHelper {
    Logger logger = LoggerFactory.getLogger(CoordinateHelper.class);

    private Integer secondThreadCount = 10;

    /**
     * 构建各个点坐标（List存储坐标）
     * 根据长宽高分隔 不同的情况下 x y坐标不一样
     *
     * @param datax       空间数据1
     * @param datay       空间数据2
     * @param offset      偏移量
     * @param unitDatax   切割单位数据1
     * @param unitDatay   切割单位数据2
     * @param offsetDatax 切割偏移数据1
     * @param offsetDatay 切割偏移数据2
     * @param position    位置
     * @return
     */
    public List<Float[]> getCoordinatesList(Float datax, Float datay, Float offset,
                                            Float unitDatax, Float unitDatay,
                                            Float offsetDatax, Float offsetDatay, String position) {

        //是不是可以整除
        boolean hasLeave = datax % unitDatax != 0;
        //Math.floor 想下取整  得到单位长度
        Integer blockx = (int) (hasLeave ? Math.floor(datax / unitDatax) : datax / unitDatax);
        Integer blocky = (int) (hasLeave ? Math.floor(datay / unitDatay) : datay / unitDatay);

        List<Float[]> coordinatesList = Lists.newArrayList();
        for (int i = 0; i < blockx; i++) {
            //取x坐标
            float coordinatex = i * unitDatax + offsetDatax;
            for (int j = 0; j < blocky; j++) {
                //取y坐标
                float coordinatey = j * unitDatay + offsetDatay;
                Float[] wholeCoordinate = getWholeCoordinate(coordinatex, coordinatey, offset, position);
                coordinatesList.add(wholeCoordinate);
            }
        }
        return coordinatesList;
    }

    public static void main(String[] args) {
        System.out.println(10.0f % 0.2f);
    }

    /**
     * 获取照度(List)
     *
     * @param coordinatesList      空间某个面坐标集合
     * @param lightSources         光源坐标
     * @param refractivity         折射率
     * @param fileId               文件Id
     * @param unitLenth            网格长度
     * @param unitWidth            网格宽度
     * @param tableCoordinatesList 桌面的坐标集合
     * @param tableCenters         桌面中心坐标
     * @param tables               桌面中长宽高
     * @return
     */
    @Resource(name = "computeFirstThreadPool")
    private ThreadPoolTaskExecutor computeFirstThreadPool;

    public List<Float> getIlluminationList(List<Float[]> coordinatesList, List<LightSource> lightSources,
                                           Float refractivity, String fileId, Float unitLenth, Float unitWidth, List<Float[]> tableCoordinatesList, List<List<Float>> tableCenters, List<List<Float>> tables) {
        logger.info("-----------计算首次光照度开始----------");
        Long startTime = System.currentTimeMillis();
        List<Float> returnIlluminationsList = Lists.newArrayList();
        if (coordinatesList != null && coordinatesList.size() > 0) {
            boolean allOver = false;
            List<Future<List<Float>>> illuminationsListTemps = new ArrayList<>();
            //多线程计算每个灯 然后叠加
            for (int i = 0; i < lightSources.size(); i++) {
                logger.info("----------总共" + lightSources.size() + "盏灯,并行计算第" + (i + 1) + "盏灯的光照度----------");
                LightSource lightSource = lightSources.get(i);
                if (tableCoordinatesList != null && tableCoordinatesList.size() > 0) {
                    //房间存在桌子
                    //判断线程池是否满了 满了就等待
                    isFull(computeFirstThreadPool);
                    //多线程并行计算每个灯的光照度
                    Future<List<Float>> illuminationsListTemp = SpringUtil.getBean(ComputeFirst.class).haveTable(coordinatesList, lightSource.toFloat(), refractivity, fileId, unitLenth, unitWidth, tableCoordinatesList, tableCenters, tables);
                    //叠加每个灯的光照度
                    illuminationsListTemps.add(illuminationsListTemp);
                } else {
                    //房间没有桌子
                    //判断线程池是否满了 满了就等待
                    isFull(computeFirstThreadPool);
                    //多线程并行计算每个灯的光照度
                    Future<List<Float>> illuminationsListTemp = SpringUtil.getBean(ComputeFirst.class).noHaveTable(coordinatesList, lightSource.toFloat(), refractivity, fileId, unitLenth, unitWidth);
                    //叠加每个灯的光照度
                    illuminationsListTemps.add(illuminationsListTemp);
                }
            }
            isAllOK(allOver, illuminationsListTemps);
            returnIlluminationsList = addAllReturnList(returnIlluminationsList, illuminationsListTemps);
        }
        logger.info("-----------计算首次光照度结束 总共耗时:" + (System.currentTimeMillis() - startTime) + "----------");
        return returnIlluminationsList;
    }


    @Resource(name = "computeSecondThreadPool")
    private ThreadPoolTaskExecutor computeSecondThreadPool;

    /**
     * 递归计算每个点照度值
     *
     * @param totalIlluminationList 每个点总照度值
     * @param illuminationList      每个点初始照度值
     * @param curIlluminationList   当前每个点照度值
     * @param coordinateList        每个点坐标集合，和初始照度值一一对应
     * @param refractivity          折射率
     * @param unitLenth             网格长度
     * @param unitWidth             网格宽度
     * @param counter               折射次数计数器
     * @param length                房间的长
     * @param width                 房间的宽
     * @param high                  房间的高度
     * @param tableHigh             桌子的高度
     * @return
     */
    public List<Float> getRecursionIllumination(List<Float> totalIlluminationList, List<Float> illuminationList, List<Float> curIlluminationList,
                                                List<Float[]> coordinateList, Float refractivity, Float unitLenth, Float unitWidth, Integer counter,
                                                Float length, Float width, Float high, Float tableHigh) {
        logger.info("-----------递归计算每次的反弹开始 次数:" + counter + "----------");
        //限制递归次数
        if (illuminationList.size() == 0 || counter >= 15) {
            return totalIlluminationList;
        }
        Map<Integer, Float> curIlluminationMap = Maps.newConcurrentMap();
        logger.info("每个点初始照度值:" + illuminationList.size() + ",每个点坐标集合，和初始照度值一一对应:" + coordinateList.size());
        List<Future> allFuture = new ArrayList<>();
        for (int i = 0; i < secondThreadCount; i++) {
            isFull(computeSecondThreadPool);
            Integer start = illuminationList.size() / secondThreadCount * i;
            Integer end = illuminationList.size() / secondThreadCount * (i + 1);
            //如果最后一次 要判断是否是少计算
            if ((i + 1) == secondThreadCount) {
                end = illuminationList.size() + 1;
            }
            logger.info("并行计算,start:" + start + ",end:" + end);
            /**
             *  把全部的点的list拆分成多个list  开始并行计算
             *  1.根据光照度计算光通
             *   2.根据光通计算光强
             *   3 根据光强计算光照度
             *   4.累计对应的点的光照度
             */
            Future<Boolean> one = SpringUtil.getBean(ComputeSecond.class).computeSecond(
                    totalIlluminationList, illuminationList, coordinateList,
                    refractivity, unitLenth, unitWidth, length, width, high, tableHigh,
                    curIlluminationMap, start, end);
            allFuture.add(one);
        }
        //判断是否全部执行完成
        Boolean allOver = false;
        while (!allOver) {
            for (Future one : allFuture) {
                allOver = one.isDone();
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        // 将Map中保存的照度再次保存至list，再次递归
        Set<Entry<Integer, Float>> set = curIlluminationMap.entrySet();
        Iterator<Entry<Integer, Float>> it = set.iterator();
        while (it.hasNext()) {
            Float illuminationPoint = it.next().getValue();
            //判断这点的光照度是不是大于1
            if (illuminationPoint > 1f) {
                curIlluminationList.add(illuminationPoint);
            }
        }
        counter++;
        // 递归
        getRecursionIllumination(totalIlluminationList, curIlluminationList, Lists.newArrayList(), coordinateList,
                refractivity, unitLenth, unitWidth, counter, length, width, high, tableHigh);
        return totalIlluminationList;
    }


    /**
     * 获取完整坐标
     *
     * @param coordinatex
     * @param coordinatey
     * @param offset
     * @param position
     * @return
     */
    public Float[] getWholeCoordinate(float coordinatex, float coordinatey, float offset, String position) {
        Float[] coordinate = null;
        //地板坐标
        if (Constants.POSITION_BOTTOM.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex;
            coordinate[1] = coordinatey;
            coordinate[2] = offset;
            coordinate[3] = 0f;
            coordinate[4] = 0f;
            coordinate[5] = 1f;

        }
        //桌顶坐标
        if (Constants.POSITION_TABLE_TOP.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex + 2;
            coordinate[1] = coordinatey + 2;
            coordinate[2] = offset;
            coordinate[3] = 0f;
            coordinate[4] = 0f;
            coordinate[5] = -1f;

        }
        //桌底坐标
        if (Constants.POSITION_TABLE_BOTTOM.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex + 2;
            coordinate[1] = coordinatey + 2;
            coordinate[2] = offset;
            coordinate[3] = 0f;
            coordinate[4] = 0f;
            coordinate[5] = 1f;

        }
        //天花板坐标
        if (Constants.POSITION_TOP.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex;
            coordinate[1] = coordinatey;
            coordinate[2] = offset;
            coordinate[3] = 0f;
            coordinate[4] = 0f;
            coordinate[5] = -1f;

        }
        //前面坐标
        if (Constants.POSITION_FRONT.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex;
            coordinate[1] = offset;
            coordinate[2] = coordinatey;
            coordinate[3] = 0f;
            coordinate[4] = 1f;
            coordinate[5] = 0f;

        }
        //后面坐标
        if (Constants.POSITION_BACK.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = coordinatex;
            coordinate[1] = offset;
            coordinate[2] = coordinatey;
            coordinate[3] = 0f;
            coordinate[4] = -1f;
            coordinate[5] = 0f;

        }
        //左面坐标
        if (Constants.POSITION_LEFT.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = offset;
            coordinate[1] = coordinatex;
            coordinate[2] = coordinatey;
            coordinate[3] = 1f;
            coordinate[4] = 0f;
            coordinate[5] = 0f;

        }
        //右边坐标
        if (Constants.POSITION_RIGHT.equals(position)) {
            coordinate = new Float[6];
            coordinate[0] = offset;
            coordinate[1] = coordinatex;
            coordinate[2] = coordinatey;
            coordinate[3] = -1f;
            coordinate[4] = 0f;
            coordinate[5] = 0f;

        }
        return coordinate;
    }


    private void isFull(ThreadPoolTaskExecutor computeFirstThreadPool) {
        Boolean full = true;
        while (full) {
            Long currentTask = computeFirstThreadPool.getThreadPoolExecutor().getTaskCount() - computeFirstThreadPool.getThreadPoolExecutor().getCompletedTaskCount();
            Integer poolSize = computeFirstThreadPool.getThreadPoolExecutor().getMaximumPoolSize();
            logger.info(computeFirstThreadPool.getThreadNamePrefix() + "线程池活跃数:" + currentTask + ",线程池大小:" + poolSize);
            if (currentTask.intValue() != poolSize) {
                full = false;
            } else {
                try {
                    Thread.sleep(500L);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void isAllOK(boolean allOver, List<Future<List<Float>>> illuminationsListTemps) {
        while (!allOver) {
            for (Future one : illuminationsListTemps) {
                allOver = one.isDone();
            }
            try {
                Thread.sleep(500L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 把多线程计算的首次的光照度累加起来
     *
     * @param returnIlluminationsList
     * @param illuminationsListTemps
     * @return
     */
    private List<Float> addAllReturnList(List<Float> returnIlluminationsList, List<Future<List<Float>>> illuminationsListTemps) {
        try {
            for (int i = 0; i < illuminationsListTemps.get(0).get().size(); i++) {
                Float oneIllumination = 0.0f;
                for (int j = 0; j < illuminationsListTemps.size(); j++) {
                    oneIllumination += illuminationsListTemps.get(j).get().get(i);
                }
                returnIlluminationsList.add(oneIllumination);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        return returnIlluminationsList;
    }
}
