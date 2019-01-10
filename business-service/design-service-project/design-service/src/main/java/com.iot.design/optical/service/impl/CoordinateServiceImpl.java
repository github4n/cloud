package com.iot.design.optical.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.iot.design.optical.vo.LightSource;
import com.iot.design.optical.vo.Rectangle;
import com.iot.design.optical.constants.Constants;
import com.iot.design.optical.helper.CoordinateHelper;
import com.iot.design.optical.helper.GlareHelper;
import com.iot.design.optical.service.CoordinateService;
import com.iot.file.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.math.BigDecimal;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class CoordinateServiceImpl implements CoordinateService {
    //格式化 默认保留小数点后2位 四舍五入
    private static DecimalFormat df = new DecimalFormat("0.00");
    private Logger logger = LoggerFactory.getLogger(CoordinateServiceImpl.class);
    @Autowired
    private CoordinateHelper coordinateHelper;
    @Autowired
    private GlareHelper glareHelper;

    @Override
    public List<Map<String, Float>> calculator(Rectangle rectangle) {
        Long startTime = System.currentTimeMillis();
        Float length = rectangle.getLength();//房间的长
        Float width = rectangle.getWidth();//房间的宽
        Float high = rectangle.getHigh();//房间的高
        Float refractivity = rectangle.getRefractivity();//折射率
        String fileId = rectangle.getFileId();// ies文件id
        Float unitLenth = rectangle.getUnitLength(); //0.2
        Float unitWidth = rectangle.getUnitWidth(); //0.2
        List<LightSource> lightSources = rectangle.getLightSources();//光源坐标
        Float offsetUnitLength = unitLenth / 2;  //0.1
        Float offsetUnitWidth = unitWidth / 2; //0.1

        List<Float> illuminationList = new ArrayList<Float>();//照度(List)

        List<Map<String, Float>> result = Lists.newArrayList();

        // 光源坐标
//		if(lightSource==null||lightSource.length==0){
//			lightSource = new Float[]{length/2, width/2, high, 0f, 0f, -1f};
//		}

        // 1.地板坐标z=0
        List<Float[]> bottomCoordinates = coordinateHelper.getCoordinatesList(length, width, 0f,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_BOTTOM);
        // 2.天花板坐标z=high
        List<Float[]> topCoordinates = coordinateHelper.getCoordinatesList(length, width, high,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_TOP);
        // 3.前侧墙壁坐标y=0
        List<Float[]> frontCoordinates = coordinateHelper.getCoordinatesList(length, high, 0f,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_FRONT);
        // 4.后侧墙壁坐标y=width
        List<Float[]> backCoordinates = coordinateHelper.getCoordinatesList(length, high, width,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_BACK);
        // 5.左侧墙壁坐标x=0
        List<Float[]> leftCoordinates = coordinateHelper.getCoordinatesList(width, high, 0f,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_LEFT);
        // 6.右侧墙壁坐标x=length
        List<Float[]> rightCoordinates = coordinateHelper.getCoordinatesList(width, high, length,
                unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_RIGHT);

        //六个面的坐标集合
        List<Float[]> coordinateList = Lists.newArrayList();
        coordinateList.addAll(bottomCoordinates);
        coordinateList.addAll(topCoordinates);
        coordinateList.addAll(frontCoordinates);
        coordinateList.addAll(backCoordinates);
        coordinateList.addAll(leftCoordinates);
        coordinateList.addAll(rightCoordinates);
        //计算首次的光照度
        if (rectangle.getTableFlag() == null) {//不存在桌子
            illuminationList = coordinateHelper.getIlluminationList(coordinateList, lightSources, refractivity, fileId, unitLenth, offsetUnitWidth, null, null, null);
        } else {
            //桌面的长宽高
            List<List<Float>> tables = Lists.newArrayList();
            List<List<Float>> tableCenters = Lists.newArrayList();
            List<Float[]> tableCoordinates = Lists.newArrayList();
            //多个桌子
            for (int i = 0; i < rectangle.getTables().size(); i++) {
                Float tableLength = rectangle.getTables().get(i).getTableLength();
                Float tableWeigh = rectangle.getTables().get(i).getTableWidth();
                Float tableHeight = rectangle.getTables().get(i).getTableHeight();
                //桌面长宽高的集合
                List<Float> table = Lists.newArrayList();
                table.add(tableLength);
                table.add(tableWeigh);
                table.add(tableHeight);
                tables.add(table);

                //桌面
                //桌面的中点
                Float tableCenterX = rectangle.getTables().get(i).getTbaleCenterX();
                Float tableCenterY = rectangle.getTables().get(i).getTbaleCenterY();
                Float tableCenterZ = rectangle.getTables().get(i).getTbaleCenterZ();
                //桌面中点的坐标
                List<Float> tableCenter = Lists.newArrayList();
                tableCenter.add(tableCenterX);
                tableCenter.add(tableCenterY);
                tableCenter.add(tableCenterZ);
                tableCenters.add(tableCenter);

                //添加桌面上平面的坐标集合
                List<Float[]> tableCoordinatesTop = coordinateHelper.getCoordinatesList(tableLength, tableWeigh, tableHeight,
                        unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_TABLE_TOP);
                //添加桌面下平面的坐标集合
                List<Float[]> tableCoordinatesBottom = coordinateHelper.getCoordinatesList(tableLength, tableWeigh, tableHeight,
                        unitLenth, unitWidth, offsetUnitLength, offsetUnitWidth, Constants.POSITION_TABLE_BOTTOM);
                //桌子上平面坐标的集合
                tableCoordinates.addAll(tableCoordinatesTop);
                tableCoordinates.addAll(tableCoordinatesBottom);
            }

            illuminationList = coordinateHelper.getIlluminationList(coordinateList, lightSources, refractivity, fileId, unitLenth, offsetUnitWidth, tableCoordinates, tableCenters, tables);
            coordinateList.addAll(tableCoordinates);
        }
        //解决并发问题
        List<Float> totalIlluminationList = Lists.newCopyOnWriteArrayList();
        totalIlluminationList.addAll(illuminationList);
        //最开始的总照度
        List<Float> curIlluminationList = Lists.newArrayList();
        //递归计算第二次以及之后的每次的反弹
        List<Float> resultIlluminationList = coordinateHelper.getRecursionIllumination(
                totalIlluminationList, illuminationList, curIlluminationList, coordinateList,
                refractivity, unitLenth, offsetUnitWidth, 0,
                length, width, high, rectangle.getTables().get(0).getTableHeight());

        for (int i = 0; i < coordinateList.size(); i++) {
            Map<String, Float> dataMap = Maps.newHashMap();
            dataMap.put(JSON.toJSONString(coordinateList.get(i)), resultIlluminationList.get(i));
            result.add(dataMap);
        }
        logger.info("总共耗时:" + (System.currentTimeMillis() - startTime));
        logger.info(JSON.toJSONString(result));
        return result;

    }

    /**
     * 单点的眩光值计算
     *
     * @param eyePoint       x y z nx ny nz
     * @param lightPoints    list< x y z nx ny nz>
     * @param lightParams    长 宽
     * @param ies            ies表的id  要事先上传到redis中
     * @param coordinateList 上一步计算好的光照度 [{"[0.1,0.1,0,0,0,1]":55.964905},{"[0.1,0.70000005,0,0,0,1]":86.67297}....] 可能存在精度问题
     */
    @Override
    public void glareCalculator(Float[] eyePoint, List<Float[]> lightPoints, Float[] lightParams, String ies, List<Map<String, Object>> coordinateList) {
        if (coordinateList == null) {
            coordinateList = readDemoFile("classpath:hrtr/光照demo.txt");
        }
        //全部的灯对这个位置的眩光值
        Float allSum = new Float(0);
        // 循环计算每个灯对这个位置的眩光值
        for (Float[] oneLightPoint : lightPoints) {
            //1 计算位置指数P
            Float p = glareHelper.calculatorP(eyePoint, oneLightPoint);
            logger.info("p:"+p);
            if (p == 0) {
                continue;
            }
            // 2 计算w值
            Float w = glareHelper.calculatorW(eyePoint, oneLightPoint, lightParams);
            logger.info("w:"+w);
            // 3 计算 La的值
            Float La = glareHelper.calculatorLa(eyePoint, oneLightPoint, ies);
            logger.info("La:"+La);
            // (La平方*w)/ p的平方
            Float temp = new Float((Math.pow(La, 2) * w) / (Math.pow(p, 2))); //new Float(Math.pow(La, 2)) * w / new Float(Math.pow(p, 2));
            logger.info("temp:"+temp);
            //灯对这个位置的眩光值累加
            allSum += temp;
        }
        if (allSum == 0f) {
            return;
        }
        // 4 计算 Lb的值
        Float lb = glareHelper.calculatorLb(eyePoint, coordinateList);
        logger.info("lb:"+lb);
        //5  根据 1,2,3,4 计算 UGA 8*lg(0.25/Lb)*Σ(（La^2 *w）P^2)

        Float lg = new Float(Math.log10((0.25f / lb) * allSum));
        Float UGA = 8 * lg;

        logger.info("UGA值:" + df.format(UGA));

    }

    private List<Map<String, Object>> readDemoFile(String fileName) {
        System.out.println(fileName);
        String data = "";
        try {
            File file = ResourceUtils.getFile(fileName);
            FileInputStream fileInputStream = new FileInputStream(file);
            data = new String(FileUtil.readInputStream(fileInputStream));
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Map> aa = JSON.parseArray(data, Map.class);
        List<Map<String, Object>> returnOne=new ArrayList<>();
        for(Map one:aa){
            returnOne.add(one);
        }
        return returnOne;
    }

    public static void main(String[] args) {
        //先把角度转换成弧度 在去计算值
        System.out.println(Math.sin(Math.toRadians(30)));
        System.out.println(Math.toDegrees(Math.acos(0.5)));
    }
}
