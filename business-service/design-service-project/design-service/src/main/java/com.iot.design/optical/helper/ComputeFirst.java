package com.iot.design.optical.helper;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import com.iot.design.optical.constants.Constants;
import com.iot.redis.RedisCacheUtil;
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
@Async("computeFirstThreadPool")
public class ComputeFirst {

    public Future<List<Float>> noHaveTable(List<Float[]> coordinatesList, Float[] lightSourceCoordinates, Float refractivity, String fileId, Float unitLenth, Float unitWidth) {
        Future<List<Float>> future = null;
        List<Float> illuminationsList = Lists.newArrayList();
        for (int i = 0; i < coordinatesList.size(); i++) {
            Float[] coordinate = (Float[]) coordinatesList.get(i);
            // alpha 角余弦值
            float cosAlpha = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                    coordinate[0], coordinate[1], coordinate[2],
                    lightSourceCoordinates[3], lightSourceCoordinates[4], lightSourceCoordinates[5]);
            // gamma 角余弦值
            float cosGamma = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                    coordinate[0], coordinate[1], coordinate[2],
                    coordinate[3], coordinate[4], coordinate[5]);
            cosAlpha = Math.abs(cosAlpha);
            cosGamma = Math.abs(cosGamma);
            // 半径（平方）
            float radius = getRadius(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                    coordinate[0], coordinate[1], coordinate[2]);
            // alpha 角度值
            float radianTemp = (float) (Math.acos(cosAlpha) / Math.PI * 180);
            float radian = (float) (radianTemp > 90 ? 180 - radianTemp : radianTemp);
            // 初始化光强值
            float intensity = initLightIntensity(radian, fileId);
            // 初始化坐标照度值
            float illumination = initIllumination(intensity, cosGamma, radius);
            illuminationsList.add(illumination);
        }
        future = new AsyncResult<>(illuminationsList);
        return future;
    }

    /**
     *
     * @param coordinatesList 空间切割点的集合
     * @param lightSourceCoordinates
     * @param refractivity
     * @param fileId
     * @param unitLenth
     * @param unitWidth
     * @param tableCoordinatesList
     * @param tableCenters
     * @param tables
     * @return
     */
    public Future<List<Float>> haveTable(List<Float[]> coordinatesList, Float[] lightSourceCoordinates, Float refractivity, String fileId, Float unitLenth, Float unitWidth,
                                         List<Float[]> tableCoordinatesList, List<List<Float>> tableCenters, List<List<Float>> tables) {
        Future<List<Float>> future = null;
        List<Float> illuminationsList = Lists.newArrayList();
        float illumination = 0.0f;
        //算六个面上的点
        for (int i = 0; i < coordinatesList.size(); i++) {
            for (int j = 0; j < tables.size(); j++) {
                List<Float> tableCenter = tableCenters.get(j);
                List<Float> table = tables.get(j);
                Float[] coordinate = (Float[]) coordinatesList.get(i);
                //判断该点坐标与光源坐标所连的直线是不是否跟桌面相交
                boolean flag = isIntersect(coordinate, lightSourceCoordinates, tableCenter, table);//是否相交的标志
                if (flag) {//相交
                    illumination = 0.0f;
                } else {//不相交
                    // alpha 角余弦值
                    float cosAlpha = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                            coordinate[0], coordinate[1], coordinate[2],
                            lightSourceCoordinates[3], lightSourceCoordinates[4], lightSourceCoordinates[5]);
                    // gamma 角余弦值
                    float cosGamma = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                            coordinate[0], coordinate[1], coordinate[2],
                            coordinate[3], coordinate[4], coordinate[5]);
                    cosAlpha = Math.abs(cosAlpha);
                    cosGamma = Math.abs(cosGamma);
                    // 半径（平方）
                    float radius = getRadius(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                            coordinate[0], coordinate[1], coordinate[2]);
                    // alpha 角度值
                    float radianTemp = (float) (Math.acos(cosAlpha) / Math.PI * 180);
                    float radian = (float) (radianTemp > 90 ? 180 - radianTemp : radianTemp);
                    // 初始化光强值
                    float intensity = initLightIntensity(radian, fileId);
                    // 初始化坐标照度值
                    illumination = initIllumination(intensity, cosGamma, radius);
                }
            }
            illuminationsList.add(illumination);
        }
        //算桌面上的点
        for (int i = 0; i < tableCoordinatesList.size(); i++) {
            Float[] coordinate = (Float[]) tableCoordinatesList.get(i);
            // alpha 角余弦值
            float cosAlpha = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                    coordinate[0], coordinate[1], coordinate[2],
                    lightSourceCoordinates[3], lightSourceCoordinates[4], lightSourceCoordinates[5]);
            // gamma 角余弦值
            float cosGamma = getCos(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                    coordinate[0], coordinate[1], coordinate[2],
                    coordinate[3], coordinate[4], coordinate[5]);
            //判断夹角是否大于等于90度,满足条件 照度设为0
            if (cosAlpha <= 0.0f || cosGamma <= 0.0f) {
                illumination = 0f;
            } else {
                cosAlpha = Math.abs(cosAlpha);
                cosGamma = Math.abs(cosGamma);
                // 半径（平方）
                float radius = getRadius(lightSourceCoordinates[0], lightSourceCoordinates[1], lightSourceCoordinates[2],
                        coordinate[0], coordinate[1], coordinate[2]);
                // alpha 角度值
                float radianTemp = (float) (Math.acos(cosAlpha) / Math.PI * 180);
                float radian = (float) (radianTemp > 90 ? 180 - radianTemp : radianTemp);
                // 初始化光强值
                float intensity = initLightIntensity(radian, fileId);
                // 初始化坐标照度值
                illumination = initIllumination(intensity, cosGamma, radius);
            }
            illuminationsList.add(illumination);
        }
        future = new AsyncResult<>(illuminationsList);
        return future;
    }

    /**
     * 判断该点坐标与光源坐标所连的直线是不是否跟桌面相交
     *
     * @param coordinate             该点坐标
     * @param lightSourceCoordinates 光源坐标
     * @param tableCenter            桌面中心坐标
     * @param table                  桌面中长宽高
     * @return
     */
    public boolean isIntersect(Float[] coordinate, Float[] lightSourceCoordinates, List<Float> tableCenter, List<Float> table) {
        Boolean flag = false;
        //桌面中的点应满足的条件
        //X应满足
        Float xMax = tableCenter.get(0) + table.get(0) * 0.5f;
        Float xMin = tableCenter.get(0) - table.get(0) * 0.5f;
        Float yMax = tableCenter.get(1) + table.get(1) * 0.5f;
        Float yMin = tableCenter.get(1) - table.get(1) * 0.5f;
        Float z = table.get(2);
        /*Float xMax = 3f;
        Float xMin = 2f;
		Float yMax = 3f;
		Float yMin = 2f;
		Float z = 1f;*/
        Float eX = 0.0f;
        Float eY = 0.0f;
        Float eZ = 0.0f;

        //直线的所有情况 光源坐标（X1，Y1，Z1） 该点坐标（X2，Y2，Z2）
        //第一种情况 Z1 = Z2 ,则在桌面中应满足条件Z1=桌子的高
        if (lightSourceCoordinates[2] == coordinate[2]) {
            if (lightSourceCoordinates[2] == z) {
                return true;
            } else {
                return false;
            }
        } else {
            //第二中情况 Z1不等Z2，X1=X2 ,则在桌面中应满足条件 eX小于等于xMin 大于等于xMax，eY小于等于yMin 大于等于 yMax
            if (lightSourceCoordinates[0] == coordinate[0]) {
                eY = (z - lightSourceCoordinates[2]) / (lightSourceCoordinates[2] - coordinate[2]) * (lightSourceCoordinates[1] - coordinate[1]) + lightSourceCoordinates[1];
                eX = lightSourceCoordinates[0];
                eZ = z;
                if (eX >= xMin && eX <= xMax && eY >= yMin && eY <= yMax) {
                    return true;
                } else {
                    return false;
                }
            } else {
                //第二中情况 Z1不等Z2，X1不等于X2 ,则在桌面中应满足条件 eX小于等于xMin 大于等于xMax，eY小于等于yMin 大于等于 yMax
                eY = (z - lightSourceCoordinates[2]) / (lightSourceCoordinates[2] - coordinate[2]) * (lightSourceCoordinates[1] - coordinate[1]) + lightSourceCoordinates[1];
                eX = (z - lightSourceCoordinates[2]) / (lightSourceCoordinates[2] - coordinate[2]) * (lightSourceCoordinates[0] - coordinate[0]) + lightSourceCoordinates[0];
                eZ = z;
                if (eX >= xMin && eX <= xMax && eY >= yMin && eY <= yMax) {
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    /**
     * 获取第一次照度（初始照度），作为递归结束条件
     *
     * @param intensity 光强
     * @param cosGamma  gamma角
     * @param radius    半径（未开平方根）
     * @return
     */
    public Float initIllumination(float intensity, float cosGamma, float radius) {
        float illumination = 0f;
        if (intensity != 0f && radius != 0f) {
            illumination = intensity * cosGamma / radius;
        }
        return illumination;
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
        //平方数
        float vectormPow = (float) Math.pow(vectorx, 2) +
                (float) Math.pow(vectory, 2) +
                (float) Math.pow(vectorz, 2);
        //平方根
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
     * 初始化光强值（通过ies文件线性查找）
     *
     * @param radian 角度
     * @param fileId 文件ID
     * @return
     */
    public Float initLightIntensity(Float radian, String fileId) {
        Map<String, Float> iesData = RedisCacheUtil.hashGetAll(Constants.DATA_PREFIX_KEY + fileId, Float.class);
        Map<String, Object> angleUnitMap = RedisCacheUtil.hashGetAll(Constants.DATA_CONFIG_KEY + fileId, Object.class);
        Float angleUnit = Float.valueOf(angleUnitMap.get(Constants.DATA_ANGLE_UNIT_KEY) + "");
        JSONArray columns = (JSONArray) angleUnitMap.get(Constants.DATA_ANGLE_COLUMN_KEY);
        JSONArray rows = (JSONArray) angleUnitMap.get(Constants.DATA_ANGLE_ROW_KEY);
        Float intensity = null;
        if (radian == 0f || radian % angleUnit == 0f) {
            //如果可以整出就直接从表格取
            intensity = iesData.get(rows.get(0) + "-" + radian);
        } else {
            //如果不行 就得到两边的值然后线性计算出中间值
            Float radianMax = 0f;
            Float radianMin = 0f;
            Float intensityMax = 0f;
            Float intensityMin = 0f;
            for (int i = 0; i < columns.size(); i++) {
                Float column = Float.valueOf(columns.get(i).toString());
                if (column > radian) {
                    radianMax = column;
                    break;
                }
            }

            for (int i = columns.size() - 1; i >= 0; i--) {
                Float column = Float.valueOf(columns.get(i).toString());
                if (column < radian) {
                    radianMin = column;
                    break;
                }
            }
            intensityMax = iesData.get(rows.get(0) + "-" + radianMax);
            intensityMin = iesData.get(rows.get(0) + "-" + radianMin);

            intensity = (intensityMax - intensityMin) / (radianMax - radianMin) * (radian - radianMin) + intensityMin;//求光强值
        }
        return intensity;
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
}
