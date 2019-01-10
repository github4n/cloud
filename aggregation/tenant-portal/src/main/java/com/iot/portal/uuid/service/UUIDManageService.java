package com.iot.portal.uuid.service;

import com.iot.common.helper.Page;
import com.iot.payment.vo.order.req.OrderPayReq;
import com.iot.portal.uuid.vo.req.GetUUIDOrderReq;
import com.iot.portal.uuid.vo.req.GetUUIDReq;
import com.iot.portal.uuid.vo.req.UuidApplyReq;
import com.iot.portal.uuid.vo.resp.*;
import com.iot.portal.web.vo.PortalTechnicalResp;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 项目名称：IOT云平台
 * 模块名称：聚合层
 * 功能描述：UUID管理接口
 * 创建人： maochengyuan
 * 创建时间：2018/6/29 15:32
 * 修改人： maochengyuan
 * 修改时间：2018/6/29 15:32
 * 修改描述：
 */
public interface UUIDManageService {

    /** 
     * 描述：获取UUID商品列表
     * @author maochengyuan
     * @created 2018/7/3 17:55
     * @param 
     * @return java.util.List<GoodsInfoResp>
     */
    List<GoodsInfoResp> getUUIDGoodsList();

    /** 
     * 描述：编辑UUID订单
     * @author maochengyuan
     * @created 2018/7/3 19:13
     * @param orderId 订单id
     * @param createNum UUID数量
     * @return void
     */
    void editOrderCreateNum(String orderId, Integer createNum);

    /**
     * 描述：查询UUID申请订单
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     * @param uuidOrderReq
     * @return
     */
    Page<UUIDOrderResp> getUUIDOrder(GetUUIDOrderReq uuidOrderReq);

    /**
     * 描述：查询UUID信息，可以查询某批次或某个UUID,也可以根据状态查询，
     * @author shenxiang
     * @created 2018年6月29日 下午1:59:27
     * @param uuidReq
     * @return
     */
    Page<UUIDInfoResp> getUUIDInfo(GetUUIDReq uuidReq);

    /**
      * @despriction：uuid申请
      * @author  yeshiyuan
      * @created 2018/7/3 20:41
      * @param uuidApplyReq
      * @return 订单id
      */
    String uuidApply(UuidApplyReq uuidApplyReq);

    /**
	 *
	 * 描述：通过批次号获取证书下载URL
	 * @author 李帅
	 * @created 2018年6月8日 下午1:51:13
	 * @since
	 * @param batchNum
	 * @return
	 */
	String getDownloadUUID(Long batchNum);

    /**
      * @despriction：uuid订单支付
      * @author  yeshiyuan
      * @created 2018/7/4 14:56
      * @param uuidOrderPayReq
      * @return 支付url
      */
    String uuidOrderPay(OrderPayReq orderPayReq);

    /**
      * @despriction：paypal支付回调
      * @author  yeshiyuan
      * @created 2018/7/4 16:31
      * @param request
      * @return 跳转URL
      */
    String uuidOrderPayCallBack(HttpServletRequest request);

    /**
     * @despriction：获取uuid订单信息
     * @author  yeshiyuan
     * @created 2018/7/5 9:44
     * @param orderId 订单id
     * @return
     */
    UuidOrderInfoResp getUuidOrderInfo(String orderId);

    /**
      * @despriction：查找某个租户下面的产品
      * @author  yeshiyuan
      * @created 2018/7/9 10:48
      * @return
      */
    List<ProductListResp> findProductListByTenantId();
    
    /**
     * 
     * 描述：查找某个租户下面的直连产品
     * @author 李帅
     * @created 2018年11月8日 上午11:25:44
     * @since 
     * @return
     */
   List<ProductListResp> findDirectProductListByTenantId();
   
    /**
     * @despriction：加载设备类型支持的技术方案
     * @author  yeshiyuan
     * @created 2018/10/16 11:41
     * @return
     */
    List<PortalTechnicalResp> getTechnicals(Long deviceTypeId);

    /**
      * @despriction：通过技术方案对应的UUID商品列表
      * @author  yeshiyuan
      * @created 2018/10/16 11:53
      * @param null
      * @return
      */
    List<GoodsInfoResp> getUuidGoodsByTechnicalId(Long technicalId);
    
    /**
     * 
     * 描述：通过技术方案对应的UUID商品列表(没有gateway)
     * @author 李帅
     * @created 2018年11月8日 下午2:15:42
     * @since 
     * @param technicalId
     * @return
     */
   List<GoodsInfoResp> getUuidGoodsNoGateWayByTechnicalId(Long technicalId);


}
