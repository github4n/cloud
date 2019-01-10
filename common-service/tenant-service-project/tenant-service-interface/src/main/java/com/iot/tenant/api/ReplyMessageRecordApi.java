package com.iot.tenant.api;

import com.iot.common.helper.Page;
import com.iot.tenant.entity.ReplyMessageRecord;
import com.iot.tenant.vo.req.reply.AddReplyMessageReq;
import com.iot.tenant.vo.req.reply.PageQueryReplyMessageReq;
import com.iot.tenant.vo.req.reply.QueryReplyMessageListReq;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：回复消息记录api
 * 创建人： yeshiyuan
 * 创建时间：2018/11/1 19:52
 * 修改人： yeshiyuan
 * 修改时间：2018/11/1 19:52
 * 修改描述：
 */
@Api(tags = "回复消息记录api")
@FeignClient(value = "tenant-service")
@RequestMapping(value = "/api/replyMessageRecord")
public interface ReplyMessageRecordApi {

    /**
      * @despriction：添加
      * @author  yeshiyuan
      * @created 2018/11/1 20:09
      * @return
      */
    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/add", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    int add(@RequestBody AddReplyMessageReq messageReq);

    /**
      * @despriction：分页查询回复消息列表
      * @author  yeshiyuan
      * @created 2018/11/1 20:34
      * @return
      */
    @ApiOperation(value = "分页查询回复消息列表", notes = "分页查询回复消息列表")
    @RequestMapping(value = "/pageQuery", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Page pageQuery(@RequestBody PageQueryReplyMessageReq pageReq);

    /**
     * @despriction：查询回复详情
     * @author  yeshiyuan
     * @created 2018/11/2 10:09
     * @return
     */
    @ApiOperation(value = "查询回复详情", notes = "查询回复详情")
    @RequestMapping(value = "/queryReplyList", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    List<ReplyMessageRecord> queryReplyList(@RequestBody QueryReplyMessageListReq req);
}
