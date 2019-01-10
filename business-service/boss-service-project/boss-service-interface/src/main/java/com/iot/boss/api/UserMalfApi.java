package com.iot.boss.api;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * 项目名称：cloud
 * 模块名称：用户报障管理api
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/5/15 15:26
 * 修改人： yeshiyuan
 * 修改时间：2018/5/15 15:26
 * 修改描述：
 */
@Api(description = "用户报障管理")
@FeignClient(name = "boss-service")
@RequestMapping(value = "/userMalf")
public interface UserMalfApi {

    @ApiOperation(value = "超级管理员确认问题已修复",notes = "超级管理员确认问题已修复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "管理员id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/confirmFixProblem",method = RequestMethod.POST)
    void confirmFixProblem(@RequestParam("adminId") Long adminId,@RequestParam("malfId") Long malfId,@RequestParam("remark") String remark);

    @ApiOperation(value = "提交报障单",notes = "提交报障单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户UUID",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "malfDesc",value = "故障描述",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tenantId",value = "租户ID",dataType = "Long",paramType = "query")
    })
    @RequestMapping(value = "/submitMalf",method = RequestMethod.POST)
    boolean submitMalf(@RequestParam("userId") String userId,@RequestParam("malfDesc") String malfDesc,@RequestParam("tenantId") Long tenantId);

    @ApiOperation(value = "管理员提交报障处理",notes = "管理员提交报障处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handleMsg",value = "留言信息",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报账单Id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "malfRank",value = "故障等级",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "handleAdminId",value = "操作人ID",dataType = "Long")
    })
    @RequestMapping(value = "/adminSubmitHandleMalf",method = RequestMethod.POST)
    boolean adminSubmitHandleMalf(@RequestParam("handleMsg") String handleMsg,@RequestParam("malfId") Long malfId,
                                  @RequestParam("malfRank") Integer malfRank,@RequestParam(value = "handleAdminId",required = false) Long handleAdminId);

    @ApiOperation(value = "超级管理员确认是否是问题",notes = "超级管理员确认是否是问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "管理员id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rollBackFlag",value = "是否问题（0：否 1：是）",dataType = "int",paramType = "query")
    })
    @RequestMapping(value = "/confirmProblem",method = RequestMethod.POST)
    void confirmProblem(@RequestParam("adminId") Long adminId,@RequestParam("malfId") Long malfId,@RequestParam("remark") String remark,@RequestParam("rollBackFlag")Integer rollBackFlag);

    @ApiOperation(value = "运维人员提交报障处理结果", notes = "给超级管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "当前处理人id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "handleMsg",value = "备注",dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/mainteProcessMalf",method = RequestMethod.POST)
    void mainteProcessMalf(@RequestParam("adminId") Long adminId, @RequestParam("malfId") Long malfId, @RequestParam("handleMsg") String handleMsg);

    @ApiOperation(value = "超级管理人员退回报障处理", notes = "退回给运维人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "当前处理人id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "handleMsg",value = "备注",dataType = "String",paramType = "query"),
    })
    @RequestMapping(value = "/returnMalfToMainte",method = RequestMethod.POST)
    void returnMalfToMainte(@RequestParam("adminId") Long adminId, @RequestParam("malfId") Long malfId, @RequestParam("handleMsg") String handleMsg);

    @ApiOperation(value = "查询用户报障处理详情", notes = "查询用户报障处理详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query")
    })
    @RequestMapping(value = "/getUserMalfDetail",method = RequestMethod.GET)
    MalfDetailDto getUserMalfDetail(@RequestParam("malfId") Long malfId);


    /**
     *
     * 描述：查询用户报障记录
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    @ApiOperation(value = "查询用户报障记录" ,  notes="查询用户报障记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "searchParam", value = "查询用户报障记录条件", required = true, dataType = "UserMalfListSearch")})
    @RequestMapping(value="/getUserMalfList",method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<UserMalfDto> getUserMalfList(@RequestBody UserMalfListSearch searchParam);

    /**
     *
     * 描述：管理(运维)人员列表查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    @ApiOperation(value = "查询管理人员列表", notes = "查询管理人员列表")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchParam", value = "查询管理人员列表", required = true, dataType = "UserAdminSearch")})
    @RequestMapping(value = "/getAdminSelectList", method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<MalfSysUserDto> getAdminSelectList(@RequestBody UserAdminSearch searchParam);

    /**
     *
     * 描述：用户报障历史记录查询
     * @author ouyangjie
     * @created 2018年5月15日 上午15:09:56
     * @since
     * @param searchParam
     * @return
     */
    @ApiOperation(value = "用户报障管理历史日志查询", notes = "用户报障管理历史日志查询")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "searchParam", value = "用户报障管理历史日志查询", required = true, dataType = "UserMalfHistorySearch")})
    @RequestMapping(value = "/getUserMalfHistoryList", method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(@RequestBody UserMalfHistorySearch searchParam);
}
