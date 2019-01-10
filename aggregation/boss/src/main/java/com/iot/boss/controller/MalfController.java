package com.iot.boss.controller;

import com.github.pagehelper.PageInfo;
import com.iot.boss.dto.*;
import com.iot.boss.service.malf.MalfService;
import com.iot.common.annotation.Action;
import com.iot.common.annotation.LoginRequired;
import com.iot.common.beans.CommonResponse;
import com.iot.common.exception.ResultMsg;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@Api(description = "系统报障接口",value = "系统报障接口")
@RequestMapping("/api/malfController")
public class MalfController {


    @Autowired
    private MalfService malfService;

    @ApiOperation(value = "运维人员提交报障处理结果", notes = "给超级管理员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "当前处理人id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "handleMsg",value = "备注",dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/mainteProcessMalf",method = RequestMethod.POST)
    public CommonResponse mainteProcessMalf(@RequestParam("adminId") Long adminId, @RequestParam("malfId") Long malfId, @RequestParam("handleMsg") String handleMsg) {
        this.malfService.mainteProcessMalf(adminId, malfId, handleMsg);
        return ResultMsg.SUCCESS.info();
    }

    @ApiOperation(value = "超级管理人员退回报障处理", notes = "退回给运维人员")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "adminId",value = "当前处理人id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "handleMsg",value = "备注",dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/returnMalfToMainte",method = RequestMethod.POST)
    public CommonResponse returnMalfToMainte(@RequestParam("adminId") Long adminId, @RequestParam("malfId") Long malfId, @RequestParam("handleMsg") String handleMsg){
        this.malfService.returnMalfToMainte(adminId, malfId, handleMsg);
        return ResultMsg.SUCCESS.info();
    }

    @ApiOperation(value = "提交报障单",notes = "提交报障单")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "userId",value = "用户UUID",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "malfDesc",value = "故障描述",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "tenantId",value = "租户ID",dataType = "Long",paramType = "query")
    })
    @RequestMapping(value = "/submitMalf",method = RequestMethod.POST)
    public CommonResponse submitMalf(@RequestParam("userId") String userId,@RequestParam("malfDesc") String malfDesc,@RequestParam("tenantId") Long tenantId){
        this.malfService.submitMalf(userId,malfDesc,tenantId);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "管理员提交报障处理",notes = "管理员提交报障处理")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "handleMsg",value = "留言信息",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "malfId",value = "报账单Id",dataType = "Long",paramType = "query"),
            @ApiImplicitParam(name = "malfRank",value = "故障等级 0：非问题 1：轻度 2：普通 3：严重",dataType = "Integer",paramType = "query"),
            @ApiImplicitParam(name = "handleAdminId",value = "操作人ID",dataType = "Long")
    })
    @RequestMapping(value = "/adminSubmitHandleMalf",method = RequestMethod.POST)
    public CommonResponse adminSubmitHandleMalf(@RequestParam("handleMsg") String handleMsg,@RequestParam("malfId") Long malfId,
                                  @RequestParam("malfRank") Integer malfRank,@RequestParam(value = "handleAdminId",required = false) Long handleAdminId){
        this.malfService.adminSubmitHandleMalf(handleMsg,malfId,malfRank,handleAdminId);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "超级管理员确认是否是问题",notes = "超级管理员确认是否是问题")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String",paramType = "query"),
            @ApiImplicitParam(name = "rollBackFlag",value = "是否问题（0：否 1：是）",dataType = "int",paramType = "query")
    })
    @RequestMapping(value = "/confirmProblem",method = RequestMethod.POST)
    public CommonResponse confirmProblem(@RequestParam("malfId") Long malfId,@RequestParam("remark") String remark,@RequestParam("rollBackFlag")Integer rollBackFlag){
        this.malfService.confirmProblem(malfId,remark,rollBackFlag);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "超级管理员确认问题已修复",notes = "超级管理员确认问题已修复")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query"),
            @ApiImplicitParam(name = "remark",value = "备注",dataType = "String",paramType = "query")
    })
    @RequestMapping(value = "/confirmFixProblem",method = RequestMethod.POST)
    public CommonResponse confirmFixProblem(@RequestParam("malfId") Long malfId,@RequestParam("remark") String remark){
        this.malfService.confirmFixProblem(malfId,remark);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询用户报障处理详情", notes = "查询用户报障处理详情")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "malfId",value = "报障单id",dataType = "long",paramType = "query")
    })
    @RequestMapping(value = "/getUserMalfDetail",method = RequestMethod.GET)
    public CommonResponse getUserMalfDetail(@RequestParam("malfId") Long malfId){
        return ResultMsg.SUCCESS.info(this.malfService.getUserMalfDetail(malfId));
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询管理员值班", notes = "查询管理员值班")
    @RequestMapping(value = "/getMalfAttendance",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse getMalfAttendance(@RequestBody MalfParam malfParam){
        return ResultMsg.SUCCESS.info(this.malfService.getMalfAttendance(malfParam));
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "设置管理员值班", notes = "设置管理员值班")
    @RequestMapping(value = "/setMalfAttendance",method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public CommonResponse setMalfAttendance(@RequestBody MalfAttendanceParam malfAttendanceParam){
    	this.malfService.setMalfAttendance(malfAttendanceParam);
        return ResultMsg.SUCCESS.info();
    }
    
    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "删除管理员值班", notes = "删除管理员值班")
    @ApiImplicitParams({@ApiImplicitParam(name = "malfAttendanceId",value = "报障单id",dataType = "String",paramType = "query")})
    @RequestMapping(value = "/deleteMalfAttendance",method = RequestMethod.POST)
    public CommonResponse deleteMalfAttendance(@RequestParam("malfAttendanceId") String malfAttendanceId){
    	this.malfService.deleteMalfAttendance(malfAttendanceId);
        return ResultMsg.SUCCESS.info();
    }

    @LoginRequired(value = Action.Normal)
    @ApiOperation(value = "查询用户报障记录" ,  notes="查询用户报障记录")
    @ApiImplicitParams({@ApiImplicitParam(name = "searchParam", value = "查询用户报障记录条件", required = true, dataType = "UserMalfListSearch")})
    @RequestMapping(value="/getUserMalfList",method=RequestMethod.POST,consumes= MediaType.APPLICATION_JSON_VALUE)
    public PageInfo<UserMalfDto> getUserMalfList(@RequestBody UserMalfListSearch searchParam){
        return this.malfService.getUserMalfList(searchParam);
    }

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
    public PageInfo<MalfSysUserDto> getAdminSelectList(@RequestBody UserAdminSearch searchParam){
        return this.malfService.getAdminSelectList(searchParam);
    }

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
    public PageInfo<UserMalfHistoryDto> getUserMalfHistoryList(@RequestBody UserMalfHistorySearch searchParam){
        return this.malfService.getUserMalfHistoryList(searchParam);
    }


}
