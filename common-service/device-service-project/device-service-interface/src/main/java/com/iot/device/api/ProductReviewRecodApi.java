package com.iot.device.api;

import com.iot.device.api.fallback.ProductAuditApiFallbackFactory;
import com.iot.device.vo.req.product.ProductReviewRecordReq;
import com.iot.device.vo.rsp.product.ProductReviewRecordResp;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：产品审核api
 * 创建人： yeshiyuan
 * 创建时间：2018/10/24 16:31
 * 修改人： yeshiyuan
 * 修改时间：2018/10/24 16:31
 * 修改描述：
 */
@Api(tags = "产品审核api")
@FeignClient(value = "device-service", fallback = ProductAuditApiFallbackFactory.class)
@RequestMapping("/productAudit")
public interface ProductReviewRecodApi {

    /**
      * @despriction：提交审核结果
      * @author  yeshiyuan
      * @created 2018/10/24 16:43
      * @return
      */
    @ApiOperation("提交审核结果")
    @RequestMapping(value = "/submitAudit", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    void submitAudit(@RequestBody ProductReviewRecordReq req);

    /**
      * @despriction：获取产品审核记录
      * @author  yeshiyuan
      * @created 2018/10/24 16:43
      * @return
      */
    @ApiOperation("获取产品审核记录")
    @RequestMapping(value = "/getReviewRecord", method = RequestMethod.GET)
    List<ProductReviewRecordResp> getReviewRecord(@RequestParam("productId") Long productId);

    /**
     * @despriction：获取租户id
     * @author  yeshiyuan
     * @created 2018/11/3 14:18
     * @return
     */
    @ApiOperation(value = "获取租户id", notes = "获取租户id")
    @RequestMapping(value = "/getTenantIdById", method = RequestMethod.GET)
    Long getTenantIdById(@RequestParam("id") Long id);

    /**
     * @despriction：添加记录
     * @author  yeshiyuan
     * @created 2018/11/3 14:49
     * @return
     */
    @ApiOperation(value = "添加记录", notes = "添加记录")
    @RequestMapping(value = "/addRecord", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    Long addRecord(@RequestBody ProductReviewRecordReq setServiceReviewReq);
}
