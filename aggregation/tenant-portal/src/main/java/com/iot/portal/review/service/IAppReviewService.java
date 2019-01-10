package com.iot.portal.review.service;

import com.iot.portal.review.vo.ReviewRecordResp;

import java.util.List;

/**
 * 项目名称：cloud
 * 功能描述：app审核记录
 * 创建人： yeshiyuan
 * 创建时间：2018/11/6 10:09
 * 修改人： yeshiyuan
 * 修改时间：2018/11/6 10:09
 * 修改描述：
 */
public interface IAppReviewService {
    /**
      * @despriction：查询app审核记录
      * @author  yeshiyuan
      * @created 2018/11/6 10:11
      * @return
      */
    List<ReviewRecordResp> getReviewList(Long appId);

}
