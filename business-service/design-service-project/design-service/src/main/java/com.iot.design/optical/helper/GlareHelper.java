package com.iot.design.optical.helper;

import com.alibaba.fastjson.JSONArray;
import com.iot.design.optical.constants.Constants;
import com.iot.design.optical.util.*;
import com.iot.redis.RedisCacheUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/12
 * 眩光计算
 */
@Component
public class GlareHelper {
    private Logger logger = LoggerFactory.getLogger(GlareHelper.class);
    @Autowired
    private TriangularUtil triangularUtil;//三角函数的工具类
    @Autowired
    private VectorUtil vectorUtil;//向量的工具类
    @Autowired
    private MathUtil mathUtil;//数学计算的工具类
    @Autowired
    private IESUtil iesUtil;//ies的工具类
    @Autowired
    private HrTrUtil hrTrUtil;// hrtr指数位置表的工具类

    /**
     * 计算La
     *
     * @param eyePoint
     * @param oneLightPoint
     * @param ies
     * @return
     */
    public Float calculatorLa(Float[] eyePoint, Float[] oneLightPoint, String ies) {
        //计算cosA值
        Float cosA = new Float(vectorUtil.getCos(oneLightPoint[0], oneLightPoint[1], oneLightPoint[2],
                oneLightPoint[3], oneLightPoint[4], oneLightPoint[5],
                eyePoint[0], eyePoint[1], eyePoint[2]));
        //根据cosA得到角度
        Float angle = new Float(triangularUtil.getAngleByCos(cosA.floatValue()));
        //根据角度和 ies文件ID 去查找
        return new Float(iesUtil.initLightIntensity(angle.floatValue(), ies));
    }


    /**
     * 计算Lb
     *
     * @param eyePoint       人眼坐标 x y z nx ny nz
     * @param coordinateList 上一步计算好的光照度 [{"[0.1,0.1,0,0,0,1]":55.964905},{"[0.1,0.70000005,0,0,0,1]":86.67297}....] 可能存在精度问题
     * @return
     */
    public Float calculatorLb(Float[] eyePoint, List<Map<String, Object>> coordinateList) {
        Float all = new Float(0F);
        //判断每个点是不是属于上下10度
        for (Map<String, Object> onePointMap : coordinateList) {
            for (Map.Entry<String, Object> onePoint : onePointMap.entrySet()) {
                try {
                    // [0.1,0.1,0,0,0,1]  字符串转集合
                    List<Float> key = Arrays.asList(onePoint.getKey().replace("[", "").replace("]", "").split(",")).stream().map(
                            one -> new Float(one)
                    ).collect(Collectors.toList());
                    // 55.964905
                    Float value = 0f;
                    if (onePoint.getValue() instanceof BigDecimal) {
                        value = ((BigDecimal) onePoint.getValue()).floatValue();
                    } else if (onePoint.getValue() instanceof Float) {
                        value = (Float) onePoint.getValue();
                    } else if (onePoint.getValue() instanceof String) {
                        value = new Float((String) onePoint.getValue());
                    }
                    if (vectorUtil.getAngleByCos(eyePoint[0], eyePoint[1], eyePoint[2], eyePoint[3], eyePoint[4], eyePoint[5], key.get(0), key.get(1), key.get(2)) <= 10f) {
                        all += value;
                    }
                } catch (Exception e) {
                    logger.error("计算角度错误 坐标值:" + onePoint + ",报错信息" + e.getMessage());
                }
            }
        }
        //每个点的光强叠加/π
        return new Float(all / Math.PI);
    }

    /**
     * 计算w值
     *
     * @param eyePoint    x y z nx ny nz
     * @param lightPoint  x y z nx ny nz
     * @param lightParams 长 宽
     * @return
     */
    public Float calculatorW(Float[] eyePoint, Float[] lightPoint, Float[] lightParams) {
        //计算光源面积
        Float s = new Float(mathUtil.getS(lightParams[0], lightParams[1]));
        //计算cosa
        Float cosA = new Float(vectorUtil.getCos(lightPoint[0], lightPoint[1], lightPoint[2], lightPoint[3], lightPoint[4], lightPoint[5], eyePoint[0], eyePoint[1], eyePoint[2]));
        //计算r
        Float r = new Float(vectorUtil.getRWith2Point_Pow2(lightPoint[0], lightPoint[1], lightPoint[2], eyePoint[0], eyePoint[1], eyePoint[2]).doubleValue());
        //w= 光源面积 *cosα  / r2
        return (s * cosA) / (r);
    }

    /**
     * 计算位置指数P
     *
     * @param eyePoint 测试点的坐标
     * @param lightPoint 光源坐标
     */
    public Float calculatorP(Float[] eyePoint, Float[] lightPoint) {
        // 1把光源坐标换算成人眼坐标的坐标系
        Float eyePointX = eyePoint[0];
        Float eyePointY = eyePoint[1];
        Float eyePointZ = eyePoint[2];

        Float lightPointX = lightPoint[0];
        Float lightPointY = lightPoint[1];
        Float lightPointZ = lightPoint[2];

        Float newlightPointX = lightPointX - eyePointX;
        Float newlightPointY = lightPointY - eyePointY;
        Float newlightPointZ = lightPointZ - eyePointZ;
        // 所有的角度的坐标的集合
        // 2.计算光源的每个角度的坐标  先写死需要算的角度
        //   cosA x2+sinA y2
        Float cos=getCos(eyePoint, lightPoint);
        Float sin=getSin(eyePoint, lightPoint);

//        Float x = Math.abs(cos * newlightPointX) + (sin * newlightPointY));
//        //  |cosA y2-sinA x2|
//        Float y = Math.abs(cos * newlightPointX) - (sin * newlightPointY));
//        Float z = newlightPointZ;
//        // 3 计算T/R  H/R  去位置指数表格查找对应的P值
//        Float tr = 0f;
//        Float hr = 0f;
//        if (y != 0) {
//            tr = x / y;
//            hr = z / y;
//        }
        Float tr = 0f;
        Float hr = 0f;
        if (newlightPointY != 0) {
            tr = newlightPointY / newlightPointX;
            hr = newlightPointZ / newlightPointX;
        }
        //去位置指数表格查找对应的P值
        logger.info("hr:"+hr+",tr:"+tr);
        Float p = hrTrUtil.getPByTrHr(hr, tr);
        return p;
    }


    public Float getCos(Float[] eyePoint, Float[] lightPoin) {
        return vectorUtil.getCos(eyePoint[0], eyePoint[1], eyePoint[2], eyePoint[3], eyePoint[4], eyePoint[5]
                , lightPoin[0], lightPoin[1], lightPoin[2])
                ;
    }

    public Float getSin(Float[] eyePoint, Float[] lightPoin) {
        return vectorUtil.getSin(eyePoint[0], eyePoint[1], eyePoint[2], eyePoint[3], eyePoint[4], eyePoint[5]
                , lightPoin[0], lightPoin[1], lightPoin[2])
                ;
    }

    public static void main(String[] args) {
        List<Float> aa = Arrays.asList("[2.1,1.9000001,0,0,0,1]".replace("[", "").replace("]", "").split(",")).stream().map(
                one -> new Float(one)
        ).collect(Collectors.toList());
        System.out.println(aa);
    }
}
