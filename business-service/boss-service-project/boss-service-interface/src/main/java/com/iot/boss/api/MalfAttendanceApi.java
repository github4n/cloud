package com.iot.boss.api;

import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.MalfAttendanceParam;
import com.iot.boss.dto.MalfAttendanceTimerDto;
import com.iot.boss.dto.MalfParam;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

/**
 * 
 * 项目名称：立达信IOT云平台
 * 模块名称：报障业务-管理员值班
 * 功能描述：报障业务-管理员值班
 * 创建人： 李帅
 * 创建时间：2018年5月15日 下午6:15:32
 * 修改人：李帅
 * 修改时间：2018年5月15日 下午6:15:32
 */
@Api(description = "报障业务-管理员值班")
@FeignClient(name = "boss-service")
@RequestMapping(value = "/api/malfattendance/service")
public interface MalfAttendanceApi {

	/**
	 * 
	 * 描述：查询管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:14:29
	 * @since 
	 * @param malfParam
	 * @return
	 */
    @ApiOperation(value = "查询管理员值班",notes = "查询管理员值班")
    @RequestMapping(value = "/getMalfAttendance",method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<MalfAttendanceTimerDto> getMalfAttendance(@RequestBody MalfParam malfParam);
    
	/**
	 * 
	 * 描述：设置管理员值班
	 * @author 李帅
	 * @created 2018年5月15日 下午8:21:12
	 * @since 
	 * @param malfAttendanceParam
	 */
    @ApiOperation(value = "设置管理员值班",notes = "设置管理员值班")
    @RequestMapping(value = "/setMalfAttendance",method = RequestMethod.POST,consumes=MediaType.APPLICATION_JSON_VALUE)
    public void setMalfAttendance(@RequestBody MalfAttendanceParam malfAttendanceParam);
    
    /**
     * 
     * 描述：删除管理员值班
     * @author 李帅
     * @created 2018年5月15日 下午6:38:44
     * @since 
     * @param malfAttendanceId
     */
    @ApiOperation(value = "删除管理员值班",notes = "删除管理员值班")
    @ApiImplicitParams({@ApiImplicitParam(name = "malfAttendanceId", value = "管理员值班id", required = true, paramType = "query", dataType = "String")})
    @RequestMapping(value = "/deleteMalfAttendance",method = RequestMethod.POST)
    public void deleteMalfAttendance(@RequestParam("malfAttendanceId") String malfAttendanceId);
}
