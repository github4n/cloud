package com.iot.design.optical.helper;

import org.springframework.context.annotation.Scope;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/7
 */
@Component
@Scope("prototype")
@Async("computeSecondThreadPool")
public class ComputeSecond {
    /**
     * 1.根据光照度计算光通
     * 2.根据光通计算光强
     * 3 根据光强计算光照度
     * 4.累计对应的点的光照度
     *
     * @param totalIlluminationList 每个点总照度值
     * @param illuminationList      每个点初始照度值
     * @param coordinateList        每个点坐标集合，和初始照度值一一对应
     * @param refractivity          折射率
     * @param unitLenth             网格长度
     * @param unitWidth             网格宽度
     * @param length                房间的长
     * @param width                 房间的宽
     * @param high                  房间的高度
     * @param tableHigh             桌子的高度
     * @param curIlluminationMap    当前的光照度的map
     * @param start                 计算的点的起始并发计算用
     * @param end                   计算的点的结束 并发计算用
     * @return
     */
    public Future<Boolean> computeSecond(List<Float> totalIlluminationList, List<Float> illuminationList, List<Float[]> coordinateList,
                                         Float refractivity, Float unitLenth, Float unitWidth, Float length, Float width,
                                         Float high, Float tableHigh, Map<Integer, Float> curIlluminationMap, Integer start, Integer end) {
        for (int i = start; i < end; i++) {
            // 光源照度
            Float illumination = illuminationList.get(i);
            // 光源坐标
            Float[] coordinate = coordinateList.get(i);
            // 光通
            Float luminousFlux = this.getLuminousFlux(illumination, unitLenth, unitWidth);
            for (int j = 0; j < coordinateList.size(); j++) {
                // 计算点坐标
                Float[] coordinatePoint = coordinateList.get(j);
                float cosAlpha = this.getCos(coordinate[0], coordinate[1], coordinate[2],
                        coordinatePoint[0], coordinatePoint[1], coordinatePoint[2],
                        getDirection(coordinate, length, width, high, "light", tableHigh)[0],
                        getDirection(coordinate, length, width, high, "light", tableHigh)[1],
                        getDirection(coordinate, length, width, high, "light", tableHigh)[2]);

                cosAlpha = Math.abs(cosAlpha);
                // 光强
                Float intensity = this.getLightIntensity(luminousFlux, cosAlpha, refractivity);
                // 半径（平方）
                Float radius = this.getRadius(coordinate[0], coordinate[1], coordinate[2],
                        coordinatePoint[0], coordinatePoint[1], coordinatePoint[2]);

                //光照度
                float cosGamma = this.getCos(coordinate[0], coordinate[1], coordinate[2],
                        coordinatePoint[0], coordinatePoint[1], coordinatePoint[2],
                        getDirection(coordinatePoint, length, width, high, "point", tableHigh)[0],
                        getDirection(coordinatePoint, length, width, high, "point", tableHigh)[1],
                        getDirection(coordinatePoint, length, width, high, "point", tableHigh)[2]);
                cosGamma = Math.abs(cosGamma);
                //计算这点的光照度
                Float illuminationPoint = this.initIllumination(intensity, cosGamma, radius);
                // 光照度叠加
                totalIlluminationList.set(j, totalIlluminationList.get(j) + illuminationPoint);

                // 所有计算点的照度值累加起来，存入Map
                if (curIlluminationMap.containsKey(j)) {
                    curIlluminationMap.put(j, curIlluminationMap.get(j) + illuminationPoint);
                } else {
                    curIlluminationMap.put(j, illuminationPoint);
                }
            }
        }

        return new AsyncResult<>(true);
    }

    /**
     * 获取光通
     *
     * @param illumination 照度
     * @param unitLenth
     * @param unitWidth
     * @return
     */
    public Float getLuminousFlux(float illumination, Float unitLenth, Float unitWidth) {
        float luminousFlux = illumination * unitLenth * unitWidth;
        return luminousFlux;
    }

