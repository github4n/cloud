package com.iot.portal.review.service.impl;

import com.iot.common.beans.BeanUtil;
import com.iot.common.util.CommonUtil;
import com.iot.portal.review.service.IAppReviewService;
import com.iot.portal.review.vo.ReviewRecordResp;
import com.iot.tenant.api.AppReviewApi;
import com.iot.tenant.vo.resp.review.AppReviewRecordResp;
import com.iot.user.api.UserApi;
import com.iot.user.vo.FetchUserResp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 项目名称：cloud
 * 功能描述：
 * 创建人： yeshiyuan
 * 创建时间：2018/11/6 10:10
 * 修改人： yeshiyuan
 * 修改时间：2018/11/6 10:10
 * 修改描述：
 */
@Service
public class AppReviewServiceImpl implements IAppReviewService{

    @Autowired
    private AppReviewApi appReviewApi;

    @Autowired
    private UserApi userApi;

    @Override
    public List<ReviewRecordResp> getReviewList(Long appId) {
        List<ReviewRecordResp> temp = new ArrayList<>();
        /**查询审核记录*/
        List<AppReviewRecordResp> list = this.appReviewApi.getAppReviewRecord(appId);
        if(!CommonUtil.isEmpty(list)){
            Set<Long> userIds = list.stream().map(AppReviewRecordResp::getCreateBy).collect(Collectors.toSet());
            /**查询操作员信息*/
            Map<Long,FetchUserResp> userMap = userApi.getByUserIds(new ArrayList<>(userIds));
            list.forEach(o->{
                ReviewRecordResp t = new ReviewRecordResp();
                BeanUtil.copyProperties(o, t);
                if(userMap.containsKey(o.getCreateBy())){
                    t.setOperateUserName(userMap.get(o.getCreateBy()).getUserName());
                }
                temp.add(t);
            });
        }
        return temp;
    }


}
