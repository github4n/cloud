package com.iot.system.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.iot.system.vo.CityInfoResp;
import com.iot.system.vo.CountryInfoResp;
import com.iot.system.vo.ProvinceInfoResp;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：区域信息
 * 功能描述：区域信息
 * 创建人： 李帅
 * 创建时间：2018年10月31日 下午2:02:05
 * 修改人：李帅
 * 修改时间：2018年10月31日 下午2:02:05
 */
public interface RegionMapper {

    /** 
     * 
     * 描述：获取国家信息
     * @author 李帅
     * @created 2018年10月31日 下午2:06:54
     * @since 
     * @return
     */
    @Select({ "SELECT " + 
    		"	country_code AS countryCode, " + 
    		"	area_code AS areaCode, " + 
    		"	country_key AS countryKey " + 
    		"FROM " + 
    		"	country " })
    List<CountryInfoResp> getCountryInfo();
    
    /**
     * 
     * 描述：获取州省信息
     * @author 李帅
     * @created 2018年10月31日 下午2:56:19
     * @since 
     * @param countryCode
     * @return
     */
    @Select({ "SELECT " + 
    		"	country_code AS countryCode, " + 
    		"	province_code AS provinceCode, " + 
    		"	province_key AS provinceKey " + 
    		"FROM " + 
    		"	province " + 
    		"WHERE " + 
    		"	country_code = #{countryCode} " })
    List<ProvinceInfoResp> getProvinceInfo(@Param("countryCode") String countryCode);
    
    /**
     * 
     * 描述：获取城市信息
     * @author 李帅
     * @created 2018年10月31日 下午2:55:41
     * @since 
     * @param countryCode
     * @param provinceCode
     * @return
     */
    @Select({"<script>",
    	"SELECT " + 
    		"	country_code AS countryCode, " + 
    		"	province_code AS provinceCode, " + 
    		"	city_code AS cityCode, " + 
    		"	city_key AS cityKey " + 
    		"FROM " + 
    		"	city " + 
    		"WHERE " + 
    		"	country_code = #{countryCode} " + 
    		"<if test=\"provinceCode != null\"> and province_code = #{provinceCode}</if>" ,
//    		"AND province_code = #{provinceCode}",
    		"</script>"})
    List<CityInfoResp> getCityInfo(@Param("countryCode") String countryCode, @Param("provinceCode") String provinceCode);
}