    /**
     * 获取距离平方
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @return
     */
    public static Float getRadius(Float x1, Float y1, Float z1,
                                  Float x2, Float y2, Float z2) {
        float vectorx = x2 - x1;
        float vectory = y2 - y1;
        float vectorz = z2 - z1;

        float radiusPow = (float) Math.pow(vectorx, 2) +
                (float) Math.pow(vectory, 2) +
                (float) Math.pow(vectorz, 2);

        return radiusPow;
    }

    /**
     * 获取夹角
     *
     * @param x1 光源坐标
     * @param y1
     * @param z1
     * @param x2 计算点坐标
     * @param y2
     * @param z2
     * @param vx 方向坐标
     * @param vy
     * @param vz
     * @return
     */
    public static Float getCos(Float x1, Float y1, Float z1,
                               Float x2, Float y2, Float z2,
                               Float vx, Float vy, Float vz) {
        float vectorx = x2 - x1;
        float vectory = y2 - y1;
        float vectorz = z2 - z1;

        float vector = vectorx * vx +
                vectory * vy +
                vectorz * vz;
        float vectormPow = (float) Math.pow(vectorx, 2) +
                (float) Math.pow(vectory, 2) +
                (float) Math.pow(vectorz, 2);
        float vectormSqrt = (float) Math.sqrt(vectormPow);

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
    }

    /**
     * 获取光线方向
     *
     * @param coordinate
     * @param length
     * @param width
     * @param high
     * @param type
     * @return
     */
    public Float[] getDirection(Float[] coordinate, Float length, Float width, Float high, String type, Float tableHigh) {
        Float coordinateLength = coordinate[0];
        Float coordinateWidth = coordinate[1];
        Float coordinateHigh = coordinate[2];

        if ("light".equals(type)) {
            if (coordinateLength.compareTo(0f) == 0) {
                return new Float[]{0f, 1f, 0f};
            }
            if (coordinateLength.compareTo(length) == 0) {
                return new Float[]{0f, -1f, 0f};
            }
            if (coordinateWidth.compareTo(0f) == 0) {
                return new Float[]{1f, 0f, 0f};
            }
            if (coordinateWidth.compareTo(width) == 0) {
                return new Float[]{-1f, 0f, 0f};
            }
            if (coordinateHigh.compareTo(0f) == 0) {
                return new Float[]{0f, 0f, 1f};
            }
            if (coordinateHigh.compareTo(high) == 0) {
                return new Float[]{0f, 0f, -1f};
            }
            //桌子的
            if (coordinateHigh.compareTo(tableHigh) == 0) {
                return new Float[]{0f, 0f, 1f};
            }
        } else if ("point".equals(type)) {
            if (coordinateLength.compareTo(0f) == 0) {
                return new Float[]{0f, -1f, 0f};
            }
            if (coordinateLength.compareTo(length) == 0) {
                return new Float[]{0f, 1f, 0f};
            }
            if (coordinateWidth.compareTo(0f) == 0) {
                return new Float[]{-1f, 0f, 0f};
            }
            if (coordinateWidth.compareTo(width) == 0) {
                return new Float[]{1f, 0f, 0f};
            }
            if (coordinateHigh.compareTo(0f) == 0) {
                return new Float[]{0f, 0f, -1f};
            }
            if (coordinateHigh.compareTo(high) == 0) {
                return new Float[]{0f, 0f, 1f};
            }
            //桌子的
            if (coordinateHigh.compareTo(tableHigh) == 0) {
                return new Float[]{0f, 0f, 1f};
            }
        }
        return null;
    }

    /**
     * 获取光强
     *
     * @param luminousFlux 光通
     * @param cosAlpha     alpha角
     * @param refractivity 折射率
     * @return
     */
    public Float getLightIntensity(Float luminousFlux, float cosAlpha, float refractivity) {
        float intensity = (float) (luminousFlux / Math.PI * cosAlpha * refractivity);
        return intensity;
    }

    /**
     * 获取第一次照度（初始照度），作为递归结束条件
     *
     * @param intensity 光强
     * @param cosGamma  gamma角
     * @param radius    半径（未开平方根）
     * @return
     */
    public static Float initIllumination(float intensity, float cosGamma, float radius) {
        float illumination = 0f;
        if (intensity != 0f && radius != 0f) {
            illumination = intensity * cosGamma / radius;
        }
        return illumination;
    }
}
