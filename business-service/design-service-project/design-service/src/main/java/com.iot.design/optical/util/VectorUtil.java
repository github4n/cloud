package com.iot.design.optical.util;

import org.apache.commons.math3.linear.ArrayRealVector;
import org.apache.commons.math3.linear.RealVector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


/**
 * 向量工具类
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
@Component
public class VectorUtil {
    private Logger logger = LoggerFactory.getLogger(VectorUtil.class);
    @Autowired
    private TriangularUtil triangularUtil;

    /**
     * 获取两个方向向量的夹角的cos值
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param vx 光源方向坐标
     * @param vy
     * @param vz
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     */
    public Float getCos(Float x1, Float y1, Float z1,
                        Float vx, Float vy, Float vz,
                        Float x2, Float y2, Float z2) {
        try {
            float vectorx = x2 - x1;
            float vectory = y2 - y1;
            float vectorz = z2 - z1;

            float vector = vectorx * vx +
                    vectory * vy +
                    vectorz * vz;
            //m的模
            float vectormPow = (float) Math.pow(vectorx, 2) +
                    (float) Math.pow(vectory, 2) +
                    (float) Math.pow(vectorz, 2);
            float vectormSqrt = (float) Math.sqrt(vectormPow);
            //n的模
            float vectornPow = (float) Math.pow(vx, 2) +
                    (float) Math.pow(vy, 2) +
                    (float) Math.pow(vz, 2);
            float vectornSqrt = (float) Math.sqrt(vectornPow);

            float vectorAbs = vectormSqrt * vectornSqrt;

            float cos = 0f;
            if (vectormSqrt != 0f && vectornSqrt != 0f) {
                cos = vector / vectorAbs;
            }
            return cos;
        } catch (Exception e) {
            logger.error("获取两个方向向量的夹角的cos值计算错误:" + e.getMessage());
            return 0f;
        }
    }

    /**
     * 获取两个方向向量的夹角的sin值
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param vx 方向坐标
     * @param vy
     * @param vz
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     */
    public Float getAngleByCos(Float x1, Float y1, Float z1,
                               Float vx, Float vy, Float vz,
                               Float x2, Float y2, Float z2) {
        try {
            //先求出cos在求出sin值
            return triangularUtil.getAngleByCos(getCos(x1, y1, z1, vx, vy, vz
                    , x2, y2, z2));
        } catch (Exception e) {
            logger.error("获取两个方向向量的夹角的sin值计算错误:" + e.getMessage());
            return 0f;
        }
    }

    /**
     * 获取两个方向向量的夹角的sin值
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param vx 方向坐标
     * @param vy
     * @param vz
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     */
    public Float getSin(Float x1, Float y1, Float z1,
                        Float vx, Float vy, Float vz,
                        Float x2, Float y2, Float z2) {
        try {
            //先求出cos在求出sin值
            return triangularUtil.getSinByCos(getCos(x1, y1, z1, vx, vy, vz
                    , x2, y2, z2));
        } catch (Exception e) {
            logger.error("获取两个方向向量的夹角的sin值计算错误:" + e.getMessage());
            return 0f;
        }
    }

    /**
     * 计算两个坐标的距离
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     * @throws Exception
     */
    public Float getRWith2Point(Float x2, Float y2, Float z2,
                                Float x1, Float y1, Float z1
    ) {
        try {
            RealVector eyePoint = new ArrayRealVector(new Double[]{
                    x1.doubleValue(),
                    y1.doubleValue(),
                    z1.doubleValue()
            });
            RealVector lightPoint = new ArrayRealVector(new Double[]{
                    x2.doubleValue(),
                    y2.doubleValue(),
                    z2.doubleValue()
            });
            //向量之间的距离
            return new Float(Math.pow(eyePoint.getDistance(lightPoint), 2));
        } catch (Exception e) {
            logger.error("计算两个坐标的距离错误:" + e.getMessage());
            return 0f;
        }
    }

    /**
     * 计算两个坐标的距离的平方
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     * @throws Exception
     */
    public Float getRWith2Point_Pow2(Float x2, Float y2, Float z2,
                                     Float x1, Float y1, Float z1
    ) {
        try {
            RealVector eyePoint = new ArrayRealVector(new Double[]{
                    x1.doubleValue(),
                    y1.doubleValue(),
                    z1.doubleValue()
            });
            RealVector lightPoint = new ArrayRealVector(new Double[]{
                    x2.doubleValue(),
                    y2.doubleValue(),
                    z2.doubleValue()
            });
            //向量之间的距离
            return new Float(eyePoint.getDistance(lightPoint));
        } catch (Exception e) {
            logger.error("计算两个坐标的距离错误:" + e.getMessage());
            return 0f;
        }
    }

    public static void main(String[] args) {
        RealVector value1 = new ArrayRealVector(new Double[]{
                2d,2d,3d
        });
        RealVector value2 = new ArrayRealVector(new Double[]{
                3d,4d,5d
        });
        //去向量的模
        value1.getNorm();
        //向量相加
        value1.add(value2);
        //向量相减
        value1.subtract(value2);
        //向量相除
        value1.ebeDivide(value2);
        //向量相乘
        value1.ebeMultiply(value2);
        //向量点积
        value1.dotProduct(value2);
        //向量之间的距离
        value1.getDistance(value2);
        // 向量的cos值
        value1.cosine(value2);
        //向量的维度
        value1.getDimension();
        //三个值相加
        value1.getL1Norm();
    }
}
