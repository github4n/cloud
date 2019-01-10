package com.iot.boss.service.order;

import com.iot.boss.vo.order.req.AppOrderPageReq;
import com.iot.boss.vo.order.resp.AppOrderListResp;
import com.iot.boss.vo.uuid.GetUUIDRefundReq;
import com.iot.boss.vo.uuid.UUIDOrderResp;
import com.iot.boss.vo.voiceaccess.VoiceAccessReq;
import com.iot.boss.vo.voiceaccess.VoiceAccessTypeResp;
import com.iot.common.helper.Page;
import com.iot.device.vo.req.servicebuyrecordreq.ServiceBuyRecordReq;

import java.util.List;
import java.util.Map;

/**
 * 项目名称：cloud
 * 功能描述：订单管理service
 * 创建人： yeshiyuan
 * 创建时间：2018/11/14 10:15
 * 修改人： yeshiyuan
 * 修改时间：2018/11/14 10:15
 * 修改描述：
 */
public interface IOrderManagerService {

    /**
      * @despriction：app列表
      * @author  yeshiyuan
      * @created 2018/11/14 10:17
      * @return
      */
    Page<AppOrderListResp> appList(AppOrderPageReq appOrderPageReq);

    /**
      * @despriction：uuid列表
      * @author  yeshiyuan
      * @created 2018/11/14 10:18
      * @return
      */
    Page<UUIDOrderResp> uuidList(GetUUIDRefundReq uuidOrderReq);

    /**
     * @despriction：语音接入列表
     * @author  yeshiyuan
     * @created 2018/11/14 10:18
     * @return
     */
    Page voiceAccessList(VoiceAccessReq req);

    /**
     * @descrpiction: 语音接入类型列表
     * @author wucheng
     * @created 2018/11/15 10:05
     * @param
     * @return
     */
    List<VoiceAccessTypeResp> getVoiceAccessType();
}
