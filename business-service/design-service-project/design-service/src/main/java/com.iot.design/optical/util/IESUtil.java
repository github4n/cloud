package com.iot.design.optical.util;

import com.alibaba.fastjson.JSONArray;
import com.iot.design.optical.constants.Constants;
import com.iot.redis.RedisCacheUtil;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * 创建人:chenweida
 * 创建时间:2018/9/17
 */
@Component
public class IESUtil {

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
}
